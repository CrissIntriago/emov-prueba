/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "dialogGrupoNiveles")
@ViewScoped
public class DialogGrupoNiveles implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private GrupoNiveles grupoNiveles;
    private LazyModel<GrupoNiveles> lazyGrupo;

    @Inject
    private GrupoNivelesService grupService;

    @PostConstruct
    public void initView() {
        lazyGrupo = new LazyModel<>(GrupoNiveles.class);
        grupoNiveles = new GrupoNiveles();
//        lazyGrupo.getFilterss().put("padre:equal", null);
        lazyGrupo.getFilterss().put("nivel.orden:equal", 3);
        lazyGrupo.getSorteds().put("padre.padre.codigo", "ASC");
        lazyGrupo.getSorteds().put("padre.codigo", "ASC");
        lazyGrupo.getSorteds().put("codigo", "ASC");
        lazyGrupo.setDistinct(Boolean.FALSE);
    }

    public GrupoNiveles obtenerNombreSub(GrupoNiveles g) {
        return grupService.findByPadreGrupo(g);
//        return grupService.findByNamedQuery1("GrupoNiveles.findByD", g.getId());
    }

    public void close(GrupoNiveles g) {
        PrimeFaces.current().dialog().closeDynamic(g);
    }

    public LazyModel<GrupoNiveles> getLazyGrupo() {
        return lazyGrupo;
    }

    public void setLazyGrupo(LazyModel<GrupoNiveles> lazyGrupo) {
        this.lazyGrupo = lazyGrupo;
    }

    public GrupoNiveles getGrupoNiveles() {
        return grupoNiveles;
    }

    public void setGrupoNiveles(GrupoNiveles grupoNiveles) {
        this.grupoNiveles = grupoNiveles;
    }

}
