/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Comisaria.Service.OrdernesService;
import com.gestionTributaria.Entities.Ordenes;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "asignaciondDelegadoMB")
@ViewScoped
public class AsignaciondDelegadoMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private OrdernesService ordenesService;
    private Usuarios clienteSelect;
    private List<Usuarios> listClientes;
    private Ordenes ordenes;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.delegados);
                ordenes = new Ordenes();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaOthers() {
        ordenes = ordenesService.findByOrden(tramite.getNumTramite());
        if (ordenes.getDelegado() == null) {
            JsfUtil.addInformationMessage("", "Debe Asignar un delegado para continuar");
        } else {
            try {
                getParamts().put("usuarioDelegado", tramite.getObservaciones().get(1).getUserCre());
                JsfUtil.executeJS("PF('dlgObservaciones').hide()");
                JsfUtil.update(":frmDlgObser");
                if (saveTramite() == null) {
                    return;
                }
                super.completeTask((HashMap<String, Object>) getParamts());
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");

            } catch (Exception e) {
                LOG.log(Level.SEVERE, "completas tareas", e);
            }
        }
    }

    public void asignarDelegado() {
        ordenes = ordenesService.findByOrden(tramite.getNumTramite());
        ordenes.setDelegado(clienteSelect.getFuncionario().getPersona());
        ordenesService.edit(ordenes);
        JsfUtil.addInformationMessage("", "Delegado Seleccionado" + clienteSelect.getNameUsuario());
    }

    public Usuarios getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Usuarios clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public List<Usuarios> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Usuarios> listClientes) {
        this.listClientes = listClientes;
    }

}
