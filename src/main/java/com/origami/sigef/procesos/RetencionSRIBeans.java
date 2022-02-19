/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos;

import com.google.gson.Gson;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.ats.entities.FacturaFormasPago;
import com.origami.sigef.ats.entities.PagoExterior;
import com.origami.sigef.ats.entities.PaisAts;
import com.origami.sigef.ats.entities.PaisParaisoFiscal;
import com.origami.sigef.ats.entities.SustentoComprobante;
import com.origami.sigef.ats.modelAts.Air;
import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.DetalleCompras;
import com.origami.sigef.ats.modelAts.FormasPagoModel;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.PagoExteriorModel;
import com.origami.sigef.ats.service.FacturaFormasPagoService;
import com.origami.sigef.ats.service.PagoExteriorService;
import com.origami.sigef.ats.service.PaisAtsService;
import com.origami.sigef.ats.service.PaisParaisoFiscalService;
import com.origami.sigef.ats.service.SustentoComprobanteService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CuentaContableRetencionService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.FormaPagoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Cabecera;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.entities.LiquidacionTipo;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import com.origami.sigef.tesoreria.service.RubroService;
import com.origami.sigef.tesoreria.service.RubroTipoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "retencionProcesoView")
@ViewScoped
public class RetencionSRIBeans extends BpmnBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Inject">
    @Inject
    private PagoExteriorService pagoExteriorService;
    @Inject
    private PaisParaisoFiscalService paisParaisoFiscalService;
    @Inject
    private PaisAtsService paisAtsService;
    @Inject
    protected UserSession userSession;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ServletSession servletSession;

    @Inject
    private ServidorService servidorService;
    @Inject
    private RubroTipoService rubroTipoService;
    @Inject
    private RubroService rubroService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private CuentaContableRetencionService cuentaContableRetencionService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private SustentoComprobanteService sustentoComprobanteService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private FormaPagoService formaPagoService;
    @Inject
    private FacturaFormasPagoService facturaFormasPagoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ClienteService clienteService;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Objetos">
    private DiarioGeneral diarioGeneral;
    private DiarioGeneral diarioGeneralClone;

    private Boolean accesoComprobante;
    private Cajero cajero;
    private Cliente cliente;
    private Liquidacion liquidacion;
    private LiquidacionDetalle liquidacionDetalle;
    private List<LiquidacionDetalle> liquidacionDetalles;
    private List<Integer> anios;
    private Integer anioMax;
    private List<String> meses;
    private Integer mesActual;
    private List<Comprobantes> comprobantes;
    private List<RubroTipo> rubroTipos;
    private RubroTipo rubroTipo;

    private List<Rubro> rubrosRentas;
    private List<Rubro> rubrosIVA;
    private Rubro rubroRentas, rubroIVA, rubroBaseImponibleDifCero, rubrosRetencionesIVA10, rubrosRetencionesIVA20,
            rubrosRetencionesIVA30, rubrosRetencionesIVA50, rubrosRetencionesIVA70, rubrosRetencionesIVA100;

    private BigDecimal baseImponible, impuesto, valorTotal, totalRetencion;

    private Boolean activarBtnDetalle, esRentas;

    private ComprobanteElectronico comprobanteElectronico;

    private SimpleDateFormat format;

    private List<Factura> facturas;
    private Factura facturaSeleccionada;
    private Factura factura;
    private List<CuentaContableRetencion> cuentaContableRetenciones;
    private CuentaContableRetencion cuentaContableRetencion;
    private List<DetalleTransaccion> detalleTransacciones;
    private DetalleTransaccion detalleTransaccionSeleccionado;
    private Boolean editar;
    private Boolean renderedBotones;
    private Boolean disabledAjaxSelection;
    private Boolean readOnlyInput;

    private List<SustentoComprobante> sustentosComprobantes;
    private List<PaisAts> paises;
    private Boolean disabledPago;
    private Boolean disabledRegimenGeneral;
    private Boolean disabledParaisoFiscal;
    private Boolean disabledDenominacion;
    private Boolean disabledguardarBases;
    private List<PaisParaisoFiscal> paisParaisoFiscal;
    private DetalleCompras detalleCompras;
    private Air air;
    private BigDecimal valorPorcentaje;
    private LazyModel<Factura> facturasLazyRegis;
    private List<FormaPago> formasPagos;
    private List<FacturaFormasPago> facturaFormasPago;
    private FormaPago formaPagoSelection;
    private Boolean renderTabFactura;
//</editor-fold>

    private List<DetalleTransaccion> detalleTransaccionesSeleccionados;
    private BigDecimal totalCuentasValor;

    @PostConstruct
    public void init() {
        loadModel();
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                this.observacion = new Observaciones();
                switch (getTramite().getTipoTramite().getAbreviatura()) {
                    case "PPS_profesionales":
                        diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());
                        if (diarioGeneral != null) {
                            cargarDatosRetenciones();
                        }
                        break;
                    default:
                        diarioGeneral = diarioGeneralService.numTramiteDiarioGeneral(tramite.getNumTramite());
                        if (diarioGeneral != null) {
                            cargarDatosRetenciones();
                        }
                        break;
                }
            }
        }
    }

    public void loadModel() {
        totalCuentasValor = BigDecimal.ZERO;
        disabledAjaxSelection = Boolean.FALSE;
        accesoComprobante = Boolean.TRUE;
        editar = Boolean.FALSE;
        renderedBotones = Boolean.TRUE;
        readOnlyInput = Boolean.FALSE;
        cajero = katalinaService.findCajero();
        disabledPago = Boolean.TRUE;
        disabledRegimenGeneral = Boolean.TRUE;
        disabledParaisoFiscal = Boolean.TRUE;
        disabledDenominacion = Boolean.TRUE;
        if (cajero != null) {
            detalleTransaccionesSeleccionados = new ArrayList<>();
            facturaSeleccionada = new Factura();
            factura = new Factura();
            detalleTransaccionSeleccionado = new DetalleTransaccion();
            renderTabFactura = Boolean.TRUE;
            formaPagoSelection = new FormaPago();
            facturasLazyRegis = new LazyModel<>(Factura.class);
            facturasLazyRegis.getFilterss().put("estado", true);
            facturasLazyRegis.getFilterss().put("retenida", false);
            valorPorcentaje = BigDecimal.ZERO;
            rubroBaseImponibleDifCero = katalinaService.findRubroPredeterminadoByTipoCompra(12, true);
            rubrosRetencionesIVA10 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(10));
            rubrosRetencionesIVA20 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(20));
            rubrosRetencionesIVA30 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(30));
            rubrosRetencionesIVA50 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(50));
            rubrosRetencionesIVA70 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(70));
            rubrosRetencionesIVA100 = katalinaService.findBaseRetencionIVA(12, true, Double.valueOf(100));

            paises = paisAtsService.findByNamedQuery("PaisAts.findAll");
            paisParaisoFiscal = paisParaisoFiscalService.findByNamedQuery("PaisParaisoFiscal.findAll");
            formasPagos = formaPagoService.findAll();
            format = new SimpleDateFormat("yyyy-MM-dd");
            valorTotal = BigDecimal.ZERO;
            impuesto = BigDecimal.ZERO;
            baseImponible = BigDecimal.ZERO;
            rubroTipo = new RubroTipo();
            liquidacion = new Liquidacion();
            liquidacion.setIdentificacionProveedor("");

            diarioGeneral = new DiarioGeneral();
            facturas = new ArrayList<>();
            detalleTransacciones = new ArrayList<>();
            cuentaContableRetenciones = new ArrayList<>();
            cuentaContableRetencion = new CuentaContableRetencion();

            liquidacionDetalle = new LiquidacionDetalle();
            liquidacionDetalles = new ArrayList<>();
            liquidacion.setSolicitante(new Proveedor());
            liquidacion.setComprobante(katalinaService.findComprobante(ComprobantesCode.COMPPROBANTERETENCION));

            comprobantes = katalinaService.findComprobantesRetienen();
            rubroTipos = rubroTipoService.findByTipo("RETENCION");
            rubrosRentas = rubroService.findRubrosByTipo(11);
            rubrosIVA = rubroService.findRubrosByTipo(12);
            esRentas = Boolean.TRUE;
            activarBtnDetalle = Boolean.FALSE;
            sustentosComprobantes = sustentoComprobanteService.findAll();
            factura = new Factura();

            detalleCompras = new DetalleCompras();
            liquidacion.setAts(new IVA());
            air = new Air();
            factura.setPagoExterior(new PagoExterior());
            loadAniosMeses();
            liquidacion.setAts(createAts(liquidacion, cajero));
            liquidacion.getAts().setCompras(new Compras());
        } else {
            accesoComprobante = Boolean.FALSE;
        }
    }

    public void loadModelSession() {
        disabledAjaxSelection = Boolean.TRUE;
        Map<String, Object> parametros = servletSession.getParametros();
        liquidacion = (Liquidacion) parametros.get("liquidacion");
        editar = (Boolean) parametros.get("edit");
        if (!editar) {
            renderedBotones = Boolean.FALSE;
            readOnlyInput = Boolean.TRUE;
        }
        liquidacion = liquidacionService.findById(liquidacion.getId());
        liquidacion.setIdentificacionProveedor(liquidacion.getSolicitante().getCliente().getIdentificacionCompleta());
//        diarioGeneral = liquidacion.getDiarioGeneral();
        detalleCuentasRetencion();
        if (!liquidacion.getLiquidacionDetalles().isEmpty()) {
            for (LiquidacionDetalle d : liquidacion.getLiquidacionDetalles()) {
                d.setLiquidacion(liquidacion);
                if (!facturas.contains(d.getFactura())) {
                    facturas.add(d.getFactura());
                }
                d.setFactura(d.getFactura());
                factura = (d.getFactura() != null ? d.getFactura() : new Factura());
                liquidacionDetalles.add(inicializarLiquidacionDetalleUpdate(d));
            }
        }
        liquidacion = inicializarLiquidacionUpdate(liquidacion);
    }

    public void completarTarea() {
        try {
            getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void loadAniosMeses() {
        anios = new ArrayList<>();
        anioMax = (Utils.getAnio(new Date()) - 4);
        for (int i = anioMax; i <= (Utils.getAnio(new Date())); i++) {
            anios.add(i);
        }
        Collections.sort(anios, Collections.reverseOrder());

        meses = Utils.getMeses();

        liquidacion.setAnio(anios.get(0));
        liquidacion.setMes(Utils.getMes(new Date()));
    }

    public Integer mes(String mes) {
        return Utils.convertirLetraAMes(mes);
    }

    public void guardarEmitirRetencion() {
        if (validarRetencion(liquidacion, facturas, liquidacionDetalles)) {
            List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
            ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
            totalRetencion = BigDecimal.ZERO;
            for (LiquidacionDetalle det : liquidacionDetalles) {
                det.setId(null);
                totalRetencion = det.getValor().add(totalRetencion);
            }
            liquidacion = initLiquidacion(liquidacion);
            liquidacion.setUserIngreso(session.getUserId());
            liquidacion.setTotalPagar(totalRetencion);
            liquidacion.setPeriodo(liquidacion.getAnio() + "-" + Utils.convertirMesALetra(liquidacion.getMes()));
            liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaRetencion()));
            liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
            Gson gson = new Gson();
            if (liquidacion.getAts() != null && liquidacion.getAts().getCompras() != null && liquidacion.getAts().getCompras().getDetalleCompras() != null) {
                for (DetalleCompras d : liquidacion.getAts().getCompras().getDetalleCompras()) {
                    d.setSecRetencion1(String.format("%09d", liquidacion.getNumeroComprobante()));
                }
            }
            liquidacion.getDiarioGeneral().setRetenido(Boolean.TRUE);
//            liquidacion.getDiarioGeneral().setNumeroTransaccion(diarioGeneralClone.getNumeroTransaccion());
//            diarioGeneralService.edit(liquidacion.getDiarioGeneral());
            liquidacion.setDetalleCompras(gson.toJson(liquidacion.getAts()));
            liquidacionService.create(liquidacion);
            for (LiquidacionDetalle detalle : liquidacionDetalles) {
                impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                impuestoComprobanteElectronico.setCodigo(detalle.getRubro().getRubroTipo().getCodigo());
                impuestoComprobanteElectronico.setCodigoPorcentaje(detalle.getRubro().getCodigo());
                impuestoComprobanteElectronico.setBaseImponible(detalle.getBaseImponible());
                impuestoComprobanteElectronico.setPorcentajeRetencion(detalle.getImpuesto());
                impuestoComprobanteElectronico.setValor(detalle.getValor());
                impuestoComprobanteElectronico.setCodigoDocumento(liquidacion.getComprobante().getCodigo()); //codDocSustento
                impuestoComprobanteElectronico.setNumDocumento(detalle.getFactura().getNumFactura().replace("-", "").trim()); //numDocSustento
                impuestoComprobanteElectronico.setFechaEmisionDocumento(format.format(detalle.getFactura().getFechaFactura())); //fechaEmisionDocSustento
                impuestoComprobanteElectronico.setDescripcionDocumentoModificado(liquidacion.getComprobanteModifica().getDescripcion());
                impuestoComprobanteRetencion.add(impuestoComprobanteElectronico);
                detalle.setId(null);
                detalle.setLiquidacion(liquidacion);
                detalle.setContabilizado(Boolean.FALSE);
                detalle.getFactura().setRetenida(Boolean.TRUE);
                facturaService.edit(detalle.getFactura());
                liquidacionDetalleService.create(detalle);
            }
            comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
            //COMPROBANTE RETENCION => 07
            comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.COMPPROBANTERETENCION);
            //comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(factura.getFechaFactura()));
            comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobanteModifica().getCodigo());
            comprobanteElectronico.setNumComprobanteModifica(factura.getNumFactura().replace("-", "").trim());
            comprobanteElectronico.setMes(liquidacion.getMes().toString());
            comprobanteElectronico.setAnio(liquidacion.getAnio().toString());
            comprobanteElectronico.setImpuestoComprobanteRetencion(impuestoComprobanteRetencion);
            comprobanteElectronicaService.enviarComprobanteRentencionSRI(comprobanteElectronico);
            JsfUtil.addSuccessMessage("", "Retencion " + (editar ? "Editada" : "Generada") + "  con éxito");
            cancelar();
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void buscarProveedor() {
        if (!liquidacion.getIdentificacionProveedor().isEmpty()) {
            Proveedor p = servidorService.findByProveedor(liquidacion.getIdentificacionProveedor());
            if (p != null) {
                liquidacion.setSolicitante(p);
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
        }
    }

    private void setPeriodoDiarioRetencion() {
        liquidacion.setFechaEmision(diarioGeneral.getFechaElaboracion());
        liquidacion.setFechaEmisionCabecera(diarioGeneral.getFechaElaboracion());
        liquidacion.setMes(Utils.getMes(diarioGeneral.getFechaElaboracion()));
        liquidacion.setAnio(Utils.getAnio(diarioGeneral.getFechaElaboracion()));
    }

    public void setPeriodoFiscal() {
        liquidacion.setAnio(Utils.getAnio(liquidacion.getFechaEmision()));
        liquidacion.setMes(Utils.getMes(liquidacion.getFechaEmision()));
        liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
    }

    public void buscarDiarioGeneral() {
        if (liquidacion.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe selecionar el periodo fiscal antes de continuar");
            return;
        }
        if (diarioGeneral.getNumeroTransaccion() != null) {
//            diarioGeneral = diarioGeneralService.getDiarioGeneralRetencionByNumTransaccion(diarioGeneral.getNumeroTransaccion(), true, false, liquidacion.getAnio().shortValue());
            setPeriodoDiarioRetencion();
            if (diarioGeneral == null) {
                Utils.openDialog("/facelet/contabilidad/DiarioGeneral/dialogDiarioGeneral", null);
            } else {
                cargarDatosRetenciones();
            }
        } else {
            Utils.openDialog("/facelet/contabilidad/DiarioGeneral/dialogDiarioGeneral", null);
        }
    }

    public void cargarValorImpuestoIVA() {
        impuesto = rubroIVA.getPorcentajeRetencion();
    }

    public void cargarValorImpuestoRentas() {
        impuesto = rubroRentas.getPorcentaje();
    }

    public void calcularValorRetencion() {
        if (validarCamposRetencion(baseImponible, impuesto, detalleTransaccionSeleccionado, cuentaContableRetencion)) {
            valorTotal = baseImponible.multiply(impuesto.setScale(2)).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
            if (!impuesto.setScale(2).equals(BigDecimal.ZERO.setScale(2))) {
                if (!valorTotal.equals(totalCuentasValor.setScale(2, RoundingMode.HALF_UP))) {
                    JsfUtil.addErrorMessage("", "El Valor Retenido no es igual al valor registrado en Diario General");
                    activarBtnDetalle = Boolean.FALSE;
                    return;
                }
            }
            activarBtnDetalle = Boolean.TRUE;
        } else {
            activarBtnDetalle = Boolean.FALSE;
        }
    }

    public void agregarDetalle() {
        if (factura == null || factura.getId() == null) {
            JsfUtil.addErrorMessage("", "Necesita selecionar una Factura");
            return;
        }
        if (detalleCompras.getCodSustento() == null) {
            JsfUtil.addErrorMessage("", "Seleccione un sustento de comprobante");
            return;
        }

        if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equalsIgnoreCase("RENTAS")) {
            Air.DetalleAir detalleAir = new Air.DetalleAir(cuentaContableRetencion.getRetencion().getCodigo(),
                    baseImponible.setScale(2), impuesto.setScale(2), valorTotal);
            air.getDetalleAir().add(detalleAir);
            for (DetalleCompras comprasModel : liquidacion.getAts().getCompras().getDetalleCompras()) {
                if (comprasModel.getNumFactura().equals(factura.getNumFactura())) {
                    comprasModel.setAir(air);
                }
            }
        } else {
            ingresarValoresFactura();
        }
        liquidacionDetalle.setFactura(factura);
        liquidacionDetalle.setRubro(cuentaContableRetencion.getRetencion());
        liquidacionDetalle.setCuentaContableRetencion(cuentaContableRetencion);
        liquidacionDetalle.setId(Utils.idTemp());
        liquidacionDetalle.setImpuesto(impuesto);
        liquidacionDetalle.setCantidad(1);
        liquidacionDetalle.setBaseImponible(baseImponible.setScale(2));
        liquidacionDetalle.setEstado(Boolean.TRUE);
        liquidacionDetalle.setValor(valorTotal);
        liquidacionDetalles.add(liquidacionDetalle);
        liquidacionDetalle = new LiquidacionDetalle();
        valorTotal = BigDecimal.ZERO;
        impuesto = BigDecimal.ZERO;
        baseImponible = BigDecimal.ZERO;
        activarBtnDetalle = Boolean.FALSE;
        removerDetalleTransaccionRetenciones();
        detalleCompras = new DetalleCompras();
        cuentaContableRetencion = new CuentaContableRetencion();
        JsfUtil.update("rubrosCuenta");
        JsfUtil.update("sustentoComp");
    }

    private void removerDetalleTransaccionRetenciones() {
        for (DetalleTransaccion d : detalleTransaccionesSeleccionados) {
            detalleTransacciones.remove(d);
        }
        detalleTransaccionesSeleccionados = new ArrayList<>();
        JsfUtil.update("dataTableDetallesCuentas");
    }

    public void removerDetalle(LiquidacionDetalle detalle) {
        liquidacionDetalles.remove(detalle);
        PrimeFaces.current().ajax().update("detalleRetencion");
    }

    public void ingresarValoresFactura() {
        switch ((int) Math.round(cuentaContableRetencion.getRetencion().getValor())) {
            case 10:
                factura.setRetencionIva10(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetBien10(valorTotal);
                });
                break;
            case 20:
                factura.setRetencionIva20(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ20(valorTotal);
                });
                break;
            case 30:
                factura.setRetencionIva30(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValorRetBienes(valorTotal);
                });
                break;
            case 50:
                factura.setRetencionIva50(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ50(valorTotal);
                });
                break;
            case 70:
                factura.setRetencionIva70(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValorRetServicios(totalRetencion);
                });
                break;
            case 100:
                factura.setRetencionIva100(valorTotal);
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ100(valorTotal);
                });
                break;
        }
    }

    public void selectData(SelectEvent evt) {
        liquidacion.setSolicitante((Proveedor) evt.getObject());
        liquidacion.setIdentificacionProveedor(((Proveedor) evt.getObject()).getCliente().getIdentificacion());
        PrimeFaces.current().ajax().update("fldInfoComprobante");
    }

    public void selectDataDiarioGeneral(SelectEvent evt) {
        diarioGeneral = (DiarioGeneral) evt.getObject();
        setPeriodoDiarioRetencion();
        cargarDatosRetenciones();
    }

    public void cargarCuentaRubro() {
        /*if (detalleTransaccionSeleccionado != null) {
            cuentaContableRetenciones = cuentaContableRetencionService.findAllByCuentaContable(detalleTransaccionSeleccionado.getCuentaContable());
            JsfUtil.addInformationMessage("", "Cuenta " + detalleTransaccionSeleccionado.getCuentaContable().getCodigo() + " Seleccionada");
        }*/
        if (!detalleTransaccionesSeleccionados.isEmpty()) {
            List<CuentaContableRetencion> newElement = new ArrayList<>();
            cuentaContableRetenciones.clear();
            totalCuentasValor = BigDecimal.ZERO;
            for (DetalleTransaccion d : detalleTransaccionesSeleccionados) {
                List<CuentaContableRetencion> cuentaRetencionesDB = null;// cuentaContableRetencionService.findAllByCuentaContable(d.getCuentaContable());
                if (!cuentaRetencionesDB.isEmpty()) {
                    totalCuentasValor = totalCuentasValor.add(d.getHaber());
                    newElement.addAll(cuentaRetencionesDB);
                }
            }
            if (!isEqualsType(newElement)) {
                cuentaContableRetenciones.clear();
                totalCuentasValor = BigDecimal.ZERO;
                JsfUtil.addErrorMessage("", "Las cuentas seleccionadas no son del mismo tipo");
                return;
            }
            rubrosCuentasRetencionesSinRepetidos(newElement);
        }
    }

    public Boolean isEqualsType(List<CuentaContableRetencion> cuentaRetencionesDB) {
        if (detalleTransaccionesSeleccionados.size() == 1) {
            return true;
        }
        CuentaContableRetencion type = cuentaRetencionesDB.get(0);
        for (int i = 1; i < cuentaRetencionesDB.size(); i++) {
            if (!type.getRetencion().getRubroTipo().getDescripcion().equals(cuentaRetencionesDB.get(i).getRetencion().getRubroTipo().getDescripcion())) {
                return false;
            }
        }
        return true;
    }

    private void rubrosCuentasRetencionesSinRepetidos(List<CuentaContableRetencion> newElement) {
        if (!newElement.isEmpty()) {
            HashMap<Integer, CuentaContableRetencion> map = new HashMap<>();
            for (CuentaContableRetencion repit : newElement) {
                map.put(repit.getRetencion().getId(), repit);
            }
            for (Entry<Integer, CuentaContableRetencion> entry : map.entrySet()) {
                cuentaContableRetenciones.add(entry.getValue());
            }
        }
    }

    public void cargarRetencionValor() {
        if (cuentaContableRetencion != null && cuentaContableRetencion.getId() != null) {
            if (factura.getId() == null) {
                JsfUtil.addErrorMessage("", "Debe seleccionar una factura");
                return;
            }
            if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("RENTAS")) {
                baseImponible = factura.getSubtotalRenta();
                impuesto = cuentaContableRetencion.getRetencion().getPorcentaje();
            } else if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("IVA")) {
                baseImponible = factura.getMontoIva();
                impuesto = cuentaContableRetencion.getRetencion().getPorcentajeRetencion();
            }
            calcularValorRetencion();
        }
    }

    public void cargarDatosRetenciones() {
        if (liquidacionService.findLiquidacionByDiarioGeneral(diarioGeneral) == null) {
//            liquidacion.setDiarioGeneral(diarioGeneral);
            liquidacion.setFechaEmision(diarioGeneral.getFechaElaboracion());
            liquidacion.setFechaEmisionCabecera(diarioGeneral.getFechaElaboracion());
            diarioGeneralClone = Utils.clone(diarioGeneral);
            setPeriodoFiscal();
            setPeriodoDiarioRetencion();
            proveedorExiste();
            existeFacturas();
            detalleCuentasRetencion();
        } else {
            JsfUtil.addWarningMessage("", "Ya existe una retencion para el Diario General " + diarioGeneral.getNumeroTransaccion());
            loadModel();
            JsfUtil.update("formMain");
        }
    }

    private void proveedorExiste() {
        if (diarioGeneral.getCertificacionesPresupuestario() != null) {
            if (diarioGeneral.getCertificacionesPresupuestario().getTipoBeneficiario() != null
                    && diarioGeneral.getCertificacionesPresupuestario().getTipoBeneficiario()
                    && diarioGeneral.getCertificacionesPresupuestario().getBeneficiario() != null) {
                liquidacion.setSolicitante(proveedorService.findByIdentificacion(diarioGeneral.getCertificacionesPresupuestario().getBeneficiario().getIdentificacion()));
                //liquidacion.setSolicitanteAuxiliar(liquidacion.getSolicitante());
                liquidacion.setIdentificacionProveedor(liquidacion.getSolicitante().getCliente().getIdentificacion());
            } else {
                consultarProveedorByDiarioGeneral();
            }
        } else {
            consultarProveedorByDiarioGeneral();
        }
        PrimeFaces.current().ajax().update("fldInfoComprobante");
    }

    private void consultarProveedorByDiarioGeneral() {
        if (diarioGeneral.getBeneficiario() != null) {
            Proveedor p = proveedorService.findByProveedorByCliente(diarioGeneral.getBeneficiario());
            if (p != null) {
                liquidacion.setSolicitante(p);
                liquidacion.setIdentificacionProveedor(liquidacion.getSolicitante().getCliente().getIdentificacion());
            } else {
                JsfUtil.addInformationMessage("", "No existe datos de proveedor");
                limpiarProveedor();
            }
        } else {
            JsfUtil.addInformationMessage("", "No existe datos de proveedor");
            limpiarProveedor();
        }
    }

    public void limpiarProveedor() {
        liquidacion.setSolicitante(null);
        liquidacion.setIdentificacionProveedor("");
    }

    public void existeFacturas() {
        if (diarioGeneral != null && diarioGeneral.getCertificacionesPresupuestario() != null) {
            facturas = liquidacionService.findAllFacturasByDiarioGeneralBySolicitud(diarioGeneral);
            List<Factura> facturasBienes = liquidacionService.findAllFacturaByDiarioGeneralForBienes(diarioGeneral);
            if (facturas.isEmpty() && facturasBienes.isEmpty()) {
                JsfUtil.addWarningMessage("", "No existen facturas Registradas");
                facturas = new ArrayList<>();
            } else {
                newFacturasDiarioGeneral(facturas, facturasBienes);
            }
        }
        PrimeFaces.current().ajax().update("fdInfoDocumentoRetencion");
    }

    public void newFacturasDiarioGeneral(List<Factura> facturasInventario, List<Factura> facturaBienes) {
        List<Factura> facturasAux = new ArrayList<>();
        if (!facturasInventario.isEmpty() && !facturaBienes.isEmpty()) {
            facturasInventario.forEach((f) -> {
                facturasAux.add(f);
            });
            facturaBienes.forEach((fa) -> {
                facturasAux.add(fa);
            });
            facturas = facturasAux;
        } else if (facturasInventario.isEmpty() && !facturaBienes.isEmpty()) {
            facturaBienes.forEach((fa) -> {
                facturasAux.add(fa);
            });
            facturas = facturasAux;
        }
    }

    public void facturaSeleccionadaDataTable() {
        if (facturaSeleccionada != null) {
            facturaSeleccionada.setFacturaFormasPago(facturaFormasPagoService.findAllFormaPagoByFactura(facturaSeleccionada));
            if (validarDatosPagoFactura(facturaSeleccionada, liquidacion)) {
                factura = facturaSeleccionada;
                air.setDetalleAir(new ArrayList<>());
                liquidacion.getAts().getCompras().setDetalleCompras(new ArrayList<>());
                detalleCompras = createDetalleCompras(factura, liquidacion, diarioGeneral, cajero);
                detalleCompras.setFormasDePago(new FormasPagoModel());
                detalleCompras.getFormasDePago().setFormaPago(new ArrayList<>());
                for (FacturaFormasPago p : factura.getFacturaFormasPago()) {
                    detalleCompras.getFormasDePago().getFormaPago().add(p.getFormaPago().getCodigo());
                }
                detalleCompras.setPagoExterior(new PagoExteriorModel(factura.getPagoExterior().getPagoLocExt(), factura.getPagoExterior().getTipoRegi(),
                        factura.getPagoExterior().getDenopagoRegFis(), factura.getPagoExterior().getPaisEfecPagoGen(), factura.getPagoExterior().getPaisEfecPagoParFis(),
                        factura.getPagoExterior().getDenopago(), factura.getPagoExterior().getPaisEfecPago(), factura.getPagoExterior().getAplicConvDobTrib(),
                        factura.getPagoExterior().getPagExtSujRetNorLeg()));
                liquidacion.getAts().getCompras().getDetalleCompras().add(detalleCompras);
                cargarRetencionValor();
                JsfUtil.addInformationMessage("", "Factura N° " + factura.getNumFactura() + " Seleccionada");
            } else {
                facturaSeleccionada = new Factura();
                baseImponible = BigDecimal.ZERO.setScale(2);
                JsfUtil.update("dataTableFacturas");
            }
        }
    }

    public void eliminarFactura(Factura f) {
        facturas.remove(f);
        PrimeFaces.current().ajax().update("dataTableFacturas");
    }

    public void formFactura(Factura f) {
        if (f != null) {
            renderTabFactura = Boolean.FALSE;
            factura = f;
        } else {
            renderTabFactura = Boolean.TRUE;
            factura = new Factura();
        }
        disabledAjaxSelection = Boolean.FALSE;
        PrimeFaces.current().ajax().update("formComprobante");
        PrimeFaces.current().executeScript("PF('dialogComprobante').show()");
    }

    public void aggFactura() {
        Boolean edit = factura.getId() != null;
        if (liquidacion.getSolicitante() == null || liquidacion.getSolicitante().getId() == null) {
            JsfUtil.addErrorMessage("", "Es necesario ingresar un proveedor");
            return;
        }
        if (factura.getEntidad() != null && factura.getEstablecimiento() != null && factura.getSecuencia() != null) {
            factura.setNumFactura(factura.getEntidad() + "-" + factura.getEstablecimiento() + "-" + factura.getSecuencia());
        } else {
            JsfUtil.addErrorMessage("", "Debe ingresar un número de factura completo");
            return;
        }
        if (factura.getNumAutorizacion().length() == 10 || factura.getNumAutorizacion().length() == 37
                || factura.getNumAutorizacion().length() == 49) {
        } else {
            JsfUtil.addErrorMessage("", "El número de autorización debe ser 10, 37 ó 49");
            return;
        }
        if (edit) {
            facturaService.edit(factura);
        } else {
            List<Factura> findFactura = facturaService.findFacturaByNumeroComprobante(factura.getNumFactura(), factura.getFacturaElectronica());
            if (!findFactura.isEmpty()) {
                JsfUtil.addErrorMessage("", "Ya Existe una Factura registrada con el código " + factura.getNumFactura());
                return;
            }
            factura.setEstado(Boolean.TRUE);
            factura.setProveedor(liquidacion.getSolicitante());
            factura = facturaService.create(factura);
            facturas.add(factura);
        }
        JsfUtil.addSuccessMessage("", "Factura N° " + (edit ? "Editado " : "Agregado ") + factura.getNumFactura() + " correctamente");
        factura = new Factura();
        PrimeFaces.current().ajax().update("dataTableFacturas");
        PrimeFaces.current().executeScript("PF('dialogComprobante').hide()");
    }

    public void detalleCuentasRetencion() {
//        detalleTransacciones = liquidacionService.findAllRetencionDiarioGeneral(diarioGeneral);
        if (detalleTransacciones.isEmpty()) {
            JsfUtil.addWarningMessage("", "No existen detalles retenidos");
        }
        PrimeFaces.current().ajax().update("dataTableDetallesCuentas");
    }

    public void cancelar() {
        servletSession.borrarParametros();
        loadModel();
    }

    public void agregarValoresIVAFactura(Factura f) {
        factura = f;
        actualizarSubtotalFactura();
        JsfUtil.update("outputBase");
        JsfUtil.executeJS("PF('dialogDetalleBases').show()");
    }

    public void guardarValoresIVAFactura() {
        facturaService.edit(factura);
        factura = new Factura();
        facturaSeleccionada = new Factura();
        JsfUtil.update("dataTableFacturas");
        JsfUtil.executeJS("PF('dialogDetalleBases').hide()");
    }

    public void loadDatosPago() {
        if (factura.getPagoExterior().getPagoLocExt() != null
                && !factura.getPagoExterior().getPagoLocExt().isEmpty()) {
            switch (factura.getPagoExterior().getPagoLocExt()) {
                case "01":
                    disabledPago = Boolean.TRUE;
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.TRUE;
                    factura.getPagoExterior().setTipoRegi(null);
                    factura.getPagoExterior().setPaisEfecPago(null);
                    factura.getPagoExterior().setPaisEfecPagoGen(null);
                    factura.getPagoExterior().setPaisEfecPagoParFis(null);
                    factura.getPagoExterior().setAplicConvDobTrib(null);
                    factura.getPagoExterior().setPagExtSujRetNorLeg(null);
                    break;
                case "02":
                    disabledPago = Boolean.FALSE;
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.TRUE;
                    break;
            }
        }
    }

    public void loadDatosRegimenExterior() {
        if (factura.getPagoExterior().getTipoRegi() != null && !factura.getPagoExterior().getTipoRegi().isEmpty()) {
            switch (factura.getPagoExterior().getTipoRegi()) {
                case "01":
                    disabledRegimenGeneral = Boolean.FALSE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.TRUE;
                    break;
                case "02":
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.FALSE;
                    disabledDenominacion = Boolean.TRUE;
                    break;
                case "03":
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.FALSE;
                    break;
            }
        }
    }

    public void desplegarDialogDatosPago(Factura f) {
        factura = f;
        if (factura.getPagoExterior() == null) {
            factura.setPagoExterior(new PagoExterior());
        }
        if (factura.getFacturaFormasPago() == null || factura.getFacturaFormasPago().isEmpty()) {
            factura.setFacturaFormasPago(new ArrayList<>());
            facturaFormasPago = new ArrayList<>();
        } else {
            facturaFormasPago = new ArrayList<>();
            for (FacturaFormasPago p : factura.getFacturaFormasPago()) {
                p.setId(Utils.idTemp());
                facturaFormasPago.add(p);
            }
        }
        loadDatosPago();
        loadDatosRegimenExterior();
        JsfUtil.update("outputDatosPagos");
        JsfUtil.executeJS("PF('dialogDetallePagos').show()");
    }

    public void guardarDatosPago() {
        //AQUI TENDRIA QUE HACER PARA GUARDAR LOS DATOS DE PAGO
        Boolean editPago = factura.getPagoExterior().getId() != null;
        createEditPago(editPago);
        createFormasPago();
        JsfUtil.addSuccessMessage("", "Datos de pagos " + (editPago ? " actualizado " : " generado ") + "con éxito");
        factura = new Factura();
        facturaSeleccionada = new Factura();
        JsfUtil.update("dataTableFacturas");
        JsfUtil.executeJS("PF('dialogDetallePagos').hide()");
    }

    public void createFormasPago() {
        if (facturaFormasPago.isEmpty()) {
            JsfUtil.addSuccessMessage("", "Debe seleccionar al menos un método de pago");
            return;
        } else {
            //eliminar facturas creadas anteriormente
            for (FacturaFormasPago p : factura.getFacturaFormasPago()) {
                FacturaFormasPago pagoDB = facturaFormasPagoService.findFacturaFormaPagobyFacturAndPago(p.getFactura(), p.getFormaPago());
                facturaFormasPagoService.remove(pagoDB);
            }
            for (FacturaFormasPago pago : facturaFormasPago) {
                pago.setId(null);
                facturaFormasPagoService.create(pago);
            }
        }
    }

    public void aggFormasPago() {
        if (formaPagoSelection != null && formaPagoSelection.getId() != null) {
            FacturaFormasPago facturaFormaPago = new FacturaFormasPago(factura, formaPagoSelection);
            if (!facturaFormasPago.isEmpty()) {
                for (FacturaFormasPago pago : facturaFormasPago) {
                    if (facturaFormaPago.getFormaPago().getCodigo().equals(pago.getFormaPago().getCodigo())) {
                        JsfUtil.addErrorMessage("", "Ya selecciono el método de Pago");
                        return;
                    }
                }
            }
            facturaFormaPago.setId(Utils.idTemp());
            facturaFormasPago.add(facturaFormaPago);
            JsfUtil.addSuccessMessage("", facturaFormaPago.getFormaPago().getDescripcion() + " agregado con éxito");
            formaPagoSelection = new FormaPago();
        }
    }

    public void createEditPago(Boolean editPago) {
        if (editPago) {
            pagoExteriorService.edit(factura.getPagoExterior());
        } else {
            pagoExteriorService.create(factura.getPagoExterior());
            factura.setPagoExterior(factura.getPagoExterior());
            facturaService.edit(factura);
        }
    }

    public void eliminarFormasPago(FacturaFormasPago pago) {
        facturaFormasPago.remove(pago);
        JsfUtil.addSuccessMessage("", "Pago eliminado con éxito");
        JsfUtil.update("dataTableFormasPago");
    }

    public void calculoIvaDiferenteCero() {
        if (factura.getBaseImponibleDiferente() != null) {
            factura.setMontoIva((factura.getBaseImponibleDiferente() == null ? BigDecimal.ZERO : factura.getBaseImponibleDiferente())
                    .multiply(new BigDecimal(rubroBaseImponibleDifCero.getValor()))
                    .divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP));
        }
        actualizarSubtotalFactura();
    }

    public void calculoBaseImponible() {
        if (factura.getMontoIva() != null && !factura.getMontoIva().equals(BigDecimal.ZERO.setScale(2))) {
            if (factura.getRetencionIva10() != null && !factura.getRetencionIva10().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva10(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA10.getValor() == null ? 0 : rubrosRetencionesIVA10.getValor())));
            }
            if (factura.getRetencionIva20() != null && !factura.getRetencionIva20().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva20(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA20.getValor() == null ? 0 : rubrosRetencionesIVA20.getValor())));
            }
            if (factura.getRetencionIva30() != null && !factura.getRetencionIva30().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva30(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA30.getValor() == null ? 0 : rubrosRetencionesIVA30.getValor())));
            }
            if (factura.getRetencionIva50() != null && !factura.getRetencionIva50().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva50(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA50.getValor() == null ? 0 : rubrosRetencionesIVA50.getValor())));
            }
            if (factura.getRetencionIva50() != null && !factura.getRetencionIva70().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva70(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA70.getValor() == null ? 0 : rubrosRetencionesIVA70.getValor())));
            }
            if (factura.getRetencionIva100() != null && !factura.getRetencionIva100().equals(BigDecimal.ZERO.setScale(2))) {
                comparacionValor(factura.getRetencionIva100(), calculoValor(factura.getMontoIva(),
                        BigDecimal.valueOf(rubrosRetencionesIVA100.getValor() == null ? 0 : rubrosRetencionesIVA100.getValor())));
            }
        } else {
            JsfUtil.addErrorMessage("", "Error la sumatoria de las retenciones no puede superar al del monto IVA");
        }
    }

    public BigDecimal calculoValor(BigDecimal montoIva, BigDecimal porcentaje) {
        valorPorcentaje = porcentaje;
        return montoIva.multiply(porcentaje)
                .divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
    }

    public void comparacionValor(BigDecimal retencion, BigDecimal valor) {
        if (!retencion.equals(valor)) {
            actualizacionMessagesConError();
        } else {
            actualizacionMessagesSinError();
        }
    }

    public void actualizacionMessagesSinError() {
        disabledguardarBases = Boolean.FALSE;
        JsfUtil.update("guardar-bases");
    }

    public void actualizacionMessagesConError() {
        JsfUtil.addErrorMessage("", "El valor ingresado " + valorPorcentaje + " no es igual al calculado por el sistema");
        disabledguardarBases = Boolean.TRUE;
        JsfUtil.update("guardar-bases");
    }

    public void aggFacturaSeleccionada(Factura f) {
        if (!facturas.isEmpty()) {
            if (facturas.contains(f)) {
                JsfUtil.addErrorMessage("", "La factura N° " + f.getNumFactura() + " ya ha sido agregada");
                return;
            }
            List<Factura> facturaAux = facturas;
            facturas = new ArrayList<>();
            facturaAux.add(f);
            facturas = facturaAux;
        } else {
            facturas.add(f);
        }
        JsfUtil.executeJS("PF('dialogComprobante').hide()");
        JsfUtil.addInformationMessage("", "Factura agregada con éxito");
    }

    public void actualizarSubtotalFactura() {
        if (factura != null) {
            factura.setSubtotalRenta((factura.getBaseImponibleIva() == null ? BigDecimal.ZERO : factura.getBaseImponibleIva())
                    .add(factura.getBaseImponibleDiferente() == null ? BigDecimal.ZERO : factura.getBaseImponibleDiferente()));
        } else {
            factura.setSubtotalRenta(BigDecimal.ZERO);
        }
    }

    public void formatNumeroComprobante() {
        if (factura.getEntidad() != null && !factura.getEntidad().isEmpty()) {
            factura.setEntidad(StringUtils.leftPad(factura.getEntidad(), 3, "0"));
        }
        if (factura.getEstablecimiento() != null && !factura.getEstablecimiento().isEmpty()) {
            factura.setEstablecimiento(StringUtils.leftPad(factura.getEstablecimiento(), 3, "0"));
        }
        if (factura.getSecuencia() != null && !factura.getSecuencia().isEmpty()) {
            factura.setSecuencia(StringUtils.leftPad(factura.getSecuencia(), 9, "0"));
        }
    }

    public IVA createAts(Liquidacion liquidacion, Cajero cajero) {
        return new IVA("R", cajero.getEntidad().getRucEntidad(), cajero.getEntidad().getNombreEntidad(), liquidacion.getAnio(),
                String.format("%02d", liquidacion.getMes()),
                cajero.getEntidad().getEstablecimiento(), BigDecimal.ZERO.setScale(2), "IVA");
    }

    public DetalleCompras createDetalleCompras(Factura factura, Liquidacion liquidacion,
            DiarioGeneral diarioGeneral, Cajero cajero) {
        DetalleCompras detalleComprasLocal = new DetalleCompras();
        String[] code = factura.getNumFactura().split("-");
        detalleComprasLocal.setIdProv(liquidacion.getIdentificacionProveedor());
        detalleComprasLocal.setTipoComprobante(liquidacion.getComprobanteModifica().getCodigo());
        detalleComprasLocal.setFechaRegistro(format.format(diarioGeneral.getFechaElaboracion()));
        detalleComprasLocal.setEstablecimiento(code[0]);
        detalleComprasLocal.setPuntoEmision(code[1]);
        detalleComprasLocal.setSecuencial(code[2]);
        detalleComprasLocal.setFechaEmision(format.format(factura.getFechaFactura()));
        detalleComprasLocal.setAutorizacion(factura.getNumAutorizacion());
        detalleComprasLocal.setBaseImponible(factura.getBaseImponibleIva().setScale(2, RoundingMode.HALF_UP)); // 0% AL IVA
        detalleComprasLocal.setBaseImpGrav(factura.getBaseImponibleDiferente().setScale(2, RoundingMode.HALF_UP)); // esta es la de diferente de 0% al IVA
        detalleComprasLocal.setMontoIce(factura.getMontoIce().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setMontoIva(factura.getMontoIva().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValRetBien10(factura.getRetencionIva10().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValRetServ20(factura.getRetencionIva20().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValorRetBienes(factura.getRetencionIva30().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValRetServ50(factura.getRetencionIva50().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValorRetServicios(factura.getRetencionIva70().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setValRetServ100(factura.getRetencionIva100().setScale(2, RoundingMode.HALF_UP));
        detalleComprasLocal.setTotbasesImpReemb(BigDecimal.ZERO.setScale(2));
        detalleComprasLocal.setNumFactura(factura.getNumFactura());
        detalleComprasLocal.setEstabRetencion1(cajero.getEntidad().getEstablecimiento());
        detalleComprasLocal.setPtoEmiRetencion1(cajero.getPuntoEmision());
        detalleComprasLocal.setFechaEmiRet1(format.format(new Date()));
        return detalleComprasLocal;
    }

    public Boolean validarDatosPagoFactura(Factura f, Liquidacion l) {
        if (f.getFacturaFormasPago().isEmpty()) {
            JsfUtil.addErrorMessage("", "Necesita seleccionar al menos una forma de pago");
            return false;
        }
        if (f.getPagoExterior().getId() == null) {
            JsfUtil.addErrorMessage("", "Error en datos de pago");
            return false;
        }
        if (l.getComprobanteModifica() == null || l.getComprobanteModifica().getId() == null) {
            JsfUtil.addErrorMessage("", "Necesita seleccionar el tipo de comprobante");
            return false;
        }
        if (l.getTipoEmision() == null || l.getTipoEmision().isEmpty()) {
            JsfUtil.addErrorMessage("", "Necesita seleccionar el tipo de emisión");
            return false;
        }
        if (l.getDiarioGeneral() == null) {
            JsfUtil.addErrorMessage("", "Necesita un Diario General");
            return false;
        }
        return true;
    }

    public Boolean validarCamposRetencion(BigDecimal baseImponible, BigDecimal impuesto, DetalleTransaccion detalleTransaccionSeleccionado,
            CuentaContableRetencion cuentaContableRetencion) {
        if (detalleTransaccionSeleccionado == null) {
            JsfUtil.addErrorMessage("", "Escoja una cuenta");
            return false;
        }
        if (cuentaContableRetencion == null) {
            JsfUtil.addErrorMessage("", "Seleccione un rubro de cuenta contable");
            return false;
        }
        if (baseImponible == null) {
            JsfUtil.addErrorMessage("", "Ingrese una base Imponible ");
            return false;
        }
        if (impuesto == null) {
            JsfUtil.addErrorMessage("", "Necesita un % Retención");
            return false;
        }

        return true;
    }

    public ComprobanteElectronico initComprobanteElectronico(Liquidacion liquidacion, Cajero cajero) {
        ComprobanteElectronico comprobanteElectronicoLocal = new ComprobanteElectronico();
        comprobanteElectronicoLocal.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronicoLocal.setNumComprobante(liquidacion.getNumeroComprobante());
        comprobanteElectronicoLocal.setAmbiente(ComprobantesCode.AMBIENTE);
        comprobanteElectronicoLocal.setIsOnline(ComprobantesCode.ONLINE);
        comprobanteElectronicoLocal.setPuntoEmision(cajero.getPuntoEmision());
        comprobanteElectronicoLocal.setRucEntidad(cajero.getEntidad().getRucEntidad());
        comprobanteElectronicoLocal.setGeneraLiquidacion(Boolean.FALSE);
        comprobanteElectronicoLocal.setFechaEmisionDocumentoModifica(format.format(liquidacion.getFechaEmision()));
        //INICIO - CABECERA
        comprobanteElectronicoLocal.setCabecera(loadCabecera(liquidacion.getFechaEmisionCabecera(), liquidacion.getSolicitante()));
        //FIN - CABECERA
        return comprobanteElectronicoLocal;
    }

    public Cabecera loadCabecera(Date fechaCreacion, Proveedor proveedor) {
        Cabecera cabecera = new Cabecera();
        cabecera.setCedulaRuc(proveedor.getCliente().getIdentificacionCompleta());
        System.out.println("Cedula Proveedor: " + cabecera.getCedulaRuc());
        cabecera.setPropietario(proveedor.getCliente().getNombre());
        cabecera.setFechaEmision(new SimpleDateFormat("yyyy-MM-dd").format(fechaCreacion));
        cabecera.setDireccion(proveedor.getCliente().getDireccion());
        cabecera.setCorreo(proveedor.getCliente().getEmail());
        cabecera.setTelefono(proveedor.getCliente().getTelefono());
        if (proveedor.getCliente().getTipoIdentificacion().getId().equals(12L)) {
            cabecera.setEsPasaporte(Boolean.TRUE);
        } else {
            cabecera.setEsPasaporte(Boolean.FALSE);
        }
        return cabecera;
    }

    public Liquidacion initLiquidacion(Liquidacion liquidacion) {
        liquidacion.setTipoLiquidacion(new LiquidacionTipo(2));
        liquidacion.setBeneficiario(liquidacion.getSolicitante());
        liquidacion.setEstadoLiquidacion(catalogoItemService.getCatalogoI("estado_liquidacion", "ingresada"));
        liquidacion.setEstadoPago(catalogoItemService.getCatalogoI("estado_pago", "cancelado"));
        liquidacion.setEstado(Boolean.TRUE);
        liquidacion.setSubTotal(BigDecimal.ZERO);
        liquidacion.setDescuentoValor(BigDecimal.ZERO);
        liquidacion.setRideEnviado(Boolean.FALSE);
        liquidacion.setGeneraFactura(Boolean.TRUE);
        liquidacion.setPesoTramite(0);
        liquidacion.setTramiteOnline(Boolean.FALSE);
        liquidacion.setUserCreador(usuarioService.findByUsuario(userSession.getNameUser()).getId());
        liquidacion.setFechaIngreso(new Date());
        liquidacion.setFechaCreacion(new Date());
        return liquidacion;
    }

    public Boolean validarRetencion(Liquidacion liquidacion, List<Factura> facturas, List<LiquidacionDetalle> detalleRetencion) {
        if (liquidacion.getComprobanteModifica() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un Comprobante", "");
            return false;
        }
        if (liquidacion.getSolicitante() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un Sujeto Retenido", "");
            return false;
        }
        if (facturas.isEmpty()) {
            JsfUtil.addErrorMessage("Debe agregar al menos una factura", "");
            return false;
        }
        if (liquidacion.getDiarioGeneral() == null) {
            JsfUtil.addErrorMessage("", "Se debe asignar un Diario General");
            return false;
        }
        if (detalleRetencion.isEmpty()) {
            JsfUtil.addErrorMessage("", "No existen detalles de retenciones");
            return false;
        }
        if (liquidacion.getTipoEmision().isEmpty()) {
            JsfUtil.addErrorMessage("", "Seleccione el tipo de emisión del comprobante");
            return false;
        }
        return true;
    }

    public Liquidacion inicializarLiquidacionUpdate(Liquidacion liquidacion) {
        return new Liquidacion(liquidacion.getTipoLiquidacion(), liquidacion.getSolicitante(), liquidacion.getBeneficiario(),
                liquidacion.getEstadoPago(), liquidacion.getEstadoLiquidacion(), liquidacion.getNumeroComprobante(),
                liquidacion.getCodigoComprobante(),
                liquidacion.getSubTotal(), liquidacion.getTotalPagar(), liquidacion.getDescuentoValor(),
                liquidacion.getAnio(), liquidacion.getMes(), liquidacion.getPeriodo(), liquidacion.getRideEnviado(),
                liquidacion.getGeneraFactura(), liquidacion.getTramiteOnline(), liquidacion.getReingreso(),
                liquidacion.getIngresado(), liquidacion.getPesoTramite(), liquidacion.getTipoEmision(), liquidacion.getConsideraIva(),
                liquidacion.getComprobante(),
                liquidacion.getComprobanteModifica(), liquidacion.getDiarioGeneral());
    }

    public LiquidacionDetalle inicializarLiquidacionDetalleUpdate(LiquidacionDetalle detalle) {
        return new LiquidacionDetalle(Utils.idTemp(), detalle.getLiquidacion(), detalle.getRubro(), detalle.getCantidad(),
                detalle.getBaseImponible(), detalle.getImpuesto(),
                detalle.getIce(), detalle.getValor(), detalle.getValorRecaudado(), detalle.getFactura(),
                detalle.getCuentaContableRetencion());
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<DetalleTransaccion> getDetalleTransaccionesSeleccionados() {
        return detalleTransaccionesSeleccionados;
    }

    public void setDetalleTransaccionesSeleccionados(List<DetalleTransaccion> detalleTransaccionesSeleccionados) {
        this.detalleTransaccionesSeleccionados = detalleTransaccionesSeleccionados;
    }

    public Boolean getRenderTabFactura() {
        return renderTabFactura;
    }

    public void setRenderTabFactura(Boolean renderTabFactura) {
        this.renderTabFactura = renderTabFactura;
    }

    public FormaPago getFormaPagoSelection() {
        return formaPagoSelection;
    }

    public void setFormaPagoSelection(FormaPago formaPagoSelection) {
        this.formaPagoSelection = formaPagoSelection;
    }

    public List<FacturaFormasPago> getFacturaFormasPago() {
        return facturaFormasPago;
    }

    public void setFacturaFormasPago(List<FacturaFormasPago> facturaFormasPago) {
        this.facturaFormasPago = facturaFormasPago;
    }

    public List<FormaPago> getFormasPagos() {
        return formasPagos;
    }

    public void setFormasPagos(List<FormaPago> formasPagos) {
        this.formasPagos = formasPagos;
    }

    public LazyModel<Factura> getFacturasLazyRegis() {
        return facturasLazyRegis;
    }

    public void setFacturasLazyRegis(LazyModel<Factura> facturasLazyRegis) {
        this.facturasLazyRegis = facturasLazyRegis;
    }

    public Boolean getDisabledguardarBases() {
        return disabledguardarBases;
    }

    public void setDisabledguardarBases(Boolean disabledguardarBases) {
        this.disabledguardarBases = disabledguardarBases;
    }

    public DetalleCompras getDetalleCompras() {
        return detalleCompras;
    }

    public void setDetalleComprasModel(DetalleCompras detalleComprasModel) {
        this.detalleCompras = detalleComprasModel;
    }

    public Boolean getDisabledRegimenGeneral() {
        return disabledRegimenGeneral;
    }

    public void setDisabledRegimenGeneral(Boolean disabledRegimenGeneral) {
        this.disabledRegimenGeneral = disabledRegimenGeneral;
    }

    public Boolean getDisabledParaisoFiscal() {
        return disabledParaisoFiscal;
    }

    public void setDisabledParaisoFiscal(Boolean disabledParaisoFiscal) {
        this.disabledParaisoFiscal = disabledParaisoFiscal;
    }

    public Boolean getDisabledDenominacion() {
        return disabledDenominacion;
    }

    public void setDisabledDenominacion(Boolean disabledDenominacion) {
        this.disabledDenominacion = disabledDenominacion;
    }

    public List<PaisParaisoFiscal> getPaisParaisoFiscal() {
        return paisParaisoFiscal;
    }

    public void setPaisParaisoFiscal(List<PaisParaisoFiscal> paisParaisoFiscal) {
        this.paisParaisoFiscal = paisParaisoFiscal;
    }

    public Boolean getDisabledPago() {
        return disabledPago;
    }

    public void setDisabledPago(Boolean disabledPago) {
        this.disabledPago = disabledPago;
    }

    public List<PaisAts> getPaises() {
        return paises;
    }

    public void setPaises(List<PaisAts> paises) {
        this.paises = paises;
    }

    public List<SustentoComprobante> getSustentosComprobantes() {
        return sustentosComprobantes;
    }

    public void setSustentosComprobantes(List<SustentoComprobante> sustentosComprobantes) {
        this.sustentosComprobantes = sustentosComprobantes;
    }

    public Boolean getReadOnlyInput() {
        return readOnlyInput;
    }

    public void setReadOnlyInput(Boolean readOnlyInput) {
        this.readOnlyInput = readOnlyInput;
    }

    public Boolean getRenderedBotones() {
        return renderedBotones;
    }

    public void setRenderedBotones(Boolean renderedBotones) {
        this.renderedBotones = renderedBotones;
    }

    public Boolean getDisabledAjaxSelection() {
        return disabledAjaxSelection;
    }

    public void setDisabledAjaxSelection(Boolean disabledAjaxSelection) {
        this.disabledAjaxSelection = disabledAjaxSelection;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public CuentaContableRetencion getCuentaContableRetencion() {
        return cuentaContableRetencion;
    }

    public void setCuentaContableRetencion(CuentaContableRetencion cuentaContableRetencion) {
        this.cuentaContableRetencion = cuentaContableRetencion;
    }

    public DetalleTransaccion getDetalleTransaccionSeleccionado() {
        return detalleTransaccionSeleccionado;
    }

    public void setDetalleTransaccionSeleccionado(DetalleTransaccion detalleTransaccionSeleccionado) {
        this.detalleTransaccionSeleccionado = detalleTransaccionSeleccionado;
    }

    public List<CuentaContableRetencion> getCuentaContableRetenciones() {
        return cuentaContableRetenciones;
    }

    public void setCuentaContableRetenciones(List<CuentaContableRetencion> cuentaContableRetenciones) {
        this.cuentaContableRetenciones = cuentaContableRetenciones;
    }

    public List<DetalleTransaccion> getDetalleTransacciones() {
        return detalleTransacciones;
    }

    public void setDetalleTransacciones(List<DetalleTransaccion> detalleTransacciones) {
        this.detalleTransacciones = detalleTransacciones;
    }

    public Factura getFacturaSeleccionada() {
        return facturaSeleccionada;
    }

    public void setFacturaSeleccionada(Factura facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public Boolean getAccesoComprobante() {
        return accesoComprobante;
    }

    public void setAccesoComprobante(Boolean accesoComprobante) {
        this.accesoComprobante = accesoComprobante;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Liquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<Integer> getAnios() {
        return anios;
    }

    public void setAnios(List<Integer> anios) {
        this.anios = anios;
    }

    public Integer getAnioMax() {
        return anioMax;
    }

    public void setAnioMax(Integer anioMax) {
        this.anioMax = anioMax;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public Integer getMesActual() {
        return mesActual;
    }

    public void setMesActual(Integer mesActual) {
        this.mesActual = mesActual;
    }

    public List<Comprobantes> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<Comprobantes> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public List<RubroTipo> getRubroTipos() {
        return rubroTipos;
    }

    public void setRubroTipos(List<RubroTipo> rubroTipos) {
        this.rubroTipos = rubroTipos;
    }

    public RubroTipo getRubroTipo() {
        return rubroTipo;
    }

    public void setRubroTipo(RubroTipo rubroTipo) {
        this.rubroTipo = rubroTipo;
    }

    public LiquidacionDetalle getLiquidacionDetalle() {
        return liquidacionDetalle;
    }

    public void setLiquidacionDetalle(LiquidacionDetalle liquidacionDetalle) {
        this.liquidacionDetalle = liquidacionDetalle;
    }

    public List<LiquidacionDetalle> getLiquidacionDetalles() {
        return liquidacionDetalles;
    }

    public void setLiquidacionDetalles(List<LiquidacionDetalle> liquidacionDetalles) {
        this.liquidacionDetalles = liquidacionDetalles;
    }

    public List<Rubro> getRubrosRentas() {
        return rubrosRentas;
    }

    public void setRubrosRentas(List<Rubro> rubrosRentas) {
        this.rubrosRentas = rubrosRentas;
    }

    public List<Rubro> getRubrosIVA() {
        return rubrosIVA;
    }

    public void setRubrosIVA(List<Rubro> rubrosIVA) {
        this.rubrosIVA = rubrosIVA;
    }

    public Rubro getRubroRentas() {
        return rubroRentas;
    }

    public void setRubroRentas(Rubro rubroRentas) {
        this.rubroRentas = rubroRentas;
    }

    public Rubro getRubroIVA() {
        return rubroIVA;
    }

    public void setRubroIVA(Rubro rubroIVA) {
        this.rubroIVA = rubroIVA;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getActivarBtnDetalle() {
        return activarBtnDetalle;
    }

    public void setActivarBtnDetalle(Boolean activarBtnDetalle) {
        this.activarBtnDetalle = activarBtnDetalle;
    }

    public Boolean getEsRentas() {
        return esRentas;
    }

    public void setEsRentas(Boolean esRentas) {
        this.esRentas = esRentas;
    }
//</editor-fold>

}
