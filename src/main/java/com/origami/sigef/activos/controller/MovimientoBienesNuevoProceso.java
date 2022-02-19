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
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.talentohumano.services.CargoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Origami
 */
@Named(value = "movimientoBienesProceso")
@ViewScoped
public class MovimientoBienesNuevoProceso extends BpmnBaseRoot implements Serializable {

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
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private CargoService cargoService;
    @Inject
    private ClienteService clienteService;

    /*Listas*/
    private List<ActivoFijoServidor> bienesItemList;
    private List<ActivoFijoServidor> bienesSeleccionados;

    private List<ActivoFijoServidor> bienesAsignados;
    private List<ActivoFijoServidor> bienesDevolver;
    private List<ActivoFijoServidor> bienesTraspasar;

    private List<UnidadAdministrativa> unidadesAdministrativas;
    private List<UnidadAdministrativa> unidades;

    /*Objetos*/
    private ActivoFijoCustodio activoFijoCustodio;
    private ActivoFijoServidor activoFijoServidor;
    private ActivoFijoCustodio activoFijoCustodioTraspaso;
    private OpcionBusqueda opcionBusqueda;

    /*LazyModel*/
    private LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel;
    private LazyModel<Servidor> servidorLazyModel;
    private LazyModel<ActivoFijoServidor> bienesLazyModel;
    private LazyModel<ActivoFijoCustodio> historialLazyModel;
    private ActivoFijoCustodio activoAdicional;
    private List<Servidor> servidores;
    private List<Cargo> cargos;

    /*Variables*/
    private Boolean ocultarBoton;
    private String DescripcionDevolucion;
    private boolean btnRegistrar;

    private String observaciones;

    /*Contructor inicializado*/
    @PostConstruct
    public void initialize() {
        btnRegistrar = false;
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                if ("ASP".equals(tramite.getTipoTramite().getAbreviatura())) {
                    btnRegistrar = true;
                }
            }
        }
        this.opcionBusqueda = new OpcionBusqueda();
        observacion = new Observaciones();
        observacion.setIdTramite(tramite);
        cargos = cargoService.findByNamedQuery("Cargo.findAll");
        servidores = servidorService.findByNamedQuery("Servidor.findAll");
        unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findAll");
        this.activoFijoCustodio = new ActivoFijoCustodio();
        this.activoFijoCustodioTraspaso = new ActivoFijoCustodio();
        this.activoFijoCustodioLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.activoFijoCustodioLazyModel.getSorteds().put("id", "ASC");
        this.activoFijoCustodioLazyModel.getFilterss().put("estado", true);
        this.activoFijoCustodioLazyModel.getFilterss().put("actaGuardalmacen", false);
        this.historialLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.historialLazyModel.getSorteds().put("id", "ASC");
        this.historialLazyModel.getFilterss().put("actaGuardalmacen", false);
        this.bienesItemList = new ArrayList<>();
        this.bienesSeleccionados = new ArrayList<>();
        this.bienesDevolver = new ArrayList<>();
        this.bienesAsignados = new ArrayList<>();
        this.bienesTraspasar = new ArrayList<>();
        this.ocultarBoton = Boolean.TRUE;
        this.unidadesAdministrativas = unidadAdministrativaService.listaDeUnidades();
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
            case "TRASPASO":
                if (bienesAsignados.isEmpty()) {
                    bienesItemList.forEach((bienes) -> {
                        bienesAsignados.add(bienes);
                    });
                }
                this.activoFijoCustodioTraspaso.setDescripcion("De conformidad con los artículos 41, 44 del Reglamento administración y control de bienes del sector publico vigente a la fecha....");
                PrimeFaces.current().executeScript("PF('traspasoCustodioDLG').show()");
                PrimeFaces.current().ajax().update(":traspasoPanel");
                PrimeFaces.current().ajax().update(":bienesDeTraspasoTable");
                break;
            case "DEVOLUCION":
                if (bienesAsignados.isEmpty()) {
                    bienesItemList.forEach((bienes) -> {
                        bienesAsignados.add(bienes);
                    });
                }
                PrimeFaces.current().executeScript("PF('devolucionCustodioDLG').show()");
                PrimeFaces.current().ajax().update(":devolucionPanel");
                PrimeFaces.current().ajax().update(":bienesDevolucionTable");
                break;
            case "VIZUALIZAR":
                PrimeFaces.current().executeScript("PF('viewCustodioDLG').show()");
                PrimeFaces.current().ajax().update(":ViewPanel");
                PrimeFaces.current().ajax().update(":bienesViewTable");
                break;
            case "DAR DE BAJA":
                PrimeFaces.current().executeScript("PF('descripcionDevolucionDLG').show()");
                PrimeFaces.current().ajax().update(":descripcionDevolucionPanel");
                break;
        }
    }

    public void completar() {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("usuariosistema", clienteService.getrolsUser(RolUsuario.responsableSistema));
            getParamts().put("idServidor", session.getUserId());
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void ingObservacion() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        observacion.setIdTramite(tramite);

        JsfUtil.executeJS("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
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

    public void openDlgBienes() {
        this.bienesLazyModel = new LazyModel<>(ActivoFijoServidor.class);
        this.bienesLazyModel.getSorteds().put("id", "ASC");
        this.bienesLazyModel.getFilterss().put("asignado", true);
        this.bienesLazyModel.getFilterss().put("estado", false);
        PrimeFaces.current().executeScript("PF('BienesDLG').show()");
        PrimeFaces.current().ajax().update(":formBienes");
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
                    activoFijoCustodio.setActaGuardalmacen(Boolean.FALSE);
                    activoFijoCustodio.setFechaCreacion(new Date());
                    activoFijoCustodio.setUsuarioCreacion(userSession.getNameUser());
                    activoFijoCustodio.setEstado(Boolean.TRUE);
                    activoFijoCustodio.setFechaEntrega(new Date());
                    activoFijoCustodio = activoFijoCustodioService.create(activoFijoCustodio);
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
                        bienes.setEstado(Boolean.TRUE);
                        bienes.setObservacionInicial(null);
                        bienes.setFechaDevolucion(new Date());
                        activoFijoServidorService.edit(bienes);
                    }
                    imprimirAsignacionCustodio(activoFijoCustodio);
                    restablecerFormulario();
                    PrimeFaces.current().executeScript("PF('asignacionCustodioDLG').hide()");
                    JsfUtil.addInformationMessage("ASIGNACIÓN DE CUSTODIO", "Se ha realizado con exitó");
                }
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Nesesita seleccionar un servidor antes de guardar");
        }
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

    private void restablecerFormulario() {
        bienesSeleccionados = new ArrayList<>();
        bienesAsignados = new ArrayList<>();
        bienesDevolver = new ArrayList<>();
        bienesItemList = new ArrayList<>();
        bienesTraspasar = new ArrayList<>();
        activoFijoCustodioTraspaso = new ActivoFijoCustodio();
        this.ocultarBoton = Boolean.TRUE;
        DescripcionDevolucion = null;
    }

    public void añadirBienes() {
        PrimeFaces.current().executeScript("PF('BienesDLG').hide()");
        actualizarFormAsignacion();
    }

    private void actualizarFormAsignacion() {
        activoFijoCustodio.setCantidadBienes((short) bienesSeleccionados.size());
        PrimeFaces.current().ajax().update("ActivosGrid");
    }

    public void añadirServidor(Servidor servidor, String tipoServidor) {

        if (tipoServidor.equals("NUEVO")) {
            activoFijoCustodio.setServidor(servidor);
        } else {
            activoFijoCustodioTraspaso.setServidor(servidor);
            this.activoFijoCustodioTraspaso.setDescripcion("De conformidad con los artículos 41, 44 del Reglamento administración y control de bienes del sector publico vigente a la fecha....");
        }
        PrimeFaces.current().executeScript("PF('servidorDLG').hide()");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public UserSession getUserSession() {
        return userSession;
    }
    
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    public ServletSession getServletSession() {
        return servletSession;
    }
    
    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
    }
    
    public List<ActivoFijoServidor> getBienesItemList() {
        return bienesItemList;
    }
    
    public void setBienesItemList(List<ActivoFijoServidor> bienesItemList) {
        this.bienesItemList = bienesItemList;
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
    
    public List<ActivoFijoServidor> getBienesDevolver() {
        return bienesDevolver;
    }
    
    public void setBienesDevolver(List<ActivoFijoServidor> bienesDevolver) {
        this.bienesDevolver = bienesDevolver;
    }
    
    public List<ActivoFijoServidor> getBienesTraspasar() {
        return bienesTraspasar;
    }
    
    public void setBienesTraspasar(List<ActivoFijoServidor> bienesTraspasar) {
        this.bienesTraspasar = bienesTraspasar;
    }
    
    public List<UnidadAdministrativa> getUnidadesAdministrativas() {
        return unidadesAdministrativas;
    }
    
    public void setUnidadesAdministrativas(List<UnidadAdministrativa> unidadesAdministrativas) {
        this.unidadesAdministrativas = unidadesAdministrativas;
    }
    
    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }
    
    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
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
    
    public ActivoFijoCustodio getActivoFijoCustodioTraspaso() {
        return activoFijoCustodioTraspaso;
    }
    
    public void setActivoFijoCustodioTraspaso(ActivoFijoCustodio activoFijoCustodioTraspaso) {
        this.activoFijoCustodioTraspaso = activoFijoCustodioTraspaso;
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
    
    public LazyModel<ActivoFijoCustodio> getHistorialLazyModel() {
        return historialLazyModel;
    }
    
    public void setHistorialLazyModel(LazyModel<ActivoFijoCustodio> historialLazyModel) {
        this.historialLazyModel = historialLazyModel;
    }
    
    public ActivoFijoCustodio getActivoAdicional() {
        return activoAdicional;
    }
    
    public void setActivoAdicional(ActivoFijoCustodio activoAdicional) {
        this.activoAdicional = activoAdicional;
    }
    
    public List<Servidor> getServidores() {
        return servidores;
    }
    
    public void setServidores(List<Servidor> servidores) {
        this.servidores = servidores;
    }
    
    public List<Cargo> getCargos() {
        return cargos;
    }
    
    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }
    
    public Boolean getOcultarBoton() {
        return ocultarBoton;
    }
    
    public void setOcultarBoton(Boolean ocultarBoton) {
        this.ocultarBoton = ocultarBoton;
    }
    
    public String getDescripcionDevolucion() {
        return DescripcionDevolucion;
    }
    
    public void setDescripcionDevolucion(String DescripcionDevolucion) {
        this.DescripcionDevolucion = DescripcionDevolucion;
    }
    
    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }
    
    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
//</editor-fold>

}
