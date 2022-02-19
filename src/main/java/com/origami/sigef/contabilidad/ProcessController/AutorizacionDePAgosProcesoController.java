/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CobrosEmisionesModel;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.model.ServidorMontoModel;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DetalleTransferenciasService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "auotrizacionPagosView")
@ViewScoped
public class AutorizacionDePAgosProcesoController extends BpmnBaseRoot implements Serializable {

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
    private ClienteService clienteService;
    @Inject
    private ProcedimientoRequisitoService requisitosService;
    @Inject
    private ProcesoService tramiteServiceu;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;

    @Inject
    private FileUploadDoc uploadDoc;

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

    /*TOTALES*/
    private double totalDebe;
    private double totalHaber;
    private double totalComprometido;
    private double totalDevengado;
    private double totalEjecutado;
    private double totalLiquidacion;

    /*STRING AUXILIARES*/
    private String tipoRetencionSeleccionado;
    private String periodoSeleccionado;

    /*LAZYMODELS*/
    private LazyModel<DiarioGeneral> diarioGeneralLazyModel;
    private LazyModel<Recaudacion> recaudacionlLazyModel;
    private LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel;
    private LazyModel<CuentaContable> cuentaContableLazyModel;
    private LazyModel<Inventario> inventarioLazyModel;
    private LazyModel<DetalleTransferencias> detalleTransferenciaLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Presupuesto> presupuestoLazyModel;
    private String observaciones;
    private boolean detalleDiario, showRequisios;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private ListarequisitosModel requisitosObjeto;
    private List<ListarequisitosModel> requisitosTramite;

    @PostConstruct
    public void initialize() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                this.opcionBusqueda = new OpcionBusqueda();

                diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());

                if (diarioGeneral != null) {
                    detalleDiario = true;
                    showRequisios = false;
                    this.diarioGeneralLazyModel = new LazyModel<>(DiarioGeneral.class);
                    this.diarioGeneralLazyModel.getSorteds().put("id", "ASC");
                    this.diarioGeneralLazyModel.getFilterss().put("estado", true);
                    this.diarioGeneralLazyModel.getFilterss().put("comprobantePago", false);
                    this.diarioGeneralLazyModel.getFilterss().put("numTramite", tramite.getNumTramite());
                    this.diarioGeneralLazyModel.getFilterss().put("periodo", Utils.getAnio(new Date()).shortValue());
                    this.clasesDiarioGeneral = catalogoService.getItemsByCatalogo("diario_general_clases");
                    this.modulosDeEnlaces = catalogoService.getItemsByCatalogo("diario_general_modulos_enlace");
                    this.tiposAdquisiciones = adquisicionesService.getTipos("sub_tipo_adquisicion");
                    reiniciarBotones();
                } else {
                    detalleDiario = false;
                    showRequisios = true;
                    procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());
                }
                listRequisitos();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = tramiteServiceu.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                requisitosTramite.add(req);
            }
        }
    }

    public void form(DiarioGeneral diarioGeneralIntegrado, String accion) {
        this.diarioGeneral = new DiarioGeneral();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.formDiarioGeneral = Boolean.FALSE;
        this.presupuestoTable = Boolean.FALSE;
        this.detalleDiarioTableView = Boolean.FALSE;
        this.detalleDiarioTableEdit = Boolean.TRUE;
        if (diarioGeneralIntegrado != null) {
            if (diarioGeneralIntegrado.getCertificacionesPresupuestario() != null) {
                this.reservaCompromiso = diarioGeneralIntegrado.getCertificacionesPresupuestario();
                if (!diarioGeneralIntegrado.getEstadoDiario().equals("ANULADO")) {
                    this.botonAnulacion = Boolean.TRUE;
                }
            }
            this.diarioGeneral = diarioGeneralIntegrado;
            if (accion.equals("visualizar")) {
                this.presupuestoTable = Boolean.FALSE;
                this.detalleDiarioTableView = Boolean.TRUE;
                this.detalleDiarioTableEdit = Boolean.FALSE;
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
            }
            List<DetalleTransaccion> auxiliar = detalleTransaccionService.getDetalleTransaccion(diarioGeneral);
            for (DetalleTransaccion detalleDG : auxiliar) {
                detalleDiarioGeneral.add(detalleDG);
            }
            this.botonNuevoEnlace = Boolean.FALSE;
            actualizarTipoDiarioGeneral();
            calcularTotalesDetalleDiarioGeneral();
        }
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("tableDiarioGeneral");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
    }

    private void datosPrecargadosDiarioGeneral() {
        this.diarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        this.diarioGeneral.setFechaElaboracion(new Date());
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(1));
        }
    }

    public void save() {
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion())));
        if (!periodoAbierto) {
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
        if (diarioGeneral.getId() != null) {
            diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
            diarioGeneral.setFechaModificacion(new Date());
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            } else {
                diarioGeneral.setTotalDebe(new BigDecimal(this.totalDebe));
                diarioGeneral.setTotalHaber(new BigDecimal(this.totalHaber));
            }
            diarioGeneralService.edit(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                if (transaccion.getId() != null) {
                    detalleTransaccionService.edit(transaccion);
                } else {
                    transaccion.setDiarioGeneral(diarioGeneral);
                    transaccion.setDatoCargado(Boolean.TRUE);
                    detalleTransaccionService.create(transaccion);
                }
            }
        } else {
            if (reservaCompromiso != null) {
                if (reservaCompromiso.getBeneficiario() != null) {
                    diarioGeneral.setBeneficiario(reservaCompromiso.getBeneficiario());
                    diarioGeneral.setTipoBeneficiario(reservaCompromiso.getTipoBeneficiario());
                    diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
                }
                diarioGeneral.setCertificacionesPresupuestario(reservaCompromiso);
            }
            if ((diarioGeneral.getClase().getCodigo().equals("clase_egreso") || diarioGeneral.getClase().getCodigo().equals("clase_diario")) && diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                diarioGeneral.setRetencion(Boolean.TRUE);
                diarioGeneral.setRetenido(Boolean.FALSE);
            }
            diarioGeneral.setUsuarioCreacion(userSession.getNameUser());
            diarioGeneral.setFechaCreacion(new Date());
            diarioGeneral.setEstado(Boolean.TRUE);
            diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase());
            diarioGeneral.setEstadoDiario("REGISTRADO");
            if (botonPartidasPresupuestarias) {
                diarioGeneral.setTotalDebe(BigDecimal.ZERO);
                diarioGeneral.setTotalHaber(BigDecimal.ZERO);
            } else {
                diarioGeneral.setTotalDebe(new BigDecimal(this.totalDebe));
                diarioGeneral.setTotalHaber(new BigDecimal(this.totalHaber));
            }
            if (moduloDeEnlaceSeleccionado != null) {
                diarioGeneral.setEnlace(moduloDeEnlaceSeleccionado);
                if (subEnlaceSeleccionado != null) {
                    diarioGeneral.setEnlace(subEnlaceSeleccionado);
                }
            }
            diarioGeneral = diarioGeneralService.create(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                transaccion.setDiarioGeneral(diarioGeneral);
                transaccion.setDatoCargado(Boolean.TRUE);
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
                }
            }
        }
        imprimirDiarioGeneral();
        cancelar();
    }

    public void cancelar() {
        reiniciarBotones();
        reiniciarValoresTotales();
        reiniciarCuentaContable();
        restablecerModuloInventario();
        restablecerModuloTesoreria();
        restablecerModuloNomina();
        this.diarioGeneral = new DiarioGeneral();
        this.subEnlaceSeleccionado = new CatalogoItem();
        this.subEnlace = new ArrayList<>();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.tiposDiarioGeneral = new ArrayList<>();
        this.cuentasConPartida = new ArrayList<>();
        this.reservaCompromiso = null;
        this.moduloDeEnlaceSeleccionado = null;
        this.inventarioLazyModel = null;
        this.corteOrdenCobroLazyModel = null;
        this.recaudacionlLazyModel = null;
        this.detalleTransferenciaLazyModel = null;
        this.adquisicion = null;
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
        PrimeFaces.current().ajax().update("tableDiarioGeneral");

    }

    public void imprimirDiarioGeneral() {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirDiarioGeneral(DiarioGeneral d) {
        servletSession.addParametro("id_diario_general", d.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString() + "-" + reservaCompromiso.getPeriodo();
        return a;
    }

    public void quitarDetalleDiarioGeneral(DetalleTransaccion detalleDG) {
        int index = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            if (detalle.getContador() == detalleDG.getContador()) {
                break;
            }
            index = index + 1;
        }
        detalleDiarioGeneral.remove(index);
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
    }

    private void calcularTotalesDetalleDiarioGeneral() {
        reiniciarValoresTotales();
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            this.totalDebe = redondearDosDecimales(totalDebe + detalleDG.getDebe().doubleValue());
            this.totalHaber = redondearDosDecimales(totalHaber + detalleDG.getHaber().doubleValue());
            this.totalComprometido = redondearDosDecimales(totalComprometido + detalleDG.getComprometido().doubleValue());
            this.totalDevengado = redondearDosDecimales(totalDevengado + detalleDG.getDevengado().doubleValue());
            this.totalEjecutado = redondearDosDecimales(totalEjecutado + detalleDG.getEjecutado().doubleValue());
        }
        if (diarioGeneral.getId() == null) {
            if (totalHaber != totalDebe) {
                diarioGeneral.setEstadoTransaccion("DESCUADRADO");
            } else {
                diarioGeneral.setEstadoTransaccion("CUADRADO");
            }
        }
    }

    private double redondearDosDecimales(double valor) {
        return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    private void reiniciarValoresTotales() {
        this.totalDebe = 0;
        this.totalHaber = 0;
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
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        if (this.reservaCompromiso != null) {
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
        detalleTransaccion.setHaber(new BigDecimal(total));
        return total == 0;
    }

    public void añadirCuentaContable() {
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
        PrimeFaces.current().ajax().update("tableDiarioGeneral");
        PrimeFaces.current().ajax().update("formDiarioGeneral");
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

    public void enlaceSeleccionado() {
        if (moduloDeEnlaceSeleccionado != null) {
            switch (moduloDeEnlaceSeleccionado.getCodigo()) {
                case "modulo_anulacion_cp_transferencia":
                    this.detalleTransferenciaLazyModel = new LazyModel<>(DetalleTransferencias.class);
                    this.detalleTransferenciaLazyModel.getSorteds().put("referencia", "ASC");
                    this.detalleTransferenciaLazyModel.getFilterss().put("estado", "ANULADO");
                    this.detalleTransferenciaLazyModel.getFilterss().put("contabilizado", false);
                    this.detalleTransferenciaLazyModel.getFilterss().put("transferencia.periodo", opcionBusqueda.getAnio());
                    PrimeFaces.current().executeScript("PF('moduloAnulacionCPTDLG').show()");
                    PrimeFaces.current().ajax().update("moduloAnulacionCPTForm");
                    break;
                case "modulo_certificaciones_presupuestarias":
                    reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion());
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
            }
        }
    }

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public void handleFileUploadCertificadoGerente(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Metodo de anulación del diario general">
    public void anulacionDiarioGeneral() {
        DiarioGeneral diarioG = new DiarioGeneral();
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioG.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        diarioG.setCertificacionesPresupuestario(diarioGeneral.getCertificacionesPresupuestario());
        diarioG.setClase(diarioGeneral.getClase());
        diarioG.setTipo(diarioGeneral.getTipo());
        diarioG.setObservacion("ANULACIÓN " + diarioGeneral.getObservacion());
        diarioG.setEstadoDiario("REGISTRADO");
        diarioG.setUsuarioCreacion(userSession.getNameUser());
        diarioG.setFechaCreacion(new Date());
        diarioG.setEstado(Boolean.TRUE);
        diarioG.setPeriodo(opcionBusqueda.getAnio());
        diarioG.setEstadoTransaccion(diarioGeneral.getEstadoTransaccion());
        diarioG.setFechaElaboracion(new Date());
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
        diarioG = diarioGeneralService.create(diarioG);
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
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
            if (detalleDG.getComprometido().doubleValue() > 0) {
                detalleT.setComprometido(new BigDecimal(detalleDG.getComprometido().doubleValue() * (-1)));
            } else {
                detalleT.setComprometido(detalleDG.getComprometido());
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
            detalleT.setContador(detalleDG.getContador());
            detalleT.setDiarioGeneral(diarioG);
            detalleTransaccionService.create(detalleT);
        }
        /*Actualizar Diario Anulado*/
        diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
        diarioGeneral.setFechaModificacion(new Date());
        diarioGeneral.setFechaAnulacion(new Date());
        diarioGeneral.setEstadoDiario("ANULADO");
        diarioGeneralService.edit(diarioGeneral);
        this.diarioGeneral = diarioG;
        imprimirDiarioGeneral();
        cancelar();
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
        this.presupuestoLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
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
        BigDecimal saldoReservasAprobadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnio(), "APRO");
        BigDecimal saldoReservasLiquidadas = diarioGeneralService.getSaldoReservas(presupuesto.getPartida(), opcionBusqueda.getAnio(), "LIQUI");
        BigDecimal saldoDevengado = diarioGeneralService.getsaldoDevengado(presupuesto.getPartida(), opcionBusqueda.getAnio());
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
    //<editor-fold defaultstate="collapsed" desc="Establecer la relación presupuestaria en el ingreso del Debe -Haber">
    public void determinarRelacionPresupuestaria(DetalleTransaccion detalleDG, Boolean tipoIngreso) {
        reiniciarValoresDinamicos();
        Boolean accion = Boolean.FALSE;
        if (!detalleDG.getDatoCargado()) {
            if (diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                detalleDG.setTipoTransaccion(null);
                detalleDG.setPartidaPresupuestaria(null);
                detalleDG.setEstructuraProgramatica(null);
                detalleDG.setFuente(null);
                detalleDG.setCedulaPresupuestaria(null);
                detalleDG.setComprometido(BigDecimal.ZERO);
                detalleDG.setDevengado(BigDecimal.ZERO);
                detalleDG.setEjecutado(BigDecimal.ZERO);
                CatalogoPresupuesto partidaPresupuestaria = null;
                partidaPresupuestariaRelacionadas = new ArrayList<>();
                presupuestoRelacionado = new ArrayList<>();
                if (tipoIngreso) {
                    if (!detalleDG.getDatoCargado()) {
                        if (detalleDG.getCuentaContable().getTitulo() == 1 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
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
                } else {
                    if (!detalleDG.getDatoCargado()) {
                        if (detalleDG.getCuentaContable().getTitulo() == 2 && detalleDG.getCuentaContable().getGrupo() == 1 && detalleDG.getCuentaContable().getSubGrupo() == 3) {
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
                if (accion) {
                    this.detalleTransaccion = detalleDG;
                    if (presupuestoRelacionado.size() == 1) {
                        for (Presupuesto presupuesto : presupuestoRelacionado) {
                            this.presupuestoSeleccionado = presupuesto;
                            guardarRelacionesPresupuestarias();
                        }
                    } else {
                        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                        PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                    }
                }
            }
        }
    }

    private void añadirRelacionItemPresupuesto(CatalogoPresupuesto catPresupuesto) {
        List<Presupuesto> auxiliarList = diarioGeneralService.getListadoPresupuesto(catPresupuesto, opcionBusqueda.getAnio());
        if (auxiliarList != null) {
            for (Presupuesto presupuesto : auxiliarList) {
                presupuestoRelacionado.add(presupuesto);
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        for (DetalleTransaccion detalleDG : detalleDiarioGeneral) {
            if (detalleDG.getContador().equals(detalleTransaccion.getContador())) {
                detalleDG.setPartidaPresupuestaria(presupuestoSeleccionado.getItem());
                detalleDG.setCedulaPresupuestaria(presupuestoSeleccionado.getPartida());
                detalleDG.setFuente(presupuestoSeleccionado.getFuenteDirecta());
                detalleDG.setEstructuraProgramatica(presupuestoSeleccionado.getEstructura());
            }
        }
        calcularTotalesDetalleDiarioGeneral();
        reiniciarValoresDinamicos();
        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
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
            this.inventarioLazyModel.getFilterss().put("anio", opcionBusqueda.getAnio());
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
                inventario.setPeriodoContable(opcionBusqueda.getAnio());
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
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.reservaCompromiso = new SolicitudReservaCompromiso();
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "rol_general1":
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "rol_general");
                    break;
                case "rol_beneficios":
                    this.renderedRolesBeneficios = Boolean.TRUE;
                    this.renderedRoles = Boolean.FALSE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesBeneficiosList = diarioGeneralService.getTiposRolesBeneficios(opcionBusqueda.getAnio(), "aprobado-rol");
                    break;
                case "rol_fondos_reserva":
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.TRUE;
                    this.botonBeneficiarioFondosReserva = Boolean.TRUE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnio(), "aprobado-rol");
                    break;
                case "rol_extras_suplementarias":
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "ROL-HORAS-EXTRAS");
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
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoRolHorasExtras(tipoRolSeleccionado, opcionBusqueda.getAnio());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioRolHorasExtras(tipoRolSeleccionado, opcionBusqueda.getAnio());
            } else {
                if (fondoReserva) {
                    this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS FONDO DE RESERVAS ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                    this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-FONDOS-RESERVA", true);
                } else {
                    this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                    this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnio());
                    this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnio());
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
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoDecimoTercero(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioDecimoTercero(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                break;
            case "ROL_TIPO_DEC_CUARTO":
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoDecimoCuarto(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiaripDecimoCuarto(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                break;
            case "ROL_TIPO_BEN_SIN":
                this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoBeneficiosSindicales(tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioBeneficiosSindicales(tipoRolBeneficiosSeleccionado);
                break;
        }
        openDlgRegistrarReservaCompromiso();
    }

    private void openDlgRegistrarReservaCompromiso() {
        this.totalLiquidacion = 0;
        Integer maximo = reservaCompromisoService.getMaxCodigo(reservaCompromiso.getPeriodo());
        reservaCompromiso.setSecuencial(maximo);
        reservaCompromiso.setPeriodo(opcionBusqueda.getAnio());
        reservaCompromiso.setFechaSolicitud(new Date());
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

    public void generarResevaCompromiso() {
        if (beneficiarioReservaCompromisoList.isEmpty() && beneficiarioReservaCompromisoList == null) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar un Beneficiario");
            return;
        }
        int contador = 0;
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            if (cuentaPartida.getMonto_2().doubleValue() <= 0) {
                contador += 1;
            }
        }
        if (contador > 0) {
            JsfUtil.addWarningMessage("AVISO", "Tiene valores negativos en el listado de la liquidacion de rol");
            return;
        }
        /*Creamos la Reserva de Compromiso*/
        reservaCompromiso.setFechaCreacion(new Date());
        reservaCompromiso.setUsuarioCreacion(userSession.getName());
        reservaCompromiso.setEstado(diarioGeneralService.getEstadoAprobado("APRO"));
        reservaCompromiso.setComprometido(Boolean.TRUE);
        reservaCompromiso.setFechaActualizacion(new Date());
        reservaCompromiso.setFechaAprobacion(new Date());
        reservaCompromiso.setDescripcion(reservaCompromiso.getDescripcion().toUpperCase());
        Procedimiento procedimiento = procedimientoService.getProcedimientoNoAplica("NO APLICA");
        if (procedimiento != null) {
            reservaCompromiso.setProcedimiento(procedimiento);
        }
        reservaCompromiso = reservaCompromisoService.create(reservaCompromiso);
        /*Guardamos el detalle de la reserva de compromiso*/
        for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentasConPartida) {
            DetalleSolicitudCompromiso detalleSolicitudReserva = new DetalleSolicitudCompromiso();
            detalleSolicitudReserva.setSolicitud(reservaCompromiso);
            detalleSolicitudReserva.setMontoDisponible(cuentaPresupuesto.getMonto_1());
            detalleSolicitudReserva.setMontoSolicitado(cuentaPresupuesto.getMonto_2());
            detalleSolicitudReserva.setEstado(Boolean.TRUE);
            detalleSolicitudReserva.setPeriodo(opcionBusqueda.getAnio());
            detalleSolicitudReserva.setUsuarioCreacion(userSession.getName());
            detalleSolicitudReserva.setFechaCreacion(new Date());
            detalleSolicitudReserva.setPresupuesto(diarioGeneralService.getPresupuesto(cuentaPresupuesto.getPartidaPresupuestaria(), opcionBusqueda.getAnio()));
            detalleSolicitudReserva.setMontoComprometido(cuentaPresupuesto.getMonto_3());
            detalleReservaService.create(detalleSolicitudReserva);
        }
        /*Asignamos los beneficiarios a la reserva de compromiso*/
        for (BeneficiarioSolicitudReserva beneficiario : beneficiarioReservaCompromisoList) {
            if (beneficiario.getTipoBeneficiario()) {
                reservaCompromiso.setBeneficiario(beneficiario.getBeneficiario());
                reservaCompromiso.setTipoBeneficiario(Boolean.TRUE);
                reservaCompromiso.setNomina(Boolean.FALSE);
                reservaCompromisoService.edit(reservaCompromiso);
            } else {
                reservaCompromiso.setNomina(Boolean.TRUE);
                reservaCompromisoService.edit(reservaCompromiso);
                beneficiario.setReservaCompromiso(reservaCompromiso);
                beneficiarioSolicitudReservaService.create(beneficiario);
            }
        }
        /*Pasamos la información de la reserva de compromiso al detalle del diario General*/
        diarioGeneral.setCertificacionesPresupuestario(reservaCompromiso);
        if (reservaCompromiso.getNomina()) {
            diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
        } else {
            diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
        }
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
        contabilizarModuloNomina();
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
        if (tipoRolSeleccionado != null) {
            if (fondoReserva) {
                tipoRolSeleccionado.setDiarioFondosReserva(Boolean.TRUE);
            } else {
                tipoRolSeleccionado.setDiarioRolGeneral(Boolean.TRUE);
            }
            tipoRolService.edit(tipoRolSeleccionado);
        }
        if (tipoRolBeneficiosSeleccionado != null) {
            tipoRolBeneficiosSeleccionado.setDiarioRolBeneficios(Boolean.TRUE);
            tipoRolBeneficiosService.edit(tipoRolBeneficiosSeleccionado);
        }
        actualizarValorReservaPartidaDistributivas();
        restablecerModuloNomina();
    }

    private void actualizarValorReservaPartidaDistributivas() {
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            ProformaPresupuestoPlanificado tipoPartida = diarioGeneralService.getTipoPartida(cuentaPartida.getPartidaPresupuestaria(), opcionBusqueda.getAnio());
            if (tipoPartida != null) {
                List<ServidorMontoModel> sevidorMontoList = new ArrayList<>();
                if (tipoRolSeleccionado != null) {
                    sevidorMontoList = diarioGeneralService.getServidorRolGeneralFondoReserva(cuentaPartida.getPartidaPresupuestaria(), tipoRolSeleccionado, opcionBusqueda.getAnio(), fondoReserva);
                }
                if (tipoRolBeneficiosSeleccionado != null) {
                    sevidorMontoList = diarioGeneralService.getServidorBeneficios(cuentaPartida.getPartidaPresupuestaria(), tipoRolBeneficiosSeleccionado, opcionBusqueda.getAnio());
                }
                for (ServidorMontoModel servidorMonto : sevidorMontoList) {
                    if (tipoPartida.getCodigo().equals("PD")) {
                        PartidasDistributivo partidaDistributivo = diarioGeneralService.getPartidaDistributiva(tipoPartida.getPartidaPresupuestaria(), tipoPartida.getPeriodo(), servidorMonto.getServidor());
                        if (partidaDistributivo != null) {
                            if (partidaDistributivo.getReserva() != null) {
                                double valorReserva = partidaDistributivo.getReserva().doubleValue() + servidorMonto.getMonto().doubleValue();
                                partidaDistributivo.setReserva(new BigDecimal(valorReserva));
                            } else {
                                partidaDistributivo.setReserva(servidorMonto.getMonto());
                            }
                            partidaDistributivo.setReserva(servidorMonto.getMonto());
                            partidaDistributivo.setUsuarioModificacion(userSession.getNameUser());
                            partidaDistributivo.setFechaModificacion(new Date());
                            partidaDistributivaService.edit(partidaDistributivo);
                        }
                    } else if (tipoPartida.getCodigo().equals("PDA")) {
                        PartidasDistributivoAnexo partidaDistributivoAnexo = diarioGeneralService.getPartidaDistributivoAnexo(tipoPartida.getPartidaPresupuestaria(), tipoPartida.getPeriodo());
                        if (partidaDistributivoAnexo != null) {
                            if (partidaDistributivoAnexo.getReserva() != null) {
                                double valorReserva = partidaDistributivoAnexo.getReserva().doubleValue() + servidorMonto.getMonto().doubleValue();
                                partidaDistributivoAnexo.setReserva(new BigDecimal(valorReserva));
                            } else {
                                partidaDistributivoAnexo.setReserva(servidorMonto.getMonto());
                            }
                            partidaDistributivoAnexo.setUsuarioModificacion(userSession.getNameUser());
                            partidaDistributivoAnexo.setFechaModificacion(new Date());
                            partidaDistributivoAnexoService.edit(partidaDistributivoAnexo);
                        }
                    }
                }
            }
        }
    }

    private void restablecerModuloNomina() {
        this.subEnlaceSeleccionado = new CatalogoItem();
        this.totalLiquidacion = 0;
        this.rolesBeneficiosList = new ArrayList<>();
        this.rolesList = new ArrayList<>();
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.proveedorSeleccionado = new Proveedor();
    }

    public boolean verificardor(DiarioGeneral d) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        String anioElaboracion = simpleDateFormat.format(d.getFechaElaboracion());
        String anioActual = simpleDateFormat.format(new Date());
        if (anioElaboracion.equals(anioActual)) {
            return true;
        }
        return false;
    }

    public BigDecimal retornaValorComprobantePago(DiarioGeneral d) {
        return diarioGeneralService.retornarValorCpago(d);
    }

    public void abriDlogo(DiarioGeneral d) {
        this.diarioGeneral = new DiarioGeneral();
        this.diarioGeneral = d;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {
        try {
            int aprobar = 0;
            observacion.setObservacion(observaciones);
            if (diarioGeneral != null) {
                if (verificardor(diarioGeneral)) {
                    aprobar = 1;
                } else {
                    aprobar = 0;
                }
            } else {
                aprobar = 1;
            }
            switch (tramite.getTipoTramite().getAbreviatura()) {
                case "PAG_ANTI_LIQUI_HABER":
                case "PAG_ANTI_VIATICOS":
                case "proceso_pago_ceb":
                case "proceso_pca_bienes":
                case "proceso_pce_servicios":
                case "proceso_pc_consultoria":
                case "proceso_pco_publica":
                case "proceso_pc_servicios":
                case "proceso_pi_cuantia_bienes":
                case "proceso_pi_cuantia_servicios":
                case "PAG_INF_CUANT_OB_MENOR":
                case "PAG_SERV_BASIC":
                    getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    break;
                //
                case "PPS_profesionales":
                case "procesos_pagos_noClasficados":
                    getParamts().put("verificar", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                case "PAG_SERV_NOTARIALES":
                case "proceso_jgvis_servicios":
                    getParamts().put("aprobado", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                case "PPRM":
                case "RET_JUD":
                case "PPPI":
                case "PAG_DEC":
                case "PRHEXSU":
                case "procesos_rf_caja_chica":
                    getParamts().put("aprobado", aprobar);
                    getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    break;
                default:
                    getParamts().put("aprobadoAutorizacion", aprobar);
                    if (aprobar == 1) {
                        getParamts().put("usuarioGeneracionCp", clienteService.getrolsUser(RolUsuario.contador));
                        getParamts().put("form", "/proceso/contabilidad/pagoServicios/comprobante-pago");
                    }
                    break;
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

    // ${aprobadoAutorizacion==1}
//</editor-fold>

    /*MODULO: RESERVA COMPROMISO*/
    //<editor-fold defaultstate="collapsed" desc="Reserva de compromiso">
    public void actualizarTablaReservaCompromiso() {
        if (tipoSeleccionado != null) {
            reservaCompromisoList = diarioGeneralService.getAdquisiciones("APRO", opcionBusqueda.getAnio(), tipoSeleccionado, diarioGeneral.getFechaElaboracion());
        } else {
            reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion());
        }
        PrimeFaces.current().ajax().update("reservaCompromisoTable");
        PrimeFaces.current().ajax().update("moduloReservaCompromisoForm");
    }

    public void openDlgDetalleReservaCompromiso(SolicitudReservaCompromiso reservaC) {
        this.reservaCompromiso = reservaC;
        this.cuentasConPartida = new ArrayList<>();
        this.filtroPartidas = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = diarioGeneralService.getDetalleReservaCompromiso(reservaCompromiso, opcionBusqueda.getAnio());
        this.adquisicion = diarioGeneralService.getAdquisicion(reservaCompromiso);
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
        contador = 0;
        relacionCuentasContables = new ArrayList<>();
        List<CuentaContable> auxiliarList;
        for (CuentaContablePresupuestoModel cuentaPartida : cuentasConPartida) {
            auxiliarList = diarioGeneralService.getCuentasContables(cuentaPartida.getCatalogoPresupuesto(), opcionBusqueda.getAnio());
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
                auxiliarList = diarioGeneralService.getCuentasContables(cuentaPartida.getCatalogoPresupuesto(), opcionBusqueda.getAnio());
                for (CuentaContable cuenta : auxiliarList) {
                    detalleDG.setCuentaContable(cuenta);
                    if (auxiliarList.size() > 1) {
                        if (!relacionCuentasContables.contains(cuenta)) {
                            relacionCuentasContables.add(cuenta);
                        }
                    }
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
            JsfUtil.addWarningMessage("AVISO", "Antes de insertar las partidas al diario general, verificar las relaciones de cuenta contable y catálogo presupuesto");
            return;
        }
        if (!relacionCuentasContables.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Revisar las relaciones de la cuenta contable con la partida presupuestaria");
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
    }

    private void contabilizarReservaCompromiso() {
        int contador = 0;
        if (reservaCompromiso != null) {
            for (CuentaContablePresupuestoModel cuentaPresupuesto : cuentaContablePresupuestoModelList) {
                BigDecimal totalPartidasRegistradas = diarioGeneralService.totalPartidaRegistrada(reservaCompromiso, cuentaPresupuesto.getPartidaPresupuestaria(), cuentaPresupuesto.getIdDetalleReserva());
                if (cuentaPresupuesto.getMonto_1().equals(totalPartidasRegistradas)) {
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
    }
//</editor-fold>

    /*MÓDULO: TESORERÍA*/
    //<editor-fold defaultstate="collapsed" desc="Tesorería">
    public void procesoTesoreriaSeleccionado() {
        if (subEnlaceSeleccionado != null) {
            this.corteOrdenCobroLazyModel = new LazyModel<>(CorteOrdenCobro.class);
            this.corteOrdenCobroLazyModel.getSorteds().put("codigo", "ASC");
            this.corteOrdenCobroLazyModel.getFilterss().put("estado", true);
            this.corteOrdenCobroLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
            this.recaudacionlLazyModel = new LazyModel<>(Recaudacion.class);
            this.recaudacionlLazyModel.getSorteds().put("codigo", "ASC");
            this.recaudacionlLazyModel.getFilterss().put("estado", true);
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "emisiones":
                    this.tableCorteOrdenCobro = Boolean.TRUE;
                    this.tableRecaudaciones = Boolean.FALSE;
                    this.procesoCobroEmisor = Boolean.TRUE;
                    this.tableRetenciones = Boolean.FALSE;
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoEmision", false);
                    break;
                case "cobros_caja":
                    this.tableCorteOrdenCobro = Boolean.TRUE;
                    this.tableRecaudaciones = Boolean.FALSE;
                    this.procesoCobroEmisor = Boolean.FALSE;
                    this.tableRetenciones = Boolean.FALSE;
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoEmision", true);
                    this.corteOrdenCobroLazyModel.getFilterss().put("contabilizadoCobroCaja", false);
                    break;
                case "recaudaciones_cobros":
                    this.tableCorteOrdenCobro = Boolean.FALSE;
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.tableRetenciones = Boolean.FALSE;
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar", 0);
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "COBROEM");
                    break;
                case "recaudaciones_ajustes":
                    this.tableCorteOrdenCobro = Boolean.FALSE;
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.tableRetenciones = Boolean.FALSE;
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "recaudaciones_cobros_ajustes":
                    this.tableCorteOrdenCobro = Boolean.FALSE;
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.tableRetenciones = Boolean.FALSE;
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar:gt", 0);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "retenciones":
                    this.retencionesSeleccionadas = new ArrayList<>();
                    this.tableCorteOrdenCobro = Boolean.FALSE;
                    this.tableRecaudaciones = Boolean.FALSE;
                    this.periodosFiscales = diarioGeneralService.getPeriodosFiscales(opcionBusqueda.getAnio());
                    this.tipoRetenciones = diarioGeneralService.getTiposRetenciones("RETENCION");
                    break;
            }
        } else {
            this.tableCorteOrdenCobro = Boolean.FALSE;
            this.tableRecaudaciones = Boolean.FALSE;
            this.procesoCobroEmisor = Boolean.FALSE;
            this.tableRecaudaciones = Boolean.FALSE;
        }
    }

    public void actualizarTablaRetenciones() {
        if (tipoRetencionSeleccionado != null && periodoSeleccionado != null) {
            this.liquidacionDetalleList = diarioGeneralService.getListDetalleRetencion(opcionBusqueda.getAnio(), tipoRetencionSeleccionado, periodoSeleccionado, "RECIBIDA;AUTORIZADO");
            this.tableRetenciones = Boolean.TRUE;
        } else {
            this.liquidacionDetalleList = new ArrayList<>();
        }
        PrimeFaces.current().ajax().update("retencionTable");
    }

    public void retencionesSeleccionadas() {
        if (retencionesSeleccionadas.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Debe seleccionar al menos una retención");
        } else {
            BigInteger bigInteger;
            for (LiquidacionDetalle retencion : retencionesSeleccionadas) {
                List<CuentaContablePresupuestoModel> cuentaPresupuesto = null;//diarioGeneralService.getListPartidasSaldos(retencion.getCuentaContableRetencion().getCuentaContable(), retencion.getLiquidacion().getDiarioGeneral());
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
            }
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
        if (retencionesSeleccionadas.isEmpty()) {
            for (LiquidacionDetalle detalleRetencion : retencionesSeleccionadas) {
                detalleRetencion.setContabilizado(Boolean.TRUE);
                detalleRetencion.setFechaContabilizado(new Date());
                detalleRetencion.setDiarioGeneral(diarioGeneral);
                liquidacionDetalleService.edit(detalleRetencion);
            }
        }
        restablecerModuloTesoreria();
    }

    private void restablecerModuloTesoreria() {
        this.recaudacionSelecionada = new Recaudacion();
        this.corteOrdenCobroSeleccionada = new CorteOrdenCobro();
        this.subEnlaceSeleccionado = new CatalogoItem();
        this.tableCorteOrdenCobro = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
    }
//</editor-fold>

    /*GET - SET*/
    //<editor-fold defaultstate="collapsed" desc="Get - Set">
    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

    public List<LiquidacionDetalle> getLiquidacionDetalleList() {
        return liquidacionDetalleList;
    }

    public void setLiquidacionDetalleList(List<LiquidacionDetalle> liquidacionDetalleList) {
        this.liquidacionDetalleList = liquidacionDetalleList;
    }

    public boolean isDetalleDiario() {
        return detalleDiario;
    }

    public void setDetalleDiario(boolean detalleDiario) {
        this.detalleDiario = detalleDiario;
    }

    public boolean isShowRequisios() {
        return showRequisios;
    }

    public void setShowRequisios(boolean showRequisios) {
        this.showRequisios = showRequisios;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
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

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public DetalleTransaccion getDetalleTransaccion() {
        return detalleTransaccion;
    }

    public void setDetalleTransaccion(DetalleTransaccion detalleTransaccion) {
        this.detalleTransaccion = detalleTransaccion;
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

    public double getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(double totalDebe) {
        this.totalDebe = totalDebe;
    }

    public double getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(double totalHaber) {
        this.totalHaber = totalHaber;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

//</editor-fold>
}
