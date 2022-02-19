/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jesus
 */
@Named(value = "dialogDiarioGeneral")
@ViewScoped
public class DialogGeneralController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MasterCatalogoService periodoService;

    private LazyModel<ContDiarioGeneral> lazyDiario;
    private List<MasterCatalogo> listaPeriodo;
    private Short periodoSeleccionado;
    private OpcionBusqueda opcionBusqueda;

    @PostConstruct
    public void init() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        if (listaPeriodo != null) {
            this.periodoSeleccionado = opcionBusqueda.getAnio();
        } else {
            this.periodoSeleccionado = opcionBusqueda.getAnio();
        }
        actualizarLazy();
    }

    public void actualizarLazy() {
        lazyDiario = new LazyModel<>(ContDiarioGeneral.class, "numRegistro", "ASC");
//        lazyDiario.getFilterss().put("estado", true);
        lazyDiario.getFilterss().put("retencion", true);
        lazyDiario.getFilterss().put("retenido", false);
        lazyDiario.getFilterss().put("anulado", false);
        lazyDiario.getFilterss().put("idDiarioGeneral:equal", null);
        lazyDiario.getFilterss().put("periodo", periodoSeleccionado);
    }

    public void close(ContDiarioGeneral d) {
        PrimeFaces.current().dialog().closeDynamic(d);
    }

    public LazyModel<ContDiarioGeneral> getLazyDiario() {
        return lazyDiario;
    }

    public void setLazyDiario(LazyModel<ContDiarioGeneral> lazyDiario) {
        this.lazyDiario = lazyDiario;
    }

    public Short getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Short periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

}
