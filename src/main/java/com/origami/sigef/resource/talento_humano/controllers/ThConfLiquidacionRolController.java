/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.services.ThConfLiquidacionRolService;
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
 * @author Criss Intriago
 */
@Named(value = "thConfLiquidacionRolView")
@ViewScoped
public class ThConfLiquidacionRolController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThConfLiquidacionRolService thConfLiquidacionRolService;

    private LazyModel<ThServidorCargo> thServidorCargoLazyModel;
    private LazyModel<ThRubro> thRubrosLazyModel;

    private OpcionBusqueda opcionBusqueda;
    private ThServidorCargo thServidorCargo;

    private List<Short> listaPeriodo;
    private List<ThConfLiquidacionRol> thConfLiquidacionRolList, thConfLiquidacionRolListDelete;

    private Boolean view;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
    }

    public void updateLazy() {
        if (opcionBusqueda.getAnio() != null) {
            thServidorCargoLazyModel = new LazyModel<>(ThServidorCargo.class);
            thServidorCargoLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
            thServidorCargoLazyModel.getFilterss().put("activo", true);
            thServidorCargoLazyModel.setDistinct(false);
        } else {
            thServidorCargoLazyModel = null;
        }
        PrimeFaces.current().ajax().update("thCargoServidorTable");
    }

    public void openDlg(ThServidorCargo thServidorCargo, Boolean view) {
        this.view = view;
        this.thServidorCargo = thServidorCargo;
        int aux = thConfLiquidacionRolService.updateValores(thServidorCargo, thServidorCargo.getIdCargo(), opcionBusqueda.getAnio());
        thConfLiquidacionRolList = thConfLiquidacionRolService.findByNamedQuery("ThConfLiquidacionRol.findByRolRubros", thServidorCargo, opcionBusqueda.getAnio());
        thConfLiquidacionRolListDelete = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('thConfLiquidacionRolDlg').show()");
        PrimeFaces.current().ajax().update("thConfLiquidacionRolForm");
        PrimeFaces.current().ajax().update("thConfLiquidacionRolTable");
    }

    public void deleteRubro(ThConfLiquidacionRol detalle, int index) {
        if (detalle.getId() != null) {
            thConfLiquidacionRolList.remove(detalle);
            thConfLiquidacionRolListDelete.add(detalle);
        } else {
            thConfLiquidacionRolList.remove(index);
        }
        PrimeFaces.current().ajax().update("thConfLiquidacionRolTable");
    }

    public void closeDlg() {
        if (!thConfLiquidacionRolList.isEmpty()) {
            for (ThConfLiquidacionRol detalle : thConfLiquidacionRolList) {
                if (detalle.getId() != null) {
                    thConfLiquidacionRolService.edit(detalle);
                } else {
                    thConfLiquidacionRolService.create(detalle);
                }
            }
        }
        if (!thConfLiquidacionRolListDelete.isEmpty()) {
            for (ThConfLiquidacionRol detalle : thConfLiquidacionRolListDelete) {
                detalle.setEstado(Boolean.FALSE);
                thConfLiquidacionRolService.edit(detalle);
            }
        }
        JsfUtil.addSuccessMessage("INFO!!!", "Se guardo los cambios correctamente");
        PrimeFaces.current().executeScript("PF('thConfLiquidacionRolDlg').hide()");
    }

    public void updateLazyRubros(Boolean accion) {
        thRubrosLazyModel = new LazyModel<>(ThRubro.class);
        thRubrosLazyModel.getSorteds().put("nombre", "ASC");
        thRubrosLazyModel.getFilterss().put("estado", true);
        thRubrosLazyModel.getFilterss().put("ingreso", accion);
        thRubrosLazyModel.getFilterss().put("activo", true);
        if (accion) {
            thRubrosLazyModel.getFilterss().put("origen", false);
        }
        PrimeFaces.current().executeScript("PF('thRubrosDlg').show()");
        PrimeFaces.current().ajax().update("thRubrosTable");
    }

    public void selectRubro(ThRubro thRubro) {
        if (!thConfLiquidacionRolList.isEmpty()) {
            for (ThConfLiquidacionRol item : thConfLiquidacionRolList) {
                if (item.getIdRubro().getId().equals(thRubro.getId())) {
                    JsfUtil.addWarningMessage("AVISO!!!", "El rubro seleccionado ya se encuentra en la lista");
                    return;
                }
            }
        }
        ThConfLiquidacionRol thConfLiquidacionRol = new ThConfLiquidacionRol();
        thConfLiquidacionRol.setIdRubro(thRubro);
        thConfLiquidacionRol.setPeriodo(opcionBusqueda.getAnio());
        thConfLiquidacionRol.setIdServidorCargo(thServidorCargo);
        thConfLiquidacionRolList.add(thConfLiquidacionRol);
        PrimeFaces.current().executeScript("PF('thRubrosDlg').hide()");
        PrimeFaces.current().ajax().update("thConfLiquidacionRolTable");
    }

    public LazyModel<ThServidorCargo> getThServidorCargoLazyModel() {
        return thServidorCargoLazyModel;
    }

    public void setThServidorCargoLazyModel(LazyModel<ThServidorCargo> thServidorCargoLazyModel) {
        this.thServidorCargoLazyModel = thServidorCargoLazyModel;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<ThConfLiquidacionRol> getThConfLiquidacionRolList() {
        return thConfLiquidacionRolList;
    }

    public void setThConfLiquidacionRolList(List<ThConfLiquidacionRol> thConfLiquidacionRolList) {
        this.thConfLiquidacionRolList = thConfLiquidacionRolList;
    }

    public ThServidorCargo getThServidorCargo() {
        return thServidorCargo;
    }

    public void setThServidorCargo(ThServidorCargo thServidorCargo) {
        this.thServidorCargo = thServidorCargo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public LazyModel<ThRubro> getThRubrosLazyModel() {
        return thRubrosLazyModel;
    }

    public void setThRubrosLazyModel(LazyModel<ThRubro> thRubrosLazyModel) {
        this.thRubrosLazyModel = thRubrosLazyModel;
    }

}
