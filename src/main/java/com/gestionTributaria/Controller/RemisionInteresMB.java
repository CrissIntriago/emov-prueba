/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.RemisionInteresMulta;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.gestionTributaria.Services.RemisionInteresServices;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class RemisionInteresMB implements Serializable {
    
    @Inject
    private RemisionInteresServices remisionInteresServices;
    @Inject
    private FinaRenTipoLiquidacionService tipoLiquidacionService;
    private LazyModel<RemisionInteresMulta> lazy;
    private RemisionInteresMulta remision;
    private List<FinaRenTipoLiquidacion> tipoLiquidaciones;
    
    @PostConstruct
    public void init() {
        remision = new RemisionInteresMulta();
        lazy = new LazyModel(RemisionInteresMulta.class);
        tipoLiquidaciones = new ArrayList<>();
        tipoLiquidaciones = tipoLiquidacionService.findAllTipoLiquidacion();
    }
    
    public void abriDlogo(RemisionInteresMulta remis) {
        
        if (remis != null) {
            remision = new RemisionInteresMulta();
            remision = remis;
        } else {
            remision = new RemisionInteresMulta();
        }
        
    }
    
    public void saveUpdate() {
        
        if (remision.getId() != null) {
            remisionInteresServices.edit(remision);
        } else {
            remision = remisionInteresServices.create(remision);
        }
        JsfUtil.addInformationMessage("", "La transaccion se realizo con exito");
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public LazyModel<RemisionInteresMulta> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<RemisionInteresMulta> lazy) {
        this.lazy = lazy;
    }
    
    public RemisionInteresMulta getRemision() {
        return remision;
    }
    
    public void setRemision(RemisionInteresMulta remision) {
        this.remision = remision;
    }
    
    public List<FinaRenTipoLiquidacion> getTipoLiquidaciones() {
        return tipoLiquidaciones;
    }
    
    public void setTipoLiquidaciones(List<FinaRenTipoLiquidacion> tipoLiquidaciones) {
        this.tipoLiquidaciones = tipoLiquidaciones;
    }
//</editor-fold>
    
}
