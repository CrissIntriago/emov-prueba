/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.SecuenciaGeneralService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.Notificacion;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudDocumento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.math.BigInteger;
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
public class NotificacionObservacionesVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private SecuenciaGeneralService secuenciaService;
    protected String observaciones;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private List<RegistroSolicitudRequisitos> registroRequisitosErrores;
    private List<SolicitudDocumento> solicitudDocumentos;
    private SolicitudServicios solicitudServicios;
    private Notificacion notificacion;
    private Observaciones ultimaObservacion;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                if (!JsfUtil.isAjaxRequest()) {
                    this.setTaskId(this.session.getTaskID());
                    observacion = new Observaciones();
                    observacion.setIdTramite(tramite);
                    Map<String, Object> params = new HashMap<>();
                    params.put("id_tramite", tramite.getTipoTramite().getId());
                    solicitudServicios = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByTramiteId", new Object[]{tramite.getId()});
                    registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
                    if (!Utils.isEmpty(tramite.getObservaciones())) {
                        ultimaObservacion = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1);
                    }
                    loadModel();
                }
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void loadModel() {
        notificacion = new Notificacion();
        notificacion.setTitulo(this.tarea.getName());
        notificacion.setSubtitulo("De la calificación realizada en virtud de los artìculos 11 numerales 1 y 5 y el artículo 12 del"
                + "Reglamento de la Ley Orgánica del Sistema Nacional del Registro de datos Públicos,"
                + "existen algunas observaciones que se la describe a continuación:");
        registroRequisitosErrores = new ArrayList<>();
    }

    public void completarTarea() {
        try {
            guardarRequisitosErrores();
            solicitudServicios.setEnObservacion(Boolean.TRUE);
            getParamts().put("usuario_2", session.getNameUser());
            observacion.setObservacion(observaciones);
            if (saveTramite() == null) {
                return;
            }
            enviarCorreo("Notificación de Observaciones");
            ventanillaService.save(solicitudServicios);
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public void abriDlogo() {
        if (validar()) {
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        }
    }

    private Boolean validar() {
        if (Utils.isEmpty(registroRequisitosErrores)) {
            JsfUtil.addErrorMessage("", "No existe ningún requisito con errores");
            return Boolean.FALSE;
        }
        if (Utils.isEmptyString(notificacion.getContenido())) {
            JsfUtil.addErrorMessage("", "ingrese el contenido de la notificación");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void guardarRequisitosErrores() {
        for (RegistroSolicitudRequisitos rs : registroRequisitosErrores) {
            rs.setCorreccion(Boolean.TRUE);
            rs.setFechaModifica(new Date());
            rs.setUsuarioModifica(session.getNameUser());
            ventanillaService.save(rs);
        }
        SecuenciaGeneral sg = secuenciaService.findByCodigoAndAnio("SECUENCIA_NOTIFICACION", Utils.getAnio(new Date()));
        notificacion.setSecuencia(sg.getSecuencia());
        notificacion.setAnio(BigInteger.valueOf(Utils.getAnio(new Date())));
        notificacion.setCodigo(solicitudServicios.getServicioTipo().getServicio().getAbreviatura() + "-" + notificacion.getSecuencia() + "-" + notificacion.getAnio());
        notificacion.setFecha(new Date());
        notificacion.setRevisada(Boolean.FALSE);
        notificacion.setSolicitudServicio(solicitudServicios);
        notificacion.setUsuarioIngreso(session.getNameUser());
        ventanillaService.save(notificacion);
        sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
        secuenciaService.edit(sg);
    }

    private void enviarCorreo(String asunto) {
        Correo c = new Correo();
        c.setDestinatario(solicitudServicios.getEnteSolicitante().getEmail());
        c.setAsunto(asunto);
        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getCodigo() + " - " + tramite.getTipoTramite().getDescripcion(),
                "Estimado(a) " + solicitudServicios.getEnteSolicitante().getNombreCompleto() + " se encontraron las siguientes observaciones "
                + "en la documentación adjuntada: " + notificacion.getContenido(),
                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
        this.correoService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitosErrores() {
        return registroRequisitosErrores;
    }

    public void setRegistroRequisitosErrores(List<RegistroSolicitudRequisitos> registroRequisitosErrores) {
        this.registroRequisitosErrores = registroRequisitosErrores;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public List<SolicitudDocumento> getSolicitudDocumentos() {
        return solicitudDocumentos;
    }

    public void setSolicitudDocumentos(List<SolicitudDocumento> solicitudDocumentos) {
        this.solicitudDocumentos = solicitudDocumentos;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }
//</editor-fold>
}
