/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThTablaImpuestoRenta;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThTablaImpuestoRentaService;
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
 * @author Jonathan Choez
 */
@Named(value = "thTablaImpuestoRentaView")
@ViewScoped
public class ThTablaImpuestoRentaController implements Serializable {

    @Inject
    private ThTablaImpuestoRentaService thTablaImpuestoRentaService;
    @Inject
    private ThInterfaces thInterfaces;

    private ThTablaImpuestoRenta thTablaImpuestoRenta;
    private OpcionBusqueda opcionBusqueda;

    private List<Short> periodos;

    private LazyModel<ThTablaImpuestoRenta> thTablaImpuestoRentLazy;

    private Boolean view;

    @PostConstruct
    public void init() {
        clean();
        periodos = thInterfaces.getPeriodos();
        opcionBusqueda = new OpcionBusqueda();
        updateLazy();
    }

    private void clean() {
        thTablaImpuestoRenta = new ThTablaImpuestoRenta();
        view = Boolean.TRUE;
    }

    public void updateLazy() {
        if (opcionBusqueda.getAnio() != null) {
            thTablaImpuestoRentLazy = new LazyModel<>(ThTablaImpuestoRenta.class);
            thTablaImpuestoRentLazy.getSorteds().put("fraccionBasica", "ASC");
            thTablaImpuestoRentLazy.getFilterss().put("estado", true);
            thTablaImpuestoRentLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
            thTablaImpuestoRentLazy.setDistinct(false);
        } else {
            thTablaImpuestoRentLazy = null;
        }
    }

    public void form(ThTablaImpuestoRenta thTablaImpuestoRenta, Boolean view) {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        this.view = view;
        if (thTablaImpuestoRenta != null) {
            this.thTablaImpuestoRenta = thTablaImpuestoRenta;
        } else {
            this.thTablaImpuestoRenta = new ThTablaImpuestoRenta();
        }
        JsfUtil.executeJS("PF('thTablaImpuestoRentaDlg').show()");
        PrimeFaces.current().ajax().update("thTablaImpuestoRentaForm");
    }

    public void save() {
        if (thTablaImpuestoRenta.getFraccionBasica() != null && thTablaImpuestoRenta.getExcesoHasta() != null) {
            if (thTablaImpuestoRenta.getFraccionBasica().doubleValue() > thTablaImpuestoRenta.getExcesoHasta().doubleValue()) {
                JsfUtil.addWarningMessage("AVISO!!!", "El valor del exceso hasta es menor al del valor de la fración básica");
                return;
            }
        }
        Boolean edit = thTablaImpuestoRenta.getId() != null;
        if (edit) {
            thTablaImpuestoRenta.setFechaModificacion(new Date());
            thTablaImpuestoRenta.setUsuarioModifica(thInterfaces.getUser());
            thTablaImpuestoRentaService.edit(thTablaImpuestoRenta);
        } else {
            thTablaImpuestoRenta.setFechaCreacion(new Date());
            thTablaImpuestoRenta.setUsuarioCreacion(thInterfaces.getUser());
            thTablaImpuestoRentaService.create(thTablaImpuestoRenta);
        }
        JsfUtil.executeJS("PF('thTablaImpuestoRentaDlg').hide()");
        PrimeFaces.current().ajax().update("thTablaImpuestoRentaForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        clean();
    }

    public void delete(ThTablaImpuestoRenta thTablaImpuestoRenta) {
        thTablaImpuestoRenta.setEstado(false);
        thTablaImpuestoRentaService.edit(thTablaImpuestoRenta);
        PrimeFaces.current().ajax().update("thTablaImpuestoRentaTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public ThTablaImpuestoRenta getThTablaImpuestoRenta() {
        return thTablaImpuestoRenta;
    }

    public void setThTablaImpuestoRenta(ThTablaImpuestoRenta thTablaImpuestoRenta) {
        this.thTablaImpuestoRenta = thTablaImpuestoRenta;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public LazyModel<ThTablaImpuestoRenta> getThTablaImpuestoRentLazy() {
        return thTablaImpuestoRentLazy;
    }

    public void setThTablaImpuestoRentLazy(LazyModel<ThTablaImpuestoRenta> thTablaImpuestoRentLazy) {
        this.thTablaImpuestoRentLazy = thTablaImpuestoRentLazy;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
