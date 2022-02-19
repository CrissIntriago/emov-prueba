/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.activos.service.ResponsableAdquisicionService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.entities.ResponsableAdquisicion;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "GestionAdquisicionesBeans")
@ViewScoped
public class GestionAdquisicionesBeansProceso extends BpmnBaseRoot implements Serializable {

    /*Inject Services*/
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private detalleBancoServices detalleBancoService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ResponsableAdquisicionService responsableAdquisicionService;
    @Inject
    private DatosGeneralesEntidadService provinciaCantonService;
    @Inject
    private CuentaContableService cuentaService;
    @Inject
    private FileUploadDoc uploadDoc;
        @Inject
    private CatalogoService catalogoService;

    /*Listas*/
    private List<CatalogoItem> tiposProcesos;
    private List<CatalogoItem> tiposAdquisicion;
    private List<CatalogoItem> subTiposAdquisicion;
    private List<DetalleBanco> detalleBancoList;
    private List<CatalogoItem> clasificaciones;
    private List<CatalogoItem> tipos;
    private List<ResponsableAdquisicion> responsableList;
    private List<Servidor> servidorList;
    private List<CuentaContable> cuentaAnticipoList;
    private List<CuentaContable> cuentaCostoGastolist;
    private UploadedFile file;
    private List<UploadedFile> files;

    /*Objectos*/
    private Adquisiciones adquisiciones;
    private OpcionBusqueda opcionBusqueda;
    private Banco banco;
    private DetalleBanco detalleBanco;
    private Proveedor proveedor;
    private Cliente contacto;
    private Cliente clienteProveedor;
    private UploadedFile uploadFile;
    private Provincia provinciaProveedor;
    private Provincia provinciaContacto;


    /*Lazy Model*/
    private LazyModel<Adquisiciones> adquisicionesLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Banco> bancoLazyModel;
    private List<Canton> cantonProveedor;
    private List<Canton> cantonContacto;
    private List<Provincia> provincias;
    private List<CatalogoItem> formasPagoList;

    /*Variables Auxiliares*/
    private Boolean habilitarContribuyente;
    private Boolean datosCargados;
    private Boolean añadirCliente;
    private Boolean habilitarContacto;
    private String fileName;
    private Boolean habilitarResponsable;
    private Boolean formAdquisicion;
    private Boolean renderedAdquisicion;
    private TareasActivas tareasActivas;

    /*Contructor inicializado*/
    @PostConstruct
    public void initialize() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
        }
        this.opcionBusqueda = new OpcionBusqueda();
        this.adquisiciones = new Adquisiciones();
        this.formasPagoList = catalogoService.getItemsByCatalogo("formasPagoContratos");
        this.tiposAdquisicion = adquisicionesService.getTipos("tipo_adquisicion");
        this.subTiposAdquisicion = adquisicionesService.getTipos("sub_tipo_adquisicion");
        this.adquisicionesLazyModel = new LazyModel<>(Adquisiciones.class);
        this.adquisicionesLazyModel.getSorteds().put("idProceso", "ASC");
        this.adquisicionesLazyModel.getFilterss().put("estado", true);
        this.servidorList = servidorService.getListadoServidores();
        this.cuentaAnticipoList = cuentaService.cuentaAnticipo(opcionBusqueda.getAnio());
        this.cuentaCostoGastolist = cuentaService.cuentaDebito(opcionBusqueda.getAnio());
        this.formAdquisicion = Boolean.TRUE;
        if (this.session.getVarTemp() instanceof TareasActivas) {
            tareasActivas = (TareasActivas) this.session.getVarTemp();
            this.adquisicionesLazyModel.getFilterss().put("numTramite", tareasActivas.getNumTramite());
        }
        vaciarFormProveedor();
    }

    //<editor-fold defaultstate="collapsed" desc="Metodos para el mantenimiento de la adquisición">
    /*Carga el formulario de adquisición*/
    public void form(Adquisiciones adquisiciones) {
        this.formAdquisicion = Boolean.FALSE;
        if (adquisiciones != null) {
            /*Carga los datos al formulario para editarlo*/
            this.adquisiciones = adquisiciones;
            List<ResponsableAdquisicion> auxiliarList = responsableAdquisicionService.getListaDeResponsablesActivo(adquisiciones);
            if (!auxiliarList.isEmpty()) {
                for (ResponsableAdquisicion responsable : auxiliarList) {
                    if (responsable.getEstado()) {
                        habilitarResponsable = Boolean.FALSE;
                    }
                    responsableList.add(responsable);
                }
            }
            actualizarTiposProcesos();
        } else {
            /*Carga nuevo formulario vacio*/
            this.adquisiciones = new Adquisiciones();
            this.adquisiciones.setNumTramite(tareasActivas.getNumTramite());
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    /*Actualiza los tipos de proceso*/
    public void actualizarTiposProcesos() {
        this.tiposProcesos = adquisicionesService.getTiposProcesos(adquisiciones.getTipoAdquisicion());
        if (adquisiciones.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_otros")) {
            renderedAdquisicion = Boolean.FALSE;
        } else {
            renderedAdquisicion = Boolean.TRUE;
        }
        PrimeFaces.current().ajax().update("gridDetalle");
    }

    /*Guardar al crear uno nuevo o al editarlo*/
    public void save() {
        /*Validacion de los servidores*/
        int contador = 0;
        for (ResponsableAdquisicion responsable : responsableList) {
            if (responsable.getResponsable() == null) {
                JsfUtil.addErrorMessage("ERROR!!", "Revisar la información del responsable Activo");
                return;
            }
            if (responsable.getEstado() && responsable.getFechaAsignacion() == null) {
                JsfUtil.addErrorMessage("ERROR!!", "Debe añadirle una fecha de asignación al responsable activo");
                return;
            }
            if (responsable.getEstado()) {
                contador += 1;
            }
        }
        if (contador == 0) {
            JsfUtil.addErrorMessage("ERROR!!", "Debe haber al menos un responsable activo");
            return;
        }
        boolean edit = adquisiciones.getId() != null;
        adquisiciones.setDescripcion(adquisiciones.getDescripcion().toUpperCase());
        if (edit) {
            adquisiciones.setUsuarioModificacion(userSession.getNameUser());
            adquisiciones.setFechaModificacion(new Date());
            adquisicionesService.edit(adquisiciones);
            for (ResponsableAdquisicion responsable : responsableList) {
                if (responsable.getId() != null) {
                    if (responsable.getFechaFinalizacion() != null) {
                        if (responsable.getFechaFinalizacion().before(new Date())) {
                            responsable.setEstado(Boolean.FALSE);
                        }
                    } else {
                        responsable.setEstado(Boolean.TRUE);
                    }
                    responsableAdquisicionService.edit(responsable);
                } else {
                    responsable.setAdquisicion(adquisiciones);
                    if (responsable.getFechaFinalizacion() != null) {
                        if (responsable.getFechaFinalizacion().before(new Date())) {
                            responsable.setEstado(Boolean.FALSE);
                        }
                    } else {
                        responsable.setEstado(Boolean.TRUE);
                    }
                    responsableAdquisicionService.create(responsable);
                }
            }
        } else {
            if (responsableList.isEmpty()) {
                JsfUtil.addWarningMessage("Aviso", "Necesita asignar minimo un responsable antes de guardar");
                return;
            } else {
                if (adquisiciones.getProveedor() != null) {
                    adquisiciones.setUsuarioCreacion(userSession.getNameUser());
                    adquisiciones.setFechaCreacion(new Date());
                    if (!adquisiciones.getTipoAdquisicion().getCodigo().equals("tipo_adquisicion_bienes")) {
                        adquisiciones.setSubTipoAdquisicion(null);
                    }
                    adquisiciones.setEstadoproceso(Boolean.TRUE);
                    adquisiciones = adquisicionesService.create(adquisiciones);
                    for (ResponsableAdquisicion responsable : responsableList) {
                        responsable.setAdquisicion(adquisiciones);
                        if (responsable.getFechaFinalizacion() != null) {
                            if (responsable.getFechaFinalizacion().before(new Date())) {
                                responsable.setEstado(Boolean.FALSE);
                            }
                        } else {
                            responsable.setEstado(Boolean.TRUE);
                        }
                        responsableAdquisicionService.create(responsable);
                    }
                } else {
                    JsfUtil.addWarningMessage("Aviso", "Necesita asignar un proveedor antes de guardar");
                    return;
                }
            }
        }
        if (adquisiciones.isGarantia() == true) {
            JsfUtil.addWarningMessage("Aviso", "La adquisición registrada aplica garantías");
        }
        if (files != null) {
            uploadDoc.upload(this.adquisiciones, files);
            JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
        }
        restablercerValores();
        JsfUtil.addSuccessMessage("Adquisición", "Ha sido " + (edit ? " editado" : " registrado") + " con éxito.");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void restablercerValores() {
        responsableList = new ArrayList<>();
        adquisiciones = new Adquisiciones();
        clienteProveedor = new Cliente();
        contacto = new Cliente();
        proveedor = new Proveedor();
        formAdquisicion = Boolean.TRUE;
        habilitarResponsable = Boolean.TRUE;
    }

    /*Eliminar una adquisición*/
    public void delete(Adquisiciones adquisiciones) {
        Boolean condicion = adquisicionesService.findReserva(adquisiciones);
        if (condicion) {
            JsfUtil.addErrorMessage("ERROR!!", adquisiciones.getIdProceso() + " no puede eliminarse, porque una reserva de compromiso lo tiene asociado");
        } else {
            adquisiciones.setEstado(Boolean.FALSE);
            adquisicionesService.edit(adquisiciones);
            JsfUtil.addSuccessMessage("INFO!!", adquisiciones.getIdProceso() + " eliminada con éxito");
            PrimeFaces.current().ajax().update("adquisionesTable");
        }
    }

    /*Genera y retorna el codigo de las siglas*/
    //<editor-fold defaultstate="collapsed" desc="Siglas">
    /*Presenta las sigas generadas*/
    public void actualizarSiglas() {
        adquisiciones.setIdProceso(generarSiglas());
    }

    private String generarSiglas() {
        String siglas = "";
        switch (adquisiciones.getTipoProceso().getCodigo()) {
            case "tipo_proceso_catalogo":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "CE-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "CE-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_subasta":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "SIE-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "SIE-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_infima":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "OC-IC-";
                        break;
                    case "tipo_adquisicion_obras":
                        siglas = "OC-IC-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "OC-IC-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_menor":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "MCB-";
                        break;
                    case "tipo_adquisicion_obras":
                        siglas = "MCO-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "MCS-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_cotizacion":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "COTB-";
                        break;
                    case "tipo_adquisicion_obras":
                        siglas = "COTO-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "COTS-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_licitacion":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "LICB-";
                        break;
                    case "tipo_adquisicion_obras":
                        siglas = "LICO-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_precio":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_bienes":
                        siglas = "PF-";
                        break;
                    case "tipo_adquisicion_obras":
                        siglas = "PF-";
                        break;
                    case "tipo_adquisicion_servicios":
                        siglas = "PF-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_lista":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_consultoria":
                        siglas = "LCC-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_concurso":
                switch (adquisiciones.getTipoAdquisicion().getCodigo()) {
                    case "tipo_adquisicion_consultoria":
                        siglas = "CPC-";
                        break;
                    default:
                        siglas = "";
                        break;
                }
                break;
            case "tipo_proceso_regimen":
                siglas = "RE-";
                break;

        }
        return siglas;
    }
//</editor-fold>

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Registro de proveedor">
    public void formProveedor() {
        this.proveedorLazyModel = new LazyModel<>(Proveedor.class);
        this.proveedorLazyModel.getSorteds().put("id", "ASC");
        this.proveedorLazyModel.getFilterss().put("estado", true);
        PrimeFaces.current().executeScript("PF('proveedorDlg').show()");
        PrimeFaces.current().ajax().update("proveedorTable");
    }

    public void formNuevoProveedor() {
        PrimeFaces.current().executeScript("PF('proveedorDlg').hide()");
        vaciarFormProveedor();
        PrimeFaces.current().executeScript("PF('proveedorNuevoDlg').show()");
    }

    public void añadirProveedor(Proveedor proveedor) {
        adquisiciones.setProveedor(proveedor);
        PrimeFaces.current().executeScript("PF('proveedorDlg').hide()");
        JsfUtil.addInformationMessage("Proveedor", adquisiciones.getProveedor().getCliente().getNombre() + " ha sido selecionado con éxito");
    }

    public void openDlgBanco() {
        this.bancoLazyModel = new LazyModel<>(Banco.class);
        this.bancoLazyModel.getSorteds().put("id", "ASC");
        this.bancoLazyModel.getFilterss().put("estado", true);
        PrimeFaces.current().executeScript("PF('BancosDlg').show()");
        PrimeFaces.current().ajax().update(":formBanco");
    }

    public void añadirBanco(Banco banco) {
        DetalleBanco nuevoDetalle = new DetalleBanco();
        nuevoDetalle.setBanco(banco);
        detalleBancoList.add(nuevoDetalle);
        PrimeFaces.current().executeScript("PF('BancosDlg').hide()");
        JsfUtil.addInformationMessage("Entidad Bancaria", "Se ha añadido correctamente");
    }

    public void removerCuentaBancaria(DetalleBanco detalleBanco) {
        detalleBancoList.remove(detalleBanco);
        JsfUtil.addInformationMessage("Entidad Bancaria", "ha sido removido correctamente");
    }

    public void actualizarTipo() {
        String opcion = clienteProveedor.getClasificacionProv().getTexto();
        if (opcion.equals("JURIDICA")) {
            habilitarContribuyente = Boolean.TRUE;
        } else {
            habilitarContribuyente = Boolean.FALSE;
        }
        proveedor.setRepresentanteLegal(habilitarContribuyente);
        tipos = catalogoItemService.findCatalogotipoLike("tipos_personeria_juridica_natural", opcion);
    }

    public void actualizarContribuyente() {
        if (clienteProveedor.getTipoProv().getTexto().equals("CONTRIBUYENTE ESPECIAL")) {
            habilitarContribuyente = Boolean.TRUE;
        } else {
            habilitarContribuyente = Boolean.FALSE;
        }
        clienteProveedor.setContribuyenteEspecial(habilitarContribuyente);
        PrimeFaces.current().ajax().update("gridContacto");
    }

    public void actualizarCantones(String tipoRegistro) {
        switch (tipoRegistro) {
            case "PROVEEDOR":
                this.cantonProveedor = provinciaCantonService.getCantones(provinciaProveedor);
                break;
            case "CONTACTO":
                this.cantonContacto = provinciaCantonService.getCantones(provinciaContacto);
                break;
            default:
                break;
        }
    }

    public void guardarAñadirProveedor() {
        if (proveedor.getId() == null) {
            if (proveedorService.findProveedorRegistrado(clienteProveedor)) {
                JsfUtil.addErrorMessage("PROVEEDOR", "Los datos de proveedor ya estan registrados");
                return;
            }
        }
        /*Validaciones datos proveedor*/
        if (clienteProveedor.getEmail() != null) {
            if (!Utils.validarEmailConExpresion(clienteProveedor.getEmail())) {
                JsfUtil.addWarningMessage("PROVEEDOR", "El correo ingresado es incorrecto");
                return;
            }
        }
        if (habilitarContribuyente) {
            if (contacto.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!", "Se debe ingresar datos de un contacto");
                return;
            }
        }
        /*Validaciones datos del contacto*/
        if (contacto.getIdentificacion() != null && contacto.getEmail() != null && !contacto.getEmail().equals("")) {
            if (!Utils.validarEmailConExpresion(contacto.getEmail())) {
                JsfUtil.addWarningMessage("CONTACTO", "El correo ingresado es incorrecto");
                return;
            }
        }
        /*Validacion de los detalles de las cuenta bancarias*/
        if (!detalleBancoList.isEmpty()) {
            int contador = 0;
            for (DetalleBanco banco : detalleBancoList) {
                if (banco.getEstadoCuenta() == true) {
                    contador = contador + 1;
                }
            }
            if (contador > 1 || contador == 0) {
                JsfUtil.addWarningMessage("CUENTA BANCARIA", "Debe haber una sola cuenta bancaria activa");
                PrimeFaces.current().ajax().update("entidadBancariaTable");
                return;
            }
        }
        if (clienteProveedor.getIdentificacion() != null) {
            proveedor.setCliente(guardarEditarCliente(clienteProveedor));
        }
        if (contacto.getIdentificacion() != null && !contacto.getIdentificacion().equals("")) {
            proveedor.setContacto(guardarEditarCliente(contacto));
        }
        proveedor.setUsuarioCreacion(userSession.getNameUser());
        proveedor.setFechaCreacion(new Date());
        proveedor = proveedorService.create(proveedor);
        /*GENERAMOS EL DETALLE DE LA CUANTA BANCARIA*/
        for (DetalleBanco bancoDetalle : detalleBancoList) {
            bancoDetalle.setProveedor(proveedor);
            detalleBancoService.create(bancoDetalle);
        }
        adquisiciones.setProveedor(proveedor);
        PrimeFaces.current().executeScript("PF('proveedorNuevoDlg').hide()");
        PrimeFaces.current().ajax().update("fieldsetProveedor");
        JsfUtil.addInformationMessage("Proveedor", adquisiciones.getProveedor().getCliente().getNombre() + " ha sido añadido con éxito");
        vaciarFormProveedor();
    }

    public Cliente guardarEditarCliente(Cliente cliente) {
        if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            cliente.setIdentificacion(cliente.getIdentificacion().substring(0, 10));
        }
        if (cliente.getId() != null) {
            cliente.setUsuarioModificacion(userSession.getNameUser());
            cliente.setFechaModificacion(new Date());
            clienteService.edit(cliente);
        } else {
            cliente.setUsuarioCreacion(userSession.getNameUser());
            cliente.setFechaCreacion(new Date());
            cliente = clienteService.create(cliente);
        }
        return cliente;
    }

    public void buscarCliente(Boolean tipoID) {
        if (tipoID) {
            if (clienteProveedor.getIdentificacion() == null) {
                JsfUtil.addErrorMessage("Identificacion", "Debe ingresar el número identificacion");
                return;
            }
            if (clienteProveedor.getIdentificacion().length() < 13) {
                JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar la cantidad de dígitos correctos para la identificación del proveedor");
                return;
            }
            if (!Utils.validateCCRuc(clienteProveedor.getIdentificacion())) {
                JsfUtil.addWarningMessage("AVISO!!", "La identificacion del proveedor es incorrecta");
                return;
            }
            try {
                Cliente c = clienteService.buscarCliente(clienteProveedor, tipoID);
                if (c.getIdentificacion() != null || c.getId() != null) {
                    clienteProveedor = new Cliente();
                    clienteProveedor = c;
                    clienteProveedor.setIdentificacion(clienteProveedor.getIdentificacion().concat(clienteProveedor.getRuc()));
                    this.datosCargados = Boolean.TRUE;
                    if (clienteProveedor.getClasificacionProv() != null) {
                        actualizarTipo();
                        actualizarContribuyente();
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Busqueda de cliente", e);
            }
        } else {
            if (contacto.getIdentificacion() == null) {
                JsfUtil.addErrorMessage("Identificacion", "Debe ingresar el número identificacion");
                return;
            }
            if (contacto.getIdentificacion().length() < 10) {
                JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar la cantidad de dígitos correctos para la identificación del contacto");
                return;
            }
            if (!Utils.validateCCRuc(contacto.getIdentificacion())) {
                JsfUtil.addWarningMessage("AVISO!!", "La identificacion del contacto es incorrecta");
                return;
            }
            try {
                Cliente c = clienteService.buscarCliente(contacto, tipoID);
                if (c.getIdentificacion() != null || c.getId() != null) {
                    contacto = new Cliente();
                    contacto = c;
                    this.habilitarContacto = Boolean.TRUE;
                    if (contacto.getCanton() != null) {
                        provinciaContacto = contacto.getCanton().getIdProvincia();
                        actualizarCantones("CONTACTO");
                    }
                    if (provinciaContacto.getId() == null && contacto.getDireccion() != null) {
                        String[] split = contacto.getDireccion().split("/");
                        provinciaContacto = proveedorService.findByNamedQuery1("Provincia.findByProvincia", split[0].replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "_"));
                        actualizarCantones("CONTACTO");
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Busqueda de cliente", e);
            }
        }
    }

//</editor-fold>
    public void cancelar() {
        adquisiciones = new Adquisiciones();
        formAdquisicion = Boolean.TRUE;
        this.formAdquisicion = Boolean.TRUE;
        vaciarFormProveedor();
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("adquisionesTable");
    }

    private void vaciarFormProveedor() {
        files = new ArrayList<>();
        this.provincias = provinciaCantonService.getProvincias();
        this.provinciaContacto = new Provincia();
        this.provinciaProveedor = new Provincia();
        this.tiposProcesos = new ArrayList<>();
        this.banco = new Banco();
        this.detalleBanco = new DetalleBanco();
        this.proveedor = new Proveedor();
        this.clienteProveedor = new Cliente();
        this.contacto = new Cliente();
        this.detalleBancoList = new ArrayList<>();
        this.tipos = new ArrayList<>();
        this.responsableList = new ArrayList<>();
        this.habilitarContribuyente = Boolean.FALSE;
        this.datosCargados = Boolean.FALSE;
        this.habilitarContacto = Boolean.FALSE;
        this.habilitarResponsable = Boolean.TRUE;
        this.renderedAdquisicion = Boolean.TRUE;
        this.clasificaciones = catalogoItemService.findCatalogoClasificacion1("personeria_juridica");
        definirTipoIdentificacion();
    }

    private void definirTipoIdentificacion() {
        this.contacto.setTipoIdentificacion(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("C", "tipo_identificacion_beneficiario"));
        this.clienteProveedor.setTipoIdentificacion(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("RUC", "tipo_identificacion_beneficiario"));
    }

    public void añadirCelda() {
        habilitarResponsable = Boolean.FALSE;
        responsableList.add(new ResponsableAdquisicion());
        PrimeFaces.current().ajax().update("servidorResponsableTable");
        PrimeFaces.current().ajax().update("fieldsetResponsable");
    }

    public void onDateSelect(ResponsableAdquisicion responsableSeleccionado) {
        if (responsableSeleccionado.getFechaFinalizacion().before(new Date())) {
            responsableSeleccionado.setEstado(Boolean.FALSE);
            habilitarResponsable = Boolean.TRUE;
        }
        PrimeFaces.current().ajax().update("fieldsetResponsable");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        this.uploadFile = event.getFile();
        byte[] contents = uploadFile.getContents();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String filePath = ec.getRealPath(String.format("/resources/demo/media/%s", uploadFile.getFileName()));
        try {
            //ruta_auxiliar = ruta_temporal + fileName.replace("", "");
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(contents);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
        PrimeFaces.current().ajax().update("requisitoDialogForm");
        FacesMessage message = new FacesMessage("Archivo", uploadFile.getFileName() + " ha sido cargado con éxito al sistema");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void controlFecha(ResponsableAdquisicion responsableSeleccionado) {
        if (!responsableList.isEmpty()) {
            if (responsableList.size() > 1) {
                Date auxiliar = responsableList.get(responsableList.size() - 2).getFechaFinalizacion();
                if (!responsableSeleccionado.getFechaAsignacion().after(auxiliar) && !responsableSeleccionado.getFechaAsignacion().equals(auxiliar)) {
                    responsableSeleccionado.setFechaAsignacion(null);
                    JsfUtil.addErrorMessage("AVISO!!", "La fecha de asignacion asignada debe ser despues o igual que la fecha de finalicacion del ultimo responsable");
                    return;
                }
            }
        }
    }

    public void verDocumento(Adquisiciones adquisiciones) {
        this.adquisiciones = adquisiciones;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
    }

    public void subirArchivo(FileUploadEvent event) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(event.getFile());
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }

    }

    public void completarTarea() {
        try {
            getParamts().put("aprobado", adquisiciones.isGarantia());
            if (adquisiciones.isGarantia()) {
                session.setVarTemp(adquisiciones);
                getParamts().put("tesoreria", clienteService.getrolsUser(RolUsuario.tesorero));
            } else {
                getParamts().put("contabilidadR", clienteService.getrolsUser(RolUsuario.contador));
            }
            getParamts().put("idServidor", session.getUserId());
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception ex) {
            Logger.getLogger(RevisionRecepcionTramiteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updialogObservacion(Adquisiciones a) {
        adquisiciones = a;
        observacion.setEstado(Boolean.TRUE);
        observacion.setFecCre(new Date());
        observacion.setUserCre(this.session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    //<editor-fold defaultstate="collapsed" desc="Get -n- Set">
    public List<CatalogoItem> getFormasPagoList() {
        return formasPagoList;
    }

    public void setFormasPagoList(List<CatalogoItem> formasPagoList) {
        this.formasPagoList = formasPagoList;
    }

    public Boolean getRenderedAdquisicion() {
        return renderedAdquisicion;
    }

    public void setRenderedAdquisicion(Boolean renderedAdquisicion) {
        this.renderedAdquisicion = renderedAdquisicion;
    }

    public List<CatalogoItem> getTiposProcesos() {
        return tiposProcesos;
    }

    public void setTiposProcesos(List<CatalogoItem> tiposProcesos) {
        this.tiposProcesos = tiposProcesos;
    }

    public List<CatalogoItem> getTiposAdquisicion() {
        return tiposAdquisicion;
    }

    public void setTiposAdquisicion(List<CatalogoItem> tiposAdquisicion) {
        this.tiposAdquisicion = tiposAdquisicion;
    }

    public List<CatalogoItem> getSubTiposAdquisicion() {
        return subTiposAdquisicion;
    }

    public void setSubTiposAdquisicion(List<CatalogoItem> subTiposAdquisicion) {
        this.subTiposAdquisicion = subTiposAdquisicion;
    }

    public Adquisiciones getAdquisiciones() {
        return adquisiciones;
    }

    public void setAdquisiciones(Adquisiciones adquisiciones) {
        this.adquisiciones = adquisiciones;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<Adquisiciones> getAdquisicionesLazyModel() {
        return adquisicionesLazyModel;
    }

    public void setAdquisicionesLazyModel(LazyModel<Adquisiciones> adquisicionesLazyModel) {
        this.adquisicionesLazyModel = adquisicionesLazyModel;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public List<DetalleBanco> getDetalleBancoList() {
        return detalleBancoList;
    }

    public void setDetalleBancoList(List<DetalleBanco> detalleBancoList) {
        this.detalleBancoList = detalleBancoList;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public DetalleBanco getDetalleBanco() {
        return detalleBanco;
    }

    public void setDetalleBanco(DetalleBanco detalleBanco) {
        this.detalleBanco = detalleBanco;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LazyModel<Banco> getBancoLazyModel() {
        return bancoLazyModel;
    }

    public void setBancoLazyModel(LazyModel<Banco> bancoLazyModel) {
        this.bancoLazyModel = bancoLazyModel;
    }

    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public List<CatalogoItem> getTipos() {
        return tipos;
    }

    public void setTipos(List<CatalogoItem> tipos) {
        this.tipos = tipos;
    }

    public Cliente getClienteProveedor() {
        return clienteProveedor;
    }

    public void setClienteProveedor(Cliente clienteProveedor) {
        this.clienteProveedor = clienteProveedor;
    }

    public Boolean getHabilitarContribuyente() {
        return habilitarContribuyente;
    }

    public void setHabilitarContribuyente(Boolean habilitarContribuyente) {
        this.habilitarContribuyente = habilitarContribuyente;
    }

    public Boolean getDatosCargados() {
        return datosCargados;
    }

    public void setDatosCargados(Boolean datosCargados) {
        this.datosCargados = datosCargados;
    }

    public Cliente getContacto() {
        return contacto;
    }

    public void setContacto(Cliente contacto) {
        this.contacto = contacto;
    }

    public Boolean getAñadirCliente() {
        return añadirCliente;
    }

    public void setAñadirCliente(Boolean añadirCliente) {
        this.añadirCliente = añadirCliente;
    }

    public Boolean getHabilitarContacto() {
        return habilitarContacto;
    }

    public void setHabilitarContacto(Boolean habilitarContacto) {
        this.habilitarContacto = habilitarContacto;
    }

    public List<ResponsableAdquisicion> getResponsableList() {
        return responsableList;
    }

    public void setResponsableList(List<ResponsableAdquisicion> responsableList) {
        this.responsableList = responsableList;
    }

    public List<Servidor> getServidorList() {
        return servidorList;
    }

    public void setServidorList(List<Servidor> servidorList) {
        this.servidorList = servidorList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getHabilitarResponsable() {
        return habilitarResponsable;
    }

    public void setHabilitarResponsable(Boolean habilitarResponsable) {
        this.habilitarResponsable = habilitarResponsable;
    }

    public Provincia getProvinciaProveedor() {
        return provinciaProveedor;
    }

    public void setProvinciaProveedor(Provincia provinciaProveedor) {
        this.provinciaProveedor = provinciaProveedor;
    }

    public Provincia getProvinciaContacto() {
        return provinciaContacto;
    }

    public void setProvinciaContacto(Provincia provinciaContacto) {
        this.provinciaContacto = provinciaContacto;
    }

    public List<Canton> getCantonProveedor() {
        return cantonProveedor;
    }

    public void setCantonProveedor(List<Canton> cantonProveedor) {
        this.cantonProveedor = cantonProveedor;
    }

    public List<Canton> getCantonContacto() {
        return cantonContacto;
    }

    public void setCantonContacto(List<Canton> cantonContacto) {
        this.cantonContacto = cantonContacto;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<CuentaContable> getCuentaAnticipoList() {
        return cuentaAnticipoList;
    }

    public void setCuentaAnticipoList(List<CuentaContable> cuentaAnticipoList) {
        this.cuentaAnticipoList = cuentaAnticipoList;
    }

    public List<CuentaContable> getCuentaCostoGastolist() {
        return cuentaCostoGastolist;
    }

    public void setCuentaCostoGastolist(List<CuentaContable> cuentaCostoGastolist) {
        this.cuentaCostoGastolist = cuentaCostoGastolist;
    }

    public Boolean getFormAdquisicion() {
        return formAdquisicion;
    }

    public void setFormAdquisicion(Boolean formAdquisicion) {
        this.formAdquisicion = formAdquisicion;
    }
//</editor-fold>

}
