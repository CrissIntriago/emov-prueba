/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import com.origami.sigef.tesoreria.service.RenFacturaService;
import com.origami.sigef.tesoreria.service.RubroService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author gutya
 */
@Named(value = "notaCreditoView")
@ViewScoped
public class NotaCreditoController extends AccesosComprobanteElectronico implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(NotaCreditoController.class.getName());
    
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private RubroService rubroService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private RenFacturaService renFacturaService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ManagerService manager;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private NotaCreditoServices notasCreditoServices;
    @Inject
    private RecaudacionInteface recaidacionService;
    @Inject
    private ServletSession ss;
    
    private Boolean accesoComprobante;
    private Cajero cajero;
    private RenFactura liquidacion, liquidacionConsultada;
    private ComprobanteElectronico comprobanteElectronico;
    
    private BigInteger numeroComprobante;
    private String codigoComprobante, numAutorizacion, ciCliente;
    private BigDecimal totalFacturaConsultada;
    protected Cliente beneficiario;
    private List<CatalogoItem> tiposIdentificacionList;
    private CatalogoItem tipoIdentificacion;
    private NotasCredito notaCredito;
    private Comprobantes comprobante;
    
    @PostConstruct
    public void init() {
        cajero = katalinaService.findCajero();
        this.notaCredito = new NotasCredito();
        this.tiposIdentificacionList = catalogoService.getItemsByCatalogo("tipo_identificacion_beneficiario");
        this.tipoIdentificacion = tiposIdentificacionList.get(0);
        if (cajero != null) {
            accesoComprobante = Boolean.TRUE;
            iniciarVariables();
        } else {
            accesoComprobante = Boolean.FALSE;
        }
//        iniciarVariables();
    }
    
    public void clearInit() {
        this.notaCredito = new NotasCredito();
        ciCliente = "";
        beneficiario = new Cliente();
        this.tipoIdentificacion = tiposIdentificacionList.get(0);
        liquidacion = new RenFactura();
        liquidacion.setFechaEmision(new Date());
        liquidacion.setFecha(new Date());
        liquidacion.setComprobante(comprobante);
    }
    
    private void iniciarVariables() {
        liquidacion = new RenFactura();
        liquidacion.setFechaEmision(new Date());
        liquidacion.setFecha(new Date());
        comprobante = katalinaService.findComprobante(ComprobantesCode.NOTACREDITO);
        liquidacion.setComprobante(comprobante);
    }
    
    public void buscarFactura() {
        if (numeroComprobante != null) {
            liquidacionConsultada = liquidacionService.findByNumeroComprobanteRenFactura(numeroComprobante);
            if (liquidacionConsultada != null) {
                codigoComprobante = liquidacionConsultada.getCodigoComprobante();
                numAutorizacion = liquidacionConsultada.getNumeroAutorizacion();
                totalFacturaConsultada = liquidacionConsultada.getTotalPagar();
                
                liquidacion.setTotalPagar(BigDecimal.ZERO);
                liquidacion.setSubTotal(BigDecimal.ZERO);
                liquidacion.setIdFacruraFefenrencia(liquidacionConsultada.getId());
                if (liquidacionConsultada.getSolicitante() != null && liquidacionConsultada.getSolicitante().getId() != null) {
                    liquidacion.setSolicitante(new Cliente());
                    liquidacion.setSolicitante(liquidacionConsultada.getSolicitante());
                } else if (liquidacion.getProveedor() != null && liquidacion.getProveedor().getId() != null) {
                    liquidacion.setProveedor(new Proveedor());
                    liquidacion.setProveedor(liquidacion.getProveedor());
                }
                ciCliente = liquidacion.getSolicitante().getIdentificacion();
                beneficiario = liquidacion.getSolicitante();
                liquidacionConsultada.setCodigoComprobante(codigoComprobante);
                liquidacionConsultada.setNumeroAutorizacion(numAutorizacion);
                liquidacionConsultada.setTotalPagar(totalFacturaConsultada);
            } else {
                iniciarVariables();
                JsfUtil.addErrorMessage("No se encontro Factura", "");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar un numero de comporbante");
        }
    }
    
    public void guardarNotaCreditoMovimiento() {
        notaCredito = new NotasCredito();
        notaCredito.setContribuyente(beneficiario);
        notaCredito.setValor(liquidacion.getTotalPagar());
        notaCredito.setValorPagado(liquidacion.getTotalPagar());
        notaCredito.setValorPagado(liquidacion.getTotalPagar());
        notaCredito.setSaldo(liquidacion.getTotalPagar());
        notaCredito.setTipoNota(BigInteger.ONE);
        notaCredito.setFechaIngreso(liquidacionConsultada.getFechaEmision());
        notaCredito.setUsuarioIngreso(userSession.getNameUser());
        notaCredito.setNotaCredito(liquidacion);
        notaCredito.setEstado(Short.parseShort("1"));
        notaCredito = notasCreditoServices.create(notaCredito);
        this.generarNotaCredito();
    }
    
    public void buscarEnte() {
        try {
            if (ciCliente != null) {
                Cliente ve = clienteService.getClienteByIdentificacionCompleta(ciCliente, tipoIdentificacion);
                if (ve != null) {
                    beneficiario = new Cliente();
                    beneficiario = ve;
                } else {
                    beneficiario = new Cliente();
                    JsfUtil.addWarningMessage("No Se Encontraron datos..", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUtil.addInformationMessage("Advertencia", "ERRO DE APLICAIÓn");
        }
    }
    
    public void guardarEmitirNotaCredito() {
        if (liquidacion.getSolicitante() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un Cliente", "");
            return;
        }
        if (liquidacion.getObservacion() == null || liquidacion.getObservacion().isEmpty()) {
            JsfUtil.addErrorMessage("Debe ingresar el Motivo ", "");
            return;
        }
        if (liquidacion.getTotalPagar() == null || liquidacion.getTotalPagar().compareTo(BigDecimal.ZERO) == 0) {
            JsfUtil.addErrorMessage("Debe ingresar el Valor de la Nota de Crédito ", "");
            return;
        }
        if (liquidacion.getTotalPagar().compareTo(liquidacionConsultada.getTotalPagar()) == 1) {
            JsfUtil.addErrorMessage("Debe ingresar el Valor de la Nota de Crédito no debe ser mayor al de la factura ", "");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            liquidacion = initLiquidacion(liquidacion);
            liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaNotaCredito()));
            liquidacion.setCodigoComprobante(String.format(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + "%09d", liquidacion.getNumeroComprobante()));
            liquidacion.setCodigoComprobanteRetenido(liquidacionConsultada.getCodigoComprobante().replace("-", ""));
            liquidacion = renFacturaService.create(liquidacion);
            this.guardarNotaCreditoMovimiento();
            
            liquidacionConsultada.setRetenido(Boolean.TRUE);
            renFacturaService.edit(liquidacionConsultada);
            comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
            comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.NOTACREDITO);
            comprobanteElectronico.setNumComprobanteModifica(liquidacionConsultada.getCodigoComprobante().replace("-", ""));
            comprobanteElectronico.setMotivoNotaCredito(liquidacion.getObservacion());
            comprobanteElectronico.setTipoDocumentoModifica(ComprobantesCode.FACTURA);
            
            comprobanteElectronico.setFechaEmisionDocumentoModifica(sdf.format(liquidacionConsultada.getFechaAutorizacion()));
            
            List<Detalle> detalles = new ArrayList<>();
            Detalle detalle = new Detalle();
            detalle.setDescripcion("FACTURA " + liquidacionConsultada.getCodigoComprobante());
            detalle.setValorTotal(liquidacion.getTotalPagar().doubleValue());
            detalle.setValorUnitario(liquidacion.getTotalPagar().doubleValue());
            detalle.setRecargo(0.0);
            detalle.setDescuento(0.0);
            detalle.setCantidad(1);
            detalle.setIva(0.00);
            detalle.setCodigoTarifa("6");
            detalles.add(detalle);
            comprobanteElectronico.setDetalle(detalles);
            comprobanteElectronicaService.enviarNotaCreditoSRI(comprobanteElectronico);
            if (recaidacionService.anularPagoLiquidacion(liquidacionConsultada)) {
                JsfUtil.addInformationMessage("Nota de Credito Generada con Exito...", "");
            }
            this.clearInit();
        } catch (Exception e) {
            JsfUtil.addWarningMessage("Error de la Aplicación...", numAutorizacion);
            LOG.log(Level.SEVERE, "guardarEmitirNotaCredito>>", e);
        }
        
    }
    
    public void generarNotaCredito() {
        ss.setNombreSubCarpeta("GestionTributatia/comprobantes");
        ss.setNombreReporte("sNotadeCredito");
        ss.addParametro("IDNOTA", notaCredito.getId());
        ss.addParametro("LOGO_R", JsfUtil.getRealPath("resources/images/duranLogo.png"));
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }
    
    public Cajero getCajero() {
        return cajero;
    }
    
    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }
    
    public RenFactura getLiquidacion() {
        return liquidacion;
    }
    
    public void setLiquidacion(RenFactura liquidacion) {
        this.liquidacion = liquidacion;
    }
    
    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }
    
    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
    
    public Boolean getAccesoComprobante() {
        return accesoComprobante;
    }
    
    public void setAccesoComprobante(Boolean accesoComprobante) {
        this.accesoComprobante = accesoComprobante;
    }
    
    public RenFactura getLiquidacionConsultada() {
        return liquidacionConsultada;
    }
    
    public void setLiquidacionConsultada(RenFactura liquidacionConsultada) {
        this.liquidacionConsultada = liquidacionConsultada;
    }
    
    public String getCiCliente() {
        return ciCliente;
    }
    
    public void setCiCliente(String ciCliente) {
        this.ciCliente = ciCliente;
    }
    
    public Cliente getBeneficiario() {
        return beneficiario;
    }
    
    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }
    
    public List<CatalogoItem> getTiposIdentificacionList() {
        return tiposIdentificacionList;
    }
    
    public void setTiposIdentificacionList(List<CatalogoItem> tiposIdentificacionList) {
        this.tiposIdentificacionList = tiposIdentificacionList;
    }
    
    public CatalogoItem getTipoIdentificacion() {
        return tipoIdentificacion;
    }
    
    public void setTipoIdentificacion(CatalogoItem tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }
    
    public NotasCredito getNotaCredito() {
        return notaCredito;
    }
    
    public void setNotaCredito(NotasCredito notaCredito) {
        this.notaCredito = notaCredito;
    }
    
}
