/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.ControlCuentaContable;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "controlCuentaView")
@ViewScoped
public class ControlCuentaContableController implements Serializable {

    @Inject
    private ControlCuentaContableService controlCuentaService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<ControlCuentaContable> lazy;
    private OpcionBusqueda opcionBusqueda;
    private List<Short> listaPeriodo;
    private List<ControlCuentaContable> listaAnio;
    private ControlCuentaContable controlCuentaContable;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        opcionBusqueda = new OpcionBusqueda();
        controlCuentaContable = new ControlCuentaContable();
        buscarLazy();
    }

    public void cierreApertura(ControlCuentaContable control, Boolean var) {
        control.setEstado(!var);
        if (!var) {
            control.setFechaCierre(null);
        } else {
            control.setFechaCierre(new Date());
        }
        control.setFechaModificacion(new Date());
        control.setUsuarioModificacion(userSession.getNameUser());
        controlCuentaService.edit(control);
        controlCuentaContable = new ControlCuentaContable();
        listaAnio = controlCuentaService.getListControl(opcionBusqueda.getAnio());
    }

    public void buscarLazy() {
        if (opcionBusqueda.getAnio() != null) {
            crearPeriodo();
            lazy = new LazyModel<>(ControlCuentaContable.class);
            lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            listaAnio = controlCuentaService.getListControl(opcionBusqueda.getAnio());
        } else {
            lazy = null;
            listaAnio = new ArrayList<>();
        }
    }

    private void crearPeriodo() {
        if (controlCuentaService.getCierrePeriodo(opcionBusqueda.getAnio())) {
            if (listaAnio != null) {
                if (!listaAnio.isEmpty() && listaAnio.size() == 12) {
                    for (ControlCuentaContable item : listaAnio) {
                        ControlCuentaContable temp = Utils.clone(item);
                        temp.setId(null);
                        temp.setFechaCierre(null);
                        temp.setPeriodo(opcionBusqueda.getAnio());
                        temp.setUsuarioCreacion(userSession.getNameUser());
                        temp.setFechaCreacion(new Date());
                        temp.setUsuarioModificacion(null);
                        temp.setFechaModificacion(null);
                        temp.setEstado(Boolean.TRUE);
                        controlCuentaService.create(temp);
                    }
                }
            }
        }
    }

    public LazyModel<ControlCuentaContable> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ControlCuentaContable> lazy) {
        this.lazy = lazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ControlCuentaContable getControlCuentaContable() {
        return controlCuentaContable;
    }

    public void setControlCuentaContable(ControlCuentaContable controlCuentaContable) {
        this.controlCuentaContable = controlCuentaContable;
    }

}
