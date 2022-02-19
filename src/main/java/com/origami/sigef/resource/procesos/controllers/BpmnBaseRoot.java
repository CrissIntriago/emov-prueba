/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.controllers;

import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.models.CorreoArchivo;
import com.origami.sigef.common.models.Documento;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.service.interfaces.DatabaseLocal;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.Tramites;
import com.origami.sigef.resource.procesos.services.HistoricoTramiteService;
import com.origami.sigef.resource.procesos.services.ObservacionesService;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 *
 * @author ANGEL NAVARRO
 */
public abstract class BpmnBaseRoot implements Serializable {

    public static final Logger LOG = Logger.getLogger(BpmnBaseRoot.class.getName());

    @Inject
    protected BpmBaseEngine engine;
    @Inject
    protected UserSession session;
    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private ManagerService service;
    @Inject
    protected KatalinaService correoService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private DatabaseLocal conexionPool;
    private List<Documento> files;
    private List<Task> tareas;
    private String taskId;
    private Map<String, Object> paramts;
    protected HistoricoTramites tramite;
    protected Observaciones observacion;
    protected SolicitudServicios solicitud;
    protected Task tarea;
    @Inject
    protected HistoricoTramiteService tramiteService;
    @Inject
    protected ObservacionesService obsService;
    @Inject
    protected SeqGenMan secuencia;

    public ProcessEngine getProcessEngine() {
        return engine.getProcessEngine();
    }

    public Task getTarea(String id) {
        return engine.getTaskDataByTaskID(id);
    }

    public List<Task> obtenerTareasUsuarios(String usuario) {
        try {
            return this.engine.getUsertasksList(usuario, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Debe setear el {@link BpmnBaseRoot#setTaskId(java.lang.String) } para
     * poder completar la tarea
     *
     * @param parameters Parametro a pasar al proceso
     */
    public void completeTask(HashMap<String, Object> parameters) {
        try {
            this.session.setVarTemp(null);
            engine.completeTask(taskId, parameters);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void completeTask(String taskid, HashMap<String, Object> parameters) {
        try {
            this.session.setVarTemp(null);
            engine.completeTask(taskid, parameters);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void continuar() {
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public HistoricoTramites saveTramite() throws Exception {
        if (tramite == null) {
            throw new NullPointerException("Tramite es nullo, no se puede iniciar tramite.");
        }
        if (tramite.getId() == null) {
            tramite.setFechaIngreso(new Date());
            ProcessInstance p = null;
            try {
                System.out.println("iniciando el tramite " + tramite.getTipoTramite().getActivitykey());
                p = engine.startProcessByDefinitionKey(tramite.getTipoTramite().getActivitykey(), (HashMap<String, Object>) paramts);
            } catch (Exception exception) {
                System.out.println("exception savetramtie " + exception.getMessage());
                LOG.log(Level.SEVERE, tramite.getTipoTramite().getDescripcion(), exception);
                return null;
            }
            if (p != null) {
                tramite.setNumTramite(secuencia.getSecuencia("NUMERO_TRAMITE").longValue());
                tramite.setIdProceso(p.getId());
                if (tramite.getServicio() != null) {
                    tramite.setCodigo(tramite.getServicio().getAbreviatura() + "-" + tramite.getNumTramite() + "-" + tramite.getPeriodo());
                }
                return tramiteService.create(tramite);
            } else {
                return null;
            }
        } else {
            if (observacion == null) {
                JsfUtil.addErrorMessage("Observacion", "debe ingresar las Observaciones");
                return null;
            }
            if (observacion.getObservacion() == null) {
                JsfUtil.addErrorMessage("Observación de trámite", "debe ingresar las Observaciones");
                return null;
            }
            tramiteService.edit(tramite);
            saveObs();
            return tramite;
        }
    }

    public HistoricoTramites saveTramiteVentanilla() throws Exception {
        if (tramite == null) {
            throw new NullPointerException("Tramite es nullo, no se puede iniciar tramite.");
        }
        if (tramite.getId() == null) {
            tramite.setFechaIngreso(new Date());
            return tramiteService.create(tramite);
        } else {
            if (observacion == null) {
                JsfUtil.addErrorMessage("Observacion", "debe ingresar las Observaciones");
                return null;
            }
            if (observacion.getObservacion() == null) {
                JsfUtil.addErrorMessage("Observación de trámite", "debe ingresar las Observaciones");
                return null;
            }
            tramiteService.edit(tramite);
            saveObs();
            return tramite;
        }
    }

    public HistoricoTramites saveTramite(HistoricoTramites tramite) throws Exception {
        if (tramite == null) {
            throw new NullPointerException("Tramite es nullo, no se puede iniciar tramite.");
        }
        if (tramite.getId() == null) {
            tramite.setFechaIngreso(new Date());
            ProcessInstance p = null;
            try {
                p = engine.startProcessByDefinitionKey(tramite.getTipoTramite().getActivitykey(), (HashMap<String, Object>) paramts);
            } catch (Exception exception) {
                LOG.log(Level.SEVERE, tramite.getTipoTramite().getDescripcion(), exception);
                return null;
            }
            if (p != null) {
                tramite.setNumTramite(secuencia.getSecuencia("NUMERO_TRAMITE").longValue());
                tramite.setIdProceso(p.getId());
                return tramiteService.create(tramite);
            } else {
                return null;
            }
        } else {
            if (observacion == null) {
                JsfUtil.addErrorMessage("Observacion", "debe ingresar las Observaciones");
                return null;
            }
            if (observacion.getObservacion() == null) {
                JsfUtil.addErrorMessage("Observación de trámite", "debe ingresar las Observaciones");
                return null;
            }
            tramiteService.edit(tramite);
            saveObs();
            return tramite;
        }
    }

    public void enviarCorreo() throws IOException {
        SolicitudServicios solicitudTemp = getSolicitud();
        Correo correo = new Correo();
        correo.setFechaEnvio(new Date());

        List<Documento> docuemntos = files(tramite);
        List<CorreoArchivo> listaCorreo = new ArrayList<>();
        if (docuemntos != null && !docuemntos.isEmpty()) {
            for (Documento doc : docuemntos) {
                CorreoArchivo docEnviar = new CorreoArchivo();
                String rutaTrmp = SisVars.RUTA_DOCS_TEMP + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                        + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + doc.getArchivoNombre();
                viewDocumento(doc.getBlobData(), rutaTrmp);
                docEnviar.setNombreArchivo(rutaTrmp);
                docEnviar.setTipoArchivo(doc.getExtension());
                listaCorreo.add(docEnviar);
            }
        }
        if (listaCorreo != null && !listaCorreo.isEmpty()) {
            correo.setArchivos(listaCorreo);
        }
        correo.setTramite(true);
        String nombresSolicitante = solicitud.getEnteSolicitante().getNombreCompleto();
        correo.setAsunto("NO.TRÁMITE " + tramite.getNumTramite() + " - " + tramite.getTipoTramite().getDescripcion().toUpperCase());
        correo.setMensaje("SR(A) " + nombresSolicitante + " SE LE COMUNICA QUE SU TRÁMITE HA FINALIZADO. A CONTINUACIÓN SE ADJUNTA LOS DOCUMENTOS QUE FUERON PARTE DE SU TRÁMITE");
        if (solicitudTemp != null && solicitudTemp.getEnteSolicitante() != null && solicitudTemp.getEnteSolicitante().getEmail() != null) {
            correo.setDestinatario(solicitudTemp.getEnteSolicitante().getEmail());
            correoService.enviarCorreo(correo);
        }
        //      if (solicitudTemp != null && solicitudTemp.getEnteSolicitante() != null && solicitudTemp.getEnteSolicitante().getEmail() != null) {
        //correo.setDestinatario("luissuarez2t@gmail.com");

        //   }
    }

    public void enviarCorreoPersonalizado(SolicitudServicios soli, HistoricoTramites tmp) throws IOException {
        SolicitudServicios solicitudTemp = soli;
        Correo correo = new Correo();
        correo.setFechaEnvio(new Date());

        List<Documento> docuemntos = files(tmp);
        List<CorreoArchivo> listaCorreo = new ArrayList<>();
        if (docuemntos != null && !docuemntos.isEmpty()) {
            for (Documento doc : docuemntos) {
                CorreoArchivo docEnviar = new CorreoArchivo();
                String rutaTrmp = SisVars.RUTA_DOCS_TEMP + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                        + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + doc.getArchivoNombre();
                viewDocumento(doc.getBlobData(), rutaTrmp);
                docEnviar.setNombreArchivo(rutaTrmp);
                docEnviar.setTipoArchivo(doc.getExtension());
                listaCorreo.add(docEnviar);
            }
        }
        if (listaCorreo != null && !listaCorreo.isEmpty()) {
            correo.setArchivos(listaCorreo);
        }
        correo.setTramite(true);
        String nombresSolicitante = solicitudTemp.getEnteSolicitante().getNombreCompleto();
        correo.setAsunto("NO.TRÁMITE " + tmp.getNumTramite() + " - " + tmp.getTipoTramite().getDescripcion().toUpperCase());
        correo.setMensaje("SR(A) " + nombresSolicitante + " SE LE COMUNICA QUE SU TRÁMITE HA FINALIZADO. A CONTINUACIÓN SE ADJUNTA LOS DOCUMENTOS QUE FUERON PARTE DE SU TRÁMITE");
        if (solicitudTemp != null && solicitudTemp.getEnteSolicitante() != null && solicitudTemp.getEnteSolicitante().getEmail() != null) {
            correo.setDestinatario(solicitudTemp.getEnteSolicitante().getEmail());
            correoService.enviarCorreo(correo);
        }
        //      if (solicitudTemp != null && solicitudTemp.getEnteSolicitante() != null && solicitudTemp.getEnteSolicitante().getEmail() != null) {
        //correo.setDestinatario("luissuarez2t@gmail.com");

        //   }
    }

    public InputStream viewDocumento(Long oid, String ruta) throws IOException {
        LargeObjectManager lobj;
        LargeObject obj = null;
        Connection conn = conexionPool.getConnection(null);
        InputStream buffer = null;
        try {
            if (conn == null) {

                return null;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, null, e);
            }
            buffer = obj.getInputStream();
            File file = new File(ruta);
            FileUtils.copyInputStreamToFile(buffer, file);
            obj.close();
            conn.commit();
        } catch (SQLException ex) {

            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {

                    conn.close();
                }
            } catch (SQLException ex) {

                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return buffer;
    }

    public List<Documento> files(Object obj) {
        if (obj == null) {
            return null;
        }
        Object id = ReflexionEntity.getIdFromEntity(obj);
        if (id == null) {
            return null;
        }
        files = uploadDoc.fileEntiti(obj);
        return files;
    }

    //@Transactional(Transactional.TxType.REQUIRED)
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveObs() {
        try {
            observacion = obsService.guardarObservaciones(tramite, observacion.getObservacion(), observacion.getTarea());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, taskId, e);
        }
    }

    public BpmBaseEngine getEngine() {
        return engine;
    }

    public void setEngine(BpmBaseEngine engine) {
        this.engine = engine;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public String getTaskId() {
        return taskId;
    }

    public void completarTareaAisladas(Long tram, String usuario) throws Exception {

//        Thread data = new Thread() {
//            @Override
//            public void run() {
        tramite = tramiteService.find(tram);
        Map<String, Object> param = new HashMap<>();
        param.put("tramite", tramite);
        Tramites tra = service.findByParameter(Tramites.class, param);
        if (tra != null) {
            if (tra.getCallProcInstId() != null) {
                tareas = engine.allTaskDataByProcessID(tra.getCallProcInstId());
            } else {
                tareas = engine.allTaskDataByProcessID(tra.getProcInstId());
            }

            if (Utils.isNotEmpty(tareas)) {

                tarea = tareas.get(0);
                tareas = null;
                this.taskId = tarea.getId();
            }

        }

        if (tramite != null) {

            if (tra != null) {
                reasignarProcess(tra, usuario);
            }
        }

        observacion = new Observaciones();
        observacion.setIdTramite(tramite);
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea("RECAUDACIONES");
        observacion.setUserCre(usuario);
        observacion.setObservacion("SIN NOVEDAD");
        try {
            if (saveTramite() == null) {

                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(BpmnBaseRoot.class.getName()).log(Level.SEVERE, null, ex);
        }

        SolicitudServicios sol = new SolicitudServicios();
        sol = comisariaServices.getSolcitud(tramite.getId());
        try {

            enviarCorreoPersonalizado(sol, tramite);
        } catch (IOException ex) {
            Logger.getLogger(BpmnBaseRoot.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setVarTemp(null);

        this.completeTask((HashMap<String, Object>) getParamts());

    }

    public void reasignarProcess(Tramites tramite, String usuario) {
        try {
            tareas = new ArrayList<>();
            if (tramite.getCallProcInstId() != null) {
                tareas = engine.allTaskDataByProcessID(tramite.getCallProcInstId());
            } else {
                tareas = engine.allTaskDataByProcessID(tramite.getProcInstId());
            }
            if (Utils.isNotEmpty(tareas)) {

                tarea = tareas.get(0);
                tareas = null;

            }
            reasignar(usuario, tramite);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reasignar(String usuario, Tramites tra) {
        try {
            String obs = "TAREA: " + tarea.getName() + ", USUARIO ANTERIOR: " + tarea.getAssignee() + ", USUARIO ACTUAL: " + usuario;
            Observaciones observ = obsService.guardarObservaciones(tra.getTramite(), obs, "REASIGNACION DE USUARIO");
            if (observ != null) {
                if (engine.setAssigneeTask(tarea.getId(), usuario)) {
                    Map<String, Object> v = this.engine.getvariables(tarea.getProcessInstanceId());
                    v.entrySet().stream().filter((entrySet) -> (entrySet.getValue() != null && entrySet.getValue().equals(tarea.getAssignee()))).forEachOrdered((entrySet) -> {
                        engine.setVariableProcessInstance(tarea.getProcessInstanceId(), entrySet.getKey(), usuario);
                    });
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "reasignar", e);
        }
    }

    /**
     * Busca la tarea y el historico tramites asociado a ese proceso.
     *
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
        try {
            tarea = this.getTarea(taskId);
            if (tarea != null) {
                ProcessInstance inst = this.engine.getProcessInstanceById(tarea.getProcessInstanceId());
                if (inst.getRootProcessInstanceId() != null) {
                    tramite = this.tramiteService.findByProcessId(inst.getRootProcessInstanceId());
                } else {
                    tramite = this.tramiteService.findByProcessId(tarea.getProcessInstanceId());
                }
                if (tramite != null) {
                    this.tramite.setNombreBeneficiario(tramiteService.findReferencia(this.tramite));
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, taskId, e);
        }
    }

    public Map<String, Object> getParamts() {
        if (paramts == null) {
            paramts = new HashMap<>();
        }
        return paramts;
    }

    public void setParamts(Map<String, Object> paramts) {
        this.paramts = paramts;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public HistoricoTramiteService getTramiteService() {
        return tramiteService;
    }

    public void setTramiteService(HistoricoTramiteService tramiteService) {
        this.tramiteService = tramiteService;
    }

    public Task getTarea() {
        return tarea;
    }

    public void setTarea(Task tarea) {
        this.tarea = tarea;
    }

    public Observaciones getObservacion() {
        return observacion;
    }

    public void setObservacion(Observaciones observacion) {
        this.observacion = observacion;
    }

    public Object getVariable(String varName) {
        return getEngine().getVariableByProcessInstance(tarea.getProcessInstanceId(), varName);
    }

    public SolicitudServicios getSolicitud() {
        solicitud = new SolicitudServicios();
        solicitud = comisariaServices.getSolcitud(tramite.getId());
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public KatalinaService getCorreoService() {
        return correoService;
    }

    public void setCorreoService(KatalinaService correoService) {
        this.correoService = correoService;
    }

}
