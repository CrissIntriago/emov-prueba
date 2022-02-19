/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ControlPresupuestarioService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "controlPresupuestarioView")
@ViewScoped
public class ControlPresupuestarioController implements Serializable {

    @Inject
    private ControlPresupuestarioService controlService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UserSession userSession;

    private LazyModel<ControlCuentaPresupuestario> lazy;
    private OpcionBusqueda opcionBusqueda;
    private List<MasterCatalogo> periodos;
    private List<ControlCuentaPresupuestario> listaAnio;
    private ControlCuentaPresupuestario controlPresupuestario;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        controlPresupuestario = new ControlCuentaPresupuestario();
        lazy = new LazyModel<>(ControlCuentaPresupuestario.class);
        lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        listaAnio = controlService.getListControl(opcionBusqueda.getAnio());
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
    }

    public void cierreApertura(ControlCuentaPresupuestario control, Boolean var) {
        if (var.equals(true)) {
            control.setEstado(Boolean.FALSE);
            control.setFechaCierre(new Date());
            control.setFechaModificacion(new Date());
            control.setUsuarioModificacion(userSession.getNameUser());

            if (!controlService.getCierrePeriodo(control.getPeriodo().intValue() - 1)) {
                JsfUtil.addWarningMessage("Verificar", "Falta por Cerrar período anterior");
                return;
            }

            if (!validacionCierre(control)) {
                JsfUtil.addWarningMessage("Verificar", "Mes " + listaAnio.get(control.getOrden().intValue() - 1).getNombreMes() + " Falta por Cerrar");
                controlPresupuestario = new ControlCuentaPresupuestario();
                return;
            }
            controlService.edit(control);
        } else {
            control.setFechaModificacion(new Date());
            control.setUsuarioCreacion(userSession.getNameUser());
            control.setEstado(Boolean.TRUE);
            controlService.edit(control);
        }
        controlPresupuestario = new ControlCuentaPresupuestario();
        listaAnio = controlService.getListControl(opcionBusqueda.getAnio());
    }

    public boolean validacionCierre(ControlCuentaPresupuestario control) {
        if (listaAnio.get(control.getOrden().intValue()).getNombreMes().equals("Enero")) {
            return true;
        } else if (listaAnio.get(control.getOrden().intValue() - 1).getEstado()) {
            return false;
        }
        return true;
    }

    public void buscarLazy() {
        lazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        listaAnio = controlService.getListControl(opcionBusqueda.getAnio());
    }

//<editor-fold defaultstate="collapsed" desc="GETTERs AND SETTERs">
    public LazyModel<ControlCuentaPresupuestario> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ControlCuentaPresupuestario> lazy) {
        this.lazy = lazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<ControlCuentaPresupuestario> getListaAnio() {
        return listaAnio;
    }

    public void setListaAnio(List<ControlCuentaPresupuestario> listaAnio) {
        this.listaAnio = listaAnio;
    }

    public ControlCuentaPresupuestario getControlPresupuestario() {
        return controlPresupuestario;
    }

    public void setControlPresupuestario(ControlCuentaPresupuestario controlPresupuestario) {
        this.controlPresupuestario = controlPresupuestario;
    }
//</editor-fold>

}
