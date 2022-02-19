/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Rol;
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
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.presupuesto.procesos.services.ClienteServices;
import com.origami.sigef.resource.procesos.controllers.StartProcessController;
import com.origami.sigef.resource.procesos.models.TareasTesoreriaRentas;
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
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class AsignacionTareaMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(AsignacionTareaMB.class.getName());

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

    @Inject
    private ClienteServices clienteService;

    protected LazyModel<TareasTesoreriaRentas> lazy;
    private List<Tramites> tramitesList;
    private List<HistoricTaskInstance> tasks;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;
    private TareasTesoreriaRentas instTramite;
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
    private List<Cliente> clientes;

    @PostConstruct

    protected void iniView() {
        try {
            clientes = new ArrayList<>();
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

    public void verProceso(TareasTesoreriaRentas tramite) {
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
            System.out.println("emtra 1");
            String usuario = "";
            usuario = clienteService.getrolsUser(RolUsuario.jefeRentas);
            lazy = new LazyModel<>(TareasTesoreriaRentas.class);
            lazy.getSorteds().put("numTramite", "DESC");
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
//            lazy.getFilterss().put("usercre:or:participants;usercre;participants", this.us.getNameUser());
            if (usuario.equals(us.getNameUser())) {
                System.out.println("ENTRA 2");
//                lazy.getFilterss().put("descripcionTarea", "Generar Liquidación");
                cargarServidores();
            }
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

    public void reasignarProcess(TareasTesoreriaRentas tramite) {
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

    public void cargarServidores() {
        clientes = clienteService.findByServidoresDepartamento(48L);
        System.out.println("clientes: " + clientes);
    }

    public void clear() {
        numTramite = null;
        fechaIngreso = null;
        lazy = null;
    }

    public LazyModel<TareasTesoreriaRentas> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<TareasTesoreriaRentas> lazy) {
        this.lazy = lazy;
    }

    public List<Tramites> getTramitesList() {
        return tramitesList;
    }

    public void setTramitesList(List<Tramites> tramitesList) {
        this.tramitesList = tramitesList;
    }

    public List<HistoricTaskInstance> getTasks() {
        return tasks;
    }

    public void setTasks(List<HistoricTaskInstance> tasks) {
        this.tasks = tasks;
    }

    public ListarequisitosModel getRequisitosObjeto() {
        return requisitosObjeto;
    }

    public void setRequisitosObjeto(ListarequisitosModel requisitosObjeto) {
        this.requisitosObjeto = requisitosObjeto;
    }

    public TareasTesoreriaRentas getInstTramite() {
        return instTramite;
    }

    public void setInstTramite(TareasTesoreriaRentas instTramite) {
        this.instTramite = instTramite;
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

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

}
