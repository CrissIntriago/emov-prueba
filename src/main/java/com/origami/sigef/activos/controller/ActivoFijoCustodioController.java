/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "activoFijoCustodioView")
@ViewScoped
public class ActivoFijoCustodioController extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ActivoFijoCustodioController.class.getName());

    /*Inject Services*/
    @Inject
    private UserSession userSession;
    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;
    @Inject
    private ActivoFijoServidorService activoFijoServidorService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ClienteService clienteService;


    /*Listas*/
    private List<ActivoFijoServidor> bienesItemList;
    private List<ActivoFijoServidor> bienesSeleccionados;

    private List<ActivoFijoServidor> bienesAsignados;


    /*Objetos*/
    private ActivoFijoCustodio activoFijoCustodio;
    private ActivoFijoServidor activoFijoServidor;
    private OpcionBusqueda opcionBusqueda;

    /*LazyModel*/
    private LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel;
    private LazyModel<Servidor> servidorLazyModel;
    private LazyModel<ActivoFijoServidor> bienesLazyModel;
    private String observador;
    private ActivoFijoCustodio activoAdicional;
    /*Variables*/
    private Boolean ocultarBoton;
    private Boolean botonCompletarTarea = Boolean.TRUE;

    /*Contructor inicializado*/
    @PostConstruct
    public void initialize() {

        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                this.observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.activoFijoCustodio = new ActivoFijoCustodio();
                this.activoFijoCustodioLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
                this.activoFijoCustodioLazyModel.getSorteds().put("id", "ASC");
                this.activoFijoCustodioLazyModel.getFilterss().put("estado", true);
                this.activoFijoCustodioLazyModel.getFilterss().put("actaGuardalmacen", false);
                if (activoFijoCustodioService.getActasConTramite(tramite.getNumTramite()) != null) {
                    this.activoFijoCustodioLazyModel.getFilterss().put("numeroTramite", tramite.getNumTramite());
                }
                this.bienesItemList = new ArrayList<>();
                this.bienesSeleccionados = new ArrayList<>();
                this.bienesAsignados = new ArrayList<>();
                this.ocultarBoton = Boolean.TRUE;
                this.opcionBusqueda = new OpcionBusqueda();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void form(ActivoFijoCustodio activoFijoCustodio, String accion) {
        if (activoFijoCustodio != null) {
            this.activoFijoCustodio = activoFijoCustodio;
            restablecerFormulario();
            this.bienesItemList = activoFijoServidorService.getBienesAsignados(activoFijoCustodio);
        } else {
            this.activoFijoCustodio = new ActivoFijoCustodio();
            this.activoFijoCustodio.setDescripcion("De conformidad con los artículos 41, 44 del Reglamento administración y control de bienes del sector publico vigente a la fecha....");
        }
        switch (accion) {
            case "ASIGNAR":
                restablecerFormulario();
                PrimeFaces.current().executeScript("PF('asignacionCustodioDLG').show()");
                PrimeFaces.current().ajax().update(":asignacionPanel");
                break;
            case "VIZUALIZAR":
                PrimeFaces.current().executeScript("PF('viewCustodioDLG').show()");
                PrimeFaces.current().ajax().update(":ViewPanel");
                PrimeFaces.current().ajax().update(":bienesViewTable");
                break;
        }
    }

    public void openDlgServidor(String servidor) {
        this.servidorLazyModel = new LazyModel<>(Servidor.class);
        this.servidorLazyModel.getSorteds().put("id", "ASC");
        this.servidorLazyModel.getFilterss().put("estado", true);
        if (servidor.equals("NUEVO")) {
            ocultarBoton = Boolean.TRUE;
        } else {
            ocultarBoton = Boolean.FALSE;
        }
        PrimeFaces.current().executeScript("PF('servidorDLG').show()");
        PrimeFaces.current().ajax().update(":servidorForm");
    }

    public void añadirServidor(Servidor servidor) {
        activoFijoCustodio.setServidor(servidor);
        PrimeFaces.current().executeScript("PF('servidorDLG').hide()");
    }

    public void removerBien(ActivoFijoServidor bienes) {
        bienesSeleccionados.remove(bienes);
        actualizarFormAsignacion();
        JsfUtil.addInformationMessage("BIEN", "ha sido removido correctamente");
    }

    public void openDlgBienes() {
        this.bienesLazyModel = new LazyModel<>(ActivoFijoServidor.class);
        this.bienesLazyModel.getSorteds().put("id", "ASC");
        this.bienesLazyModel.getFilterss().put("asignado", true);
        this.bienesLazyModel.getFilterss().put("estado", false);
        PrimeFaces.current().executeScript("PF('BienesDLG').show()");
        PrimeFaces.current().ajax().update(":formBienes");
    }

    public void añadirBienes() {
        PrimeFaces.current().executeScript("PF('BienesDLG').hide()");
        actualizarFormAsignacion();
    }

    private void actualizarFormAsignacion() {
        activoFijoCustodio.setCantidadBienes((short) bienesSeleccionados.size());
    }

    public void guardarAsignacion() {
        if (activoFijoCustodio.getServidor() != null) {
            if (activoFijoCustodio.getServidor().getDistributivo().getCargo().getNombreCargo().equals("GUARDALMACÉN")) {
                JsfUtil.addWarningMessage("AVISO", "No se le puede asignar un Bien a un servidor con cargo de guardalmacén");
            } else {
                if (bienesSeleccionados.isEmpty()) {
                    JsfUtil.addWarningMessage("AVISO", "Nesesita asignarle minímo un bien al servidor seleccionado");
                } else {
                    /*Traer el número de acta de la ultima acta registrada*/
                    ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.FALSE);
                    /*Creamos el activoFijoCustodio*/
                    if (ultimaActa != null) {
                        activoFijoCustodio.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
                    } else {
                        activoFijoCustodio.setNumeroActa(BigInteger.valueOf(1));
                    }
                    /*Creamos el activoFijoCustodio*/
                    activoFijoCustodio.setUsuarioCreacion(userSession.getNameUser());
                    activoFijoCustodio.setFechaCreacion(new Date());
                    activoFijoCustodio.setEstado(Boolean.TRUE);
                    activoFijoCustodio.setActaGuardalmacen(Boolean.FALSE);
                    activoFijoCustodio.setFechaEntrega(new Date());
                    activoFijoCustodio.setNumeroTramite(new BigInteger("" + tramite.getNumTramite()));
                    activoFijoCustodio.setEstadoProceso("INCOMPLETO");
                    activoFijoCustodio = activoFijoCustodioService.create(activoFijoCustodio);
                    this.activoAdicional = new ActivoFijoCustodio();
                    this.activoAdicional = Utils.clone(activoFijoCustodio);
                    /*Creamos los nuevos bienes para el sevidor que se le asigno*/
                    for (ActivoFijoServidor bienes : bienesSeleccionados) {
                        activoFijoServidor = new ActivoFijoServidor();
                        activoFijoServidor.setActivoFijoCustodio(activoFijoCustodio);
                        activoFijoServidor.setAsignado(Boolean.TRUE);
                        activoFijoServidor.setEstado(Boolean.TRUE);
                        activoFijoServidor.setObservacionInicial(bienes.getObservacionInicial());
                        activoFijoServidor.setBienesItem(bienes.getBienesItem());
                        activoFijoServidor.setEstadoBien(activoFijoServidor.getBienesItem().getEstadoBien().getTexto());
                        activoFijoServidor.setFechaAsignacion(new Date());
                        activoFijoServidor = activoFijoServidorService.create(activoFijoServidor);
                    }
                    /*Modificamos el estado de asignado a false para determinar que este bien ya no pertenece al Guardalmacén*/
                    for (ActivoFijoServidor bienes : bienesSeleccionados) {
                        bienes.setAsignado(Boolean.FALSE);
                        bienes.setObservacionInicial(null);
                        bienes.setFechaDevolucion(new Date());
                        activoFijoServidorService.edit(bienes);
                    }
                    imprimirAsignacionCustodio(activoFijoCustodio);
                    restablecerFormulario();
                    PrimeFaces.current().executeScript("PF('asignacionCustodioDLG').hide()");
                    JsfUtil.addInformationMessage("ASIGNACIÓN", "se ha realizado con exitó");
                }
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Nesesita seleccionar un servidor antes de guardar");
        }
    }

    private void restablecerFormulario() {
        bienesSeleccionados = new ArrayList<>();
        bienesAsignados = new ArrayList<>();
        bienesItemList = new ArrayList<>();
        this.ocultarBoton = Boolean.TRUE;
    }

    public void imprimirAsignacionCustodio(ActivoFijoCustodio activoAsignado) {
        Servidor guardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        servletSession.addParametro("id_custodio", activoAsignado.getId());
        servletSession.addParametro("nombre_guardalmacen", guardalmacen.getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_guardalmacen", guardalmacen.getPersona().getIdentificacion());
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        if (guardalmacen.getDistributivo() != null) {
            if (guardalmacen.getDistributivo().getCargo() != null) {
                servletSession.addParametro("cargo_guardalmacen", guardalmacen.getDistributivo().getCargo().getNombreCargo());
            } else {
                servletSession.addParametro("cargo_guardalmacen", "");
            }
        } else {
            servletSession.addParametro("cargo_guardalmacen", "");
        }
        servletSession.setNombreReporte("AsignacionDeCustodio");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void ingObservacion() {

        List<ActivoFijoCustodio> lista = activoFijoCustodioService.getActasConTramite(tramite.getNumTramite());
        if (lista.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Debe por los menos asignar un bien");
            return;
        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        observacion.setIdTramite(tramite);

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void terminartareaCustodio() {
        try {
            activoFijoCustodioService.getAcualizarEstadosCustodio("COMPLETO", tramite.getNumTramite());
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET-SET">
    public List<ActivoFijoServidor> getBienesItemList() {
        return bienesItemList;
    }

    public void setBienesItemList(List<ActivoFijoServidor> bienesItemList) {
        this.bienesItemList = bienesItemList;
    }

    public ActivoFijoCustodio getActivoFijoCustodio() {
        return activoFijoCustodio;
    }

    public void setActivoFijoCustodio(ActivoFijoCustodio activoFijoCustodio) {
        this.activoFijoCustodio = activoFijoCustodio;
    }

    public ActivoFijoServidor getActivoFijoServidor() {
        return activoFijoServidor;
    }

    public void setActivoFijoServidor(ActivoFijoServidor activoFijoServidor) {
        this.activoFijoServidor = activoFijoServidor;
    }

    public LazyModel<ActivoFijoCustodio> getActivoFijoCustodioLazyModel() {
        return activoFijoCustodioLazyModel;
    }

    public void setActivoFijoCustodioLazyModel(LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel) {
        this.activoFijoCustodioLazyModel = activoFijoCustodioLazyModel;
    }

    public LazyModel<Servidor> getServidorLazyModel() {
        return servidorLazyModel;
    }

    public void setServidorLazyModel(LazyModel<Servidor> servidorLazyModel) {
        this.servidorLazyModel = servidorLazyModel;
    }

    public LazyModel<ActivoFijoServidor> getBienesLazyModel() {
        return bienesLazyModel;
    }

    public void setBienesLazyModel(LazyModel<ActivoFijoServidor> bienesLazyModel) {
        this.bienesLazyModel = bienesLazyModel;
    }

    public List<ActivoFijoServidor> getBienesSeleccionados() {
        return bienesSeleccionados;
    }

    public void setBienesSeleccionados(List<ActivoFijoServidor> bienesSeleccionados) {
        this.bienesSeleccionados = bienesSeleccionados;
    }

    public List<ActivoFijoServidor> getBienesAsignados() {
        return bienesAsignados;
    }

    public void setBienesAsignados(List<ActivoFijoServidor> bienesAsignados) {
        this.bienesAsignados = bienesAsignados;
    }

    public Boolean getOcultarBoton() {
        return ocultarBoton;
    }

    public void setOcultarBoton(Boolean ocultarBoton) {
        this.ocultarBoton = ocultarBoton;
    }

    public String getObservador() {
        return observador;
    }

    public void setObservador(String observador) {
        this.observador = observador;
    }

    public ActivoFijoCustodio getActivoAdicional() {
        return activoAdicional;
    }

    public void setActivoAdicional(ActivoFijoCustodio activoAdicional) {
        this.activoAdicional = activoAdicional;
    }

    public Boolean getBotonCompletarTarea() {
        return botonCompletarTarea;
    }

    public void setBotonCompletarTarea(Boolean botonCompletarTarea) {
        this.botonCompletarTarea = botonCompletarTarea;
    }
//</editor-fold>

}
