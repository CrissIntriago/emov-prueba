/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.Presupuesto.Service.DetalleReformaTraspasoService;
import com.origami.sigef.Presupuesto.Service.ReformaTraspasoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.lazy.ProductoLazy;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.PlanAnualPoliticaPublicaService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanLocalPoliticaService;
import com.origami.sigef.contabilidad.service.PlanLocalProgramaProyectoService;
import com.origami.sigef.contabilidad.service.PlanLocalSistemaService;
import com.origami.sigef.contabilidad.service.PlanNacionalEjeService;
import com.origami.sigef.contabilidad.service.PlanNacionalObjetivoService;
import com.origami.sigef.contabilidad.service.PlanNacionalPoliticaService;
import com.origami.sigef.contabilidad.service.PlanProgramaticoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reformaT2View")
@ViewScoped
public class ReformaT2Controller extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReformaT2Controller.class.getName());

    //<editor-fold defaultstate="collapsed" desc="OBJETOS">
    private ActividadOperativa actividad;
    private OpcionBusqueda busqueda;
    private Producto producto;
    private ReformaTraspaso reformaTraspaso;
    private ReformaTraspaso reformaVerificar;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private PlanAnualProgramaProyecto programaProyecto;
    private PlanAnualPoliticaPublica planAnual;
    private ProformaPresupuestoPlanificado proformaPresupuestoPlanificado;
    private PartidasDistributivo partidaDistributivo;
    private PartidasDistributivoAnexo partidaDistributivoAnexo;

    private PlanNacionalEje ejeNacionalSeleccionado;
    private PlanNacionalObjetivo objetivoNacionalSeleccionado;
    private PlanNacionalPolitica politicaNacionalSeleccionada;
    private PlanLocalSistema sistemaLocalSeleccionado;
    private PlanLocalObjetivo objetivoLocalSeleccionado;
    private PlanLocalPolitica politicaLocalSeleccionado;
    private PlanAnualProgramaProyecto programaProyectoSeleccionado;
    private ActividadOperativa actividadSeleccionada;
    private Producto productoSeleccionado;

    private PlanAnualPoliticaPublica planAnualSeleccionado;
    private Distributivo mostrarData, dataView;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GLOBALES">
    private Long idUnidadA;
    private Long idPlanProgramatico;
    private Long codigo;
    private double totalSaldoDisponible;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private double totalMontoReformado;
    private double totalSaldoDisponibleDGeneral;
    private double totalTraspasoReduccionDGeneral;
    private double totalTraspasoIncrementoDGeneral;
    private double totalMontoReformadoDGeneral;
    private double totalSaldoDisponibleDAnexo;
    private double totalTraspasoReduccionDAnexo;
    private double totalTraspasoIncrementoDAnexo;
    private double totalMontoReformadoDAnexo;
    private double totalSaldoDisponiblePDirecta;
    private double totalTraspasoReduccionPDirecta;
    private double totalTraspasoIncrementoPDirecta;
    private double totalMontoReformadoPDirecta;
    private double totalDefinitivoIncremento = 0;
    private double totalDefinitivoReduccion = 0;

    private int indiceDeAcordian;
    private Integer anioActual;
    private String programaProyectoLocal;
    private String productoGlobal;
    private String tipoTraspaso;
    private String observaciones;
    private BigDecimal rmu;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LAZYS">
    private ProductoLazy productoLaz;
    private ProductoLazy lazyProductos;
    private LazyModel<Producto> productoLazy;
    private ProductoLazy productoAsignacion;
    private LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy;
    private LazyModel<PlanAnualPoliticaPublica> planAnualazy;
    private LazyModel<ActividadOperativa> lazyActividadOperativas;
    private LazyModel<ReformaTraspaso> lazyReformaTraspaso;
    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LISTAS">
    private List<MasterCatalogo> listaAnio;
    private List<UnidadAdministrativa> unidades;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<DetalleReformaTraspaso> listaProductoReforma;
    private List<Producto> listaProducto;
    private List<ReformaTraspaso> listaCodReforma;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private List<PartidasDistributivo> listaPartidaDistributivo;
    private List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo;
    private List<DistributivoAnexo> listaDistributivoAnexo;
    private List<PartidasDistributivo> listaRubros;
    private List<PlanProgramatico> listaEstructura;
    private List<CatalogoPresupuesto> listaItem;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PartidasDistributivo> listaVista;
    private List<Distributivo> listaDistributivo;
    private List<FuenteFinanciamiento> listaFuenteDirecta;
    private List<PlanProgramatico> listaPlanProgramaticoDirecta;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestosDirecta;

    private List<PlanNacionalEje> ejesNacionales;
    private List<PlanNacionalObjetivo> objetivosNacionales;
    private List<PlanNacionalObjetivo> objetivosNa;
    private List<PlanNacionalPolitica> politicasNacionales;
    private List<PlanNacionalPolitica> politicasNa;
    private List<PlanLocalSistema> sistemasLocales;
    private List<PlanLocalObjetivo> objetivosLocales;
    private List<PlanLocalObjetivo> objetivosLo;
    private List<PlanLocalPolitica> politicasLocales;
    private List<PlanLocalPolitica> politicasLo;
    private List<ValoresDistributivo> listaValoresDistributivo;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;

    //NUEVO
    private List<ThCargoRubros> listaCargosRubros;
    private List<ThCargo> listaCargos;
    
    private List<CatalogoItem> distribuccion, distribuccionMetaList;
    private List<CatalogoItem> listacomponentes;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BOOLEANOS">
    private boolean btnNuevoPapp;
    private boolean registraNuevoPapp;
    private boolean ocultarMostarPlanNacional;
    private boolean bloqueo;
    private boolean filtroDatosNullProgramaProyectos;
    private boolean filtroDatosNullTablaGeneralPlanes;
    private boolean filtroDatosNullTablaPlanesAnuales;
    private boolean bolReformaT1;
    private boolean btnGuardar;
    private boolean btnContinuarTerminarTarea;
    private boolean btnCancelar;
    private boolean btnGenerarReforma;
    private boolean formBusqRegistrar;
    private boolean formBusqRegistrarT2;
    private boolean formBusqConsultar;
    private boolean btnRegistrar;
    private boolean btnRegistrarT2;
    private boolean btnConsultar;
    private boolean formConsultaReforma;
    private boolean panelDistributivo;
    private boolean bolDistributivoGeneral;
    private boolean bolDistributivoAnexo;
    private boolean bolPartidaDirecta;
    private boolean bolPapp;
    private boolean btnTraspasos;
    private boolean editar;
    private boolean btnNuevoPartidaDirecta;
    private boolean vistaPartidaDisGeneral;
    private boolean btnGenerarTraspaso;
    private boolean editarObserv;
    private boolean renderedDistributivo;
    private boolean renderedDistributivoAnexo;
    private boolean renderedPAPP;
    private boolean renderedPartidaDirecta;
    private Boolean semestreMetal, trimestreMeta, cuatrimestreMeta, mensualMeta;
    private Boolean semestre, trimestre, cuatrimestre, mensual;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SERVICES">
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private PlanProgramaticoService planProgramaticoService;
    @Inject
    private ReformaTraspasoService reformaTraspasoService;
    @Inject
    private DetalleReformaTraspasoService detalleReformaTraspasoService;
    @Inject
    private ActividadOperativaService actividadOperativaService;
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private ProductoService productoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private PlanNacionalEjeService planNacionalEjeService;
    @Inject
    private PlanNacionalObjetivoService planNacionalObjetivoService;
    @Inject
    private PlanNacionalPoliticaService planNacionalPoliticaService;
    @Inject
    private PlanLocalObjetivoService planLocalObjetivoService;
    @Inject
    private PlanLocalPoliticaService planLocalPoliticaService;
    @Inject
    private PlanLocalSistemaService planLocalSistemaService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private PartidaDistributivoService partidasDistributivoService;
    @Inject
    private PartidaDistributivoAnexoService partidasDistributivoAnexoService;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private PlanProgramaticoService estructuraService;
    @Inject
    private CatalogoPresupuestoService itemService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private ManagerService service;
//</editor-fold>

    @PostConstruct
    public void initView() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                actividad = new ActividadOperativa();
                busqueda = new OpcionBusqueda();
                producto = new Producto();
                reformaTraspaso = new ReformaTraspaso();
                reformaVerificar = new ReformaTraspaso();
                detalleReformaTraspaso = new DetalleReformaTraspaso();
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                planAnualPoliticaPublica = new PlanAnualPoliticaPublica();
                planAnualProgramaProyecto = new PlanAnualProgramaProyecto();
                actividad = new ActividadOperativa();
                planAnual = new PlanAnualPoliticaPublica();
                proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
                mostrarData = new Distributivo();
                dataView = new Distributivo();
                reformaTraspaso.setPeriodo(Utils.getAnio(new Date()).shortValue());
                Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
                idUnidadA = new Long(62);//ENRIQUE
//                if (d != null) { ENRIQUE
//                    if (d.getUnidadAdministrativa() != null) {
//                        idUnidadA = d.getUnidadAdministrativa().getId();
//                    }
//                } else {
//                    JsfUtil.addErrorMessage("Advertencia", "No puede efectuar una reforma, ya que no cuenta con un cargo en el Distributivo.");
//                    return;
//                }
                setIndiceDeAcordian(-1);
                short anio = Utils.getAnio(new Date()).shortValue();
                this.totalSaldoDisponible = 0;
                this.totalTraspasoReduccion = 0;
                this.totalTraspasoIncremento = 0;
                this.totalMontoReformado = 0;
                busqueda.setAnio(anio);
                productoLaz = new ProductoLazy(busqueda);
                lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
                CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
                lazyReformaTraspaso.getFilterss().put("estadoReforma:equal", estadoReforma);
                lazyReformaTraspaso.getFilterss().put("tipo", Boolean.FALSE);
                lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
//                lazyReformaTraspaso.getFilterss().put("periodo", busqueda.getAnio());
                productoLazy = new LazyModel(Producto.class);
                listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
                unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
                listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
                listaProductoReforma = new ArrayList<>();
                listaProducto = new ArrayList<>();
                listaCodReforma = new ArrayList<>();
                ejesNacionales = planNacionalEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
                sistemasLocales = planLocalSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
                listaPartidaDistributivo = new ArrayList<>();
                listaPartidaDistributivoAnexo = new ArrayList<>();
                listaProformaPresupuestoPlanificado = new ArrayList<>();
                programaProyecto = new PlanAnualProgramaProyecto();
                booleanosReset();
                reformaVerificar = reformaTraspasoService.verificarNumTramiteReforma(BigInteger.valueOf(tramite.getNumTramite()), busqueda.getAnio());
                if (reformaVerificar != null) {
                    if (reformaVerificar.getEstadoReformaTramite() != null) {
                        if (reformaVerificar.getEstadoReformaTramite().getCodigo().equals("OBS-REF") || reformaVerificar.getEstadoReformaTramite().getCodigo().equals("OBSP-REF")) {
                            btnRegistrar = Boolean.FALSE;
                            btnRegistrarT2 = Boolean.FALSE;
                        }
                    }
                }
                listaCargosRubros = new ArrayList<>();
                anioActual = Utils.getAnio(new Date());
            } else {
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
    }

    public void booleanosReset() {
        System.out.println("Boolean Reset..");
        btnNuevoPapp = true;
        registraNuevoPapp = false;
        ocultarMostarPlanNacional = false;
        bloqueo = false;
        bolReformaT1 = false;
        btnGuardar = false;
        btnContinuarTerminarTarea = true;
        btnCancelar = true;
        btnGenerarReforma = false;
        formBusqConsultar = false;
        formBusqRegistrar = false;
        formBusqRegistrarT2 = false;
        btnRegistrar = true;
        btnRegistrarT2 = false;
        btnConsultar = false;
        btnTraspasos = false;
        bolPapp = false;
        bolDistributivoGeneral = Boolean.FALSE;
        bolDistributivoAnexo = Boolean.FALSE;
        panelDistributivo = false;
        bolPartidaDirecta = false;
        formConsultaReforma = true;
        editar = false;
        tipoTraspaso = "";
        ReformaTraspaso ref = reformaTraspasoService.verificarNumTramiteReforma(BigInteger.valueOf(tramite.getNumTramite()), busqueda.getAnio());
        System.out.println("TrReforma " + ref);
        if (ref != null) {
            btnRegistrarT2 = true;
            btnRegistrar = false;
        } else {
            btnRegistrarT2 = false;
        }
    }

    public double getTotalIncrementoOrReduccionByReforma(ReformaTraspaso reforma, boolean incremento) {
        busqueda.setAnio(reforma.getPeriodo());
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reforma.getPeriodo(), "REGT", true, BigInteger.valueOf(reforma.getId()));
        listaRubros = reformaTraspasoService.listaPresupuestoPartidasTodasReforma(busqueda.getAnio(), BigInteger.valueOf(reforma.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reforma.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reforma.getPeriodo(), BigInteger.valueOf(reforma.getId()));

        double totalSaldoDisponibleR = 0;
        double totalTraspasoIncrementoR = 0;
        double totalTraspasoReduccionR = 0;
        double totalMontoReformadoR = 0;
        if (!listaProducto.isEmpty()) {
            for (Producto item : listaProducto) {

                if (item.getEstado()) {
                    totalSaldoDisponibleR = totalSaldoDisponibleR + item.getSaldoDisponible().doubleValue();
                    totalTraspasoIncrementoR = totalTraspasoIncrementoR + item.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionR = totalTraspasoReduccionR + item.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoR = totalMontoReformadoR + item.getMontoReformada().doubleValue();
                }
            }
        }
        if (!listaRubros.isEmpty()) {
            for (PartidasDistributivo listaRubro : listaRubros) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaRubro.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + listaRubro.getTraspasoReduccion().doubleValue();
            }
        }
        if (!listaPartidaDistributivoAnexo.isEmpty()) {
            for (PartidasDistributivoAnexo listaAnexo : listaPartidaDistributivoAnexo) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + listaAnexo.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + listaAnexo.getTraspasoReduccion().doubleValue();
            }
        }
        if (!listaProformaPresupuestoPlanificado.isEmpty()) {
            for (ProformaPresupuestoPlanificado partidaDirecta : listaProformaPresupuestoPlanificado) {
                totalTraspasoIncrementoR = totalTraspasoIncrementoR + partidaDirecta.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionR = totalTraspasoReduccionR + partidaDirecta.getTraspasoReduccion().doubleValue();
            }
        }
        if (incremento) {
            totalDefinitivoIncremento = totalTraspasoIncrementoR;
            return totalTraspasoIncrementoR;
        } else {
            totalDefinitivoReduccion = totalTraspasoReduccionR;
            return totalTraspasoReduccionR;
        }
    }

    public void enviar(ReformaTraspaso ref) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " enviada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void registrarMostrarOcultar() {
        formBusqConsultar = false;
        formBusqRegistrar = true;
        formConsultaReforma = false;
        btnRegistrar = false;
        btnRegistrarT2 = false;
        btnConsultar = true;
        btnGenerarReforma = false;
        btnTraspasos = true;
        editar = false;
        editarObserv = false;
//        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
//        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaProducto = new ArrayList<>();
        reformaTraspaso = new ReformaTraspaso();
        short anio = Utils.getAnio(new Date()).shortValue();
        this.totalSaldoDisponible = 0;
        this.totalTraspasoReduccion = 0;
        this.totalTraspasoIncremento = 0;
        this.totalMontoReformado = 0;
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        PrimeFaces.current().ajax().update("formBusqueda");
        PrimeFaces.current().ajax().update("formBusqueda:fsetBusqueda");
        PrimeFaces.current().ajax().update("formTablaReforma");
        PrimeFaces.current().ajax().update("botonesReforma");
    }

    public void registrarMostrarOcultarT2() {
        formBusqConsultar = false;
        formBusqRegistrar = false;
        formBusqRegistrarT2 = true;
        formConsultaReforma = false;
        btnRegistrar = false;
        btnRegistrarT2 = false;
        btnConsultar = true;
        btnGenerarReforma = false;
//        btnGenerarTraspaso = false;
        editar = true;
        editarObserv = false;
//        this.listaGlobalDetalleSolicitudCompromisos = new ArrayList<>();
//        this.listaGlobalDetalleSolicitudPartidas = new ArrayList<>();
        listaProducto = new ArrayList<>();
        reformaTraspaso = new ReformaTraspaso();
        short anio = Utils.getAnio(new Date()).shortValue();
        this.totalSaldoDisponible = 0;
        this.totalTraspasoReduccion = 0;
        this.totalTraspasoIncremento = 0;
        this.totalMontoReformado = 0;
//        busqueda.setAnio(anio);
//        reformaTraspaso.setPeriodo(busqueda.getAnio());
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        CatalogoItem estadoReformRegistrada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
        CatalogoItem estadoReformObservada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
        listaCodReforma = reformaTraspasoService.getListReformaTramite(busqueda.getAnio(), Boolean.FALSE, BigInteger.valueOf(tramite.getNumTramite()));
        PrimeFaces.current().ajax().update("formBusqueda");
        PrimeFaces.current().ajax().update("formBusqueda:fsetBusqueda");
        PrimeFaces.current().ajax().update("formTablaReforma");
    }

    public void consultarMostrarOcultar() {
        booleanosReset();
        listaProducto = new ArrayList<>();
        reformaTraspaso = new ReformaTraspaso();
        short anio = Utils.getAnio(new Date()).shortValue();
        CatalogoItem estadoReforma = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        lazyReformaTraspaso = new LazyModel(ReformaTraspaso.class);
        lazyReformaTraspaso.getFilterss().put("estadoReforma:equal", estadoReforma);
        lazyReformaTraspaso.getFilterss().put("tipo", Boolean.FALSE);
        lazyReformaTraspaso.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
        lazyReformaTraspaso.getFilterss().put("periodo", busqueda.getAnio());
        this.totalSaldoDisponible = 0;
        this.totalTraspasoReduccion = 0;
        this.totalTraspasoIncremento = 0;
        this.totalMontoReformado = 0;
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        CatalogoItem estadoReformRegistrada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
        CatalogoItem estadoReformObservada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBS-REF");
        listaCodReforma = reformaTraspasoService.getListReformaTramite(busqueda.getAnio(), Boolean.FALSE, BigInteger.valueOf(tramite.getNumTramite()));
        PrimeFaces.current().ajax().update("formBusqueda");
        PrimeFaces.current().ajax().update("formBusqueda:fsetBusqueda");
        PrimeFaces.current().ajax().update("formTablaReforma");
        PrimeFaces.current().ajax().update("consultaTablaReforma");
    }

    public void generarTipoTraspaso() {
        if (null != tipoTraspaso) {
            switch (tipoTraspaso) {
                case "PAPP":
                    panelDistributivo = false;
                    bolPapp = Boolean.TRUE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    if (editar) {
                        System.out.println("busqueda.getAnio() >>  "+busqueda.getAnio()+" idUnidadA: "+idUnidadA+"CODIGO CATALOGO >> "+"REGT"+" || ID REFORMA >> "+ BigInteger.valueOf(reformaTraspaso.getId()));
                        listaProducto = reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
                        if (listaProducto.isEmpty()) {
                            btnGenerarReforma = false;
                        } else {
                            btnGenerarReforma = true;
                        }
                        actualizarTotales();
                    }
                    break;
                case "PD":
                    panelDistributivo = true;
                    bolDistributivoGeneral = Boolean.TRUE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    if (editar) {
                        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                        if (listaDistributivo.isEmpty()) {
                            btnGenerarReforma = false;
                        } else {
                            btnGenerarReforma = true;
                        }
                        listaDistributivo.sort(Comparator.comparing(Distributivo::getId));
                        actualizarTotalesDistributivo();
                    }
                    break;
                case "PDA":
                    panelDistributivo = false;
                    bolDistributivoAnexo = Boolean.TRUE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    if (editar) {
                        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                        if (listaPartidaDistributivoAnexo.isEmpty()) {
                            btnGenerarReforma = false;
                        } else {
                            btnGenerarReforma = true;
                        }
                        actualizarTotalesDistributivoAnexo();
                    }
                    break;
                case "PDI":
                    panelDistributivo = false;
                    bolPartidaDirecta = Boolean.TRUE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    if (editar) {
                        btnNuevoPartidaDirecta = false;
                        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
                        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));

                        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                        if (listaProformaPresupuestoPlanificado.isEmpty()) {
                            btnGenerarReforma = false;
                        } else {
                            btnGenerarReforma = true;
                        }
                        actualizarTotalesPartidaDirecta();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean verificarPartidaDistributivo(Distributivo d) {
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        List<PartidasDistributivo> partidaverificar = reformaTraspasoService.verificarlistaPresupuestoSinPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (!partidaverificar.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String loadCodigo(Long id) {
        String cod = Utils.completarCadenaConCeros(id.toString(), 6);
        return "C-" + cod;
    }

    public void abrirDlgPartidasPresupuestariaDistributivo(Distributivo d) {
        this.listaRubros = new ArrayList<>();
        this.listaItem = new ArrayList<>();
        this.listaEstructura = new ArrayList<>();
        this.listaFuente = new ArrayList<>();
        this.listaRubros = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        this.listaItem = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), busqueda.getAnio());
        this.listaEstructura = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        this.listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
//        setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        mostrarData = d;
        PrimeFaces.current().executeScript("PF('DlgPartidasDistributivos').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivos");

    }

    public void generarReformaT2() {
        if (reformaTraspaso.getCodigo() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un código de Reforma");
            return;
        }
        reformaTraspaso = reformaTraspasoService.getReformaByCodigo(reformaTraspaso.getCodigo());
        busqueda = new OpcionBusqueda();
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        bolReformaT1 = Boolean.FALSE;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.FALSE;
        btnGenerarReforma = Boolean.TRUE;
        if (reformaTraspaso.getId() != null) {
            btnGenerarTraspaso = true;
            btnGenerarReforma = false;
            btnTraspasos = true;
        } else {
            btnGenerarTraspaso = false;
            btnTraspasos = false;
        }
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), reformaTraspaso.getPeriodo());
        btnNuevoPapp = Boolean.FALSE;
        CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
    }

    public void buscarEditar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        if (reformaTraspaso.getId() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un código de Reforma");
            return;
        }
        if (reformaTraspaso.getPeriodo() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un Período");
            return;
        }
        reformaTraspaso = reformaTraspasoService.findByNamedQuery1("ReformaTraspaso.findById", reformaTraspaso.getId());
        editar = true;
        editarObserv = true;
        bolReformaT1 = Boolean.FALSE;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.FALSE;
        btnGenerarReforma = Boolean.TRUE;
        formBusqRegistrar = Boolean.TRUE;
        formBusqConsultar = Boolean.TRUE;
        formConsultaReforma = Boolean.FALSE;
        btnGenerarReforma = Boolean.TRUE; //Deshabilitar
        btnTraspasos = true;
        if (reformaTraspaso.getEstadoReformaTramite() == null) {
            btnGenerarReforma = Boolean.FALSE;
        } else if (reformaTraspaso.getEstadoReformaTramite().getCodigo().equals("OBS-REF")) {
            btnGenerarReforma = Boolean.TRUE;
        } else if (reformaTraspaso.getEstadoReformaTramite().getCodigo().equals("REG-REF")) {
            btnGenerarReforma = Boolean.TRUE;
        }
        try {
            idUnidadA = reformaTraspaso.getUnidadRequiriente().getId();
        } catch (Exception e) {
            JsfUtil.addWarningMessage("ERROR", "No tiene una unidad");
        }
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }

        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        btnNuevoPapp = Boolean.FALSE;
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
        reformaTraspaso.setFechaModificacion(new Date());
        reformaTraspasoService.edit(reformaTraspaso);
        listaProducto = reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        for (Producto producto1 : listaProducto) {
            BigDecimal saldoDisponible = producto1.getMonto().subtract(producto1.getReserva());
            producto1.setSaldoDisponible(saldoDisponible);
        }
        actualizarTotales();
    }

    public void cargarDatosTablaGeneralPlanAnual() {
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }
        productoLaz = new ProductoLazy(busqueda);
        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        PrimeFaces.current().ajax().update("formTablaReforma");
    }

    /*Funcion que me permite filtrar los valores null que estan presenten en la tabla "GENERAL DE LOS PLANES ANUALES" como "NO APLICA"*/
    public void filtroDeDatosNullPlanesAnualesAsignacionPartida() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":dataTReformaT2");
            filtroDatosNullTablaPlanesAnuales = true;
        } else {
            if (!filtroDatosNullTablaPlanesAnuales) {
                productoLaz = new ProductoLazy(!filtroDatosNullTablaPlanesAnuales, busqueda);
                PrimeFaces.current().ajax().update("formTablaReforma");
            } else {
                productoLaz = new ProductoLazy(busqueda);
                PrimeFaces.current().ajax().update("formTablaReforma");
            }
        }
    }

    public void resetValuesReforma() {
        if (reformaTraspaso.getId() != null) {
            busqueda.setAnio(reformaTraspaso.getPeriodo());
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
            System.out.println("reforma " + reformaTraspaso.getPeriodo());
            System.out.println("reforma " + reformaTraspaso);
            for (Producto producto1 : listaProducto) {
                System.out.println("item " + producto1.getDescripcion());
            }
            if (!listaProducto.isEmpty()) {
                CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                List<PlanLocalProgramaProyecto> planLocalListC = reformaTraspasoService.getReformaPlanLocalProgramaProyecto(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<PlanAnualPoliticaPublica> planAnualPoliticaListC = reformaTraspasoService.getReformaPlanPoliticaPublica(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<PlanAnualProgramaProyecto> planAnualProgramaListC = reformaTraspasoService.getReformaPlanAnulaProgramaProyecto(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<ActividadOperativa> actividadOperativaListC = reformaTraspasoService.getReformaActividadOperativa(busqueda.getAnio(), estado2, true, idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
                for (Producto producto1 : listaProducto) {
                    productoService.remove(producto1);
                }
                List<Producto> listTem = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
                for (Producto producto1 : listTem) {
                    System.out.println("item " + producto1.getDescripcion());
                }
                for (ActividadOperativa actividadOperativa : actividadOperativaListC) {
                    actividadOperativaService.remove(actividadOperativa);
                }
                for (PlanAnualProgramaProyecto planAnualProgramaProyecto1 : planAnualProgramaListC) {
                    planAnualProgramaProyectoService.remove(planAnualProgramaProyecto1);
                }
                for (PlanAnualPoliticaPublica planAnualPoliticaPublica1 : planAnualPoliticaListC) {
                    planAnualPoliticaPublicaService.remove(planAnualPoliticaPublica1);
                }
                for (PlanLocalProgramaProyecto planLocalProgramaProyecto1 : planLocalListC) {
                    planLocalProgramaProyectoService.remove(planLocalProgramaProyecto1);
                }
                this.totalSaldoDisponible = 0;
                this.totalTraspasoReduccion = 0;
                this.totalTraspasoIncremento = 0;
                this.totalMontoReformado = 0;
                listaProducto = new ArrayList<>();
                btnNuevoPapp = true;
                registraNuevoPapp = false;
                ocultarMostarPlanNacional = false;
                btnConsultar = true;
                JsfUtil.addInformationMessage("PAPP", "Reforma del PAPP Eliminada Correctamente");
                PrimeFaces.current().ajax().update("formTablaReforma:dataTReformaT2");
            }
//                            break;
//                        case "PD":
            listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaDistributivo.isEmpty()) {
                for (Distributivo distributivo : listaDistributivo) {
                    this.listaRubros = reformaTraspasoService.listaPresupuestoPartidasReforma(distributivo, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                    for (PartidasDistributivo partidaDis : listaRubros) {
                        partidasDistributivoService.remove(partidaDis);
                    }
                }
                this.totalSaldoDisponibleDGeneral = 0;
                this.totalTraspasoReduccionDGeneral = 0;
                this.totalTraspasoIncrementoDGeneral = 0;
                this.totalMontoReformadoDGeneral = 0;
                listaDistributivo = new ArrayList<>();
                JsfUtil.addInformationMessage("Partida Distributivo General", "Reforma del Distributivo General Cancelada Correctamente");
            }
//                            break;
//                        case "PDI":

            proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
            proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
            proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));

            listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                for (ProformaPresupuestoPlanificado proformaPresupuestoPlanificado1 : listaProformaPresupuestoPlanificado) {
                    proformaPresupuestoPlanificadoService.remove(proformaPresupuestoPlanificado1);
                }
                proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
                btnNuevoPartidaDirecta = true;
                this.totalSaldoDisponiblePDirecta = 0;
                this.totalTraspasoIncrementoPDirecta = 0;
                this.totalTraspasoReduccionPDirecta = 0;
                this.totalMontoReformadoPDirecta = 0;
                JsfUtil.addInformationMessage("Partida Directa", "Reforma de Partidas Directas Cancelada Correctamente");

            }
//                            break;
//                        case "PDA":
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaPartidaDistributivoAnexo.isEmpty()) {
                for (PartidasDistributivoAnexo partidasDistributivoAnexo : listaPartidaDistributivoAnexo) {
                    partidasDistributivoAnexoService.remove(partidasDistributivoAnexo);
                }
                listaPartidaDistributivoAnexo = new ArrayList<>();
                this.totalSaldoDisponibleDAnexo = 0;
                this.totalTraspasoReduccionDAnexo = 0;
                this.totalTraspasoIncrementoDAnexo = 0;
                this.totalMontoReformadoDAnexo = 0;
                JsfUtil.addInformationMessage("Partida Distributivo Anexo", "Reforma del Distributivo Anexo Cancelada Correctamente");
            }
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
            List<DetalleReformaTraspaso> listDetRefTraspaso = detalleReformaTraspasoService.getListDetalleReformaTraspasoByReforma(reformaTraspaso);
            if (!listDetRefTraspaso.isEmpty()) {
                for (DetalleReformaTraspaso detalleRef : listDetRefTraspaso) {
                    detalleReformaTraspasoService.remove(detalleRef);
                }
            }
            reformaTraspasoService.remove(reformaTraspaso);
            JsfUtil.addInformationMessage("Reforma", "Reforma Eliminada Correctamente");
            btnGenerarReforma = false;
            limpiarReforma();
        } else {
            limpiarReforma();
            listaProducto = new ArrayList<>();
            btnNuevoPapp = true;
            registraNuevoPapp = false;
            ocultarMostarPlanNacional = false;
            btnConsultar = true;
            this.totalSaldoDisponible = 0;
            this.totalTraspasoReduccion = 0;
            this.totalTraspasoIncremento = 0;
            this.totalMontoReformado = 0;
        }
    }

    public void limpiarReforma() {
        booleanosReset();
        reformaTraspaso = new ReformaTraspaso();
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        actividad = new ActividadOperativa();
        busqueda = new OpcionBusqueda();
        producto = new Producto();
        proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        setIndiceDeAcordian(-1);
        short anio = Utils.getAnio(new Date()).shortValue();
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
    }

    public void getSetPeriodoReforma() {
        reformaTraspaso.setPeriodo(busqueda.getAnio());
    }

    public void generarReforma(boolean tipo) {
        if (idUnidadA == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Unidad Administrativa");
            return;
        }
        if (reformaTraspaso.getFechaTraspasoReforma() == null) {
            JsfUtil.addErrorMessage("AVISO!!!", "Seleccione la fecha de la reforma");
            return;
        }
        bolReformaT1 = Boolean.FALSE;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.FALSE;
        btnConsultar = false;
        listaProducto = new ArrayList<>();
        listaDistributivo = new ArrayList<>();
        listaPartidaDistributivoAnexo = new ArrayList<>();
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        if (reformaTraspaso.getId() == null) {
            this.reformaTraspaso.setNumeracion(reformaTraspasoService.maxNumeracion(tipo, busqueda.getAnio()));
            String codigos = Utils.completarCadenaConCeros(reformaTraspaso.getNumeracion().toString(), 3);
            this.reformaTraspaso.setCodigo("T2-" + codigos + "-" + reformaTraspaso.getPeriodo());
            CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
            reformaTraspaso.setEstadoReforma(estadoReform);
            if (busqueda.getAnio() == null) {
                short anio = 0;
                busqueda.setAnio(anio);
            }
            if (tipoTraspaso != null) {
                if (tipoTraspaso.equals("PAPP")) {
                    listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
                    System.out.println("id unidad " + idUnidadA);
                    listaProducto = reformaTraspasoService.getListProducto(busqueda.getAnio(), idUnidadA, "AP", true);
                    btnNuevoPapp = Boolean.FALSE;
                }
            }
            UnidadAdministrativa unidad = reformaTraspasoService.getUnidadById(idUnidadA);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
            reformaTraspaso.setUnidadRequiriente(unidad);
            reformaTraspaso.setTipo(tipo);
            reformaTraspaso.setEstado(Boolean.TRUE);
            reformaTraspaso.setUsuarioCreacion(userSession.getNameUser());
            reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
            reformaTraspaso.setFechaCreacion(new Date());
            reformaTraspaso.setFechaModificacion(new Date());
            reformaTraspaso.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            reformaTraspaso = reformaTraspasoService.create(reformaTraspaso);
        }
        if (null != tipoTraspaso) {
            switch (tipoTraspaso) {
                case "PAPP":
                    if (idUnidadA == null) {
                        JsfUtil.addErrorMessage("ERROR", "Ingrese una Unidad Administrativa");
                        return;
                    }
                    panelDistributivo = false;
                    bolPapp = Boolean.TRUE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                    List<ActividadOperativa> actividadOperativaListC = reformaTraspasoService.getReformaActividadOperativa(reformaTraspaso.getPeriodo(), estadoReform, true, idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
                    if (!actividadOperativaListC.isEmpty()) {
                        JsfUtil.addErrorMessage("ERROR", "Ya existe reforma de aquella Unidad");
                        return;
                    }
                    CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
                    crearPapp(reformaTraspaso.getPeriodo(), estado, true, reformaTraspaso.getId());
                    listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId())); 
                    //reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId())); ENRIQUE
                    for (Producto producto1 : listaProducto) {
                        BigDecimal saldoDisponible = producto1.getMonto().subtract(producto1.getReserva());
                        producto1.setSaldoDisponible(saldoDisponible);
                    }
                    actualizarTotales();
                    btnGenerarReforma = true;
                    PrimeFaces.current().ajax().update("formTablaReforma:dataTReformaT2");
                    break;
                case "PD":
                    panelDistributivo = true;
                    bolDistributivoGeneral = Boolean.TRUE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    //listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                    listaCargosRubros = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(reformaTraspaso.getId()));
                    if (!listaCargosRubros.isEmpty()) {
                        btnGenerarReforma = true;
                    } else {
                        creacionDistributivoCompleto(busqueda.getAnio(), reformaTraspaso);
                        listaCargosRubros = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(reformaTraspaso.getId()));
                        if (!listaCargosRubros.isEmpty()) {
                            btnGenerarReforma = true;
                        } else {
                            btnGenerarReforma = false;
                        }
                    }
                    listaCargos = reformaTraspasoService.getThCargoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
                    actualizarTotalesDistributivo();
//                    listaDistributivo.sort(Comparator.comparing(Distributivo::getId));
                    break;
                case "PDA":
                    panelDistributivo = false;
                    bolDistributivoAnexo = Boolean.TRUE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolPartidaDirecta = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    listaCargosRubros = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(reformaTraspaso.getId()));
                    if (!listaCargosRubros.isEmpty()) {
                        btnGenerarReforma = true;
                    } else {
                        creacionDistributivoCompleto(busqueda.getAnio(), reformaTraspaso);
                        listaCargosRubros = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(reformaTraspaso.getId()));
                        if (!listaCargosRubros.isEmpty()) {
                            btnGenerarReforma = true;
                        } else {
                            btnGenerarReforma = false;
                        }
                    }
                    actualizarTotalesDistributivoAnexo();
                    break;
                case "PDI":
                    panelDistributivo = false;
                    bolPartidaDirecta = Boolean.TRUE;
                    bolDistributivoAnexo = Boolean.FALSE;
                    bolDistributivoGeneral = Boolean.FALSE;
                    bolPapp = Boolean.FALSE;
                    listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                        btnGenerarReforma = true;
                    } else {
                        crearPartidaDirecta(reformaTraspaso);
                        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                        if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                            btnGenerarReforma = true;
                        } else {
                            btnGenerarReforma = false;
                        }
                    }
                    actualizarTotalesPartidaDirecta();
                    break;
                default:
                    break;
            }
        }
    }

    public void crearPapp(Short periodo, CatalogoItem c, Boolean estado, Long num) {
        try {
            List<ActividadOperativa> actividadOperativaList = actividadOperativaService.getReformaActividadOperativaByUnidad(periodo, c, estado, idUnidadA);
            List<Producto> productoList = reformaTraspasoService.getListProducto(periodo, idUnidadA, "AP", estado);
            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
            if (actividadOperativaList != null && !actividadOperativaList.isEmpty()) {
                for (ActividadOperativa data4 : actividadOperativaList) {
                    this.actividad = new ActividadOperativa();
                    this.actividad = Utils.clone(data4);
                    this.actividad.setId(null);
                    this.actividad.setEstado(true);
                    this.actividad.setFechaCreacion(data4.getFechaCreacion());
                    this.actividad.setUsuarioCreacion(data4.getUsuarioCreacion());
                    this.actividad.setFechaModificacion(data4.getFechaModificacion());
                    this.actividad.setUsuarioModifica(data4.getUsuarioModifica());
                    this.actividad.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                    this.actividad.setNumeroOrdenId(BigInteger.valueOf(data4.getId()));
                    this.actividad.setNumeroTramite(null);
                    this.actividad.setMonotReformado(data4.getMonotReformado());
                    this.actividad.setNumeroTramite(tramite.getNumTramite().shortValue());
                    this.actividad.setEstadoPapp(estado2);
                    this.actividad = actividadOperativaService.create(actividad);
                    this.actividad = new ActividadOperativa();
                }
            }
            if (productoList != null && !productoList.isEmpty()) {
                for (Producto data5 : productoList) {
                    ActividadOperativa actividads = actividadOperativaService.showActividadByTraspaso(BigInteger.valueOf(num), BigInteger.valueOf(data5.getActividadOperativa().getId()));
                    this.producto = new Producto();
                    this.producto = Utils.clone(data5);
                    this.producto.setId(null);
                    this.producto.setEstado(true);
                    this.producto.setMonto(data5.getMontoReformada());
                    this.producto.setActividadOperativa(actividads);
                    this.producto.setFechaCreacion(data5.getFechaCreacion());
                    this.producto.setUsuarioCreacion(data5.getUsuarioCreacion());
                    this.producto.setFechaModificacion(data5.getFechaModificacion());
                    this.producto.setUsuarioModifica(data5.getUsuarioModifica());
                    this.producto.setPeriodo(data5.getPeriodo());
                    this.producto.setSaldoDisponible(producto.getMonto().subtract(producto.getReserva()));
                    this.producto.setTraspasoIncremento(BigDecimal.ZERO);
                    this.producto.setTraspasoReduccion(BigDecimal.ZERO);
                    this.producto.setSuplementoEgreso(BigDecimal.ZERO);
                    this.producto.setReduccionEgreso(BigDecimal.ZERO);
                    this.producto.setMontoReformada(data5.getMontoReformada());
                    this.producto.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                    this.producto.setNumeroOrdenId(BigInteger.valueOf(data5.getId()));
                    this.producto.setNumeroTramite(tramite.getNumTramite().shortValue());
                    this.producto.setEstadoPapp(estado2);
                    this.producto = productoService.create(producto);
                    this.producto = new Producto();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error " + e);
        }
    }

    public void crearPappOld(Short periodo, CatalogoItem c, Boolean estado, Long num) { //ENRIQUE
        try {
            List<PlanLocalProgramaProyecto> planLocalList = actividadOperativaService.getReformaPlanLocalProgramaProyecto(periodo, c, estado);
            List<PlanAnualPoliticaPublica> planAnualPoliticaList = actividadOperativaService.getReformaPlanPoliticaPublica(periodo, c, estado);
            List<PlanAnualProgramaProyecto> planAnualProgramaList = actividadOperativaService.getReformaPlanAnulaProgramaProyecto(periodo, c, estado);
            List<ActividadOperativa> actividadOperativaList = actividadOperativaService.getReformaActividadOperativaByUnidad(periodo, c, estado, idUnidadA);
//            List<Producto> productoList = actividadOperativaService.getReformaProducto(periodo, c, estado);
            List<Producto> productoList = reformaTraspasoService.getListProducto(periodo, idUnidadA, "AP", estado);

            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");

            /*Verificar si ya existe duplicación*/
            List<PlanLocalProgramaProyecto> planLocalListC = reformaTraspasoService.getReformaPlanLocalProgramaProyecto(periodo, estado2, estado, BigInteger.valueOf(num));
            List<PlanAnualPoliticaPublica> planAnualPoliticaListC = reformaTraspasoService.getReformaPlanPoliticaPublica(periodo, estado2, estado, BigInteger.valueOf(num));
            List<PlanAnualProgramaProyecto> planAnualProgramaListC = reformaTraspasoService.getReformaPlanAnulaProgramaProyecto(periodo, estado2, estado, BigInteger.valueOf(num));
            List<ActividadOperativa> actividadOperativaListC = reformaTraspasoService.getReformaActividadOperativa(periodo, estado2, estado, idUnidadA, BigInteger.valueOf(num));
            if (planLocalListC.isEmpty()) {
                System.out.println("Lista plan local programa proyecto list vacia");
            }
            if (planAnualPoliticaListC.isEmpty()) {
                System.out.println("Lista plan anual politica list vacia");
            }
            if (planAnualProgramaListC.isEmpty()) {
                System.out.println("Lista plan anual programa list vacia");
            }
            if (planLocalListC.isEmpty() && planAnualProgramaListC.isEmpty() && planAnualPoliticaListC.isEmpty()) {

                for (PlanLocalProgramaProyecto data1 : planLocalList) {
                    this.planLocalProgramaProyecto.setId(null);
                    this.planLocalProgramaProyecto.setCodigo(null);
                    this.planLocalProgramaProyecto.setPolitica(data1.getPolitica());
                    this.planLocalProgramaProyecto.setPeriodo(data1.getPeriodo());
                    this.planLocalProgramaProyecto.setDescripcion(data1.getDescripcion());
                    this.planLocalProgramaProyecto.setFechaVigencia(data1.getFechaVigencia());
                    this.planLocalProgramaProyecto.setFechaCaducidad(data1.getFechaCaducidad());
                    this.planLocalProgramaProyecto.setEstado(true);
                    this.planLocalProgramaProyecto.setFechaCreacion(data1.getFechaCreacion());
                    this.planLocalProgramaProyecto.setFechaModificacion(data1.getFechaModificacion());
                    this.planLocalProgramaProyecto.setUsuarioCreacion(data1.getUsuarioCreacion());
                    this.planLocalProgramaProyecto.setUsuarioModifica(data1.getUsuarioModifica());
                    this.planLocalProgramaProyecto.setCodigoReforma(null);
                    this.planLocalProgramaProyecto.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                    this.planLocalProgramaProyecto.setNumeroTramite(null);
                    this.planLocalProgramaProyecto.setNumeroOrdenId(BigInteger.valueOf(data1.getId()));

                    this.planLocalProgramaProyecto.setEstadoPapp(estado2);
                    this.planLocalProgramaProyecto = planLocalProgramaProyectoService.create(planLocalProgramaProyecto);
                    this.planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                }

                for (PlanAnualPoliticaPublica data2 : planAnualPoliticaList) {
                    PlanLocalProgramaProyecto planLocal = actividadOperativaService.showPlanLocalByTraspaso(BigInteger.valueOf(num), BigInteger.valueOf(data2.getPlanLocal().getId()));

                    this.planAnualPoliticaPublica.setId(null);
                    this.planAnualPoliticaPublica.setPoliticaNacional(data2.getPoliticaNacional());
                    this.planAnualPoliticaPublica.setPlanLocal(planLocal);
                    this.planAnualPoliticaPublica.setPeriodo(data2.getPeriodo());
                    this.planAnualPoliticaPublica.setEstado(true);
                    this.planAnualPoliticaPublica.setFechaCreacion(data2.getFechaCreacion());
                    this.planAnualPoliticaPublica.setUsuarioCreacion(data2.getUsuarioCreacion());
                    this.planAnualPoliticaPublica.setUsuarioModifica(data2.getUsuarioModifica());
                    this.planAnualPoliticaPublica.setFechaModificacion(data2.getFechaModificacion());
                    this.planAnualPoliticaPublica.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                    this.planAnualPoliticaPublica.setNumeroOrdenId(BigInteger.valueOf(data2.getId()));
                    this.planAnualPoliticaPublica.setNumeroTramite(null);
                    this.planAnualPoliticaPublica.setEstadoPapp(estado2);
                    this.planAnualPoliticaPublica = planAnualPoliticaPublicaService.create(planAnualPoliticaPublica);
                    this.planAnualPoliticaPublica = new PlanAnualPoliticaPublica();
                }

                for (PlanAnualProgramaProyecto data3 : planAnualProgramaList) {

                    if (data3.getPlanAnual() == null) {
                        this.planAnualProgramaProyecto.setPlanAnual(null);
                    } else {
                        PlanAnualPoliticaPublica planPolitica = actividadOperativaService.showPlanPoliticaPublicaByTraspaso(BigInteger.valueOf(num), BigInteger.valueOf(data3.getPlanAnual().getId()));
                        this.planAnualProgramaProyecto.setPlanAnual(planPolitica);
                    }
                    this.planAnualProgramaProyecto.setId(null);
                    this.planAnualProgramaProyecto.setPeriodo(data3.getPeriodo());
                    this.planAnualProgramaProyecto.setNombreProgramaProyecto(data3.getNombreProgramaProyecto());
                    this.planAnualProgramaProyecto.setEstado(true);
                    this.planAnualProgramaProyecto.setFechaCreacion(data3.getFechaCreacion());
                    this.planAnualProgramaProyecto.setUsuarioCreacion(data3.getUsuarioCreacion());
                    this.planAnualProgramaProyecto.setFechaModificacion(data3.getFechaModificacion());
                    this.planAnualProgramaProyecto.setUsuarioModifica(data3.getUsuarioModifica());
                    this.planAnualProgramaProyecto.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                    this.planAnualProgramaProyecto.setNumeroOrdenId(BigInteger.valueOf(data3.getId()));
                    this.planAnualProgramaProyecto.setEstadoPapp(estado2);
                    this.planAnualProgramaProyecto.setNumeroTramite(null);
                    this.planAnualProgramaProyecto = planAnualProgramaProyectoService.create(planAnualProgramaProyecto);
                    this.planAnualProgramaProyecto = new PlanAnualProgramaProyecto();

                }
            }

            for (ActividadOperativa data4 : actividadOperativaList) {
                PlanAnualProgramaProyecto planAnualPrograma = actividadOperativaService.showPlanAnualProgramaByTraspaso(BigInteger.valueOf(num), BigInteger.valueOf(data4.getPlanProgramaProyecto().getId()));
                this.actividad.setId(null);
                this.actividad.setPlanProgramaProyecto(planAnualPrograma);
                this.actividad.setUnidadResponsable(data4.getUnidadResponsable());
                this.actividad.setNombreActividad(data4.getNombreActividad());
                this.actividad.setObjetivoOperativo(data4.getObjetivoOperativo());
                this.actividad.setMedicionMeta(data4.getMedicionMeta());
                this.actividad.setMedicionPorcentaje(data4.getMedicionPorcentaje());
                this.actividad.setDescripcionMeta(data4.getDescripcionMeta());
                this.actividad.setCuatrimestre1Meta(data4.getCuatrimestre1Meta());
                this.actividad.setCuatrimestre1Logrado(data4.getCuatrimestre1Logrado());
                this.actividad.setCuatrimestre2Meta(data4.getCuatrimestre2Meta());
                this.actividad.setCuatrimestre2Logrado(data4.getCuatrimestre2Logrado());
                this.actividad.setCuatrimestre3Meta(data4.getCuatrimestre3Meta());
                this.actividad.setCuatrimestre3Logrado(data4.getCuatrimestre3Logrado());
                this.actividad.setIndicadorMeta(data4.getIndicadorMeta());
                this.actividad.setMonto(data4.getMonto());
                this.actividad.setEstado(true);
                this.actividad.setFechaCreacion(data4.getFechaCreacion());
                this.actividad.setUsuarioCreacion(data4.getUsuarioCreacion());
                this.actividad.setFechaModificacion(data4.getFechaModificacion());
                this.actividad.setUsuarioModifica(data4.getUsuarioModifica());
                this.actividad.setPeriodo(data4.getPeriodo());
                this.actividad.setCuatrimestre1Actividad(data4.getCuatrimestre1Actividad());
                this.actividad.setCuatrimestre2Actividad(data4.getCuatrimestre2Actividad());
                this.actividad.setCuatrimestre3Actividad(data4.getCuatrimestre3Actividad());
                this.actividad.setCr1(data4.getCr1());
                this.actividad.setCr2(data4.getCr2());
                this.actividad.setCr3(data4.getCr3());
                this.actividad.setMonotReformado(data4.getMonotReformado());
                this.actividad.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                this.actividad.setNumeroOrdenId(BigInteger.valueOf(data4.getId()));
                this.actividad.setNumeroTramite(null);
                this.actividad.setEstadoPapp(estado2);
                this.actividad = actividadOperativaService.create(actividad);
                this.actividad = new ActividadOperativa();
            }

            for (Producto data5 : productoList) {
                ActividadOperativa actividads = actividadOperativaService.showActividadByTraspaso(BigInteger.valueOf(num), BigInteger.valueOf(data5.getActividadOperativa().getId()));
                this.producto.setId(null);
                this.producto.setDescripcion(data5.getDescripcion());
                this.producto.setMonto(data5.getMontoReformada());
                this.producto.setItemPresupuestario(data5.getItemPresupuestario());
                this.producto.setEstructuraProgramatica(data5.getEstructuraProgramatica());
                this.producto.setFuente(data5.getFuente());
                this.producto.setEstado(true);
                this.producto.setActividadOperativa(actividads);
                this.producto.setFechaCreacion(data5.getFechaCreacion());
                this.producto.setUsuarioCreacion(data5.getUsuarioCreacion());
                this.producto.setFechaModificacion(data5.getFechaModificacion());
                this.producto.setUsuarioModifica(data5.getUsuarioModifica());
                this.producto.setCodigoPresupuestario(data5.getCodigoPresupuestario());
                this.producto.setPeriodo(data5.getPeriodo());
                this.producto.setFuenteDirecta(data5.getFuenteDirecta());
                this.producto.setReserva(data5.getReserva());
                this.producto.setSaldoDisponible(producto.getMonto().subtract(data5.getReserva()));
                this.producto.setTraspasoIncremento(BigDecimal.ZERO);
                this.producto.setTraspasoReduccion(BigDecimal.ZERO);
                this.producto.setSuplementoEgreso(BigDecimal.ZERO);
                this.producto.setReduccionEgreso(BigDecimal.ZERO);
                this.producto.setMontoReformada((data5.getMonto().add(data5.getTraspasoIncremento().add(data5.getSuplementoEgreso()))).subtract(data5.getTraspasoReduccion().add(data5.getReduccionEgreso())));
                this.producto.setCodigoReformaTraspaso(BigInteger.valueOf(num));
                this.producto.setNumeroOrdenId(BigInteger.valueOf(data5.getId()));
                this.producto.setNumeroTramite(null);
                this.producto.setEstadoPapp(estado2);
                this.producto = productoService.create(producto);
                this.producto = new Producto();

            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error " + e);
        }

    }

    public void creacionDistributivoCompleto(Short periodo, ReformaTraspaso r) {

        List<ThCargoRubros> distributivos = thCargoRubrosService.copiaReforma(Boolean.TRUE, periodo);
        List<ThCargoRubros> verificarList = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(r.getId()));

        if (verificarList.isEmpty()) {
            if (!distributivos.isEmpty()) {
                for (ThCargoRubros item : distributivos) {
                    ThCargoRubros data = new ThCargoRubros();
                    data = Utils.clone(item);
                    data.setId(null);
                    data.setNumtramite(tramite.getId());
                    data.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                    data.setCodigoReformaTraspaso(BigInteger.valueOf(r.getId()));
                    data.setMonto(item.getReformaCodificado());
                    data.setReformaCodificado(data.getMonto());
                    data.setReformaSuplemento(BigDecimal.ZERO);
                    data.setReformaReduccion(BigDecimal.ZERO);
                    data.setTraspasoIncremento(BigDecimal.ZERO);
                    data.setTraspasoReduccion(BigDecimal.ZERO);
                    data.setFechaCreacion(new Date());
                    data.setFechaModificacion(new Date());
                    data.setUsuarioCreacion(userSession.getNameUser());
                    data.setUsuarioModificacion(userSession.getNameUser());
                    thCargoRubrosService.create(data);
                    data = new ThCargoRubros();

                }
            }
        }
        System.out.println("DISTRIBUTIVO AND DISTRIBUTIVO ANEXO CREATED...!");
    }

    public void crearPartidaDistributivoGeneralOld(ReformaTraspaso r) {
        this.reformaTraspaso = new ReformaTraspaso();
        this.reformaTraspaso = r;
        busqueda.setAnio(r.getPeriodo());
        this.listaValoresDistributivo = new ArrayList<>();
//        copiaDiReforma = new ArrayList<>();
        listaDistributivo = new ArrayList<>();
        List<PartidasDistributivo> verificarList = valoresService.verificarReformaTraspasoDis(BigInteger.valueOf(r.getId()));
        if (verificarList.isEmpty()) {

            listaValoresDistributivo = valoresService.getObjetoDistributivo(busqueda.getAnio());
            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
            if (listaValoresDistributivo.size() > 0) {
                for (ValoresDistributivo item : listaValoresDistributivo) {
                    partidaDistributivo = new PartidasDistributivo();
                    partidaDistributivo.setDistributivo(item);
                    partidaDistributivo.setEstado(Boolean.TRUE);
                    partidaDistributivo.setPeriodo(busqueda.getAnio());
                    partidaDistributivo.setUsuarioCreacion(userSession.getName());
                    partidaDistributivo.setFechaCreacion(new Date());
                    partidaDistributivo.setUsuarioModificacion(userSession.getName());
                    partidaDistributivo.setFechaModificacion(new Date());
                    partidaDistributivo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivo.setMonto(item.getValorResultante());
                    partidaDistributivo.setReformaCodificado(item.getValorResultante());
                    partidaDistributivo = partidasDistributivoService.create(partidaDistributivo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivo> copiaRubrosPartidasReforma = valoresService.getCopiaReforma(busqueda.getAnio(), estadoAprobado);
            CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RTD");

            if (!copiaRubrosPartidasReforma.isEmpty()) {
                PartidasDistributivo item = new PartidasDistributivo();
                for (PartidasDistributivo data : copiaRubrosPartidasReforma) {
                    item.setDistributivo(data.getDistributivo());
                    item.setCodigoReferencia(BigInteger.valueOf(data.getId()));
                    item.setCodigoReformaTraspaso(BigInteger.valueOf(r.getId()));
                    item.setEstado(data.isEstado());
                    item.setEstadoPartida(estado);
                    item.setEstructuraAp(data.getEstructuraAp());
                    item.setItemAp(data.getItemAp());
                    item.setFechaCreacion(new Date());
                    item.setFechaModificacion(new Date());
                    item.setFuenteAp(data.getFuenteAp());
                    item.setFuenteDirecta(data.getFuenteDirecta());
                    item.setPartidaAp(data.getPartidaAp());
                    item.setPeriodo(data.getPeriodo());
                    item.setUsuarioCreacion(userSession.getNameUser());
                    item.setUsuarioModificacion(userSession.getNameUser());
                    item.setReformaSuplemento(BigDecimal.ZERO);
                    item.setReformaReduccion(BigDecimal.ZERO);
                    item.setTraspasoIncremento(BigDecimal.ZERO);
                    item.setTraspasoReduccion(BigDecimal.ZERO);
                    item.setReformaCodificado(data.getReformaCodificado());
                    item.setMonto(data.getReformaCodificado());
                    item = partidasDistributivoService.create(item);
                    item = new PartidasDistributivo();
                }

            }
        }
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(busqueda.getAnio(), BigInteger.valueOf(r.getId()));
        panelDistributivo = true;
    }

    public void crearPartidaDistributivoAnexo(ReformaTraspaso r) {
//        cargarDatosGenerarPartida();
        listaDistributivoAnexo = new ArrayList<>();

        List<PartidasDistributivoAnexo> listavVerificacion = null;//partidasDistributivoAnexoService.getPartidasAnexoReforma(BigInteger.valueOf(r.getId()));
        if (listavVerificacion.size() < 1) {
            listaDistributivoAnexo = distributivoAnexoService.getDisAnexosNoExistInPartidaAnexo(busqueda.getAnio());

            CatalogoItem estadoRegistarAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RD");
            if (!listaDistributivoAnexo.isEmpty()) {
                for (DistributivoAnexo item : listaDistributivoAnexo) {
                    partidaDistributivoAnexo = new PartidasDistributivoAnexo();
                    partidaDistributivoAnexo.setDistributivoAnexo(item);
                    partidaDistributivoAnexo.setEstado(Boolean.TRUE);
                    partidaDistributivoAnexo.setPeriodo(busqueda.getAnio());
                    partidaDistributivoAnexo.setUsuarioCreacion(userSession.getName());
                    partidaDistributivoAnexo.setFechaCreacion(new Date());
                    partidaDistributivoAnexo.setFechaModificacion(new Date());
                    partidaDistributivoAnexo.setUsuarioModificacion(userSession.getName());
                    partidaDistributivoAnexo.setReformaSuplemento(BigDecimal.ZERO);
                    partidaDistributivoAnexo.setReformaReduccion(BigDecimal.ZERO);
                    partidaDistributivoAnexo.setTraspasoIncremento(BigDecimal.ZERO);
                    partidaDistributivoAnexo.setTraspasoReduccion(BigDecimal.ZERO);
                    partidaDistributivoAnexo.setEstadoPartida(estadoRegistarAprobado);
                    partidaDistributivoAnexo.setReformaCodificado(item.getMonto());
                    partidaDistributivoAnexo.setMonto(BigDecimal.ZERO);
                    partidaDistributivoAnexo = partidasDistributivoAnexoService.create(partidaDistributivoAnexo);
                }
            }

            CatalogoItem estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "AD");
            List<PartidasDistributivoAnexo> listaPartidasReforma = partidasDistributivoAnexoService.getPartidasAnexo(estadoAprobado, busqueda.getAnio());
            if (!listaPartidasReforma.isEmpty()) {
                PartidasDistributivoAnexo reformaPartida = new PartidasDistributivoAnexo();
                CatalogoItem estadoAprobadoItem = catalogoService.getItemByCatalogoAndCodigo("estado_distributivo", "RTD");
                for (PartidasDistributivoAnexo item : listaPartidasReforma) {
                    reformaPartida.setDistributivoAnexo(item.getDistributivoAnexo());
                    reformaPartida.setEstado(Boolean.TRUE);
                    reformaPartida.setPeriodo(item.getPeriodo());
                    reformaPartida.setUsuarioCreacion(userSession.getName());
                    reformaPartida.setFechaCreacion(item.getFechaCreacion());
                    reformaPartida.setFechaCreacion(new Date());
                    reformaPartida.setUsuarioModificacion(userSession.getName());
                    reformaPartida.setEstadoPartida(estadoAprobadoItem);
                    reformaPartida.setCodigoReformaTraspaso(BigInteger.valueOf(r.getId()));
                    reformaPartida.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                    reformaPartida.setItemApA(item.getItemApA());
                    reformaPartida.setEstructuraApA(item.getEstructuraApA());
                    reformaPartida.setFuenteApA(item.getFuenteApA());
                    reformaPartida.setFuenteDirectaA(item.getFuenteDirectaA());
                    reformaPartida.setPartidaAp(item.getPartidaAp());
                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
                    reformaPartida.setReformaSuplemento(BigDecimal.ZERO);
                    reformaPartida.setTraspasoIncremento(BigDecimal.ZERO);
                    reformaPartida.setTraspasoReduccion(BigDecimal.ZERO);
                    reformaPartida.setMonto(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento().add(item.getTraspasoIncremento())).subtract(item.getReformaReduccion().add(item.getTraspasoReduccion())));
                    reformaPartida.setReformaCodificado(item.getDistributivoAnexo().getMonto().add(item.getReformaSuplemento().add(item.getTraspasoIncremento())).subtract(item.getReformaReduccion().add(item.getTraspasoReduccion())));
                    reformaPartida = partidasDistributivoAnexoService.create(reformaPartida);
                    reformaPartida = new PartidasDistributivoAnexo();

                }

            }
            listaPartidaDistributivoAnexo = new ArrayList<>();
            listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getDisAnexosEstadoFalse(busqueda.getAnio());
            if (!listaPartidaDistributivoAnexo.isEmpty()) {
                for (PartidasDistributivoAnexo item : listaPartidaDistributivoAnexo) {
                    partidasDistributivoAnexoService.remove(item);
                }
            }

        }

        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(r.getId()));

//        if (r.getTipo()) {
//            columnaSuplemento = true;
//            columnaReduccion = false;
//            totalCupoDA = valoresService.getCupoDistributivo(r, "DA");
//            totalCupoAsignado(BigInteger.valueOf(r.getId()));
//        } else {
//            columnaSuplemento = false;
//            columnaReduccion = true;
//            totalCupoDA = BigDecimal.ZERO;
//            cupoAsignado = BigDecimal.ZERO;
//        }
//
//        panelReforma = false;
//        panelData = true;
    }

    public void crearPartidaDirecta(ReformaTraspaso reforma) {
        reformaTraspaso = new ReformaTraspaso();
        reformaTraspaso = reforma;
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        List<ProformaPresupuestoPlanificado> listVerificar = reformaTraspasoService.getListaVerificacion(BigInteger.valueOf(reformaTraspaso.getId()));
        if (listVerificar.isEmpty()) {
            listaProformaPresupuestoPlanificado = reformaTraspasoService.getPartidasDirectasReformas(reformaTraspaso.getPeriodo(), "PDI");
            ProformaPresupuestoPlanificado pro = new ProformaPresupuestoPlanificado();
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                pro.setCodigo(item.getCodigo());
                pro.setCodigoReferencia(BigInteger.valueOf(item.getId()));
                pro.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                pro.setCondicion(item.getCondicion());
                pro.setEstado(item.getEstado());
                pro.setEstructuraProgramatica(item.getEstructuraProgramatica());
                pro.setItemPresupuestario(item.getItemPresupuestario());
                pro.setItemNew(item.getItemNew());
                pro.setEstructruaNew(item.getEstructruaNew());
                pro.setFuenteNew(item.getFuenteNew());
                pro.setFechaCreacion(item.getFechaCreacion());
                pro.setFechaModificacion(item.getFechaModificacion());
                pro.setFuente(item.getFuente());
                pro.setFuenteDirecta(item.getFuenteDirecta());
                pro.setGenerado(pro.getGenerado());
                pro.setCondicion(item.getCondicion());
                pro.setValor(item.getReformaCodificado());
                pro.setReformaSuplemento(BigDecimal.ZERO);
                pro.setReformaReduccion(BigDecimal.ZERO);
                pro.setTraspasoIncremento(BigDecimal.ZERO);
                pro.setTraspasoReduccion(BigDecimal.ZERO);
                pro.setUsuarioModificacion(userSession.getNameUser());
                pro.setPartidaPresupuestaria(item.getPartidaPresupuestaria());
                pro.setReformaCodificado(item.getValor().add(item.getTraspasoIncremento().add(item.getReformaSuplemento())).subtract(item.getTraspasoReduccion().add(item.getReformaReduccion())));
                pro.setPeriodo(reformaTraspaso.getPeriodo());
                pro.setUsuarioCreacion(item.getUsuarioCreacion());
                pro.setGenerado(item.getGenerado());
                pro = proformaPresupuestoPlanificadoService.create(pro);
                pro = new ProformaPresupuestoPlanificado();

            }
        }
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        System.out.println("REFORMA ANIO: " + reformaTraspaso.getPeriodo());
        this.listaFuenteDirecta = fuenteService.findByNamedQuery("FuenteFinanciamiento.findEstadoValido", true, reformaTraspaso.getPeriodo());
        this.listaPlanProgramaticoDirecta = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), reformaTraspaso.getPeriodo());
        this.listaCatalogoPresupuestosDirecta = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), reformaTraspaso.getPeriodo());
        btnNuevoPartidaDirecta = false;
//        cupoAsignado=BigDecimal.ZERO;
//
//        totalCupoPDI=BigDecimal.ZERO;
//        cupoAsignado=BigDecimal.ZERO;
//        if (r.getTipo()) {
//            columnaSuplmento = true;
//            columnaReduccion = false;
//            totalCupoPDI = valoresService.getCupoDistributivo(r, "PD");
//            btnnuevo=false;
//            totalCupoAsignado(BigInteger.valueOf(reformaGlobal.getId()));
//        } else {
//            columnaSuplmento = false;
//            columnaReduccion = true;
//            btnnuevo=true;
//        }
//        panelReforma = false;
//        panelData = true;
    }

    public void editarPartidaDirectaValor(ProformaPresupuestoPlanificado p) {
        proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        proformaPresupuestoPlanificado = p;
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");

    }

    public void abriDlgPartidas() {
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");
    }

    public void listaVisualizacion(Distributivo d, boolean vista) {
        listaVista = new ArrayList<>();
        listaVista = reformaTraspasoService.listaPresupuestoPartidasReforma(d, busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        dataView = d;
        //     setRmu(valoresService.getRMu(d, busqueda.getAnio()));
        if (vista) {
            vistaPartidaDisGeneral = true;
        } else {
            vistaPartidaDisGeneral = false;
        }
        PrimeFaces.current().executeScript("PF('DlgpartidasDistributivosvista').show()");
        PrimeFaces.current().ajax().update(":formDlgpartidasDistributivosvista");
    }

    public void savePdReforma() {
        if (proformaPresupuestoPlanificado.getEstructuraProgramatica() == null && proformaPresupuestoPlanificado.getFuente() == null && proformaPresupuestoPlanificado.getItemPresupuestario() == null && (proformaPresupuestoPlanificado.getValor() == null || proformaPresupuestoPlanificado.getValor() == BigDecimal.ZERO)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Los Campos no deben estar vacios");
            return;

        }

        if (proformaPresupuestoPlanificado.getValor() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Ingrese un valor ");
            return;
        }

        if (proformaPresupuestoPlanificado.getValor().doubleValue() < 1) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Ingrese un valor mayor a cero");
            return;
        }
        if (proformaPresupuestoPlanificado.getId() == null) {

//            BigDecimal result = proformaService.totalSuplemento(BigInteger.valueOf(reformaTraspaso.getId()));
//            //BigDecimal valorActual = proformaService.totalSuplementoActual(proformaPresupuesto);
//            BigDecimal confirma = result.add(proformaPresupuesto.getValor());
//            if (confirma.doubleValue() > totalCupoPDI.doubleValue()) {
//
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addErrorMessage("Error", "No tiene suficiente Cupo");
//                return;
//            }
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructuraProgramatica().getCodigo() + proformaPresupuestoPlanificado.getItemPresupuestario().getCodigo() + proformaPresupuestoPlanificado.getFuente().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuente().getTipoFuente());
            proformaPresupuestoPlanificado.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
            proformaPresupuestoPlanificado.setCondicion(false);
            proformaPresupuestoPlanificado.setGenerado(true);
            proformaPresupuestoPlanificado.setCodigo("PDI");
            proformaPresupuestoPlanificado.setEstado(true);
            proformaPresupuestoPlanificado.setUsuarioCreacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaCreacion(new Date());
            proformaPresupuestoPlanificado.setPeriodo(reformaTraspaso.getPeriodo());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado = proformaPresupuestoPlanificadoService.create(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "El registro se realizo correctamente");
            PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').hide()");
            PrimeFaces.current().ajax().update(":formPresegresoPartidas");

        } else {

//            BigDecimal result = proformaPresupuestoPlanificadoService.totalSuplemento(BigInteger.valueOf(reformaTraspaso.getId()));
//            BigDecimal valorActual = proformaService.totalSuplementoActual(proformaPresupuesto);
//            BigDecimal confirma = result.subtract(valorActual).add(proformaPresupuesto.getValor());
//
//            if (confirma.doubleValue() > totalCupoPDI.doubleValue()) {
//
//                PrimeFaces.current().ajax().update("messages");
//                JsfUtil.addErrorMessage("Error", "No tiene suficiente Cupo");
//                return;
//            }
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructuraProgramatica().getCodigo() + proformaPresupuestoPlanificado.getItemPresupuestario().getCodigo() + proformaPresupuestoPlanificado.getFuente().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuente().getTipoFuente());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificadoService.edit(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "El registro se realizo correctamente");
            PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').hide()");
            PrimeFaces.current().ajax().update(":formPresegresoPartidas");

        }

//        if(reformaTraspaso.getTipo()){
//         totalCupoAsignado(BigInteger.valueOf(reformaTraspaso.getId()));
//        }
    }

    public void nuevoPAPP() {
//        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser()); //ENRIQUE
//        UnidadAdministrativa unid = reformaTraspasoService.getUnidadById(idUnidadA); //ENRIQUE
//        Distributivo d = new Distributivo();//ENRIQUE
//        d.setUnidadAdministrativa(new UnidadAdministrativa(new Long(62), "DIRECCIÓN GENERAL DE SERVICIOS Y ESPACIOS PÚBLICOS", new Date(), true));//ENRIQUE
//        UnidadAdministrativa unid = d.getUnidadAdministrativa();//ENRIQUE
        UnidadAdministrativa unid = unidadAdministrativaService.find(idUnidadA);
//        listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, 3, busqueda.getAnio());
//        listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, 4, busqueda.getAnio());
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        lazyProductos = new ProductoLazy(busqueda, BigInteger.valueOf(reformaTraspaso.getId()), true);
        planAnualazy = new LazyModel(PlanAnualPoliticaPublica.class);
        planAnualazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        planAnualazy.getFilterss().put("periodo", reformaTraspaso.getPeriodo());
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        planAnualProgramaLazy.getFilterss().put("periodo", reformaTraspaso.getPeriodo());
        productoLazy = new LazyModel(Producto.class);
        productoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        productoLazy.getFilterss().put("actividadOperativa.unidadResponsable:equal", unid);
        productoLazy.getFilterss().put("periodo", reformaTraspaso.getPeriodo());
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", unid);
        lazyActividadOperativas.getFilterss().put("periodo", reformaTraspaso.getPeriodo());
        registraNuevoPapp = true;
        programaProyecto = new PlanAnualProgramaProyecto();
//        vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
//        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
        //PrimeFaces.current().ajax().update(":formPlanes");
        //PrimeFaces.current().ajax().update(":proyectos");
        PrimeFaces.current().ajax().update(":actividadesOperativa");
        PrimeFaces.current().ajax().update(":productos");
    }

    /**
     * añadir en memoria para mostrar en un tabla el proyecto a realizar para
     * realizar actividades
     */
    public void savePlanAnualPoliticaPublica() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String fecha = String.valueOf(year);
        boolean edit = planAnual.getId() != null;
        if (busqueda.getAnio() == 0) {
            JsfUtil.addErrorMessage("FALLO REGISTRO", "Debe selecionar un año para poder registrar un Plan Nacionar y un Plan Local");
            setIndiceDeAcordian(0);
            PrimeFaces.current().ajax().update(":formMain");
        } else {
            if (!edit) {
                if (politicaLocalSeleccionado != null && politicaNacionalSeleccionada != null && planLocalProgramaProyecto.getDescripcion() != "") {
                    planLocalProgramaProyecto.setPolitica(politicaLocalSeleccionado);
                    planLocalProgramaProyecto.setCodigo(null);
                    planLocalProgramaProyecto.setPeriodo(busqueda.getAnio());
                    planLocalProgramaProyecto.setFechaVigencia(new Date());
                    planLocalProgramaProyecto.setFechaCaducidad(new Date());
                    planLocalProgramaProyecto.setEstado(Boolean.TRUE);
                    planLocalProgramaProyecto.setFechaCreacion(new Date());
                    planLocalProgramaProyecto.setFechaModificacion(new Date());
                    planLocalProgramaProyecto.setUsuarioCreacion(userSession.getNameUser());
                    planLocalProgramaProyecto.setUsuarioModifica(userSession.getNameUser());
                    planLocalProgramaProyecto.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                    planLocalProgramaProyecto = planLocalProgramaProyectoService.create(planLocalProgramaProyecto);

                    planAnual.setPoliticaNacional(politicaNacionalSeleccionada);
                    planAnual.setPlanLocal(planLocalProgramaProyecto);
                    planAnual.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                    planAnual.setPeriodo(busqueda.getAnio());
                    planAnual.setAsignacionInicial(null);
                    planAnual.setReformas(null);
                    planAnual.setCodificado(null);
                    planAnual.setComprometido(null);
                    planAnual.setDevengado(null);
                    planAnual.setRecaudado(null);
                    planAnual.setEstado(true);
                    planAnual.setFechaCreacion(new Date());
                    planAnual.setFechaModificacion(new Date());
                    planAnual.setUsuarioModifica(userSession.getNameUser());

                    planAnual.setUsuarioCreacion(userSession.getNameUser());
                    planAnual = planAnualPoliticaPublicaService.create(planAnual);
                    setIndiceDeAcordian(1);
                    PrimeFaces.current().ajax().update(":formMain");
                    //PrimeFaces.current().ajax().update(":formPlanes");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addSuccessMessage("PLAN, PROGRAMA, PROYECTO", planLocalProgramaProyecto.getDescripcion() + " HA SIDO REGISTRADO EN EL SISTEMA");
                    resetValues();
                } else {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("PLAN, PROGRAMA, PROYECTO", "TODOS LOS CAMPOS DEBEN ESTAR LLENOS ANTES DE GUARDAR");
                }
            } else {
                planLocalProgramaProyecto.setUsuarioModifica(userSession.getNameUser());
                planLocalProgramaProyecto.setFechaModificacion(new Date());
                planLocalProgramaProyecto.setPolitica(politicaLocalSeleccionado);
                planLocalProgramaProyectoService.edit(planLocalProgramaProyecto);
                planAnual.setPoliticaNacional(politicaNacionalSeleccionada);
                planAnual.setPlanLocal(planLocalProgramaProyecto);
                planAnual.setFechaModificacion(new Date());
                planAnual.setUsuarioModifica(userSession.getNameUser());
                planAnualPoliticaPublicaService.edit(planAnual);

                //PrimeFaces.current().ajax().update(":formPlanes");
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addSuccessMessage("PLAN, PROGRAMA, PROYECTO", planLocalProgramaProyecto.getDescripcion() + " HA SIDO EDITADO CON EXITO");
                resetValues();
                setIndiceDeAcordian(1);
                PrimeFaces.current().ajax().update(":formMain");
            }
        }
    }

    /*Cargar los datos al formulario para editarlos*/
    public void form(PlanAnualPoliticaPublica plan) {
        if (plan != null) {
            this.planAnual = plan;
            this.planLocalProgramaProyecto = plan.getPlanLocal();
            this.ejeNacionalSeleccionado = plan.getPoliticaNacional().getObjetivo().getEje();
            this.objetivoNacionalSeleccionado = plan.getPoliticaNacional().getObjetivo();
            this.objetivosNacionales = planNacionalObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByFiltroPolitica", new Object[]{ejeNacionalSeleccionado});
            this.politicaNacionalSeleccionada = plan.getPoliticaNacional();
            this.politicasNacionales = planNacionalPoliticaService.findByNamedQuery("PlanNacionalPolitica.findFiltrarPolitica", new Object[]{objetivoNacionalSeleccionado});
            this.sistemaLocalSeleccionado = plan.getPlanLocal().getPolitica().getObjetivo().getSistema();
            this.objetivoLocalSeleccionado = plan.getPlanLocal().getPolitica().getObjetivo();
            this.objetivosLocales = planLocalObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByObjetivo", new Object[]{sistemaLocalSeleccionado});
            this.politicaLocalSeleccionado = plan.getPlanLocal().getPolitica();
            this.politicasLocales = planLocalPoliticaService.findByNamedQuery("PlanLocalPolitica.findByPolitica", new Object[]{objetivoLocalSeleccionado});
        }
        setIndiceDeAcordian(0);
        //PrimeFaces.current().ajax().update(":formPlanes");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addWarningMessage("AVISO:", "Se han cargados los datos en la tabla PLAN NACIONAL - PLAN LOCAL - NOMBRE PLAN PROGRAMA PROYECTO");
    }

    private void resetValues() {
        planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
        planAnual = new PlanAnualPoliticaPublica();
        ejeNacionalSeleccionado = new PlanNacionalEje();
        objetivoNacionalSeleccionado = new PlanNacionalObjetivo();
        politicaNacionalSeleccionada = new PlanNacionalPolitica();
        sistemaLocalSeleccionado = new PlanLocalSistema();
        objetivoLocalSeleccionado = new PlanLocalObjetivo();
        politicaLocalSeleccionado = new PlanLocalPolitica();
        objetivosNacionales = null;
        politicasNacionales = null;
        objetivosLocales = null;
        politicasLocales = null;
    }

    /**
     * Este metodo sirve para verificar si el plan nual de proyecto tiene hijo
     * como actividades, productos, planes programa,proyectos, etc.
     *
     * @param a
     * @param b
     * @param p
     * @param opcion
     * @return
     */
    public boolean getHijosByPadre(PlanAnualPoliticaPublica a, PlanAnualProgramaProyecto b, ActividadOperativa p, int opcion) {
        boolean verificacion = false;
        switch (opcion) {
            case 1:
                List<PlanAnualProgramaProyecto> lista1 = planAnualProgramaProyectoService.findByNamedQuery("PlanAnualProgramaProyecto.findVerificarHijos", new Object[]{a.getId()});

                if (lista1.isEmpty()) {
                    verificacion = false;

                } else {
                    verificacion = true;
                }

                break;

            case 2:

                List<ActividadOperativa> lista2 = actividadOperativaService.findByNamedQuery("ActividadOperativa.findByVerificarHijos", new Object[]{b.getId()});
                if (lista2.isEmpty()) {
                    verificacion = false;

                } else {
                    verificacion = true;
                }
                break;

            case 3:
                List<Producto> lista3 = productoService.findByNamedQuery("Producto.findByVerificarHijos", p.getId());
                if (lista3.isEmpty()) {
                    verificacion = false;

                } else {
                    verificacion = true;
                }
                break;

        }
//        return findByNamedQuery("CuentaContable.findByPadre", new Object[]{padre.getId()});
        return verificacion;
    }

    public void eliminarPlanAnual(PlanAnualPoliticaPublica c) {

        if (getHijosByPadre(c, null, null, 1)) {
            PrimeFaces.current().ajax().update("messages");

            JsfUtil.addErrorMessage("Plan Programa Proyecto", "No se puede eliminar porque tiene Planes-Programa-proyecto asociados");
            return;
        }
        PlanLocalProgramaProyecto PLPP = new PlanLocalProgramaProyecto();
        PLPP = c.getPlanLocal();
        planAnualPoliticaPublicaService.remove(c);
        planLocalProgramaProyectoService.remove(PLPP);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Plan Programa Proyecto", c.getPlanLocal().getDescripcion() + "eliminado con éxito");
    }

    /**
     * Actualiza el combo de Objetivo dependiendo de lo que eliga el en eje
     *
     */
    public void actualizarCbObjetivo() {
        this.objetivosNacionales = planNacionalObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByFiltroPolitica", new Object[]{ejeNacionalSeleccionado});

    }

    /**
     * Actualiza el combo de Politica dependiendo de lo que eliga el objetivo
     *
     */
    public void actualizarCbPlotica() {
        this.politicasNacionales = planNacionalPoliticaService.findByNamedQuery("PlanNacionalPolitica.findFiltrarPolitica", new Object[]{objetivoNacionalSeleccionado});

    }

    /**
     * Actualiza el combo de Objetivo dependiendo de lo que eliga el Sistema
     *
     */
    public void actualizarObjetivoLocal() {
        this.objetivosLocales = planLocalObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByObjetivo", new Object[]{sistemaLocalSeleccionado});
    }

    /**
     * Actualiza el combo de Politica dependiendo de lo que eliga el objetivo
     *
     */
    public void actualizarPoliticaLocal() {
        this.politicasLocales = planLocalPoliticaService.findByNamedQuery("PlanLocalPolitica.findByPolitica", new Object[]{objetivoLocalSeleccionado});
    }

    public void añadirPlanAnualPoliticaPublica(PlanAnualPoliticaPublica pa) {
        programaProyecto = new PlanAnualProgramaProyecto();

        programaProyecto.setPlanAnual(pa);

        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
    }

    public void añadirPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {
//        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        UnidadAdministrativa unid = reformaTraspasoService.getUnidadById(idUnidadA);
        actividad.setPlanProgramaProyecto(p);
        actividad.setUnidadResponsable(unid);
        PrimeFaces.current().executeScript("PF('DlgActividad').show()");
        PrimeFaces.current().ajax().update(":formActividad");
    }

    public void editarPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {
        this.programaProyecto = p;
        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
    }

    public void eliminarPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {

        if (getHijosByPadre(null, p, null, 2)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "No se puede eliminar porque tiene actividades operacionales sociales.");
            return;
        }
        planAnualProgramaProyectoService.remove(p);
        PrimeFaces.current().ajax().update(":formPlanProyecto");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "eliminado con éxito.");
    }

    /*Funcion para cambiar y definir el poncentaje en 100%*/
    public void cambiarMedicionPorcentual() {
        BigDecimal medicionMeta = BigDecimal.valueOf(100);
        /*si selecciono que el valor a registrar es porcentual entonces sett el valor de 100*/
        if (actividad.getMedicionPorcentaje()) {
            actividad.setMedicionMeta(medicionMeta);
        } else {
            actividad.setMedicionMeta(null);
        }
    }

    /*Funcion que me permite filtrar los valores null que estan presenten en la tabla "PLAN PROGRAMA PROYECTO" como "NO APLICA"*/
    public void filtroDeDatosNullPlanProgramaProyecto() {
        if (!filtroDatosNullProgramaProyectos) {
            planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
            planAnualProgramaLazy.getFilterss().put("planAnual:equal", null);
            planAnualProgramaLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
            PrimeFaces.current().ajax().update(":datatable2");
        } else {
            planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
            planAnualProgramaLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
            PrimeFaces.current().ajax().update(":planProgramaProyectos");
        }
    }

    public void noAplica() {
        programaProyecto = new PlanAnualProgramaProyecto();
        programaProyecto.setPlanAnual(null);
        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");

    }

    public void savePlanNombreProyecto() {
        if (programaProyecto.getNombreProgramaProyecto() == null) {
            JsfUtil.addWarningMessage("AVISO", "NECESITA INGRESAR O SELECIONAR UN NOMBRE DEL PLAN ANUAL, PROGRAMA, PROYECTO");
        } else {
            boolean edit = programaProyecto.getId() != null;
            if (!edit) {
                programaProyecto.setPeriodo(busqueda.getAnio());
                programaProyecto.setEstado(true);
                programaProyecto.setFechaCreacion(new Date());
                programaProyecto.setFechaModificacion(new Date());
                programaProyecto.setUsuarioCreacion(userSession.getNameUser());
                programaProyecto.setUsuarioModifica(userSession.getNameUser());
                programaProyecto.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                this.programaProyecto = planAnualProgramaProyectoService.create(programaProyecto);
            } else {
                programaProyecto.setUsuarioModifica(userSession.getNameUser());
                programaProyecto.setFechaModificacion(new Date());
                planAnualProgramaProyectoService.edit(programaProyecto);
            }
            setIndiceDeAcordian(2);
            PrimeFaces.current().executeScript("PF('DlgPlanProyecto').hide()");
            PrimeFaces.current().ajax().update("messages");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", programaProyecto.getNombreProgramaProyecto() + (edit ? " editado" : " guardado") + " con éxito.");
            programaProyecto = new PlanAnualProgramaProyecto();
        }
    }

    public void añadirActividadOperativa(ActividadOperativa a) {
        producto = new Producto();
        producto.setMonto(BigDecimal.ZERO);
        producto.setActividadOperativa(a);
        PrimeFaces.current().executeScript("PF('DdlgProducto').show()");
        PrimeFaces.current().ajax().update(":formProducto");
    }

    public Boolean calcularActividadMonto() {
        boolean verificacion = actividad.getCr1().add(actividad.getCr2()).add(actividad.getCr3()).compareTo(actividad.getMonotReformado()) == 0;
        return verificacion;
    }

    public Boolean calcularSumaActividades() {
        boolean verificacion = actividad.getCuatrimestre1Meta().add(actividad.getCuatrimestre2Meta()).add(actividad.getCuatrimestre3Meta()).compareTo(actividad.getMedicionMeta()) == 0;
        return verificacion;
    }

    public void saveActividadOperativa() {
        try {
            CatalogoItem estadoReg = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
            boolean edit = actividad.getId() != null;
            if (actividad.getNumeroOrdenId() == null) {
                if (programaProyecto != null && programaProyecto.getId() != null) {
                    actividad.setPlanProgramaProyecto(programaProyecto);
                }
            }
//            calcularActividadMonto()
            if (actividad.sumaDsitribuccionMeta()) {
//                calcularSumaActividades()
                if (actividad.sumaDsitribuccion()) {
                    if (!edit) {
                        if (actividadOperativaService.getListaVerificacionActividadesReformaTraspaso(actividad.getNombreActividad(), BigInteger.valueOf(reformaTraspaso.getId()))) {
                            JsfUtil.addWarningMessage("AVISO", "ESTA ACTIVIDAD OPERATIVA YA FUE CREADA");
                            return;
                        }
                        actividad.setEstado(true);
                        actividad.setFechaCreacion(new Date());
                        actividad.setUsuarioCreacion(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setMonto(BigDecimal.ZERO);
                        actividad.distribuirMeta();
                        actividad.distribuirMonto();
//                        actividad.setCuatrimestre1Actividad(BigDecimal.ZERO);
//                        actividad.setCuatrimestre2Actividad(BigDecimal.ZERO);
//                        actividad.setCuatrimestre3Actividad(BigDecimal.ZERO);
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                        actividad.setEstadoPapp(estadoReg);
                        this.actividad = actividadOperativaService.create(actividad);
                    } else {
                        actividad.setEstado(true);
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setEstadoPapp(estadoReg);
                        System.out.println("Activ Meta: "+actividad.getTrimestreUnoMeta());
                        System.out.println("Activ: "+actividad.getTrimestreUno());
                        actividadOperativaService.edit(actividad);
                    }
                    setIndiceDeAcordian(1);
                    PrimeFaces.current().ajax().update("formMain");
                    PrimeFaces.current().executeScript("PF('DlgActividad').hide()");
                    PrimeFaces.current().ajax().update(":formActividad");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addInformationMessage("Información", actividad.getNombreActividad() + (edit ? "  editado" : "  guardado") + " con éxito.");
                    actividad = new ActividadOperativa();
                } else {
                    PrimeFaces.current().ajax().update(":formActividad");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage(null, "La suma de la programación meta deber ser igual al monto de la medición meta");
//                    JsfUtil.addErrorMessage("Información", "La suma de los cuatrimestre no son iguales a la meta indicada");
                }
            } else {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage(null, "La suma de la programación financiera debe ser igual al monto de la actividad.");
//                JsfUtil.addErrorMessage("Información", "La suma de los cuatrimestre de la actividad deber ser igual al monto de la actividad");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "No pueden estar vacios los campos");
        }

    }

    public void editarActividadOperativa(ActividadOperativa a) {
        actividad = new ActividadOperativa();
        actividad = a;
        programaProyecto = a.getPlanProgramaProyecto();
        listacomponentes = new ArrayList<>();
        listacomponentes = catalogoService.MostarTodoCatalogo("TIPO_COMPONENTE_PAPP");
        PrimeFaces.current().executeScript("PF('DlgActividad').show()");
        PrimeFaces.current().ajax().update(":formActividad");
        PrimeFaces.current().ajax().update(":listaProductos");
    }

    public void eliminarActividadOperativa(ActividadOperativa a) {
        if (getHijosByPadre(null, null, a, 3)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "No se puede eliminar porque tiene Productos asociados");
            return;
        }
        actividadOperativaService.remove(a);
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":productos");
        JsfUtil.addInformationMessage("Información", a.getNombreActividad() + " eliminado con éxito.");

    }

    public void editarProducto(Producto p) {
        productoGlobal = "";
        this.producto = p;
        productoGlobal = String.valueOf(p.getMonto());
        PrimeFaces.current().executeScript("PF('DdlgProducto').show()");
        PrimeFaces.current().ajax().update(":formProducto");
    }

    public double calcularMontoProducto(ActividadOperativa a) {
        List<Producto> verificacion = productoService.findByNamedQuery("Producto.findByVerificarMonto", a);
        String monto;
        double suma = 0;
        for (Producto item : verificacion) {
            monto = String.valueOf(item.getMonto());
            suma += Double.valueOf(monto);
        }
        return suma;
    }

    public void eliminarProducto(Producto p) {
        productoService.eliminarPstoPlanificado(p.getPeriodo(), p.getCodigoPresupuestario());
        this.productoService.remove(p);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", p.getDescripcion() + " eliminado con éxito.");

    }

    public void saveProducto() {
        try {
            boolean edit = producto.getId() != null;
            String actividadComparar = "";
            actividadComparar = String.valueOf(producto.getActividadOperativa().getMonto());
            if (!edit) {
                if (calcularMontoProducto(producto.getActividadOperativa()) <= Double.valueOf(actividadComparar)) {
                    double resultado = calcularMontoProducto(producto.getActividadOperativa());
                    String productonuevo = String.valueOf(producto.getMonto());
                    double resultado2 = resultado + Double.valueOf(productonuevo);
                    if (resultado2 <= Double.valueOf(actividadComparar)) {
                        producto.setEstado(true);
                        producto.setFechaCreacion(new Date());
                        producto.setFechaModificacion(new Date());
                        producto.setUsuarioCreacion(userSession.getNameUser());
                        producto.setUsuarioModifica(userSession.getNameUser());
                        producto.setPeriodo(busqueda.getAnio());
                        producto.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                        producto.setSuplementoEgreso(BigDecimal.ZERO);
                        producto.setReduccionEgreso(BigDecimal.ZERO);
                        producto.setReserva(BigDecimal.ZERO);
                        producto.setSaldoDisponible(BigDecimal.ZERO);
                        producto.setTraspasoIncremento(BigDecimal.ZERO);
                        producto.setTraspasoReduccion(BigDecimal.ZERO);
                        producto.setMontoReformada((producto.getMonto().add(producto.getTraspasoIncremento())).subtract(producto.getTraspasoReduccion())); //Revisaaaaar
                        this.producto = productoService.create(producto);
                        setIndiceDeAcordian(4);
                        PrimeFaces.current().ajax().update(":formMain");
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                        producto = new Producto();
                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Información", "La suma de los productos deber ser igual al monto de la actividad");
                    }
                } else {
                    PrimeFaces.current().ajax().update(":formProducto");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Información", "La suma de los productos deber ser igual al monto de la actividad");
                }
            } else {
                if (calcularMontoProducto(producto.getActividadOperativa()) <= Double.valueOf(actividadComparar)) {
                    double resultado = calcularMontoProducto(producto.getActividadOperativa());
                    String productonuevo = String.valueOf(producto.getMonto());
                    double resultado2 = resultado + Double.valueOf(productonuevo) - Double.valueOf(productoGlobal);
                    if (resultado2 <= Double.valueOf(actividadComparar)) {
                        producto.setUsuarioModifica(userSession.getNameUser());
                        producto.setFechaModificacion(new Date());
                        producto.setSuplementoEgreso(BigDecimal.ZERO);
                        producto.setReduccionEgreso(BigDecimal.ZERO);
                        producto.setReserva(BigDecimal.ZERO);
                        producto.setTraspasoIncremento(BigDecimal.ZERO);
                        producto.setTraspasoReduccion(BigDecimal.ZERO);
                        producto.setMontoReformada((producto.getMonto().add(producto.getTraspasoIncremento())).subtract(producto.getTraspasoReduccion()));
                        productoService.edit(producto);
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                        producto = new Producto();
                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Información", "La suma de los productos deber ser igual al monto de la actividad");
                    }

                } else {
                    if (Double.valueOf(actividadComparar) <= calcularMontoProducto(producto.getActividadOperativa())) {
                        producto.setUsuarioModifica(userSession.getNameUser());
                        producto.setFechaModificacion(new Date());
                        productoService.edit(producto);
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                        producto = new Producto();
                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Información", "La suma de los productos deber ser igual al monto de la actividad");
                    }
                }
                PrimeFaces.current().ajax().update("formMain");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "Los campos no puedene estar vacios");
        }
    }

    public void abrirDlgTraspaso(Producto p) {
        producto = new Producto();
        producto = p;
        PrimeFaces.current().executeScript("PF('DlgProductoTraspaso').show()");
        PrimeFaces.current().ajax().update(":formProductoTraspaso");
    }

    public void editarTraspaso() {
        BigDecimal cupConfirmar = BigDecimal.ZERO;
        if (producto.getTraspasoReduccion().doubleValue() > 0 || producto.getTraspasoReduccion().doubleValue() > 0) {
            if (producto.getTraspasoReduccion().doubleValue() > producto.getSaldoDisponible().doubleValue()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Error", "Esta sobrepasando el Saldo disponible");
                return;
            }
            producto.setSaldoDisponible(producto.getMonto().subtract(producto.getReserva()));
            producto.setMontoReformada((producto.getMonto().add(producto.getTraspasoIncremento())).subtract(producto.getTraspasoReduccion()));
            productoService.edit(producto);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Información", "El Suplemento se realizo correctamente");
            PrimeFaces.current().executeScript("PF('DdlgProductoTraspaso').hide()");
            PrimeFaces.current().ajax().update(":formProductoTraspaso");
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No se aceptar un valor de cero");
        }
    }

    public void anadirProductoAReforma(Producto p) {
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        p.setEstadoPapp(estadopapp);
        productoService.edit(p);
        listaProducto = reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        PrimeFaces.current().ajax().update(":formTablaReforma");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        int i = event.getRowIndex();
        for (Producto producto1 : listaProducto) {
            if (!producto1.getTraspasoReduccion().equals(newValue)) {
                BigDecimal reduccion = (BigDecimal) newValue;
                BigDecimal saldoDisponible = producto1.getSaldoDisponible();
                if (newValue == producto1.getSaldoDisponible()) {
                    JsfUtil.addErrorMessage("ERROR", "Monto no puede ser reducido menos del Saldo Disponible");
                }
            }
        }
    }

    public void calcular(Producto p, boolean esIncremento) {
        if (p.getTraspasoIncremento() == null) {
            p.setTraspasoIncremento(BigDecimal.ZERO);
        }
        if (p.getTraspasoReduccion() == null) {
            p.setTraspasoReduccion(BigDecimal.ZERO);
        }
        if (p.getTraspasoIncremento().doubleValue() > 0 && p.getTraspasoReduccion().doubleValue() > 0) {
            if (esIncremento) {
                p.setTraspasoIncremento(BigDecimal.ZERO);
            } else {
                p.setTraspasoReduccion(BigDecimal.ZERO);
            }
            productoService.edit(p);
            JsfUtil.addWarningMessage("TRASPASO", "No se puede realizar un incremento y reducción del mismo producto a la vez.");
            return;
        }
        BigDecimal montoReformadoIncremento = p.getMonto().add(p.getTraspasoIncremento());
        BigDecimal montoReformadoReduccion = montoReformadoIncremento.subtract(p.getTraspasoReduccion());
        p.setMontoReformada(montoReformadoReduccion);
        Double reduccion = p.getTraspasoReduccion().doubleValue();
        Double incremento = p.getTraspasoIncremento().doubleValue();
        Double saldoDisponible = p.getSaldoDisponible().doubleValue();
        if (reduccion > saldoDisponible) {
            JsfUtil.addErrorMessage("ERROR", "Monto no puede ser reducido a menos del Saldo Disponible");
            p.setTraspasoReduccion(BigDecimal.ZERO);
            BigDecimal montoReformadoIncrementos = p.getMonto().add(p.getTraspasoIncremento());
            BigDecimal montoReformadoReduccions = montoReformadoIncrementos.subtract(p.getTraspasoReduccion());
            p.setMontoReformada(montoReformadoReduccions);
            actualizarTotales();
            productoService.edit(p);
            return;
        }
        productoService.edit(p);
        actualizarTotales();
        System.out.println("activi " + p.getActividadOperativa());
        if (p.getActividadOperativa().getNumeroOrdenId() != null) {
            int a = reformaTraspasoService.actualizarValorActividad(p.getActividadOperativa());
            System.out.println("a -->" + a);
        }
    }

    public void calcularDistributivoGeneral(PartidasDistributivo p, boolean bolIncremento) {
        if (bolIncremento) {
            if (p.getTraspasoIncremento() == null) {
                p.setTraspasoIncremento(BigDecimal.ZERO);
            }
            if (p.getPartidaAp() == null || p.getPartidaAp().equals("")) {
                p.setReformaCodificado(p.getTraspasoIncremento());
            } else {
                p.setReformaCodificado(p.getMonto().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion()));
            }
        } else {
            if (p.getTraspasoReduccion() == null) {
                p.setTraspasoReduccion(BigDecimal.ZERO);
            }
            if (p.getPartidaAp() == null || p.getPartidaAp().equals("")) {
                p.setTraspasoReduccion(BigDecimal.ZERO);
                JsfUtil.addErrorMessage("Error", "No tiene asignado presupuesto, no se le puede reducir. El monto es de proyección");
                return;
            } else {
                BigDecimal saldoDisponible = p.getMonto().subtract(p.getReserva());
                if (p.getTraspasoReduccion().doubleValue() > p.getMonto().doubleValue()) {
                    p.setTraspasoReduccion(BigDecimal.ZERO);
                    p.setTraspasoIncremento(BigDecimal.ZERO);
                    p.setReformaCodificado(p.getMonto());
                    JsfUtil.addErrorMessage("Error", "El valor es mayor al valor original");
                    return;
                }
                if (p.getTraspasoReduccion().doubleValue() > saldoDisponible.doubleValue()) {
                    p.setTraspasoReduccion(BigDecimal.ZERO);
                    JsfUtil.addErrorMessage("Error", "El valor de reducción no puede ser mayor al Saldo Disponible");
                    return;
                }
                if (p.getPartidaAp() == null) {
                    p.setTraspasoReduccion(BigDecimal.ZERO);
                    BigDecimal montoReformado2 = p.getTraspasoIncremento();
                    p.setReformaCodificado(montoReformado2);
                } else {
                    p.setReformaCodificado(p.getMonto().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion()));
                }
            }
        }
        partidasDistributivoService.edit(p);
    }

    public void calcularDistributivoAnexo(PartidasDistributivoAnexo p) {
        BigDecimal montoReformado = p.getDistributivoAnexo().getMonto().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion());
        p.setReformaCodificado(montoReformado);
        Double reduccion = p.getTraspasoReduccion().doubleValue();
        Double incremento = p.getTraspasoIncremento().doubleValue();
        partidasDistributivoAnexoService.edit(p);
    }

    public void calcularPartidaDirecta(ProformaPresupuestoPlanificado p) {
        if (p.getCodigoReferencia() == null) {
            p.setTraspasoReduccion(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "El valor de reducción no puede ser mayor al valor de original");
        }
        BigDecimal montoReformado = p.getValor().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion());
        p.setReformaCodificado(montoReformado);
        proformaPresupuestoPlanificadoService.edit(p);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
    }

    public void actualizarTotales() {
        totalSaldoDisponible = 0;
        totalTraspasoIncremento = 0;
        totalTraspasoReduccion = 0;
        totalMontoReformado = 0;
        for (Producto item : listaProducto) {
            if (item.getEstado()) {
                totalSaldoDisponible = totalSaldoDisponible + item.getSaldoDisponible().doubleValue();
                totalTraspasoIncremento = totalTraspasoIncremento + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccion = totalTraspasoReduccion + item.getTraspasoReduccion().doubleValue();
                totalMontoReformado = totalMontoReformado + item.getMontoReformada().doubleValue();
            }
        }
    }

    public void actualizarTotalesDistributivo() {
        totalTraspasoReduccionDGeneral = 0;
        totalTraspasoIncrementoDGeneral = 0;
        totalMontoReformadoDGeneral = 0;
        listaCargosRubros = thCargoRubrosService.copiaVerificacion2(BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isNotEmpty(listaCargosRubros)) {
            for (ThCargoRubros item : listaCargosRubros) {
                totalTraspasoIncrementoDGeneral = totalTraspasoIncrementoDGeneral + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionDGeneral = totalTraspasoReduccionDGeneral + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoDGeneral = totalMontoReformadoDGeneral + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void actualizarTotalesDistributivoAnexo() {
        totalTraspasoReduccionDAnexo = 0;
        totalTraspasoIncrementoDAnexo = 0;
        totalMontoReformadoDAnexo = 0;
        if (Utils.isNotEmpty(listaPartidaDistributivoAnexo)) {
            for (PartidasDistributivoAnexo item : listaPartidaDistributivoAnexo) {
                totalTraspasoIncrementoDAnexo = totalTraspasoIncrementoDAnexo + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionDAnexo = totalTraspasoReduccionDAnexo + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoDAnexo = totalMontoReformadoDAnexo + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void actualizarTotalesPartidaDirecta() {
        totalTraspasoIncrementoPDirecta = 0;
        totalTraspasoReduccionPDirecta = 0;
        totalMontoReformadoPDirecta = 0;
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta + item.getTraspasoIncremento().doubleValue();
                totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta + item.getTraspasoReduccion().doubleValue();
                totalMontoReformadoPDirecta = totalMontoReformadoPDirecta + item.getReformaCodificado().doubleValue();
            }
        }
    }

    public void saveReformaTraspaso() {
        if (reformaTraspaso.getId() == null) {
            JsfUtil.addErrorMessage("REFORMA", "Genere la reforma correspondiente");
            return;
        }
        bolReformaT1 = Boolean.TRUE;
        btnGuardar = Boolean.TRUE;
        btnContinuarTerminarTarea = Boolean.FALSE;
        btnCancelar = Boolean.TRUE;
        btnGenerarReforma = Boolean.TRUE;
        btnNuevoPapp = Boolean.TRUE;
        if (editarObserv) {
            CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REGT-REF");
            reformaTraspaso.setEstadoReformaTramite(estadopapp);
            reformaTraspasoService.edit(reformaTraspaso);
        }
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        List<DetalleReformaTraspaso> listaDetalleReformaTraspasoPAPP = detalleReformaTraspasoService.getListDetalleReforma(busqueda.getAnio(), estadoReform, Boolean.FALSE, "PAPP", reformaTraspaso);
        List<DetalleReformaTraspaso> listaDetalleReformaTraspasoPD = detalleReformaTraspasoService.getListDetalleReforma(busqueda.getAnio(), estadoReform, Boolean.FALSE, "PD", reformaTraspaso);
        List<DetalleReformaTraspaso> listaDetalleReformaTraspasoPDA = detalleReformaTraspasoService.getListDetalleReforma(busqueda.getAnio(), estadoReform, Boolean.FALSE, "PDA", reformaTraspaso);
        List<DetalleReformaTraspaso> listaDetalleReformaTraspasoPDI = detalleReformaTraspasoService.getListDetalleReforma(busqueda.getAnio(), estadoReform, Boolean.FALSE, "PDI", reformaTraspaso);
        listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        totalSaldoDisponible = 0;
        totalTraspasoReduccion = 0;
        totalTraspasoIncremento = 0;
        totalMontoReformado = 0;
        if (!listaProducto.isEmpty()) {
            for (Producto producto1 : listaProducto) {
                if (producto1.getEstado()) {
                    totalSaldoDisponible = totalSaldoDisponible + producto1.getSaldoDisponible().doubleValue();
                    totalTraspasoIncremento = totalTraspasoIncremento + producto1.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccion = totalTraspasoReduccion + producto1.getTraspasoReduccion().doubleValue();
                    totalMontoReformado = totalMontoReformado + producto1.getMontoReformada().doubleValue();
                }
            }
        }
        listaPartidaDistributivo = detalleReformaTraspasoService.getListPartidaDistributivoReforma(busqueda.getAnio(), BigInteger.valueOf(reformaTraspaso.getId()));
        totalSaldoDisponibleDGeneral = 0;
        totalTraspasoReduccionDGeneral = 0;
        totalTraspasoIncrementoDGeneral = 0;
        totalMontoReformadoDGeneral = 0;
        if (!listaPartidaDistributivo.isEmpty()) {
            for (PartidasDistributivo distributivo : listaPartidaDistributivo) {
                if (distributivo.isEstado()) {
                    totalTraspasoIncrementoDGeneral = totalTraspasoIncrementoDGeneral + distributivo.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionDGeneral = totalTraspasoReduccionDGeneral + distributivo.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoDGeneral = totalMontoReformadoDGeneral + distributivo.getReformaCodificado().doubleValue();
                }
            }
        }
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        totalSaldoDisponibleDAnexo = 0;
        totalTraspasoReduccionDAnexo = 0;
        totalTraspasoIncrementoDAnexo = 0;
        totalMontoReformadoDAnexo = 0;
        if (!listaPartidaDistributivoAnexo.isEmpty()) {
            for (PartidasDistributivoAnexo distributivo : listaPartidaDistributivoAnexo) {
                if (distributivo.getEstado()) {
                    totalTraspasoIncrementoDAnexo = totalTraspasoIncrementoDAnexo + distributivo.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionDAnexo = totalTraspasoReduccionDAnexo + distributivo.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoDAnexo = totalMontoReformadoDAnexo + distributivo.getReformaCodificado().doubleValue();
                }
            }
        }
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        totalSaldoDisponiblePDirecta = 0;
        totalTraspasoReduccionPDirecta = 0;
        totalTraspasoIncrementoPDirecta = 0;
        totalMontoReformadoPDirecta = 0;
        if (!listaProformaPresupuestoPlanificado.isEmpty()) {
            for (ProformaPresupuestoPlanificado partidaDirecta : listaProformaPresupuestoPlanificado) {
                if (partidaDirecta.getEstado()) {
                    totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta + partidaDirecta.getTraspasoIncremento().doubleValue();
                    totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta + partidaDirecta.getTraspasoReduccion().doubleValue();
                    totalMontoReformadoPDirecta = totalMontoReformadoPDirecta + partidaDirecta.getReformaCodificado().doubleValue();
                }
            }
        }
        if (listaDetalleReformaTraspasoPAPP.isEmpty()) {
            if (!listaProducto.isEmpty()) {
                detalleReformaTraspaso.setId(null);
                detalleReformaTraspaso.setReforma(reformaTraspaso);
                detalleReformaTraspaso.setTipoTraspaso("PAPP");
                detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncremento));
                detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccion));
                detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformado));
                detalleReformaTraspaso = detalleReformaTraspasoService.create(detalleReformaTraspaso);
                detalleReformaTraspaso = new DetalleReformaTraspaso();
            }
        } else {
            detalleReformaTraspaso = listaDetalleReformaTraspasoPAPP.get(0);
            detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncremento));
            detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccion));
            detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformado));
            detalleReformaTraspasoService.edit(detalleReformaTraspaso);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
        }
        if (listaDetalleReformaTraspasoPD.isEmpty()) {
            if (!listaPartidaDistributivo.isEmpty()) {
                detalleReformaTraspaso.setId(null);
                detalleReformaTraspaso.setReforma(reformaTraspaso);
                detalleReformaTraspaso.setTipoTraspaso("PD");
                detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoDGeneral));
                detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionDGeneral));
                detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoDGeneral));
                detalleReformaTraspaso = detalleReformaTraspasoService.create(detalleReformaTraspaso);
                detalleReformaTraspaso = new DetalleReformaTraspaso();
            }
        } else {
            detalleReformaTraspaso = listaDetalleReformaTraspasoPD.get(0);
            detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoDGeneral));
            detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionDGeneral));
            detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoDGeneral));
            detalleReformaTraspasoService.edit(detalleReformaTraspaso);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
        }
        if (listaDetalleReformaTraspasoPDA.isEmpty()) {
            if (!listaPartidaDistributivoAnexo.isEmpty()) {
                detalleReformaTraspaso.setId(null);
                detalleReformaTraspaso.setReforma(reformaTraspaso);
                detalleReformaTraspaso.setTipoTraspaso("PDA");
                detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoDAnexo));
                detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionDAnexo));
                detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoDAnexo));
                detalleReformaTraspaso = detalleReformaTraspasoService.create(detalleReformaTraspaso);
                detalleReformaTraspaso = new DetalleReformaTraspaso();
            }
        } else {
            detalleReformaTraspaso = listaDetalleReformaTraspasoPDA.get(0);
            detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoDAnexo));
            detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionDAnexo));
            detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoDAnexo));
            detalleReformaTraspasoService.edit(detalleReformaTraspaso);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
        }
        if (listaDetalleReformaTraspasoPDI.isEmpty()) {
            if (!listaProformaPresupuestoPlanificado.isEmpty()) {
                detalleReformaTraspaso.setId(null);
                detalleReformaTraspaso.setReforma(reformaTraspaso);
                detalleReformaTraspaso.setTipoTraspaso("PDI");
                detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoPDirecta));
                detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionPDirecta));
                detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoPDirecta));
                detalleReformaTraspaso = detalleReformaTraspasoService.create(detalleReformaTraspaso);
                detalleReformaTraspaso = new DetalleReformaTraspaso();
            }
        } else {
            detalleReformaTraspaso = listaDetalleReformaTraspasoPDA.get(0);
            detalleReformaTraspaso.setTraspasoIncremento(BigDecimal.valueOf(totalTraspasoIncrementoPDirecta));
            detalleReformaTraspaso.setTraspasoReduccion(BigDecimal.valueOf(totalTraspasoReduccionPDirecta));
            detalleReformaTraspaso.setMontoReformada(BigDecimal.valueOf(totalMontoReformadoPDirecta));
            detalleReformaTraspasoService.edit(detalleReformaTraspaso);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
        }
        JsfUtil.addInformationMessage("Exitoso", "Registro" + ((formBusqRegistrar || formBusqRegistrarT2 ? " guardado" : " editado") + " con éxito."));
    }

    public void continuarYTerminarTarea() {
        reformaTraspaso = new ReformaTraspaso();
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        actividad = new ActividadOperativa();
        busqueda = new OpcionBusqueda();
        producto = new Producto();
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        idUnidadA = d.getUnidadAdministrativa().getId();
        this.listaProducto = new ArrayList<>();
        this.listaDistributivo = new ArrayList<>();
        this.listaDistributivoAnexo = new ArrayList<>();
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        short anio = Utils.getAnio(new Date()).shortValue();
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        tipoTraspaso = "";
        totalSaldoDisponible = 0;
        totalTraspasoIncremento = 0;
        totalTraspasoReduccion = 0;
        totalMontoReformado = 0;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.TRUE;
        btnGenerarReforma = Boolean.FALSE;
        btnNuevoPapp = Boolean.TRUE;
        booleanosReset();
    }

    public void consultarReforma(ReformaTraspaso reforma) {
        reformaTraspaso = reforma;
        listaProducto = new ArrayList<>();
        listaProformaPresupuestoPlanificado = new ArrayList<>();
        listaDistributivo = new ArrayList<>();
        listaPartidaDistributivoAnexo = new ArrayList<>();
        busqueda.setAnio(reformaTraspaso.getPeriodo());
        listaProducto = reformaTraspasoService.getListProductoByReformaConsulta(reformaTraspaso.getPeriodo(), true, BigInteger.valueOf(reformaTraspaso.getId()));
        listaDistributivo = reformaTraspasoService.getDistributivoFinalReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        listaPartidaDistributivoAnexo = partidasDistributivoAnexoService.getPartidasAnexoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isEmpty(listaProducto)) {
            listaProducto = reformaTraspasoService.getListProductoByReformaT2(reformaTraspaso.getPeriodo(), "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        }
        renderedPAPP = !listaProducto.isEmpty();
        renderedDistributivo = !listaDistributivo.isEmpty();
        renderedDistributivoAnexo = !listaPartidaDistributivoAnexo.isEmpty();
        renderedPartidaDirecta = !listaProformaPresupuestoPlanificado.isEmpty();

        if (!listaProducto.isEmpty()) {
            actualizarTotales();
        }
        PrimeFaces.current().executeScript("PF('DlgVistaReforma').show()");
    }

    public void clearAllFiltersPAPP() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formDlgVistaReforma:tablaSolicitud");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formDlgVistaReforma:tablaSolicitud");
        }
    }

    public void dlogoObservaciones(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        if (busqueda.getAnio() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Elija un Período");
            return;
        }
        BigDecimal traspasoIncr = BigDecimal.valueOf(totalDefinitivoIncremento).setScale(2, RoundingMode.HALF_UP);
        BigDecimal traspasoReduc = BigDecimal.valueOf(totalDefinitivoReduccion).setScale(2, RoundingMode.HALF_UP);
//        System.out.println("traspasoIncr.compareTo(BigDecimal.ZERO) >>"+traspasoIncr.compareTo(BigDecimal.ZERO));
        if(traspasoIncr.compareTo(BigDecimal.ZERO)==0 || traspasoReduc.compareTo(BigDecimal.ZERO)==0){
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("TRASPASO TIPO 2", "El total de Traspaso Incremento o total de Traspaso Reducción debe ser mayor a 0");
            return;
        }else{
            if (traspasoIncr.compareTo(traspasoReduc) != 0) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("TRASPASO TIPO 2", "El total de Traspaso Incremento debe ser igual al Total Traspaso Reducción");
                return;
            }
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }
    
    public void dlgoDocumento(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        PrimeFaces.current().executeScript("PF('dlgoDocumento').show()");
        PrimeFaces.current().ajax().update(":formDocumento");
    }

    public void observar(ReformaTraspaso ref) {
        reformaTraspaso = ref;
        CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBS-REF");
        reformaTraspaso.setEstadoReformaTramite(estadopapp);
        reformaTraspasoService.edit(reformaTraspaso);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solicitud " + reformaTraspaso.getCodigo() + " observada con éxito");
        this.reformaTraspaso = new ReformaTraspaso();
    }

    public void completarTarea(int aprobar) {
        try {
            observacion.setObservacion(observaciones);
            if (aprobar == 1) {
                enviar(reformaTraspaso);
                getParamts().put("userPlanificacion", clienteService.getrolsUser(RolUsuario.planificacion));
            } 
//            else {
//                getParamts().put("usuario", reformaTraspaso.getUsuarioCreacion());
//                observar(reformaTraspaso);
//            }
//            getParamts().put("aprobado", aprobar);
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            PrimeFaces.current().ajax().update("messages");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            if (aprobar == 1) {
                JsfUtil.addInformationMessage("Información", "Solicitud se ha aprobado con éxito");
            } 
//            else {
//                JsfUtil.addInformationMessage("Información", "Solcitud se ha rechazado con éxito");
//            }
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }
    
    public void renderizarDistribuccionMeta() {
        semestreMetal = Boolean.FALSE;
        trimestreMeta = Boolean.FALSE;
        cuatrimestreMeta = Boolean.FALSE;
        mensualMeta = Boolean.FALSE;
        if (actividad.getDistribucionMeta() != null) {

            switch (actividad.getDistribucionMeta().getCodigo()) {
                case "S":
                    semestreMetal = Boolean.TRUE;
                    break;
                case "T":
                    trimestreMeta = Boolean.TRUE;
                    break;
                case "C":
                    cuatrimestreMeta = Boolean.TRUE;
                    break;
                case "M":
                    mensualMeta = Boolean.TRUE;
                    break;
                default:
                    semestreMetal = Boolean.FALSE;
                    trimestreMeta = Boolean.FALSE;
                    cuatrimestreMeta = Boolean.FALSE;
                    mensualMeta = Boolean.FALSE;
                    break;
            }
        }
    }

    public void renderizarDistribuccion() {
        semestre = Boolean.FALSE;
        trimestre = Boolean.FALSE;
        cuatrimestre = Boolean.FALSE;
        mensual = Boolean.FALSE;
        if (actividad.getTipoDistribuccion() != null) {

            switch (actividad.getTipoDistribuccion().getCodigo()) {
                case "S":
                    semestre = Boolean.TRUE;
                    break;
                case "T":
                    trimestre = Boolean.TRUE;
                    break;
                case "C":
                    cuatrimestre = Boolean.TRUE;
                    break;
                case "M":
                    mensual = Boolean.TRUE;
                    break;
                default:
                    semestre = Boolean.FALSE;
                    trimestre = Boolean.FALSE;
                    cuatrimestre = Boolean.FALSE;
                    mensual = Boolean.FALSE;
                    break;
            }
        }

    }
    
    public void openActividadOperativa(ActividadOperativa a) {
        if (a == null) {
            UnidadAdministrativa unidadAdministrativa = unidadAdministrativaService.find(idUnidadA);
            System.out.println("Unidad Administrativa:s "+unidadAdministrativa);
            programaProyecto = new PlanAnualProgramaProyecto();
            actividad = new ActividadOperativa();
            actividad.setUnidadResponsable(unidadAdministrativa);
            if (busqueda.getAnio() < anioActual) {
                actividad.setArrastre(false);
            } else {
                actividad.setArrastre(true);
            }
        } else {
            actividad = new ActividadOperativa();
            actividad = a;
            programaProyecto = actividad.getPlanProgramaProyecto();
        }
        actividad.reversarDistribucionMeta();
        actividad.reversarDistribuirMonto();
        renderizarDistribuccionMeta();
        renderizarDistribuccion();
        distribuccion = new ArrayList<>();
        distribuccion = catalogoService.MostarTodoCatalogo("TIPO_DISTRIBUCCION_ACO");
        distribuccionMetaList = new ArrayList<>();
        distribuccionMetaList = catalogoService.MostarTodoCatalogo("TIPO_DISTRIBUCCION_ACO");
        listacomponentes = new ArrayList<>();
        listacomponentes = catalogoService.MostarTodoCatalogo("TIPO_COMPONENTE_PAPP");
    }
     
    public void loadingProgramas() {
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("estado", true);
        programaProyecto = new PlanAnualProgramaProyecto();
    }
    
    public void sumaMontos() {
        if (actividad.getPresupuestoPropio() == null) {
            actividad.setPresupuestoPropio(BigDecimal.ZERO);
        }
        if (actividad.getPresupuestoFinanciamiento() == null) {
            actividad.setPresupuestoFinanciamiento(BigDecimal.ZERO);
        }
        actividad.setMonto(actividad.getPresupuestoPropio().setScale(2, RoundingMode.HALF_UP).add(actividad.getPresupuestoFinanciamiento().setScale(2, RoundingMode.HALF_UP)));
    }
    
    public void loadingActividadLazy() {
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", reformaTraspaso.getUnidadRequiriente());
    }
    
    public void saveActividadOperativaReforma() {
        try {
            boolean edit = actividad.getId() != null;
            if (programaProyecto != null && programaProyecto.getId() != null) {
                actividad.setPlanProgramaProyecto(programaProyecto);
            }
            CatalogoItem estadoRegistrado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RP");
            if (actividad.sumaDsitribuccionMeta()) {
                if (actividad.sumaDsitribuccion()) {
                    if (!edit) {
                        if (actividadOperativaService.getListaVerificacionActividades(actividad.getNombreActividad(), busqueda.getAnio())) {
                            JsfUtil.addWarningMessage("AVISO", "ESTA ACTIVIDAD OPERATIVA YA FUE CREADA");
                            return;
                        }
                        actividad.setEstado(true);
                        actividad.setEstadoPapp(estadoRegistrado);
                        actividad.setFechaCreacion(new Date());
                        actividad.setUsuarioCreacion(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setEstadoPapp(estadoRegistrado);
                        actividad.setMonotReformado(actividad.getMonto());
                        actividad.distribuirMeta();
                        actividad.distribuirMonto();
                        actividad.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                        actividad.setNumeroTramite(reformaTraspaso.getNumeroTramite());
                        actividadOperativaService.create(actividad);
                        PrimeFaces.current().ajax().update("formActividadReforma");
                    } else {
                        BigDecimal valorEspecifico = actividadOperativaService.verificandoProductosActividades(actividad);
                        if (valorEspecifico.doubleValue() > actividad.getMonto().doubleValue()) {
                            JsfUtil.addErrorMessage("ERROR", "EL VALOR A EDITAR NO PUEDE SER MENOR AL MONTO DE SUS PRODUCTO");
                            return;
                        }
                        actividad.setEstado(true);
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setAnioPresPar(Integer.valueOf(busqueda.getAnio()));
                        actividad.setMonotReformado(actividad.getMonto());
                        actividad.distribuirMeta();
                        actividad.distribuirMonto();
                        service.updateEntity(actividad);
                    }
                    JsfUtil.addInformationMessage("Información", actividad.getNombreActividad() + (edit ? "  editado" : "  guardado") + " con éxito.");
                    PrimeFaces.current().executeScript("PF('DlgActividadReforma').hide()");
                    actividad = new ActividadOperativa();
                    loadingActividadLazy();
                } else {
                    JsfUtil.addErrorMessage(null, "LA SUMA DE LAPROGRAMACIÓN META DEBE SER IGUAL AL MONTO DE LA MEDICIÓN META");
                }
            } else {
                JsfUtil.addErrorMessage(null, "LA SUMA DE LA PROGAMACIÓN FINANCIERA DEBE SER IGUAL AL MONTO DE LA ACTIVIDAD");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Informacsion", "No pueden estar vacios los campos");
            System.out.println("error " + e);
        }
        UnidadAdministrativa unid = unidadAdministrativaService.find(idUnidadA);
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", unid);
        lazyActividadOperativas.getFilterss().put("periodo", reformaTraspaso.getPeriodo());
    }
    
    public void seleccionProyecto(PlanAnualProgramaProyecto proyecto) {
        programaProyecto = new PlanAnualProgramaProyecto();
        programaProyecto = proyecto;
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER & SETTER">
    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public ReformaTraspaso getReformaVerificar() {
        return reformaVerificar;
    }

    public void setReformaVerificar(ReformaTraspaso reformaVerificar) {
        this.reformaVerificar = reformaVerificar;
    }

    public DetalleReformaTraspaso getDetalleReformaTraspaso() {
        return detalleReformaTraspaso;
    }

    public void setDetalleReformaTraspaso(DetalleReformaTraspaso detalleReformaTraspaso) {
        this.detalleReformaTraspaso = detalleReformaTraspaso;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public PlanAnualPoliticaPublica getPlanAnualPoliticaPublica() {
        return planAnualPoliticaPublica;
    }

    public void setPlanAnualPoliticaPublica(PlanAnualPoliticaPublica planAnualPoliticaPublica) {
        this.planAnualPoliticaPublica = planAnualPoliticaPublica;
    }

    public PlanAnualProgramaProyecto getPlanAnualProgramaProyecto() {
        return planAnualProgramaProyecto;
    }

    public void setPlanAnualProgramaProyecto(PlanAnualProgramaProyecto planAnualProgramaProyecto) {
        this.planAnualProgramaProyecto = planAnualProgramaProyecto;
    }

    public PlanAnualProgramaProyecto getProgramaProyecto() {
        return programaProyecto;
    }

    public void setProgramaProyecto(PlanAnualProgramaProyecto programaProyecto) {
        this.programaProyecto = programaProyecto;
    }

    public PlanAnualPoliticaPublica getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualPoliticaPublica planAnual) {
        this.planAnual = planAnual;
    }

    public PlanNacionalEje getEjeNacionalSeleccionado() {
        return ejeNacionalSeleccionado;
    }

    public void setEjeNacionalSeleccionado(PlanNacionalEje ejeNacionalSeleccionado) {
        this.ejeNacionalSeleccionado = ejeNacionalSeleccionado;
    }

    public PlanNacionalObjetivo getObjetivoNacionalSeleccionado() {
        return objetivoNacionalSeleccionado;
    }

    public void setObjetivoNacionalSeleccionado(PlanNacionalObjetivo objetivoNacionalSeleccionado) {
        this.objetivoNacionalSeleccionado = objetivoNacionalSeleccionado;
    }

    public PlanNacionalPolitica getPoliticaNacionalSeleccionada() {
        return politicaNacionalSeleccionada;
    }

    public void setPoliticaNacionalSeleccionada(PlanNacionalPolitica politicaNacionalSeleccionada) {
        this.politicaNacionalSeleccionada = politicaNacionalSeleccionada;
    }

    public PlanLocalSistema getSistemaLocalSeleccionado() {
        return sistemaLocalSeleccionado;
    }

    public void setSistemaLocalSeleccionado(PlanLocalSistema sistemaLocalSeleccionado) {
        this.sistemaLocalSeleccionado = sistemaLocalSeleccionado;
    }

    public PlanLocalObjetivo getObjetivoLocalSeleccionado() {
        return objetivoLocalSeleccionado;
    }

    public void setObjetivoLocalSeleccionado(PlanLocalObjetivo objetivoLocalSeleccionado) {
        this.objetivoLocalSeleccionado = objetivoLocalSeleccionado;
    }

    public PlanLocalPolitica getPoliticaLocalSeleccionado() {
        return politicaLocalSeleccionado;
    }

    public void setPoliticaLocalSeleccionado(PlanLocalPolitica politicaLocalSeleccionado) {
        this.politicaLocalSeleccionado = politicaLocalSeleccionado;
    }

    public PlanAnualProgramaProyecto getProgramaProyectoSeleccionado() {
        return programaProyectoSeleccionado;
    }

    public void setProgramaProyectoSeleccionado(PlanAnualProgramaProyecto programaProyectoSeleccionado) {
        this.programaProyectoSeleccionado = programaProyectoSeleccionado;
    }

    public ActividadOperativa getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(ActividadOperativa actividadSeleccionada) {
        this.actividadSeleccionada = actividadSeleccionada;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public PlanAnualPoliticaPublica getPlanAnualSeleccionado() {
        return planAnualSeleccionado;
    }

    public void setPlanAnualSeleccionado(PlanAnualPoliticaPublica planAnualSeleccionado) {
        this.planAnualSeleccionado = planAnualSeleccionado;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuestoPlanificado() {
        return proformaPresupuestoPlanificado;
    }

    public void setProformaPresupuestoPlanificado(ProformaPresupuestoPlanificado proformaPresupuestoPlanificado) {
        this.proformaPresupuestoPlanificado = proformaPresupuestoPlanificado;
    }

    public PartidasDistributivo getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(PartidasDistributivo partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public PartidasDistributivoAnexo getPartidaDistributivoAnexo() {
        return partidaDistributivoAnexo;
    }

    public void setPartidaDistributivoAnexo(PartidasDistributivoAnexo partidaDistributivoAnexo) {
        this.partidaDistributivoAnexo = partidaDistributivoAnexo;
    }

    public Long getIdUnidadA() {
        return idUnidadA;
    }

    public void setIdUnidadA(Long idUnidadA) {
        this.idUnidadA = idUnidadA;
    }

    public Long getIdPlanProgramatico() {
        return idPlanProgramatico;
    }

    public void setIdPlanProgramatico(Long idPlanProgramatico) {
        this.idPlanProgramatico = idPlanProgramatico;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public double getTotalSaldoDisponible() {
        return totalSaldoDisponible;
    }

    public void setTotalSaldoDisponible(double totalSaldoDisponible) {
        this.totalSaldoDisponible = totalSaldoDisponible;
    }

    public double getTotalTraspasoReduccion() {
        return totalTraspasoReduccion;
    }

    public void setTotalTraspasoReduccion(double totalTraspasoReduccion) {
        this.totalTraspasoReduccion = totalTraspasoReduccion;
    }

    public double getTotalTraspasoIncremento() {
        return totalTraspasoIncremento;
    }

    public void setTotalTraspasoIncremento(double totalTraspasoIncremento) {
        this.totalTraspasoIncremento = totalTraspasoIncremento;
    }

    public double getTotalMontoReformado() {
        return totalMontoReformado;
    }

    public void setTotalMontoReformado(double totalMontoReformado) {
        this.totalMontoReformado = totalMontoReformado;
    }

    public double getTotalSaldoDisponibleDGeneral() {
        return totalSaldoDisponibleDGeneral;
    }

    public void setTotalSaldoDisponibleDGeneral(double totalSaldoDisponibleDGeneral) {
        this.totalSaldoDisponibleDGeneral = totalSaldoDisponibleDGeneral;
    }

    public double getTotalTraspasoReduccionDGeneral() {
        return totalTraspasoReduccionDGeneral;
    }

    public void setTotalTraspasoReduccionDGeneral(double totalTraspasoReduccionDGeneral) {
        this.totalTraspasoReduccionDGeneral = totalTraspasoReduccionDGeneral;
    }

    public double getTotalTraspasoIncrementoDGeneral() {
        return totalTraspasoIncrementoDGeneral;
    }

    public void setTotalTraspasoIncrementoDGeneral(double totalTraspasoIncrementoDGeneral) {
        this.totalTraspasoIncrementoDGeneral = totalTraspasoIncrementoDGeneral;
    }

    public double getTotalMontoReformadoDGeneral() {
        return totalMontoReformadoDGeneral;
    }

    public void setTotalMontoReformadoDGeneral(double totalMontoReformadoDGeneral) {
        this.totalMontoReformadoDGeneral = totalMontoReformadoDGeneral;
    }

    public double getTotalSaldoDisponibleDAnexo() {
        return totalSaldoDisponibleDAnexo;
    }

    public void setTotalSaldoDisponibleDAnexo(double totalSaldoDisponibleDAnexo) {
        this.totalSaldoDisponibleDAnexo = totalSaldoDisponibleDAnexo;
    }

    public double getTotalTraspasoReduccionDAnexo() {
        return totalTraspasoReduccionDAnexo;
    }

    public void setTotalTraspasoReduccionDAnexo(double totalTraspasoReduccionDAnexo) {
        this.totalTraspasoReduccionDAnexo = totalTraspasoReduccionDAnexo;
    }

    public double getTotalTraspasoIncrementoDAnexo() {
        return totalTraspasoIncrementoDAnexo;
    }

    public void setTotalTraspasoIncrementoDAnexo(double totalTraspasoIncrementoDAnexo) {
        this.totalTraspasoIncrementoDAnexo = totalTraspasoIncrementoDAnexo;
    }

    public double getTotalMontoReformadoDAnexo() {
        return totalMontoReformadoDAnexo;
    }

    public void setTotalMontoReformadoDAnexo(double totalMontoReformadoDAnexo) {
        this.totalMontoReformadoDAnexo = totalMontoReformadoDAnexo;
    }

    public double getTotalSaldoDisponiblePDirecta() {
        return totalSaldoDisponiblePDirecta;
    }

    public void setTotalSaldoDisponiblePDirecta(double totalSaldoDisponiblePDirecta) {
        this.totalSaldoDisponiblePDirecta = totalSaldoDisponiblePDirecta;
    }

    public double getTotalTraspasoReduccionPDirecta() {
        return totalTraspasoReduccionPDirecta;
    }

    public void setTotalTraspasoReduccionPDirecta(double totalTraspasoReduccionPDirecta) {
        this.totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta;
    }

    public double getTotalTraspasoIncrementoPDirecta() {
        return totalTraspasoIncrementoPDirecta;
    }

    public void setTotalTraspasoIncrementoPDirecta(double totalTraspasoIncrementoPDirecta) {
        this.totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta;
    }

    public double getTotalMontoReformadoPDirecta() {
        return totalMontoReformadoPDirecta;
    }

    public void setTotalMontoReformadoPDirecta(double totalMontoReformadoPDirecta) {
        this.totalMontoReformadoPDirecta = totalMontoReformadoPDirecta;
    }

    public int getIndiceDeAcordian() {
        return indiceDeAcordian;
    }

    public void setIndiceDeAcordian(int indiceDeAcordian) {
        this.indiceDeAcordian = indiceDeAcordian;
    }

    public String getProgramaProyectoLocal() {
        return programaProyectoLocal;
    }

    public void setProgramaProyectoLocal(String programaProyectoLocal) {
        this.programaProyectoLocal = programaProyectoLocal;
    }

    public String getProductoGlobal() {
        return productoGlobal;
    }

    public void setProductoGlobal(String productoGlobal) {
        this.productoGlobal = productoGlobal;
    }

    public String getTipoTraspaso() {
        return tipoTraspaso;
    }

    public void setTipoTraspaso(String tipoTraspaso) {
        this.tipoTraspaso = tipoTraspaso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ProductoLazy getProductoLaz() {
        return productoLaz;
    }

    public void setProductoLaz(ProductoLazy productoLaz) {
        this.productoLaz = productoLaz;
    }

    public ProductoLazy getLazyProductos() {
        return lazyProductos;
    }

    public void setLazyProductos(ProductoLazy lazyProductos) {
        this.lazyProductos = lazyProductos;
    }

    public LazyModel<Producto> getProductoLazy() {
        return productoLazy;
    }

    public void setProductoLazy(LazyModel<Producto> productoLazy) {
        this.productoLazy = productoLazy;
    }

    public ProductoLazy getProductoAsignacion() {
        return productoAsignacion;
    }

    public void setProductoAsignacion(ProductoLazy productoAsignacion) {
        this.productoAsignacion = productoAsignacion;
    }

    public LazyModel<PlanAnualProgramaProyecto> getPlanAnualProgramaLazy() {
        return planAnualProgramaLazy;
    }

    public void setPlanAnualProgramaLazy(LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy) {
        this.planAnualProgramaLazy = planAnualProgramaLazy;
    }

    public LazyModel<PlanAnualPoliticaPublica> getPlanAnualazy() {
        return planAnualazy;
    }

    public void setPlanAnualazy(LazyModel<PlanAnualPoliticaPublica> planAnualazy) {
        this.planAnualazy = planAnualazy;
    }

    public LazyModel<ActividadOperativa> getLazyActividadOperativas() {
        return lazyActividadOperativas;
    }

    public void setLazyActividadOperativas(LazyModel<ActividadOperativa> lazyActividadOperativas) {
        this.lazyActividadOperativas = lazyActividadOperativas;
    }

    public LazyModel<ReformaTraspaso> getLazyReformaTraspaso() {
        return lazyReformaTraspaso;
    }

    public void setLazyReformaTraspaso(LazyModel<ReformaTraspaso> lazyReformaTraspaso) {
        this.lazyReformaTraspaso = lazyReformaTraspaso;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getProformaPresupuestoLazy() {
        return proformaPresupuestoLazy;
    }

    public void setProformaPresupuestoLazy(LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy) {
        this.proformaPresupuestoLazy = proformaPresupuestoLazy;
    }

    public List<MasterCatalogo> getListaAnio() {
        return listaAnio;
    }

    public void setListaAnio(List<MasterCatalogo> listaAnio) {
        this.listaAnio = listaAnio;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<DetalleReformaTraspaso> getListaProductoReforma() {
        return listaProductoReforma;
    }

    public void setListaProductoReforma(List<DetalleReformaTraspaso> listaProductoReforma) {
        this.listaProductoReforma = listaProductoReforma;
    }

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<ReformaTraspaso> getListaCodReforma() {
        return listaCodReforma;
    }

    public void setListaCodReforma(List<ReformaTraspaso> listaCodReforma) {
        this.listaCodReforma = listaCodReforma;
    }

    public List<PlanNacionalEje> getEjesNacionales() {
        return ejesNacionales;
    }

    public void setEjesNacionales(List<PlanNacionalEje> ejesNacionales) {
        this.ejesNacionales = ejesNacionales;
    }

    public List<PlanNacionalObjetivo> getObjetivosNacionales() {
        return objetivosNacionales;
    }

    public void setObjetivosNacionales(List<PlanNacionalObjetivo> objetivosNacionales) {
        this.objetivosNacionales = objetivosNacionales;
    }

    public List<PlanNacionalObjetivo> getObjetivosNa() {
        return objetivosNa;
    }

    public void setObjetivosNa(List<PlanNacionalObjetivo> objetivosNa) {
        this.objetivosNa = objetivosNa;
    }

    public List<PlanNacionalPolitica> getPoliticasNacionales() {
        return politicasNacionales;
    }

    public void setPoliticasNacionales(List<PlanNacionalPolitica> politicasNacionales) {
        this.politicasNacionales = politicasNacionales;
    }

    public List<PlanNacionalPolitica> getPoliticasNa() {
        return politicasNa;
    }

    public void setPoliticasNa(List<PlanNacionalPolitica> politicasNa) {
        this.politicasNa = politicasNa;
    }

    public List<PlanLocalSistema> getSistemasLocales() {
        return sistemasLocales;
    }

    public void setSistemasLocales(List<PlanLocalSistema> sistemasLocales) {
        this.sistemasLocales = sistemasLocales;
    }

    public List<PlanLocalObjetivo> getObjetivosLocales() {
        return objetivosLocales;
    }

    public void setObjetivosLocales(List<PlanLocalObjetivo> objetivosLocales) {
        this.objetivosLocales = objetivosLocales;
    }

    public List<PlanLocalObjetivo> getObjetivosLo() {
        return objetivosLo;
    }

    public void setObjetivosLo(List<PlanLocalObjetivo> objetivosLo) {
        this.objetivosLo = objetivosLo;
    }

    public List<PlanLocalPolitica> getPoliticasLocales() {
        return politicasLocales;
    }

    public void setPoliticasLocales(List<PlanLocalPolitica> politicasLocales) {
        this.politicasLocales = politicasLocales;
    }

    public List<PlanLocalPolitica> getPoliticasLo() {
        return politicasLo;
    }

    public void setPoliticasLo(List<PlanLocalPolitica> politicasLo) {
        this.politicasLo = politicasLo;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public List<PartidasDistributivo> getListaPartidaDistributivo() {
        return listaPartidaDistributivo;
    }

    public void setListaPartidaDistributivo(List<PartidasDistributivo> listaPartidaDistributivo) {
        this.listaPartidaDistributivo = listaPartidaDistributivo;
    }

    public List<PartidasDistributivoAnexo> getListaPartidaDistributivoAnexo() {
        return listaPartidaDistributivoAnexo;
    }

    public void setListaPartidaDistributivoAnexo(List<PartidasDistributivoAnexo> listaPartidaDistributivoAnexo) {
        this.listaPartidaDistributivoAnexo = listaPartidaDistributivoAnexo;
    }

    public List<DistributivoAnexo> getListaDistributivoAnexo() {
        return listaDistributivoAnexo;
    }

    public void setListaDistributivoAnexo(List<DistributivoAnexo> listaDistributivoAnexo) {
        this.listaDistributivoAnexo = listaDistributivoAnexo;
    }

    public List<PartidasDistributivo> getListaRubros() {
        return listaRubros;
    }

    public void setListaRubros(List<PartidasDistributivo> listaRubros) {
        this.listaRubros = listaRubros;
    }

    public List<PlanProgramatico> getListaEstructura() {
        return listaEstructura;
    }

    public void setListaEstructura(List<PlanProgramatico> listaEstructura) {
        this.listaEstructura = listaEstructura;
    }

    public List<CatalogoPresupuesto> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<CatalogoPresupuesto> listaItem) {
        this.listaItem = listaItem;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<Distributivo> getListaDistributivo() {
        return listaDistributivo;
    }

    public void setListaDistributivo(List<Distributivo> listaDistributivo) {
        this.listaDistributivo = listaDistributivo;
    }

    public List<ValoresDistributivo> getListaValoresDistributivo() {
        return listaValoresDistributivo;
    }

    public void setListaValoresDistributivo(List<ValoresDistributivo> listaValoresDistributivo) {
        this.listaValoresDistributivo = listaValoresDistributivo;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }

    public List<FuenteFinanciamiento> getListaFuenteDirecta() {
        return listaFuenteDirecta;
    }

    public void setListaFuenteDirecta(List<FuenteFinanciamiento> listaFuenteDirecta) {
        this.listaFuenteDirecta = listaFuenteDirecta;
    }

    public List<PlanProgramatico> getListaPlanProgramaticoDirecta() {
        return listaPlanProgramaticoDirecta;
    }

    public void setListaPlanProgramaticoDirecta(List<PlanProgramatico> listaPlanProgramaticoDirecta) {
        this.listaPlanProgramaticoDirecta = listaPlanProgramaticoDirecta;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestosDirecta() {
        return listaCatalogoPresupuestosDirecta;
    }

    public void setListaCatalogoPresupuestosDirecta(List<CatalogoPresupuesto> listaCatalogoPresupuestosDirecta) {
        this.listaCatalogoPresupuestosDirecta = listaCatalogoPresupuestosDirecta;
    }

    public List<PartidasDistributivo> getListaVista() {
        return listaVista;
    }

    public void setListaVista(List<PartidasDistributivo> listaVista) {
        this.listaVista = listaVista;
    }

    public List<ThCargoRubros> getListaCargosRubros() {
        return listaCargosRubros;
    }

    public void setListaCargosRubros(List<ThCargoRubros> listaCargosRubros) {
        this.listaCargosRubros = listaCargosRubros;
    }

    public List<ThCargo> getListaCargos() {
        return listaCargos;
    }

    public void setListaCargos(List<ThCargo> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public boolean isBtnNuevoPapp() {
        return btnNuevoPapp;
    }

    public void setBtnNuevoPapp(boolean btnNuevoPapp) {
        this.btnNuevoPapp = btnNuevoPapp;
    }

    public boolean isRegistraNuevoPapp() {
        return registraNuevoPapp;
    }

    public void setRegistraNuevoPapp(boolean registraNuevoPapp) {
        this.registraNuevoPapp = registraNuevoPapp;
    }

    public boolean isOcultarMostarPlanNacional() {
        return ocultarMostarPlanNacional;
    }

    public void setOcultarMostarPlanNacional(boolean ocultarMostarPlanNacional) {
        this.ocultarMostarPlanNacional = ocultarMostarPlanNacional;
    }

    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public boolean isFiltroDatosNullProgramaProyectos() {
        return filtroDatosNullProgramaProyectos;
    }

    public void setFiltroDatosNullProgramaProyectos(boolean filtroDatosNullProgramaProyectos) {
        this.filtroDatosNullProgramaProyectos = filtroDatosNullProgramaProyectos;
    }

    public boolean isFiltroDatosNullTablaGeneralPlanes() {
        return filtroDatosNullTablaGeneralPlanes;
    }

    public void setFiltroDatosNullTablaGeneralPlanes(boolean filtroDatosNullTablaGeneralPlanes) {
        this.filtroDatosNullTablaGeneralPlanes = filtroDatosNullTablaGeneralPlanes;
    }

    public boolean isFiltroDatosNullTablaPlanesAnuales() {
        return filtroDatosNullTablaPlanesAnuales;
    }

    public void setFiltroDatosNullTablaPlanesAnuales(boolean filtroDatosNullTablaPlanesAnuales) {
        this.filtroDatosNullTablaPlanesAnuales = filtroDatosNullTablaPlanesAnuales;
    }

    public boolean isBolReformaT1() {
        return bolReformaT1;
    }

    public void setBolReformaT1(boolean bolReformaT1) {
        this.bolReformaT1 = bolReformaT1;
    }

    public boolean isBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public boolean isBtnContinuarTerminarTarea() {
        return btnContinuarTerminarTarea;
    }

    public void setBtnContinuarTerminarTarea(boolean btnContinuarTerminarTarea) {
        this.btnContinuarTerminarTarea = btnContinuarTerminarTarea;
    }

    public boolean isBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(boolean btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public boolean isBtnGenerarReforma() {
        return btnGenerarReforma;
    }

    public void setBtnGenerarReforma(boolean btnGenerarReforma) {
        this.btnGenerarReforma = btnGenerarReforma;
    }

    public boolean isFormBusqRegistrar() {
        return formBusqRegistrar;
    }

    public void setFormBusqRegistrar(boolean formBusqRegistrar) {
        this.formBusqRegistrar = formBusqRegistrar;
    }

    public boolean isFormBusqConsultar() {
        return formBusqConsultar;
    }

    public void setFormBusqConsultar(boolean formBusqConsultar) {
        this.formBusqConsultar = formBusqConsultar;
    }

    public boolean isBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public boolean isBtnConsultar() {
        return btnConsultar;
    }

    public void setBtnConsultar(boolean btnConsultar) {
        this.btnConsultar = btnConsultar;
    }

    public boolean isFormBusqRegistrarT2() {
        return formBusqRegistrarT2;
    }

    public void setFormBusqRegistrarT2(boolean formBusqRegistrarT2) {
        this.formBusqRegistrarT2 = formBusqRegistrarT2;
    }

    public boolean isBtnRegistrarT2() {
        return btnRegistrarT2;
    }

    public void setBtnRegistrarT2(boolean btnRegistrarT2) {
        this.btnRegistrarT2 = btnRegistrarT2;
    }

    public boolean isFormConsultaReforma() {
        return formConsultaReforma;
    }

    public void setFormConsultaReforma(boolean formConsultaReforma) {
        this.formConsultaReforma = formConsultaReforma;
    }

    public boolean isPanelDistributivo() {
        return panelDistributivo;
    }

    public void setPanelDistributivo(boolean panelDistributivo) {
        this.panelDistributivo = panelDistributivo;
    }

    public boolean isBolDistributivoGeneral() {
        return bolDistributivoGeneral;
    }

    public void setBolDistributivoGeneral(boolean bolDistributivoGeneral) {
        this.bolDistributivoGeneral = bolDistributivoGeneral;
    }

    public boolean isBolDistributivoAnexo() {
        return bolDistributivoAnexo;
    }

    public void setBolDistributivoAnexo(boolean bolDistributivoAnexo) {
        this.bolDistributivoAnexo = bolDistributivoAnexo;
    }

    public boolean isBolPartidaDirecta() {
        return bolPartidaDirecta;
    }

    public void setBolPartidaDirecta(boolean bolPartidaDirecta) {
        this.bolPartidaDirecta = bolPartidaDirecta;
    }

    public boolean isBolPapp() {
        return bolPapp;
    }

    public void setBolPapp(boolean bolPapp) {
        this.bolPapp = bolPapp;
    }

    public boolean isBtnTraspasos() {
        return btnTraspasos;
    }

    public void setBtnTraspasos(boolean btnTraspasos) {
        this.btnTraspasos = btnTraspasos;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean isBtnNuevoPartidaDirecta() {
        return btnNuevoPartidaDirecta;
    }

    public void setBtnNuevoPartidaDirecta(boolean btnNuevoPartidaDirecta) {
        this.btnNuevoPartidaDirecta = btnNuevoPartidaDirecta;
    }

    public boolean isVistaPartidaDisGeneral() {
        return vistaPartidaDisGeneral;
    }

    public void setVistaPartidaDisGeneral(boolean vistaPartidaDisGeneral) {
        this.vistaPartidaDisGeneral = vistaPartidaDisGeneral;
    }

    public boolean isBtnGenerarTraspaso() {
        return btnGenerarTraspaso;
    }

    public void setBtnGenerarTraspaso(boolean btnGenerarTraspaso) {
        this.btnGenerarTraspaso = btnGenerarTraspaso;
    }

    public boolean isEditarObserv() {
        return editarObserv;
    }

    public void setEditarObserv(boolean editarObserv) {
        this.editarObserv = editarObserv;
    }

    public Distributivo getMostrarData() {
        return mostrarData;
    }

    public void setMostrarData(Distributivo mostrarData) {
        this.mostrarData = mostrarData;
    }

    public Distributivo getDataView() {
        return dataView;
    }

    public void setDataView(Distributivo dataView) {
        this.dataView = dataView;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
    }

    public double getTotalDefinitivoIncremento() {
        return totalDefinitivoIncremento;
    }

    public void setTotalDefinitivoIncremento(double totalDefinitivoIncremento) {
        this.totalDefinitivoIncremento = totalDefinitivoIncremento;
    }

    public double getTotalDefinitivoReduccion() {
        return totalDefinitivoReduccion;
    }

    public void setTotalDefinitivoReduccion(double totalDefinitivoReduccion) {
        this.totalDefinitivoReduccion = totalDefinitivoReduccion;
    }

    public boolean isRenderedDistributivo() {
        return renderedDistributivo;
    }

    public void setRenderedDistributivo(boolean renderedDistributivo) {
        this.renderedDistributivo = renderedDistributivo;
    }

    public boolean isRenderedDistributivoAnexo() {
        return renderedDistributivoAnexo;
    }

    public void setRenderedDistributivoAnexo(boolean renderedDistributivoAnexo) {
        this.renderedDistributivoAnexo = renderedDistributivoAnexo;
    }

    public boolean isRenderedPAPP() {
        return renderedPAPP;
    }

    public void setRenderedPAPP(boolean renderedPAPP) {
        this.renderedPAPP = renderedPAPP;
    }

    public boolean isRenderedPartidaDirecta() {
        return renderedPartidaDirecta;
    }

    public void setRenderedPartidaDirecta(boolean renderedPartidaDirecta) {
        this.renderedPartidaDirecta = renderedPartidaDirecta;
    }
    
    public Boolean getSemestreMetal() {
        return semestreMetal;
    }

    public void setSemestreMetal(Boolean semestreMetal) {
        this.semestreMetal = semestreMetal;
    }

    public Boolean getTrimestreMeta() {
        return trimestreMeta;
    }

    public void setTrimestreMeta(Boolean trimestreMeta) {
        this.trimestreMeta = trimestreMeta;
    }

    public Boolean getCuatrimestreMeta() {
        return cuatrimestreMeta;
    }

    public void setCuatrimestreMeta(Boolean cuatrimestreMeta) {
        this.cuatrimestreMeta = cuatrimestreMeta;
    }

    public Boolean getMensualMeta() {
        return mensualMeta;
    }

    public void setMensualMeta(Boolean mensualMeta) {
        this.mensualMeta = mensualMeta;
    }

    public Boolean getSemestre() {
        return semestre;
    }

    public void setSemestre(Boolean semestre) {
        this.semestre = semestre;
    }

    public Boolean getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Boolean trimestre) {
        this.trimestre = trimestre;
    }

    public Boolean getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Boolean cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Boolean getMensual() {
        return mensual;
    }

    public void setMensual(Boolean mensual) {
        this.mensual = mensual;
    }
    
    public Integer getAnioActual() {
        return anioActual;
    }

    public void setAnioActual(Integer anioActual) {
        this.anioActual = anioActual;
    }

    public List<CatalogoItem> getDistribuccion() {
        return distribuccion;
    }

    public void setDistribuccion(List<CatalogoItem> distribuccion) {
        this.distribuccion = distribuccion;
    }

    public List<CatalogoItem> getDistribuccionMetaList() {
        return distribuccionMetaList;
    }

    public void setDistribuccionMetaList(List<CatalogoItem> distribuccionMetaList) {
        this.distribuccionMetaList = distribuccionMetaList;
    }

    public List<CatalogoItem> getListacomponentes() {
        return listacomponentes;
    }

    public void setListacomponentes(List<CatalogoItem> listacomponentes) {
        this.listacomponentes = listacomponentes;
    }

//</editor-fold>

    
    
}
