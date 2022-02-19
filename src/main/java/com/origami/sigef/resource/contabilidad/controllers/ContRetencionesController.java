/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.google.gson.Gson;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.ats.entities.FacturaFormasPago;
import com.origami.sigef.ats.modelAts.Air;
import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.DetalleCompras;
import com.origami.sigef.ats.modelAts.FormasPagoModel;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.PagoExteriorModel;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaDetalle;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.entities.ContFacturaDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.services.ContFacturaDetalleService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RenFacturaDetalleService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Jhonathan Choez
 * @author Criss Intriago
 */
@Named(value = "contRetencionesView")
@ViewScoped
public class ContRetencionesController extends AccesosComprobanteElectronico implements Serializable {

    @Inject
    private ContRegistroContable contRegistroContable;
    @Inject
    private FacturaService facturaService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private RenFacturaDetalleService renFacturaDetalleService;
    @Inject
    private RenFacturaService renFacturaService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ContFacturaDetalleService contFacturaDetalleService;

    private Integer numRegistroContable;

    private String identificacion;

    private Boolean editar, accesoComprobante;

    private List<Factura> facturaList;
    private List<Short> periodos;
    private List<Factura> facturasSeleccionadas;
    private List<RenFacturaDetalle> liquidacionDetalles;
    private List<String> meses;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle;
    private List<Comprobantes> comprobantes;

    private ContDiarioGeneral contDiarioGeneral;
    private OpcionBusqueda opcionBusqueda;
    private Proveedor proveedor;
    private RenFactura liquidacion;
    private Cajero cajero;
    private ComprobanteElectronico comprobanteElectronico;

    private LazyModel<ContDiarioGeneral> contDiarioGeneralLazy;

    private SimpleDateFormat format;

    private BigDecimal totalRetencionIva, totalRetencionRentas;

    @PostConstruct
    public void init() {
        periodos = catalogoItemService.getPeriodo();
        cleanForm();
        if (cajero != null) {
            accesoComprobante = true;
        } else {
            accesoComprobante = false;
        }
        if (accesoComprobante && servletSession.getParametros() != null && !servletSession.getParametros().isEmpty()) {
            loadRedirect();
        }
    }

    public void loadRedirect() {
        Map<String, Object> parametros = servletSession.getParametros();
        liquidacion = (RenFactura) parametros.get("liquidacion");
        if (liquidacion.getId() != null) {
            contDiarioGeneral = liquidacion.getDiario();
            proveedor = liquidacion.getProveedor();
            identificacion = proveedor.getCliente().getIdentificacion();
        }
        editar = (Boolean) parametros.get("edit");
        if (liquidacion.getIdFactura() != null) {
            facturaList.add(liquidacion.getIdFactura());
            facturasSeleccionadas.add(liquidacion.getIdFactura());
        }
        numRegistroContable = contDiarioGeneral.getNumRegistro();
        loadDetalle();
        servletSession.borrarParametros();
    }

    public void setPeriodoFiscal() {
        liquidacion.setAnio(Utils.getAnio(liquidacion.getFechaEmision()));
        liquidacion.setMes(Utils.getMes(liquidacion.getFechaEmision()));
        liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
    }

    public Integer mes(String mes) {
        return Utils.convertirLetraAMes(mes);
    }

    public void fechaPerido() {
        int dias = Utils.getDia(liquidacion.getFechaEmision());
        int anio = liquidacion.getAnio();
        int mes = liquidacion.getMes() - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, mes, dias);
        liquidacion.setFechaEmision(calendar.getTime());
    }

    public void cleanForm() {
        facturaList = new ArrayList<>();
        liquidacionDetalles = new ArrayList<>();
        facturasSeleccionadas = new ArrayList<>();
        opcionBusqueda = new OpcionBusqueda();
        identificacion = "";
        numRegistroContable = null;
        format = new SimpleDateFormat("yyyy-MM-dd");
        cajero = katalinaService.findCajero();
        meses = Utils.getMeses();
        comprobantes = katalinaService.findComprobantesRetienen();
        liquidacion = new RenFactura();
        liquidacion.setAnio(Utils.clone(opcionBusqueda.getAnio().intValue()));
        liquidacion.setMes(Utils.getMes(new Date()));
        liquidacion.setFechaEmision(new Date());
        liquidacion.setComprobante(katalinaService.findComprobante(ComprobantesCode.COMPPROBANTERETENCION));
        liquidacion.setComprobanteModifica(comprobantes.get(0));
        liquidacion.setTipoEmision("ELECTRÓNICA");
        totalRetencionIva = BigDecimal.ZERO;
        totalRetencionRentas = BigDecimal.ZERO;
    }

    public void findDiario() {
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        if (numRegistroContable == null) {
            openDlgDiarioGeneral();
        } else {
            contDiarioGeneral = contRegistroContable.findByDiarioPeriodoRetenido(numRegistroContable, opcionBusqueda.getAnio());
            if (contDiarioGeneral != null) {
                closeDlgDiarioGeneral(contDiarioGeneral, false);
            } else {
                openDlgDiarioGeneral();
            }
        }
    }

    public void openDlgDiarioGeneral() {
        contDiarioGeneralLazy = new LazyModel<>(ContDiarioGeneral.class);
        contDiarioGeneralLazy.getSorteds().put("numRegistro", "ASC");
        contDiarioGeneralLazy.getFilterss().put("retencion", true);
        contDiarioGeneralLazy.getFilterss().put("retenido", false);
        contDiarioGeneralLazy.getFilterss().put("cuadrado", true);
        contDiarioGeneralLazy.getFilterss().put("anulado", false);
        contDiarioGeneralLazy.getFilterss().put("idDiarioGeneral:equal", null);
//        contDiarioGeneralLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        PrimeFaces.current().executeScript("PF('dlgDiarioGeneral').show()");
        PrimeFaces.current().ajax().update("dataDiarioGeneral");
    }

    public void closeDlgDiarioGeneral(ContDiarioGeneral contDiarioGeneral, Boolean accion) {
        Boolean temp = Boolean.TRUE;
        if (accion) {
            this.contDiarioGeneral = contDiarioGeneral;
        }
        facturaList = facturaService.findByNamedQuery("Factura.findByIdDiario", contDiarioGeneral);
        if (facturaList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No tiene facturas relacionadas");
            return;
        } else {
            proveedor = facturaList.get(0).getProveedor();
            identificacion = proveedor.getCliente().getIdentificacionCompleta();
            temp = Boolean.FALSE;
            if (accion) {
                PrimeFaces.current().executeScript("PF('dlgDiarioGeneral').hide()");
            }
        }
        if (temp) {
            identificacion = contDiarioGeneral.getIdentificacion();
            findProveedor();
        }
        PrimeFaces.current().ajax().update("fielsetDiario");
        PrimeFaces.current().ajax().update("fielsetProveedor");
        PrimeFaces.current().ajax().update("facturasDataTable");
    }

    public void loadDetalle() {
        totalRetencionIva = BigDecimal.ZERO;
        totalRetencionRentas = BigDecimal.ZERO;
        if (facturasSeleccionadas.isEmpty()) {
            liquidacionDetalles = new ArrayList<>();
            proveedor = null;
        } else {
            liquidacionDetalles = new ArrayList<>();
            calcularTotales();
            List<ContFacturaDetalle> temp = contFacturaDetalleService.getDetalle(facturasSeleccionadas);
            if (!temp.isEmpty()) {
                for (ContFacturaDetalle detalle : temp) {
                    RenFacturaDetalle liquidacionDetalle = new RenFacturaDetalle();
                    BigDecimal impuesto;
                    if (detalle.getIdConfRetencion().getRetencion().getRubroTipo().getDescripcion().equals("RENTAS")) {
                        impuesto = detalle.getIdConfRetencion().getRetencion().getPorcentaje();
                    } else {
                        impuesto = detalle.getIdConfRetencion().getRetencion().getPorcentajeRetencion();
                    }
                    liquidacionDetalle.setFactura(detalle.getIdFactura());
                    liquidacionDetalle.setRubro(detalle.getIdConfRetencion().getRetencion());
                    liquidacionDetalle.setCuentaContableRetencion(detalle.getIdConfRetencion());
                    liquidacionDetalle.setImpuesto(impuesto);
                    liquidacionDetalle.setCantidad(1);
                    liquidacionDetalle.setEstado(Boolean.TRUE);
                    liquidacionDetalle.setBaseImponible(detalle.getValorBase());
                    liquidacionDetalle.setValor(detalle.getValorDetalle());
                    liquidacionDetalles.add(liquidacionDetalle);
                }
            }
            contDiarioGeneralDetalle = new ArrayList<>();
            contDiarioGeneralDetalle = contRegistroContable.getaddCuentas(contDiarioGeneralDetalle, facturasSeleccionadas);
        }
        PrimeFaces.current().ajax().update("contabilidadDetalle");
        PrimeFaces.current().ajax().update("detalleRetencion");
    }

    public void findProveedor() {
        if (identificacion != null) {
            proveedor = proveedorService.findByProveedor(identificacion);
            if (proveedor == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "No existe ningun provedor");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No debe ingresar un proveedor");
        }
    }

    public void removerDetalle(LiquidacionDetalle ld, int index) {
        if (ld.getId() != null) {
            liquidacionDetalles.remove(ld);
        } else {
            liquidacionDetalles.remove(index);
        }
    }

    public void guardarGenerarRetenciones() {
        loadLiquidacion();
        if (liquidacion.getFechaEmision() == null) {
            JsfUtil.addWarningMessage("AVISO", "Asignar fecha de emisión");
            return;
        }
        if (contRegistroContable.periodoContableValidador(liquidacion.getFechaEmision(), Utils.getAnio(liquidacion.getFechaEmision()).shortValue())) {
            JsfUtil.addWarningMessage("AVISO", "El mes de la fecha de emision seleccionada no se encuentra abierto");
            return;
        }
        System.out.println("FACTURAS SELECCIONADAS: " + facturasSeleccionadas.size());
        if (validarRetencion(liquidacion, facturaList, liquidacionDetalles)) {
            for (Factura factura : facturasSeleccionadas) {
                List<RenFacturaDetalle> detalle = new ArrayList<>();
                for (RenFacturaDetalle det : liquidacionDetalles) {
                    if (factura.getId().equals(det.getFactura().getId())) {
                        detalle.add(det);
                    }
                }
                factura.setDetalleRetencion(detalle);
                loadDataComprobante(factura);
            }
        }
        contRegistroContable.getUpdateFacturas(facturasSeleccionadas, contDiarioGeneral);
        cleanForm();
        PrimeFaces.current().ajax().update("formMain");
        JsfUtil.redirect(CONFIG.URL_APP + "comprobantes-electronicos/comprobantesRegistrados");
    }

    public void loadDataComprobante(Factura factura) {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
        BigDecimal totalRetencion = BigDecimal.ZERO;
        for (RenFacturaDetalle det : factura.getDetalleRetencion()) {
            det.setId(null);
            totalRetencion = det.getValor().add(totalRetencion);
        }
        RenFactura liquidacion = Utils.clone(this.liquidacion);
        liquidacion.setId(null);
        if (factura.getFacturaElectronica()) {
            liquidacion.setTipoEmision("ELECTRÓNICA");
        } else {
            liquidacion.setTipoEmision("FÍSICA");
        }
        liquidacion.setFechaEmisionCabecera(liquidacion.getFechaEmision());
        liquidacion = initLiquidacion(liquidacion);
        liquidacion.setCaja(cajero);
        liquidacion.setTotalPagar(totalRetencion);
        liquidacion.setValorRetenido(totalRetencion);
        liquidacion.setIdFactura(factura);
        liquidacion.setPeriodo(liquidacion.getAnio() + "-" + Utils.convertirMesALetra(liquidacion.getMes()));
        liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaRetencion()));
        liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
        Gson gson = new Gson();
        if (liquidacion.getAts() != null && liquidacion.getAts().getCompras() != null && liquidacion.getAts().getCompras().getDetalleCompras() != null) {
            for (DetalleCompras d : liquidacion.getAts().getCompras().getDetalleCompras()) {
                d.setSecRetencion1(String.format("%09d", liquidacion.getNumeroComprobante()));
            }
        }
        liquidacion.setDiario(contDiarioGeneral);
        //guardar datos para el ATS
        loadATS(liquidacion, factura);
        liquidacion.setDetalleCompras(gson.toJson(liquidacion.getAts()));
        renFacturaService.create(liquidacion);
        for (RenFacturaDetalle detalle : factura.getDetalleRetencion()) {
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

    public Boolean validarRetencion(RenFactura liquidacion, List<Factura> facturas, List<RenFacturaDetalle> detalleRetencion) {
        if (liquidacion.getComprobanteModifica() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un Comprobante", "");
            return false;
        }
        if (liquidacion.getProveedor() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un Sujeto Retenido", "");
            return false;
        }
        if (facturas.isEmpty()) {
            JsfUtil.addErrorMessage("Debe agregar al menos una factura", "");
            return false;
        }
        if (liquidacion.getDiario() == null) {
            JsfUtil.addErrorMessage("", "Se debe asignar un diario General");
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
        if (contDiarioGeneral.getFechaRegistro().compareTo(liquidacion.getFechaEmision()) > 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "La fecha del registro contable debe ser menor o igual al de la fecha de la emisión de la retención");
            return false;
        }
        if (!(Utils.getMes(contDiarioGeneral.getFechaRegistro()).equals(Utils.getMes(liquidacion.getFechaEmision())))) {
            JsfUtil.addWarningMessage("AVISO!!!", "El mes del registro contable es diferente al mes del periodo fiscal");
            return false;
        }
        if (!(Utils.getAnio(contDiarioGeneral.getFechaRegistro()).equals(Utils.getAnio(liquidacion.getFechaEmision())))) {
            JsfUtil.addWarningMessage("AVISO!!!", "El período del registro contable es diferente al período del periodo fiscal");
            return false;
        }
        return true;
    }

    public void loadATS(RenFactura liquidacion, Factura factura) {
        DetalleCompras detalleCompras = new DetalleCompras();
        detalleCompras.setCodSustento(factura.getCodSustento());
        liquidacion.setAts(new IVA());
        Air air = new Air();
        liquidacion.setAts(createAts(liquidacion, cajero));
        liquidacion.getAts().setCompras(new Compras());
        //detalle compras
        detalleCompras = createDetalleCompras(factura, liquidacion, contDiarioGeneral, cajero);
        detalleCompras.setCodSustento(factura.getCodSustento());
        detalleCompras.setIdProv(liquidacion.getProveedor().getCliente().getIdentificacionCompleta());
        detalleCompras.setFormasDePago(new FormasPagoModel());
        detalleCompras.getFormasDePago().setFormaPago(new ArrayList<>());
        for (FacturaFormasPago p : factura.getFacturaFormasPago()) {
            detalleCompras.getFormasDePago().getFormaPago().add(p.getFormaPago().getCodigo());
        }
        detalleCompras.setPagoExterior(new PagoExteriorModel(factura.getPagoExterior().getPagoLocExt(), factura.getPagoExterior().getTipoRegi(),
                factura.getPagoExterior().getDenopagoRegFis(), factura.getPagoExterior().getPaisEfecPagoGen(), factura.getPagoExterior().getPaisEfecPagoParFis(),
                factura.getPagoExterior().getDenopago(), factura.getPagoExterior().getPaisEfecPago(), factura.getPagoExterior().getAplicConvDobTrib(),
                factura.getPagoExterior().getPagExtSujRetNorLeg()));
        detalleCompras.setSecRetencion1(String.format("%09d", liquidacion.getNumeroComprobante()));
        liquidacion.getAts().getCompras().setDetalleCompras(new ArrayList<>());
        List<String> codes = new ArrayList<>();
        for (RenFacturaDetalle item : factura.getDetalleRetencion()) {
            if (item.getCuentaContableRetencion().getRetencion().getRubroTipo().getDescripcion().equalsIgnoreCase("RENTAS")) {
                Air.DetalleAir detalleAir = new Air.DetalleAir(item.getCuentaContableRetencion().getRetencion().getCodigo(), item.getBaseImponible().setScale(2, RoundingMode.HALF_UP), item.getImpuesto().setScale(2, RoundingMode.HALF_UP), item.getValor().setScale(2, RoundingMode.HALF_UP));
                air.getDetalleAir().add(detalleAir);
                if (!codes.contains(item.getCuentaContableRetencion().getRetencion().getCodigo())) {
                    codes.add(item.getCuentaContableRetencion().getRetencion().getCodigo());
                }
            } else {
                ingresarValoresFactura(item, factura, liquidacion);
            }
        }
        List<Air.DetalleAir> result = new ArrayList<>();
        for (String code : codes) {
            Air.DetalleAir detalleAir = new Air.DetalleAir(code);
            for (Air.DetalleAir item : air.getDetalleAir()) {
                if (item.getCodRetAir().equals(code)) {
                    detalleAir.setBaseImpAir(detalleAir.getBaseImpAir().add(item.getBaseImpAir()).setScale(2, RoundingMode.HALF_UP));
                    detalleAir.setPorcentajeAir(detalleAir.getPorcentajeAir().add(item.getPorcentajeAir()).setScale(2, RoundingMode.HALF_UP));
                    detalleAir.setValRetAir(detalleAir.getValRetAir().add(item.getValRetAir()).setScale(2, RoundingMode.HALF_UP));
                }
            }
            result.add(detalleAir);
        }
        air.setDetalleAir(result);
        detalleCompras.setAir(air);
        liquidacion.getAts().getCompras().getDetalleCompras().add(detalleCompras);
    }

    public void loadLiquidacion() {
        liquidacion.setDiario(contDiarioGeneral);
        liquidacion.setProveedor(proveedor);
    }

    public void ingresarValoresFactura(RenFacturaDetalle item, Factura factura, RenFactura liquidacion) {
        switch ((int) Math.round(item.getCuentaContableRetencion().getRetencion().getValor())) {
            case 10:
                factura.setRetencionIva10(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetBien10(item.getValor());
                });
                break;
            case 20:
                factura.setRetencionIva20(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ20(item.getValor());
                });
                break;
            case 30:
                factura.setRetencionIva30(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValorRetBienes(item.getValor());
                });
                break;
            case 50:
                factura.setRetencionIva50(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ50(item.getValor());
                });
                break;
            case 70:
                factura.setRetencionIva70(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValorRetServicios(item.getValor());
                });
                break;
            case 100:
                factura.setRetencionIva100(item.getValor());
                liquidacion.getAts().getCompras().getDetalleCompras().forEach((comprasModel) -> {
                    comprasModel.setValRetServ100(item.getValor());
                });
                break;
        }
    }

    public void calcularTotales() {
        if (!facturaList.isEmpty()) {
            for (Factura fac : facturasSeleccionadas) {
                totalRetencionIva = totalRetencionIva.add(fac.getRetencionIva()).setScale(2, RoundingMode.HALF_UP);
                totalRetencionRentas = totalRetencionRentas.add(fac.getRetencionRenta()).setScale(2, RoundingMode.HALF_UP);
            }
        }
    }

    public void verificarWSDL() {
        String msj = comprobanteElectronicaService.verificarServiciosSRI();
        if (msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede obtener resultados");
        } else {
            String[] array = msj.split("-");
            JsfUtil.addSuccessMessage("INFO!!!", array[0]);
            JsfUtil.addSuccessMessage("INFO!!!", array[1]);
        }
    }

    public Integer getNumRegistroContable() {
        return numRegistroContable;
    }

    public void setNumRegistroContable(Integer numRegistroContable) {
        this.numRegistroContable = numRegistroContable;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<Factura> getFacturasSeleccionadas() {
        return facturasSeleccionadas;
    }

    public void setFacturasSeleccionadas(List<Factura> facturasSeleccionadas) {
        this.facturasSeleccionadas = facturasSeleccionadas;
    }

    public List<RenFacturaDetalle> getLiquidacionDetalles() {
        return liquidacionDetalles;
    }

    public void setLiquidacionDetalles(List<RenFacturaDetalle> liquidacionDetalles) {
        this.liquidacionDetalles = liquidacionDetalles;
    }

    public ContDiarioGeneral getContDiarioGeneral() {
        return contDiarioGeneral;
    }

    public void setContDiarioGeneral(ContDiarioGeneral contDiarioGeneral) {
        this.contDiarioGeneral = contDiarioGeneral;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LazyModel<ContDiarioGeneral> getContDiarioGeneralLazy() {
        return contDiarioGeneralLazy;
    }

    public void setContDiarioGeneralLazy(LazyModel<ContDiarioGeneral> contDiarioGeneralLazy) {
        this.contDiarioGeneralLazy = contDiarioGeneralLazy;
    }

    public RenFactura getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RenFactura liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public BigDecimal getTotalRetencionIva() {
        return totalRetencionIva;
    }

    public void setTotalRetencionIva(BigDecimal totalRetencionIva) {
        this.totalRetencionIva = totalRetencionIva;
    }

    public BigDecimal getTotalRetencionRentas() {
        return totalRetencionRentas;
    }

    public void setTotalRetencionRentas(BigDecimal totalRetencionRentas) {
        this.totalRetencionRentas = totalRetencionRentas;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetalle() {
        return contDiarioGeneralDetalle;
    }

    public void setContDiarioGeneralDetalle(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle) {
        this.contDiarioGeneralDetalle = contDiarioGeneralDetalle;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Boolean getAccesoComprobante() {
        return accesoComprobante;
    }

    public void setAccesoComprobante(Boolean accesoComprobante) {
        this.accesoComprobante = accesoComprobante;
    }

}
