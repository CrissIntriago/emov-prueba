/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
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
public class GenerarInformeInspeccionVUMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private FileUploadDoc uploadDoc;
    protected String observaciones;
    private List<HistoricoTramites> historicoTramite;
    private SolicitudServicios solicitudServicios;
    private List<UsuarioRol> usuariosRol;
    private UsuarioRol usuarioSeleccionado;
    private List<UnidadAdministrativa> departamentos;
    private UnidadAdministrativa departamento;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private Boolean asignar;
    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;
    private Observaciones ultimaObservacion;
    private int aprueba;

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                Map<String, Object> params = new HashMap<>();
                params.put("num_tramite", tramite.getNumTramite());
                solicitudServicios = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByTramiteId", new Object[]{tramite.getId()});
                historicoTramite = ventanillaService.findAllQuery("SELECT h FROM HistoricoTramites h where numTramite=:num_tramite", params);
                registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
                asignar = Boolean.FALSE;
                if (!Utils.isEmpty(tramite.getObservaciones())) {
                    ultimaObservacion = tramite.getObservaciones().get(tramite.getObservaciones().size() - 1);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abriDlogo(int tipo) {
        if (file == null) {
            JsfUtil.addErrorMessage("", "Primero ingrese un documento");
            return;
        }
        aprueba = tipo;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        switch (aprueba) {
            case 0:
                JsfUtil.executeJS("PF('dlgAsignarAtender').show()");
                JsfUtil.update("frmDlgAsignarAtender");
                break;
            case 1:
                PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
                PrimeFaces.current().ajax().update("frmDlgObser");
                break;
        }
    }

    public void completarTarea() {
        try {
            if (validar()) {
                switch (aprueba) {
                    case 0:
                        /*Generar Liquidación */
                        getParamts().put("usuario_5", (asignar && usuarioSeleccionado != null && usuarioSeleccionado.getId() != null)
                                ? usuarioSeleccionado.getUsuario().getUsuario() : session.getNameUser());
                        getParamts().put("liquidacion", 0);
                        break;
                    case 1:
                        List<UsuarioRol> users = ventanillaService.findAllUsuariosRolByDepartamento(solicitudServicios.getServicioTipo().getServicio().getDepartamento(), null, null, Boolean.TRUE, null);
                        String us = "";
                        if (!Utils.isEmpty(users)) {
                            us = users.get(0).getUsuario().getUsuario();
                        }
                        /*Aprobación Jefe*/
                        getParamts().put("usuario_6", !Utils.isEmptyString(us) ? us : session.getNameUser());
                        getParamts().put("liquidacion", 1);
                        break;
                }
                observacion.setObservacion(observaciones);
                if (saveTramite() == null) {
                    return;
                }
                uploadDoc.upload(tramite, file);
                super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(null, "ERROR DE APLICACIÓN");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private Boolean validar() {
        if (Utils.isEmptyString(observaciones)) {
            JsfUtil.addWarningMessage("Genera Informe de Inspección", "Ingrese una observación");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
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

    public void loadUsuariosDepartamento() {
        if (departamento != null && departamento.getId() != null) {
            usuarioSeleccionado = new UsuarioRol();
            usuariosRol = ventanillaService.findAllUsuariosRolByDepartamento(departamento, null, null, null, null);
        }
    }

    public void asignarAtender(Boolean a) {
        asignar = a;
        if (asignar) {
            departamentos = ventanillaService.findAllQuery("SELECT ua FROM UnidadAdministrativa ua where estado= true ORDER BY nombre", null);
            departamento = new UnidadAdministrativa();
            usuarioSeleccionado = new UsuarioRol();
            PrimeFaces.current().executeScript("PF('dlgAsignar').show()");
            PrimeFaces.current().ajax().update("frmDlgAsignar");
        } else {
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update("frmDlgObser");
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
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

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public Boolean getAsignar() {
        return asignar;
    }

    public void setAsignar(Boolean asignar) {
        this.asignar = asignar;
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

    public List<UsuarioRol> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<UsuarioRol> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

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

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }
//</editor-fold>
}
