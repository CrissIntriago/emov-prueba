/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.commons.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "dlgContCuentasView")
@ViewScoped
public class dlgContCuentasController implements Serializable {

    private LazyModel<ContCuentas> contCuentasLazy;

    @PostConstruct
    public void initView() {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
    }

    public void closeDlgContCuentas(ContCuentas contCuentas) {
        PrimeFaces.current().dialog().closeDynamic(contCuentas);
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

}
