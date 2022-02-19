/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenLocalComercial;
import com.gestionTributaria.Comisaria.Service.InspeccionService;
import com.gestionTributaria.Comisaria.Service.OrdernesService;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.Ordenes;
import com.gestionTributaria.Services.CatPredioService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "recepcionComisariaPFNMB")
@ViewScoped
public class RecepcionComisariaPFNMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private OrdernesService ordenesService;
    @Inject
    private CatPredioService catPredioService;
    @Inject
    private InspeccionService inspeccionService;
    private CatPredio predio;
    private String claveCatastral;
    private LazyModel<FinaRenLocalComercial> localesLazy;
    private Integer opc = 0;
    private List<Usuarios> listClientes;
    private Usuarios clienteSelect;
    private UploadedFile file;
    private Ordenes ordenes;
    private Date fecha;
    private String direccion, nroLocal, actividadLocal;
    private FinaRenLocalComercial localComercial;

    @PostConstruct

    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.delegados);
                ordenes = new Ordenes();
                predio = new CatPredio();
                fecha = new Date();
                localesLazy = new LazyModel(FinaRenLocalComercial.class);
                localesLazy.getFilterss().put("estadoLocalComercial", 1L);
                localesLazy.getFilterss().put("estado", true);
                localComercial = new FinaRenLocalComercial();
                buscarOrden();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarPredio() {
        if (claveCatastral.isEmpty()) {
            JsfUtil.executeJS("PF('dlgLocales').show()");
        }
    }

    public void buscarOrden() {
        ordenes = ordenesService.findByOrden(tramite.getNumTramite());
        if (ordenes == null) {
            ordenes = new Ordenes();
        } else {
            direccion = ordenes.getLugar();
            nroLocal = ordenes.getLocalComercial().getNumLocal();
            predio = ordenes.getPredio();
            actividadLocal = ordenes.getLocalComercial().getActividadComercial();
            JsfUtil.update("dlgOrdenInspeccion");
        }
    }

    public void seleccionarLocal(FinaRenLocalComercial local) {
        JsfUtil.addInformationMessage("", "Local Seleccionado: " + local.getClavePreial() + " Numero Local: " + local.getNumLocal());
        predio = catPredioService.finByPredioClaveCatastral(local.getClavePreial());
        localComercial = local;
        direccion = predio.getDireccion();
        nroLocal = local.getNumLocal();
        actividadLocal = local.getActividadComercial();
    }

    public void guardarOrden() {
        if (ordenes.getId() != null) {
            ordenesService.edit(ordenes);
            JsfUtil.addInformationMessage("", "Se actualizó con éxito");
        } else {
            if (direccion.isEmpty() || ordenes.getMotivo().isEmpty()) {
                JsfUtil.addErrorMessage("", "LOS CAMPOS SON OBLIGATORIOS");
            } else {
                ordenes.setFecha(fecha);
                ordenes.setPredio(predio);
                ordenes.setLugar(predio.getDireccion());
                ordenes.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
                ordenes.setOrigen("FUNCIONAMIENTO");
                ordenes.setTipoComisaria("FUNCIONAMIENTO");
                ordenes.setNoSolicitud(BigInteger.valueOf(inspeccionService.noSolicitud()));
                ordenes.setComisariaUser(BigInteger.valueOf(session.getUsuario().getId()));
                ordenes.setTipoTramite(BigInteger.valueOf(tramite.getTipoTramite().getId()));
                ordenes.setLocalComercial(localComercial);
                ordenes.setActividadLocal(BigInteger.valueOf(localComercial.getTipoLocal().getId()));
                ordenes = ordenesService.create(ordenes);
                JsfUtil.addInformationMessage("", "Se creó con éxito la orden");
            }
        }
    }

    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }

    public void opendlg(Integer opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void continuarJefeDelegado() {
        try {
            String usuario = "";
            System.out.println("valor de la opcion: " + this.opc);
//        Ordenes orden = new Ordenes();
//        orden = ordenesService.findByOrden(tramite.getNumTramite());
//        if (orden != null) {
            if (this.opc == 1) {
                usuario = clienteService.getrolsUser(RolUsuario.jefeDelegado);
                getParamts().put("usuarioJefeDelegado", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("opcion", 0);
            }
            if (this.opc == 0) {
                usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
                getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("opcion", 2);
            }
            if (this.opc == 2) {
                usuario = clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
                getParamts().put("usuarioComisariaInforme", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("opcion", 1);
            }
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void completarTarea() {
        try {
            String usuario = "";
            switch (opc) {
                case 0://Coreccion
                    usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
                    getParamts().put("usuarioAsistenteJVCD", usuario.equals("") ? "admin_1" : usuario);
                    break;
                case 1://Generar Inspección 
                    if (clienteSelect == null) {
                        JsfUtil.addWarningMessage("Información", "Seleccionar un delegado.");
                        return;
                    }
                    getParamts().put("usuarioDelegado", clienteSelect.getUsuario());
                    break;
                case 2://Informe Comisaría
                    usuario = clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
                    getParamts().put("usuarioComisariaInforme", usuario.equals("") ? "admin_1" : usuario);
                    break;
            }
            getParamts().put("inspeccion", opc);

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public List<Usuarios> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Usuarios> listClientes) {
        this.listClientes = listClientes;
    }

    public Usuarios getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Usuarios clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public Integer getOpc() {
        return opc;
    }

    public void setOpc(Integer opc) {
        this.opc = opc;
    }

    public Ordenes getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Ordenes ordenes) {
        this.ordenes = ordenes;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LazyModel<FinaRenLocalComercial> getLocalesLazy() {
        return localesLazy;
    }

    public void setLocalesLazy(LazyModel<FinaRenLocalComercial> localesLazy) {
        this.localesLazy = localesLazy;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public String getNroLocal() {
        return nroLocal;
    }

    public void setNroLocal(String nroLocal) {
        this.nroLocal = nroLocal;
    }

    public String getActividadLocal() {
        return actividadLocal;
    }

    public void setActividadLocal(String actividadLocal) {
        this.actividadLocal = actividadLocal;
    }

//</editor-fold>
}
