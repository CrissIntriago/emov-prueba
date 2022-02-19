/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class recepcionDocumentosFisicosController extends BpmnBaseRoot implements Serializable{
    
     private String observaciones;
    
    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
            }else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        }catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    
    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update("frmDlgObser");
    }
    
     public void completarTarea() {
        try {
            if (Utils.isEmptyString(observaciones)) {
                JsfUtil.addWarningMessage("Revisión Documentos", "Ingrese una observación");
                return;
            }
            getParamts().put("usuario_2", session.getNameUser());
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
    
}
