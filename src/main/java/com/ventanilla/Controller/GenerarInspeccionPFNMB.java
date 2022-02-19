/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Comisaria.Service.OrdernesService;
import com.gestionTributaria.Entities.Ordenes;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "generarInspeccionPFNMB")
@ViewScoped
public class GenerarInspeccionPFNMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private OrdernesService ordenesService;
    private UploadedFile file;
    private Ordenes orden;
    private String observacionInspeccion;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                orden = new Ordenes();
                JsfUtil.addInformationMessage("Información", "Adjuntar Informe de Inspección");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }

    public void opendlg() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
            getParamts().put("usuarioComisariaInforme", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void verDetalleInspeccion() {
        orden = ordenesService.findByOrden(this.tramite.getNumTramite());
        if (orden == null) {
            orden = new Ordenes();
        } else {
            observacionInspeccion = orden.getObservacion();
        }
    }

    public void guardarObservacion() {
        orden = ordenesService.findByOrden(tramite.getNumTramite());
        orden.setObservacion(observacionInspeccion);
        ordenesService.edit(orden);
        JsfUtil.addInformationMessage("","Se guardó la observación con éxto");
    }

    public Ordenes getOrden() {
        return orden;
    }

    public void setOrden(Ordenes orden) {
        this.orden = orden;
    }

    public String getObservacionInspeccion() {
        return observacionInspeccion;
    }

    public void setObservacionInspeccion(String observacionInspeccion) {
        this.observacionInspeccion = observacionInspeccion;
    }

}
