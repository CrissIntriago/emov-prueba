/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

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
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaDetalle;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CuentaContableRetencionService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.FormaPagoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RenFacturaDetalleService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import com.origami.sigef.tesoreria.service.RubroTipoService;
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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 * @author gutya
 */
@Named(value = "retencionView")
@ViewScoped
public class RetencionController extends AccesosComprobanteElectronico implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Inject">
    @Inject
    private PagoExteriorService pagoExteriorService;
    @Inject
    private PaisParaisoFiscalService paisParaisoFiscalService;
    @Inject
    private PaisAtsService paisAtsService;
    @Inject
    protected UserSession session;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ContRegistroContable contRegistroContableService;

    @Inject
    private ServidorService servidorService;
    @Inject
    private RubroTipoService rubroTipoService;
    @Inject
    private RenFacturaDetalleService renFacturaDetalleService;
    @Inject
    private RenFacturaService renFacturaService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private ContDiarioGeneralService contDiarioGeneralService;
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
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Objetos">
    private ContDiarioGeneral diarioGeneral;
    private ContDiarioGeneral diarioGeneralClone;

    private Boolean accesoComprobante;
    private Cajero cajero;
    private Cliente cliente;
    private RenFactura liquidacion;
    private RenFacturaDetalle liquidacionDetalle;
    private List<RenFacturaDetalle> liquidacionDetalles;
    private List<Integer> anios;
    private Integer anioMax;
    private List<String> meses;
    private Integer mesActual;
    private List<Comprobantes> comprobantes;
    private List<RubroTipo> rubroTipos;
    private RubroTipo rubroTipo;

    private Rubro rubroRentas, rubroIVA, rubroBaseImponibleDifCero, rubrosRetencionesIVA10, rubrosRetencionesIVA20,
            rubrosRetencionesIVA30, rubrosRetencionesIVA50, rubrosRetencionesIVA70, rubrosRetencionesIVA100;

    private BigDecimal baseImponible, impuesto, valorTotal, totalRetencion;

    private Boolean activarBtnDetalle, esRentas;

    private ComprobanteElectronico comprobanteElectronico;

    private SimpleDateFormat format;

    private List<Factura> facturasSeleccionadas;
    private List<Factura> facturasAgregadas;
    private List<Factura> facturas;
//    private Factura facturaSeleccionada;
    private Factura factura;
    private List<CuentaContableRetencion> cuentaContableRetenciones;
    private CuentaContableRetencion cuentaContableRetencion;
    private List<ContDiarioGeneralDetalle> detalleTransacciones;
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
    private SolicitudReservaCompromiso solicitud;
//</editor-fold>

    private List<ContDiarioGeneralDetalle> detalleTransaccionesSeleccionados;
    private BigDecimal totalCuentasValor;

    private Boolean disabledPeriodos;

    /*VALIDACION DE FECHAS DE PERIODO FISCAL .. ADVERTENCIA....... 
            * 1.- La fecha de diario general debe ser el menor o igual a la fecha del periodo fiscal de la retencion .... 
            * 2.- La fecha de la factura debe ser menor o igual a la fecha del diario general PILAAAAAAAAAAAAAAAS
     */
    @PostConstruct
    public void init() {
        loadModel();
        if (accesoComprobante && servletSession.getParametros() != null && !servletSession.getParametros().isEmpty()) {
            loadModelSession();
        }
    }

    public void loadModel() {
        disabledPeriodos = Boolean.FALSE;
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
//            facturaSeleccionada = new Factura();
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
            liquidacion = new RenFactura();
            liquidacion.setIdentificacionProveedor(null);
            liquidacion.setFechaEmision(new Date());

            diarioGeneral = new ContDiarioGeneral();
            facturas = new ArrayList<>();
            detalleTransacciones = new ArrayList<>();
            cuentaContableRetenciones = new ArrayList<>();
            cuentaContableRetencion = new CuentaContableRetencion();

            liquidacionDetalle = new RenFacturaDetalle();
            liquidacionDetalles = new ArrayList<>();
            liquidacion.setProveedor(new Proveedor());
            liquidacion.setComprobante(katalinaService.findComprobante(ComprobantesCode.COMPPROBANTERETENCION));

            comprobantes = katalinaService.findComprobantesRetienen();
            rubroTipos = rubroTipoService.findByTipo("RETENCION");
            liquidacion.setComprobanteModifica(comprobantes.get(0));
            liquidacion.setTipoEmision("ELECTRÓNICA");
            esRentas = Boolean.TRUE;
            activarBtnDetalle = Boolean.FALSE;
            sustentosComprobantes = sustentoComprobanteService.findAll();
            factura = new Factura();
            loadAniosMeses();
            loadATS();
        } else {
            accesoComprobante = Boolean.FALSE;
        }
    }

    public void loadModelSession() {
        disabledAjaxSelection = Boolean.FALSE;
        Map<String, Object> parametros = servletSession.getParametros();
        liquidacion = (RenFactura) parametros.get("liquidacion");
        editar = (Boolean) parametros.get("edit");

        liquidacion = renFacturaService.findById(liquidacion.getId());
        liquidacion.setIdentificacionProveedor(liquidacion.getProveedor().getCliente().getIdentificacionCompleta());
        diarioGeneral = liquidacion.getDiario();
        diarioGeneralClone = Utils.clone(diarioGeneral);
        detalleCuentasRetencion();
        if (!editar) {
            disabledPeriodos = Boolean.TRUE;
            bloquearOpciones();
            if (!liquidacion.getRenFacturaDetalleList().isEmpty()) {
                if (facturas == null) {
                    facturas = new ArrayList<>();
                }
                for (RenFacturaDetalle d : liquidacion.getRenFacturaDetalleList()) {
                    d.setRenFactura(liquidacion);
                    if (!facturas.contains(d.getFactura())) {
                        facturas.add(d.getFactura());
                    }
                    d.setFactura(d.getFactura());
                    factura = (d.getFactura() != null ? d.getFactura() : new Factura());
                    liquidacionDetalles.add(inicializarLiquidacionDetalleUpdate(d));
                }
            }
        } else {
            if (!liquidacion.getRenFacturaDetalleList().isEmpty()) {
                if (facturas == null) {
                    facturas = new ArrayList<>();
                }
                for (RenFacturaDetalle d : liquidacion.getRenFacturaDetalleList()) {
                    d.setRenFactura(liquidacion);
                    if (!facturas.contains(d.getFactura())) {
                        facturas.add(d.getFactura());
                    }
                }
                liquidacion.setRenFacturaDetalleList(new ArrayList<>());
            }
        }
        liquidacion = inicializarLiquidacionUpdate(liquidacion);
        loadATS();
    }

    private void bloquearOpciones() {
        renderedBotones = Boolean.FALSE;
        readOnlyInput = Boolean.TRUE;
        disabledAjaxSelection = Boolean.TRUE;
    }

    private void desbloquearOpciones() {
        renderedBotones = Boolean.TRUE;
        readOnlyInput = Boolean.FALSE;
        disabledAjaxSelection = Boolean.FALSE;
    }

    private void loadATS() {
        detalleCompras = new DetalleCompras();
        detalleCompras.setCodSustento(sustentosComprobantes.get(1).getCodigo());
        liquidacion.setAts(new IVA());
        air = new Air();
        factura.setPagoExterior(new PagoExterior());

        liquidacion.setAts(createAts(liquidacion, cajero));
        liquidacion.getAts().setCompras(new Compras());
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

    public void actualizarPeriodoFiscal() {
        if (!verificacionPeriodoFiscal(liquidacion)) {
            bloquearOpciones();
        } else {
            desbloquearOpciones();
        }
        JsfUtil.update("formMain");
    }

    public void setPeriodoFiscal() {
        liquidacion.setAnio(Utils.getAnio(liquidacion.getFechaEmision()));
        liquidacion.setMes(Utils.getMes(liquidacion.getFechaEmision()));
        liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
    }

    public void guardarEmitirRetencionLote(List<RenFacturaDetalle> liquidacionDetalles) {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
//            totalRetencion = BigDecimal.ZERO;
        for (RenFacturaDetalle det : liquidacionDetalles) {
            RenFactura liquidacion = new RenFactura();
            liquidacion = this.liquidacion;
            liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
            liquidacion = initLiquidacion(liquidacion);
            liquidacion.setCaja(cajero);
            liquidacion.setTotalPagar(det.getValor());
            liquidacion.setValorRetenido(det.getValor());
            liquidacion.setPeriodo(liquidacion.getAnio() + "-" + Utils.convertirMesALetra(liquidacion.getMes()));
            liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaRetencion()));
            liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
            det.getFactura().setFacturaFormasPago(facturaFormasPagoService.findAllFormaPagoByFactura(det.getFactura()));

            Gson gson = new Gson();
            if (liquidacion.getAts() != null && liquidacion.getAts().getCompras() != null && liquidacion.getAts().getCompras().getDetalleCompras() != null) {
                for (DetalleCompras d : liquidacion.getAts().getCompras().getDetalleCompras()) {
                    d.setSecRetencion1(String.format("%09d", liquidacion.getNumeroComprobante()));
                }
            }

            liquidacion.setDetalleCompras(gson.toJson(liquidacion.getAts()));
            renFacturaService.create(liquidacion);
            ////
            impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
            impuestoComprobanteElectronico.setCodigo(det.getRubro().getRubroTipo().getCodigo());
            impuestoComprobanteElectronico.setCodigoPorcentaje(det.getRubro().getCodigo());
            impuestoComprobanteElectronico.setBaseImponible(det.getBaseImponible());
            impuestoComprobanteElectronico.setPorcentajeRetencion(det.getImpuesto());
            impuestoComprobanteElectronico.setValor(det.getValor());
            impuestoComprobanteElectronico.setDescripcionDocumentoModificado(liquidacion.getComprobanteModifica().getDescripcion());
            impuestoComprobanteElectronico.setCodigoDocumento(liquidacion.getComprobante().getCodigo()); //codDocSustento
            impuestoComprobanteElectronico.setNumDocumento(det.getFactura().getNumFactura().replace("-", "").trim()); //numDocSustento
            impuestoComprobanteElectronico.setFechaEmisionDocumento(format.format(det.getFactura().getFechaFactura())); //fechaEmisionDocSustento
            impuestoComprobanteRetencion.add(impuestoComprobanteElectronico);
            det.setId(null);
            det.setRenFactura(liquidacion);
            det.setContabilizado(Boolean.FALSE);
            det.getFactura().setRetenida(Boolean.TRUE);
            facturaService.edit(det.getFactura());
            renFacturaDetalleService.create(det);
            //para la fecha de emision de la factura. anteriormente salia la fecha del dia que se realizaba la emision del comprobante, solucion poner otro campo para
            //no modificar el codigo de envio de factura xD
            //liquidacion.setFechaEmisionCabecera(factura.getFechaFactura());
            comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
            //COMPROBANTE RETENCION => 07
            comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.COMPPROBANTERETENCION);
            comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(liquidacion.getFechaEmision()));
            comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobanteModifica().getCodigo());
            comprobanteElectronico.setNumComprobanteModifica(det.getFactura().getNumFactura().replace("-", "").trim());
            comprobanteElectronico.setMes(liquidacion.getMes().toString());
            comprobanteElectronico.setAnio(liquidacion.getAnio().toString());
            comprobanteElectronico.setImpuestoComprobanteRetencion(impuestoComprobanteRetencion);
            comprobanteElectronicaService.enviarComprobanteRentencionSRI(comprobanteElectronico);
        }

    }

    public void guardarGenerarRetenciones() {
        if (liquidacion.getFechaEmision() == null) {
            JsfUtil.addWarningMessage("AVISO", "Asignar fecha de Emision");
            return;
        }
        Boolean periodoAbierto = diarioGeneralService.getPeriodoAbierto(Utils.getAnio(liquidacion.getFechaEmision()), Utils.convertirMesALetra(Utils.getMes(liquidacion.getFechaEmision())));
        if (!periodoAbierto) {
            JsfUtil.addWarningMessage("AVISO", "El mes de la fecha de emision seleccionada no se encuentra abierto");
            return;
        }
        if (validarRetencion(liquidacion, facturas, liquidacionDetalles)) {
            List<RenFacturaDetalle> listFaturasSelect = liquidacionDetalles.stream().filter(Utils.distinctByKey(RenFacturaDetalle::getFactura)).collect(Collectors.toList());
            for (RenFacturaDetalle ld : listFaturasSelect) {
                List<RenFacturaDetalle> listDetalleFacturaSelect = liquidacionDetalles.stream().filter(x -> x.getFactura().equals(ld.getFactura())).collect(Collectors.toList());
                this.guardarEmitirRetencion(listDetalleFacturaSelect);
            }
        }
        liquidacion.getDiario().setRetenido(Boolean.TRUE);
        liquidacion.getDiario().setNumRegistro(diarioGeneralClone.getNumRegistro());
        contDiarioGeneralService.edit(liquidacion.getDiario());
        JsfUtil.addSuccessMessage("", "Retencion " + (editar ? "Editada" : "Generada") + "  con éxito");
        cancelar();
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.redirect(CONFIG.URL_APP + "comprobantes-electronicos/comprobantesRegistrados");
    }

    public void guardarEmitirRetencion(List<RenFacturaDetalle> liquidacionDetalles) {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
        totalRetencion = BigDecimal.ZERO;
        for (RenFacturaDetalle det : liquidacionDetalles) {
            det.setId(null);
            totalRetencion = det.getValor().add(totalRetencion);
        }
        RenFactura liquidacion = Utils.clone(this.liquidacion);
        liquidacion.setId(null);
        liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
        liquidacion = initLiquidacion(liquidacion);
        liquidacion.setCaja(cajero);
        liquidacion.setTotalPagar(totalRetencion);
        liquidacion.setValorRetenido(totalRetencion);
        liquidacion.setPeriodo(liquidacion.getAnio() + "-" + Utils.convertirMesALetra(liquidacion.getMes()));
        liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaRetencion()));
        liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
        Gson gson = new Gson();
        if (liquidacion.getAts() != null && liquidacion.getAts().getCompras() != null && liquidacion.getAts().getCompras().getDetalleCompras() != null) {
            for (DetalleCompras d : liquidacion.getAts().getCompras().getDetalleCompras()) {
                d.setSecRetencion1(String.format("%09d", liquidacion.getNumeroComprobante()));
            }
        }
        liquidacion.getDiario().setRetenido(Boolean.TRUE);
        liquidacion.getDiario().setNumRegistro(diarioGeneralClone.getNumRegistro());
        contDiarioGeneralService.edit(liquidacion.getDiario());
        liquidacion.setDetalleCompras(gson.toJson(liquidacion.getAts()));
        renFacturaService.create(liquidacion);
        for (RenFacturaDetalle detalle : liquidacionDetalles) {
            impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
            impuestoComprobanteElectronico.setCodigo(detalle.getRubro().getRubroTipo().getCodigo());
            impuestoComprobanteElectronico.setCodigoPorcentaje(detalle.getRubro().getCodigo());
            impuestoComprobanteElectronico.setBaseImponible(detalle.getBaseImponible());
            impuestoComprobanteElectronico.setPorcentajeRetencion(detalle.getImpuesto());
            impuestoComprobanteElectronico.setValor(detalle.getValor());
            impuestoComprobanteElectronico.setDescripcionDocumentoModificado(liquidacion.getComprobanteModifica().getDescripcion());
            impuestoComprobanteElectronico.setCodigoDocumento(liquidacion.getComprobante().getCodigo()); //codDocSustento
            impuestoComprobanteElectronico.setNumDocumento(detalle.getFactura().getNumFactura().replace("-", "").trim()); //numDocSustento
            impuestoComprobanteElectronico.setFechaEmisionDocumento(format.format(detalle.getFactura().getFechaFactura())); //fechaEmisionDocSustento
            impuestoComprobanteRetencion.add(impuestoComprobanteElectronico);
            detalle.setId(null);
            detalle.setRenFactura(liquidacion);
            detalle.setContabilizado(Boolean.FALSE);
            detalle.getFactura().setRetenida(Boolean.TRUE);
            facturaService.edit(detalle.getFactura());
            renFacturaDetalleService.create(detalle);
        }
        //para la fecha de emision de la factura. anteriormente salia la fecha del dia que se realizaba la emision del comprobante, solucion poner otro campo para
        //no modificar el codigo de envio de factura xD
        //liquidacion.setFechaEmisionCabecera(factura.getFechaFactura());
        comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
        //COMPROBANTE RETENCION => 07
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.COMPPROBANTERETENCION);
        comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(liquidacion.getFechaEmision()));
        comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobanteModifica().getCodigo());
        comprobanteElectronico.setNumComprobanteModifica(factura.getNumFactura().replace("-", "").trim());
        comprobanteElectronico.setMes(liquidacion.getMes().toString());
        comprobanteElectronico.setAnio(liquidacion.getAnio().toString());
        comprobanteElectronico.setImpuestoComprobanteRetencion(impuestoComprobanteRetencion);
        comprobanteElectronicaService.enviarComprobanteRentencionSRI(comprobanteElectronico);
    }

    public void buscarProveedor() {
        if (!liquidacion.getIdentificacionProveedor().isEmpty()) {
            Proveedor p = servidorService.findByProveedor(liquidacion.getIdentificacionProveedor());
            if (p != null) {
                liquidacion.setProveedor(p);
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
        }
    }

    private void setPeriodoDiarioRetencion() {
        diarioGeneralClone = Utils.clone(diarioGeneral);
        liquidacion.setFechaEmision(diarioGeneral.getFechaRegistro());
        liquidacion.setFechaEmisionCabecera(diarioGeneral.getFechaRegistro());
        liquidacion.setMes(Utils.getMes(diarioGeneral.getFechaRegistro()));
        liquidacion.setAnio(Utils.getAnio(diarioGeneral.getFechaRegistro()));
    }

    public void buscarDiarioGeneral() {
        if (liquidacion.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe selecionar el periodo fiscal antes de continuar");
            return;
        }
        if (diarioGeneral.getNumRegistro() != null) {
            diarioGeneral = diarioGeneralService.getDiarioGeneralRetencionByNumTransaccion(diarioGeneral.getNumRegistro(), true, false, liquidacion.getAnio().shortValue());
            if (diarioGeneral == null) {
                Utils.openDialog("/facelet/contabilidad/DiarioGeneral/dialogDiarioGeneral", null);
            } else {
                setPeriodoDiarioRetencion();
                cargarDatosRetenciones();
            }
        } else {
            Utils.openDialog("/facelet/contabilidad/DiarioGeneral/dialogDiarioGeneral", null);
        }
        PrimeFaces.current().ajax().update("gridPeriodos");
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
                System.out.println("valor total>>>" + valorTotal + " valor diario>>" + totalCuentasValor);
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
        if (facturasAgregadas == null || facturasAgregadas.isEmpty()) {
            JsfUtil.addErrorMessage("", "Necesita selecionar una Factura");
            return;
        }
        if (detalleCompras.getCodSustento() == null) {
            JsfUtil.addErrorMessage("", "Seleccione un sustento de comprobante");
            return;
        }

        for (Factura factura : facturasAgregadas) {
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
            liquidacionDetalle.setEstado(Boolean.TRUE);
            if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("RENTAS")) {
                liquidacionDetalle.setBaseImponible(factura.getSubtotalRenta());
                liquidacionDetalle.setValor(factura.getSubtotalRenta().multiply(impuesto.setScale(2)).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP));
            } else if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("IVA")) {
                liquidacionDetalle.setBaseImponible(factura.getMontoIva());
                liquidacionDetalle.setValor(factura.getMontoIva().multiply(impuesto.setScale(2)).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP));
            }
            liquidacionDetalles.add(liquidacionDetalle);
            liquidacionDetalle = new RenFacturaDetalle();

        }
        valorTotal = BigDecimal.ZERO;
        impuesto = BigDecimal.ZERO;
        baseImponible = BigDecimal.ZERO;
        activarBtnDetalle = Boolean.FALSE;
        cuentaContableRetencion = new CuentaContableRetencion();
        removerDetalleTransaccionRetenciones();
        JsfUtil.update("rubrosCuenta");
        JsfUtil.update("sustentoComp");
    }

    private void removerDetalleTransaccionRetenciones() {
        for (ContDiarioGeneralDetalle d : detalleTransaccionesSeleccionados) {
            detalleTransacciones.remove(d);
        }
        detalleTransaccionesSeleccionados = new ArrayList<>();
        JsfUtil.update("dataTableDetallesCuentas");
    }

    public void removerDetalle(RenFacturaDetalle detalle) {
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
        liquidacion.setProveedor((Proveedor) evt.getObject());
        liquidacion.setIdentificacionProveedor(((Proveedor) evt.getObject()).getCliente().getIdentificacionCompleta());
        PrimeFaces.current().ajax().update("fldInfoComprobante");
    }

    public void selectDataDiarioGeneral(SelectEvent evt) {
        diarioGeneral = (ContDiarioGeneral) evt.getObject();
        setPeriodoDiarioRetencion();
        cargarDatosRetenciones();
        PrimeFaces.current().ajax().update("fldInfoDiarioGeneral");
        PrimeFaces.current().ajax().update("gridPeriodos");
    }

    public void cargarCuentaRubro() {
        /* if (detalleTransaccionSeleccionado != null) {
            cuentaContableRetenciones = cuentaContableRetencionService.findAllByCuentaContable(detalleTransaccionSeleccionado.getCuentaContable());
            JsfUtil.addInformationMessage("", "Cuenta " + detalleTransaccionSeleccionado.getCuentaContable().getCodigo() + " Seleccionada");
        }*/
        if (!detalleTransaccionesSeleccionados.isEmpty()) {
            List<CuentaContableRetencion> newElement = new ArrayList<>();
            cuentaContableRetenciones.clear();
            totalCuentasValor = BigDecimal.ZERO;
            for (ContDiarioGeneralDetalle d : detalleTransaccionesSeleccionados) {
                List<CuentaContableRetencion> cuentaRetencionesDB = cuentaContableRetencionService.findAllByCuentaContable(d.getIdContCuentas());
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
            if (facturasSeleccionadas == null || facturasSeleccionadas.isEmpty()) {
                JsfUtil.addErrorMessage("", "Debe seleccionar una factura como minimo...");
                return;
            }
            if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("RENTAS")) {
//                baseImponible = factura.getSubtotalRenta();
                baseImponible = facturasSeleccionadas.stream().map(Factura::getSubtotalRenta).reduce(BigDecimal.ZERO, BigDecimal::add);
                impuesto = cuentaContableRetencion.getRetencion().getPorcentaje();
            } else if (cuentaContableRetencion.getRetencion().getRubroTipo().getDescripcion().equals("IVA")) {
//                baseImponible = factura.getMontoIva();
                baseImponible = facturasSeleccionadas.stream().map(Factura::getMontoIva).reduce(BigDecimal.ZERO, BigDecimal::add);
                impuesto = cuentaContableRetencion.getRetencion().getPorcentajeRetencion();
            }
            calcularValorRetencion();
        }
    }

    public void cargarDatosRetenciones() {
//        System.out.println("diario>>" + diarioGeneral);
        if (!diarioGeneral.getRetenido()) {
            liquidacion.setDiario(diarioGeneral);
            proveedorExiste();
            existeFacturas();
            detalleCuentasRetencion();
            /*VALIDACION DE FECHAS DE PERIODO FISCAL .. ADVERTENCIA....... 
            * 1.- La fecha de diario general debe ser el menor o igual a la fecha del periodo fiscal de la retencion .... 
            * 2.- La fecha de la factura debe ser menor o igual a la fecha del diario general PILAAAAAAAAAAAAAAAS
             */
            // PRIMER PUNTO OK
            if (!verificacionPeriodoFiscal(liquidacion)) {
                bloquearOpciones();
                JsfUtil.update("formMain");
                PrimeFaces.current().ajax().update("gridPeriodos");
            }
        } else {
            JsfUtil.addWarningMessage("", "Ya existe una retencion para el Diario General " + diarioGeneral.getNumRegistro());
            loadModel();
            JsfUtil.update("formMain");
            PrimeFaces.current().ajax().update("gridPeriodos");
        }
    }

    private void proveedorExiste() {
        solicitud = contRegistroContableService.getReservaCompromiso(diarioGeneral);
        if (solicitud != null) {
            if (solicitud.getTipoBeneficiario() != null
                    && solicitud.getTipoBeneficiario()
                    && solicitud.getBeneficiario() != null) {
                liquidacion.setProveedor(proveedorService.findByIdentificacion(solicitud.getBeneficiario().getIdentificacion()));
                //liquidacion.setSolicitanteAuxiliar(liquidacion.getSolicitante());
                liquidacion.setIdentificacionProveedor(liquidacion.getProveedor().getCliente().getIdentificacionCompleta());
            } else {
                consultarProveedorByDiarioGeneral();
            }
        } else {
            consultarProveedorByDiarioGeneral();
        }
        PrimeFaces.current().ajax().update("fldInfoComprobante");
    }

    private void consultarProveedorByDiarioGeneral() {
        if (solicitud != null && solicitud.getBeneficiario() != null) {
            Proveedor p = proveedorService.findByProveedorByCliente(solicitud.getBeneficiario());
            if (p != null) {
                liquidacion.setProveedor(p);
                liquidacion.setIdentificacionProveedor(liquidacion.getProveedor().getCliente().getIdentificacionCompleta());
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
        if (diarioGeneral != null && solicitud != null) {
            facturas = contDiarioGeneralService.findAllFacturasByDiarioGeneralBySolicitud(solicitud);
            List<Factura> facturasBienes = contDiarioGeneralService.findAllFacturaByDiarioGeneralForBienes(solicitud);
            if (facturas.isEmpty() && facturasBienes.isEmpty()) {
                JsfUtil.addWarningMessage("", "No existen facturas Registradas");
                facturas = new ArrayList<>();
            } else {
                newFacturasDiarioGeneral(facturas, facturasBienes);
                if (!validarFacturasFechaDiario(liquidacion, facturas)) {
                    bloquearOpciones();
                    JsfUtil.update("formMain");
                }
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
        if (facturasSeleccionadas != null && !facturasSeleccionadas.isEmpty()) {
            for (Factura facturaSeleccionada : facturasSeleccionadas) {
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
        if (liquidacion.getProveedor() == null || liquidacion.getProveedor().getId() == null) {
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
            List<Factura> findFactura = facturaService.findFacturaByNumeroComprobante(factura.getNumAutorizacion(), factura.getFacturaElectronica());
            if (!findFactura.isEmpty()) {
                JsfUtil.addErrorMessage("", "Ya Existe una Factura registrada con el código " + factura.getNumFactura());
                return;
            }
            factura.setEstado(Boolean.TRUE);
            factura.setProveedor(liquidacion.getProveedor());
            factura = facturaService.create(factura);
            System.out.println("factura>>" + factura);
            this.guardarDatosPagoByFactura();
            facturas.add(factura);
        }
        JsfUtil.addSuccessMessage("", "Factura N° " + (edit ? "Editado " : "Agregado ") + factura.getNumFactura() + " correctamente");
        factura = new Factura();
        PrimeFaces.current().ajax().update("dataTableFacturas");
        PrimeFaces.current().executeScript("PF('dialogComprobante').hide()");
    }

    public void detalleCuentasRetencion() {
        detalleTransacciones = contDiarioGeneralService.findAllRetencionDiarioGeneral(diarioGeneral);
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
        facturasSeleccionadas = new ArrayList<>();
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

    public void guardarDatosPagoByFactura() {
        System.out.println("factura>>" + factura);
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
        System.out.println("loadDatosPago()");
        loadDatosRegimenExterior();
        System.out.println("loadDatosRegimenExterior()");
        Boolean editPago = factura.getPagoExterior().getId() != null;
        System.out.println("foma de pago >>" + formasPagos.get(0));
        formaPagoSelection = formasPagos.get(0);
        this.aggFormasPago();
        createEditPago(editPago);
        createFormasPago();
//        factura = new Factura();
        facturasSeleccionadas = new ArrayList<>();
    }

    public void guardarDatosPago() {
        //AQUI TENDRIA QUE HACER PARA GUARDAR LOS DATOS DE PAGO
        Boolean editPago = factura.getPagoExterior().getId() != null;
        createEditPago(editPago);
        createFormasPago();
        JsfUtil.addSuccessMessage("", "Datos de pagos " + (editPago ? " actualizado " : " generado ") + "con éxito");
        factura = new Factura();
        facturasSeleccionadas = new ArrayList<>();
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

    public void aggFacturaSeleccionada() {
        System.out.println("factutas " + facturasAgregadas.size());
        for (Factura f : facturasAgregadas) {
            if (!facturas.isEmpty()) {
                if (facturas.contains(f)) {
                    JsfUtil.addErrorMessage("", "La factura N° " + f.getNumFactura() + " ya ha sido agregada");
                    break;
                }
                List<Factura> facturaAux = facturas;
                facturas = new ArrayList<>();
                facturaAux.add(f);
                facturas = facturaAux;
            } else {
                facturas.add(f);
            }
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

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Boolean getDisabledPeriodos() {
        return disabledPeriodos;
    }

    public void setDisabledPeriodos(Boolean disabledPeriodos) {
        this.disabledPeriodos = disabledPeriodos;
    }

    public List<Factura> getFacturasSeleccionadas() {
        return facturasSeleccionadas;
    }

    public void setFacturasSeleccionadas(List<Factura> facturasSeleccionadas) {
        this.facturasSeleccionadas = facturasSeleccionadas;
    }

    public List<ContDiarioGeneralDetalle> getDetalleTransaccionesSeleccionados() {
        return detalleTransaccionesSeleccionados;
    }

    public void setDetalleTransaccionesSeleccionados(List<ContDiarioGeneralDetalle> detalleTransaccionesSeleccionados) {
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

    public List<ContDiarioGeneralDetalle> getDetalleTransacciones() {
        return detalleTransacciones;
    }

    public void setDetalleTransacciones(List<ContDiarioGeneralDetalle> detalleTransacciones) {
        this.detalleTransacciones = detalleTransacciones;
    }

    public ContDiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public List<Factura> getFacturasAgregadas() {
        return facturasAgregadas;
    }

    public void setFacturasAgregadas(List<Factura> facturasAgregadas) {
        this.facturasAgregadas = facturasAgregadas;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void setDiarioGeneral(ContDiarioGeneral diarioGeneral) {
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

    public RenFactura getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RenFactura liquidacion) {
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

    public RenFacturaDetalle getLiquidacionDetalle() {
        return liquidacionDetalle;
    }

    public void setLiquidacionDetalle(RenFacturaDetalle liquidacionDetalle) {
        this.liquidacionDetalle = liquidacionDetalle;
    }

    public List<RenFacturaDetalle> getLiquidacionDetalles() {
        return liquidacionDetalles;
    }

    public void setLiquidacionDetalles(List<RenFacturaDetalle> liquidacionDetalles) {
        this.liquidacionDetalles = liquidacionDetalles;
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
