/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.RenFactorPorCapital;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
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
public class CuantiaPatneteMunicipalMB implements Serializable {

    @Inject
    private ManagerService services;

    private RenFactorPorCapital capital;
    private LazyModel<RenFactorPorCapital> lazy;

    @PostConstruct
    public void init() {
        capital = new RenFactorPorCapital();
        lazy = new LazyModel(RenFactorPorCapital.class);
        lazy.getSorteds().put("id", "ASC");
        lazy.setDistinct(false);

    }

    public void nuevoEditar(RenFactorPorCapital r) {
        if (r == null) {
            capital = new RenFactorPorCapital();

        } else {

            capital = new RenFactorPorCapital();
            capital = r;
        }
    }

    public void saveEditar() {

        if (capital.getId() == null) {
            capital.setEstado(Boolean.TRUE);
            services.save(capital);
        } else {
            services.update(capital);
        }
        JsfUtil.addInformationMessage("", "Registro exitoso");
        capital = new RenFactorPorCapital();
    }

    public RenFactorPorCapital getCapital() {
        return capital;
    }

    public void setCapital(RenFactorPorCapital capital) {
        this.capital = capital;
    }

    public LazyModel<RenFactorPorCapital> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RenFactorPorCapital> lazy) {
        this.lazy = lazy;
    }

}
