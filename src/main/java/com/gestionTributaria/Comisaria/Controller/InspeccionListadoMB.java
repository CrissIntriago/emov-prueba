/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Entities.Inspecciones;
import com.gestionTributaria.Comisaria.Service.InspeccionService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import java.io.Serializable;
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
public class InspeccionListadoMB implements Serializable {

    @Inject
    private InspeccionService inspeccionService;
    @Inject
    private UserSession userSession;
    private Inspecciones inspeccion;
    private LazyModel<Inspecciones> lazy;

    @PostConstruct
    public void init() {
        inspeccion = new Inspecciones();
        lazy = new LazyModel<>(Inspecciones.class);
        lazy.getFilterss().put("comisariaUser.usuario", userSession.getNameUser());
        ////PERMITE SOLO VER LAS QUE HIZO EL USUARIO
    }

    public void visualizar(Inspecciones insp) {
        inspeccion = new Inspecciones();
        inspeccion = insp;
    }
    
    

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public Inspecciones getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(Inspecciones inspeccion) {
        this.inspeccion = inspeccion;
    }

    public LazyModel<Inspecciones> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<Inspecciones> lazy) {
        this.lazy = lazy;
    }
//</editor-fold>

}
