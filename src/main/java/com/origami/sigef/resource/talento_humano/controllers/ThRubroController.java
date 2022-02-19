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
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.services.ThRubroService;
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
@Named(value = "thRubroView")
@ViewScoped
public class ThRubroController implements Serializable {

    @Inject
    private ThRubroService thRubroService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UserSession userSession;

    private LazyModel<ThRubro> thRubroLazy;

    private List<CatalogoItem> listCatalogo;

    private ThRubro thRubro;

    private Boolean view;

    @PostConstruct
    public void init() {
        this.thRubroLazy = new LazyModel<>(ThRubro.class);
        this.thRubroLazy.getSorteds().put("nombre", "ASC");
        this.thRubroLazy.getFilterss().put("estado", true);
        cleanForm();
    }

    public void updateTipo() {
        if (thRubro.getIngreso()) {
            thRubro.setOrigen(true);
            listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
        } else {
            thRubro.setOrigen(null);
            listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
        }
    }

    public void form(ThRubro thRubro, Boolean view) {
        this.view = view;
        if (thRubro != null) {
            this.thRubro = thRubro;
        } else {
            this.thRubro = new ThRubro();
        }
        updateTipo();
        JsfUtil.executeJS("PF('thRubroDlg').show()");
        PrimeFaces.current().ajax().update("thRubroForm");
    }

    public void save() {
        boolean edit = thRubro.getId() != null;
        if (thRubro.getNombre() == null || thRubro.getNombre().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción");
            return;
        }
        if (thRubro.getValor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un valor");
            return;
        }
        if (thRubro.getClasificacion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una clasificación");
            return;
        }
        if (!thRubro.getTipoValor() && thRubro.getPorcentaje()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede guardar un valor que sea de valor y de porcentaje");
            return;
        }
        if (edit) {
            thRubro.setUserModificacion(userSession.getNameUser());
            thRubro.setFechaModificacion(new Date());
            thRubroService.edit(thRubro);
        } else {
            thRubro.setUserCreacion(userSession.getNameUser());
            thRubro.setFechaCreacion(new Date());
            thRubro = thRubroService.create(thRubro);
        }
        JsfUtil.executeJS("PF('thRubroDlg').hide()");
        PrimeFaces.current().ajax().update("thRubroForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void delete(ThRubro thRubro) {
        thRubro.setEstado(false);
        thRubroService.edit(thRubro);
        PrimeFaces.current().ajax().update("thRubroTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public void closeForm() {
        thRubro = new ThRubro();
        cleanForm();
        JsfUtil.executeJS("PF('thRubroDlg').hide()");
    }

    private void cleanForm() {
        thRubro = new ThRubro();
    }

    public LazyModel<ThRubro> getThRubroLazy() {
        return thRubroLazy;
    }

    public void setThRubroLazy(LazyModel<ThRubro> thRubroLazy) {
        this.thRubroLazy = thRubroLazy;
    }

    public ThRubro getThRubro() {
        return thRubro;
    }

    public void setThRubro(ThRubro thRubro) {
        this.thRubro = thRubro;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

}
