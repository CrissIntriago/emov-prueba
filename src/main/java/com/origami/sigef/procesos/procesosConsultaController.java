/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos;

import com.origami.sigef.resource.procesos.controllers.StartProcessController;
import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
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
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.models.Tramites;
import com.origami.sigef.resource.procesos.services.HistoricoTramiteService;
import com.origami.sigef.resource.procesos.services.ObservacionesService;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "consultarProcesosView")
@ViewScoped
public class procesosConsultaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TareasAdminView.class.getName());

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
    private List<Tramites> tramitesList;
    private List<HistoricTaskInstance> tasks;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;
    private Tramites instTramite;
    private StreamedContent imageProcessInstance;
    private Integer prioridad = 50;
    private String deleteReason;
    private List<Usuarios> usuarios;
    private Task tarea;
    private List<Task> tareas;
    private Usuarios usuario;
    private Long numTramite;
    private Date fechaIngreso;
    private String userName;
    private OpcionBusqueda opcionBusqueda;
//    private List<MasterCatalogo> listaPeriodo;
    private List<Short> listaPeriodo;

    @PostConstruct
    protected void iniView() {
        try {
            opcionBusqueda = new OpcionBusqueda();
            userName = us.getNameUser();
//            this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
            this.listaPeriodo = tramiteService.findPeriodoDisponible();
            if (listaPeriodo != null) {
                int indice = listaPeriodo.size();
                if (!listaPeriodo.isEmpty() && indice == 1) {
                    opcionBusqueda.setAnio(listaPeriodo.get(0).shortValue());
                } else {
                    opcionBusqueda.setAnio(listaPeriodo.get(indice - 1).shortValue());
                }
            }
            tramitesList = new ArrayList<>();
            filterByEstado(-1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "finalizados", e);
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

    /**
     *
     * @param tipo 1 Inactivo, 2 Activo, 3 Finalizado
     */
    public void filterByEstado(int tipo) {
        try {
            lazy = new LazyModel<>(Tramites.class);
            lazy.getSorteds().put("numTramite", "DESC");
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            lazy.getFilterss().put("usercre:or:participants;usercre;participants", this.us.getNameUser());
            if (tipo > -1) {
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
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "finalizados", e);
        }
    }

    public void clear() {
        numTramite = null;
        fechaIngreso = null;
        lazy = null;
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

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<Tramites> getTramitesList() {
        return tramitesList;
    }

    public void setTramitesList(List<Tramites> tramitesList) {
        this.tramitesList = tramitesList;
    }

}
