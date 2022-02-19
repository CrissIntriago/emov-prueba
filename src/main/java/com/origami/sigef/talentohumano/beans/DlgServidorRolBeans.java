/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "dlgServidorRol")
@ViewScoped
public class DlgServidorRolBeans implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CatalogoItemService catalogoService;

    private LazyModel<UsuarioRol> lazy;
    private List<CatalogoItem> catalogoList;
    private List<String> maximaAutoridades;

    @PostConstruct
    public void init() {
        catalogoList = catalogoService.findCatalogoClasificacion1("ROL-CATEGORIA");
        maximaAutoridades = new ArrayList<>();
        if (!catalogoList.isEmpty()) {
            for (CatalogoItem c : catalogoList) {
                if (c.getCodigo().equals("1") || c.getCodigo().equals("7") || c.getCodigo().equals("15")) {
                    maximaAutoridades.add(c.getCodigo());
                }
            }
        }
        lazy = new LazyModel<>(UsuarioRol.class);
//        , "usuario.funcionario.persona.apellido", "ASC"
        lazy.setDistinct(false);
//        lazy.getFilterss().put("rol.unidadAdministrativa.codigo", "CP");
        lazy.getFilterss().put("rol.categoria.codigo", maximaAutoridades);
        lazy.getFilterss().put("rol.estado", true);
        lazy.getFilterss().put("usuario:noEqual", null);
        lazy.getFilterss().put("usuario.funcionario:notEqual", null);
    }

    public void close(UsuarioRol p) {
        PrimeFaces.current().dialog().closeDynamic(p);
    }

    public LazyModel<UsuarioRol> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<UsuarioRol> lazy) {
        this.lazy = lazy;
    }

}
