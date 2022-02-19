/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.activos.service.DepreciacionService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.arrendamiento.entities.OrdenesEmitidas;
import com.origami.sigef.arrendamiento.service.OrdenesEmitidasService;
import com.origami.sigef.ats.entities.AtsArchivo;
import com.origami.sigef.ats.service.AtsArchivoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.RetencionesRegistradas;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CobrosEmisionesModel;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DetalleTransferenciasService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.GarantiaService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.RetencionesRegistradasService;
import com.origami.sigef.talentohumano.services.CuotaAnticipoService;
import com.origami.sigef.talentohumano.services.TipoRolBeneficiosService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.service.CorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.RecaudacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "diarioGeneralPeriodoView")
@ViewScoped
public class DiarioGeneralPeriodoAnteriorController implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private CorteOrdenCobroService corteOrdenCobroService;
    @Inject
    private RecaudacionService recaudacionService;
    @Inject
    private ProcesoIngresoService inventarioService;
    @Inject
    private BeneficiarioComprobantePagoService beneficiarioComprobantePagoService;
    @Inject
    private DetalleComprobantePagoService detalleComprobantePagoService;
    @Inject
    private DetalleTransferenciasService detalleTransferenciaService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private BeneficiarioSolicitudReservaService beneficiarioSolicitudReservaService;
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private TipoRolBeneficiosService tipoRolBeneficiosService;
    @Inject
    private PartidaDistributivoService partidaDistributivaService;
    @Inject
    private PartidaDistributivoAnexoService partidaDistributivoAnexoService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private RetencionesRegistradasService retencionesRegistradasService;
    @Inject
    private GarantiaService garantiasService;
    @Inject
    private AtsArchivoService atsArchivoService;
    @Inject
    private MasterCatalogoService periodoService;
    @Inject
    private CuotaAnticipoService cuotaAnticipoService;
    @Inject
    private OrdenesEmitidasService ordenesEmitidasService;
    @Inject
    private DepreciacionService depreciacionService;
    @Inject
    private BienesMovimientoService bienesMovimientoService;


    /*OBJECTOS*/
    private OpcionBusqueda opcionBusqueda;
    private DiarioGeneral diarioGeneral;
    private CatalogoItem moduloDeEnlaceSeleccionado;
    private Recaudacion recaudacionSelecionada;
    private CorteOrdenCobro corteOrdenCobroSeleccionada;
    private CuentaContable cuentaContableSeleccionado;
    private CatalogoMovimiento catalogoMovimientoSeleccionado;
    private DetalleTransferencias transferenciaAnuladaSeleccionada;
    private TipoRol tipoRolSeleccionado;
    private TipoRolBeneficios tipoRolBeneficiosSeleccionado;
    private SolicitudReservaCompromiso reservaCompromiso;
    private Proveedor proveedorSeleccionado;
    private CatalogoItem tipoSeleccionado;
    private DetalleTransaccion detalleTransaccion;
    private Presupuesto presupuestoSeleccionado;
    private CatalogoItem subEnlaceSeleccionado;
    private Adquisiciones adquisicion;
    private CatalogoItem mesSeleccionado;
    private Depreciacion depreciacion;

    /*LISTAS*/
    private List<DetalleTransaccion> detalleDiarioGeneral;
    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<CatalogoItem> modulosDeEnlaces;
    private List<CatalogoMovimiento> motivoMovimientosList;
    private List<Inventario> inventariosSeleccionadoList;
    private List<TipoRolBeneficios> rolesBeneficiosList;
    private List<TipoRol> rolesList;
    private List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList;
    private List<CuentaContablePresupuestoModel> cuentasConPartida;
    private List<BeneficiarioSolicitudReserva> beneficiarioReservaCompromisoList;
    private List<SolicitudReservaCompromiso> reservaCompromisoList;
    private List<CatalogoItem> tiposAdquisiciones;
    private List<CatalogoPresupuesto> filtroPartidas;
    private List<CuentaContable> relacionCuentasContables;
    private List<DetalleTransaccion> partidaEstructura;
    private List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas;
    private List<Presupuesto> presupuestoRelacionado;
    private List<CatalogoItem> subEnlace;
    private List<String> periodosFiscales;
    private List<String> tipoRetenciones;
    private List<LiquidacionDetalle> retencionesSeleccionadas;
    private List<LiquidacionDetalle> liquidacionDetalleList;
    private List<RetencionesRegistradas> retencionesRegistradasList;
    private List<Garantias> garantiasList;
    private List<Garantias> garantiasSeleccionadasList;
    private List<MasterCatalogo> listaPeriodo;
    private List<DetalleTransaccion> detalleDiarioGeneralEliminar;
    private List<Adquisiciones> adquisicionesList;
    private List<DetalleSolicitudCompromiso> detalleReservaList;
    private List<OrdenesEmitidas> ordenesEmitidasList;
    private List<CatalogoItem> mesesArriendoList;
    private List<Depreciacion> depreciacionList;
    private List<BienesMovimiento> bienesListSeleccionados;

    /*CONTROLADORES DE BOTONES*/
    private Boolean formDiarioGeneral;
    private Boolean botonCuentasContables;
    private Boolean botonNuevoEnlace;
    private Boolean botonEnlaces;
    private Boolean disableClaseDiarioGeneral;
    private Boolean disableTipoDiarioGeneral;
    private Boolean tableCorteOrdenCobro;
    private Boolean tableRecaudaciones;
    private Boolean procesoCobroEmisor;
    private Boolean botonPartidasPresupuestarias;
    private Boolean renderedRoles;
    private Boolean renderedRolesBeneficios;
    private Boolean fondoReserva;
    private Boolean botonBeneficiarioFondosReserva;
    private Boolean presupuestoTable;
    private Boolean detalleDiarioTableView;
    private Boolean detalleDiarioTableEdit;
    private Boolean botonAnulacion;
    private Boolean tableRetenciones;
    private Boolean booleanAdquisiciones;
    private Boolean cpObligatorio;
    private Boolean tableArriendo;
    private Date fechaBusquedaArriendo;
    private Boolean periodoBoolean;
    private Boolean renderedListRol;
    private Boolean renderDepreciacion;

    /*TOTALES*/
    private double totalComprometido;
    private double totalDevengado;
    private double totalEjecutado;
    private double totalLiquidacion;
    private Short periodo;

    /*STRING AUXILIARES*/
    private String tipoRetencionSeleccionado;
    private String periodoSeleccionado;
    private String tipoGarantia;

    /*LAZYMODELS*/
    private LazyModel<DiarioGeneral> diarioGeneralLazyModel;
    private LazyModel<Recaudacion> recaudacionlLazyModel;
    private LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel;
    private LazyModel<CuentaContable> cuentaContableLazyModel;
    private LazyModel<Inventario> inventarioLazyModel;
    private LazyModel<DetalleTransferencias> detalleTransferenciaLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Presupuesto> presupuestoLazyModel;
    private LazyModel<Adquisiciones> adquisicionesLazyModel;
    private LazyModel<OrdenesEmitidas> ordenesEmitidasLazyModel;
    private LazyModel<BienesMovimiento> bienesList;

    /*
    VARIABLES PARA EL PROCESO DE IMPUESTOS SRI
     */
    private Long tramite, ats;
    private String tarea, taskID, tipo;
    private Date desde, hasta;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.listaPeriodo = periodoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        this.clasesDiarioGeneral = catalogoService.getItemsByCatalogo("diario_general_clases");
        this.modulosDeEnlaces = catalogoService.getItemsByCatalogo("diario_general_modulos_enlace");
        this.tiposAdquisiciones = adquisicionesService.getTipos("sub_tipo_adquisicion");
        this.periodo = opcionBusqueda.getAnioAnterior();
        this.mesesArriendoList = catalogoService.getItemsByCatalogo("meses_anio");
        cargarRegistros();
        reiniciarBotones();
//        cargarValoresProcesoImpuestoSRI();
    }

    public void cargarRegistros() {
        if (periodo != null) {
            this.diarioGeneralLazyModel = new LazyModel<>(DiarioGeneral.class);
            this.diarioGeneralLazyModel.getSorteds().put("id", "ASC");
            this.diarioGeneralLazyModel.getFilterss().put("estado", true);
            this.diarioGeneralLazyModel.getFilterss().put("periodo", this.periodo);
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    public void form(DiarioGeneral diarioGeneralIntegrado, String accion) {
        if (periodo != null) {
            if (periodo.equals(opcionBusqueda.getAnioAnterior()) || accion.equals("visualizar")) {
                this.diarioGeneral = new DiarioGeneral();
                this.detalleDiarioGeneral = new ArrayList<>();
                this.formDiarioGeneral = Boolean.FALSE;
                this.presupuestoTable = Boolean.FALSE;
                this.detalleDiarioTableView = Boolean.FALSE;
                this.detalleDiarioTableEdit = Boolean.TRUE;
                if (diarioGeneralIntegrado != null) {
                    if (periodo.equals(opcionBusqueda.getAnioAnterior())) {
                        if (!diarioGeneralIntegrado.getEstadoDiario().equals("ANULADO")
                                && diarioGeneralIntegrado.getRefDiarioAnulado() == null
                                && diarioGeneralIntegrado.getTotalDebe().doubleValue() > 0) {
                            this.botonAnulacion = Boolean.TRUE;
                        }
                    }
                    this.diarioGeneral = diarioGeneralIntegrado;
                    if (diarioGeneral.getCertificacionesPresupuestario() != null) {
                        reservaCompromiso = diarioGeneral.getCertificacionesPresupuestario();
                    }
                    Boolean accion_detalle = false;
                    if (accion.equals("visualizar")) {
                        this.presupuestoTable = Boolean.FALSE;
                        this.detalleDiarioTableView = Boolean.TRUE;
                        this.detalleDiarioTableEdit = Boolean.FALSE;
                        accion_detalle = false;
                    }
                    if (accion.equals("editar")) {
                        this.botonCuentasContables = Boolean.TRUE;
                        if (diarioGeneral.getClase().getCodigo().equals("clase_diario") && diarioGeneral.getTipo().getCodigo().equals("tipo_presupuesto")) {
                            this.presupuestoTable = Boolean.TRUE;
                            this.detalleDiarioTableView = Boolean.FALSE;
                            this.detalleDiarioTableEdit = Boolean.FALSE;
                        } else {
                            this.presupuestoTable = Boolean.FALSE;
                            this.detalleDiarioTableView = Boolean.FALSE;
                            this.detalleDiarioTableEdit = Boolean.TRUE;
                        }
                        accion_detalle = true;
                    }
                    List<DetalleTransaccion> auxiliar = detalleTransaccionService.getDetalleTransaccion(diarioGeneral);
                    aggDetalle(accion_detalle, auxiliar);
                    this.botonNuevoEnlace = Boolean.FALSE;
                    actualizarTipoDiarioGeneral();
                    calcularTotalesDetalleDiarioGeneral();
                }
                PrimeFaces.current().ajax().update("formMain");
                PrimeFaces.current().ajax().update("formDiarioGeneral");
                PrimeFaces.current().ajax().update("tableDiarioGeneral");
                PrimeFaces.current().ajax().update("detalleDiarioGeneralView");
                PrimeFaces.current().ajax().update("presupuestoDiarioGeneralTable");
            } else {
                JsfUtil.addErrorMessage("AVISO!!", "No puede realizar ninguna acción debido a que el periodo seleccionado no es igual al periodo que se esta ejerciendo");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un periodo y actualizar la tabla");
        }
    }

    private void aggDetalle(Boolean accion, List<DetalleTransaccion> auxiliar) {
        for (DetalleTransaccion detalleDG : auxiliar) {
            if (accion) {
                if (detalleDG.getCedulaPresupuestaria() != null) {
                    List<CuentaContable> auxiliarList = diarioGeneralService.getCuentasContables(detalleDG.getPartidaPresupuestaria(), opcionBusqueda.getAnioAnterior());
                    if (auxiliarList != null) {
                        detalleDG.setSeleccionarCuentaContable(auxiliarList);
                    } else {
                        detalleDG.setSeleccionarCuentaContable(new ArrayList<>());
                    }
                    if (!detalleDG.getSeleccionarCuentaContable().contains(detalleDG.getCuentaContable())) {
                        detalleDG.getSeleccionarCuentaContable().add(detalleDG.getCuentaContable());
                    }
                }
            }
            detalleDiarioGeneral.add(detalleDG);
        }
    }

    private void datosPrecargadosDiarioGeneral() {
        this.diarioGeneral.setPeriodo(opcionBusqueda.getAnioAnterior());
        this.diarioGeneral.setFechaElaboracion(new Date());
        this.diarioGeneral.setTotalDebe(BigDecimal.ZERO);
        this.diarioGeneral.setTotalHaber(BigDecimal.ZERO);
        this.diarioGeneral.setPeriodo(opcionBusqueda.getAnioAnterior());
        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf2.parse("01-12-" + opcionBusqueda.getAnioAnterior());
            this.diarioGeneral.setFechaElaboracion(date);
        } catch (ParseException parseException) {
            this.diarioGeneral.setFechaElaboracion(new Date());
        }
    }

    private void colocarNumActa() {
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnioAnterior());
        if (ultimaActa != null) {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(1));
        }
    }

    public void save() {
        if (!(diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion()))))) {
            JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada esta cerrado");
            return;
        }
        if (diarioGeneral.getObservacion() == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe ingresar el detalle del diario general");
            return;
        }
        if (detalleDiarioGeneral.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay detalle en el asiento contable");
            return;
        }
        if (diarioGeneral.getClase() == null || diarioGeneral.getTipo() == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un tipo y una clase para el diario General");
            return;
        }
        //Registra el la reserva de compromisio si se genero una en el modulo de nomina
        diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase());
        if (diarioGeneral.getId() != null) {
            diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
            diarioGeneral.setFechaModificacion(new Date());
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            }
            diarioGeneralService.edit(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                if (transaccion.getId() != null) {
                    detalleTransaccionService.edit(transaccion);
                } else {
                    transaccion.setDiarioGeneral(diarioGeneral);
                    detalleTransaccionService.create(transaccion);
                }
            }
            if (!detalleDiarioGeneralEliminar.isEmpty()) {
                for (DetalleTransaccion transaccion : detalleDiarioGeneralEliminar) {
                    detalleTransaccionService.remove(transaccion);
                }
            }
        } else {
            if (moduloDeEnlaceSeleccionado != null && moduloDeEnlaceSeleccionado.getId() != null) {
                if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_nomina")) {
                    guardarReserva();
                }
            }
            if (reservaCompromiso != null) {
                if (reservaCompromiso.getId() != null) {
                    if (reservaCompromiso.getBeneficiario() != null) {
                        diarioGeneral.setBeneficiario(reservaCompromiso.getBeneficiario());
                        diarioGeneral.setTipoBeneficiario(reservaCompromiso.getTipoBeneficiario());
                        diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
                    } else {
                        if (reservaCompromiso.getNomina()) {
                            diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
                        }
                    }
                    diarioGeneral.setCertificacionesPresupuestario(reservaCompromiso);
                }
            }
            if ((diarioGeneral.getClase().getCodigo().equals("clase_egreso") || diarioGeneral.getClase().getCodigo().equals("clase_diario")) && diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                diarioGeneral.setRetencion(Boolean.TRUE);
                diarioGeneral.setRetenido(Boolean.FALSE);
            }
            colocarNumActa();
            diarioGeneral.setUsuarioCreacion(userSession.getNameUser());
            diarioGeneral.setFechaCreacion(new Date());
            diarioGeneral.setEstado(Boolean.TRUE);
            diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase());
            diarioGeneral.setEstadoDiario("REGISTRADO");
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            }
            if (moduloDeEnlaceSeleccionado.getId() != null) {
                diarioGeneral.setEnlace(moduloDeEnlaceSeleccionado);
                if (subEnlaceSeleccionado != null && subEnlaceSeleccionado.getId() != null) {
                    diarioGeneral.setEnlace(subEnlaceSeleccionado);
                }
            }
            diarioGeneral = diarioGeneralService.create(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                transaccion.setDiarioGeneral(diarioGeneral);
                if (botonPartidasPresupuestarias) {
                    transaccion.setHaber(BigDecimal.ZERO);
                }
                detalleTransaccionService.create(transaccion);
            }
            if (moduloDeEnlaceSeleccionado.getId() != null) {
                switch (moduloDeEnlaceSeleccionado.getCodigo()) {
                    case "modulo_anulacion_cp_transferencia":
                        contabilizarModuloAnulacion();
                        break;
                    case "modulo_certificaciones_presupuestarias":
                        contabilizarReservaCompromiso();
                        break;
                    case "modulo_inventario_salidas":
                        contabilizarModuloInventario();
                        break;
                    case "modulo_inventario_entradas":
                        contabilizarModuloInventario();
                        break;
                    case "modulo_nomina":
                        contabilizarModuloNomina();
                        break;
                    case "modulo_tesoreria":
                        contabilizarModuloTesoreria();
                        break;
                    case "modulo_bienes":
                        contabilizarBienes();
                        break;
                }
            }
        }
        imprimirDiarioGeneral();
        if (tramite != null) {
            actualizarImpuestoSRI(diarioGeneral.getId());
        } else {
            if (diarioGeneral.getEstadoTransaccion().equals("DESCUADRADO")) {
                JsfUtil.addErrorMessage("ERROR!!", "Se ha registrado el diario descuadrado");
            }
            JsfUtil.addInformationMessage("INFO!!", "Se ha guardado correctamente");
            cancelar();
        }
    }

    public void cancelar() {
        reiniciarBotones();
        reiniciarValoresTotales();
        reiniciarCuentaContable();
        restablecerModuloInventario();
        restablecerModuloTesoreria();
        restablecerModuloNomina();
        this.diarioGeneral = new DiarioGeneral();
        this.subEnlaceSeleccionado = null;
        this.subEnlace = new ArrayList<>();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.tiposDiarioGeneral = new ArrayList<>();
        this.cuentasConPartida = new ArrayList<>();
        this.detalleReservaList = new ArrayList<>();
        this.reservaCompromiso = null;
        this.moduloDeEnlaceSeleccionado = null;
        this.inventarioLazyModel = null;
        this.corteOrdenCobroLazyModel = null;
        this.recaudacionlLazyModel = null;
        this.detalleTransferenciaLazyModel = null;
        this.periodoSeleccionado = "";
        this.tipoRetencionSeleccionado = "";
        PrimeFaces.current().ajax().update("formMain");
    }

    public void imprimirDiarioGeneral() {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void abrirDlogoImpresion() {
        PrimeFaces.current().executeScript("PF('DlogoGlobalImpresion').show()");
        PrimeFaces.current().ajax().update(":formImpresionGlobal");
    }

    public void imprimirDiarioGlobal() {
        int a = desde.compareTo(hasta);
        if (a > 0) {
            JsfUtil.addWarningMessage("AVISO", "LA FECHA HASTA DEBER SER IGUAL O MAYOR A LA FECHA DESDE");
            return;
        }
        servletSession.addParametro("desde", desde);
        servletSession.addParametro("hasta", hasta);
        servletSession.setNombreReporte("asientosContables");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void cerrarDlogoImpresion() {
        PrimeFaces.current().executeScript("PF('DlogoGlobalImpresion').hide()");
        PrimeFaces.current().ajax().update(":formImpresionGlobal");
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString() + "-" + reservaCompromiso.getPeriodo();
        return a;
    }

    public void quitarDetalleDiarioGeneral(DetalleTransaccion detalleDG) {
        if (diarioGeneralService.getComprobanteRegistrado(detalleDG.getDiarioGeneral())) {
            JsfUtil.addErrorMessage("ERROR!!!", "No se puede quitar la cuenta debido a que ya tiene un comprobante de pago generado");
            return;
        }
        int index = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            if (detalle.getContador() == detalleDG.getContador()) {
                break;
            }
            index = index + 1;
        }
        if (detalleDG.getId() != null) {
            /*Eliminamos el registro desde la base de datos*/
            detalleDiarioGeneralEliminar.add(detalleDG);
            detalleDiarioGeneral.remove(index);
        } else {
            detalleDiarioGeneral.remove(index);
        }
        int contador = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalle.setContador(bigInteger);
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    public void registrarDiarioGeneral(String accion) {
        this.moduloDeEnlaceSeleccionado = new CatalogoItem();
        this.botonNuevoEnlace = Boolean.FALSE;
        datosPrecargadosDiarioGeneral();
        if (accion.equals("enlaces")) {
            this.botonEnlaces = Boolean.TRUE;
        } else {
            this.disableClaseDiarioGeneral = Boolean.FALSE;
            this.disableTipoDiarioGeneral = Boolean.FALSE;
        }
    }

    public void actualizarTipoDiarioGeneral() {
        if (diarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(diarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void actualizarBotonCuentasContables() {
        if (diarioGeneral.getClase() != null && diarioGeneral.getTipo() != null) {
            if (diarioGeneral.getClase().getCodigo().equals("clase_diario") && diarioGeneral.getTipo().getCodigo().equals("tipo_presupuesto")) {
                this.botonCuentasContables = Boolean.FALSE;
                this.botonPartidasPresupuestarias = Boolean.TRUE;
            } else {
                this.botonCuentasContables = Boolean.TRUE;
                this.botonPartidasPresupuestarias = Boolean.FALSE;
            }
        } else {
            this.botonCuentasContables = Boolean.FALSE;
            this.botonPartidasPresupuestarias = Boolean.FALSE;
        }
    }

    private void reiniciarBotones() {
        tramite = null;
        ats = null;
        tarea = null;
        taskID = null;
        this.formDiarioGeneral = Boolean.TRUE;
        this.botonCuentasContables = Boolean.FALSE;
        this.botonNuevoEnlace = Boolean.TRUE;
        this.botonEnlaces = Boolean.FALSE;
        this.disableClaseDiarioGeneral = Boolean.TRUE;
        this.disableTipoDiarioGeneral = Boolean.TRUE;
        this.presupuestoTable = Boolean.FALSE;
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.TRUE;
        this.tableCorteOrdenCobro = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
        this.botonPartidasPresupuestarias = Boolean.FALSE;
        this.renderedRolesBeneficios = Boolean.FALSE;
        this.renderedRoles = Boolean.FALSE;
        this.botonBeneficiarioFondosReserva = Boolean.FALSE;
        this.botonAnulacion = Boolean.FALSE;
        this.tableRetenciones = Boolean.FALSE;
        this.detalleDiarioGeneralEliminar = new ArrayList<>();
        this.adquisicion = new Adquisiciones();
    }

    private void calcularTotalesDetalleDiarioGeneral() {
        reiniciarValoresTotales();
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            this.diarioGeneral.setTotalDebe(new BigDecimal(redondearDosDecimales(this.diarioGeneral.getTotalDebe().doubleValue() + detalleDG.getDebe().doubleValue())));
            this.diarioGeneral.setTotalHaber(new BigDecimal(redondearDosDecimales(this.diarioGeneral.getTotalHaber().doubleValue() + detalleDG.getHaber().doubleValue())));
            this.totalComprometido = redondearDosDecimales(totalComprometido + detalleDG.getComprometido().doubleValue());
            this.totalDevengado = redondearDosDecimales(totalDevengado + detalleDG.getDevengado().doubleValue());
            this.totalEjecutado = redondearDosDecimales(totalEjecutado + detalleDG.getEjecutado().doubleValue());
        }
        if (this.diarioGeneral.getTotalDebe().doubleValue() != this.diarioGeneral.getTotalHaber().doubleValue()) {
            this.diarioGeneral.setEstadoTransaccion("DESCUADRADO");
        } else {
            this.diarioGeneral.setEstadoTransaccion("CUADRADO");
        }
    }

    private double redondearDosDecimales(double valor) {
        return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    private void reiniciarValoresTotales() {
        this.diarioGeneral.setTotalDebe(BigDecimal.ZERO);
        this.diarioGeneral.setTotalHaber(BigDecimal.ZERO);
        this.totalComprometido = 0;
        this.totalDevengado = 0;
        this.totalEjecutado = 0;
    }

    public void openDlgCuentasContables() {
        reiniciarCuentaContable();
        this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnioAnterior());
        if (this.reservaCompromiso != null) {
            if (filtroPartidas != null) {
                if (filtroPartidas.size() > 0) {
                    this.detalleTransaccion = new DetalleTransaccion();
                    for (CatalogoPresupuesto partida : filtroPartidas) {
                        this.detalleTransaccion.setPartidaPresupuestaria(partida);
                        break;
                    }
                    if (calcularHaberTotalPartidasRepetidas()) {
                        filtroPartidas.remove(detalleTransaccion.getPartidaPresupuestaria());
                        openDlgCuentasContables();
                    } else {
                        if (detalleTransaccion.getPartidaPresupuestaria() != null) {
                            this.cuentaContableLazyModel.getFilterss().put("titulo", 2);
                            this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
                            this.cuentaContableLazyModel.getFilterss().put("subGrupo", 3);
                            this.cuentaContableLazyModel.getFilterss().put("cuentaNivel1", detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2));
                        }
                    }
                }
            }
        }
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
    }

    private Boolean calcularHaberTotalPartidasRepetidas() {
        double sumatoriaHaber = 0;
        double sumatoriaDebe = 0;
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            if (detalleDG.getPartidaPresupuestaria() != null) {
                if (detalleDG.getPartidaPresupuestaria().getCodigo().substring(0, 2).equals(detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                    sumatoriaDebe = sumatoriaDebe + detalleDG.getDebe().doubleValue();
                }
            }
            if (detalleDG.getCuentaContable().getCodigo().substring(3, 5).equals(detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                sumatoriaHaber = sumatoriaHaber + detalleDG.getHaber().doubleValue();
            }
        }
        double total = sumatoriaDebe - sumatoriaHaber;
        if (total < 0) {
            detalleTransaccion.setHaber(BigDecimal.ZERO);
        } else {
            detalleTransaccion.setHaber(new BigDecimal(total));
        }
        return total <= 0;
    }

    public void aniadirCuentaContable() {
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        int contador = detalleDiarioGeneral.size() + 1;
        BigInteger bigInteger = BigInteger.valueOf(contador);
        detalleDG.setContador(bigInteger);
        detalleDG.setCuentaContable(cuentaContableSeleccionado);
        detalleDG.setDebe(BigDecimal.ZERO);
        if (detalleTransaccion != null) {
            if (detalleTransaccion.getHaber() != null) {
                detalleDG.setHaber(detalleTransaccion.getHaber());
            } else {
                detalleDG.setHaber(BigDecimal.ZERO);
            }
        } else {
            detalleDG.setHaber(BigDecimal.ZERO);
        }
        detalleDiarioGeneral.add(detalleDG);
        calcularTotalesDetalleDiarioGeneral();
        this.detalleTransaccion = new DetalleTransaccion();
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    private void reiniciarCuentaContable() {
        this.cuentaContableSeleccionado = new CuentaContable();
    }

    public void consultarPeriodoElegido() {
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "El mes de la fecha seleccionada no se encuentra abierto");
        }
    }

    public void quitarFiltros() {
        filtroPartidas = new ArrayList<>();
        openDlgCuentasContables();
        PrimeFaces.current().ajax().update("cuentasPresupuestarioTable");
        PrimeFaces.current().executeScript("PF('cuentasPresupuestarioTable').clearFilters()");
    }

    public void enlaceSeleccionado() {
        if (moduloDeEnlaceSeleccionado != null) {
            switch (moduloDeEnlaceSeleccionado.getCodigo()) {
                case "modulo_anulacion_cp_transferencia":
                    this.detalleTransferenciaLazyModel = new LazyModel<>(DetalleTransferencias.class);
                    this.detalleTransferenciaLazyModel.getSorteds().put("referencia", "ASC");
                    this.detalleTransferenciaLazyModel.getFilterss().put("estado", "ANULADO");
                    this.detalleTransferenciaLazyModel.getFilterss().put("contabilizado", false);
                    this.detalleTransferenciaLazyModel.getFilterss().put("transferencia.periodo", opcionBusqueda.getAnioAnterior());
                    PrimeFaces.current().executeScript("PF('moduloAnulacionCPTDLG').show()");
                    PrimeFaces.current().ajax().update("moduloAnulacionCPTForm");
                    break;
                case "modulo_certificaciones_presupuestarias":
                    SimpleDateFormat f = new SimpleDateFormat("yyy-MM-dd");
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(f.parse(f.format(diarioGeneral.getFechaElaboracion())));
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        c.add(Calendar.SECOND, -1);
                        reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnioAnterior(), c.getTime());
                    } catch (ParseException ex) {
                        Logger.getLogger(DiarioGeneralController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    PrimeFaces.current().executeScript("PF('moduloReservaCompromisoDlg').show()");
                    PrimeFaces.current().ajax().update("moduloReservaCompromisoForm");
                    break;

                case "modulo_inventario_salidas":
                    this.motivoMovimientosList = diarioGeneralService.getMotivoMovimientos("SALIDAS");
                    PrimeFaces.current().executeScript("PF('moduloInventarioDlg').show()");
                    PrimeFaces.current().ajax().update("moduloInventarioForm");
                    break;
                case "modulo_inventario_entradas":
                    this.motivoMovimientosList = diarioGeneralService.getMotivoMovimientos("ENTRADAS");
                    PrimeFaces.current().executeScript("PF('moduloInventarioDlg').show()");
                    PrimeFaces.current().ajax().update("moduloInventarioForm");
                    break;
                case "modulo_nomina":
                    this.subEnlace = catalogoItemService.getPadreCatalogoItem(moduloDeEnlaceSeleccionado);
                    PrimeFaces.current().executeScript("PF('moduloNominaDlg').show()");
                    PrimeFaces.current().ajax().update("moduloNominaForm");
                    break;
                case "modulo_tesoreria":
                    this.subEnlace = catalogoItemService.getPadreCatalogoItem(moduloDeEnlaceSeleccionado);
                    PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').show()");
                    PrimeFaces.current().ajax().update("moduloTesoreriaForm");
                    break;
                case "modulo_bienes":
                    this.subEnlace = catalogoItemService.getPadreCatalogoItem(moduloDeEnlaceSeleccionado);
                    restablecerModuloBienes();
                    PrimeFaces.current().executeScript("PF('bienesDlg').show()");
                    PrimeFaces.current().ajax().update("bienesForm");
                    break;
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Metodo de anulación del diario general">
    public void anulacionDiarioGeneral() {
        if (diarioGeneralService.getComprobanteRegistrado(this.diarioGeneral)) {
            JsfUtil.addErrorMessage("ERROR!!!", "No se puede anular el registro contable debido a que tiene un comprobante de pago generado");
            return;
        }
        if (!(diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion()))))) {
            JsfUtil.addWarningMessage("AVISO", "Es imposible anular porque la fecha seleccionada esta cerrado");
            return;
        }
        DiarioGeneral diarioG = new DiarioGeneral();
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnioAnterior());
        if (ultimaActa != null) {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        diarioG.setClase(diarioGeneral.getClase());
        diarioG.setTipo(diarioGeneral.getTipo());
        diarioG.setObservacion("ANULACIÓN " + diarioGeneral.getObservacion());
        diarioG.setEstadoDiario("REGISTRADO");
        diarioG.setUsuarioCreacion(userSession.getNameUser());
        diarioG.setFechaCreacion(new Date());
        diarioG.setEstado(Boolean.TRUE);
        diarioG.setRefDiarioAnulado(diarioGeneral);
        diarioG.setPeriodo(opcionBusqueda.getAnioAnterior());
        diarioG.setEstadoTransaccion(diarioGeneral.getEstadoTransaccion());
        diarioG.setFechaElaboracion(diarioGeneral.getFechaElaboracion());
        diarioG.setRetencion(diarioGeneral.getRetencion());
        diarioG.setRetenido(diarioGeneral.getRetenido());
        diarioG.setTotalDebe(new BigDecimal(diarioGeneral.getTotalDebe().doubleValue() * (-1)));
        diarioG.setTotalHaber(new BigDecimal(diarioGeneral.getTotalHaber().doubleValue() * (-1)));
        if (diarioGeneral.getVariosBeneficiarios() != null) {
            diarioG.setVariosBeneficiarios(diarioGeneral.getVariosBeneficiarios());
        }
        if (diarioGeneral.getBeneficiario() != null) {
            diarioG.setBeneficiario(diarioGeneral.getBeneficiario());
        }
        if (diarioGeneral.getTipoBeneficiario() != null) {
            diarioG.setTipoBeneficiario(diarioGeneral.getTipoBeneficiario());
        }
        if (diarioGeneral.getNumTramite() != null) {
            diarioG.setNumTramite(diarioGeneral.getNumTramite());
        }
        if (diarioGeneral.getEnlace() != null) {
            diarioG.setEnlace(diarioGeneral.getEnlace());
        }
        if (diarioGeneral.getCertificacionesPresupuestario() != null) {
            diarioG.setCertificacionesPresupuestario(diarioGeneral.getCertificacionesPresupuestario());
        }
        diarioG = diarioGeneralService.create(diarioG);
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            detalleDG.setDatoCargado(Boolean.TRUE);
            detalleTransaccionService.edit(detalleDG);
            DetalleTransaccion detalleT = new DetalleTransaccion();
            detalleT.setCuentaContable(detalleDG.getCuentaContable());
            if (detalleDG.getDebe().doubleValue() > 0) {
                detalleT.setDebe(new BigDecimal(detalleDG.getDebe().doubleValue() * (-1)));
            } else {
                detalleT.setDebe(detalleDG.getDebe());
            }
            if (detalleDG.getHaber().doubleValue() > 0) {
                detalleT.setHaber(new BigDecimal(detalleDG.getHaber().doubleValue() * (-1)));
            } else {
                detalleT.setHaber(detalleDG.getHaber());
            }
            if (detalleDG.getTipoTransaccion() != null) {
                detalleT.setTipoTransaccion(detalleDG.getTipoTransaccion());
            }
            if (detalleDG.getPartidaPresupuestaria() != null) {
                detalleT.setPartidaPresupuestaria(detalleDG.getPartidaPresupuestaria());
            }
            if (detalleDG.getEstructuraProgramatica() != null) {
                detalleT.setEstructuraProgramatica(detalleDG.getEstructuraProgramatica());
            }
            if (detalleDG.getDevengado().doubleValue() > 0) {
                detalleT.setDevengado(new BigDecimal(detalleDG.getDevengado().doubleValue() * (-1)));
            } else {
                detalleT.setDevengado(detalleDG.getDevengado());
            }
            if (detalleDG.getEjecutado().doubleValue() > 0) {
                detalleT.setEjecutado(new BigDecimal(detalleDG.getEjecutado().doubleValue() * (-1)));
            } else {
                detalleT.setEjecutado(detalleDG.getEjecutado());
            }
            if (detalleDG.getCedulaPresupuestaria() != null) {
                detalleT.setCedulaPresupuestaria(detalleDG.getCedulaPresupuestaria());
            }
            if (detalleDG.getFuente() != null) {
                detalleT.setFuente(detalleDG.getFuente());
            }
            if (detalleDG.getTipoDevengado() != null) {
                detalleT.setTipoDevengado(detalleDG.getTipoDevengado());
            }
            if (detalleDG.getComprobantePago() != null) {
                detalleT.setComprobantePago(detalleDG.getComprobantePago());
            }
            if (detalleDG.getIdDetalleReserva() != null) {
                detalleT.setIdDetalleReserva(detalleDG.getIdDetalleReserva());
            }
            detalleT.setDatoCargado(Boolean.TRUE);
            detalleT.setContador(detalleDG.getContador());
            detalleT.setDiarioGeneral(diarioG);
            detalleTransaccionService.create(detalleT);
        }
        /*Actualizar Diario Anulado*/
        diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
        diarioGeneral.setFechaModificacion(new Date());
        diarioGeneral.setFechaAnulacion(diarioG.getFechaElaboracion());
        diarioGeneral.setEstadoDiario("ANULADO");
        diarioGeneral.setRefDiarioAnulado(diarioG);
        if (diarioGeneral.getEnlace() != null) {
            restablecerValoresModulos(diarioGeneral);
        }
        diarioGeneralService.edit(diarioGeneral);
        //*IMPRIMIMOS EL REPORTE DE LA ANULACIÓN*//
        this.diarioGeneral = diarioG;
        imprimirDiarioGeneral();
        cancelar();
    }

    private void restablecerValoresModulos(DiarioGeneral diarioGeneral) {
        int aux;
        String codigo = "";
        String subCodigo = "";
        if (diarioGeneral.getEnlace().getPadre() != null) {
            codigo = diarioGeneral.getEnlace().getPadre().getCodigo();
            subCodigo = diarioGeneral.getEnlace().getCodigo();
        } else {
            codigo = diarioGeneral.getEnlace().getCodigo();
        }
        switch (codigo) {
            case "modulo_anulacion_cp_transferencia":
                aux = detalleTransferenciaService.getRestablecerValores(diarioGeneral);
                break;
            case "modulo_certificaciones_presupuestarias":
                diarioGeneral.getCertificacionesPresupuestario().setContabilizado(Boolean.FALSE);
                reservaCompromisoService.edit(diarioGeneral.getCertificacionesPresupuestario());
                break;
            case "modulo_inventario_salidas":
            case "modulo_inventario_entradas":
                aux = inventarioService.getRestablecerValores(diarioGeneral);
                break;
            case "modulo_nomina":
                CatalogoItem estadoAprobacion = catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("aprobado-rol", "estados_tipo_rol");
                switch (subCodigo) {
                    case "rol_beneficios":
                        aux = tipoRolBeneficiosService.getRestablecerValores(diarioGeneral, estadoAprobacion);
                        break;
                    case "rol_general1":
                    case "rol_extras_suplementarias":
                        aux = tipoRolService.getRestablecerValoresRoles(diarioGeneral, estadoAprobacion);
                        aux = cuotaAnticipoService.getRestablecerValores(diarioGeneral);
                        break;
                    case "rol_fondos_reserva":
                    case "rol_adicional1":
                        aux = tipoRolService.getRestablecerValoresFondos(diarioGeneral, estadoAprobacion);
                        break;
                }
                break;
            case "modulo_tesoreria":
                switch (subCodigo) {
                    case "emisiones":
                        aux = corteOrdenCobroService.getRestablecerValoresEmisiones(diarioGeneral);
                        break;
                    case "cobros_caja":
                        aux = corteOrdenCobroService.getRestablecerValoresCobroCaja(diarioGeneral);
                        break;
                    case "recaudaciones_cobros":
                    case "recaudaciones_ajustes":
                    case "recaudaciones_cobros_ajustes":
                        aux = recaudacionService.getRestablecerValores(diarioGeneral);
                        break;
                    case "retenciones":
                        aux = liquidacionDetalleService.getRestablecerValores(diarioGeneral);
                        break;
                    case "ordenes_cobro":
                        aux = ordenesEmitidasService.getRestablecerValores(diarioGeneral, true);
                        break;
                    case "ordenes_ingreso":
                        aux = ordenesEmitidasService.getRestablecerValores(diarioGeneral, false);
                        break;
                }
                break;
            case "modulo_bienes":
                switch (subCodigo) {
                    case "depreciaciones":
                        aux = depreciacionService.getRestablecerValores(diarioGeneral);
                        break;
                    case "bienes_ingresos":
                        aux = bienesMovimientoService.getRestablecerValores(diarioGeneral);
                        break;
                }
                break;
        }
    }
//</editor-fold>

    /*METODOS SOLO PARA LA PARTE PRESUPUESTARIA CUANDO ESTE SEA DIARIO - FINANCIERO*/
    //<editor-fold defaultstate="collapsed" desc="Parte Presupuestaria">
    public void openDlgPartidasPresupuestarias() {
        this.presupuestoTable = Boolean.TRUE;
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.FALSE;
        this.presupuestoLazyModel = new LazyModel<>(Presupuesto.class);
        this.presupuestoLazyModel.getSorteds().put("partida", "ASC");
        this.presupuestoLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnioAnterior());
        PrimeFaces.current().executeScript("PF('presupuestoDlg').show()");
        PrimeFaces.current().ajax().update("presupuestoForm");
    }

    public void añadirPartidasPresupuestarias(Presupuesto presupuesto) {
        this.disableClaseDiarioGeneral = Boolean.TRUE;
        this.disableTipoDiarioGeneral = Boolean.TRUE;
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        int contador = detalleDiarioGeneral.size() + 1;
        BigInteger bigInteger = BigInteger.valueOf(contador);
        detalleDG.setContador(bigInteger);
        detalleDG.setDebe(BigDecimal.ZERO);
        detalleDG.setDatoCargado(presupuesto.getTipo());
        detalleDG.setCedulaPresupuestaria(presupuesto.getPartida());
        if (presupuesto.getItem() != null) {
            detalleDG.setPartidaPresupuestaria(presupuesto.getItem());
        }
        if (presupuesto.getEstructura() != null) {
            detalleDG.setEstructuraProgramatica(presupuesto.getEstructura());
        }
        if (presupuesto.getFuenteDirecta() != null) {
            detalleDG.setFuente(presupuesto.getFuenteDirecta());
        }
        /*Calculamos el saldo disponible de la partida seleccionada*/
        double comprometidoRegistrado = 0;
        if (detalleDiarioGeneral.isEmpty()) {
            for (DetalleTransaccion detalle : detalleDiarioGeneral) {
                if (detalle.getCedulaPresupuestaria().equals(detalleDG.getCedulaPresupuestaria())) {
                    comprometidoRegistrado = comprometidoRegistrado + detalle.getComprometido().doubleValue();
                }
            }
        }
        BigDecimal saldoReservasAprobadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnioAnterior(), "APRO");
        BigDecimal saldoReservasLiquidadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnioAnterior(), "LIQUI");
        BigDecimal saldoDevengado = diarioGeneralService.getsaldoDevengado(presupuesto.getPartida(), opcionBusqueda.getAnioAnterior());
        double saldoDisponible = presupuesto.getCodificado().doubleValue() - comprometidoRegistrado - saldoReservasAprobadas.doubleValue() - saldoReservasLiquidadas.doubleValue() - saldoDevengado.doubleValue();
        if (!detalleDG.getDatoCargado()) {
            detalleDG.setHaber(new BigDecimal(presupuesto.getCodificado().doubleValue() - saldoDevengado.doubleValue()));
        } else {
            detalleDG.setHaber(new BigDecimal(saldoDisponible));
        }
        this.detalleDiarioGeneral.add(detalleDG);
        PrimeFaces.current().executeScript("PF('presupuestoDlg').hide()");
        PrimeFaces.current().ajax().update("presupuestoDiarioGeneralTable");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    public void validarMontosPresupueto(DetalleTransaccion detalleDG, String accion) {
        switch (accion) {
            case "1":
                if (detalleDG.getHaber().doubleValue() < detalleDG.getComprometido().doubleValue()) {
                    JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                    detalleDG.setComprometido(BigDecimal.ZERO);
                }
                break;
            case "2":
                if (!detalleDG.getDatoCargado()) {
                    if (detalleDG.getComprometido() != null) {
                        if (detalleDG.getComprometido().doubleValue() < detalleDG.getDevengado().doubleValue()) {
                            JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                            detalleDG.setDevengado(BigDecimal.ZERO);
                        }
                    } else {
                        JsfUtil.addWarningMessage("AVISO", "Primero debe ingresar el monto comprometido");
                        detalleDG.setDevengado(BigDecimal.ZERO);
                    }
                }
                break;
            case "3":
                if (detalleDG.getDevengado() != null) {
                    if (detalleDG.getDevengado().doubleValue() < detalleDG.getEjecutado().doubleValue()) {
                        JsfUtil.addWarningMessage("AVISO", "El monto del comprometido es mayor al saldo disponible");
                        detalleDG.setEjecutado(BigDecimal.ZERO);
                    }
                } else {
                    JsfUtil.addWarningMessage("AVISO", "Primero debe ingresar el monto devengado");
                    detalleDG.setEjecutado(BigDecimal.ZERO);
                }
                break;
        }
        calcularTotalesDetalleDiarioGeneral();
    }
//</editor-fold>

    /*Determina su relacion presupuestaria al momento de ingresar el valor en el debe o en el haber*/
    //<editor-fold defaultstate="collapsed" desc="Establecer la relación presupuestaria en el ingreso del Debe - Haber">
    public void determinarRelacionPresupuestaria(DetalleTransaccion detalleDG, Boolean tipoIngreso) {
        reiniciarValoresDinamicos();
        Boolean accion = Boolean.FALSE;
        if (tipoIngreso) {
            detalleDG.setHaber(BigDecimal.ZERO);
        } else {
            detalleDG.setDebe(BigDecimal.ZERO);
        }
        if (!detalleDG.getDatoCargado()) {
            detalleDG.setTipoTransaccion(null);
            detalleDG.setPartidaPresupuestaria(null);
            detalleDG.setEstructuraProgramatica(null);
            detalleDG.setFuente(null);
            detalleDG.setCedulaPresupuestaria(null);
            detalleDG.setComprometido(BigDecimal.ZERO);
            detalleDG.setDevengado(BigDecimal.ZERO);
            detalleDG.setEjecutado(BigDecimal.ZERO);
            CatalogoPresupuesto partidaPresupuestaria = null;
            if (tipoIngreso) {
                if (!detalleDG.getDatoCargado()) {
                    if (diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                        if (detalleDG.getCuentaContable().getTitulo() == 1 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
                            calcularTotalesDetalleDiarioGeneral();
                            return;
                        }
                        if (detalleDG.getCuentaContable().getCtaPagarCobrar()) {
                            partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleDG.getCuentaContable());
                            if (partidaPresupuestariaRelacionadas != null) {
                                for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                                    añadirRelacionItemPresupuesto(catPresupuesto);
                                }
                            }
                        } else {
                            partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleDG.getCuentaContable(), true);
                            if (partidaPresupuestaria != null) {
                                añadirRelacionItemPresupuesto(partidaPresupuestaria);
                            }
                        }
                        if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                            if (detalleDG.getCuentaContable().getTitulo() == 2) {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                detalleDG.setEjecutado(detalleDG.getDebe());
                            } else {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                                detalleDG.setDevengado(detalleDG.getDebe());
                                detalleDG.setComprometido(detalleDG.getDebe());
                            }
                            accion = Boolean.TRUE;
                        }
                    }
                }
            } else {
                if (!detalleDG.getDatoCargado()) {
                    if (diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                        if (detalleDG.getCuentaContable().getTitulo() == 2 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
                            calcularTotalesDetalleDiarioGeneral();
                            return;
                        }
                        if (detalleDG.getCuentaContable().getCtaPagarCobrar()) {
                            partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleDG.getCuentaContable());
                            if (partidaPresupuestariaRelacionadas != null) {
                                for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                                    añadirRelacionItemPresupuesto(catPresupuesto);
                                }
                            }
                        } else {
                            partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleDG.getCuentaContable(), false);
                            if (partidaPresupuestaria != null) {
                                añadirRelacionItemPresupuesto(partidaPresupuestaria);
                            }
                        }
                        if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                            if (detalleDG.getCuentaContable().getTitulo() == 1) {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                detalleDG.setEjecutado(detalleDG.getHaber());
                            } else {
                                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                                detalleDG.setDevengado(detalleDG.getHaber());
                            }
                            accion = Boolean.TRUE;
                        }
                    }
                }
            }
            if (accion) {
                this.detalleTransaccion = detalleDG;
                if (presupuestoRelacionado.size() == 1) {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        this.presupuestoSeleccionado = presupuesto;
                        guardarRelacionesPresupuestarias();
                    }
                } else {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        BigDecimal saldoPartida = diarioGeneralService.getSaldoPresupuesto(presupuesto, diarioGeneral.getFechaElaboracion());
                        if (saldoPartida != null) {
                            presupuesto.setMontoDisponible(saldoPartida);
                        } else {
                            presupuesto.setMontoDisponible(BigDecimal.ZERO);
                        }
                    }
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("gridPartidas");
                }
            }
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("diarioGeneralTable");

    }

    private void añadirRelacionItemPresupuesto(CatalogoPresupuesto catPresupuesto) {
        List<Presupuesto> auxiliarList = diarioGeneralService.getListadoPresupuesto(catPresupuesto, opcionBusqueda.getAnioAnterior());
        if (auxiliarList != null) {
            for (Presupuesto presupuesto : auxiliarList) {
                presupuestoRelacionado.add(presupuesto);
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        if (presupuestoSeleccionado != null) {
            for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
                if (detalleDG.getContador().equals(detalleTransaccion.getContador())) {
                    detalleDG.setPartidaPresupuestaria(presupuestoSeleccionado.getItem());
                    detalleDG.setCedulaPresupuestaria(presupuestoSeleccionado.getPartida());
                    detalleDG.setFuente(presupuestoSeleccionado.getFuenteDirecta());
                    detalleDG.setEstructuraProgramatica(presupuestoSeleccionado.getEstructura());
                }
            }
            reiniciarValoresDinamicos();
            PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar una cédula presupuestario");
        }
    }

    private void reiniciarValoresDinamicos() {
        this.detalleTransaccion = new DetalleTransaccion();
        this.partidaPresupuestariaRelacionadas = new ArrayList<>();
        this.presupuestoRelacionado = new ArrayList<>();
        this.presupuestoSeleccionado = new Presupuesto();
        this.partidaEstructura = new ArrayList<>();
    }
//</editor-fold>

    /*MÓDULO: ANULACIÓN COMPROBANTES DE PAGO/TRANSFERENCIAS*/
    //<editor-fold defaultstate="collapsed" desc="Anulación de comprobante de pago y/o transferencias">
    public void registrarTransferenciaEnDiarioGeneral(DetalleTransferencias transferenciaAnulada) {
        this.transferenciaAnuladaSeleccionada = transferenciaAnulada;
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        actualizarTipoDiarioGeneral();
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneral.setObservacion("MODULO DE ANULACION CP/TRANSFERENCIA: P.R. ANULACION DE TRANSFERENCIA No. " + transferenciaAnuladaSeleccionada.getReferencia() + ", COMPROBANTE DE PAGO No. " + transferenciaAnuladaSeleccionada.getComprobantePago().getNumComprobante());
        BeneficiarioComprobantePago beneficiarioComprobantePago = beneficiarioComprobantePagoService.getBeneficiarioComprobantePago(transferenciaAnuladaSeleccionada.getComprobantePago(), transferenciaAnuladaSeleccionada.getIdentificacion().substring(0, 10));
        diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
        diarioGeneral.setBeneficiario(beneficiarioComprobantePago.getBeneficiario());
        diarioGeneral.setTipoBeneficiario(beneficiarioComprobantePago.getTipoBeneficiario());
        DetalleComprobantePago cuentaBancariaContable = detalleComprobantePagoService.getCuentaBancariaContableHaber(transferenciaAnuladaSeleccionada.getComprobantePago());
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        detalleDG.setDatoCargado(Boolean.TRUE);
        detalleDG.setContador(BigInteger.ONE);
        detalleDG.setCuentaContable(cuentaBancariaContable.getCuentaContable());
        detalleDG.setDebe(transferenciaAnuladaSeleccionada.getValor());
        detalleDG.setHaber(BigDecimal.ZERO);
        detalleDiarioGeneral.add(detalleDG);
        this.botonCuentasContables = Boolean.TRUE;
        calcularTotalesDetalleDiarioGeneral();
        this.botonCuentasContables = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('moduloAnulacionCPTDLG').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    private void contabilizarModuloAnulacion() {
        if (transferenciaAnuladaSeleccionada != null) {
            transferenciaAnuladaSeleccionada.setContabilizado(Boolean.TRUE);
            transferenciaAnuladaSeleccionada.setDiarioGeneral(diarioGeneral);
            detalleTransferenciaService.edit(transferenciaAnuladaSeleccionada);
        }
        restablecerModuloAnulacion();
    }

    private void restablecerModuloAnulacion() {
        this.transferenciaAnuladaSeleccionada = new DetalleTransferencias();
    }
//</editor-fold>

    /*MÓDULO: INVENTARIO*/
    //<editor-fold defaultstate="collapsed" desc="Inventario">
    public void actualizarTablaInventario() {
        if (catalogoMovimientoSeleccionado != null) {
            this.inventarioLazyModel = new LazyModel<>(Inventario.class);
            this.inventarioLazyModel.getSorteds().put("id", "ASC");
            this.inventarioLazyModel.getFilterss().put("estado", true);
            if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_salidas")) {
                this.inventarioLazyModel.getFilterss().put("tipoMovimiento", "EGRESO");
            } else if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_entradas")) {
                this.inventarioLazyModel.getFilterss().put("tipoMovimiento", "INGRESO");
            }
            this.inventarioLazyModel.getFilterss().put("contabilizado", false);
            this.inventarioLazyModel.getFilterss().put("motivoMovimiento", catalogoMovimientoSeleccionado);
            this.inventarioLazyModel.getFilterss().put("anio", opcionBusqueda.getAnioAnterior());
        } else {
            this.inventarioLazyModel = null;
        }
    }

    public void registrarInventarioEnDiarioGeneral() {
        if (inventariosSeleccionadoList != null && !inventariosSeleccionadoList.isEmpty()) {
            diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
            actualizarTipoDiarioGeneral();
            diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_ajuste"));
            if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_salidas")) {
                diarioGeneral.setObservacion("P.R. SALIDA DE INVENTARIO ");
            } else if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_entradas")) {
                diarioGeneral.setObservacion("P.R. ENTRADA DE INVENTARIO ");
            }
            this.botonCuentasContables = Boolean.TRUE;
            List<CuentaContable> listCuentaContable = new ArrayList<>();
            List<DetalleTransaccion> listAuxiliar = new ArrayList<>();
            for (Inventario inventario : inventariosSeleccionadoList) {
                diarioGeneral.setObservacion(diarioGeneral.getObservacion() + " #" + inventario.getCodigo());
                List<InventarioItems> inventarioItemsList = diarioGeneralService.getListInventarioItems(inventario);
                if (inventarioItemsList != null && !inventarioItemsList.isEmpty()) {
                    for (InventarioItems inventarioItems : inventarioItemsList) {
                        DetalleTransaccion detalle = new DetalleTransaccion();
                        if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_salidas")) {
                            detalle.setHaber(inventarioItems.getTotal());
                        } else if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_entradas")) {
                            detalle.setDebe(inventarioItems.getTotal());
                        }
                        detalle.setHaber(inventarioItems.getTotal());
                        //Verificar detalle cuenta_contable a cont_cuentas
//                        detalle.setCuentaContable(inventarioItems.getDetalleItem().getCuentaContable());
                        listAuxiliar.add(detalle);
                        if (!listCuentaContable.contains(detalle.getCuentaContable())) {
                            listCuentaContable.add(detalle.getCuentaContable());
                        }
                    }
                }
            }
            for (CuentaContable cuentacontable : listCuentaContable) {
                DetalleTransaccion detalleDG = new DetalleTransaccion();
                BigInteger bigInteger = BigInteger.valueOf(detalleDiarioGeneral.size() + 1);
                detalleDG.setDatoCargado(Boolean.TRUE);
                detalleDG.setContador(bigInteger);
                detalleDG.setDebe(BigDecimal.ZERO);
                detalleDG.setHaber(BigDecimal.ZERO);
                detalleDG.setCuentaContable(cuentacontable);
                for (DetalleTransaccion detalleTrans : listAuxiliar) {
                    if (detalleTrans.getCuentaContable().equals(cuentacontable)) {
                        if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_salidas")) {
                            detalleDG.setHaber(detalleDG.getHaber().add(detalleTrans.getHaber()));
                        } else if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_inventario_entradas")) {
                            detalleDG.setDebe(detalleDG.getDebe().add(detalleTrans.getDebe()));
                        }
                    }
                }
                detalleDiarioGeneral.add(detalleDG);
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar al menos uno antes de cargar los datos a la transaccion");
            return;
        }
        diarioGeneral.setObservacion("MÓDULO DE INVENTARIO: " + diarioGeneral.getObservacion().toUpperCase());
        calcularTotalesDetalleDiarioGeneral();
        this.botonCuentasContables = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('moduloInventarioDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    private void contabilizarModuloInventario() {
        if (inventariosSeleccionadoList != null && !inventariosSeleccionadoList.isEmpty()) {
            for (Inventario inventario : inventariosSeleccionadoList) {
                inventario.setContabilizado(Boolean.TRUE);
                inventario.setTransaccionContable(diarioGeneral);
                inventario.setFechaContable(diarioGeneral.getFechaElaboracion());
                inventario.setPeriodoContable(opcionBusqueda.getAnioAnterior());
                inventarioService.edit(inventario);
            }
        }
        restablecerModuloInventario();
    }

    private void restablecerModuloInventario() {
        inventariosSeleccionadoList = new ArrayList<>();
        catalogoMovimientoSeleccionado = new CatalogoMovimiento();
    }
//</editor-fold>

    /*MÓDULO: NÓMINA*/
    //<editor-fold defaultstate="collapsed" desc="Nómina">
    public void listadoTipoNomina() {
        this.rolesList = new ArrayList<>();
        this.rolesBeneficiosList = new ArrayList<>();
        this.detalleReservaList = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "rol_general1":
                case "rol_adicional1":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    if (subEnlaceSeleccionado.getCodigo().equals("rol_general1")) {
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnioAnterior(), "aprobado-rol", "rol_general");
                    } else {
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnioAnterior(), "aprobado-rol", "rol_adicional");
                    }
                    break;
                case "rol_beneficios":
                    this.renderedRolesBeneficios = Boolean.TRUE;
                    this.renderedRoles = Boolean.FALSE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesBeneficiosList = diarioGeneralService.getTiposRolesBeneficios(opcionBusqueda.getAnioAnterior(), "aprobado-rol");
                    break;
                case "rol_fondos_reserva":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.TRUE;
                    this.botonBeneficiarioFondosReserva = Boolean.TRUE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnioAnterior(), "aprobado-rol", 4);
                    break;
                case "rol_fondos_reserva_no_acumulado":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.TRUE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnioAnterior(), "aprobado-rol", 5);
                    break;
                case "rol_extras_suplementarias":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnioAnterior(), "aprobado-rol", "ROL-HORAS-EXTRAS");
                    break;
                case "provisiones_decimo_tercero":
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedListRol = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnioAnterior(), "registrado-rol", 2);
                    break;
                case "provisiones_decimo_cuarto":
                    this.renderedListRol = Boolean.FALSE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnioAnterior(), "registrado-rol", 3);
                    break;
                default:
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.FALSE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    break;
            }
        } else {
            this.renderedRolesBeneficios = Boolean.FALSE;
            this.renderedRoles = Boolean.FALSE;
            this.fondoReserva = Boolean.FALSE;
            this.botonBeneficiarioFondosReserva = Boolean.FALSE;
            rolesList = new ArrayList<>();
            rolesBeneficiosList = new ArrayList<>();
        }
    }

    public void fondosReservaRolGeneral() {
        if (tipoRolSeleccionado != null) {
            if (tipoRolSeleccionado.getTipoRol() != null && tipoRolSeleccionado.getTipoRol().getCodigo().equals("ROL-HORAS-EXTRAS")) {
                this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoRolHorasExtras(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioRolHorasExtras(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior());
            } else {
                if (fondoReserva) {
                    if (subEnlaceSeleccionado.getCodigo().equals("rol_fondos_reserva")) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS FONDO DE RESERVAS ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-FONDOS-RESERVA", true);
                    } else {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS FONDO DE RESERVAS NO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-FONDOS-RESERVA", false);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioFondoReserva(tipoRolSeleccionado);
                    }
                } else {
                    this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                    this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior());
                    this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior());
                }
            }
            if (cuentaContablePresupuestoModelList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "No hay datos para cargar");
            } else {
                openDlgRegistrarReservaCompromiso();
            }
        }
    }

    public void rolBeneficioSociales() {
        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DEL " + tipoRolBeneficiosSeleccionado.getTipo().getTexto()
                + " DESDE: " + Utils.dateFormatPattern("dd/MM/yyyy", tipoRolBeneficiosSeleccionado.getPeriodoDesde())
                + " HASTA: " + Utils.dateFormatPattern("dd/MM/yyyy", tipoRolBeneficiosSeleccionado.getPeriodoHasta()));
        switch (tipoRolBeneficiosSeleccionado.getTipo().getCodigo()) {
            case "ROL_TIPO_DEC_TERCERO":
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoDecimoTercero(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnioAnterior());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioDecimoTercero(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnioAnterior());
                break;
            case "ROL_TIPO_DEC_CUARTO":
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoDecimoCuarto(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnioAnterior());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiaripDecimoCuarto(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnioAnterior());
                break;
            case "ROL_TIPO_BEN_SIN":
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoBeneficiosSindicales(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnioAnterior());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioBeneficiosSindicales(tipoRolBeneficiosSeleccionado);
                break;
        }
        openDlgRegistrarReservaCompromiso();
    }

    public void rolBeneficiosMensuales() {
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "provisiones_decimo_tercero":
                    if (tipoRolSeleccionado != null) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS DECIMOS TERCER SUELDO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-DECIMO-3ro", true);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioProvisionales(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-DECIMO-3ro");
                    }
                    break;
                case "provisiones_decimo_cuarto":
                    if (tipoRolSeleccionado != null) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS DECIMOS CUARTO SUELDO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-DECIMO-4to", true);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioProvisionales(tipoRolSeleccionado, opcionBusqueda.getAnioAnterior(), "ACU-DECIMO-4to");
                    }
                    break;
            }
            openDlgRegistrarReservaCompromiso();
        }
    }

    private void openDlgRegistrarReservaCompromiso() {
        this.totalLiquidacion = 0;
        reservaCompromiso.setPeriodo(opcionBusqueda.getAnioAnterior());
        reservaCompromiso.setFechaSolicitud(diarioGeneral.getFechaElaboracion());
        UnidadAdministrativa unidad = diarioGeneralService.getUnidad("CONTABILIDAD PRESUPUESTO");
        if (unidad != null) {
            reservaCompromiso.setUnidadRequiriente(unidad);
        }
        this.cuentasConPartida = new ArrayList<>();
        for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentaContablePresupuestoModelList) {
            if (cuentaPresupuesto.getPartidaPresupuestaria() != null) {
                this.totalLiquidacion = redondearDosDecimales(totalLiquidacion + cuentaPresupuesto.getMonto_2().doubleValue());
                cuentasConPartida.add(cuentaPresupuesto);
            }
        }
        PrimeFaces.current().executeScript("PF('moduloNominaDlg').hide()");
        PrimeFaces.current().executeScript("PF('moduloNominaReservaCompromisoDlg').show()");
        PrimeFaces.current().ajax().update("moduloNominaReservaCompromiso");
    }

    public void guardarReserva() {
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);
        reservaCompromiso.setContabilizado(Boolean.TRUE);
        reservaCompromiso.setFechaContabilizado(diarioGeneral.getFechaElaboracion());
        reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);
        for (DetalleSolicitudCompromiso detalleReserva : detalleReservaList) {
            detalleReserva = detalleReservaService.create(detalleReserva);
            for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
                if (detalleDG.getCedulaPresupuestaria() != null && detalleDG.getCedulaPresupuestaria().equals(detalleReserva.getPresupuesto().getPartida())) {
                    detalleDG.setIdDetalleReserva(detalleReserva);
                }
            }
        }
        for (BeneficiarioSolicitudReserva beneficiario : beneficiarioReservaCompromisoList) {
            if (beneficiario.getTipoBeneficiario()) {
                reservaCompromiso.setBeneficiario(beneficiario.getBeneficiario());
                reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
                reservaCompromiso.setNomina(Boolean.FALSE);
            } else {
                reservaCompromiso.setNomina(Boolean.TRUE);
                beneficiario.setReservaCompromiso(reservaCompromiso);
                beneficiarioSolicitudReservaService.create(beneficiario);
            }
            reservaCompromisoService.edit(reservaCompromiso);
        }
        if (reservaCompromiso.getNomina()) {
            diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
        } else {
            diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
        }
        diarioGeneral.setCertificacionesPresupuestario(reservaCompromiso);
    }

    public void generarResevaCompromiso() {
        if (beneficiarioReservaCompromisoList.isEmpty() && beneficiarioReservaCompromisoList == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un Beneficiario");
            return;
        }
        int contador = 0;
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            if (cuentaPartida.getMonto_3().doubleValue() <= 0) {
                contador += 1;
            }
        }
        if (contador > 0) {
            JsfUtil.addWarningMessage("AVISO", "Tiene valores negativos en el listado de la liquidacion de rol");
            return;
        }
        /*Creamos la Reserva de Compromiso*/
        reservaCompromiso.setFechaCreacion(diarioGeneral.getFechaElaboracion());
        reservaCompromiso.setUsuarioCreacion(userSession.getName());
        reservaCompromiso.setEstado(diarioGeneralService.getEstadoAprobado("APRO"));
        reservaCompromiso.setComprometido(Boolean.TRUE);
        reservaCompromiso.setFechaActualizacion(diarioGeneral.getFechaElaboracion());
        reservaCompromiso.setFechaAprobacion(diarioGeneral.getFechaElaboracion());
        reservaCompromiso.setDescripcion(reservaCompromiso.getDescripcion().toUpperCase());
        Procedimiento procedimiento = procedimientoService.getProcedimientoNoAplica("NO APLICA");
        if (procedimiento != null) {
            reservaCompromiso.setProcedimiento(procedimiento);
        }
        //reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);
        /*Guardamos el detalle de la reserva de compromiso*/
        for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentasConPartida) {
            DetalleSolicitudCompromiso detalleSolicitudReserva = new DetalleSolicitudCompromiso();
            detalleSolicitudReserva.setSolicitud(reservaCompromiso);
            detalleSolicitudReserva.setMontoDisponible(cuentaPresupuesto.getMonto_1());
            detalleSolicitudReserva.setMontoSolicitado(cuentaPresupuesto.getMonto_2());
            detalleSolicitudReserva.setEstado(Boolean.TRUE);
            detalleSolicitudReserva.setPeriodo(opcionBusqueda.getAnioAnterior());
            detalleSolicitudReserva.setUsuarioCreacion(userSession.getName());
            detalleSolicitudReserva.setFechaCreacion(new Date());
            detalleSolicitudReserva.setPresupuesto(diarioGeneralService.getPresupuesto(cuentaPresupuesto.getPartidaPresupuestaria(), opcionBusqueda.getAnioAnterior()));
            detalleSolicitudReserva.setMontoComprometido(cuentaPresupuesto.getMonto_3());
            detalleReservaList.add(detalleSolicitudReserva);
        }
        /*Pasamos la informacion de la reserva de compromiso al detalle del diario General*/
        if (diarioGeneral.getClase() == null) {
            diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_egreso"));
        }
        actualizarTipoDiarioGeneral();
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneral.setObservacion("MÓDULO DE NÓMINA: P.R. " + reservaCompromiso.getDescripcion());
        for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentaContablePresupuestoModelList) {
            DetalleTransaccion detalleDG = new DetalleTransaccion();
            detalleDG.setDatoCargado(Boolean.TRUE);
            int linea = detalleDiarioGeneral.size() + 1;
            BigInteger bigInteger = BigInteger.valueOf(linea);
            detalleDG.setContador(bigInteger);
            detalleDG.setCuentaContable(cuentaPresupuesto.getCuentaContable());
            detalleDG.setDebe(BigDecimal.ZERO);
            detalleDG.setHaber(BigDecimal.ZERO);
            if (cuentaPresupuesto.getPartidaPresupuestaria() != null) {
                detalleDG.setDebe(cuentaPresupuesto.getMonto_2());
                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                detalleDG.setCedulaPresupuestaria(cuentaPresupuesto.getPartidaPresupuestaria());
                detalleDG.setFuente(cuentaPresupuesto.getFuenteDirecta());
                detalleDG.setPartidaPresupuestaria(cuentaPresupuesto.getCatalogoPresupuesto());
                detalleDG.setEstructuraProgramatica(cuentaPresupuesto.getPlanProgramatico());
                detalleDG.setComprometido(detalleDG.getDebe());
                detalleDG.setDevengado(detalleDG.getDebe());
            } else {
                detalleDG.setHaber(cuentaPresupuesto.getMonto_2());
            }
            detalleDiarioGeneral.add(detalleDG);
        }
        JsfUtil.addSuccessMessage("INFO", "La Reserva de Compromiso se generó correctamente");
        calcularTotalesDetalleDiarioGeneral();
        this.botonCuentasContables = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('moduloNominaReservaCompromisoDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    public void openDlgProveedor() {
        this.proveedorLazyModel = new LazyModel<>(Proveedor.class);
        this.proveedorLazyModel.getSorteds().put("id", "ASC");
        this.proveedorLazyModel.getFilterss().put("estado", true);
        PrimeFaces.current().executeScript("PF('proveedorDlg').show()");
        PrimeFaces.current().ajax().update("proveedorTable");
    }

    public void añadirBeneficiarioProveedor(Proveedor proveedor) {
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.proveedorSeleccionado = proveedor;
        BeneficiarioSolicitudReserva beneficiario = new BeneficiarioSolicitudReserva();
        beneficiario.setTipoBeneficiario(Boolean.TRUE);
        beneficiario.setBeneficiario(proveedor.getCliente());
        this.beneficiarioReservaCompromisoList.add(beneficiario);
        this.fondoReserva = Boolean.FALSE;
        PrimeFaces.current().ajax().update("proveedorTable");
        PrimeFaces.current().ajax().update("fieldsetBeneficiario");
        PrimeFaces.current().executeScript("PF('proveedorDlg').hide()");
    }

    private void contabilizarModuloNomina() {
        if (tipoRolSeleccionado != null && tipoRolSeleccionado.getId() != null) {
            tipoRolSeleccionado.setEstadoAprobacion(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("pagado-rol", "estados_tipo_rol"));
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "rol_general1":
                    tipoRolSeleccionado.setDiarioRolGeneral(Boolean.TRUE);
                    tipoRolSeleccionado.setDiarioGeneralRol(diarioGeneral);
                    diarioGeneralService.getActualizarAnticipo(tipoRolSeleccionado, beneficiarioReservaCompromisoList, diarioGeneral);
                    break;
                case "rol_fondos_reserva_no_acumulado":
                    tipoRolSeleccionado.setDiarioRolGeneral(Boolean.TRUE);
                    tipoRolSeleccionado.setDiarioGeneralRol(diarioGeneral);
                    break;
                case "rol_fondos_reserva":
                    tipoRolSeleccionado.setDiarioFondosReserva(Boolean.TRUE);
                    tipoRolSeleccionado.setDiarioGeneralFondos(diarioGeneral);
                    break;
                case "rol_adicional1":
                    tipoRolSeleccionado.setDiarioRolGeneral(Boolean.TRUE);
                    tipoRolSeleccionado.setDiarioGeneralRol(diarioGeneral);
                    break;
            }
            tipoRolService.edit(tipoRolSeleccionado);
        }
        if (tipoRolBeneficiosSeleccionado != null) {
            tipoRolBeneficiosSeleccionado.setEstadoAprobacionBen(catalogoItemService.findCatalogoItemByCodigoAndCatalogo_Codigo("pagado-rol", "estados_tipo_rol"));
            tipoRolBeneficiosSeleccionado.setDiarioRolBeneficios(Boolean.TRUE);
            tipoRolBeneficiosSeleccionado.setDiarioGeneral(diarioGeneral);
            tipoRolBeneficiosService.edit(tipoRolBeneficiosSeleccionado);
        }
        restablecerModuloNomina();
    }

    private void restablecerModuloNomina() {
        this.totalLiquidacion = 0;
        this.rolesBeneficiosList = new ArrayList<>();
        this.rolesList = new ArrayList<>();
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.proveedorSeleccionado = new Proveedor();
    }
//</editor-fold>

    /*MODULO: RESERVA COMPROMISO*/
    //<editor-fold defaultstate="collapsed" desc="Reserva de compromiso">
    public void actualizarTablaReservaCompromiso() {
        if (tipoSeleccionado != null) {
            reservaCompromisoList = diarioGeneralService.getAdquisiciones("APRO", opcionBusqueda.getAnioAnterior(), tipoSeleccionado, diarioGeneral.getFechaElaboracion());
        } else {
            reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnioAnterior(), diarioGeneral.getFechaElaboracion());
        }
        PrimeFaces.current().ajax().update("reservaCompromisoTable");
        PrimeFaces.current().ajax().update("moduloReservaCompromisoForm");
    }

    public void openDlgDetalleReservaCompromiso(SolicitudReservaCompromiso reservaC) {
        this.reservaCompromiso = reservaC;
        this.cuentasConPartida = new ArrayList<>();
        this.filtroPartidas = new ArrayList<>();
        this.adquisicion = new Adquisiciones();
        this.adquisicionesList = new ArrayList<>();
        this.booleanAdquisiciones = Boolean.FALSE;
        this.cuentaContablePresupuestoModelList = diarioGeneralService.getDetalleReservaCompromiso(reservaCompromiso, opcionBusqueda.getAnioAnterior());
        this.adquisicionesList = diarioGeneralService.getAdquisicionList(reservaCompromiso);
        if (!adquisicionesList.isEmpty()) {
            if (adquisicionesList.size() > 1) {
                booleanAdquisiciones = Boolean.TRUE;
            } else {
                this.adquisicion = adquisicionesList.get(0);
                booleanAdquisiciones = Boolean.FALSE;
            }
        }
        PrimeFaces.current().executeScript("PF('moduloReservaCompromisoDlg').hide()");
        PrimeFaces.current().executeScript("PF('detalleReservaCompromisoDlg').show()");
        PrimeFaces.current().ajax().update("detalleReservaCompromisoForm");
    }

    public void tipoDevengado(CuentaContablePresupuestoModel cuentaPresupuesto) {
        if (cuentaPresupuesto.getDevengadoTotal()) {
            cuentaPresupuesto.setMonto_3(cuentaPresupuesto.getMonto_2());
            cuentaPresupuesto.setParcialTotal(diarioGeneralService.getTipoDevengado("devengado_total"));
        } else {
            cuentaPresupuesto.setMonto_3(BigDecimal.ZERO);
            cuentaPresupuesto.setParcialTotal(diarioGeneralService.getTipoDevengado("devengado_parcial"));
        }
    }

    public void validarValorDevengado(CuentaContablePresupuestoModel cuentaPresupuesto) {
        if (cuentaPresupuesto.getMonto_3().doubleValue() > cuentaPresupuesto.getMonto_2().doubleValue()) {
            JsfUtil.addWarningMessage("AVISO", "El monto del devengado es mayor al monto disponible");
            cuentaPresupuesto.setMonto_3(BigDecimal.ZERO);
        }
    }

    public void cargarPartidasDiarioGeneral() {
        int contador = 0;
        if (cuentasConPartida.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Debe Seleccionar al menos una partida");
            return;
        }
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            if (cuentaPartida.getMonto_3().doubleValue() == 0) {
                contador += 1;
            }
        }
        if (contador > 0) {
            JsfUtil.addWarningMessage("AVISO", "La partida presupuestaria seleccionada no tiene valor a devengar");
            return;
        }
        if (booleanAdquisiciones) {
            if (adquisicion == null || adquisicion.getId() == null) {
                JsfUtil.addWarningMessage("AVISO", "Debe Seleccionar un contrato");
                return;
            } else {
                diarioGeneral.setBeneficiario(adquisicion.getProveedor().getCliente());
                diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
                diarioGeneral.setTipoBeneficiario(Boolean.FALSE);
            }
        }
        contador = 0;
        Boolean accion = false;
        relacionCuentasContables = new ArrayList<>();
        List<CuentaContable> auxiliarList;
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            auxiliarList = diarioGeneralService.getCuentasContables(cuentaPartida.getCatalogoPresupuesto(), opcionBusqueda.getAnioAnterior());
            if (auxiliarList != null) {
                contador += 1;
            }
        }
        if (contador == cuentasConPartida.size()) {
            contador = 0;
            for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
                DetalleTransaccion detalleDG = new DetalleTransaccion();
                detalleDG.setDatoCargado(Boolean.TRUE);
                contador += 1;
                BigInteger bigInteger = BigInteger.valueOf(contador);
                detalleDG.setContador(bigInteger);
                /*Evaluamos las relaciones de la cuentas contables con catalogo presupuesto*/
                auxiliarList = diarioGeneralService.getCuentasContables(cuentaPartida.getCatalogoPresupuesto(), opcionBusqueda.getAnioAnterior());
                if (auxiliarList.size() > 1) {
                    accion = true;
                    detalleDG.setSeleccionarCuentaContable(auxiliarList);
                } else if (auxiliarList.size() == 1) {
                    detalleDG.setCuentaContable(auxiliarList.get(0));
                }
                detalleDG.setDebe(cuentaPartida.getMonto_3());
                detalleDG.setHaber(BigDecimal.ZERO);
                detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                detalleDG.setCedulaPresupuestaria(cuentaPartida.getPartidaPresupuestaria());
                detalleDG.setEstructuraProgramatica(cuentaPartida.getPlanProgramatico());
                detalleDG.setPartidaPresupuestaria(cuentaPartida.getCatalogoPresupuesto());
                detalleDG.setFuente(cuentaPartida.getFuenteDirecta());
                detalleDG.setTipoDevengado(cuentaPartida.getParcialTotal());
                detalleDG.setComprometido(cuentaPartida.getMonto_1());
                detalleDG.setDevengado(cuentaPartida.getMonto_3());
                detalleDG.setEjecutado(BigDecimal.ZERO);
                detalleDG.setIdDetalleReserva(cuentaPartida.getIdDetalleReserva());
                detalleDiarioGeneral.add(detalleDG);
                /*Añadimos los filtros para las ceuntas contable*/
                if (!filtroPartidas.contains(detalleDG.getPartidaPresupuestaria())) {
                    filtroPartidas.add(detalleDG.getPartidaPresupuestaria());
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "Antes de insertar las partidas al diario general, verificar las relaciones de cuenta contable y catálogo presupuesto"));
            return;
        }
        if (accion) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "Revisar el detalle de la transaccon, debido a que la partida presupuestraia esta relacionado con más de 2 cuentas contables"));
        }
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_egreso"));
        actualizarTipoDiarioGeneral();
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneral.setObservacion(reservaCompromiso.getDescripcion().toUpperCase());
        diarioGeneral.setObservacion("MÓDULO DE RESERVA DE COMPROMISO: P.R. " + diarioGeneral.getObservacion().toUpperCase());
        this.botonCuentasContables = Boolean.TRUE;
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().executeScript("PF('detalleReservaCompromisoDlg').hide()");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
        PrimeFaces.current().ajax().update("mensaje");
    }

    private void contabilizarReservaCompromiso() {
        int contador = 0;
        if (reservaCompromiso != null) {
            for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentaContablePresupuestoModelList) {
                BigDecimal totalPartidasRegistradas = diarioGeneralService.totalPartidaRegistrada(reservaCompromiso, cuentaPresupuesto.getPartidaPresupuestaria(), cuentaPresupuesto.getIdDetalleReserva());
                if (cuentaPresupuesto.getMonto_1().doubleValue() == totalPartidasRegistradas.doubleValue()) {
                    contador += 1;
                }
            }
            if (cuentaContablePresupuestoModelList.size() == contador) {
                reservaCompromiso.setUsuarioModificacion(userSession.getName());
                reservaCompromiso.setFechaModificacion(new Date());
                reservaCompromiso.setContabilizado(Boolean.TRUE);
                reservaCompromiso.setFechaContabilizado(new Date());
                reservaCompromisoService.edit(reservaCompromiso);
            }
            restablecerModuloReservaCompromiso();
        }
    }

    private void restablecerModuloReservaCompromiso() {
        this.cuentasConPartida = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.adquisicionesList = new ArrayList<>();
    }
//</editor-fold>

    /*MÓDULO: TESORERÍA*/
    //<editor-fold defaultstate="collapsed" desc="Tesorería">
    private void inicializarBooleanTesoreria() {
        this.tableCorteOrdenCobro = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
        this.procesoCobroEmisor = Boolean.FALSE;
        this.tableRetenciones = Boolean.FALSE;
        this.tableArriendo = Boolean.FALSE;
    }

    public void procesoTesoreriaSeleccionado() {
        if (subEnlaceSeleccionado != null) {
            this.retencionesSeleccionadas = new ArrayList<>();
            this.garantiasList = new ArrayList<>();
            this.garantiasSeleccionadasList = new ArrayList<>();
            this.corteOrdenCobroLazyModel = new LazyModel<>(CorteOrdenCobro.class);
            this.corteOrdenCobroLazyModel.getSorteds().put("codigo", "ASC");
            this.corteOrdenCobroLazyModel.getFilterss().put("estado", true);
            this.corteOrdenCobroLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnioAnterior());
            this.recaudacionlLazyModel = new LazyModel<>(Recaudacion.class);
            this.recaudacionlLazyModel.getSorteds().put("codigo", "ASC");
            this.recaudacionlLazyModel.getFilterss().put("estado", true);

            switch (subEnlaceSeleccionado.getCodigo()) {
                case "emisiones":
                    inicializarBooleanTesoreria();
                    this.tableCorteOrdenCobro = Boolean.TRUE;
                    this.procesoCobroEmisor = Boolean.TRUE;
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoEmision", false);
                    break;
                case "cobros_caja":
                    inicializarBooleanTesoreria();
                    this.tableCorteOrdenCobro = Boolean.TRUE;
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoEmision", true);
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoCobroCaja", false);
                    break;
                case "recaudaciones_cobros":
                    inicializarBooleanTesoreria();
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar", 0);
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "COBROEM");
                    break;
                case "recaudaciones_ajustes":
                    inicializarBooleanTesoreria();
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "recaudaciones_cobros_ajustes":
                    inicializarBooleanTesoreria();
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar:gt", 0);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "retenciones":
                    inicializarBooleanTesoreria();
                    this.periodosFiscales = diarioGeneralService.getPeriodosFiscales(opcionBusqueda.getAnioAnterior());
                    this.tipoRetenciones = diarioGeneralService.getTiposRetenciones("RETENCION");
                    break;
                case "garantias":
                    inicializarBooleanTesoreria();
                    this.adquisicionesLazyModel = new LazyModel<>(Adquisiciones.class);
                    this.adquisicionesLazyModel.getSorteds().put("id", "ASC");
                    this.adquisicionesLazyModel.getFilterss().put("estado", true);
                    this.adquisicionesLazyModel.getFilterss().put("garantia", true);
                    break;
                case "ordenes_cobro":
                case "ordenes_ingreso":
                    if (subEnlaceSeleccionado.getCodigo().equals("ordenes_cobro")) {
                        periodoBoolean = false;
                    } else if (subEnlaceSeleccionado.getCodigo().equals("ordenes_ingreso")) {
                        periodoBoolean = true;
                    }
                    this.ordenesEmitidasList = new ArrayList<>();
                    this.cuentaContablePresupuestoModelList = new ArrayList<>();
                    this.ordenesEmitidasLazyModel = null;
                    this.mesSeleccionado = null;
                    inicializarBooleanTesoreria();
                    this.tableArriendo = Boolean.TRUE;
                    break;
            }
        } else {
            inicializarBooleanTesoreria();
        }
    }

    public void actualizarTablaRetenciones() {
        if (tipoRetencionSeleccionado != null && periodoSeleccionado != null) {
            this.liquidacionDetalleList = diarioGeneralService.getListDetalleRetencion(opcionBusqueda.getAnioAnterior(), tipoRetencionSeleccionado, periodoSeleccionado, "RECIBIDA;AUTORIZADO");
        } else {
            this.liquidacionDetalleList = new ArrayList<>();
        }
        this.tableRetenciones = Boolean.TRUE;
        PrimeFaces.current().ajax().update("retencionTable");
    }

    public void retencionesSeleccionadas() {
        if (retencionesSeleccionadas.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar al menos una retención");
        } else {
            retencionesRegistradasList = new ArrayList<>();
            BigInteger bigInteger;
            for (LiquidacionDetalle retencion : retencionesSeleccionadas) {
                List<CuentaContablePresupuestoModel> cuentaPresupuesto = null;// diarioGeneralService.getListPartidasSaldos(retencion.getCuentaContableRetencion().getCuentaContable(), retencion.getLiquidacion().getDiarioGeneral());
                if (cuentaPresupuesto != null) {
                    if (cuentaPresupuesto.size() > 1) {
                        cicloDetalleDiarioGeneral(cuentaPresupuesto, retencion.getValor().doubleValue(), retencion);
                    } else {
                        for (CuentaContablePresupuestoModel cuenta : cuentaPresupuesto) {
                            DetalleTransaccion detalleDG = new DetalleTransaccion();
                            bigInteger = BigInteger.valueOf(detalleDiarioGeneral.size() + 1);
                            detalleDG.setContador(bigInteger);
//                            detalleDG.setCuentaContable(retencion.getCuentaContableRetencion().getCuentaContable());
                            detalleDG.setDebe(retencion.getValor());
                            detalleDG.setHaber(BigDecimal.ZERO);
                            detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                            detalleDG.setPartidaPresupuestaria(cuenta.getCatalogoPresupuesto());
                            detalleDG.setEstructuraProgramatica(cuenta.getPlanProgramatico());
                            detalleDG.setCedulaPresupuestaria(cuenta.getPartidaPresupuestaria());
                            detalleDG.setFuente(cuenta.getFuenteDirecta());
                            detalleDG.setEjecutado(detalleDG.getDebe());
                            detalleDiarioGeneral.add(detalleDG);
                            listadoRetencionesRegistradas(retencion, detalleDG);
                        }
                    }
                }
            }
            diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
            actualizarTipoDiarioGeneral();
            diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
            diarioGeneral.setObservacion(periodoSeleccionado);
            diarioGeneral.setObservacion("MÓDULO DE TESORERÍA: P.R. PAGO DE RETENCIONES DE " + tipoRetencionSeleccionado + " DEL PERÍODO FISCAL " + periodoSeleccionado);
            this.botonCuentasContables = Boolean.TRUE;
            calcularTotalesDetalleDiarioGeneral();
            PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
            PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
            PrimeFaces.current().ajax().update("formDiarioGeneral");
        }
    }

    private void cicloDetalleDiarioGeneral(List<CuentaContablePresupuestoModel> cuentaPresupuestoList, double valor, LiquidacionDetalle retencion) {
        double diferencia = valor;
        for (CuentaContablePresupuestoModel cuenta : cuentaPresupuestoList) {
            DetalleTransaccion detalleDG = new DetalleTransaccion();
            BigInteger bigInteger = BigInteger.valueOf(detalleDiarioGeneral.size() + 1);
            detalleDG.setContador(bigInteger);
//            detalleDG.setCuentaContable(retencion.getCuentaContableRetencion().getCuentaContable());
            detalleDG.setDebe(BigDecimal.ZERO);
            detalleDG.setHaber(BigDecimal.ZERO);
            detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
            detalleDG.setPartidaPresupuestaria(cuenta.getCatalogoPresupuesto());
            detalleDG.setEstructuraProgramatica(cuenta.getPlanProgramatico());
            detalleDG.setCedulaPresupuestaria(cuenta.getPartidaPresupuestaria());
            detalleDG.setFuente(cuenta.getFuenteDirecta());
            if (cuenta.getMonto_3().doubleValue() > 0) {
                if (cuenta.getMonto_3().doubleValue() > diferencia) {
                    detalleDG.setEjecutado(new BigDecimal(diferencia));
                    diferencia = 0;
                } else {
                    detalleDG.setEjecutado(cuenta.getMonto_3());
                    diferencia = diferencia - cuenta.getMonto_3().doubleValue();
                    cuenta.setMonto_3(BigDecimal.ZERO);
                }
                detalleDiarioGeneral.add(detalleDG);
                listadoRetencionesRegistradas(retencion, detalleDG);
            }
        }
    }

    private void listadoRetencionesRegistradas(LiquidacionDetalle retencion, DetalleTransaccion detalleDG) {
        if (detalleDG.getCedulaPresupuestaria() != null) {
            RetencionesRegistradas retencionR = new RetencionesRegistradas();
//            retencionR.setDiarioGeneral(retencion.getLiquidacion().getDiarioGeneral());
            retencionR.setMonto(detalleDG.getEjecutado());
            retencionR.setPartidaPresupuestaria(detalleDG.getCedulaPresupuestaria());
            retencionesRegistradasList.add(retencionR);
        }
    }

    public void corteOrdenSeleccionada(CorteOrdenCobro corteOrden) {
        this.corteOrdenCobroSeleccionada = new CorteOrdenCobro();
        this.corteOrdenCobroSeleccionada = corteOrden;
        if (corteOrdenCobroSeleccionada != null) {
            diarioGeneral.setFechaElaboracion(corteOrdenCobroSeleccionada.getFechaCorte());
            String tipoProceso;
            if (procesoCobroEmisor) {
                diarioGeneral.setObservacion("P.R. EMISIÓN DE COBRO " + corteOrdenCobroSeleccionada.getCodigoDes());
                diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_ingreso"));
                tipoProceso = "EMISION";
            } else {
                diarioGeneral.setObservacion("P.R. COBROS A CAJA DE " + corteOrdenCobroSeleccionada.getCodigoDes());
                diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
                tipoProceso = "COBROS";
            }
            actualizarTipoDiarioGeneral();
            diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
            guardarDatosLisTransacciones(diarioGeneralService.getCobrosCajasList(corteOrdenCobroSeleccionada, true, tipoProceso), true);
            guardarDatosLisTransacciones(diarioGeneralService.getCobrosCajasList(corteOrdenCobroSeleccionada, false, tipoProceso), false);
            diarioGeneral.setObservacion("MÓDULO DE TESORERÍA: " + diarioGeneral.getObservacion().toUpperCase());
            calcularTotalesDetalleDiarioGeneral();
            this.botonCuentasContables = Boolean.TRUE;
        }
        PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    private void guardarDatosLisTransacciones(List<CobrosEmisionesModel> cobrosList, Boolean accion) {
        if (cobrosList != null && !cobrosList.isEmpty()) {
            for (CobrosEmisionesModel cobrosCaja : cobrosList) {
                DetalleTransaccion detalleDG = new DetalleTransaccion();
                detalleDG.setDatoCargado(Boolean.TRUE);
                int linea = detalleDiarioGeneral.size() + 1;
                BigInteger bigInteger = BigInteger.valueOf(linea);
                detalleDG.setContador(bigInteger);
                detalleDG.setCuentaContable(cobrosCaja.getCuentacontable());
                detalleDG.setDebe(BigDecimal.ZERO);
                detalleDG.setHaber(BigDecimal.ZERO);
                if (Objects.equals(accion, procesoCobroEmisor)) {
                    detalleDG.setHaber(cobrosCaja.getMontototal());
                    detalleDG.setPartidaPresupuestaria(cobrosCaja.getItempresupuestario().getItem());
                    detalleDG.setEstructuraProgramatica(cobrosCaja.getItempresupuestario().getEstructura());
                    detalleDG.setFuente(cobrosCaja.getItempresupuestario().getFuenteDirecta());
                    detalleDG.setCedulaPresupuestaria(cobrosCaja.getItempresupuestario().getPartida());
                    if (procesoCobroEmisor) {
                        detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                        detalleDG.setDevengado(detalleDG.getHaber());
                    } else {
                        detalleDG.setTipoTransaccion(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleDG.setEjecutado(detalleDG.getHaber());
                    }
                } else {
                    detalleDG.setDebe(cobrosCaja.getMontototal());
                }
                detalleDiarioGeneral.add(detalleDG);
            }
        }
    }

    public void recaudacionSeleccionada(Recaudacion recaudacion) {
        this.recaudacionSelecionada = new Recaudacion();
        this.recaudacionSelecionada = recaudacion;
        /*INGRESO DE INFORMACION DEL DIARIO GENERAL*/
        diarioGeneral.setObservacion("MÓDULO DE TESORERÍA: P.R. RECAUDACIÓN DEL " + recaudacionSelecionada.getCodigo()
                + ", BANCO ORIGEN: '" + recaudacionSelecionada.getAgenciaOrigen() + "' AL BANCO DESTINO: '"
                + recaudacionSelecionada.getAgenciaDestino().getNombreBanco().getNombreBanco()
                + "' CON EL VALOR DE $");
        diarioGeneral.setFechaElaboracion(recaudacionSelecionada.getFechaAfeccion());
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        actualizarTipoDiarioGeneral();
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        BigDecimal bigDecimal = BigDecimal.ZERO;
        switch (subEnlaceSeleccionado.getCodigo()) {
            case "recaudaciones_cobros":
                bigDecimal = diarioGeneralService.getSaldoCobroAjuste(recaudacionSelecionada, true);
                guardarDetalleTransaccion(recaudacionSelecionada.getAgenciaDestino().getCuentaMovimiento(), bigDecimal, false);
                guardarDetalleTransaccion(diarioGeneralService.getCuentaContableTesoreria(recaudacionSelecionada), bigDecimal, true);
                break;
            case "recaudaciones_ajustes":
                guardarDetalleTransaccion(recaudacionSelecionada.getAgenciaDestino().getCuentaMovimiento(), recaudacionSelecionada.getValor(), false);
                guardarDetalleTransaccion(recaudacionSelecionada.getCuentaContable(), recaudacionSelecionada.getValor(), true);
                break;
            case "recaudaciones_cobros_ajustes":
                bigDecimal = diarioGeneralService.getSaldoCobroAjuste(recaudacionSelecionada, false);
                guardarDetalleTransaccion(recaudacionSelecionada.getCuentaContable(), bigDecimal, false);
                guardarDetalleTransaccion(diarioGeneralService.getCuentaContableTesoreria(recaudacionSelecionada), bigDecimal, true);
                break;
        }
        calcularTotalesDetalleDiarioGeneral();
        diarioGeneral.setObservacion(diarioGeneral.getObservacion() + bigDecimal);
        this.botonCuentasContables = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    private void guardarDetalleTransaccion(CuentaContable cuenta, BigDecimal valor, Boolean accion) {
        DetalleTransaccion detalleDG = new DetalleTransaccion();
        detalleDG.setDatoCargado(Boolean.TRUE);
        detalleDG.setContador(BigInteger.valueOf(this.detalleDiarioGeneral.size() + 1));
        detalleDG.setCuentaContable(cuenta);
        if (accion) {
            detalleDG.setDebe(BigDecimal.ZERO);
            detalleDG.setHaber(valor);
        } else {
            detalleDG.setDebe(valor);
            detalleDG.setHaber(BigDecimal.ZERO);
        }
        detalleDiarioGeneral.add(detalleDG);
    }

    public Boolean polizaVigente(Date fechaHasta) {
        return fechaHasta.compareTo(new Date()) > 0;
    }

    public void tipoGarantiaSeleccionado() {
        if (tipoGarantia != null) {
            this.garantiasList = diarioGeneralService.getGarantiasList(tipoGarantia);
        } else {
            this.garantiasList = new ArrayList<>();
        }
        PrimeFaces.current().ajax().update("garantiasTable");
    }

    public void garantiasSeleccionadas() {
        if (garantiasSeleccionadasList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar al menos una poliza");
        } else {
            for (Garantias garantia : garantiasSeleccionadasList) {
                DetalleTransaccion detalleDG = new DetalleTransaccion();
                detalleDG.setDatoCargado(Boolean.TRUE);
                int linea = detalleDiarioGeneral.size() + 1;
                BigInteger bigInteger = BigInteger.valueOf(linea);
                detalleDG.setContador(bigInteger);
//                detalleDG.setCuentaContable(garantia.getCuentaContable());
                detalleDG.setDebe(BigDecimal.ZERO);
                detalleDG.setHaber(BigDecimal.ZERO);
                if (garantia.getDevolucion()) {
                    detalleDG.setHaber(garantia.getSuma());
                } else {
                    if (polizaVigente(garantia.getFechaHasta())) {
                        detalleDG.setDebe(garantia.getSuma());
                    } else {
                        detalleDG.setHaber(garantia.getSuma());
                    }
                }
                detalleDiarioGeneral.add(detalleDG);
            }
            this.botonCuentasContables = Boolean.TRUE;
            diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
            actualizarTipoDiarioGeneral();
            diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_ajuste"));
            calcularTotalesDetalleDiarioGeneral();
            PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
            PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
            PrimeFaces.current().ajax().update("formDiarioGeneral");
        }
    }

    public void actualizarTablaArriendos() {
        if (mesSeleccionado != null && subEnlaceSeleccionado != null && fechaBusquedaArriendo != null) {
            this.ordenesEmitidasLazyModel = new LazyModel<>(OrdenesEmitidas.class);
            this.ordenesEmitidasLazyModel.getSorteds().put("mes", "ASC");
            this.ordenesEmitidasLazyModel.getFilterss().put("mes", mesSeleccionado.getOrden());
            this.ordenesEmitidasLazyModel.getFilterss().put("anio", periodo);
            this.ordenesEmitidasLazyModel.getFilterss().put("estado", true);
            if (subEnlaceSeleccionado.getCodigo().equals("ordenes_cobro")) {
                this.ordenesEmitidasLazyModel.getFilterss().put("diarioCobrar", false);
            } else {
                this.ordenesEmitidasLazyModel.getFilterss().put("diarioPagar", false);
                this.ordenesEmitidasLazyModel.getFilterss().put("ordenPagada", true);
            }
            if (periodoBoolean) {
                this.ordenesEmitidasLazyModel.getFilterss().put("fechaPago", fechaBusquedaArriendo);
            } else {
                this.ordenesEmitidasLazyModel.getFilterss().put("fechaEmision", fechaBusquedaArriendo);
            }
        } else {
            this.ordenesEmitidasLazyModel = null;
        }
    }

    public void arriendosSeleccionados() {
        List<String> aux = new ArrayList<>();
        List<CuentaContablePresupuestoModel> aux_1 = new ArrayList<>();
        if (ordenesEmitidasList != null && !ordenesEmitidasList.isEmpty()) {
            for (OrdenesEmitidas arriendo : ordenesEmitidasList) {
//                if (subEnlaceSeleccionado.getCodigo().equals("ordenes_cobro")) {
//                    ordernarCuentasArriendo(arriendo.getIdArrendamiento().getLocal().getIdTarifa().getCuentaCobro(), arriendo.getMontoPagar(), true, null, aux, aux_1);
//                    ordernarCuentasArriendo(arriendo.getIdArrendamiento().getLocal().getIdTarifa().getCuentaIngreso(), arriendo.getMontoPagar(), false, arriendo.getIdArrendamiento().getLocal().getIdTarifa().getItemPresupuesto(), aux, aux_1);
//                } else {
//                    ordernarCuentasArriendo(arriendo.getIdArrendamiento().getLocal().getIdTarifa().getCuentaCobro(), arriendo.getMontoPagar(), false, arriendo.getIdArrendamiento().getLocal().getIdTarifa().getItemPresupuesto(), aux, aux_1);
//                    ordernarCuentasArriendo(arriendo.getIdArrendamiento().getLocal().getIdTarifa().getCuentaBanco(), arriendo.getMontoPagar(), true, null, aux, aux_1);
//                }
            }
            agruparListado(aux_1);
            calcularTotalesDetalleDiarioGeneral();
            diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
            diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
            actualizarTipoDiarioGeneral();
            botonCuentasContables = Boolean.TRUE;
            diarioGeneral.setFechaElaboracion(fechaBusquedaArriendo);
            diarioGeneral.setObservacion("MÓDULO DE TESORERÍA: P.R. DE ORDENES DE " + subEnlaceSeleccionado.getDescripcion() + " DEL MES DE " + mesSeleccionado.getTexto());
            PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
            PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
            PrimeFaces.current().ajax().update("formDiarioGeneral");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay arriendos Seleccionados");
        }
    }

    private void ordernarCuentasArriendo(CuentaContable cuenta, BigDecimal monto, Boolean accion, Presupuesto presupuesto, List<String> aux, List<CuentaContablePresupuestoModel> aux_1) {
        CuentaContablePresupuestoModel c = new CuentaContablePresupuestoModel();
        c.setCuentaContable(cuenta);
        if (accion) {
            c.setMonto_1(monto);
            c.setMonto_2(BigDecimal.ZERO);
        } else {
            c.setMonto_1(BigDecimal.ZERO);
            c.setMonto_2(monto);
        }
        c.setMonto_3(BigDecimal.ZERO);
        c.setMonto_4(BigDecimal.ZERO);
        if (presupuesto != null) {
            c.setCatalogoPresupuesto(presupuesto.getItem());
            c.setFuenteDirecta(presupuesto.getFuenteDirecta());
            c.setPartidaPresupuestaria(presupuesto.getPartida());
            c.setPlanProgramatico(presupuesto.getEstructura());
            if (subEnlaceSeleccionado.getCodigo().equals("ordenes_cobro")) {
                c.setParcialTotal(diarioGeneralService.getClaseTipo("diario_general_devengado"));
                c.setMonto_3(monto);
                c.setMonto_4(BigDecimal.ZERO);
            } else {
                c.setParcialTotal(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                c.setMonto_3(BigDecimal.ZERO);
                c.setMonto_4(monto);
            }
            c.setCuentaPartidaPresupuestaria(c.getCuentaContable().getCodigo().concat(presupuesto.getPartida()));
        } else {
            c.setCuentaPartidaPresupuestaria(c.getCuentaContable().getCodigo());
        }
        if (!aux.contains(c.getCuentaPartidaPresupuestaria())) {
            aux.add(c.getCuentaPartidaPresupuestaria());
            aux_1.add(c);
        }
        CuentaContablePresupuestoModel a = Utils.clone(c);
        cuentaContablePresupuestoModelList.add(a);
    }

    private void agruparListado(List<CuentaContablePresupuestoModel> aux) {
        for (CuentaContablePresupuestoModel cuentaPresupuesto_2 : aux) {
            cuentaPresupuesto_2.setMonto_1(BigDecimal.ZERO);
            cuentaPresupuesto_2.setMonto_2(BigDecimal.ZERO);
            cuentaPresupuesto_2.setMonto_3(BigDecimal.ZERO);
            cuentaPresupuesto_2.setMonto_4(BigDecimal.ZERO);
            for (CuentaContablePresupuestoModel cuentaPresupuesto_1 : cuentaContablePresupuestoModelList) {
                if (cuentaPresupuesto_1.getCuentaPartidaPresupuestaria().equals(cuentaPresupuesto_2.getCuentaPartidaPresupuestaria())) {
                    cuentaPresupuesto_2.setMonto_1(new BigDecimal(cuentaPresupuesto_2.getMonto_1().doubleValue() + cuentaPresupuesto_1.getMonto_1().doubleValue()));
                    cuentaPresupuesto_2.setMonto_2(new BigDecimal(cuentaPresupuesto_2.getMonto_2().doubleValue() + cuentaPresupuesto_1.getMonto_2().doubleValue()));
                    cuentaPresupuesto_2.setMonto_3(new BigDecimal(cuentaPresupuesto_2.getMonto_3().doubleValue() + cuentaPresupuesto_1.getMonto_3().doubleValue()));
                    cuentaPresupuesto_2.setMonto_4(new BigDecimal(cuentaPresupuesto_2.getMonto_4().doubleValue() + cuentaPresupuesto_1.getMonto_4().doubleValue()));
                }
            }
        }
        int cont = 1;
        for (CuentaContablePresupuestoModel cuentaPresupuesto : aux) {
            DetalleTransaccion detalle = new DetalleTransaccion();
            detalle.setContador(BigInteger.valueOf(cont));
            detalle.setDatoCargado(true);
            detalle.setCuentaContable(cuentaPresupuesto.getCuentaContable());
            detalle.setDebe(cuentaPresupuesto.getMonto_1());
            detalle.setHaber(cuentaPresupuesto.getMonto_2());
            detalle.setDevengado(cuentaPresupuesto.getMonto_3());
            detalle.setEjecutado(cuentaPresupuesto.getMonto_4());
            if (cuentaPresupuesto.getPartidaPresupuestaria() != null) {
                detalle.setTipoTransaccion(cuentaPresupuesto.getParcialTotal());
                detalle.setPartidaPresupuestaria(cuentaPresupuesto.getCatalogoPresupuesto());
                detalle.setEstructuraProgramatica(cuentaPresupuesto.getPlanProgramatico());
                detalle.setCedulaPresupuestaria(cuentaPresupuesto.getPartidaPresupuestaria());
                detalle.setFuente(cuentaPresupuesto.getFuenteDirecta());
            }
            detalleDiarioGeneral.add(detalle);
            cont += 1;
        }
    }

    private void contabilizarModuloTesoreria() {
        if (corteOrdenCobroSeleccionada != null) {
            if (procesoCobroEmisor) {
                corteOrdenCobroSeleccionada.setDiarioGeneralEmision(diarioGeneral);
                corteOrdenCobroSeleccionada.setFechaContabilizadoEmision(new Date());
                corteOrdenCobroSeleccionada.setContabilizadoEmision(Boolean.TRUE);
            } else {
                corteOrdenCobroSeleccionada.setDiarioGeneralCobroCaja(diarioGeneral);
                corteOrdenCobroSeleccionada.setFechaContabilizadoCobroCaja(new Date());
                corteOrdenCobroSeleccionada.setContabilizadoCobroCaja(Boolean.TRUE);
            }
            corteOrdenCobroService.edit(corteOrdenCobroSeleccionada);
        }
        if (recaudacionSelecionada != null) {
            recaudacionSelecionada.setDiarioGeneral(diarioGeneral);
            recaudacionSelecionada.setFechaContabilizado(new Date());
            recaudacionSelecionada.setContabilizado(Boolean.TRUE);
            recaudacionService.edit(recaudacionSelecionada);
            if (subEnlaceSeleccionado.getCodigo().equals("recaudaciones_cobros_ajustes")) {
                int index = diarioGeneralService.getActualizarCobrosXAjuste(recaudacionSelecionada);
            }

        }
        if (!retencionesSeleccionadas.isEmpty()) {
            for (LiquidacionDetalle detalleRetencion : retencionesSeleccionadas) {
                detalleRetencion.setContabilizado(Boolean.TRUE);
                detalleRetencion.setFechaContabilizado(new Date());
                detalleRetencion.setDiarioGeneral(diarioGeneral);
                liquidacionDetalleService.edit(detalleRetencion);
            }
            for (RetencionesRegistradas retencionR : retencionesRegistradasList) {
                retencionesRegistradasService.create(retencionR);
            }
        }
        if (!garantiasSeleccionadasList.isEmpty()) {
            for (Garantias garantia : garantiasSeleccionadasList) {
                garantia.setUsuarioModifica(userSession.getNameUser());
                garantia.setFechaModifica(new Date());
                if (garantia.getDevolucion()) {
//                    garantia.setDiarioGeneralDevuelto(diarioGeneral);
                    garantia.setFechaContabilizadoDevuelto(new Date());
                } else {
                    if (polizaVigente(garantia.getFechaHasta())) {
//                        garantia.setDiarioGeneralVigente(diarioGeneral);
                        garantia.setFechaContabilizadoVigente(new Date());
                    } else {
//                        garantia.setDiarioGeneralVencido(diarioGeneral);
                        garantia.setFechaContabilizadoVencido(new Date());
                    }
                }
                garantiasService.edit(garantia);
            }
        }
        if (ordenesEmitidasList != null && !ordenesEmitidasList.isEmpty()) {
            for (OrdenesEmitidas arriendo : ordenesEmitidasList) {
                BigInteger bi = BigInteger.valueOf(diarioGeneral.getId());

                if (subEnlaceSeleccionado.getCodigo().equals("ordenes_cobro")) {
                    arriendo.setIdDiarioCobrar(bi);
                    arriendo.setDiarioCobrar(true);
                } else {
                    arriendo.setIdDiarioPagar(bi);
                    arriendo.setDiarioPagar(true);
                }
                ordenesEmitidasService.edit(arriendo);
            }
        }
        restablecerModuloTesoreria();
    }

    private void restablecerModuloTesoreria() {
        this.recaudacionSelecionada = new Recaudacion();
        this.corteOrdenCobroSeleccionada = new CorteOrdenCobro();
        this.tableCorteOrdenCobro = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
        this.retencionesRegistradasList = new ArrayList<>();
        this.retencionesSeleccionadas = new ArrayList<>();
        this.garantiasSeleccionadasList = new ArrayList<>();
        this.garantiasList = new ArrayList<>();
    }
//</editor-fold>

    /*MÓDULO: BIENES*/
    //<editor-fold defaultstate="collapsed" desc="Bienes">
    public void procesoBienesSeleccionado() {
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "depreciaciones":
                    renderDepreciacion = true;
                    depreciacion = new Depreciacion();
                    depreciacionList = depreciacionService.getListNoContabilizadas(false, periodo);
                    break;
                case "bienes_ingresos":
                    renderDepreciacion = false;
                    motivoMovimientosList = diarioGeneralService.getMotivoMovimientos("INGRESO");
                    break;
            }
        } else {
            renderDepreciacion = false;
            motivoMovimientosList = new ArrayList<>();
            bienesList = null;
        }
    }

    private void cabezeraDiario() {
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        actualizarTipoDiarioGeneral();
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_ajuste"));
        this.botonCuentasContables = Boolean.TRUE;
    }

    public void actualizarRegistros() {
        if (catalogoMovimientoSeleccionado != null) {
            this.bienesList = new LazyModel<>(BienesMovimiento.class);
            this.bienesList.getSorteds().put("orden", "ASC");
            this.bienesList.getFilterss().put("estado", true);
            this.bienesList.getFilterss().put("contabilizado", false);
            this.bienesList.getFilterss().put("periodo", this.periodo);
            this.bienesList.getFilterss().put("motivoMovimiento", this.catalogoMovimientoSeleccionado);
        } else {
            bienesList = null;
        }
        PrimeFaces.current().ajax().update("bienesTable");
    }

    public void bienesListSeleccionado() {
        if (bienesListSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar por lo menos un bien");
            return;
        }
        List<Long> sendList = new ArrayList<>();
        for (BienesMovimiento item : bienesListSeleccionados) {
            sendList.add(item.getId());
        }
        detalleDiarioGeneral = diarioGeneralService.getBienesIngreso(sendList);
        cabezeraDiario();
        diarioGeneral.setObservacion("MÓDULO DE BIENES: P.R. BIENES DE INGRESO DE TIPO: " + catalogoMovimientoSeleccionado.getTexto());
        this.botonCuentasContables = Boolean.TRUE;
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().executeScript("PF('bienesDlg').hide()");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    public void depreciacionSeleccionado() {
        cabezeraDiario();
        diarioGeneral.setObservacion("MÓDULO DE BIENES: P.R. DEPRECIACIÓN CON CODIGO: " + depreciacion.getCodigoDepreciacion());
        detalleDiarioGeneral = diarioGeneralService.getDepreciaciones(depreciacion);
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().executeScript("PF('bienesDlg').hide()");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
    }

    public void contabilizarBienes() {
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "depreciaciones":
                    depreciacion.setContabilizado(Boolean.TRUE);
                    depreciacion.setDiarioGeneral(diarioGeneral);
                    depreciacionService.edit(depreciacion);
                    break;
                case "bienes_ingresos":
                    for (BienesMovimiento item : bienesListSeleccionados) {
                        item.setContabilizado(Boolean.TRUE);
                        item.setTransaccionContable(diarioGeneral);
                        item.setPeriodoContable(diarioGeneral.getPeriodo());
                        item.setFechaContable(diarioGeneral.getFechaElaboracion());
                        bienesMovimientoService.edit(item);
                    }
                    break;
            }
        }
        restablecerModuloBienes();
    }

    private void restablecerModuloBienes() {
        renderDepreciacion = false;
        motivoMovimientosList = new ArrayList<>();
        depreciacionList = new ArrayList<>();
        bienesListSeleccionados = new ArrayList<>();
        catalogoMovimientoSeleccionado = null;
        bienesList = null;
        depreciacion = null;
    }
//</editor-fold>

    /*MÓDULO: PAGO IMPUESTOS SRI PROCESO*/
    //<editor-fold defaultstate="collapsed" desc="IMPUESTOS SRI PROCESO">
    private void cargarValoresProcesoImpuestoSRI() {
        if (!servletSession.estaVacio()) {
            listaPeriodo = new ArrayList<>();
            periodo = opcionBusqueda.getAnioAnterior();
            tramite = Long.valueOf(servletSession.getParametros().get("tramite").toString());

            ats = Long.valueOf(servletSession.getParametros().get("ats").toString());

            tarea = String.valueOf(servletSession.getParametros().get("tarea"));
            tipo = String.valueOf(servletSession.getParametros().get("tipo"));
            Long diario = null;
            if (servletSession.getParametros().get("diario") != null) {
                diario = Long.valueOf(servletSession.getParametros().get("diario").toString());
            }

            if (diario == null) {

                form(null, "nuevoDiario");
                registrarDiarioGeneral("enlaces");
                for (CatalogoItem modulo : modulosDeEnlaces) {
                    if (modulo.getId() == 658L) {
                        moduloDeEnlaceSeleccionado = modulo;
                        break;
                    }
                }
                if (moduloDeEnlaceSeleccionado != null) {
                    enlaceSeleccionado();
                }
                for (CatalogoItem modulo : subEnlace) {
                    if (modulo.getId() == 706L) {
                        subEnlaceSeleccionado = modulo;
                        break;
                    }
                }
                if (subEnlaceSeleccionado != null) {
                    procesoTesoreriaSeleccionado();
                    PrimeFaces.current().ajax().update("moduloTesoreriaForm");
                }
                for (String t : tipoRetenciones) {
                    if (t.contains(tipo)) {
                        tipoRetencionSeleccionado = t;
                    }
                }
                actualizarTablaRetenciones();
                PrimeFaces.current().ajax().update("moduloTesoreriaForm");
                PrimeFaces.current().ajax().update("retencionTable");
            } else {
                form(diarioGeneralService.find(diario), "editar");

            }

            servletSession.borrarDatos();
        }
    }

    private void actualizarImpuestoSRI(Long diario) {
        if (tramite != null) {
            AtsArchivo atsArchivo = atsArchivoService.find(ats);
            atsArchivo.setArchivo("OK");
            atsArchivo.setDiario(diario);
            atsArchivoService.edit(atsArchivo);
            //servletSession.borrarDatos();
            servletSession.addParametro("taskID", taskID);
            JsfUtil.redirect(CONFIG.URL_APP + "Contabilidad/Tributacion/registroContableATS");
        }

    }
//</editor-fold>


    /*GET - SET*/
    //<editor-fold defaultstate="collapsed" desc="Get - Set">
    public Depreciacion getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(Depreciacion depreciacion) {
        this.depreciacion = depreciacion;
    }

    public List<Depreciacion> getDepreciacionList() {
        return depreciacionList;
    }

    public void setDepreciacionList(List<Depreciacion> depreciacionList) {
        this.depreciacionList = depreciacionList;
    }

    public Boolean getRenderDepreciacion() {
        return renderDepreciacion;
    }

    public void setRenderDepreciacion(Boolean renderDepreciacion) {
        this.renderDepreciacion = renderDepreciacion;
    }

    public List<BienesMovimiento> getBienesListSeleccionados() {
        return bienesListSeleccionados;
    }

    public void setBienesListSeleccionados(List<BienesMovimiento> bienesListSeleccionados) {
        this.bienesListSeleccionados = bienesListSeleccionados;
    }

    public LazyModel<BienesMovimiento> getBienesList() {
        return bienesList;
    }

    public void setBienesList(LazyModel<BienesMovimiento> bienesList) {
        this.bienesList = bienesList;
    }

    public Boolean getRenderedListRol() {
        return renderedListRol;
    }

    public void setRenderedListRol(Boolean renderedListRol) {
        this.renderedListRol = renderedListRol;
    }

    public Boolean getPeriodoBoolean() {
        return periodoBoolean;
    }

    public void setPeriodoBoolean(Boolean periodoBoolean) {
        this.periodoBoolean = periodoBoolean;
    }

    public Date getFechaBusquedaArriendo() {
        return fechaBusquedaArriendo;
    }

    public void setFechaBusquedaArriendo(Date fechaBusquedaArriendo) {
        this.fechaBusquedaArriendo = fechaBusquedaArriendo;
    }

    public CatalogoItem getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(CatalogoItem mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public List<CatalogoItem> getMesesArriendoList() {
        return mesesArriendoList;
    }

    public void setMesesArriendoList(List<CatalogoItem> mesesArriendoList) {
        this.mesesArriendoList = mesesArriendoList;
    }

    public LazyModel<OrdenesEmitidas> getOrdenesEmitidasLazyModel() {
        return ordenesEmitidasLazyModel;
    }

    public void setOrdenesEmitidasLazyModel(LazyModel<OrdenesEmitidas> ordenesEmitidasLazyModel) {
        this.ordenesEmitidasLazyModel = ordenesEmitidasLazyModel;
    }

    public List<OrdenesEmitidas> getOrdenesEmitidasList() {
        return ordenesEmitidasList;
    }

    public void setOrdenesEmitidasList(List<OrdenesEmitidas> ordenesEmitidasList) {
        this.ordenesEmitidasList = ordenesEmitidasList;
    }

    public Boolean getTableArriendo() {
        return tableArriendo;
    }

    public void setTableArriendo(Boolean tableArriendo) {
        this.tableArriendo = tableArriendo;
    }

    public List<Adquisiciones> getAdquisicionesList() {
        return adquisicionesList;
    }

    public void setAdquisicionesList(List<Adquisiciones> adquisicionesList) {
        this.adquisicionesList = adquisicionesList;
    }

    public Boolean getBooleanAdquisiciones() {
        return booleanAdquisiciones;
    }

    public void setBooleanAdquisiciones(Boolean booleanAdquisiciones) {
        this.booleanAdquisiciones = booleanAdquisiciones;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public List<MasterCatalogo> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<MasterCatalogo> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public String getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(String tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
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

    public List<Garantias> getGarantiasList() {
        return garantiasList;
    }

    public void setGarantiasList(List<Garantias> garantiasList) {
        this.garantiasList = garantiasList;
    }

    public List<Garantias> getGarantiasSeleccionadasList() {
        return garantiasSeleccionadasList;
    }

    public void setGarantiasSeleccionadasList(List<Garantias> garantiasSeleccionadasList) {
        this.garantiasSeleccionadasList = garantiasSeleccionadasList;
    }

    public LazyModel<Adquisiciones> getAdquisicionesLazyModel() {
        return adquisicionesLazyModel;
    }

    public void setAdquisicionesLazyModel(LazyModel<Adquisiciones> adquisicionesLazyModel) {
        this.adquisicionesLazyModel = adquisicionesLazyModel;
    }

    public List<LiquidacionDetalle> getLiquidacionDetalleList() {
        return liquidacionDetalleList;
    }

    public void setLiquidacionDetalleList(List<LiquidacionDetalle> liquidacionDetalleList) {
        this.liquidacionDetalleList = liquidacionDetalleList;
    }

    public List<LiquidacionDetalle> getRetencionesSeleccionadas() {
        return retencionesSeleccionadas;
    }

    public void setRetencionesSeleccionadas(List<LiquidacionDetalle> retencionesSeleccionadas) {
        this.retencionesSeleccionadas = retencionesSeleccionadas;
    }

    public Boolean getTableRetenciones() {
        return tableRetenciones;
    }

    public void setTableRetenciones(Boolean tableRetenciones) {
        this.tableRetenciones = tableRetenciones;
    }

    public List<String> getPeriodosFiscales() {
        return periodosFiscales;
    }

    public void setPeriodosFiscales(List<String> periodosFiscales) {
        this.periodosFiscales = periodosFiscales;
    }

    public List<String> getTipoRetenciones() {
        return tipoRetenciones;
    }

    public void setTipoRetenciones(List<String> tipoRetenciones) {
        this.tipoRetenciones = tipoRetenciones;
    }

    public String getTipoRetencionSeleccionado() {
        return tipoRetencionSeleccionado;
    }

    public void setTipoRetencionSeleccionado(String tipoRetencionSeleccionado) {
        this.tipoRetencionSeleccionado = tipoRetencionSeleccionado;
    }

    public String getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(String periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public CatalogoItem getSubEnlaceSeleccionado() {
        return subEnlaceSeleccionado;
    }

    public void setSubEnlaceSeleccionado(CatalogoItem subEnlaceSeleccionado) {
        this.subEnlaceSeleccionado = subEnlaceSeleccionado;
    }

    public List<CatalogoItem> getSubEnlace() {
        return subEnlace;
    }

    public void setSubEnlace(List<CatalogoItem> subEnlace) {
        this.subEnlace = subEnlace;
    }

    public DetalleTransaccion getDetalleTransaccion() {
        return detalleTransaccion;
    }

    public void setDetalleTransaccion(DetalleTransaccion detalleTransaccion) {
        this.detalleTransaccion = detalleTransaccion;
    }

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public void setPresupuestoSeleccionado(Presupuesto presupuestoSeleccionado) {
        this.presupuestoSeleccionado = presupuestoSeleccionado;
    }

    public List<CatalogoPresupuesto> getPartidaPresupuestariaRelacionadas() {
        return partidaPresupuestariaRelacionadas;
    }

    public void setPartidaPresupuestariaRelacionadas(List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas) {
        this.partidaPresupuestariaRelacionadas = partidaPresupuestariaRelacionadas;
    }

    public List<Presupuesto> getPresupuestoRelacionado() {
        return presupuestoRelacionado;
    }

    public void setPresupuestoRelacionado(List<Presupuesto> presupuestoRelacionado) {
        this.presupuestoRelacionado = presupuestoRelacionado;
    }

    public Boolean getBotonAnulacion() {
        return botonAnulacion;
    }

    public void setBotonAnulacion(Boolean botonAnulacion) {
        this.botonAnulacion = botonAnulacion;
    }

    public CatalogoItem getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(CatalogoItem tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<SolicitudReservaCompromiso> getReservaCompromisoList() {
        return reservaCompromisoList;
    }

    public void setReservaCompromisoList(List<SolicitudReservaCompromiso> reservaCompromisoList) {
        this.reservaCompromisoList = reservaCompromisoList;
    }

    public List<CatalogoItem> getTiposAdquisiciones() {
        return tiposAdquisiciones;
    }

    public void setTiposAdquisiciones(List<CatalogoItem> tiposAdquisiciones) {
        this.tiposAdquisiciones = tiposAdquisiciones;
    }

    public List<CuentaContable> getRelacionCuentasContables() {
        return relacionCuentasContables;
    }

    public void setRelacionCuentasContables(List<CuentaContable> relacionCuentasContables) {
        this.relacionCuentasContables = relacionCuentasContables;
    }

    public Boolean getBotonBeneficiarioFondosReserva() {
        return botonBeneficiarioFondosReserva;
    }

    public void setBotonBeneficiarioFondosReserva(Boolean botonBeneficiarioFondosReserva) {
        this.botonBeneficiarioFondosReserva = botonBeneficiarioFondosReserva;
    }

    public CatalogoItem getModuloDeEnlaceSeleccionado() {
        return moduloDeEnlaceSeleccionado;
    }

    public void setModuloDeEnlaceSeleccionado(CatalogoItem moduloDeEnlaceSeleccionado) {
        this.moduloDeEnlaceSeleccionado = moduloDeEnlaceSeleccionado;
    }

    public List<CatalogoItem> getModulosDeEnlaces() {
        return modulosDeEnlaces;
    }

    public void setModulosDeEnlaces(List<CatalogoItem> modulosDeEnlaces) {
        this.modulosDeEnlaces = modulosDeEnlaces;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public List<DetalleTransaccion> getDetalleDiarioGeneral() {
        return detalleDiarioGeneral;
    }

    public void setDetalleDiarioGeneral(List<DetalleTransaccion> detalleDiarioGeneral) {
        this.detalleDiarioGeneral = detalleDiarioGeneral;
    }

    public List<CatalogoItem> getClasesDiarioGeneral() {
        return clasesDiarioGeneral;
    }

    public void setClasesDiarioGeneral(List<CatalogoItem> clasesDiarioGeneral) {
        this.clasesDiarioGeneral = clasesDiarioGeneral;
    }

    public List<CatalogoItem> getTiposDiarioGeneral() {
        return tiposDiarioGeneral;
    }

    public void setTiposDiarioGeneral(List<CatalogoItem> tiposDiarioGeneral) {
        this.tiposDiarioGeneral = tiposDiarioGeneral;
    }

    public Boolean getFormDiarioGeneral() {
        return formDiarioGeneral;
    }

    public void setFormDiarioGeneral(Boolean formDiarioGeneral) {
        this.formDiarioGeneral = formDiarioGeneral;
    }

    public Boolean getBotonCuentasContables() {
        return botonCuentasContables;
    }

    public void setBotonCuentasContables(Boolean botonCuentasContables) {
        this.botonCuentasContables = botonCuentasContables;
    }

    public Boolean getBotonNuevoEnlace() {
        return botonNuevoEnlace;
    }

    public void setBotonNuevoEnlace(Boolean botonNuevoEnlace) {
        this.botonNuevoEnlace = botonNuevoEnlace;
    }

    public Boolean getBotonEnlaces() {
        return botonEnlaces;
    }

    public void setBotonEnlaces(Boolean botonEnlaces) {
        this.botonEnlaces = botonEnlaces;
    }

    public Boolean getDisableClaseDiarioGeneral() {
        return disableClaseDiarioGeneral;
    }

    public void setDisableClaseDiarioGeneral(Boolean disableClaseDiarioGeneral) {
        this.disableClaseDiarioGeneral = disableClaseDiarioGeneral;
    }

    public Boolean getDisableTipoDiarioGeneral() {
        return disableTipoDiarioGeneral;
    }

    public void setDisableTipoDiarioGeneral(Boolean disableTipoDiarioGeneral) {
        this.disableTipoDiarioGeneral = disableTipoDiarioGeneral;
    }

    public double getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(double totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public double getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(double totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public double getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(double totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public LazyModel<DiarioGeneral> getDiarioGeneralLazyModel() {
        return diarioGeneralLazyModel;
    }

    public void setDiarioGeneralLazyModel(LazyModel<DiarioGeneral> diarioGeneralLazyModel) {
        this.diarioGeneralLazyModel = diarioGeneralLazyModel;
    }

    public Boolean getTableCorteOrdenCobro() {
        return tableCorteOrdenCobro;
    }

    public void setTableCorteOrdenCobro(Boolean tableCorteOrdenCobro) {
        this.tableCorteOrdenCobro = tableCorteOrdenCobro;
    }

    public Boolean getTableRecaudaciones() {
        return tableRecaudaciones;
    }

    public void setTableRecaudaciones(Boolean tableRecaudaciones) {
        this.tableRecaudaciones = tableRecaudaciones;
    }

    public LazyModel<Recaudacion> getRecaudacionlLazyModel() {
        return recaudacionlLazyModel;
    }

    public void setRecaudacionlLazyModel(LazyModel<Recaudacion> recaudacionlLazyModel) {
        this.recaudacionlLazyModel = recaudacionlLazyModel;
    }

    public LazyModel<CorteOrdenCobro> getCorteOrdenCobroLazyModel() {
        return corteOrdenCobroLazyModel;
    }

    public void setCorteOrdenCobroLazyModel(LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel) {
        this.corteOrdenCobroLazyModel = corteOrdenCobroLazyModel;
    }

    public CuentaContable getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(CuentaContable cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public Boolean getProcesoCobroEmisor() {
        return procesoCobroEmisor;
    }

    public void setProcesoCobroEmisor(Boolean procesoCobroEmisor) {
        this.procesoCobroEmisor = procesoCobroEmisor;
    }

    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

    public Boolean getBotonPartidasPresupuestarias() {
        return botonPartidasPresupuestarias;
    }

    public void setBotonPartidasPresupuestarias(Boolean botonPartidasPresupuestarias) {
        this.botonPartidasPresupuestarias = botonPartidasPresupuestarias;
    }

    public CatalogoMovimiento getCatalogoMovimientoSeleccionado() {
        return catalogoMovimientoSeleccionado;
    }

    public void setCatalogoMovimientoSeleccionado(CatalogoMovimiento catalogoMovimientoSeleccionado) {
        this.catalogoMovimientoSeleccionado = catalogoMovimientoSeleccionado;
    }

    public List<CatalogoMovimiento> getMotivoMovimientosList() {
        return motivoMovimientosList;
    }

    public void setMotivoMovimientosList(List<CatalogoMovimiento> motivoMovimientosList) {
        this.motivoMovimientosList = motivoMovimientosList;
    }

    public LazyModel<Inventario> getInventarioLazyModel() {
        return inventarioLazyModel;
    }

    public void setInventarioLazyModel(LazyModel<Inventario> inventarioLazyModel) {
        this.inventarioLazyModel = inventarioLazyModel;
    }

    public List<Inventario> getInventariosSeleccionadoList() {
        return inventariosSeleccionadoList;
    }

    public void setInventariosSeleccionadoList(List<Inventario> inventariosSeleccionadoList) {
        this.inventariosSeleccionadoList = inventariosSeleccionadoList;
    }

    public DetalleTransferencias getTransferenciaAnuladaSeleccionada() {
        return transferenciaAnuladaSeleccionada;
    }

    public void setTransferenciaAnuladaSeleccionada(DetalleTransferencias transferenciaAnuladaSeleccionada) {
        this.transferenciaAnuladaSeleccionada = transferenciaAnuladaSeleccionada;
    }

    public LazyModel<DetalleTransferencias> getDetalleTransferenciaLazyModel() {
        return detalleTransferenciaLazyModel;
    }

    public void setDetalleTransferenciaLazyModel(LazyModel<DetalleTransferencias> detalleTransferenciaLazyModel) {
        this.detalleTransferenciaLazyModel = detalleTransferenciaLazyModel;
    }

    public TipoRol getTipoRolSeleccionado() {
        return tipoRolSeleccionado;
    }

    public void setTipoRolSeleccionado(TipoRol tipoRolSeleccionado) {
        this.tipoRolSeleccionado = tipoRolSeleccionado;
    }

    public TipoRolBeneficios getTipoRolBeneficiosSeleccionado() {
        return tipoRolBeneficiosSeleccionado;
    }

    public void setTipoRolBeneficiosSeleccionado(TipoRolBeneficios tipoRolBeneficiosSeleccionado) {
        this.tipoRolBeneficiosSeleccionado = tipoRolBeneficiosSeleccionado;
    }

    public List<TipoRolBeneficios> getRolesBeneficiosList() {
        return rolesBeneficiosList;
    }

    public void setRolesBeneficiosList(List<TipoRolBeneficios> rolesBeneficiosList) {
        this.rolesBeneficiosList = rolesBeneficiosList;
    }

    public List<TipoRol> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<TipoRol> rolesList) {
        this.rolesList = rolesList;
    }

    public Boolean getRenderedRoles() {
        return renderedRoles;
    }

    public void setRenderedRoles(Boolean renderedRoles) {
        this.renderedRoles = renderedRoles;
    }

    public Boolean getRenderedRolesBeneficios() {
        return renderedRolesBeneficios;
    }

    public void setRenderedRolesBeneficios(Boolean renderedRolesBeneficios) {
        this.renderedRolesBeneficios = renderedRolesBeneficios;
    }

    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoModelList() {
        return cuentaContablePresupuestoModelList;
    }

    public void setCuentaContablePresupuestoModelList(List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList) {
        this.cuentaContablePresupuestoModelList = cuentaContablePresupuestoModelList;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public double getTotalLiquidacion() {
        return totalLiquidacion;
    }

    public void setTotalLiquidacion(double totalLiquidacion) {
        this.totalLiquidacion = totalLiquidacion;
    }

    public List<BeneficiarioSolicitudReserva> getBeneficiarioReservaCompromisoList() {
        return beneficiarioReservaCompromisoList;
    }

    public void setBeneficiarioReservaCompromisoList(List<BeneficiarioSolicitudReserva> beneficiarioReservaCompromisoList) {
        this.beneficiarioReservaCompromisoList = beneficiarioReservaCompromisoList;
    }

    public Boolean getFondoReserva() {
        return fondoReserva;
    }

    public void setFondoReserva(Boolean fondoReserva) {
        this.fondoReserva = fondoReserva;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public List<CuentaContablePresupuestoModel> getCuentasConPartida() {
        return cuentasConPartida;
    }

    public void setCuentasConPartida(List<CuentaContablePresupuestoModel> cuentasConPartida) {
        this.cuentasConPartida = cuentasConPartida;
    }

    public List<DetalleTransaccion> getPartidaEstructura() {
        return partidaEstructura;
    }

    public void setPartidaEstructura(List<DetalleTransaccion> partidaEstructura) {
        this.partidaEstructura = partidaEstructura;
    }

    public Boolean getPresupuestoTable() {
        return presupuestoTable;
    }

    public void setPresupuestoTable(Boolean presupuestoTable) {
        this.presupuestoTable = presupuestoTable;
    }

    public Boolean getDetalleDiarioTableView() {
        return detalleDiarioTableView;
    }

    public void setDetalleDiarioTableView(Boolean detalleDiarioTableView) {
        this.detalleDiarioTableView = detalleDiarioTableView;
    }

    public Boolean getDetalleDiarioTableEdit() {
        return detalleDiarioTableEdit;
    }

    public void setDetalleDiarioTableEdit(Boolean detalleDiarioTableEdit) {
        this.detalleDiarioTableEdit = detalleDiarioTableEdit;
    }

    public LazyModel<Presupuesto> getPresupuestoLazyModel() {
        return presupuestoLazyModel;
    }

    public void setPresupuestoLazyModel(LazyModel<Presupuesto> presupuestoLazyModel) {
        this.presupuestoLazyModel = presupuestoLazyModel;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getAts() {
        return ats;
    }

    public void setAts(Long ats) {
        this.ats = ats;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
//</editor-fold>
}
