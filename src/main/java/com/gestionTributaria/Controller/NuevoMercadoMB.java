/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CategoriaMercado;
import com.gestionTributaria.Services.CategoriaMercadoService;
import com.gestionTributaria.Services.MercadoService;
import com.origami.sigef.arrendamiento.entities.DetalleMercado;
import com.origami.sigef.arrendamiento.service.DetalleMercadoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class NuevoMercadoMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(NuevoMercadoMB.class.getName());
    
    @Inject
    private MercadoService mercadoService;
    @Inject
    private CategoriaMercadoService categoriaMercadoService;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private UserSession ss;
    @Inject
    private DetalleMercadoService detalleMercadoService;
    
    private LazyModel<Mercado> lazy;
    private Mercado mercado;
    private List<DetalleMercado> detalleMercado;
    private DetalleMercado detMer;
    private CategoriaMercado categoriaMc;
    private List<CatalogoItem> categorias;
    private CatalogoItem categoria;
    
    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(Mercado.class);
        lazy.getFilterss().put("estado", Boolean.TRUE);
        detalleMercado = new ArrayList<>();
        detMer = new DetalleMercado();
        mercado = new Mercado();
        categorias = catalogoService.findCatalogoItemByCodigoPadre("mercado_categoria");
    }
    
    public void saveEdit() {
        if (mercado.getNombre() == null || mercado.getNombre().length() <= 0) {
            JsfUtil.addWarningMessage("ERROR", "DEBE AGREGAR DATOS...");
            return;
        }
        if (mercado.getId() == null) {
            mercado.setUsuarioCreacion(ss.getNameUser());
            mercado.setFechaCreacion(new Date());
            mercadoService.create(mercado);
            JsfUtil.addSuccessMessage("informacion", "Mercado Creado con Exito");
        } else {
            mercado.setUsuarioModificacion(ss.getNameUser());
            mercado.setFechaModificacion(new Date());
            mercadoService.edit(mercado);
            JsfUtil.addSuccessMessage("informacion", "Mercado Editado con Exito");
        }
        if (Utils.isNotEmpty(this.detalleMercado)) {
            for (DetalleMercado dt : this.detalleMercado) {
                if (dt.getId() == null) {
                    dt.setMercado(mercado);
                    dt.setUsuarioIngreso(ss.getNameUser());
                    dt.setFechaIngreso(new Date());
                    detalleMercadoService.create(dt);
                } else {
                    dt.setUsuarioModifica(ss.getNameUser());
                    dt.setFechaModifica(new Date());
                    detalleMercadoService.edit(dt);
                }
            }
        }
        this.init();
        JsfUtil.update("mainForm");
    }
    
    public void eliminar(Mercado m) {
        m.setEstado(Boolean.FALSE);
        mercadoService.edit(m);
    }
    
    public void editar(Mercado m) {
        mercado = new Mercado();
        mercado = m;
        this.detalleMercado = m.getDetalleMercado();
    }
    
    public void openDetMercado() {
        detMer = new DetalleMercado();
        JsfUtil.executeJS("PF('dlgDetMercado').show()");
        JsfUtil.update("formdetMercado");
    }
    
    public void guardarDetalle() {
        this.detalleMercado.add(detMer);
        System.out.println("detalle>>" + detalleMercado.size());
        JsfUtil.executeJS("PF('dlgDetMercado').hide()");
        JsfUtil.update("formdetMercado");
        JsfUtil.addSuccessMessage("Detalle", "a sido agregado con exito...");
    }
    
    public LazyModel<Mercado> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<Mercado> lazy) {
        this.lazy = lazy;
    }
    
    public Mercado getMercado() {
        return mercado;
    }
    
    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
    }
    
    public List<CatalogoItem> getCategorias() {
        return categorias;
    }
    
    public void setCategorias(List<CatalogoItem> categorias) {
        this.categorias = categorias;
    }
    
    public CatalogoItem getCategoria() {
        return categoria;
    }
    
    public void setCategoria(CatalogoItem categoria) {
        this.categoria = categoria;
    }
    
    public List<DetalleMercado> getDetalleMercado() {
        return detalleMercado;
    }
    
    public void setDetalleMercado(List<DetalleMercado> detalleMercado) {
        this.detalleMercado = detalleMercado;
    }
    
    public DetalleMercado getDetMer() {
        return detMer;
    }
    
    public void setDetMer(DetalleMercado detMer) {
        this.detMer = detMer;
    }
    
}
