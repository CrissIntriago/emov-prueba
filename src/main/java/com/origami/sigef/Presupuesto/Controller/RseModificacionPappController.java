/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
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
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.ActividadOperativaLazy;
import com.origami.sigef.contabilidad.lazy.PlanAnualPoliticaPublicaLazy;
import com.origami.sigef.contabilidad.lazy.PlanAnualProgramaProyectoLazy;
import com.origami.sigef.contabilidad.lazy.ProductoLazy;
import com.origami.sigef.contabilidad.lazy.view.VistaGeneralPlanAnualLazy;
import com.origami.sigef.contabilidad.model.ProformaDTO;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
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
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
 * @author ORIGAMIEC
 */
@Named(value = "reformaSuplementoEView")
@ViewScoped
public class RseModificacionPappController extends BpmnBaseRoot implements Serializable {

//<editor-fold defaultstate="collapsed" desc="VARIBALES E INJECT">
    private static final long serialVersionUID = 1L;
    /*Services*/
    @Inject
    private PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService;
    @Inject
    private PlanNacionalEjeService planNacionalEjeService;
    @Inject
    private PlanNacionalObjetivoService planNacionalObjetivoService;
    @Inject
    private PlanNacionalPoliticaService planNacionalPoliticaService;
    @Inject
    private PlanLocalSistemaService planLocalSistemaService;
    @Inject
    private PlanLocalObjetivoService planLocalObjetivoService;
    @Inject
    private PlanLocalPoliticaService planLocalPoliticaService;
    @Inject
    private PlanLocalProgramaProyectoService planLocalProgramaProyectoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private ActividadOperativaService actividadService;
    @Inject
    private ProductoService productoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private PlanProgramaticoService planProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPrespuestoService;
    @Inject
    private FuenteFinanciamientoService fuenteService;
    @Inject
    private PlanProgramaticoService estrucPlanProgramaticoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CuposService cupoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    /*Listas*/
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
    private List<MasterCatalogo> periodos;
    private List<MasterCatalogo> listaAnio;
    private List<PlanAnualProgramaProyecto> programaProyectos;
    private List<ActividadOperativa> actividades;
    private List<Producto> productos;
    private List<PlanAnualPoliticaPublica> listaPlanAnualPoliticaPublicas;
    private List<PlanLocalProgramaProyecto> listaPlanLocalProgramaProyectos;
    private List<PlanAnualProgramaProyecto> planesAnualesProgramas;
    private List<CatalogoItem> listafuenteFinanciamiento;
    private List<PlanProgramatico> listaPlanProgramatico;
    private List<CatalogoPresupuesto> listaPresupuesto;
    private List<UnidadAdministrativa> unidades;
    private List<FuenteFinanciamiento> listaFuente;
    private List<PlanProgramatico> listaPlanProgramaticos;
    private List<CatalogoPresupuesto> listaCatalogoPresupuestos;
    private List<ProformaDTO> proformaModel;
    private ArrayList<ReporteDeActividades> list;
    private Long idUnidad;
    private List<Cupos> listaCupos;
    /*Objetos*/
    private PlanNacionalEje ejeNacionalSeleccionado;
    private PlanNacionalObjetivo objetivoNacionalSeleccionado;
    private PlanNacionalPolitica politicaNacionalSeleccionada;
    private PlanLocalSistema sistemaLocalSeleccionado;
    private PlanLocalObjetivo objetivoLocalSeleccionado;
    private PlanLocalPolitica politicaLocalSeleccionado;
    private PlanLocalProgramaProyecto planLocalProgramaProyecto;
    private PlanAnualProgramaProyecto programaProyecto;
    private PlanAnualProgramaProyecto programaProyectoSeleccionado;
    private ActividadOperativa actividad;
    private ActividadOperativa actividadSeleccionada;
    private Producto producto;
    private Producto productoSeleccionado;
    private PlanAnualPoliticaPublica planAnual;
    private PlanAnualPoliticaPublica planAnualSeleccionado;
    private OpcionBusqueda opcionBusqueda;
    private OpcionBusqueda busqueda;
    private ReporteDeActividades reporte;
    private ProformaPresupuestoPlanificado proformaPresupuesto;
    private Cupos cupo;
    /*Variables Globales*/
    private String programaProyectoLocal;
    private String productoGlobal;
    private int indiceDeAcordian;
    private Short anioProforma;
    private double valorTotalPresupuestoEgresos;
    private BigDecimal totalActividades;
    private BigDecimal totalCupo;
    private BigDecimal totalSaldoSuplemento;
    /*Lazy*/
    private PlanAnualPoliticaPublicaLazy lazy;
    private PlanAnualProgramaProyectoLazy lazyprogramaProyectos;

    private LazyModel<ActividadOperativa> lazyActividadOperativas;

    private LazyModel<Producto> productoLazy;
    private ProductoLazy lazyProductos;
    private ProductoLazy productoAsignacion;
    private LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy;
    private LazyModel<PlanAnualPoliticaPublica> planAnualazy;
    // private VistaGeneralPlanAnualLazy vistaGeneralPlanAnualLazy;
    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private BigDecimal TotalProducto;
    private List<Cupos> listaCuposAsignacion;

    /*Booleanos*/
    private boolean requierePlanNacional;
    private boolean requierePlanLocal;
    private boolean ocultarMostarPlanNacional;
    private boolean ocultarNombreProyectos;
    private boolean bloqueo;
    private boolean filtroDatosNullProgramaProyectos;
    private boolean filtroDatosNullTablaGeneralPlanes;
    private boolean filtroDatosNullTablaPlanesAnuales;
    private boolean registranuevo;
    private boolean consultarPapp;
    private boolean consultaRe;
    private LazyModel<Cupos> lazyOtrosReforma;
    private ReformaIngresoSuplemento reformaSuplemento;
    private LazyModel<Cupos> lazyCupos;
    private Distributivo unidadGeneral;
//</editor-fold>
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    ClienteService clienteService;
    private List<Cupos> listaReformas;

    private List<ReformaIngresoSuplemento> lazyAsignacion;

    private BigDecimal totalSuplementoIngreso;
    private BigDecimal cupoUnidadValor;
    private CatalogoItem estadoMostrar;
    private List<CatalogoItem> estadoFiltros;
    private CatalogoItem estadoRegistrado;
    private Cupos cupoUnidad;
    private LazyModel<ReformaIngresoSuplemento> lazy2;
    private UnidadAdministrativa unidadGlobal;
    private String observaciones;
    private boolean enabledReformas;

    //NUEVO
    @Inject
    private ManagerService service;
    private List<CatalogoItem> distribuccion, distribuccionMetaList;
    private List<CatalogoItem> listacomponentes;
    private Boolean semeste, trimestre, cuatrimestre, mensual;
    private Boolean semesteMetal, trimestreMeta, cuatrimestreMeta, mensualMeta;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                consultarPapp = false;
                listaCupos = new ArrayList<>();
                registranuevo = false;
                consultaRe = true;
                cupo = new Cupos();
                this.listaReformas = new ArrayList<>();
                Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
                unidadGeneral = new Distributivo();
                unidadGeneral = d;
                //this.listaReformas = suplementoIngresoService.listaReformaSuplmento2(true, unidadGeneral.getUnidadAdministrativa());
                opcionBusqueda = new OpcionBusqueda();
                /*Lista*/ programaProyectos = new ArrayList<>();
                actividades = new ArrayList<>();
                planesAnualesProgramas = new ArrayList<>();
                productos = new ArrayList<>();

                list = new ArrayList<>();
                estadoRegistrado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RR");
                /*Objetos*/
                actividad = new ActividadOperativa();
                programaProyecto = new PlanAnualProgramaProyecto();
                producto = new Producto();
                opcionBusqueda = new OpcionBusqueda();
                busqueda = new OpcionBusqueda();
                planLocalProgramaProyecto = new PlanLocalProgramaProyecto();
                planAnual = new PlanAnualPoliticaPublica();
                reporte = new ReporteDeActividades();
                this.TotalProducto = BigDecimal.ZERO;
                this.totalActividades = BigDecimal.ZERO;
                this.cupo = new Cupos();
                /*List*/
                listaPlanAnualPoliticaPublicas = planAnualPoliticaPublicaService.findAll();
                listaPresupuesto = catalogoPrespuestoService.findAll();
                programaProyectos = planAnualProgramaProyectoService.findAll();
                unidades = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
                productos = productoService.findAll();
                productos = productoService.findAll();
                listafuenteFinanciamiento = catalogoService.MostarTodoCatalogo("tipo_fuente_financiamiento");
                ejesNacionales = planNacionalEjeService.findByNamedQuery("PlanNacionalEje.findByEstado");
                sistemasLocales = planLocalSistemaService.findByNamedQuery("PlanLocalSistema.findByEstado");
                listaFuente = fuenteService.findByNamedQuery("FuenteFinanciamiento.findByEstado", true);
                periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
                enabledReformas = true;


                //listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
//                listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, 3, opcionBusqueda.getAnio());
//                listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, 4, opcionBusqueda.getAnio());

                /*Booleanos*/ ocultarMostarPlanNacional = false;
                requierePlanLocal = true;
                requierePlanNacional = true;
                ocultarNombreProyectos = false;
                bloqueo = false;
                filtroDatosNullProgramaProyectos = true;
                filtroDatosNullTablaGeneralPlanes = true;
                filtroDatosNullTablaPlanesAnuales = true;
                consultarPapp = false;
                registranuevo = false;
                consultaRe = true;
                totalCupo = BigDecimal.ZERO;
                /*Variables Globales*/
                setIndiceDeAcordian(-1);
                busqueda.setAnio(Short.valueOf("0"));
                lazyAsignacion = suplementoIngresoService.findAll();
                estadoFiltros = catalogoService.MostarTodoCatalogo("estado_solicitud");
                actividad = new ActividadOperativa();
                cupoUnidad = new Cupos();
                lazy2 = new LazyModel(ReformaIngresoSuplemento.class);
                lazy2.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                unidadGlobal = new UnidadAdministrativa();
                distribuccionMetaList = new ArrayList<>();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    //<editor-fold defaultstate="collapsed" desc="METODOS LOGICA NEGOCIO">

    public void mostrEstado(BigInteger b) {
        estadoMostrar = new CatalogoItem();
        estadoMostrar = suplementoIngresoService.getEstadoNombre(b);

    }

    public void seleccionProyecto(PlanAnualProgramaProyecto proyecto) {
        programaProyecto = new PlanAnualProgramaProyecto();
        programaProyecto = proyecto;
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

    public BigDecimal retornarCupo() {
        reformaSuplemento = new ReformaIngresoSuplemento();
        reformaSuplemento = suplementoIngresoService.getUnidadReforma(BigInteger.valueOf(tramite.getNumTramite()));
        Cupos c = new Cupos();
        c = suplementoIngresoService.cupoReformasUser(userSession.getNameUser(), reformaSuplemento);
        if (c == null) {
            return BigDecimal.ZERO;
        } else {
            return c.getMontoCupo();
        }
    }

    public BigDecimal retornarCupo(ReformaIngresoSuplemento r) {

        Cupos c = new Cupos();
        c = suplementoIngresoService.cupoReformasUser(userSession.getNameUser(), r);
        if (c == null) {
            return BigDecimal.ZERO;
        } else {
            return c.getMontoCupo();
        }
    }

    public void loadingProigramas() {
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("estado", true);
        programaProyecto = new PlanAnualProgramaProyecto();
    }

    public BigDecimal valorUnidad(UnidadAdministrativa u, ReformaIngresoSuplemento r) {

        return cupoService.cupoUnidadValor(u, r);
    }

    public void guardarCupoDistributivo() {

        Cupos c = cupoService.getCodigoCupo(reformaSuplemento, "D");

        BigDecimal valor = cupoService.getSumaVerificacion(reformaSuplemento).add(cupo.getMontoCupo());

        BigDecimal suplementoValor = getRetornaTotal(reformaSuplemento);

        if (valor.doubleValue() <= suplementoValor.doubleValue()) {
            if (c == null) {
                cupo.setReforma(reformaSuplemento);
                cupo.setOtros("D");
                cupo = cupoService.create(cupo);
            } else {
                c.setMontoCupo(cupo.getMontoCupo());

                cupoService.edit(cupo);
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aignarle ese valor");
        }

        this.cupo = new Cupos();

    }

    public void guardarCupoDistributivoAnexo() {

        Cupos c = cupoService.getCodigoCupo(reformaSuplemento, "DA");

        BigDecimal valor = cupoService.getSumaVerificacion(reformaSuplemento).add(cupo.getMontoCupo());

        BigDecimal suplementoValor = getRetornaTotal(reformaSuplemento);

        if (valor.doubleValue() <= suplementoValor.doubleValue()) {
            if (c == null) {
                cupo.setReforma(reformaSuplemento);
                cupo.setOtros("DA");
                cupo = cupoService.create(cupo);
            } else {
                c.setMontoCupo(cupo.getMontoCupo());

                cupoService.edit(cupo);
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aignarle ese valor");
        }

        this.cupo = new Cupos();

    }

    public void guardarCupoPartidasDirectas() {

        Cupos c = cupoService.getCodigoCupo(reformaSuplemento, "PD");
        BigDecimal valor = cupoService.getSumaVerificacion(reformaSuplemento).add(cupo.getMontoCupo());
        BigDecimal suplementoValor = getRetornaTotal(reformaSuplemento);
        if (valor.doubleValue() <= suplementoValor.doubleValue()) {
            if (c == null) {
                cupo.setReforma(reformaSuplemento);
                cupo.setOtros("PD");
                cupo = cupoService.create(cupo);
            } else {
                c.setMontoCupo(cupo.getMontoCupo());

                cupoService.edit(cupo);
            }

        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "No puede aignarle ese valor");
        }

        this.cupo = new Cupos();
    }

    public void showCupoTotal(ReformaIngresoSuplemento r) {

        Cupos c = suplementoIngresoService.cupoReformasUser(userSession.getNameUser(), r);
        if (c == null) {

            totalCupo = BigDecimal.ZERO;
        } else {

            totalCupo = c.getMontoCupo();
        }
    }

    public void showaDatable() {
        consultarPapp = true;
        registranuevo = false;
        consultaRe = false;
    }

    public BigDecimal verficarCupoSuplmento(BigInteger r) {

        BigDecimal vCupo = cupoService.verficiarCupoSuplemento(r, unidadGlobal);
        return vCupo;
    }

    public void showPapp(ReformaIngresoSuplemento r) {

        List<Cupos> listaVerficarcupos = new ArrayList<>();
        listaVerficarcupos = cupoService.getVerificacionCupos(r);

        if (listaVerficarcupos.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Asigne Cupos a las Unidades Administrativas");
            return;
        }

        BigDecimal consultarSuma = BigDecimal.ZERO;

        if (listaVerficarcupos.size() > 0) {
            for (Cupos itemCupo : listaVerficarcupos) {

                if (itemCupo.getUnidadAdministrativa() != null) {

                    consultarSuma = consultarSuma.add(itemCupo.getMontoCupo());
                }
            }

        }

        if (consultarSuma.doubleValue() == 0) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Advertencia", "Asigne Cupos a las Unidades Administrativas");
            return;
        }

        Cupos cp = suplementoIngresoService.cupoReformasUser(userSession.getNameUser(), r);
        unidadGlobal = new UnidadAdministrativa();
        unidadGlobal = cp.getUnidadAdministrativa();

        showCupoTotal(r);

        reformaSuplemento = new ReformaIngresoSuplemento();
        opcionBusqueda.setAnio(r.getPeriodo());
        reformaSuplemento = r;
        List<CatalogoItem> catalogoList = catalogoService.getMuestraTipo("estado_papp", Arrays.asList("RRE", "AR", "RR"));
        List<BigInteger> listaFiltrar = new ArrayList<>();

        for (CatalogoItem data : catalogoList) {
            listaFiltrar.add(BigInteger.valueOf(data.getId()));
        }

        listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        consultarPapp = false;
        registranuevo = true;
        consultaRe = false;
        opcionBusqueda.setAnio(r.getPeriodo());
        busqueda.setAnio(r.getPeriodo());
        saldoSuplemento(r);
        listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, Short.valueOf("3"), busqueda.getAnio());
        listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, Short.valueOf("4"), busqueda.getAnio());
        productoLazy = new LazyModel(Producto.class);
        productoLazy.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(r.getId()));
        productoLazy.getFilterss().put("actividadOperativa.unidadResponsable:equal", unidadGlobal);
        productoLazy.getSorteds().put("actividadOperativa.id", "ASC");
        productoLazy.getSorteds().put("descripcion", "ASC");
        productoLazy.setDistinct(false);
        //productoLazy.getFilterss().put("actividadOperativa.unidadResponsable:equal", unidadGeneral.getUnidadAdministrativa());
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(r.getId()));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", unidadGlobal);
        lazyActividadOperativas.getSorteds().put("id", "ASC");
        lazyActividadOperativas.setDistinct(false);
        // lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", unidadGeneral.getUnidadAdministrativa());
        vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", BigInteger.valueOf(r.getId()));
        mostrEstado(BigInteger.valueOf(reformaSuplemento.getId()));
        enabledReformas = false;
        PrimeFaces.current().ajax().update(":actividadesOperativa");
        PrimeFaces.current().ajax().update(":productos");
        PrimeFaces.current().ajax().update(":formTablaData");
    }

    public void asignacionCupo(ReformaIngresoSuplemento r) {

        List<Cupos> lista = cupoService.getVerificacionCupos(r);
        listaCuposAsignacion = new ArrayList<>();
        reformaSuplemento = r;
        if (lista.isEmpty()) {

            this.reformaSuplemento = new ReformaIngresoSuplemento();
            this.reformaSuplemento = r;

            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa data : unidades) {
                this.cupo = new Cupos();
                this.cupo.setReforma(reformaSuplemento);
                this.cupo.setUnidadAdministrativa(data);
                this.cupo.setMontoCupo(BigDecimal.ZERO);
                cupo = cupoService.create(cupo);
            }

            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("D");
            cupo = cupoService.create(cupo);
            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("DA");
            cupo = cupoService.create(cupo);
            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("PD");
            cupo = cupoService.create(cupo);

            this.cupo = new Cupos();
        }

        lazyCupos = new LazyModel(Cupos.class);
        lazyCupos.getFilterss().put("reforma:equal", r);
        lazyCupos.getFilterss().put("otros:equal", null);
        lazyOtrosReforma = new LazyModel(Cupos.class);
        lazyOtrosReforma.getFilterss().put("reforma:equal", r);
        lazyOtrosReforma.getFilterss().put("unidadAdministrativa:equal", null);
        listaCupos.clear();
        listaCuposAsignacion = cupoService.getVerificacionCupos(r);
        this.cupo = new Cupos();
        PrimeFaces.current().executeScript("PF('DloCupos').show()");
        PrimeFaces.current().ajax().update(":formCupos");

    }

    public void editarCell(Cupos c) {
        BigDecimal valor = cupoService.getSumaVerificacion(reformaSuplemento).add(c.getMontoCupo());

        BigDecimal suplementoValor = getRetornaTotal(reformaSuplemento);

        if (valor.doubleValue() <= suplementoValor.doubleValue()) {
            Cupos cupos = c;
            cupoService.edit(cupos);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "Cupo agregado Correctamente");

        } else {
            c.setMontoCupo(BigDecimal.ZERO);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Advertencia", "No puede asignarle ese valor");

        }

    }

    public void showReformas() {
        consultarPapp = false;
        registranuevo = false;
        consultaRe = true;
        totalCupo = BigDecimal.ZERO;
        enabledReformas = true;
    }

    public void cargandofecha() {
//        if (busqueda.getAnio() == null) {
//            short anio = 0;
//            busqueda.setAnio(anio);
//        }
//        listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, 3, busqueda.getAnio());
//        listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, 4, busqueda.getAnio());
//        planAnualazy = new PlanAnualPoliticaPublicaLazy(busqueda);
//        lazyProductos = new ProductoLazy(busqueda);
//        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
//        productoLazy = new ProductoLazy(busqueda);
//        lazyActividadOperativas = new ActividadOperativaLazy(busqueda);
//        vistaGeneralPlanAnualLazy = new VistaGeneralPlanAnualLazy(busqueda);
//        desactivarAprobado(busqueda.getAnio());
//        resetValues();
//        PrimeFaces.current().ajax().update(":formPlanes");
//        PrimeFaces.current().ajax().update(":proyectos");
//        PrimeFaces.current().ajax().update(":planProgramaProyectos");
//        PrimeFaces.current().ajax().update(":actividadesOperativa");
//        PrimeFaces.current().ajax().update(":productos");
    }

    public void cargarDatosTablaGeneralPlanAnual() {
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }
        productoAsignacion = new ProductoLazy(busqueda);
        this.TotalProducto = productoService.getSumaTotalProductos(null, busqueda.getAnio());
        this.totalActividades = productoService.getSumaTotalActividad(null, busqueda.getAnio());

        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(busqueda.getAnio(), false, true);
        if (li.size() > 0) {
            setBloqueo(true);
        } else {
            setBloqueo(false);

        }
        PrimeFaces.current().ajax().update(":formTablaMain");
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
            planAnualProgramaLazy.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(reformaSuplemento.getId()));

            PrimeFaces.current().ajax().update(":datatable2");
        } else {
            planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
            planAnualProgramaLazy.getFilterss().put("codigoReforma:equal", BigInteger.valueOf(reformaSuplemento.getId()));
            PrimeFaces.current().ajax().update(":planProgramaProyectos");
        }

    }

    /*Funcion que me permite filtrar los valores null que estan presenten en la tabla "GENERAL DE LOS PLANES ANUALES" como "NO APLICA"*/
    public void filtroDeDatosNullVistaGeneralPlanAnual() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":dataTablePlanes");
            filtroDatosNullTablaGeneralPlanes = true;
        } else {
            if (!filtroDatosNullTablaGeneralPlanes) {
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto", null);

                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", BigInteger.valueOf(reformaSuplemento.getId()));
                //new VistaGeneralPlanAnualLazy(!filtroDatosNullTablaGeneralPlanes, busqueda);
                PrimeFaces.current().ajax().update("formTablaMain");
            } else {
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", BigInteger.valueOf(reformaSuplemento.getId()));
                PrimeFaces.current().ajax().update("formTablaMain");
            }
        }
    }

    /*Funcion que me permite filtrar los valores null que estan presenten en la tabla "GENERAL DE LOS PLANES ANUALES" como "NO APLICA"*/
    public void filtroDeDatosNullPlanesAnualesAsignacionPartida() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":dataTablePlane");
            filtroDatosNullTablaPlanesAnuales = true;
        } else {
            if (!filtroDatosNullTablaPlanesAnuales) {
                productoAsignacion = new ProductoLazy(!filtroDatosNullTablaPlanesAnuales, busqueda);
                PrimeFaces.current().ajax().update("formTablaMain");
            } else {
                productoAsignacion = new ProductoLazy(busqueda);
                PrimeFaces.current().ajax().update("formTablaMain");
            }
        }
    }

    public void desactivarAprobado(Short periodo) {
        boolean verificar = true;
        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, false, true);

        if (li.size() > 0) {
            verificar = false;
        } else {
            verificar = true;
        }
        setBloqueo(verificar);
    }

    public void calcularSumaEgresoso() {

        List<String> listaProductosra = productoService.CodigoSinRepetir();
        for (String producto1 : listaProductosra) {
            productoService.SumatoriaProformaEgresosProducto(producto1);
        }
    }

    /**
     * muestra todos los nombre de los proyectos relacionado con plan anual
     * politica publica que se para por parametro
     *
     * @param i
     */
    public void cargandoNombreProyectos(PlanAnualPoliticaPublica i) {

        if (i == null) {
            this.programaProyectos = null;
        } else {
            this.programaProyectos = planAnualProgramaProyectoService.MostarNombresProyectos(i);
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
                    planLocalProgramaProyecto.setUsuarioCreacion("system");
                    planLocalProgramaProyecto.setUsuarioModifica("system");
                    planLocalProgramaProyecto.setCodigoReforma(BigInteger.valueOf(reformaSuplemento.getId()));
                    planLocalProgramaProyecto.setEstadoPapp(estadoRegistrado);
                    planLocalProgramaProyecto = planLocalProgramaProyectoService.create(planLocalProgramaProyecto);

                    planAnual.setPoliticaNacional(politicaNacionalSeleccionada);
                    planAnual.setPlanLocal(planLocalProgramaProyecto);
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
                    planAnual.setUsuarioModifica("system");
                    planAnual.setCodigoReforma(BigInteger.valueOf(reformaSuplemento.getId()));
                    planAnual.setUsuarioCreacion("system");
                    planAnual.setEstadoPapp(estadoRegistrado);
                    planAnual = planAnualPoliticaPublicaService.create(planAnual);
                    setIndiceDeAcordian(1);
                    PrimeFaces.current().ajax().update(":formMain");
                    PrimeFaces.current().ajax().update(":formPlanes");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addSuccessMessage("PLAN, PROGRAMA, PROYECTO", planLocalProgramaProyecto.getDescripcion() + " HA SIDO REGISTRADO EN EL SISTEMA");
                    PrimeFaces.current().ajax().update(":dataTablePlanes");
                    resetValues();
                } else {
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("PLAN, PROGRAMA, PROYECTO", "TODOS LOS CAMPOS DEBEN ESTAR LLENOS ANTES DE GUARDAR");
                }
            } else {
                planLocalProgramaProyecto.setUsuarioModifica("system");
                planLocalProgramaProyecto.setFechaModificacion(new Date());
                planLocalProgramaProyecto.setPolitica(politicaLocalSeleccionado);
                planLocalProgramaProyectoService.edit(planLocalProgramaProyecto);
                planAnual.setPoliticaNacional(politicaNacionalSeleccionada);
                planAnual.setPlanLocal(planLocalProgramaProyecto);
                planAnual.setFechaModificacion(new Date());
                planAnual.setUsuarioModifica("system");
                planAnualPoliticaPublicaService.edit(planAnual);

                PrimeFaces.current().ajax().update(":formPlanes");
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addSuccessMessage("PLAN, PROGRAMA, PROYECTO", planLocalProgramaProyecto.getDescripcion() + " HA SIDO EDITADO CON EXITO");
                PrimeFaces.current().ajax().update(":dataTablePlanes");
                resetValues();
                setIndiceDeAcordian(1);
                PrimeFaces.current().ajax().update(":formMain");
            }
        }
    }

    public void abrirDlPlanPublico(PlanLocalProgramaProyecto pl, PlanAnualPoliticaPublica pa) {
        this.planLocalProgramaProyecto = pl;
        this.planAnual = pa;
        politicaNacionalSeleccionada = pa.getPoliticaNacional();
        planAnual.setPlanLocal(pl);
        politicaLocalSeleccionado = pa.getPlanLocal().getPolitica();
        PrimeFaces.current().executeScript("PF('DlGlobal').show()");
        PrimeFaces.current().ajax().update(":fmGlobalPlan");

    }

    public void editarCell(Producto p) {
        try {

            Boolean modifico = false;
            proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(busqueda.getAnio(), p.getCodigoPresupuestario());

            if (p.getItemPresupuestario() != null && p.getEstructuraProgramatica() != null && p.getFuente() != null) {
                p.setCodigoPresupuestario(p.getEstructuraProgramatica().getCodigo() + p.getItemPresupuestario().getCodigo() + p.getFuente().getTipoFuente().getOrden());
                modifico = true;
            } else {
                p.setCodigoPresupuestario("");
            }
            if (p.getFuente() != null) {
                p.setFuenteDirecta(p.getFuente().getTipoFuente());
                modifico = true;
            }
            productoService.edit(p);

            Boolean consultaTabla = proformaPresupuestoPlanificadoService.consultarDatos(p.getPeriodo(), "PPPA");
            if (consultaTabla) {
                //Al actualizar un item, estructura o fuente, en la tabla de proforma_presupuesto_planificado se actualizara esta partida modificada
                //if (!partidaAntigua.equals("") && !partidaAntigua.equals(partidaNueva)) {
                seModificoPPPA(modifico, p.getPeriodo());
                // }
            }
            PrimeFaces.current().ajax().update("dataTablePlane");
            JsfUtil.addInformationMessage("Plan Programa Proyecto", "Se ha registrado correctamente");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*Funcion que me actualizara los registros de la tabla en caso de que una partida del plan anual haya sido modificado, eliminado o aggregado*/
    public void seModificoPPPA(Boolean modifico, Short periodo) {
        if (modifico) {
            proformaPresupuesto = new ProformaPresupuestoPlanificado();
            proformaPresupuestoPlanificadoService.eliminandoRegistroLogica(periodo, "PPPA");
            List<ProformaPDTO> lista = proformaPresupuestoPlanificadoService.muestrameEgresos(periodo);
            for (ProformaPDTO ob : lista) {
                proformaPresupuesto.setValor(ob.getTotal());
                proformaPresupuesto.setEstado(true);
                proformaPresupuesto.setPeriodo(periodo);
                proformaPresupuesto.setGenerado(false);
                proformaPresupuesto.setUsuarioCreacion("system");
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion("system");
                proformaPresupuesto.setFechaModificacion(new Date());
                proformaPresupuesto.setPartidaPresupuestaria(ob.getPartida());
                proformaPresupuesto.setItemPresupuestario(ob.getItemPresupuestario());
                proformaPresupuesto.setEstructuraProgramatica(ob.getEstructuraProgramatica());
                proformaPresupuesto.setFuente(ob.getFuente());
                proformaPresupuesto.setFuenteDirecta(ob.getFuenteDirecta());
                proformaPresupuesto.setCodigo("PPPA");
                this.proformaPresupuesto = proformaPresupuestoPlanificadoService.create(proformaPresupuesto);
                proformaPresupuesto = new ProformaPresupuestoPlanificado();
            }
        }
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
        semeste = Boolean.FALSE;
        trimestre = Boolean.FALSE;
        cuatrimestre = Boolean.FALSE;
        mensual = Boolean.FALSE;
        semesteMetal = Boolean.FALSE;
        trimestreMeta = Boolean.FALSE;
        cuatrimestreMeta = Boolean.FALSE;
        mensualMeta = Boolean.FALSE;
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
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addSuccessMessage("Plan Programa Proyecto", c.getPlanLocal().getDescripcion() + "eliminado con éxito");
    }

    public void añadirPlanAnualPoliticaPublica(PlanAnualPoliticaPublica pa) {
        programaProyecto = new PlanAnualProgramaProyecto();

        programaProyecto.setPlanAnual(pa);

        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
    }

    public void noAplica() {
        programaProyecto.setPlanAnual(null);
        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
        PrimeFaces.current().ajax().update(":dataTablePlanes");

    }

    public void savePlanNombreProyecto() {
        if (programaProyecto.getNombreProgramaProyecto() == null) {
            JsfUtil.addWarningMessage("AVISO", "NECESITA INGRESAR O SELECIONAR UN NOMBRE DEL PLAN ANUL, PROGRAMA, PROYECTO");
        } else {
            boolean edit = programaProyecto.getId() != null;
            if (!edit) {
                programaProyecto.setPeriodo(busqueda.getAnio());
                programaProyecto.setEstado(true);
                programaProyecto.setFechaCreacion(new Date());
                programaProyecto.setFechaModificacion(new Date());
                programaProyecto.setUsuarioCreacion("system");
                programaProyecto.setUsuarioModifica("system");
                programaProyecto.setCodigoReforma(BigInteger.valueOf(reformaSuplemento.getId()));
                programaProyecto.setEstadoPapp(estadoRegistrado);
                this.programaProyecto = planAnualProgramaProyectoService.create(programaProyecto);
            } else {
                programaProyecto.setUsuarioModifica("system");
                programaProyecto.setFechaModificacion(new Date());
                planAnualProgramaProyectoService.edit(programaProyecto);
            }
            setIndiceDeAcordian(2);

            PrimeFaces.current().executeScript("PF('DlgPlanProyecto').hide()");
            PrimeFaces.current().ajax().update(":dataTablePlanes");
            PrimeFaces.current().ajax().update("messages");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", programaProyecto.getNombreProgramaProyecto() + (edit ? " editado" : " guardado") + " con éxito.");
            programaProyecto = new PlanAnualProgramaProyecto();
        }
    }

    public void editarPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {
        this.programaProyecto = p;
        PrimeFaces.current().executeScript("PF('DlgPlanProyecto').show()");
        PrimeFaces.current().ajax().update(":formPlanProyecto");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
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
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Información", "eliminado con éxito.");
    }

    public void añadirPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {
        this.actividad = new ActividadOperativa();
        actividad.setPlanProgramaProyecto(p);
        actividad.setUnidadResponsable(unidadGlobal);
        PrimeFaces.current().executeScript("PF('DlgActividad').show()");
        PrimeFaces.current().ajax().update(":formActividad");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
    }

    public void saveActividadOperativa() {
        boolean edit = actividad.getId() != null;
        actividad.setEstadoPapp(estadoRegistrado);
        if (programaProyecto != null && programaProyecto.getId() != null) {
            actividad.setPlanProgramaProyecto(programaProyecto);
        }
        if (actividad.sumaDsitribuccionMeta()) {
            if (actividad.sumaDsitribuccionReformado()) {
                if (!edit) {
                    if (actividadService.getListaVerificacionActividadesReforma(actividad.getNombreActividad(), BigInteger.valueOf(reformaSuplemento.getId()))) {
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
                    actividad.reformandoValores();
                    actividad.setPeriodo(busqueda.getAnio());
                    actividad.setCodigoReforma(BigInteger.valueOf(reformaSuplemento.getId()));
                    actividad.setEstadoPapp(estadoRegistrado);
                    actividad.encerarMesesOriginales();
                    this.actividad = actividadService.create(actividad);
                } else {
                    actividad.setEstado(true);
                    actividad.setFechaModificacion(new Date());
                    actividad.setUsuarioModifica(userSession.getNameUser());
                    actividad.setPeriodo(busqueda.getAnio());
                    actividad.distribuirMeta();
                    actividad.distribuirMonto();
                    actividad.reformandoValores();
                    service.updateEntity(actividad);
                }
                setIndiceDeAcordian(3);
                PrimeFaces.current().ajax().update("formMain");
                PrimeFaces.current().executeScript("PF('DlgActividad').hide()");
                PrimeFaces.current().ajax().update(":formActividad");
                PrimeFaces.current().ajax().update(":dataTablePlanes");
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addInformationMessage("Información", actividad.getNombreActividad() + (edit ? "  editado" : "  guardado") + " con éxito.");
                actividad = new ActividadOperativa();
            } else {
                PrimeFaces.current().ajax().update(":formActividad");
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Información", "La distribución de la programación finaciera no es igual al monto de la actividad");
            }
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "La distribución de la programación meta no es igual a la medición meta");
        }

    }

    public void editarActividadOperativa(ActividadOperativa a) {
        if (a == null) {
            actividad = new ActividadOperativa();
            actividad.setUnidadResponsable(unidadGlobal);
        } else {
            actividad = new ActividadOperativa();
            actividad = a;
            actividad.setUnidadResponsable(unidadGlobal);
            programaProyecto = actividad.getPlanProgramaProyecto();
        }
        actividad.reversarDistribucionMeta();
        actividad.reversarDistribuirMonto();
        actividad.reformandoValores();
        renderizarDistribuccionMeta();
        renderizarDistribuccion();
        distribuccion = new ArrayList<>();
        distribuccion = catalogoService.MostarTodoCatalogo("TIPO_DISTRIBUCCION_ACO");
        distribuccionMetaList = new ArrayList<>();
        distribuccionMetaList = catalogoService.MostarTodoCatalogo("TIPO_DISTRIBUCCION_ACO");
        listacomponentes = new ArrayList<>();
        listacomponentes = catalogoService.MostarTodoCatalogo("TIPO_COMPONENTE_PAPP");

    }

    public void eliminarActividadOperativa(ActividadOperativa a) {

        if (getHijosByPadre(null, null, a, 3)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "No se puede eliminar porque tiene Productos asociados");
            return;
        }

        actividadService.remove(a);
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":productos");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Información", a.getNombreActividad() + " eliminado con éxito.");

    }

    public Boolean calcularActiviadaMonto() {

        boolean verificacion = actividad.getCuatrimestre1Actividad().add(actividad.getCuatrimestre2Actividad()).add(actividad.getCuatrimestre3Actividad()).compareTo(actividad.getMonto()) == 0;
        return verificacion;

    }

    public Boolean calcularSumaActividades() {
        boolean verificacion = actividad.getCuatrimestre1Meta().add(actividad.getCuatrimestre2Meta()).add(actividad.getCuatrimestre3Meta()).compareTo(actividad.getMedicionMeta()) == 0;
        return verificacion;
    }

    public void añadirActividadOperativa(ActividadOperativa a) {
        producto = new Producto();
        producto.setActividadOperativa(a);
        PrimeFaces.current().executeScript("PF('DdlgProducto').show()");
        PrimeFaces.current().ajax().update(":formProducto");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
    }

    public void editarProducto(Producto p) {
        productoGlobal = "";
        this.producto = p;
        productoGlobal = String.valueOf(p.getMonto());
        PrimeFaces.current().executeScript("PF('DdlgProducto').show()");
        PrimeFaces.current().ajax().update(":formProducto");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
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

    public void abrirDloSuplementoEgreso(Producto p) {
        producto = new Producto();
        producto = p;
        PrimeFaces.current().executeScript("PF('DdlgProductoSuplemento').show()");
        PrimeFaces.current().ajax().update(":formProductoSuplemento");

    }

    public void editarSuplelemtoEgresos(Producto p) {
        producto = new Producto();
        producto = p;
        BigDecimal cupConfirmar = BigDecimal.ZERO;
        BigDecimal prod = cupoService.getValorActualSuplemento(producto);

        if (producto.getSuplementoEgreso() == null) {
            producto.setSuplementoEgreso(BigDecimal.ZERO);

        }

        cupConfirmar = verficarCupoSuplmento(producto.getCodigoReforma());
        cupConfirmar = cupConfirmar.add(producto.getSuplementoEgreso());
        if (cupConfirmar.subtract(prod).doubleValue() > totalCupo.doubleValue()) {
            producto.setSuplementoEgreso(BigDecimal.ZERO);
            producto.setMontoReformada(producto.getMonto().add(producto.getSuplementoEgreso()));
            productoService.edit(producto);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Error", "Esta sobrepasando el cupos que se le asigno");

        } else {

            producto.setMontoReformada(producto.getMonto().add(producto.getSuplementoEgreso()));
            productoService.edit(producto);

            int a = cupoService.actualizarValorActividad(producto.getActividadOperativa());

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addSuccessMessage("Información", "El Suplemento se realizo correctamente");
//            PrimeFaces.current().executeScript("PF('DdlgProductoSuplemento').hide()");
//            PrimeFaces.current().ajax().update(":formProductoSuplemento");
        }
        saldoSuplemento(reformaSuplemento);
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
                producto.setTraspasoIncremento(BigDecimal.ZERO);
                producto.setTraspasoReduccion(BigDecimal.ZERO);
                producto.setReduccionEgreso(BigDecimal.ZERO);
                producto.setCodigoReforma(BigInteger.valueOf(reformaSuplemento.getId()));
                producto.setSuplementoEgreso(monto);
                producto.setMontoReformada(monto);
                producto.setSaldoDisponible(monto);
                producto.setEstadoPapp(estadoRegistrado);
                this.producto = productoService.create(producto);
                PrimeFaces.current().ajax().update(":formMain");
                PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                PrimeFaces.current().ajax().update(":dataTablePlanes");
                PrimeFaces.current().ajax().update(":formProducto");
                PrimeFaces.current().ajax().update("messages");

                JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                producto = new Producto();

//               
            } else {

                producto.setUsuarioModifica(userSession.getNameUser());
                producto.setFechaModificacion(new Date());
                producto.setSuplementoEgreso(producto.getMonto());
                producto.setReserva(BigDecimal.ZERO);
                producto.setTraspasoIncremento(BigDecimal.ZERO);
                producto.setTraspasoReduccion(BigDecimal.ZERO);
                producto.setMontoReformada(producto.getMonto());
                producto.setSaldoDisponible(producto.getMonto());
                productoService.edit(producto);
                PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                PrimeFaces.current().ajax().update(":formProducto");
                PrimeFaces.current().ajax().update("messages");
                PrimeFaces.current().ajax().update(":dataTablePlanes");
                JsfUtil.addInformationMessage("Información", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con éxito.");
                producto = new Producto();
            }
            saldoSuplemento(reformaSuplemento);
            PrimeFaces.current().ajax().update(":dataTablePlanes");
            PrimeFaces.current().ajax().update("formMain");
            PrimeFaces.current().ajax().update("formulariobuscar");
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Información", "Los campos no puedene estar vacios");
        }
    }

    public void saldoSuplemento(ReformaIngresoSuplemento r) {
        totalSaldoSuplemento = BigDecimal.ZERO;
        Cupos c = suplementoIngresoService.cupoReformasUser(userSession.getNameUser(), r);

        totalSaldoSuplemento = cupoService.recaudadoSuplemento(BigInteger.valueOf(r.getId()), c.getUnidadAdministrativa());

    }

    public void eliminarProducto(Producto p) {

        productoService.eliminarPstoPlanificado(p.getPeriodo(), p.getCodigoPresupuestario());

        this.productoService.remove(p);

        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Información", p.getDescripcion() + " eliminado con éxito.");

    }

    public void desgloceProducto(ActividadOperativa a) {

        PrimeFaces.current().executeScript("PF('DlGlobal').show()");
        PrimeFaces.current().ajax().update(":fmGlobalPlan");
//        this.mostrarProductos=productoService.findByNamedQuery("Producto.findByVerificarMonto", a);

    }

    public void fmPresupuestoProducto(Producto p) {
        this.producto = new Producto();
        this.producto = p;
        PrimeFaces.current().executeScript("PF('DlgproductoPresupuesto').show()");
        PrimeFaces.current().ajax().update(":fmproductopresupuesto");

    }

    public void guardaPresupuestoProducto() {
        productoService.edit(producto);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", " Presupuesto asignado al producto " + producto.getDescripcion() + " con éxito.");

        PrimeFaces.current().executeScript("PF('DlgproductoPresupuesto').hide()");
        PrimeFaces.current().ajax().update(":fmproductopresupuesto");

    }

    public void fmDesglosesarInformacion(Producto p) {
        this.producto = p;
        PrimeFaces.current().executeScript("PF('Dldesglose').show()");
        PrimeFaces.current().ajax().update(":fmdesglose");

    }

    public void guardarItem(Producto p) {
        try {
            if (p.getItemPresupuestario() != null && p.getEstructuraProgramatica() != null && p.getFuente() != null) {
                p.setCodigoPresupuestario(p.getItemPresupuestario().getCodigo() + p.getEstructuraProgramatica().getCodigo() + p.getFuente().getTipoFuente().getOrden());
            } else {
                p.setCodigoPresupuestario("");
            }
            p.setFechaModificacion(new Date());
            p.setUsuarioModifica("system");
            productoService.edit(p);

            JsfUtil.addInformationMessage("Información", " Presupuesto asignado al producto " + producto.getDescripcion() + " con éxito.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    /*Permite verificar si la suma de los productos asociada a un actividad es igual al valor de la actividad */
    public void verificadorDeValores() {
        list = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListEquilibrado = new ArrayList<>();
        ArrayList<ReporteDeActividades> ListDesequilibrado = new ArrayList<>();
        ListEquilibrado = new ArrayList<>();
        List<ActividadOperativa> resultActividades = actividadService.getListActividades(busqueda.getAnio(), Arrays.asList("AR", "RR", "RRE"));
        List<Producto> resultProductos = productoService.getListProductos(busqueda.getAnio(), Arrays.asList("AR", "RR", "RRE"));
        if (resultActividades.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay Actividades Operativas registrados en el período " + busqueda.getAnio());
            return;
        } else {
            if (resultProductos.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO", "No hay Productos registrados en el período " + busqueda.getAnio());
                return;
            } else {
                for (ActividadOperativa actividad : resultActividades) {
                    BigDecimal totalProducto = productoService.getSumaProducto(actividad, busqueda.getAnio(), Arrays.asList("AR", "RR", "RRE"));
                    if (!actividad.getMonto().equals(totalProducto)) {
                        /*Se iran agregando las actividades que la suma de sus productos no sean igual al monto asignado*/
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListDesequilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    } else {
                        reporte.setActividadOperativa(actividad);
                        reporte.setMontoTotalDeProductos(totalProducto);
                        reporte.setDiferencia(actividad.getMonto().subtract(totalProducto));
                        ListEquilibrado.add(reporte);
                        reporte = new ReporteDeActividades();
                    }
                }
                if (ListDesequilibrado.isEmpty()) {
                    list = ListEquilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductos').show()");
                    PrimeFaces.current().ajax().update("formActividadProductos");
                } else {
                    list = ListDesequilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductos').show()");
                    PrimeFaces.current().ajax().update("formActividadProductos");
                }
            }
        }
    }

    public void sumatotalArticulosPartidas() {
        this.TotalProducto = productoService.getSumaTotalProductos(idUnidad, busqueda.getAnio());
        this.totalActividades = productoService.getSumaTotalActividad(idUnidad, busqueda.getAnio());
    }
//</editor-fold>

    public void abriDlogo(ReformaIngresoSuplemento r) {
        reformaSuplemento = new ReformaIngresoSuplemento();
        reformaSuplemento = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int aprobar) {
        try {
            getParamts().put("aprobado", aprobar);
            if (aprobar == 1) {

                getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.planificacion));

            } else {
                getParamts().put("user", clienteService.getrolsUser(RolUsuario.financiero));
            }

            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("CPR","2")

            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaSuplemento = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("CPR","2")
            getParamts().put("usuarioplan", clienteService.getrolsUser(RolUsuario.planificacion));
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaSuplemento = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void sumaMontos() {
        if (actividad.getPresupuestoPropio() == null) {
            actividad.setPresupuestoPropio(BigDecimal.ZERO);
        }
        if (actividad.getPresupuestoFinanciamiento() == null) {
            actividad.setPresupuestoFinanciamiento(BigDecimal.ZERO);
        }
        actividad.setMonotReformado(actividad.getPresupuestoPropio().setScale(2, RoundingMode.HALF_UP).add(actividad.getPresupuestoFinanciamiento().setScale(2, RoundingMode.HALF_UP)));
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public List<Cupos> getListaCuposAsignacion() {
        return listaCuposAsignacion;
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public boolean isEnabledReformas() {
        return enabledReformas;
    }

    public void setEnabledReformas(boolean enabledReformas) {
        this.enabledReformas = enabledReformas;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<CatalogoItem> getEstadoFiltros() {
        return estadoFiltros;
    }

    public void setEstadoFiltros(List<CatalogoItem> estadoFiltros) {
        this.estadoFiltros = estadoFiltros;
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public void setListaCuposAsignacion(List<Cupos> listaCuposAsignacion) {
        this.listaCuposAsignacion = listaCuposAsignacion;
    }

    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getTotalSuplementoIngreso() {
        return totalSuplementoIngreso;
    }

    public void setTotalSuplementoIngreso(BigDecimal totalSuplementoIngreso) {
        this.totalSuplementoIngreso = totalSuplementoIngreso;
    }

    public LazyModel<Cupos> getLazyOtrosReforma() {
        return lazyOtrosReforma;
    }

    public void setLazyOtrosReforma(LazyModel<Cupos> lazyOtrosReforma) {
        this.lazyOtrosReforma = lazyOtrosReforma;
    }

    public BigDecimal getTotalSaldoSuplemento() {
        return totalSaldoSuplemento;
    }

    public void setTotalSaldoSuplemento(BigDecimal totalSaldoSuplemento) {
        this.totalSaldoSuplemento = totalSaldoSuplemento;
    }

    public List<ReformaIngresoSuplemento> getLazyAsignacion() {
        return lazyAsignacion;
    }

    public void setLazyAsignacion(List<ReformaIngresoSuplemento> lazyAsignacion) {
        this.lazyAsignacion = lazyAsignacion;
    }

    public ReformaIngresoSuplemento getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(ReformaIngresoSuplemento reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }

    public Distributivo getUnidadGeneral() {
        return unidadGeneral;
    }

    public void setUnidadGeneral(Distributivo unidadGeneral) {
        this.unidadGeneral = unidadGeneral;
    }

    public List<Cupos> getListaReformas() {
        return listaReformas;
    }

    public void setListaReformas(List<Cupos> listaReformas) {
        this.listaReformas = listaReformas;
    }

    public BigDecimal getCupoUnidadValor() {
        return cupoUnidadValor;
    }

    public void setCupoUnidadValor(BigDecimal cupoUnidadValor) {
        this.cupoUnidadValor = cupoUnidadValor;
    }

    public List<Cupos> getListaCupos() {
        return listaCupos;
    }

    public void setListaCupos(List<Cupos> listaCupos) {
        this.listaCupos = listaCupos;
    }

    public Cupos getCupo() {
        return cupo;
    }

    public void setCupo(Cupos cupo) {
        this.cupo = cupo;
    }

    public LazyModel<Cupos> getLazyCupos() {
        return lazyCupos;
    }

    public void setLazyCupos(LazyModel<Cupos> lazyCupos) {
        this.lazyCupos = lazyCupos;
    }

    public boolean isConsultaRe() {
        return consultaRe;
    }

    public void setConsultaRe(boolean consultaRe) {
        this.consultaRe = consultaRe;
    }

    public boolean isRegistranuevo() {
        return registranuevo;
    }

    public void setRegistranuevo(boolean registranuevo) {
        this.registranuevo = registranuevo;
    }

    public boolean isConsultarPapp() {
        return consultarPapp;
    }

    public void setConsultarPapp(boolean consultarPapp) {
        this.consultarPapp = consultarPapp;
    }

    public BigDecimal getTotalProducto() {
        return TotalProducto;
    }

    public void setTotalProducto(BigDecimal TotalProducto) {
        this.TotalProducto = TotalProducto;
    }

    public Long getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Long idUnidad) {
        this.idUnidad = idUnidad;
    }

    public BigDecimal getTotalActividades() {
        return totalActividades;
    }

    public void setTotalActividades(BigDecimal totalActividades) {
        this.totalActividades = totalActividades;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuesto() {
        return proformaPresupuesto;
    }

    public void setProformaPresupuesto(ProformaPresupuestoPlanificado proformaPresupuesto) {
        this.proformaPresupuesto = proformaPresupuesto;
    }

    public LazyModel<VistaGeneralPlanAnual> getVistaGeneralPlanAnualLazy() {
        return vistaGeneralPlanAnualLazy;
    }

    public void setVistaGeneralPlanAnualLazy(LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy) {
        this.vistaGeneralPlanAnualLazy = vistaGeneralPlanAnualLazy;
    }

    public boolean isFiltroDatosNullTablaPlanesAnuales() {
        return filtroDatosNullTablaPlanesAnuales;
    }

    public void setFiltroDatosNullTablaPlanesAnuales(boolean filtroDatosNullTablaPlanesAnuales) {
        this.filtroDatosNullTablaPlanesAnuales = filtroDatosNullTablaPlanesAnuales;
    }

    public ReporteDeActividades getReporte() {
        return reporte;
    }

    public void setReporte(ReporteDeActividades reporte) {
        this.reporte = reporte;
    }

    public ArrayList<ReporteDeActividades> getList() {
        return list;
    }

    public void setList(ArrayList<ReporteDeActividades> list) {
        this.list = list;
    }

    public PlanLocalProgramaProyecto getPlanLocalProgramaProyecto() {
        return planLocalProgramaProyecto;
    }

    public void setPlanLocalProgramaProyecto(PlanLocalProgramaProyecto planLocalProgramaProyecto) {
        this.planLocalProgramaProyecto = planLocalProgramaProyecto;
    }

    public String getProgramaProyectoLocal() {
        return programaProyectoLocal;
    }

    public void setProgramaProyectoLocal(String programaProyectoLocal) {
        this.programaProyectoLocal = programaProyectoLocal;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public PlanAnualPoliticaPublicaService getPlanAnualPoliticaPublicaService() {
        return planAnualPoliticaPublicaService;
    }

    public void setPlanAnualPoliticaPublicaService(PlanAnualPoliticaPublicaService planAnualPoliticaPublicaService) {
        this.planAnualPoliticaPublicaService = planAnualPoliticaPublicaService;
    }

    public PlanNacionalEjeService getPlanNacionalEjeService() {
        return planNacionalEjeService;
    }

    public void setPlanNacionalEjeService(PlanNacionalEjeService planNacionalEjeService) {
        this.planNacionalEjeService = planNacionalEjeService;
    }

    public PlanNacionalPoliticaService getPlanNacionalPoliticaService() {
        return planNacionalPoliticaService;
    }

    public void setPlanNacionalPoliticaService(PlanNacionalPoliticaService planNacionalPoliticaService) {
        this.planNacionalPoliticaService = planNacionalPoliticaService;
    }

    public PlanLocalSistemaService getPlanLocalSistemaService() {
        return planLocalSistemaService;
    }

    public void setPlanLocalSistemaService(PlanLocalSistemaService planLocalSistemaService) {
        this.planLocalSistemaService = planLocalSistemaService;
    }

    public LazyModel<ActividadOperativa> getLazyActividadOperativas() {
        return lazyActividadOperativas;
    }

    public void setLazyActividadOperativas(LazyModel<ActividadOperativa> lazyActividadOperativas) {
        this.lazyActividadOperativas = lazyActividadOperativas;
    }

    public LazyModel<Producto> getProductoLazy() {
        return productoLazy;
    }

    public void setProductoLazy(LazyModel<Producto> productoLazy) {
        this.productoLazy = productoLazy;
    }

    public PlanLocalObjetivoService getPlanLocalObjetivoService() {
        return planLocalObjetivoService;
    }

    public void setPlanLocalObjetivoService(PlanLocalObjetivoService planLocalObjetivoService) {
        this.planLocalObjetivoService = planLocalObjetivoService;
    }

    public PlanLocalPoliticaService getPlanLocalPoliticaService() {
        return planLocalPoliticaService;
    }

    public void setPlanLocalPoliticaService(PlanLocalPoliticaService planLocalPoliticaService) {
        this.planLocalPoliticaService = planLocalPoliticaService;
    }

    public PlanLocalProgramaProyectoService getPlanLocalProgramaProyectoService() {
        return planLocalProgramaProyectoService;
    }

    public void setPlanLocalProgramaProyectoService(PlanLocalProgramaProyectoService planLocalProgramaProyectoService) {
        this.planLocalProgramaProyectoService = planLocalProgramaProyectoService;
    }

    public List<PlanNacionalPolitica> getPoliticasNa() {
        return politicasNa;
    }

    public void setPoliticasNa(List<PlanNacionalPolitica> politicasNa) {
        this.politicasNa = politicasNa;
    }

    public List<PlanLocalObjetivo> getObjetivosLo() {
        return objetivosLo;
    }

    public void setObjetivosLo(List<PlanLocalObjetivo> objetivosLo) {
        this.objetivosLo = objetivosLo;
    }

    public List<PlanLocalPolitica> getPoliticasLo() {
        return politicasLo;
    }

    public void setPoliticasLo(List<PlanLocalPolitica> politicasLo) {
        this.politicasLo = politicasLo;
    }

    public MasterCatalogoService getMasterCatalogoService() {
        return masterCatalogoService;
    }

    public void setMasterCatalogoService(MasterCatalogoService masterCatalogoService) {
        this.masterCatalogoService = masterCatalogoService;
    }

    public List<PlanNacionalEje> getEjesNacionales() {
        return ejesNacionales;
    }

    public void setEjesNacionales(List<PlanNacionalEje> ejesNacionales) {
        this.ejesNacionales = ejesNacionales;
    }

    public PlanNacionalEje getEjeNacionalSeleccionado() {
        return ejeNacionalSeleccionado;
    }

    public void setEjeNacionalSeleccionado(PlanNacionalEje ejeNacionalSeleccionado) {
        this.ejeNacionalSeleccionado = ejeNacionalSeleccionado;
    }

    public List<PlanNacionalObjetivo> getObjetivosNacionales() {
        return objetivosNacionales;
    }

    public void setObjetivosNacionales(List<PlanNacionalObjetivo> objetivosNacionales) {
        this.objetivosNacionales = objetivosNacionales;
    }

    public PlanNacionalObjetivo getObjetivoNacionalSeleccionado() {
        return objetivoNacionalSeleccionado;
    }

    public void setObjetivoNacionalSeleccionado(PlanNacionalObjetivo objetivoNacionalSeleccionado) {
        this.objetivoNacionalSeleccionado = objetivoNacionalSeleccionado;
    }

    public List<PlanNacionalPolitica> getPoliticasNacionales() {
        return politicasNacionales;
    }

    public List<PlanAnualProgramaProyecto> getPlanesAnualesProgramas() {
        return planesAnualesProgramas;
    }

    public void setPlanesAnualesProgramas(List<PlanAnualProgramaProyecto> planesAnualesProgramas) {
        this.planesAnualesProgramas = planesAnualesProgramas;
    }

    public void setPoliticasNacionales(List<PlanNacionalPolitica> politicasNacionales) {
        this.politicasNacionales = politicasNacionales;
    }

    public PlanNacionalPolitica getPoliticaNacionalSeleccionada() {
        return politicaNacionalSeleccionada;
    }

    public void setPoliticaNacionalSeleccionada(PlanNacionalPolitica politicaNacionalSeleccionada) {
        this.politicaNacionalSeleccionada = politicaNacionalSeleccionada;
    }

    public LazyModel<PlanAnualPoliticaPublica> getPlanAnualazy() {
        return planAnualazy;
    }

    public void setPlanAnualazy(LazyModel<PlanAnualPoliticaPublica> planAnualazy) {
        this.planAnualazy = planAnualazy;
    }

    public List<PlanLocalSistema> getSistemasLocales() {
        return sistemasLocales;
    }

    public void setSistemasLocales(List<PlanLocalSistema> sistemasLocales) {
        this.sistemasLocales = sistemasLocales;
    }

    public PlanLocalSistema getSistemaLocalSeleccionado() {
        return sistemaLocalSeleccionado;
    }

    public List<UnidadAdministrativa> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadAdministrativa> unidades) {
        this.unidades = unidades;
    }

    public void setSistemaLocalSeleccionado(PlanLocalSistema sistemaLocalSeleccionado) {
        this.sistemaLocalSeleccionado = sistemaLocalSeleccionado;
    }

    public List<PlanLocalObjetivo> getObjetivosLocales() {
        return objetivosLocales;
    }

    public void setObjetivosLocales(List<PlanLocalObjetivo> objetivosLocales) {
        this.objetivosLocales = objetivosLocales;
    }

    public PlanLocalObjetivo getObjetivoLocalSeleccionado() {
        return objetivoLocalSeleccionado;
    }

    public void setObjetivoLocalSeleccionado(PlanLocalObjetivo objetivoLocalSeleccionado) {
        this.objetivoLocalSeleccionado = objetivoLocalSeleccionado;
    }

    public List<PlanLocalPolitica> getPoliticasLocales() {
        return politicasLocales;
    }

    public void setPoliticasLocales(List<PlanLocalPolitica> politicasLocales) {
        this.politicasLocales = politicasLocales;
    }

    public PlanLocalPolitica getPoliticaLocalSeleccionado() {
        return politicaLocalSeleccionado;
    }

    public void setPoliticaLocalSeleccionado(PlanLocalPolitica politicaLocalSeleccionado) {
        this.politicaLocalSeleccionado = politicaLocalSeleccionado;
    }

    public boolean isRequierePlanNacional() {
        return requierePlanNacional;
    }

    public void setRequierePlanNacional(boolean requierePlanNacional) {
        this.requierePlanNacional = requierePlanNacional;
    }

    public boolean isRequierePlanLocal() {
        return requierePlanLocal;
    }

    public List<PlanNacionalObjetivo> getObjetivosNa() {
        return objetivosNa;
    }

    public void setObjetivosNa(List<PlanNacionalObjetivo> objetivosNa) {
        this.objetivosNa = objetivosNa;
    }

    public void setRequierePlanLocal(boolean requierePlanLocal) {
        this.requierePlanLocal = requierePlanLocal;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public PlanAnualPoliticaPublicaLazy getLazy() {
        return lazy;
    }

    public void setLazy(PlanAnualPoliticaPublicaLazy lazy) {
        this.lazy = lazy;
    }

    public List<PlanAnualProgramaProyecto> getProgramaProyectos() {
        return programaProyectos;
    }

    public void setProgramaProyectos(List<PlanAnualProgramaProyecto> programaProyectos) {
        this.programaProyectos = programaProyectos;
    }

    public PlanAnualProgramaProyecto getProgramaProyecto() {
        return programaProyecto;
    }

    public void setProgramaProyecto(PlanAnualProgramaProyecto programaProyecto) {
        this.programaProyecto = programaProyecto;
    }

    public PlanAnualProgramaProyecto getProgramaProyectoSeleccionado() {
        return programaProyectoSeleccionado;
    }

    public void setProgramaProyectoSeleccionado(PlanAnualProgramaProyecto programaProyectoSeleccionado) {
        this.programaProyectoSeleccionado = programaProyectoSeleccionado;
    }

    public List<ActividadOperativa> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadOperativa> actividades) {
        this.actividades = actividades;
    }

    public ActividadOperativa getActividad() {
        return actividad;
    }

    public void setActividad(ActividadOperativa actividad) {
        this.actividad = actividad;
    }

    public ActividadOperativa getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(ActividadOperativa actividadSeleccionada) {
        this.actividadSeleccionada = actividadSeleccionada;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public PlanAnualPoliticaPublica getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualPoliticaPublica planAnual) {
        this.planAnual = planAnual;
    }

    public PlanAnualPoliticaPublica getPlanAnualSeleccionado() {
        return planAnualSeleccionado;
    }

    public void setPlanAnualSeleccionado(PlanAnualPoliticaPublica planAnualSeleccionado) {
        this.planAnualSeleccionado = planAnualSeleccionado;
    }

    public boolean isOcultarMostarPlanNacional() {
        return ocultarMostarPlanNacional;
    }

    public void setOcultarMostarPlanNacional(boolean ocultarMostarPlanNacional) {
        this.ocultarMostarPlanNacional = ocultarMostarPlanNacional;
    }

    public List<PlanAnualPoliticaPublica> getListaPlanAnualPoliticaPublicas() {
        return listaPlanAnualPoliticaPublicas;
    }

    public void setListaPlanAnualPoliticaPublicas(List<PlanAnualPoliticaPublica> listaPlanAnualPoliticaPublicas) {
        this.listaPlanAnualPoliticaPublicas = listaPlanAnualPoliticaPublicas;
    }

    public List<PlanLocalProgramaProyecto> getListaPlanLocalProgramaProyectos() {
        return listaPlanLocalProgramaProyectos;
    }

    public void setListaPlanLocalProgramaProyectos(List<PlanLocalProgramaProyecto> listaPlanLocalProgramaProyectos) {
        this.listaPlanLocalProgramaProyectos = listaPlanLocalProgramaProyectos;
    }

    public PlanAnualProgramaProyectoLazy getLazyprogramaProyectos() {
        return lazyprogramaProyectos;
    }

    public void setLazyprogramaProyectos(PlanAnualProgramaProyectoLazy lazyprogramaProyectos) {
        this.lazyprogramaProyectos = lazyprogramaProyectos;
    }

    public PlanNacionalObjetivoService getPlanNacionalObjetivoService() {
        return planNacionalObjetivoService;
    }

    public void setPlanNacionalObjetivoService(PlanNacionalObjetivoService planNacionalObjetivoService) {
        this.planNacionalObjetivoService = planNacionalObjetivoService;
    }

    public PlanAnualProgramaProyectoService getPlanAnualProgramaProyectoService() {
        return planAnualProgramaProyectoService;
    }

    public void setPlanAnualProgramaProyectoService(PlanAnualProgramaProyectoService planAnualProgramaProyectoService) {
        this.planAnualProgramaProyectoService = planAnualProgramaProyectoService;
    }

    public UnidadAdministrativaService getUnidadAdministrativaService() {
        return unidadAdministrativaService;
    }

    public void setUnidadAdministrativaService(UnidadAdministrativaService unidadAdministrativaService) {
        this.unidadAdministrativaService = unidadAdministrativaService;
    }

    public ActividadOperativaService getActividadService() {
        return actividadService;
    }

    public void setActividadService(ActividadOperativaService actividadService) {
        this.actividadService = actividadService;
    }

    public ProductoService getProductoService() {
        return productoService;
    }

    public void setProductoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public PlanProgramaticoService getPlanProgramaticoService() {
        return planProgramaticoService;
    }

    public void setPlanProgramaticoService(PlanProgramaticoService planProgramaticoService) {
        this.planProgramaticoService = planProgramaticoService;
    }

    public CatalogoPresupuestoService getCatalogoPrespuestoService() {
        return catalogoPrespuestoService;
    }

    public void setCatalogoPrespuestoService(CatalogoPresupuestoService catalogoPrespuestoService) {
        this.catalogoPrespuestoService = catalogoPrespuestoService;
    }

    public boolean isOcultarNombreProyectos() {
        return ocultarNombreProyectos;
    }

    public void setOcultarNombreProyectos(boolean ocultarNombreProyectos) {
        this.ocultarNombreProyectos = ocultarNombreProyectos;
    }

    public List<CatalogoItem> getListafuenteFinanciamiento() {
        return listafuenteFinanciamiento;
    }

    public void setListafuenteFinanciamiento(List<CatalogoItem> listafuenteFinanciamiento) {
        this.listafuenteFinanciamiento = listafuenteFinanciamiento;
    }

    public List<PlanProgramatico> getListaPlanProgramatico() {
        return listaPlanProgramatico;
    }

    public void setListaPlanProgramatico(List<PlanProgramatico> listaPlanProgramatico) {
        this.listaPlanProgramatico = listaPlanProgramatico;
    }

    public List<CatalogoPresupuesto> getListaPresupuesto() {
        return listaPresupuesto;
    }

    public void setListaPresupuesto(List<CatalogoPresupuesto> listaPresupuesto) {
        this.listaPresupuesto = listaPresupuesto;
    }

    public ProductoLazy getLazyProductos() {
        return lazyProductos;
    }

    public void setLazyProductos(ProductoLazy lazyProductos) {
        this.lazyProductos = lazyProductos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getProductoGlobal() {
        return productoGlobal;
    }

    public void setProductoGlobal(String productoGlobal) {
        this.productoGlobal = productoGlobal;
    }

    public List<FuenteFinanciamiento> getListaFuente() {
        return listaFuente;
    }

    public void setListaFuente(List<FuenteFinanciamiento> listaFuente) {
        this.listaFuente = listaFuente;
    }

    public List<PlanProgramatico> getListaPlanProgramaticos() {
        return listaPlanProgramaticos;
    }

    public void setListaPlanProgramaticos(List<PlanProgramatico> listaPlanProgramaticos) {
        this.listaPlanProgramaticos = listaPlanProgramaticos;
    }

    public FuenteFinanciamientoService getFuenteService() {
        return fuenteService;
    }

    public LazyModel<PlanAnualProgramaProyecto> getPlanAnualProgramaLazy() {
        return planAnualProgramaLazy;
    }

    public void setPlanAnualProgramaLazy(LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy) {
        this.planAnualProgramaLazy = planAnualProgramaLazy;
    }

    public ReformaSuplementoIngresoService getSuplementoIngresoService() {
        return suplementoIngresoService;
    }

    public void setSuplementoIngresoService(ReformaSuplementoIngresoService suplementoIngresoService) {
        this.suplementoIngresoService = suplementoIngresoService;
    }

    public void setFuenteService(FuenteFinanciamientoService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public PlanProgramaticoService getEstrucPlanProgramaticoService() {
        return estrucPlanProgramaticoService;
    }

    public void setEstrucPlanProgramaticoService(PlanProgramaticoService estrucPlanProgramaticoService) {
        this.estrucPlanProgramaticoService = estrucPlanProgramaticoService;
    }

    public CatalogoPresupuestoService getCatalogoPresupuestoService() {
        return catalogoPresupuestoService;
    }

    public void setCatalogoPresupuestoService(CatalogoPresupuestoService catalogoPresupuestoService) {
        this.catalogoPresupuestoService = catalogoPresupuestoService;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuestos() {
        return listaCatalogoPresupuestos;
    }

    public void setListaCatalogoPresupuestos(List<CatalogoPresupuesto> listaCatalogoPresupuestos) {
        this.listaCatalogoPresupuestos = listaCatalogoPresupuestos;
    }

    public List<MasterCatalogo> getListaAnio() {
        return listaAnio;
    }

    public void setListaAnio(List<MasterCatalogo> listaAnio) {
        this.listaAnio = listaAnio;
    }

    public List<ProformaDTO> getProformaModel() {
        return proformaModel;
    }

    public void setProformaModel(List<ProformaDTO> proformaModel) {
        this.proformaModel = proformaModel;
    }

    public Short getAnioProforma() {
        return anioProforma;
    }

    public void setAnioProforma(Short anioProforma) {
        this.anioProforma = anioProforma;
    }

    public double getValorTotalPresupuestoEgresos() {
        return valorTotalPresupuestoEgresos;
    }

    public void setValorTotalPresupuestoEgresos(double valorTotalPresupuestoEgresos) {
        this.valorTotalPresupuestoEgresos = valorTotalPresupuestoEgresos;
    }

    public ProformaPresupuestoPlanificadoService getProformaPresupuestoPlanificadoService() {
        return proformaPresupuestoPlanificadoService;
    }

    public void setProformaPresupuestoPlanificadoService(ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService) {
        this.proformaPresupuestoPlanificadoService = proformaPresupuestoPlanificadoService;
    }

    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public ProductoLazy getProductoAsignacion() {
        return productoAsignacion;
    }

    public void setProductoAsignacion(ProductoLazy productoAsignacion) {
        this.productoAsignacion = productoAsignacion;
    }

    public int getIndiceDeAcordian() {
        return indiceDeAcordian;
    }

    public void setIndiceDeAcordian(int indiceDeAcordian) {
        this.indiceDeAcordian = indiceDeAcordian;
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

    public LazyModel<ReformaIngresoSuplemento> getLazy2() {
        return lazy2;
    }

    public void setLazy2(LazyModel<ReformaIngresoSuplemento> lazy2) {
        this.lazy2 = lazy2;
    }

    public UnidadAdministrativa getUnidadGlobal() {
        return unidadGlobal;
    }

    public void setUnidadGlobal(UnidadAdministrativa unidadGlobal) {
        this.unidadGlobal = unidadGlobal;
    }

    public CatalogoItem getEstadoMostrar() {
        return estadoMostrar;
    }

    public void setEstadoMostrar(CatalogoItem estadoMostrar) {
        this.estadoMostrar = estadoMostrar;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public CuposService getCupoService() {
        return cupoService;
    }

    public void setCupoService(CuposService cupoService) {
        this.cupoService = cupoService;
    }

    public UnidadAdministrativaService getUnidadService() {
        return unidadService;
    }

    public void setUnidadService(UnidadAdministrativaService unidadService) {
        this.unidadService = unidadService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public CatalogoItem getEstadoRegistrado() {
        return estadoRegistrado;
    }

    public void setEstadoRegistrado(CatalogoItem estadoRegistrado) {
        this.estadoRegistrado = estadoRegistrado;
    }

    public Cupos getCupoUnidad() {
        return cupoUnidad;
    }

    public void setCupoUnidad(Cupos cupoUnidad) {
        this.cupoUnidad = cupoUnidad;
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
//</editor-fold>

}
