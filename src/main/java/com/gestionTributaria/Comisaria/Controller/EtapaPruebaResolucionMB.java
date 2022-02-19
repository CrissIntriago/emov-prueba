/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.ResolucionesService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * @author Luis Pozo gonzabay
 */
@Named(value = "etapaPruebaResolucionMB")
@ViewScoped
public class EtapaPruebaResolucionMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ResolucionesService resolucionesService;
    @Inject
    private ManagerService services;

    private UploadedFile file;
    private FnResolucion resolucion;
    private Documentos documento;
    private List<Documentos> listaDocumentos;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                resolucion = new FnResolucion();
                listaDocumentos = new ArrayList<>();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void openDialogResolucion() {
        resolucion = new FnResolucion();
        resolucion.setTramite(BigInteger.valueOf(tramite.getId()));
        resolucion.setFechaTramite(new Date());
        PrimeFaces.current().executeScript("PF('dlgResolucion').show()");
        PrimeFaces.current().ajax().update("dglFormRes");
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

    public void addResolucion() {
        if (file == null) {
            JsfUtil.addWarningMessage("Advertencía", "Adjunte la resolución.");
            return;
        }
        uploadDoc.upload(tramite, file);
        resolucion.setEstado("A");
        resolucion.setIdTramite(tramite);
        resolucion.setTramite(BigInteger.valueOf(tramite.getId()));
        resolucion = resolucionesService.create(resolucion);
        handleFileDocumentBySave(file);
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
//        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }

    public void handleFileDocumentBySave(UploadedFile file) {
        try {
            Boolean bandera = false;
            documento = new Documentos();
            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + file.getFileName();
            documento.setDepartamento(session.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(file.getFileName());
            documento.setDescripcion(file.getContentType());
            documento.setEstado(Boolean.TRUE);
            documento.setClaseNombre(resolucion.getClass().getPackage().getName() + "." + resolucion.getClass().getSimpleName());
            documento.setIdentificador(resolucion.getId());
            listaDocumentos.add(documento);
            if (listaDocumentos.size() <= 1) {
                Utils.copyFileServerDoc(ruta, file.getInputstream());
                for (Documentos doc : listaDocumentos) {
                    documento = (Documentos) services.save(doc);
                }
                bandera = true;
            } else {
                documento = new Documentos();
                listaDocumentos.remove(listaDocumentos.size() - 1);
                JsfUtil.addInformationMessage("Solo puede subir un documento", "");
            }
            if (bandera == true) {
                JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
            }
            listaDocumentos.clear();
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subir el documento");
        }
    }

    public FnResolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
    }

}
