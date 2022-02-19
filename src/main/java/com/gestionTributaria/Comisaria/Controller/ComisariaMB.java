/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.ServicioRequisito;
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
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class ComisariaMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private ComisariaServices comisariaServices;

    private List<ServicioRequisito> listRequisitoTasa;

    private List<Usuarios> comisariosViasPuiblica;
    private Usuarios userSeelect;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                userSeelect = new Usuarios();
                comisariosViasPuiblica = new ArrayList<>();
                comisariosViasPuiblica = clienteService.getListClientesByCodeRol(RolUsuario.comisarioViaPublica);
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                listRequisitoTasa = new ArrayList<>();
                getRequisitosByServicio();

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void getRequisitosByServicio() {
        if (tramite != null) {
            listRequisitoTasa = new ArrayList<>();
            listRequisitoTasa = comisariaServices.getRequisitosTrmaitesServices(tramite.getId());
        }

    }

    public void send() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
    }

    public void completarTarea() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia);
            getParamts().put("usuarioDirector", usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void completarTareaViaPublica() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.directorJusticiaVigilancia);
            getParamts().put("director", usuario);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void completarTareaViaPublica(int aprobacion) {
        try {
            String usuario = "";
            getParamts().put("aprobado", aprobacion);

            if (aprobacion == 1) {
                if (tramite.getTipoTramite().getAbreviatura().equals("PVPU")) {

                    if (userSeelect == null) {
                        JsfUtil.addWarningMessage("", "Elija al comisari@ que le va a llegar el trámite");
                        return;
                    }

                    if (userSeelect != null && userSeelect.getId() != null && userSeelect.getUsuario() != null) {
                        usuario = userSeelect.getUsuario();
                    }

                } else if (tramite.getTipoTramite().getAbreviatura().equals("IPVE") || tramite.getTipoTramite().getAbreviatura().equals("IPRN")) {
                    usuario = clienteService.getrolsUser(RolUsuario.comisarioInquilinato);
                }
                getParamts().put("solicitar_delegado", usuario);
            } else {
                getParamts().put("usuario", tramite.getObservaciones().get(0).getUserCre());
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public List<ServicioRequisito> getListRequisitoTasa() {
        return listRequisitoTasa;
    }

    public void setListRequisitoTasa(List<ServicioRequisito> listRequisitoTasa) {
        this.listRequisitoTasa = listRequisitoTasa;
    }

    public List<Usuarios> getComisariosViasPuiblica() {
        return comisariosViasPuiblica;
    }

    public void setComisariosViasPuiblica(List<Usuarios> comisariosViasPuiblica) {
        this.comisariosViasPuiblica = comisariosViasPuiblica;
    }

    public Usuarios getUserSeelect() {
        return userSeelect;
    }

    public void setUserSeelect(Usuarios userSeelect) {
        this.userSeelect = userSeelect;
    }

}
