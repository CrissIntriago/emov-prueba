package com.gestionTributaria.Controller;

import com.asgard.Entity.AppDepartamentoDetalleTitulos;
import com.asgard.Entity.FinaRenActividadComercial;
import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEntidadBancaria;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalCategoria;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoEntidadBancaria;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoLocalComercial;
import com.asgard.Entity.RenTipoLiquidacionDepartamento;
import com.gestionTributaria.Commons.VerCedulaUtils;
import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Entities.EspCementerioBoveda;
import com.gestionTributaria.Entities.EspFotoBovedas;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.RecActasEspeciesDet;
import com.gestionTributaria.Entities.RenDepartamentoDetalleTitulos;
import com.gestionTributaria.Entities.RenLocalCantidadAccesorios;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Entities.RenTasaTurismo;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.PlanificacionService;
import com.gestionTributaria.Services.RenDepartamentoDetalleTitulosServices;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.PagoTituloReporteModel;
import com.gestionTributaria.models.ReportGenealModel;
import com.gestionTributaria.models.TitulosReporteCacheLocal;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@Named(value = "turismoHabilitacionView")
@ViewScoped
public class TurismoHabilitacionMB extends ReportGenealModel implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES">
    public static final Long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(TurismoHabilitacionMB.class.getName());

    @Inject
    private ManagerService service;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;

    @Inject
    private TitulosReporteCacheLocal titulosReporte;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private RenDepartamentoDetalleTitulosServices renDepartamentoDetalleTitulosServices;

    @Inject
    private PlanificacionService planificacionService;

    @Inject
    private FinaRenLiquidacionService liquidacionService;

    private TreeNode root = new DefaultTreeNode("Titulos", null);
    private TreeNode selectedNode;
    private Usuarios cajero;
    //TITULO DE CREDITO
    private LazyModel<FinaRenLiquidacion> titulosCredito;
    //TITULO DE CREDITO PAGADO
    private LazyModel<FinaRenLiquidacion> titulosCreditoPagado;

    //EMISIONES PREDIALES
    //private RenLiquidacionesLazy emisionesPrediales;
    //COBROS GENERALES
    private TreeNode root2;
    private FinaRenTipoLiquidacion liquidacionCG;
    private List<FinaRenRubrosLiquidacion> rubrosSeleccionado = new ArrayList<>();
    private FinaRenDetLiquidacion detalle = new FinaRenDetLiquidacion();
    private FinaRenRubrosLiquidacion rubroSelect = new FinaRenRubrosLiquidacion();
    private FinaRenLiquidacion cobrosGenerales = new FinaRenLiquidacion();
    private FinaRenLiquidacion liquidacion;
    private RecActasEspeciesDet acta = new RecActasEspeciesDet();
    private LazyModel<Cliente> solicitantes;
    private CatPredioModel pr = new CatPredioModel();
    private Integer numPredio;
    private Integer cantidad;
    private Integer desdeTemp;
    private List<String> cdlas;
    private List<FinaRenLiquidacion> emisiones = new ArrayList<>();
    private FinaRenLiquidacion liqSelect = new FinaRenLiquidacion();
    private List<FinaRenEntidadBancaria> bancos;
    private List<FinaRenEntidadBancaria> tarjetas;
    private BigDecimal totalCoactiva = new BigDecimal("0.00");
    private Map<String, Object> paramt;
    private PagoTituloReporteModel modelPago = new PagoTituloReporteModel();
    private Boolean pagoRealizado = Boolean.FALSE;
    private FnSolicitudExoneracion solicitud;
    private Integer tipoConsulta = 1;
    private Integer tipoSolicitante;
    private CatPredioModel predioModel = new CatPredioModel();
    private Cliente ente = new Cliente();
    private List<CatPredioModel> propiedades = new ArrayList<>();
    private List<CatPredioModel> catPredioLazy;
    private Date fechaReporte = new Date();
    private Date fechaCierre = new Date();
    private Long tipoReporte;
    /*AGREGADA*/
    private Long tipoReporte2;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Cliente registroEnte = new Cliente();
    private Integer tipoEnte = 1;
    private Boolean excepcionalEmpresa = Boolean.FALSE;
    private String ciRucCobros;
    private Boolean variosPagos = Boolean.FALSE;
    private final Long asistente_renta = 189L;
    private final Long director_renta = 98L;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<Object> parametros;
    private AppDepartamentoDetalleTitulos catPlanificacionTitulos;
    private Boolean tipoCertificado;
    private LazyModel<AppDepartamentoDetalleTitulos> catPlanificacionTitulosLazy;
    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;
    private RenDepartamentoDetalleTitulos planificacionTitulos;
    private List<RenDepartamentoDetalleTitulos> planificacionTitulosList;
    private List<UnidadAdministrativa> deps;
    private UnidadAdministrativa departamento;
    private Boolean esLiq;
    private List<Usuarios> usuarios;
    private Usuarios usuario;
    private Date desde;
    private Date hasta;
    private Boolean calculoEmpleados;
    private Boolean calculo10Porciento, calculoConvenio;
    private Boolean renderPlanificacion, renderPermisoCementerio;
    private LazyModel<EspCementerioBoveda> bovedasLazy;
    private EspCementerioBoveda boveda;
    private AppDepartamentoDetalleTitulos departamentoDetalleTitulosGetionAmbiental;
    private LazyModel<FinaRenLocalComercial> localesComercialesLazy;
    private FinaRenLocalComercial localComercial;
    private String identificacionCobros;
    private RenTasaTurismo renTasaTurismo;
    private Date emisionTurismo;
//    private CuentaLazy cuentaLazy;
//    private List<Medidor> medidores;
//    private MedidorCuenta medidorCuenta;
//    private ConvenioMedidorLazy convenioMedidorLazy;
    private List<EspFotoBovedas> bovedasPropietario;

    private List<Long> departamentosIds;
    private Boolean esRegistroPropiedad = Boolean.FALSE;

//    private ConvenioCuenta convenioCuenta;
    private String observacionConvenioCuenta;
    private boolean turismotipo, tasaHabitacion, permisoAdicional;
    private static final BigDecimal PORCENTAJE = BigDecimal.valueOf(100L);
    private static final BigDecimal PORCENTAJE_TREINTA = BigDecimal.valueOf(30L);
    private BigDecimal sueldoBasico;
    private List<String> listaMultasIneteres;
    private RenParametrosInteresMulta renMultasIneteres;
//</editor-fold>
    private boolean renovacion = false;
    private String descripcionMulta;
    private List<Integer> listaVeces;
    private Integer veces;
    private List<RenTasaTurismo> listaTasasTurismo;
    private List<FinaRenActividadComercial> actividesLocales;
    private RenTasaTurismo seleccionTasa;
    private FinaRenActividadComercial seleccionActividad;
    private List<FinaRenTipoLocalComercial> listaTipoLocales;
    private FinaRenTipoLocalComercial tipoLocal;
    private List<CatalogoItem> listaFactoresUbicacion;
    private CatalogoItem factorUbicacion;

    @PostConstruct
    public void initView() {
        listaFactoresUbicacion = new ArrayList<>();
        factorUbicacion = new CatalogoItem();
        listaTipoLocales = new ArrayList<>();
        tipoLocal = new FinaRenTipoLocalComercial();
        seleccionTasa = new RenTasaTurismo();
        seleccionActividad = new FinaRenActividadComercial();
        listaTasasTurismo = new ArrayList<>();
        actividesLocales = new ArrayList<>();
        sueldoBasico = BigDecimal.ZERO;
        listaMultasIneteres = new ArrayList<>();
        cobrosGenerales = new FinaRenLiquidacion();
        cobrosGenerales.setAnio(Utils.getAnio(new Date()));
        rubrosSeleccionado = new ArrayList<>();
        fechaDesde = new Date();
        fechaHasta = new Date();
        listaVeces = new ArrayList<>();
        observacionConvenioCuenta = "";
        renMultasIneteres = new RenParametrosInteresMulta();
        turismotipo = Boolean.FALSE;
        tasaHabitacion = Boolean.FALSE;
        permisoAdicional = Boolean.FALSE;
        this.localComercial = new FinaRenLocalComercial();
        try {
            if (session != null) {
                departamentosIds = new ArrayList<>();
                for (Long id : session.getDepts()) {
                    if (!departamentosIds.contains(id)) {
                        departamentosIds.add(id);
                    }
                }
                deps = service.getDepartamentosById(departamentosIds);
                tipoCertificado = Boolean.FALSE;
                //catPlanificacionTitulosLazy = new CatPlanificacionTitulosLazy();

                esLiq = true;
                titulosReporte.clearCache();
                paramt = new HashMap<>();
                paramt.put("usuario", session.getNameUser());
                cajero = (Usuarios) service.findByParameter(Usuarios.class, paramt);
                paramt = new HashMap<>();
                paramt.put("estado", Boolean.TRUE);
                paramt.put("tipo", new FinaRenTipoEntidadBancaria(1L));
                bancos = service.findAll(FinaRenEntidadBancaria.class, paramt);
                paramt.put("tipo", new FinaRenTipoEntidadBancaria(2L));
                tarjetas = service.findAll(FinaRenEntidadBancaria.class, paramt);
                loadValuesPlanificacionTitulo();
                desde = new Date();
                hasta = new Date();
                calculoEmpleados = Boolean.FALSE;
                calculo10Porciento = Boolean.FALSE;
                calculoConvenio = Boolean.FALSE;
                loadRenders();
                bovedasLazy = new LazyModel<>(EspCementerioBoveda.class);
                localesComercialesLazy = new LazyModel<>(FinaRenLocalComercial.class);
                localesComercialesLazy.getFilterss().put("estado", true);
                localesComercialesLazy.getSorteds().put("id", "DESC");
                localesComercialesLazy.setDistinct(false);
                //cdlas = service.findAll(Querys.getListNombresCdla);
                //cuentaLazy = new CuentaLazy();
                //medidores = service.findAll(Medidor.class);
                pr = null;
                if (service.isAdmin(session.getNameUser(), "admin") || service.isAdmin(session.getNameUser(), "JEFA DE RENTAS")) {
                    llenarArbolAdmin();
                } else {
                    llenarArbol();
                }
                if (esRegistroPropiedad != null) {
                    if (esRegistroPropiedad) {
                        cobrosGenerales.setObservacion("RETIRAR 8 DIAS LABORABLES DESPUÉS DE SU PAGO");
                    }

                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void loadMedidorCuentaValues() {
//        medidorCuenta = new MedidorCuenta();
//        medidorCuenta.setLecturaInicial(BigInteger.ZERO);
//        medidorCuenta.setLecturaFinal(BigInteger.ZERO);
//        medidorCuenta.setEstado(Boolean.TRUE);
//        medidorCuenta.setFechaIngreso(new Date());
//        medidorCuenta.setUsuarioIngreso(session.getName_user());
    }

    private void loadRenders() {
        renderPlanificacion = false;
        for (Long l : session.getDepts()) {
            if (l.equals(47L)) {
                renderPlanificacion = true;
                break;
            }
            // REGISTRO DE LA PROPIEDAD
            if (l.equals(4L)) {
                esRegistroPropiedad = Boolean.TRUE;
                break;
            }

        }

    }

    public void renderPermisoCementerio(FinaRenLiquidacion lR) {
        this.renderPermisoCementerio = false;
        if (lR.getTipoLiquidacion().getId().equals(215L)) {
            this.renderPermisoCementerio = true;
        }
    }

    public void loadValuesPlanificacionTitulo() {

        catPlanificacionTitulos = new AppDepartamentoDetalleTitulos();
        catPlanificacionTitulos.setId(null);
        //   catPlanificacionTitulos.setPara(groovyUtil.getExpression("getPara", null).toString());
        //   catPlanificacionTitulos.setDe(groovyUtil.getExpression("getDe", null).toString());
        catPlanificacionTitulos.setFecha(new Date());
        catPlanificacionTitulos.setFechaLetra(Utils.convertirFechaLetra(new Date()));
        catPlanificacionTitulos.setUsuario(session.getNameUser().trim());
        Usuarios aclUser = service.find(Usuarios.class, session.getUserId());
        catPlanificacionTitulos.setDepartamento(clienteService.getUnidadPrincipalUSer(session.getNameUser()).getNombre().toUpperCase());
        catPlanificacionTitulos.setEstado(Boolean.TRUE);
        planificacionTitulosList = new ArrayList();
    }

    public void onChangeTab(TabChangeEvent event) {
        try {
            if (event.getTab().getId().equals("tituloCredito")) {
                if (titulosCredito == null) {
                    emisiones = new ArrayList<>();
                    liquidacion = new FinaRenLiquidacion();
                    cobrosGenerales = new FinaRenLiquidacion();
                    cobrosGenerales.setAnio(Utils.getAnio(new Date()));
                    liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
                    liquidacionesLazy.getFilterss().put("tipoLiquidacion.prefijo", Arrays.asList("TLT","PLC", "PFU", "PES", "PMM", "PCD"));
                    liquidacionesLazy.getSorteds().put("id", "DESC");
                    liquidacionesLazy.setDistinct(false);
                    JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidaciones");

                    //   cargarLazy();
                    JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidaciones");

                }
            }
            if (event.getTab().getId().equals("tituloCreditoPagado")) {

                if (titulosCreditoPagado == null) {
                    emisiones = new ArrayList<>();
                    liquidacion = new FinaRenLiquidacion();
                    cobrosGenerales = new FinaRenLiquidacion();
                    cobrosGenerales.setAnio(Utils.getAnio(new Date()));
                    titulosCreditoPagado = new LazyModel<>(FinaRenLiquidacion.class);
                    titulosCreditoPagado.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(1L));
                    titulosCreditoPagado.getFilterss().put("tipoLiquidacion.prefijo", Arrays.asList("TLT", "PLC", "PFU", "PES", "PMM", "PCD"));
                    titulosCreditoPagado.getSorteds().put("fechaIngreso", "DESC");
                    titulosCreditoPagado.setDistinct(false);
                    //RenLiquidacionesLazy("mainForm:tvRecaudaciones:dtLiquidacionesPagadas");
                    JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidacionesPagadas");
                }
            }
            if (event.getTab().getId().equals("conveniosMedidores")) {
                // convenioMedidorLazy = new ConvenioMedidorLazy();
                JsfUtil.update("mainForm:tvRecaudaciones:dtConvenioMedidores");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    public void cargarLazy() {
        liquidacionesLazy = new LazyModel<>(FinaRenLiquidacion.class);
        // liquidacionesLazy.getFilterss().put("", Arrays.asList("","",""));
    }

    public void encerarValor() {
        if (rubroSelect.getValor() == null) {
            return;
        }
        if (rubroSelect.getValor().compareTo(BigDecimal.ZERO) == 0) {
            rubroSelect.setValor(null);
            JsfUtil.update("formValores");
        }
    }

    public void showDlgEntes(Integer codigo) {
        try {
            System.out.println("numRecibpr test " + planificacionService.getMaxNumReciboCatPlanificacionTitulos());

            VerCedulaUtils validacion = new VerCedulaUtils();
            tipoSolicitante = codigo;
            if (this.ciRucCobros != null && this.ciRucCobros.length() == 10 && validacion.isCIValida(ciRucCobros)) {
                if (this.registroEnte == null) {
                    this.registroEnte = new Cliente();
                }
                this.registroEnte.setIdentificacion(this.ciRucCobros);
                this.existeCedula();
//                if (this.registroEnte != null && (this.registroEnte.getId() != null || this.registroEnte.getIdentificacion() != null)) {
//                    if (this.registroEnte.getId() == null) {
//                        registroEnte.setUsuarioCreacion(session.getNameUser());
//                        registroEnte.setFechaCreacion(new Date());
//                        registroEnte = (registroEnte);
//                    }
//                    this.cobrosGenerales.setComprador(this.registroEnte);
//                } }
                if (this.registroEnte == null) {
                    solicitantes = new LazyModel(Cliente.class);
                    solicitantes.getFilterss().put("validado", true);
                    solicitantes.getFilterss().put("estado", true);
                    JsfUtil.update("frmSolicitante");
                    JsfUtil.executeJS("PF('dlgSolicitante').show();");
                } else {
                    this.cobrosGenerales.setComprador(this.registroEnte);
                    JsfUtil.update("mainForm");

                }

            } else {
                solicitantes = new LazyModel(Cliente.class);
                solicitantes.getFilterss().put("validado", true);
                solicitantes.getFilterss().put("estado", true);
                JsfUtil.update("frmSolicitante");
                JsfUtil.executeJS("PF('dlgSolicitante').show();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cambioTipoConsulta() {
        totalCoactiva = new BigDecimal("0.00");
        emisiones = new ArrayList<>();
        pr = new CatPredioModel();
        ente = new Cliente();
        predioModel = new CatPredioModel();
    }

    public void onRowSelect() {
        try {
            if (rubroSelect != null) {
                System.out.println("rubroSelect " + rubroSelect);
                if (rubroSelect.getId() != null) {
                    rubroSelect = service.find(FinaRenRubrosLiquidacion.class, rubroSelect.getId());

                    if (rubrosSeleccionado.contains(rubroSelect)) {
                        JsfUtil.addErrorMessage("Ya esta seleccionado el mismo rubro.", "");
                    } else {
                        rubroSelect.setValorCalculo(BigDecimal.ZERO);
                        rubroSelect.setCantidad(1);
                        rubroSelect.setAnio(Utils.getAnio(new Date()));
                        rubroSelect.setValorTotal(rubroSelect.getValor());
                        if (rubroSelect.getRecEspecies() != null) {
                            acta = service.getActaByEspecieYUser(rubroSelect.getRecEspecies().getId(), session.getUserId());
                            if (acta != null) {
                                acta.setDisponiblesTemp(acta.getDisponibles());
                                acta.setDisponibles(acta.getDisponibles() - 1);
                                if (acta.getUltimoVendido() == 0) {
                                    acta.setDesdeTemp(acta.getDesde());
                                    acta.setHastaTemp(acta.getDesde());
                                } else {
                                    acta.setDesdeTemp(acta.getUltimoVendido() + 1);
                                    acta.setHastaTemp(acta.getDesdeTemp());
                                }
                                rubroSelect.setValorTotal(rubroSelect.getValor());
                                JsfUtil.update("formEspecies");
                                JsfUtil.executeJS("PF('dlgEspeciesDisp').show();");
                            } else {
                                JsfUtil.addErrorMessage("No tiene asignado especies.", "");
                            }
                        } else {
                            switch (rubroSelect.getId().intValue()) {
                                case 0:
                                    loadMedidorCuotasDialog();

                                default:
                                    JsfUtil.update("formValores");
                                    JsfUtil.executeJS("PF('dlgValorRubro').show();");
                                    break;
                            }
                        }
                    }
                } else {
                    JsfUtil.addInformationMessage("Intente Nuevamente.", "");
                }
            } else {
                JsfUtil.addInformationMessage("Intente Nuevamente.", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aplicarExoneracion() {
        List<FnExoneracionLiquidacion> listaExoneracion = new ArrayList<>();
        FnExoneracionLiquidacion exoneracion = new FnExoneracionLiquidacion();
        exoneracion.setLiquidacionOriginal(cobrosGenerales);
        exoneracion.setEstado(Boolean.TRUE);
        exoneracion.setLiquidacionPosterior(cobrosGenerales);
        listaExoneracion.add(exoneracion);
        solicitud = new FnSolicitudExoneracion();
        solicitud.setExoneracionTipo(service.find(FnExoneracionTipo.class, 41L));
        //   solicitud.setEstado(Boolean.TRUE);
        solicitud.setAnioInicio(Utils.getAnio(new Date()));
        solicitud.setSolicitante(cobrosGenerales.getComprador());
        BigDecimal valor = (rubrosSeleccionado.get(0).getValorTotal().setScale(2, RoundingMode.HALF_UP).multiply(solicitud.getExoneracionTipo().getPorcentaje().setScale(2, RoundingMode.HALF_UP))).divide(PORCENTAJE, 2, RoundingMode.HALF_UP);
        solicitud.setValor(valor);
        solicitud.setFnExoneracionLiquidacionList(listaExoneracion);
        cobrosGenerales.setExoneracionLiquidacionCollection(listaExoneracion);
        cobrosGenerales.setExoneracionDescripcion("EXONERADO POR " + solicitud.getExoneracionTipo().getDescripcion().toUpperCase());
        cobrosGenerales.setValorExoneracion(valor);

        FinaRenRubrosLiquidacion ru = rubrosSeleccionado.get(0);
        ru.setValorTotal(ru.getValorTotal().subtract(valor));
        rubrosSeleccionado.remove(0);

        rubrosSeleccionado.add(0, ru);

    }

    public void loadMedidorCuotasDialog() {
//        if (this.cobrosGenerales.getCuenta() != null) {
//            if (!aps.cuentaTieneConvenioMedidor(this.cobrosGenerales.getCuenta())) {
//                loadMedidorCuentaValues();
//                JsfUtil.update("formValoresMedidor");
//                JsfUtil.executeJS("PF('dlgValorRubroMedidor').show();");
//            } else {
//                JsfUtil.addErrorMessage( "Cuenta tiene deudas de Medidor Pendiente: " + this.cobrosGenerales.getCuenta().getCuenta(), "");
//            }
//        } else {
//            JsfUtil.addErrorMessage( "Seleccione una Cuenta ", "");
//        }
    }

    public void loadPrediosByPropietario() {
        if (cobrosGenerales.getComprador() == null) {
            JsfUtil.addErrorMessage("Seleccione un Contribuyente para continuar", "");
        } else {
//            catPredioLazy = new CatPredioLazy();
            propiedades = service.getListPrediosByPropietario(cobrosGenerales.getComprador().getId());
            JsfUtil.update("formPredSel");
            JsfUtil.executeJS("PF('selPredio').show();");
        }
    }

    public void loadBovedasByPropietario() {
        if (cobrosGenerales.getComprador() == null) {
            JsfUtil.addErrorMessage("Seleccione un Contribuyente para continuar", "");
        } else {
            bovedasLazy = new LazyModel<>(EspCementerioBoveda.class);
            String sql = "SELECT r FROM EspCementerioBoveda r  INNER JOIN r.espCementerioBovedaEnteList e WHERE e.ente.id = :ente AND e.tipo.id =  :tipo";
            paramt = new HashMap<>();
            paramt.put("ente", cobrosGenerales.getComprador().getId());
            paramt.put("tipo", 763L);
            bovedasPropietario = (List<EspFotoBovedas>) service.findAllQuery(sql, paramt);
            JsfUtil.update("formBoveda");
            JsfUtil.executeJS("PF('selBoveda').show();");
        }
    }

    public void nuevoCliente() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "cliente-adm");
    }

    public void guardaRubro() {
        parametros = new ArrayList<>();

        String dialogo = "dlgValorRubro";
        if (rubroSelect.getValor() == null) {
            return;
        }

        if (rubroSelect.getRecEspecies() != null) {
            if (rubroSelect.getCantidad() > acta.getDisponiblesTemp()) {
                JsfUtil.addErrorMessage("No debe vender mas especies de las disponibles.", "");
                return;
            }

            rubroSelect.setActa(acta);
            dialogo = "dlgEspeciesDisp";
        }

        ///"id_rubro", 
        parametros.add(rubroSelect.getId().intValue());
        ///"valor_calcular", 
        if (rubroSelect.getValorCalculo().compareTo(BigDecimal.ZERO) == 0) {
            rubroSelect.setValorCalculo(rubroSelect.getValor());
            parametros.add(rubroSelect.getValorCalculo());
        } else {
            parametros.add(rubroSelect.getValorCalculo());
        }
        if (rubroSelect.getAnio() != null) {
            this.cobrosGenerales.setAnio(rubroSelect.getAnio());
        } else {
            this.cobrosGenerales.setAnio(Utils.getAnio(new Date()));
        }

        ///valor_porcentual
        parametros.add(rubroSelect.getValor());
        //"cantidad", 
        parametros.add(rubroSelect.getCantidad());
        //año
        parametros.add(rubroSelect.getAnio());
        //ente
        if (cobrosGenerales.getComprador() == null) {
            parametros.add(1L);
        } else {
            parametros.add(cobrosGenerales.getComprador().getId());
        }

        //calculo10Porciento
        parametros.add(calculo10Porciento);
        planificacionTitulos.setValorCalcular(rubroSelect.getValorCalculo());
        planificacionTitulos.setCantidad(rubroSelect.getCantidad());
        planificacionTitulosList.add(planificacionTitulos);

        BigDecimal valor = (BigDecimal) service.valorExecutefinl("sgm.planificacion_calculos", parametros, Boolean.FALSE);
        rubroSelect.setValorTotal(valor);
        rubroSelect.setValor(valor);
        rubrosSeleccionado.add(rubroSelect);
        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
        JsfUtil.executeJS("PF('" + dialogo + "').hide();");
    }

    public void guardaRubroMedidor() {
        parametros = new ArrayList<>();
        if (rubroSelect.getValor() == null) {
            return;
        }
        ///"id_rubro", 
        parametros.add(rubroSelect.getId().intValue());
        ///"valor_calcular", 
        if (rubroSelect.getValorCalculo().compareTo(BigDecimal.ZERO) == 0) {
            rubroSelect.setValorCalculo(rubroSelect.getValor());
            parametros.add(rubroSelect.getValorCalculo());
        } else {
            parametros.add(rubroSelect.getValorCalculo());
        }
        if (rubroSelect.getAnio() != null) {
            this.cobrosGenerales.setAnio(rubroSelect.getAnio());
        } else {
            this.cobrosGenerales.setAnio(Utils.getAnio(new Date()));
        }

        ///valor_porcentual
        parametros.add(rubroSelect.getValor());
        //"cantidad",  -- cuotas
        parametros.add(rubroSelect.getCantidad());
        //año
        parametros.add(rubroSelect.getAnio());
        //ente
        if (cobrosGenerales.getComprador() == null) {
            parametros.add(1L);
        } else {
            parametros.add(cobrosGenerales.getComprador().getId());
        }

        //calculo10Porciento :: calculoConvenio
        switch (rubroSelect.getId().intValue()) {
            case 56: ///MEDIDOR
                calculoConvenio = Boolean.FALSE;
                break;
            case 775: // MEDIDOR CUOTAS
                calculoConvenio = Boolean.TRUE;
                break;
            case 833: // RDM CUOTAS
                calculoConvenio = Boolean.TRUE;
                break;
            case 825: // RDM 
                calculoConvenio = Boolean.FALSE;
                break;
            case 834: // MULTAS
                calculoConvenio = Boolean.FALSE;
                break;
            case 835: // MULTAS CUOTAS
                calculoConvenio = Boolean.TRUE;
                break;
            default:
                calculoConvenio = Boolean.FALSE;
                break;
        }
        parametros.add(calculoConvenio);
        planificacionTitulos.setValorCalcular(rubroSelect.getValorCalculo());
        planificacionTitulos.setCantidad(rubroSelect.getCantidad());
        planificacionTitulosList.add(planificacionTitulos);

        BigDecimal valor = (BigDecimal) service.valorExecutefinl("sgm.planificacion_calculos", parametros, Boolean.FALSE);
        rubroSelect.setValorTotal(valor);
        rubroSelect.setValor(valor);
        rubrosSeleccionado.add(rubroSelect);
        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
        JsfUtil.executeJS("PF('dlgValorRubroMedidor').hide();");
        JsfUtil.executeJS("PF('dlgValorCuotas').hide();");
        calculoConvenio = Boolean.FALSE;
    }

    public void deleteRubrosDetalle(int indice) {
        rubrosSeleccionado.remove(indice);
        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
    }

    public void onNodeSelect() throws ParseException {
        if (localComercial.getId() == null) {
            JsfUtil.addWarningMessage("", "Seleccione un local");
            return;
        }

        FinaRenTipoLiquidacion fina = service.find(FinaRenTipoLiquidacion.class, ((FinaRenTipoLiquidacion) selectedNode.getData()).getId());

        if (fina == null) {
            JsfUtil.addErrorMessage("", "Ocurrió un error con el tipo de liquidacion seleccionado");
            return;
        }
        JsfUtil.addInformationMessage("", "El tipo de liquidación que selecciono es " + fina.getNombreTransaccion().toUpperCase());
        if (selectedNode != null) {
            cobrosGenerales.setTipoLiquidacion(fina);
            planificacionTitulos = new RenDepartamentoDetalleTitulos();
            planificacionTitulos.setTipoLiquidacion(cobrosGenerales.getTipoLiquidacion());
            cobrosGenerales.setObservacion("LIQUIDACIÓN DE " + fina.getNombreTransaccion());
            casoLiquidaciones(fina.getPrefijo());
        }
        if (localComercial.getPropietario() != null) {
            ciRucCobros = localComercial.getPropietario().getIdentificacion();
            cobrosGenerales.setComprador(localComercial.getPropietario());
        }
    }

    public void casoLiquidaciones(String prefijo) throws ParseException {
        turismotipo = false;
        tasaHabitacion = false;
        listaTasasTurismo = new ArrayList<>();
        actividesLocales = new ArrayList<>();
        permisoAdicional = false;
        paramt = new HashMap<>();
        paramt.put("estado", true);
        switch (prefijo) {

            case "TLT":
                listaTasasTurismo = service.findObjectByParameterList(RenTasaTurismo.class, paramt);
                turismotipo = true;
                listaMultasIneteres = new ArrayList<>();
                paramt = new HashMap<>();
                paramt.put("tipo", "M");
                paramt.put("tipoLiquidacion", cobrosGenerales.getTipoLiquidacion());
                paramt.put("estado", true);
                //r.id not in (34,35,36)
                listaMultasIneteres = (List<String>) service.findAllQuery("select distinct(r.descripcion) from RenParametrosInteresMulta r where  r.tipo=:tipo and r.tipoLiquidacion=:tipoLiquidacion and r.estado=:estado", paramt);
                listaVeces = (List<Integer>) service.findAllQuery("select distinct(r.veces) from RenParametrosInteresMulta r where r.tipo=:tipo and r.tipoLiquidacion=:tipoLiquidacion and r.estado=:estado order by r.veces asc", paramt);

                if (this.localComercial.getCategoria() != null) {
                    renTasaTurismo = new RenTasaTurismo();
                    renTasaTurismo = (RenTasaTurismo) service.find("SELECT rc FROM RenTasaTurismo rc WHERE rc.categoria.id = :categoria",
                            new String[]{"categoria"}, new Object[]{localComercial.getCategoria().getId()});
                    System.out.println("renTasaTurismo " + renTasaTurismo);
                    if (renTasaTurismo != null) {
                        if (renTasaTurismo.getValor() == null) {
                            renTasaTurismo.setValor(BigDecimal.ZERO);
                        }
                        if (renTasaTurismo.getMaximo() == null) {
                            renTasaTurismo.setMaximo(BigDecimal.ZERO);
                        }
                        if (renTasaTurismo.getRubro() != null) {
                            if (rubrosSeleccionado.contains(renTasaTurismo.getRubro())) {
                                JsfUtil.addErrorMessage("Ya esta seleccionado el mismo rubro.", "");
                                return;
                            } else {
                                ///SET TIPO LIQUIDACION 
                                cobrosGenerales.setTipoLiquidacion(service.find(FinaRenTipoLiquidacion.class,
                                        renTasaTurismo.getRubro().getTipoLiquidacion().getId()));

                                this.cobrosGenerales.setComprador(localComercial.getPropietario());
                                this.cobrosGenerales.setLocalComercial(localComercial);
                                if (this.cobrosGenerales.getComprador() != null && this.cobrosGenerales.getComprador().getIdentificacion() != null) {
                                    this.ciRucCobros = this.cobrosGenerales.getComprador().getIdentificacion();
                                }
                                if (localComercial.getActividadComercial() != null) {
                                    this.catPlanificacionTitulos.setUbicacion(localComercial.getActividadComercial());
                                }
                                planificacionTitulos = new RenDepartamentoDetalleTitulos();
                                planificacionTitulos.setTipoLiquidacion(cobrosGenerales.getTipoLiquidacion());
                                BigDecimal calculo = BigDecimal.ZERO;
                                System.out.println("rubro " + cobrosGenerales.getTipoLiquidacion().getRenRubrosLiquidacionCollection().get(0));
                                rubroSelect = new FinaRenRubrosLiquidacion();
                                rubroSelect = cobrosGenerales.getTipoLiquidacion().getRenRubrosLiquidacionCollection().get(0);
                                //RenLocalCantidadAccesorios accesorios = localComercial.getCantidadAccesoriosCollection().get(0);
                                //CALCULO TASA DE LICENCIA UNICA DE FUNCIONAMIENTO

                                if (renTasaTurismo.getTipoItem() != null && "5".equals(renTasaTurismo.getTipoItem().getCodigo())) {
                                    rubroSelect.setCantidad(0);
                                    rubroSelect.setAnio(Utils.getAnio(new Date()));
                                    rubroSelect.setValor(renTasaTurismo.getValor());
                                    calculo = renTasaTurismo.getValor().setScale(2, RoundingMode.HALF_UP);
                                    rubroSelect.setValorCalculo(calculo.setScale(2, RoundingMode.HALF_UP));
                                    rubroSelect.setValorTotal(rubroSelect.getValorCalculo());
                                    rubrosSeleccionado.add(rubroSelect);
                                }

                                rubroSelect = new FinaRenRubrosLiquidacion();
                                rubroSelect = cobrosGenerales.getTipoLiquidacion().getRenRubrosLiquidacionCollection().get(1);
                                rubroSelect.setValorTotal(rubroSelect.getValor());
                                rubroSelect.setCantidad(1);
                                rubrosSeleccionado.add(rubroSelect);

                                SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
                                Date fechaAbril = obj.parse(Utils.getAnio(new Date()).toString() + "-03-01");
                                Integer dias = Utils.getDiasResta(fechaAbril, new Date());
                                System.out.println("dias ->" + dias);

                                //CALCULO DE MULTA
//                               rubroSelect.
                                //rubrosSeleccionado.add(rubroSelect);
//                                JsfUtil.update("formValores");
//                                JsfUtil.executeJS("PF('dlgValorRubro').show();");
                            }
                        } else {
                            JsfUtil.addErrorMessage("No existen Rubros registrados para la categoria seleccionada", "");
                        }
                    } else {
                        JsfUtil.addErrorMessage("No existen Tasas Turisticas Registradas para la categoria seleccionada", "");
                    }
                } else {
                    JsfUtil.addErrorMessage("El Local Seleccionado no posee Cateogria", "");
                }

                break;

            case "PFU":
                rubrosSeleccionado.clear();
                setTipoLocal(tipoLocal);
                setFactorUbicacion(factorUbicacion);
                setSeleccionActividad(seleccionActividad);
                System.out.println("Tipo local " + tipoLocal);
                System.out.println("activ " + seleccionActividad);
                System.out.println("factorUbicacion " + factorUbicacion);

                actividesLocales = service.findObjectByParameterList(FinaRenActividadComercial.class, paramt);

                paramt = new HashMap<>();
                paramt.put("estado", true);
                listaTipoLocales = new ArrayList<>();
                listaTipoLocales = service.findObjectByParameterList(FinaRenTipoLocalComercial.class, paramt);
                listaFactoresUbicacion = new ArrayList<>();
                listaFactoresUbicacion = catalogoService.MostarTodoCatalogo("GT_FACTOR_UBICACION_LOCALES");
                tasaHabitacion = true;
                BigDecimal actividad = BigDecimal.ZERO;
                BigDecimal tamanio = BigDecimal.ZERO;
                BigDecimal ubicacion = BigDecimal.ZERO;
                if (localComercial != null) {
                    FinaRenActividadComercial actividadLocal = service.actividadPOrLocal(localComercial.getId(), liquidacion.getAnio());

                    rubroSelect = new FinaRenRubrosLiquidacion();
                    rubroSelect = cobrosGenerales.getTipoLiquidacion().getRenRubrosLiquidacionCollection().get(0);
                    rubroSelect.setCantidad(1);
                    rubroSelect.setAnio(Utils.getAnio(new Date()));

                    if (actividadLocal != null) {
                        actividad = actividadLocal.getValor();
                        seleccionActividad = actividadLocal;
                        System.out.println("entrando seleccionActividad  " + actividad);
                    } else {
                        actividad = seleccionActividad.getValor();
                        System.out.println("entrando " + actividad);

                    }
                    if (localComercial.getTipoLocal() != null) {
                        System.out.println("localComercial.getTipoLocal() " + localComercial.getTipoLocal().toString());
                        System.out.println("localComercial.getTipoLocal().getValor() " + localComercial.getTipoLocal().getValor());
                        tamanio = localComercial.getTipoLocal().getValor();
                        tipoLocal = localComercial.getTipoLocal();
                        System.out.println("entrando getTipoLocal " + tamanio);
                    } else {
                        System.out.println("tipoLocal.getValor() " + tipoLocal.getValor());
                        tamanio = tipoLocal.getValor();
                        System.out.println(" tamanio " + tamanio);

                    }
                    if (localComercial.getFactorUbicacion() != null) {
                        ubicacion = localComercial.getFactorUbicacion().getValor();
                        factorUbicacion = localComercial.getFactorUbicacion();
                        System.out.println("entrando factorUbicacion " + ubicacion);
                    } else {

                        ubicacion = factorUbicacion.getValor();
                        System.out.println(" ubicacion " + ubicacion);
                    }

                    if (tamanio == null) {
                        System.out.println("null");
                        tamanio = BigDecimal.ZERO;

                    }
                    if (ubicacion == null) {
                        System.out.println("null");
                        ubicacion = BigDecimal.ZERO;
                    }
                    if (actividad == null) {
                        System.out.println("null");
                        actividad = BigDecimal.ZERO;
                    }

                    System.out.println("tasa htl " + ubicacion + "\t \t" + tamanio + "\t \t" + actividad);

                    BigDecimal temp = ubicacion.multiply(tamanio).multiply(actividad);
                    rubroSelect.setValor(temp.setScale(2, RoundingMode.UP));
                    rubroSelect.setCantidad(1);
                    rubroSelect.setValorTotal(rubroSelect.getValor());
                    rubrosSeleccionado.add(rubroSelect);
//                    rubroSelect = new FinaRenRubrosLiquidacion();
//                    rubroSelect = cobrosGenerales.getTipoLiquidacion().getRenRubrosLiquidacionCollection().get(1);
//                    rubroSelect.setCantidad(1);
//                    rubroSelect.setAnio(Utils.getAnio(new Date()));
//                    rubroSelect.setValorTotal(rubroSelect.getValor());
//                    rubrosSeleccionado.add(rubroSelect);
                }

                if (tamanio.doubleValue() == 0 && ubicacion.doubleValue() == 0 && actividad.doubleValue() == 0) {
                    JsfUtil.addWarningMessage("Requisitios Necesario para Hallar La tasa de habilitacion y control",
                            "El Local Seleccionado no posee tamaño o actividad o ubicación");
                    return;
                }

                break;
        }

        JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
        calcularTotalCobroGeneral();
    }

    public void seleccionarMulta(String tipo) {
        descripcionMulta = "";
        descripcionMulta = tipo;
        System.out.println("multa " + tipo);
    }

    public void onNodeSelectAmbiente() {
        if (cobrosGenerales.getComprador() != null) {
            if (selectedNode != null) {
                cobrosGenerales.setTipoLiquidacion(service.find(FinaRenTipoLiquidacion.class,
                        ((FinaRenTipoLiquidacion) selectedNode.getData()).getId()));
                planificacionTitulos = new RenDepartamentoDetalleTitulos();
                planificacionTitulos.setTipoLiquidacion(cobrosGenerales.getTipoLiquidacion());
            }
        } else {
            JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar un Solicitante.");
        }

    }

    public void seleccionarLiquidacion() {
        if (cobrosGenerales.getComprador() == null) {
            JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar un Solicitante.");
        } else {
            savePlanificacionTitulo();
            pagoRealizado = Boolean.FALSE;
            cobrosGenerales.setSaldo(cobrosGenerales.getTotalPago());
            this.liquidacion = cobrosGenerales;
            if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
                if ((this.liquidacion.getComprador() != null || this.liquidacion.getNombreComprador() != null) && this.liquidacion.getSaldo() != null && this.liquidacion.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                    this.liquidacion.calcularPago();
                    //SE AGREGA PARA SALDO DE PAGO
                    this.generarLiquidacion();
                    JsfUtil.update("formLiq");
                    JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");
                    initView();
                } else {
                    if (this.liquidacion.getComprador() == null) {
                        JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar un Solicitante.");
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "Valor del Cobro debe ser mayor a 0.00.");
                    }
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "Seleccione un elemento de la lista.");
            }
        }

    }

    public void seleccionarLiquidacionCementerio() {
        if (cobrosGenerales.getComprador() == null) {
            JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar un Solicitante.");
        } else {
            if (boveda == null) {
                JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar una Boveda Para Continuar.");
            } else {
                savePlanificacionTitulo();
                pagoRealizado = Boolean.FALSE;
                cobrosGenerales.setSaldo(cobrosGenerales.getTotalPago());
                this.liquidacion = cobrosGenerales;
                if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
                    if ((this.liquidacion.getComprador() != null || this.liquidacion.getNombreComprador() != null) && this.liquidacion.getSaldo() != null && this.liquidacion.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                        this.liquidacion.calcularPago();
                        //SE AGREGA PARA SALDO DE PAGO
                        this.generarLiquidacion();
                        JsfUtil.update("formLiq");
                        JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");
                        initView();
                    } else {
                        if (this.liquidacion.getComprador() == null) {
                            JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar un Solicitante.");
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "Valor del Cobro debe ser mayor a 0.00.");
                        }
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "Seleccione un elemento de la lista.");
                }
            }
        }

    }

    public void seleccionarLiquidacionLocalComercial() {
        if (cobrosGenerales.getLocalComercial() == null) {
            JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar un Local Comercial.");
        } else {
            savePlanificacionTitulo();
            pagoRealizado = Boolean.FALSE;
            cobrosGenerales.setSaldo(cobrosGenerales.getTotalPago());
            this.liquidacion = cobrosGenerales;
            if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
                if ((this.liquidacion.getComprador() != null || this.liquidacion.getNombreComprador() != null) && this.liquidacion.getSaldo() != null && this.liquidacion.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
                    this.liquidacion.calcularPago();
                    //SE AGREGA PARA SALDO DE PAGO
                    this.generarLiquidacion();
                    JsfUtil.update("formLiq");
                    JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");
                    initView();
                } else {
                    if (this.liquidacion.getComprador() == null) {
                        JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar un Solicitante.");
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "Valor del Cobro debe ser mayor a 0.00.");
                    }
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "Seleccione un elemento de la lista.");
            }
        }

    }

    public void seleccionarLiquidacionAguaPotable() {
//        if (cobrosGenerales.getComprador() == null) {
//            JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar un Solicitante.");
//        } else {
//            if (isCuotasConvenios()) {
//                if (cobrosGenerales.getCuenta() == null) {
//                    JsfUtil.addErrorMessage("Mensaje", "Debe seleccionar una Cuenta.");
//                    return;
//                }
//            }
//            pagoRealizado = Boolean.FALSE;
//            cobrosGenerales.setSaldo(cobrosGenerales.getTotalPago());
//            this.liquidacion = cobrosGenerales;
//            if (!isCuotasConvenios()) {
//                savePlanificacionTitulo();
//                if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
//                    if ((this.liquidacion.getComprador() != null || this.liquidacion.getNombreComprador() != null) && this.liquidacion.getSaldo() != null && this.liquidacion.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
//                        this.liquidacion.calcularPago();
//                        //SE AGREGA PARA SALDO DE PAGO
//                        this.generarLiquidacion();
//                        if (this.liquidacion.getCuenta() != null) {
//                            if (pr != null) {
//                                if (pr.getId() != null) {
//                                    Cuenta c = this.liquidacion.getCuenta();
//                                    c.setPredio(pr);
//                                    service.persist(c);
//                                }
//                            }
//                            updateMedidorCuenta(this.liquidacion.getCuenta());
//                        }
//                        JsfUtil.update("formLiq");
//                        JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");
//                        initView();
//                    } else {
//                        if (this.liquidacion.getComprador() == null) {
//                            JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar un Solicitante.");
//                        } else {
//                            JsfUtil.addInformationMessage("Mensaje", "Valor del Cobro debe ser mayor a 0.00.");
//                        }
//                    }
//                } else {
//                    JsfUtil.addInformationMessage("Mensaje", "Seleccione un elemento de la lista.");
//                }
//            } else {
//                if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
//                    saveConvenioCuotasCuenta();
//                } else {
//                    JsfUtil.addErrorMessage("Mensaje", "Verifique los Datos");
//                }
//            }
//
//        }
    }

    public Boolean isCuotasConvenios() {
        Boolean esCuotaConvenio = Boolean.FALSE;
        switch (this.rubrosSeleccionado.get(0).getId().intValue()) {
            case 56: ///MEDIDOR
                esCuotaConvenio = Boolean.FALSE;
                break;
            case 775: // MEDIDOR CUOTAS
                esCuotaConvenio = Boolean.TRUE;
                break;
            case 833: // RDM CUOTAS
                esCuotaConvenio = Boolean.TRUE;
                break;
            case 825: // RDM 
                esCuotaConvenio = Boolean.FALSE;
                break;
            case 834: // MULTAS
                esCuotaConvenio = Boolean.TRUE;
                break;
            case 835: // MULTAS CUOTAS
                esCuotaConvenio = Boolean.TRUE;
                break;
            default:
                esCuotaConvenio = Boolean.FALSE;
                break;
        }
        return esCuotaConvenio;
    }

    public void saveConvenioCuotasCuenta() {
//        ConvenioMedidor medidor;
//        Calendar calendar = Calendar.getInstance();
//        convenioCuenta = new ConvenioCuenta();
//        convenioCuenta.setObservacion(observacionConvenioCuenta);
//        convenioCuenta.setCuenta(liquidacion.getCuenta());
//        convenioCuenta.setEstado(Boolean.TRUE);
//        convenioCuenta.setFechaCreacion(new Date());
//        convenioCuenta.setMesesDiferir(rubroSelect.getCantidad());
//        convenioCuenta.setTipoConvenio(rubroSelect);
//        convenioCuenta.setTotalPago(rubroSelect.getValorCalculo());
//        convenioCuenta.setUsuarioCreacion(session.getName_user());
//        convenioCuenta = (ConvenioCuenta) service.persist(convenioCuenta);
//        for (RenRubrosLiquidacion renRubrosLiquidacion : this.rubrosSeleccionado) {
//            for (int i = 1; i <= renRubrosLiquidacion.getCantidad(); i++) {
//                medidor = new ConvenioMedidor();
//                calendar = Calendar.getInstance();
//                if (i > 1) {
//                    calendar.add(Calendar.MONTH, i - 1);
//                }
//                medidor.setConvenio(convenioCuenta);
//                medidor.setAnio(Utils.getAnio(calendar.getTime()));
//                medidor.setCuenta(liquidacion.getCuenta());
//                medidor.setCuota(renRubrosLiquidacion.getValorTotal());
//                medidor.setDescripcion(renRubrosLiquidacion.getDescripcion());
//                medidor.setEstado(true);
//                medidor.setFechaIngreso(new Date());
//                medidor.setMes(Utils.getMes(calendar.getTime()) + 1);
//                medidor.setUsuarioIngreso(session.getName_user());
//                medidor.setEstadoConvenio(new RenEstadoLiquidacion(2L));
//                service.persist(medidor);
//            }
//            updateMedidorCuenta(this.liquidacion.getCuenta());
//            JsfUtil.update("formLiqConvenioMedidor");
//            JsfUtil.executeJS("PF('dlgLiquidacionConvenio').show();");
//            initView();
//        }
    }

    public void updateMedidorCuenta(CuentaContable cuenta) {
//        List<MedidorCuenta> medidorCuentas = (List<MedidorCuenta>) service.findAll(QuerysAguaPotable.getMedidorByCuenta, new String[]{"cuenta"}, new Object[]{cuenta.getId()});
//        if (medidorCuentas != null) {
//            for (MedidorCuenta m : medidorCuentas) {
//                m.setEstado(Boolean.FALSE);
//                service.persist(m);
//            }
//            if (this.medidorCuenta != null) {
//                this.medidorCuenta.setCuenta(cuenta);
//                service.persist(this.medidorCuenta);
//            }
//        }
    }

    public void saveEditPermiso() {
        if (this.departamentoDetalleTitulosGetionAmbiental.getId() != null) {
            if (departamentoDetalleTitulosGetionAmbiental.getId() != null) {
                service.update(departamentoDetalleTitulosGetionAmbiental);
            } else {
                service.save(this.departamentoDetalleTitulosGetionAmbiental);
            }
            JsfUtil.addInformationMessage("Mensaje", "Guardado Correctamente.");
            JsfUtil.executeJS("PF('dlgEditPermiso').hide();");
            JsfUtil.update("mainForm");
        } else {
            JsfUtil.addErrorMessage("Mensaje", "Error");
        }
    }

    public void savePlanificacionTitulo() {

        this.catPlanificacionTitulos.setTotalPago(this.cobrosGenerales.getTotalPago().add(interes(this.cobrosGenerales)));

        this.catPlanificacionTitulos.setEnte(this.cobrosGenerales.getComprador());
        this.catPlanificacionTitulos = planificacionService.savePlanificacionTitulo(catPlanificacionTitulos);
        for (RenDepartamentoDetalleTitulos rpt : planificacionTitulosList) {
            rpt.setPlanificacion(catPlanificacionTitulos);
            service.updateEntity(rpt);
        }
        //   this.cobrosGenerales.setUbicacion(catPlanificacionTitulos.getUbicacion());
        this.cobrosGenerales.setDepartamentoDetalleTitulos(catPlanificacionTitulos);
    }

    public void llenarArbolAdmin() {
        List<FinaRenTipoLiquidacion> raices = null;

        paramt = new HashMap<>();
        paramt.put("items", Arrays.asList("TLT"));
        raices = service.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.prefijo in (:items) AND r.nombreTransaccion IS NOT NULL and r.estado = true"
                + " ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC", paramt);

        root2 = new DefaultTreeNode("tipos_liquidaciones", null);
        if (raices != null) {
            for (FinaRenTipoLiquidacion temp : raices) {
                if (!temp.getTomado()) {
                    temp.setTomado(true);
                    TreeNode node = new DefaultTreeNode(temp, root2);
                    node.setExpanded(false);
                    llenarHijosArbolAdmin(temp, node);
                }
            }
        }
    }

    public void llenarHijosArbolAdmin(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            List<FinaRenTipoLiquidacion> hijos;

            hijos = service.listaRubrosHijosAdmin(hoja.getId());

            if (hijos == null || hijos.isEmpty()) {
                return;
            }

            for (FinaRenTipoLiquidacion temp2 : hijos) {
                if (!temp2.getTomado()) {
                    TreeNode node = new DefaultTreeNode(temp2, padre);
                    temp2.setTomado(true);
                    llenarHijosArbol(temp2, node);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void llenarArbol() {
        try {

            List<FinaRenTipoLiquidacion> raices = new ArrayList<>();
            List<RenTipoLiquidacionDepartamento> departamentos = service.listaRubrosPadres(session.getNameUser(), Arrays.asList("TLT"));
            FinaRenTipoLiquidacion rtl = null;
            System.out.println("user " + session.getNameUser());
            System.out.println("departamentos " + departamentos.size());
            raices = new ArrayList<>();
            if (departamentos != null) {
                for (RenTipoLiquidacionDepartamento rtld : departamentos) {
                    System.out.println("ingreaando " + rtld);
                    raices.add(rtld.getTipoLiquidacion());
                }
            }
            root2 = new DefaultTreeNode("tipos_liquidaciones", null);
            if (raices != null) {
                for (FinaRenTipoLiquidacion temp : raices) {
                    if (!temp.getTomado()) {
                        temp.setTomado(true);
                        TreeNode node = new DefaultTreeNode(temp, root2);
                        node.setExpanded(true);
                        llenarHijosArbol(temp, node);
                    }
                }
            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarHijosArbol(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            List<FinaRenTipoLiquidacion> hijos;

            hijos = service.listaRubrosHijos(session.getNameUser(), hoja.getId());

            if (hijos == null || hijos.isEmpty()) {
                return;
            }

            for (FinaRenTipoLiquidacion temp2 : hijos) {
                if (!temp2.getTomado()) {
                    TreeNode node = new DefaultTreeNode(temp2, padre);
                    temp2.setTomado(true);
                    llenarHijosArbol(temp2, node);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDescripcionRubro(Long idRubro) {
        return (String) service.find("SELECT r.descripcion FROM FinaRenRubrosLiquidacion r WHERE r.id=:idRubro", new String[]{"idRubro"}, new Object[]{idRubro});
    }

    public void calculosAdicionales() {
        if (this.liquidacion != null) {
            if (this.liquidacion.getTipoLiquidacion().getId() == 13L) {
                try {
                    this.liquidacion = service.realizarDescuentoRecargaInteresPredial(this.liquidacion, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            List<RenParametrosInteresMulta> parametrosInteresMultas = service.getListParametrosInteresMulta(liquidacion);
            if (parametrosInteresMultas != null && !parametrosInteresMultas.isEmpty()) {//VERIFICAR SI EMITE MULTA-INTERES
                for (RenParametrosInteresMulta interesMulta : parametrosInteresMultas) {
                    if (interesMulta.getTipo().equalsIgnoreCase("I")) {
                        try {
                            Calendar fecha = Calendar.getInstance();
                            fecha.set(Calendar.DAY_OF_MONTH, interesMulta.getDia());
                            fecha.set(Calendar.MONTH, interesMulta.getMes() - 1);
                            fecha.set(Calendar.YEAR, liquidacion.getAnio() + 1);
                            liquidacion.setInteres(service.generarInteres(liquidacion.getSaldo(), fecha.getTime(), null));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            this.liquidacion.calcularPago();
        }
    }

    public void generarLiquidacion() {
        if (session != null && session.getNameUser() != null) {
            if (liquidacion != null && liquidacion.getId() != null) {
                esLiq = false;
                return;
            }
            liquidacion.setUsuarioIngreso(session.getNameUser());
            //liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);

            ///SI ES DEL REGISTRO PODRÁ COBRAR  =o
            if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas()) {
                liquidacion.setValidada(Boolean.FALSE);
            } else {
                liquidacion.setValidada(Boolean.TRUE);
            }

            List<FinaRenDetLiquidacion> detalle = new ArrayList<>();

            for (FinaRenRubrosLiquidacion item : rubrosSeleccionado) {
                FinaRenDetLiquidacion dat = new FinaRenDetLiquidacion();
                dat.setRubro(item);
                dat.setValorSinDescuento(BigDecimal.ZERO);
                dat.setEstado(true);
                dat.setValor(item.getValorTotal());
                dat.setEstado(true);
                dat.setCantidad(item.getCantidad());
                detalle.add(dat);
            }
            liquidacion.setLocalComercial(localComercial);
            liquidacion.setFechaIngreso(new Date());
            liquidacion = liquidacionService.crearLiquidacionTursmo(liquidacion, detalle);
            JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " Generada con exito");
            liquidacion.calcularPago();

        }
    }

    /*METODO MODIFICADO PARA LOS COMPROBANTE GENERALES*/
    public void generarComprobante(FinaRenPago p) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            //    String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarParametros();
            ss.instanciarParametros();
            //ss.addParametro("SUBREPORT_DIR", SisVars.getRealPath("/reportes/Emision/"));
            //     ss.addParametro("LOGO", path + SisVars.logoReportes);
            ss.addParametro("ID_LIQUIDACION", p.getLiquidacion().getId());
            ss.addParametro("NUM_COMPROBANTE", p.getNumComprobante());
            //  ss.setTieneDatasource(Boolean.TRUE);
            if (liquidacion.getTipoLiquidacion().getNombreReporte() == null) {
                ss.setNombreReporte("sCobrosGenerales");
            } else {
                ss.setNombreReporte(liquidacion.getTipoLiquidacion().getNombreReporte());
                if (p.getLiquidacion().getTipoLiquidacion().getId() == 98L) {
                    if (p.getLiquidacion().getRenValoresPlusvalia() == null) {
                        ss.setNombreReporte("sCobrosGenerales");
                    }
                }
            }
            pagos.add(p);
            ss.addParametro("liquidaciones", pagos);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documents");
        } catch (Exception e) {
            LOG.log(Level.OFF, null, e);
        }
    }

    public void seleccionar() {
        if (this.cobrosGenerales.getComprador() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un solicitante del listado");
        } else {
            this.ciRucCobros = this.cobrosGenerales.getComprador().getIdentificacion();
            if (tipoSolicitante == 1) {
                JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
            } else if (tipoSolicitante == 2) {
                ente = this.cobrosGenerales.getComprador();
                JsfUtil.update("mainForm:tvRecaudaciones:groupCoactiva");
            }
            JsfUtil.addInformationMessage("Mensaje", "Contribuyente seleccionado.");
            JsfUtil.executeJS("PF('dlgSolicitante').hide();");
        }
    }

    public void calcularTotalCobroGeneral() {
        cobrosGenerales.setTotalPago(new BigDecimal("0.00"));
        if (this.rubrosSeleccionado != null && !this.rubrosSeleccionado.isEmpty()) {
            for (FinaRenRubrosLiquidacion r : rubrosSeleccionado) {
                if (r.getValorTotal() != null) {
                    cobrosGenerales.setTotalPago(cobrosGenerales.getTotalPago().add(r.getValorTotal()));
                }
            }
        }
//        else {
//            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar el rubro");
//        }
    }

    public void actualizarLiquidacion() {
        service.update(liquidacion);
    }

    public void limpiarDatosContribuyente() {
        if (this.cobrosGenerales != null && this.cobrosGenerales.getComprador() != null) {
            this.cobrosGenerales.setComprador(null);
            this.ciRucCobros = null;
        }
    }

    public SelectItem[] getLisUrbanizaciones() {
        int cantRegis = cdlas.size();
        SelectItem[] options = new SelectItem[cantRegis + 1];
        options[0] = new SelectItem("", "Seleccione");
        for (int i = 0; i < cantRegis; i++) {
            options[i + 1] = new SelectItem(cdlas.get(i), cdlas.get(i));
        }
        return options;
    }

    public void editarPermisoAmbiental() {
        this.departamentoDetalleTitulosGetionAmbiental = liquidacion.getDepartamentoDetalleTitulos();
    }

    public void anularTituloReporte() {
        try {
            if (this.liquidacion != null) {
                this.liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
                service.update(this.liquidacion);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void anularLiquidacion(FinaRenLiquidacion liq) {
        if (liq.getValidada() && liq.getTipoLiquidacion().getNecesitaValidacionRentas()) {
            JsfUtil.addErrorMessage("Error", "La liquidacion ya fue validada solo el departamento de Rentas puede Eliminar");
            return;
        }
        if (liq.getEstadoLiquidacion().getId().equals(1L)) {
            JsfUtil.addErrorMessage("Error", "La liquidacion ya fue Cancelada");
            return;
        }
        liq.setUsuarioAnular(session.getNameUser());
        liq.setIpUserSession(session.getIpClient());
        liq.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
        service.update(liq);
        liq.getDepartamentoDetalleTitulos().setEstado(Boolean.FALSE);
        service.update(liq.getDepartamentoDetalleTitulos());
        emisiones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();

        JsfUtil.addInformationMessage("ANULADO", "Liquidacion : " + liq.getIdLiquidacion() + " anulada con exito");

    }

    public boolean renderAnular(FinaRenLiquidacion liq) {

        switch (liq.getTipoLiquidacion().getId().intValue()) {
            case 17:
            case 20:
            case 98:
            case 99:
                return true;
        }
        return false;
    }

    public void seleccionaPredio(CatPredioModel predio) {
        pr = predio;
        this.calculoValoresCoactiva();
        if (poseeDeudas(pr)) {
            JsfUtil.addErrorMessage("El Predio Seleccionado Posee deudas pendientes de Pago", "");
        } else {
            catPlanificacionTitulos.setPredio(predio.getId());
            catPlanificacionTitulos.setClavePredio(predio.getClaveCat());
            if (pr.getCalle() != null) {
                //    if (this.cobrosGenerales.getCuenta() == null) {
                catPlanificacionTitulos.setUbicacion(pr.getCalle());
                //   }
            }
            JsfUtil.addInformationMessage("Mensaje", "# Predio: " + pr.getNumPredio());
            JsfUtil.executeJS("PF('selPredio').hide();");
            JsfUtil.update("mainForm");
        }
    }

    public void seleccionaPredioAguaPotable(CatPredioModel predio) {
        pr = predio;
        this.calculoValoresCoactiva();
        if (poseeDeudas(pr)) {
            JsfUtil.addErrorMessage("El Predio Seleccionado Posee deudas pendientes de Pago", "");
        }
        catPlanificacionTitulos.setPredio(predio.getId());
        catPlanificacionTitulos.setClavePredio(predio.getClaveCat());
        if (pr.getCalle() != null) {
//            if (this.cobrosGenerales.getCuenta() == null) {
//                catPlanificacionTitulos.setUbicacion(pr.getCalle());
//            }
        }
        JsfUtil.addInformationMessage("Mensaje", "# Predio: " + pr.getNumPredio());
        JsfUtil.executeJS("PF('selPredio').hide();");
        JsfUtil.update("mainForm");
    }

    public void seleccionaBoveda() {

        catPlanificacionTitulos.setCementerioBoveda(boveda);
        if (boveda.getManzana() != null) {
            catPlanificacionTitulos.setUbicacion(boveda.getCementerio().getNombreCementerio() + ": Manzana: " + boveda.getManzana() + " - Lote: " + boveda.getLoteAlfanumerico());
        } else {
            catPlanificacionTitulos.setUbicacion(boveda.getCementerio().getNombreCementerio());
        }

        JsfUtil.addInformationMessage("Mensaje", "# Boveda: " + boveda.getManzana());
        JsfUtil.executeJS("PF('selBoveda').hide();");
        JsfUtil.update("mainForm");

    }

    public void calculoValoresCoactiva() {
        try {
            paramt = new HashMap<>();
            paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
            paramt.put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
            paramt.put("predio", pr);
            paramt.put("estadoCoactiva", 2);
            emisiones = service.findAllQuery("Select c from FinaRenLiquidacion c where c.tipoLiquidacion=:tipoLiquidacion and  c.estadoLiquidacion=: estadoLiquidacion"
                    + " and c.clavePredio=:predio and c.estadoCoactiva=:estadoCoactiva order by c.anio asc ", paramt);
            desdeTemp = (Integer) service.findNativeQuery("select min(anio) from asgard.fina_ren_liquidacion where tipo_liquidacion = 13 "
                    + "and estado_liquidacion = 2 and estado_coactiva = 2 and predio = ?", new Object[]{pr.getId()});
            totalCoactiva = new BigDecimal("0.00");
            if (emisiones != null && !emisiones.isEmpty()) {
                for (FinaRenLiquidacion e : emisiones) {
                    e = service.realizarDescuentoRecargaInteresPredial(e, null);
                    e.calcularPago();
                    totalCoactiva = totalCoactiva.add(e.getValorCoactiva());
                }
            } else {
                emisiones = new ArrayList<>();
                JsfUtil.addInformationMessage("Mensaje", "El predio seleccionado no tiene emisiones prediales en coactiva.");
            }
            //JsfUtil.update("mainForm:tvRecaudaciones:dtEmisionesCoactiva");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean poseeDeudas(CatPredioModel cp) {
        FinaRenLiquidacion liq = service.getDeudasPredioAnioActual(cp);

        if (liq != null) {
            ///SI POSEE DEUDAS
            return Boolean.TRUE;
        } else {
            ///NO POSEE DEUDAS
            return Boolean.FALSE;
        }
    }

    public void seleccionarEmision() {
        totalCoactiva = new BigDecimal("0.00");
        cantidad = 0;
        for (FinaRenLiquidacion e : emisiones) {
            if (e.getAnio() <= liqSelect.getAnio()) {
                cantidad++;
                totalCoactiva = totalCoactiva.add(e.getValorCoactiva());
            }
        }
        JsfUtil.update("mainForm:tvRecaudaciones:dtEmisionesCoactiva");
    }

    public void ingresarCoactiva() {
        pagoRealizado = Boolean.FALSE;
        try {
            if (liqSelect == null) {
                JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar hasta la liquidacion que va a cancelar.");
            } else {
                paramt = new HashMap<>();
                paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(49L));
                paramt.put("codigoRubro", 1L);
                FinaRenRubrosLiquidacion rubro = service.findByParameter(FinaRenRubrosLiquidacion.class,
                        paramt);

                liquidacion = new FinaRenLiquidacion();
                liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
                liquidacion.setComprador(liqSelect.getComprador());
                liquidacion.setTotalPago(totalCoactiva);
                liquidacion.setTipoLiquidacion(rubro.getTipoLiquidacion());
                liquidacion.setPredio(liqSelect.getPredio());
                liquidacion.setSaldo(totalCoactiva);
                liquidacion.setObservacion("Codigo Predial: " + pr.getCodigoPredialCompleto());

                rubrosSeleccionado = new ArrayList<>();
                detalle = new FinaRenDetLiquidacion();
                detalle.getRubro().setId(rubro.getId());
                detalle.setValor(totalCoactiva);
                detalle.setCantidad(cantidad);
                detalle.setDesde(new BigInteger(desdeTemp.toString()));
                detalle.setHasta(new BigInteger(liqSelect.getAnio().toString()));
                JsfUtil.update("formProcesar");
                JsfUtil.executeJS("PF('dlgProcesar').show();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarEnte() {
        registroEnte = new Cliente();
    }

    public void existeCedula() {
        VerCedulaUtils validacion = new VerCedulaUtils();
        String identificacion = registroEnte.getIdentificacion();
        if (registroEnte.getIdentificacion() != null && registroEnte.getIdentificacion().length() > 0) {
            registroEnte = (Cliente) service.find("Select c from Cliente c where c.identificacion=:ciRuc", new String[]{"ciRuc"}, new Object[]{identificacion});

//            if (registroEnte == null) {
//                registroEnte = new Cliente();
//                registroEnte.setIdentificacion(identificacion);
//                if (tipoEnte == 1 || this.ciRucCobros != null) {
//                    if (validacion.isCIValida(identificacion)) {
//                        registroEnte = clienteService.buscarCliente(identificacion);
//                    }
//                }
//            }
        }
//         else {
//            registroEnte = new Cliente();
//        }
    }

    public String descripcionRubro(Long id) {
        FinaRenRubrosLiquidacion renRubrosLiquidacion = service.find(FinaRenRubrosLiquidacion.class,
                id);
        return renRubrosLiquidacion.getDescripcion();
    }

    public void loadRubroTurismo(FinaRenLocalComercial l) {
        try {
            if (l != null) {
                localComercial = l;
                JsfUtil.addInformationMessage("", "Local seleecionado con exito");
            } else {
                localComercial = null;
                JsfUtil.addErrorMessage("Seleccione un Local Comercial.", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void loadRubroCuentasAguas(CuentaContable cuenta) {
//        try {
//            if (cuenta != null) {
//                pr = new CatPredio();
//                this.cobrosGenerales.setComprador(cuenta.getBeneficiario());
//                this.cobrosGenerales.setCuenta(cuenta);
//                this.ciRucCobros = this.cobrosGenerales.getComprador().getCiRuc();
//                if (cuenta.getDireccion() != null) {
//                    this.catPlanificacionTitulos.setUbicacion(cuenta.getDireccion());
//                }
//                if (cuenta.getCuentaAnterior() != null) {
//                    this.cobrosGenerales.setObservacion("Cuenta Actual: " + cuenta.getCuenta() + " Cuenta Anterior: " + cuenta.getCuentaAnterior());
//                }
//                if (cuenta.getPredio() != null) {
//                    this.pr = cuenta.getPredio();
//                    if (poseeDeudas(this.pr)) {
//                        JsfUtil.addErrorMessage("El Predio Seleccionado Posee deudas pendientes de Pago", "");
//                        // return;
//                    }
//                }
//                planificacionTitulos = new RenDepartamentoDetalleTitulos();
//                JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
    }

    public void cantidadAccesoriosLocal() {
        for (RenLocalCantidadAccesorios accesorios : localComercial.getCantidadAccesoriosCollection()) {
            if (accesorios.getAnio().equals(this.rubroSelect.getAnio())) {
                rubroSelect.setCantidad(accesorios.getCantidad());
            }
        }
        if (rubroSelect.getCantidad() == null) {
            rubroSelect.setCantidad(1);
        }
    }

    public void guardaRubroLocal() throws ParseException {

        BigDecimal valor = BigDecimal.ZERO;
        if (turismotipo) {
            if (localComercial.getCategoria() != null) {
                System.out.println("entr 1");
                if (rubroSelect.getId().equals(542L)) {
                    System.out.println("rubroSelect.getId() "+rubroSelect.toString());
                    multaDescuentoturismo();
                } else if (rubroSelect.getId().equals(544L)) {
                    aplicandoMulta();
                } else {
                    rubroSelect.setValorTotal(rubroSelect.getValor().multiply(new BigDecimal(rubroSelect.getCantidad())));
                    rubrosSeleccionado.add(rubroSelect);
                }
            } else {
                JsfUtil.addWarningMessage("", "Seleccion una categoria para realizar los calculos para el tipo de liquidacion seleccionado");
            }

        } else {
            System.out.println("entr 2");
            rubroSelect.setValorTotal(rubroSelect.getValor().multiply(new BigDecimal(rubroSelect.getCantidad())));
            rubrosSeleccionado.add(rubroSelect);
        }

        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
        JsfUtil.executeJS("PF('dlgValorRubro').hide();");
    }

    public void aplicandoMulta() throws ParseException {

        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaAbril = obj.parse(Utils.getAnio(new Date()).toString() + "-04-01");
        Integer dias = Utils.getDiasResta(fechaAbril, new Date());
        BigDecimal base = BigDecimal.ZERO;
        System.out.println("dia multas " + dias);

        if (dias > 0) {
            BigDecimal cal = BigDecimal.ZERO;
            rubroSelect.setCantidad(1);
            for (FinaRenRubrosLiquidacion item : rubrosSeleccionado) {
                if (item.getId().equals(542L)) {
                    base = item.getValorTotal();
                }
            }

            paramt = new HashMap<>();
            paramt.put("descripcion", descripcionMulta);
            paramt.put("veces", veces);
            paramt.put("estado", true);
            renMultasIneteres = service.findByParameter(RenParametrosInteresMulta.class, paramt);

            if (renMultasIneteres.getCodigo().equals("NORMAL")) {
                rubroSelect.setValorTotal(base.multiply(new BigDecimal(dias)).multiply(renMultasIneteres.getValor()));
            } else if (renMultasIneteres.getCodigo().equals("VERBAL")) {
                rubroSelect.setValorTotal(BigDecimal.ZERO);
            } else if (renMultasIneteres.getCodigo().equals("SBU")) {
                Map<String, Object> pm = new HashMap<>();
                pm.put("anio", Utils.getAnio(new Date()));
                CtlgSalario salario = service.findByParameter(CtlgSalario.class, pm);
                rubroSelect.setValorTotal(salario.getValor().multiply(renMultasIneteres.getValor()));
            }

            rubrosSeleccionado.add(rubroSelect);
        }
    }

    public void seteandoCategoria() {
        this.localComercial.setCategoria(this.localComercial.getCategoria());
    }

    public void multaDescuentoturismo() throws ParseException {
        //30 dias
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaEnero = obj.parse(Utils.getAnio(new Date()).toString() + "-01-01");
        int dias = Utils.getDiasResta(fechaEnero, new Date());
        System.out.println("dias enero" + dias);
        FinaRenRubrosLiquidacion ru = null;
        BigDecimal desc = BigDecimal.ZERO;
        CatalogoItem itemDescuentoUno = catalogoService.getItemByCatalogoAndCodigo("GT_DESC_LICENCIA_TURISMO", "DESC_UNO");
        CatalogoItem itemDescuentoDos = catalogoService.getItemByCatalogoAndCodigo("GT_DESC_LICENCIA_TURISMO", "DESC_DOS");
        CatalogoItem itemDescuentoTres = catalogoService.getItemByCatalogoAndCodigo("GT_DESC_LICENCIA_TURISMO", "DESC_TRES");
        renTasaTurismo = new RenTasaTurismo();
        renTasaTurismo = (RenTasaTurismo) service.find("SELECT rc FROM RenTasaTurismo rc WHERE rc.categoria.id = :categoria",
                new String[]{"categoria"}, new Object[]{localComercial.getCategoria().getId()});
        System.out.println("renTasaTurismo " + renTasaTurismo);
        desc = BigDecimal.ZERO;
        ru = rubroSelect;
        System.out.println("renTasaTurismo " + renTasaTurismo.getValor() + "\t \t" + renTasaTurismo.getTipoItem().getCodigo());
        if (renTasaTurismo.getTipoItem() != null && !"5".equals(renTasaTurismo.getTipoItem().getCodigo())) {
            System.out.println("diferente a 5");
            BigDecimal calculo = new BigDecimal(ru.getCantidad().toString()).multiply(renTasaTurismo.getValor());
            System.out.println("calculo " + calculo);
            if (calculo.compareTo(renTasaTurismo.getMaximo()) == 1) {
                calculo = renTasaTurismo.getMaximo();
            }
            ru.setValorTotal(calculo);
            if (renovacion) {
                if (dias <= 90) {

                    if (dias >= 1 && dias <= 30) {
                        desc = (ru.getValorTotal().setScale(2, RoundingMode.HALF_UP).multiply(itemDescuentoUno.getValor().setScale(2, RoundingMode.HALF_UP))).divide(PORCENTAJE, 2, RoundingMode.HALF_UP);

                    } else if (dias >= 31 && dias <= 60) {
                        desc = (ru.getValorTotal().setScale(2, RoundingMode.HALF_UP).multiply(itemDescuentoDos.getValor().setScale(2, RoundingMode.HALF_UP))).divide(PORCENTAJE, 2, RoundingMode.HALF_UP);

                    } else if (dias >= 61 && dias <= 90) {
                        desc = (ru.getValorTotal().setScale(2, RoundingMode.HALF_UP).multiply(itemDescuentoTres.getValor().setScale(2, RoundingMode.HALF_UP))).divide(PORCENTAJE, 2, RoundingMode.HALF_UP);

                    }

                }
            }
            ru.setValorTotal(ru.getValorTotal().subtract(desc));
            rubrosSeleccionado.add(ru);
            Integer diaAtraso = 0;
        }

    }

    public Map<String, Object> pagoLicencUnicaNuevoRenovascion(FinaRenLocalComercial fina, FinaRenRubrosLiquidacion rubro) {
        return service.licenciaUnicaNewRenovacion(fina, rubro);
    }

    public void validarLiquidacion(FinaRenLiquidacion liq) {
        liq.setValidada(Boolean.TRUE);
        service.update(liq);
        JsfUtil.addInformationMessage("Liquidacion " + liq.getIdLiquidacion() + " validada con exito.", "");
    }

    public String getDetalleCategoria(FinaRenLocalComercial l) {
        String detalle = "";

        if (l.getCategoria() != null) {
            FinaRenLocalCategoria cat = service.find(FinaRenLocalCategoria.class,
                    l.getCategoria().getId());
            FinaRenLocalCategoria catPadre = service.find(FinaRenLocalCategoria.class,
                    cat.getPadre().longValue());
            detalle = catPadre.getDescripcion() + ": " + cat.getDescripcion();
            if (detalle == null) {
                detalle = "";
            }
        }
        return detalle;
    }

    public Boolean mostrarValidar() {

        if (clienteService.isAdmin(session.getUserRoles())) {
            return true;
        } else {
            if (session.getRoles().contains(director_renta) || session.getRoles().contains(asistente_renta)) {
                return true;
            }
        }
        return false;
    }

    public void cargarUsuariosDepartamento() {
        try {
            usuarios = new ArrayList<>();
            if (departamento != null) {
                usuarios.addAll(service.getUser(departamento));
            }
        } catch (Exception e) {
            System.err.println("e: " + e);
        }
    }

    /*AGREGADO 22/06/2018*/
    public void imprimirReporteTramitesActualizacion() {
//        try {
//            String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarDatos();
//            ss.instanciarParametros();
//            ss.setTieneDatasource(Boolean.TRUE);
//            ss.setNombreSubCarpeta("planificacion/certificados");
//
//            ss.addParametro("DEPARTAMENTO", "%" + departamento.getNombre() + "%");
//
//            ss.addParametro("LOGO", JsfUtil.getRealPath("/").concat(SisVars.sisLogo1));
//            ss.addParametro("LOGO2", path + SisVars.sisLogo);
//            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
//
//            switch (this.tipoReporte.intValue()) {
//                case 1:
//                    ss.setNombreReporte("sReporteTareasPagadas");
//                    ss.addParametro("DESDE", sdf.format(fechaDesde));
//                    ss.addParametro("HASTA", sdf.format(fechaHasta));
//                    if (usuario == null) {
//                        ss.addParametro("USUARIO", 0L);
//                    } else {
//                        ss.addParametro("USUARIO", usuario.getId());
//                    }
//                    break;
//
//                case 2:
//                    ss.setNombreReporte("sReporteTareasPorPagar");
//                    ss.addParametro("DESDE", sdf.format(fechaDesde));
//                    ss.addParametro("HASTA", sdf.format(fechaHasta));
//                    if (usuario == null) {
//                        ss.addParametro("USUARIO", 0L);
//                    } else {
//                        ss.addParametro("USUARIO", usuario.getId());
//                    }
//                    break;
//
//                /*AGREGADO 10/07/2018*/
//                case 3:
//                    ss.setNombreReporte("sReporteRubrosAgua");
//                    ss.addParametro("DESDE", sdf.format(fechaDesde));
//                    ss.addParametro("HASTA", sdf.format(fechaHasta));
//                    if (usuario == null) {
//                        ss.addParametro("USUARIO", 0L);
//                    } else {
//                        ss.addParametro("USUARIO", usuario.getId());
//                    }
//                    break;
//                default:
//                    return;
//
//            }
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//
//        } catch (Exception e) {
//            Logger.getLogger(GeneracionAvaluos.class
//                    .getName()).log(Level.SEVERE, null, e);
//        }
    }

    public void imprimirCertificadoPlanificacion() {

//        String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//        ss.borrarParametros();
//        ss.instanciarParametros();
//        ss.setNombreSubCarpeta("planificacion/certificados");
//        ss.setTieneDatasource(Boolean.TRUE);
//        ss.addParametro("LOGO", path + SisVars.sisLogo);
//        ss.addParametro("FIRMA", path + SisVars.firmaUsuario + session.getName_user() + ".png");
//        ss.addParametro("ID", liquidacion.getCatPlanificacionTitulos().getId());
//        ss.setNombreReporte(liquidacion.getTipoLiquidacion().getNombreReporte());
//        JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
        clean();
    }

    public void imprimirCertificadoConvenio() {
//
//        String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//        ss.borrarParametros();
//        ss.instanciarParametros();
//        ss.setTieneDatasource(Boolean.TRUE);
//        ss.setNombreSubCarpeta("RentasMontecristi/ConveniosdePago/");
//        ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/RentasMontecristi/ConveniosdePago/"));
//        ss.addParametro("ID", convenioCuenta.getId());
//        ss.setNombreReporte("sConveniodePagoMedidor");
//        ss.addParametro("Marca", path + SisVars.sisPermisoFuncionamiento);
//        ss.addParametro("Fondo", path + SisVars.sisMarcaAgua);
//
//        JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
        clean();
    }

    public void clean() {
        rubroSelect = null;
        solicitantes = new LazyModel<>(Cliente.class);
        solicitantes.getFilterss().put("validado", true);
        solicitantes.getFilterss().put("estado", true);
        loadValuesPlanificacionTitulo();
        calculoEmpleados = Boolean.FALSE;
        calculo10Porciento = Boolean.FALSE;
        cobrosGenerales = new FinaRenLiquidacion();
        cobrosGenerales.setAnio(Utils.getAnio(new Date()));
        rubrosSeleccionado = new ArrayList<>();
        ciRucCobros = "";
        loadRenders();
    }

    public void imprimirCertificadoPlanificacion(FinaRenLiquidacion l) {

        // String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.setNombreSubCarpeta("planificacion/certificados");
        //  ss.setTieneDatasource(Boolean.TRUE);
//        ss.addParametro("LOGO", path + SisVars.sisLogo);
//        ss.addParametro("FIRMA", path + SisVars.firmaUsuario + session.getName_user() + ".png");
//        ss.addParametro("ID", l.getCatPlanificacionTitulos().getId());
        ss.setNombreReporte(l.getTipoLiquidacion().getNombreReporte());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirPermiso(FinaRenLiquidacion l) {
//        if (l.getEstadoLiquidacion().getId().equals(1L)) {
//            String firma = "";
//            //  String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//            ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
//            //    ss.setTieneDatasource(Boolean.TRUE);
//            //         ss.addParametro("LOGO", path + SisVars.sisLogo);
//            ///DIRECTOR DE GESTION AMBIENTAL
//            Rol catRol = (Rol) acl.find(Querys.getAclRolByDepaartamentoDirector, new String[]{"departamento"},
//                    new Object[]{47L});
//            if (catRol != null) {
//                for (AclUser dir : catRol.getAclUserCollection()) {
//                    System.out.println("dir.getUsuario(); " + dir.getUsuario());
//                    firma = dir.getUsuario();
//                    break;
//                }
//            }
//            System.out.println("firma " + firma);
//            ss.addParametro("FIRMA", path + SisVars.firmaUsuario + firma + ".png");
//            ss.addParametro("Fondo", path + SisVars.sisMarcaAgua);
//            ss.addParametro("ID", l.getCatPlanificacionTitulos().getId());
//            ss.setNombreReporte("sPermisoMunicipal");
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//        } else {
//            JsfUtil.addErrorMessage("PERMISO TIENE DEUDAS PENDIENTE", null);
//        }

    }

    public void imprimirPermisoEspaciosPublicos(FinaRenLiquidacion l) {
//
//        if (l.getEstadoLiquidacion().getId().equals(1L)) {
//            String firma = "";
//            String path = JsfUtilContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//            ss.setNombreSubCarpeta("RentasMontecristi/Liquidaciones");
//            ss.setTieneDatasource(Boolean.TRUE);
//            ss.addParametro("FIRMA", path + SisVars.firmaUsuario + "EspaciosPublicos.png");
//            ss.addParametro("ID", l.getId());
//            ss.setNombreReporte("sPermisoConstruccionEspacioPublico");
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");
//        } else {
//            JsfUtil.addErrorMessage("PERMISO TIENE DEUDAS PENDIENTE", null);
//        }

    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        if (l.getTipoLiquidacion() != null) {
            if (l.getTipoLiquidacion().getTransaccionPadre() != null) {
                if (l.getTipoLiquidacion().getTransaccionPadre() == 16L && l.getAnio() < Utils.getAnio(new Date())) {
                    try {
                        Calendar fechaInteres = Calendar.getInstance();
                        fechaInteres.set(l.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
                        l.setInteres(service.generarInteres(l.getTotalPago(), fechaInteres.getTime(), null));
                        if (l.getInteres() == null) {
                            l.setInteres(BigDecimal.ZERO);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    l.setInteres(BigDecimal.ZERO);
                }
            }
        }

        return l.getInteres();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public RenDepartamentoDetalleTitulosServices getRenDepartamentoDetalleTitulosServices() {
        return renDepartamentoDetalleTitulosServices;
    }

    public void setRenDepartamentoDetalleTitulosServices(RenDepartamentoDetalleTitulosServices renDepartamentoDetalleTitulosServices) {
        this.renDepartamentoDetalleTitulosServices = renDepartamentoDetalleTitulosServices;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public FinaRenLiquidacionService getLiquidacionService() {
        return liquidacionService;
    }

    public void setLiquidacionService(FinaRenLiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    public BigDecimal getSueldoBasico() {
        return sueldoBasico;
    }

    public void setSueldoBasico(BigDecimal sueldoBasico) {
        this.sueldoBasico = sueldoBasico;
    }

    public List<RenTasaTurismo> getListaTasasTurismo() {
        return listaTasasTurismo;
    }

    public void setListaTasasTurismo(List<RenTasaTurismo> listaTasasTurismo) {
        this.listaTasasTurismo = listaTasasTurismo;
    }

    public List<FinaRenActividadComercial> getActividesLocales() {
        return actividesLocales;
    }

    public void setActividesLocales(List<FinaRenActividadComercial> actividesLocales) {
        this.actividesLocales = actividesLocales;
    }

    public RenTasaTurismo getSeleccionTasa() {
        return seleccionTasa;
    }

    public void setSeleccionTasa(RenTasaTurismo seleccionTasa) {
        this.seleccionTasa = seleccionTasa;
    }

    public PlanificacionService getPlanificacionService() {
        return planificacionService;
    }

    public void setPlanificacionService(PlanificacionService planificacionService) {
        this.planificacionService = planificacionService;
    }

    public String getIdentificacionCobros() {
        return identificacionCobros;
    }

    public void setIdentificacionCobros(String identificacionCobros) {
        this.identificacionCobros = identificacionCobros;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Logger getLOG() {
        return LOG;
    }

    public Long getAsistente_renta() {
        return asistente_renta;
    }

    public Long getDirector_renta() {
        return director_renta;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public TitulosReporteCacheLocal getTitulosReporte() {
        return titulosReporte;
    }

    public void setTitulosReporte(TitulosReporteCacheLocal titulosReporte) {
        this.titulosReporte = titulosReporte;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Usuarios getCajero() {
        return cajero;
    }

    public void setCajero(Usuarios cajero) {
        this.cajero = cajero;
    }

    public LazyModel<FinaRenLiquidacion> getTitulosCredito() {
        return titulosCredito;
    }

    public void setTitulosCredito(LazyModel<FinaRenLiquidacion> titulosCredito) {
        this.titulosCredito = titulosCredito;
    }

    public LazyModel<FinaRenLiquidacion> getTitulosCreditoPagado() {
        return titulosCreditoPagado;
    }

    public void setTitulosCreditoPagado(LazyModel<FinaRenLiquidacion> titulosCreditoPagado) {
        this.titulosCreditoPagado = titulosCreditoPagado;
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public FinaRenTipoLiquidacion getLiquidacionCG() {
        return liquidacionCG;
    }

    public void setLiquidacionCG(FinaRenTipoLiquidacion liquidacionCG) {
        this.liquidacionCG = liquidacionCG;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosSeleccionado() {
        return rubrosSeleccionado;
    }

    public void setRubrosSeleccionado(List<FinaRenRubrosLiquidacion> rubrosSeleccionado) {
        this.rubrosSeleccionado = rubrosSeleccionado;
    }

    public FinaRenDetLiquidacion getDetalle() {
        return detalle;
    }

    public void setDetalle(FinaRenDetLiquidacion detalle) {
        this.detalle = detalle;
    }

    public FinaRenRubrosLiquidacion getRubroSelect() {
        return rubroSelect;
    }

    public void setRubroSelect(FinaRenRubrosLiquidacion rubroSelect) {
        this.rubroSelect = rubroSelect;
    }

    public FinaRenLiquidacion getCobrosGenerales() {
        return cobrosGenerales;
    }

    public void setCobrosGenerales(FinaRenLiquidacion cobrosGenerales) {
        this.cobrosGenerales = cobrosGenerales;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RecActasEspeciesDet getActa() {
        return acta;
    }

    public void setActa(RecActasEspeciesDet acta) {
        this.acta = acta;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public CatPredioModel getPr() {
        return pr;
    }

    public void setPr(CatPredioModel pr) {
        this.pr = pr;
    }

    public Integer getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(Integer numPredio) {
        this.numPredio = numPredio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getDesdeTemp() {
        return desdeTemp;
    }

    public void setDesdeTemp(Integer desdeTemp) {
        this.desdeTemp = desdeTemp;
    }

    public List<String> getCdlas() {
        return cdlas;
    }

    public void setCdlas(List<String> cdlas) {
        this.cdlas = cdlas;
    }

    public List<FinaRenLiquidacion> getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(List<FinaRenLiquidacion> emisiones) {
        this.emisiones = emisiones;
    }

    public FinaRenLiquidacion getLiqSelect() {
        return liqSelect;
    }

    public void setLiqSelect(FinaRenLiquidacion liqSelect) {
        this.liqSelect = liqSelect;
    }

    public List<FinaRenEntidadBancaria> getBancos() {
        return bancos;
    }

    public void setBancos(List<FinaRenEntidadBancaria> bancos) {
        this.bancos = bancos;
    }

    public List<FinaRenEntidadBancaria> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<FinaRenEntidadBancaria> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public BigDecimal getTotalCoactiva() {
        return totalCoactiva;
    }

    public void setTotalCoactiva(BigDecimal totalCoactiva) {
        this.totalCoactiva = totalCoactiva;
    }

    public Map<String, Object> getParamt() {
        return paramt;
    }

    public void setParamt(Map<String, Object> paramt) {
        this.paramt = paramt;
    }

    public PagoTituloReporteModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoTituloReporteModel modelPago) {
        this.modelPago = modelPago;
    }

    public Boolean getPagoRealizado() {
        return pagoRealizado;
    }

    public void setPagoRealizado(Boolean pagoRealizado) {
        this.pagoRealizado = pagoRealizado;
    }

    public Integer getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(Integer tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public Integer getTipoSolicitante() {
        return tipoSolicitante;
    }

    public void setTipoSolicitante(Integer tipoSolicitante) {
        this.tipoSolicitante = tipoSolicitante;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public List<CatPredioModel> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<CatPredioModel> propiedades) {
        this.propiedades = propiedades;
    }

    public Date getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(Date fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Long getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Long tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Long getTipoReporte2() {
        return tipoReporte2;
    }

    public void setTipoReporte2(Long tipoReporte2) {
        this.tipoReporte2 = tipoReporte2;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Cliente getRegistroEnte() {
        return registroEnte;
    }

    public void setRegistroEnte(Cliente registroEnte) {
        this.registroEnte = registroEnte;
    }

    public Integer getTipoEnte() {
        return tipoEnte;
    }

    public void setTipoEnte(Integer tipoEnte) {
        this.tipoEnte = tipoEnte;
    }

    public Boolean getExcepcionalEmpresa() {
        return excepcionalEmpresa;
    }

    public void setExcepcionalEmpresa(Boolean excepcionalEmpresa) {
        this.excepcionalEmpresa = excepcionalEmpresa;
    }

    public String getCiRucCobros() {
        return ciRucCobros;
    }

    public void setCiRucCobros(String ciRucCobros) {
        this.ciRucCobros = ciRucCobros;
    }

    public Boolean getVariosPagos() {
        return variosPagos;
    }

    public void setVariosPagos(Boolean variosPagos) {
        this.variosPagos = variosPagos;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<Object> getParametros() {
        return parametros;
    }

    public void setParametros(List<Object> parametros) {
        this.parametros = parametros;
    }

    public AppDepartamentoDetalleTitulos getCatPlanificacionTitulos() {
        return catPlanificacionTitulos;
    }

    public void setCatPlanificacionTitulos(AppDepartamentoDetalleTitulos catPlanificacionTitulos) {
        this.catPlanificacionTitulos = catPlanificacionTitulos;
    }

    public Boolean getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Boolean tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public LazyModel<AppDepartamentoDetalleTitulos> getCatPlanificacionTitulosLazy() {
        return catPlanificacionTitulosLazy;
    }

    public void setCatPlanificacionTitulosLazy(LazyModel<AppDepartamentoDetalleTitulos> catPlanificacionTitulosLazy) {
        this.catPlanificacionTitulosLazy = catPlanificacionTitulosLazy;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }

    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
    }

    public RenDepartamentoDetalleTitulos getPlanificacionTitulos() {
        return planificacionTitulos;
    }

    public void setPlanificacionTitulos(RenDepartamentoDetalleTitulos planificacionTitulos) {
        this.planificacionTitulos = planificacionTitulos;
    }

    public List<RenDepartamentoDetalleTitulos> getPlanificacionTitulosList() {
        return planificacionTitulosList;
    }

    public void setPlanificacionTitulosList(List<RenDepartamentoDetalleTitulos> planificacionTitulosList) {
        this.planificacionTitulosList = planificacionTitulosList;
    }

    public List<UnidadAdministrativa> getDeps() {
        return deps;
    }

    public void setDeps(List<UnidadAdministrativa> deps) {
        this.deps = deps;
    }

    public Boolean getEsLiq() {
        return esLiq;
    }

    public void setEsLiq(Boolean esLiq) {
        this.esLiq = esLiq;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Boolean getCalculoEmpleados() {
        return calculoEmpleados;
    }

    public void setCalculoEmpleados(Boolean calculoEmpleados) {
        this.calculoEmpleados = calculoEmpleados;
    }

    public Boolean getCalculo10Porciento() {
        return calculo10Porciento;
    }

    public void setCalculo10Porciento(Boolean calculo10Porciento) {
        this.calculo10Porciento = calculo10Porciento;
    }

    public Boolean getCalculoConvenio() {
        return calculoConvenio;
    }

    public void setCalculoConvenio(Boolean calculoConvenio) {
        this.calculoConvenio = calculoConvenio;
    }

    public Boolean getRenderPlanificacion() {
        return renderPlanificacion;
    }

    public void setRenderPlanificacion(Boolean renderPlanificacion) {
        this.renderPlanificacion = renderPlanificacion;
    }

    public Boolean getRenderPermisoCementerio() {
        return renderPermisoCementerio;
    }

    public void setRenderPermisoCementerio(Boolean renderPermisoCementerio) {
        this.renderPermisoCementerio = renderPermisoCementerio;
    }

    public LazyModel<EspCementerioBoveda> getBovedasLazy() {
        return bovedasLazy;
    }

    public void setBovedasLazy(LazyModel<EspCementerioBoveda> bovedasLazy) {
        this.bovedasLazy = bovedasLazy;
    }

    public EspCementerioBoveda getBoveda() {
        return boveda;
    }

    public void setBoveda(EspCementerioBoveda boveda) {
        this.boveda = boveda;
    }

    public AppDepartamentoDetalleTitulos getDepartamentoDetalleTitulosGetionAmbiental() {
        return departamentoDetalleTitulosGetionAmbiental;
    }

    public void setDepartamentoDetalleTitulosGetionAmbiental(AppDepartamentoDetalleTitulos departamentoDetalleTitulosGetionAmbiental) {
        this.departamentoDetalleTitulosGetionAmbiental = departamentoDetalleTitulosGetionAmbiental;
    }

    public LazyModel<FinaRenLocalComercial> getLocalesComercialesLazy() {
        return localesComercialesLazy;
    }

    public void setLocalesComercialesLazy(LazyModel<FinaRenLocalComercial> localesComercialesLazy) {
        this.localesComercialesLazy = localesComercialesLazy;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public List<EspFotoBovedas> getBovedasPropietario() {
        return bovedasPropietario;
    }

    public void setBovedasPropietario(List<EspFotoBovedas> bovedasPropietario) {
        this.bovedasPropietario = bovedasPropietario;
    }

    public List<Long> getDepartamentosIds() {
        return departamentosIds;
    }

    public void setDepartamentosIds(List<Long> departamentosIds) {
        this.departamentosIds = departamentosIds;
    }

    public Boolean getEsRegistroPropiedad() {
        return esRegistroPropiedad;
    }

    public void setEsRegistroPropiedad(Boolean esRegistroPropiedad) {
        this.esRegistroPropiedad = esRegistroPropiedad;
    }

    public String getObservacionConvenioCuenta() {
        return observacionConvenioCuenta;
    }

    public void setObservacionConvenioCuenta(String observacionConvenioCuenta) {
        this.observacionConvenioCuenta = observacionConvenioCuenta;
    }

    public List<CatPredioModel> getCatPredioLazy() {
        return catPredioLazy;
    }

    public void setCatPredioLazy(List<CatPredioModel> catPredioLazy) {
        this.catPredioLazy = catPredioLazy;
    }

    public RenTasaTurismo getRenTasaTurismo() {
        return renTasaTurismo;
    }

    public void setRenTasaTurismo(RenTasaTurismo renTasaTurismo) {
        this.renTasaTurismo = renTasaTurismo;
    }

    public boolean isTurismotipo() {
        return turismotipo;
    }

    public void setTurismotipo(boolean turismotipo) {
        this.turismotipo = turismotipo;
    }

    public boolean isTasaHabitacion() {
        return tasaHabitacion;
    }

    public void setTasaHabitacion(boolean tasaHabitacion) {
        this.tasaHabitacion = tasaHabitacion;
    }

    public boolean isPermisoAdicional() {
        return permisoAdicional;
    }

    public void setPermisoAdicional(boolean permisoAdicional) {
        this.permisoAdicional = permisoAdicional;
    }

    public Date getEmisionTurismo() {
        return emisionTurismo;
    }

    public void setEmisionTurismo(Date emisionTurismo) {
        this.emisionTurismo = emisionTurismo;
    }

    public List<String> getListaMultasIneteres() {
        return listaMultasIneteres;
    }

    public void setListaMultasIneteres(List<String> listaMultasIneteres) {
        this.listaMultasIneteres = listaMultasIneteres;
    }

    public boolean isRenovacion() {
        return renovacion;
    }

    public void setRenovacion(boolean renovacion) {
        this.renovacion = renovacion;
    }

    public FnSolicitudExoneracion getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(FnSolicitudExoneracion solicitud) {
        this.solicitud = solicitud;
    }

    public RenParametrosInteresMulta getRenMultasIneteres() {
        return renMultasIneteres;
    }

    public void setRenMultasIneteres(RenParametrosInteresMulta renMultasIneteres) {
        this.renMultasIneteres = renMultasIneteres;
    }

    public String getDescripcionMulta() {
        return descripcionMulta;
    }

    public void setDescripcionMulta(String descripcionMulta) {
        this.descripcionMulta = descripcionMulta;
    }

    public List<Integer> getListaVeces() {
        return listaVeces;
    }

    public void setListaVeces(List<Integer> listaVeces) {
        this.listaVeces = listaVeces;
    }

    public Integer getVeces() {
        return veces;
    }

    public void setVeces(Integer veces) {
        this.veces = veces;
    }

    public FinaRenActividadComercial getSeleccionActividad() {
        return seleccionActividad;
    }

    public void setSeleccionActividad(FinaRenActividadComercial seleccionActividad) {
        this.seleccionActividad = seleccionActividad;
    }

    public List<FinaRenTipoLocalComercial> getListaTipoLocales() {
        return listaTipoLocales;
    }

    public void setListaTipoLocales(List<FinaRenTipoLocalComercial> listaTipoLocales) {
        this.listaTipoLocales = listaTipoLocales;
    }

    public FinaRenTipoLocalComercial getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(FinaRenTipoLocalComercial tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public List<CatalogoItem> getListaFactoresUbicacion() {
        return listaFactoresUbicacion;
    }

    public void setListaFactoresUbicacion(List<CatalogoItem> listaFactoresUbicacion) {
        this.listaFactoresUbicacion = listaFactoresUbicacion;
    }

    public CatalogoItem getFactorUbicacion() {
        return factorUbicacion;
    }

    public void setFactorUbicacion(CatalogoItem factorUbicacion) {
        this.factorUbicacion = factorUbicacion;
    }

//</editor-fold>
}
