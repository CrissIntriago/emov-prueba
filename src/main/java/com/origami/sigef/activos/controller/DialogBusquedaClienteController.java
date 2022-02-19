/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "dialogBusquedaClienteview")
@ViewScoped
public class DialogBusquedaClienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LazyModel<Cliente> lazyModel;

    @PostConstruct
    public void initView() {
        lazyModel = new LazyModel<>(Cliente.class);
    }

    public void cerrar(Cliente cli) {
        PrimeFaces.current().dialog().closeDynamic(cli);
    }

    public LazyModel<Cliente> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyModel<Cliente> lazyModel) {
        this.lazyModel = lazyModel;
    }

}
