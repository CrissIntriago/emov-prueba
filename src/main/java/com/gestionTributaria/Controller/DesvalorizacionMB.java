/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.RenDesvalorizacion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class DesvalorizacionMB implements Serializable {
    
    @Inject
    private ManagerService services;
    @Inject
    private UserSession user;
    private LazyModel<RenDesvalorizacion> lazy;
    private RenDesvalorizacion renDesvalorizacion;
    
    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(RenDesvalorizacion.class);
    }
    
    public void newRow(RenDesvalorizacion d) {
        
        if (d != null) {
            renDesvalorizacion = new RenDesvalorizacion();
            renDesvalorizacion = d;
        } else {
            renDesvalorizacion = new RenDesvalorizacion();
            renDesvalorizacion.setAnio(Utils.getAnio(new Date()));
        }
        
    }
    
    public void saveUpdate() {
        try {
            renDesvalorizacion.setUsuarioCreac(user.getNameUser());
            renDesvalorizacion.setEstado(Boolean.TRUE);
            renDesvalorizacion.setFechaCreac(new Date());
            if (renDesvalorizacion.getId() != null) {
                renDesvalorizacion = (RenDesvalorizacion) services.save(renDesvalorizacion);
            } else {
                services.update(renDesvalorizacion);
            }
            JsfUtil.addInformationMessage("", "Registro Correcto");
        } catch (Exception e) {
            System.out.println("e");
            JsfUtil.addErrorMessage("", "Ocurrio un error en registro");
        }
    }
    
    public void eliminar(RenDesvalorizacion d) {
        if (d.getEstado() != null) {
            if (d.getEstado()) {
                renDesvalorizacion.setEstado(Boolean.FALSE);
                JsfUtil.addInformationMessage("", "El registro se inactivo correctamente");
            } else {
                renDesvalorizacion.setEstado(Boolean.TRUE);
                JsfUtil.addInformationMessage("", "El registro se activo correctamente");
            }
        } else {
            renDesvalorizacion.setEstado(Boolean.FALSE);
            JsfUtil.addInformationMessage("", "El registro se inactivo correctamente");
        }
        services.update(renDesvalorizacion);
        
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER AN SETTER">
    public LazyModel<RenDesvalorizacion> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<RenDesvalorizacion> lazy) {
        this.lazy = lazy;
    }
    
    public RenDesvalorizacion getRenDesvalorizacion() {
        return renDesvalorizacion;
    }
    
    public void setRenDesvalorizacion(RenDesvalorizacion renDesvalorizacion) {
        this.renDesvalorizacion = renDesvalorizacion;
    }
//</editor-fold>

}
