/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.CatPredioService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "recepcionAsignarComisariaPFNMB")
@ViewScoped
public class RecepcionAsignarComisariaPFNMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;

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
                LlenarComisariosPermisoFuncionamiento();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void LlenarComisariosPermisoFuncionamiento() {
        listaComisarios = clienteService.getListClientesByCodeRol(RolUsuario.comisarioPermisoFuncionamiento);
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
            if (comisario != null) {
                getParamts().put("usuarioComisaria", comisario.getUsuario());
                JsfUtil.executeJS("PF('dlgObservaciones').hide()");
                if (saveTramite() == null) {
                    return;
                }
                this.session.setVarTemp(null);
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            } else {
                JsfUtil.addInformationMessage("", "Debe seleccionar un comisario");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
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
