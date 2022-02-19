/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.StartProcessController;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.Tramites;
import com.origami.sigef.resource.procesos.services.HistoricoTramiteService;
import com.origami.sigef.resource.procesos.services.ObservacionesService;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class OthersControlProcess implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OthersControlProcess.class.getName());
    
    @Inject
    private HistoricoTramiteService tramiteService;
    @Inject
    private ObservacionesService obsService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private BpmBaseEngine baseEngine;
    @Inject
    private FileUploadDoc fileSUploadDoc;
    @Inject
    private ProcesoService tramiteServiceu;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private MasterCatalogoService periodoService;
    
    protected LazyModel<Tramites> lazy;
    private List<HistoricTaskInstance> tasks;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;
    private Tramites instTramite;
    private StreamedContent imageProcessInstance;
    private Integer prioridad = 50;
    private String deleteReason;
    private String reasondatemodif;
    private List<Usuarios> usuarios;
    private Task tarea;
    private List<Task> tareas;
    private Usuarios usuario;
    private OpcionBusqueda opcionBusqueda;
    private List<MasterCatalogo> listaPeriodo;
    
    @PostConstruct
    protected void iniView() {
        try {
            opcionBusqueda = new OpcionBusqueda();
            if (!JsfUtil.isAjaxRequest()) {
                this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
                if (listaPeriodo != null) {
                    int indice = listaPeriodo.size();
                    if (!listaPeriodo.isEmpty() && indice == 1) {
                        opcionBusqueda.setAnio(listaPeriodo.get(0).getAnio());
                    } else {
                        //opcionBusqueda.setAnio(listaPeriodo.get(indice - 1).getAnio());
                        opcionBusqueda.setAnio(Short.valueOf(Utils.getAnio(new Date()).toString()));
                    }
                }
                actualizarTabla();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void actualizarTabla() {
        if (opcionBusqueda.getAnio() != null) {
            lazy = new LazyModel<>(Tramites.class);
            lazy.getFilterss().put("idTipoTramite.id", Arrays.asList(61L,69L,70L,126L,127L,128L,129L));
            lazy.getSorteds().put("startTime", "DESC");
            //lazy.getSorteds().put("numTramite", "DESC");
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            lazy = null;
        }
    }
    
    public void viewProcess(Tramites tramite) {
        tasks = baseEngine.getTaskByProcessInstanceId(tramite.getProcInstId());
        this.instTramite = tramite;
        this.instTramite.getTramite().setNombreBeneficiario(tramiteService.findReferencia(tramite.getTramite()));
        viewRequisitos(tramite.getTramite().getTipoTramite().getId());
        InputStream diagram = baseEngine.getProcessInstanceDiagram(tramite.getProcInstId());
        if (diagram != null) {
            imageProcessInstance = new DefaultStreamedContent(baseEngine.getProcessInstanceDiagram(tramite.getProcInstId()));
        } else {
            imageProcessInstance = new DefaultStreamedContent();
        }
        JsfUtil.executeJS("PF('dlgTramite').show()");
        JsfUtil.update("frmDetalleTarea");
    }
    
    private void viewRequisitos(Long idTipo) {
        List<ProcedimientoRequisito> listaRequisitos = tramiteServiceu.getListaRequisito(idTipo);
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            ListarequisitosModel req = new ListarequisitosModel();
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                req.setRequisitos(tipoTramite);
                req.setFile(null);
                requisitosTramite.add(req);
                req = new ListarequisitosModel();
            }
        }
    }
    
    public void viewFile(ListarequisitosModel modelFile) {
        this.requisitosObjeto = modelFile;
        if (modelFile.getFile() != null) {
            try {
                ss.setContentType(modelFile.getFile().getContentType());
                ss.setNombreDocumento(modelFile.getFile().getFileName());
                ss.setTempData(modelFile.getFile().getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(StartProcessController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        try {
            fileSUploadDoc.upload(instTramite.getTramite(), Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(instTramite.getTramite().getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.create(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.update("frmDetalleTarea:tbDetalletramite:docTramite:dtArchivosTramites");
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }
    
    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }
    
    public void reasignarProcess(Tramites tramite) {
        try {
            this.instTramite = tramite;
            usuarios = this.usuarioService.findAllOrderActivos();
            if (tramite.getCallProcInstId() != null) {
                tareas = this.baseEngine.allTaskDataByProcessID(tramite.getCallProcInstId());
            } else {
                tareas = this.baseEngine.allTaskDataByProcessID(tramite.getProcInstId());
            }
            if (Utils.isNotEmpty(tareas)) {
                if (tareas.size() == 0) {
                    tarea = tareas.get(0);
                    tareas = null;
                }
            }
            JsfUtil.update("reasignarTraDlg");
            JsfUtil.update("formReasignarTra");
            JsfUtil.executeJS("PF('reasignarTraDlg').show()");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void reasignar(Usuarios usuario) {
        try {
            String obs = "TAREA: " + tarea.getName() + ", USUARIO ANTERIOR: " + tarea.getAssignee() + ", USUARIO ACTUAL: " + usuario.getUsuario();
            Observaciones observ = obsService.guardarObservaciones(this.instTramite.getTramite(), obs, "REASIGNACION DE USUARIO");
            if (observ != null) {
                if (baseEngine.setAssigneeTask(tarea.getId(), usuario.getUsuario())) {
                    Map<String, Object> v = this.baseEngine.getvariables(tarea.getProcessInstanceId());
                    v.entrySet().stream().filter((entrySet) -> (entrySet.getValue() != null && entrySet.getValue().equals(tarea.getAssignee()))).forEachOrdered((entrySet) -> {
                        baseEngine.setVariableProcessInstance(tarea.getProcessInstanceId(), entrySet.getKey(), usuario.getUsuario());
                    });
                }
                JsfUtil.update("taskGrid");
                JsfUtil.executeJS("PF('reasignarTraDlg').hide()");
                JsfUtil.addSuccessMessage("Reasignación", "Se ha reasignado la tarea Correctamente");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "reasignar", e);
        }
    }
    
    public void reasignar(Usuarios usuario, Task task) {
        try {
            String obs = "TAREA: " + task.getName() + ", USUARIO ANTERIOR: " + task.getAssignee() + ", USUARIO ACTUAL: " + usuario.getUsuario();
            Observaciones observ = obsService.guardarObservaciones(this.instTramite.getTramite(), obs, "REASIGNACION DE USUARIO");
            if (observ != null) {
                if (baseEngine.setAssigneeTask(task.getId(), usuario.getUsuario())) {
                    Map<String, Object> v = this.baseEngine.getvariables(task.getProcessInstanceId());
                    v.entrySet().stream().filter((entrySet) -> (entrySet.getValue() != null && entrySet.getValue().equals(task.getAssignee()))).forEachOrdered((entrySet) -> {
                        baseEngine.setVariableProcessInstance(task.getProcessInstanceId(), entrySet.getKey(), usuario.getUsuario());
                    });
                }
                JsfUtil.update("taskGrid");
                JsfUtil.executeJS("PF('reasignarTraDlg').hide()");
                JsfUtil.addSuccessMessage("Reasignación", "Se ha reasignado la tarea Correctamente");
            }
            this.usuario = null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "reasignar", e);
        }
    }
    
    public void deleteProcess(Tramites tramite) {
        try {
            this.instTramite = tramite;
            deleteReason = "";
            JsfUtil.update("formEliminarTra");
            JsfUtil.executeJS("PF('eliminarTraDlg').show()");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "deleteProcess", e);
        }
    }
    
    public void delete() {
        try {
            if (deleteReason.length() == 0) {
                JsfUtil.addErrorMessage("Error", "Debe ingresar la razon porque elimina el tramite.");
                return;
            }
            Observaciones obs = obsService.guardarObservaciones(instTramite.getTramite(), deleteReason, "Eliminación");
            if (obs != null) {
                baseEngine.deleteProcessInstance(instTramite.getProcInstId(), deleteReason);
                this.instTramite.getTramite().setFechaEntrega(new Date());
                this.tramiteService.edit(this.instTramite.getTramite());
                JsfUtil.executeJS("PF('eliminarTraDlg').hide()");
                JsfUtil.update("frmDetalleTarea");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "delete", e);
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al eliminar la instancia del procesos " + instTramite.getProcInstId());
        }
    }

    /**
     *
     * @param tipo 1 Inactivo, 2 Activo, 3 Finalizado
     */
    public void filterByEstado(int tipo) {
        try {
            lazy = new LazyModel<>(Tramites.class);
            lazy.getSorteds().put("numTramite", "DESC");
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            switch (tipo) {
                case 1:
                    lazy.getFilterss().put("deleteReason:notEqual", null);
                    break;
                case 2:
                    lazy.getFilterss().put("endTime:equal", null);
                    break;
                case 3:
                    lazy.getFilterss().put("endTime:notEqual", null);
                    lazy.getFilterss().put("deleteReason:equal", null);
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "finalizados", e);
        }
    }
    
    public void changePriority(Tramites tramite) {
        try {
            this.instTramite = tramite;
            JsfUtil.executeJS("PF('editPrioridadTraDlg').show()");
            JsfUtil.update("formEditPrioridadTra");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "changePriority", e);
        }
    }
    
    public void actualizarPrioridad() {
        try {
            baseEngine.setVariableProcessInstance(instTramite.getProcInstId(), "prioridad", prioridad);
            List<Task> tareasActivas = baseEngine.getListTaskActiveByProcessInstance(instTramite.getProcInstId());
            if (tareasActivas != null) {
                for (Task task : tareasActivas) {
                    baseEngine.setTaskPriority(task.getId(), prioridad);
                }
            }
            JsfUtil.update("taskGrid");
            JsfUtil.executeJS("PF('editPrioridadTraDlg').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "actualizarPrioridad", e);
        }
    }
    
    public void editarFechaInicio() {
        JsfUtil.update("formModificarFecha");
        JsfUtil.executeJS("PF('modificarFechaDlg').show()");
    }
    
    public void modificarFecha() {
        try {
            if (reasondatemodif.length() == 0) {
                JsfUtil.addErrorMessage("Error", "Debe ingresar la razón del porque Modifica la Fecha del tramite.");
                return;
            }
            Observaciones obs = obsService.guardarObservaciones(instTramite.getTramite(), reasondatemodif, "Modificación Fecha Trámite");
            if (obs != null) {
                this.tramiteService.edit(this.instTramite.getTramite());
                JsfUtil.executeJS("PF('modificarFechaDlg').hide()");
                JsfUtil.update("dlgTramite");
                JsfUtil.update("frmDetalleTarea");
                reasondatemodif = "";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Modificar fecha ", e);
        }
    }
    
    public LazyModel<Tramites> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<Tramites> lazy) {
        this.lazy = lazy;
    }
    
    public List<HistoricTaskInstance> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<HistoricTaskInstance> tasks) {
        this.tasks = tasks;
    }
    
    public Tramites getInstTramite() {
        return instTramite;
    }
    
    public void setInstTramite(Tramites instTramite) {
        this.instTramite = instTramite;
    }
    
    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }
    
    public Integer getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }
    
    public String getDeleteReason() {
        return deleteReason;
    }
    
    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }
    
    public List<Usuarios> getUsuarios() {
        return usuarios;
    }
    
    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }
    
    public Task getTarea() {
        return tarea;
    }
    
    public void setTarea(Task tarea) {
        this.tarea = tarea;
    }
    
    public List<Task> getTareas() {
        return tareas;
    }
    
    public void setTareas(List<Task> tareas) {
        this.tareas = tareas;
    }
    
    public Usuarios getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
    
    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }
    
    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }
    
    public ListarequisitosModel getRequisitosObjeto() {
        return requisitosObjeto;
    }
    
    public void setRequisitosObjeto(ListarequisitosModel requisitosObjeto) {
        this.requisitosObjeto = requisitosObjeto;
    }
    
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }
    
    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
    
    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }
    
    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
    
    public String getReasondatemodif() {
        return reasondatemodif;
    }
    
    public void setReasondatemodif(String reasondatemodif) {
        this.reasondatemodif = reasondatemodif;
    }
    
}
