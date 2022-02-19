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
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
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
public class AsignarTramiteVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    private List<HistoricoTramites> historicoTramite;
    protected String observaciones;
    private SolicitudServicios solicitudServicios;
    private List<UsuarioRol> usuariosRol;
    private UsuarioRol usuarioSeleccionado;
    private List<UnidadAdministrativa> departamentos;
    private UnidadAdministrativa departamento;

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
                loadModel();
            }
        }
    }

    private void loadModel() {
        departamentos = ventanillaService.findAllQuery("SELECT ua FROM UnidadAdministrativa ua where estado= true ORDER BY nombre", null);
        departamento = new UnidadAdministrativa();
        usuarioSeleccionado = new UsuarioRol();
        if (!Utils.isEmpty(session.getUserRoles())) {
            UsuarioRol ur = session.getUserRoles().get(0);
            departamento = ur.getRol().getUnidadAdministrativa();
            usuariosRol = ventanillaService.findAllUsuariosRolByDepartamento(departamento, null, null, null, null);
        }
    }

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new UsuarioRol();
            usuariosRol = ventanillaService.findAllUsuariosRolByDepartamento(departamento, null, null, null, null);
        }
    }

    /*Método para abrir el dialogo de observación */
    public void abriDialog() {
        if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
            JsfUtil.addErrorMessage("", "Debe seleccionar un usuario");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    /*Método para abrir el dialogo de detalles */
    public void openDialogDetalles(HistoricoTramites item) {
        PrimeFaces.current().executeScript("PF('dlgDet').show()");
        PrimeFaces.current().ajax().update("frmDet");
    }

    public void completarTarea() {
        try {
            if (validar()) {
                getParamts().put("usuario_2", usuarioSeleccionado.getUsuario().getUsuario());
                observacion.setObservacion(observaciones);
                if (saveTramite() == null) {
                    return;
                }
                usuarioSeleccionado = new UsuarioRol();
                super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    private Boolean validar() {
        if (Utils.isEmptyString(observaciones)) {
            JsfUtil.addErrorMessage("", "Ingrese una observación");
            return Boolean.FALSE;
        }
        if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
            JsfUtil.addErrorMessage("", "Escoja un usuario");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public UsuarioRol getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioRol usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public List<UsuarioRol> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<UsuarioRol> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
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
//</editor-fold>
}
