/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoExistenciasService;
import com.origami.sigef.common.entities.CatalogoExistencias;
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
@Named(value = "dialogCatExistenciasView")
@ViewScoped
public class DialogCatalogoExistenciasController implements Serializable {

    private LazyModel<CatalogoExistencias> lazyCatalogoExistencias;

    @Inject
    private CatalogoExistenciasService catalogoExistenciasService;

    @PostConstruct
    public void initview() {
        lazyCatalogoExistencias = new LazyModel<>(CatalogoExistencias.class);
        lazyCatalogoExistencias.getSorteds().put("idExistencia", "ASC");
    }

    public void close(CatalogoExistencias exist) {
        PrimeFaces.current().dialog().closeDynamic(exist);
    }

    public LazyModel<CatalogoExistencias> getLazyCatalogoExistencias() {
        return lazyCatalogoExistencias;
    }

    public void setLazyCatalogoExistencias(LazyModel<CatalogoExistencias> lazyCatalogoExistencias) {
        this.lazyCatalogoExistencias = lazyCatalogoExistencias;
    }

}
