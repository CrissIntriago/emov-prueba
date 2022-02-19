/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "catalogoMovimientoController")
@ViewScoped
public class CatalogoMovimientoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyModel<CatalogoMovimiento> lazyMovimiento;
    private List<CatalogoItem> catalogoItem;
    private CatalogoMovimiento catalogoMovimiento;
    private Boolean nuevo = false;

    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;

    @Inject
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void initView() {
        lazyMovimiento = new LazyModel<>(CatalogoMovimiento.class);
        catalogoItem = catalogoItemService.findMovimientoInventario("movimientos_activos_inventarios");
        this.catalogoMovimiento = new CatalogoMovimiento();
        lazyMovimiento.getFilterss().put("estado", true);
        lazyMovimiento.getFilterss().put("tipoMovimientos.catalogo.codigo", "movimientos_activos_inventarios");
    }

    public void form(CatalogoMovimiento movimiento) {
        if (movimiento == null) {
            catalogoMovimiento = new CatalogoMovimiento();
            nuevo = true;
        } else {
            this.catalogoMovimiento = movimiento;
            nuevo = false;
        }
        catalogoMovimiento.setEstado(true);
        PrimeFaces.current().executeScript("PF('dlgMovimiento').show()");
    }

    public void eliminar(CatalogoMovimiento movimiento) {
        movimiento.setEstado(Boolean.FALSE);
        catalogoMovimientoService.edit(movimiento);
        JsfUtil.addSuccessMessage("Info", "Movimiento eliminado correctamente");
    }

    public void guardar() {
        if (catalogoMovimiento.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (catalogoMovimiento.getTipoMovimientos() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar un tipo de movimiento");
            return;
        }
        String descripcion = (catalogoMovimiento.getDescripcion().toUpperCase());
        String nombre = (catalogoMovimiento.getTexto()).toUpperCase();
        catalogoMovimiento.setDescripcion(descripcion);
        catalogoMovimiento.setTexto(nombre);
        catalogoMovimiento.setEstado(true);
        CatalogoMovimiento temp = catalogoMovimientoService.create(catalogoMovimiento);
        if (temp != null) {
            catalogoMovimiento = new CatalogoMovimiento();
            PrimeFaces.current().executeScript("PF('dlgMovimiento').hide();");
            JsfUtil.addSuccessMessage("Info", "Movimiento guardado correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrio un error al guardar el registro.");
        }
    }

    public void editar() {
        if (catalogoMovimiento.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (catalogoMovimiento.getTipoMovimientos() == null || "Seleccione".equals((catalogoMovimiento.getTipoMovimientos()).toString())) {
            JsfUtil.addErrorMessage("Error", "Debe escoger un tipo de movimiento");
            return;
        }
        String descripcion = (catalogoMovimiento.getDescripcion().toUpperCase());
        String nombre = (catalogoMovimiento.getTexto()).toUpperCase();
        catalogoMovimiento.setDescripcion(descripcion);
        catalogoMovimiento.setTexto(nombre);
        catalogoMovimiento.setEstado(true);
        catalogoMovimientoService.edit(catalogoMovimiento);
        catalogoMovimiento = new CatalogoMovimiento();
        PrimeFaces.current().executeScript("PF('dlgMovimiento').hide();");
        JsfUtil.addSuccessMessage("Info", "Movimiento editado correctamente");
    }

    public void limpiarVariable() {
        nuevo = false;
        catalogoMovimiento = new CatalogoMovimiento();
    }

    public LazyModel<CatalogoMovimiento> getLazyMovimiento() {
        return lazyMovimiento;
    }

    public void setLazyMovimiento(LazyModel<CatalogoMovimiento> lazyMovimiento) {
        this.lazyMovimiento = lazyMovimiento;
    }

    public CatalogoMovimiento getCatalogoMovimiento() {
        return catalogoMovimiento;
    }

    public void setCatalogoMovimiento(CatalogoMovimiento catalogoMovimiento) {
        this.catalogoMovimiento = catalogoMovimiento;
    }

    public List<CatalogoItem> getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(List<CatalogoItem> catalogoItem) {
        this.catalogoItem = catalogoItem;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

}
