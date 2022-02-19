/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class DlgVehiculosMb implements Serializable {

    private LazyModel<Vehiculo> vehiculoLazy;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            vehiculoLazy = new LazyModel<>(Vehiculo.class);
            vehiculoLazy.getFilterss().put("estado", "AC");
        }
    }

    public void select(Vehiculo coop) {
        PrimeFaces.current().dialog().closeDynamic(coop);
    }

    public LazyModel<Vehiculo> getVehiculoLazy() {
        return vehiculoLazy;
    }

    public void setVehiculoLazy(LazyModel<Vehiculo> vehiculoLazy) {
        this.vehiculoLazy = vehiculoLazy;
    }

}
