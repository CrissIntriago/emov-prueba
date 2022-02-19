/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.services.ThTipoRolService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thTipoRolView")
@ViewScoped
public class ThTipoRolController implements Serializable {

    @Inject
    private ThTipoRolService thTipoRolService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<ThTipoRol> thTipoRolLazyModel;

    private ThTipoRol thTipoRol;
    private OpcionBusqueda opcionBusqueda;

    private boolean view;

    private List<Short> listaPeriodo;
    private List<CatalogoItem> listaMeses;
    private List<CatalogoItem> listaTipos;

    @PostConstruct
    public void init() {
        cleanForm(false);
        actualizarDatos();
    }

    public void cleanForm(boolean accion) {
        opcionBusqueda = new OpcionBusqueda();
        thTipoRol = new ThTipoRol();
        thTipoRol.setPeriodo(opcionBusqueda.getAnio());
        view = false;
        listaPeriodo = catalogoItemService.getPeriodo();
        listaMeses = catalogoItemService.findByCatalogo("meses_anio");
        listaTipos = catalogoItemService.findByCatalogo("tipo_rol");
        if (accion) {
            JsfUtil.executeJS("PF('thTipoRolDlg').hide()");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void actualizarDatos() {
        if (opcionBusqueda.getAnio() != null) {
            thTipoRolLazyModel = new LazyModel<>(ThTipoRol.class);
            thTipoRolLazyModel.getSorteds().put("idMes.orden", "ASC");
            thTipoRolLazyModel.getFilterss().put("estado", true);
            thTipoRolLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
            thTipoRolLazyModel.setDistinct(false);
        } else {
            thTipoRolLazyModel = null;
        }
    }

    public void form(ThTipoRol thTipoRol, boolean view) {
        cleanForm(false);
        this.view = view;
        if (thTipoRol != null) {
            this.thTipoRol = thTipoRol;
        }
        JsfUtil.executeJS("PF('thTipoRolDlg').show()");
        PrimeFaces.current().ajax().update("thTipoRolForm");
    }

    public void save() {
        Boolean edit = thTipoRol.getId() != null;
        if (thTipoRol.getPeriodo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        if (thTipoRol.getIdMes() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un mes");
            return;
        }
        if (thTipoRol.getIdTipo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un tipo de rol");
            return;
        }
        if (thTipoRol.getIdTipo() == null || thTipoRol.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción");
            return;
        }
        if (edit) {
            thTipoRol.setUserModificacion(userSession.getNameUser());
            thTipoRol.setFechaModificacion(new Date());
            thTipoRolService.edit(thTipoRol);
        } else {
            thTipoRol.setUserCreacion(userSession.getNameUser());
            thTipoRol.setFechaCreacion(new Date());
            thTipoRol = thTipoRolService.create(thTipoRol);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        cleanForm(true);
    }

    public void delete(ThTipoRol thTipoRol) {
        thTipoRol.setUserModificacion(userSession.getNameUser());
        thTipoRol.setFechaModificacion(new Date());
        thTipoRol.setEstado(false);
        thTipoRolService.edit(thTipoRol);
        PrimeFaces.current().ajax().update("thTipoRolTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public LazyModel<ThTipoRol> getThTipoRolLazyModel() {
        return thTipoRolLazyModel;
    }

    public void setThTipoRolLazyModel(LazyModel<ThTipoRol> thTipoRolLazyModel) {
        this.thTipoRolLazyModel = thTipoRolLazyModel;
    }

    public ThTipoRol getThTipoRol() {
        return thTipoRol;
    }

    public void setThTipoRol(ThTipoRol thTipoRol) {
        this.thTipoRol = thTipoRol;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<CatalogoItem> getListaMeses() {
        return listaMeses;
    }

    public void setListaMeses(List<CatalogoItem> listaMeses) {
        this.listaMeses = listaMeses;
    }

    public List<CatalogoItem> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<CatalogoItem> listaTipos) {
        this.listaTipos = listaTipos;
    }

}
