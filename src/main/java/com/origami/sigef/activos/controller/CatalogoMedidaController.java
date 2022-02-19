/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoMedidaService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMedida;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Erwin Rivas
 */
@Named(value = "catalogoMedidaController")
@ViewScoped
public class CatalogoMedidaController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyModel<CatalogoMedida> lazyMedida;
    private List<CatalogoItem> catalogoItem;
    private CatalogoMedida catalogoMedida;
    private Boolean nuevo = false;
    private Boolean tmedida = false;

    @Inject
    private CatalogoMedidaService catalogoMedidaService;

    @Inject
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void initView() {
        lazyMedida = new LazyModel<>(CatalogoMedida.class);
        catalogoItem = catalogoItemService.findCatalogoItems("tipo_medida");
        this.catalogoMedida = new CatalogoMedida();
        lazyMedida.getFilterss().put("estado", true);
        lazyMedida.setDistinct(false);
        lazyMedida.getSorteds().put("descripcion", "ASC");
    }

    public void form(CatalogoMedida medida) {

        if (medida == null) {
            catalogoMedida = new CatalogoMedida();
            tmedida = false;
            nuevo = true;
        } else {
            this.catalogoMedida = medida;
            tmedida = true;
            nuevo = false;
        }
        catalogoMedida.setEstado(true);
        PrimeFaces.current().executeScript("PF('dlgMedida').show()");
    }

    public void guardar() {
        if (catalogoMedida.getDescripcion() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (catalogoMedida.getTipoMedida() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Error", "Debe ingresar un tipo de medida");
            return;
        }
        catalogoMedida.setEstado(true);
        CatalogoMedida temp = catalogoMedidaService.create(catalogoMedida);
        if (temp != null) {
            catalogoMedida = new CatalogoMedida();
            PrimeFaces.current().executeScript("PF('dlgMedida').hide();");
            JsfUtil.addSuccessMessage("Info", "Medida guardada correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar la medida.");
        }
    }

    public void editar() {
        if (catalogoMedida.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (catalogoMedida.getTipoMedida() == null || "Seleccione".equals((catalogoMedida.getTipoMedida()).toString())) {
            JsfUtil.addErrorMessage("Error", "Debe escoger un tipo de medida");
            return;
        }
        catalogoMedidaService.edit(catalogoMedida);
        catalogoMedida = new CatalogoMedida();
        PrimeFaces.current().executeScript("PF('dlgMedida').hide();");
        JsfUtil.addSuccessMessage("Info", "Medida editada correctamente");
    }

    public void eliminar(CatalogoMedida medida) {
        List<DetalleItem> listItem = catalogoMedidaService.verificarEnDetalleItem(medida);
        if (Utils.isNotEmpty(listItem)) {
            JsfUtil.addWarningMessage("Advertencia", "No se puede eliminar, el tipo de medida está siendo usado por un item de inventario");
            return;
        }
        medida.setEstado(Boolean.FALSE);
        catalogoMedidaService.edit(medida);
        JsfUtil.addSuccessMessage("Info", "Medida eliminada correctamente");
    }

    public void limpiarVariable() {
        nuevo = false;
        tmedida = false;
        catalogoMedida = new CatalogoMedida();
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS AND SETTERS">
    public CatalogoMedida getCatalogoMedida() {
        return catalogoMedida;
    }

    public void setCatalogoMedida(CatalogoMedida catalogoMedida) {
        this.catalogoMedida = catalogoMedida;
    }

    public LazyModel<CatalogoMedida> getLazyMedida() {
        return lazyMedida;
    }

    public void setLazyMedida(LazyModel<CatalogoMedida> lazyMedida) {
        this.lazyMedida = lazyMedida;
    }

    public CatalogoMedidaService getCatalogoMedidaService() {
        return catalogoMedidaService;
    }

    public void setCatalogoMedidaService(CatalogoMedidaService catalogoMedidaService) {
        this.catalogoMedidaService = catalogoMedidaService;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getTmedida() {
        return tmedida;
    }

    public void setTmedida(Boolean tmedida) {
        this.tmedida = tmedida;
    }

    public List<CatalogoItem> getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(List<CatalogoItem> catalogoItem) {
        this.catalogoItem = catalogoItem;
    }
//</editor-fold>
}
