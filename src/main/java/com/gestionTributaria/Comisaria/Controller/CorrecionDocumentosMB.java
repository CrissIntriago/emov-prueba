/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.asgard.Entity.AppDepartamentoDetalleTitulos_;
import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
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
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class CorrecionDocumentosMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private ProcesoService procesoService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ComisariaServices comisariaServices;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;
    private SolicitudServicios solicitud;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listRequisitos();
                solicitud = new SolicitudServicios();
                solicitud = comisariaServices.getSolcitud(tramite.getId());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = procesoService.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                requisitosTramite.add(req);
            }
        }
    }

    public void send() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
    }

    public void completarTarea() {
        try {

            getParamts().put("recepcion", clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia));
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void completarTareaOthers() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);

            getParamts().put("usuario_justicia", tramite.getObservaciones().get(1).getUserCre());
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void completarTareaReceptaDocumentos() {
        try {
            getParamts().put("directorJV", clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia));
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
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

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public FileUploadDoc getUploadDoc() {
        return uploadDoc;
    }

    public void setUploadDoc(FileUploadDoc uploadDoc) {
        this.uploadDoc = uploadDoc;
    }

    public TramiteRequisitoHistorialService getTramiteRequisitoHistorialService() {
        return tramiteRequisitoHistorialService;
    }

    public void setTramiteRequisitoHistorialService(TramiteRequisitoHistorialService tramiteRequisitoHistorialService) {
        this.tramiteRequisitoHistorialService = tramiteRequisitoHistorialService;
    }

    public ProcesoService getProcesoService() {
        return procesoService;
    }

    public void setProcesoService(ProcesoService procesoService) {
        this.procesoService = procesoService;
    }

    public ServletSession getServletSession() {
        return servletSession;
    }

    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
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
//</editor-fold>

}
