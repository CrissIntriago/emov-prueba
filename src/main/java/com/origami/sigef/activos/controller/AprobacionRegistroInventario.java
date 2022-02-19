/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.InventarioItemsService;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author erwin
 */
@Named(value = "aprobacionRegistroInventarioView")
@ViewScoped
public class AprobacionRegistroInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    private LazyModel<Inventario> lazyModel;
    private Inventario inventario;
    private List<InventarioItems> listaDetalleItems;

    @Inject
    private InventarioItemsService inventarioItemsService;

    @PostConstruct
    public void initView() {
        lazyModel = new LazyModel(Inventario.class);
        lazyModel.getFilterss().put("tipoMovimiento", "INGRESO");
        listaDetalleItems = new ArrayList<>();
        inventario = new Inventario();
    }

    public List<InventarioItems> getItemByInventario(Inventario inv) {
        return inventarioItemsService.getItemByInventarioItems(inv);
    }

    public LazyModel<Inventario> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyModel<Inventario> lazyModel) {
        this.lazyModel = lazyModel;
    }

}
