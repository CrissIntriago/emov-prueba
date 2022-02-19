/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
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
 * @author Administrator
 */
@Named
@ViewScoped
public class CatalogoTributarioMB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @Inject
    private CatalogoService catalogoService;
    
    private LazyModel<Catalogo> lazy;
    private Catalogo catalogo;
    private Catalogo catalogoSeleccionado;
    private List<CatalogoItem> listaTabla;
    private CatalogoItem catalogoItem;
    private List<CatalogoItem> listaItem;
    
    @PostConstruct
    public void init() {
        listaItem = new ArrayList<>();
        listaTabla = new ArrayList<>();
        catalogo = new Catalogo();
        lazy = new LazyModel<>(Catalogo.class);
        lazy.getFilterss().put("codigo:startsWith", "GT");
        lazy.getSorteds().put("id", "DESC");
        catalogoItem = new CatalogoItem();
    }

    /**
     * este metodo realiza la consulta de todos los items que pertenece a
     * catalgoo que se le pasara por parametro
     *
     * @param i Catalogo seleccionado
     */
    public void cargandoListaItmes(Catalogo i) {
        if (i == null) {
            this.listaTabla = null;
        } else {
            this.listaTabla = catalogoService.MostarTodoCatalogoItems(i);
            listaTabla.forEach((catalogoItem1) -> {
                catalogoItem1.setEstado(true);
                listaItem.add(catalogoItem1);
            });
        }
    }

    /*
     sirve para abrir el modal para poder ingresar
     los datos del catalogo Item
     */
    public void fmAddAniadiritem(CatalogoItem c) {
        
        if (c != null) {
            this.catalogoItem = c;
            
        } else {
            this.catalogoItem = new CatalogoItem();
            
        }
        PrimeFaces.current().executeScript("PF('DLgitem').show()");
        PrimeFaces.current().ajax().update("formitem");
        
    }

    /*
     añade elemenotos de tipo cataloItem a la lita
     y para editar un elemento de la lista
     */
    public void aniadirCatalogoItem() {
        
        boolean edit = catalogoItem.getId() != null;
        if (!edit) {
            catalogoItem.setCatalogo(catalogo);
            catalogoItem.setEstado(true);
            this.listaItem.add(catalogoItem);
        } else {
            catalogoItem.setCatalogo(catalogo);
            this.listaItem.add(this.listaItem.indexOf(catalogoItem), catalogoItem);
            this.listaItem.remove(this.listaItem.indexOf(catalogoItem));
        }
        catalogoItem = new CatalogoItem();
        PrimeFaces.current().executeScript("PF('DLgitem').hide()");
        PrimeFaces.current().ajax().update(":formitem");
    }
    
    public void form(Catalogo catalogo) {
        listaItem = new ArrayList<>();
        if (catalogo != null) {
            this.catalogo = catalogo;
            cargandoListaItmes(catalogo);
        } else {
            this.catalogo = new Catalogo();
            cargandoListaItmes(null);
        }
        PrimeFaces.current().executeScript("PF('catalogoDialog').show()");
        PrimeFaces.current().ajax().update(":formCatalogo");
        
    }

    /**/
    public void delete(Catalogo c) {
        catalogoService.remove(c);
        
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Catalogo", c.getNombre() + " eliminada con exito");
        
    }
    
    public void eliminarCatalogoItem(CatalogoItem i) {
        listaItem.remove(i);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Item", " eliminada con exito");
    }
    
    public void save() {
        
        boolean edit = catalogo.getId() != null;
        
        if (!edit) {
            catalogo.setItems(listaItem);
            this.catalogo = catalogoService.create(catalogo);
            
        } else {
            
            catalogo.setItems(listaItem);
            catalogoService.edit(catalogo);
        }
        PrimeFaces.current().executeScript("PF('catalogoDialog').hide()");
        PrimeFaces.current().ajax().update(":formCatalogo");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Informacion", catalogo.getNombre() + (edit ? " editado" : " guardado") + " con exito.");
        
        System.out.println("Guaradado");
        
    }
    
    public void handleCloseForm() {
        PrimeFaces.current().ajax().update("mostrarColumnas");
        PrimeFaces.current().ajax().update("catalogos");
        PrimeFaces.current().ajax().update("formCatalogo");
    }
    
    public LazyModel<Catalogo> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<Catalogo> lazy) {
        this.lazy = lazy;
    }
    
    public Catalogo getCatalogo() {
        return catalogo;
    }
    
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }
    
    public Catalogo getCatalogoSeleccionado() {
        return catalogoSeleccionado;
    }
    
    public void setCatalogoSeleccionado(Catalogo catalogoSeleccionado) {
        this.catalogoSeleccionado = catalogoSeleccionado;
    }
    
    public List<CatalogoItem> getListaTabla() {
        return listaTabla;
    }
    
    public void setListaTabla(List<CatalogoItem> listaTabla) {
        this.listaTabla = listaTabla;
    }
    
    public CatalogoItem getCatalogoItem() {
        return catalogoItem;
    }
    
    public void setCatalogoItem(CatalogoItem catalogoItem) {
        this.catalogoItem = catalogoItem;
    }
    
    public List<CatalogoItem> getListaItem() {
        return listaItem;
    }
    
    public void setListaItem(List<CatalogoItem> listaItem) {
        this.listaItem = listaItem;
    }
    
}
