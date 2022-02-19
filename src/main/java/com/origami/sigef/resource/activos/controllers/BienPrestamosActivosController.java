/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.activos.service.PrestamosActivosBienesService;
import com.origami.sigef.activos.service.PrestamosActivosService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.PrestamosActivos;
import com.origami.sigef.common.entities.PrestamosActivosBienes;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author Luis Pozo Gonzabay
 */
@Named(value = "prestamoActivoView")
@ViewScoped
public class BienPrestamosActivosController implements Serializable {

    /*Inject*/
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ActivoFijoServidorService activoFijoServidorService;
    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;
    @Inject
    private PrestamosActivosBienesService prestamosActivosBienesService;
    @Inject
    private PrestamosActivosService prestamosActivosService;
    @Inject
    private ThServidorCargoService thServidorCargoService;

    /*Objectos*/
    private PrestamosActivos prestamosActivos;
    private PrestamosActivosBienes prestamosActivosBienes;
    private Proveedor proveedorSeleccionado;
    private ActivoFijoCustodio custodio;

    /*Lazy Model*/
    private LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel;
    private LazyModel<PrestamosActivos> prestamosActivosLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;

    /*Listas*/
    private List<ActivoFijoServidor> bienesItemsList;
    private List<BienesItem> componentesList;
    private List<ActivoFijoServidor> bienesSeleccionados;
    private List<BienesItem> componentesSeleccionados;
    private List<ActivoFijoServidor> bienesPrestados;
    private List<BienesItem> componentesPrestados;
    private List<PrestamosActivos> listPrestamosActivos;

    private Date dateNow;

    /*Constructor*/
    @PostConstruct
    public void initialize() {
        this.activoFijoCustodioLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.activoFijoCustodioLazyModel.getSorteds().put("id", "ASC");
        this.activoFijoCustodioLazyModel.getFilterss().put("estado", true);
        this.activoFijoCustodioLazyModel.getFilterss().put("actaGuardalmacen", false);

        this.prestamosActivosLazyModel = new LazyModel<>(PrestamosActivos.class);
        this.prestamosActivosLazyModel.getSorteds().put("id", "ASC");
        this.prestamosActivosLazyModel.getFilterss().put("estado", true);

        this.proveedorLazyModel = new LazyModel<>(Proveedor.class);
        this.proveedorLazyModel.getSorteds().put("id", "ASC");
        this.proveedorLazyModel.getFilterss().put("estado", true);
        dateNow = new Date();
        vaciarFormulario();
    }

    /*formulario*/
    public void form(PrestamosActivos prestamosActivos) {
        if (prestamosActivos != null) {
            this.prestamosActivos = prestamosActivos;
        } else {
            this.prestamosActivos = new PrestamosActivos();
            this.bienesPrestados = new ArrayList<>();
            this.componentesPrestados = new ArrayList<>();
        }
        PrimeFaces.current().executeScript("PF('custodioDialog').show()");
        PrimeFaces.current().ajax().update(":custodioPanel");
    }

    public void addCustodio(ActivoFijoCustodio custodio) {
        this.custodio = custodio;
        proveedorSeleccionado = new Proveedor();
        PrimeFaces.current().executeScript("PF('custodioDialog').hide()");

        bienesItemsList = activoFijoServidorService.getBienesAsignados(this.custodio);
        PrimeFaces.current().executeScript("PF('custodioBienesDialog').show()");
        PrimeFaces.current().ajax().update(":custodioBienesTable");
    }

    public void addBienes() {
        PrimeFaces.current().executeScript("PF('custodioBienesDialog').hide()");
        for (ActivoFijoServidor bienes : bienesSeleccionados) {
            this.bienesPrestados.add(bienes);
        }
        PrimeFaces.current().executeScript("PF('prestamoActivosDialog').show()");
        PrimeFaces.current().ajax().update(":prestamosActivosPanel");
    }

    public void addProveedor(Proveedor proveedor) {
        this.proveedorSeleccionado = proveedor;
        PrimeFaces.current().executeScript("PF('proveedorDlg').hide()");
    }

    public void openDlgComponentes(ActivoFijoServidor bienesItem) {
        componentesList = activoFijoServidorService.getComponentes(bienesItem.getBienesItem());
        PrimeFaces.current().executeScript("PF('componentesDialog').show()");
        PrimeFaces.current().ajax().update(":componentesTable");
    }

    public void addComponentes() {
        for (BienesItem componente : componentesSeleccionados) {
            if (!componentesPrestados.contains(componente)) {
                componentesPrestados.add(componente);
            } else {
                JsfUtil.addWarningMessage("Componente", "El componente " + componente.getDescripcion() + " ya se encuentra agregado.");
            }
        }
        PrimeFaces.current().executeScript("PF('componentesDialog').hide()");
        PrimeFaces.current().ajax().update(":componentesSeleccionadosTable");
    }

    /*Guardar*/
    public void save() {
        if (prestamosActivos.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO", "Debe registrar la descripción/motivo del prestamo del activo");
            return;
        }
        if (prestamosActivos.getMotivo().equals("COMODATO")) {
            if (prestamosActivos.getFechaDevolucionComodato() == null) {
                JsfUtil.addWarningMessage("AVISO", "Debe registrar la fecha de devolución de Comodato.");
                return;
            }
        }
        if (proveedorSeleccionado.getId() != null) {
            if (!bienesPrestados.isEmpty()) {
                if (prestamosActivos.getMotivo() != null) {
                    prestamosActivos.setProveedor(proveedorSeleccionado);
                    prestamosActivos.setFechaCreacion(new Date());
                    prestamosActivos.setUsuarioCreacion(userSession.getNameUser());
                    prestamosActivos.setCustodio(custodio);
                    prestamosActivos.setDescripcion(prestamosActivos.getDescripcion().toUpperCase());
                    prestamosActivos = prestamosActivosService.create(prestamosActivos);
                    for (ActivoFijoServidor bienesItem : bienesPrestados) {
                        PrestamosActivosBienes prestamo = new PrestamosActivosBienes();
                        prestamo.setPrestamosActivos(prestamosActivos);
                        prestamo.setBienesItem(bienesItem.getBienesItem());
                        prestamo.setComponente(Boolean.FALSE);
                        prestamo.setFechaPrestamo(new Date());
                        prestamo.setActivoFijoServidor(bienesItem);
                        prestamosActivosBienesService.create(prestamo);
                    }
                    if (!componentesPrestados.isEmpty()) {
                        for (BienesItem bienesItem : componentesPrestados) {
                            prestamosActivosBienes = new PrestamosActivosBienes();
                            prestamosActivosBienes.setBienesItem(bienesItem);
                            prestamosActivosBienes.setGrupoPadre(bienesItem.getGrupoPadre());
                            prestamosActivosBienes.setComponente(Boolean.TRUE);
                            prestamosActivosBienes.setPrestamosActivos(prestamosActivos);
                            prestamosActivosBienes.setFechaPrestamo(new Date());
                            prestamosActivosBienesService.create(prestamosActivosBienes);
                        }
                    }
                    imprimirReporte(prestamosActivos);
                    PrimeFaces.current().executeScript("PF('prestamoActivosDialog').hide()");
                    PrimeFaces.current().ajax().update("prestamosTable");
                    JsfUtil.addSuccessMessage("PRESTAMO", " Registrado con éxito.");
                } else {
                    JsfUtil.addWarningMessage("AVISO", "Debe seleccionar el motivo del prestamo antes de guardar");
                }
            } else {
                JsfUtil.addWarningMessage("AVISO", "Debe al menos seleccionar un bien para efectuar el prestamo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un proveedor antes de guardar");
        }
    }

    private void vaciarFormulario() {
        this.bienesItemsList = new ArrayList<>();
        this.componentesList = new ArrayList<>();
        this.bienesSeleccionados = new ArrayList<>();
        this.componentesSeleccionados = new ArrayList<>();
        this.bienesPrestados = new ArrayList<>();
        this.componentesPrestados = new ArrayList<>();
        this.prestamosActivos = new PrestamosActivos();
        this.prestamosActivosBienes = new PrestamosActivosBienes();
        this.proveedorSeleccionado = new Proveedor();
        this.custodio = new ActivoFijoCustodio();
    }

    public ThServidorCargo getThServidorCargoByServidor(Servidor servidor) {
        return thServidorCargoService.findByThServidor(servidor);
    }

    public void imprimirReporte(PrestamosActivos prestamo) {
        servletSession.addParametro("id_prestamo", prestamo.getId());
        /*DATOS SERVIDOR*/
        servletSession.addParametro("nombre_servidor", prestamo.getCustodio().getServidor().getPersona().getNombreCompleto());
        servletSession.addParametro("identificacion_servidor", prestamo.getCustodio().getServidor().getPersona().getIdentificacion());
        ThServidorCargo cargo = thServidorCargoService.findByThServidor(prestamo.getCustodio().getServidor());
        if (cargo != null) {
            servletSession.addParametro("unidad_servidor", cargo.getIdCargo().getIdUnidad().getNombre());
            servletSession.addParametro("cargo_servidor", cargo.getIdCargo().getNombreCargo());
        } else {
            servletSession.addParametro("unidad_servidor", "");
            servletSession.addParametro("cargo_servidor", "");
        }
        /*DATOS PROVEEDOR*/
        servletSession.addParametro("proveedor_nombre", prestamo.getProveedor().getCliente().getNombre());
        servletSession.addParametro("proveedor_identificacion", prestamo.getProveedor().getCliente().getIdentificacionCompleta());
        servletSession.setNombreReporte("ReportePrestamo");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void openDlgDevolucion(PrestamosActivos prestamo) {
        prestamosActivos = prestamo;
        PrimeFaces.current().executeScript("PF('devolucionPrestamoDialog').show()");
        PrimeFaces.current().ajax().update("prestamosDevolucionPanel");
    }

    public void imprimirReporteDevolucion(PrestamosActivos prestamo) {
        if (prestamo == null) {
            if (prestamosActivos.getDescripcionDevolucion() == null || prestamosActivos.getDescripcionDevolucion().equals("")) {
                JsfUtil.addWarningMessage("AVISO", "Ingrese una descripción antes de generar el acta de devolución");
                return;
            }
            //editar
            prestamosActivos.setEstadoPrestamo(Boolean.TRUE);
            prestamosActivosService.edit(prestamosActivos);
        } else {
            prestamosActivos = prestamo;
        }
        servletSession.addParametro("id_prestamo", prestamosActivos.getId());
        servletSession.addParametro("descripcion_acta_devolucion", prestamosActivos.getDescripcionDevolucion().toUpperCase());
        /*DATOS SERVIDOR*/
        servletSession.addParametro("nombre_servidor", prestamosActivos.getCustodio().getServidor().getPersona().getNombreCompleto());
        servletSession.addParametro("identificacion_servidor", prestamosActivos.getCustodio().getServidor().getPersona().getIdentificacion());
        ThServidorCargo cargo = thServidorCargoService.findByThServidor(prestamosActivos.getCustodio().getServidor());
        if (cargo != null) {
            servletSession.addParametro("unidad_servidor", cargo.getIdCargo().getIdUnidad().getNombre());
            servletSession.addParametro("cargo_servidor", cargo.getIdCargo().getNombreCargo());
        } else {
            servletSession.addParametro("unidad_servidor", "");
            servletSession.addParametro("cargo_servidor", "");
        }
        /*DATOS PROVEEDOR*/
        servletSession.addParametro("proveedor_nombre", prestamosActivos.getProveedor().getCliente().getNombre());
        servletSession.addParametro("proveedor_identificacion", prestamosActivos.getProveedor().getCliente().getIdentificacionCompleta());
        servletSession.setNombreReporte("ReportePrestamoDevolucion");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

        PrimeFaces.current().executeScript("PF('devolucionPrestamoDialog').hide()");
        PrimeFaces.current().ajax().update("prestamosDevolucionPanel", "prestamosTable");
    }

    public void openDialogComodato() {
        listPrestamosActivos = prestamosActivosService.getComodatoByFechaDevolucionComodato();
        PrimeFaces.current().executeScript("PF('dialogComodato').show()");
        PrimeFaces.current().ajax().update("formComodato");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public PrestamosActivos getPrestamosActivos() {
        return prestamosActivos;
    }

    public void setPrestamosActivos(PrestamosActivos prestamosActivos) {
        this.prestamosActivos = prestamosActivos;
    }

    public PrestamosActivosBienes getPrestamosActivosBienes() {
        return prestamosActivosBienes;
    }

    public void setPrestamosActivosBienes(PrestamosActivosBienes prestamosActivosBienes) {
        this.prestamosActivosBienes = prestamosActivosBienes;
    }

    public Proveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public LazyModel<ActivoFijoCustodio> getActivoFijoCustodioLazyModel() {
        return activoFijoCustodioLazyModel;
    }

    public void setActivoFijoCustodioLazyModel(LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel) {
        this.activoFijoCustodioLazyModel = activoFijoCustodioLazyModel;
    }

    public LazyModel<PrestamosActivos> getPrestamosActivosLazyModel() {
        return prestamosActivosLazyModel;
    }

    public void setPrestamosActivosLazyModel(LazyModel<PrestamosActivos> prestamosActivosLazyModel) {
        this.prestamosActivosLazyModel = prestamosActivosLazyModel;
    }

    public List<ActivoFijoServidor> getBienesSeleccionados() {
        return bienesSeleccionados;
    }

    public void setBienesSeleccionados(List<ActivoFijoServidor> bienesSeleccionados) {
        this.bienesSeleccionados = bienesSeleccionados;
    }

    public List<BienesItem> getComponentesSeleccionados() {
        return componentesSeleccionados;
    }

    public void setComponentesSeleccionados(List<BienesItem> componentesSeleccionados) {
        this.componentesSeleccionados = componentesSeleccionados;
    }

    public List<ActivoFijoServidor> getBienesItemsList() {
        return bienesItemsList;
    }

    public void setBienesItemsList(List<ActivoFijoServidor> bienesItemsList) {
        this.bienesItemsList = bienesItemsList;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public List<BienesItem> getComponentesList() {
        return componentesList;
    }

    public void setComponentesList(List<BienesItem> componentesList) {
        this.componentesList = componentesList;
    }

    public List<ActivoFijoServidor> getBienesPrestados() {
        return bienesPrestados;
    }

    public void setBienesPrestados(List<ActivoFijoServidor> bienesPrestados) {
        this.bienesPrestados = bienesPrestados;
    }

    public List<BienesItem> getComponentesPrestados() {
        return componentesPrestados;
    }

    public void setComponentesPrestados(List<BienesItem> componentesPrestados) {
        this.componentesPrestados = componentesPrestados;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }

//</editor-fold>
    public List<PrestamosActivos> getListPrestamosActivos() {
        return listPrestamosActivos;
    }

    public void setListPrestamosActivos(List<PrestamosActivos> listPrestamosActivos) {
        this.listPrestamosActivos = listPrestamosActivos;
    }
}
