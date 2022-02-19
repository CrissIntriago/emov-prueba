/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.beans;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import com.origami.sigef.tesoreria.service.RubroTipoService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named(value = "rubroTipoView")
@ViewScoped
public class RubroTipoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RubroTipoService rubroTipoService;

    private LazyModel<RubroTipo> rubroTipos;
    private RubroTipo rubroTipo;
    private Boolean editar;

    @PostConstruct
    public void init() {
        editar = Boolean.FALSE;
        rubroTipo = new RubroTipo();
        rubroTipo.setEstado(Boolean.TRUE);
        rubroTipos = new LazyModel<>(RubroTipo.class);
        rubroTipos.getFilterss().put("estado", true);
    }

    public void guardar() {
        if (rubroTipo.getId() == null) {
            rubroTipo.setDescripcion(rubroTipo.getDescripcion().toUpperCase());
            rubroTipoService.create(rubroTipo);
            rubroTipo = new RubroTipo();
        } else {
            rubroTipo.setDescripcion(rubroTipo.getDescripcion().toUpperCase());
            rubroTipoService.edit(rubroTipo);
            rubroTipo = new RubroTipo();
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
        }
    }

    public void eliminar(RubroTipo rt) {
        rt.setEstado(Boolean.FALSE);
        rubroTipoService.edit(rt);
    }

    public void editar(RubroTipo rt) {
        editar = Boolean.TRUE;
        rubroTipo = rt;
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        rubroTipo = new RubroTipo();
    }

    public LazyModel<RubroTipo> getRubroTipos() {
        return rubroTipos;
    }

    public void setRubroTipos(LazyModel<RubroTipo> rubroTipos) {
        this.rubroTipos = rubroTipos;
    }

    public RubroTipo getRubroTipo() {
        return rubroTipo;
    }

    public void setRubroTipo(RubroTipo rubroTipo) {
        this.rubroTipo = rubroTipo;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

}
