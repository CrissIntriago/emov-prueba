/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.OrdenRequisicionItemsService;
import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.OrdenRequisicionItems;
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
 * @author Dairon Freddy
 */
@Named(value = "solicitudesRequisicionView")
@ViewScoped
public class SolicitudesRequisicionController implements Serializable {

    private LazyModel<OrdenRequisicion> lazyOrdenRequisicion;
    private String estadoOrd;
    @Inject
    private OrdenRequisicionItemsService ordenRequisicionItemsService;

    private static final Logger LOG = Logger.getLogger(SolicitudesRequisicionController.class.getName());

    @PostConstruct
    public void initView() {
        lazyOrdenRequisicion = new LazyModel<>(OrdenRequisicion.class);
        lazyOrdenRequisicion.getFilterss().put("estado", Boolean.TRUE);
        estadoOrd = "";
    }

    public List<OrdenRequisicionItems> getItemByOrdenRequisicion(OrdenRequisicion orden) {

        List<OrdenRequisicionItems> result = new ArrayList<>();
        List<OrdenRequisicionItems> temp = ordenRequisicionItemsService.getItemsByOrden(orden);
        if (temp != null && !temp.isEmpty()) {
            temp.stream().filter((i) -> (!result.contains(i))).forEachOrdered((i) -> {
                result.add(i);
            });
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public String getEstadoOrd() {
        return estadoOrd;
    }

    public void setEstadoOrd(String estadoOrd) {
        this.estadoOrd = estadoOrd;
    }

    public LazyModel<OrdenRequisicion> getLazyOrdenRequisicion() {
        return lazyOrdenRequisicion;
    }

    public void setLazyOrdenRequisicion(LazyModel<OrdenRequisicion> lazyOrdenRequisicion) {
        this.lazyOrdenRequisicion = lazyOrdenRequisicion;
    }

//</editor-fold>
}
