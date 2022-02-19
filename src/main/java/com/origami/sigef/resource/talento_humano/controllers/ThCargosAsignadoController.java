/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thCargosAsignadoView")
@ViewScoped
public class ThCargosAsignadoController implements Serializable {

    private LazyModel<ThServidorCargo> thServidorCargoLazyModel;

    @PostConstruct
    public void init() {
        thServidorCargoLazyModel = new LazyModel<>(ThServidorCargo.class);
        thServidorCargoLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
        thServidorCargoLazyModel.getFilterss().put("activo", true);
        thServidorCargoLazyModel.setDistinct(false);
    }

    public LazyModel<ThServidorCargo> getThServidorCargoLazyModel() {
        return thServidorCargoLazyModel;
    }

    public void setThServidorCargoLazyModel(LazyModel<ThServidorCargo> thServidorCargoLazyModel) {
        this.thServidorCargoLazyModel = thServidorCargoLazyModel;
    }
    
    
}
