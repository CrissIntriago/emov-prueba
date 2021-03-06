/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

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
import com.origami.sigef.contabilidad.lazy.PlanAnualPoliticaPublicaLazy;
import com.origami.sigef.contabilidad.lazy.PlanAnualProgramaProyectoLazy;
import com.origami.sigef.contabilidad.lazy.ProductoLazy;
import com.origami.sigef.contabilidad.model.ProformaDTO;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
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
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "pappPresupuestoAsignacionView")
@ViewScoped
public class AsigancionPartidasPappPresupuestoController extends BpmnBaseRoot implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="SERVICIOS - VARIABLES - LISTAS -LAZY">
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
    private ClienteService clienteService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
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

    /*Variables Globales*/
    private String programaProyectoLocal;
    private String productoGlobal;
    private int indiceDeAcordian;
    private Short anioProforma;
    private double valorTotalPresupuestoEgresos;
    private BigDecimal totalActividades;

    /*Lazy*/
    private PlanAnualPoliticaPublicaLazy lazy;
    private PlanAnualProgramaProyectoLazy lazyprogramaProyectos;

    private ProductoLazy lazyProductos;
    //private ProductoLazy productoAsignacion;
    private LazyModel<Producto> productoAsignacion;
    private LazyModel<ActividadOperativa> lazyActividadOperativas;

    private LazyModel<Producto> productoLazy;
    private LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy;
    private LazyModel<PlanAnualPoliticaPublica> planAnualazy;
    private LazyModel<VistaGeneralPlanAnual> vistaGeneralPlanAnualLazy;
    private BigDecimal TotalProducto;
    /*Booleanos*/
    private boolean requierePlanNacional;
    private boolean requierePlanLocal;
    private boolean ocultarMostarPlanNacional;
    private boolean ocultarNombreProyectos;
    private boolean bloqueo;
    private boolean filtroDatosNullProgramaProyectos;
    private boolean filtroDatosNullTablaGeneralPlanes;
    private boolean filtroDatosNullTablaPlanesAnuales;
    private CatalogoItem estadoRegistrado, estadoRechazada, estadoAprobado;
//</editor-fold>

    private BigDecimal totalCupo, cupoAsigando;
    private String estadoGeneral;
    private String observaciones;
    private Short periodoSacado;

    //NUEVO
    @Inject
    private PresCatalogoPresupuestarioService catalogoServiceNew;
    @Inject
    private PresFuenteFinanciamientoService fuenteServiceNew;
    @Inject
    private PresPlanProgramaticoService estructuraServiceNew;

    private List<PresCatalogoPresupuestario> listItem;
    private List<PresPlanProgramatico> listEstructura;
    private List<PresFuenteFinanciamiento> listFuente;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);

                /*Lista*/
                programaProyectos = new ArrayList<>();
                actividades = new ArrayList<>();
                planesAnualesProgramas = new ArrayList<>();
                productos = new ArrayList<>();
                list = new ArrayList<>();

                //NUEVO
                listItem = new ArrayList<>();
                listItem = catalogoServiceNew.findTipoPresupuesto(false);
                listEstructura = new ArrayList<>();
                listEstructura = estructuraServiceNew.getEstructuraProgramatica("programatico_subprograma");
                listFuente = new ArrayList<>();
                listFuente = fuenteServiceNew.getFuenteFinanciamiento();
                //

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
                estadoRegistrado = new CatalogoItem();
                estadoRechazada = new CatalogoItem();
                estadoAprobado = new CatalogoItem();

                estadoRegistrado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "RP");
                estadoRechazada = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "REP");
                estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_papp", "AP");
                periodoSacado = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));
                busqueda.setAnio(periodoSacado);
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
                listaAnio = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "PA", periodoSacado});
                listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, (short) 3, busqueda.getAnio());
                listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, (short) 4, busqueda.getAnio());

                /*Booleanos*/
                ocultarMostarPlanNacional = false;
                requierePlanLocal = true;
                requierePlanNacional = true;
                ocultarNombreProyectos = false;
                bloqueo = false;
                filtroDatosNullProgramaProyectos = true;
                filtroDatosNullTablaGeneralPlanes = true;
                filtroDatosNullTablaPlanesAnuales = true;
                totalCupo = BigDecimal.ZERO;
                cupoAsigando = BigDecimal.ZERO;

                /*Variables Globales*/
                setIndiceDeAcordian(-1);
                productoAsignacion = new LazyModel(Producto.class);
                productoAsignacion.getFilterss().put("periodo:equal", busqueda.getAnio());
                productoAsignacion.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
                productoAsignacion.getFilterss().put("codigoReforma:equal", null);
                productoAsignacion.getFilterss().put("codigoReformaTraspaso:equal", null);
                this.TotalProducto = productoService.getSumaTotalProductos(null, busqueda.getAnio());
                this.totalActividades = productoService.getSumaTotalActividad(null, busqueda.getAnio());

                List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(busqueda.getAnio(), false, true);
                if (li.size() > 0) {
                    setBloqueo(true);
                } else {
                    setBloqueo(false);

                }
                PrimeFaces.current().ajax().update(":formTablaMain");
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void clearAllFilters() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formTablaMain:dataTablePlane");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();

            PrimeFaces.current().ajax().update("formTablaMain:dataTablePlane");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="LOGICA NEGOCIO">
    public void cargandofecha() {
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }

        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        listaPlanProgramatico = estrucPlanProgramaticoService.findByNamedQuery("PlanProgramatico.findByNivelPeriodo", true, (short) 3, busqueda.getAnio());
        listaCatalogoPresupuestos = catalogoPrespuestoService.findByNamedQuery("CatalogoPresupuesto.findByNivelEgresos", true, false, (short) 4, busqueda.getAnio());
        planAnualazy = new LazyModel(PlanAnualPoliticaPublica.class);
        planAnualazy.getFilterss().put("periodo:equal", busqueda.getAnio());
        planAnualazy.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
        lazyProductos = new ProductoLazy(busqueda);
        planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
        planAnualProgramaLazy.getFilterss().put("periodo:equal", busqueda.getAnio());
        planAnualProgramaLazy.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
        productoLazy = new LazyModel(Producto.class);
        productoLazy.getFilterss().put("periodo:equal", busqueda.getAnio());
        productoLazy.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
        productoLazy.getFilterss().put("actividadOperativa.unidadResponsable:equal", d.getUnidadAdministrativa());
        productoLazy.getFilterss().put("codigoReforma:equal", null);
        productoLazy.getFilterss().put("codigoReformaTraspaso:equal", null);
        lazyActividadOperativas = new LazyModel(ActividadOperativa.class);
        lazyActividadOperativas.getFilterss().put("periodo:equal", busqueda.getAnio());
        lazyActividadOperativas.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
        lazyActividadOperativas.getFilterss().put("unidadResponsable:equal", d.getUnidadAdministrativa());
        vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
        vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
        vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
        estadoGeneral = cupoPresupuestoService.estadoPappNomral(busqueda.getAnio());
        desactivarAprobado(busqueda.getAnio());
        resetValues();
        updateTotaleCupos();
        PrimeFaces.current().ajax().update(":formPlanes");
        PrimeFaces.current().ajax().update(":proyectos");
        PrimeFaces.current().ajax().update(":planProgramaProyectos");
        PrimeFaces.current().ajax().update(":actividadesOperativa");
        PrimeFaces.current().ajax().update(":productos");

    }

    public void cargarDatosTablaGeneralPlanAnual() {
        if (busqueda.getAnio() == null) {
            short anio = 0;
            busqueda.setAnio(anio);
        }
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        productoAsignacion = new LazyModel(Producto.class);
        productoAsignacion.getFilterss().put("periodo", busqueda.getAnio());
        productoAsignacion.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
        productoAsignacion.getFilterss().put("codigoReforma:equal", null);
        productoAsignacion.getFilterss().put("codigoReformaTraspaso:equal", null);
        //productoAsignacion.getFilterss().put("actividadOperativa.unidadResponsable:equal", d.getUnidadAdministrativa());
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

    public boolean verificarCuadradoReparticion(ActividadOperativa a) {
        boolean confirmar = true;
        if (a.getCr1().add(a.getCr2()).add(a.getCr3()).doubleValue() == a.getMonotReformado().doubleValue()) {
            confirmar = true;
        } else {
            confirmar = false;
        }
        return confirmar;
    }

    /*Funcion que me permite filtrar los valores null que estan presenten en la tabla "PLAN PROGRAMA PROYECTO" como "NO APLICA"*/
    public void filtroDeDatosNullPlanProgramaProyecto() {
        if (busqueda.getAnio() == 0) {
            JsfUtil.addWarningMessage("AVISO", "SELECIONES UN PERDIODO ANTES DE INICIAR EL PROCESO");
            PrimeFaces.current().ajax().update(":planProgramaProyectos");
            filtroDatosNullProgramaProyectos = true;
        } else {
            if (!filtroDatosNullProgramaProyectos) {
                planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
                planAnualProgramaLazy.getFilterss().put("planAnual:equal", null);
                planAnualProgramaLazy.getFilterss().put("periodo:equal", busqueda.getAnio());
                planAnualProgramaLazy.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
                PrimeFaces.current().ajax().update(":datatable2");
            } else {
                planAnualProgramaLazy = new LazyModel(PlanAnualProgramaProyecto.class);
                planAnualProgramaLazy.getFilterss().put("periodo:equal", busqueda.getAnio());
                planAnualProgramaLazy.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
                PrimeFaces.current().ajax().update(":planProgramaProyectos");
            }
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
                vistaGeneralPlanAnualLazy.getFilterss().put("nombrePlanLocalProgramaProyecto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
                //new VistaGeneralPlanAnualLazy(!filtroDatosNullTablaGeneralPlanes, busqueda);
                PrimeFaces.current().ajax().update("formTablaMain");
            } else {
                vistaGeneralPlanAnualLazy = new LazyModel(VistaGeneralPlanAnual.class);
                vistaGeneralPlanAnualLazy.getFilterss().put("periodoProducto:equal", busqueda.getAnio());
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProducto:equal", null);
                vistaGeneralPlanAnualLazy.getFilterss().put("codigoProductoTraspaso:equal", null);
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
                // productoAsignacion = new ProductoLazy(!filtroDatosNullTablaPlanesAnuales, busqueda);
                productoAsignacion = new LazyModel(Producto.class);
                productoAsignacion.getFilterss().put("periodo:equal", busqueda.getAnio());
                productoAsignacion.getFilterss().put("estadoPapp.codigo", Arrays.asList("RP", "AP", "REP"));
                productoAsignacion.getFilterss().put("actividadOperativa.planProgramaProyecto.planAnual.planLocal:equal", null);
                productoAsignacion.getFilterss().put("codigoReforma:equal", null);
                productoAsignacion.getFilterss().put("codigoReformaTraspaso:equal", null);
                PrimeFaces.current().ajax().update("formTablaMain");
            } else {
                // productoAsignacion = new ProductoLazy(busqueda);
                productoAsignacion = new LazyModel(Producto.class);
                productoAsignacion.getFilterss().put("periodo:equal", busqueda.getAnio());
                productoAsignacion.getFilterss().put("estadoPapp.codigo:equal", Arrays.asList("RP", "AP", "REP"));
                productoAsignacion.getFilterss().put("codigoReforma:equal", null);
                productoAsignacion.getFilterss().put("codigoReformaTraspaso:equal", null);
                PrimeFaces.current().ajax().update("formTablaMain");
            }
        }
    }

    public void desactivarAprobado(Short periodo) {
        boolean verificar = true;
        List<CatalogoProformaPresupuesto> li = proformaPresupuestoPlanificadoService.desactivarBoton(periodo, false, true);

        if (li.size() > 0) {
            verificar = true;
        } else {
            verificar = false;
        }
        setBloqueo(verificar);
    }

    public void calcularSumaEgresoso() {

        List<String> listaProductosra = productoService.CodigoSinRepetir();
        for (String producto1 : listaProductosra) {
            productoService.SumatoriaProformaEgresosProducto(producto1);
        }
    }

//    public void presupuestoEgresos() {
//        valorTotalPresupuestoEgresos = 0;
//
//        this.proformaModel = proformaPresupuestoPlanificadoService.totalProductoProforma(getAnioProforma());
//
//        for (ProformaDTO i : proformaModel) {
//            valorTotalPresupuestoEgresos = valorTotalPresupuestoEgresos + i.getTotal().doubleValue();
//        }
//
//    }
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
     * a??adir en memoria para mostrar en un tabla el proyecto a realizar para
     * realizar actividades
     */
    public void savePlanAnualPoliticaPublica() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String fecha = String.valueOf(year);
        boolean edit = planAnual.getId() != null;
        if (busqueda.getAnio() == 0) {
            JsfUtil.addErrorMessage("FALLO REGISTRO", "Debe selecionar un a??o para poder registrar un Plan Nacionar y un Plan Local");
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
                    planAnual.setEstadoPapp(estadoRegistrado);
                    planAnual.setFechaCreacion(new Date());
                    planAnual.setFechaModificacion(new Date());
                    planAnual.setUsuarioModifica(userSession.getNameUser());

                    planAnual.setUsuarioCreacion("system");
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
                planAnual.setUsuarioModifica(userSession.getNameUser());
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

            if (p.getItemNew() != null && p.getEstructruaNew() != null && p.getFuenteNew() != null) {
                p.setCodigoPresupuestario(p.getEstructruaNew().getCodigo() + p.getItemNew().getCodigo() + p.getFuenteNew().getCodFuente());
                modifico = true;
            } else {
                p.setCodigoPresupuestario(null);
            }
//            if (p.getFuente() != null) {
//              // p.setFuenteDirecta(p.getFuente().getTipoFuente());
//                modifico = true;
//            }
            productoService.edit(p);

            Boolean consultaTabla = proformaPresupuestoPlanificadoService.consultarDatos(p.getPeriodo(), "PPPA");
            if (consultaTabla) {
                //Al actualizar un item, estructura o fuente, en la tabla de proforma_presupuesto_planificado se actualizara esta partida modificada
                //if (!partidaAntigua.equals("") && !partidaAntigua.equals(partidaNueva)) {
                seModificoPPPA(modifico, p.getPeriodo());
                // }
            }

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
                proformaPresupuesto.setUsuarioCreacion(userSession.getNameUser());
                proformaPresupuesto.setFechaCreacion(new Date());
                proformaPresupuesto.setUsuarioModificacion(userSession.getNameUser());
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
        JsfUtil.addSuccessMessage("Plan Programa Proyecto", c.getPlanLocal().getDescripcion() + "eliminado con ??xito");
    }

    public void a??adirPlanAnualPoliticaPublica(PlanAnualPoliticaPublica pa) {
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
                programaProyecto.setUsuarioCreacion(userSession.getNameUser());
                programaProyecto.setUsuarioModifica(userSession.getNameUser());
                programaProyecto.setEstadoPapp(estadoRegistrado);
                this.programaProyecto = planAnualProgramaProyectoService.create(programaProyecto);
            } else {
                programaProyecto.setUsuarioModifica(userSession.getNameUser());
                programaProyecto.setFechaModificacion(new Date());
                planAnualProgramaProyectoService.edit(programaProyecto);
            }
            setIndiceDeAcordian(2);

            PrimeFaces.current().executeScript("PF('DlgPlanProyecto').hide()");
            PrimeFaces.current().ajax().update(":dataTablePlanes");
            PrimeFaces.current().ajax().update("messages");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Informaci??n", programaProyecto.getNombreProgramaProyecto() + (edit ? " editado" : " guardado") + " con ??xito.");
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
            JsfUtil.addErrorMessage("Informaci??n", "No se puede eliminar porque tiene actividades operacionales sociales.");
            return;
        }
        planAnualProgramaProyectoService.remove(p);
        PrimeFaces.current().ajax().update(":formPlanProyecto");
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Informaci??n", "eliminado con ??xito.");
    }

    public void a??adirPlanAnualProgramaProyecto(PlanAnualProgramaProyecto p) {
        actividad = new ActividadOperativa();
        actividad.setPlanProgramaProyecto(p);
        PrimeFaces.current().executeScript("PF('DlgActividad').show()");
        PrimeFaces.current().ajax().update(":formActividad");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
    }

    public void saveActividadOperativa() {
        try {

            boolean edit = actividad.getId() != null;
            String meta1 = "", meta2 = "", meta3 = "";
            meta1 = String.valueOf(actividad.getCuatrimestre1Meta());
            meta2 = String.valueOf(actividad.getCuatrimestre2Meta());
            meta3 = String.valueOf(actividad.getCuatrimestre3Meta());

            if (calcularActiviadaMonto()) {
                if (actividad.getMedicionPorcentaje() == true) {
                    actividad.setMedicionMeta(BigDecimal.valueOf(100));
                }
                if (actividad.getCuatrimestre1Meta() == null || meta1.length() == 0) {
                    actividad.setCuatrimestre1Meta(BigDecimal.ZERO);

                } else if (actividad.getCuatrimestre2Meta() == null || meta2.length() == 0) {
                    actividad.setCuatrimestre2Meta(BigDecimal.ZERO);

                } else if (actividad.getCuatrimestre3Meta() == null || meta3.length() == 0) {
                    actividad.setCuatrimestre3Meta(BigDecimal.ZERO);
                }
                if (calcularSumaActividades()) {
                    if (!edit) {
                        BigDecimal valorAsignacion = cupoPresupuestoService.verficiarCupoSuplemento(busqueda.getAnio(), actividad.getUnidadResponsable());
                        if (valorAsignacion.add(actividad.getMonto()).doubleValue() > totalCupo.doubleValue()) {
                            JsfUtil.addErrorMessage("Error", "No se asignar ese valor porque sobrepasa el cupo que se le asigno");
                            return;
                        }

                        actividad.setEstado(true);
                        actividad.setFechaCreacion(new Date());
                        actividad.setUsuarioCreacion(userSession.getNameUser());
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setEstadoPapp(estadoRegistrado);
                        actividad.setMonotReformado(actividad.getMonto());
                        actividad.setCr1(actividad.getCuatrimestre1Actividad());
                        actividad.setCr2(actividad.getCuatrimestre2Actividad());
                        actividad.setCr3(actividad.getCuatrimestre3Actividad());
                        this.actividad = actividadService.create(actividad);
                    } else {
                        BigDecimal valorasignadoActual = cupoPresupuestoService.getValorAsignado(actividad);
                        BigDecimal valorAsignacion = cupoPresupuestoService.verficiarCupoSuplemento(busqueda.getAnio(), actividad.getUnidadResponsable());
                        if (valorAsignacion.add(actividad.getMonto()).subtract(valorasignadoActual).doubleValue() > totalCupo.doubleValue()) {
                            JsfUtil.addErrorMessage("Error", "No se asignar ese valor porque sobrepasa el cupo que se le asigno");
                            return;
                        }
                        actividad.setEstado(true);
                        actividad.setFechaModificacion(new Date());
                        actividad.setUsuarioModifica(userSession.getNameUser());
                        actividad.setPeriodo(busqueda.getAnio());
                        actividad.setMonotReformado(actividad.getMonto());
                        actividad.setCr1(actividad.getCuatrimestre1Actividad());
                        actividad.setCr2(actividad.getCuatrimestre2Actividad());
                        actividad.setCr3(actividad.getCuatrimestre3Actividad());
                        actividadService.edit(actividad);
                    }
                    setIndiceDeAcordian(3);
                    updateTotaleCupos();
                    PrimeFaces.current().ajax().update("formMain");
                    PrimeFaces.current().executeScript("PF('DlgActividad').hide()");
                    PrimeFaces.current().ajax().update(":formActividad");
                    PrimeFaces.current().ajax().update(":dataTablePlanes");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addInformationMessage("Informaci??n", actividad.getNombreActividad() + (edit ? "  editado" : "  guardado") + " con ??xito.");
                    actividad = new ActividadOperativa();
                } else {
                    PrimeFaces.current().ajax().update(":formActividad");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Informaci??n", "La suma de los cuatrimestre no son iguales a la meta indicada");
                }
            } else {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Informaci??n", "La suma de los cuatrimestre de a actividad deber ser igual al monto de la actividad");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Informacsion", "No pueden estar vacion los campos");
        }

    }

    public void editarActividadOperativa(ActividadOperativa a) {
        actividad = a;
        PrimeFaces.current().executeScript("PF('DlgActividad').show()");
        PrimeFaces.current().ajax().update(":formActividad");
        PrimeFaces.current().ajax().update(":listaProductos");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
    }

    public void eliminarActividadOperativa(ActividadOperativa a) {

        if (getHijosByPadre(null, null, a, 3)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Informaci??n", "No se puede eliminar porque tiene Productos asociados");
            return;
        }

        actividadService.remove(a);
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":productos");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Informaci??n", a.getNombreActividad() + " eliminado con ??xito.");

    }

    public Boolean calcularActiviadaMonto() {
        boolean verificacion = true;

        String cuat1 = String.valueOf(actividad.getCuatrimestre2Actividad());
        String cuat2 = String.valueOf(actividad.getCuatrimestre1Actividad());
        String cuat3 = String.valueOf(actividad.getCuatrimestre3Actividad());
        String monto = String.valueOf(actividad.getMonto());
        double num1, num2, num3, result, resultado;

        num1 = Double.valueOf(cuat1);
        num2 = Double.valueOf(cuat2);
        num3 = Double.valueOf(cuat3);
        result = num1 + num2 + num3;
        resultado = Double.valueOf(monto);
        if (result == resultado) {
            verificacion = true;
        } else {
            verificacion = false;
        }

        return verificacion;
    }

    public Boolean calcularSumaActividades() {

        boolean verificacion = true;
        String cuat1 = String.valueOf(actividad.getCuatrimestre1Meta());
        String cuat2 = String.valueOf(actividad.getCuatrimestre2Meta());
        String cuat3 = String.valueOf(actividad.getCuatrimestre3Meta());
        String meta = String.valueOf(actividad.getMedicionMeta());
        double num1, num2, num3, result, medicionMeta;

        num1 = Double.valueOf(cuat1);
        num2 = Double.valueOf(cuat2);
        num3 = Double.valueOf(cuat3);
        result = num1 + num2 + num3;
        medicionMeta = Double.valueOf(meta);

        if (result == medicionMeta) {
            verificacion = true;
        } else {
            verificacion = false;
        }
        return verificacion;
    }

    public void abrirDlogoRestribuccion(ActividadOperativa a) {
        actividad = new ActividadOperativa();
        actividad = a;
        PrimeFaces.current().executeScript("PF('DlgActividadRestribucciones').show()");
        PrimeFaces.current().ajax().update(":formActividadRestribucciones");
    }

    public void guardarRestribucciones() {

        if (actividad.getCr1() == null && actividad.getCr2() == null && actividad.getCr3() == null) {
            actividad.setCr1(BigDecimal.ZERO);
            actividad.setCr2(BigDecimal.ZERO);
            actividad.setCr3(BigDecimal.ZERO);
            actividadService.edit(actividad);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "LOS NO PUEDEN ESTAR VACIOS");
            return;
        }

        if (actividad.getCr1().add(actividad.getCr2()).add(actividad.getCr3()).doubleValue() != actividad.getMonotReformado().doubleValue()) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "LA SUMA DE LOS CUATRIMESTRE REFORMADOS NO SON IGUALES AL MONTO REFORMADA DE LA ACTIVIDAD");
            return;
        }

        actividadService.edit(actividad);
        actividad = new ActividadOperativa();
        PrimeFaces.current().executeScript("PF('DlgActividadRestribucciones').hide()");
        PrimeFaces.current().ajax().update(":formActividadRestribucciones");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("ERROR", "SU REGISTRO SE REALIZO CON EXITO");
    }

    public void a??adirActividadOperativa(ActividadOperativa a) {
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
                        producto.setSuplementoEgreso(BigDecimal.ZERO);
                        producto.setReserva(BigDecimal.ZERO);
                        producto.setComprometido(BigDecimal.ZERO);
                        producto.setTraspasoIncremento(BigDecimal.ZERO);
                        producto.setReduccionEgreso(BigDecimal.ZERO);
                        producto.setTraspasoReduccion(BigDecimal.ZERO);
                        producto.setMontoReformada(producto.getMonto());
                        producto.setEstadoPapp(estadoRegistrado);
                        this.producto = productoService.create(producto);
                        setIndiceDeAcordian(4);
                        PrimeFaces.current().ajax().update(":formMain");
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":dataTablePlanes");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addInformationMessage("Informaci??n", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con ??xito.");
                        producto = new Producto();

                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Informaci??n", "La suma de los productos deber ser igual al monto de la actividad");
                    }
                } else {
                    PrimeFaces.current().ajax().update(":formProducto");
                    PrimeFaces.current().ajax().update("messages");
                    JsfUtil.addErrorMessage("Informaci??n", "La suma de los productos deber ser igual al monto de la actividad");
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
                        producto.setReserva(BigDecimal.ZERO);
                        producto.setTraspasoIncremento(BigDecimal.ZERO);
                        producto.setTraspasoReduccion(BigDecimal.ZERO);
                        producto.setMontoReformada(producto.getMonto().add(producto.getSuplementoEgreso()));
                        productoService.edit(producto);
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        PrimeFaces.current().ajax().update(":dataTablePlanes");
                        JsfUtil.addInformationMessage("Informaci??n", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con ??xito.");
                        producto = new Producto();
                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Informaci??n", "La suma de los productos deber ser igual al monto de la actividad");
                    }

                } else {
                    if (Double.valueOf(actividadComparar) <= calcularMontoProducto(producto.getActividadOperativa())) {
                        producto.setUsuarioModifica(userSession.getNameUser());
                        producto.setFechaModificacion(new Date());
                        productoService.edit(producto);
                        PrimeFaces.current().executeScript("PF('DdlgProducto').hide()");
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        PrimeFaces.current().ajax().update(":dataTablePlanes");
                        JsfUtil.addInformationMessage("Informaci??n", producto.getDescripcion() + (edit ? "  editado" : "  guardado") + " con ??xito.");
                        producto = new Producto();
                    } else {
                        PrimeFaces.current().ajax().update(":formProducto");
                        PrimeFaces.current().ajax().update("messages");
                        JsfUtil.addErrorMessage("Informaci??n", "La suma de los productos deber ser igual al monto de la actividad");
                    }
                }
                PrimeFaces.current().ajax().update(":dataTablePlanes");
                PrimeFaces.current().ajax().update("formMain");
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Informaci??n", "Los campos no puedene estar vacios");
        }
    }

    public void eliminarProducto(Producto p) {

        productoService.eliminarPstoPlanificado(p.getPeriodo(), p.getCodigoPresupuestario());

        this.productoService.remove(p);

        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":dataTablePlanes");
        JsfUtil.addInformationMessage("Informaci??n", p.getDescripcion() + " eliminado con ??xito.");

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
        JsfUtil.addInformationMessage("Informaci??n", " Presupuesto asignado al producto " + producto.getDescripcion() + " con ??xito.");

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

            JsfUtil.addInformationMessage("Informaci??n", " Presupuesto asignado al producto " + producto.getDescripcion() + " con ??xito.");

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
        List<ActividadOperativa> resultActividades = actividadService.getListActividades(busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        List<Producto> resultProductos = productoService.getListProductos(busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
        if (resultActividades.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay Actividades Operativas registrados en el per??odo " + busqueda.getAnio());
            return;
        } else {
            if (resultProductos.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO", "No hay Productos registrados en el per??odo " + busqueda.getAnio());
                return;
            } else {
                for (ActividadOperativa actividad : resultActividades) {
                    BigDecimal totalProducto = productoService.getSumaProducto(actividad, busqueda.getAnio(), Arrays.asList("AP", "RP", "REP"));
                    if (actividad.getMonto().compareTo(totalProducto) != 0) {
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
                    PrimeFaces.current().executeScript("PF('dlgActividadProductosConsulta').show()");
                    PrimeFaces.current().ajax().update("formActividadProductosConsulta");
                } else {
                    list = ListDesequilibrado;
                    PrimeFaces.current().executeScript("PF('dlgActividadProductosConsulta').show()");
                    PrimeFaces.current().ajax().update("formActividadProductosConsulta");
                }
            }
        }
    }

    public void updateTotaleCupos() {
        Distributivo d = clienteService.getuusuarioLogeado(userSession.getNameUser());
        totalCupo = BigDecimal.ZERO;
        cupoAsigando = BigDecimal.ZERO;
        totalCupo = cupoPresupuestoService.getCuposPadres(busqueda.getAnio(), d.getUnidadAdministrativa(), BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsigando = cupoPresupuestoService.verficiarCupoSuplemento(busqueda.getAnio(), d.getUnidadAdministrativa());

    }

    public void sumatotalArticulosPartidas() {

        this.TotalProducto = productoService.getSumaTotalProductos(idUnidad, busqueda.getAnio());
        this.totalActividades = productoService.getSumaTotalActividad(idUnidad, busqueda.getAnio());
    }
//</editor-fold>

    public void abriDlogo() {

        if (busqueda.getAnio() == null) {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "Eliga un Per??odo");
            return;

        }

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
            //clienteService.getUnidadUserCodigo("CPR","2")
            getParamts().put("usuario", userSession.getNameUser());
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

//<editor-fold defaultstate="collapsed" desc="Getters-n-Setters">
    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getCupoAsigando() {
        return cupoAsigando;
    }

    public void setCupoAsigando(BigDecimal cupoAsigando) {
        this.cupoAsigando = cupoAsigando;
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

    public LazyModel<PlanAnualProgramaProyecto> getPlanAnualProgramaLazy() {
        return planAnualProgramaLazy;
    }

    public void setPlanAnualProgramaLazy(LazyModel<PlanAnualProgramaProyecto> planAnualProgramaLazy) {
        this.planAnualProgramaLazy = planAnualProgramaLazy;
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

    public LazyModel<Producto> getProductoAsignacion() {
        return productoAsignacion;
    }

    public void setProductoAsignacion(LazyModel<Producto> productoAsignacion) {
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

//</editor-fold>
    public String getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(String estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

    public List<PresPlanProgramatico> getListEstructura() {
        return listEstructura;
    }

    public void setListEstructura(List<PresPlanProgramatico> listEstructura) {
        this.listEstructura = listEstructura;
    }

    public List<PresFuenteFinanciamiento> getListFuente() {
        return listFuente;
    }

    public void setListFuente(List<PresFuenteFinanciamiento> listFuente) {
        this.listFuente = listFuente;
    }

}
