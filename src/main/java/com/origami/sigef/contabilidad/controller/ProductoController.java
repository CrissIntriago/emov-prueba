/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.lazy.ActividadOperativaLazy;
import com.origami.sigef.contabilidad.lazy.ProductoLazy;
import com.origami.sigef.contabilidad.service.ProductoService;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Crizz Intriago
 */
@Named(value = "productoView")
@ViewScoped
public class ProductoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private ProductoService productoService;
    @Inject
    private CatalogoService catalogoService;

    private ProductoLazy lazy;
    private Producto producto;
    private PlanProgramatico planProgramatico;
    private FuenteFinanciamiento fuente;
    private CatalogoPresupuesto item;
    private List<CatalogoItem> clasificacion;
    private ActividadOperativaLazy lazyActividadOperativas;
    private ProductoLazy productoLazy;

    @PostConstruct
    public void inicializate() {
        this.producto = new Producto();
        this.lazy = new ProductoLazy();
        clasificacion = catalogoService.getItemsByCatalogo("tipo_fuente_financiamiento");
    }

    public void form(Producto p) {
        if (p != null) {
            this.producto = p;
        } else {
            this.producto = new Producto();
        }
        PrimeFaces.current().ajax().update(":formProducto");
    }

    public void showDlgPlan() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
        PrimeFaces.current().dialog().openDynamic("/common/Dialog/dlgPlanProg", options, null);
    }

//    public void selectionPlan(SelectEvent event) {
//        this.planProgramatico = (PlanProgramatico) event.getObject();
//        producto.setEstructuraProgramatica(planProgramatico);
//        PrimeFaces.current().ajax().update(":gridCodPlan");
//        PrimeFaces.current().ajax().update(":gridNomPlan");
//    }
    public void showDlgFuente() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 540);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        PrimeFaces.current().dialog().openDynamic("/common/Dialog/dlgFuente", options, null);
    }

//    public void selectionFuente(SelectEvent event) {
//        this.fuente = (FuenteFinanciamiento) event.getObject();
//        producto.setFuente(fuente);
//        PrimeFaces.current().ajax().update(":gridCodFuente");
//        PrimeFaces.current().ajax().update(":gridNomFuente");
//    }
    public void showDlgItem() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 540);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        PrimeFaces.current().dialog().openDynamic("/common/Dialog/dlgItem", options, null);
    }

//    public void selectionItem(SelectEvent event) {
//        this.item = (CatalogoPresupuesto) event.getObject();
//        producto.setItemPresupuestario(item);
//        PrimeFaces.current().ajax().update(":gridCodItem");
//        PrimeFaces.current().ajax().update(":gridNomItem");
//    }
    public void save() {

        boolean edit = producto.getId() != null;
        if (!edit) {
            producto.setUsuarioCreacion("system");
            producto.setFechaCreacion(new Date());
            producto.setUsuarioModifica("system");
            producto.setFechaModificacion(new Date());
            this.producto = productoService.create(producto);
        } else {
            this.productoService.edit(producto);
            producto.setUsuarioModifica("system");
            producto.setFechaModificacion(new Date());
        }
        PrimeFaces.current().ajax().update(":productos");
        PrimeFaces.current().ajax().update(":formProducto");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Producto", producto.getDescripcion() + (edit ? " editada" : " registrada") + " con éxito.");
        this.producto = new Producto();

    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public void delete(Producto p) {
        p.setEstado(Boolean.FALSE);
        productoService.edit(p);
        JsfUtil.addSuccessMessage("Producto", p.getDescripcion() + " eliminada con éxito");
        PrimeFaces.current().ajax().update("productos");
        PrimeFaces.current().ajax().update("messages");
    }

    public ProductoService getProductoService() {
        return productoService;
    }

    public PlanProgramatico getPlanProgramatico() {
        return planProgramatico;
    }

    public void setPlanProgramatico(PlanProgramatico planProgramatico) {
        this.planProgramatico = planProgramatico;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public List<CatalogoItem> getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(List<CatalogoItem> clasificacion) {
        this.clasificacion = clasificacion;
    }

    public void setProductoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public ProductoLazy getLazy() {
        return lazy;
    }

    public void setLazy(ProductoLazy lazy) {
        this.lazy = lazy;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public CatalogoPresupuesto getItem() {
        return item;
    }

    public void setItem(CatalogoPresupuesto item) {
        this.item = item;
    }
}
