/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import com.origami.sigef.talentohumano.services.CargoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.Serializable;
import java.math.BigInteger;
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
 *
 */
@Named(value = "bienMovimientoView")
@ViewScoped
public class BienMovimientoController implements Serializable {

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
    @Inject
    private ThServidorCargoService thServidorCargoService;
    /*Listas*/
    private List<ActivoFijoServidor> bienesItemList;
    private List<ActivoFijoServidor> bienesSeleccionados;

    private List<ActivoFijoServidor> bienesAsignados;
    private List<ActivoFijoServidor> bienesDevolver;
    private List<ActivoFijoServidor> bienesTraspasar;

    private List<UnidadAdministrativa> unidades;

    /*Objetos*/
    private ActivoFijoCustodio activoFijoCustodio;
    private ActivoFijoServidor activoFijoServidor;
    private ActivoFijoCustodio activoFijoCustodioTraspaso;
    private OpcionBusqueda opcionBusqueda;

    /*LazyModel*/
    private LazyModel<ActivoFijoCustodio> activoFijoCustodioLazyModel;
    private LazyModel<ThServidorCargo> thServidorLazyModel;
    private LazyModel<ActivoFijoServidor> bienesLazyModel;
    private LazyModel<ActivoFijoCustodio> historialLazyModel;
    private ActivoFijoCustodio activoAdicional;
    private List<Servidor> servidores;
    private List<Cargo> cargos;

    /*Variables*/
    private Boolean ocultarBoton;
    private String DescripcionDevolucion;
    private boolean btnRegistrar;
    private ThServidorCargo thServidorCargo, thServidorCargoTraspaso;

    /*Contructor inicializado*/
    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        cargos = cargoService.findByNamedQuery("Cargo.findAll");
        servidores = servidorService.findByNamedQuery("Servidor.findAll");
        unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findAll");
        this.activoFijoCustodio = new ActivoFijoCustodio();
        this.activoFijoCustodioTraspaso = new ActivoFijoCustodio();
        this.activoFijoCustodioLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.activoFijoCustodioLazyModel.getSorteds().put("id", "ASC");
        this.activoFijoCustodioLazyModel.getFilterss().put("estado", true);
        this.activoFijoCustodioLazyModel.getFilterss().put("actaGuardalmacen", false);
        this.activoFijoCustodioLazyModel.getFilterss().put("cantidadBienes:ne", 0);
        this.historialLazyModel = new LazyModel<>(ActivoFijoCustodio.class);
        this.historialLazyModel.getSorteds().put("id", "ASC");
        this.historialLazyModel.getFilterss().put("actaGuardalmacen", false);
        this.bienesItemList = new ArrayList<>();
        this.bienesSeleccionados = new ArrayList<>();
        this.bienesDevolver = new ArrayList<>();
        this.bienesAsignados = new ArrayList<>();
        this.bienesTraspasar = new ArrayList<>();
        this.ocultarBoton = Boolean.TRUE;
    }

    public void form(ActivoFijoCustodio activoFijoCustodio, String accion) {
        if (activoFijoCustodio != null) {
            this.activoFijoCustodio = activoFijoCustodio;
            restablecerFormulario();
            thServidorCargo = thServidorCargoService.findByThServidor(activoFijoCustodio.getServidor());
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

    public void openDlgServidor(String servidor) {
        this.thServidorLazyModel = new LazyModel<>(ThServidorCargo.class);
        this.thServidorLazyModel.setDistinct(false);
        this.thServidorLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
        this.thServidorLazyModel.getSorteds().put("idServidor.persona.nombre", "ASC");
        this.thServidorLazyModel.getFilterss().put("idServidor.estado", true);

        if (servidor.equals("NUEVO")) {
            ocultarBoton = Boolean.TRUE;
        } else {
            ocultarBoton = Boolean.FALSE;
        }
        PrimeFaces.current().executeScript("PF('servidorDLG').show()");
        PrimeFaces.current().ajax().update(":servidorForm");
    }

    public void aniadirServidor(Servidor servidor, String tipoServidor) {
        if (tipoServidor.equals("NUEVO")) {
            activoFijoCustodio.setServidor(servidor);
            thServidorCargo = thServidorCargoService.findByThServidor(servidor);
        } else {
            activoFijoCustodioTraspaso.setServidor(servidor);
            thServidorCargoTraspaso = thServidorCargoService.findByThServidor(servidor);
            this.activoFijoCustodioTraspaso.setDescripcion("De conformidad con los artículos 41, 44 del Reglamento administración y control de bienes del sector público vigente a la fecha....");
        }
        PrimeFaces.current().executeScript("PF('servidorDLG').hide()");
    }

    public void openDlgBienes() {
        this.bienesLazyModel = new LazyModel<>(ActivoFijoServidor.class);
        this.bienesLazyModel.getSorteds().put("bienesItem.codigoBienAgrupado", "ASC");
        this.bienesLazyModel.getFilterss().put("asignado", true);
        this.bienesLazyModel.getFilterss().put("bienesItem.estado", true);
        this.bienesLazyModel.getFilterss().put("estado", false);
        this.bienesLazyModel.setDistinct(false);
        PrimeFaces.current().executeScript("PF('BienesDLG').show()");
        PrimeFaces.current().ajax().update(":formBienes");
    }

    public void añadirBienes() {
        PrimeFaces.current().executeScript("PF('BienesDLG').hide()");
        actualizarFormAsignacion();
    }

    private void actualizarFormAsignacion() {
        activoFijoCustodio.setCantidadBienes((short) bienesSeleccionados.size());
        PrimeFaces.current().ajax().update("ActivosGrid");
    }

    public void botonDeAccionDevolucionTraspaso(ActivoFijoServidor bien, String accion) {
        bienesAsignados.remove(bien);
        if (accion.equals("DEVOLUCION")) {
            bienesDevolver.add(bien);
            JsfUtil.addInformationMessage("AVISO", "se ha añadido a la lista de de los bienes a devolver");
            PrimeFaces.current().ajax().update("devolucionTable");
            PrimeFaces.current().ajax().update("bienesDevolucionTable");
        } else if (accion.equals("TRASPASO")) {
            bienesTraspasar.add(bien);
            JsfUtil.addInformationMessage("AVISO", "se ha añadido a la lista de de los bienes a traspasar");
            PrimeFaces.current().ajax().update("bienesDeTraspasoTable");
            PrimeFaces.current().ajax().update("bienesTraspasadosTable");
        }
    }

    public void removerItemDevolucionTraspaso(ActivoFijoServidor bien, String accion) {
        bienesAsignados.add(bien);
        if (accion.equals("DEVOLUCION")) {
            bienesDevolver.remove(bien);
        } else if (accion.equals("TRASPASO")) {
            bienesTraspasar.remove(bien);
        }
    }

    public void guardarAsignacion() {
        if (activoFijoCustodio.getServidor() != null) {
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
        } else {
            JsfUtil.addWarningMessage("AVISO", "Nesesita seleccionar un servidor antes de guardar");
        }
    }

    public ThServidorCargo getServidorByThServidorCargo(Servidor servidor) {
        ThServidorCargo cargoGuardalmacen = thServidorCargoService.findByThServidor(servidor);
        if (cargoGuardalmacen != null) {
            return cargoGuardalmacen;
        } else {
            return null;
        }
    }

    public void imprimirAsignacionCustodio(ActivoFijoCustodio activoAsignado) {
        Servidor guardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        servletSession.addParametro("id_custodio", activoAsignado.getId());
        servletSession.addParametro("nombre_guardalmacen", guardalmacen.getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_guardalmacen", guardalmacen.getPersona().getIdentificacion());
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        ThServidorCargo cargoGuardalmacen = thServidorCargoService.findByThServidor(guardalmacen);
        if (cargoGuardalmacen != null) {
            if (cargoGuardalmacen.getIdCargo() != null) {
                servletSession.addParametro("cargo_guardalmacen", cargoGuardalmacen.getIdCargo().getNombreCargo());
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

    public void darDeBajaCustodio() {
        if (DescripcionDevolucion.equals("")) {
            JsfUtil.addWarningMessage("AVISO", "Debe ingresar la descripción/motivo de la devolución de los bienes");
            return;
        }
        short cero = 0;
        activoFijoCustodio.setCantidadBienes(cero);
        activoFijoCustodio.setEstado(Boolean.FALSE);
        activoFijoCustodio.setUsuarioModificacion(userSession.getNameUser());
        activoFijoCustodio.setFechaModificacion(new Date());
        activoFijoCustodioService.edit(activoFijoCustodio);
        List<ActivoFijoServidor> ListadoDeBaja = activoFijoServidorService.getBienesAsignados(activoFijoCustodio);
        /*DAR DE BAJA A LOS SERVICIOS QUE ESTABAN ASIGNADOS A ESTE CUSTODIO*/
        for (ActivoFijoServidor bienes : ListadoDeBaja) {
            bienes.setEstado(Boolean.FALSE);
            bienes.setAsignado(Boolean.FALSE);
            bienes.setFechaDevolucion(new Date());
            bienes.setObservacionFinal("SERVIDOR DADO DE BAJA");
            activoFijoServidorService.edit(bienes);
        }
        List<Long> aux = actaGuardalmacen(ListadoDeBaja);
        PrimeFaces.current().executeScript("PF('descripcionDevolucionDLG').hide()");
        imprimirDevolucion(activoFijoCustodio, aux);
        restablecerFormulario();
        JsfUtil.addSuccessMessage("ASIGNACIÓN DE CUSTODIO", "Se ha dado de baja correctamente");
    }

    private List<Long> actaGuardalmacen(List<ActivoFijoServidor> bienesList) {
        /*Volvemos a colocar los bienes al guardalmacén activo*/
        ActivoFijoCustodio nuevaActaGuardalmacen = new ActivoFijoCustodio();
        nuevaActaGuardalmacen.setServidor(clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen));
        nuevaActaGuardalmacen.setDescripcion("ESTOS BIENES SON ASIGNADOS AL SERVIDOR CON CARGO DE GUARDALMACEN");
        nuevaActaGuardalmacen.setCantidadBienes((short) bienesList.size());
        nuevaActaGuardalmacen.setFechaCreacion(new Date());
        nuevaActaGuardalmacen.setUsuarioCreacion(userSession.getNameUser());
        nuevaActaGuardalmacen.setEstado(Boolean.TRUE);
        nuevaActaGuardalmacen.setFechaEntrega(new Date());
        ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.TRUE);
        if (ultimaActa != null) {
            nuevaActaGuardalmacen.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
        } else {
            nuevaActaGuardalmacen.setNumeroActa(BigInteger.valueOf(1));
        }
        nuevaActaGuardalmacen.setActaGuardalmacen(Boolean.TRUE);
        nuevaActaGuardalmacen = activoFijoCustodioService.create(nuevaActaGuardalmacen);
        List<Long> aux = new ArrayList<>();
        for (ActivoFijoServidor bienes : bienesList) {
            aux.add(bienes.getId());
            ActivoFijoServidor bienDevuelto = new ActivoFijoServidor();
            bienDevuelto.setActivoFijoCustodio(nuevaActaGuardalmacen);
            bienDevuelto.setBienesItem(bienes.getBienesItem());
            bienDevuelto.setAsignado(Boolean.TRUE);
            bienDevuelto.setEstado(Boolean.FALSE);
            bienDevuelto.setFechaAsignacion(new Date());
            bienDevuelto.setObservacionInicial("");
            bienDevuelto.setObservacionFinal("");
            bienDevuelto.setEstadoBien(bienes.getBienesItem().getEstadoBien().getTexto());
            activoFijoServidorService.create(bienDevuelto);
        }
        return aux;
    }

    public void guardarDevoluciones() {
        if (DescripcionDevolucion.equals("")) {
            JsfUtil.addWarningMessage("DEVOLUCIÓN", "Debe ingresar la descripción explicando le motivo de la devolución");
            return;
        }
        if (bienesDevolver.isEmpty()) {
            JsfUtil.addWarningMessage("DEVOLUCIÓN", "El listado de las devoluciones esta vacio");
        } else {
            /*Objeto del custodio*/
            ActivoFijoCustodio editarCustodio = new ActivoFijoCustodio();
            /*Editamos primero los bienes que vamos a devolver diciendo que ya no estaran a cargo del servidor por lo tanto pasan al guardalmacen*/
            for (ActivoFijoServidor bienes : bienesDevolver) {
                bienes.setAsignado(Boolean.FALSE);
                bienes.setEstado(Boolean.FALSE);
                bienes.setFechaDevolucion(new Date());
                activoFijoServidorService.edit(bienes);
                editarCustodio = bienes.getActivoFijoCustodio();
            }
            /*Actualizamos el monto y la cantidad de Bienes*/
            editarCustodio.setCantidadBienes((short) bienesAsignados.size());
            if (bienesAsignados.isEmpty()) {
                editarCustodio.setEstado(Boolean.FALSE);
            }
            editarCustodio.setFechaModificacion(new Date());
            editarCustodio.setUsuarioModificacion(userSession.getNameUser());
            activoFijoCustodioService.edit(editarCustodio);
            List<Long> aux = actaGuardalmacen(bienesDevolver);
            imprimirDevolucion(editarCustodio, aux);
            restablecerFormulario();
            PrimeFaces.current().executeScript("PF('devolucionCustodioDLG').hide()");
            PrimeFaces.current().ajax().update("activosFijosCustodioTable");
            JsfUtil.addInformationMessage("DEVOLUCIÓN", "se ha realizado con exitó");
        }
    }

    public void imprimirDevolucion(ActivoFijoCustodio activoAsignado, List<Long> listaDevolucion) {
        Servidor guardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        servletSession.addParametro("id_custodio", activoAsignado.getId());
        servletSession.addParametro("descripcion_devolucion", getDescripcionDevolucion());
        servletSession.addParametro("nombre_guardalmacen", guardalmacen.getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_guardalmacen", guardalmacen.getPersona().getIdentificacion());
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.addParametro("bienes", listaDevolucion);
        servletSession.addParametro("fecha_devolucion", new Date());
        ThServidorCargo cargoGuardalmacen = thServidorCargoService.findByThServidor(guardalmacen);
        if (cargoGuardalmacen != null) {
            if (cargoGuardalmacen.getIdCargo() != null) {
                servletSession.addParametro("cargo_guardalmacen", cargoGuardalmacen.getIdCargo().getNombreCargo());
            } else {
                servletSession.addParametro("cargo_guardalmacen", "");
            }
        } else {
            servletSession.addParametro("cargo_guardalmacen", "");
        }
        servletSession.setNombreReporte("DevolucionDeBienes");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void guardarTraspaso() {
        if (activoFijoCustodioTraspaso.getServidor() == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un servidor público a quien se le va a traspasar");
            return;
        }
        if (bienesTraspasar.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay bienes seleccionados para traspasar");
            return;
        }
        if (activoFijoCustodio.getServidor().getId().equals(activoFijoCustodioTraspaso.getServidor().getId())) {
            JsfUtil.addWarningMessage("AVISO", "No se pùede traspasar al mismo servidor");
        } else {
            /*Modificamos los la cantidad de bienes que tenia el custodio actual*/
            activoFijoCustodio.setCantidadBienes((short) bienesAsignados.size());
            activoFijoCustodio.setUsuarioModificacion(userSession.getNameUser());
            activoFijoCustodio.setFechaModificacion(new Date());
            activoFijoCustodioService.edit(activoFijoCustodio);
            /*Damos de baja a los bienes que del custodio actual*/
            for (ActivoFijoServidor bienes : bienesTraspasar) {
                bienes.setAsignado(Boolean.FALSE);
                bienes.setEstado(Boolean.FALSE);
                bienes.setFechaDevolucion(new Date());
                activoFijoServidorService.edit(bienes);
            }
            /*Traer el número de acta de la ultima acta registrada*/
            ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.FALSE);
            /*Creamos el activoFijoCustodio*/
            if (ultimaActa != null) {
                activoFijoCustodioTraspaso.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
            } else {
                activoFijoCustodioTraspaso.setNumeroActa(BigInteger.valueOf(1));
            }
            /*Creamos al nuevo servidor que se les traspaso los bienes*/
            activoFijoCustodioTraspaso.setActaGuardalmacen(Boolean.FALSE);
            activoFijoCustodioTraspaso.setUsuarioCreacion(userSession.getNameUser());
            activoFijoCustodioTraspaso.setFechaCreacion(new Date());
            activoFijoCustodioTraspaso.setEstado(Boolean.TRUE);
            activoFijoCustodioTraspaso.setFechaEntrega(new Date());
            activoFijoCustodioTraspaso.setCantidadBienes((short) bienesTraspasar.size());
            activoFijoCustodioTraspaso = activoFijoCustodioService.create(activoFijoCustodioTraspaso);
            /*asignamos el los bienes al nuevo servidor*/
            for (ActivoFijoServidor bienes : bienesTraspasar) {
                activoFijoServidor = new ActivoFijoServidor();
                activoFijoServidor.setActivoFijoCustodio(activoFijoCustodioTraspaso);
                activoFijoServidor.setAsignado(Boolean.TRUE);
                activoFijoServidor.setEstado(Boolean.TRUE);
                activoFijoServidor.setObservacionInicial(bienes.getObservacionFinal());
                activoFijoServidor.setObservacionFinal(null);
                activoFijoServidor.setBienesItem(bienes.getBienesItem());
                activoFijoServidor.setEstadoBien(bienes.getBienesItem().getEstadoBien().getTexto());
                activoFijoServidor.setFechaAsignacion(new Date());
                activoFijoServidor = activoFijoServidorService.create(activoFijoServidor);
            }
            imprimirTraspaso(activoFijoCustodio, activoFijoCustodioTraspaso);
            restablecerFormulario();
            PrimeFaces.current().executeScript("PF('traspasoCustodioDLG').hide()");
            JsfUtil.addInformationMessage("TRASPASO DE CUSTODIO", "Se ha realizado con exitó");
        }
    }

    public void imprimirTraspaso(ActivoFijoCustodio servidorAnterior, ActivoFijoCustodio servidorNuevo) {
        servletSession.addParametro("id_servidorNuevo", servidorNuevo.getId());
        servletSession.addParametro("nombre_servidorAnterior", servidorAnterior.getServidor().getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_servidorAnterior", servidorAnterior.getServidor().getPersona().getIdentificacion());

        ThServidorCargo cargoServidorAnterior = thServidorCargoService.findByThServidor(servidorAnterior.getServidor());
        if (cargoServidorAnterior != null) {
            if (cargoServidorAnterior.getIdCargo().getIdUnidad() != null) {
                servletSession.addParametro("unidad_servidorAnterior", cargoServidorAnterior.getIdCargo().getIdUnidad().getNombre());
            } else {
                servletSession.addParametro("unidad_servidorAnterior", "");
            }
            if (cargoServidorAnterior.getIdCargo() != null) {
                servletSession.addParametro("cargo_servidorAnterior", cargoServidorAnterior.getIdCargo().getNombreCargo());
            } else {
                servletSession.addParametro("cargo_servidorAnterior", "");
            }
        } else {
            servletSession.addParametro("unidad_servidorAnterior", "");
            servletSession.addParametro("cargo_servidorAnterior", "");
        }
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.setNombreReporte("TraspasoDeCustodio");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirHistorial(ActivoFijoCustodio acta) {
        Servidor guardalmacen = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        servletSession.addParametro("id_custodio", acta.getId());
        servletSession.addParametro("nombre_guardalmacen", guardalmacen.getPersona().getNombreCompleto());
        servletSession.addParametro("cedula_guardalmacen", guardalmacen.getPersona().getIdentificacion());
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        ThServidorCargo cargoGuardalmacen = thServidorCargoService.findByThServidor(guardalmacen);
        if (cargoGuardalmacen != null) {
            if (cargoGuardalmacen.getIdCargo() != null) {
                servletSession.addParametro("cargo_guardalmacen", cargoGuardalmacen.getIdCargo().getNombreCargo());
            } else {
                servletSession.addParametro("cargo_guardalmacen", "");
            }
        } else {
            servletSession.addParametro("cargo_guardalmacen", "");
        }
        servletSession.setNombreReporte("ReimprimirAsignacionDeCustodio");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void asignarGuardalmacen() {
        ActivoFijoCustodio guardalmacen = new ActivoFijoCustodio();
        Servidor guardalmacenServidor = clienteService.getResponsableTransferencia(RolUsuario.guardaAlmacen);
        guardalmacen.setServidor(guardalmacenServidor);
        guardalmacen.setDescripcion("ESTOS BIENES SON ASIGNADOS AL SERVIDOR CON CARGO DE GUARDALMACEN");
        List<BienesItem> bienes = activoFijoCustodioService.listadoDeBienes();
        guardalmacen.setCantidadBienes((short) bienes.size());
        guardalmacen.setFechaCreacion(new Date());
        guardalmacen.setUsuarioCreacion(userSession.getNameUser());
        guardalmacen.setEstado(Boolean.TRUE);
        guardalmacen.setFechaEntrega(new Date());
        //Traer el número de acta de la ultima acta registrada del guardalmacen/
        ActivoFijoCustodio ultimaActa = activoFijoCustodioService.getUltimaActa(Boolean.TRUE);
        //Creamos el activoFijoCustodio/
        if (ultimaActa != null) {
            guardalmacen.setNumeroActa(BigInteger.valueOf(ultimaActa.getNumeroActa().longValue() + 1));
        } else {
            guardalmacen.setNumeroActa(BigInteger.valueOf(1));
        }
        guardalmacen.setActaGuardalmacen(Boolean.TRUE);
        guardalmacen = activoFijoCustodioService.create(guardalmacen);
        for (BienesItem bien : bienes) {
            ActivoFijoServidor guardalmacen2 = new ActivoFijoServidor();
            guardalmacen2.setActivoFijoCustodio(guardalmacen);
            guardalmacen2.setBienesItem(bien);
            guardalmacen2.setAsignado(Boolean.TRUE);
            guardalmacen2.setFechaAsignacion(new Date());
            guardalmacen2.setEstado(Boolean.FALSE);
            guardalmacen2.setEstadoBien(bien.getEstadoBien().getTexto());
            activoFijoServidorService.create(guardalmacen2);
        }
        JsfUtil.addInformationMessage("GUARDALMACEN", "Se le asigno correctamente los bienes al guardalmacen");
    }

    private void restablecerFormulario() {
        this.bienesSeleccionados = new ArrayList<>();
        this.bienesAsignados = new ArrayList<>();
        this.bienesDevolver = new ArrayList<>();
        this.bienesItemList = new ArrayList<>();
        this.bienesTraspasar = new ArrayList<>();
        this.activoFijoCustodioTraspaso = new ActivoFijoCustodio();
        this.ocultarBoton = Boolean.TRUE;
        this.DescripcionDevolucion = "";
    }

    public void removerBien(ActivoFijoServidor bienes) {
        bienesSeleccionados.remove(bienes);
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

    public LazyModel<ThServidorCargo> getThServidorLazyModel() {
        return thServidorLazyModel;
    }

    public void setThServidorLazyModel(LazyModel<ThServidorCargo> thServidorLazyModel) {
        this.thServidorLazyModel = thServidorLazyModel;
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

    public List<ActivoFijoServidor> getBienesDevolver() {
        return bienesDevolver;
    }

    public void setBienesDevolver(List<ActivoFijoServidor> bienesDevolver) {
        this.bienesDevolver = bienesDevolver;
    }

    public List<ActivoFijoServidor> getBienesAsignados() {
        return bienesAsignados;
    }

    public void setBienesAsignados(List<ActivoFijoServidor> bienesAsignados) {
        this.bienesAsignados = bienesAsignados;
    }

    public ActivoFijoCustodio getActivoFijoCustodioTraspaso() {
        return activoFijoCustodioTraspaso;
    }

    public void setActivoFijoCustodioTraspaso(ActivoFijoCustodio activoFijoCustodioTraspaso) {
        this.activoFijoCustodioTraspaso = activoFijoCustodioTraspaso;
    }

    public Boolean getOcultarBoton() {
        return ocultarBoton;
    }

    public void setOcultarBoton(Boolean ocultarBoton) {
        this.ocultarBoton = ocultarBoton;
    }

    public List<ActivoFijoServidor> getBienesTraspasar() {
        return bienesTraspasar;
    }

    public void setBienesTraspasar(List<ActivoFijoServidor> bienesTraspasar) {
        this.bienesTraspasar = bienesTraspasar;
    }

    public ActivoFijoCustodio getActivoAdicional() {
        return activoAdicional;
    }

    public void setActivoAdicional(ActivoFijoCustodio activoAdicional) {
        this.activoAdicional = activoAdicional;
    }

    public LazyModel<ActivoFijoCustodio> getHistorialLazyModel() {
        return historialLazyModel;
    }

    public void setHistorialLazyModel(LazyModel<ActivoFijoCustodio> historialLazyModel) {
        this.historialLazyModel = historialLazyModel;
    }

    public String getDescripcionDevolucion() {
        return DescripcionDevolucion;
    }

    public void setDescripcionDevolucion(String DescripcionDevolucion) {
        this.DescripcionDevolucion = DescripcionDevolucion;
    }

    public List<Servidor> getServidores() {
        return servidores;
    }

    public void setServidores(List<Servidor> servidores) {
        this.servidores = servidores;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public ThServidorCargo getThServidorCargo() {
        return thServidorCargo;
    }

    public void setThServidorCargo(ThServidorCargo thServidorCargo) {
        this.thServidorCargo = thServidorCargo;
    }

    public ThServidorCargo getThServidorCargoTraspaso() {
        return thServidorCargoTraspaso;
    }

    public void setThServidorCargoTraspaso(ThServidorCargo thServidorCargoTraspaso) {
        this.thServidorCargoTraspaso = thServidorCargoTraspaso;
    }

//</editor-fold>
}
