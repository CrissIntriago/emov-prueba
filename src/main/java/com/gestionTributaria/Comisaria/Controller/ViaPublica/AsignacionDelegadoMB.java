/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller.ViaPublica;

import com.gestionTributaria.Comisaria.Entities.Inspecciones;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Comisaria.Service.InspeccionService;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Admin
 */
@Named
@ViewScoped
public class AsignacionDelegadoMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private ManagerService service;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private InspeccionService inspeccionService;
    @Inject
    private ComisariaServices comisariaServices;
    private List<Usuarios> listClientes;
    private Usuarios clienteSelect;
    private CatalogoItem comisariaSelect;
    private LazyModel<Inspecciones> lazyInspeccion;
    private Inspecciones inspeccion;
    private CatPredio predio;
    private List<Inspecciones> listaInspecciones;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                inspeccion = new Inspecciones();
                predio = new CatPredio();
                listaInspecciones = new ArrayList<>();
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.delegados);
                comisariaSelect = catalogoService.getItemByCatalogoAndCodigo("GT_lista_comisarias", (String) getVariable("tipo_comisaria"));
                loadInspecciones();

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));

            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void opendlg() {

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void loadInspecciones() {
        lazyInspeccion = new LazyModel<>(Inspecciones.class);
        lazyInspeccion.getFilterss().put("numTramite", tramite.getNumTramite());
    }

    public void completarTarea() throws Exception {
        if (clienteSelect == null) {
            JsfUtil.addWarningMessage("Información", "Seleccionar un delegado.");
            return;
        }
        String usuario = clienteSelect != null && clienteSelect.getId() != null ? clienteSelect.getUsuario() : session.getNameUser();
        getParamts().put("evidencia", usuario);

        listaInspecciones = new ArrayList<>();

        listaInspecciones = inspeccionService.getListaInspecciones(tramite.getNumTramite());

        if (listaInspecciones != null && !listaInspecciones.isEmpty()) {
            for (Inspecciones item : listaInspecciones) {
                if (clienteSelect != null && clienteSelect.getFuncionario() != null && clienteSelect.getFuncionario().getPersona() != null) {
                    item.setDelegado(clienteSelect.getFuncionario().getPersona());
                    inspeccionService.edit(item);
                }

            }
        }
        JsfUtil.executeJS("PF('dlgObservaciones').hide()");
        JsfUtil.update(":frmDlgObser");
        if (this.saveTramite() == null) {
            return;
        }
        this.session.setVarTemp(null);
        super.completeTask((HashMap<String, Object>) getParamts());
        JsfUtil.update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");

    }

    public void saveSolcitud() {

        inspeccionService.edit(inspeccion);

        JsfUtil.addInformationMessage("", "La observación se registro correctamente");
        JsfUtil.executeJS("PF('dlgoInspeccion').hide()");
        inspeccion = new Inspecciones();

    }

    public void generarSolciitud(Inspecciones solicitudInspeccion) {
        this.inspeccion = new Inspecciones();
        if (solicitudInspeccion != null) {
            this.inspeccion = solicitudInspeccion;
            if (solicitudInspeccion.getPredio() != null) {
                this.predio = service.find(CatPredio.class, solicitudInspeccion.getPredio());
            }
        } else {
            this.inspeccion.setComisariaUser(session.getUsuario());
        }

    }

    public void completarTareaEvidencia() throws Exception {
        String usuario = "";
        List<HistoricTaskInstance> tasks = engine.getTaskByProcessInstanceId(this.tramite.getIdProceso());
        System.out.println("tastk " + tasks.size());
        if (tasks != null && !tasks.isEmpty()) {
            for (HistoricTaskInstance item : tasks) {

                if (item.getTaskDefinitionKey().equals("usertask10")) {

                    usuario = item.getAssignee();
                    break;
                }
            }
        }

        getParamts().put("emision", usuario);
        JsfUtil.executeJS("PF('dlgObservaciones').hide()");
        JsfUtil.update(":frmDlgObser");
        if (listaInspecciones != null && !listaInspecciones.isEmpty()) {
            for (Inspecciones item : listaInspecciones) {
                if (clienteSelect != null && clienteSelect.getFuncionario() != null && clienteSelect.getFuncionario().getPersona() != null) {
                    item.setDelegado(clienteSelect.getFuncionario().getPersona());
                    inspeccionService.edit(item);
                }
            }
        }
        if (this.saveTramite() == null) {
            return;
        }
        this.session.setVarTemp(null);
        super.completeTask((HashMap<String, Object>) getParamts());
        JsfUtil.update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");

    }

    public void completarTareaRealizarLiquidacion(int index) throws Exception {
        String usuario = "";

        getParamts().put("condicion", index);

        if (index == 1) {
            if (tramite.getTipoTramite().getAbreviatura().equals("PVPU")) {
                List<HistoricTaskInstance> tasks = engine.getTaskByProcessInstanceId(this.tramite.getIdProceso());
                System.out.println("tastk " + tasks.size());
                if (tasks != null && !tasks.isEmpty()) {
                    for (HistoricTaskInstance item : tasks) {

                        if (item.getTaskDefinitionKey().equals("usertask10")) {

                            usuario = item.getAssignee();
                            break;
                        }
                    }
                }

            } else {
                usuario = clienteService.getrolsUser(RolUsuario.comisarioInquilinato);
            }
            getParamts().put("realizacion_liquidacion", usuario);
        } else if (index == 2) {
            List<HistoricTaskInstance> tasks = engine.getTaskByProcessInstanceId(this.tramite.getIdProceso());
         
            if (tasks != null && !tasks.isEmpty()) {
                for (HistoricTaskInstance item : tasks) {
                  
                    if (item.getTaskDefinitionKey().equals("usertask10")) {
                      
                        usuario = item.getAssignee();
                        break;
                    }
                }
            }

            getParamts().put("solicitar_delegado", usuario);
        } else {
            this.enviarCorreo();

        }

        JsfUtil.executeJS("PF('dlgObservaciones').hide()");
        JsfUtil.update(":frmDlgObser");
        if (listaInspecciones != null && !listaInspecciones.isEmpty()) {
            for (Inspecciones item : listaInspecciones) {
                item.setDelegado(clienteService.obtClienteByUsuario(usuario));
                inspeccionService.edit(item);
            }
        }
        if (this.saveTramite() == null) {
            return;
        }
        this.session.setVarTemp(null);
        super.completeTask((HashMap<String, Object>) getParamts());
        JsfUtil.update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");

    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
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

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public LazyModel<Inspecciones> getLazyInspeccion() {
        return lazyInspeccion;
    }

    public void setLazyInspeccion(LazyModel<Inspecciones> lazyInspeccion) {
        this.lazyInspeccion = lazyInspeccion;
    }

    public Inspecciones getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Inspecciones inspeccion) {
        this.inspeccion = inspeccion;
    }

    public List<Inspecciones> getListaInspecciones() {
        return listaInspecciones;
    }

    public void setListaInspecciones(List<Inspecciones> listaInspecciones) {
        this.listaInspecciones = listaInspecciones;
    }

    public CatalogoItem getComisariaSelect() {
        return comisariaSelect;
    }

    public void setComisariaSelect(CatalogoItem comisariaSelect) {
        this.comisariaSelect = comisariaSelect;
    }
//</editor-fold>
}
