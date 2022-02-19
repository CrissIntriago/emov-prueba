/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "grupoNivelesView")
@ViewScoped
public class GrupoNivelesController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(GrupoNivelesController.class.getName());
    
    private GrupoNiveles grupoNiveles;
    private LazyModel<GrupoNiveles> lazyGrupoNiveles;
    private Boolean nuevo = false;
    private Boolean iNivel = false;
    private GrupoNiveles grupoSeleccionado;
    private Utils util;
    
    @Inject
    private GrupoNivelesService grupoNivelesService;
    @Inject
    private NivelService nivelService;
    
    @PostConstruct
    public void initView() {
        this.grupoNiveles = new GrupoNiveles();
        lazyGrupoNiveles = new LazyModel<>(GrupoNiveles.class);
        lazyGrupoNiveles.getFilterss().put("estado", true);
        lazyGrupoNiveles.getSorteds().put("orden", "ASC");
        lazyGrupoNiveles.getFilterss().put("padre:equal", null);
        util = new Utils();
    }
    
    public void frmArea(GrupoNiveles a) {
        if (a == null) {
            grupoNiveles = new GrupoNiveles();
            grupoNiveles.setPadre(null);
            try {
                grupoNiveles.setNivel(this.nivelService.getNivelOrden("tipo_activo", "INV", Short.valueOf("1")));
                Long nivelOrden = grupoNivelesService.getNivelOrden("tipo_activo", "INV", Short.valueOf("1"));
                grupoNiveles.setOrden(nivelOrden);
//                String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", Integer.valueOf(grupoNiveles.getNivel().getLongitud()));
                String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", 3);
                this.grupoNiveles.setCodigo(completarCadenaConCeros);
                grupoNiveles.setEstado(true);
            } catch (NumberFormatException e) {
                LOG.log(Level.SEVERE, null, e);
            }
            nuevo = true;
        } else {
            this.grupoNiveles = a;
            nuevo = false;
        }
        PrimeFaces.current().executeScript("PF('DlgArea').show()");
        PrimeFaces.current().ajax().update("formArea");
    }
    
    public void frmGrupo(GrupoNiveles gnivel, GrupoNiveles padre) {
        if (gnivel == null) {
            grupoNiveles = new GrupoNiveles();
            grupoNiveles.setPadre(padre);
            grupoNiveles.setNivel(this.nivelService.getNivelOrden("tipo_activo", "INV", Short.valueOf((padre.getNivel().getOrden() + 1) + "")));
            Long n = grupoNivelesService.getNivelOrdenSub("tipo_activo", "INV", Short.valueOf((padre.getNivel().getOrden() + 1) + ""), grupoNiveles.getPadre());
            grupoNiveles.setOrden(n);
//            String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", Integer.valueOf(grupoNiveles.getNivel().getLongitud()));
            String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", 3);
            this.grupoNiveles.setCodigo(completarCadenaConCeros);
            grupoNiveles.setEstado(true);
            nuevo = true;
        } else {
            this.grupoNiveles = gnivel;
            nuevo = false;
        }
        PrimeFaces.current().executeScript("PF('DlgGrupo').show()");
        PrimeFaces.current().ajax().update("formArea");
    }
    
    public void frmSubGrupo(GrupoNiveles s, GrupoNiveles padre) {
        if (s == null) {
            grupoNiveles = new GrupoNiveles();
            grupoNiveles.setPadre(padre);
            grupoNiveles.setNivel(this.nivelService.getNivelOrden("tipo_activo", "INV", Short.valueOf((padre.getNivel().getOrden() + 1) + "")));
            Long n = grupoNivelesService.getNivelOrdenSub("tipo_activo", "INV", Short.valueOf((padre.getNivel().getOrden() + 1) + ""), grupoNiveles.getPadre());
            grupoNiveles.setOrden(n);
//            String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", Integer.valueOf(grupoNiveles.getNivel().getLongitud()));
            String completarCadenaConCeros = Utils.completarCadenaConCeros(grupoNiveles.getOrden() + "", 3);            
            this.grupoNiveles.setCodigo(completarCadenaConCeros);
            grupoNiveles.setEstado(true);
            nuevo = true;
        } else {
            this.grupoNiveles = s;
            nuevo = false;
        }
        PrimeFaces.current().executeScript("PF('DlgSubGrupo').show()");
        PrimeFaces.current().ajax().update("formArea");
    }
    
    public void saveArea() {
        try {
            if (!"".equals(grupoNiveles.getNombre()) && !"".equals(grupoNiveles.getDescripcion())) {
                GrupoNiveles temp = grupoNivelesService.create(grupoNiveles);
                if (temp != null) {
                    grupoNiveles = new GrupoNiveles();
                    PrimeFaces.current().executeScript("PF('DlgArea').hide();");
                    PrimeFaces.current().ajax().update("formArea");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addSuccessMessage("Info", "Área guardada correctamente");
                } else {
                    JsfUtil.addErrorMessage("Error", "Ocurrió un error al guardar el área.");
                }
            } else {
                JsfUtil.addWarningMessage("Advertencia", "Complete los datos.");
            }
        } catch (Exception e) {
            PrimeFaces.current().executeScript("PF('DlgArea').show();");
            PrimeFaces.current().ajax().update("formA");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "Los campos no pueden estar vacios");
        }
    }
    
    public void saveGrupo() {
        if (grupoNiveles.getNombre() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el nombre del Grupo");
            return;
        }
        if (grupoNiveles.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una Descripción");
            return;
        }
        GrupoNiveles temp = grupoNivelesService.create(grupoNiveles);
        try {
            if (temp != null) {
                grupoNiveles.setEstado(true);
                grupoNiveles = new GrupoNiveles();
                PrimeFaces.current().executeScript("PF('DlgGrupo').hide();");
                JsfUtil.addSuccessMessage("Info", "Grupo guardada correctamente");
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrió un error al guardar el Grupo.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void saveSubGrupo() {
        if (grupoNiveles.getNombre() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar el nombre del SubGrupo");
            return;
        }
        if (grupoNiveles.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una Descripción");
            return;
        }
        String nombre = grupoNiveles.getNombre().toUpperCase();
        grupoNiveles.setNombre(nombre);
        String descripcion = grupoNiveles.getDescripcion().toUpperCase();
        grupoNiveles.setDescripcion(descripcion);
        GrupoNiveles temp = grupoNivelesService.create(grupoNiveles);
        if (temp != null) {
            grupoNiveles.setEstado(true);
            grupoNiveles = new GrupoNiveles();
            PrimeFaces.current().executeScript("PF('DlgSubGrupo').hide();");
            PrimeFaces.current().ajax().update("DlgSubGrupo");
            JsfUtil.addSuccessMessage("Info", "SubGrupo guardada correctamente");
        } else {
            JsfUtil.addErrorMessage("Error", "Ocurrió un error al guardar el SubGrupo.");
        }
    }
    
    public void editar() {
        if (grupoNiveles.getNombre() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        if (grupoNiveles.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Debe ingresar una descripción");
            return;
        }
        String nombre = grupoNiveles.getNombre().toUpperCase();
        grupoNiveles.setNombre(nombre);
        String descripcion = grupoNiveles.getDescripcion().toUpperCase();
        grupoNiveles.setDescripcion(descripcion);
        grupoNivelesService.edit(grupoNiveles);
        grupoNiveles = new GrupoNiveles();
        PrimeFaces.current().executeScript("PF('DlgArea').hide();");
        PrimeFaces.current().executeScript("PF('DlgGrupo').hide();");
        PrimeFaces.current().executeScript("PF('DlgSubGrupo').hide();");
        JsfUtil.addSuccessMessage("Info", "Registro editado correctamente");
    }
    
    public void eliminar(GrupoNiveles gniveles) {
        if (!gniveles.getGrupoNivelesList().isEmpty()) {
            JsfUtil.addErrorMessage("ADVERTENCIA", "No se puede eliminar el registro de " + gniveles.getNombre() + ", tiene grupos asociados.");
            return;
        }
        gniveles.setEstado(Boolean.FALSE);
        if (gniveles.getPadre() == null) {
            grupoNivelesService.edit(gniveles);
            JsfUtil.addSuccessMessage("Info", "Área eliminada correctamente");
        } else {
            if (gniveles.getNivel().getOrden() == 2) {
                grupoNivelesService.edit(gniveles);
                JsfUtil.addSuccessMessage("Info", "Grupo eliminada correctamente");
            } else {
                if (!gniveles.getDetalleItemList().isEmpty()) {
                    JsfUtil.addErrorMessage("ADVERTENCIA", "No se puede eliminar el registro de " + gniveles.getNombre() + ", tiene items de inventarios asociados.");
                    return;
                }
                grupoNivelesService.edit(gniveles);
                JsfUtil.addSuccessMessage("Info", "SubGrupo eliminada correctamente");
            }
        }
        PrimeFaces.current().ajax().update("formArea");
    }
    
    public boolean verificarSubGrupo(GrupoNiveles g) {
        boolean verificar = true;
        Short sub = grupoNivelesService.getOrdenSubgrupo(g);
        if (sub == 3) {
            verificar = true;
        } else {
            verificar = false;
        }
        return verificar;
    }
    
    public void limpiarVariable() {
        nuevo = false;
        grupoNiveles = new GrupoNiveles();
    }
    
    public void frmGrupo(GrupoNiveles gnivel) {
        if (gnivel == null) {
            grupoNiveles = new GrupoNiveles();
            nuevo = true;
        } else {
            this.grupoNiveles = gnivel;
            nuevo = false;
        }
        grupoNiveles.setEstado(true);
        PrimeFaces.current().executeScript("PF('DlgGrupo').show()");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public GrupoNiveles getGrupoNiveles() {
        return grupoNiveles;
    }
    
    public void setGrupoNiveles(GrupoNiveles grupoNiveles) {
        this.grupoNiveles = grupoNiveles;
    }
    
    public LazyModel<GrupoNiveles> getLazyGrupoNiveles() {
        return lazyGrupoNiveles;
    }
    
    public void setLazyGrupoNiveles(LazyModel<GrupoNiveles> lazyGrupoNiveles) {
        this.lazyGrupoNiveles = lazyGrupoNiveles;
    }
    
    public Boolean getNuevo() {
        return nuevo;
    }
    
    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }
    
    public GrupoNiveles getGrupoSeleccionado() {
        return grupoSeleccionado;
    }
    
    public void setGrupoSeleccionado(GrupoNiveles grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }
    
    public Utils getUtil() {
        return util;
    }
    
    public void setUtil(Utils util) {
        this.util = util;
    }
    
    public Boolean getiNivel() {
        return iNivel;
    }
    
    public void setiNivel(Boolean iNivel) {
        this.iNivel = iNivel;
    }
//</editor-fold>

}
