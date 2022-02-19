/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "dialogItemView")
@ViewScoped
public class DialogItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private LazyModel<DetalleItem> lazyItem;

    @Inject
    private DetalleItemService detalleItemService;

    @PostConstruct
    public void initview() {
        lazyItem = new LazyModel<>(DetalleItem.class);
        lazyItem.getFilterss().put("estado", true);
        lazyItem.getFilterss().put("cuentaContable:equal", null);
        lazyItem.getSorteds().put("codigo", "ASC");
    }

    public void close(DetalleItem item) {
        PrimeFaces.current().dialog().closeDynamic(item);
    }

    public LazyModel<DetalleItem> getLazyItem() {
        return lazyItem;
    }

    public void setLazyItem(LazyModel<DetalleItem> lazyItem) {
        this.lazyItem = lazyItem;
    }

}
