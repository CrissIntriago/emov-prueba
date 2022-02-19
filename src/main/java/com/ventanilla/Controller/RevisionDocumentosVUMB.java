/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudDocumento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
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

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class RevisionDocumentosVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private List<SolicitudDocumento> solicitudDocumentos;
    private SolicitudServicios solicitudServicios;
    private String observaciones;
    private int tipo;
    private Boolean asignar;
    private List<UnidadAdministrativa> departamentos;
    private UnidadAdministrativa departamento;
    private UsuarioRol usuarioSeleccionado;
    private List<UsuarioRol> usuariosRol;
    private Observaciones ultimaObservacion;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
//                procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());
                Map<String, Object> params = new HashMap<>();
                params.put("id_tramite", tramite.getTipoTramite().getId());
                solicitudServicios = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByTramiteId", new Object[]{tramite.getId()});
                registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
                asignar = Boolean.FALSE;
                if (!Utils.isEmpty(tramite.getObservaciones())) {
                    ultimaObservacion = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1);
                }
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abriDlogo(int t) {
        tipo = t;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
//        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
//        PrimeFaces.current().ajax().update(":frmDlgObser");
        PrimeFaces.current().executeScript("PF('dlgAsignarAtender').show()");
        PrimeFaces.current().ajax().update("frmDlgAsignarAtender");
    }

    public void completarTarea() {
        try {
            if (Utils.isEmptyString(observaciones)) {
                JsfUtil.addWarningMessage("Revisión Documentos", "Ingrese una observación");
                return;
            }
            if (asignar) {
                if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
                    JsfUtil.addWarningMessage("", "Escoja un usuario");
                    return;
                }
            }
            switch (tipo) {
                case 0:
                    /*Rechaza y notifica las observaciones  */
                    getParamts().put("usuario_3", session.getNameUser());
                    getParamts().put("aprobado", 0);
                    break;
                case 1:
                    /*Genera liquidación*/
                    getParamts().put("usuario_5", asignar ? usuarioSeleccionado.getUsuario().getUsuario() : session.getNameUser());
                    getParamts().put("aprobado", 2);
                    break;
                case 2:
                    /*Genera informe*/
                    getParamts().put("usuario_4", asignar ? usuarioSeleccionado.getUsuario().getUsuario() : session.getNameUser());
                    getParamts().put("aprobado", 1);
                    break;
            }
            observacion.setObservacion(observaciones);
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(null, "ERROR DE APLICACIÓN");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void asignarAtender(Boolean a) {
        String script, update;
        asignar = a;
        if (asignar) {
            departamentos = ventanillaService.findAllQuery("SELECT ua FROM UnidadAdministrativa ua where estado= true ORDER BY nombre", null);
            departamento = new UnidadAdministrativa();
            usuarioSeleccionado = new UsuarioRol();
            script = "PF('dlgAsignar').show()";
            update = "frmDlgAsignar";
        } else {
            script = "PF('dlgObservaciones').show()";
            update = "frmDlgObser";
        }
        PrimeFaces.current().executeScript(script);
        PrimeFaces.current().ajax().update(update);
    }

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new UsuarioRol();
            usuariosRol = ventanillaService.findAllUsuariosRolByDepartamento(departamento, null, null, null, null);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public UsuarioRol getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioRol usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<UsuarioRol> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<UsuarioRol> usuariosRol) {
        this.usuariosRol = usuariosRol;
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
