package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaDetalle;
import com.origami.sigef.common.entities.RenFacturaPago;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RenFacturaDetalleService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.*;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.*;
import com.origami.sigef.tesoreria.service.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@Named(value = "notaDebitoView")
@ViewScoped
public class NotaDebitoController extends AccesosComprobanteElectronico implements Serializable {

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private RubroService rubroService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private LiquidacionDetalleService liquidacionDetalleService;
    @Inject
    private RenFacturaDetalleService facturaDetalleService;
    @Inject
    private LiquidacionMotivoService liquidacionMotivoService;
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private RenFacturaService renFacturaService;

    private Boolean accesoComprobante;
    private Cajero cajero;
    private Cliente cliente;
    private RenFactura liquidacion;
    private RenFacturaDetalle liquidacionDetalle;
    private List<RenFacturaDetalle> liquidacionDetalles;

    private List<LiquidacionMotivo> liquidacionMotivos;
    private LiquidacionMotivo liquidacionMotivo;

    private List<FormaPago> formaPagos;
    private RenFacturaPago liquidacionPago;
    private List<RenFacturaPago> liquidacionPagos;

    private List<Comprobantes> comprobantes;

    private SimpleDateFormat format;
    private List<Rubro> rubrosIVA, rubrosICE;
    private Rubro rubroIVA, rubroICE;

    private Boolean activarBtnDetalle;

    private BigDecimal valorIVA, subTotal, totalND, totalIVA, totalICE, totalFormaPago;
    private ComprobanteElectronico comprobanteElectronico;

    @PostConstruct
    public void init() {
        cajero = katalinaService.findCajero();
        if (cajero != null) {
            formaPagos = katalinaService.findAllFormaPago();

            comprobantes = katalinaService.findComprobantesRetienen();
            format = new SimpleDateFormat("yyyy-MM-dd");

            accesoComprobante = Boolean.TRUE;
            activarBtnDetalle = Boolean.FALSE;
            rubrosICE = rubroService.findRubrosByTipo(13); //ICE
            rubrosIVA = rubroService.findRubrosByTipo(12); //IVA
            iniciarVariables();
        } else {
            accesoComprobante = Boolean.FALSE;
        }
    }

    public void iniciarVariables() {

        liquidacion = new RenFactura();
        liquidacion.setFechaEmision(new Date());
        liquidacion.setFecha(new Date());
        liquidacionDetalle = new RenFacturaDetalle();
        liquidacionDetalles = new ArrayList<>();
        liquidacionPagos = new ArrayList<>();
        liquidacion.setProveedor(new Proveedor());
        liquidacion.setComprobante(katalinaService.findComprobante(ComprobantesCode.NOTADEBITO));
        valorIVA = BigDecimal.ZERO;
        totalND = BigDecimal.ZERO;
        subTotal = BigDecimal.ZERO;
        totalIVA = BigDecimal.ZERO;
        totalICE = BigDecimal.ZERO;
        totalFormaPago = BigDecimal.ZERO;

        liquidacionPago = new RenFacturaPago();
        liquidacionPago.setFormaPago(new FormaPago());
        liquidacionMotivos = new ArrayList<>();
        liquidacionMotivo = new LiquidacionMotivo();
        rubroICE = new Rubro();
        rubroIVA = new Rubro();
        cliente = new Cliente();
    }

    public void buscarCliente() {
        if (cliente.getIdentificacion().equals("")) {
            consultaProveedor();
        } else {
            if (cliente.getIdentificacion().length() < 13) {
                JsfUtil.addWarningMessage("AVISO!!", "El RUC ingresado le faltan digitos");
                return;
            } else {
                consultaProveedor();
            }
        }
        PrimeFaces.current().ajax().update("formMain:pnlDatos");
    }

    private void consultaProveedor() {
        Proveedor temp = new Proveedor();
        temp.setCliente(cliente);
        liquidacion.setProveedor(temp);
        temp = buscarCliente(liquidacion);
        if (temp != null) {
            liquidacion.setProveedor(temp);
        } else {
            liquidacion.setSolicitante(null);
        }
    }

    public void agregarMotivo() {
        if (rubroIVA == null) {
            JsfUtil.addErrorMessage("Debe Seleccionar el IVA", "");
            return;
        }
        if (liquidacionMotivo.getValor() == null) {
            JsfUtil.addErrorMessage("Debe Ingresar el Monto de la Nota de Débito", "");
            return;
        }
        if (liquidacionMotivo.getRazon() == null || liquidacionMotivo.getRazon().isEmpty()) {
            JsfUtil.addErrorMessage("Debe Ingresar el Motivo de la Nota de Débito", "");
            return;
        }
        if (totalICE.compareTo(BigDecimal.ZERO) != 0 && rubroICE == null) {
            JsfUtil.addErrorMessage("Si el valor del ICE es superior a 0, debe seleccionar el tipo", "");
            return;
        }
        liquidacionMotivo.setId(Utils.idTemp());
        liquidacionMotivos.add(liquidacionMotivo);
        calcularTotales(Boolean.FALSE);
        liquidacionMotivo = new LiquidacionMotivo();
    }

    public void removerMotivo(LiquidacionMotivo lm) {
        liquidacionMotivos.remove(lm);
//        calcularTotales(Boolean.TRUE);
    }

    public void calcularTotales(Boolean validaIVAICE) {
        if (validaIVAICE && rubroIVA == null) {
            JsfUtil.addErrorMessage("Debe Seleccionar el IVA", "");
            return;
        }
        if (validaIVAICE && totalICE.compareTo(BigDecimal.ZERO) != 0 && rubroICE == null) {
            totalICE = BigDecimal.ZERO;
            JsfUtil.addErrorMessage("Si el valor del ICE es superior a 0, debe seleccionar el tipo", "");
            return;
        }
        subTotal = BigDecimal.ZERO;
        for (LiquidacionMotivo motivo : liquidacionMotivos) {
            subTotal = subTotal.add(motivo.getValor());
        }
        valorIVA = new BigDecimal(rubroIVA.getValor());

        totalND = subTotal;
        System.out.println("valor iva--> " + valorIVA);
        if (valorIVA.compareTo(BigDecimal.ZERO) == 0) {
            if (totalICE != null) {
                totalND = totalND.add(totalICE);
            }
        } else {
            if (totalICE != null) {
                totalND = totalND.add(totalICE);
            }
            totalIVA = totalND.multiply(valorIVA).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
            System.out.println("total iva --> " + totalIVA);
            totalND = totalND.add(totalIVA);
        }
    }

    public void agregarFormaPago() {
        if (liquidacionPago.getFormaPago() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar una forma de pago");
            return;
        }
        if (liquidacionPago.getPlazo() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar el plazo");
            return;
        }
        if (liquidacionPago.getValor() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar el valor");
            return;
        }
        if (liquidacionPago.getTiempo() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar un tiempo");
            return;
        }
        Boolean existeFormaPago = Boolean.FALSE;
        for (RenFacturaPago pago : liquidacionPagos) {
            if (pago.getFormaPago().getCodigo().equals(liquidacionPago.getFormaPago().getCodigo())) {
                existeFormaPago = Boolean.TRUE;
                break;
            }
        }
        if (!existeFormaPago) {
            liquidacionPago.setId(Utils.idTemp());
            totalFormaPago = totalFormaPago.add(liquidacionPago.getValor());
            liquidacionPagos.add(liquidacionPago);
            liquidacionPago = new RenFacturaPago();
            liquidacionPago.setFormaPago(new FormaPago());
        } else {
            JsfUtil.addErrorMessage("La forma de pago ingresada ya existe eliminela y vuelva a intentar", "");
        }
    }

    public void removerFormaPago(LiquidacionPago liquidacionPago) {
        liquidacionPagos.remove(liquidacionPago);
    }

    public void guardarEmitirNotaDebito() {
        if (validar(liquidacion)) {
            if (liquidacionMotivos.isEmpty()) {
                JsfUtil.addErrorMessage("Debe ingresar los motivos de la Nota de Débito", "");
                return;
            }
            if (totalND.compareTo(totalFormaPago) == 0) {
                liquidacion = initLiquidacion(liquidacion);
                liquidacion.setTotalPagar(totalND);
                liquidacion.setSubTotal(subTotal);
                liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaNotaDebito()));
                liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
                liquidacion = renFacturaService.create(liquidacion);
                liquidacionDetalle.setRenFactura(liquidacion);
                liquidacionDetalle.setCantidad(1);
                liquidacionDetalle.setBaseImponible(subTotal);
                liquidacionDetalle.setRubro(rubroIVA);
                liquidacionDetalle.setRubroIce(rubroICE);
                liquidacionDetalle.setEstado(Boolean.TRUE);
                liquidacionDetalle.setImpuesto(totalIVA);
                liquidacionDetalle.setIce(totalICE);
                liquidacionDetalle.setValor(totalND);
                facturaDetalleService.create(liquidacionDetalle);

                List<MotivoNotaDebito> motivoNotaDebitos = new ArrayList<>();
                MotivoNotaDebito motivoNotaDebito;
                for (LiquidacionMotivo motivo : liquidacionMotivos) {
                    motivo.setId(null);
                    motivo.setLiquidacion(liquidacion);
                    liquidacionMotivoService.create(motivo);

                    motivoNotaDebito = new MotivoNotaDebito(motivo.getRazon(), motivo.getValor());
                    motivoNotaDebitos.add(motivoNotaDebito);

                }

                List<DetallePago> pagos = guardarLiquidacionPagos(liquidacion, liquidacionPagos);
                List<ImpuestoComprobanteElectronico> impuestosNotaDebitos = new ArrayList<>();
                ImpuestoComprobanteElectronico impuestoComprobanteElectronico;

                if (rubroIVA != null) {
                    impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo(rubroIVA.getRubroTipo().getCodigo());
                    impuestoComprobanteElectronico.setCodigoPorcentaje(rubroIVA.getCodigo());
                    impuestoComprobanteElectronico.setTarifa(rubroIVA.getPorcentaje());
                    impuestoComprobanteElectronico.setBaseImponible(subTotal);
                    impuestoComprobanteElectronico.setValor(totalIVA);
                    impuestosNotaDebitos.add(impuestoComprobanteElectronico);
                }
                if (rubroICE != null) {
                    impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo(rubroICE.getRubroTipo().getCodigo());
                    impuestoComprobanteElectronico.setCodigoPorcentaje(rubroICE.getCodigo());
                    impuestoComprobanteElectronico.setTarifa(rubroICE.getPorcentaje());
                    impuestoComprobanteElectronico.setBaseImponible(subTotal.subtract(totalICE));
                    impuestoComprobanteElectronico.setValor(totalICE);
                    impuestosNotaDebitos.add(impuestoComprobanteElectronico);
                }

                comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);

                comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.NOTADEBITO);
                comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(liquidacion.getFechaEmision()));
                comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobante().getCodigo());
                comprobanteElectronico.setNumComprobanteModifica(liquidacion.getCodigoComprobanteRetenido());
                comprobanteElectronico.setValorTotalNotaDebito(totalND);

                comprobanteElectronico.setDetallePagos(pagos);
                comprobanteElectronico.setMotivosNotaDebito(motivoNotaDebitos);
                comprobanteElectronico.setImpuestosNotaDebitos(impuestosNotaDebitos);

                comprobanteElectronicaService.enviarNotaDebitoSRI(comprobanteElectronico);
                iniciarVariables();
            } else {
                JsfUtil.addErrorMessage("Los valores de la forma de pago coincidir con el total al pagar", "");
            }
        }
    }

    public void selectData(SelectEvent evt) {
        liquidacion.setProveedor((Proveedor) evt.getObject());
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
    
    public List<Comprobantes> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<Comprobantes> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public List<Rubro> getRubrosIVA() {
        return rubrosIVA;
    }

    public void setRubrosIVA(List<Rubro> rubrosIVA) {
        this.rubrosIVA = rubrosIVA;
    }

    public List<Rubro> getRubrosICE() {
        return rubrosICE;
    }

    public void setRubrosICE(List<Rubro> rubrosICE) {
        this.rubrosICE = rubrosICE;
    }

    public Rubro getRubroIVA() {
        return rubroIVA;
    }

    public void setRubroIVA(Rubro rubroIVA) {
        this.rubroIVA = rubroIVA;
    }

    public Rubro getRubroICE() {
        return rubroICE;
    }

    public void setRubroICE(Rubro rubroICE) {
        this.rubroICE = rubroICE;
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


    public BigDecimal getValorIVA() {
        return valorIVA;
    }

    public void setValorIVA(BigDecimal valorIVA) {
        this.valorIVA = valorIVA;
    }

    public Boolean getActivarBtnDetalle() {
        return activarBtnDetalle;
    }

    public void setActivarBtnDetalle(Boolean activarBtnDetalle) {
        this.activarBtnDetalle = activarBtnDetalle;
    }

    public List<FormaPago> getFormaPagos() {
        return formaPagos;
    }

    public void setFormaPagos(List<FormaPago> formaPagos) {
        this.formaPagos = formaPagos;
    }

    public RenFacturaPago getLiquidacionPago() {
        return liquidacionPago;
    }

    public void setLiquidacionPago(RenFacturaPago liquidacionPago) {
        this.liquidacionPago = liquidacionPago;
    }

    public List<RenFacturaPago> getLiquidacionPagos() {
        return liquidacionPagos;
    }

    public void setLiquidacionPagos(List<RenFacturaPago> liquidacionPagos) {
        this.liquidacionPagos = liquidacionPagos;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotalND() {
        return totalND;
    }

    public void setTotalND(BigDecimal totalND) {
        this.totalND = totalND;
    }

    public BigDecimal getTotalIVA() {
        return totalIVA;
    }

    public void setTotalIVA(BigDecimal totalIVA) {
        this.totalIVA = totalIVA;
    }

    public BigDecimal getTotalICE() {
        return totalICE;
    }

    public void setTotalICE(BigDecimal totalICE) {
        this.totalICE = totalICE;
    }

    public List<LiquidacionMotivo> getLiquidacionMotivos() {
        return liquidacionMotivos;
    }

    public void setLiquidacionMotivos(List<LiquidacionMotivo> liquidacionMotivos) {
        this.liquidacionMotivos = liquidacionMotivos;
    }

    public LiquidacionMotivo getLiquidacionMotivo() {
        return liquidacionMotivo;
    }

    public void setLiquidacionMotivo(LiquidacionMotivo liquidacionMotivo) {
        this.liquidacionMotivo = liquidacionMotivo;
    }
}
