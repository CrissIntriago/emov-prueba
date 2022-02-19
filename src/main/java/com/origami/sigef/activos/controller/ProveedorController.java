/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "proveedorView")
@ViewScoped
public class ProveedorController implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ProveedorController.class.getName());
    
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession userSession;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private detalleBancoServices detalleBancoService;
    @Inject
    private DatosGeneralesEntidadService provinciaCantonService;
    
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Banco> bancoLazyModel;
    
    private List<Provincia> provincias;
    private List<Canton> cantonProveedor, cantonContacto;
    private List<CatalogoItem> tiposIdentificacion, generos, clasificaciones, tipos;
    private List<DetalleBanco> detalleBancoList, cuentasRemovidas;
    private List<CatalogoItem> tipoCuentaBancaria;
    
    private Cliente clienteProveedor, clienteContacto;
    private Proveedor proveedor;
    private Provincia provinciaProveedor, provinciaContacto;
    
    private Boolean vistaFormulario, view;
    
    @PostConstruct
    public void initialize() {
        this.proveedorLazyModel = new LazyModel<>(Proveedor.class);
        this.proveedorLazyModel.getSorteds().put("cliente.nombre", "ASC");
        this.proveedorLazyModel.getFilterss().put("estado", true);
        this.proveedorLazyModel.setDistinct(false);
        this.tiposIdentificacion = catalogoItemService.findByCatalogo("tipo_identificacion_beneficiario");
        this.generos = catalogoItemService.findByCatalogo("tipo_genero");
        this.provincias = provinciaCantonService.getProvincias();
        this.clasificaciones = catalogoItemService.findByCatalogo("personeria_juridica");
        this.tipoCuentaBancaria = catalogoItemService.findByCatalogo("tipo_cuenta_bancaria");
        vistaFormulario = Boolean.TRUE;
        cleanFormulario();
    }
    
    public void cleanFormulario() {
        proveedor = new Proveedor();
        clienteProveedor = new Cliente();
        clienteContacto = new Cliente();
        provinciaProveedor = null;
        provinciaContacto = null;
        cantonProveedor = new ArrayList<>();
        cantonContacto = new ArrayList<>();
        detalleBancoList = new ArrayList<>();
        cuentasRemovidas = new ArrayList<>();
    }
    
    public void fomulario(Proveedor proveedor, Boolean view) {
        this.view = view;
        this.vistaFormulario = Boolean.FALSE;
        if (proveedor == null) {
            cleanFormulario();
        } else {
            this.proveedor = proveedor;
            this.clienteProveedor = proveedor.getCliente();
            if (this.clienteProveedor.getTipoIdentificacion().getCodigo().equals("RUC")) {
                if (this.clienteProveedor.getRuc() != null) {
                    this.clienteProveedor.setIdentificacion(this.clienteProveedor.getIdentificacion().concat(this.clienteProveedor.getRuc()));
                } else {
                    this.clienteProveedor.setIdentificacion(this.clienteProveedor.getIdentificacion().concat("001"));
                }
            }
            if (this.proveedor.getContacto() != null) {
                this.clienteContacto = this.proveedor.getContacto();
                if (this.clienteContacto.getTipoIdentificacion().getCodigo().equals("RUC")) {
                    if (this.clienteContacto.getRuc() != null) {
                        this.clienteContacto.setIdentificacion(this.clienteContacto.getIdentificacion().concat(this.clienteContacto.getRuc()));
                    } else {
                        this.clienteContacto.setIdentificacion(this.clienteContacto.getIdentificacion().concat("001"));
                    }
                }
            } else {
                this.clienteContacto = new Cliente();
            }
            detalleBancoList = detalleBancoService.findListBancoByProveedor(proveedor);
        }
        PrimeFaces.current().ajax().update("formMain");
    }
    
    public void returnListado() {
        this.vistaFormulario = Boolean.TRUE;
        cleanFormulario();
        PrimeFaces.current().ajax().update("formMain");
    }
    
    public void findProveedor(Boolean tipoProveedor) {
        String msj = "";
        if (tipoProveedor) {
            msj = validador(clienteProveedor);
        } else {
            msj = validador(clienteContacto);
        }
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        if (tipoProveedor) {
            clienteProveedor = Utils.clone(clienteService.findByCliente(clienteProveedor));
            proveedor.setActividadComercial(clienteProveedor.getActividadGeneral());
        } else {
            clienteContacto = Utils.clone(clienteService.findByCliente(clienteContacto));
        }
    }
    
    private String validador(Cliente cliente) {
        String result = "";
        if (cliente.getTipoIdentificacion() == null) {
            return "Debe seleccionar un tipo de identificación";
        }
        if (cliente.getIdentificacion() == null || cliente.getIdentificacion().equals("")) {
            return "Debe ingresar un número de identificación";
        }
        if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            if (cliente.getIdentificacion().length() < 13 || cliente.getIdentificacion().length() > 13) {
                return "El numero de digitos ingresados en la identificación es incorrecto";
            }
        } else if (cliente.getTipoIdentificacion().getCodigo().equals("C")) {
            if (cliente.getIdentificacion().length() < 10 || cliente.getIdentificacion().length() > 10) {
                return "El numero de digitos ingresados en la identificación es incorrecto";
            }
        }
        if (!Utils.validateCCRuc(cliente.getIdentificacion())) {
            return "El numero de identificación es incorrecto";
        }
        return result;
    }
    
    public void actualizarCantones(Boolean tipoCliente) {
        if (tipoCliente) {
            if (provinciaProveedor != null) {
                this.cantonProveedor = provinciaCantonService.getCantones(provinciaProveedor);
            } else {
                this.cantonProveedor = null;
            }
        } else {
            if (provinciaContacto != null) {
                this.cantonContacto = provinciaCantonService.getCantones(provinciaContacto);
            } else {
                this.cantonContacto = null;
            }
        }
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
        PrimeFaces.current().ajax().update("entidadBancariaTable");
        JsfUtil.addInformationMessage("Entidad Bancaria", "Se ha añadido correctamente");
    }
    
    public void saveFormulario() {
        Boolean edit = proveedor.getId() != null;
        String msj_1 = validarFormularioCliente(clienteProveedor);
        if (!msj_1.equals("")) {
            JsfUtil.addWarningMessage("PROVEEDOR", msj_1);
            return;
        }
        if (clienteContacto.getTipoIdentificacion() != null && clienteContacto.getIdentificacion() != null) {
            String msj_2 = validarFormularioCliente(clienteContacto);
            if (!msj_2.equals("")) {
                JsfUtil.addWarningMessage("CONTACTO", msj_2);
                return;
            }
        }
        int countEstados = 0;
        int countRetenciones = 0;
        if (!detalleBancoList.isEmpty()) {
            for (DetalleBanco item : detalleBancoList) {
                if (item.getEstadoCuenta()) {
                    countEstados += 1;
                }
                if (item.getCtaRetenciones()) {
                    countRetenciones += 1;
                }
            }
            //validar si hay activos
            if (countEstados > 1 && countRetenciones == 0) {
                JsfUtil.addWarningMessage("AVISO!!!", "Solo debe tener una cuenta activa");
                return;
            }
            // validar que solo haya uno activo de retenciones
            if (countRetenciones > 1 && countEstados > 1 && countRetenciones == countEstados) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe tener una cuenta de retenciones judiciales activa");
                return;
            }
        }
        if (edit) {
            clienteProveedor = createUpdateRegistro(clienteProveedor);
            registrarContacto();
            proveedor.setUsuarioModificacion(userSession.getNameUser());
            proveedor.setFechaModificacion(new Date());
            proveedorService.edit(proveedor);
        } else {
            String id_tempt = Utils.clone(clienteProveedor.getIdentificacion());
            if (clienteProveedor.getTipoIdentificacion().getCodigo().equals("RUC")) {
                id_tempt = id_tempt.substring(0, 10);
            }
            if (proveedorService.findByNamedQuery1("Proveedor.findByIdentificacion", clienteProveedor.getTipoIdentificacion(), id_tempt) != null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Ya exite un proveedor registrado");
                return;
            }
            clienteProveedor = createUpdateRegistro(clienteProveedor);
            registrarContacto();
            proveedor.setCliente(clienteProveedor);
            proveedor.setUsuarioCreacion(userSession.getNameUser());
            proveedor.setFechaCreacion(new Date());
            proveedor = proveedorService.create(proveedor);
        }
        //crear detalle de banco
        for (DetalleBanco bancoDetalle : detalleBancoList) {
            bancoDetalle.setProveedor(proveedor);
            if (bancoDetalle.getId() == null) {
                detalleBancoService.create(bancoDetalle);
            } else {
                detalleBancoService.edit(bancoDetalle);
            }
        }
        //eliminar detalle de banco
        for (DetalleBanco bancoDetalle : cuentasRemovidas) {
            detalleBancoService.remove(bancoDetalle);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        returnListado();
    }
    
    public Cliente createUpdateRegistro(Cliente cliente) {
        if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            String temp = Utils.clone(cliente.getIdentificacion());
            cliente.setIdentificacion(cliente.getIdentificacion().substring(0, 10));
            cliente.setRuc(temp.substring(10, 13));
        } else {
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
    
    public void registrarContacto() {
        if (clienteContacto.getTipoIdentificacion() != null && clienteContacto.getIdentificacion() != null && clienteContacto.getNombre() != null && clienteContacto.getApellido() != null) {
            clienteContacto = createUpdateRegistro(clienteContacto);
            proveedor.setContacto(clienteContacto);
        }
    }
    
    public String validarFormularioCliente(Cliente cliente) {
        String mensaje = validador(cliente);
        if (mensaje.equals("")) {
            if (cliente.getTipoIdentificacion() != null && cliente.getIdentificacion() != null) {
                if (cliente.getNombre() == null || cliente.getNombre().equals("")) {
                    return "Debe ingresar la razón social o nombres";
                }
                if (cliente.getDireccion() == null || cliente.getDireccion().equals("")) {
                    return "Debe ingresar una dirección";
                }
            }
        }
        return mensaje;
    }
    
    public void actualizarTipo() {
        if (clienteProveedor.getClasificacionProv() != null) {
            String opcion = clienteProveedor.getClasificacionProv().getTexto();
            if (opcion.equals("JURIDICA")) {
                proveedor.setRepresentanteLegal(Boolean.TRUE);
            } else {
                proveedor.setRepresentanteLegal(Boolean.FALSE);
            }
            tipos = catalogoItemService.findCatalogotipoLike("tipos_personeria_juridica_natural", opcion);
        } else {
            tipos = null;
            proveedor.setRepresentanteLegal(Boolean.FALSE);
        }
        PrimeFaces.current().ajax().update("fieldContacto");
    }
    
    public void actualizarContribuyente() {
        if (clienteProveedor.getTipoProv().getTexto().equals("CONTRIBUYENTE ESPECIAL")) {
            clienteProveedor.setContribuyenteEspecial(Boolean.TRUE);
        } else {
            clienteProveedor.setContribuyenteEspecial(Boolean.FALSE);
        }
    }
    
    public void removerCuentaBancaria(DetalleBanco detalleBanco, int index) {
        if (detalleBanco.getId() != null) {
            cuentasRemovidas.add(detalleBanco);
            detalleBancoList.remove(detalleBanco);
        } else {
            detalleBancoList.remove(index);
        }
        JsfUtil.addInformationMessage("Entidad Bancaria", "ha sido removido correctamente");
    }
    
    public void deleteProveedor(Proveedor proveedor) {
        proveedor.setEstado(Boolean.TRUE);
        proveedorService.edit(proveedor);
        JsfUtil.addSuccessMessage("INFO!!!", "Proveedor eliminado correctamente");
    }
    
    public Boolean getView() {
        return view;
    }
    
    public void setView(Boolean view) {
        this.view = view;
    }
    
    public List<CatalogoItem> getTiposIdentificacion() {
        return tiposIdentificacion;
    }
    
    public void setTiposIdentificacion(List<CatalogoItem> tiposIdentificacion) {
        this.tiposIdentificacion = tiposIdentificacion;
    }
    
    public List<CatalogoItem> getGeneros() {
        return generos;
    }
    
    public void setGeneros(List<CatalogoItem> generos) {
        this.generos = generos;
    }
    
    public LazyModel<Banco> getBancoLazyModel() {
        return bancoLazyModel;
    }
    
    public List<DetalleBanco> getDetalleBancoList() {
        return detalleBancoList;
    }
    
    public List<CatalogoItem> getTipoCuentaBancaria() {
        return tipoCuentaBancaria;
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
    
    public List<Provincia> getProvincias() {
        return provincias;
    }
    
    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
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
    
    public Cliente getClienteProveedor() {
        return clienteProveedor;
    }
    
    public void setClienteProveedor(Cliente clienteProveedor) {
        this.clienteProveedor = clienteProveedor;
    }
    
    public Cliente getClienteContacto() {
        return clienteContacto;
    }
    
    public void setClienteContacto(Cliente clienteContacto) {
        this.clienteContacto = clienteContacto;
    }
    
    public Proveedor getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }
    
    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }
    
    public Boolean getVistaFormulario() {
        return vistaFormulario;
    }
    
    public void setVistaFormulario(Boolean vistaFormulario) {
        this.vistaFormulario = vistaFormulario;
    }
    
}
