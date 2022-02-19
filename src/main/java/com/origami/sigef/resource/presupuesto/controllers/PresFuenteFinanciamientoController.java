/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
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
@Named(value = "presFuenteFinanciamientoView")
@ViewScoped
public class PresFuenteFinanciamientoController implements Serializable {

    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanciamientoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<PresFuenteFinanciamiento> presFuenteFinanciamientoLazy;

    private List<CatalogoItem> tipoFuenteList;

    private PresFuenteFinanciamiento presFuenteFinanciamiento;

    private boolean editView;

    @PostConstruct
    public void init() {
        presFuenteFinanciamientoLazy = new LazyModel<>(PresFuenteFinanciamiento.class);
        presFuenteFinanciamientoLazy.getSorteds().put("codFuente", "ASC");
        tipoFuenteList = catalogoItemService.findByCatalogo("tipo_fuente_financiamiento");
        closeForm(false);
    }

    public void form(PresFuenteFinanciamiento presFuenteFinanciamiento, Boolean view) {
        this.editView = view;
        closeForm(false);
        if (presFuenteFinanciamiento != null) {
            this.presFuenteFinanciamiento = presFuenteFinanciamiento;
        }
        JsfUtil.executeJS("PF('fuenteFinanciamientoDlg').show()");
        PrimeFaces.current().ajax().update("fuenteFinanciamientoForm");
    }

    public void save() {
        boolean edit = presFuenteFinanciamiento.getId() != null;
        if (presFuenteFinanciamiento.getNombre() == null || presFuenteFinanciamiento.getNombre().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Ingrese una descripción");
            return;
        }
        if (presFuenteFinanciamiento.getTipoFuente() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un tipo de financiamiento");
            return;
        }
        presFuenteFinanciamiento.setCodFuente(presFuenteFinanciamiento.getTipoFuente().getOrden().toString());
        if (edit) {
            presFuenteFinanciamiento.setUsuarioModifica(userSession.getNameUser());
            presFuenteFinanciamiento.setFechaModificacion(new Date());
            presFuenteFinanciamientoService.edit(presFuenteFinanciamiento);
        } else {
            presFuenteFinanciamiento.setUsuarioCreacion(userSession.getNameUser());
            presFuenteFinanciamiento.setFechaCreacion(new Date());
            presFuenteFinanciamientoService.create(presFuenteFinanciamiento);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm(true);
        PrimeFaces.current().ajax().update("fuenteFinanciamientoTable");
    }

    public void closeForm(boolean accion) {
        presFuenteFinanciamiento = new PresFuenteFinanciamiento();
        if (accion) {
            JsfUtil.executeJS("PF('fuenteFinanciamientoDlg').hide()");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void desactivarFuenteFinanciamiento(PresFuenteFinanciamiento presFuenteFinanciamiento) {
        presFuenteFinanciamiento.setEstado(false);
        presFuenteFinanciamiento.setUsuarioModifica(userSession.getNameUser());
        presFuenteFinanciamiento.setFechaModificacion(new Date());
        presFuenteFinanciamientoService.edit(presFuenteFinanciamiento);
        JsfUtil.addInformationMessage("INFO!!!", "Se ha desactivado correctamente");
        PrimeFaces.current().ajax().update("fuenteFinanciamientoTable");
    }

    public LazyModel<PresFuenteFinanciamiento> getPresFuenteFinanciamientoLazy() {
        return presFuenteFinanciamientoLazy;
    }

    public void setPresFuenteFinanciamientoLazy(LazyModel<PresFuenteFinanciamiento> presFuenteFinanciamientoLazy) {
        this.presFuenteFinanciamientoLazy = presFuenteFinanciamientoLazy;
    }

    public PresFuenteFinanciamiento getPresFuenteFinanciamiento() {
        return presFuenteFinanciamiento;
    }

    public void setPresFuenteFinanciamiento(PresFuenteFinanciamiento presFuenteFinanciamiento) {
        this.presFuenteFinanciamiento = presFuenteFinanciamiento;
    }

    public boolean isEditView() {
        return editView;
    }

    public void setEditView(boolean editView) {
        this.editView = editView;
    }

    public List<CatalogoItem> getTipoFuenteList() {
        return tipoFuenteList;
    }

    public void setTipoFuenteList(List<CatalogoItem> tipoFuenteList) {
        this.tipoFuenteList = tipoFuenteList;
    }

}
