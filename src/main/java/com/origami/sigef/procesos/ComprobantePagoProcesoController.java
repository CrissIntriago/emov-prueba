/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.service.BeneficiarioComprobantePagoService;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.DetalleComprobantePagoService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.procesos.Model.RubroPlanillaIESS;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.RolRubroService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

/**
 *
 * @author OrigamiEC
 */
@Named(value = "comprobantePagoProcesoView")
@ViewScoped
public class ComprobantePagoProcesoController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(ComprobantePagoProcesoController.class.getName());

    /*Inject*/
    @Inject
    private UserSession userSession;
    @Inject
    private ComprobantePagoService comprobantePagoService;
    @Inject
    private DetalleComprobantePagoService detalleComprobantePagoService;
    @Inject
    private DetalleTransaccionService detalleTransaccionService;
    @Inject
    private BeneficiarioComprobantePagoService beneficiarioComprobantePagoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private BeneficiarioSolicitudReservaService beneficiarioSolicitudReservaService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private AnticipoRemuneracionService anticipoRMUService;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private RolRubroService rolRubroService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private ParametrizableService parametroService;

    /*Objecto*/
    private ComprobantePago comprobantePago;
    private OpcionBusqueda opcionBusqueda;
    private DiarioGeneral diarioGeneral;
    private DiarioGeneral diarioGeneralAnulacion;
    private BeneficiarioComprobantePago beneficiarioComprobantePago;
    private CuentaBancaria cuentaBancariaSelecionada;
    private CatalogoItem enlaceSeleccionado;
    private CatalogoItem autorizado;
    private AnticipoRemuneracion anticipoSeleccionado;
    private Proveedor proveedor;
    private Servidor servidor;
    private DetalleComprobantePago detalleComprobantePago;
    private Presupuesto presupuestoSeleccionado;
    private String mesSeleccionado;
    private ParametrosTalentoHumano tipoPlanillaSeleccionada;

    /*LazyModel*/
    private LazyModel<ComprobantePago> comprobantePagoLazy;
    private LazyModel<CuentaBancaria> cuentaBancariaLazyModel;

    /*Listas*/
    private List<DetalleTransaccion> detalleTransaccionList;
    private List<DetalleComprobantePago> detalleComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList;
    private List<BeneficiarioComprobantePago> beneficiariosAnuladosSeleccionados;
    private List<CatalogoItem> enlacesComprobantePago;
    private List<DetalleTransaccion> partidaEstructura;
    private List<CatalogoPresupuesto> partidaPresupuestariaRelacionadas;
    private List<Presupuesto> presupuestoRelacionado;
    private List<CuentaContable> listaCuentaContables;
    private List<TipoRol> listaRol;
    private List<TipoRol> rolesSeleccionados;
    private List<RubroPlanillaIESS> rolRubroList;
    private List<Long> rubros;
    private List<ParametrosTalentoHumano> planillaIESSList;
    private List<CatalogoItem> mesList;

    /*Variables*/
    private int proceso;
    private Boolean ocultarTabla;
    private Boolean ocultarBotonesAcciones;
    private Boolean botonCuentaBanco;
    private Boolean fielsetDiarioGeneral;
    private Boolean fielsetEnlaces;
    private Boolean botonAcciones;
    private Boolean talentoHumano;
    private Boolean financiero;
    private Boolean botonAgregarCuentaContable;
    private Boolean fieldsetReservaContrato;
    private String tipoProcesoSeleccionado;
    private double totalDebe;
    private double totalHaber;
    private double totalValor;
    private double totalValorBeneficiarios;
    private boolean btnCompletarTarea;
    private boolean btniniciar;
    private BigInteger numTramite;
    private int codigoComprobantePago;
    private boolean eleccion;
    private CatalogoItem subEnlaceSeleccionado;
    private boolean bloquearItems;

    /**
     *
     */
    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        numTramite = null;
        diarioGeneral = new DiarioGeneral();
        anticipoSeleccionado = new AnticipoRemuneracion();
        anticipoSeleccionado.setComprobantePago(new ComprobantePago());
        planillaIESSList = parametroService.valoreRolesXplanillaIESS();
        mesList = catalogoItemService.findCatalogoItems("meses_anio");
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                eleccion = false;
                bloquearItems = false;
                numTramite = new BigInteger("" + tramite.getNumTramite());
                observacion = new Observaciones();
                enlaceSeleccionado = new CatalogoItem();
                this.comprobantePagoLazy = new LazyModel<>(ComprobantePago.class);
                this.comprobantePagoLazy.getSorteds().put("id", "ASC");
                this.comprobantePagoLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
                this.comprobantePagoLazy.getFilterss().put("numeroTramite", new BigInteger("" + tramite.getNumTramite()));
                this.cuentaBancariaLazyModel = new LazyModel<>(CuentaBancaria.class);
                this.cuentaBancariaLazyModel.getSorteds().put("id", "ASC");
                this.cuentaBancariaLazyModel.getFilterss().put("estado", true);
                this.cuentaBancariaLazyModel.getFilterss().put("tipoCuenta", true);
                this.enlacesComprobantePago = catalogoService.getItemsByCatalogo("enlace_comprobante_pago");
                verificadores();
            }
        }
        vaciarFormulario("REINICIAR");
    }

    public void inhabilitarItems() {
        bloquearItems = true;
        if (subEnlaceSeleccionado != null) {
            if ("CAFP".equals(subEnlaceSeleccionado.getCodigo())) {
                codigoComprobantePago = 1;
            } else {
                codigoComprobantePago = 2;
            }

        }

    }

    private void determinarBeneficiario(Boolean determinarTipoBeneficiario) {
        try {
            servidor = new Servidor();
            proveedor = new Proveedor();
            if (determinarTipoBeneficiario) {
                proveedor = (Proveedor) getTramiteService().getEntityManager().find(Class.forName(tramite.getNameReferencia()), tramite.getIdReferencia().longValue());
                añadirBeneficiario(null, proveedor);
            } else {
                servidor = (Servidor) getTramiteService().getEntityManager().find(Class.forName(tramite.getNameReferencia()), tramite.getIdReferencia().longValue());
                añadirBeneficiario(servidor, null);
            }
        } catch (ClassNotFoundException classNotFoundException) {
        }
    }

    private void añadirBeneficiario(Servidor servidor, Proveedor proveedor) {
        beneficiarioComprobantePagoList = new ArrayList<>();
        BeneficiarioComprobantePago beneficiarioComprobante = new BeneficiarioComprobantePago();
        DetalleBanco detalleBanco = null;
        if (servidor != null) {
            beneficiarioComprobante.setBeneficiario(servidor.getPersona());
            beneficiarioComprobante.setTipoBeneficiario(Boolean.FALSE);
        } else {
            beneficiarioComprobante.setBeneficiario(proveedor.getCliente());
            beneficiarioComprobante.setTipoBeneficiario(Boolean.TRUE);
        }
        if (getTramite().getTipoTramite().getAbreviatura().equals("RET_JUD")) {
            detalleBanco = beneficiarioComprobantePagoService.getCuentaBancariaRJ(beneficiarioComprobante.getBeneficiario(), beneficiarioComprobante.getTipoBeneficiario());
        } else {
            detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioComprobante.getBeneficiario(), beneficiarioComprobante.getTipoBeneficiario());
        }
        if (detalleBanco != null) {
            beneficiarioComprobante.setDetalleBanco(detalleBanco);
        }
        beneficiarioComprobante.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
        beneficiarioComprobante.setValor(BigDecimal.ZERO);
        beneficiarioComprobantePagoList.add(beneficiarioComprobante);
    }

    public void verificador(BigInteger num) {
        List<ComprobantePago> lista = comprobantePagoService.getVerificadorComprobantePago(num, "");
        if (lista.isEmpty()) {
            btnCompletarTarea = false;
        } else {
            List<ComprobantePago> lista2 = comprobantePagoService.getVerificadorComprobantePago(num, "REGISTRADO");
            if (lista2.isEmpty()) {
                btnCompletarTarea = false;
            } else {
                btnCompletarTarea = true;
            }
        }
    }

    public void verificadorInicializadorCP(BigInteger num) {
        List<ComprobantePago> lista = comprobantePagoService.getVerificadorComprobantePago(num, "REGISTRADO");
        if (lista.isEmpty()) {
            btniniciar = false;
        } else {
            btniniciar = true;
        }
    }

    public void eliminarList(int var) {
        detalleComprobantePagoList.remove(var);
    }

    public void form(ComprobantePago comprobante, String accion) {
        String codigo = "";
        this.fieldsetReservaContrato = Boolean.FALSE;
        this.ocultarTabla = Boolean.FALSE;
        if (comprobante != null) {
            this.comprobantePago = comprobante;
            this.diarioGeneral = comprobante.getDiarioGeneral();
            this.ocultarTabla = Boolean.FALSE;
            this.ocultarBotonesAcciones = Boolean.FALSE;
            this.detalleComprobantePagoList = detalleComprobantePagoService.getDetalleComprobantePago(comprobantePago);
            this.beneficiarioComprobantePagoList = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobantePago);
            actualizarTotales();
            totalBeneficiario();
            for (DetalleComprobantePago dComp : detalleComprobantePagoList) {
                dComp.setDatoCargado(true);
            }
            if (comprobantePago.getDiarioGeneral() != null) {
                proceso = 2;
                fielsetDiarioGeneral = Boolean.TRUE;
            } else {
                proceso = 1;
                fielsetDiarioGeneral = Boolean.FALSE;
            }
        } else {
            this.comprobantePago = new ComprobantePago();
            this.comprobantePago.setPeriodo(opcionBusqueda.getAnio());
            this.comprobantePago.setFechaComprobante(new Date());
            this.comprobantePago.setDetalle("PROCESO " + tramite.getTipoTramite().getDescripcion().toUpperCase() + ", TRÁMITE No." + tramite.getNumTramite() + " - " + opcionBusqueda.getAnio() + ",");
            if ("NUEVO".equals(accion)) {
                this.ocultarBotonesAcciones = Boolean.TRUE;
                switch (getTramite().getTipoTramite().getAbreviatura()) {
                    /*Cris Intriago*/
                    case "PAG_ANTI_VIATICOS":
                        proceso = 1; // muestra el nombre del proceso o enlace; para que muestre es el número "2"
                        codigoComprobantePago = 2; // no tiene afectacion Presupuestaria en caso de que si tenga se iguala a "1"
                        fielsetDiarioGeneral = Boolean.FALSE; // no se mostrara la informcion del diarioGeneral si este no pasa por Registro Contable
                        determinarBeneficiario(false); // BUSCA EL BENEFICIARIO DEL COMPROBANTE
                        codigo = "cp_pago_anticipo_viaticos";
                        break;
                    case "RET_JUD":
                        proceso = 1; // SOLO MUESTRA LA PARTE DE ENLACE
                        codigo = "cod_retenciones_judiciales";
                        codigoComprobantePago = 1; // PARA QUE MUESTRE LA ASICIACION PRESUPUESTARIA
                        fielsetDiarioGeneral = Boolean.FALSE;
                        determinarBeneficiario(true); // BUSCA EL BENEFICIARIO DEL COMPROBANTE
                        break;
                    case "PAGORMU":
                        proceso = 1;
                        anticipoSeleccionado = anticipoRMUService.findAnticipoByNTramite(numTramite);
                        autorizado = anticipoRMUService.getEstadoAnticipo("EST_ANTI", (short) 6);
                        codigo = "cod_anticipos_RM";
                        if (anticipoSeleccionado.getEstadoAnticipo().equals(autorizado)) {
                            cargarAnticipo();
                        }
                        break;
                    /*Existente*/
                    case "PAG_ANTI_BIENES":
                        proceso = 1;
                        codigoComprobantePago = 2;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "cp_anticipos_prov_bienes";
                        cargarAnticipoAProveedores();
                        break;
                    case "PAG_ANTI_SERVICIOS":
                        proceso = 1;
                        codigoComprobantePago = 2;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "cp_anticipos_prov_servicios";
                        cargarAnticipoAProveedores();
                        break;
                    case "PAG_ANTI_CONSULTORIA":
                        proceso = 1;
                        codigoComprobantePago = 2;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "cp_anticipos_prov_consultoria";
                        cargarAnticipoAProveedores();
                        break;
                    case "PAG_ANTI_OBRAPUBLICA":
                        proceso = 1;
                        codigoComprobantePago = 2;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "cp_anticipos_prov_obraspublicas";
                        cargarAnticipoAProveedores();
                        break;
                    case "proceso_af_caja_chica":
                        proceso = 1;
                        codigoComprobantePago = 2;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "op_caja_chica";
                        determinarBeneficiario(false);
                        actualizarFormulario(true);
                        break;
                    case "PPPI":
                        initPlanilla();
                        proceso = 1;
                        codigoComprobantePago = 1;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        determinarBeneficiario(true);
                        codigo = "pago_planillas_IESS";
                        break;
                    case "procesos_pagos_noClasficados":
                        diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());
                        if (diarioGeneral != null) {
                            proceso = 2;
                            codigoComprobantePago = 1;
                            fielsetDiarioGeneral = Boolean.TRUE;
                            DiarioGeneralSeleccionado("aniadir");
                            subEnlaceSeleccionado = catalogoItemService.getTipoIdentificacion("CAFP");
                        } else {
                            proceso = 1;
                            eleccion = true;
                            codigoComprobantePago = 2;
                            fielsetDiarioGeneral = Boolean.FALSE;
                            codigo = "enlace_financiero";
                            determinarBeneficiario(true);
                            subEnlaceSeleccionado = catalogoItemService.getTipoIdentificacion("SAFP");
                        }
                        break;
                    case "proceso_jgvis_servicios":
                    case "proceso_pi_cuantia_bienes":
                    case "proceso_pi_cuantia_servicios":
                    case "PAG_INF_CUANT_OB_MENOR":
                    case "PAG_SERV_BASIC":
                    case "PAG_DEC":
                    case "proceso_pc_consultoria":
                    case "proceso_pca_bienes":
                    case "proceso_pco_publica":
                    case "proceso_pc_servicios":
                    case "proceso_pce_servicios":
                    case "proceso_pago_ceb":
                    case "PPRM":
                    case "PRHEXSU":
                    case "procesos_rf_caja_chica":
                    case "PPS_profesionales":
                    case "PAG_SERV_NOTARIALES":
                        proceso = 2; // SOLO MUESTRA LA PARTE DE ENLACE
                        codigoComprobantePago = 1; // PARA QUE MUESTRE LA ASICIACION PRESUPUESTARIA
                        fielsetDiarioGeneral = Boolean.TRUE;
                        diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());
                        DiarioGeneralSeleccionado("aniadir");
                        break;
                    case "PAG_ANTI_LIQUI_HABER":
                        proceso = 2; // SOLO MUESTRA LA PARTE DE ENLACE
                        codigoComprobantePago = 1; // PARA QUE MUESTRE LA ASICIACION PRESUPUESTARIA
                        fielsetDiarioGeneral = Boolean.TRUE;
                        diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());
                        DiarioGeneralSeleccionado("aniadir");
                        determinarBeneficiario(false);
                        break;
                    default:
                        proceso = 1;
                        codigoComprobantePago = 1;
                        fielsetDiarioGeneral = Boolean.FALSE;
                        codigo = "cp_liquidacion_pagoTerceros";
                        determinarBeneficiario(true);
                        break;
                }
            } else {
                vaciarFormulario(accion);
            }
        }
        if (!codigo.equals("")) {
            enlaceSeleccionado = catalogoItemService.getEstadoRol(codigo);
        }
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    public void initPlanilla() {
        listaRol = tipoRolService.getTipoRolAprobado("aprobado-rol", "pagado-rol", opcionBusqueda.getAnio());
        rolRubroList = new ArrayList<>();
    }

    public void updatePlanilla() {
        rolRubroList = new ArrayList<>();
        detalleComprobantePagoList = new ArrayList<>();
        List<TipoRol> auxList = tipoRolService.getRolMes(mesSeleccionado, "pagado-rol");
        List<DetalleComprobantePago> array = new ArrayList<>();
        for (TipoRol item : auxList) {
            detalleComprobantePagoList = new ArrayList<>();
            comprobantePago.setDetalle(comprobantePago.getDetalle() + ", " + item.getDescripcion());
            List<CuentaContablePresupuestoModel> aux = new ArrayList<>();
            if (tipoPlanillaSeleccionada != null) {
                aux = rolRubroService.getPagoPlanilla(item, tipoPlanillaSeleccionada);
            } else {
                aux = rolRubroService.getPagoPlanilla(item, null);
            }
            List<DetalleTransaccion> aux_1 = detalleTransaccionService.getDetalleTransaccionDevengadosPlanilla(item.getDiarioGeneralRol());
            List<DetalleTransaccion> aux_2 = new ArrayList<>();
            for (CuentaContablePresupuestoModel cuenta : aux) {
                DetalleTransaccion detalleT = new DetalleTransaccion();
                detalleT.setCuentaContable(cuenta.getCuentaContable());
                detalleT.setHaber(cuenta.getMonto_1());
                aux_2.add(detalleT);
            }
            List<DetalleComprobantePago> temporal = asientoComprobante(aux_1, aux_2);
            if (!temporal.isEmpty()) {
                for (DetalleComprobantePago item_1 : temporal) {
                    array.add(item_1);
                }
            }
        }
        
        detalleComprobantePagoList = array;
        actualizarTotales();
        totalBeneficiario();
        PrimeFaces.current().ajax().update("formMain");
    }

    private List<DetalleComprobantePago> asientoComprobante(List<DetalleTransaccion> detallesTransacciones, List<DetalleTransaccion> detalleTransaccionList) {
        /*LISTAS AUXILIARES*/
        List<DetalleComprobantePago> detallesComprobantePago_1 = new ArrayList<>();
        List<DetalleComprobantePago> detallesComprobantePago_2 = new ArrayList<>();
        if (detalleTransaccionList != null && !detalleTransaccionList.isEmpty()) {
            /*GUARDAMOS SOLO LA CUENTA CONTABLE  EL VALOR AL DEBE EN detallesComprobantePago_1*/
            for (DetalleTransaccion detalle : detalleTransaccionList) {
                detalleComprobantePago = new DetalleComprobantePago();
                detalleComprobantePago.setCuentaContable(detalle.getCuentaContable());
                detalleComprobantePago.setDebe(detalle.getHaber());
                detallesComprobantePago_1.add(detalleComprobantePago);
            }
            /*GUARDAMOS LAS PARTIDAS PRESUPUESTARIAS Y LA ESTRUCTURA PROGRAMATICA EN detallesComprobantePago_2*/
            for (DetalleTransaccion detalle : detallesTransacciones) {
                if (detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado")) {
                    detalleComprobantePago = new DetalleComprobantePago();
                    detalleComprobantePago.setPartidaPresupuestaria(detalle.getPartidaPresupuestaria());
                    detalleComprobantePago.setEstructuraProgramatica(detalle.getEstructuraProgramatica());
                    detalleComprobantePago.setFuente(detalle.getFuente());
                    detalleComprobantePago.setCedulaPresupuestaria(detalle.getCedulaPresupuestaria());
                    detalleComprobantePago.setIdDetalleReserva(detalle.getIdDetalleReserva());
                    /*EL VALOR DEL COMPROMETIDO LO GUARDAMOS MOMENTANEMENTE EN LA VARIABLE DEL HABER*/
                    detalleComprobantePago.setDebe(detalle.getComprometido());
                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                    for (DetalleTransaccion detalleTransaccion : detallesTransacciones) {
                        if (detalle.getPartidaPresupuestaria().equals(detalleTransaccion.getPartidaPresupuestaria())
                                && detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado") && detalleTransaccion.getTipoTransaccion().getCodigo().equals("diario_general_ejecucion")) {
                            detalleComprobantePago.setHaber(detalleTransaccion.getEjecutado());
                        }
                    }
                    detallesComprobantePago_2.add(detalleComprobantePago);
                }
            }
            /*UNIR LAS 2 LISTAS*/
            if (detallesComprobantePago_2.size() == 1) {
                for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                    DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                    int contador = detalleComprobantePagoList.size() + 1;
                    BigInteger bigInteger = BigInteger.valueOf(contador);
                    detalleComprobantePago.setLinea(bigInteger);
                    detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                    detalleComprobantePago.setDebe(detalle1.getDebe());
                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                    for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                        detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                        detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                        detalleComprobantePago.setFuente(detalle2.getFuente());
                        detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                        detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                    }
                    detalleComprobantePago.setEjecutado(detalleComprobantePago.getDebe());
                    detalleComprobantePagoList.add(detalleComprobantePago);
                }
            } else {
                for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                    double diferencia = detalle1.getDebe().doubleValue();
                    for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                        if (detalle1.getCuentaContable().getCodigo().substring(3, 5).equals(detalle2.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                            /*VERIFICAMOS SI LA ASOCIACION CUENTA CONTABLE - CATALOGO PRESUPUESTO ESTA CORRECTA*/
                            Boolean relacionContable = comprobantePagoService.getRelacionCuentaContable(detalle1.getCuentaContable(), detalle2.getPartidaPresupuestaria());
                            if (relacionContable) {
                                /*Condicion para solo registrar una vez la cuenta contable*/
                                Boolean registrado = true;
                                Boolean bandera = false;
                                for (DetalleComprobantePago detComprobante : detalleComprobantePagoList) {
                                    if (detalle1.getCuentaContable().equals(detComprobante.getCuentaContable())) {
                                        registrado = false;
                                    }
                                }
                                /*CREAMOS UN NUEVO OBJETO PARA AÑADIRLO EN LA LISTA A PRESENTAR Y GUARDAR*/
                                DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                                int contador = detalleComprobantePagoList.size() + 1;
                                BigInteger bigInteger = BigInteger.valueOf(contador);
                                detalleComprobantePago.setLinea(bigInteger);
                                if (registrado) {
                                    detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                                    detalleComprobantePago.setDebe(detalle1.getDebe());
                                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                                }
                                /*DEBE = COMPROMETIDO Y HABER = EJECUTADO  y EJECUTADO = SALDO*/
                                detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                if (diferencia >= detalle2.getDebe().doubleValue()) {
                                    detalle2.setHaber(detalle2.getDebe().add(detalle2.getHaber()));
                                    detalleComprobantePago.setEjecutado(detalle2.getDebe());
                                    diferencia = diferencia - detalleComprobantePago.getEjecutado().doubleValue();
                                    detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                    bandera = true;
                                } else {
                                    if ((diferencia > 0) && detalle2.getEjecutado().doubleValue() > 0) {
                                        detalleComprobantePago.setEjecutado(new BigDecimal(diferencia));
                                        detalle2.setHaber(new BigDecimal(detalle2.getHaber().doubleValue() + diferencia));
                                        detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - diferencia));
                                        diferencia = diferencia - detalleComprobantePago.getHaber().doubleValue();
                                        bandera = true;
                                    } else {
                                        bandera = false;
                                    }
                                }
                                if (bandera) {
                                    detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                    detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                                    detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                                    detalleComprobantePago.setFuente(detalle2.getFuente());
                                    detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                                    detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                                    detalleComprobantePago.setDatoCargado(true);
                                    detalleComprobantePagoList.add(detalleComprobantePago);
                                }
                            }
                        }
                    }
                }
            }
            return detalleComprobantePagoList;
        } else {
            JsfUtil.addWarningMessage("AVISO", "No hay cuentas seleccionadas para generar el comprobante");
            return new ArrayList<>();
        }
    }

    public void addRolRubro(List<RubroPlanillaIESS> lista) {
        lista.forEach((r) -> {
            rolRubroList.add(r);
        });
    }

    public Boolean renderedPlanilla() {
        switch (getTramite().getTipoTramite().getAbreviatura()) {
            case "PPPI":
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void saveComprobante() {
        boolean edit = comprobantePago.getId() != null;
        if (comprobantePago.getDetalle().equals("")) {
            JsfUtil.addWarningMessage("COMPROBANTE DE PAGO", "Debe ingresar el detalle del comprobante de pago antes de guardar");
            return;
        }
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(comprobantePago.getFechaComprobante()), Utils.convertirMesALetra(Utils.getMes(comprobantePago.getFechaComprobante())));
        try {
            if (!periodoAbierto) {
                JsfUtil.addWarningMessage("AVISO", "Es imposible guardar porque la fecha seleccionada seleccionado esta cerrado");
                return;
            } else {
                if (comprobantePago.getCuentaBancaria() == null) {
                    JsfUtil.addWarningMessage("AVISO ", "Debe ingresar una cuenta de banco");
                    return;
                }
                System.out.println("codigoComprobantePago: " + codigoComprobantePago);
                if (codigoComprobantePago == 1) {
                    if (totalDebe != totalValor || totalDebe != totalHaber) {
                        JsfUtil.addWarningMessage("AVISO ", "La suma de los montos del Comprobante de Pago esta descuadrado");
                        return;
                    }
                } else {
                    if (totalDebe != totalHaber) {
                        JsfUtil.addWarningMessage("AVISO ", "La suma de los montos del Comprobante de Pago esta descuadrado");
                        return;
                    }
                }
                if (detalleComprobantePagoList == null && detalleComprobantePagoList.isEmpty()) {
                    JsfUtil.addWarningMessage("AVISO ", "No hay cuentas relacionadas, se recomienda verificar la relación Cuenta Contable - Catálogo Presupuesto");
                    return;
                }
                int auxiliar = 0;
                if (beneficiarioComprobantePagoList == null && beneficiarioComprobantePagoList.isEmpty()) {
                    JsfUtil.addWarningMessage("AVISO ", "No existen datos del beneficiario");
                    return;
                } else {
                    for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
                        if (beneficiario.getDetalleBanco() != null && beneficiario.getBeneficiario() != null) {
                            auxiliar = auxiliar + 1;
                        }
                    }
                }
                if (auxiliar < beneficiarioComprobantePagoList.size()) {
                    JsfUtil.addWarningMessage("AVISO ", "Revise la información del beneficiario");
                    return;
                }
                /*GUARDAR EL ENCABEZADO DEL COMPROBANTE DE PAGO*/
                if (enlaceSeleccionado.getId() != null) {
                    comprobantePago.setEnlace(enlaceSeleccionado);
                }
                comprobantePago.setUsuarioCreacion(userSession.getNameUser());
                comprobantePago.setFechaCreacion(new Date());
                comprobantePago.setEstado("REGISTRADO");
                comprobantePago.setPeriodo(opcionBusqueda.getAnio());
                comprobantePago.setDetalle(comprobantePago.getDetalle().toUpperCase());
                comprobantePago.setNumeroTramite(numTramite);
                ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
                if (ultimoComprobantePago != null) {
                    comprobantePago.setNumComprobante(BigInteger.valueOf(ultimoComprobantePago.getNumComprobante().longValue() + 1));
                } else {
                    comprobantePago.setNumComprobante(BigInteger.valueOf(1));
                }
                int contador = ultimoBeneficiarioComprobanteAnterior();
                comprobantePagoService.create(comprobantePago);
                /*GUARDAR EL DETALLE DEL COMPROBANTE DE PAGO*/
                for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                    detalleComprobante.setComprobantePago(comprobantePago);
                    detalleComprobantePagoService.create(detalleComprobante);
                }
                for (BeneficiarioComprobantePago beneficiarioComprobante : beneficiarioComprobantePagoList) {
                    beneficiarioComprobante.setComprobantePago(comprobantePago);
                    contador += 1;
                    beneficiarioComprobante.setNumeroTransferencia(BigInteger.valueOf(contador));
                    beneficiarioComprobante.setEstadoBeneficiario("REGISTRADO");
                    beneficiarioComprobantePagoService.create(beneficiarioComprobante);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
        if (comprobantePago.getDiarioGeneral() != null) {
            /*ACTUALIZAMOS EL ESTADO DEL DIARIO GENERAL */
            comprobantePago.getDiarioGeneral().setComprobantePago(Boolean.TRUE);
            diarioGeneralService.edit(comprobantePago.getDiarioGeneral());
        }
        this.ocultarTabla = Boolean.TRUE;
        verificadores();
        imprimirReporteComprobante(comprobantePago);
        vaciarFormulario("REINICIAR");
        PrimeFaces.current().ajax().update("comprobantePagoTable");
        PrimeFaces.current().ajax().update("formMain");
    }

    private void verificadores() {
        verificador(BigInteger.valueOf(tramite.getNumTramite()));
        verificadorInicializadorCP(BigInteger.valueOf(tramite.getNumTramite()));
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
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "PAGORMU":
                    if (anticipoSeleccionado.getId() == null) {
                        anticipoSeleccionado = anticipoRMUService.findAnticipoByNTramite(numTramite);
                    }
                    comprobantePago = diarioGeneralService.getComprobanteProcess(tramite.getNumTramite());
                    getParamts().put("usuarioTe", clienteService.getrolsUser(RolUsuario.tesorero));
                    anticipoSeleccionado.setComprobantePago(new ComprobantePago());
                    anticipoSeleccionado.setComprobantePago(comprobantePago);
                    anticipoSeleccionado.setFechaComprobantePago(new Date());
                    anticipoSeleccionado.setComprobante(Boolean.TRUE);
                    anticipoRMUService.edit(anticipoSeleccionado);
                    break;
                default:
                    getParamts().put("usuarioTe", clienteService.getrolsUser(RolUsuario.tesorero));
                    break;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public void anularComprobante(ComprobantePago comprobante) {
        this.totalDebe = 0;
        this.totalHaber = 0;
        /*EDITAMOS EL ESTADO DEL COMPROBANTE DE PAGO*/
        comprobante.setEstado("ANULADO");
        comprobante.setUsuarioModificacion(userSession.getNameUser());
        comprobante.setFechaModificacion(new Date());
        comprobantePagoService.edit(comprobante);
        /*TRAEMOS LOS DETALLES DEL COMPROBANTE DE PAGO*/
        detalleComprobantePagoList = detalleComprobantePagoService.getDetalleComprobantePago(comprobante);
        /*TRAEMOS LOS BENEFICIAROS DEL COMPROBANTE DE PAGO*/
        beneficiarioComprobantePagoList = beneficiarioComprobantePagoService.getBeneficiarioComprobante(comprobante);
        /*MODIFICAMOS EL ESTADO DE LOS BENEFICIARIOS*/
        for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
            beneficiario.setEstadoBeneficiario(comprobante.getEstado());
            beneficiarioComprobantePagoService.edit(beneficiario);
        }
        /*BUSCAMOS LA ULTIMA TRANSACCION EN EL LIBRO DIARIO*/
        DiarioGeneral ultimaActa = diarioGeneralService.getUltimaTransaccion(opcionBusqueda.getAnio());
        diarioGeneralAnulacion = new DiarioGeneral();
        if (ultimaActa != null) {
            diarioGeneralAnulacion.setNumeroTransaccion(BigInteger.valueOf(ultimaActa.getNumeroTransaccion().longValue() + 1));
        } else {
            diarioGeneralAnulacion.setNumeroTransaccion(BigInteger.valueOf(1));
        }
        /*CREAMOS UNA TRANSACCION EN EL LIBRO DIARIO*/
        if (comprobante.getDiarioGeneral() != null) {
            diarioGeneralAnulacion.setRetenido(comprobante.getDiarioGeneral().getRetenido());
            if (comprobante.getDiarioGeneral().getVariosBeneficiarios() != null) {
                diarioGeneralAnulacion.setVariosBeneficiarios(comprobante.getDiarioGeneral().getVariosBeneficiarios());
            }
            if (comprobante.getDiarioGeneral().getBeneficiario() != null) {
                diarioGeneralAnulacion.setBeneficiario(comprobante.getDiarioGeneral().getBeneficiario());
            }
            if (comprobante.getDiarioGeneral().getTipoBeneficiario() != null) {
                diarioGeneralAnulacion.setTipoBeneficiario(comprobante.getDiarioGeneral().getTipoBeneficiario());
            }
            if (comprobante.getDiarioGeneral().getEnlace() != null) {
                diarioGeneralAnulacion.setEnlace(comprobante.getDiarioGeneral().getEnlace());
            }
            if (comprobante.getDiarioGeneral().getCertificacionesPresupuestario() != null) {
                diarioGeneralAnulacion.setCertificacionesPresupuestario(comprobante.getDiarioGeneral().getCertificacionesPresupuestario());
            }
            diarioGeneralAnulacion.setComprobantePago(Boolean.TRUE);
            /*ACTUALIZAMOS EL ESTADO DE COMPROBANTE DEL DIARIO GENERAL PARA QUE VUELVA A ESTAR ACTIVO Y PODER REGISTRARLO*/
            comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
            diarioGeneralService.edit(comprobante.getDiarioGeneral());
        } else {
            diarioGeneralAnulacion.setRetenido(Boolean.FALSE);
        }
        diarioGeneralAnulacion.setObservacion("ANULACIÓN CP NO." + comprobante.getNumComprobante() + ", " + comprobante.getDetalle());
        diarioGeneralAnulacion.setUsuarioCreacion(userSession.getNameUser());
        diarioGeneralAnulacion.setFechaCreacion(new Date());
        diarioGeneralAnulacion.setEstado(Boolean.TRUE);
        diarioGeneralAnulacion.setPeriodo(opcionBusqueda.getAnio());
        diarioGeneralAnulacion.setClase(diarioGeneralService.getClaseTipo("clase_diario"));
        diarioGeneralAnulacion.setTipo(diarioGeneralService.getClaseTipo("tipo_financiero"));
        diarioGeneralAnulacion.setEstadoTransaccion("CUADRADO");
        diarioGeneralAnulacion.setFechaElaboracion(new Date());
        diarioGeneralAnulacion.setEstadoDiario("REGISTRADO");
        diarioGeneralAnulacion.setNumTramite(tramite.getNumTramite());
        diarioGeneralAnulacion = diarioGeneralService.create(diarioGeneralAnulacion);
        /*CREAMOS LOS DETALLES DE LA TRANSACCION*/
        int contador = 0;
        for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
            DetalleTransaccion detalleTransaccion = new DetalleTransaccion();
            detalleTransaccion.setDiarioGeneral(diarioGeneralAnulacion);
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalleTransaccion.setContador(bigInteger);
            detalleTransaccion.setCuentaContable(detalleComprobante.getCuentaContable());
            if (detalleComprobante.getDebe() != null) {
                double debe = detalleComprobante.getDebe().doubleValue() * (-1);
                detalleTransaccion.setDebe(new BigDecimal(debe));
            }
            if (detalleComprobante.getHaber() != null) {
                double haber = detalleComprobante.getHaber().doubleValue() * (-1);
                detalleTransaccion.setHaber(new BigDecimal(haber));
            }
            if (detalleComprobante.getTipoPago() != null) {
                detalleTransaccion.setTipoTransaccion(detalleComprobante.getTipoPago());
            }
            if (detalleComprobante.getEjecutado() != null) {
                double ejecutado = detalleComprobante.getEjecutado().doubleValue() * (-1);
                detalleTransaccion.setEjecutado(new BigDecimal(ejecutado));
            } else {
                detalleTransaccion.setEjecutado(BigDecimal.ZERO);
            }
            if (detalleComprobante.getPartidaPresupuestaria() != null) {
                detalleTransaccion.setPartidaPresupuestaria(detalleComprobante.getPartidaPresupuestaria());
            }
            if (detalleComprobante.getEstructuraProgramatica() != null) {
                detalleTransaccion.setEstructuraProgramatica(detalleComprobante.getEstructuraProgramatica());
            }
            if (detalleComprobante.getFuente() != null) {
                detalleTransaccion.setFuente(detalleComprobante.getFuente());
            }
            if (detalleComprobante.getCedulaPresupuestaria() != null) {
                detalleTransaccion.setCedulaPresupuestaria(detalleComprobante.getCedulaPresupuestaria());
            }
            detalleTransaccion.setDevengado(BigDecimal.ZERO);
            detalleTransaccion.setComprometido(BigDecimal.ZERO);
            if (detalleTransaccion.getDebe() != null) {
                totalDebe = Math.round((totalDebe + detalleTransaccion.getDebe().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            if (detalleTransaccion.getHaber() != null) {
                totalHaber = Math.round((totalHaber + detalleTransaccion.getHaber().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
            }
            detalleTransaccionService.create(detalleTransaccion);
        }
        /*ACTUALIZAMOS EL TOTAL DE LOS DEBES Y HABER DEL DETALLE DEL LIBRO DIARIO*/
        diarioGeneralAnulacion.setTotalDebe(new BigDecimal(totalDebe));
        diarioGeneralAnulacion.setTotalHaber(new BigDecimal(totalHaber));
        diarioGeneralService.edit(diarioGeneralAnulacion);
        if (comprobante.getDiarioGeneral() != null) {
            comprobante.getDiarioGeneral().setComprobantePago(Boolean.FALSE);
            diarioGeneralService.edit(comprobante.getDiarioGeneral());
        }
        /*ACTUALIZAMOS EL ESTADO DE COMPROBANTE DEL DIARIO GENERAL PARA QUE VUELVA A ESTAR ACTIVO Y PODER REGISTRARLO*/
        switch (getTramite().getTipoTramite().getAbreviatura()) {
            case "PAGORMU":
                anticipoSeleccionado.setComprobante(Boolean.FALSE);
                anticipoSeleccionado.setEstadoAnticipo(autorizado);
                anticipoRMUService.edit(anticipoSeleccionado);
                break;
        }
        verificadores();
        /*IMPRIMIMOS EL REPORTE DEL COMPROBANTE DE PAGO*/
        imprimirReporteComprobante(comprobante);
        PrimeFaces.current().executeScript("PF('comprobanteDiarioGeneralDLG').show()");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void generarArchivoDiarioGeneral(Boolean estado) {
        if (estado) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            imprimirReporteDiarioGeneral(diarioGeneralAnulacion);
        }
        vaciarFormulario("REINICIAR");
        diarioGeneralAnulacion = new DiarioGeneral();
        PrimeFaces.current().executeScript("PF('comprobanteDiarioGeneralDLG').hide()");
        PrimeFaces.current().ajax().update("comprobantePagoTable");
    }

    public void buscarDiarioGeneral(String accion) {
        if (accion.equals("BOTONBUSQUEDA")) {
            this.detalleComprobantePagoList = new ArrayList<>();
            this.beneficiarioComprobantePagoList = new ArrayList<>();
        }
        if (diarioGeneral.getNumeroTransaccion() != null) {
            DiarioGeneral diario = comprobantePagoService.getDiarioGeneral(diarioGeneral.getNumeroTransaccion(), opcionBusqueda.getAnio(), "CUADRADO", "clase_egreso", "tipo_financiero");
            if (diario != null) {
                DiarioGeneralSeleccionado("INGRESO");
            } else {
                PrimeFaces.current().ajax().update("diarioGeneralForm");
                PrimeFaces.current().executeScript("PF('diarioGeneralDLG').show()");
            }
        } else {
            PrimeFaces.current().ajax().update("diarioGeneralForm");
            PrimeFaces.current().executeScript("PF('diarioGeneralDLG').show()");
        }
        this.botonCuentaBanco = Boolean.TRUE;
        PrimeFaces.current().ajax().update("formComprobantePagos");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    public void openDlgCuentaBanco() {
        if ("procesos_pagos_noClasficados".equals(tramite.getTipoTramite().getAbreviatura())) {
            if (subEnlaceSeleccionado == null) {
                JsfUtil.addWarningMessage("AVISO", "ELIGA SI TIENE O NO AFECTACION PRESUPUESTARIA");
                return;
            }
        }
        if (comprobantePago.getCuentaBancaria() != null) {
            JsfUtil.addWarningMessage("AVISO", "Solo puede agregar una cuenta bancaria");
        } else {
            PrimeFaces.current().ajax().update("cuentaBancoDLG");
            PrimeFaces.current().ajax().update("cuentaBancoForm");
            PrimeFaces.current().executeScript("PF('cuentaBancoDLG').show()");
        }
    }

    public void añadirCuentaBanco() {
        if (cuentaBancariaSelecionada != null) {
            DetalleComprobantePago comprobante = new DetalleComprobantePago();
            int contador = detalleComprobantePagoList.size() + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            comprobante.setLinea(bigInteger);
            comprobante.setCuentaContable(cuentaBancariaSelecionada.getCuentaMovimiento());
            double sumaDebe = 0;
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                if (detalleComprobante.getDebe() != null) {
                    sumaDebe = sumaDebe + detalleComprobante.getDebe().doubleValue();
                }
            }
            comprobante.setHaber(new BigDecimal(sumaDebe));
            comprobante.setDebe(BigDecimal.ZERO);
            comprobante.setEjecutado(BigDecimal.ZERO);
            detalleComprobantePagoList.add(comprobante);
        }
        comprobantePago.setCuentaBancaria(cuentaBancariaSelecionada);
        actualizarTotales();
        totalBeneficiario();
        PrimeFaces.current().executeScript("PF('cuentaBancoDLG').hide()");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    private void totalBeneficiario() {
        if (beneficiarioComprobantePagoList.size() > 1) {
            actualizarTotalVariosBeneficiarios();
        } else {
            actualizarTotalBeneficiario();
        }
    }

    public void DiarioGeneralSeleccionado(String accion) {
        this.comprobantePago.setDiarioGeneral(diarioGeneral);
        if (diarioGeneral.getCertificacionesPresupuestario() != null) {
            this.comprobantePago.setReservaCompromiso(diarioGeneral.getCertificacionesPresupuestario());
        }
        /*TRAE LAS CUENTAS CON LAS PARTIDAS PRESUPUESTARIAS*/
        List<DetalleTransaccion> detallesTransacciones = detalleTransaccionService.getDetalleTransaccionDevengados(diarioGeneral);
        /*TRAE LAS CUENTA QUE ESTAN RELACIONADAS*/
        this.detalleTransaccionList = comprobantePagoService.getDetalleTransaccion(diarioGeneral);
        /*LISTAS AUXILIARES*/
        List<DetalleComprobantePago> detallesComprobantePago_1 = new ArrayList<>();
        List<DetalleComprobantePago> detallesComprobantePago_2 = new ArrayList<>();
        /*COMENZAMOS AÑADIR DATOS A LA LISTA*/
        if (detalleTransaccionList != null && !detalleTransaccionList.isEmpty()) {
            /*GUARDAMOS SOLO LA CUENTA CONTABLE  EL VALOR AL DEBE EN detallesComprobantePago_1*/
            for (DetalleTransaccion detalle : detalleTransaccionList) {
                detalleComprobantePago = new DetalleComprobantePago();
                detalleComprobantePago.setCuentaContable(detalle.getCuentaContable());
                detalleComprobantePago.setDebe(detalle.getHaber());
                detallesComprobantePago_1.add(detalleComprobantePago);
            }
            /*GUARDAMOS LAS PARTIDAS PRESUPUESTARIAS Y LA ESTRUCTURA PROGRAMATICA EN detallesComprobantePago_2*/
            for (DetalleTransaccion detalle : detallesTransacciones) {
                if (detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado")) {
                    detalleComprobantePago = new DetalleComprobantePago();
                    detalleComprobantePago.setPartidaPresupuestaria(detalle.getPartidaPresupuestaria());
                    detalleComprobantePago.setEstructuraProgramatica(detalle.getEstructuraProgramatica());
                    detalleComprobantePago.setFuente(detalle.getFuente());
                    detalleComprobantePago.setCedulaPresupuestaria(detalle.getCedulaPresupuestaria());
                    detalleComprobantePago.setIdDetalleReserva(detalle.getIdDetalleReserva());
                    /*EL VALOR DEL COMPROMETIDO LO GUARDAMOS MOMENTANEMENTE EN LA VARIABLE DEL HABER*/
                    detalleComprobantePago.setDebe(detalle.getComprometido());
                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                    for (DetalleTransaccion detalleTransaccion : detallesTransacciones) {
                        if (detalle.getPartidaPresupuestaria().equals(detalleTransaccion.getPartidaPresupuestaria())
                                && detalle.getTipoTransaccion().getCodigo().equals("diario_general_devengado") && detalleTransaccion.getTipoTransaccion().getCodigo().equals("diario_general_ejecucion")) {
                            detalleComprobantePago.setHaber(detalleTransaccion.getEjecutado());
                        }
                    }
                    detallesComprobantePago_2.add(detalleComprobantePago);
                }
            }
            /*UNIR LAS 2 LISTAS*/
            if (detallesComprobantePago_2.size() == 1) {
                for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                    DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                    int contador = detalleComprobantePagoList.size() + 1;
                    BigInteger bigInteger = BigInteger.valueOf(contador);
                    detalleComprobantePago.setLinea(bigInteger);
                    detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                    detalleComprobantePago.setDebe(detalle1.getDebe());
                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                    for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                        detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                        detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                        detalleComprobantePago.setFuente(detalle2.getFuente());
                        detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                        detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                    }
                    detalleComprobantePago.setEjecutado(detalleComprobantePago.getDebe());
                    detalleComprobantePagoList.add(detalleComprobantePago);
                }
            } else {
                for (DetalleComprobantePago detalle1 : detallesComprobantePago_1) {
                    double diferencia = detalle1.getDebe().doubleValue();
                    for (DetalleComprobantePago detalle2 : detallesComprobantePago_2) {
                        if (detalle1.getCuentaContable().getCodigo().substring(3, 5).equals(detalle2.getPartidaPresupuestaria().getCodigo().substring(0, 2))) {
                            /*VERIFICAMOS SI LA ASOCIACION CUENTA CONTABLE - CATALOGO PRESUPUESTO ESTA CORRECTA*/
                            Boolean relacionContable = comprobantePagoService.getRelacionCuentaContable(detalle1.getCuentaContable(), detalle2.getPartidaPresupuestaria());
                            if (relacionContable) {
                                /*Condicion para solo registrar una vez la cuenta contable*/
                                Boolean registrado = true;
                                Boolean bandera = false;
                                for (DetalleComprobantePago detComprobante : detalleComprobantePagoList) {
                                    if (detalle1.getCuentaContable().equals(detComprobante.getCuentaContable())) {
                                        registrado = false;
                                    }
                                }
                                /*CREAMOS UN NUEVO OBJETO PARA AÑADIRLO EN LA LISTA A PRESENTAR Y GUARDAR*/
                                DetalleComprobantePago detalleComprobantePago = new DetalleComprobantePago();
                                int contador = detalleComprobantePagoList.size() + 1;
                                BigInteger bigInteger = BigInteger.valueOf(contador);
                                detalleComprobantePago.setLinea(bigInteger);
                                if (registrado) {
                                    detalleComprobantePago.setCuentaContable(detalle1.getCuentaContable());
                                    detalleComprobantePago.setDebe(detalle1.getDebe());
                                    detalleComprobantePago.setHaber(BigDecimal.ZERO);
                                }
                                /*DEBE = COMPROMETIDO Y HABER = EJECUTADO  y EJECUTADO = SALDO*/
                                detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                if (diferencia >= detalle2.getDebe().doubleValue()) {
                                    detalle2.setHaber(detalle2.getDebe().add(detalle2.getHaber()));
                                    detalleComprobantePago.setEjecutado(detalle2.getDebe());
                                    diferencia = diferencia - detalleComprobantePago.getEjecutado().doubleValue();
                                    detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - detalle2.getHaber().doubleValue()));
                                    bandera = true;
                                } else {
                                    if ((diferencia > 0) && detalle2.getEjecutado().doubleValue() > 0) {
                                        detalleComprobantePago.setEjecutado(new BigDecimal(diferencia));
                                        detalle2.setHaber(new BigDecimal(detalle2.getHaber().doubleValue() + diferencia));
                                        detalle2.setEjecutado(new BigDecimal(detalle2.getDebe().doubleValue() - diferencia));
                                        diferencia = diferencia - detalleComprobantePago.getHaber().doubleValue();
                                        bandera = true;
                                    } else {
                                        bandera = false;
                                    }
                                }
                                if (bandera) {
                                    detalleComprobantePago.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                                    detalleComprobantePago.setPartidaPresupuestaria(detalle2.getPartidaPresupuestaria());
                                    detalleComprobantePago.setEstructuraProgramatica(detalle2.getEstructuraProgramatica());
                                    detalleComprobantePago.setFuente(detalle2.getFuente());
                                    detalleComprobantePago.setCedulaPresupuestaria(detalle2.getCedulaPresupuestaria());
                                    detalleComprobantePago.setIdDetalleReserva(detalle2.getIdDetalleReserva());
                                    detalleComprobantePago.setDatoCargado(true);
                                    detalleComprobantePagoList.add(detalleComprobantePago);
                                }
                            }
                        }
                    }
                }
            }
            actualizarTotales();
            /*AGREGAR LOS BENEFICIARIOS*/
            if (diarioGeneral.getVariosBeneficiarios()) {
                List<BeneficiarioSolicitudReserva> beneficiariosReservaList = beneficiarioSolicitudReservaService.getBeneficiarioReservasComprometidas(diarioGeneral.getCertificacionesPresupuestario());
                int contador = ultimoBeneficiarioComprobanteAnterior();
                for (BeneficiarioSolicitudReserva beneficiarioReserva : beneficiariosReservaList) {
                    beneficiarioComprobantePago = new BeneficiarioComprobantePago();
                    beneficiarioComprobantePago.setBeneficiario(beneficiarioReserva.getBeneficiario());
                    beneficiarioComprobantePago.setTipoBeneficiario(beneficiarioReserva.getTipoBeneficiario());
                    DetalleBanco detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioReserva.getBeneficiario(), beneficiarioReserva.getTipoBeneficiario());
                    if (detalleBanco != null) {
                        beneficiarioComprobantePago.setDetalleBanco(detalleBanco);
                    }
                    contador += 1;
                    beneficiarioComprobantePago.setNumeroTransferencia(BigInteger.valueOf(contador));
                    beneficiarioComprobantePago.setValor(beneficiarioReserva.getValor());
                    beneficiarioComprobantePagoList.add(beneficiarioComprobantePago);
                }
                actualizarTotalVariosBeneficiarios();
            } else {
                if (diarioGeneral.getBeneficiario() != null) {
                    DetalleBanco detalleBanco;
                    beneficiarioComprobantePago = new BeneficiarioComprobantePago();
                    beneficiarioComprobantePago.setBeneficiario(diarioGeneral.getBeneficiario());
                    beneficiarioComprobantePago.setTipoBeneficiario(diarioGeneral.getTipoBeneficiario());
                    detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiarioComprobantePago.getBeneficiario(), beneficiarioComprobantePago.getTipoBeneficiario());
                    if (detalleBanco != null) {
                        beneficiarioComprobantePago.setDetalleBanco(detalleBanco);
                    }
                    beneficiarioComprobantePago.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
                    beneficiarioComprobantePago.setValor(new BigDecimal(totalValor));
                    beneficiarioComprobantePagoList.add(beneficiarioComprobantePago);
                    actualizarTotalBeneficiario();
                }
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "No hay cuentas seleccionadas para generar el comprobante");
        }
        if (accion.equals("DIALOG")) {
            PrimeFaces.current().executeScript("PF('diarioGeneralDLG').hide()");
            PrimeFaces.current().ajax().update("formComprobantePagos");
            PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
            PrimeFaces.current().ajax().update("beneficiarioTable");
        }
    }

    private void actualizarTotales() {
        this.totalDebe = 0;
        this.totalHaber = 0;
        this.totalValor = 0;
        if (!detalleComprobantePagoList.isEmpty()) {
            for (DetalleComprobantePago comprobante : detalleComprobantePagoList) {
                if (comprobante.getDebe() != null) {
                    this.totalDebe = Math.round((totalDebe + comprobante.getDebe().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
                }
                if (comprobante.getHaber() != null) {
                    this.totalHaber = Math.round((totalHaber + comprobante.getHaber().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
                }
                if (comprobante.getEjecutado() != null) {
                    this.totalValor = Math.round((totalValor + comprobante.getEjecutado().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
                }
            }
        }
    }

    private void actualizarTotalBeneficiario() {
        this.totalValorBeneficiarios = 0;
        for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
            beneficiario.setValor(new BigDecimal(totalDebe));
            this.totalValorBeneficiarios = Math.round((totalValorBeneficiarios + beneficiario.getValor().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
        }
    }

    private void actualizarTotalVariosBeneficiarios() {
        this.totalValorBeneficiarios = 0;
        for (BeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoList) {
            this.totalValorBeneficiarios = Math.round((totalValorBeneficiarios + beneficiario.getValor().doubleValue()) * Math.pow(10, 2)) / Math.pow(10, 2);
        }
    }

    public void imprimirReporteComprobante(ComprobantePago comprobantePago) {
        servletSession.addParametro("id_comprobante_pago", comprobantePago.getId());
        servletSession.setNombreReporte("ComprobantePago_Registrado");
        servletSession.setNombreSubCarpeta("ComprobantePago");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirReporteDiarioGeneral(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void vaciarFormulario(String accion) {
        this.ocultarTabla = Boolean.TRUE;
        this.tipoPlanillaSeleccionada = null;
        this.beneficiarioComprobantePago = new BeneficiarioComprobantePago();
        this.detalleTransaccionList = new ArrayList<>();
        this.detalleComprobantePagoList = new ArrayList<>();
        this.beneficiarioComprobantePagoList = new ArrayList<>();
        this.beneficiariosAnuladosSeleccionados = new ArrayList<>();
        if (accion.equals("CANCELAR")) {
            PrimeFaces.current().ajax().update("dataTableComprobantePagos");
            PrimeFaces.current().ajax().update("formComprobantePagos");
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void renderedRMU() {
        this.ocultarTabla = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.fielsetDiarioGeneral = Boolean.FALSE;
        this.fielsetEnlaces = Boolean.FALSE;
        this.talentoHumano = Boolean.FALSE;
        this.financiero = Boolean.FALSE;
        this.botonAcciones = Boolean.FALSE;
        this.botonCuentaBanco = Boolean.TRUE;
    }

    public void renderedServiciosProfecionales() {
        this.ocultarTabla = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.fielsetDiarioGeneral = Boolean.FALSE;
        this.fielsetEnlaces = Boolean.FALSE;
        this.talentoHumano = Boolean.FALSE;
        this.financiero = Boolean.FALSE;
        this.botonAcciones = Boolean.FALSE;
        this.botonCuentaBanco = Boolean.TRUE;
    }

    public void renderedPagoServicio() {
        this.ocultarTabla = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.fielsetDiarioGeneral = Boolean.TRUE;
        this.fielsetEnlaces = Boolean.FALSE;
        this.talentoHumano = Boolean.FALSE;
        this.financiero = Boolean.FALSE;
        this.botonAcciones = Boolean.FALSE;
        this.botonCuentaBanco = Boolean.TRUE;
    }

    public void renderedAnticiposAProveedores() {
        this.ocultarTabla = Boolean.TRUE;
        this.ocultarBotonesAcciones = Boolean.TRUE;
        this.fielsetDiarioGeneral = Boolean.FALSE;
        this.fielsetEnlaces = Boolean.FALSE;
        this.talentoHumano = Boolean.FALSE;
        this.botonAcciones = Boolean.FALSE;
        this.botonAgregarCuentaContable = Boolean.TRUE;
        this.botonCuentaBanco = Boolean.TRUE;
    }

    public void actualizarFormulario(Boolean accion) {
        if (accion) {
            this.fielsetDiarioGeneral = Boolean.FALSE;
            this.fielsetEnlaces = Boolean.TRUE;
        } else {
            this.fielsetDiarioGeneral = Boolean.TRUE;
            this.fielsetEnlaces = Boolean.FALSE;
        }
        this.botonAcciones = Boolean.FALSE;
    }

    public void removerCuenta(DetalleComprobantePago detalleCP) {
        detalleComprobantePagoList.remove(detalleCP);
        int contador = 0;
        if (detalleComprobantePagoList.isEmpty()) {
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                contador += 1;
                detalleComprobante.setLinea(BigInteger.valueOf(contador + 1));
            }
            actualizarTotales();
        }
    }

    private int ultimoBeneficiarioComprobanteAnterior() {
        int contador = 0;
        ComprobantePago ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
        if (ultimoComprobantePago != null) {
            BeneficiarioComprobantePago beneficiarioComprobante = detalleComprobantePagoService.getUltimoNumeroReferencia(ultimoComprobantePago);
            if (beneficiarioComprobante != null) {
                contador = beneficiarioComprobante.getNumeroTransferencia().intValue();
            }
        }
        return contador;
    }


    /*MODULO DE ENLACE DE TALENTO HUMANO*/
    public void cargarAnticipo() {
        if (anticipoSeleccionado.getEstadoAnticipo().equals(autorizado)) {
            DetalleComprobantePago detalleCP = new DetalleComprobantePago();
            detalleCP.setLinea(BigInteger.valueOf(detalleComprobantePagoList.size() + 1));
            detalleCP.setCuentaContable(anticipoSeleccionado.getCuentaContable());
            detalleCP.setDebe(anticipoSeleccionado.getMontoAnticipo());
            detalleCP.setHaber(BigDecimal.ZERO);
            detalleCP.setEjecutado(BigDecimal.ZERO);
            detalleComprobantePagoList.add(detalleCP);
            BeneficiarioComprobantePago beneficiario = new BeneficiarioComprobantePago();
            beneficiario.setBeneficiario(anticipoSeleccionado.getServidor().getPersona());
            beneficiario.setTipoBeneficiario(Boolean.FALSE);
            DetalleBanco detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiario.getBeneficiario(), beneficiario.getTipoBeneficiario());
            if (detalleBanco != null) {
                beneficiario.setDetalleBanco(detalleBanco);
            }
            beneficiario.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
            beneficiario.setValor(anticipoSeleccionado.getMontoAnticipo());
            beneficiarioComprobantePagoList.add(beneficiario);
            botonCuentaBanco = Boolean.TRUE;
            actualizarTotales();
            actualizarTotalBeneficiario();
            anticipoSeleccionado.setEstadoAnticipo(autorizado);
        } else {
            JsfUtil.addWarningMessage("Error", "El anticipo no se encuentra en estado Aprobado: \n" + anticipoSeleccionado.getDescripcion());
        }
    }

    public void determinarRelacionPresupuestaria(DetalleComprobantePago detalleCP, Boolean tipoIngreso) {
        if (tipoIngreso) {
            detalleCP.setHaber(BigDecimal.ZERO);
        } else {
            detalleCP.setDebe(BigDecimal.ZERO);
        }
        reiniciarValoresDinamicos();
        Boolean accion = Boolean.FALSE;
        detalleCP.setTipoPago(null);
        detalleCP.setPartidaPresupuestaria(null);
        detalleCP.setEstructuraProgramatica(null);
        detalleCP.setFuente(null);
        detalleCP.setCedulaPresupuestaria(null);
        detalleCP.setEjecutado(BigDecimal.ZERO);
        CatalogoPresupuesto partidaPresupuestaria = null;
        if (tipoIngreso) {
            if (codigoComprobantePago == 1) {
                if (diarioGeneral != null && detalleCP.getEjecutado().doubleValue() > 0) {
                    detalleCP.setEjecutado(detalleCP.getDebe());
                }
                if (detalleCP.getCuentaContable().getTitulo() == 1 && detalleCP.getCuentaContable().getGrupo() == 1 && detalleCP.getCuentaContable().getSubGrupo() == 3) {
                    container();
                    return;
                }
                if (detalleCP.getCuentaContable().getCtaPagarCobrar()) {
                    partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleCP.getCuentaContable());
                    if (partidaPresupuestariaRelacionadas != null) {
                        for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                            añadirRelacionItemPresupuesto(catPresupuesto);
                        }
                    }
                } else {
                    partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleCP.getCuentaContable(), true);
                    if (partidaPresupuestaria != null) {
                        añadirRelacionItemPresupuesto(partidaPresupuestaria);
                    }
                }
                if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                    if (detalleCP.getCuentaContable().getTitulo() == 2) {
                        detalleCP.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleCP.setEjecutado(detalleCP.getDebe());
                    }
                    accion = Boolean.TRUE;
                }
            }
        } else {
            if (codigoComprobantePago == 1) {
                if (detalleCP.getCuentaContable().getTitulo() == 2 && detalleCP.getCuentaContable().getGrupo() == 1 && detalleCP.getCuentaContable().getSubGrupo() == 3) {
                    container();
                    return;
                }
                if (detalleCP.getCuentaContable().getCtaPagarCobrar()) {
                    partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleCP.getCuentaContable());
                    if (partidaPresupuestariaRelacionadas != null) {
                        for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                            añadirRelacionItemPresupuesto(catPresupuesto);
                        }
                    }
                } else {
                    partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleCP.getCuentaContable(), false);
                    if (partidaPresupuestaria != null) {
                        añadirRelacionItemPresupuesto(partidaPresupuestaria);
                    }
                }
                if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                    if (detalleCP.getCuentaContable().getTitulo() == 1) {
                        detalleCP.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleCP.setEjecutado(detalleCP.getHaber());
                    }
                    accion = Boolean.TRUE;
                }
            }
        }
        if (codigoComprobantePago == 1) {
            if (accion) {
                this.detalleComprobantePago = detalleCP;
                if (presupuestoRelacionado.size() == 1) {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        this.presupuestoSeleccionado = presupuesto;
                        guardarRelacionesPresupuestarias();
                    }
                } else {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        BigDecimal saldoPartida = diarioGeneralService.getSaldoPresupuesto(presupuesto, comprobantePago.getFechaComprobante());
                        if (saldoPartida != null) {
                            presupuesto.setMontoDisponible(saldoPartida);
                        } else {
                            presupuesto.setMontoDisponible(BigDecimal.ZERO);
                        }
                    }
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                }
            }
        }
        container();
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    public void determinarRelacionPresupuestaria2(DetalleComprobantePago detalleCP, Boolean tipoIngreso) {
        reiniciarValoresDinamicos();
        Boolean accion = Boolean.FALSE;
        detalleCP.setTipoPago(null);
        detalleCP.setPartidaPresupuestaria(null);
        detalleCP.setEstructuraProgramatica(null);
        detalleCP.setFuente(null);
        detalleCP.setCedulaPresupuestaria(null);
        detalleCP.setEjecutado(BigDecimal.ZERO);
        CatalogoPresupuesto partidaPresupuestaria = null;
        if (tipoIngreso) {
            detalleCP.setHaber(BigDecimal.ZERO);
            if (codigoComprobantePago == 1) {
                if (diarioGeneral != null && detalleCP.getEjecutado().doubleValue() > 0) {
                    detalleCP.setEjecutado(detalleCP.getDebe());
                }
                if (detalleCP.getCuentaContable().getTitulo() == 1 && detalleCP.getCuentaContable().getGrupo() == 1 && detalleCP.getCuentaContable().getSubGrupo() == 3) {
                    container();
                    return;
                }
                if (detalleCP.getCuentaContable().getCtaPagarCobrar()) {
                    partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleCP.getCuentaContable());
                    if (partidaPresupuestariaRelacionadas != null) {
                        for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                            añadirRelacionItemPresupuesto(catPresupuesto);
                        }
                    }
                } else {
                    partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleCP.getCuentaContable(), true);
                    if (partidaPresupuestaria != null) {
                        añadirRelacionItemPresupuesto(partidaPresupuestaria);
                    }
                }
                if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                    if (detalleCP.getCuentaContable().getTitulo() == 2) {
                        detalleCP.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleCP.setEjecutado(detalleCP.getDebe());
                    }
                    accion = Boolean.TRUE;
                }
            }
        } else {
            detalleCP.setDebe(BigDecimal.ZERO);
            if (codigoComprobantePago == 1) {
                if (detalleCP.getCuentaContable().getTitulo() == 2 && detalleCP.getCuentaContable().getGrupo() == 1 && detalleCP.getCuentaContable().getSubGrupo() == 3) {
                    container();
                    return;
                }
                if (detalleCP.getCuentaContable().getCtaPagarCobrar()) {
                    partidaPresupuestariaRelacionadas = diarioGeneralService.getListadoCatalogoPresupuesto(detalleCP.getCuentaContable());
                    if (partidaPresupuestariaRelacionadas != null) {
                        for (CatalogoPresupuesto catPresupuesto : partidaPresupuestariaRelacionadas) {
                            añadirRelacionItemPresupuesto(catPresupuesto);
                        }
                    }
                } else {
                    partidaPresupuestaria = diarioGeneralService.getRelacionPresupuestaria(detalleCP.getCuentaContable(), false);
                    if (partidaPresupuestaria != null) {
                        añadirRelacionItemPresupuesto(partidaPresupuestaria);
                    }
                }
                if (!presupuestoRelacionado.isEmpty() && presupuestoRelacionado != null) {
                    if (detalleCP.getCuentaContable().getTitulo() == 1) {
                        detalleCP.setTipoPago(diarioGeneralService.getClaseTipo("diario_general_ejecucion"));
                        detalleCP.setEjecutado(detalleCP.getHaber());
                    }
                    accion = Boolean.TRUE;
                }
            }
        }
        if (codigoComprobantePago == 1) {
            if (accion) {
                this.detalleComprobantePago = detalleCP;
                if (presupuestoRelacionado.size() == 1) {
                    for (Presupuesto presupuesto : presupuestoRelacionado) {
                        this.presupuestoSeleccionado = presupuesto;
                        guardarRelacionesPresupuestarias();
                    }
                } else {
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                }
            } else {
                JsfUtil.addWarningMessage("AVISO!!", "No tiene relación presupuestaria");

            }
        }
        container();
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    private void container() {
        actualizarTotales();
        totalBeneficiario();
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
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                if (detalleComprobante.getLinea().equals(detalleComprobantePago.getLinea())) {
                    detalleComprobante.setPartidaPresupuestaria(presupuestoSeleccionado.getItem());
                    detalleComprobante.setCedulaPresupuestaria(presupuestoSeleccionado.getPartida());
                    detalleComprobante.setFuente(presupuestoSeleccionado.getFuenteDirecta());
                    detalleComprobante.setEstructuraProgramatica(presupuestoSeleccionado.getEstructura());
                }
            }
            reiniciarValoresDinamicos();
            PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Dese selecionar una partida");
        }

    }

    private void reiniciarValoresDinamicos() {
        this.detalleComprobantePago = new DetalleComprobantePago();
        this.partidaPresupuestariaRelacionadas = new ArrayList<>();
        this.presupuestoRelacionado = new ArrayList<>();
        this.presupuestoSeleccionado = new Presupuesto();
    }

    /*ANTICIPOS A PROVEEDORES: BIENES-SERVICIOS-CONSULTORIAS-OBRAS*/
    public void cargarAnticipoAProveedores() {
        this.fieldsetReservaContrato = Boolean.TRUE;
        Adquisiciones adquisiciones = new Adquisiciones();
        adquisiciones = adquisicionesService.getAdquisicionById((BigInteger) getTramite().getIdReferencia());
        if (adquisiciones.getAnticipo() == null) {
            this.fieldsetReservaContrato = Boolean.FALSE;
            this.ocultarBotonesAcciones = Boolean.FALSE;
            this.ocultarTabla = Boolean.TRUE;
            JsfUtil.addWarningMessage("Advertencia", "El N° Contrato " + adquisiciones.getNumeroContrato() + " debe tener una cuenta de Anticipo, verifique en Gestión de Adquisiciones.");
            return;
        }
        List<SolicitudReservaCompromiso> listReserva = adquisicionesService.verificarReservaByContrato(true, adquisiciones);
        SolicitudReservaCompromiso reserva = new SolicitudReservaCompromiso();
        if (!listReserva.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                reserva = listReserva.get(0);
            }
            comprobantePago.setReservaCompromiso(reserva);
        }
        this.comprobantePago.setDetalle("PROCESO " + tramite.getTipoTramite().getDescripcion().toUpperCase() + ",TRAMITE " + tramite.getNumTramite() + "-" + comprobantePago.getPeriodo() + ", ");
        CuentaContable cc = new CuentaContable();
        cc = comprobantePagoService.getCuentaAnticipoByAdquisicion(BigInteger.valueOf(adquisiciones.getId()));
        proveedor = adquisiciones.getProveedor();
        if (cc != null) {
            botonAgregarCuentaContable = Boolean.FALSE;
            DetalleComprobantePago dcp = new DetalleComprobantePago();
            if (comprobantePago != null) {
                dcp.setComprobantePago(comprobantePago);
            }
            dcp.setCuentaContable(cc);
            dcp.setDebe(BigDecimal.ZERO);
            dcp.setHaber(BigDecimal.ZERO);
            dcp.setLinea(BigInteger.valueOf(detalleComprobantePagoList.size() + 1));
            dcp.setDatoCargado(false);
            detalleComprobantePagoList.add(dcp);
        } else {
            listaCuentaContables = new ArrayList<>();
            botonAgregarCuentaContable = Boolean.TRUE;
        }
        BeneficiarioComprobantePago beneficiario = new BeneficiarioComprobantePago();
        beneficiario.setBeneficiario(adquisiciones.getProveedor().getCliente());
        beneficiario.setTipoBeneficiario(Boolean.TRUE);
        DetalleBanco detalleBanco = beneficiarioComprobantePagoService.getCuentaBancaria(beneficiario.getBeneficiario(), beneficiario.getTipoBeneficiario());
        if (detalleBanco != null) {
            beneficiario.setDetalleBanco(detalleBanco);
        }
        beneficiario.setNumeroTransferencia(BigInteger.valueOf(ultimoBeneficiarioComprobanteAnterior() + 1));
        beneficiario.setValor(adquisiciones.getValorContratado());
        beneficiarioComprobantePagoList.add(beneficiario);
        actualizarTotalBeneficiario();
        actualizarTotales();
    }

    public void mostrarListaCuentas() {
        if ("procesos_pagos_noClasficados".equals(tramite.getTipoTramite().getAbreviatura())) {
            if (subEnlaceSeleccionado == null) {
                JsfUtil.addWarningMessage("AVISO", "ELIGA SI TIENE O NO AFECTACION PRESUPUESTARIA");
                return;
            }
        }
        listaCuentaContables = new ArrayList<>();
        if (getTramite().getTipoTramite().getAbreviatura().equals("PPPI")) {
            listaCuentaContables = comprobantePagoService.getlistaCuentasContablesFiltradas(comprobantePago.getPeriodo());
        } else {
            listaCuentaContables = comprobantePagoService.getlistaCuentasContables(comprobantePago.getPeriodo());
        }
        PrimeFaces.current().executeScript("PF('DlogoCuentasContables').show()");
        PrimeFaces.current().ajax().update(":formCuentaAnt");
    }

    public void anadirCuentaContable(CuentaContable cuentaContable) {
        DetalleComprobantePago detalleCP = new DetalleComprobantePago();
        detalleCP.setLinea(BigInteger.valueOf(detalleComprobantePagoList.size() + 1));
        detalleCP.setCuentaContable(cuentaContable);
        detalleComprobantePagoList.add(detalleCP);
        PrimeFaces.current().executeScript("PF('DlogoCuentasContables').hide()");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    public void quitarCuentaContable(DetalleComprobantePago detalleCP) {
        int index = 0;
        for (DetalleComprobantePago detalle : detalleComprobantePagoList) {
            if (detalle.getLinea() == detalleCP.getLinea()) {
                if (detalleCP.getHaber().doubleValue() != 0) {
                    comprobantePago.setCuentaBancaria(null);
                }
                break;
            }
            index = index + 1;
        }
        verificarCambiosDebeAnticipos(detalleCP);
        detalleComprobantePagoList.remove(index);
        int contador = 0;
        for (DetalleComprobantePago detalle : detalleComprobantePagoList) {
            contador = contador + 1;
            BigInteger bigInteger = BigInteger.valueOf(contador);
            detalle.setLinea(bigInteger);
        }

        actualizarTotales();
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
    }

    public void valorDebeParcialAnticipo(DetalleComprobantePago detalleComprobantePagoSeleccionado) {
        DetalleComprobantePago detalleCP = detalleComprobantePagoSeleccionado;
        verificarCambiosDebeAnticipos(detalleCP);
        actualizarTotales();
        totalBeneficiario();
        PrimeFaces.current().ajax().update("formComprobantePagos");
        PrimeFaces.current().ajax().update("detalleComprobantePagoTable");
        PrimeFaces.current().ajax().update("beneficiarioTable");
    }

    public void verificarCambiosDebeAnticipos(DetalleComprobantePago detalleCP) {
        if (detalleCP.getDebe().doubleValue() != 0) {
            double sumaDebe = 0;
            for (DetalleComprobantePago detalleComprobante : detalleComprobantePagoList) {
                if (detalleComprobante.getDebe() != null) {
                    sumaDebe = sumaDebe + detalleComprobante.getDebe().doubleValue();
                }
            }
            for (DetalleComprobantePago comprobante : detalleComprobantePagoList) {
                if (comprobante.getHaber().doubleValue() != 0) {
                    comprobante.setHaber(new BigDecimal(sumaDebe));
                }
            }
        }
    }

    public void deleteComprobantePago(int index, DetalleComprobantePago d) {
        if (cuentaBancariaSelecionada != null) {
            if (cuentaBancariaSelecionada.getId() != null) {
                if (d.getCuentaContable().getCodigo() == null ? cuentaBancariaSelecionada.getCuentaMovimiento().getCodigo() == null : d.getCuentaContable().getCodigo().equals(cuentaBancariaSelecionada.getCuentaMovimiento().getCodigo())) {
                    cuentaBancariaSelecionada = new CuentaBancaria();
                    comprobantePago.setCuentaBancaria(null);
                }
            }
        }
        detalleComprobantePagoList.remove(index);
        reecribirValorLinea();
        actualizarTotales();
        totalBeneficiario();
    }

    private void reecribirValorLinea() {
        int index = 0;

        for (DetalleComprobantePago detalleCP : detalleComprobantePagoList) {
            index += 1;
            BigInteger bigInteger = BigInteger.valueOf(index);
            detalleCP.setLinea(bigInteger);
        }
    }

    public List<CatalogoItem> getMesList() {
        return mesList;
    }

    public void setMesList(List<CatalogoItem> mesList) {
        this.mesList = mesList;
    }

    //<editor-fold defaultstate="collapsed" desc="Get -- Set">
    public ParametrosTalentoHumano getTipoPlanillaSeleccionada() {
        return tipoPlanillaSeleccionada;
    }

    public void setTipoPlanillaSeleccionada(ParametrosTalentoHumano tipoPlanillaSeleccionada) {
        this.tipoPlanillaSeleccionada = tipoPlanillaSeleccionada;
    }

    public List<ParametrosTalentoHumano> getPlanillaIESSList() {
        return planillaIESSList;
    }

    public void setPlanillaIESSList(List<ParametrosTalentoHumano> planillaIESSList) {
        this.planillaIESSList = planillaIESSList;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public String getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(String mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public List<TipoRol> getRolesSeleccionados() {
        return rolesSeleccionados;
    }

    public void setRolesSeleccionados(List<TipoRol> rolesSeleccionados) {
        this.rolesSeleccionados = rolesSeleccionados;
    }

    public List<TipoRol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<TipoRol> listaRol) {
        this.listaRol = listaRol;
    }

    public boolean isBloquearItems() {
        return bloquearItems;
    }

    public void setBloquearItems(boolean bloquearItems) {
        this.bloquearItems = bloquearItems;
    }

    public CatalogoItem getSubEnlaceSeleccionado() {
        return subEnlaceSeleccionado;
    }

    public void setSubEnlaceSeleccionado(CatalogoItem subEnlaceSeleccionado) {
        this.subEnlaceSeleccionado = subEnlaceSeleccionado;
    }

    public boolean isEleccion() {
        return eleccion;
    }

    public void setEleccion(boolean eleccion) {
        this.eleccion = eleccion;
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

    public DiarioGeneral getDiarioGeneralAnulacion() {
        return diarioGeneralAnulacion;
    }

    public void setDiarioGeneralAnulacion(DiarioGeneral diarioGeneralAnulacion) {
        this.diarioGeneralAnulacion = diarioGeneralAnulacion;
    }

    public BeneficiarioComprobantePago getBeneficiarioComprobantePago() {
        return beneficiarioComprobantePago;
    }

    public void setBeneficiarioComprobantePago(BeneficiarioComprobantePago beneficiarioComprobantePago) {
        this.beneficiarioComprobantePago = beneficiarioComprobantePago;
    }

    public CuentaBancaria getCuentaBancariaSelecionada() {
        return cuentaBancariaSelecionada;
    }

    public void setCuentaBancariaSelecionada(CuentaBancaria cuentaBancariaSelecionada) {
        this.cuentaBancariaSelecionada = cuentaBancariaSelecionada;
    }

    public CatalogoItem getEnlaceSeleccionado() {
        return enlaceSeleccionado;
    }

    public void setEnlaceSeleccionado(CatalogoItem enlaceSeleccionado) {
        this.enlaceSeleccionado = enlaceSeleccionado;
    }

    public CatalogoItem getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(CatalogoItem autorizado) {
        this.autorizado = autorizado;
    }

    public AnticipoRemuneracion getAnticipoSeleccionado() {
        return anticipoSeleccionado;
    }

    public void setAnticipoSeleccionado(AnticipoRemuneracion anticipoSeleccionado) {
        this.anticipoSeleccionado = anticipoSeleccionado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public DetalleComprobantePago getDetalleComprobantePago() {
        return detalleComprobantePago;
    }

    public void setDetalleComprobantePago(DetalleComprobantePago detalleComprobantePago) {
        this.detalleComprobantePago = detalleComprobantePago;
    }

    public Presupuesto getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public void setPresupuestoSeleccionado(Presupuesto presupuestoSeleccionado) {
        this.presupuestoSeleccionado = presupuestoSeleccionado;
    }

    public LazyModel<ComprobantePago> getComprobantePagoLazy() {
        return comprobantePagoLazy;
    }

    public void setComprobantePagoLazy(LazyModel<ComprobantePago> comprobantePagoLazy) {
        this.comprobantePagoLazy = comprobantePagoLazy;
    }

    public LazyModel<CuentaBancaria> getCuentaBancariaLazyModel() {
        return cuentaBancariaLazyModel;
    }

    public void setCuentaBancariaLazyModel(LazyModel<CuentaBancaria> cuentaBancariaLazyModel) {
        this.cuentaBancariaLazyModel = cuentaBancariaLazyModel;
    }

    public List<DetalleTransaccion> getDetalleTransaccionList() {
        return detalleTransaccionList;
    }

    public void setDetalleTransaccionList(List<DetalleTransaccion> detalleTransaccionList) {
        this.detalleTransaccionList = detalleTransaccionList;
    }

    public List<DetalleComprobantePago> getDetalleComprobantePagoList() {
        return detalleComprobantePagoList;
    }

    public void setDetalleComprobantePagoList(List<DetalleComprobantePago> detalleComprobantePagoList) {
        this.detalleComprobantePagoList = detalleComprobantePagoList;
    }

    public List<BeneficiarioComprobantePago> getBeneficiarioComprobantePagoList() {
        return beneficiarioComprobantePagoList;
    }

    public void setBeneficiarioComprobantePagoList(List<BeneficiarioComprobantePago> beneficiarioComprobantePagoList) {
        this.beneficiarioComprobantePagoList = beneficiarioComprobantePagoList;
    }

    public List<BeneficiarioComprobantePago> getBeneficiariosAnuladosSeleccionados() {
        return beneficiariosAnuladosSeleccionados;
    }

    public void setBeneficiariosAnuladosSeleccionados(List<BeneficiarioComprobantePago> beneficiariosAnuladosSeleccionados) {
        this.beneficiariosAnuladosSeleccionados = beneficiariosAnuladosSeleccionados;
    }

    public List<CatalogoItem> getEnlacesComprobantePago() {
        return enlacesComprobantePago;
    }

    public void setEnlacesComprobantePago(List<CatalogoItem> enlacesComprobantePago) {
        this.enlacesComprobantePago = enlacesComprobantePago;
    }

    public List<DetalleTransaccion> getPartidaEstructura() {
        return partidaEstructura;
    }

    public void setPartidaEstructura(List<DetalleTransaccion> partidaEstructura) {
        this.partidaEstructura = partidaEstructura;
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

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public Boolean getOcultarTabla() {
        return ocultarTabla;
    }

    public void setOcultarTabla(Boolean ocultarTabla) {
        this.ocultarTabla = ocultarTabla;
    }

    public Boolean getOcultarBotonesAcciones() {
        return ocultarBotonesAcciones;
    }

    public void setOcultarBotonesAcciones(Boolean ocultarBotonesAcciones) {
        this.ocultarBotonesAcciones = ocultarBotonesAcciones;
    }

    public Boolean getBotonCuentaBanco() {
        return botonCuentaBanco;
    }

    public void setBotonCuentaBanco(Boolean botonCuentaBanco) {
        this.botonCuentaBanco = botonCuentaBanco;
    }

    public Boolean getFielsetDiarioGeneral() {
        return fielsetDiarioGeneral;
    }

    public void setFielsetDiarioGeneral(Boolean fielsetDiarioGeneral) {
        this.fielsetDiarioGeneral = fielsetDiarioGeneral;
    }

    public Boolean getFielsetEnlaces() {
        return fielsetEnlaces;
    }

    public void setFielsetEnlaces(Boolean fielsetEnlaces) {
        this.fielsetEnlaces = fielsetEnlaces;
    }

    public Boolean getBotonAcciones() {
        return botonAcciones;
    }

    public void setBotonAcciones(Boolean botonAcciones) {
        this.botonAcciones = botonAcciones;
    }

    public Boolean getTalentoHumano() {
        return talentoHumano;
    }

    public void setTalentoHumano(Boolean talentoHumano) {
        this.talentoHumano = talentoHumano;
    }

    public Boolean getFinanciero() {
        return financiero;
    }

    public void setFinanciero(Boolean financiero) {
        this.financiero = financiero;
    }

    public String getTipoProcesoSeleccionado() {
        return tipoProcesoSeleccionado;
    }

    public void setTipoProcesoSeleccionado(String tipoProcesoSeleccionado) {
        this.tipoProcesoSeleccionado = tipoProcesoSeleccionado;
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

    public double getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(double totalValor) {
        this.totalValor = totalValor;
    }

    public double getTotalValorBeneficiarios() {
        return totalValorBeneficiarios;
    }

    public void setTotalValorBeneficiarios(double totalValorBeneficiarios) {
        this.totalValorBeneficiarios = totalValorBeneficiarios;
    }

    public boolean isBtnCompletarTarea() {
        return btnCompletarTarea;
    }

    public void setBtnCompletarTarea(boolean btnCompletarTarea) {
        this.btnCompletarTarea = btnCompletarTarea;
    }

    public boolean isBtniniciar() {
        return btniniciar;
    }

    public void setBtniniciar(boolean btniniciar) {
        this.btniniciar = btniniciar;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public int getCodigoComprobantePago() {
        return codigoComprobantePago;
    }

    public void setCodigoComprobantePago(int codigoComprobantePago) {
        this.codigoComprobantePago = codigoComprobantePago;
    }

    public List<CuentaContable> getListaCuentaContables() {
        return listaCuentaContables;
    }

    public void setListaCuentaContables(List<CuentaContable> listaCuentaContables) {
        this.listaCuentaContables = listaCuentaContables;
    }

    public Boolean getBotonAgregarCuentaContable() {
        return botonAgregarCuentaContable;
    }

    public void setBotonAgregarCuentaContable(Boolean botonAgregarCuentaContable) {
        this.botonAgregarCuentaContable = botonAgregarCuentaContable;
    }

//    public List<CatalogoItem> getListaEnlaces() {
//        return listaEnlaces;
//    }
//
//    public void setListaEnlaces(List<CatalogoItem> listaEnlaces) {
//        this.listaEnlaces = listaEnlaces;
//    }
    public Boolean getFieldsetReservaContrato() {
        return fieldsetReservaContrato;
    }

    public void setFieldsetReservaContrato(Boolean fieldsetReservaContrato) {
        this.fieldsetReservaContrato = fieldsetReservaContrato;
    }

//</editor-fold>
}
