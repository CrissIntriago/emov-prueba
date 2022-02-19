/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "receptaDocumentoTramiteAJVMB")
@ViewScoped
public class ReceptaDocumentoTramiteAJVMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;

//    private Boolean opc = Boolean.FALSE;
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

    public void opendlg() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
//        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void mandarCorrecionRentas() {
        try {
            String usuario = "";
            usuario = clienteService.getrolsUser(RolUsuario.liquidadorDosRentas);
            getParamts().put("usuarioRentas", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al completar las tareas", ex);
        }
    }

    public void mandarCorrecionVentanilla() {
        try {
            String usuario = "";
            usuario = clienteService.getrolsUser(RolUsuario.liquidadorDosRentas);
            getParamts().put("usuarioRentas", usuario.equals("") ? "admin_1" : usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al completar las tareas", ex);
        }
    }

    public void completarTarea() {
        try {
            String usuario = "";
//            if (opc) {//continuar
            usuario = clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia);
            getParamts().put("usuarioDirectorJV", usuario.equals("") ? "admin_1" : usuario);
//                getParamts().put("receptarDocumento", 1);
//            } else {//correccion
//                usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
//                getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
//                getParamts().put("receptarDocumento", 0);
//            }
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

//    public Boolean getOpc() {
//        return opc;
//    }
//
//    public void setOpc(Boolean opc) {
//        this.opc = opc;
//    }
}
