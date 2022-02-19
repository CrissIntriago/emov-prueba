/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.ThGrupoServidor;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThGrupoServidorService;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thGrupoServidorView")
@ViewScoped
public class ThGrupoServidorController implements Serializable {

    @Inject
    private ThGrupoServidorService thGrupoServidorService;
    @Inject
    private ThInterfaces thInterfaces;

    private ThGrupoServidor thGrupoServidor;

    private LazyModel<ThGrupoServidor> thGrupoServidorLazy;

    private Boolean view;

    @PostConstruct
    public void init() {
        thGrupoServidor = new ThGrupoServidor();
        thGrupoServidorLazy = new LazyModel<>(ThGrupoServidor.class);
        thGrupoServidorLazy.getSorteds().put("codigo", "ASC");
        thGrupoServidorLazy.getFilterss().put("estado", true);
        thGrupoServidorLazy.setDistinct(false);
    }

    public void form(ThGrupoServidor thGrupoServidor, Boolean view) {
        this.view = view;
        if (thGrupoServidor != null) {
            this.thGrupoServidor = thGrupoServidor;
        } else {
            this.thGrupoServidor = new ThGrupoServidor();
        }
        JsfUtil.executeJS("PF('thGrupoServidorDlg').show()");
        PrimeFaces.current().ajax().update("thGrupoServidorForm");
    }

    public void save() {
        Boolean edit = thGrupoServidor.getId() != null;
        if (thGrupoServidor.getCodigo() == null || thGrupoServidor.getCodigo().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un código");
            return;
        }
        if (thGrupoServidor.getDescripcion() == null || thGrupoServidor.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción");
            return;
        }
        if (edit) {
            thGrupoServidor.setUsuarioModificacion(thInterfaces.getUser());
            thGrupoServidor.setFechaModificacion(new Date());
            thGrupoServidorService.edit(thGrupoServidor);
        } else {
            thGrupoServidor.setCantidad(0);
            thGrupoServidor.setUsuarioCreacion(thInterfaces.getUser());
            thGrupoServidor.setFechaCreacion(new Date());
            thGrupoServidorService.create(thGrupoServidor);
        }
        thGrupoServidor = new ThGrupoServidor();
        JsfUtil.executeJS("PF('thGrupoServidorDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        PrimeFaces.current().ajax().update("thGrupoServidorTable");
    }

    public void delete(ThGrupoServidor thGrupoServidor) {
        if (thGrupoServidor.getCantidad() == 0) {
            thGrupoServidor.setEstado(Boolean.FALSE);
            thGrupoServidor.setUsuarioModificacion(thInterfaces.getUser());
            thGrupoServidor.setFechaModificacion(new Date());
            thGrupoServidorService.edit(thGrupoServidor);
            JsfUtil.addSuccessMessage("INFO!!!", "Eliminado con exito");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar debido a que tiene sevidores asociados");
        }
    }

    public ThGrupoServidor getThGrupoServidor() {
        return thGrupoServidor;
    }

    public void setThGrupoServidor(ThGrupoServidor thGrupoServidor) {
        this.thGrupoServidor = thGrupoServidor;
    }

    public LazyModel<ThGrupoServidor> getThGrupoServidorLazy() {
        return thGrupoServidorLazy;
    }

    public void setThGrupoServidorLazy(LazyModel<ThGrupoServidor> thGrupoServidorLazy) {
        this.thGrupoServidorLazy = thGrupoServidorLazy;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
