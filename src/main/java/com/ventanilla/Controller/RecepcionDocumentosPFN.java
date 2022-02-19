/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

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

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "recepcionDocumentosPFN")
@ViewScoped
public class RecepcionDocumentosPFN extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;

    private Integer opc = 0;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void opendlg(Integer opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            String usuario = "";
            switch (opc) {
                case 0://Coreccion
                    usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
                    getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                    getParamts().put("opcion", 1);
                    break;
                case 1://Primera Vez
                    usuario = clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia);
                    getParamts().put("usuarioRecepcionDirectorJV", usuario.equals("") ? "admin_1" : usuario);
                    getParamts().put("opcion", 0);
                    break;
                case 2://Renovación
                    usuario = clienteService.getrolsUser(RolUsuario.comisarioPermisoFuncionamiento);
                    getParamts().put("usuarioComisaria", usuario.equals("") ? "admin_1" : usuario);
                    break;
            }
            getParamts().put("tipo", opc);

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

}
