/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.controller;

import com.origami.sigef.arrendamiento.service.LocalesService;
import com.origami.sigef.arrendamiento.entities.Locales;
import com.origami.sigef.arrendamiento.entities.TarifasArriendo;
import com.origami.sigef.arrendamiento.service.TarifasArriendoService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
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
@Named(value = "localesView")
@ViewScoped
public class LocalesController implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private LocalesService localesService;
    @Inject
    private TarifasArriendoService tarifasArriendoService;

    private Locales locales;

    private LazyModel<Locales> localesLazyModel;

    private Boolean ver;
    private Boolean required;

    private List<TarifasArriendo> tarifasList;

    @PostConstruct
    public void initialize() {
        this.locales = new Locales();
        this.localesLazyModel = new LazyModel<>(Locales.class);
        this.localesLazyModel.getSorteds().put("numeroLocal", "ASC");
        this.localesLazyModel.getFilterss().put("estado", true);
        this.tarifasList = tarifasArriendoService.getTarifasList();
    }

    public void form(Locales local, Boolean accion) {
        this.ver = accion;
        this.locales = new Locales();
        if (local != null) {
            this.locales = local;
            dimensionesRequeridas();
        }
        PrimeFaces.current().executeScript("PF('localesDlg').show()");
        PrimeFaces.current().ajax().update("formLocales");
    }

    public void saveEdit() {
        if (locales.getIdTarifa().getAreaDesde() != null && locales.getIdTarifa().getAreaHasta() != null) {
            double area = locales.getAncho().doubleValue() * locales.getLargo().doubleValue();
            double areaDesde = locales.getIdTarifa().getAreaDesde().doubleValue();
            double areaHasta = locales.getIdTarifa().getAreaHasta().doubleValue();
            if (area < areaDesde || area > areaHasta) {
                JsfUtil.addWarningMessage("AVISO!!!", "El área del local, no se encuentra en el rango que establece la tarifa seleccionada");
                return;
            } 
        }
        boolean edit = locales.getId() != null;
        if (edit) {
            edit(locales);
        } else {
            locales.setUsuarioCreacion(userSession.getNameUser());
            locales = localesService.create(locales);
        }
        PrimeFaces.current().executeScript("PF('localesDlg').hide()");
        PrimeFaces.current().ajax().update("localesTable");
        JsfUtil.addSuccessMessage("Local", (edit ? "Editado" : " Registrado") + " con éxito.");
    }

    public void delete(Locales locales) {
        if (localesService.getRelacion(locales)) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar porque tiene un arriendo vigente");
            return;
        }
        locales.setEstado(Boolean.FALSE);
        edit(locales);
        JsfUtil.addSuccessMessage("Local", "Eliminada con éxito");
    }

    private void edit(Locales locales) {
        locales.setFechaModificacion(new Date());
        locales.setUsuarioModificacion(userSession.getNameUser());
        localesService.edit(locales);
    }
    
    public void dimensionesRequeridas(){
        if(locales.getIdTarifa()!=null){
            if(locales.getIdTarifa().getAreaDesde()!=null && locales.getIdTarifa().getAreaHasta()!=null){
              required = Boolean.TRUE;  
            }else{
                required = Boolean.FALSE;
            }
        }else{
            required = Boolean.FALSE;
        }
    }

    public Locales getLocales() {
        return locales;
    }

    public void setLocales(Locales locales) {
        this.locales = locales;
    }

    public LazyModel<Locales> getLocalesLazyModel() {
        return localesLazyModel;
    }

    public void setLocalesLazyModel(LazyModel<Locales> localesLazyModel) {
        this.localesLazyModel = localesLazyModel;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public List<TarifasArriendo> getTarifasList() {
        return tarifasList;
    }

    public void setTarifasList(List<TarifasArriendo> tarifasList) {
        this.tarifasList = tarifasList;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

}
