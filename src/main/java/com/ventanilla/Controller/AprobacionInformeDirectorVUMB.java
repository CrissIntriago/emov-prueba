/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class AprobacionInformeDirectorVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private FileUploadDoc uploadDoc;
    protected String observaciones;
    private List<HistoricoTramites> historicoTramite;
    private SolicitudServicios solicitudServicios;
    private Observaciones ultimaObservacion;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;

    @PostConstruct
    public void initView() {

        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Map<String, Object> params = new HashMap<>();
                params.put("num_tramite", tramite.getNumTramite());
                solicitudServicios = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByTramiteId", new Object[]{tramite.getId()});
                historicoTramite = ventanillaService.findAllQuery("SELECT h FROM HistoricoTramites h where numTramite=:num_tramite", params);
                registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
                if (!Utils.isEmpty(tramite.getObservaciones())) {
                    ultimaObservacion = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1);
                }
            }
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            if (observaciones.isEmpty()) {
                JsfUtil.addWarningMessage("Aprobación Informe Director", "Ingrese una observación");
                return;
            }
            getParamts().put("usuario_8", session.getNameUser());
            observacion.setObservacion(observaciones);
            if (saveTramite() == null) {
                return;
            }
            if (file != null) {
                uploadDoc.upload(tramite, file);
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(null, "ERROR DE APLICACIÓN");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            FILE = Utils.copyFileServer(file, SisVars.rutaRepositorioArchivo);
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (IOException e) {
            JsfUtil.addErrorMessage(null, "Ocurrió un error al subir el archivo");
        }
    }

    public List<HistoricoTramites> getHistoricoTramite() {
        return historicoTramite;
    }

    public void setHistoricoTramite(List<HistoricoTramites> historicoTramite) {
        this.historicoTramite = historicoTramite;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public File getFILE() {
        return FILE;
    }

    public void setFILE(File FILE) {
        this.FILE = FILE;
    }

}
