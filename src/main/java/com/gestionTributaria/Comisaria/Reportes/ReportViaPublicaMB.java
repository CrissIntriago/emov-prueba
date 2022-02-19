/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Reportes;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEntidadBancaria;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.RenTipoLiquidacionDepartamento;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Controller.GenerarConvenioPago;
import com.gestionTributaria.Controller.LiquidacionGeneralMB;
import com.gestionTributaria.Controller.ReporteRentasMB;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.RecActasEspeciesDet;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoDetallerService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ActualizarListado;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.CtlgItemDTO;
import com.gestionTributaria.models.PagoTituloReporteModel;
import com.gestionTributaria.models.TitulosReporteCacheLocal;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */

@Named
@ViewScoped
public class ReportViaPublicaMB implements Serializable {

    public static final Long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ReporteRentasMB.class.getName());

    @Inject
    private Event<ActualizarListado> eventUpdate;
    @Inject
    private ManagerService manager;
    @Inject
    private TitulosReporteCacheLocal titulosReporte;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private UsuarioService userService;
    @Inject
    private ManagerService service;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FnConvenioPagoDetallerService cv;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;

    private String esUrbano = "1";
    private String sss = "No se ecuentra el sector";
    protected PagoTituloReporteModel modelPago = new PagoTituloReporteModel();
    protected TreeNode root = new DefaultTreeNode("Titulos", null);
    protected TreeNode selectedNode;
    protected Cajero cajero;
    private Boolean esLiq;
    protected LazyModel<FinaRenLiquidacion> titulosCredito;
    protected LazyModel<FinaRenLiquidacion> titulosCreditoPagado;
    protected TreeNode root2;
    protected FinaRenTipoLiquidacion liquidacionCG;
    protected List<FinaRenRubrosLiquidacion> rubrosSeleccionado = new ArrayList<>();
    protected FinaRenDetLiquidacion detalle = new FinaRenDetLiquidacion();
    protected FinaRenRubrosLiquidacion rubroSelect = new FinaRenRubrosLiquidacion();
    protected FinaRenLiquidacion cobrosGenerales = new FinaRenLiquidacion();
    protected FinaRenLiquidacion liquidacion;
    protected RecActasEspeciesDet acta = new RecActasEspeciesDet();
    protected LazyModel<Cliente> solicitantes;
    protected CatPredioModel pr = new CatPredioModel();
    protected Integer numPredio;
    protected Integer cantidad;
    protected Integer desdeTemp;
    protected List<String> cdlas;
    protected List<FinaRenLiquidacion> emisiones = new ArrayList<>();
    protected FinaRenLiquidacion liqSelect = new FinaRenLiquidacion();
    protected List<FinaRenEntidadBancaria> bancos;
    protected List<FinaRenEntidadBancaria> tarjetas;
    protected LiquidacionGeneralMB consulP;
    protected BigDecimal totalCoactiva = new BigDecimal("0.00");
    private Map<String, Object> paramt;
    protected Boolean pagoRealizado = Boolean.FALSE;
    protected Integer tipoConsulta = 1;
    protected Integer tipoSolicitante;
    protected CatPredioModel predioModel = new CatPredioModel();
    protected Cliente ente = new Cliente();
    protected Cliente ente2 = new Cliente();
    protected List<CatPredioModel> propiedades = new ArrayList<>();
    protected Date fechaReporte = new Date();
    protected Date fechaCierre = new Date();
    protected Long tipoReporte;
    protected Long tipoReporte2;
    protected FinaRenTipoLiquidacion tipoReporteLiqui;
    protected FinaRenRubrosLiquidacion rubroLi;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    protected Cliente registroEnte = new Cliente();
    private Integer tipoEnte = 1;
    protected Boolean excepcionalEmpresa = Boolean.FALSE;
    protected String ciRucCobros;
    protected Boolean variosPagos = Boolean.FALSE;
    private final Long asistente_renta = 189L;
    private final Long director_renta = 98L;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaDesde1;
    private Date fechaHasta1;
    private FinaRenLiquidacion liquidacionAnular;
    private String observacion;
    private String clase;
    private String placa;
    private String orden;
    private String marca;
    private String aniosPrivado;
    private String aniosPublicos;
    private BigDecimal aval;
    private CtlgSalario salario;
    private Map<String, Object> parametros;
    private Integer tipoCons;
    private Integer casoConvenio;
    private String textoBusquedaConvenio;
    private BigDecimal abono = BigDecimal.ZERO;

    private CatPredio predio;
    private List<Documentos> listaDocumentos;
    private Documentos documento;
    private FinaRenLiquidacion original;
    private Documentos documentoDescargar;
    private String numLiquidacionReport = "";
    private String tipoPredio = "";
    private String observacionT = "";
    private String claveCatCon = "";
    private GenerarConvenioPago gc;
    private Integer eleccion = null;
    private Integer eleccion2 = null;

    /*AGREGADO*/
    protected List<String> user = new ArrayList<>();
    private LazyModel<FinaRenLocalComercial> lazyLocal;
    private LazyModel<FinaRenPatente> clienteLazy;

    private Integer anio;
    private FinaRenLocalComercial localSel;
    private FinaRenPatente client;

    private Boolean abrirRubro = Boolean.FALSE;
    private List<Integer> estadosConvenios = null;

    //NUEVO ENRIQUE
    private Boolean renderEditCliente;

    private Integer tipoContribuyente;
    private List<FinaRenEstadoLiquidacion> estadosLiquidaciones;
    private boolean detallado;
    private FinaRenEstadoLiquidacion estadoLiquidacion;
    private String tipoBusqueda;
    private Boolean abrirTipoP;

    @PostConstruct
    public void initView() {
        try {
            estadoLiquidacion = new FinaRenEstadoLiquidacion();
            tipoContribuyente = 0;
            detallado = false;
            anio = Utils.getAnio(new Date());
            tipoReporteLiquiList = new ArrayList<>();
            tipoReporteLiquiList = service.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.id IN (13,149,10,16) AND r.nombreTransaccion IS NOT NULL and r.estado = true"
                    + " ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC", null);
            estadosLiquidaciones = new ArrayList<>();
            estadosLiquidaciones = manager.getEstadoLiquidaciones(new String[]{"pagado,por_pagar,inactivo,baja_n"});
            abrirTipoP = Boolean.FALSE;
            estadosConvenios = new ArrayList<>();
            estadosConvenios.add(0);
            estadosConvenios.add(1);
            estadosConvenios.add(2);
            estadosConvenios.add(3);
            estadosConvenios.add(4);
            estadosConvenios.add(5);
            estadosConvenios.add(6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getObservacionEstado(Integer tt) {
        switch (tt) {
            case 0:
                return "PREELABORACION DEL CONVENIO.";
            case 1:
                return "ACTUALIZACION DE CUOTAS O PORCENTAJE INICIAL PARA PAGO INICIAL.";
            case 2:
                return "PENDIENTE DE ACTIVACION POR COBRO ABONO INICIAL.";
            case 3:
                return "APROBADO.";
            case 4:
                return "NO APROBADO.";
            case 5:
                return "CANCELADO.";
            default:
                return "COMPLETADO.";
        }

    }

    public void generarReporteCajero2(FnConvenioPago c) {

    }

    public void lazyLocalSearch() {
        lazyLocal = new LazyModel(FinaRenLocalComercial.class);
        lazyLocal.getFilterss().put("estado", true);

    }

    public void lazyClienteSearch() {
        clienteLazy = new LazyModel(FinaRenPatente.class);
        clienteLazy.getFilterss().put("propietario:noEqual", null);

    }

    public void seleccionLocal(FinaRenLocalComercial lo) {
        localSel = lo;
        JsfUtil.executeJS("PF('dlogoLocales').hide()");
        JsfUtil.update("fmlocal");
    }

    public void seleccionCliente(FinaRenPatente lo) {
        client = lo;
        JsfUtil.executeJS("PF('dlogoClientes').hide()");
        JsfUtil.update("fmlocal2");
        JsfUtil.update("mainForm");
    }

    public Boolean abrirRubroF(FinaRenTipoLiquidacion id) {
        System.out.println("el valor que viene es--->>  " + tipoReporteLiqui.getId());
        rubroReporteList = new ArrayList<>();
        rubroReporteList = manager.getRubrosByTipoLiquidacionCodRubroASC(tipoReporteLiqui.getId());
        for (FinaRenRubrosLiquidacion rubro : rubroReporteList) {
            System.out.println("los rubros son--->  " + rubro.getDescripcion());
        }

        setAbrirRubro(Boolean.TRUE);
        JsfUtil.update("fmlocal");
        return abrirRubro;
    }

    public void abrirRubroF() {
        rubroReporteList = new ArrayList<>();
        rubroReporteList = manager.getRubrosByTipoLiquidacionCodRubroASC(tipoReporteLiqui.getId());
    }

    public void actuFormEstado() {
        System.out.println("int " + eleccion);
        if (eleccion != null) {
            abrirTipoP = Boolean.TRUE;
            JsfUtil.update("mainForm");
        } else {
            abrirTipoP = Boolean.FALSE;
            JsfUtil.update("mainForm");
        }
//        JsfUtil.update("mainForm");

    }

    public void renderTipoP() {
        if (eleccion != null) {
            abrirTipoP = Boolean.TRUE;
            JsfUtil.update("mainForm");
        } else {
            abrirTipoP = Boolean.FALSE;
            JsfUtil.update("mainForm");
        }
    }

    public void generarReporteCajero(Boolean excel) {
        try {
            ss.instanciarParametros();
            if (this.tipoReporte2 == null) {
                JsfUtil.addWarningMessage("Error", "Seleccione un Tipo");
                return;
            }

            if (rubroLi != null && rubroLi.getId() != null) {
                Long estado = null;
                ss.addParametro("persona", tipoContribuyente);
                ss.addParametro("fecha_desde", fechaDesde1);
                ss.addParametro("fecha_hasta", fechaHasta1);
                ss.addParametro("rubro", rubroLi.getId());

                if (estadoLiquidacion != null && estadoLiquidacion.getId() != null) {
                    estado = estadoLiquidacion.getId();
                }
                if (excel) {
                    ss.setOnePagePerSheet(false);
                    ss.setContentType("xlsx");
                }

                ss.addParametro("estado", estado);
                ss.setNombreReporte("tipoRubros");
                ss.setNombreSubCarpeta("GestionTributatia/Transacciones");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
                return;
            }

            Long estado = null;
            Long tipoLioqui = null;
            ss.addParametro("fecha_desde", fechaDesde1);
            ss.addParametro("fecha_hasta", fechaHasta1);
            
            if (!detallado) {
                ss.addParametro("tipo_contri", tipoContribuyente);
                if (tipoReporteLiqui != null && tipoReporteLiqui.getId() != null) {
                    tipoLioqui = tipoReporteLiqui.getId();
                }
                ss.addParametro("tipo_liqui", tipoLioqui);

                if (estadoLiquidacion != null && estadoLiquidacion.getId() != null) {
                    estado = estadoLiquidacion.getId();
                }
                ss.addParametro("estado", estado);
                ss.addParametro("impresion", session.getNameUser());
                ss.setNombreReporte("permisosViaPublica");
                
            } else {
                ss.addParametro("tipo_contri", tipoContribuyente);
                if (tipoReporteLiqui != null && tipoReporteLiqui.getId() != null) {
                    tipoLioqui = tipoReporteLiqui.getId();
                }
                ss.addParametro("tipo_liqui", tipoLioqui);
                if (estadoLiquidacion != null && estadoLiquidacion.getId() != null) {
                    estado = estadoLiquidacion.getId();
                }

                ss.addParametro("estado", estado);
                ss.addParametro("impresion", session.getNameUser());
                ss.setNombreReporte("permisosViaPublica");

            }

            if (excel) {
                ss.setOnePagePerSheet(false);
                ss.setContentType("xlsx");
            }

            ss.setNombreSubCarpeta("GestionTributatia/comisaria");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirConvenioCuotaInicial(FnConvenioPago fnc) {
        gc.imprimirConvenioCuotaInicial(fnc);
    }

    //<editor-fold defaultstate="collapsed" desc="LOGICA OTHERS">
    public void llenarArbolAdmin() {
        List<FinaRenTipoLiquidacion> raices = null;

        parametros = new HashMap<>();
        parametros.put("idPadre", 0L);
        raices = manager.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre = :idPadre AND r.nombreTransaccion IS NOT NULL AND r.mostrarTransaccion = true and r.estado = true"
                + " ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC", parametros);

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

            hijos = manager.listaRubrosHijosAdmin(hoja.getId());

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

    private void llenarHijosArbol2(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            parametros = new HashMap<>();
            parametros.put("usuario", userSession.getUserId());
            parametros.put("idPadre", hoja.getId());
            List<FinaRenTipoLiquidacion> hijos = new ArrayList();
            List<RenTipoLiquidacionDepartamento> departamentos = manager.findAllQuery("SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.usuario.id =:usuario AND r.estado = true AND r.tipoLiquidacion.transaccionPadre=:idPadre",
                    parametros);
            FinaRenTipoLiquidacion rtl = null;
            if (departamentos != null && !departamentos.isEmpty()) {
                for (RenTipoLiquidacionDepartamento rtld : departamentos) {
                    rtl = manager.find(FinaRenTipoLiquidacion.class,
                            rtld.getTipoLiquidacion().getId());
                    hijos.add(rtl);
                }
            } else {
                return;
            }

            if (hijos.isEmpty()) {
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
            System.err.println(e);
        }
    }

    public void buscarCliente() {
        try {
            if (ente2.getIdentificacion() != null) {
                Map<String, Object> paramcli = new HashMap<>();
                paramcli.put("identificacion", ente2.getIdentificacion());
                Cliente cli = this.manager.findByParameter(Cliente.class,
                        paramcli);
                if (cli.getIdentificacion() != null) {
                    ente2 = cli;
                    PrimeFaces.current().ajax().update(":formEditSolicitante");
                } else {
                    JsfUtil.addInformationMessage("Advertencia", "CLIENTE NO EXISTE");
                }
            } else {
                JsfUtil.addInformationMessage("Advertencia", "DEBE INGRESAR UNA IDENTIFICACIÓN");
            }
        } catch (Exception e) {

        }
    }

    public void actualizarClienteEnBase(Cliente c) {
        try {
            if (c.getId() != null) {
                c.setUsuarioModificacion(userSession.getNameUser());
                c.setFechaModificacion(new Date());
                clienteService.edit(c);
                //System.out.println("Cliente: "+c.getNombre()+" fechaMod: "+c.getFechaModificacion()+" user: "+c.getUsuarioModificacion());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Advertencia", "OCURRIÓ UN ERROR");
        }

    }

    public void resetForm() {
        initView();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();
        cobrosGenerales.setComprador(new Cliente());
        rubrosSeleccionado = new ArrayList<>();
        ente = new Cliente();
        ciRucCobros = "";
    }

    public void actualizarListado(@Observes ActualizarListado listado) {
        if (listado.isActualizarValidar()) {
            titulosCredito = new LazyModel<>(FinaRenLiquidacion.class
            );
            titulosCredito.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
            titulosCredito.getSorteds().put("fechaIngreso", "desc");
            JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidaciones");
        }
    }

    public void onChangeTab(TabChangeEvent event) {
        try {
            List<FinaRenTipoLiquidacion> liqs = Arrays.asList(new FinaRenTipoLiquidacion(260L), new FinaRenTipoLiquidacion(261L));
//            List<RenTipoLiquidacion> liqs = new ArrayList<>();
//            liqs.add(new RenTipoLiquidacion(260L));
//            liqs.add(new RenTipoLiquidacion(261L));
            if (event.getTab().getId().equals("tituloCredito")) {

                if (titulosCredito == null) {
                    emisiones = new ArrayList<>();
                    liquidacion = new FinaRenLiquidacion();
                    cobrosGenerales = new FinaRenLiquidacion();
                    titulosCredito = new LazyModel<>(FinaRenLiquidacion.class
                    );
                    titulosCredito.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
                    titulosCredito.getSorteds().put("fechaIngreso", "desc");
//                    titulosCredito.getSorteds().put("fechaIngreso", "DESC");
                    JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidaciones");
//titulosCredito.addFilter("tipoLiquidacion:notEqual", liqs);

                }
            }
            if (event.getTab().getId().equals("tituloCreditoPagado")) {

                if (titulosCreditoPagado == null) {
                    emisiones = new ArrayList<>();
                    liquidacion = new FinaRenLiquidacion();
                    cobrosGenerales = new FinaRenLiquidacion();
                    titulosCreditoPagado = new LazyModel<>(FinaRenLiquidacion.class
                    );
                    titulosCreditoPagado.addFilter("estadoLiquidacion", new FinaRenEstadoLiquidacion(1L));
                    titulosCreditoPagado.getSorteds().put("fechaIngreso", "DESC");
                    JsfUtil.update("mainForm:tvRecaudaciones:dtLiquidacionesPagadas");
//                    if (liqs != null) {
//                        titulosCredito.addFilter("tipoLiquidacion:notEqual", liqs);
//                    }

                }

            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public boolean renderAlcabalaSolicitante(FinaRenLiquidacion liq) {
        return liq.getTipoLiquidacion() != null && liq.getTipoLiquidacion().getPrefijo().equals("ALC") && liq.getPredio() == null;
    }

    public void encerarValor() {
        if (rubroSelect.getValorCalculo() == null) {
            rubroSelect.setValorCalculo(BigDecimal.ZERO);
        }
        Map<String, Object> pm = new HashMap<>();
        pm.put("anio", Utils.getAnio(new Date()));
        salario = manager.findByParameter(CtlgSalario.class,
                pm);
        if (salario == null) {
            JsfUtil.addInformationMessage("Validacion", "Debe ingresar salario básico en el módulo de Administración opción Salarios");
            rubroSelect.setValor(BigDecimal.ZERO);
            return;
        }
        if (rubroSelect.getId() == 1138) {

            /* consumr web services los avaluos
             (CtlgItem) manager.find("SELECT i FROM CtlgItem i INNER JOIN i.catalogo c WHERE c.nombre = 'rango_rodaje' AND :avaluo BETWEEN i.rangoDesde AND i.rangoHasta",
             new String[]{"avaluo"}, new Object[]{rubroSelect.getValorCalculo()});
             */
            CtlgItemDTO fac = new CtlgItemDTO();
            if (fac != null) {
                rubroSelect.setValor(fac.getFactor());
                this.aval = rubroSelect.getValorCalculo();
//                cobrosGenerales.setObservacion(String.format(obsRodaje, Utils.getAnio(new Date()), rubroSelect.getValorCalculo()));
                cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), Utils.getAnio(new Date()), this.aval, cobrosGenerales.getCodigoLocal(), this.clase, this.marca, this.orden));
            }
        } else if (rubroSelect.getTipoValor().getId() == 14L) {
            BigDecimal s = salario.getValor().multiply(rubroSelect.getValorPorcentualTemp()).setScale(2, RoundingMode.HALF_UP);
            System.out.println("valor porcentaje-->" + s);
            BigDecimal v = rubroSelect.getValorCalculo().multiply(s).setScale(2, RoundingMode.HALF_UP);
//            v = v.multiply(BigDecimal.valueOf(rubroSelect.getCantidad().doubleValue()), MathContext.UNLIMITED);

            rubroSelect.setValor(v);
        } else if (rubroSelect.getTipoValor().getId() == 3L) {
//            BigDecimal v = BigDecimal.valueOf(rubroSelect.getCantidad().doubleValue()).multiply(rubroSelect.getValor()).setScale(2, RoundingMode.HALF_UP);
            rubroSelect.setValor(rubroSelect.getValor().setScale(2, RoundingMode.HALF_UP));
            System.out.println("valor--> " + rubroSelect.getValor());
        }
        JsfUtil.update("formValores");
    }

    public void replacePlaca(int tipo) {
        if (tipo == 1) {
            cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), Utils.getAnio(new Date()), this.aval, cobrosGenerales.getCodigoLocal(), this.clase, this.marca, this.orden));
        } else if (tipo == 2) {
            cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), this.orden, this.aval, this.clase));
        }
        if (tipo == 3) {
            cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), this.orden, this.marca, this.placa, this.aval, this.clase));
        }
        if (tipo == 4) {
            cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), this.orden, this.placa, this.clase));
        }
        if (aniosPublicos.length() > 0) {
            cobrosGenerales.setObservacion(cobrosGenerales.getObservacion() + "\n RECARGO POR ATRASO PÚBLICO: " + aniosPublicos);
//            aniosPublicos = "";
        }
        if (aniosPrivado.length() > 0) {
            cobrosGenerales.setObservacion(cobrosGenerales.getObservacion() + "\n RECARGO POR ATRASO PARTICULAR: " + aniosPrivado);
//            aniosPrivado = "";
        }
    }

    public void showDlgEntes(Integer codigo) {
        try {

            tipoSolicitante = codigo;
            if (this.ciRucCobros != null && this.ciRucCobros.length() >= 10) {
                if (this.registroEnte == null) {
                    this.registroEnte = new Cliente();
                }
                this.registroEnte.setIdentificacion(this.ciRucCobros);
                this.existeCedula();
                if (this.registroEnte != null && (this.registroEnte.getId() != null || this.registroEnte.getIdentificacion() != null)) {
                    if (this.registroEnte.getId() == null) {
                        registroEnte.setUsuarioCreacion(session.getNameUser());
                        registroEnte.setFechaCreacion(new Date());
                        registroEnte = (Cliente) manager.save(registroEnte);
                    }
                    this.cobrosGenerales.setComprador(this.registroEnte);
                } else {
                    solicitantes = new LazyModel<>(Cliente.class
                    );
                    solicitantes.getSorteds().put("apellido", "ASC");
                    JsfUtil.update("frmSolicitante");
                    JsfUtil.executeJS("PF('dlgSolicitante').show();");
                }
            } else {
                solicitantes = new LazyModel<>(Cliente.class
                );
                solicitantes.getSorteds().put("apellido", "ASC");
                JsfUtil.update("frmSolicitante");
                JsfUtil.executeJS("PF('dlgSolicitante').show();");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void cambioTipoConsulta() {
        totalCoactiva = new BigDecimal("0.00");
        emisiones = new ArrayList<>();
        pr = new CatPredioModel();
        ente = new Cliente();
        predioModel = new CatPredioModel();
    }

    public void consultarPredios() {
        try {
            switch (tipoConsulta) {
                case 1://NUMERO PREDIAL
                    if (predioModel.getNumPredio() != null && predioModel.getNumPredio().compareTo(BigInteger.ZERO) > 0) {
                        paramt = new HashMap<>();
                        paramt.put("numPredio", predioModel.getNumPredio());
                        paramt.put("estado", "A");
                        /*
                         consumir web services (CatPredio) manager.findByParameter(CatPredio.class, paramt);
                         */
                        pr = new CatPredioModel();
                        if (pr != null) {
                            this.calculoValoresCoactiva();
                        } else {
                            JsfUtil.addErrorMessage("Error", "Numero de Predio no es valido.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Numero de Predio no es valido.");
                    }
                    break;
                case 2://CONTRIBUYENTE
                    if (ente.getId() != null) {
                        propiedades = manager.getListPrediosByPropietario(ente.getId());
                        if (propiedades != null && !propiedades.isEmpty()) {
                            JsfUtil.update("formPredSel");
                            JsfUtil.executeJS("PF('selPredio').show();");
                        } else {
                            propiedades = new ArrayList<>();
                            JsfUtil.addErrorMessage("Error", "No se encontraron predios.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Debe seleccionar un contribuyente.");
                    }
                    break;
                case 3://CODIGO PREDIAL
                    if (predioModel.getSector() > 0 || predioModel.getMz() > 0 || predioModel.getCdla() > 0 || predioModel.getMzDiv() > 0 || predioModel.getSolar() > 0
                            || predioModel.getDiv1() > 0 || predioModel.getDiv2() > 0 || predioModel.getDiv3() > 0 || predioModel.getDiv4() > 0 || predioModel.getDiv5() > 0 || predioModel.getDiv6() > 0
                            || predioModel.getDiv7() > 0 || predioModel.getDiv8() > 0 || predioModel.getDiv9() > 0 || predioModel.getPhv() > 0 || predioModel.getPhh() > 0) {
                        propiedades = manager.getListPrediosByCodigoPredial(predioModel);
                        if (propiedades != null && !propiedades.isEmpty()) {
                            JsfUtil.update("formPredSel");
                            JsfUtil.executeJS("PF('selPredio').show();");
                        } else {
                            propiedades = new ArrayList<>();
                            JsfUtil.addErrorMessage("Error", "No se encontraron predios.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Error", "Codigo Predial no es valido.");
                    }
                    break;
                default:
                    JsfUtil.addErrorMessage("ERROR", "OPCION NO VALIDA.");
                    break;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void updateValoresEspecies() {
        acta.setDisponibles(acta.getDisponiblesTemp() - rubroSelect.getCantidad());
        acta.setHastaTemp(acta.getDesdeTemp() + rubroSelect.getCantidad() - 1);
        rubroSelect.setValorTotal(rubroSelect.getValor().multiply(new BigDecimal(rubroSelect.getCantidad())));
    }

    public void onRowSelect() {
        try {
            Map<String, Object> pm = new HashMap<>();
            pm.put("anio", Utils.getAnio(new Date()));
            salario = manager.findByParameter(CtlgSalario.class,
                    pm);
            if (salario == null) {
                JsfUtil.addInformationMessage("Validacion", "Debe ingresar salario básico en el módulo de Administración opción Salarios");
                rubroSelect.setValor(BigDecimal.ZERO);
                return;
            }
            if (rubroSelect.getId() != null) {
                rubroSelect = manager.find(FinaRenRubrosLiquidacion.class,
                        rubroSelect.getId());

                if (rubrosSeleccionado.contains(rubroSelect)) {
                    JsfUtil.addErrorMessage("Ya esta seleccionado el mismo rubro.", "");
                } else {
                    rubroSelect.setCantidad(1);
                    if (rubroSelect.getTipoValor().getId() == 2L) {
                        System.out.println("volor rubro>>" + rubroSelect.getValor() + "  cantidad >>" + rubroSelect.getCantidad());

                        rubroSelect.setValor(rubroSelect.getValor().multiply(new BigDecimal(rubroSelect.getCantidad())));
                    } else {

                        if ("SBU".equals(rubroSelect.getTipoValor().getPrefijo())) {
                            if (salario == null) {
                                JsfUtil.addInformationMessage("Validacion", "Debe ingresar salario básico en el módulo de Administración opción Salarios");
                                rubroSelect.setValor(BigDecimal.ZERO);
                                return;
                            }
                            FinaRenRubrosLiquidacion rubroTemp = manager.find(FinaRenRubrosLiquidacion.class,
                                    rubroSelect.getId());
                            rubroSelect.setValor(salario.getValor().setScale(2, RoundingMode.HALF_UP).multiply(rubroTemp.getValor().setScale(2, RoundingMode.HALF_UP)
                                    .divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP))));
                        } else {
                            rubroSelect.setValorCalculo(BigDecimal.ZERO);
                            rubroSelect.setCantidad(1);
                        }

                        if (rubroSelect.getValorPorcentualTemp() == null) {
                            rubroSelect.setValorPorcentualTemp(rubroSelect.getValor());
                        }
                        rubroSelect.setValorTotal(rubroSelect.getValor());
                    }
                    System.out.println("valor>>" + rubroSelect.getValor());
                    JsfUtil.update("formValores");
                    JsfUtil.executeJS("PF('dlgValorRubro').show();");
//                    }
                }
            } else {
                JsfUtil.addInformationMessage("Intente Nuevamente.", "");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardaRubro() {
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
        rubroSelect.setValorTotal(rubroSelect.getValor().multiply(new BigDecimal(rubroSelect.getCantidad())));

        if ("PCEP".equalsIgnoreCase(rubroSelect.getTipoActo())) {
            rubroSelect.setValorTotal(rubroSelect.getValorTotal().add(rubroSelect.getValor()));
        }
        if (aniosPublicos.length() > 0) {
            cobrosGenerales.setObservacion(cobrosGenerales.getObservacion() + "\n RECARGO POR ATRASO PÚBLICOS: " + aniosPublicos);
//            aniosPublicos = "";
        }
        if (aniosPrivado.length() > 0) {
            cobrosGenerales.setObservacion(cobrosGenerales.getObservacion() + "\n RECARGO POR ATRASO PARTICULARE: " + aniosPrivado);
//            aniosPrivado = "";
        }
        rubrosSeleccionado.add(rubroSelect);
        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
        JsfUtil.executeJS("PF('" + dialogo + "').hide();");
    }

    public void deleteRubrosDetalle(int indice) {
        rubrosSeleccionado.remove(indice);
        this.calcularTotalCobroGeneral();
        JsfUtil.update("mainForm:tvRecaudaciones:panelDetalle");
    }

    public void onNodeSelect() {
        String descripcion = "";
        if (selectedNode != null) {
            cobrosGenerales.setTipoLiquidacion(manager.find(FinaRenTipoLiquidacion.class,
                    ((FinaRenTipoLiquidacion) selectedNode.getData()).getId()));
            System.out.println("tipo liquidacion--> " + cobrosGenerales.getTipoLiquidacion().getDescripcion());
            System.out.println("nombre liquidacion--> " + cobrosGenerales.getTipoLiquidacion().getNombreTitulo());
            if (cobrosGenerales.getTipoLiquidacion().getDescripcion() != null && cobrosGenerales.getTipoLiquidacion().getDescripcion().length() > 0) {
                if (cobrosGenerales.getTipoLiquidacion().getTransaccionPadre() == 313L) {
                    if (cobrosGenerales.getObservacion() != null && cobrosGenerales.getObservacion().length() > 0) {
                        System.out.println("no actualiza");
                    } else {
                        cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), Utils.getAnio(new Date()), "0", "____", "CAMIONETA", "____", "____"));
                        JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
                    }
                } else {
                    cobrosGenerales.setObservacion(String.format(cobrosGenerales.getTipoLiquidacion().getDescripcion(), "____", "____", "____", "____", "____", "____", "____", "____", "____", "____"));
                    JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
                }
            } else if (cobrosGenerales.getTipoLiquidacion().getId() == 313) {
                cobrosGenerales.setObservacion(null);
                JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
            } else {
                descripcion = "%s POR...";
                cobrosGenerales.setObservacion(String.format(descripcion, cobrosGenerales.getTipoLiquidacion().getNombreTitulo()));
                JsfUtil.update("mainForm:tvRecaudaciones:panelInfoAdc");
            }
        } else {
            JsfUtil.addErrorMessage("ERROR", "ERROR");
        }
    }

    public void seleccionarLiquidacion() {
        pagoRealizado = Boolean.FALSE;
        cobrosGenerales.setSaldo(cobrosGenerales.getTotalPago());

        cobrosGenerales.setFechaIngreso(new Date());
        cobrosGenerales.setFechaCreacionOriginal(new Date());
        if (predio != null && predio.getId() != null) {
            cobrosGenerales.setPredio(predio);
            cobrosGenerales.setAvaluoConstruccion(predio.getAvaluoConstruccion());
            cobrosGenerales.setAvaluoMunicipal(predio.getAvaluoMunicipal());
            cobrosGenerales.setAvaluoSolar(predio.getAvaluoSolar());
        }
        //cobrosGenerales.setUsuarioValida(session.getNameUser());
        this.liquidacion = cobrosGenerales;

        if (this.liquidacion != null && this.liquidacion.getTipoLiquidacion() != null && this.rubrosSeleccionado != null
                && !this.rubrosSeleccionado.isEmpty()) {
            if (this.liquidacion.getComprador() != null) {
                liquidacion.getComprador().setIdentificacion(ciRucCobros);
                this.liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleto());
            }
            if ((this.liquidacion.getComprador() != null || this.liquidacion.getNombreComprador() != null) && this.liquidacion.getSaldo() != null && this.liquidacion.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
//                this.liquidacion.calcularPago();
//SE AGREGA PARA SALDO DE PAGO
                this.generarLiquidacion();
//modelPago = new PagoTituloReporteModel(this.liquidacion.getPagoFinal(), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
                JsfUtil.update("formLiq");
                JsfUtil.executeJS("PF('dlgNumeroLiquidacion').show();");
                initView();
                ActualizarListado ac = new ActualizarListado();
                ac.setActualizarValidar(true);
                eventUpdate.fire(ac);

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

    public void llenarArbol() {
        try {

            List<FinaRenTipoLiquidacion> raices = new ArrayList<>();
            List<RenTipoLiquidacionDepartamento> departamentos = manager.listaRubrosPadres(userSession.getNameUser());
            FinaRenTipoLiquidacion rtl = null;
            System.out.println("user " + userSession.getNameUser());
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

            hijos = manager.listaRubrosHijos(userSession.getNameUser(), hoja.getId());

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
        return (String) manager.find("SELECT r.descripcion FROM FinaRenRubrosLiquidacion r WHERE r.id=:idRubro", new String[]{"idRubro"}, new Object[]{idRubro});
    }

    public void calculosAdicionales() {

        interes(liquidacion);
        liquidacion.setPagoFinal(liquidacion.getTotalPago().add(liquidacion.getRecargo()).add(liquidacion.getInteres()));
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getTipoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public BigDecimal interestmp(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");

        return interes;
    }

    public BigDecimal interes2(FinaRenLiquidacion l) {
        l.setInteres(manager.interesCalculado(l, new Date()));
        return l.getInteres();
    }

    public void generarLiquidacion() {
        if (session != null && session.getNameUser() != null) {
            if (liquidacion != null && liquidacion.getId() != null) {
                esLiq = false;
                return;
            }
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);
            liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            liquidacion.setRentas(Boolean.TRUE);
            List<FinaRenDetLiquidacion> detallado = new ArrayList<>();
            for (FinaRenRubrosLiquidacion item : rubrosSeleccionado) {
                FinaRenDetLiquidacion data = new FinaRenDetLiquidacion();
                data.setCantidad(data.getCantidad());
                data.setRubro(item);
                data.setEstado(true);
                data.setValor(item.getValorTotal());
                data.setValorRecaudado(BigDecimal.ZERO);
                data.setValorSinDescuento(BigDecimal.ZERO);
                detallado.add(data);
            }
            liquidacion = liquidacionService.crearLiquidacion(liquidacion, detallado);
            JsfUtil.addInformationMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " genrada con exito");

//            liquidacion.calcularPago();

            /*COBRO PARA LIQUDACIONES DE COACTIVA*/
 /*if (liquidacion.getTipoLiquidacion().getId() == 49L) { // TIPO LIQUIDACION COACTIVA
liquidacion = manager.grabaLiquidacionRubro(liquidacion, detalle);
} else {
liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);
liquidacion = manager.grabarLiquidacion(liquidacion);
}*/
        }
    }

    /*METODO MODIFICADO PARA LOS COMPROBANTE GENERALES*/
    public void generarComprobante(FinaRenPago p) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarParametros();
            ss.instanciarParametros();
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/Emision/"));
            ss.addParametro("//LOGO", JsfUtil.getRealPath(SisVars.logoReportes));
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
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "DOCUMENTO");
        } catch (Exception e) {
            //LOG.log(Level.OFF, null, e);
        }
    }

    public void validarClaveOtroCanton() {
        predio = new CatPredio();
        if (cobrosGenerales.getTipoLiquidacion() == null) {
            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar el tipo de liquidación a realizar");
            return;
        }
        try {
            if (predioModel.getProvincia() > 0 && predioModel.getCanton() > 0
                    && predioModel.getParroquiaShort() > 0 && predioModel.getZona() > 0 && predioModel.getSector() > 0
                    && predioModel.getMz() > 0 && predioModel.getLote() > 0) {

            } else {
                JsfUtil.addWarningMessage("Advertencia", "Verifique los datos ingresados");
            }

            JsfUtil.update("frmAlcPlus");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void consultar() {

        try {
            CatPredio temp = null;
            temp = consultar(tipoCons, predio);
            if (temp != null) {
                predio = temp;
                if (predio.getCatPredioPropietarioList() != null && predio.getCatPredioPropietarioList().size() == 1) {
                    cobrosGenerales.setComprador(Utils.get(predio.getCatPredioPropietarioList(), 0).getEnte());
                    setCiRucCobros(cobrosGenerales.getComprador().getIdentificacionCompleta());
                    ente2 = new Cliente();
                    ente2 = cobrosGenerales.getComprador();
                }
                cobrosGenerales.setUbicacion(predio.getDireccion());
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.error, MessagesRentas.predioNoEncontrado);
            }
            JsfUtil.update("mainForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CatPredio consultar(Integer tipoCons, CatPredio pred) {
        CatPredio temp = new CatPredio();
        CatPredio predio = pred;
        switch (tipoCons) {
            case 1: // Codigo Anterior

                break;
            case 2: // Codigo Nuevo
                System.out.println("estructura predial");
                if (predio.getSector() > 0 || predio.getMz() > 0 || predio.getProvincia() > 0 || predio.getCanton() > 0
                        || predio.getParroquia() > 0 || predio.getZona() > 0 || predio.getSolar() > 0 || predio.getPiso() >= 0
                        || predio.getUnidad() >= 0 || predio.getBloque() >= 0) {
                    Map<String, Object> paramtr = new HashMap<>();
                    paramtr.put("estado", "A");
                    paramtr.put("sector", predio.getSector());
                    paramtr.put("mz", predio.getMz());
                    paramtr.put("provincia", predio.getProvincia());
                    paramtr.put("canton", predio.getCanton());
                    paramtr.put("parroquia", predio.getParroquia());
                    paramtr.put("zona", predio.getZona());
                    paramtr.put("solar", predio.getSolar());
                    paramtr.put("piso", predio.getPiso());
                    paramtr.put("unidad", predio.getUnidad());
                    paramtr.put("bloque", predio.getBloque());
                    if (esUrbano == "1") {
                        paramtr.put("tipoPredio", "U");
                    } else {
                        paramtr.put("tipoPredio", "R");
                    }
                    temp = service.findByParameter(CatPredio.class,
                            paramtr);
                }
                break;

            case 3:// Numero de Predio

                if (predio.getNumPredio() != null) {
                    System.out.println("num predio");
                    temp = service.getPredioNumPredio(predio.getNumPredio().longValue(), esUrbano);
                }

                break;
            case 4:
                if (predio.getClaveCat() != null) {
                    System.out.println("clave catastral");
                    temp = service.getPredioByClaveCat(predio.getClaveCat(), esUrbano);
                }

                break;
            case 5:// Clave anterior
                if (predio.getPredialant() != null) {
                    temp = service.getPredioByClaveCatAnt(predio.getPredialant(), esUrbano);
                }

                break;

        }
        if (temp != null) {
            JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.predioEncontrado + temp.getClaveCat());
            return temp;
        } else {
            return null;
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
                cobrosGenerales.setTotalPago(cobrosGenerales.getTotalPago().add(r.getValorTotal()));
            }
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar el rubro");
        }
    }

    public void actualizarLiquidacion() {
        manager.update(liquidacion);
    }

    //Método que contiene los métodos de generar la liquidación y el pago
    public void pago() {
        try {
            if (this.liquidacion != null && this.modelPago != null && this.liquidacion.getTipoLiquidacion().getId().equals(181L) && this.liquidacion.getSaldo().compareTo(this.modelPago.getValorTotal()) > 0) {
                JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser igual a la recaudación");
                return;
            }
            this.generarLiquidacion();
            this.realizarPago();
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pagarLiquidacion(FinaRenLiquidacion liq) {
        this.liquidacion = liq;
        this.procesarPago();
    }

    public void procesarPago() {
        try {
            pagoRealizado = Boolean.FALSE;
            //modelPago = new PagoTituloReporteModel();
            if (this.liquidacion != null) {
                List<RenParametrosInteresMulta> parametrosInteresMultas = manager.getListParametrosInteresMulta(liquidacion);
                if (parametrosInteresMultas != null && !parametrosInteresMultas.isEmpty()) {//VERIFICAR SI EMITE MULTA-INTERES
                    for (RenParametrosInteresMulta interesMulta : parametrosInteresMultas) {
                        if (interesMulta.getTipo().equalsIgnoreCase("I")) {
                            Calendar fecha = Calendar.getInstance();
                            fecha.set(Calendar.DAY_OF_MONTH, interesMulta.getDia().intValue());
                            fecha.set(Calendar.MONTH, interesMulta.getMes().intValue() - 1);
                            fecha.set(Calendar.YEAR, liquidacion.getAnio());
                            liquidacion.setInteres(manager.generarInteres(liquidacion.getSaldo(), fecha.getTime(), null));
                        }
                        if (interesMulta.getTipo().equalsIgnoreCase("M")) {
                            liquidacion.setRecargo(manager.generarMultas(liquidacion, interesMulta));
                        }
                    }
                }
                this.liquidacion.calcularPago();
                modelPago = new PagoTituloReporteModel(this.liquidacion.getPagoFinal(), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
    }

    public void realizarPago() {
        FinaRenPago p;
        try {
            if (modelPago.getValorTotal().compareTo(liquidacion.getPagoFinal()) <= 0) {
                if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                    liquidacion.calcularPago();
                    //liquidacion=manager.realizarPago(liquidacion, modelPago.realizarPago(liquidacion), cajero);
                    p = manager.realizarPago(liquidacion, modelPago.realizarPago(liquidacion), cajero, true);

                    if (esLiq) {
                        liquidacion.setNumLiquidacion(new BigInteger(p.getNumComprobante() + ""));
                        liquidacion.setNumComprobante(new BigInteger(p.getNumComprobante() + ""));
                        liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-" + p.getNumComprobante());
                        manager.save(liquidacion);
                    }
                    esLiq = true;
                    if (p != null) {
                        modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
                        pagoRealizado = Boolean.TRUE;
                        rubrosSeleccionado = new ArrayList<>();
                        generarComprobante(p);
                        onNodeSelect();
                        this.cobrosGenerales.setTotalPago(null);
                        //this.cobrosGenerales.setComprador(null);
                        //this.cobrosGenerales.setObservacion(null);
                        this.cobrosGenerales.setNumLiquidacion(null);
                        liquidacion = null;
                        //this.ciRucCobros=null;
                        //this.cobrosGenerales.setNombreComprador(null);
                    }
                } else {
                    JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
            } else {
                JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor al de la Recaudacion");
            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
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

    public void anularTituloReporte() {
        try {
            if (this.liquidacion != null) {
                this.liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
                this.liquidacion.setUsuarioAnular(session.getNameUser());
                manager.update(this.liquidacion);

            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
    }

    public void anularLiquidacion() {
//        if (documento == null) {
//            JsfUtil.addErrorMessage( "Información", "Debe Subir un Documento Para Continuar");
//            return;
//        }
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addErrorMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        if (liquidacionAnular.getObservacion() != null) {
            liquidacionAnular.setObservacion(liquidacionAnular.getObservacion() + "; ANULACION: " + observacion);
        } else {
            liquidacionAnular.setObservacion("ANULACION: " + observacion);
        }
        if (liquidacionAnular.getIpUserSession() == null || liquidacionAnular.getIpUserSession().equals("")) {
            liquidacionAnular.setIpUserSession(session.getIpClient());
        } else {
            liquidacionAnular.setIpUserSession(liquidacionAnular.getIpUserSession() + " - ANULA" + session.getIpClient());
        }
        if (liquidacionAnular.getMacAddresUsuarioIngreso() == null || liquidacionAnular.getMacAddresUsuarioIngreso().equals("")) {
            liquidacionAnular.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        } else {
            liquidacionAnular.setMacAddresUsuarioIngreso(liquidacionAnular.getMacAddresUsuarioIngreso() + " - ANULA" + session.getMACAddressEquipo());
        }
        liquidacionAnular.setUsuarioAnular(session.getNameUser());
        liquidacionAnular.setFechaAnulacion(new Date());
        liquidacionAnular.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));

        manager.update(liquidacionAnular);

        for (Documentos doc : listaDocumentos) {
            manager.save(doc);
        }

        emisiones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();
        JsfUtil.addInformationMessage("ANULADO", "Liquidacion : " + liquidacionAnular.getIdLiquidacion() + " anulada con exito");

        JsfUtil.executeJS("PF('dlgAnular').hide()");
    }

    public void openDialogAnularLiquidacion(FinaRenLiquidacion liq) {
        //geDocumentosList = new ArrayList();
        liquidacionAnular = new FinaRenLiquidacion();
        liquidacionAnular = liq;
        documento = new Documentos();
        listaDocumentos = new ArrayList<>();
        observacion = null;
        System.out.println("->" + liquidacionAnular.toString());

    }

    public void viewDocumento(FinaRenLiquidacion r) throws ClassNotFoundException {
        original = new FinaRenLiquidacion();
        original = r;
        documentoDescargar = new Documentos();
        documentoDescargar = (Documentos) manager.documentoGestionTribtaria(r.getClass().getPackage().getName() + "." + r.getClass().getSimpleName(), r.getId());
        System.out.println("documentoDescargar " + documentoDescargar.getRutaDocumento());

    }

    public void handleFileDocumentBySave(FileUploadEvent event) {
        try {

            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            documento = new Documentos();
            documento.setDepartamento(session.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(event.getFile().getFileName());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setEstado(Boolean.TRUE);

            documento.setClaseNombre(liquidacionAnular.getClass().getPackage().getName() + "." + liquidacionAnular.getClass().getSimpleName());
            documento.setIdentificador(liquidacionAnular.getId());

            listaDocumentos.add(documento);

            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subri el documento");
        }
    }

    public void inactivarDocumento() {
//        try {
//            documentos = catas.saveInactivarDocumentos(documentos);
//            if (!documentos.getEstado()) {
//                JsfUtil.addInformationMessage("Exito!", "Datos Registrados Correctamente");
//                geDocumentosList.remove(documentos);
//            } else {
//                JsfUtil.addErrorMessage("", "Error al Eliminar los Archivos");
//            }
//        } catch (Exception e) {
//            //Logger.get//Logger(LiquidacionGeneral.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    public void enviarConvenio(FinaRenLiquidacion liq) {

        // if (liq.getTipoLiquidacion().getCodigoTituloReporte() == 260 || liq.getTipoLiquidacion().getCodigoTituloReporte() == 261) {
        //     JsfUtil.addErrorMessage("Convenio", "Liquidacion : " + liq.getIdLiquidacion() + " forma parte de los convenios de pago, no puede ser procesada.");
        //     return;
        // }
        System.out.println("enviando a convenio");
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(6L));
        manager.update(liq);

        emisiones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();

        JsfUtil.addInformationMessage("Convenio", "Liquidacion : " + liq.getIdLiquidacion() + " enviada a convenio de pago.");

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

    public void seleccionaPredio(Long pred) {
        CatPredioModel predio = manager.buscarPredio(pred);

        pr = predio;
        this.calculoValoresCoactiva();
        JsfUtil.addInformationMessage("Mensaje", "No de Predio: " + pr.getNumPredio());
    }

    public void calculoValoresCoactiva() {
        try {
            paramt = new HashMap<>();
            paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
            paramt.put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
            paramt.put("predio", pr);
            paramt.put("estadoCoactiva", 2);
            emisiones = manager.findAll(FinaRenLiquidacion.class,
                    paramt);
            desdeTemp = (Integer) manager.findNativeQuery("\"select min(anio) from asgard.fina_ren_liquidacion where tipo_liquidacion = 13 and estado_liquidacion = 2 and estado_coactiva = 2 and predio = ?", new Object[]{pr.getId()});
            totalCoactiva = new BigDecimal("0.00");
            if (emisiones != null && !emisiones.isEmpty()) {
                for (FinaRenLiquidacion e : emisiones) {
                    e = manager.realizarDescuentoRecargaInteresPredial(e, null);
                    e.calcularPago();
                    totalCoactiva = totalCoactiva.add(e.getValorCoactiva());
                }
            } else {
                emisiones = new ArrayList<>();
                JsfUtil.addInformationMessage("Mensaje", "El predio seleccionado no tiene emisiones prediales en coactiva.");
            }
            JsfUtil.update("mainForm:tvRecaudaciones:dtEmisionesCoactiva");
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
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
                FinaRenRubrosLiquidacion rubro = manager.findByParameter(FinaRenRubrosLiquidacion.class,
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
                detalle.setRubro(rubro);
                detalle.setValor(totalCoactiva);
                detalle.setCantidad(cantidad);
                detalle.setDesde(new BigInteger(desdeTemp.toString()));
                detalle.setHasta(new BigInteger(liqSelect.getAnio().toString()));
                JsfUtil.update("formProcesar");
                JsfUtil.executeJS("PF('dlgProcesar').show();");
            }
        } catch (Exception e) {
            //LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporteParteRentas() {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.instanciarParametros();
            //  ss.setTieneDatasource(Boolean.TRUE);
            ss.setNombreSubCarpeta("FinancieroMontecristi/Partes");
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/FinancieroMontecristi/Partes/"));
            ss.addParametro("//LOGO", JsfUtil.getRealPath(SisVars.logoReportes));
            ss.addParametro("//LOGO_HOJA", JsfUtil.getRealPath(SisVars.logoReportes));
            ss.addParametro("FECHA", sdf.format(fechaReporte));

            switch (this.tipoReporte.intValue()) {
                /*PARTE RENTAS JC */
                case 1:
                    ss.setNombreReporte("sParteGeneral");
                    ss.addParametro("DESDE", fechaDesde);
                    System.out.println("DESDE " + fechaDesde);
                    ss.addParametro("HASTA", fechaHasta);
                    System.out.println("HASTA " + fechaHasta);

                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 2:
                    ss.setNombreReporte("sParteAlcabalas");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 3:
                    ss.setNombreReporte("sPartePlusvalias");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 4:
                    ss.setNombreReporte("sParteActivosTotales");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 5:
                    ss.setNombreReporte("sPartePatenteEmpresarial");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 6:
                    ss.setNombreReporte("sParteAdjudicacion");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 7:
                    ss.setNombreReporte("sParteAguaPotable");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 8:
                    ss.setNombreReporte("sPartePlaneamiento");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 9:
                    ss.setNombreReporte("sParteCementerio");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 10:
                    ss.setNombreReporte("sParteArriendoMercado");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 11:
                    ss.setNombreReporte("sParteHigieneAmbiental");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 12:
                    ss.setNombreReporte("sParteVallasPublicitarias");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 13:
                    ss.setNombreReporte("sParteExedente");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 14:
                    ss.setNombreReporte("sParteTurismo");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 15:
                    ss.setNombreReporte("sPartePatenteMunicipal");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 16:
                    ss.setNombreReporte("sPermisoFuncionamiento");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 17:
                    ss.setNombreReporte("sPartePatenteMinera");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 18:
                    ss.setNombreReporte("sParteConvenioPagos");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 19:
                    ss.setNombreReporte("sPermisoArrendamiento");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    break;

                case 20:
                    ss.setNombreReporte("sParteServicioAdministrativos");
                    ss.addParametro("DESDE", fechaDesde);
                    ss.addParametro("HASTA", fechaHasta);
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;
                case 21:
                    ss.setNombreReporte("sPartePatenteApertura");
                    ss.addParametro("DESDE", sdf.format(fechaDesde));
                    ss.addParametro("HASTA", sdf.format(fechaHasta));
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;

                case 22:
                    ss.setNombreReporte("sParteRegistroPropiedad");
                    ss.addParametro("DESDE", fechaDesde);
                    ss.addParametro("HASTA", fechaHasta);
                    if (fechaCierre != null) {
                        ss.addParametro("FECHA_CIERRE", fechaCierre);
                    }
                    break;
                default:
                    return;
            }
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } catch (Exception e) {
            //LOG.log(Level.OFF, null, e);
        }
    }

    public void actualizarEnte() {
        registroEnte = new Cliente();
    }

    public void buscarEnte() {
        try {
            if (ciRucCobros != null) {
                Map<String, Object> pm = new HashMap<>();
                pm.put("identificacion", ciRucCobros);
                Cliente ve = this.manager.findByParameter(Cliente.class,
                        pm);
                if (ve != null) {
                    cobrosGenerales.setComprador(ve);
                    ente2 = new Cliente();
                    ente2 = ve;
                } else {
                    cobrosGenerales.setComprador(new Cliente());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar cliente" + e.getMessage());
            cobrosGenerales.setComprador(new Cliente());
            JsfUtil.addInformationMessage("Advertencia", "CLIENTE NO HAYADO");
        }
    }

    public void existeCedula() {

        String identificacion = registroEnte.getIdentificacion();
        if (registroEnte.getIdentificacion() != null && registroEnte.getIdentificacion().length() > 0) {
            registroEnte = (Cliente) manager.find("select c from Cliente c where c.identificacion= :ciRuc", new String[]{"ciRuc"}, new Object[]{identificacion});
            if (registroEnte == null) {
                registroEnte = new Cliente();
                registroEnte.setIdentificacion(identificacion);
                if (tipoEnte == 1 || this.ciRucCobros != null) {

                }
            }
        } else {
            registroEnte = new Cliente();
        }
    }

    public void guardarEnte() {
        try {
//            VerCedulaUtils validacion = new VerCedulaUtils();
            paramt = new HashMap<>();
            Boolean esExcepcional = false;

            if (tipoEnte == 1) {
                if (registroEnte.getIdentificacion() == null || registroEnte.getApellido() == null || registroEnte.getNombre() == null) {
                    JsfUtil.addInformationMessage("Llene los campos", "");
                    return;
                }
                paramt.put("identificacion", registroEnte.getIdentificacion());
                if (manager.findByParameter(Cliente.class,
                        paramt) != null) {
                    JsfUtil.addInformationMessage("El numero identicacion no existe", "");
                }
                if (registroEnte.getIdentificacion().length() != 10) {
                    JsfUtil.addInformationMessage("el numero de identificación es invalida", "");
                    return;
                }
                registroEnte.setTipoProv(new CatalogoItem(10L));
            }
            if (tipoEnte == 2) {
                if (registroEnte.getIdentificacion() == null || registroEnte.getRazonSocial() == null) {
                    JsfUtil.addInformationMessage("Llene los campos por favor", "");
                    return;
                }
                paramt.put("identificacion", registroEnte.getIdentificacion());
                if (manager.findByParameter(Cliente.class,
                        paramt) != null) {
                    JsfUtil.addInformationMessage("El numero de identificacion ya existe", "");
                }
                if (registroEnte.getIdentificacion().length() != 13) {
                    JsfUtil.addInformationMessage("El numero de identifacion es invalida", "");
                    return;
                }
                registroEnte.setTipoProv(new CatalogoItem(11L));
            }
            if (tipoEnte == 3) {
                esExcepcional = true;
                if (excepcionalEmpresa) {
                    if (registroEnte.getRazonSocial() == null) {
                        JsfUtil.addInformationMessage("Llene los campos", "");
                        return;
                    }
                    //liqSelect.setNombreComprador(registroEnte.getRazonSocial());
                    cobrosGenerales.setNombreComprador(registroEnte.getRazonSocial());
                } else {
                    if (registroEnte.getApellido() == null || registroEnte.getNombre() == null) {
                        JsfUtil.addInformationMessage("Llene los campos", "");
                        return;
                    }
                    //liqSelect.setNombreComprador(registroEnte.getNombres() + " " + registroEnte.getApellidos());
                    cobrosGenerales.setNombreComprador(registroEnte.getNombre() + " " + registroEnte.getApellido());
                }
                JsfUtil.addInformationMessage("Seleccionado correctamente", "");
                JsfUtil.executeJS("PF('dlgNewClient').hide();");
                JsfUtil.update("mainForm");
                JsfUtil.executeJS("PF('dlgSolicitante').hide();");
            }

            if (registroEnte != null && registroEnte.getId() == null && !esExcepcional) {
                registroEnte = (Cliente) manager.save(registroEnte);
            }
            if (registroEnte != null && !esExcepcional) {
                JsfUtil.addInformationMessage("Registro Grabado", "");
                cobrosGenerales.setComprador((Cliente) manager.find("select e from Cliente e where e.identificacion = :ciRuc", new String[]{"ciRuc"}, new Object[]{registroEnte.getIdentificacion()}));
                JsfUtil.executeJS("PF('dlgNewClient').hide();");
                seleccionar();
            }
        } catch (Exception e) {
            //LOG.log(Level.OFF, null, e);
        }
    }

    public void validarLiquidacion(FinaRenLiquidacion liq) {
        liq.setValidada(Boolean.TRUE);
        liq.setUsuarioValida(session.getNameUser());
        liq.setLiquidadorAprobador(clienteService.getUsuarioNombre(session.getNameUser()));
        manager.update(liq);
        JsfUtil.addInformationMessage("Liquidacion " + liq.getIdLiquidacion() + " validada con exito.", "");
    }

    public boolean renderImprimir(FinaRenLiquidacion liq) {
        if (liq.getTipoLiquidacion().getPrefijo() != null) {
            if (liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("PLU") || liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("ALC")
                    || liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("PAT") || liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("MIL")
                    || liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("PFU") || liq.getTipoLiquidacion().getPrefijo().equalsIgnoreCase("ADJ")) {
                return true;
            }
        }
//        if (liq.getCatPlanificacionTitulos() != null) {
//            return true;
//        }
        return false;
    }

    public Boolean mostrarValidar() {
//        if (session.getIsAdmin()) {
//            return true;
//        } else {
//            if (session.getRoles().contains(director_renta) || session.getRoles().contains(asistente_renta)) {
//                return true;
//            }
//        }
        return false;
    }

    /*
     *
     *CALCULO DE REMISION DE INTERESES
     */
    public void openDlgRemisionInteres(FinaRenLiquidacion rl) {
        if (rl.getAnio() <= 2017) {
            if (rl.getComprador() != null) {
                List<FinaRenLiquidacion> emisionesPrediales = new ArrayList<>();
                emisionesPrediales.add(rl);
                Map<String, List<String>> params = new HashMap<>();
                List<String> p = new ArrayList<>();

                p = new ArrayList<>();
                //p.add(manager.saveFnRemisionSolicitudProceso(emisionesPrediales).getId().toString());
                params.put("fnRemisionSolicitudId", p);

                Map<String, Object> options = new HashMap<>();
                options.put("resizable", true);
                options.put("draggable", true);
                options.put("modal", true);
                options.put("closable", true);
                PrimeFaces.current().dialog().openDynamic("dlgRemisionInteres", options, params);
            } else {
                JsfUtil.addErrorMessage("Debe Actualizar los Datos del Propietario", null);
            }
        } else {
            JsfUtil.addErrorMessage("No aplica para remision de Intereses", null);
        }

    }

    public void procesarRemision(SelectEvent event) {
        FnSolicitudExoneracion frs = (FnSolicitudExoneracion) event.getObject();
        if (frs != null) {
            if (frs.getId() != null) {
                JsfUtil.addInformationMessage("Info", "Datos Guardados Correctamente");
                //JsfUtil.closeDialog(frs);
            } else {
                JsfUtil.addErrorMessage("Info", "Ocurrio un Problema Mientras Se persistian los datos");
            }
        } else {
            JsfUtil.addErrorMessage("Info", "Ocurrio un Problema Mientras Se persistian los datos");
        }
    }

    public Boolean getRenderEditCliente() {
        return renderEditCliente;
    }

    public void setRenderEditCliente(Boolean renderEditCliente) {
        this.renderEditCliente = renderEditCliente;
    }

    public Event<ActualizarListado> getEventUpdate() {
        return eventUpdate;
    }

    public void setEventUpdate(Event<ActualizarListado> eventUpdate) {
        this.eventUpdate = eventUpdate;
    }

    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
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

    public PagoTituloReporteModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoTituloReporteModel modelPago) {
        this.modelPago = modelPago;
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

    public Boolean getEsLiq() {
        return esLiq;
    }

    public void setEsLiq(Boolean esLiq) {
        this.esLiq = esLiq;
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

    public LazyModel<FinaRenPatente> getClienteLazy() {
        return clienteLazy;
    }

    public void setClienteLazy(LazyModel<FinaRenPatente> clienteLazy) {
        this.clienteLazy = clienteLazy;
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

    public FinaRenTipoLiquidacion getTipoReporteLiqui() {
        return tipoReporteLiqui;
    }

    public void setTipoReporteLiqui(FinaRenTipoLiquidacion tipoReporteLiqui) {
        this.tipoReporteLiqui = tipoReporteLiqui;
    }

    public List<FinaRenTipoLiquidacion> getTipoReporteLiquiList() {
        return tipoReporteLiquiList;
    }

    public void setTipoReporteLiquiList(List<FinaRenTipoLiquidacion> tipoReporteLiquiList) {
        this.tipoReporteLiquiList = tipoReporteLiquiList;
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

    public Date getFechaDesde1() {
        return fechaDesde1;
    }

    public void setFechaDesde1(Date fechaDesde1) {
        this.fechaDesde1 = fechaDesde1;
    }

    public Date getFechaHasta1() {
        return fechaHasta1;
    }

    public void setFechaHasta1(Date fechaHasta1) {
        this.fechaHasta1 = fechaHasta1;
    }

    public FinaRenLiquidacion getLiquidacionAnular() {
        return liquidacionAnular;
    }

    public void setLiquidacionAnular(FinaRenLiquidacion liquidacionAnular) {
        this.liquidacionAnular = liquidacionAnular;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAniosPrivado() {
        return aniosPrivado;
    }

    public void setAniosPrivado(String aniosPrivado) {
        this.aniosPrivado = aniosPrivado;
    }

    public String getAniosPublicos() {
        return aniosPublicos;
    }

    public void setAniosPublicos(String aniosPublicos) {
        this.aniosPublicos = aniosPublicos;
    }

    public BigDecimal getAval() {
        return aval;
    }

    public void setAval(BigDecimal aval) {
        this.aval = aval;
    }

    public CtlgSalario getSalario() {
        return salario;
    }

    public void setSalario(CtlgSalario salario) {
        this.salario = salario;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }

    public Integer getTipoCons() {
        return tipoCons;
    }

    public void setTipoCons(Integer tipoCons) {
        this.tipoCons = tipoCons;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public String getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(String esUrbano) {
        this.esUrbano = esUrbano;
    }

    public String getNumLiquidacionReport() {
        return numLiquidacionReport;
    }

    public void setNumLiquidacionReport(String numLiquidacionReport) {
        this.numLiquidacionReport = numLiquidacionReport;
    }

    public LazyModel<FinaRenLocalComercial> getLazyLocal() {
        return lazyLocal;
    }

    public void setLazyLocal(LazyModel<FinaRenLocalComercial> lazyLocal) {
        this.lazyLocal = lazyLocal;
    }

    public FinaRenLocalComercial getLocalSel() {
        return localSel;
    }

    public void setLocalSel(FinaRenLocalComercial localSel) {
        this.localSel = localSel;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getClaveCatCon() {
        return claveCatCon;
    }

    public void setClaveCatCon(String claveCatCon) {
        this.claveCatCon = claveCatCon;
    }

    public String getObservacionT() {
        return observacionT;
    }

    public void setObservacionT(String observacionT) {
        this.observacionT = observacionT;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getTextoBusquedaConvenio() {
        return textoBusquedaConvenio;
    }

    public void setTextoBusquedaConvenio(String textoBusquedaConvenio) {
        this.textoBusquedaConvenio = textoBusquedaConvenio;
    }

    public Integer getCasoConvenio() {
        return casoConvenio;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public UsuarioService getUserService() {
        return userService;
    }

    public void setUserService(UsuarioService userService) {
        this.userService = userService;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
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

    public FnConvenioPagoDetallerService getCv() {
        return cv;
    }

    public void setCv(FnConvenioPagoDetallerService cv) {
        this.cv = cv;
    }

    public CajeroService getCajeroService() {
        return cajeroService;
    }

    public void setCajeroService(CajeroService cajeroService) {
        this.cajeroService = cajeroService;
    }

    public FinaRenLiquidacionService getLiquidacionService() {
        return liquidacionService;
    }

    public void setLiquidacionService(FinaRenLiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    public String getSss() {
        return sss;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public LiquidacionGeneralMB getConsulP() {
        return consulP;
    }

    public void setConsulP(LiquidacionGeneralMB consulP) {
        this.consulP = consulP;
    }

    public FinaRenLiquidacion getOriginal() {
        return original;
    }

    public void setOriginal(FinaRenLiquidacion original) {
        this.original = original;
    }

    public Documentos getDocumentoDescargar() {
        return documentoDescargar;
    }

    public void setDocumentoDescargar(Documentos documentoDescargar) {
        this.documentoDescargar = documentoDescargar;
    }

    public GenerarConvenioPago getGc() {
        return gc;
    }

    public void setGc(GenerarConvenioPago gc) {
        this.gc = gc;
    }

    public Integer getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(Integer tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public List<FinaRenEstadoLiquidacion> getEstadosLiquidaciones() {
        return estadosLiquidaciones;
    }

    public void setEstadosLiquidaciones(List<FinaRenEstadoLiquidacion> estadosLiquidaciones) {
        this.estadosLiquidaciones = estadosLiquidaciones;
    }

    public boolean isDetallado() {
        return detallado;
    }

    public void setDetallado(boolean detallado) {
        this.detallado = detallado;
    }

    public FinaRenEstadoLiquidacion getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(FinaRenEstadoLiquidacion estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public void setCasoConvenio(Integer casoConvenio) {
        this.casoConvenio = casoConvenio;
    }

    public Cliente getEnte2() {
        return ente2;
    }

    public void setEnte2(Cliente ente2) {
        this.ente2 = ente2;
    }
//</editor-fold>

    public Boolean getAbrirTipoP() {
        return abrirTipoP;
    }

    public void setAbrirTipoP(Boolean abrirTipoP) {
        this.abrirTipoP = abrirTipoP;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Integer getEleccion2() {
        return eleccion2;
    }

    public void setEleccion2(Integer eleccion2) {
        this.eleccion2 = eleccion2;
    }

    public Integer getEleccion() {
        return eleccion;
    }

    public void setEleccion(Integer eleccion) {
        this.eleccion = eleccion;
    }

    public List<Integer> getEstadosConvenios() {
        return estadosConvenios;
    }

    public void setEstadosConvenios(List<Integer> estadosConvenios) {
        this.estadosConvenios = estadosConvenios;
    }

    public Boolean getAbrirRubro() {
        return abrirRubro;
    }

    public void setAbrirRubro(Boolean abrirRubro) {
        this.abrirRubro = abrirRubro;
    }

    public FinaRenPatente getClient() {
        return client;
    }

    public void setClient(FinaRenPatente client) {
        this.client = client;
    }

    public FinaRenRubrosLiquidacion getRubroLi() {
        return rubroLi;
    }

    public void setRubroLi(FinaRenRubrosLiquidacion rubroLi) {
        this.rubroLi = rubroLi;
    }

    protected List<FinaRenTipoLiquidacion> tipoReporteLiquiList;
    protected List<FinaRenRubrosLiquidacion> rubroReporteList;

    public List<FinaRenRubrosLiquidacion> getRubroReporteList() {
        return rubroReporteList;
    }

    public void setRubroReporteList(List<FinaRenRubrosLiquidacion> rubroReporteList) {
        this.rubroReporteList = rubroReporteList;
    }
}
