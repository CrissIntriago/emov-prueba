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
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
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
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Sandra
 */
@Named(value = "reformaT1View")
@ViewScoped
public class ReformaT1Controller extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReformaT1Controller.class.getName());

    //<editor-fold defaultstate="collapsed" desc="Objetos">
    private ActividadOperativa actividad;
    private OpcionBusqueda busqueda;
    private Producto producto;
    private ReformaTraspaso reformaTraspaso;
    private DetalleReformaTraspaso detalleReformaTraspaso;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualPoliticaPublica planAnualPoliticaPublica;
    private PlanAnualProgramaProyecto planAnualProgramaProyecto;
    private PlanAnualProgramaProyecto programaProyecto;
    private PlanAnualPoliticaPublica planAnual;

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

//    NUEVO
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PlanProgramaticoService estructuraService;
    @Inject
    private CatalogoPresupuestoService itemService;
    private String tipoTraspaso = "";
    private boolean panelDistributivo;
    private boolean bolPapp;
    private boolean bolPartidaDirecta;
    private boolean editar;
    private boolean btnNuevoPartidaDirecta;
    private boolean btnTraspasos;
    private LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy;
    private List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado;
    private BigDecimal totalTraspasoIncrementoPDirecta;
    private BigDecimal totalTraspasoReduccionPDirecta;
    private BigDecimal totalMontoReformadoPDirecta;
    private ProformaPresupuestoPlanificado proformaPresupuestoPlanificado;
    private List<PlanProgramatico> listaPlanProgramaticoDirecta;
    private List<FuenteFinanciamiento> listaFuenteDirecta;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestosDirecta;
    private Long numTramite;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Globales">
    private Long idUnidadA;
    private Long idPlanProgramatico;
    private Long codigo;
    private double totalSaldoDisponible;
    private double totalTraspasoReduccion;
    private double totalTraspasoIncremento;
    private double totalMontoReformado;
    private int indiceDeAcordian;
    private String programaProyectoLocal;
    private String productoGlobal;
    private String observaciones;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Lazys">
    private ProductoLazy productoLaz;
    private ProductoLazy lazyProductos;
    private LazyModel<Producto> productoLazy;
    private ProductoLazy productoAsignacion;
    private LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy;
    private LazyModel<PlanAnualPoliticaPublica> planAnualazy;
    private LazyModel<ActividadOperativa> lazyActividadOperativas;
    //ENRIQUE
    private List<CatalogoItem> listacomponentes;
    private UnidadAdministrativa unidadAdministrativa;
    private Integer anioActual;
    private List<CatalogoItem> distribuccion, distribuccionMetaList;
    private Boolean semesteMetal, trimestreMeta, cuatrimestreMeta, mensualMeta;
    private Boolean semeste, trimestre, cuatrimestre, mensual;
    private CatalogoItem estadoRegistrado;
    private List<PresPlanProgramatico> listaPresPlanProgramatico;
    private List<PresCatalogoPresupuestario> listaPresCataPresupuestario;
    private List<PresFuenteFinanciamiento> listaPresFuenteFinanciamiento;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listas">
    private List<MasterCatalogo> listaAnio;
    private List<UnidadAdministrativa> unidades;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<DetalleReformaTraspaso> listaProductoReforma;
    private List<Producto> listaProducto;
    private List<ReformaTraspaso> listaCodReforma;
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
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Booleanos">
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
    private boolean btnGenerarReformaPDI;
    private boolean formBusqRegistrar;
    private boolean formBusqConsultar;
    private boolean btnRegistrar;
    private boolean btnConsultar;
    private boolean nuevo;
    private Boolean renderBtnGenRefPAPP;
    private Boolean renderBtnGenRefPDI, disableTipoTraspaso;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Servicios">
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
    private ActividadOperativaService actividadService;
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
    //ENRIQUE
    @Inject
    private ManagerService service;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private PresPlanProgramaticoService presPlanProgramaticoService;
    @Inject
    private PresCatalogoPresupuestarioService presCataPresupuestarioService;
    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanService;
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
                detalleReformaTraspaso = new DetalleReformaTraspaso();
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                planAnualPoliticaPublica = new PlanAnualPoliticaPublica();
                planAnualProgramaProyecto = new PlanAnualProgramaProyecto();
                planAnual = new PlanAnualPoliticaPublica();
                Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
                //disableTipoTraspaso = !clienteService.isUnidadAdministrativa("PRESUPUESTO", userSession.getNameUser());
                idUnidadA = d.getUnidadAdministrativa().getId();
                //idUnidadA = new Long(62);//COMENTAR AL PASAR A PROD
                setIndiceDeAcordian(-1);
                this.totalSaldoDisponible = 0;
                this.totalTraspasoReduccion = 0;
                this.totalTraspasoIncremento = 0;
                this.totalMontoReformado = 0;
                productoLaz = new ProductoLazy(busqueda);
                lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
                productoLazy = new LazyModel(Producto.class);
                listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
                unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
                listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
                listaProductoReforma = new ArrayList<>();
                listaProducto = new ArrayList<>();
                listaCodReforma = new ArrayList<>();
                ejesNacionales = planNacionalEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
                sistemasLocales = planLocalSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
                btnNuevoPapp = true;
                registraNuevoPapp = false;
                ocultarMostarPlanNacional = false;
                bloqueo = false;
                bolReformaT1 = false;
                btnGuardar = false;
                btnContinuarTerminarTarea = true;
                btnCancelar = true;
                btnGenerarReforma = false;
                btnGenerarReformaPDI = false;
                renderBtnGenRefPAPP = Boolean.TRUE;
                formBusqConsultar = false;
                formBusqRegistrar = false;
                btnRegistrar = true;
                btnConsultar = true;
                reformaTraspaso.setPeriodo(Utils.getAnio(new Date()).shortValue());
                short anio = Utils.getAnio(new Date()).shortValue();
                busqueda.setAnio(anio);
                proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
                if (tramite == null) {
                    numTramite = ((TareasActivas) userSession.getVarTemp()).getNumTramite();
                } else {
                    numTramite = tramite.getNumTramite();
                }
                ReformaTraspaso ref = reformaTraspasoService.verificarNumTramiteReforma(BigInteger.valueOf(numTramite), busqueda.getAnio());
                if (ref == null) {
                    editar = false;
                    btnGenerarReforma = false;
                    registrarMostrarOcultar();
                } else {
                    busqueda.setAnio(ref.getPeriodo());
                    String refObservada = reformaTraspasoService.verificarEstadoNumTramiteReforma(busqueda.getAnio(), BigInteger.valueOf(numTramite));
                    if (refObservada != null) {
                        if (ref.getEstadoReformaTramite() == null || ref.getEstadoReformaTramite().getCodigo().equalsIgnoreCase("REG-REF")) {
                            reformaTraspaso = ref;
                            if (reformaTraspaso.getTipo()) {
                                listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
                                //reformaTraspasoService.getListProductoByReforma(reformaTraspaso.getPeriodo(), reformaTraspaso.getUnidadRequiriente().getId(), reformaTraspaso.getEstadoReforma().getCodigo(), true, BigInteger.valueOf(reformaTraspaso.getId()));
                                consultarMostrarOcultar();
                            }
                        } else if (refObservada.equals("OBS-REF") || refObservada.equals("OBSP-REF")) {
                            consultarMostrarOcultar();
                        }
                    } else if (ref.getEstadoReforma().getCodigo().equalsIgnoreCase("REGT")) {
                        reformaTraspaso = ref;
                        if (reformaTraspaso.getTipo()) {
                            editar = true;
                            registrarMostrarOcultar();
                            btnGenerarReforma = true;
                            btnNuevoPapp = false;
                            btnCancelar = false;
                            reformaTraspaso = ref;
                            busqueda.setAnio(reformaTraspaso.getPeriodo());
                            listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
                            if (listaProducto.isEmpty()) {
                                btnGenerarReforma = false;
                            }
                            if (btnGenerarReforma) {
                                btnGenerarReformaPDI = false;
                            } else {
                                btnGenerarReformaPDI = true;
                            }
                            //reformaTraspasoService.getListProductoByReforma(reformaTraspaso.getPeriodo(), reformaTraspaso.getUnidadRequiriente().getId(), reformaTraspaso.getEstadoReforma().getCodigo(), true, BigInteger.valueOf(reformaTraspaso.getId()));
                            actualizarTotales();
                        }
                    }
                }
                unidadAdministrativa = new UnidadAdministrativa();
                unidadAdministrativa = clienteService.getUnidadPrincipalUSer(userSession.getNameUser());
                anioActual = Utils.getAnio(new Date());
                estadoRegistrado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RP");
                btnTraspasos = false;
                bolPapp = true;
                totalTraspasoIncrementoPDirecta = BigDecimal.ZERO;
                totalTraspasoReduccionPDirecta = BigDecimal.ZERO;
                totalMontoReformadoPDirecta = BigDecimal.ZERO;
                tipoTraspaso = "PAPP";
                generarTipoTraspaso();
            } else {
                JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean disabledTipoTraspaso() {
        return clienteService.isUnidadAdministrativa("PRESUPUESTO", userSession.getNameUser());
    }

    public void generarTipoTraspaso() {
        switch (tipoTraspaso) {
            case "PAPP":
                bolPapp = Boolean.TRUE;
                bolPartidaDirecta = Boolean.FALSE;
                renderBtnGenRefPAPP = Boolean.TRUE;
                renderBtnGenRefPDI = Boolean.FALSE;
                if (editar) {
                    listaProducto = obtProdUniRespRefor(reformaTraspaso.getUnidadRequiriente().getId(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (listaProducto.isEmpty()) {
                        btnGenerarReforma = false;
                    } else {
                        btnGenerarReforma = true;
                    }
                    actualizarTotales();
                }
                break;
            case "PDI":
                bolPapp = Boolean.FALSE;
                bolPartidaDirecta = Boolean.TRUE;
                renderBtnGenRefPDI = Boolean.TRUE;
                renderBtnGenRefPAPP = Boolean.FALSE;
                if (editar) {
                    btnNuevoPartidaDirecta = false;
                    proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
                    proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
                    proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
                    listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
                    if (listaProformaPresupuestoPlanificado.isEmpty()) {
                        btnGenerarReformaPDI = false;
                    } else {
                        btnGenerarReformaPDI = true;
                    }
                    actualizarTotalesPartidaDirecta();
                }
                break;
            default:
                break;
        }
        btnTraspasos = true;
    }

    public BigDecimal obtieneTotalComprometido(String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalComprometido(partida);
        return resultado;
    }

    public BigDecimal obtieneTotalReservado(String partida) {
        BigDecimal resultado = reservaCompromisoService.obtTotalReservado(partida);
        return resultado;
    }

    public void calcularPartidaDirecta(ProformaPresupuestoPlanificado p, Boolean esIncremento) {
        if (p.getCodigoReferencia() == null) {
            p.setTraspasoReduccion(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "El valor de reducción no puede ser mayor al valor de original");
        }
        BigDecimal montoReformado = p.getValor().add(p.getTraspasoIncremento()).subtract(p.getTraspasoReduccion());
        if (p.getTraspasoIncremento().doubleValue() > 0 && p.getTraspasoReduccion().doubleValue() > 0) {
            if (esIncremento) {
                p.setTraspasoIncremento(BigDecimal.ZERO);
            } else {
                p.setTraspasoReduccion(BigDecimal.ZERO);
            }
            proformaPresupuestoPlanificadoService.edit(p);
            actualizarTotalesPartidaDirecta();
            PrimeFaces.current().ajax().update("proformaEgresos");
            JsfUtil.addWarningMessage("TRASPASO", "No se puede realizar un incremento y reducción de la misma partida a la vez.");
            return;
        }
        if (p.getTraspasoReduccion().doubleValue() > p.getValor().subtract(obtieneTotalComprometido(p.getPartidaPresupuestaria())).subtract(obtieneTotalReservado(p.getPartidaPresupuestaria())).doubleValue()) {
            p.setTraspasoReduccion(BigDecimal.ZERO);
            proformaPresupuestoPlanificadoService.edit(p);
            actualizarTotalesPartidaDirecta();
            PrimeFaces.current().ajax().update("proformaEgresos");
            JsfUtil.addWarningMessage("TRASPASO", "La reducción no puede ser mayor al saldo disponible");
            return;
        }
        p.setReformaCodificado(montoReformado);
        proformaPresupuestoPlanificadoService.edit(p);
        actualizarTotalesPartidaDirecta();
        PrimeFaces.current().ajax().update("formReformaPartidaDirecta");
        JsfUtil.addInformationMessage("Exitoso", "El registro se actualizo correctamente");
    }

    public void abriDlgPartidas() {
        listaPresPlanProgramatico = presPlanProgramaticoService.getEstructurasSubProg();
        listaPresCataPresupuestario = presCataPresupuestarioService.getCatPresMovimiento();
        listaPresFuenteFinanciamiento = presFuenteFinanService.getFteFinanTrue();
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').show()");
        PrimeFaces.current().ajax().update(":formPresegresoPartidas");
    }

    public void savePdReforma() {
        if (proformaPresupuestoPlanificado.getEstructruaNew() == null || proformaPresupuestoPlanificado.getFuenteNew() == null || proformaPresupuestoPlanificado.getItemNew() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Los Campos no deben estar vacios");
            return;
        }
//        if (proformaPresupuestoPlanificado.getValor() == null) {
//            PrimeFaces.current().ajax().update("messages");
//            JsfUtil.addErrorMessage("Error", "Ingrese un valor ");
//            return;
//        }
//        if (proformaPresupuestoPlanificado.getValor().doubleValue() < 1) {
//            PrimeFaces.current().ajax().update("messages");
//            JsfUtil.addErrorMessage("Error", "Ingrese un valor mayor a cero");
//            return;
//        }
        if (proformaPresupuestoPlanificado.getId() == null) {
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructruaNew().getCodigo() + proformaPresupuestoPlanificado.getItemNew().getCodigo() + proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente());
            proformaPresupuestoPlanificado.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
            proformaPresupuestoPlanificado.setCondicion(false);
            proformaPresupuestoPlanificado.setGenerado(true);
            proformaPresupuestoPlanificado.setCodigo("PDI");
            proformaPresupuestoPlanificado.setEstado(true);
            proformaPresupuestoPlanificado.setValor(BigDecimal.ZERO);
            proformaPresupuestoPlanificado.setUsuarioCreacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaCreacion(new Date());
            proformaPresupuestoPlanificado.setPeriodo(reformaTraspaso.getPeriodo());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado = proformaPresupuestoPlanificadoService.create(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        } else {
            proformaPresupuestoPlanificado.setPartidaPresupuestaria(proformaPresupuestoPlanificado.getEstructruaNew().getCodigo() + proformaPresupuestoPlanificado.getItemNew().getCodigo() + proformaPresupuestoPlanificado.getFuenteNew().getTipoFuente().getOrden());
            proformaPresupuestoPlanificado.setFuenteDirecta(proformaPresupuestoPlanificado.getFuente().getTipoFuente());
            proformaPresupuestoPlanificado.setUsuarioModificacion(userSession.getNameUser());
            proformaPresupuestoPlanificado.setFechaModificacion(new Date());
            proformaPresupuestoPlanificado.setTraspasoIncremento(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificado.setReformaCodificado(proformaPresupuestoPlanificado.getValor());
            proformaPresupuestoPlanificadoService.edit(proformaPresupuestoPlanificado);
            proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        }
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Exitoso", "El registro se realizo correctamente");
        PrimeFaces.current().executeScript("PF('DlgproformaRegistroPartidas').hide()");
        PrimeFaces.current().ajax().update(":formReformaPartidaDirecta");
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
                pro = new ProformaPresupuestoPlanificado();
                pro.setId(null);
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
                pro.setGenerado(item.getGenerado());
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
                proformaPresupuestoPlanificadoService.create(pro);
            }
        }
        proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
        proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
        proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        this.listaFuenteDirecta = fuenteService.findByNamedQuery("FuenteFinanciamiento.findEstadoValido", true, reformaTraspaso.getPeriodo());
        this.listaPlanProgramaticoDirecta = estructuraService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), reformaTraspaso.getPeriodo());
        listaPresPlanProgramatico = presPlanProgramaticoService.getEstructurasSubProg();
        this.listaCatalogoPresupuestosDirecta = itemService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), reformaTraspaso.getPeriodo());
        btnNuevoPartidaDirecta = false;
    }

    public void actualizarTotalesPartidaDirecta() {
        totalTraspasoIncrementoPDirecta = BigDecimal.ZERO;
        totalTraspasoReduccionPDirecta = BigDecimal.ZERO;
        totalMontoReformadoPDirecta = BigDecimal.ZERO;
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
            for (ProformaPresupuestoPlanificado item : listaProformaPresupuestoPlanificado) {
                totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta.add(item.getTraspasoIncremento());
                totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta.add(item.getTraspasoReduccion());
                totalMontoReformadoPDirecta = totalMontoReformadoPDirecta.add(item.getReformaCodificado());
            }
        }
    }

    public List<Producto> obtProdUniRespRefor(Long unidadResp, BigInteger codReforma) {
        List<Producto> result = reformaTraspasoService.getListProductoByReforma2(unidadResp, codReforma);
        return result;
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

    public void openActividadOperativa(ActividadOperativa a) {
        if (a == null) {
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

    public void saveActividadOperativaReforma() {
        try {
            boolean edit = actividad.getId() != null;
            if (programaProyecto != null && programaProyecto.getId() != null) {
                actividad.setPlanProgramaProyecto(programaProyecto);
            }
            if (actividad.sumaDsitribuccionMeta()) {
                if (actividad.sumaDsitribuccion()) {
                    if (!edit) {
                        if (actividadService.getListaVerificacionActividades(actividad.getNombreActividad(), busqueda.getAnio())) {
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
                        actividadService.create(actividad);
                        PrimeFaces.current().ajax().update("formActividadReforma");
                    } else {
                        BigDecimal valorEspecifico = actividadService.verificandoProductosActividades(actividad);
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
    }

    public void loadingActividadLazy() {
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", reformaTraspaso.getUnidadRequiriente());
    }

    public void loadingProgramas() {
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("estado", true);
        programaProyecto = new PlanAnualProgramaProyecto();
    }

    public void seleccionProyecto(PlanAnualProgramaProyecto proyecto) {
        programaProyecto = new PlanAnualProgramaProyecto();
        programaProyecto = proyecto;
    }

    public void renderizarDistribuccionMeta() {
        semesteMetal = Boolean.FALSE;
        trimestreMeta = Boolean.FALSE;
        cuatrimestreMeta = Boolean.FALSE;
        mensualMeta = Boolean.FALSE;
        if (actividad.getDistribucionMeta() != null) {

            switch (actividad.getDistribucionMeta().getCodigo()) {
                case "S":
                    semesteMetal = Boolean.TRUE;
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
                    semesteMetal = Boolean.FALSE;
                    trimestreMeta = Boolean.FALSE;
                    cuatrimestreMeta = Boolean.FALSE;
                    mensualMeta = Boolean.FALSE;
                    break;
            }
        }
    }

    public void renderizarDistribuccion() {
        semeste = Boolean.FALSE;
        trimestre = Boolean.FALSE;
        cuatrimestre = Boolean.FALSE;
        mensual = Boolean.FALSE;
        if (actividad.getTipoDistribuccion() != null) {

            switch (actividad.getTipoDistribuccion().getCodigo()) {
                case "S":
                    semeste = Boolean.TRUE;
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
                    semeste = Boolean.FALSE;
                    trimestre = Boolean.FALSE;
                    cuatrimestre = Boolean.FALSE;
                    mensual = Boolean.FALSE;
                    break;
            }
        }

    }

    public void registrarMostrarOcultar() {
        formBusqConsultar = false;
        formBusqRegistrar = true;
        btnRegistrar = false;
        btnConsultar = false;
        btnGenerarReforma = false;
        btnGenerarReformaPDI = false;
        nuevo = true;
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
        PrimeFaces.current().ajax().update("formBusquedaCons");
        PrimeFaces.current().ajax().update("formBusquedaCons:fsetBusquedaCons");
        PrimeFaces.current().ajax().update("formTablaReforma");
    }

    public void consultarMostrarOcultar() {
        formBusqConsultar = true;
        formBusqRegistrar = false;
        btnRegistrar = false;
        btnConsultar = false;
        btnGuardar = Boolean.TRUE;
        nuevo = false;
        listaProducto = new ArrayList<>();
        reformaTraspaso = new ReformaTraspaso();
        short anio = Utils.getAnio(new Date()).shortValue();
        this.totalSaldoDisponible = 0;
        this.totalTraspasoReduccion = 0;
        this.totalTraspasoIncremento = 0;
        this.totalMontoReformado = 0;
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        CatalogoItem estadoReformRegistrada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
        CatalogoItem estadoReformObservada = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBS-REF");
        CatalogoItem estadoReformObservadaP = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "OBSP-REF");
        listaCodReforma = reformaTraspasoService.getListReformaT1(busqueda.getAnio(), estadoReform, Boolean.TRUE, estadoReformRegistrada, estadoReformObservada, estadoReformObservadaP, BigInteger.valueOf(numTramite));
    }

    public void buscarEditar() {
        if (reformaTraspaso.getId() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un código de Reforma");
            return;
        }
        if (reformaTraspaso.getPeriodo() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese un Período");
            return;
        }
        reformaTraspaso = reformaTraspasoService.findByNamedQuery1("ReformaTraspaso.findById", reformaTraspaso.getId());
        bolReformaT1 = Boolean.FALSE;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.FALSE;
        btnGenerarReforma = Boolean.TRUE;
//        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
//        reformaTraspaso.setEstadoReforma(estadoReform);
        try {
            idUnidadA = reformaTraspaso.getUnidadRequiriente().getId();
        } catch (Exception e) {
            JsfUtil.addWarningMessage("ERROR", "No tiene una unidad");
        }
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
        listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
        for (Producto producto1 : listaProducto) {
            BigDecimal saldoDisponible = producto1.getMonto().subtract(producto1.getReserva());
            producto1.setSaldoDisponible(saldoDisponible);
        }
        listaProformaPresupuestoPlanificado = detalleReformaTraspasoService.getListPartidaDirectaReforma(reformaTraspaso.getPeriodo(), BigInteger.valueOf(reformaTraspaso.getId()));
        if (listaProducto.isEmpty() && !listaProformaPresupuestoPlanificado.isEmpty()) {
            bolPapp = Boolean.FALSE;
            bolPartidaDirecta = Boolean.TRUE;
            renderBtnGenRefPDI = Boolean.TRUE;
            renderBtnGenRefPAPP = Boolean.FALSE;
            btnNuevoPartidaDirecta = false;
            proformaPresupuestoLazy = new LazyModel(ProformaPresupuestoPlanificado.class);
            proformaPresupuestoLazy.getFilterss().put("periodo:equal", reformaTraspaso.getPeriodo());
            proformaPresupuestoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
            if (listaProformaPresupuestoPlanificado.isEmpty()) {
                btnGenerarReformaPDI = false;
            } else {
                btnGenerarReformaPDI = true;
            }
            actualizarTotalesPartidaDirecta();
            btnTraspasos = true;
            JsfUtil.update("formReformaPartidaDirecta");
            JsfUtil.update("botonesReforma");
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
//        this.TotalProducto = productoService.getSumaTotalProductos(null, busqueda.getAnio()); YA ESTABA
//        this.totalActividades = productoService.getSumaTotalActividad(null, busqueda.getAnio());

//        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(busqueda.getAnio(), false, true);
//        if (li.size() > 0) {
//            setBloqueo(true);
//        } else {
//            setBloqueo(false);
//
//        }
        PrimeFaces.current().ajax().update("formTablaReforma");
    }

    public void filtroDeDatosNullPlanesAnualesAsignacionPartida() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":dataTReformaT1");
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
        if (nuevo) {
            listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
            if (!listaProducto.isEmpty()) {
                CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
                List<PlanLocalProgramaProyecto> planLocalListC = reformaTraspasoService.getReformaPlanLocalProgramaProyecto(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<PlanAnualPoliticaPublica> planAnualPoliticaListC = reformaTraspasoService.getReformaPlanPoliticaPublica(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<PlanAnualProgramaProyecto> planAnualProgramaListC = reformaTraspasoService.getReformaPlanAnulaProgramaProyecto(busqueda.getAnio(), estado2, true, BigInteger.valueOf(reformaTraspaso.getId()));
                List<ActividadOperativa> actividadListC = reformaTraspasoService.getReformaActividadOperativa(busqueda.getAnio(), estado2, true, idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
                for (Producto producto1 : listaProducto) {
                    productoService.remove(producto1);
                }
                for (ActividadOperativa actividad : actividadListC) {
                    actividadService.remove(actividad);
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
            }
            reformaTraspasoService.remove(reformaTraspaso);
            btnGenerarReforma = Boolean.FALSE;
            JsfUtil.addInformationMessage("Reforma", "Reforma Cancelada Correctamente");
        }
        reformaTraspaso = new ReformaTraspaso();
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        actividad = new ActividadOperativa();
        busqueda = new OpcionBusqueda();
        producto = new Producto();
        setIndiceDeAcordian(-1);
        short anio = Utils.getAnio(new Date()).shortValue();
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
        listaProducto = new ArrayList<>();
        btnNuevoPapp = true;
        registraNuevoPapp = false;
        ocultarMostarPlanNacional = false;
        btnGuardar = true;
        btnContinuarTerminarTarea = true;
        btnCancelar = true;
        this.totalSaldoDisponible = 0;
        this.totalTraspasoReduccion = 0;
        this.totalTraspasoIncremento = 0;
        this.totalMontoReformado = 0;
        totalTraspasoIncrementoPDirecta = BigDecimal.ZERO;
        totalTraspasoReduccionPDirecta = BigDecimal.ZERO;
        totalMontoReformadoPDirecta = BigDecimal.ZERO;
    }

    public void generarReforma(boolean tipo) {
        if (null != tipoTraspaso) {
            if (idUnidadA == null) {
                JsfUtil.addErrorMessage("ERROR", "Ingrese una Unidad Administrativa");
                return;
            }
            bolReformaT1 = Boolean.FALSE;
            btnGuardar = Boolean.FALSE;
            btnContinuarTerminarTarea = Boolean.TRUE;
            btnCancelar = Boolean.FALSE;
            btnGenerarReforma = Boolean.TRUE;
            reformaTraspaso.setPeriodo(busqueda.getAnio());
            reformaTraspaso.setNumeracion(reformaTraspasoService.maxNumeracion(tipo, busqueda.getAnio()));
            String codigos = Utils.completarCadenaConCeros(reformaTraspaso.getNumeracion().toString(), 3);
            reformaTraspaso.setCodigo("T1-" + codigos + "-" + reformaTraspaso.getPeriodo());
            reformaTraspaso.setFechaTraspasoReforma(new Date());
            CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
            reformaTraspaso.setEstadoReforma(estadoReform);
            if (busqueda.getAnio() == null) {
                short anio = 0;
                busqueda.setAnio(anio);
            }
            productoLaz = new ProductoLazy(busqueda);
            listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
            listaProducto = reformaTraspasoService.getListProducto2(busqueda.getAnio(), idUnidadA, true);
            btnNuevoPapp = Boolean.FALSE;
            UnidadAdministrativa unidad = reformaTraspasoService.getUnidadById(idUnidadA);
            detalleReformaTraspaso = new DetalleReformaTraspaso();
            reformaTraspaso.setUnidadRequiriente(unidad);
            reformaTraspaso.setTipo(tipo);
            reformaTraspaso.setEstado(Boolean.TRUE);
            reformaTraspaso.setUsuarioCreacion(userSession.getNameUser());
            reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
            reformaTraspaso.setFechaCreacion(new Date());
            reformaTraspaso.setFechaModificacion(new Date());
            reformaTraspaso.setNumTramite(BigInteger.valueOf(numTramite));
            ReformaTraspaso ref = reformaTraspasoService.verificarNumTramiteReforma(BigInteger.valueOf(numTramite), busqueda.getAnio());
            if (null == ref) {
                reformaTraspaso = reformaTraspasoService.create(reformaTraspaso);
            } else {
                reformaTraspaso = ref;
            }
            switch (tipoTraspaso) {
                case "PAPP":
                    CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
                    listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
                    if (listaProducto == null || listaProducto.isEmpty()) {
                        crearPapp(reformaTraspaso.getPeriodo(), estado, true, reformaTraspaso.getId());
                    }
                    listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
//                    for (Producto producto1 : listaProducto) {
//                        BigDecimal saldoDisponible = producto1.getMonto().subtract(producto1.getReserva());
//                        producto1.setSaldoDisponible(saldoDisponible);
//                    }
                    actualizarTotales();
                    JsfUtil.update("formTablaReforma");
                    JsfUtil.update("dataTReformaT1");
                    break;
                case "PDI":
                    panelDistributivo = false;
                    bolPartidaDirecta = Boolean.TRUE;
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
                    PrimeFaces.current().ajax().update("formReformaPartidaDirecta");
                    PrimeFaces.current().ajax().update("proformaEgresos");
                    break;
            }
        }
    }

    public void generarReformaOld(boolean tipo) {
        if (idUnidadA == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Unidad Administrativa");
            return;
        }
        bolReformaT1 = Boolean.FALSE;
        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.FALSE;
        btnGenerarReforma = Boolean.TRUE;
        reformaTraspaso.setPeriodo(busqueda.getAnio());
        reformaTraspaso.setNumeracion(reformaTraspasoService.maxNumeracion(tipo, busqueda.getAnio()));
        String codigos = Utils.completarCadenaConCeros(reformaTraspaso.getNumeracion().toString(), 3);
        reformaTraspaso.setCodigo("T1-" + codigos + "-" + reformaTraspaso.getPeriodo());
        reformaTraspaso.setFechaTraspasoReforma(new Date());
        CatalogoItem estadoReform = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REGT");
        reformaTraspaso.setEstadoReforma(estadoReform);

        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }
        productoLaz = new ProductoLazy(busqueda);

        listaPlanProgramatico = planProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        listaProducto = reformaTraspasoService.getListProducto2(busqueda.getAnio(), idUnidadA, true);
        btnNuevoPapp = Boolean.FALSE;

        UnidadAdministrativa unidad = reformaTraspasoService.getUnidadById(idUnidadA);
        detalleReformaTraspaso = new DetalleReformaTraspaso();
        reformaTraspaso.setUnidadRequiriente(unidad);
        reformaTraspaso.setTipo(tipo);
        reformaTraspaso.setEstado(Boolean.TRUE);
        reformaTraspaso.setUsuarioCreacion(userSession.getNameUser());
        reformaTraspaso.setUsuarioModificacion(userSession.getNameUser());
        reformaTraspaso.setFechaCreacion(new Date());
        reformaTraspaso.setFechaModificacion(new Date());
        reformaTraspaso.setNumTramite(BigInteger.valueOf(numTramite));
        reformaTraspaso = reformaTraspasoService.create(reformaTraspaso);
        CatalogoItem estado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
        crearPapp(reformaTraspaso.getPeriodo(), estado, true, reformaTraspaso.getId());
        listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
        //reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        for (Producto producto1 : listaProducto) {
            BigDecimal saldoDisponible = producto1.getMonto().subtract(producto1.getReserva());
            producto1.setSaldoDisponible(saldoDisponible);
        }
        actualizarTotales();
        PrimeFaces.current().ajax().update("formTablaReforma");
        PrimeFaces.current().ajax().update("dataTReformaT1");
    }

    public void crearPapp(Short periodo, CatalogoItem c, Boolean estado, Long idReforma) {
        try {
            List<ActividadOperativa> actividadList = actividadService.getReformaActividadOperativaByUnidad(periodo, c, estado, idUnidadA);
            List<Producto> productoList = reformaTraspasoService.getListProducto3(periodo, idUnidadA, c, estado);
            CatalogoItem estado2 = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RR");
            if (actividadList != null && !actividadList.isEmpty()) {
                for (ActividadOperativa data4 : actividadList) {
                    this.actividad = new ActividadOperativa();
                    this.actividad = Utils.clone(data4);
                    this.actividad.setId(null);
                    this.actividad.setEstado(true);
                    this.actividad.setFechaCreacion(data4.getFechaCreacion());
                    this.actividad.setUsuarioCreacion(data4.getUsuarioCreacion());
                    this.actividad.setFechaModificacion(data4.getFechaModificacion());
                    this.actividad.setUsuarioModifica(data4.getUsuarioModifica());
                    this.actividad.setCodigoReformaTraspaso(BigInteger.valueOf(idReforma));
                    this.actividad.setNumeroOrdenId(BigInteger.valueOf(data4.getId()));
                    this.actividad.setNumeroTramite(null);
                    this.actividad.setMonotReformado(data4.getMonotReformado());
                    this.actividad.setNumeroTramite(numTramite.shortValue());
                    this.actividad.setEstadoPapp(estado2);
                    this.actividad = actividadService.create(actividad);
                }
            }
            if (productoList != null && !productoList.isEmpty()) {
                for (Producto data5 : productoList) {
                    ActividadOperativa actividad = actividadService.showActividadByTraspaso(BigInteger.valueOf(idReforma), BigInteger.valueOf(data5.getActividadOperativa().getId()));
                    this.producto = new Producto();
                    this.producto = Utils.clone(data5);
                    this.producto.setId(null);
                    this.producto.setEstado(true);
                    this.producto.setMonto(data5.getMontoReformada());
                    this.producto.setActividadOperativa(actividad);
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
                    this.producto.setCodigoReformaTraspaso(BigInteger.valueOf(idReforma));
                    this.producto.setNumeroOrdenId(BigInteger.valueOf(data5.getId()));
                    this.producto.setNumeroTramite(numTramite.shortValue());
                    this.producto.setEstadoPapp(estado2);
                    this.producto = productoService.create(producto);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error " + e);
        }

    }

    public void nuevoPAPP() {
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser()); //ENRIQUE QUITAR COMENTARIO AL PASAR A PROD
        //Distributivo d = new Distributivo();// COMENTAR AL PASAR A PROD
        //d.setUnidadAdministrativa(new UnidadAdministrativa(idUnidadA));// COMENTAR AL PASAR A PROD
        lazyProductos = new ProductoLazy(busqueda, BigInteger.valueOf(reformaTraspaso.getId()), true);
        planAnualazy = new LazyModel(PlanAnualPoliticaPublica.class);
        planAnualazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        productoLazy = new LazyModel(Producto.class);
        productoLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        productoLazy.getFilterss().put("actividadOperativa.unidadResponsable:equal", d.getUnidadAdministrativa());
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", d.getUnidadAdministrativa());
        registraNuevoPapp = true;
        programaProyecto = new PlanAnualProgramaProyecto();
        PrimeFaces.current().executeScript("PF('dlgPapp').show()");
        PrimeFaces.current().ajax().update(":actividadesOperativa");
        PrimeFaces.current().ajax().update(":productos");
    }

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
                    PrimeFaces.current().ajax().update(":formPlanes");
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
                PrimeFaces.current().ajax().update(":formPlanes");
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
        PrimeFaces.current().ajax().update(":formPlanes");
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
                List<ActividadOperativa> lista2 = actividadService.findByNamedQuery("ActividadOperativa.findByVerificarHijos", new Object[]{b.getId()});
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

    public void actualizarCbObjetivo() {
        this.objetivosNacionales = planNacionalObjetivoService.findByNamedQuery("PlanNacionalObjetivo.findByFiltroPolitica", new Object[]{ejeNacionalSeleccionado});
    }

    public void actualizarCbPlotica() {
        this.politicasNacionales = planNacionalPoliticaService.findByNamedQuery("PlanNacionalPolitica.findFiltrarPolitica", new Object[]{objetivoNacionalSeleccionado});
    }

    public void actualizarObjetivoLocal() {
        this.objetivosLocales = planLocalObjetivoService.findByNamedQuery("PlanLocalObjetivo.findByObjetivo", new Object[]{sistemaLocalSeleccionado});
    }

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
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        actividad.setPlanProgramaProyecto(p);
        actividad.setUnidadResponsable(d.getUnidadAdministrativa());
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
            planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class
            );
            planAnualProgramaLazy.getFilterss().put("codigoReformaTraspaso:equal", BigInteger.valueOf(reformaTraspaso.getId()));
            PrimeFaces.current().ajax().update(":planProgramaProyectos");
        }
    }

    public void noAplica() {
        programaProyecto.setPlanAnual(null);
        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
    }

    public void savePlanNombreProyecto() {
        if (programaProyecto.getNombreProgramaProyecto() == null) {
            JsfUtil.addWarningMessage("AVISO", "NECESITA INGRESAR O SELECCIONAR UN NOMBRE DEL PLAN ANUAL, PROGRAMA, PROYECTO");
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
                        if (actividadService.getListaVerificacionActividadesReformaTraspaso(actividad.getNombreActividad(), BigInteger.valueOf(reformaTraspaso.getId()))) {
                            JsfUtil.addWarningMessage("AVISO", "ESTA ACTIVIDAD OPERATIVA YA FUE CREADA");
                            return;
                        }
                        actividad.setEstado(true);
                        actividad.setFechaCreacion(new Date());
                        actividad.setUsuarioCreacion(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setMonto(BigDecimal.ZERO);
                        actividad.setCuatrimestre1Actividad(BigDecimal.ZERO);
                        actividad.setCuatrimestre2Actividad(BigDecimal.ZERO);
                        actividad.setCuatrimestre3Actividad(BigDecimal.ZERO);
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                        actividad.setEstadoPapp(estadoReg);
                        this.actividad = actividadService.create(actividad);
                    } else {

                        actividad.setEstado(true);
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividadService.edit(actividad);
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
        actividad = a;
        programaProyecto = a.getPlanProgramaProyecto();
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
        System.out.println("Actividad Operativa " + a);
        actividadService.removeActividad(a);
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":productos");
        JsfUtil.addInformationMessage("Información", a.getNombreActividad() + " eliminado con éxito.");

    }

//    public Boolean calcularActividadMonto() {
//        boolean verificacion = true;
//
//        String cuat1 = String.valueOf(actividad.getCuatrimestre2Actividad());
//        String cuat2 = String.valueOf(actividad.getCuatrimestre1Actividad());
//        String cuat3 = String.valueOf(actividad.getCuatrimestre3Actividad());
//        String monto = String.valueOf(actividad.getMonto());
//        double num1, num2, num3, result, resultado;
//
//        num1 = Double.valueOf(cuat1);
//        num2 = Double.valueOf(cuat2);
//        num3 = Double.valueOf(cuat3);
//        result = num1 + num2 + num3;
//        resultado = Double.valueOf(monto);
//        if (result == resultado) {
//            verificacion = true;
//        } else {
//            verificacion = false;
//        }
//
//        return verificacion;
//    }
/*VERIFICAR*/
//    public Boolean calcularSumaActividades() {
//
//        boolean verificacion = true;
//        String cuat1 = String.valueOf(actividad.getCuatrimestre1Meta());
//        String cuat2 = String.valueOf(actividad.getCuatrimestre2Meta());
//        String cuat3 = String.valueOf(actividad.getCuatrimestre3Meta());
//        String meta = String.valueOf(actividad.getMedicionMeta());
//        double num1, num2, num3, result, medicionMeta;
//
//        num1 = Double.valueOf(cuat1);
//        num2 = Double.valueOf(cuat2);
//        num3 = Double.valueOf(cuat3);
//        result = num1 + num2 + num3;
//        medicionMeta = Double.valueOf(meta);
//
//        if (result == medicionMeta) {
//            verificacion = true;
//        } else {
//            verificacion = false;
//        }
//        return verificacion;
//    }
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
        PrimeFaces.current().ajax().update("formTablaReforma");
        PrimeFaces.current().ajax().update("dataTReformaT1");
        JsfUtil.addInformationMessage("Información", p.getDescripcion() + " eliminado con éxito.");

    }

    public void saveProducto() {
        if (producto.getDescripcion() == null || producto.getDescripcion() == "") {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Escriba el nombre del producto");
            return;
        }
        try {
            boolean edit = producto.getId() != null;
            if (!edit) {
                producto.setMonto(BigDecimal.ZERO);
                BigDecimal monto = producto.getMonto();
                producto.setEstado(true);
                producto.setFechaCreacion(new Date());
                producto.setFechaModificacion(new Date());
                producto.setUsuarioCreacion(userSession.getNameUser());
                producto.setUsuarioModifica(userSession.getNameUser());
                producto.setPeriodo(busqueda.getAnio());
                producto.setReserva(BigDecimal.ZERO);
                producto.setCodigoReformaTraspaso(BigInteger.valueOf(reformaTraspaso.getId()));
                producto.setSuplementoEgreso(BigDecimal.ZERO);
                producto.setTraspasoIncremento(BigDecimal.ZERO);
                producto.setTraspasoReduccion(BigDecimal.ZERO);
                producto.setReduccionEgreso(BigDecimal.ZERO);
                producto.setMontoReformada(monto);
                producto.setSaldoDisponible(monto);
                this.producto = productoService.create(producto);
                PrimeFaces.current().ajax().update(":formMain");
                PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                PrimeFaces.current().ajax().update(":dataTReformaT1");
                PrimeFaces.current().ajax().update(":formProducto");
                PrimeFaces.current().ajax().update("messages");

                JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                producto = new Producto();
            } else {
                producto.setUsuarioModifica(userSession.getNameUser());
                producto.setFechaModificacion(new Date());
                producto.setSuplementoEgreso(BigDecimal.ZERO);
                producto.setReduccionEgreso(BigDecimal.ZERO);
                producto.setReserva(BigDecimal.ZERO);
                producto.setTraspasoIncremento(producto.getMonto());
                producto.setTraspasoReduccion(BigDecimal.ZERO);
                producto.setMontoReformada(producto.getMonto());
                producto.setSaldoDisponible(producto.getMonto());
                productoService.edit(producto);
                PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                PrimeFaces.current().ajax().update(":formProducto");
                PrimeFaces.current().ajax().update("messages");
                PrimeFaces.current().ajax().update(":dataTReformaT1");
                JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                producto = new Producto();

                PrimeFaces.current().ajax().update("formMain");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "Los campos no pueden estar vacios");
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
//            cupConfirmar = verficarCupoSuplmento(producto.getCodigoReforma());
//            cupConfirmar = cupConfirmar.add(producto.getSuplementoEgreso());

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
        //listaProducto = reformaTraspasoService.getListProductoByReforma(busqueda.getAnio(), idUnidadA, "REGT", true, BigInteger.valueOf(reformaTraspaso.getId()));
        listaProducto = obtProdUniRespRefor(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
        //reformaTraspasoService.getListProductoByReforma2(idUnidadA, BigInteger.valueOf(reformaTraspaso.getId()));
        PrimeFaces.current().executeScript("PF('dlgPapp').hide()");
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
        for (int j = 0; j < 10; j++) {

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
        if (reduccion != 0) {
            if (reduccion > saldoDisponible) {
                JsfUtil.addErrorMessage("ERROR", "Monto no puede ser reducido a menos del Saldo Disponible");
                p.setTraspasoReduccion(BigDecimal.ZERO);
                BigDecimal montoReformadoIncrementos = p.getMonto().add(p.getTraspasoIncremento());
                BigDecimal montoReformadoReduccions = montoReformadoIncrementos.subtract(p.getTraspasoReduccion());
                p.setMontoReformada(montoReformadoReduccions);
                productoService.edit(p);
                actualizarTotales();
                return;
            }
        }
        productoService.edit(p);
        actualizarTotales();
        JsfUtil.update("formTablaReforma");
        if (p.getActividadOperativa().getNumeroOrdenId() != null) {
            int a = reformaTraspasoService.actualizarValorActividad(p.getActividadOperativa());
        }
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

    public void saveReformaTraspaso() {
        actualizarTotales();
        actualizarTotalesPartidaDirecta();
        if (reformaTraspaso.getId() == null) {
            JsfUtil.addErrorMessage("REFORMA", "Genere las reforma correspondiente");
            return;
        }
        BigDecimal traspasoIncr = BigDecimal.valueOf(totalTraspasoIncremento).setScale(2, RoundingMode.HALF_UP);
        BigDecimal traspasoReduc = BigDecimal.valueOf(totalTraspasoReduccion).setScale(2, RoundingMode.HALF_UP);
        if (traspasoIncr.compareTo(traspasoReduc) != 0) {
            JsfUtil.addErrorMessage("ERROR", "El total del traspaso de incremento debe coincidir con el total del traspaso de reducción");
            return;
        }
        if (Utils.isNotEmpty(listaProformaPresupuestoPlanificado)) {
            BigDecimal traspasoIncrPDI = totalTraspasoIncrementoPDirecta.setScale(2, RoundingMode.HALF_UP);
            BigDecimal traspasoReducPDI = totalTraspasoReduccionPDirecta.setScale(2, RoundingMode.HALF_UP);
            if (traspasoIncrPDI.compareTo(traspasoReducPDI) != 0) {
                JsfUtil.addErrorMessage("ERROR", "El total del traspaso de incremento debe coincidir con el total del traspaso de reducción Partidas Directas");
                return;
            }
        }
        bolReformaT1 = Boolean.TRUE;
        btnGuardar = Boolean.TRUE;
        btnContinuarTerminarTarea = Boolean.FALSE;
        btnCancelar = Boolean.TRUE;
        btnGenerarReforma = Boolean.TRUE;
        btnNuevoPapp = Boolean.TRUE;
        btnNuevoPartidaDirecta = Boolean.TRUE;
        reformaTraspaso.setEstadoReformaTramite(null);
        reformaTraspaso.setFechaModificacion(new Date());
        reformaTraspaso.setNumTramite(BigInteger.valueOf(numTramite));
        reformaTraspasoService.edit(reformaTraspaso);
        JsfUtil.update("botonesReforma");
        JsfUtil.update("formReformaPartidaDirecta");
        JsfUtil.addInformationMessage("Exitoso", "Registro" + ((formBusqRegistrar ? " guardado" : " editado") + " con éxito."));
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
        short anio = Utils.getAnio(new Date()).shortValue();
        busqueda.setAnio(anio);
        reformaTraspaso.setPeriodo(busqueda.getAnio());

        totalSaldoDisponible = 0;
        totalTraspasoIncremento = 0;
        totalTraspasoReduccion = 0;
        totalMontoReformado = 0;

        btnGuardar = Boolean.FALSE;
        btnContinuarTerminarTarea = Boolean.TRUE;
        btnCancelar = Boolean.TRUE;
        btnGenerarReforma = Boolean.FALSE;
        btnNuevoPapp = Boolean.TRUE;
    }

    public void dlogoObservaciones() {
        if (busqueda.getAnio() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Eliga un Período");
            return;
        }

        String tareaName = "";
        if (tarea == null) {
            tareaName = ((TareasActivas) userSession.getVarTemp()).getName();
        } else {
            tareaName = tarea.getName();
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tareaName);
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {
        try {
            CatalogoItem estadopapp = catalogoService.getItemByCatalogoAndCodigo("estado_reforma_tramite", "REG-REF");
            reformaTraspaso.setEstadoReformaTramite(estadopapp);
            reformaTraspasoService.edit(reformaTraspaso);
            observacion.setObservacion(observaciones);
            if (listaProducto.isEmpty() && !listaProformaPresupuestoPlanificado.isEmpty()) {
                getParamts().put("userPlanificacion", clienteService.getrolsUser(RolUsuario.rolReformasPDI));
            } else {
                System.out.println("Entro Director Planificacion: " + clienteService.getrolsUser(RolUsuario.planificacion));
                getParamts().put("userPlanificacion", clienteService.getrolsUser(RolUsuario.planificacion));
            }

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public List<PresFuenteFinanciamiento> getListaPresFuenteFinanciamiento() {
        return listaPresFuenteFinanciamiento;
    }

    public void setListaPresFuenteFinanciamiento(List<PresFuenteFinanciamiento> listaPresFuenteFinanciamiento) {
        this.listaPresFuenteFinanciamiento = listaPresFuenteFinanciamiento;
    }

    public List<PresCatalogoPresupuestario> getListaPresCataPresupuestario() {
        return listaPresCataPresupuestario;
    }

    public void setListaPresCataPresupuestario(List<PresCatalogoPresupuestario> listaPresCataPresupuestario) {
        this.listaPresCataPresupuestario = listaPresCataPresupuestario;
    }

    public List<PresPlanProgramatico> getListaPresPlanProgramatico() {
        return listaPresPlanProgramatico;
    }

    public void setListaPresPlanProgramatico(List<PresPlanProgramatico> listaPresPlanProgramatico) {
        this.listaPresPlanProgramatico = listaPresPlanProgramatico;
    }

    public Boolean getDisableTipoTraspaso() {
        return disableTipoTraspaso;
    }

    public void setDisableTipoTraspaso(Boolean disableTipoTraspaso) {
        this.disableTipoTraspaso = disableTipoTraspaso;
    }

    public List<FuenteFinanciamiento> getListaFuenteDirecta() {
        return listaFuenteDirecta;
    }

    public void setListaFuenteDirecta(List<FuenteFinanciamiento> listaFuenteDirecta) {
        this.listaFuenteDirecta = listaFuenteDirecta;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestosDirecta() {
        return listaCatalogoPresupuestosDirecta;
    }

    public void setListaCatalogoPresupuestosDirecta(List<CatalogoPresupuesto> listaCatalogoPresupuestosDirecta) {
        this.listaCatalogoPresupuestosDirecta = listaCatalogoPresupuestosDirecta;
    }

    public List<PlanProgramatico> getListaPlanProgramaticoDirecta() {
        return listaPlanProgramaticoDirecta;
    }

    public void setListaPlanProgramaticoDirecta(List<PlanProgramatico> listaPlanProgramaticoDirecta) {
        this.listaPlanProgramaticoDirecta = listaPlanProgramaticoDirecta;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuestoPlanificado() {
        return proformaPresupuestoPlanificado;
    }

    public void setProformaPresupuestoPlanificado(ProformaPresupuestoPlanificado proformaPresupuestoPlanificado) {
        this.proformaPresupuestoPlanificado = proformaPresupuestoPlanificado;
    }

    public boolean isBtnTraspasos() {
        return btnTraspasos;
    }

    public void setBtnTraspasos(boolean btnTraspasos) {
        this.btnTraspasos = btnTraspasos;
    }

    public boolean isPanelDistributivo() {
        return panelDistributivo;
    }

    public void setPanelDistributivo(boolean panelDistributivo) {
        this.panelDistributivo = panelDistributivo;
    }

    public boolean isBolPapp() {
        return bolPapp;
    }

    public void setBolPapp(boolean bolPapp) {
        this.bolPapp = bolPapp;
    }

    public boolean isBolPartidaDirecta() {
        return bolPartidaDirecta;
    }

    public void setBolPartidaDirecta(boolean bolPartidaDirecta) {
        this.bolPartidaDirecta = bolPartidaDirecta;
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

    public LazyModel<ProformaPresupuestoPlanificado> getProformaPresupuestoLazy() {
        return proformaPresupuestoLazy;
    }

    public void setProformaPresupuestoLazy(LazyModel<ProformaPresupuestoPlanificado> proformaPresupuestoLazy) {
        this.proformaPresupuestoLazy = proformaPresupuestoLazy;
    }

    public List<ProformaPresupuestoPlanificado> getListaProformaPresupuestoPlanificado() {
        return listaProformaPresupuestoPlanificado;
    }

    public void setListaProformaPresupuestoPlanificado(List<ProformaPresupuestoPlanificado> listaProformaPresupuestoPlanificado) {
        this.listaProformaPresupuestoPlanificado = listaProformaPresupuestoPlanificado;
    }

    public BigDecimal getTotalTraspasoIncrementoPDirecta() {
        return totalTraspasoIncrementoPDirecta;
    }

    public void setTotalTraspasoIncrementoPDirecta(BigDecimal totalTraspasoIncrementoPDirecta) {
        this.totalTraspasoIncrementoPDirecta = totalTraspasoIncrementoPDirecta;
    }

    public BigDecimal getTotalTraspasoReduccionPDirecta() {
        return totalTraspasoReduccionPDirecta;
    }

    public void setTotalTraspasoReduccionPDirecta(BigDecimal totalTraspasoReduccionPDirecta) {
        this.totalTraspasoReduccionPDirecta = totalTraspasoReduccionPDirecta;
    }

    public BigDecimal getTotalMontoReformadoPDirecta() {
        return totalMontoReformadoPDirecta;
    }

    public void setTotalMontoReformadoPDirecta(BigDecimal totalMontoReformadoPDirecta) {
        this.totalMontoReformadoPDirecta = totalMontoReformadoPDirecta;
    }

    public String getTipoTraspaso() {
        return tipoTraspaso;
    }

    public void setTipoTraspaso(String tipoTraspaso) {
        this.tipoTraspaso = tipoTraspaso;
    }

    public List<CatalogoItem> getDistribuccionMetaList() {
        return distribuccionMetaList;
    }

    public void setDistribuccionMetaList(List<CatalogoItem> distribuccionMetaList) {
        this.distribuccionMetaList = distribuccionMetaList;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
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

    public Boolean getSemesteMetal() {
        return semesteMetal;
    }

    public void setSemesteMetal(Boolean semesteMetal) {
        this.semesteMetal = semesteMetal;
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

    public Boolean getSemeste() {
        return semeste;
    }

    public void setSemeste(Boolean semeste) {
        this.semeste = semeste;
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

    public List<CatalogoItem> getListacomponentes() {
        return listacomponentes;
    }

    public void setListacomponentes(List<CatalogoItem> listacomponentes) {
        this.listacomponentes = listacomponentes;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
    }

    public ReformaTraspaso getReformaTraspaso() {
        return reformaTraspaso;
    }

    public void setReformaTraspaso(ReformaTraspaso reformaTraspaso) {
        this.reformaTraspaso = reformaTraspaso;
    }

    public DetalleReformaTraspaso getDetalleReformaTraspaso() {
        return detalleReformaTraspaso;
    }

    public void setDetalleReformaTraspaso(DetalleReformaTraspaso detalleReformaTraspaso) {
        this.detalleReformaTraspaso = detalleReformaTraspaso;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

    public PlanAnualProgramaProyecto getProgramaProyectoSeleccionado() {
        return programaProyectoSeleccionado;
    }

    public void setProgramaProyectoSeleccionado(PlanAnualProgramaProyecto programaProyectoSeleccionado) {
        this.programaProyectoSeleccionado = programaProyectoSeleccionado;
    }

    public PlanAnualPoliticaPublica getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualPoliticaPublica planAnual) {
        this.planAnual = planAnual;
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

    public PlanAnualProgramaProyecto getProgramaProyecto() {
        return programaProyecto;
    }

    public void setProgramaProyecto(PlanAnualProgramaProyecto programaProyecto) {
        this.programaProyecto = programaProyecto;
    }

    public ActividadOperativa getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(ActividadOperativa actividadSeleccionada) {
        this.actividadSeleccionada = actividadSeleccionada;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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

    public int getIndiceDeAcordian() {
        return indiceDeAcordian;
    }

    public void setIndiceDeAcordian(int indiceDeAcordian) {
        this.indiceDeAcordian = indiceDeAcordian;
    }

    public void setIdPlanProgramatico(Long idPlanProgramatico) {
        this.idPlanProgramatico = idPlanProgramatico;
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

    public ProductoLazy getProductoAsignacion() {
        return productoAsignacion;
    }

    public void setProductoAsignacion(ProductoLazy productoAsignacion) {
        this.productoAsignacion = productoAsignacion;
    }

    public LazyModel<ActividadOperativa> getLazyActividadOperativas() {
        return lazyActividadOperativas;
    }

    public void setLazyActividadOperativas(LazyModel<ActividadOperativa> lazyActividadOperativas) {
        this.lazyActividadOperativas = lazyActividadOperativas;
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

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public List<ReformaTraspaso> getListaCodReforma() {
        return listaCodReforma;
    }

    public void setListaCodReforma(List<ReformaTraspaso> listaCodReforma) {
        this.listaCodReforma = listaCodReforma;
    }

    public boolean getFiltroDatosNullTablaPlanesAnuales() {
        return filtroDatosNullTablaPlanesAnuales;
    }

    public void setFiltroDatosNullTablaPlanesAnuales(boolean filtroDatosNullTablaPlanesAnuales) {
        this.filtroDatosNullTablaPlanesAnuales = filtroDatosNullTablaPlanesAnuales;
    }

    public boolean getRegistraNuevoPapp() {
        return registraNuevoPapp;
    }

    public void setRegistraNuevoPapp(boolean registraNuevoPapp) {
        this.registraNuevoPapp = registraNuevoPapp;
    }

    public boolean getOcultarMostarPlanNacional() {
        return ocultarMostarPlanNacional;
    }

    public void setOcultarMostarPlanNacional(boolean ocultarMostarPlanNacional) {
        this.ocultarMostarPlanNacional = ocultarMostarPlanNacional;
    }

    public boolean getBtnNuevoPapp() {
        return btnNuevoPapp;
    }

    public void setBtnNuevoPapp(boolean btnNuevoPapp) {
        this.btnNuevoPapp = btnNuevoPapp;
    }

    public boolean getFiltroDatosNullProgramaProyectos() {
        return filtroDatosNullProgramaProyectos;
    }

    public void setFiltroDatosNullProgramaProyectos(boolean filtroDatosNullProgramaProyectos) {
        this.filtroDatosNullProgramaProyectos = filtroDatosNullProgramaProyectos;
    }

    public boolean getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public boolean getFiltroDatosNullTablaGeneralPlanes() {
        return filtroDatosNullTablaGeneralPlanes;
    }

    public void setFiltroDatosNullTablaGeneralPlanes(boolean filtroDatosNullTablaGeneralPlanes) {
        this.filtroDatosNullTablaGeneralPlanes = filtroDatosNullTablaGeneralPlanes;
    }

    public boolean getBolReformaT1() {
        return bolReformaT1;
    }

    public void setBolReformaT1(boolean bolReformaT1) {
        this.bolReformaT1 = bolReformaT1;
    }

    public boolean getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public boolean getBtnContinuarTerminarTarea() {
        return btnContinuarTerminarTarea;
    }

    public void setBtnContinuarTerminarTarea(boolean btnContinuarTerminarTarea) {
        this.btnContinuarTerminarTarea = btnContinuarTerminarTarea;
    }

    public boolean getBtnCancelar() {
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

    public boolean getFormBusqRegistrar() {
        return formBusqRegistrar;
    }

    public void setFormBusqRegistrar(boolean formBusqRegistrar) {
        this.formBusqRegistrar = formBusqRegistrar;
    }

    public boolean getFormBusqConsultar() {
        return formBusqConsultar;
    }

    public void setFormBusqConsultar(boolean formBusqConsultar) {
        this.formBusqConsultar = formBusqConsultar;
    }

    public boolean getBtnRegistrar() {
        return btnRegistrar;
    }

    public void setBtnRegistrar(boolean btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    public boolean getBtnConsultar() {
        return btnConsultar;
    }

    public void setBtnConsultar(boolean btnConsultar) {
        this.btnConsultar = btnConsultar;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getRenderBtnGenRefPAPP() {
        return renderBtnGenRefPAPP;
    }

    public void setRenderBtnGenRefPAPP(Boolean renderBtnGenRefPAPP) {
        this.renderBtnGenRefPAPP = renderBtnGenRefPAPP;
    }

    public Boolean getRenderBtnGenRefPDI() {
        return renderBtnGenRefPDI;
    }

    public void setRenderBtnGenRefPDI(Boolean renderBtnGenRefPDI) {
        this.renderBtnGenRefPDI = renderBtnGenRefPDI;
    }

    public boolean isBtnGenerarReformaPDI() {
        return btnGenerarReformaPDI;
    }

    public void setBtnGenerarReformaPDI(boolean btnGenerarReformaPDI) {
        this.btnGenerarReformaPDI = btnGenerarReformaPDI;
    }

//</editor-fold>
}
