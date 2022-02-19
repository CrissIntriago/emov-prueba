/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.mantenimientos.controllers;

import com.origami.sigef.auth.services.ModuloEjb;
import com.origami.sigef.common.entities.Modulo;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@RequestScoped
public class MenuAppView implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<Modulo> modulos;

    @Inject
    private ModuloEjb moduloEjb;

    @PostConstruct
    public void initView() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            if (modulos == null) {
                modulos = moduloEjb.loadModulos();
            }
        }
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

}
