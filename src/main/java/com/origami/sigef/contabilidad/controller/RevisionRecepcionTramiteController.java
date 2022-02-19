/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.lazy.TareasWFLazy;
import com.origami.sigef.common.service.TareasActivasService;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ORIGAMI
 */
@Named(value = "recepcionTramiteView")
@ViewScoped
public class RevisionRecepcionTramiteController extends BpmnBaseRoot implements Serializable {

    @Inject
    private TareasActivasService tareasActivasService;
    @Inject
    private BpmBaseEngine baseEngine;
    @Inject
    private ClienteService clienteService;

    private TareasActivas tareaActiva;
    protected TareasWFLazy lazy;
    private List<HistoricTaskInstance> tasks;
    private StreamedContent imageProcessInstance;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
            if (this.session.getVarTemp() instanceof TareasActivas) {
                lazy = new TareasWFLazy(this.session.getName());
                lazy.getSorteds().put("numTramite", "ASC");
                lazy.getFilterss().put("numTramite", ((TareasActivas) this.session.getVarTemp()).getNumTramite());
            }
            //loadData();
        }

    }

    public void visualizar(TareasActivas task) {
        tasks = baseEngine.getTaskByProcessInstanceId(task.getProcInstId());
        tareaActiva = task;
        InputStream stream = baseEngine.getProcessInstanceDiagram(task.getProcInstId());
        imageProcessInstance = new DefaultStreamedContent(stream);
        JsfUtil.executeJS("PF('dlgTarea').show()");
        JsfUtil.update("frmDetalleTarea");
    }

    public void continuarTask() {
        try {
            getParamts().put("contabilidad", clienteService.getrolsUser(RolUsuario.contador));
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            observacion = new Observaciones();
        } catch (Exception ex) {
            Logger.getLogger(RevisionRecepcionTramiteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void completarTask(TareasActivas task) {
        tareaActiva = task;
        observacion.setEstado(Boolean.TRUE);
        observacion.setFecCre(new Date());
        observacion.setUserCre(this.session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public BpmBaseEngine getBaseEngine() {
        return baseEngine;
    }

    public void setBaseEngine(BpmBaseEngine baseEngine) {
        this.baseEngine = baseEngine;
    }

    public List<HistoricTaskInstance> getTasks() {
        return tasks;
    }

    public void setTasks(List<HistoricTaskInstance> tasks) {
        this.tasks = tasks;
    }

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

    public TareasActivas getTareaActiva() {
        return tareaActiva;
    }

    public void setTareaActiva(TareasActivas tareaActiva) {
        this.tareaActiva = tareaActiva;
    }

    public TareasWFLazy getLazy() {
        return lazy;
    }

    public void setLazy(TareasWFLazy lazy) {
        this.lazy = lazy;
    }
    //</editor-fold>
}
