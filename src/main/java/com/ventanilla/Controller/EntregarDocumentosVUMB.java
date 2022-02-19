/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.models.CorreoArchivo;
import com.origami.sigef.common.models.Documento;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.Notificacion;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.ArrayList;
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

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class EntregarDocumentosVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    private FinaRenLiquidacion liquidacion;
    protected String observaciones;
    private List<HistoricoTramites> historicoTramite;
    private SolicitudServicios solicitudServicios;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private Observaciones ultimaObservacion;
    private Notificacion notificacion;

    private List<Documento> files;
    private List<Documento> filesSend;

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
                //tiene un pago pendiente el usuario
                liquidacion = new FinaRenLiquidacion();
                if (solicitudServicios != null && solicitudServicios.getPendientePago()
                        && !Utils.isEmptyString(solicitudServicios.getReferenciaLiquidacionPago())) {
                    liquidacion = liquidacionService.getFinaRenLiquidacionByIdLiquidacion(solicitudServicios.getReferenciaLiquidacionPago());
                }
                loadModel();
                files();
            }
        }
    }

    private void loadModel() {
        files = new ArrayList<>();
        notificacion = new Notificacion();
        notificacion.setTitulo(this.tarea.getName());
        notificacion.setContenido("Estimad@ " + solicitudServicios.getEnteSolicitante().getNombreCompleto());
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
            if (validar()) {
                enviarCorreo(notificacion.getTitulo());
                //actualizar solicitud
                solicitudServicios.setFinalizado(Boolean.TRUE);
                ventanillaService.save(solicitudServicios);
                observacion.setObservacion(observaciones);
                if (saveTramite() == null) {
                    return;
                }
                super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    private Boolean validar() {
        if (Utils.isEmptyString(notificacion.getContenido())) {
            JsfUtil.addErrorMessage("", "Ingrese el contenido de la notificación");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void enviarCorreo(String asunto) {
        Correo c = new Correo();
        List<CorreoArchivo> archivos = new ArrayList<>();
        if (!Utils.isEmpty(filesSend)) {
            for (Documento d : filesSend) {
                CorreoArchivo correoArchivo = new CorreoArchivo();
                correoArchivo.setNombreArchivo(d.getArchivoNombre());
                System.out.println("PathPDF: " + d.getArchivoNombre());
                correoArchivo.setTipoArchivo("pdf");
                correoArchivo.setArchivoBase64("");
                archivos.add(correoArchivo);
            }
            c.setArchivos(archivos);
        }
        c.setDestinatario(solicitudServicios.getEnteSolicitante().getEmail());
        c.setAsunto(asunto);
        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getCodigo() + " - " + tramite.getTipoTramite().getDescripcion(),
                notificacion.getContenido(),
                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
        correoService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito");
    }

    public void files() {
        if (tramite == null) {
            return;
        }
        Object id = ReflexionEntity.getIdFromEntity(tramite);
        if (id == null) {
            return;
        }
        files = uploadDoc.fileEntiti(tramite);
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<Documento> getFilesSend() {
        return filesSend;
    }

    public void setFilesSend(List<Documento> filesSend) {
        this.filesSend = filesSend;
    }

    public List<Documento> getFiles() {
        return files;
    }

    public void setFiles(List<Documento> files) {
        this.files = files;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
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

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }
//</editor-fold>
}
