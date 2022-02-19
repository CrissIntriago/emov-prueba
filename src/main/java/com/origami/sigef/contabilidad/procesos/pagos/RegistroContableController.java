/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.procesos.pagos;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.DetalleReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Garantias;
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
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CobrosEmisionesModel;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.contabilidad.service.GarantiaService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.RetencionesRegistradasService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.talentohumano.services.ServidorService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jintr
 */
@Named(value = "registroContableView")
@ViewScoped
public class RegistroContableController extends BpmnBaseRoot implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private BeneficiarioSolicitudReservaService beneficiarioSolicitudReservaService;
    @Inject
    private PartidaDistributivoService partidaDistributivaService;
    @Inject
    private PartidaDistributivoAnexoService partidaDistributivoAnexoService;
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private DetalleReservaCompromisoService detalleReservaService;
    @Inject
    private TipoRolBeneficiosService tipoRolBeneficiosService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CorteOrdenCobroService corteOrdenCobroService;
    @Inject
    private RecaudacionService recaudacionService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private RetencionesRegistradasService retencionesRegistradasService;
    @Inject
    private GarantiaService garantiasService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private ServidorService servidorService;

    private CatalogoItem moduloDeEnlaceSeleccionado;
    private CuentaContable cuentaContableSeleccionado;
    private DiarioGeneral diarioGeneral;
    private DetalleTransaccion detalleTransaccion;
    private OpcionBusqueda opcionBusqueda;
    private SolicitudReservaCompromiso reservaCompromiso;
    private Presupuesto presupuestoSeleccionado;
    private CatalogoItem tipoSeleccionado;
    private Adquisiciones adquisicion;
    private TipoRol tipoRolSeleccionado;
    private TipoRolBeneficios tipoRolBeneficiosSeleccionado;
    private Proveedor proveedorSeleccionado;
    private CorteOrdenCobro corteOrdenCobroSeleccionada;
    private Recaudacion recaudacionSelecionada;

    private int proceso;

    private BigInteger numTramite;

    private String observaciones;

    private double totalComprometido;
    private double totalDevengado;
    private double totalEjecutado;
    private double totalLiquidacion;

    private LazyModel<DiarioGeneral> diarioGeneralLazyModel;
    private LazyModel<CuentaContable> cuentaContableLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel;
    private LazyModel<Recaudacion> recaudacionlLazyModel;
    private LazyModel<Adquisiciones> adquisicionesLazyModel;

    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<CatalogoItem> subEnlace;
    private List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas;
    private List<DetalleTransaccion> detalleDiarioGeneral;
    private List<CatalogoPresupuesto> filtroPartidas;
    private List<SolicitudReservaCompromiso> reservaCompromisoList;
    private List<Presupuesto> presupuestoRelacionado;
    private List<CuentaContable> relacionCuentasContables;
    private List<CuentaContablePresupuestoModel> cuentasConPartida;
    private List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList;
    private List<TipoRol> rolesList;
    private List<CatalogoItem> tiposAdquisiciones;
    private List<TipoRolBeneficios> rolesBeneficiosList;
    private List<BeneficiarioSolicitudReserva> beneficiarioReservaCompromisoList;
    private List<LiquidacionDetalle> retencionesSeleccionadas;
    private List<LiquidacionDetalle> liquidacionDetalleList;
    private List<RetencionesRegistradas> retencionesRegistradasList;
    private List<DetalleTransaccion> detalleDiarioEliminar;
    private List<Adquisiciones> adquisicionesList;
    private List<DetalleSolicitudCompromiso> detalleReservaList;

    private List<String> periodosFiscales;
    private List<String> tipoRetenciones;

    /*STRING AUXILIARES*/
    private String periodoSeleccionado;
    private String tipoRetencionSeleccionado;
    private String tipoGarantia;

    private List<Garantias> garantiasList;
    private List<Garantias> garantiasSeleccionadasList;

    private Boolean renderedRolesBeneficios;
    private Boolean renderedRoles;
    private Boolean fondoReserva;
    private Boolean botonBeneficiarioFondosReserva;
    private Boolean tableCorteOrdenCobro;
    private Boolean procesoCobroEmisor;
    private Boolean tableRecaudaciones;
    private Boolean tableRetenciones;
    private Boolean botonCuentasContables;
    private Boolean viewDiarioGeneral;
    private Boolean accionesEditar;
    private Boolean booleanAdquisiciones;
    private Boolean validarRechazoRol = false;
    private Boolean rechazoRol;
    private Boolean btnNuevoRegistro;
    private Boolean renderedListRol;

    private CatalogoItem subEnlaceSeleccionado;

    @PostConstruct
    public void initialize() {
        opcionBusqueda = new OpcionBusqueda();
        numTramite = null;
        btnNuevoRegistro = false;
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                switch (getTramite().getTipoTramite().getAbreviatura()) {
                    case "PAG_ANTI_LIQUI_HABER":
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_certificaciones_presupuestarias", 1);
                        break;
                    case "PPS_profesionales":
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_certificaciones_presupuestarias", 2);
                        break;
                    case "CIERRE_CAJA":
                        btnNuevoRegistro = true;
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_tesoreria", 1);
                        break;
                    case "PAG_DEC":
                        renderedRolesBeneficios = Boolean.TRUE;
                        this.rolesBeneficiosList = diarioGeneralService.getTiposRolesBeneficios(opcionBusqueda.getAnio(), "aprobado-rol");
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_nomina", 1);
                        break;
                    case "PRHEXSU":
                        renderedRoles = Boolean.TRUE;
                        validarRechazoRol = true;
                        rechazoRol = false;
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "ROL-HORAS-EXTRAS");
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_nomina", 1);
                        break;
                    case "PPRM":
                        renderedRoles = Boolean.TRUE;
                        validarRechazoRol = true;
                        rechazoRol = false;
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "rol_general");
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_nomina", 1);
                        break;
                    default:
                        moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_certificaciones_presupuestarias", 1);
                        break;
                }
            }
        }
        this.opcionBusqueda = new OpcionBusqueda();
        verificarRegistros();
        this.diarioGeneralLazyModel = new LazyModel<>(DiarioGeneral.class);
        this.diarioGeneralLazyModel.getSorteds().put("id", "ASC");
        this.diarioGeneralLazyModel.getFilterss().put("estado", true);
        this.diarioGeneralLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        this.diarioGeneralLazyModel.getFilterss().put("numTramite", tramite.getNumTramite());
        this.clasesDiarioGeneral = catalogoService.getItemsByCatalogo("diario_general_clases");
        this.tiposAdquisiciones = adquisicionesService.getTipos("sub_tipo_adquisicion");
        form(null, "nuevo");
        this.diarioGeneral.setFechaElaboracion(new Date());
    }

    /*Solo funcionara para el proceso de cierre a caja*/
    public void metodoCierreCaja() {
        opcionBusqueda = new OpcionBusqueda();
        numTramite = null;
        btnNuevoRegistro = true;
        if (this.session.getTaskID() != null) {
            this.setTaskId(this.session.getTaskID());
            observacion = new Observaciones();
            observacion.setIdTramite(tramite);
            btnNuevoRegistro = true;
            moduloEnlaceSeleccionado("diario_general_modulos_enlace", "modulo_tesoreria", 1);
        }
        this.opcionBusqueda = new OpcionBusqueda();
        this.clasesDiarioGeneral = catalogoService.getItemsByCatalogo("diario_general_clases");
        this.tiposAdquisiciones = adquisicionesService.getTipos("sub_tipo_adquisicion");
        form(null, "nuevo");
        this.diarioGeneral.setFechaElaboracion(new Date());
        this.viewDiarioGeneral = Boolean.FALSE;
    }

    private void verificarRegistros() {
        Boolean condicion = diarioGeneralService.getDiarioGeneralTramite(tramite.getNumTramite(), opcionBusqueda.getAnio());
        if (condicion) {
            this.viewDiarioGeneral = Boolean.TRUE;
        } else {
            this.viewDiarioGeneral = Boolean.FALSE;
        }
    }

    public void cancelar() {
        reiniciarValoresTotales();
        reiniciarCuentaContable();
        restablecerModuloTesoreria();
        restablecerModuloNomina();
        this.diarioGeneral = new DiarioGeneral();
        this.subEnlaceSeleccionado = null;
        this.subEnlace = new ArrayList<>();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = new ArrayList<>();
        this.tiposDiarioGeneral = new ArrayList<>();
        this.cuentasConPartida = new ArrayList<>();
        this.detalleDiarioEliminar = new ArrayList<>();
        this.detalleReservaList = new ArrayList<>();
        this.reservaCompromiso = null;
        this.moduloDeEnlaceSeleccionado = null;
        this.corteOrdenCobroLazyModel = null;
        this.recaudacionlLazyModel = null;
        this.adquisicion = null;
        this.tipoRetencionSeleccionado = "";
        this.viewDiarioGeneral = Boolean.TRUE;
        PrimeFaces.current().ajax().update("formMain");
    }

    private void moduloEnlaceSeleccionado(String catalogo, String catalogoItem, int proceso) {
        this.proceso = proceso;
        moduloDeEnlaceSeleccionado = catalogoItemService.getCatalogoI(catalogo, catalogoItem);
    }

    public void form(DiarioGeneral diarioG, String accion) {
        this.adquisicion = new Adquisiciones();
        this.diarioGeneral = new DiarioGeneral();
        this.detalleDiarioGeneral = new ArrayList<>();
        this.diarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        this.detalleDiarioGeneral = new ArrayList<>();
        this.detalleDiarioEliminar = new ArrayList<>();
        this.accionesEditar = Boolean.FALSE;
        if (diarioG != null) {
            if (diarioG.getCertificacionesPresupuestario() != null) {
                this.reservaCompromiso = diarioG.getCertificacionesPresupuestario();
            }
            if (diarioG.getEnlace() != null) {
                moduloDeEnlaceSeleccionado = diarioG.getEnlace();
            }
            this.viewDiarioGeneral = Boolean.FALSE;
            if (accion.equals("visualizar")) {
                this.accionesEditar = Boolean.TRUE;
            }
            this.diarioGeneral = diarioG;
            List<DetalleTransaccion> auxiliar = detalleTransaccionService.getDetalleTransaccion(diarioGeneral);
            for (DetalleTransaccion detalleDG : auxiliar) {
                detalleDiarioGeneral.add(detalleDG);
            }
            actualizarTipoDiarioGeneral();
            calcularTotalesDetalleDiarioGeneral();
        }
        if (!accion.equals("nuevo")) {
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    private void colocarNumActa() {
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        if (ultimaActa != null) {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            this.diarioGeneral.setNumeroTransaccion(BigInteger.valueOf(1));
        }
    }

    public void enlaceSeleccionado() {
        if (moduloDeEnlaceSeleccionado != null) {
            switch (moduloDeEnlaceSeleccionado.getCodigo()) {
                case "modulo_certificaciones_presupuestarias":
                    listadoReservaCompromiso();
                    break;
                case "modulo_nomina":
                    this.subEnlace = catalogoItemService.getPadreCatalogoItem(moduloDeEnlaceSeleccionado);
                    this.fondoReserva = Boolean.FALSE;
                    this.reservaCompromiso = new SolicitudReservaCompromiso();
                    this.filtroPartidas = new ArrayList<>();
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

    private void listadoReservaCompromiso() {
        Cliente clienteAux = null;
        switch (getTramite().getTipoTramite().getAbreviatura()) {
            case "PAG_ANTI_LIQUI_HABER":
            case "procesos_rf_caja_chica":
                clienteAux = servidorService.find((BigInteger) getTramite().getIdReferencia()).getPersona();
                if (clienteAux != null) {
                    reservaCompromisoList = diarioGeneralService.getReservaBeneficiario("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion(), clienteAux);
                } else {
                    reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion());
                }
                break;
            case "PAG_SERV_NOTARIALES":
                clienteAux = proveedorService.findById((BigInteger) getTramite().getIdReferencia()).getCliente();
                if (clienteAux != null) {
                    reservaCompromisoList = diarioGeneralService.getReservaBeneficiario("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion(), clienteAux);
                } else {
                    reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion());
                }
                break;
            case "proceso_pago_ceb":
            case "proceso_pce_servicios":
            case "proceso_pca_bienes":
            case "proceso_pc_consultoria":
            case "proceso_pco_publica":
            case "proceso_pc_servicios":
            case "proceso_pi_cuantia_bienes":
            case "proceso_pi_cuantia_servicios":
            case "PAG_INF_CUANT_OB_MENOR":
                this.adquisicion = adquisicionesService.getAdquisicionById((BigInteger) getTramite().getIdReferencia());
                reservaCompromisoList = diarioGeneralService.getContractosResrrvas(this.adquisicion);
                break;
            default:
                reservaCompromisoList = diarioGeneralService.getListadoReservaCompromiso("APRO", opcionBusqueda.getAnio(), diarioGeneral.getFechaElaboracion());
                break;
        }
        PrimeFaces.current().executeScript("PF('moduloReservaCompromisoDlg').show()");
        PrimeFaces.current().ajax().update("formoduloReservaCompromisoDlg");
    }

    public void validaciones() {
        /*Validacion de que el periodo esta abierto*/
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(diarioGeneral.getFechaElaboracion()), Utils.convertirMesALetra(Utils.getMes(diarioGeneral.getFechaElaboracion())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada esta cerrado");
            return;
        }
        /*Validamos que el asiento este cuadrado */
        if (diarioGeneral.getEstadoTransaccion() != null && diarioGeneral.getEstadoTransaccion().equals("DESCUADRADO")) {
            JsfUtil.addErrorMessage("ERROR!!", "No se puede guardar un diario general descuadrado");
            return;
        }
        if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_certificaciones_presupuestarias")) {
            if (reservaCompromiso == null) {
                JsfUtil.addErrorMessage("ERROR!!", "Debe seleccionar una reserva antes de continuar");
                return;
            }
        }
        /*Validamos que en el diario esten registradas las cuentas*/
        if (detalleDiarioGeneral.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "No hay detalle en el asiento contable");
            return;
        }
        /*Validamos que se cumpla la partida doble "DEBE = HABER"*/
        if ("DESCUADRADO".equals(diarioGeneral.getEstadoTransaccion())) {
            JsfUtil.addWarningMessage("AVISO", "El asiento del libro diario debe estar cuadrado antes de guardar");
            return;
        }
        save();
        verificarRegistros();
        cancelar();
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("diarioGeneralTable");
    }

    public void save() {
        if (diarioGeneral.getId() != null) {
            diarioGeneral.setUsuarioModificacion(userSession.getNameUser());
            diarioGeneral.setFechaModificacion(new Date());
            diarioGeneralService.edit(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                if (transaccion.getId() != null) {
                    detalleTransaccionService.edit(transaccion);
                } else {
                    transaccion.setDiarioGeneral(diarioGeneral);
                    detalleTransaccionService.create(transaccion);
                }
            }
            for (DetalleTransaccion detalle : detalleDiarioEliminar) {
                detalleTransaccionService.remove(detalle);
            }
        } else {
            if (moduloDeEnlaceSeleccionado != null && moduloDeEnlaceSeleccionado.getId() != null) {
                if (moduloDeEnlaceSeleccionado.getCodigo().equals("modulo_nomina")) {
                    guardarReserva();
                }
            }
            /*Si hay reserva de compromiso entonces guardamos la reserva y el beneficiario en diario general*/
            if (reservaCompromiso != null) {
                if (reservaCompromiso.getId() != null) {
                    if (reservaCompromiso.getBeneficiario() != null) {
                        diarioGeneral.setBeneficiario(reservaCompromiso.getBeneficiario());
                        diarioGeneral.setTipoBeneficiario(reservaCompromiso.getTipoBeneficiario());
                        diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
                    } else {
                        if (adquisicion != null) {
                            if (adquisicion.getId() != null) {
                                if (adquisicion.getProveedor() != null) {
                                    diarioGeneral.setBeneficiario(adquisicion.getProveedor().getCliente());
                                    diarioGeneral.setTipoBeneficiario(Boolean.TRUE);
                                    diarioGeneral.setVariosBeneficiarios(Boolean.FALSE);
                                } else {
                                    if (reservaCompromiso.getNomina()) {
                                        diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
                                    }
                                }
                            } else {
                                if (reservaCompromiso.getNomina()) {
                                    diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
                                }
                            }
                        } else {
                            if (reservaCompromiso.getNomina()) {
                                diarioGeneral.setVariosBeneficiarios(Boolean.TRUE);
                            }
                        }
                    }
                    diarioGeneral.setCertificacionesPresupuestario(reservaCompromiso);
                }
            }
            /*Establecemos que tenga retencion SRI*/
            if ((diarioGeneral.getClase().getCodigo().equals("clase_egreso") || diarioGeneral.getClase().getCodigo().equals("clase_diario")) && diarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
                diarioGeneral.setRetencion(Boolean.TRUE);
                diarioGeneral.setRetenido(Boolean.FALSE);
            }
            colocarNumActa();
            diarioGeneral.setUsuarioCreacion(userSession.getNameUser());
            diarioGeneral.setFechaCreacion(new Date());
            diarioGeneral.setEstado(Boolean.TRUE);
            diarioGeneral.setObservacion(diarioGeneral.getObservacion().toUpperCase() + ", TRÁMITE: " + tramite.getNumTramite() + " - " + opcionBusqueda.getAnio());
            diarioGeneral.setEstadoDiario("REGISTRADO");
            diarioGeneral.setEnlace(moduloDeEnlaceSeleccionado);
            diarioGeneral.setNumTramite(tramite.getNumTramite());
            diarioGeneral = diarioGeneralService.create(diarioGeneral);
            for (DetalleTransaccion transaccion : detalleDiarioGeneral) {
                transaccion.setDiarioGeneral(diarioGeneral);
                detalleTransaccionService.create(transaccion);
            }
            /*Contabilizar los enlaces*/
            if (moduloDeEnlaceSeleccionado.getId() != null) {
                switch (moduloDeEnlaceSeleccionado.getCodigo()) {
                    case "modulo_certificaciones_presupuestarias":
                        contabilizarReservaCompromiso();
                        break;
                    case "modulo_nomina":
                        contabilizarModuloNomina();
                        break;
                    case "modulo_tesoreria":
                        contabilizarModuloTesoreria();
                        break;
                }
            }
            imprimirDiarioGeneral();
        }
    }

    public boolean renderedProcess() {
        if (proceso == 1) {
            return false;
        }
        return true;
    }

    public void imprimirDiarioGeneral() {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
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
        detalleDiarioEliminar.add(detalleDG);
        detalleDiarioGeneral.remove(index);
        int contador = 0;
        for (DetalleTransaccion detalle : detalleDiarioGeneral) {
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalle.setContador(bigInteger);
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("formMain");
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
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
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

    public void actualizarTipoDiarioGeneral() {
        if (diarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(diarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void openDlgCuentasContables() {
        reiniciarCuentaContable();
        this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
        this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
        this.cuentaContableLazyModel.getFilterss().put("estado", true);
        this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
        this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
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
                            if (!detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2).equals("97")) {
                                this.cuentaContableLazyModel.getFilterss().put("titulo", 2);
                                this.cuentaContableLazyModel.getFilterss().put("grupo", 1);
                                this.cuentaContableLazyModel.getFilterss().put("subGrupo", 3);
                                this.cuentaContableLazyModel.getFilterss().put("cuentaNivel1", detalleTransaccion.getPartidaPresupuestaria().getCodigo().substring(0, 2));
                            }
                        }
                    }
                }
            }
        }
        PrimeFaces.current().executeScript("PF('cuentasContablesDlg').show()");
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
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formMain");
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

    public void abriDlogo() {
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
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "procesp_fc_liquidacion":
                case "proceso_jgvis_servicios":
                case "CIERRE_CAJA":
                    getParamts().put("validadorContable", clienteService.getrolsUser(RolUsuario.validadorContable));
                    break;
                case "PAG_ANTI_LIQUI_HABER":
                case "PAG_DEC":
                case "procesos_rf_caja_chica":
                    getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
                    break;
                case "PPRM":
                    if (!rechazoRol) {
                        getParamts().put("aprobado", 1);
                        getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
                    } else {
                        getParamts().put("aprobado", 0);
                        getParamts().put("usuarioTalentoHumano", clienteService.getrolsUser(RolUsuario.titularTH));
                    }
                    break;
                default:
                    getParamts().put("usuarioRen", clienteService.getrolsUser(RolUsuario.retencion));
                    break;
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
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                }
            }
        }
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().ajax().update("diarioGeneralTable");

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
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "rol_general");
                    } else {
                        this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "rol_adicional");
                    }
                    break;
                case "rol_beneficios":
                    this.renderedRolesBeneficios = Boolean.TRUE;
                    this.renderedRoles = Boolean.FALSE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesBeneficiosList = diarioGeneralService.getTiposRolesBeneficios(opcionBusqueda.getAnio(), "aprobado-rol");
                    break;
                case "rol_fondos_reserva":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.TRUE;
                    this.botonBeneficiarioFondosReserva = Boolean.TRUE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnio(), "aprobado-rol", 4);
                    break;
                case "rol_fondos_reserva_no_acumulado":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.TRUE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnio(), "aprobado-rol", 5);
                    break;
                case "rol_extras_suplementarias":
                    this.renderedListRol = Boolean.TRUE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesAprobados(opcionBusqueda.getAnio(), "aprobado-rol", "ROL-HORAS-EXTRAS");
                    break;
                case "provisiones_decimo_tercero":
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedListRol = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnio(), "registrado-rol", 2);
                    break;
                case "provisiones_decimo_cuarto":
                    this.renderedListRol = Boolean.FALSE;
                    this.renderedRolesBeneficios = Boolean.FALSE;
                    this.renderedRoles = Boolean.TRUE;
                    this.fondoReserva = Boolean.FALSE;
                    this.botonBeneficiarioFondosReserva = Boolean.FALSE;
                    this.rolesList = tipoRolService.getTipoRolesFondosReserva(opcionBusqueda.getAnio(), "registrado-rol", 3);
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
                    if (subEnlaceSeleccionado.getCodigo().equals("rol_fondos_reserva")) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS FONDO DE RESERVAS ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-FONDOS-RESERVA", true);
                    } else {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS FONDO DE RESERVAS NO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-FONDOS-RESERVA", false);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioFondoReserva(tipoRolSeleccionado);
                    }
                } else {
                    this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                    this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnio());
                    this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioRolGeneral(tipoRolSeleccionado, opcionBusqueda.getAnio());
                }
            }
            reservaCompromiso.setFechaSolicitud(diarioGeneral.getFechaElaboracion());
            if (cuentaContablePresupuestoModelList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO!!!", "No hay datos para cargar");
            } else {
                openDlgRegistrarReservaCompromiso();
            }
        }
    }

    public void rolBeneficioSociales() {
        this.reservaCompromiso = new SolicitudReservaCompromiso();
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

    public void rolBeneficiosMensuales() {
        if (subEnlaceSeleccionado != null) {
            switch (subEnlaceSeleccionado.getCodigo()) {
                case "provisiones_decimo_tercero":
                    if (tipoRolSeleccionado != null) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS DECIMOS TERCER SUELDO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-DECIMO-3ro", true);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioProvisionales(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-DECIMO-3ro");
                    }
                    break;
                case "provisiones_decimo_cuarto":
                    if (tipoRolSeleccionado != null) {
                        this.reservaCompromiso.setDescripcion("RESERVA DE COMPROMISO DE LOS DECIMOS CUARTO SUELDO ACUMULADOS DEL " + tipoRolSeleccionado.getDescripcion().toUpperCase());
                        this.cuentaContablePresupuestoModelList = diarioGeneralService.getCuentaContablePresupuestoFondosReservaAcumulados(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-DECIMO-4to", true);
                        this.beneficiarioReservaCompromisoList = beneficiarioSolicitudReservaService.getBeneficiarioProvisionales(tipoRolSeleccionado, opcionBusqueda.getAnio(), "ACU-DECIMO-4to");
                    }
                    break;
            }
            openDlgRegistrarReservaCompromiso();
        }
    }

    private void openDlgRegistrarReservaCompromiso() {
        this.totalLiquidacion = 0;
        reservaCompromiso.setPeriodo(opcionBusqueda.getAnio());
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
        this.detalleReservaList = new ArrayList<>();
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
            detalleReservaList.add(detalleSolicitudReserva);
        }
        /*Asignamos los beneficiarios a la reserva de compromiso*/
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
        PrimeFaces.current().executeScript("PF('moduloNominaReservaCompromisoDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
        PrimeFaces.current().ajax().update("formoduloReservaCompromisoDlg");
        PrimeFaces.current().ajax().update("formMain");
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
        this.moduloDeEnlaceSeleccionado = new CatalogoItem();
        this.totalLiquidacion = 0;
        this.rolesBeneficiosList = new ArrayList<>();
        this.rolesList = new ArrayList<>();
        this.beneficiarioReservaCompromisoList = new ArrayList<>();
        this.proveedorSeleccionado = new Proveedor();
    }

    public void rechazarAprobacionRol() {
        rechazoRol = true;
        abriDlogo();
    }
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
        this.adquisicionesList = new ArrayList<>();
        this.booleanAdquisiciones = Boolean.FALSE;
        this.detalleDiarioGeneral = new ArrayList<>();
        this.cuentaContablePresupuestoModelList = diarioGeneralService.getDetalleReservaCompromiso(reservaCompromiso, opcionBusqueda.getAnio());
        this.adquisicionesList = diarioGeneralService.getAdquisicionList(reservaCompromiso);
        if (adquisicion != null || adquisicion.getId() != null) {
            booleanAdquisiciones = Boolean.FALSE;
        } else {
            if (!adquisicionesList.isEmpty()) {
                if (adquisicionesList.size() > 1) {
                    booleanAdquisiciones = Boolean.TRUE;
                } else {
                    this.adquisicion = adquisicionesList.get(0);
                    booleanAdquisiciones = Boolean.FALSE;
                }
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "Revisar las relaciones de la cuenta contable con la partida presupuestaria"));
        }
        diarioGeneral.setClase(diarioGeneralService.getClaseTipo("clase_egreso"));
        diarioGeneral.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneral.setObservacion(reservaCompromiso.getDescripcion().toUpperCase());
        diarioGeneral.setObservacion("MÓDULO DE RESERVA DE COMPROMISO: P.R. " + diarioGeneral.getObservacion().toUpperCase());
        calcularTotalesDetalleDiarioGeneral();
        PrimeFaces.current().executeScript("PF('detalleReservaCompromisoDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("formoduloReservaCompromisoDlg");
        PrimeFaces.current().ajax().update("mensaje");
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
    }

    public void procesoTesoreriaSeleccionado() {
        if (subEnlaceSeleccionado != null) {
            this.retencionesSeleccionadas = new ArrayList<>();
            this.garantiasList = new ArrayList<>();
            this.garantiasSeleccionadasList = new ArrayList<>();
            this.corteOrdenCobroLazyModel = new LazyModel<>(CorteOrdenCobro.class);
            this.corteOrdenCobroLazyModel.getSorteds().put("codigo", "ASC");
            this.corteOrdenCobroLazyModel.getFilterss().put("estado", true);
            this.corteOrdenCobroLazyModel.getFilterss().put("numTramite", this.tramite.getNumTramite());
            this.corteOrdenCobroLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
            this.recaudacionlLazyModel = new LazyModel<>(Recaudacion.class);
            this.recaudacionlLazyModel.getSorteds().put("codigo", "ASC");
            this.recaudacionlLazyModel.getFilterss().put("estado", true);
            this.recaudacionlLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
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
                    this.recaudacionlLazyModel.getFilterss().put("numTramite", this.tramite.getNumTramite());
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar", 0);
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "COBROEM");
                    break;
                case "recaudaciones_ajustes":
                    inicializarBooleanTesoreria();
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.recaudacionlLazyModel.getFilterss().put("ajuste.numTramite", this.tramite.getNumTramite());
                    this.recaudacionlLazyModel.getFilterss().put("contabilizado", false);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "recaudaciones_cobros_ajustes":
                    inicializarBooleanTesoreria();
                    this.tableRecaudaciones = Boolean.TRUE;
                    this.recaudacionlLazyModel.getFilterss().put("ajuste.numTramite", this.tramite.getNumTramite());
                    this.recaudacionlLazyModel.getFilterss().put("saldoAfectar:gt", 0);
                    this.recaudacionlLazyModel.getFilterss().put("tipoRecaudacion.codigo", "AJUSTEX");
                    break;
                case "retenciones":
                    inicializarBooleanTesoreria();
                    this.periodosFiscales = diarioGeneralService.getPeriodosFiscales(opcionBusqueda.getAnio());
                    this.tipoRetenciones = diarioGeneralService.getTiposRetenciones("RETENCION");
                    break;
                case "garantias":
                    this.tableCorteOrdenCobro = Boolean.FALSE;
                    this.tableRecaudaciones = Boolean.FALSE;
                    this.adquisicionesLazyModel = new LazyModel<>(Adquisiciones.class);
                    this.adquisicionesLazyModel.getSorteds().put("id", "ASC");
                    this.adquisicionesLazyModel.getFilterss().put("estado", true);
                    this.adquisicionesLazyModel.getFilterss().put("garantia", true);
                    break;
                default:
                    JsfUtil.addWarningMessage("AVISO!!!", "No se puede contabilizar los arriendos en este proceso");
                    return;
            }
        } else {
            inicializarBooleanTesoreria();
        }
    }

    public void actualizarTablaRetenciones() {
        if (tipoRetencionSeleccionado != null && periodoSeleccionado != null) {
            this.liquidacionDetalleList = diarioGeneralService.getListDetalleRetencion(opcionBusqueda.getAnio(), tipoRetencionSeleccionado, periodoSeleccionado, "RECIBIDA;AUTORIZADO");
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
                List<CuentaContablePresupuestoModel> cuentaPresupuesto = null;//diarioGeneralService.getListPartidasSaldos(retencion.getCuentaContableRetencion().getCuentaContable(), retencion.getLiquidacion().getDiarioGeneral());
                if (cuentaPresupuesto != null) {
                    if (cuentaPresupuesto.size() > 1) {
                        cicloDetalleDiarioGeneral(cuentaPresupuesto, retencion.getValor().doubleValue(), retencion);
                    } else {
                        for (CuentaContablePresupuestoModel cuenta : cuentaPresupuesto) {
                            DetalleTransaccion detalleDG = new DetalleTransaccion();
                            bigInteger = BigInteger.valueOf(detalleDiarioGeneral.size() + 1);
                            detalleDG.setContador(bigInteger);
//                            detalleDG.setCuentaContable(retencion.getCuentaContableRetencion().getContContable());
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
                    detalleDG.setEstructuraProgramatica(cobrosCaja.getItempresupuestario().getEstructura());
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
                try {
                    guardarDetalleTransaccion(recaudacionSelecionada.getAgenciaDestino().getCuentaMovimiento(), bigDecimal, false);
                    guardarDetalleTransaccion(diarioGeneralService.getCuentaContableTesoreria(recaudacionSelecionada), bigDecimal, true);
                } catch (Exception e) {
                    JsfUtil.addWarningMessage("AVISO!!!", "No hay detalle en las recaudaciones - cobros");
                    return;
                }
                break;
            case "recaudaciones_ajustes":
                try {
                guardarDetalleTransaccion(recaudacionSelecionada.getAgenciaDestino().getCuentaMovimiento(), recaudacionSelecionada.getValor(), false);
                guardarDetalleTransaccion(recaudacionSelecionada.getCuentaContable(), recaudacionSelecionada.getValor(), true);
            } catch (Exception e) {
                JsfUtil.addWarningMessage("AVISO!!!", "No hay detalle en las recaudaciones - ajustes");
                return;
            }
            break;
            case "recaudaciones_cobros_ajustes":
                try {
                bigDecimal = diarioGeneralService.getSaldoCobroAjuste(recaudacionSelecionada, false);
                guardarDetalleTransaccion(recaudacionSelecionada.getCuentaContable(), bigDecimal, false);
                guardarDetalleTransaccion(diarioGeneralService.getCuentaContableTesoreria(recaudacionSelecionada), bigDecimal, true);
            } catch (Exception e) {
                JsfUtil.addWarningMessage("AVISO!!!", "No hay detalle en las recaudaciones - cobros x ajustes");
                return;
            }
            break;
        }
        calcularTotalesDetalleDiarioGeneral();
        diarioGeneral.setObservacion(diarioGeneral.getObservacion() + bigDecimal);
        this.botonCuentasContables = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('moduloTesoreriaDlg').hide()");
        PrimeFaces.current().ajax().update("detalleDiarioGeneralEdit");
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
        this.tableCorteOrdenCobro = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
        this.procesoCobroEmisor = Boolean.FALSE;
        this.tableRecaudaciones = Boolean.FALSE;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get -- Set">
    public Boolean getRenderedListRol() {
        return renderedListRol;
    }

    public void setRenderedListRol(Boolean renderedListRol) {
        this.renderedListRol = renderedListRol;
    }

    public Boolean getBtnNuevoRegistro() {
        return btnNuevoRegistro;
    }

    public void setBtnNuevoRegistro(Boolean btnNuevoRegistro) {
        this.btnNuevoRegistro = btnNuevoRegistro;
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

    public Boolean getAccionesEditar() {
        return accionesEditar;
    }

    public void setAccionesEditar(Boolean accionesEditar) {
        this.accionesEditar = accionesEditar;
    }

    public Boolean getViewDiarioGeneral() {
        return viewDiarioGeneral;
    }

    public void setViewDiarioGeneral(Boolean viewDiarioGeneral) {
        this.viewDiarioGeneral = viewDiarioGeneral;
    }

    public List<CatalogoItem> getTiposAdquisiciones() {
        return tiposAdquisiciones;
    }

    public void setTiposAdquisiciones(List<CatalogoItem> tiposAdquisiciones) {
        this.tiposAdquisiciones = tiposAdquisiciones;
    }

    public CatalogoItem getSubEnlaceSeleccionado() {
        return subEnlaceSeleccionado;
    }

    public void setSubEnlaceSeleccionado(CatalogoItem subEnlaceSeleccionado) {
        this.subEnlaceSeleccionado = subEnlaceSeleccionado;
    }

    public CatalogoItem getModuloDeEnlaceSeleccionado() {
        return moduloDeEnlaceSeleccionado;
    }

    public void setModuloDeEnlaceSeleccionado(CatalogoItem moduloDeEnlaceSeleccionado) {
        this.moduloDeEnlaceSeleccionado = moduloDeEnlaceSeleccionado;
    }

    public CuentaContable getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(CuentaContable cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public DetalleTransaccion getDetalleTransaccion() {
        return detalleTransaccion;
    }

    public void setDetalleTransaccion(DetalleTransaccion detalleTransaccion) {
        this.detalleTransaccion = detalleTransaccion;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public void setPresupuestoSeleccionado(Presupuesto presupuestoSeleccionado) {
        this.presupuestoSeleccionado = presupuestoSeleccionado;
    }

    public CatalogoItem getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(CatalogoItem tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
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

    public Proveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public double getTotalLiquidacion() {
        return totalLiquidacion;
    }

    public void setTotalLiquidacion(double totalLiquidacion) {
        this.totalLiquidacion = totalLiquidacion;
    }

    public LazyModel<DiarioGeneral> getDiarioGeneralLazyModel() {
        return diarioGeneralLazyModel;
    }

    public void setDiarioGeneralLazyModel(LazyModel<DiarioGeneral> diarioGeneralLazyModel) {
        this.diarioGeneralLazyModel = diarioGeneralLazyModel;
    }

    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
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

    public List<CatalogoItem> getSubEnlace() {
        return subEnlace;
    }

    public void setSubEnlace(List<CatalogoItem> subEnlace) {
        this.subEnlace = subEnlace;
    }

    public List<CatalogoPresupuesto> getPartidaPresupuestariaRelacionadas() {
        return partidaPresupuestariaRelacionadas;
    }

    public void setPartidaPresupuestariaRelacionadas(List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas) {
        this.partidaPresupuestariaRelacionadas = partidaPresupuestariaRelacionadas;
    }

    public List<DetalleTransaccion> getDetalleDiarioGeneral() {
        return detalleDiarioGeneral;
    }

    public void setDetalleDiarioGeneral(List<DetalleTransaccion> detalleDiarioGeneral) {
        this.detalleDiarioGeneral = detalleDiarioGeneral;
    }

    public List<CatalogoPresupuesto> getFiltroPartidas() {
        return filtroPartidas;
    }

    public void setFiltroPartidas(List<CatalogoPresupuesto> filtroPartidas) {
        this.filtroPartidas = filtroPartidas;
    }

    public List<SolicitudReservaCompromiso> getReservaCompromisoList() {
        return reservaCompromisoList;
    }

    public void setReservaCompromisoList(List<SolicitudReservaCompromiso> reservaCompromisoList) {
        this.reservaCompromisoList = reservaCompromisoList;
    }

    public List<Presupuesto> getPresupuestoRelacionado() {
        return presupuestoRelacionado;
    }

    public void setPresupuestoRelacionado(List<Presupuesto> presupuestoRelacionado) {
        this.presupuestoRelacionado = presupuestoRelacionado;
    }

    public List<CuentaContable> getRelacionCuentasContables() {
        return relacionCuentasContables;
    }

    public void setRelacionCuentasContables(List<CuentaContable> relacionCuentasContables) {
        this.relacionCuentasContables = relacionCuentasContables;
    }

    public List<CuentaContablePresupuestoModel> getCuentasConPartida() {
        return cuentasConPartida;
    }

    public void setCuentasConPartida(List<CuentaContablePresupuestoModel> cuentasConPartida) {
        this.cuentasConPartida = cuentasConPartida;
    }

    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoModelList() {
        return cuentaContablePresupuestoModelList;
    }

    public void setCuentaContablePresupuestoModelList(List<CuentaContablePresupuestoModel> cuentaContablePresupuestoModelList) {
        this.cuentaContablePresupuestoModelList = cuentaContablePresupuestoModelList;
    }

    public List<TipoRol> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<TipoRol> rolesList) {
        this.rolesList = rolesList;
    }

    public List<TipoRolBeneficios> getRolesBeneficiosList() {
        return rolesBeneficiosList;
    }

    public void setRolesBeneficiosList(List<TipoRolBeneficios> rolesBeneficiosList) {
        this.rolesBeneficiosList = rolesBeneficiosList;
    }

    public List<BeneficiarioSolicitudReserva> getBeneficiarioReservaCompromisoList() {
        return beneficiarioReservaCompromisoList;
    }

    public void setBeneficiarioReservaCompromisoList(List<BeneficiarioSolicitudReserva> beneficiarioReservaCompromisoList) {
        this.beneficiarioReservaCompromisoList = beneficiarioReservaCompromisoList;
    }

    public Boolean getRenderedRolesBeneficios() {
        return renderedRolesBeneficios;
    }

    public void setRenderedRolesBeneficios(Boolean renderedRolesBeneficios) {
        this.renderedRolesBeneficios = renderedRolesBeneficios;
    }

    public Boolean getRenderedRoles() {
        return renderedRoles;
    }

    public void setRenderedRoles(Boolean renderedRoles) {
        this.renderedRoles = renderedRoles;
    }

    public Boolean getFondoReserva() {
        return fondoReserva;
    }

    public void setFondoReserva(Boolean fondoReserva) {
        this.fondoReserva = fondoReserva;
    }

    public Boolean getBotonBeneficiarioFondosReserva() {
        return botonBeneficiarioFondosReserva;
    }

    public void setBotonBeneficiarioFondosReserva(Boolean botonBeneficiarioFondosReserva) {
        this.botonBeneficiarioFondosReserva = botonBeneficiarioFondosReserva;
    }

    public List<LiquidacionDetalle> getRetencionesSeleccionadas() {
        return retencionesSeleccionadas;
    }

    public void setRetencionesSeleccionadas(List<LiquidacionDetalle> retencionesSeleccionadas) {
        this.retencionesSeleccionadas = retencionesSeleccionadas;
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

    public List<Garantias> getGarantiasList() {
        return garantiasList;
    }

    public void setGarantiasList(List<Garantias> garantiasList) {
        this.garantiasList = garantiasList;
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

    public List<LiquidacionDetalle> getLiquidacionDetalleList() {
        return liquidacionDetalleList;
    }

    public void setLiquidacionDetalleList(List<LiquidacionDetalle> liquidacionDetalleList) {
        this.liquidacionDetalleList = liquidacionDetalleList;
    }

    public Boolean getBotonCuentasContables() {
        return botonCuentasContables;
    }

    public void setBotonCuentasContables(Boolean botonCuentasContables) {
        this.botonCuentasContables = botonCuentasContables;
    }

    public String getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(String tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    public LazyModel<CorteOrdenCobro> getCorteOrdenCobroLazyModel() {
        return corteOrdenCobroLazyModel;
    }

    public void setCorteOrdenCobroLazyModel(LazyModel<CorteOrdenCobro> corteOrdenCobroLazyModel) {
        this.corteOrdenCobroLazyModel = corteOrdenCobroLazyModel;
    }

    public LazyModel<Recaudacion> getRecaudacionlLazyModel() {
        return recaudacionlLazyModel;
    }

    public void setRecaudacionlLazyModel(LazyModel<Recaudacion> recaudacionlLazyModel) {
        this.recaudacionlLazyModel = recaudacionlLazyModel;
    }

    public Boolean getTableCorteOrdenCobro() {
        return tableCorteOrdenCobro;
    }

    public void setTableCorteOrdenCobro(Boolean tableCorteOrdenCobro) {
        this.tableCorteOrdenCobro = tableCorteOrdenCobro;
    }

    public Boolean getProcesoCobroEmisor() {
        return procesoCobroEmisor;
    }

    public void setProcesoCobroEmisor(Boolean procesoCobroEmisor) {
        this.procesoCobroEmisor = procesoCobroEmisor;
    }

    public Boolean getTableRecaudaciones() {
        return tableRecaudaciones;
    }

    public void setTableRecaudaciones(Boolean tableRecaudaciones) {
        this.tableRecaudaciones = tableRecaudaciones;
    }

    public Boolean getTableRetenciones() {
        return tableRetenciones;
    }

    public void setTableRetenciones(Boolean tableRetenciones) {
        this.tableRetenciones = tableRetenciones;
    }

    public Boolean getValidarRechazoRol() {
        return validarRechazoRol;
    }

    public void setValidarRechazoRol(Boolean validarRechazoRol) {
        this.validarRechazoRol = validarRechazoRol;
    }
//</editor-fold>
}
