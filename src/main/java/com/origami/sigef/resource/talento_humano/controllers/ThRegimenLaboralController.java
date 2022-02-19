/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.services.ThRegimenLaboralService;
import java.io.Serializable;
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
@Named(value = "thRegimenLaboralView")
@ViewScoped
public class ThRegimenLaboralController implements Serializable {

    @Inject
    private ThRegimenLaboralService thRegimenLaboralService;

    private LazyModel<ThRegimenLaboral> thRegimenLaboralLazy;

    private ThRegimenLaboral thRegimenLaboral;

    private Boolean view;

    @PostConstruct
    public void init() {
        this.thRegimenLaboralLazy = new LazyModel<>(ThRegimenLaboral.class);
        this.thRegimenLaboralLazy.getSorteds().put("padre", "DESC");
        this.thRegimenLaboralLazy.getFilterss().put("estado", true);
        cleanForm();
    }

    public void form(ThRegimenLaboral thRegimenLaboral, Boolean view) {
        this.view = view;
        if (thRegimenLaboral != null) {
            this.thRegimenLaboral = thRegimenLaboral;
        } else {
            cleanForm();
        }
        JsfUtil.executeJS("PF('thRegimenLaboralDlg').show()");
        PrimeFaces.current().ajax().update("thRegimenLaboralForm");
    }

    public void add(ThRegimenLaboral thRegimenLaboral) {
        this.view = false;
        this.thRegimenLaboral = new ThRegimenLaboral(thRegimenLaboral);
        JsfUtil.executeJS("PF('thRegimenLaboralDlg').show()");
        PrimeFaces.current().ajax().update("thRegimenLaboralForm");
    }

    public void save() {
        boolean edit = thRegimenLaboral.getId() != null;
        if (thRegimenLaboral.getNombre() == null || thRegimenLaboral.getNombre().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un nombre");
            return;
        }
        if (thRegimenLaboral.getCodigo() == null || thRegimenLaboral.getCodigo().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un codigo");
            return;
        }
        if (thRegimenLaboralService.findCode(thRegimenLaboral.getCodigo())) {
            JsfUtil.addWarningMessage("AVISO!!!", "El código ya se encuentra registrado");
            return;
        }
        if (edit) {
            thRegimenLaboralService.edit(thRegimenLaboral);
        } else {
            thRegimenLaboral = thRegimenLaboralService.create(thRegimenLaboral);
        }
        JsfUtil.executeJS("PF('thRegimenLaboralDlg').hide()");
        PrimeFaces.current().ajax().update("thRegimenLaboralForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void delete(ThRegimenLaboral thRegimenLaboral) {
        if (getList(thRegimenLaboral).isEmpty()) {
            thRegimenLaboral.setEstado(false);
            thRegimenLaboralService.edit(thRegimenLaboral);
            PrimeFaces.current().ajax().update("thRegimenLaboralTable");
            JsfUtil.addSuccessMessage("INFO!!", "Se ha eliminado correctamente");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar debido a que tiene registros asociados");
        }
    }

    public void closeForm() {
        thRegimenLaboral = new ThRegimenLaboral();
        cleanForm();
        JsfUtil.executeJS("PF('thRegimenLaboralDlg').hide()");
    }

    private void cleanForm() {
        thRegimenLaboral = new ThRegimenLaboral();
    }

    public List<ThRegimenLaboral> getList(ThRegimenLaboral thRegimenLaboral) {
        return thRegimenLaboralService.getListEscala(thRegimenLaboral);
    }

    public LazyModel<ThRegimenLaboral> getThRegimenLaboralLazy() {
        return thRegimenLaboralLazy;
    }

    public void setThRegimenLaboralLazy(LazyModel<ThRegimenLaboral> thRegimenLaboralLazy) {
        this.thRegimenLaboralLazy = thRegimenLaboralLazy;
    }

    public ThRegimenLaboral getThRegimenLaboral() {
        return thRegimenLaboral;
    }

    public void setThRegimenLaboral(ThRegimenLaboral thRegimenLaboral) {
        this.thRegimenLaboral = thRegimenLaboral;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
