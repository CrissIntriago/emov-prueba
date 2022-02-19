/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Exoneracion;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.entities.LiquidacionPago;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.service.ExoneracionService;
import com.origami.sigef.tesoreria.service.LiquidacionDetalleService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import com.origami.sigef.tesoreria.service.RubroService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author gutya
 */
@Named(value = "facturaView")
@ViewScoped
public class FacturaController extends AccesosComprobanteElectronico implements Serializable {

    private static final long serialVersionUID = 1L;
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
    private ExoneracionService exoneracionService;
    @Inject
    private SeqGenMan seqGenManService;

    private Boolean accesoComprobante;
    private Cajero cajero;
    private Cliente cliente;
    private Liquidacion liquidacion;
    private LiquidacionDetalle liquidacionDetalle;
    private List<LiquidacionDetalle> liquidacionDetalles;
    private List<Rubro> rubros;
    private Rubro rubro, rubroIVA;
    private ComprobanteElectronico comprobanteElectronico;

    private List<FormaPago> formaPagos;
    private LiquidacionPago liquidacionPago;
    private List<LiquidacionPago> liquidacionPagos;

    private BigDecimal subTotal, totalIVA, totalPago, totalFormaPago, totalDescuento, valorItem;

    private List<Exoneracion> exoneraciones;
    private Exoneracion exoneracion;

    @PostConstruct
    public void init() {
        cajero = katalinaService.findCajero();
        if (cajero != null) {
            rubroIVA = katalinaService.findPredeterminadoTipo(12, true);
            accesoComprobante = Boolean.TRUE;
            formaPagos = katalinaService.findAllFormaPago();
            rubros = rubroService.findRubrosByTipo(14); //FACTURAS
            iniciarVariables();
        } else {
            accesoComprobante = Boolean.FALSE;
        }
    }

    public void iniciarVariables() {
        liquidacion = new Liquidacion();
        liquidacion.setFechaEmision(new Date());
        liquidacion.setFechaIngreso(new Date());
        liquidacion.setCliente(new Cliente());
        liquidacion.setComprobante(katalinaService.findComprobante(ComprobantesCode.FACTURA));
        liquidacionDetalle = new LiquidacionDetalle();
        liquidacionDetalles = new ArrayList<>();

        liquidacionPagos = new ArrayList<>();
        liquidacionPago = new LiquidacionPago();
        liquidacionPago.setId(new Random(System.currentTimeMillis()).nextLong());
        liquidacionPago.setFormaPago(formaPagos.get(0));

        totalIVA = BigDecimal.ZERO;
        totalPago = BigDecimal.ZERO;
        totalFormaPago = BigDecimal.ZERO;
        totalDescuento = BigDecimal.ZERO;
        subTotal = BigDecimal.ZERO;
    }

    public void buscarCliente() {
//        Cliente c = buscarClienteByLiquidacion(liquidacion);
//        if (c != null) {
//            liquidacion.setCliente(c);
//        }
    }

    public void selectData(SelectEvent evt) {
        liquidacion.setCliente((Cliente) evt.getObject());
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
        for (LiquidacionPago pago : liquidacionPagos) {
            if (pago.getFormaPago().getCodigo().equals(liquidacionPago.getFormaPago().getCodigo())) {
                existeFormaPago = Boolean.TRUE;
                break;
            }
        }
        if (!existeFormaPago) {
            liquidacionPago.setId(new Random(System.currentTimeMillis()).nextLong());
            totalFormaPago = totalFormaPago.add(liquidacionPago.getValor());
            liquidacionPagos.add(liquidacionPago);
            liquidacionPago = new LiquidacionPago();
            liquidacionPago.setFormaPago(new FormaPago());
        } else {
            JsfUtil.addErrorMessage("La forma de pago ingresada ya existe eliminela y vuelva a intentar", "");
        }
    }

    public void removerFormaPago(LiquidacionPago liquidacionPago) {
        liquidacionPagos.remove(liquidacionPago);
    }

    public void selectRubro() {
        if (validarRubroSeleccionado()) {
            liquidacionDetalle.setRubro(rubro);
            liquidacionDetalle.setCantidad(1);
            liquidacionDetalle.setBaseImponible(new BigDecimal(rubro.getValor()));
            liquidacionDetalle.setValor(new BigDecimal(rubro.getValor()));
            liquidacionDetalle.setEstado(Boolean.TRUE);
            liquidacionDetalle.setValorRecaudado(BigDecimal.ZERO);
            liquidacionDetalle.setValorDescuento(BigDecimal.ZERO);
            JsfUtil.executeJS("PF('dlgRubroValor').show()");
        } else {
            JsfUtil.addErrorMessage("Rubro ya fue seleccionado", "");
        }
    }

    private Boolean validarRubroSeleccionado() {
        for (LiquidacionDetalle det : liquidacionDetalles) {
            if (det.getRubro().getId().equals(rubro.getId())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void agregarDetalle() {
        liquidacionDetalle.setId(Utils.idTemp());
        liquidacionDetalles.add(liquidacionDetalle);
        subTotal = BigDecimal.ZERO;
        totalDescuento = BigDecimal.ZERO;
        for (LiquidacionDetalle det : liquidacionDetalles) {
            subTotal = subTotal.add(det.getBaseImponible());
            totalDescuento = totalDescuento.add(det.getValorDescuento());
        };
        totalPago = subTotal.subtract(totalDescuento);
        liquidacionPago.setValor(totalPago);
        liquidacionDetalle = new LiquidacionDetalle();
        rubro = new Rubro();
    }

    public void consideraIVA() {
        if (liquidacion.getConsideraIva()) {
            totalIVA = totalPago.multiply(new BigDecimal(rubroIVA.getValor())).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
            totalPago = totalPago.add(totalIVA);
            liquidacionPago.setValor(totalPago);
        } else {
            totalIVA = BigDecimal.ZERO;
            totalPago = subTotal;
            liquidacionPago.setValor(totalPago);
        }
    }

    public void calcularRubro() {
        valorItem = liquidacionDetalle.getBaseImponible().multiply(new BigDecimal(liquidacionDetalle.getCantidad()));
        if (liquidacionDetalle.getExoneracion() != null) {
            if (liquidacionDetalle.getExoneracion().getId() != null) {
                liquidacionDetalle.setValorDescuento(valorItem.multiply(liquidacionDetalle.getExoneracion().getValor()).setScale(2, RoundingMode.HALF_UP));
                liquidacionDetalle.setValor(valorItem.subtract(liquidacionDetalle.getValorDescuento()).setScale(2, RoundingMode.HALF_UP));
            } else {
                liquidacionDetalle.setValor(valorItem.setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            liquidacionDetalle.setValor(valorItem.setScale(2, RoundingMode.HALF_UP));
        }
    }

    public void loadExoneraciones() {
        exoneraciones = exoneracionService.findAllExoneracion();
    }

    public void removerExoneracion() {
        liquidacionDetalle.setExoneracion(null);
        liquidacionDetalle.setValorDescuento(BigDecimal.ZERO);
        liquidacionDetalle.setValor(
                liquidacionDetalle.getBaseImponible().multiply(new BigDecimal(liquidacionDetalle.getCantidad())));
    }

    public void selectExoneracion(Exoneracion exo) {
        exoneracion = exo;
        liquidacionDetalle.setExoneracion(exoneracion);
        calcularRubro();
    }

    public void removerDetalle(Integer index) {
        liquidacionDetalles.remove(index.intValue());
        subTotal = BigDecimal.ZERO;
        totalPago = BigDecimal.ZERO;
        if (Utils.isNotEmpty(liquidacionDetalles)) {
            liquidacionDetalles.forEach((det) -> {
                subTotal = subTotal.add(det.getValor());
            });
            totalPago = subTotal;
            liquidacionPago.setValor(totalPago);
            consideraIVA();
        } else {
            liquidacion.setConsideraIva(Boolean.FALSE);
            totalIVA = BigDecimal.ZERO;
            liquidacionPago.setValor(totalPago);
        }

    }

    public void guardarEmitirFactura() {
        if (liquidacion.getCliente() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un Cliente", "");
            return;
        }
        if (Utils.isEmpty(liquidacionPagos)) {
            JsfUtil.addErrorMessage("Debe ingresar las Formas de Pago", "");
            return;
        }
        if (Utils.isEmpty(liquidacionDetalles)) {
            JsfUtil.addErrorMessage("Debe ingresar el Detalle de la Factura", "");
            return;
        }

        if (totalPago.compareTo(totalFormaPago) == 0) {
            liquidacion = initLiquidacion(liquidacion);
            liquidacion.setTotalPagar(totalPago);
            liquidacion.setDescuentoValor(totalDescuento);
            liquidacion.setSubTotal(subTotal);
            liquidacion.setNumeroComprobante(seqGenManService.getSecuenciaAmbiente(cajero.getVariableSecuenciaFacturas()));
            liquidacion.setCodigoComprobante(cajero.getEntidad().getEstablecimiento() + "-" + cajero.getPuntoEmision() + "-" + String.format("%09d", liquidacion.getNumeroComprobante()));
            liquidacion = liquidacionService.create(liquidacion);
            Detalle detalle;
            List<Detalle> detalles = new ArrayList<>();

            for (LiquidacionDetalle det : liquidacionDetalles) {
                det.setId(null);
                if (det.getExoneracion() != null && det.getExoneracion().getId() == null) {
                    det.setExoneracion(null);
                }
                det.setLiquidacion(liquidacion);
                det = liquidacionDetalleService.create(det);

                detalle = new Detalle();
                detalle.setCodigoPrincipal(det.getRubro().getId().toString());
                detalle.setDescripcion(det.getRubro().getDescripcion());
                detalle.setValorTotal(det.getValor().doubleValue());
                detalle.setValorUnitario(det.getBaseImponible().doubleValue());
                detalle.setRecargo(0.0);
                detalle.setDescuento(det.getValorDescuento().doubleValue());
                detalle.setCantidad(det.getCantidad());
                detalle.setIva(0.00);
                detalle.setCodigoTarifa("6");
                detalles.add(detalle);
            }
            List<DetallePago> pagos = guardarLiquidacionPagos(liquidacion, liquidacionPagos);
            comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
            comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.FACTURA);
            comprobanteElectronico.setDetallePagos(pagos);
            comprobanteElectronico.setDetalle(detalles);
            comprobanteElectronico.setTipoLiquidacion("RET");
            comprobanteElectronicaService.enviarFacturaElectronicaSRI(comprobanteElectronico);
            iniciarVariables();
        } else {
            JsfUtil.addErrorMessage("Los valores de la forma de pago deben coincidir con el total al pagar", "");
        }
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

    public ComprobanteElectronico getComprobanteElectronico() {
        return comprobanteElectronico;
    }

    public void setComprobanteElectronico(ComprobanteElectronico comprobanteElectronico) {
        this.comprobanteElectronico = comprobanteElectronico;
    }

    public List<FormaPago> getFormaPagos() {
        return formaPagos;
    }

    public void setFormaPagos(List<FormaPago> formaPagos) {
        this.formaPagos = formaPagos;
    }

    public LiquidacionPago getLiquidacionPago() {
        return liquidacionPago;
    }

    public void setLiquidacionPago(LiquidacionPago liquidacionPago) {
        this.liquidacionPago = liquidacionPago;
    }

    public List<LiquidacionPago> getLiquidacionPagos() {
        return liquidacionPagos;
    }

    public void setLiquidacionPagos(List<LiquidacionPago> liquidacionPagos) {
        this.liquidacionPagos = liquidacionPagos;
    }

    public List<Rubro> getRubros() {
        return rubros;
    }

    public void setRubros(List<Rubro> rubros) {
        this.rubros = rubros;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotalFormaPago() {
        return totalFormaPago;
    }

    public void setTotalFormaPago(BigDecimal totalFormaPago) {
        this.totalFormaPago = totalFormaPago;
    }

    public BigDecimal getTotalIVA() {
        return totalIVA;
    }

    public void setTotalIVA(BigDecimal totalIVA) {
        this.totalIVA = totalIVA;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public List<Exoneracion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(List<Exoneracion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public Exoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(Exoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

}
