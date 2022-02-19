/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
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
@Named(value = "asignarComisarioBM")
@ViewScoped
public class AsignarComisarioBM extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;

    @Inject
    private CatalogoService catalogoService;
    private List<Usuarios> listClientes;
    private Usuarios clienteSelect;
    private CatalogoItem comisariaSelect;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listClientes = clienteService.getListClientesByCodeRol(RolUsuario.delegados);
                comisariaSelect = catalogoService.getItemByCatalogoAndCodigo("GT_lista_comisarias", (String) getVariable("tipo_comisaria"));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void opendlg() {
        if (clienteSelect == null) {
            JsfUtil.addWarningMessage("Información", "Seleccionar un delegado.");
            return;
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            getParamts().put("usuarioDelegado", clienteSelect.getUsuario());
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            getParamts().put("COMISARIO_SELECT_ID", clienteSelect.getFuncionario().getPersona().getId());
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public List<Usuarios> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Usuarios> listClientes) {
        this.listClientes = listClientes;
    }

    public Usuarios getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Usuarios clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public CatalogoItem getComisariaSelect() {
        return comisariaSelect;
    }

    public void setComisariaSelect(CatalogoItem comisariaSelect) {
        this.comisariaSelect = comisariaSelect;
    }
}
