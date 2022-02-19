/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.certificacion_presupuesto_anual.lazy.ReservaCompromisoDetalleLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.SoliciudReservaCompromisoLazy;
import com.origami.sigef.certificacion_presupuesto_anual.model.DocumentosModel;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.impl.el.VariableScopeElResolver;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Suarez & John Intriago
 */
@Named(value = "reservaCompromisoView")
@ViewScoped
public class ReservaCompromisoController extends BpmnBaseRoot implements Serializable {
//<editor-fold defaultstate="collapsed" desc="VARIABLES">

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
    private List<DetalleSolicitudCompromiso> removerDetalle;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudCompromisos;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicitudPartidas;
    private List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicPartidasDirectas;
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
    private LazyModel<Proveedor> proveedorLazy;
    private LazyModel<Servidor> servidorLazy;
    private UnidadAdministrativa unidadAdministrativa;
    private BigDecimal valorAnteirorPresupuesto;

    @Inject
    private UserSession user;

    /*CRISS*/
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;

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
    private List<DetalleSolicitudCompromiso> listaeditable3;
    private double num1, num2;
    private double result1, result2;
    private boolean panelProveedor, panelServidor;
//</editor-fold>
    private List<UploadedFile> files;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession ss;

    private List<DocumentosModel> documentosListas;
    private DocumentosModel archivo;
    private List<CatalogoItem> estadoFiltro;
    private List<UnidadAdministrativa> unidadFiltros;

    private static final Logger LOG = Logger.getLogger(ReservaCompromisoController.class.getName());

    private boolean btnRegistrar, btnEditar;
    private String observaciones;
    private List<SolicitudReservaCompromiso> listaVerificadora;
    private boolean registraNuevoVerificar;

    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalDisponible;
    private UploadedFile file;
    private BigDecimal totalSolicitadoPD;
    private BigDecimal totalComprometidoPD;
    private BigDecimal totalDisponiblePD;
    private boolean verififcar;
    private boolean bolSistemaAntiguo = false;
    private DocumentosModel data;
    
// NUEVO
    @Inject
    private ThCargoRubrosService servicioThCargos;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaEgresosService;
    private List<ThCargoRubros> listaDistributivo;
    private List<DetalleSolicitudCompromiso> listaGlobalDetaSolicCompr;
    private ProformaPresupuestoPlanificado partidaDirecta;
    private BigDecimal totalDisponiblePDI = BigDecimal.ZERO;
    private BigDecimal totalComprometidoPDI = BigDecimal.ZERO;
    private BigDecimal totalSolicitadoPDI = BigDecimal.ZERO;

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
                removerDetalle = new ArrayList<>();
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
                documentosListas = new ArrayList<>();
                mostrarButton = false;
                catalogoItem = new CatalogoItem();
                lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
                lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
                lazyReal.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                unidadAdministrativa = new UnidadAdministrativa();
                unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
                procedimientoSeleccionado = new Procedimiento();
                solicitudRequisito = new SolicitudRequisito();
                procedimientoList = procedimientoService.getProcedimientos("SOL_RC");
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
                this.valorAnteirorPresupuesto = BigDecimal.ZERO;
                listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
                removerDetalle = new ArrayList<>();
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
                archivo = new DocumentosModel();
                estadoFiltro = new ArrayList<>();
                estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
                unidadFiltros = new ArrayList<>();
                unidadFiltros = reservaCompromisoService.getListaUnidadesReservas();
                btnEditar = false;
                btnRegistrar = false;
                data = new DocumentosModel();
                listaVerificadora = new ArrayList<>();
                listaVerificadora = reservaCompromisoService.listaSolicitud(BigInteger.valueOf(tramite.getNumTramite()));
                if (listaVerificadora.size() > 0) {
                    registraNuevoVerificar = true;
                } else {
                    registraNuevoVerificar = false;
                }

                List<Rol> rol = new ArrayList<>();
                rol = clienteService.getRolCategoriaUnidad(userSession.getNameUser());
                verififcar = true;
                bolSistemaAntiguo = false;
                for (Rol item : rol) {
                    switch (item.getCategoria().getCodigo()) {
                        case "2":
                        case "14":
                            verififcar = false;
                            bolSistemaAntiguo = true;
                            break;
                        case "3":
                        case "15":
                            verififcar = false;
                            break;
                        case "6":
                        case "21":
                            bolSistemaAntiguo = true;
                            break;
                        default:
                            break;
                    }
                }

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void accionComponentesDistriuvtioPartida(Boolean distributivo) {
        listaPresupuesto = new ArrayList<>();
        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {
            //if (false) {
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
        reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
        if (reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {
            if (reservaCompromiso.getPeriodo() == null && reservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Elija una fecha y una unidad");
            } else {
                if (distributivo) {
                    obtieneListaDistributivo(reservaCompromiso);
                    JsfUtil.executeJS("PF('Dlogo3').show()");
                    JsfUtil.update(":formDlogo3");
                } else {
                    obtenerPartidasDirectas();
                    JsfUtil.executeJS("PF('dlogoPartidasDirectas').show()");
                    JsfUtil.update("fromPartidasDirectas");
                }

            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + reservaCompromiso.getPeriodo());
        }

    }

    public void obtieneListaDistributivo(SolicitudReservaCompromiso p) {
        List<ThCargoRubros> tmp = servicioThCargos.distributivoCompleto(p.getPeriodo());
        listaDistributivo = new ArrayList<>();
        System.out.println("tmp " + tmp.size());
        if (tmp != null && !tmp.isEmpty()) {

            for (ThCargoRubros item : tmp) {
                listaDistributivo.add(item);
            }
        }
    }

    public void obtenerPartidasDirectas() {
        listaPartidasDirectas = new ArrayList<>();
        listaPartidasDirectas = proformaEgresosService.getProformaPresupuestoPartidaDirecta(reservaCompromiso.getPeriodo(), "PDI");
    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:dataSolciitudesReserva");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formMain:dataSolciitudesReserva");
        }
    }

    public void consultarMostrarAndOcultarPaneles() {
        panel1 = true;
        panel2 = false;

    }

    public void registarrMostrarAndOcultarPaneles() throws ParseException {
        clearAllFilters();
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        //reservaCompromiso = new SolicitudReservaCompromiso();
        panel1 = false;
        panel2 = true;
        ocultarbutton = false;
        mostrarButton = true;
        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
        this.removerDetalle = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        this.reservaCompromiso.setFechaSolicitud(new Date());
        this.reservaCompromiso.setPeriodo((short) anio);
    }

    public void inicializarMaxcimoCodigo() throws ParseException {
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);
    }

    public void planAnualesActividades() {
        String nameMax = clienteService.getrolsUser(RolUsuario.maximaAutoridad);
        System.out.println("NameMax: "+nameMax+ " | userSession.getNameUser() >> "+userSession.getNameUser());
        if (nameMax.equalsIgnoreCase(userSession.getNameUser())) {
            System.out.println("NameMax: "+nameMax);
            this.listaProductos = reservaCompromisoService.listadoProductoActividadesMaxAut(reservaCompromiso.getPeriodo());
        }else{
            System.out.println("NoNameMax user: "+userSession.getNameUser());
            this.listaProductos = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), reservaCompromiso.getUnidadRequiriente());
        }
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
            //cargoUnidadUser
            unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
            System.out.println("unidadAdministrativa " + unidadAdministrativa.toString());
            reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
            //Distributivo d = clienteService.getuusuarioLogeado(user.getNameUser());
            List<Producto> listaAnaidir = new ArrayList<>();
            
            String nameMax = clienteService.getrolsUser(RolUsuario.maximaAutoridad);
            System.out.println("NameMax: "+nameMax+ " | userSession.getNameUser() >> "+userSession.getNameUser());
            if (nameMax.equals(userSession.getNameUser())) {
                System.out.println("NameMax: "+nameMax);
                this.listaProductos = reservaCompromisoService.listadoProductoActividadesMaxAut(reservaCompromiso.getPeriodo());
            }else{
                System.out.println("NoNameMax user: "+userSession.getNameUser());
                if (unidadAdministrativa != null) {
                    listaAnaidir = reservaCompromisoService.listadoProductoActividades(reservaCompromiso.getPeriodo(), unidadAdministrativa);
                }
            }
            if (!listaAnaidir.isEmpty()) {
                listaProductos = listaAnaidir;
            }
            if (listaProductos.isEmpty()) {

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No existe datos");
                return;
            }
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

        reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);

        if (reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {

            if (reservaCompromiso.getPeriodo() == null && reservaCompromiso.getUnidadRequiriente() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "eliga una fecha y una unidad");

            } else {

                List<Presupuesto> listaPr = reservaCompromisoService.getPresupuestoPartidasDandPD(reservaCompromiso.getPeriodo(), Arrays.asList("PD", "PDI", "PDA"));
                listaPresupuesto.clear();
                listaPresupuesto = new ArrayList<>();
                if (!listaPr.isEmpty()) {
                    listaPresupuesto = listaPr;
                }

//                if (!listaGlobalDetalleSolicitudPartidas.isEmpty()) {
//                    for (DetalleSolicitudCompromiso dataPar : listaGlobalDetalleSolicitudPartidas) {
//                        for (Presupuesto dataPr : listaPr) {
//
//                            if (!this.listaGlobalDetalleSolicitudPartidas.isEmpty()) {
//
//                                if (dataPar.getPresupuesto().equals(dataPr)) {
//
//                                } else {
//                                    listaPresupuesto.add(dataPr);
//                                }
//                            } else {
//
//                                listaPresupuesto.add(dataPr);
//
//                            }
//                        }
//                        //listaPresupuesto.add(dataPr);
//                    }
//                } else {
//                    listaPresupuesto = listaPr;
//                }
                PrimeFaces.current().executeScript("PF('Dlogo3').show()");
                PrimeFaces.current().ajax().update(":formDlogo3");
            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "No hay aprobacion " + reservaCompromiso.getPeriodo());
        }

    }

    public String partidaNombre(String partida) {
        return reservaCompromisoService.conceptoPartidas(partida, reservaCompromiso.getPeriodo());

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

    public Boolean documentosObligaotrios() {
        boolean verificacion = false;
        int contado1 = 0;
        int contador2 = 0;

        if (!procedimientoRequisitoList.isEmpty()) {
            if (!documentosListas.isEmpty()) {

                List<ProcedimientoRequisito> lista1 = procedimientoRequisitoList.stream().filter(data1 -> data1.getObligatorio() == true).collect(Collectors.toList());
                List<DocumentosModel> lista2 = documentosListas.stream().filter(data -> data.getRequisito().getObligatorio() == true).collect(Collectors.toList());

                if (lista1.size() == lista2.size()) {
                    verificacion = false;
                } else {
                    verificacion = true;
                }
                for (DocumentosModel data : documentosListas) {
                    if (data.getRequisito().getObligatorio()) {
                        if (data.getUrl() == null || "".equals(data.getUrl())) {
                            verificacion = true;
                            break;
                        }
                    }
                }
            } else {
                for (ProcedimientoRequisito item : procedimientoRequisitoList) {
                    if (item.getObligatorio()) {
                        verificacion = true;
                        break;
                    }
                }
            }
        } else {
            verificacion = true;
        }
        return verificacion;
    }

    public void prueba() {
        boolean verificacion = false;
        int contado1 = 0;
        int contador2 = 0;

        if (!procedimientoRequisitoList.isEmpty()) {
            if (!documentosListas.isEmpty()) {

                List<ProcedimientoRequisito> lista1 = procedimientoRequisitoList.stream().filter(data1 -> data1.getObligatorio() == true).collect(Collectors.toList());
                List<DocumentosModel> lista2 = documentosListas.stream().filter(data -> data.getRequisito().getObligatorio() == true).collect(Collectors.toList());

                if (lista1.size() == lista2.size()) {
                    verificacion = false;
                } else {
                    verificacion = true;
                }
                for (DocumentosModel data : documentosListas) {
                    if (data.getRequisito().getObligatorio()) {
                        if (data.getUrl() == null || "".equals(data.getUrl())) {
                            verificacion = true;
                            break;
                        }
                    }
                }
            } else {
                for (ProcedimientoRequisito item : procedimientoRequisitoList) {
                    if (item.getObligatorio()) {
                        verificacion = true;
                        break;
                    }
                }
            }
        } else {
            verificacion = true;
        }
    }

    public void savesolicitud() {
        try {
            if (procedimientoSeleccionado != null) {
                if (documentosObligaotrios()) {
                    JsfUtil.addWarningMessage("Error: ", "suba los documentos que son obligatorios");
                    return;
                }
            } else {
                JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
                return;
            }

            boolean verificar = true;
            boolean pararMetodo = true;
            boolean pararMetodoPdi = true;
            listaGuardar = new ArrayList<>();
            if (listaGlobalDetalleSolicitudCompromisos != null) {
                for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
                    num1 = 0;
                    result1 = 0;
                    num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();
                    //result1 = num1 + itemsuma.getMontoSolicitado().doubleValue();
                    if (itemsuma.getMontoSolicitado() == null || itemsuma.getMontoSolicitado().compareTo(BigDecimal.ZERO) == 0) {
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
            }
            if (!verificar) {

                return;
            }
            if (listaGlobalDetalleSolicitudPartidas != null) {
                for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicitudPartidas) {

                    num2 = 0;
                    result2 = 0;
                    num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getRefDistributivo().getId(), true).doubleValue();
                    if (itemverificar.getMontoSolicitado() == null || itemverificar.getMontoSolicitado().compareTo(BigDecimal.ZERO) == 0) {
                        JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL DISTRIBUIDO ANTES DE GUARDAR");
                        return;
                    } else {
                        result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
                    }

                    if (result2 > itemverificar.getRefDistributivo().getReformaCodificado().doubleValue()) {
                        pararMetodo = false;
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                        break;
                    }

                }
            }
            if (!pararMetodo) {

                return;
            }

            if (listaGlobalDetalleSolicPartidasDirectas != null) {
                for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicPartidasDirectas) {
                    num2 = 0;
                    result2 = 0;
                    num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getPartidasDirecta().getId(), false).doubleValue();
                    if (itemverificar.getMontoSolicitado() == null || itemverificar.getMontoSolicitado().compareTo(BigDecimal.ZERO) == 0) {
                        JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL DISTRIBUIDO ANTES DE GUARDAR");
                        return;
                    } else {
                        result2 = num2 + itemverificar.getMontoSolicitado().doubleValue();
                    }
                    if (result2 > itemverificar.getPartidasDirecta().getReformaCodificado().doubleValue()) {
                        pararMetodoPdi = false;
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                        break;
                    }
                }

            }

            if (!pararMetodoPdi) {
                return;
            }

            if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty() || !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {

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

                if (!listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {
                    listaGlobalDetalleSolicPartidasDirectas.forEach((item) -> {
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
            reservaCompromiso.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
            reservaCompromiso.setSecuencial(maximo);
            reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);

            for (DetalleSolicitudCompromiso li : listaGuardar) {
                detalleSolicitud = new DetalleSolicitudCompromiso();

                detalleSolicitud.setSolicitud(reservaCompromiso);

                detalleSolicitud.setActividadProducto(li.getActividadProducto());
                detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
                detalleSolicitud.setEstado(true);
                detalleSolicitud.setRefDistributivo(li.getRefDistributivo());
                detalleSolicitud.setFechaCreacion(new Date());
                detalleSolicitud.setFechaModifcacion(new Date());
                detalleSolicitud.setUsuarioCreacion(userSession.getName());
                detalleSolicitud.setUsuarioModificacion(userSession.getName());
                detalleSolicitud.setPeriodo(li.getPeriodo());
                detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
                detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
                detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
                detalleSolicitud = detalleReservaService.create(detalleSolicitud);

            }

            //Guardamos la los requisitos en relacion a la reserva de compromiso que se creo en la linea de codigo anterior
            if (reservaCompromiso != null) {
                if (files != null) {
                    uploadDoc.upload(reservaCompromiso, files);
                }
                JsfUtil.addInformationMessage("Reserva", "Datos almacenados correctamente");
            } else {
                JsfUtil.addErrorMessage("Usuario", "Ocurrio un error al guardar los datos del usuario");
            }

            if (verificacion) {

                PrimeFaces.current().ajax().update("messages");

                JsfUtil.addSuccessMessage("SOLICITUD", "La solicitud " + reservaCompromiso.getSecuencial() + "-" + reservaCompromiso.getPeriodo() + " se ha realizado con éxito");
                listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
                removerDetalle = new ArrayList<>();
                listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
                listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
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

            listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
            removerDetalle = new ArrayList<>();
            listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
            listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
            listaGuardar = new ArrayList<>();
            solicitudRequisitoList = new ArrayList<>();
            procedimientoRequisitoList = new ArrayList<>();
            reservaCompromiso = new SolicitudReservaCompromiso();
            detalleSolicitud = new DetalleSolicitudCompromiso();
            procedimientoSeleccionado = new Procedimiento();
            servidorLazy = null;
            proveedorLazy = null;
            setCbTipo("");
            listaVerificadora = new ArrayList<>();
            listaVerificadora = reservaCompromisoService.listaSolicitud(BigInteger.valueOf(tramite.getNumTramite()));

            if (listaVerificadora.size() > 0) {
                registraNuevoVerificar = true;
            } else {
                registraNuevoVerificar = false;
            }

        } catch (Exception e) {
            System.out.println("Ocurrió un error" + e);
        }
    }

    public void añadiendoFormato(Producto p) {
        BigDecimal maximo_resultado = BigDecimal.ZERO;

        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudCompromisos) {
            if (data.getActividadProducto().equals(p)) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
            }
        }

        detalleSolicitud = new DetalleSolicitudCompromiso();
        producto = new Producto();
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

        calcularTotales();
        JsfUtil.addInformationMessage("INFORMATION", "PRODUCTO SELECIONADO CORRECTAMENTE");
        producto = new Producto();
        detalleSolicitud = new DetalleSolicitudCompromiso();
    }

    public void eliminadoMemoriaDetalleSOlicitud(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudCompromisos.get(index));
        listaGlobalDetalleSolicitudCompromisos.remove(index);
        calcularTotales();
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
        PrimeFaces.current().executeScript("PF('DlEdicionPartida').show()");
        PrimeFaces.current().ajax().update(":formEdicionPartida");
    }

    public void solicitarReservas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {
            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudCompromisos.add(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudCompromisos.remove(this.listaGlobalDetalleSolicitudCompromisos.indexOf(detalleSolicitud));
            calcularTotales();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void solicitarReservasPartidas(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();

        if (detalleSolicitud.getMontoComprometido() != null) {

            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudPartidas.add(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudPartidas.remove(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud));
            calcularTotalesPD();
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
    }

    public void eliminarPArtidasDirectasAndDistributivo(int index) {
        removerDetalle.add(listaGlobalDetalleSolicitudPartidas.get(index));
        listaGlobalDetalleSolicitudPartidas.remove(index);
        calcularTotalesPD();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void solicitarReservasPD(DetalleSolicitudCompromiso d) {
        detalleSolicitud = new DetalleSolicitudCompromiso();
        detalleSolicitud = d;
        BigDecimal valorAnterior = d.getMontoSolicitado();
        if (detalleSolicitud.getMontoComprometido() != null) {
            if (detalleSolicitud.getMontoSolicitado().doubleValue() < detalleSolicitud.getMontoComprometido().doubleValue()) {
                detalleSolicitud.setMontoSolicitado(detalleSolicitud.getMontoComprometido());
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "El monto no debe ser menor al monto comprometido");
            }
        }
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicPartidasDirectas.add(this.listaGlobalDetalleSolicPartidasDirectas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicPartidasDirectas.remove(this.listaGlobalDetalleSolicPartidasDirectas.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
        calcularTotalesPD();
    }

    public void seleccionPresupuestoDPD(Presupuesto p) {

        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudPartidas) {
            if (data.getPresupuesto().equals(p)) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
            }
        }

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
        detalleSolicitud = new DetalleSolicitudCompromiso();
        calcularTotalesPD();
        JsfUtil.addInformationMessage("INFORMACIÒN", "PARTIDA SELECIONADO CORRECTAMENTE");

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
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;

        for (DetalleSolicitudCompromiso detalle : visualizacionSolicitud) {
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
        }
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
        removerDetalle = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        listaeditable3 = new ArrayList<>();
        listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;

//        if (!"APRO".equals(reservaCompromiso.getEstado().getCodigo())) {
//            PrimeFaces.current().ajax().update("messages");
//            JsfUtil.addWarningMessage("Advertencia", "La solicitud debe estar aprobada para poder actualizarla");
//
//            return;
//        }
//
        this.procedimientoSeleccionado = s.getProcedimiento();

        listaeditable1 = reservaCompromisoService.getEdicionDetalleProducto(s);
        listaeditable2 = reservaCompromisoService.getEdicionDetallePDAndD(s);
        listaeditable3 = reservaCompromisoService.getEdicionDetallePD(s);
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

        listaeditable3.forEach((detalle2) -> {
            this.listaGlobalDetalleSolicPartidasDirectas.add(detalle2);
        });

        panel1 = false;
        panel2 = true;

        listaGlobalDetalleSolicitudCompromisos.forEach((lista1) -> {
            lista1.setMontoDisponible(lista1.getActividadProducto().getMontoReformada().subtract(reservaCompromisoService.getSumaEdicionProductos(lista1.getActividadProducto())).add(lista1.getMontoSolicitado()));
        });
        listaGlobalDetalleSolicitudPartidas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getRefDistributivo().getReformaCodificado().subtract(reservaCompromisoService.getSumaEdicionPresupuesto(lis.getRefDistributivo().getId(), true)).add(lis.getMontoSolicitado()));
        });

        listaGlobalDetalleSolicPartidasDirectas.forEach((lis) -> {
            lis.setMontoDisponible(lis.getPartidasDirecta().getReformaCodificado().subtract(reservaCompromisoService.getSumaEdicionPresupuesto(lis.getPartidasDirecta().getId(), false)).add(lis.getMontoSolicitado()));
        });

        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGÚN DOCUMENTO ADJUNTO A LOS REQUISITOS");
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
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGÚN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void eliminarPDF(ProcedimientoRequisito procedimientoRequisito) {
        DocumentosModel item = new DocumentosModel();
        boolean verificar = true;

        if (Utils.isNotEmpty(documentosListas)) {
            for (DocumentosModel data : documentosListas) {
                if (data.getRequisito().equals(procedimientoRequisito)) {
                    if (data.getUrl() == null || "".equals(data.getUrl())) {

                        JsfUtil.addErrorMessage("Error", "No se puede eliminar porque no hay ningún archivo");
                        return;
                    } else {
                        item = new DocumentosModel();
                        item = data;
                        verificar = false;
                        break;
                    }

                }
            }

            if (!verificar) {
                documentosListas.remove(item);
                files.remove(item.getArchivo());
                JsfUtil.addInformationMessage("Información", "Eliminado Correctamente");
            }
        } else {
            JsfUtil.addWarningMessage("Advertencia", "No hay archivos");

        }

        //        Boolean auxiliar = true;
        //        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
        //            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        //        } else {
        //            for (SolicitudRequisito pr : solicitudRequisitoList) {
        //                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
        //                    solicitudRequisitoList.remove(pr);
        //                    break;
        //                } else {
        //                    auxiliar = false;
        //                }
        //            }
        //            if (auxiliar) {
        //                JsfUtil.addInformationMessage("INFORMACIÓN", "DOCUMENTO ADJUNTO ELIMINADO CORRECTAMENTE");
        //            } else {
        //                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
        //            }
        //        }
    }

    public void cargarRequisitos() {
        this.procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);

        PrimeFaces.current().ajax().update("requisitosTabla");
    }

    public void requisitoSeleccionado(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            for (DocumentosModel item : documentosListas) {
                if (procedimientoRequisito == item.getRequisito()) {
                    if (item.getUrl().length() > 0) {
                        JsfUtil.addErrorMessage("Error", "Ya subió este requisito, si quiere subir uno nuevo primero borrelo y después suba de nuevo");
                        return;
                    }
                }
            }
            file = null;
            file = event.getFile();
            archivo = new DocumentosModel();
            archivo.setRequisito(procedimientoRequisito);
            archivo.setUrl(file.getFileName());
            archivo.setArchivo(file);
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(event.getFile());
            this.documentosListas.add(archivo);
            this.procedimientoRequisito = new ProcedimientoRequisito();
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("requisitoDialogForm");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "Su archivo se subió exitosamente");
        } catch (Exception e) {
            System.out.println("Error al subir el archivo " + e);
        }
    }

    public void viewFile(int index, ProcedimientoRequisito req) {
        try {
            data = new DocumentosModel();
            if(Utils.isNotEmpty(documentosListas)){
                for (DocumentosModel documentosLista : documentosListas) {
                    if(documentosLista.getRequisito().equals(req)){
                        data = documentosLista;
                    }
                }
//                data = documentosListas.get(index);
                if (data.getArchivo() != null) {
                    ss.setContentType(data.getArchivo().getContentType());
                    ss.setNombreDocumento(data.getArchivo().getFileName());
                    ss.setTempData(data.getArchivo().getInputstream());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarReservaCompromiso() {
        listaGuardar.clear();
        listaGuardar = new ArrayList<>();
        List<DetalleSolicitudCompromiso> listaValoresAnteriores;
        if (procedimientoSeleccionado == null) {
            JsfUtil.addWarningMessage("PROCEDIMIENTO: ", "NO SE HA SELECCIONADO NINGÚN PROCEDIMIENTO");
            return;
        }
        boolean verificar = true;
        boolean pararMetodo = true;
        boolean pararMetodoPdi = true;
        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty() || !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {

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
            if (listaGlobalDetalleSolicPartidasDirectas != null && !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {
                listaGlobalDetalleSolicPartidasDirectas.forEach((item) -> {
                    listaGuardar.add(item);
                });

            }
        }
        if (listaGuardar.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No hay datos");
            return;
        }
        listaValoresAnteriores = listaGuardar;
        for (DetalleSolicitudCompromiso itemsuma : listaGlobalDetalleSolicitudCompromisos) {
            num1 = 0;
            result1 = 0;
            num1 = reservaCompromisoService.getSumaEdicionProductos(itemsuma.getActividadProducto()).doubleValue();
            BigDecimal valorActual = reservaCompromisoService.getValorActual(itemsuma);
            if (itemsuma.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result1 = num1 + itemsuma.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }

            if (result1 > itemsuma.getActividadProducto().getMontoReformada().doubleValue()) {
                verificar = false;
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo para su reserva solicitada");
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
            BigDecimal valorActual = reservaCompromisoService.getValorActual(itemverificar);
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }
            if (result2 > itemverificar.getRefDistributivo().getReformaCodificado().doubleValue()) {
                pararMetodo = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }
        }
        if (!pararMetodo) {
            return;
        }

        for (DetalleSolicitudCompromiso itemverificar : listaGlobalDetalleSolicPartidasDirectas) {
            num2 = 0;
            result2 = 0;
            num2 = reservaCompromisoService.getSumaEdicionPresupuesto(itemverificar.getPartidasDirecta().getId(), false).doubleValue();
            BigDecimal valorActual = reservaCompromisoService.getValorActual(itemverificar);
            if (itemverificar.getMontoSolicitado() == null) {
                JsfUtil.addWarningMessage("INCONSISTENCIA", "ASIGNAR MONTO SOLICITADO EN EL PAPP ANTES DE GUARDAR");
                return;
            } else {
                result2 = num2 + itemverificar.getMontoSolicitado().doubleValue() - valorActual.doubleValue();
            }

            if (result2 > itemverificar.getPartidasDirecta().getReformaCodificado().doubleValue()) {
                pararMetodoPdi = false;

                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "Esta partida" + itemverificar.getPresupuesto().getPartida() + " no tiene mucho fondo para su reserva");
                break;
            }
        }

        if (!pararMetodoPdi) {
            return;
        }

        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromisoService.edit(reservaCompromiso);
        if (files != null) {
            uploadDoc.upload(reservaCompromiso, files);
        }
        for (DetalleSolicitudCompromiso li : listaGuardar) {
            if (li.getId() != null) {
                detalleSolicitud = new DetalleSolicitudCompromiso();
                detalleSolicitud.setId(li.getId());
                detalleSolicitud.setSolicitud(reservaCompromiso);
                detalleSolicitud.setActividadProducto(li.getActividadProducto());
                detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
                detalleSolicitud.setEstado(true);
                detalleSolicitud.setRefDistributivo(li.getRefDistributivo());
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
            } else {
                detalleSolicitud = new DetalleSolicitudCompromiso();
                detalleSolicitud = Utils.clone(li);
                detalleSolicitud.setSolicitud(reservaCompromiso);
                detalleSolicitud.setActividadProducto(li.getActividadProducto());
                detalleSolicitud.setPartidasDirecta(li.getPartidasDirecta());
                detalleSolicitud.setEstado(true);
                detalleSolicitud.setRefDistributivo(li.getRefDistributivo());
                detalleSolicitud.setFechaCreacion(new Date());
                detalleSolicitud.setFechaModifcacion(new Date());
                detalleSolicitud.setUsuarioCreacion(userSession.getName());
                detalleSolicitud.setUsuarioModificacion(userSession.getName());
                detalleSolicitud.setPeriodo(li.getPeriodo());
                detalleSolicitud.setMontoDisponible(li.getMontoDisponible());
                detalleSolicitud.setMontoSolicitado(li.getMontoSolicitado());
                detalleSolicitud.setMontoComprometido(BigDecimal.ZERO);
                detalleSolicitud = detalleReservaService.create(detalleSolicitud);
            }
        }
        if (!removerDetalle.isEmpty()) {
            for (DetalleSolicitudCompromiso detalle : removerDetalle) {
                if (detalle.getId() != null) {
                    detalleReservaService.getRemove(detalle);
                }
            }
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
        JsfUtil.addInformationMessage("Información", "Editado correctamente");
        resetSolicitud();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        consultarMostrarAndOcultarPaneles();
        listaVerificadora = new ArrayList<>();
        listaVerificadora = reservaCompromisoService.listaSolicitud(BigInteger.valueOf(tramite.getNumTramite()));
        if (listaVerificadora.size() > 0) {
            registraNuevoVerificar = true;
        } else {
            registraNuevoVerificar = false;
        }
    }

    public void enviarSolicitud(SolicitudReservaCompromiso s) {
        this.reservaCompromiso = s;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {

        try {
            observacion.setObservacion(observaciones);
            reservaCompromiso.setEstado(reservaCompromisoService.getestados("E", "estado_solicitud"));
            reservaCompromisoService.edit(reservaCompromiso);
            reservaCompromiso = new SolicitudReservaCompromiso();
            PrimeFaces.current().executeScript("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            System.out.println("tarea.getTaskDefinitionKey() >> "+tarea.getTaskDefinitionKey());
            if (tarea.getTaskDefinitionKey().equals("usertask2")) {
//                System.out.println("tarea.getTaskDefinitionKey().equals(\"usertask2\")");
//                System.out.println("Rol Director Finan"+clienteService.getrolsUser(RolUsuario.directorFinanciero));
                getParamts().put("usertask3", clienteService.getrolsUser(RolUsuario.directorFinanciero));
            } else {
//                System.out.println("Rol Director Finan"+clienteService.getrolsUser(RolUsuario.asistenteDireccion));
                getParamts().put("usuario2", clienteService.getrolsUser(RolUsuario.asistenteDireccion));
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Información", "La solicitud se envió correctamente");
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void resetSolicitud() {
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
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
                proveedorLazy
                        = new LazyModel(Proveedor.class
                        );
                proveedorLazy.getFilterss().put("estado", Boolean.TRUE);
                proveedorLazy.setDistinct(false);
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                servidorLazy = new LazyModel(Servidor.class
                );
                servidorLazy.setDistinct(false);
                // servidorLazy.getFilterss().put("estado", Boolean.TRUE);
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

    private void calcularTotales() {
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudCompromisos) {
            totalDisponible = totalDisponible.add(detalle.getMontoDisponible());
            totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
            totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
        }
    }

    private void calcularTotalesPD() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
            totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
            totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
            totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
        }
        totalDisponiblePDI = BigDecimal.ZERO;
        totalComprometidoPDI = BigDecimal.ZERO;
        totalSolicitadoPDI = BigDecimal.ZERO;
        if (listaGlobalDetalleSolicPartidasDirectas != null) {
            for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicPartidasDirectas) {
                totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible());
                totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
                totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
            }
        }
    }

    public void seleccionPresupuesto(ThCargoRubros p) {
        //if (!listaGlobalDetaSolicCompr.isEmpty()) {
        if (listaGlobalDetalleSolicitudPartidas != null) {
            for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudPartidas) {
                if (p.equals(data.getRefDistributivo())) {
                    JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                    return;
                }
            }
        }
        DetalleSolicitudCompromiso detSoliComp = new DetalleSolicitudCompromiso();
        detSoliComp.setSolicitud(reservaCompromiso);
        detSoliComp.setRefDistributivo(p);
        detSoliComp.setEstado(true);
        detSoliComp.setFechaCreacion(new Date());
        detSoliComp.setFechaModifcacion(new Date());
        detSoliComp.setUsuarioCreacion(userSession.getName());
        detSoliComp.setUsuarioModificacion(userSession.getName());
        detSoliComp.setPeriodo(reservaCompromiso.getPeriodo());
        BigDecimal maximo_resultado = BigDecimal.ZERO;
        BigDecimal resultado_valor_disponble = (BigDecimal) reservaCompromisoService.getSueldoDisponibleCargosRubros(p, reservaCompromiso.getPeriodo());
        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detSoliComp.setMontoDisponible(p.getReformaCodificado());
        } else {
            maximo_resultado = detSoliComp.getRefDistributivo().getReformaCodificado().subtract(resultado_valor_disponble);
            detSoliComp.setMontoDisponible(maximo_resultado);
        }

        listaGlobalDetalleSolicitudPartidas.add(detSoliComp);
        sumaTotales();
        //calcularTotales();
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        JsfUtil.addInformationMessage("", "EL REGISTRO QUE SELECCION FUE EXITOSO");
    }

    public void seleccionPartidaDirecta(ProformaPresupuestoPlanificado pr) {
        //if (!listaGlobalDetaSolicCompr.isEmpty()) {

        if (listaGlobalDetalleSolicPartidasDirectas != null) {
            for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicPartidasDirectas) {
                if (pr.equals(data.getPartidasDirecta())) {
                    JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                    return;
                }
            }
        }
        //}
        DetalleSolicitudCompromiso detSoliComp = new DetalleSolicitudCompromiso();
        detSoliComp.setSolicitud(reservaCompromiso);
        detSoliComp.setPartidasDirecta(pr);
        detSoliComp.setEstado(true);
        detSoliComp.setFechaCreacion(new Date());
        detSoliComp.setFechaModifcacion(new Date());
        detSoliComp.setUsuarioCreacion(userSession.getName());
        detSoliComp.setUsuarioModificacion(userSession.getName());
        detSoliComp.setPeriodo(reservaCompromiso.getPeriodo());
        BigDecimal maximo_resultado = BigDecimal.ZERO;
        BigDecimal resultado_valor_disponble = (BigDecimal) reservaCompromisoService.getSueldoDisponiblePD(pr, reservaCompromiso.getPeriodo());
        if (resultado_valor_disponble == null || resultado_valor_disponble.toString().length() == 0) {
            detSoliComp.setMontoDisponible(pr.getReformaCodificado());
        } else {
            maximo_resultado = detSoliComp.getPartidasDirecta().getReformaCodificado().subtract(resultado_valor_disponble);
            detSoliComp.setMontoDisponible(maximo_resultado);
        }

        listaGlobalDetalleSolicPartidasDirectas.add(detSoliComp);
        sumaTotales();
        JsfUtil.addInformationMessage("", "EL REGISTRO QUE SELECCION FUE EXITOSO");
        //calcularTotales();

    }

    public void sumaTotales() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        totalDisponiblePDI = BigDecimal.ZERO;
        totalComprometidoPDI = BigDecimal.ZERO;
        totalSolicitadoPDI = BigDecimal.ZERO;
        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
            totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
            totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
            totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
        }

        for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicPartidasDirectas) {
            totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible());
            totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
            totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER AN SETTER">
    public BigDecimal getTotalSolicitado() {
        return totalSolicitado;
    }

    public void setTotalSolicitado(BigDecimal totalSolicitado) {
        this.totalSolicitado = totalSolicitado;
    }

    public List<DetalleSolicitudCompromiso> getListaeditable3() {
        return listaeditable3;
    }

    public void setListaeditable3(List<DetalleSolicitudCompromiso> listaeditable3) {
        this.listaeditable3 = listaeditable3;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicPartidasDirectas() {
        return listaGlobalDetalleSolicPartidasDirectas;
    }

    public void setListaGlobalDetalleSolicPartidasDirectas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicPartidasDirectas) {
        this.listaGlobalDetalleSolicPartidasDirectas = listaGlobalDetalleSolicPartidasDirectas;
    }

    public boolean isVerififcar() {
        return verififcar;
    }

    public void setVerififcar(boolean verififcar) {
        this.verififcar = verififcar;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
    }

    public BigDecimal getTotalSolicitadoPD() {
        return totalSolicitadoPD;
    }

    public void setTotalSolicitadoPD(BigDecimal totalSolicitadoPD) {
        this.totalSolicitadoPD = totalSolicitadoPD;
    }

    public BigDecimal getTotalComprometidoPD() {
        return totalComprometidoPD;
    }

    public void setTotalComprometidoPD(BigDecimal totalComprometidoPD) {
        this.totalComprometidoPD = totalComprometidoPD;
    }

    public BigDecimal getTotalDisponiblePD() {
        return totalDisponiblePD;
    }

    public void setTotalDisponiblePD(BigDecimal totalDisponiblePD) {
        this.totalDisponiblePD = totalDisponiblePD;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
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

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
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

    public List<DocumentosModel> getDocuemtosListas() {
        return documentosListas;
    }

    public void setDocuemtosListas(List<DocumentosModel> docuemtosListas) {
        this.documentosListas = docuemtosListas;
    }

    public List<CatalogoItem> getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(List<CatalogoItem> estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public List<UnidadAdministrativa> getUnidadFiltros() {
        return unidadFiltros;
    }

    public void setUnidadFiltros(List<UnidadAdministrativa> unidadFiltros) {
        this.unidadFiltros = unidadFiltros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public boolean isBtnEditar() {
        return btnEditar;
    }

    public void setBtnEditar(boolean btnEditar) {
        this.btnEditar = btnEditar;
    }

    public boolean isRegistraNuevoVerificar() {
        return registraNuevoVerificar;
    }

    public void setRegistraNuevoVerificar(boolean registraNuevoVerificar) {
        this.registraNuevoVerificar = registraNuevoVerificar;
    }

    public DocumentosModel getData() {
        return data;
    }

    public void setData(DocumentosModel data) {
        this.data = data;
    }

    public ReservaCompromisoService getReservaCompromisoService() {
        return reservaCompromisoService;
    }

    public void setReservaCompromisoService(ReservaCompromisoService reservaCompromisoService) {
        this.reservaCompromisoService = reservaCompromisoService;
    }

    public UnidadAdministrativaService getUnidadService() {
        return unidadService;
    }

    public void setUnidadService(UnidadAdministrativaService unidadService) {
        this.unidadService = unidadService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public ProductoService getProductoService() {
        return productoService;
    }

    public void setProductoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public DetalleReservaCompromisoService getDetalleReservaService() {
        return detalleReservaService;
    }

    public void setDetalleReservaService(DetalleReservaCompromisoService detalleReservaService) {
        this.detalleReservaService = detalleReservaService;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<DetalleSolicitudCompromiso> getRemoverDetalle() {
        return removerDetalle;
    }

    public void setRemoverDetalle(List<DetalleSolicitudCompromiso> removerDetalle) {
        this.removerDetalle = removerDetalle;
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

    public ProcedimientoService getProcedimientoService() {
        return procedimientoService;
    }

    public void setProcedimientoService(ProcedimientoService procedimientoService) {
        this.procedimientoService = procedimientoService;
    }

    public ProcedimientoRequisitoService getProcedimientoRequisitoService() {
        return procedimientoRequisitoService;
    }

    public void setProcedimientoRequisitoService(ProcedimientoRequisitoService procedimientoRequisitoService) {
        this.procedimientoRequisitoService = procedimientoRequisitoService;
    }

    public SolicitudRequisitoService getSolicitudRequisitoService() {
        return solicitudRequisitoService;
    }

    public void setSolicitudRequisitoService(SolicitudRequisitoService solicitudRequisitoService) {
        this.solicitudRequisitoService = solicitudRequisitoService;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public FileUploadDoc getUploadDoc() {
        return uploadDoc;
    }

    public void setUploadDoc(FileUploadDoc uploadDoc) {
        this.uploadDoc = uploadDoc;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<DocumentosModel> getDocumentosListas() {
        return documentosListas;
    }

    public void setDocumentosListas(List<DocumentosModel> documentosListas) {
        this.documentosListas = documentosListas;
    }

    public DocumentosModel getArchivo() {
        return archivo;
    }

    public void setArchivo(DocumentosModel archivo) {
        this.archivo = archivo;
    }

    public List<SolicitudReservaCompromiso> getListaVerificadora() {
        return listaVerificadora;
    }

    public void setListaVerificadora(List<SolicitudReservaCompromiso> listaVerificadora) {
        this.listaVerificadora = listaVerificadora;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ThCargoRubrosService getServicioThCargos() {
        return servicioThCargos;
    }

    public void setServicioThCargos(ThCargoRubrosService servicioThCargos) {
        this.servicioThCargos = servicioThCargos;
    }

    public ProformaPresupuestoPlanificadoService getProformaEgresosService() {
        return proformaEgresosService;
    }

    public void setProformaEgresosService(ProformaPresupuestoPlanificadoService proformaEgresosService) {
        this.proformaEgresosService = proformaEgresosService;
    }

    public List<ThCargoRubros> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<ThCargoRubros> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetaSolicCompr() {
        return listaGlobalDetaSolicCompr;
    }

    public void setListaGlobalDetaSolicCompr(List<DetalleSolicitudCompromiso> listaGlobalDetaSolicCompr) {
        this.listaGlobalDetaSolicCompr = listaGlobalDetaSolicCompr;
    }

    public ProformaPresupuestoPlanificado getPartidaDirecta() {
        return partidaDirecta;
    }

    public void setPartidaDirecta(ProformaPresupuestoPlanificado partidaDirecta) {
        this.partidaDirecta = partidaDirecta;
    }

    public BigDecimal getTotalDisponiblePDI() {
        return totalDisponiblePDI;
    }

    public void setTotalDisponiblePDI(BigDecimal totalDisponiblePDI) {
        this.totalDisponiblePDI = totalDisponiblePDI;
    }

    public BigDecimal getTotalComprometidoPDI() {
        return totalComprometidoPDI;
    }

    public void setTotalComprometidoPDI(BigDecimal totalComprometidoPDI) {
        this.totalComprometidoPDI = totalComprometidoPDI;
    }

    public BigDecimal getTotalSolicitadoPDI() {
        return totalSolicitadoPDI;
    }

    public void setTotalSolicitadoPDI(BigDecimal totalSolicitadoPDI) {
        this.totalSolicitadoPDI = totalSolicitadoPDI;
    }

    public boolean isBolSistemaAntiguo() {
        return bolSistemaAntiguo;
    }

    public void setBolSistemaAntiguo(boolean bolSistemaAntiguo) {
        this.bolSistemaAntiguo = bolSistemaAntiguo;
    }
//</editor-fold>

}
