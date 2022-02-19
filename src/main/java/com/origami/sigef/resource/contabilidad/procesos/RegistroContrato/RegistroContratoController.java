/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.resource.contabilidad.procesos.RegistroContrato;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Doris Santa cruz
 */
@Named(value = "registroContratoView")
@ViewScoped

public class RegistroContratoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private FileUploadDoc uploadDoc;
    private ListarequisitosModel requisitosObjeto;
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    private List<ListarequisitosModel> requisitosTramite;
    private ProcesoService procesoService;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listRequisitos();
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

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
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
            getParamts().put("usuario_2", "admi_1");
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.executeJS("PF('dlgObservaciones2').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

}
