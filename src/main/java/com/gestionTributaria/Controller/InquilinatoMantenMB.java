/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.ParamInquilinato;
import com.gestionTributaria.Services.ParamInquilinatoServices;
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
public class InquilinatoMantenMB implements Serializable {

    @Inject
    private ParamInquilinatoServices inquilinatoServices;
    private ParamInquilinato inquilinato;
    private LazyModel<ParamInquilinato> lazy;

    @PostConstruct
    public void init() {
        inquilinato = new ParamInquilinato();
        lazy = new LazyModel<>(ParamInquilinato.class);
    }

    public void openDlogo(ParamInquilinato inqu) {
        inquilinato = new ParamInquilinato();
        if (inqu != null) {
            inquilinato = inqu;
        }

    }

    public void save() {
        if (inquilinato.getId() != null) {
            inquilinatoServices.edit(inquilinato);

        } else {
            inquilinato = inquilinatoServices.create(inquilinato);
        }

        init();
        JsfUtil.addInformationMessage("", "La transacción se realizo correctamente");
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    public ParamInquilinatoServices getInquilinatoServices() {
        return inquilinatoServices;
    }

    public void setInquilinatoServices(ParamInquilinatoServices inquilinatoServices) {
        this.inquilinatoServices = inquilinatoServices;
    }

    public ParamInquilinato getInquilinato() {
        return inquilinato;
    }

    public void setInquilinato(ParamInquilinato inquilinato) {
        this.inquilinato = inquilinato;
    }

    public LazyModel<ParamInquilinato> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ParamInquilinato> lazy) {
        this.lazy = lazy;
    }
//</editor-fold>

}
