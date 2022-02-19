/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.TablaImpuestoRenta;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.services.TablaImpuestoService;
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
 * @author Origami
 */
@Named(value = "tablaImpuestoRentaBeans")
@ViewScoped
public class TablaImpuestoRentaController implements Serializable {

    private TablaImpuestoRenta tablaimpuesto;
    private OpcionBusqueda busqueda;
    private List<MasterCatalogo> listaPeriodos;
    private LazyModel<TablaImpuestoRenta> lazytabla;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private UserSession userSession;
    @Inject
    private TablaImpuestoService tablaservice;

    @PostConstruct
    public void initView() {
        tablaimpuesto = new TablaImpuestoRenta();
        busqueda = new OpcionBusqueda();
        listaPeriodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        //tablaimpuesto.setPeriodo(busqueda.getAnio());
        lazytabla = new LazyModel<>(TablaImpuestoRenta.class);
        lazytabla.getFilterss().put("estado", Boolean.TRUE);
        lazytabla.getFilterss().put("periodo", busqueda.getAnio());
    }

    public void buscarPeriodo() {
        lazytabla = new LazyModel<>(TablaImpuestoRenta.class);
        lazytabla.getFilterss().put("estado", true);
        lazytabla.getFilterss().put("periodo", busqueda.getAnio());
    }

    public void formNew(TablaImpuestoRenta t) {

        if (t != null) {
            this.tablaimpuesto = t;
        } else {
            tablaimpuesto = new TablaImpuestoRenta();
        }
        tablaimpuesto.setUsuarioModifica(userSession.getNameUser());
        tablaimpuesto.setFechaModificacion(new Date());
        tablaimpuesto.setEstado(true);
        tablaimpuesto.setPeriodo(busqueda.getAnio());
        PrimeFaces.current().executeScript("PF('dlgtabla').show()");
        PrimeFaces.current().ajax().update("frmtabla");
    }

    public void guardarTabla() {
        boolean edit = tablaimpuesto.getId() != null;

        if (!edit) {
            if (this.tablaimpuesto.getFraccionBasica().doubleValue() >= this.tablaimpuesto.getExcesoHasta().doubleValue()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Tabla de Impuesto", "la Fraccion Basica no puede ser mayor o igual al Exceso hasta");
                return;
            }
 
//            if (this.tablaimpuesto.getExcesoHasta().doubleValue() < this.tablaimpuesto.getFraccionBasica().doubleValue()) {
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addWarningMessage("Tabla de Impuesto", "El Exceso hasta no puede ser menor a la Fraccion Basica");
//                return;
//            }
            tablaimpuesto.setUsuarioCreacion(userSession.getNameUser());
            tablaimpuesto.setFechaCreacion(new Date());
            tablaimpuesto = tablaservice.create(tablaimpuesto);

        } else {

            tablaimpuesto.setUsuarioCreacion(userSession.getNameUser());
            tablaimpuesto.setFechaCreacion(new Date());
            if (this.tablaimpuesto.getFraccionBasica().doubleValue() > this.tablaimpuesto.getExcesoHasta().doubleValue()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Tabla de Impuesto", "la Fraccion Basica no puede ser mayor al Exceso hasta");
                return;
            }
//            if (this.tablaimpuesto.getExcesoHasta().doubleValue() < this.tablaimpuesto.getFraccionBasica().doubleValue()) {
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addWarningMessage("Tabla de Impuesto", "El Exceso hasta no puede ser menor a la Fraccion Basica");
//                return;
//            }
            tablaservice.edit(tablaimpuesto);
        }
        PrimeFaces.current().executeScript("PF('dlgtabla').hide()");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", tablaimpuesto.getImpuestoFraccionBasica() + (edit ? " editada" : " registrada") + " con éxito.");

    }

    public void borrar(TablaImpuestoRenta ta) {
        ta.setEstado(Boolean.FALSE);
        tablaservice.edit(ta);
        PrimeFaces.current().ajax().update(":dttabla");
    }

    public void cancelar() {
        tablaimpuesto = new TablaImpuestoRenta();
        PrimeFaces.current().executeScript("PF('dlgtabla').hide()");
    }

    public TablaImpuestoRenta getTablaimpuesto() {
        return tablaimpuesto;
    }

    public void setTablaimpuesto(TablaImpuestoRenta tablaimpuesto) {
        this.tablaimpuesto = tablaimpuesto;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getListaPeriodos() {
        return listaPeriodos;
    }

    public void setListaPeriodos(List<MasterCatalogo> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public LazyModel<TablaImpuestoRenta> getLazytabla() {
        return lazytabla;
    }

    public void setLazytabla(LazyModel<TablaImpuestoRenta> lazytabla) {
        this.lazytabla = lazytabla;
    }

}
