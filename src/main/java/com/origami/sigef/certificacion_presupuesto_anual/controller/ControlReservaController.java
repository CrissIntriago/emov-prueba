/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.ReservaCompromisoDetalleLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.SoliciudReservaCompromisoLazy;
import com.origami.sigef.certificacion_presupuesto_anual.service.ContratoReservaService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "ControlReservaView")
@ViewScoped
public class ControlReservaController implements Serializable {

    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ProductoService productoService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ContratoReservaService contratoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private UserSession user;
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;
    @Inject
    private ServletSession ss;

    private LazyModel<SolicitudReservaCompromiso> lazyControl;
    private SolicitudReservaCompromiso reservaCompromiso;
    private List<Producto> listaProductos;
    private List<UnidadAdministrativa> listaUnidades;
    private List<Producto> listadeProductoSeleccionados;
    private List<DetalleSolicitudCompromiso> ListadetalleSolicitud;
    private DetalleSolicitudCompromiso detalleSolicitud;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas;
    private Producto producto;
    private boolean mostrarOculutarTabla;
    private List<DetalleSolicitudCompromiso> listaTotal;
    private String cbTipo;
    private boolean ocultarcb;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private boolean tabla1;
    private boolean tabla2;
    private boolean tabla3;
    private List<DetalleSolicitudCompromiso> listaGuardar;
    private List<Presupuesto> listaPresupuesto;
    private List<DetalleTransaccion> detalleAcumulado;

    private ReservaCompromisoDetalleLazy lazy;
    private SoliciudReservaCompromisoLazy lazyPrincipall;
    private List<DetalleSolicitudCompromiso> visualizacionSolicitud;
    private LazyModel<SolicitudRequisito> solicitudRequisitoLazyModel;
    private boolean panel1, panel2, ocultarbutton, mostrarButton;
    private CatalogoItem catalogoItem;
    private Cliente beneficiario;
    private ProveedorLazy proveedorLazy;
    private ServidorLazy servidorLazy;
    private Distributivo cargoUnidadUser;
    private BigDecimal valorAnteirorPresupuesto;
    private List<ContratosReservaEjecuion> listaAqusicionesGuardar;
    private List<Procedimiento> procedimientoList;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;
    private UploadedFile uploadFile;
    private SolicitudRequisito solicitudRequisito;
    private ProcedimientoRequisito procedimientoRequisito;

    private String fileName;

    private LazyModel<SolicitudReservaCompromiso> lazyReal;
    private List<DetalleSolicitudCompromiso> listaeditable1;
    private List<DetalleSolicitudCompromiso> listaeditable2;
    private double num1, num2;
    private double result1, result2;
    private boolean panelProveedor, panelServidor;

    private List<UnidadAdministrativa> listaFilroUnidad;
    private List<CatalogoItem> listaFiltroEstado;
    private List<Cliente> listaFiltroCliente;
    private List<Short> listaPeriodo;

    private Boolean tipoContrato;
    private Boolean tablaContrato;

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalEjecutado;
    private BigDecimal totalLiquidado;
    private BigDecimal totalLiberado;

    private Short periodo;
    private OpcionBusqueda opcionBusqueda;

    private List<UploadedFile> files;

    @Inject
    private FileUploadDoc uploadDoc;
    private String rutaDocumento;

    @PostConstruct
    public void inicializar() {

        this.listaPeriodo = reservaCompromisoService.listaAniosAprobados(Boolean.TRUE);

        fileName = "";
        reservaCompromiso = new SolicitudReservaCompromiso();
        this.listaUnidades = unidadService.findByNamedQuery("UnidadAdministrativa.TiposUnidades", "DIR", true);
        this.listaProductos = new ArrayList<>();
        this.listadeProductoSeleccionados = new ArrayList<>();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        ListadetalleSolicitud = new ArrayList<>();
        mostrarOculutarTabla = true;
        listaTotal = new ArrayList<>();
        ocultarcb = true;
        this.rutaDocumento = "";
        listaPartidasDirectas = new ArrayList<>();
        tabla1 = false;
        tabla2 = false;
        tabla3 = false;
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        this.listaPresupuesto = new ArrayList<>();
        this.lazy = new ReservaCompromisoDetalleLazy();
        this.beneficiario = new Cliente();
        visualizacionSolicitud = new ArrayList<>();
        panel1 = true;
        panel2 = false;
        ocultarbutton = false;
        mostrarButton = false;
        catalogoItem = new CatalogoItem();
        lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
        lazyReal.getFilterss().put("estado.codigo", Arrays.asList("R", "O", "APRO"));
        lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
//        if (userSession.hasRole("admin")) {
//            Distributivo usrLogeado = clienteService.getuusuarioLogeado(userSession.getNameUser());
//            if (usrLogeado != null) {
//                lazyReal.getFilterss().put("unidadRequiriente:equal", usrLogeado.getUnidadAdministrativa());
//            }
//        }
        procedimientoSeleccionado = new Procedimiento();
        solicitudRequisito = new SolicitudRequisito();
        procedimientoList = procedimientoService.getProcedimientos("RC");
        procedimientoRequisito = new ProcedimientoRequisito();
        solicitudRequisitoList = new ArrayList<>();
        solicitudRequisitoLazyModel = new LazyModel<>(SolicitudRequisito.class);
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        num1 = 0;
        num2 = 0;
        result1 = 0;
        result2 = 0;
        panelProveedor = true;
        panelServidor = false;
        cargoUnidadUser = new Distributivo();
        this.valorAnteirorPresupuesto = BigDecimal.ZERO;
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        procedimientoSeleccionado = new Procedimiento();
        servidorLazy = null;
        proveedorLazy = null;
        setCbTipo("");
        tipoContrato = Boolean.TRUE;
        tablaContrato = Boolean.FALSE;
        this.listaFilroUnidad = reservaCompromisoService.getListaUnidadesReserva();
        this.listaFiltroCliente = reservaCompromisoService.getListaBeneficiarioReserva();
        this.listaFiltroEstado = reservaCompromisoService.getListaEstadosReserva();
        this.listaAqusicionesGuardar = new ArrayList<>();
        detalleAcumulado = new ArrayList<>();
        opcionBusqueda = new OpcionBusqueda();
        if (listaPeriodo != null && !listaPeriodo.isEmpty()) {
            int indice = listaPeriodo.size();
            if (!listaPeriodo.isEmpty() && indice == 1) {
                periodo = listaPeriodo.get(0);
            } else {
                periodo = listaPeriodo.get(indice - 1);
            }
        } else {
            periodo = opcionBusqueda.getAnio();
        }
        cargarRegistros();
    }

    public void cargarRegistros() {
        if (periodo != null) {
            this.lazyControl = new LazyModel<>(SolicitudReservaCompromiso.class);
            this.lazyControl.getSorteds().put("secuencial", "ASC");
            this.lazyControl.getFilterss().put("periodo", this.periodo);
            this.lazyControl.getFilterss().put("antiguo", false);
        } else {
            this.lazyControl = null;
        }
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formGlobalReserva:dataSolciitudesReserva");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formGlobalReserva:dataSolciitudesReserva");
        }
    }

    public void consultarMostrarAndOcultarPaneles() {
        panel1 = true;
        panel2 = false;

    }

    public void registarrMostrarAndOcultarPaneles() {
        //reservaCompromiso = new SolicitudReservaCompromiso();
        panel1 = false;
        panel2 = true;
        ocultarbutton = false;
        mostrarButton = true;
        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
    }

    public void inicializarMaxcimoCodigo() throws ParseException {
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);

    }

    public void enviarSolicitud(SolicitudReservaCompromiso s) {
        this.reservaCompromiso = s;
        reservaCompromiso.setEstado(reservaCompromisoService.getestados("E", "estado_solicitud"));
        reservaCompromisoService.edit(reservaCompromiso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envio correctamente");
        reservaCompromiso = new SolicitudReservaCompromiso();

    }

    public void planAnualesActividades() {

        this.listaProductos = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), reservaCompromiso.getUnidadRequiriente());

        PrimeFaces.current().executeScript("PF('Dlogo2').show()");
        PrimeFaces.current().ajax().update(":formDlogo2");

    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();
        return a;
    }

    /**
     * Realiza la llamanda de los diaoogos segun su condicion que puede ser el
     * PAPP, Plan Distriubtivo o las partidas Directas.
     */
    public void accionComponenetes() throws ParseException {

        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 0) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        if (reservaCompromiso.getPeriodo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

        } else {

            tabla1 = true;
            tabla2 = false;
            tabla3 = false;
            cargoUnidadUser = clienteService.getuusuarioLogeado(userSession.getNameUser());
            reservaCompromiso.setUnidadRequiriente(cargoUnidadUser.getUnidadAdministrativa());

            Distributivo d = clienteService.getuusuarioLogeado(user.getNameUser());

            this.listaProductos = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), d.getUnidadAdministrativa());

            if (listaProductos.isEmpty()) {

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No existe datos");

            } else {

                tabla1 = true;
                tabla2 = false;
                tabla3 = false;

                this.listaProductos = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), reservaCompromiso.getUnidadRequiriente());

                if (listaProductos.isEmpty()) {

                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addWarningMessage("Advertencia", "No hay datos " + reservaCompromiso.getPeriodo());
                    return;
                }

                inicializarMaxcimoCodigo();
                PrimeFaces.current().executeScript("PF('Dlogo2').show()");
                PrimeFaces.current().ajax().update(":formDlogo2");
            }

            inicializarMaxcimoCodigo();

        }

    }

    public void accionComponentesDistriuvtioPartida() throws ParseException {

        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 1) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        cargoUnidadUser = clienteService.getuusuarioLogeado(userSession.getNameUser());
        reservaCompromiso.setUnidadRequiriente(cargoUnidadUser.getUnidadAdministrativa());

        if (reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            if (reservaCompromiso.getPeriodo() == null && reservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

            } else {
                inicializarMaxcimoCodigo();
                listaPresupuesto = reservaCompromisoService.getPresupuestoPartidasDandPD(reservaCompromiso.getPeriodo(), Arrays.asList("PD", "PDI"));

                PrimeFaces.current().executeScript("PF('Dlogo3').show()");
                PrimeFaces.current().ajax().update(":formDlogo3");
            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + reservaCompromiso.getPeriodo());
        }

    }

    public void tipoCbombo() {
        if (cbTipo.equals("PAPP")) {

        } else if (cbTipo.equals("PDI")) {
            ocultarcb = true;
            tabla1 = false;
            tabla2 = true;
            tabla3 = false;

            //  this.listaPartidasDirectas = reservaCompromisoService.GetPartidasDirectcas(reservaCompromiso.getPeriodo(), "PDI");
            PrimeFaces.current().executeScript("PF('Dlogo3').show()");
            PrimeFaces.current().ajax().update(":formDlogo3");
        }

    }

    public Boolean validarRequisitos() {
        boolean validador = true;
        Long cantidadNoRequeridas = procedimientoRequisitoService.getCantidadRequerida(procedimientoSeleccionado, false);
        Long cantidadRequeridas = procedimientoRequisitoService.getCantidadRequerida(procedimientoSeleccionado, true);
        if (cantidadNoRequeridas == procedimientoRequisitoList.size()) {
            return false;
        } else {
            if (solicitudRequisitoList.size() > 0) {
                //consulto la cantidad de archivos que SI son requeridos
                if (cantidadRequeridas <= solicitudRequisitoList.size()) {
                    Short contador = 0;
                    for (SolicitudRequisito solicitud : solicitudRequisitoList) {
                        if (solicitud.getIdProcedimientoRequisito().getObligatorio() == true) {
                            contador++;
                        }
                    }
                    if (contador < cantidadRequeridas) {
                        JsfUtil.addWarningMessage("REQUISITOS:", "FALTA SUBIR ARCHIVOS EN " + (cantidadRequeridas - contador) + " REQUISITO(S) OBLIGATORIO");
                        validador = true;
                    } else {
                        validador = false;
                    }
                } else {
                    JsfUtil.addWarningMessage("REQUISITOS:", "EXISTEN ARCHIVOS SIN SUBIR EN LOS REQUISITOS OBLIGATORIOS");
                    validador = true;
                }
                return validador;
            } else {
                JsfUtil.addWarningMessage("REQUISITOS:", "SUBIR ARCHIVOS EN LOS " + cantidadRequeridas + " REQUISITO(S) OBLIGATORIO");
                return validador;
            }
        }
    }

    public void eliminarSolicitudCompleta(SolicitudReservaCompromiso s) {
        reservaCompromisoService.remove(s);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solcitud eliminada");
    }

    public void savesolicitud() {

        if (procedimientoSeleccionado.getId() != null) {
            if (validarRequisitos()) {
                return;
            }
        } else {
            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }

        boolean verificar = true;
        boolean pararMetodo = true;
        listaGuardar = new ArrayList<>();

        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();
            //result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            }
            if (result1 > itemsuma.getActividadProducto().getMonto().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo par su reserva solicitada");
                break;
            }

        }

        if (!verificar) {

            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {

            num2 = 0;
            result2 = 0;
            num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL DISTRIBUIDO ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
            }

            if (result2 > itemverificar.getPresupuesto().getValorEgreso().doubleValue()) {
                pararMetodo = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }

        }

        if (!pararMetodo) {

            return;
        }

        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty()) {

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {
                    listaGuardar.add(l);
                });
            }

            if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                listaGlobalDetalleSolicitudPartidas.forEach((item) -> {
                    listaGuardar.add(item);
                });
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No hay datos Seleccionados");
            return;
        }

        int resultado = 0;
        boolean verificacion = true;
        reservaCompromiso.setFechaCreacion(new Date());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromiso.setUsuarioCreacion(userSession.getName());
        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setEstado(reservaCompromisoService.getestados("R", "estado_solicitud"));
        reservaCompromiso.setProcedimiento(procedimientoSeleccionado);
        reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);

        for (DetalleSolicitudCompromiso li : listaGuardar) {
            detalleSolicitud = new DetalleSolicitudCompromiso();

            detalleSolicitud.setSolicitud(reservaCompromiso);

            detalleSolicitud.setActividadProducto(li.getActividadProducto());
            detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
            detalleSolicitud.setEstado(true);
            detalleSolicitud.setPresupuesto(li.getPresupuesto());
            detalleSolicitud.setFechaCreacion(new Date());
            detalleSolicitud.setFechaModifcacion(new Date());
            detalleSolicitud.setUsuarioCreacion(userSession.getName());
            detalleSolicitud.setUsuarioModificacion(userSession.getName());
            detalleSolicitud.setPeriodo(li.getPeriodo());
            detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
            detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());

            detalleSolicitud = detalleReservaService.create(detalleSolicitud);

        }

        //Guardamos la los requisitos en relacion a la reserva de compromiso que se creo en la linea de codigo anterior
        for (SolicitudRequisito solicitud : solicitudRequisitoList) {
            solicitudRequisito = new SolicitudRequisito();
            solicitudRequisito.setIdSolicitudReserva(reservaCompromiso);
            solicitudRequisito.setIdProcedimientoRequisito(solicitud.getIdProcedimientoRequisito());
            solicitudRequisito.setUrl(solicitud.getUrl());
            solicitudRequisito = solicitudRequisitoService.create(solicitudRequisito);
        }

        if (verificacion) {

            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addSuccessMessage("SOLICITUD", "La solicitud " + reservaCompromiso.getSecuencial() + "-" + reservaCompromiso.getPeriodo() + " se ha realizado con éxito");
            listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
            listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
            listaGuardar = new ArrayList<>();
            solicitudRequisitoList = new ArrayList<>();
            procedimientoRequisitoList = new ArrayList<>();
            reservaCompromiso = new SolicitudReservaCompromiso();
            detalleSolicitud = new DetalleSolicitudCompromiso();
            procedimientoSeleccionado = new Procedimiento();
            servidorLazy = null;
            proveedorLazy = null;
            setCbTipo("");
            consultarMostrarAndOcultarPaneles();
        } else {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Los campos no pueder ser nulo o tener valor con ceros");

        }

        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        procedimientoSeleccionado = new Procedimiento();
        servidorLazy = null;
        proveedorLazy = null;
        setCbTipo("");

    }

    public void añadiendoFormato(Producto p) {
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        detalleSolicitud = new DetalleSolicitudCompromiso();
        producto = p;

        detalleSolicitud.setActividadProducto(producto);
        detalleSolicitud.setEstado(true);
        detalleSolicitud.setFechaCreacion(new Date());
        detalleSolicitud.setFechaModifcacion(new Date());
        detalleSolicitud.setUsuarioCreacion(userSession.getName());
        detalleSolicitud.setUsuarioModificacion(userSession.getName());
        detalleSolicitud.setPeriodo(reservaCompromiso.getPeriodo());

        BigDecimal resultado_valor_disponble = reservaCompromisoService.getSueldoDisponible(producto, reservaCompromiso.getPeriodo());

        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detalleSolicitud.setMontoDisponible(producto.getMonto());

        } else {

            maximo_resultado = (detalleSolicitud.getActividadProducto().getMonto()).subtract(resultado_valor_disponble);

            detalleSolicitud.setMontoDisponible(maximo_resultado);

        }
        listaGlobalDetalleSolicitudCompromisos.add(detalleSolicitud);
        JsfUtil.addInformationMessage("INFORMATION", "PRODUCTO SELECIONADO CORRECTAMENTE");
        producto = new Producto();
        detalleSolicitud = new DetalleSolicitudCompromiso();
    }

    public void eliminadoMemoriaDetalleSOlicitud(int posicion) {
        listaGlobalDetalleSolicitudCompromisos.remove(posicion);
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void abrirDlgo(DetalleSolicitudCompromiso d) {
        detalleSolicitud = d;

        PrimeFaces.current().executeScript("PF('Dlogo').show()");
        PrimeFaces.current().ajax().update(":formDlogo");
    }

    public void abrirDlgoPartidas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = d;
        valorAnteirorPresupuesto = d.getMontoSolicitado();
        PrimeFaces.current().executeScript("PF('DlEdicionPartida').show()");
        PrimeFaces.current().ajax().update(":formEdicionPartida");
    }

    public void solicitarReservas(DetalleSolicitudCompromiso d) {

        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "el monto no debe ser menor al monto comprometido");

            }

        }

        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "el monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void solicitarReservasPartidas() {

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "el monto no debe ser menor al monto comprometido");

            }

        }

        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue() || detalleSolicitud.getMontoSolicitado().doubleValue() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "el monto a solicitar es mayor al monto disponible o el valor ingresado no puede ser cero");
            detalleSolicitud.setMontoSolicitado(null);
            return;
        } else {
            this.listaGlobalDetalleSolicitudPartidas.add(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudPartidas.remove(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('DlEdicionPartida').hide()");
            PrimeFaces.current().ajax().update(":formEdicionPartida");
        }

    }

    public void eliminarPArtidasDirectasAndDistributivo(int posicion) {
        listaGlobalDetalleSolicitudPartidas.remove(posicion);
    }

    public void seleccionPresupuestoDPD(Presupuesto p) {

        detalleSolicitud = new DetalleSolicitudCompromiso();
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        detalleSolicitud.setPresupuesto(p);
        detalleSolicitud.setEstado(true);
        detalleSolicitud.setFechaCreacion(new Date());
        detalleSolicitud.setFechaModifcacion(new Date());
        detalleSolicitud.setUsuarioCreacion(userSession.getName());
        detalleSolicitud.setUsuarioModificacion(userSession.getName());
        detalleSolicitud.setPeriodo(reservaCompromiso.getPeriodo());

        BigDecimal resultado_valor_disponble = reservaCompromisoService.getSueldoDisponiblePresupuesto(p, reservaCompromiso.getPeriodo());

        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detalleSolicitud.setMontoDisponible(p.getValorEgreso());

        } else {

            maximo_resultado = detalleSolicitud.getPresupuesto().getValorEgreso().subtract(resultado_valor_disponble);

            detalleSolicitud.setMontoDisponible(maximo_resultado);

        }

        listaGlobalDetalleSolicitudPartidas.add(detalleSolicitud);
        detalleSolicitud = new DetalleSolicitudCompromiso();
        JsfUtil.addInformationMessage("INFORMATION", "PARTIDA SELECIONADO CORRECTAMENTE");

    }

    public void visualizarDetalleSolcitud(SolicitudReservaCompromiso s) {
        tipoContrato = Boolean.TRUE;
        tablaContrato = Boolean.FALSE;
        reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        this.procedimientoSeleccionado = s.getProcedimiento();
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }
        this.listaAqusicionesGuardar = contratoService.getListaContratos(s);

        if (listaAqusicionesGuardar.size() > 0) {
            tipoContrato = Boolean.FALSE;
            tablaContrato = Boolean.TRUE;
        }
        this.visualizacionSolicitud = reservaCompromisoService.getListaDetlleSolciitud(s);
        showEjecutadoReservasGlobal(s);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('DlgoVisualizacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacion");
    }

    public void showEjecutadoReservasGlobal(SolicitudReservaCompromiso r) {

    }

    public void detallePartidasGlobal() {

        BigDecimal sumandoOtros = BigDecimal.ZERO;
        BigDecimal totalEjecuato = BigDecimal.ZERO;
        visualizacionSolicitud = new ArrayList<>();
        visualizacionSolicitud.clear();
        List<DetalleSolicitudCompromiso> lista = reservaCompromisoService.getListaDetlleSolciitud(reservaCompromiso);
        for (DetalleSolicitudCompromiso i : lista) {
            this.visualizacionSolicitud.add(i);
        }
        for (DetalleSolicitudCompromiso item : visualizacionSolicitud) {
            sumandoOtros = BigDecimal.ZERO;
            for (DetalleTransaccion data : detalleAcumulado) {
                if (data.getTipoTransaccion() != null) {
                    if ("diario_general_devengado".equals(data.getTipoTransaccion().getCodigo())) {
                        if (data.getPartidaPresupuestaria() != null && data.getEstructuraProgramatica() != null) {
                            if (data.getIdDetalleReserva() != null) {
                                if (item != null) {
                                    if (data.getIdDetalleReserva().equals(item)) {
                                        if (item.getActividadProducto() == null) {
                                            if (item.getPresupuesto().getItem().equals(data.getPartidaPresupuestaria()) && item.getPresupuesto().getEstructura().equals(data.getEstructuraProgramatica())) {
                                                sumandoOtros = sumandoOtros.add(data.getDevengado());
                                            }
                                        } else {
                                            if (item.getActividadProducto().getItemPresupuestario().equals(data.getPartidaPresupuestaria()) && item.getActividadProducto().getEstructuraProgramatica().equals(data.getEstructuraProgramatica())) {
                                                sumandoOtros = sumandoOtros.add(data.getDevengado());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item.setEjecutado(sumandoOtros);
            totalEjecuato = totalEjecuato.add(item.getEjecutado());
        }
    }

    public void editar() {
        reservaCompromisoService.edit(reservaCompromiso);
        reservaCompromiso = new SolicitudReservaCompromiso();
    }

    public void edicionSolicitud(SolicitudReservaCompromiso s) {
        reservaCompromiso = new SolicitudReservaCompromiso();
        ocultarbutton = true;
        mostrarButton = false;
        listaGuardar = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;

        if (!"APRO".equals(reservaCompromiso.getEstado().getCodigo())) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "La solicitud debe estar aprobada para poder actualizarla");

            return;
        }

        this.procedimientoSeleccionado = s.getProcedimiento();

        listaeditable1 = reservaCompromisoService.getEdicionDetalleProducto(s);
        listaeditable2 = reservaCompromisoService.getEdicionDetallePDAndD(s);

        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        listaeditable1.forEach((detalle1) -> {
            this.listaGlobalDetalleSolicitudCompromisos.add(detalle1);
        });

        listaeditable2.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicitudPartidas.add(detalle2);
        });

        panel1 = false;
        panel2 = true;

        listaGlobalDetalleSolicitudCompromisos.forEach((lista1) -> {
            lista1.setMontoDisponible(lista1.getActividadProducto().getMonto().subtract(reservaCompromisoService.getSumaEdicionProductos(lista1.getActividadProducto())).add(lista1.getMontoSolicitado()));
        });

        listaGlobalDetalleSolicitudPartidas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getPresupuesto().getValorEgreso().subtract(reservaCompromisoService.getSumaEdicionPresupuesto(lis.getRefDistributivo().getId(), true)).add(lis.getMontoSolicitado()));
        });

        PrimeFaces.current().ajax().update("formMain");
    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    setFileName("/resources/demo/media/" + pr.getUrl());
                    break;
                } else {
                    setFileName("");
                }
            }
            if (getFileName().equals("")) {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void eliminarPDF(ProcedimientoRequisito procedimientoRequisito) {
        Boolean auxiliar = true;
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (pr.getIdProcedimientoRequisito().getId() == procedimientoRequisito.getId()) {
                    solicitudRequisitoList.remove(pr);
                    break;
                } else {
                    auxiliar = false;
                }
            }
            if (auxiliar) {
                JsfUtil.addInformationMessage("INFORMACIÓN", "DOCUMENTO ADJUNTO ELIMINADO CORRECTAMENTE");
            } else {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            }
        }
    }

    public void cargarRequisitos() {
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        PrimeFaces.current().ajax().update("requisitosTabla");
    }

    public void requisitoSeleccionado(ProcedimientoRequisito procedimientoRequisito) {
        files = new ArrayList<>();
        this.procedimientoRequisito = procedimientoRequisito;
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        try {
            files.add(event.getFile());
            if (files != null) {
                uploadDoc.upload(this.reservaCompromiso, files);
                JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
            }
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
        } catch (Exception e) {
        }
    }

    public void editarReservaCompromiso() {

        List<DetalleSolicitudCompromiso> listaValoresAnteriores;

        if (procedimientoSeleccionado.getId() != null) {
            if (validarRequisitos()) {
                return;
            }
        } else {
            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }
        boolean verificar = true;
        boolean pararMetodo = true;

        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty()) {

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {
                    listaGuardar.add(l);
                });
            }

            if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                listaGlobalDetalleSolicitudPartidas.forEach((item) -> {
                    listaGuardar.add(item);
                });
            }

        }

        listaValoresAnteriores = listaGuardar;

        reservaCompromisoService.eliminarSOlicitudesCascade(reservaCompromiso);
        reservaCompromisoService.eliminarSoclitudRequisitios(reservaCompromiso);

        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();

            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            }

            if (result1 > itemsuma.getActividadProducto().getMonto().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo par su reserva solicitada");
                break;
            }

        }

        if (!verificar) {

            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {

            num2 = 0;
            result2 = 0;
            num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();

            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
            }

            if (result2 > itemverificar.getPresupuesto().getValorEgreso().doubleValue()) {
                pararMetodo = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }

        }

        if (!pararMetodo) {

            return;
        }

        if (listaGlobalDetalleSolicitudCompromisos.isEmpty() || listaGlobalDetalleSolicitudPartidas.isEmpty()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No hay datos Seleccionados");

            return;
        }
        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromisoService.edit(reservaCompromiso);

        for (SolicitudRequisito solicitud : solicitudRequisitoList) {
            solicitudRequisito = new SolicitudRequisito();
            solicitudRequisito.setIdSolicitudReserva(reservaCompromiso);
            solicitudRequisito.setIdProcedimientoRequisito(solicitud.getIdProcedimientoRequisito());
            solicitudRequisito.setUrl(solicitud.getUrl());
            solicitudRequisito = solicitudRequisitoService.create(solicitudRequisito);
        }

        for (DetalleSolicitudCompromiso li : listaGuardar) {
            detalleSolicitud = new DetalleSolicitudCompromiso();

            detalleSolicitud.setSolicitud(reservaCompromiso);

            detalleSolicitud.setActividadProducto(li.getActividadProducto());
            detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
            detalleSolicitud.setEstado(true);
            detalleSolicitud.setPresupuesto(li.getPresupuesto());
            detalleSolicitud.setFechaCreacion(new Date());
            detalleSolicitud.setFechaModifcacion(new Date());
            detalleSolicitud.setUsuarioCreacion(userSession.getName());
            detalleSolicitud.setUsuarioModificacion(userSession.getName());
            detalleSolicitud.setPeriodo(li.getPeriodo());
            detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
            detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
            detalleSolicitud.setMontoComprometido(valorAnteirorPresupuesto);

            for (DetalleSolicitudCompromiso lli : listaValoresAnteriores) {

                if (lli.equals(li)) {
                    detalleSolicitud.setMontoComprometido(lli.getMontoComprometido());
                }
            }

            detalleSolicitud = detalleReservaService.create(detalleSolicitud);

        }

        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "editado correctamente");
        resetSolicitud();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        consultarMostrarAndOcultarPaneles();

    }

    public void resetSolicitud() {
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        ListadetalleSolicitud = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoSeleccionado = new Procedimiento();
    }

    public void CargarDatosBeneficiario() {
        if (reservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (reservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                proveedorLazy = new ProveedorLazy();
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new ServidorLazy();
            }
        } else {
            PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        }
    }

    public void AñadirBeneficiarioServidor(Servidor servidor) {
        reservaCompromiso.setBeneficiario(servidor.getPersona());
        reservaCompromiso.setTipoBeneficiario(Boolean.FALSE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        reservaCompromiso.setBeneficiario(provedor.getCliente());
        reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void handleCloseForm(CloseEvent event) {
        resetSolicitud();
        PrimeFaces.current().ajax().update(":DlgoVisualizacion");
    }

    public void armarDocumento(SolicitudReservaCompromiso s) {
        ss.borrarDatos();
        try {
            /*
            "EMITIDA"	"E"
            "OBSERVADA"	"O"
            "RECHAZADA"	"RECHA"
            "APROBADA"	"APRO"
            "REGISTRADA"	"R"
            "REVISIÓN "	"REVIS"
            "ANULADA"	"ADA"
            "LIQUIDADA"	"LIQUI"
            "LEGALIZADA"	"LEG"
            "LIBERADA"	"LIBE"
             */
            switch (s.getEstado().getCodigo()) {
                case "E":
                case "O":
                case "R":
                case "LEG":
                case "REVIS":
                    rutaDocumento = "";
                    break;
                case "APRO":
                    rutaDocumento = s.getRutaReserva();
                case "ADA":
                case "RECHA":
                    rutaDocumento = s.getRutaAnulada();
                    break;
                case "LIQUI":
                    rutaDocumento = s.getRutaLiquidada();
                    break;
                case "LIBE":
                    rutaDocumento = s.getRutaLiberada();
                    break;
                default:
                    rutaDocumento = "";
                    JsfUtil.addWarningMessage("ADVERTENCIA", "NO SE PUEDO ENVIAR EL CORREO");
                    break;

            }
            if (this.rutaDocumento == null || this.rutaDocumento.length() < 3 || this.rutaDocumento == "") {
                Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
                Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
                ss.addParametro("ci_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
                ss.addParametro("nombre_financiero", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
                 ss.addParametro("cargo_financiero", distributivo != null && distributivo.getCargo()!=null ? distributivo.getCargo().getNombreCargo():"");
                ss.addParametro("ci_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
                ss.addParametro("nombre_presupuesto", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
                ss.addParametro("cargo_presupuesto", distributivoMax != null && distributivoMax.getCargo()!=null ? distributivoMax.getCargo().getNombreCargo():"");
                if ("LIQUI".equals(s.getEstado().getCodigo())) {
                    ss.addParametro("reserva", s.getId());
                    ss.setNombreReporte("certificacionLiquidacion");
                    ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
                } else {
                    ss.addParametro("id", s.getId());
                    ss.addParametro("SUBREPORT_ENCABEZADO", JsfUtil.getRealPath("/reportes/"));
                    ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
                    ss.setNombreReporte("certificacionReservaCompromiso");
                    ss.setNombreSubCarpeta("CertificacionPresupuestaria");
                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                }
            } else {
                JsfUtil.executeJS("PF('dlgoDcoument').show()");
                JsfUtil.update("frmDocument");
            }

        } catch (Exception e) {

        }
    }

    private void calcularTotales() {
        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        totalLiquidado = BigDecimal.ZERO;
        totalLiberado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : visualizacionSolicitud) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
            totalEjecutado = totalEjecutado.add(detalle.getEjecutado());
            totalLiberado = totalLiberado.add(detalle.getLiberado());
            totalLiquidado = totalLiquidado.add(detalle.getLiquidado());
        }
        totalSolicitado = totalSolicitado.add(totalLiberado).add(totalLiquidado);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public BigDecimal getTotalLiquidado() {
        return totalLiquidado;
    }

    public void setTotalLiquidado(BigDecimal totalLiquidado) {
        this.totalLiquidado = totalLiquidado;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public List<UnidadAdministrativa> getListaFilroUnidad() {
        return listaFilroUnidad;
    }

    public void setListaFilroUnidad(List<UnidadAdministrativa> listaFilroUnidad) {
        this.listaFilroUnidad = listaFilroUnidad;
    }

    public List<CatalogoItem> getListaFiltroEstado() {
        return listaFiltroEstado;
    }

    public void setListaFiltroEstado(List<CatalogoItem> listaFiltroEstado) {
        this.listaFiltroEstado = listaFiltroEstado;
    }

    public List<Cliente> getListaFiltroCliente() {
        return listaFiltroCliente;
    }

    public void setListaFiltroCliente(List<Cliente> listaFiltroCliente) {
        this.listaFiltroCliente = listaFiltroCliente;
    }

    public BigDecimal getTotalLiberado() {
        return totalLiberado;
    }

    public void setTotalLiberado(BigDecimal totalLiberado) {
        this.totalLiberado = totalLiberado;
    }

    public List<ContratosReservaEjecuion> getListaAqusicionesGuardar() {
        return listaAqusicionesGuardar;
    }

    public void setListaAqusicionesGuardar(List<ContratosReservaEjecuion> listaAqusicionesGuardar) {
        this.listaAqusicionesGuardar = listaAqusicionesGuardar;
    }

    public Boolean getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Boolean tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Boolean getTablaContrato() {
        return tablaContrato;
    }

    public void setTablaContrato(Boolean tablaContrato) {
        this.tablaContrato = tablaContrato;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazyControl() {
        return lazyControl;
    }

    public void setLazyControl(LazyModel<SolicitudReservaCompromiso> lazyControl) {
        this.lazyControl = lazyControl;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Distributivo getCargoUnidadUser() {
        return cargoUnidadUser;
    }

    public void setCargoUnidadUser(Distributivo cargoUnidadUser) {
        this.cargoUnidadUser = cargoUnidadUser;
    }

    public BigDecimal getValorAnteirorPresupuesto() {
        return valorAnteirorPresupuesto;
    }

    public void setValorAnteirorPresupuesto(BigDecimal valorAnteirorPresupuesto) {
        this.valorAnteirorPresupuesto = valorAnteirorPresupuesto;
    }

    public List<Procedimiento> getProcedimientoList() {
        return procedimientoList;
    }

    public ReservaCompromisoDetalleLazy getLazy() {
        return lazy;
    }

    public void setLazy(ReservaCompromisoDetalleLazy lazy) {
        this.lazy = lazy;
    }

    public SoliciudReservaCompromisoLazy getLazyPrincipall() {
        return lazyPrincipall;
    }

    public void setLazyPrincipall(SoliciudReservaCompromisoLazy lazyPrincipall) {
        this.lazyPrincipall = lazyPrincipall;
    }

    public void setProcedimientoList(List<Procedimiento> procedimientoList) {
        this.procedimientoList = procedimientoList;
    }

    public Procedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(Procedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public LazyModel<SolicitudRequisito> getSolicitudRequisitoLazyModel() {
        return solicitudRequisitoLazyModel;
    }

    public void setSolicitudRequisitoLazyModel(LazyModel<SolicitudRequisito> solicitudRequisitoLazyModel) {
        this.solicitudRequisitoLazyModel = solicitudRequisitoLazyModel;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<UnidadAdministrativa> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadAdministrativa> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<Producto> getListadeProductoSeleccionados() {
        return listadeProductoSeleccionados;
    }

    public void setListadeProductoSeleccionados(List<Producto> listadeProductoSeleccionados) {
        this.listadeProductoSeleccionados = listadeProductoSeleccionados;
    }

    public List<DetalleSolicitudCompromiso> getListadetalleSolicitud() {
        return ListadetalleSolicitud;
    }

    public void setListadetalleSolicitud(List<DetalleSolicitudCompromiso> ListadetalleSolicitud) {
        this.ListadetalleSolicitud = ListadetalleSolicitud;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(DetalleSolicitudCompromiso detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudCompromisos() {
        return listaGlobalDetalleSolicitudCompromisos;
    }

    public void setListaGlobalDetalleSolicitudCompromisos(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos) {
        this.listaGlobalDetalleSolicitudCompromisos = listaGlobalDetalleSolicitudCompromisos;
    }

    public boolean isMostrarOculutarTabla() {
        return mostrarOculutarTabla;
    }

    public void setMostrarOculutarTabla(boolean mostrarOculutarTabla) {
        this.mostrarOculutarTabla = mostrarOculutarTabla;
    }

    public String getCbTipo() {
        return cbTipo;
    }

    public void setCbTipo(String cbTipo) {
        this.cbTipo = cbTipo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<DetalleSolicitudCompromiso> getListaTotal() {
        return listaTotal;
    }

    public void setListaTotal(List<DetalleSolicitudCompromiso> listaTotal) {
        this.listaTotal = listaTotal;
    }

    public boolean isOcultarcb() {
        return ocultarcb;
    }

    public void setOcultarcb(boolean ocultarcb) {
        this.ocultarcb = ocultarcb;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }

    public boolean isTabla1() {
        return tabla1;
    }

    public void setTabla1(boolean tabla1) {
        this.tabla1 = tabla1;
    }

    public boolean isTabla2() {
        return tabla2;
    }

    public void setTabla2(boolean tabla2) {
        this.tabla2 = tabla2;
    }

    public boolean isTabla3() {
        return tabla3;
    }

    public void setTabla3(boolean tabla3) {
        this.tabla3 = tabla3;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudPartidas() {
        return listaGlobalDetalleSolicitudPartidas;
    }

    public void setListaGlobalDetalleSolicitudPartidas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas) {
        this.listaGlobalDetalleSolicitudPartidas = listaGlobalDetalleSolicitudPartidas;
    }

    public List<DetalleSolicitudCompromiso> getListaGuardar() {
        return listaGuardar;
    }

    public void setListaGuardar(List<DetalleSolicitudCompromiso> listaGuardar) {
        this.listaGuardar = listaGuardar;
    }

    public List<Presupuesto> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<Presupuesto> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public List<SolicitudRequisito> getSolicitudRequisitoList() {
        return solicitudRequisitoList;
    }

    public void setSolicitudRequisitoList(List<SolicitudRequisito> solicitudRequisitoList) {
        this.solicitudRequisitoList = solicitudRequisitoList;
    }

    public SolicitudRequisito getSolicitudRequisito() {
        return solicitudRequisito;
    }

    public void setSolicitudRequisito(SolicitudRequisito solicitudRequisito) {
        this.solicitudRequisito = solicitudRequisito;
    }

    public ProcedimientoRequisito getProcedimientoRequisito() {
        return procedimientoRequisito;
    }

    public void setProcedimientoRequisito(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public CatalogoItem getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(CatalogoItem catalogoItem) {
        this.catalogoItem = catalogoItem;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazyReal() {
        return lazyReal;
    }

    public void setLazyReal(LazyModel<SolicitudReservaCompromiso> lazyReal) {
        this.lazyReal = lazyReal;
    }

    public List<DetalleSolicitudCompromiso> getVisualizacionSolicitud() {
        return visualizacionSolicitud;
    }

    public void setVisualizacionSolicitud(List<DetalleSolicitudCompromiso> visualizacionSolicitud) {
        this.visualizacionSolicitud = visualizacionSolicitud;
    }

    public boolean isOcultarbutton() {
        return ocultarbutton;
    }

    public void setOcultarbutton(boolean ocultarbutton) {
        this.ocultarbutton = ocultarbutton;
    }

    public boolean isMostrarButton() {
        return mostrarButton;
    }

    public void setMostrarButton(boolean mostrarButton) {
        this.mostrarButton = mostrarButton;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable1() {
        return listaeditable1;
    }

    public void setListaeditable1(List<DetalleSolicitudCompromiso> listaeditable1) {
        this.listaeditable1 = listaeditable1;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable2() {
        return listaeditable2;
    }

    public void setListaeditable2(List<DetalleSolicitudCompromiso> listaeditable2) {
        this.listaeditable2 = listaeditable2;
    }

    public double getNum1() {
        return num1;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public double getResult1() {
        return result1;
    }

    public void setResult1(double result1) {
        this.result1 = result1;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    public double getResult2() {
        return result2;
    }

    public void setResult2(double result2) {
        this.result2 = result2;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public ProveedorLazy getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(ProveedorLazy proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public ServidorLazy getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(ServidorLazy servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public boolean isPanelProveedor() {
        return panelProveedor;
    }

    public void setPanelProveedor(boolean panelProveedor) {
        this.panelProveedor = panelProveedor;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public boolean isPanelServidor() {
        return panelServidor;
    }

    public void setPanelServidor(boolean panelServidor) {
        this.panelServidor = panelServidor;
    }
//</editor-fold>

}
