/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.ReservaCompromisoDetalleLazy;
import com.origami.sigef.certificacion_presupuesto_anual.lazy.SoliciudReservaCompromisoLazy;
import com.origami.sigef.certificacion_presupuesto_anual.model.DocumentosModel;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.MasterCatalogo;
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
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
 * @author Luis Suarez & John Intriago & Sandra Arroba(Copia y Modificaciones -
 * Sin Flujo de Proceso)
 */
@Named(value = "reservaCompromisoSinProcesoView")
@ViewScoped
public class ReservaCompromisoSinProcesoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="VARIBLES">
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
    @Inject
    private BeneficiarioSolicitudReservaService reservaBeneficiarioService;
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
    private FileUploadDoc uploadDoc;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    private ManagerService service;

    private SolicitudReservaCompromiso reservaCompromiso;
    private List<Producto> listaProductos;
    private List<UnidadAdministrativa> listaUnidades;
    private List<Producto> listadeProductoSeleccionados;
    private List<DetalleSolicitudCompromiso> ListadetalleSolicitud;
    private DetalleSolicitudCompromiso detalleSolicitud;
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
    private ProveedorLazy proveedorLazy;
    private ServidorLazy servidorLazy;
    private UnidadAdministrativa unidadAdministrativa;
    private BigDecimal valorAnteirorPresupuesto;
    private LazyModel<Proveedor> lazyProveedor;
    private LazyModel<Servidor> lazyServidor;
    private List<Procedimiento> procedimientoList;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private List<UploadedFile> files;
    private Procedimiento procedimientoSeleccionado;
    private UploadedFile uploadFile;
    private SolicitudRequisito solicitudRequisito;
    private ProcedimientoRequisito procedimientoRequisito;
    private String fileName;
    private List<DocumentosModel> documentosListas;
    private DocumentosModel archivo;
    private List<CatalogoItem> estadoFiltro;
    private List<UnidadAdministrativa> unidadFiltros;
    private LazyModel<SolicitudReservaCompromiso> lazyReal;
    private List<DetalleSolicitudCompromiso> listaeditable1;
    private List<DetalleSolicitudCompromiso> listaeditable2;
    private List<DetalleSolicitudCompromiso> listaeditable3;
    private double num1, num2;
    private double result1, result2;
    private boolean panelProveedor, panelServidor;
    private boolean btnRegistrar, btnEditar;
    private String observaciones;
    private List<SolicitudReservaCompromiso> listaVerificadora;
    private boolean registraNuevoVerificar;
    /*FIN CRISS*/
    private static final Logger LOG = Logger.getLogger(ReservaCompromisoSinProcesoController.class.getName());
    private boolean consultaBtnRendered, verififcar;
    private boolean soloPresupuesto = Boolean.FALSE;
    private boolean renderedRequisitos = Boolean.FALSE;
    private BigDecimal totalSolicitado;
    private BigDecimal totalComprometido;
    private BigDecimal totalDisponible;
    private BigDecimal totalSolicitadoPD;
    private BigDecimal totalComprometidoPD;
    private BigDecimal totalDisponiblePD;
    private List<Cliente> listaBeneficiarios;
    private int conteo;
    private List<BeneficiarioSolicitudReserva> listaBeneficariosReservas;
    private List<Cliente> listaBeneficariosReservasvista;
    private List<DetalleSolicitudCompromiso> listDetalleReservaEliminar;
    private OpcionBusqueda opcionBusqueda;
    private List<Short> listaPeriodo;
    private Integer anioActual;

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
    private List<UnidadAdministrativa> unidades;

//</editor-fold>
    @PostConstruct
    public void inicializar() {
        opcionBusqueda = new OpcionBusqueda();
        fileName = "";
        listDetalleReservaEliminar = new ArrayList<>();
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
        partidaDirecta = new ProformaPresupuestoPlanificado();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
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
        // this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        anioActual = Utils.getAnio(new Date());
        listaPeriodo = reservaCompromisoService.listaAniosAprobados(Boolean.TRUE);

        if (listaPeriodo != null && !listaPeriodo.isEmpty()) {
            opcionBusqueda.setAnio(listaPeriodo.get(0).shortValue());
        }

        actualizarListadoReservas();
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
        listaeditable3 = new ArrayList<>();
        num1 = 0;
        num2 = 0;
        result1 = 0;
        result2 = 0;
        panelProveedor = true;
        panelServidor = false;
        this.valorAnteirorPresupuesto = BigDecimal.ZERO;
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        procedimientoSeleccionado = new Procedimiento();
        lazyServidor = null;
        lazyProveedor = null;
        setCbTipo("");
        archivo = new DocumentosModel();
        estadoFiltro = new ArrayList<>();
        estadoFiltro = catalogoService.MostarTodoCatalogo("estado_solicitud");
        unidadFiltros = new ArrayList<>();
        unidadFiltros = reservaCompromisoService.getListaUnidadesReservas();
        btnEditar = false;
        btnRegistrar = false;
        listaVerificadora = new ArrayList<>();
        consultaBtnRendered = false;
        registraNuevoVerificar = false;
        renderedRequisitos = false;
        listaBeneficiarios = new ArrayList<>();
        conteo = 0;
        listaBeneficariosReservas = new ArrayList<>();
        List<Rol> rol = new ArrayList<>();
        rol = clienteService.getRolCategoriaUnidad(userSession.getNameUser());
        verififcar = true;
        for (Rol item : rol) {
            switch (item.getCategoria().getCodigo()) {
                case "2":
                case "3":
                case "14":
                case "15":
                    verififcar = false;
                    break;
                default:
                    break;
            }
        }

        listaGlobalDetaSolicCompr = new ArrayList<>();
        unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");
    }

    public void seleccionPresupuesto(ThCargoRubros p) {
        //if (!listaGlobalDetaSolicCompr.isEmpty()) {
        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudPartidas) {
            if (p.equals(data.getRefDistributivo())) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
            }
        }
        //}
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
        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
        JsfUtil.addInformationMessage("", "EL REGISTRO QUE SELECCION FUE EXITOSO");
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
            totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible()).subtract(obtieneTotalComprometido(detalle.getPartidasDirecta().getPartidaPresupuestaria()));
            totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
            totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
        }
    }

    public void obtieneListaDistributivo(SolicitudReservaCompromiso p) {
        List<ThCargoRubros> tmp = servicioThCargos.distributivoCompleto(p.getPeriodo());
        listaDistributivo = new ArrayList<>();
        if (tmp != null && !tmp.isEmpty()) {

            for (ThCargoRubros item : tmp) {
                listaDistributivo.add(item);
            }
        }
    }

    public void refrescarBeneficiarios() {
        lazyProveedor = new LazyModel<>(Proveedor.class);
    }

    public void actualizarListadoReservas() {
        if (opcionBusqueda.getAnio() != null) {
            lazyReal = new LazyModel(SolicitudReservaCompromiso.class);
            lazyReal.getFilterss().put("estado.codigo", Arrays.asList("R", "E", "REVIS", "O"));
            lazyReal.getFilterss().put("usuarioCreacion:equal", user.getNameUser());
            lazyReal.getFilterss().put("numTramite:equal", null);
            lazyReal.getFilterss().put("procedimiento:equal", null);
            lazyReal.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            lazyReal = null;
        }
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
        consultaBtnRendered = false;
        registraNuevoVerificar = false;
    }

    public void registarrMostrarAndOcultarPaneles() {
        clearAllFilters();
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        reservaCompromiso = new SolicitudReservaCompromiso();
        panel1 = false;
        panel2 = true;
        ocultarbutton = false;
        mostrarButton = true;
        consultaBtnRendered = true;
        registraNuevoVerificar = true;
        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        this.reservaCompromiso.setFechaSolicitud(new Date());
        this.reservaCompromiso.setPeriodo(opcionBusqueda.getAnio());
        listaBeneficiarios = new ArrayList<>();
        listaBeneficariosReservas = new ArrayList<>();
    }

    public void inicializarMaxcimoCodigo() {
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);
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
     * Realiza la llamanda de los diaLogos segun su condicion que puede ser el
     * PAPP, Plan Distriubtivo o las partidas Directas.
     */
    public void accionComponenetes() {
        if (!reservaCompromisoService.getPeriodoAprobado(reservaCompromiso.getPeriodo())) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "ESTE PERÍODO NO SE ENCUENTRA APROBADO");
            return;
        }
        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }
        if (reservaCompromiso.getPeriodo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Elija una fecha y una unidad");
        } else {
            tabla1 = true;
            tabla2 = false;
            tabla3 = false;
            //cargoUnidadUser
//            unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
            reservaCompromiso.setUnidadRequiriente(unidadAdministrativa);
            List<Producto> listaAnaidir = productoService.getListaProductoByPeriodo(reservaCompromiso.getPeriodo());
            for (Producto item1 : listaAnaidir) {
                if (!listaProductos.contains(item1)) {
                    this.listaProductos.add(item1);
                }
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
        if (!documentosListas.isEmpty()) {
            for (DocumentosModel data : documentosListas) {
                if (data.getRequisito().getObligatorio()) {
                    if (data.getUrl() == null || data.getUrl() == "") {
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

    public boolean verificarMes() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        simpleDateFormat = new SimpleDateFormat("MM");
        int mes = Integer.valueOf(simpleDateFormat.format(reservaCompromiso.getFechaSolicitud())) - 1;
        String mesString = String.valueOf(mes);
        Short mesShort = Short.valueOf(mesString);
        simpleDateFormat = new SimpleDateFormat("YYYY");
        Short anio = Short.valueOf(simpleDateFormat.format(reservaCompromiso.getFechaSolicitud()));
        ControlCuentaPresupuestario control = suplementoIngresoService.getVerificarMesAnio(mesShort, anio);
        if (control != null) {
            return control.getEstado();
        } else {
            return false;
        }
    }

    public void savesolicitud() {
        boolean verificar = true;
        boolean pararMetodo = true;
        boolean pararMetodoPdi = true;
        listaGuardar = new ArrayList<>();
        if (!verificarMes()) {
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE REALIZAR ESTA REFORMA PORQUE EL MES SE ENCUENTRA CERRADO PARA EL AÑO REQUIRIENTE");
            return;
        }
        if (listaGlobalDetalleSolicitudCompromisos != null) {
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
                if (result1 > itemsuma.getActividadProducto().getMontoReformada().doubleValue()) {
                    verificar = false;
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Información", "Este producto " + itemsuma.getActividadProducto().getDescripcion() + " no tiene mucho fondo para su reserva solicitada");
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
                if (itemverificar.getMontoSolicitado() == null) {
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
                if (itemverificar.getMontoSolicitado() == null) {
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
        inicializarMaxcimoCodigo();
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
            detalleSolicitud.setEjecutado(BigDecimal.ZERO);
            detalleSolicitud.setLiquidado(BigDecimal.ZERO);
            detalleSolicitud = detalleReservaService.create(detalleSolicitud);
        }
        //Guardamos la los requisitos en relacion a la reserva de compromiso que se creo en la linea de codigo anterior
        if (reservaCompromiso != null) {
            JsfUtil.addInformationMessage("Reserva", "Datos almacenados correctamente");
        } else {
            JsfUtil.addErrorMessage("Usuario", "Ocurrió un error al guardar los datos del usuario");
        }
        if (verificacion) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("SOLICITUD", "La solicitud " + reservaCompromiso.getSecuencial() + "-" + reservaCompromiso.getPeriodo() + " se ha realizado con éxito");
            listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
            listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
            listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
            listaGuardar = new ArrayList<>();
            solicitudRequisitoList = new ArrayList<>();
            procedimientoRequisitoList = new ArrayList<>();
            reservaCompromiso = new SolicitudReservaCompromiso();
            detalleSolicitud = new DetalleSolicitudCompromiso();
            procedimientoSeleccionado = new Procedimiento();
            lazyServidor = null;
            lazyProveedor = null;
            setCbTipo("");
            consultarMostrarAndOcultarPaneles();
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Los campos no pueden ser nulo o tener valor con ceros");
        }
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
         listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        reservaCompromiso = new SolicitudReservaCompromiso();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        procedimientoSeleccionado = new Procedimiento();
        lazyServidor = null;
        lazyProveedor = null;
        setCbTipo("");
        listaVerificadora = new ArrayList<>();
        consultaBtnRendered = true;
        registraNuevoVerificar = false;
        consultaBtnRendered = false;
    }

    public void añadiendoFormato(Producto p) {
        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicitudCompromisos) {
            if (data.getActividadProducto().equals(p)) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
            }
        }
        BigDecimal maximo_resultado = BigDecimal.ZERO;
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
        JsfUtil.addInformationMessage("INFORMATION", "PRODUCTO SELECIONADO CORRECTAMENTE");
        producto = new Producto();
        detalleSolicitud = new DetalleSolicitudCompromiso();
        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
    }

    public void eliminadoMemoriaDetalleSOlicitud(DetalleSolicitudCompromiso d) {
        listaGlobalDetalleSolicitudCompromisos.remove(d);
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
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
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
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().subtract(obtieneTotalComprometido(d.getPartidasDirecta().getPartidaPresupuestaria())).doubleValue()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "El monto a solicitar es mayor al monto disponible");
            detalleSolicitud.setMontoSolicitado(BigDecimal.ZERO);
        } else {
            this.listaGlobalDetalleSolicitudPartidas.add(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud), detalleSolicitud);
            this.listaGlobalDetalleSolicitudPartidas.remove(this.listaGlobalDetalleSolicitudPartidas.indexOf(detalleSolicitud));
            PrimeFaces.current().executeScript("PF('Dlogo').hide()");
            PrimeFaces.current().ajax().update(":formDlogo");
        }
        calcularTotalesPD();
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
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
        if (detalleSolicitud.getMontoSolicitado().doubleValue() > detalleSolicitud.getMontoDisponible().subtract(obtieneTotalComprometido(d.getPartidasDirecta().getPartidaPresupuestaria())).doubleValue()) {
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
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
    }

    public void eliminarPArtidasDirectasAndDistributivo(DetalleSolicitudCompromiso d) {
        listaGlobalDetalleSolicitudPartidas.remove(d);
        calcularTotalesPD();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
    }

    public void eliminarPArtidasDirectas(DetalleSolicitudCompromiso d) {
        listaGlobalDetalleSolicPartidasDirectas.remove(d);
        calcularTotalesPD();
        JsfUtil.addInformationMessage("PRODUCTO", "Ha sido removido correctamente");
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
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
        JsfUtil.addInformationMessage("INFORMATION", "PARTIDA SELECIONADO CORRECTAMENTE");
    }

    public void visualizarDetalleSolcitud(SolicitudReservaCompromiso s) {
        reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = s;
        listaBeneficariosReservasvista = new ArrayList<>();
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
        renderedRequisitos = Boolean.FALSE;
        this.visualizacionSolicitud = reservaCompromisoService.getListaDetlleSolciitud(s);
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        if (reservaCompromiso.getBeneficiario() != null) {
            listaBeneficariosReservasvista.add(s.getBeneficiario());
        } else {
            List<BeneficiarioSolicitudReserva> ver = reservaBeneficiarioService.getListaBeneficiarioSolicitudReservas(reservaCompromiso);
            for (BeneficiarioSolicitudReserva data : ver) {
                listaBeneficariosReservasvista.add(data.getBeneficiario());
            }
        }
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

    public void edicionSolicitud(SolicitudReservaCompromiso solicitudReserva) {
        if (solicitudReserva.getEstado().getCodigo().equals("APRO")) {
            JsfUtil.addWarningMessage("Advertencia", "La Reserva no puede ser modificada, Estado:Aprobada.");
            return;
        }
        consultaBtnRendered = true;
        reservaCompromiso = new SolicitudReservaCompromiso();
        ocultarbutton = true;
        mostrarButton = false;
        listaGuardar = new ArrayList<>();
        listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
        listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaGlobalDetalleSolicPartidasDirectas=new ArrayList<>();
        listaeditable1 = new ArrayList<>();
        listaeditable2 = new ArrayList<>();
        listaeditable3 = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        this.reservaCompromiso = solicitudReserva;
        this.unidadAdministrativa = this.reservaCompromiso.getUnidadRequiriente();
        listaBeneficiarios = new ArrayList<>();
        if (reservaCompromiso.getBeneficiario() != null) {
            listaBeneficiarios.add(reservaCompromiso.getBeneficiario());
        } else {
            List<BeneficiarioSolicitudReserva> ver = reservaBeneficiarioService.getListaBeneficiarioSolicitudReservas(solicitudReserva);
            for (BeneficiarioSolicitudReserva data : ver) {
                listaBeneficiarios.add(data.getBeneficiario());
            }
        }
        this.procedimientoSeleccionado = solicitudReserva.getProcedimiento();
        listaeditable1 = reservaCompromisoService.getEdicionDetalleProducto(solicitudReserva);
        listaeditable2 = reservaCompromisoService.getEdicionDetallePDAndD(solicitudReserva);
        listaeditable3 = reservaCompromisoService.getEdicionDetallePD(solicitudReserva);
        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(solicitudReserva);
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
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
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
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGÚN DOCUMENTO ADJUNTO AL REQUISITO SELECCIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public void eliminarPDF(ProcedimientoRequisito procedimientoRequisito) {
        DocumentosModel item = new DocumentosModel();
        boolean verificar = true;
        if (!documentosListas.isEmpty()) {
            for (DocumentosModel data : documentosListas) {
                if (data.getRequisito().equals(procedimientoRequisito)) {
                    if (data.getUrl() == null || "".equals(data.getUrl())) {

                        JsfUtil.addErrorMessage("Error", "No se puede eliminar porque no hay ningún archivos");
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
            archivo = new DocumentosModel();
            archivo.setRequisito(procedimientoRequisito);
            archivo.setUrl(event.getFile().getFileName());
            archivo.setArchivo(event.getFile());
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

    public void editarReservaCompromiso() {
        if (reservaCompromiso.getEstado().getCodigo().equals("APRO")
                || reservaCompromiso.getEstado().getCodigo().equals("LIQUI")
                || reservaCompromiso.getEstado().getCodigo().equals("LEG")) {
            JsfUtil.addWarningMessage("Advertencia", "La Reserva no puede ser modificada, Estado:Aprobada.");
            return;
        }
        listaGuardar.clear();
        listaGuardar = new ArrayList<>();
        List<DetalleSolicitudCompromiso> listaValoresAnteriores;
        boolean verificar = true;
        boolean pararMetodo = true;
        boolean pararMetodoPdi = true;
        if (!listaGlobalDetalleSolicitudCompromisos.isEmpty() || !listaGlobalDetalleSolicitudPartidas.isEmpty() || !listaGlobalDetalleSolicPartidasDirectas.isEmpty()) {
            if (listaGlobalDetalleSolicitudCompromisos != null && !listaGlobalDetalleSolicitudCompromisos.isEmpty()) {
                listaGlobalDetalleSolicitudCompromisos.forEach((l) -> {
                    listaGuardar.add(l);
                });
            }
            if (listaGlobalDetalleSolicitudPartidas != null && !listaGlobalDetalleSolicitudPartidas.isEmpty()) {
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

        List<BeneficiarioSolicitudReserva> listaTemporal = reservaBeneficiarioService.getListaBeneficiarioSolicitudReservas(reservaCompromiso);
        for (BeneficiarioSolicitudReserva item : listaTemporal) {
            reservaBeneficiarioService.remove(item);

        }
        reservaCompromiso.setUsuarioModificacion(userSession.getName());
        reservaCompromiso.setFechaModificacion(new Date());
        reservaCompromisoService.edit(reservaCompromiso);
        int a = detalleReservaService.removeDetReservaUpdate(reservaCompromiso);

        if (a == 1) {
            System.out.println("se elimino");
        } else {
            JsfUtil.addErrorMessage("ERROR", "NO SE PUEDE REALIZAR LA EDICION DEL DETALLE DE LA RESERVA");
            return;
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
        listaVerificadora = new ArrayList<>();
        registraNuevoVerificar = true;
    }

    public void enviarSolicitud(SolicitudReservaCompromiso s) {
        this.reservaCompromiso = s;
        reservaCompromiso.setEstado(reservaCompromisoService.getestados("E", "estado_solicitud"));
        reservaCompromisoService.edit(reservaCompromiso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Información", "La solicitud se envio correctamente");
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
        listaGlobalDetalleSolicPartidasDirectas = new ArrayList<>();
        ListadetalleSolicitud = new ArrayList<>();
        listaGuardar = new ArrayList<>();
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoSeleccionado = new Procedimiento();
        listaBeneficiarios = new ArrayList<>();
        listaBeneficariosReservas = new ArrayList<>();
        unidadAdministrativa = new UnidadAdministrativa();
    }

    public void CargarDatosBeneficiario() {
        if (reservaCompromiso.getTipoBeneficiario() != null) {
            //si reservaCompromiso.getTipoBeneficiario() es igual a TRUE entonces es PROVEEDOR
            if (reservaCompromiso.getTipoBeneficiario()) {
                panelProveedor = true;
                panelServidor = false;
                lazyProveedor = new LazyModel<>(Proveedor.class);
            } //si reservaCompromiso.getTipoBeneficiario() es igual a FALSE entonces es SERVIDOR
            else {
                panelProveedor = false;
                panelServidor = true;
                lazyServidor = new LazyModel<>(Servidor.class);
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
        JsfUtil.update("prupoBeneficiario");
    }

    public void AñadirBeneficiarioProveedor(Proveedor provedor) {
        reservaCompromiso.setBeneficiario(provedor.getCliente());
        reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
        PrimeFaces.current().executeScript("PF('dlgBeneficiario').hide()");
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario " + reservaCompromiso.getBeneficiario().getNombreCompleto() + " ha sido selecionado con éxito");
        JsfUtil.update("prupoBeneficiario");
    }

    public void eliminarDataBeneficiario(Cliente c) {
        listaBeneficiarios.remove(listaBeneficiarios.indexOf(c));
        JsfUtil.addInformationMessage("BENEFICIARIO", "El Beneficiario se elimino con éxito");

    }

    public void handleCloseForm(CloseEvent event) {
        resetSolicitud();
        PrimeFaces.current().ajax().update(":DlgoVisualizacion");
    }

    private void calcularTotales() {
        totalDisponible = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalSolicitado = BigDecimal.ZERO;
        if (listaGlobalDetalleSolicitudCompromisos != null) {
            for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudCompromisos) {
                totalDisponible = totalDisponible.add(detalle.getMontoDisponible());
                totalComprometido = totalComprometido.add(detalle.getMontoComprometido());
                totalSolicitado = totalSolicitado.add(detalle.getMontoSolicitado());
            }
        }
    }

    private void calcularTotalesPD() {
        totalDisponiblePD = BigDecimal.ZERO;
        totalComprometidoPD = BigDecimal.ZERO;
        totalSolicitadoPD = BigDecimal.ZERO;
        if (listaGlobalDetalleSolicitudPartidas != null) {
            for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicitudPartidas) {
                totalDisponiblePD = totalDisponiblePD.add(detalle.getMontoDisponible());
                totalComprometidoPD = totalComprometidoPD.add(detalle.getMontoComprometido());
                totalSolicitadoPD = totalSolicitadoPD.add(detalle.getMontoSolicitado());
            }
        }
        totalDisponiblePDI = BigDecimal.ZERO;
        totalComprometidoPDI = BigDecimal.ZERO;
        totalSolicitadoPDI = BigDecimal.ZERO;
        if (listaGlobalDetalleSolicPartidasDirectas != null) {
            for (DetalleSolicitudCompromiso detalle : listaGlobalDetalleSolicPartidasDirectas) {
                totalDisponiblePDI = totalDisponiblePDI.add(detalle.getMontoDisponible()).subtract(obtieneTotalComprometido(detalle.getPartidasDirecta().getPartidaPresupuestaria()));
                totalComprometidoPDI = totalComprometidoPDI.add(detalle.getMontoComprometido());
                totalSolicitadoPDI = totalSolicitadoPDI.add(detalle.getMontoSolicitado());
            }
        }
    }

    public void abrirDlogoBeneficiario() {
        if (reservaCompromiso.getDescripcion().length() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Campos Vacios");
            return;
        }

        PrimeFaces.current().executeScript("PF('dlgBeneficiario').show()");
        PrimeFaces.current().ajax().update("formDlgBeneficiario");
    }

    //NUEVO
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

    public void obtenerPartidasDirectas() {
        listaPartidasDirectas = new ArrayList<>();
        listaPartidasDirectas = proformaEgresosService.getProformaPresupuestoPartidaDirecta(opcionBusqueda.getAnio(), "PDI");
    }

    public void seleccionPartidaDirecta(ProformaPresupuestoPlanificado pr) {
        //if (!listaGlobalDetaSolicCompr.isEmpty()) {
        for (DetalleSolicitudCompromiso data : listaGlobalDetalleSolicPartidasDirectas) {
            if (pr.equals(data.getPartidasDirecta())) {
                JsfUtil.addWarningMessage("AVISO", "YA HA SIDO SELECCIONADO");
                return;
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
        calcularTotales();
        calcularTotalesPD();
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");

    }

    public void solicitarReservasPDI(DetalleSolicitudCompromiso d) {
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
        PrimeFaces.current().ajax().update(":tablaSolicitud");
        PrimeFaces.current().ajax().update(":tablaSolicitud2");
        PrimeFaces.current().ajax().update(":tablaSolicitudPdi");
    }
    
    public BigDecimal obtieneTotalComprometido (String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalComprometido(partida);
        return resultado;
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER AN SETTER">
    public boolean isVerififcar() {
        return verififcar;
    }

    public List<DetalleSolicitudCompromiso> getListaGlobalDetaSolicCompr() {
        return listaGlobalDetaSolicCompr;
    }

    public void setListaGlobalDetaSolicCompr(List<DetalleSolicitudCompromiso> listaGlobalDetaSolicCompr) {
        this.listaGlobalDetaSolicCompr = listaGlobalDetaSolicCompr;
    }

    public List<ThCargoRubros> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<ThCargoRubros> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public void setVerififcar(boolean verififcar) {
        this.verififcar = verififcar;
    }

    public BigDecimal getTotalSolicitadoPD() {
        return totalSolicitadoPD;
    }

    public void setTotalSolicitadoPD(BigDecimal totalSolicitadoPD) {
        this.totalSolicitadoPD = totalSolicitadoPD;
    }

    public List<Cliente> getListaBeneficariosReservasvista() {
        return listaBeneficariosReservasvista;
    }

    public void setListaBeneficariosReservasvista(List<Cliente> listaBeneficariosReservasvista) {
        this.listaBeneficariosReservasvista = listaBeneficariosReservasvista;
    }

    public List<Cliente> getListaBeneficiarios() {
        return listaBeneficiarios;
    }

    public void setListaBeneficiarios(List<Cliente> listaBeneficiarios) {
        this.listaBeneficiarios = listaBeneficiarios;
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

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
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

    public LazyModel<Proveedor> getLazyProveedor() {
        return lazyProveedor;
    }

    public void setLazyProveedor(LazyModel<Proveedor> lazyProveedor) {
        this.lazyProveedor = lazyProveedor;
    }

    public LazyModel<Servidor> getLazyServidor() {
        return lazyServidor;
    }

    public void setLazyServidor(LazyModel<Servidor> lazyServidor) {
        this.lazyServidor = lazyServidor;
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

    public boolean isConsultaBtnRendered() {
        return consultaBtnRendered;
    }

    public void setConsultaBtnRendered(boolean consultaBtnRendered) {
        this.consultaBtnRendered = consultaBtnRendered;
    }

    public boolean isSoloPresupuesto() {
        return soloPresupuesto;
    }

    public void setSoloPresupuesto(boolean soloPresupuesto) {
        this.soloPresupuesto = soloPresupuesto;
    }

    public boolean isRenderedRequisitos() {
        return renderedRequisitos;
    }

    public void setRenderedRequisitos(boolean renderedRequisitos) {
        this.renderedRequisitos = renderedRequisitos;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
//</editor-fold>

    public List<DetalleSolicitudCompromiso> getListaGlobalDetalleSolicPartidasDirectas() {
        return listaGlobalDetalleSolicPartidasDirectas;
    }

    public void setListaGlobalDetalleSolicPartidasDirectas(List<DetalleSolicitudCompromiso> listaGlobalDetalleSolicPartidasDirectas) {
        this.listaGlobalDetalleSolicPartidasDirectas = listaGlobalDetalleSolicPartidasDirectas;
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

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }
    
    
    
}
