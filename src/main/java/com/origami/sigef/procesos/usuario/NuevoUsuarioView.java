/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos.usuario;

import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class NuevoUsuarioView extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(NuevoUsuarioView.class.getName());

    @PostConstruct
    protected void iniView() {
        try {
            if (this.session.getTaskID() != null) {
                //System.out.println("// getTaskID " + this.session.getTaskID());
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarObs() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObs').show()");
    }

    public void completarTarea() {
        try {
            getParamts().put("usuario", this.session.getNameUser());
            this.getParamts().put("usuarios", Arrays.asList("anavarro", "JIntriago", "SSalinas"));
            if (tarea.getName().equals("Roles y Permisos")) {
                getParamts().put("formulario", "/usuario/aprobacion-task");
            } else {
                getParamts().put("formulario", "/usuario/permisos-task");
            }
            if (saveTramite() == null) {
                return;
            }
            super.completeTask((HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void terminarProceso(int aprobado) {
        try {
            if (aprobado == 0) {
                getParamts().put("aprobado", aprobado);
            } else {
                getParamts().put("usuario", this.session.getNameUser());
                getParamts().put("formulario", "/usuario/edicion-task");
            }
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

}
