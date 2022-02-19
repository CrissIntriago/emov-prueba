/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.ThConfigHorasExtra;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThConfigHorasExtraService;
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
@Named(value = "thConfigHorasExtraView")
@ViewScoped
public class ThConfigHorasExtraController implements Serializable {
    
    @Inject
    private ThConfigHorasExtraService thConfigHorasExtraService;
    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private CatalogoItemService catalogoItemService;
    
    private ThConfigHorasExtra thConfigHorasExtra;
    
    private List<CatalogoItem> clasificaciones;
    private List<CatalogoItem> tipos;
    
    private LazyModel<ThConfigHorasExtra> thConfigHorasExtraLazy;
    
    Boolean view;
    
    @PostConstruct
    public void init() {
        thConfigHorasExtraLazy = new LazyModel<>(ThConfigHorasExtra.class);
        thConfigHorasExtraLazy.addSorted("idClasificacion.texto", "ASC");
        thConfigHorasExtraLazy.addSorted("idTipo.texto", "ASC");
        thConfigHorasExtraLazy.addSorted("porcentaje", "ASC");
        thConfigHorasExtraLazy.getFilterss().put("estado", true);
        thConfigHorasExtraLazy.setDistinct(false);
        clasificaciones = catalogoItemService.findByCatalogo("tipoCargo");
        tipos = catalogoItemService.findByCatalogo("tipo_hora_extra");
        cleanForm();
    }
    
    public void cleanForm() {
        thConfigHorasExtra = new ThConfigHorasExtra();
    }
    
    public void form(ThConfigHorasExtra parameter, Boolean view) {
        this.view = view;
        if (parameter != null) {
            this.thConfigHorasExtra = parameter;
        } else {
            this.thConfigHorasExtra = new ThConfigHorasExtra();
        }
        JsfUtil.executeJS("PF('thConfigHorasExtraDlg').show()");
        PrimeFaces.current().ajax().update("thConfigHorasExtraForm");
    }
    
    public void closeForm() {
        cleanForm();
        PrimeFaces.current().ajax().update("thConfigHorasExtraForm");
        JsfUtil.executeJS("PF('thConfigHorasExtraDlg').hide()");
    }
    
    public void save() {
        Boolean edit = thConfigHorasExtra.getId() != null;
        String msj = validaciones();
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        if (edit) {
            edit();
        } else {
            thConfigHorasExtra.setUsuarioCreacion(thInterfaces.getUser());
            thConfigHorasExtra.setFechaCreacion(new Date());
            thConfigHorasExtraService.create(thConfigHorasExtra);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        cleanForm();
        JsfUtil.executeJS("PF('thConfigHorasExtraDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigHorasExtraTable");
    }
    
    public void delete(ThConfigHorasExtra parameter) {
        parameter.setEstado(Boolean.FALSE);
        edit();
        JsfUtil.addErrorMessage("INFO!!!", "Se elimino correctamente");
    }
    
    public void edit() {
        thConfigHorasExtra.setUsuarioModificacion(thInterfaces.getUser());
        thConfigHorasExtra.setFechaModificacion(new Date());
        thConfigHorasExtraService.edit(thConfigHorasExtra);
    }
    
    public String validaciones() {
        String result = "";
        if (thConfigHorasExtra.getIdClasificacion() == null) {
            return "Debe seleccionar una calisifación";
        }
        if (thConfigHorasExtra.getIdTipo() == null) {
            return "Debe seleccionar un tipo";
        }
        if (thConfigHorasExtra.getPorcentaje() <= 0) {
            return "Debe ingresar un porcenjae mayor a 0";
        }
        if (thConfigHorasExtra.getMaxHoras() <= 0) {
            return "Debe ingresar un maximo de horas";
        }
        if (thConfigHorasExtra.getIndice().doubleValue() <= 0) {
            return "Debe ingresar el valor del indice a calcular";
        }
        List<ThConfigHorasExtra> aux = thConfigHorasExtraService.findByNamedQuery("ThConfigHorasExtra.validar", thConfigHorasExtra.getIdClasificacion(), thConfigHorasExtra.getIdTipo(),
                thConfigHorasExtra.getPorcentaje(), thConfigHorasExtra.getMaxHoras(), thConfigHorasExtra.getIndice());
        if (!aux.isEmpty()) {
            return "Ya existe un registro con los parametros ingresados";
        }
        return result;
    }
    
    public ThConfigHorasExtra getThConfigHorasExtra() {
        return thConfigHorasExtra;
    }
    
    public void setThConfigHorasExtra(ThConfigHorasExtra thConfigHorasExtra) {
        this.thConfigHorasExtra = thConfigHorasExtra;
    }
    
    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }
    
    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }
    
    public List<CatalogoItem> getTipos() {
        return tipos;
    }
    
    public void setTipos(List<CatalogoItem> tipos) {
        this.tipos = tipos;
    }
    
    public LazyModel<ThConfigHorasExtra> getThConfigHorasExtraLazy() {
        return thConfigHorasExtraLazy;
    }
    
    public void setThConfigHorasExtraLazy(LazyModel<ThConfigHorasExtra> thConfigHorasExtraLazy) {
        this.thConfigHorasExtraLazy = thConfigHorasExtraLazy;
    }
    
    public Boolean getView() {
        return view;
    }
    
    public void setView(Boolean view) {
        this.view = view;
    }
    
}
