/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.ReservaCompromisoDetalleLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.SoliciudReservaCompromisoLazy;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "seleccionReservaView")
@ViewScoped
public class SeleccionReservaController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ProductoService productoService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;

    private SolicitudReservaCompromiso reservaCompromiso;
    private List<Producto> listaProductos;
    private List<UnidadAdministrativa> listaUnidades;
    private List<Producto> listadeProductoSeleccionados;
    private List<DetalleSolicitudCompromiso> ListadetalleSolicitud;
    private DetalleSolicitudCompromiso detalleSolicitud;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas;
    private Producto producto;
    private boolean mostrarOculutarTabla;
    private List<DetalleSolicitudCompromiso> listaTotal;
    private String cbTipo;
    private boolean ocultarcb;
    private List<ProformaPresupuestoPlanificado> listaPartidasDirectas;
    private boolean tabla1;
    private boolean tabla2;
    private boolean tabla3;
    private List<DetalleSolicitudCompromiso> listaGuardar;
    private List<Presupuesto> listaPresupuesto;

    private ReservaCompromisoDetalleLazy lazy;
    private SoliciudReservaCompromisoLazy lazyPrincipall;
    private List<DetalleSolicitudCompromiso> visualizacionSolicitud;
    private LazyModel<SolicitudRequisito> solicitudRequisitoLazyModel;
    private boolean panel1, panel2, ocultarbutton, mostrarButton;
    private CatalogoItem catalogoItem;
    private Cliente beneficiario;
    private ProveedorLazy proveedorLazy;
    private ServidorLazy servidorLazy;
    private Distributivo cargoUnidadUser;
    private BigDecimal valorAnteirorPresupuesto;
    private String observaciones;

    @Inject
    private UserSession user;

    /*CRISS*/
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    private List<Procedimiento> procedimientoList;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;
    private UploadedFile uploadFile;
    private SolicitudRequisito solicitudRequisito;
    private ProcedimientoRequisito procedimientoRequisito;

    private String fileName;

    private LazyModel<SolicitudReservaCompromiso> lazyReal;
    private List<DetalleSolicitudCompromiso> listaeditable1;
    private List<DetalleSolicitudCompromiso> listaeditable2;
    private double num1, num2;
    private double result1, result2;
    private boolean panelProveedor, panelServidor;
    private List<CatalogoItem> estadoFiltros;
    private List<UnidadAdministrativa> unidadFiltros;

    private static final Logger LOG = Logger.getLogger(SeleccionReservaController.class.getName());

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;

    /*FIN CRISS*/
    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                fileName = "";
                reservaCompromiso = new SolicitudReservaCompromiso();
                this.listaUnidades = unidadService.findByNamedQuery("UnidadAdministrativa.TiposUnidades", "DIR", true);
                this.listaProductos = new ArrayList<>();
                this.listadeProductoSeleccionados = new ArrayList<>();
                detalleSolicitud = new DetalleSolicitudCompromiso();
                listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
                ListadetalleSolicitud = new ArrayList<>();
                mostrarOculutarTabla = true;
                listaTotal = new ArrayList<>();
                ocultarcb = true;
                listaPartidasDirectas = new ArrayList<>();
                tabla1 = false;
                tabla2 = false;
                tabla3 = false;
                listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
                listaGuardar = new ArrayList<>();
                this.listaPresupuesto = new ArrayList<>();
                this.lazy = new ReservaCompromisoDetalleLazy();
                this.beneficiario = new Cliente();
                visualizacionSolicitud = new ArrayList<>();
                panel1 = true;
                panel2 = false;
                ocultarbutton = false;
                mostrarButton = false;
                catalogoItem = new CatalogoItem();

                switch (tarea.getTaskDefinitionKey()) {
                    case "eleigir_a":
                        lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
                        lazyReal.getFilterss().put("estado.codigo:notEqual", Arrays.asList("ADA", "LIQUI", "LIBE"));
                        lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
                        break;
                    case "eleigir_reserva_id":
                        lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
                        lazyReal.getFilterss().put("estado.codigo", Arrays.asList("APRO"));
                        lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
                        break;

                    case "elegir_l":
                        lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
                        lazyReal.getFilterss().put("estado.codigo", Arrays.asList("APRO", "LIBE"));
                        lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
                        break;
                    case "elegir_libe":
                        lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
                        lazyReal.getFilterss().put("estado.codigo", Arrays.asList("APRO"));
                        lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
                        break;
                }

//        if (userSession.hasRole("admin")) {
//            Distributivo usrLogeado = clienteService.getuusuarioLogeado(userSession.getNameUser());
//            if (usrLogeado != null) {
//                lazyReal.getFilterss().put("unidadRequiriente:equal", usrLogeado.getUnidadAdministrativa());
//            }
//        }
                estadoFiltros = new ArrayList<>();
                estadoFiltros = catalogoService.getItemsByCatalogo("estado_solicitud");
                unidadFiltros = new ArrayList<>();
                unidadFiltros = reservaCompromisoService.getListaUnidadesReservas();

                procedimientoSeleccionado = new Procedimiento();
                solicitudRequisito = new SolicitudRequisito();
                procedimientoList = procedimientoService.getProcedimientos("RC");
                procedimientoRequisito = new ProcedimientoRequisito();
                solicitudRequisitoList = new ArrayList<>();
                solicitudRequisitoLazyModel = new LazyModel<>(SolicitudRequisito.class);
                listaeditable1 = new ArrayList<>();
                listaeditable2 = new ArrayList<>();
                num1 = 0;
                num2 = 0;
                result1 = 0;
                result2 = 0;
                panelProveedor = true;
                panelServidor = false;
                cargoUnidadUser = new Distributivo();
                this.valorAnteirorPresupuesto = BigDecimal.ZERO;
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void consultarMostrarAndOcultarPaneles() {
        panel1 = true;
        panel2 = false;

    }

    public void registarrMostrarAndOcultarPaneles() {
        //reservaCompromiso = new SolicitudReservaCompromiso();
        panel1 = false;
        panel2 = true;
        ocultarbutton = false;
        mostrarButton = true;
        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
    }

    public void inicializarMaxcimoCodigo() throws ParseException {
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);

    }

    public void enviarSolicitud(SolicitudReservaCompromiso s) {
        this.reservaCompromiso = s;
        reservaCompromiso.setEstado(reservaCompromisoService.getestados("E", "estado_solicitud"));
        reservaCompromisoService.edit(reservaCompromiso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envio correctamente");
        reservaCompromiso = new SolicitudReservaCompromiso();

    }

    public void planAnualesActividades() {

        this.listaProductos = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), reservaCompromiso.getUnidadRequiriente());

        PrimeFaces.current().executeScript("PF('Dlogo2').show()");
        PrimeFaces.current().ajax().update(":formDlogo2");

    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    /**
     * Realiza la llamanda de los diaoogos segun su condicion que puede ser el
     * PAPP, Plan Distriubtivo o las partidas Directas.
     */
    public void accionComponenetes() throws ParseException {

        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 0) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        if (reservaCompromiso.getPeriodo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

        } else {

            tabla1 = true;
            tabla2 = false;
            tabla3 = false;
            cargoUnidadUser = clienteService.getuusuarioLogeado(userSession.getNameUser());
            reservaCompromiso.setUnidadRequiriente(cargoUnidadUser.getUnidadAdministrativa());

            Distributivo d = clienteService.getuusuarioLogeado(user.getNameUser());

            List<Producto> listaAnaidir = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), d.getUnidadAdministrativa());

            if (!listaAnaidir.isEmpty()) {
                this.listaProductos.clear();

                for (Producto item1 : listaAnaidir) {
                    if (!this.listaGlobalDetalleSolicitudCompromisos.isEmpty()) {

                        for (DetalleSolicitudCompromiso datos : listaGlobalDetalleSolicitudCompromisos) {
                            if (!datos.getActividadProducto().equals(item1)) {
                                this.listaProductos.add(item1);
                            }
                        }
                    } else {
                        this.listaProductos.add(item1);
                    }

                }

            }

            if (listaProductos.isEmpty()) {

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No hay datos " + reservaCompromiso.getPeriodo());
                return;
            }

            inicializarMaxcimoCodigo();
            PrimeFaces.current().executeScript("PF('Dlogo2').show()");
            PrimeFaces.current().ajax().update(":formDlogo2");

        }

    }

    public void accionComponentesDistriuvtioPartida() throws ParseException {

        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ECUENTRA APROBADO");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 1) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        cargoUnidadUser = clienteService.getuusuarioLogeado(userSession.getNameUser());
        reservaCompromiso.setUnidadRequiriente(cargoUnidadUser.getUnidadAdministrativa());

        if (reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            if (reservaCompromiso.getPeriodo() == null && reservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

            } else {
                inicializarMaxcimoCodigo();
                List<Presupuesto> listaPr = reservaCompromisoService.getPresupuestoPartidasDandPD(reservaCompromiso.getPeriodo(), Arrays.asList("PD", "PDI", "PDA"));
                listaPresupuesto.clear();
                for (Presupuesto dataPr : listaPr) {

                    if (!this.listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                        for (DetalleSolicitudCompromiso dataPar : listaGlobalDetalleSolicitudPartidas) {
                            if (!dataPar.getPresupuesto().equals(dataPr)) {
                                listaPresupuesto.add(dataPr);
                            }
                        }
                    } else {

                        listaPresupuesto.add(dataPr);

                    }
                }
                PrimeFaces.current().executeScript("PF('Dlogo3').show()");
                PrimeFaces.current().ajax().update(":formDlogo3");
            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + reservaCompromiso.getPeriodo());
        }

    }

    public void tipoCbombo() {
        if (cbTipo.equals("PAPP")) {

        } else if (cbTipo.equals("PDI")) {
            ocultarcb = true;
            tabla1 = false;
            tabla2 = true;
            tabla3 = false;

            //  this.listaPartidasDirectas = reservaCompromisoService.GetPartidasDirectcas(reservaCompromiso.getPeriodo(), "PDI");
            PrimeFaces.current().executeScript("PF('Dlogo3').show()");
            PrimeFaces.current().ajax().update(":formDlogo3");
        }

    }

    public Boolean validarRequisitos() {
        boolean validador = true;
        Long cantidadNoRequeridas = procedimientoRequisitoService.getCantidadRequerida(procedimientoSeleccionado, false);
        Long cantidadRequeridas = procedimientoRequisitoService.getCantidadRequerida(procedimientoSeleccionado, true);
        if (cantidadNoRequeridas == procedimientoRequisitoList.size()) {
            return false;
        } else {
            if (solicitudRequisitoList.size() > 0) {
                //consulto la cantidad de archivos que SI son requeridos
                if (cantidadRequeridas <= solicitudRequisitoList.size()) {
                    Short contador = 0;
                    for (SolicitudRequisito solicitud : solicitudRequisitoList) {
                        if (solicitud.getIdProcedimientoRequisito().getObligatorio() == true) {
                            contador++;
                        }
                    }
                    if (contador < cantidadRequeridas) {
                        JsfUtil.addWarningMessage("REQUISITOS:", "FALTA SUBIR ARCHIVOS EN " + (cantidadRequeridas - contador) + " REQUISITO(S) OBLIGATORIO");
                        validador = true;
                    } else {
                        validador = false;
                    }
                } else {
                    JsfUtil.addWarningMessage("REQUISITOS:", "EXISTEN ARCHIVOS SIN SUBIR EN LOS REQUISITOS OBLIGATORIOS");
                    validador = true;
                }
                return validador;
            } else {
                JsfUtil.addWarningMessage("REQUISITOS:", "SUBIR ARCHIVOS EN LOS " + cantidadRequeridas + " REQUISITO(S) OBLIGATORIO");
                return validador;
            }
        }
    }

    public void eliminarSolicitudCompleta(SolicitudReservaCompromiso s) {
        reservaCompromisoService.remove(s);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solcitud eliminada");
    }

    public void savesolicitud() {

        if (procedimientoSeleccionado.getId() != null) {
            if (validarRequisitos()) {
                return;
            }
        } else {
            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }

        boolean verificar = true;
        boolean pararMetodo = true;
        listaGuardar = new ArrayList<>();

        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();
            //result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            }
            if (result1 > itemsuma.getActividadProducto().getMonto().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo par su reserva solicitada");
                break;
            }

        }

        if (!verificar) {

            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {

            num2 = 0;
            result2 = 0;
            num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL DISTRIBUIDO ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
            }

            if (result2 > itemverificar.getPresupuesto().getValorEgreso().doubleValue()) {
                pararMetodo = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }

        }

        if (!pararMetodo) {

            return;
        }

        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty()) {

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {
                    listaGuardar.add(l);
                });
            }

            if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                listaGlobalDetalleSolicitudPartidas.forEach((item) -> {
                    listaGuardar.add(item);
                });
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No hay datos Seleccionados");
            return;
        }

        int resultado = 0;
        boolean verificacion = true;
        reservaCompromiso.setFechaCreacion(new Date());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromiso.setUsuarioCreacion(userSession.getName());
        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setEstado(reservaCompromisoService.getestados("R", "estado_solicitud"));
        reservaCompromiso.setProcedimiento(procedimientoSeleccionado);
        reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);

        for (DetalleSolicitudCompromiso li : listaGuardar) {
            detalleSolicitud = new DetalleSolicitudCompromiso();

            detalleSolicitud.setSolicitud(reservaCompromiso);

            detalleSolicitud.setActividadProducto(li.getActividadProducto());
            detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
            detalleSolicitud.setEstado(true);
            detalleSolicitud.setPresupuesto(li.getPresupuesto());
            detalleSolicitud.setFechaCreacion(new Date());
            detalleSolicitud.setFechaModifcacion(new Date());
            detalleSolicitud.setUsuarioCreacion(userSession.getName());
            detalleSolicitud.setUsuarioModificacion(userSession.getName());
            detalleSolicitud.setPeriodo(li.getPeriodo());
            detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
            detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());

            detalleSolicitud = detalleReservaService.create(detalleSolicitud);

        }

        //Guardamos la los requisitos en relacion a la reserva de compromiso que se creo en la linea de codigo anterior
        for (SolicitudRequisito solicitud : solicitudRequisitoList) {
            solicitudRequisito = new SolicitudRequisito();
            solicitudRequisito.setIdSolicitudReserva(reservaCompromiso);
            solicitudRequisito.setIdProcedimientoRequisito(solicitud.getIdProcedimientoRequisito());
            solicitudRequisito.setUrl(solicitud.getUrl());
            solicitudRequisito = solicitudRequisitoService.create(solicitudRequisito);
        }

        if (verificacion) {

            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addSuccessMessage("SOLICITUD", "La solicitud " + reservaCompromiso.getSecuencial() + "-" + reservaCompromiso.getPeriodo() + " se ha realizado con éxito");
            listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
            listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
            listaGuardar = new ArrayList<>();
            solicitudRequisitoList = new ArrayList<>();
            procedimientoRequisitoList = new ArrayList<>();
            reservaCompromiso = new SolicitudReservaCompromiso();
            detalleSolicitud = new DetalleSolicitudCompromiso();
            procedimientoSeleccionado = new Procedimiento();
            servidorLazy = null;
            proveedorLazy = null;
            setCbTipo("");
            consultarMostrarAndOcultarPaneles();
        } else {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Los campos no pueder ser nulo o tener valor con ceros");

        }
    }

    public void añadiendoFormato(Producto p) {
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        detalleSolicitud = new DetalleSolicitudCompromiso();
        producto = p;

        detalleSolicitud.setActividadProducto(producto);
        detalleSolicitud.setEstado(true);
        detalleSolicitud.setFechaCreacion(new Date());
        detalleSolicitud.setFechaModifcacion(new Date());
        detalleSolicitud.setUsuarioCreacion(userSession.getName());
        detalleSolicitud.setUsuarioModificacion(userSession.getName());
        detalleSolicitud.setPeriodo(reservaCompromiso.getPeriodo());

        BigDecimal resultado_valor_disponble = reservaCompromisoService.getSueldoDisponible(producto, reservaCompromiso.getPeriodo());

        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detalleSolicitud.setMontoDisponible(producto.getMontoReformada());

        } else {

            maximo_resultado = (detalleSolicitud.getActividadProducto().getMontoReformada()).subtract(resultado_valor_disponble);

            detalleSolicitud.setMontoDisponible(maximo_resultado);

        }

        detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
        listaGlobalDetalleSolicitudCompromisos.add(detalleSolicitud);
        listaProductos.remove(p);
        JsfUtil.addInformationMessage("INFORMATION", "PRODUCTO SELECIONADO CORRECTAMENTE");
        producto = new Producto();
        detalleSolicitud = new DetalleSolicitudCompromiso();
    }

    public void eliminadoMemoriaDetalleSOlicitud(DetalleSolicitudCompromiso d) {
        listaGlobalDetalleSolicitudCompromisos.remove(d);
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void abrirDlgo(DetalleSolicitudCompromiso d) {
        detalleSolicitud = d;

        PrimeFaces.current().executeScript("PF('Dlogo').show()");
        PrimeFaces.current().ajax().update(":formDlogo");
    }

    public void abrirDlgoPartidas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = d;
        valorAnteirorPresupuesto = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "el monto no debe ser menor al monto comprometido");

            }
        }

    }

    public void solicitarReservas(DetalleSolicitudCompromiso d) {

        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "el monto no debe ser menor al monto comprometido");

            }

        }

        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "el monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void solicitarReservasPartidas(DetalleSolicitudCompromiso d) {

        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "el monto no debe ser menor al monto comprometido");

            }

        }

        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "el monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void eliminarPArtidasDirectasAndDistributivo(DetalleSolicitudCompromiso d) {
        listaGlobalDetalleSolicitudPartidas.remove(d);
    }

    public void seleccionPresupuestoDPD(Presupuesto p) {

        detalleSolicitud = new DetalleSolicitudCompromiso();
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        detalleSolicitud.setPresupuesto(p);
        detalleSolicitud.setEstado(true);
        detalleSolicitud.setFechaCreacion(new Date());
        detalleSolicitud.setFechaModifcacion(new Date());
        detalleSolicitud.setUsuarioCreacion(userSession.getName());
        detalleSolicitud.setUsuarioModificacion(userSession.getName());
        detalleSolicitud.setPeriodo(reservaCompromiso.getPeriodo());

        BigDecimal resultado_valor_disponble = reservaCompromisoService.getSueldoDisponiblePresupuesto(p, reservaCompromiso.getPeriodo());

        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detalleSolicitud.setMontoDisponible(p.getCodificado());

        } else {

            maximo_resultado = detalleSolicitud.getPresupuesto().getCodificado().subtract(resultado_valor_disponble);

            detalleSolicitud.setMontoDisponible(maximo_resultado);

        }
        detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
        listaGlobalDetalleSolicitudPartidas.add(detalleSolicitud);
        listaPresupuesto.remove(p);
        detalleSolicitud = new DetalleSolicitudCompromiso();
        JsfUtil.addInformationMessage("INFORMATION", "PARTIDA SELECIONADO CORRECTAMENTE");

    }

    public void visualizarDetalleSolcitud(SolicitudReservaCompromiso s) {

        reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        this.procedimientoSeleccionado = s.getProcedimiento();

        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        this.visualizacionSolicitud = reservaCompromisoService.getListaDetlleSolciitud(s);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('DlgoVisualizacion').show()");
        PrimeFaces.current().ajax().update(":formVisualizacion");

    }

    public void editar() {
        reservaCompromisoService.edit(reservaCompromiso);
        reservaCompromiso = new SolicitudReservaCompromiso();
    }

    public void edicionSolicitud(SolicitudReservaCompromiso s) {
        reservaCompromiso = new SolicitudReservaCompromiso();
        ocultarbutton = true;
        mostrarButton = false;
        listaGuardar = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;

        if (!"APRO".equals(reservaCompromiso.getEstado().getCodigo())) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "La solicitud debe estar aprobada para poder actualizarla");

            return;
        }

        this.procedimientoSeleccionado = s.getProcedimiento();

        listaeditable1 = reservaCompromisoService.getEdicionDetalleProducto(s);
        listaeditable2 = reservaCompromisoService.getEdicionDetallePDAndD(s);

        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }

        listaeditable1.forEach((detalle1) -> {
            this.listaGlobalDetalleSolicitudCompromisos.add(detalle1);
        });

        listaeditable2.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicitudPartidas.add(detalle2);
        });

        panel1 = false;
        panel2 = true;

        listaGlobalDetalleSolicitudCompromisos.forEach((lista1) -> {
            lista1.setMontoDisponible(lista1.getActividadProducto().getMonto().subtract(reservaCompromisoService.getSumaEdicionProductos(lista1.getActividadProducto())).add(lista1.getMontoSolicitado()));
        });

        listaGlobalDetalleSolicitudPartidas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getPresupuesto().getValorEgreso().subtract(reservaCompromisoService.getSumaEdicionPresupuesto(lis.getRefDistributivo().getId(), true)).add(lis.getMontoSolicitado()));
        });

        PrimeFaces.current().ajax().update("formMain");
    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    setFileName("/resources/demo/media/" + pr.getUrl());
                    break;
                } else {
                    setFileName("");
                }
            }
            if (getFileName().equals("")) {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void eliminarPDF(ProcedimientoRequisito procedimientoRequisito) {
        Boolean auxiliar = true;
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    solicitudRequisitoList.remove(pr);
                    break;
                } else {
                    auxiliar = false;
                }
            }
            if (auxiliar) {
                JsfUtil.addInformationMessage("INFORMACIÓN", "DOCUMENTO ADJUNTO ELIMINADO CORRECTAMENTE");
            } else {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            }
        }
    }

    public void cargarRequisitos() {
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        PrimeFaces.current().ajax().update("requisitosTabla");
    }

    public void requisitoSeleccionado(ProcedimientoRequisito procedimientoRequisito) {
        for (SolicitudRequisito requisito : solicitudRequisitoList) {
            if (Objects.equals(requisito.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                if (!requisito.getUrl().equals("")) {
                    JsfUtil.addWarningMessage("INFORMACIÓN", "YA HA SIDO CARGADO UN ARCHIVO A ESTE REQUISITO");
                    return;
                }
            }
        }
        this.procedimientoRequisito = procedimientoRequisito;
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        this.uploadFile = event.getFile();
        byte[] contents = uploadFile.getContents();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String filePath = ec.getRealPath(String.format("/resources/demo/media/%s", uploadFile.getFileName()));

        this.solicitudRequisito = new SolicitudRequisito();
        // ruta_auxiliar = ruta_temporal + fileName.replace("", "");
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(contents);
        fos.close();
        this.solicitudRequisito.setIdSolicitudReserva(null);
        this.solicitudRequisito.setUrl(event.getFile().getFileName());
        this.solicitudRequisito.setIdProcedimientoRequisito(procedimientoRequisito);
        this.solicitudRequisitoList.add(solicitudRequisito);

        for (SolicitudRequisito dataR : solicitudRequisitoList) {

        }

        PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
        PrimeFaces.current().ajax().update("requisitoDialogForm");

        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void editarReservaCompromiso() {
        List<DetalleSolicitudCompromiso> listaValoresAnteriores;
        if (procedimientoSeleccionado == null) {

            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }
        boolean verificar = true;
        boolean pararMetodo = true;

        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty()) {

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {

                    listaGuardar.add(l);
                });
            }

            if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
                listaGlobalDetalleSolicitudPartidas.forEach((item) -> {

                    listaGuardar.add(item);
                });
            }

        }

        if (listaGuardar.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No hay datos");
            return;
        }

        listaValoresAnteriores = listaGuardar;

//        reservaCompromisoService.eliminarSOlicitudesCascade(reservaCompromiso);
//        reservaCompromisoService.eliminarSoclitudRequisitios(reservaCompromiso);
        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();

            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
            }

            if (result1 > itemsuma.getActividadProducto().getMontoReformada().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo par su reserva solicitada");
                break;
            }

        }

        if (!verificar) {

            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {

            num2 = 0;
            result2 = 0;
            num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();

            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
            }

            if (result2 > itemverificar.getPresupuesto().getCodificado().doubleValue()) {
                pararMetodo = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }

        }

        if (!pararMetodo) {

            return;
        }

        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromisoService.edit(reservaCompromiso);

//        for (SolicitudRequisito solicitud : solicitudRequisitoList) {
//            solicitudRequisito = new SolicitudRequisito();
//            solicitudRequisito.setIdSolicitudReserva(reservaCompromiso);
//            solicitudRequisito.setIdProcedimientoRequisito(solicitud.getIdProcedimientoRequisito());
//            solicitudRequisito.setUrl(solicitud.getUrl());
//            solicitudRequisito = solicitudRequisitoService.create(solicitudRequisito);
//        }
        for (DetalleSolicitudCompromiso li : listaGuardar) {
            detalleSolicitud = new DetalleSolicitudCompromiso();
            detalleSolicitud.setId(li.getId());
            detalleSolicitud.setSolicitud(reservaCompromiso);
            detalleSolicitud.setActividadProducto(li.getActividadProducto());
            detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
            detalleSolicitud.setEstado(true);
            detalleSolicitud.setPresupuesto(li.getPresupuesto());
            detalleSolicitud.setFechaCreacion(new Date());
            detalleSolicitud.setFechaModifcacion(new Date());
            detalleSolicitud.setUsuarioCreacion(userSession.getName());
            detalleSolicitud.setUsuarioModificacion(userSession.getName());
            detalleSolicitud.setPeriodo(li.getPeriodo());
            detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
            detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
            detalleSolicitud.setMontoComprometido(valorAnteirorPresupuesto);

            for (DetalleSolicitudCompromiso lli : listaValoresAnteriores) {

                if (lli.equals(li)) {
                    detalleSolicitud.setMontoComprometido(lli.getMontoComprometido());
                }
            }

            detalleReservaService.edit(detalleSolicitud);

        }

        short periodo = reservaCompromiso.getPeriodo();
        List<Producto> listP = reservaCompromisoService.listaReservasSinRepetirProducto(periodo);
        BigDecimal valor = BigDecimal.ZERO, valor2 = BigDecimal.ZERO;
        if (listP != null && !listP.isEmpty()) {
            for (Producto pr : listP) {

                valor = reservaCompromisoService.sumaTotalDeReservasProducto(periodo, pr.getId());
                int result = reservaCompromisoService.updateReservaProducto(valor, periodo, pr.getId());

                if (result > 0) {

                }
            }
        }
        List<ThCargoRubros> lis = reservaCompromisoService.listaReservasSinRepetir(periodo);
        if (lis != null && !lis.isEmpty()) {
            for (ThCargoRubros li : lis) {
                valor2 = reservaCompromisoService.sumaTotalDeReservasDevengado(periodo, li.getId());
                int result2 = reservaCompromisoService.actualizarReservaPresupuesto(valor2, periodo, li.getId());

                if (result2 > 0) {

                }
            }
        }

        List<ProformaPresupuestoPlanificado> tmpPd = reservaCompromisoService.listaReservasPdSinRepetir(periodo);

        if (tmpPd != null && !tmpPd.isEmpty()) {
            for (ProformaPresupuestoPlanificado li : tmpPd) {
                valor2 = reservaCompromisoService.sumaTotalDeReservasDevengadoPd(periodo, li.getId());
                int result2 = reservaCompromisoService.actualizarReservaPresupuestoPd(valor2, periodo, li.getId());

                if (result2 > 0) {

                }
            }
        }
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "editado correctamente");
        resetSolicitud();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        consultarMostrarAndOcultarPaneles();

    }

    public void resetSolicitud() {
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        ListadetalleSolicitud = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoSeleccionado = new Procedimiento();
    }

    public void CargarDatosBeneficiario() {
        if (reservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (reservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                proveedorLazy = new ProveedorLazy();
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new ServidorLazy();
            }
        } else {
            PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        }
    }

    public void AñadirBeneficiarioServidor(Servidor servidor) {
        reservaCompromiso.setBeneficiario(servidor.getPersona());
        reservaCompromiso.setTipoBeneficiario(Boolean.FALSE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        reservaCompromiso.setBeneficiario(provedor.getCliente());
        reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
    }

    public void handleCloseForm(CloseEvent event) {
        resetSolicitud();
        PrimeFaces.current().ajax().update(":DlgoVisualizacion");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:consultaTable");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:consultaTable");
        }
    }

    public boolean verificarMes(Date fecha) {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");

        simpleDateFormat = new SimpleDateFormat("MM");
        int mes = Integer.valueOf(simpleDateFormat.format(fecha)) - 1;
        String mesString = String.valueOf(mes);
        Short mesShort = Short.valueOf(mesString);
        simpleDateFormat = new SimpleDateFormat("YYYY");
        Short anio = Short.valueOf(simpleDateFormat.format(fecha));

        ControlCuentaPresupuestario control = suplementoIngresoService.getVerificarMesAnio(mesShort, anio);

        if (control != null) {

            return control.getEstado();

        } else {
            return false;
        }

    }

    public void seleccionarReservaAndEnviar(SolicitudReservaCompromiso s) {
        reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        if ("MOD_RC".equals(tramite.getTipoTramite().getAbreviatura())) {

            if (!verificarMes(s.getFechaAprobacion())) {
                JsfUtil.addWarningMessage("AVISO", "EL MES DEL PRESENTE AÑO SE ENCUENTRA CERRADO");
                return;
            }

        } else if ("ANU_RC".equals(tramite.getTipoTramite().getAbreviatura())) {
            if (s.getFechaAprobacion() == null) {
                if (!verificarMes(s.getFechaSolicitud()) || reservaCompromiso.getComprometido()) {
                    JsfUtil.addWarningMessage("AVISO", "NO PUEDE SELECCIONAR ESTA RESERVA PORQUE SE ENCUENTRA COMPROMETIDA O EL MES DEL PRESENTE AÑO SE ENCUENTRA CERRADO");
                    return;
                }
            } else {
                if (!verificarMes(s.getFechaAprobacion()) || reservaCompromiso.getComprometido()) {
                    JsfUtil.addWarningMessage("AVISO", "NO PUEDE SELECCIONAR ESTA RESERVA PORQUE SE ENCUENTRA COMPROMETIDA O EL MES DEL PRESENTE AÑO SE ENCUENTRA CERRADO");
                    return;
                }
            }

        }

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completTarea() {
        try {
            observacion.setObservacion(observaciones);
            reservaCompromiso.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            reservaCompromisoService.edit(reservaCompromiso);
            reservaCompromiso = new SolicitudReservaCompromiso();
            PrimeFaces.current().executeScript("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            getParamts().put("usuarioRevision", clienteService.getrolsUser(RolUsuario.financiero));

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envio correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    private void calcularTotales() {
        totalSolicitado = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : visualizacionSolicitud) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<CatalogoItem> getEstadoFiltros() {
        return estadoFiltros;
    }

    public void setEstadoFiltros(List<CatalogoItem> estadoFiltros) {
        this.estadoFiltros = estadoFiltros;
    }

    public List<UnidadAdministrativa> getUnidadFiltros() {
        return unidadFiltros;
    }

    public void setUnidadFiltros(List<UnidadAdministrativa> unidadFiltros) {
        this.unidadFiltros = unidadFiltros;
    }

    public Distributivo getCargoUnidadUser() {
        return cargoUnidadUser;
    }

    public void setCargoUnidadUser(Distributivo cargoUnidadUser) {
        this.cargoUnidadUser = cargoUnidadUser;
    }

    public BigDecimal getValorAnteirorPresupuesto() {
        return valorAnteirorPresupuesto;
    }

    public void setValorAnteirorPresupuesto(BigDecimal valorAnteirorPresupuesto) {
        this.valorAnteirorPresupuesto = valorAnteirorPresupuesto;
    }

    public List<Procedimiento> getProcedimientoList() {
        return procedimientoList;
    }

    public ReservaCompromisoDetalleLazy getLazy() {
        return lazy;
    }

    public void setLazy(ReservaCompromisoDetalleLazy lazy) {
        this.lazy = lazy;
    }

    public SoliciudReservaCompromisoLazy getLazyPrincipall() {
        return lazyPrincipall;
    }

    public void setLazyPrincipall(SoliciudReservaCompromisoLazy lazyPrincipall) {
        this.lazyPrincipall = lazyPrincipall;
    }

    public void setProcedimientoList(List<Procedimiento> procedimientoList) {
        this.procedimientoList = procedimientoList;
    }

    public Procedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(Procedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public LazyModel<SolicitudRequisito> getSolicitudRequisitoLazyModel() {
        return solicitudRequisitoLazyModel;
    }

    public void setSolicitudRequisitoLazyModel(LazyModel<SolicitudRequisito> solicitudRequisitoLazyModel) {
        this.solicitudRequisitoLazyModel = solicitudRequisitoLazyModel;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<UnidadAdministrativa> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadAdministrativa> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<Producto> getListadeProductoSeleccionados() {
        return listadeProductoSeleccionados;
    }

    public void setListadeProductoSeleccionados(List<Producto> listadeProductoSeleccionados) {
        this.listadeProductoSeleccionados = listadeProductoSeleccionados;
    }

    public List<DetalleSolicitudCompromiso> getListadetalleSolicitud() {
        return ListadetalleSolicitud;
    }

    public void setListadetalleSolicitud(List<DetalleSolicitudCompromiso> ListadetalleSolicitud) {
        this.ListadetalleSolicitud = ListadetalleSolicitud;
    }

    public DetalleSolicitudCompromiso getDetalleSolicitud() {
        return detalleSolicitud;
    }

    public void setDetalleSolicitud(DetalleSolicitudCompromiso detalleSolicitud) {
        this.detalleSolicitud = detalleSolicitud;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudCompromisos() {
        return listaGlobalDetalleSolicitudCompromisos;
    }

    public void setListaGlobalDetalleSolicitudCompromisos(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos) {
        this.listaGlobalDetalleSolicitudCompromisos = listaGlobalDetalleSolicitudCompromisos;
    }

    public boolean isMostrarOculutarTabla() {
        return mostrarOculutarTabla;
    }

    public void setMostrarOculutarTabla(boolean mostrarOculutarTabla) {
        this.mostrarOculutarTabla = mostrarOculutarTabla;
    }

    public String getCbTipo() {
        return cbTipo;
    }

    public void setCbTipo(String cbTipo) {
        this.cbTipo = cbTipo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<DetalleSolicitudCompromiso> getListaTotal() {
        return listaTotal;
    }

    public void setListaTotal(List<DetalleSolicitudCompromiso> listaTotal) {
        this.listaTotal = listaTotal;
    }

    public boolean isOcultarcb() {
        return ocultarcb;
    }

    public void setOcultarcb(boolean ocultarcb) {
        this.ocultarcb = ocultarcb;
    }

    public List<ProformaPresupuestoPlanificado> getListaPartidasDirectas() {
        return listaPartidasDirectas;
    }

    public void setListaPartidasDirectas(List<ProformaPresupuestoPlanificado> listaPartidasDirectas) {
        this.listaPartidasDirectas = listaPartidasDirectas;
    }

    public boolean isTabla1() {
        return tabla1;
    }

    public void setTabla1(boolean tabla1) {
        this.tabla1 = tabla1;
    }

    public boolean isTabla2() {
        return tabla2;
    }

    public void setTabla2(boolean tabla2) {
        this.tabla2 = tabla2;
    }

    public boolean isTabla3() {
        return tabla3;
    }

    public void setTabla3(boolean tabla3) {
        this.tabla3 = tabla3;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicitudPartidas() {
        return listaGlobalDetalleSolicitudPartidas;
    }

    public void setListaGlobalDetalleSolicitudPartidas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas) {
        this.listaGlobalDetalleSolicitudPartidas = listaGlobalDetalleSolicitudPartidas;
    }

    public List<DetalleSolicitudCompromiso> getListaGuardar() {
        return listaGuardar;
    }

    public void setListaGuardar(List<DetalleSolicitudCompromiso> listaGuardar) {
        this.listaGuardar = listaGuardar;
    }

    public List<Presupuesto> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<Presupuesto> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public List<SolicitudRequisito> getSolicitudRequisitoList() {
        return solicitudRequisitoList;
    }

    public void setSolicitudRequisitoList(List<SolicitudRequisito> solicitudRequisitoList) {
        this.solicitudRequisitoList = solicitudRequisitoList;
    }

    public SolicitudRequisito getSolicitudRequisito() {
        return solicitudRequisito;
    }

    public void setSolicitudRequisito(SolicitudRequisito solicitudRequisito) {
        this.solicitudRequisito = solicitudRequisito;
    }

    public ProcedimientoRequisito getProcedimientoRequisito() {
        return procedimientoRequisito;
    }

    public void setProcedimientoRequisito(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPanel1(boolean panel1) {
        this.panel1 = panel1;
    }

    public boolean isPanel1() {
        return panel1;
    }

    public boolean isPanel2() {
        return panel2;
    }

    public void setPanel2(boolean panel2) {
        this.panel2 = panel2;
    }

    public CatalogoItem getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(CatalogoItem catalogoItem) {
        this.catalogoItem = catalogoItem;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazyReal() {
        return lazyReal;
    }

    public void setLazyReal(LazyModel<SolicitudReservaCompromiso> lazyReal) {
        this.lazyReal = lazyReal;
    }

    public List<DetalleSolicitudCompromiso> getVisualizacionSolicitud() {
        return visualizacionSolicitud;
    }

    public void setVisualizacionSolicitud(List<DetalleSolicitudCompromiso> visualizacionSolicitud) {
        this.visualizacionSolicitud = visualizacionSolicitud;
    }

    public boolean isOcultarbutton() {
        return ocultarbutton;
    }

    public void setOcultarbutton(boolean ocultarbutton) {
        this.ocultarbutton = ocultarbutton;
    }

    public boolean isMostrarButton() {
        return mostrarButton;
    }

    public void setMostrarButton(boolean mostrarButton) {
        this.mostrarButton = mostrarButton;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable1() {
        return listaeditable1;
    }

    public void setListaeditable1(List<DetalleSolicitudCompromiso> listaeditable1) {
        this.listaeditable1 = listaeditable1;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable2() {
        return listaeditable2;
    }

    public void setListaeditable2(List<DetalleSolicitudCompromiso> listaeditable2) {
        this.listaeditable2 = listaeditable2;
    }

    public double getNum1() {
        return num1;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public double getResult1() {
        return result1;
    }

    public void setResult1(double result1) {
        this.result1 = result1;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    public double getResult2() {
        return result2;
    }

    public void setResult2(double result2) {
        this.result2 = result2;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public ProveedorLazy getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(ProveedorLazy proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public ServidorLazy getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(ServidorLazy servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public boolean isPanelProveedor() {
        return panelProveedor;
    }

    public void setPanelProveedor(boolean panelProveedor) {
        this.panelProveedor = panelProveedor;
    }

    public boolean isPanelServidor() {
        return panelServidor;
    }

    public void setPanelServidor(boolean panelServidor) {
        this.panelServidor = panelServidor;
    }
//</editor-fold>
}
