/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named
@ViewScoped
public class StockCriticoMB implements Serializable {

    private LazyModel<DetalleItem> lazyDetalleItem;

    @PostConstruct
    public void init() {
        lazyDetalleItem = new LazyModel<>(DetalleItem.class);
        lazyDetalleItem.setDistinct(false);
        lazyDetalleItem.getSorteds().put("codigoAgrupado", "ASC");
        lazyDetalleItem.getFilterss().put("critico", 1);

    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<DetalleItem> getLazyDetalleItem() {
        return lazyDetalleItem;
    }

    public void setLazyDetalleItem(LazyModel<DetalleItem> lazyDetalleItem) {
        this.lazyDetalleItem = lazyDetalleItem;
    }
//</editor-fold>
}
