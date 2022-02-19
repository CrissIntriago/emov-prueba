/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaDetalle;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.DiarioGeneralService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Directorio;
import com.origami.sigef.tesoreria.comprobantelectronico.service.AmbienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.DirectorioService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.LiquidacionPagoService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author gutya
 */
@Named(value = "comprobanteElectronicoView")
@ViewScoped
public class ComprobantesElectronicosController extends AccesosComprobanteElectronico implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //Para enviar la liquidacion a visualizar o editar
    @Inject
    private ServletSession servletSession;
    @Inject
    private ComprobanteService comprobanteService;
    @Inject
    private DirectorioService directorioService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private RenFacturaService renFacturaService;
    @Inject
    private ContDiarioGeneralService contDiarioGeneralService;
    @Inject
    private LiquidacionPagoService liquidacionPagoService;
    @Inject
    private DiarioGeneralService diarioGeneralService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private AmbienteService ambienteService;
    @Inject
    private RecaudacionInteface recaudacionService;

    private List<Comprobantes> comprobantes;
    private Comprobantes comprobante;
    private LazyModel<RenFactura> facturaLazy;
    private LazyModel<RenFactura> retencioLazy;
    private LazyModel<RenFactura> notaCreditoLazy;
    private LazyModel<RenFactura> notaDebitoLazy;
    private LazyModel<RenFactura> autorizadotoLazy;
    private Date desde, hasta;
    private String identificacion;
    private SimpleDateFormat format;
    private ComprobanteElectronico comprobanteElectronico;
    //PARA LA RUTA DE DESCARGAS 
    private Directorio directorio;
    private Cajero cajero;
    private List<CatalogoItem> estadosLiquidaciones;
    private Long idEstadoLiquidacion;

    private String numeroAutorizacion;
    private Ambiente ambiente;
    private Boolean urlAutorizacion;
    private Boolean reseccion;

    @PostConstruct
    public void init() {
        comprobantes = comprobanteService.findComprobantesActivos();
//        RECIBIDOS
        directorio = directorioService.findByCodigo(3);
        cajero = katalinaService.findCajero();
        estadosLiquidaciones = catalogoItemService.findCatalogoItems("estado_liquidacion");
//        ambiente = ambienteService.findAmbienteActivo();
//        System.out.println("ambiente>>"+ambiente.getId());
        loadModel();
//        urlAutorizacion = Utils.existsURLActiva(ambiente.getWsUrlAutorizacion());
//        reseccion = Utils.existsURLActiva(ambiente.getWsUrlRecepcion());
//        System.out.println("aut>>" + urlAutorizacion + " _reseccion>>" + reseccion);
    }

    public void loadModel() {
        desde = new Date();
        hasta = new Date();
        identificacion = "";
        numeroAutorizacion = "";
        format = new SimpleDateFormat("yyyy-MM-dd");
        this.consultar();
    }

    public void consultar() {
        try {
            hasta = Utils.sumarRestarDiasFecha(hasta, 1);
            if (hasta.before(desde)) {
                JsfUtil.addInformationMessage("", "Error en fechas");
                return;
            }
            facturaLazy = new LazyModel<>(RenFactura.class);
//            facturaLazy.getFilterss().put("caja", cajero);
            if (desde != null && hasta != null && numeroAutorizacion != null && !numeroAutorizacion.isEmpty()) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("numeroAutorizacion", numeroAutorizacion);
            } else if (desde != null && hasta != null && !identificacion.isEmpty() && comprobante == null && idEstadoLiquidacion == null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("proveedor.cliente.identificacion", identificacion);
            } else if (desde != null && hasta != null && !identificacion.isEmpty() && comprobante != null && idEstadoLiquidacion == null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("proveedor.cliente.identificacion", identificacion);
                facturaLazy.getFilterss().put("comprobante", comprobante);
            } else if (desde != null && hasta != null && !identificacion.isEmpty() && comprobante != null && idEstadoLiquidacion != null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("proveedor.cliente.identificacion", identificacion);
                facturaLazy.getFilterss().put("comprobante", comprobante);
                facturaLazy.getFilterss().put("estadoLiquidacion.id", idEstadoLiquidacion);
            } else if (desde != null && hasta != null && !identificacion.isEmpty() && comprobante == null && idEstadoLiquidacion != null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("proveedor.cliente.identificacion", identificacion);
                facturaLazy.getFilterss().put("estadoLiquidacion.id", idEstadoLiquidacion);
            } else if (desde != null && hasta != null && identificacion.isEmpty() && comprobante != null && idEstadoLiquidacion == null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("comprobante", comprobante);
            } else if (desde != null && hasta != null && identificacion.isEmpty() && comprobante == null && idEstadoLiquidacion != null) {
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                facturaLazy.getFilterss().put("estadoLiquidacion.id", idEstadoLiquidacion);
            }else{
                facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultar(int tipo) {
        try {
            if (hasta.before(desde)) {
                JsfUtil.addInformationMessage("", "Error en fechas");
                return;
            }
            switch (tipo) {
                case 1:
                    if (desde != null && hasta != null) {
                        facturaLazy = new LazyModel<>(RenFactura.class);
                        facturaLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                        facturaLazy.getFilterss().put("comprobante", comprobante);
                    }
                    break;
                case 2:
                    if (desde != null && hasta != null) {
                        retencioLazy = new LazyModel<>(RenFactura.class);
                        retencioLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                        retencioLazy.getFilterss().put("comprobante", comprobante);
                    }
                    break;
                case 3:
                    if (desde != null && hasta != null) {
                        notaCreditoLazy = new LazyModel<>(RenFactura.class);
                        notaCreditoLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                        notaCreditoLazy.getFilterss().put("comprobante", comprobante);
                    }
                    break;
                case 4:
                    if (desde != null && hasta != null) {
                        notaDebitoLazy = new LazyModel<>(RenFactura.class);
                        notaDebitoLazy.getFilterss().put("fechaEmision:between", Arrays.asList(desde, hasta));
                        notaDebitoLazy.getFilterss().put("comprobante", comprobante);
                    }
                    break;
                case 5:
                    if (desde != null && hasta != null) {
                        autorizadotoLazy = new LazyModel<>(RenFactura.class);
                        autorizadotoLazy.getFilterss().put("numeroAutorizacion", numeroAutorizacion);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //envia la liquidacion a editar o visualizar
    public void editarVisualizar(RenFactura liquidacion, Boolean edit) {
        servletSession.borrarParametros();
        servletSession.addParametro("liquidacion", liquidacion);
        servletSession.addParametro("edit", edit);
        if (edit) {
            liquidacion.setEstadoLiquidacion(catalogoItemService.getCatalogoI("estado_liquidacion", "anulada"));
            liquidacion.setEstado(Boolean.FALSE);
            renFacturaService.edit(liquidacion);
            if (liquidacion.getDiario() != null) {
                liquidacion.getDiario().setRetenido(false);
                contDiarioGeneralService.edit(liquidacion.getDiario());
                for (RenFacturaDetalle dt : liquidacion.getRenFacturaDetalleList()) {
                    dt.getFactura().setRetenida(Boolean.FALSE);
                    facturaService.edit(dt.getFactura());
                }
            }
        }
        JsfUtil.redirectFaces("comprobantes-electronicos/retenciones");
    }

    public void reenviarComprobante(RenFactura liquidacion) {
        if (cajero != null) {
            switch (liquidacion.getComprobante().getCodigo()) {
                case "01"://FACTURAS
                    reenviarFacturaElectronica(liquidacion);
                    break;
                case "04"://NOTA DE CREDITO
                    break;
                case "05"://NOTA DE DEBITO
                    break;
                case "07"://COMPROBANTE RETENCION
                    reenviarComprobanteRetencion(liquidacion);
                    break;
            }

        } else {
            JsfUtil.addErrorMessage("", "No tiene Acceso a reenviar el comprobante");
        }
    }

    public void reenviarFacturaElectronica(RenFactura liquidacion) {
        recaudacionService.reenviarFactura(liquidacion, cajero);
    }

    public void reenviarComprobanteRetencion(RenFactura liquidacion) {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
        //liquidacion.setLiquidacionDetalles(liquidacionDetalleService.findAllLiquidacionDetalleByLiquidacion_Id(liquidacion.getId()));
        Factura factura = new Factura();
        for (RenFacturaDetalle detalle : liquidacion.getRenFacturaDetalleList()) {
            //LiquidacionDetalle d = liquidacionDetalleService.findById(detalle.getId());
            //d.setFacturaOpcional(liquidacionDetalleService.findFacturaByLiquidacionDetalle(detalle.getId()));
            //d.setFactura(d.getFacturaOpcional());

            impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
            impuestoComprobanteElectronico.setCodigo(detalle.getRubro().getRubroTipo().getCodigo());
            impuestoComprobanteElectronico.setCodigoPorcentaje(detalle.getRubro().getCodigo());
            impuestoComprobanteElectronico.setBaseImponible(detalle.getBaseImponible());
            impuestoComprobanteElectronico.setPorcentajeRetencion(detalle.getImpuesto());
            impuestoComprobanteElectronico.setValor(detalle.getValor());
            impuestoComprobanteElectronico.setCodigoDocumento(liquidacion.getComprobante().getCodigo()); //codDocSustento
            impuestoComprobanteElectronico.setNumDocumento(detalle.getFactura().getNumFactura().replace("-", "").trim()); //numDocSustento
            impuestoComprobanteElectronico.setFechaEmisionDocumento(format.format(detalle.getFactura().getFechaFactura())); //fechaEmisionDocSustento
            impuestoComprobanteRetencion.add(impuestoComprobanteElectronico);
            factura = (detalle.getFactura());
        }
        //para la fecha de emision de la factura. anteriormente salia la fecha del dia que se realizaba la emision del comprobante, 
        //solucion poner otro campo para
        //no modificar el codigo de envio de factura xD
        liquidacion.setFechaEmisionCabecera(factura.getFechaFactura());
        comprobanteElectronico = initComprobanteElectronico(liquidacion, liquidacion.getCaja());
        //COMPROBANTE RETENCION => 07
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.COMPPROBANTERETENCION);
        comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(factura.getFechaFactura()));
        comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobanteModifica().getCodigo());
        comprobanteElectronico.setNumComprobanteModifica(factura.getNumFactura().replace("-", "").trim());
        comprobanteElectronico.setMes(liquidacion.getMes().toString());
        comprobanteElectronico.setAnio(liquidacion.getAnio().toString());
        comprobanteElectronico.setImpuestoComprobanteRetencion(impuestoComprobanteRetencion);
        comprobanteElectronicaService.enviarComprobanteRentencionSRI(comprobanteElectronico);
        JsfUtil.addSuccessMessage("", "Comprobante Reenviado");
        loadModel();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void downloadFile(RenFactura l, Boolean xml) {
        Map<String, Object> parametros = new HashMap<String, Object>();
        servletSession.borrarParametros();
        String ruta = "";
        if (l.getClaveAcceso() == null || l.getClaveAcceso().isEmpty()) {
            JsfUtil.addInformationMessage("", "No se puede descargar el " + (xml ? "xml" : "RIDE"));
            return;
        }
        if (l.getEstadoWs().equals("RECIBIDA;AUTORIZADO")) {
            if (l.getComprobante().getCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
                ruta = "retencion_" + l.getCodigoComprobante().replace("-", "");
            } else if (l.getComprobante().getCodigo().equals(ComprobantesCode.FACTURA)) {
                ruta = "factura_" + l.getCodigoComprobante().replace("-", "");
            } else if (l.getComprobante().getCodigo().equals(ComprobantesCode.NOTACREDITO)) {
                ruta = "notacredito_" + l.getCodigoComprobante().replace("-", "");
            } else if (l.getComprobante().getCodigo().equals(ComprobantesCode.NOTADEBITO)) {
                ruta = "notadebito_" + l.getCodigoComprobante().replace("-", "");
            }
        }
//        System.out.println("directotio>>" + directorio.getRuta());//download
        parametros.put("download", true);
        servletSession.setParametros(parametros);
        if (xml) {
            servletSession.setContentType("text/xml");
            servletSession.setNombreDocumento(directorio.getRuta() + (ruta.isEmpty() ? l.getClaveAcceso() : ruta) + ".xml");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            servletSession.setContentType("application/pdf");
            servletSession.setNombreDocumento(directorio.getRuta() + (ruta.isEmpty() ? l.getClaveAcceso() : ruta) + ".pdf");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        }
    }

    public void eliminarComprobante(RenFactura liquidacion) {
        CatalogoItem aux = catalogoItemService.getEstadoRol("anulada");
        liquidacion.setEstadoLiquidacion(aux);
        liquidacion.setEstado(Boolean.FALSE);
        renFacturaService.edit(liquidacion);
        if (liquidacion.getDiario() != null) {
            liquidacion.getDiario().setRetenido(false);
            contDiarioGeneralService.edit(liquidacion.getDiario());
            for (RenFacturaDetalle dt : liquidacion.getRenFacturaDetalleList()) {
                dt.getFactura().setRetenida(Boolean.FALSE);
                facturaService.edit(dt.getFactura());
            }
        }
    }

    public void actualizarRespuesta(Liquidacion liquidacion) {
        if (liquidacion.getClaveAcceso() != null) {
            liquidacion.setNumeroAutorizacion(liquidacion.getClaveAcceso());
            liquidacion.setEstadoWs("RECIBIDA;AUTORIZADO");
            liquidacionService.edit(liquidacion);
        }
    }

    public boolean determinarActualizacion(RenFactura liquidacion) {
        boolean resultado = false;
        if (liquidacion.getEstadoWs() != null) {
            if (!liquidacion.getEstadoWs().equals("RECIBIDA;AUTORIZADO") && liquidacion.getEstado() && liquidacion.getEstadoLiquidacion() != null && !liquidacion.getEstadoLiquidacion().getCodigo().equals("anulada")) {
                resultado = true;
            }
        }
        return resultado;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public LazyModel<RenFactura> getRetencioLazy() {
        return retencioLazy;
    }

    public void setRetencioLazy(LazyModel<RenFactura> retencioLazy) {
        this.retencioLazy = retencioLazy;
    }

    public LazyModel<RenFactura> getNotaCreditoLazy() {
        return notaCreditoLazy;
    }

    public void setNotaCreditoLazy(LazyModel<RenFactura> notaCreditoLazy) {
        this.notaCreditoLazy = notaCreditoLazy;
    }

    public LazyModel<RenFactura> getNotaDebitoLazy() {
        return notaDebitoLazy;
    }

    public void setNotaDebitoLazy(LazyModel<RenFactura> notaDebitoLazy) {
        this.notaDebitoLazy = notaDebitoLazy;
    }

    public LazyModel<RenFactura> getFacturaLazy() {
        return facturaLazy;
    }

    public void setFacturaLazy(LazyModel<RenFactura> facturaLazy) {
        this.facturaLazy = facturaLazy;
    }

    public Long getIdEstadoLiquidacion() {
        return idEstadoLiquidacion;
    }

    public void setIdEstadoLiquidacion(Long idEstadoLiquidacion) {
        this.idEstadoLiquidacion = idEstadoLiquidacion;
    }

    public List<CatalogoItem> getEstadosLiquidaciones() {
        return estadosLiquidaciones;
    }

    public void setEstadosLiquidaciones(List<CatalogoItem> estadosLiquidaciones) {
        this.estadosLiquidaciones = estadosLiquidaciones;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<Comprobantes> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<Comprobantes> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public Comprobantes getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobantes comprobante) {
        this.comprobante = comprobante;
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
//</editor-fold>    
}
