/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.transporte.Cooperativa;
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
public class DlgCooperativasMb implements Serializable {

    private LazyModel<Cooperativa> cooperativaLazy;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            cooperativaLazy = new LazyModel<>(Cooperativa.class);
            cooperativaLazy.getFilterss().put("estado", "AC");
        }
    }

    public void select(Cooperativa coop) {
        PrimeFaces.current().dialog().closeDynamic(coop);
    }

    public LazyModel<Cooperativa> getCooperativaLazy() {
        return cooperativaLazy;
    }

    public void setCooperativaLazy(LazyModel<Cooperativa> cooperativaLazy) {
        this.cooperativaLazy = cooperativaLazy;
    }

}
