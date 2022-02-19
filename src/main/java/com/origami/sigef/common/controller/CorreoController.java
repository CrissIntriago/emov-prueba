/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.entities.CorreosEnviados;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.ventanilla.Entity.Servicio;
import ec.gob.duran.lazymodels.LazyModelWS;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class CorreoController implements Serializable {
    
    @Inject
    protected UserSession us;
    
    @Inject
    private KatalinaService katalinaService;

    private LazyModel<CorreosEnviados> lazyCorreo;
    private LazyModel<Servicio> lazyServicio;
    private List<Correo> correos;
    private CorreosEnviados correo;
    private String mensaje;
    
    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        this.lazyCorreo =  new LazyModel<>(CorreosEnviados.class);
        this.lazyCorreo.getSorteds().put("fecha_envio", "DESC");
    }
    
     public void showDialogMessage(CorreosEnviados c) {
        mensaje = c.getMensaje();
        JsfUtil.executeJS("PF('dlgConfirmationMessage').show()");
        JsfUtil.update("mensaje");
       
    }
     
    public void showDialog(CorreosEnviados c) {
        if (c.getEnviado()) {
            correo = c;
            JsfUtil.executeJS("PF('dlgConfirmation').show()");
        } else {
            JsfUtil.addErrorMessage("REENVIAR CORREO", "El correo se envio.");
        }
    }

    public void reenviarCorreo() {
        Correo correo = new Correo();
        correo.setId(this.correo.getId());
        correo.setAsunto(this.correo.getAsunto());
        correo.setDestinatario(this.correo.getDestinatario());
        correo.setMensaje(this.correo.getMensaje());
      
        Correo correoREST = katalinaService.reenviarCorreo(correo);
        System.out.println(correoREST);
        if (correoREST != null) {
            JsfUtil.addInformationMessage("REENVIAR CORREO", "Correo Reenviado Correctamente");
        } else {
            JsfUtil.addErrorMessage("REENVIAR CORREO", "Error al reenviar el Correo");
        }

        JsfUtil.executeJS("PF('dlgConfirmation').hide()");

    }

    public void closeDialog() {
        JsfUtil.executeJS("PF('dlgConfirmation').hide()");
    }

    public LazyModel<CorreosEnviados> getLazyCorreo() {
        return lazyCorreo;
    }

    public void setLazyCorreo(LazyModel<CorreosEnviados> lazyCorreo) {
        this.lazyCorreo = lazyCorreo;
    }

    public List<Correo> getCorreos() {
        return correos;
    }

    public void setCorreos(List<Correo> correos) {
        this.correos = correos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LazyModel<Servicio> getLazyServicio() {
        return lazyServicio;
    }

    public void setLazyServicio(LazyModel<Servicio> lazyServicio) {
        this.lazyServicio = lazyServicio;
    }

    
    
}
