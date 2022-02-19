/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThPeriodoBeneficiosSociales;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.services.ThPeriodoBeneficiosSocialesService;
import java.io.Serializable;
import java.util.Calendar;
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
@Named(value = "thPeriodoBeneficiosSocialesView")
@ViewScoped
public class ThPeriodoBeneficiosSocialesController implements Serializable {

    @Inject
    private ThPeriodoBeneficiosSocialesService socialesService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UserSession userSession;

    private LazyModel<ThPeriodoBeneficiosSociales> periodoBSLazy;
    private LazyModel<ThRubro> thRubroLazyModel;

    private ThPeriodoBeneficiosSociales periodoBS;
    private OpcionBusqueda opcionBusqueda;
    private Boolean view;

    private List<Short> listaPeriodo;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        opcionBusqueda = new OpcionBusqueda();
        updateLazy();
    }

    public void updateLazy() {
        if (opcionBusqueda.getAnio() != null) {
            periodoBSLazy = new LazyModel<>(ThPeriodoBeneficiosSociales.class);
            periodoBSLazy.getSorteds().put("idRubro.nombre", "ASC");
            periodoBSLazy.getFilterss().put("estado", true);
            periodoBSLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            periodoBSLazy.setDistinct(false);
        } else {
            periodoBSLazy = null;
        }
    }

    public void openDlgRubro() {
        thRubroLazyModel = new LazyModel<>(ThRubro.class);
        thRubroLazyModel.getSorteds().put("nombre", "ASC");
        thRubroLazyModel.getFilterss().put("estado", true);
        thRubroLazyModel.getFilterss().put("activo", true);
        thRubroLazyModel.getFilterss().put("ingreso", true);
        JsfUtil.executeJS("PF('rubrosDlg').show()");
        PrimeFaces.current().ajax().update("rubroTable");
    }

    public void closeDlgRubro(ThRubro thRubro) {
        periodoBS.setIdRubro(thRubro);
        JsfUtil.addSuccessMessage("INFO!!", "Se ha seleccionado el rubro correctamente");
        JsfUtil.executeJS("PF('rubrosDlg').hide()");
        PrimeFaces.current().ajax().update("gridrubro");
    }

    public void form(ThPeriodoBeneficiosSociales periodoBS, Boolean view) {
        this.view = view;
        if (periodoBS != null) {
            this.periodoBS = periodoBS;
        } else {
            this.periodoBS = new ThPeriodoBeneficiosSociales();
            Calendar calendar = Calendar.getInstance();
            calendar.set(opcionBusqueda.getAnio(), 0, 1);
            this.periodoBS.setFechaInicio(calendar.getTime());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(opcionBusqueda.getAnio(), 11, 31);
            this.periodoBS.setFechaFin(calendar1.getTime());
            this.periodoBS.setPeriodo(opcionBusqueda.getAnio());
        }
        JsfUtil.executeJS("PF('periodoBSDlg').show()");
        PrimeFaces.current().ajax().update("periodoBSForm");
    }

    public void save() {
        Boolean edit = periodoBS.getId() != null;
        if (periodoBS.getIdRubro() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un rubro");
            return;
        }
        if (edit) {
            periodoBS.setUsuarioModificacion(userSession.getNameUser());
            periodoBS.setFechaModificacion(new Date());
            socialesService.edit(periodoBS);
        } else {
            periodoBS.setUsuarioCreacion(userSession.getNameUser());
            periodoBS.setFechaCreacion(new Date());
            socialesService.create(periodoBS);
        }
        periodoBS = new ThPeriodoBeneficiosSociales();
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        JsfUtil.executeJS("PF('periodoBSDlg').hide()");
        PrimeFaces.current().ajax().update("periodoBSTable");
    }

    public void delete(ThPeriodoBeneficiosSociales periodoBS) {
        periodoBS.setEstado(Boolean.FALSE);
        periodoBS.setUsuarioModificacion(userSession.getNameUser());
        periodoBS.setFechaModificacion(new Date());
        socialesService.edit(periodoBS);
        JsfUtil.addSuccessMessage("INFO!!!", "Se elimino correctamente");
        PrimeFaces.current().ajax().update("periodoBSTable");
    }

    public LazyModel<ThPeriodoBeneficiosSociales> getPeriodoBSLazy() {
        return periodoBSLazy;
    }

    public void setPeriodoBSLazy(LazyModel<ThPeriodoBeneficiosSociales> periodoBSLazy) {
        this.periodoBSLazy = periodoBSLazy;
    }

    public LazyModel<ThRubro> getThRubroLazyModel() {
        return thRubroLazyModel;
    }

    public void setThRubroLazyModel(LazyModel<ThRubro> thRubroLazyModel) {
        this.thRubroLazyModel = thRubroLazyModel;
    }

    public ThPeriodoBeneficiosSociales getPeriodoBS() {
        return periodoBS;
    }

    public void setPeriodoBS(ThPeriodoBeneficiosSociales periodoBS) {
        this.periodoBS = periodoBS;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

}
