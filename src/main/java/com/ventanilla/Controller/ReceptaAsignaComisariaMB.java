/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "receptaAsignaComisariaMB")
@ViewScoped
public class ReceptaAsignaComisariaMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    private Boolean opc = Boolean.FALSE;
    private List<Usuarios> listaComisarios;
    private Usuarios comisario;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listaComisarios = new ArrayList<>();
                comisario = new Usuarios();
                LlenarComisariosPermisoFuncionamiento();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void opendlg(Boolean opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void LlenarComisariosPermisoFuncionamiento() {
        listaComisarios = clienteService.getListClientesByCodeRol(RolUsuario.comisarioPermisoFuncionamiento);
    }

    public void completarTarea() {
        try {
            String usuario;
            if (opc) {//continuar
                getParamts().put("usuarioComisaria", comisario.getUsuario());
                getParamts().put("tipo", 0);
            } else {//correccion
                usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
                getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                getParamts().put("tipo", 1);
            }
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

    public Boolean getOpc() {
        return opc;
    }

    public void setOpc(Boolean opc) {
        this.opc = opc;
    }

    public List<Usuarios> getListaComisarios() {
        return listaComisarios;
    }

    public void setListaComisarios(List<Usuarios> listaComisarios) {
        this.listaComisarios = listaComisarios;
    }

    public Usuarios getComisario() {
        return comisario;
    }

    public void setComisario(Usuarios comisario) {
        this.comisario = comisario;
    }

}
