/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.TipoObra;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "tipoObra")
@ViewScoped
public class TiposObrasControllerMB implements Serializable {
    
    @Inject
    private ManagerService services;
    @Inject
    private UserSession user;
    private LazyModel<TipoObra> lazy;
    private TipoObra tipoObra;
    
    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(TipoObra.class);
        tipoObra = new TipoObra();
    }
    
    public void openDialog(TipoObra tipo) {
        tipoObra = new TipoObra();
        
        if (tipo != null) {
            tipoObra = tipo;
        } else {
            tipoObra.setEstado(Boolean.TRUE);
            tipoObra.setFechaIngreso(new Date());
        }
    }
    
    public void saveUpdate() {
        tipoObra.setUsuario(user.getUsuario());
        services.save(tipoObra);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<TipoObra> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<TipoObra> lazy) {
        this.lazy = lazy;
    }
    
    public TipoObra getTipoObra() {
        return tipoObra;
    }
    
    public void setTipoObra(TipoObra tipoObra) {
        this.tipoObra = tipoObra;
    }
//</editor-fold>

}
