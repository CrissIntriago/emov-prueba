/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.arrendamiento.entities.OrdenesEmitidas;
import com.origami.sigef.arrendamiento.service.OrdenesEmitidasService;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.tesoreria.comprobantelectronico.config.AccesosComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.MotivoNotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.entities.LiquidacionMotivo;
import com.origami.sigef.tesoreria.entities.LiquidacionPago;
import com.origami.sigef.tesoreria.service.LiquidacionPagoService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author OrigamiEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class EnviarDocumentosElectronicosPendientes extends AccesosComprobanteElectronico implements Serializable {

    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject
    private LiquidacionPagoService liquidacionPagoService;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private OrdenesEmitidasService ordenEmitidaService;

    private ComprobanteElectronico comprobanteElectronico;
    private Cajero cajero;
    private SimpleDateFormat format;
    private Comprobantes comprobante;
    private Comprobantes comprobante_;

    /**
     * ENVIA FACTURAS PENDIENTES A LA 23 HORA Y 55 MINUTOS
     */
//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*",
//            year = "*", minute = "30", second = "0", persistent = false)
//    @AccessTimeout(value = -1)
    public void enviarFacturasPendientes() {
        comprobante = new Comprobantes();
        comprobante = liquidacionService.getTipoComprobante("01");
        comprobante_ = new Comprobantes();
        comprobante_ = liquidacionService.getTipoComprobante("18");
        List<Liquidacion> lista = liquidacionService._getLiquidacionPendientes(comprobante,comprobante_);
        if (!lista.isEmpty()) {
            for (Liquidacion lq : lista) {
                System.out.println("ENVIAR FACTURAS PENDIENTES");
                reenviarDocumentoElectronica(lq, 1);
            }
        } else {
            System.out.println("NO HAY FACTURAS PARA ENVIAR");
        }
    }

    /**
     * ENVIA NOTAS DE CREDITOS PENDIENTES A LA 23 HORA Y 55 MINUTOS
     */
//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*",
//            year = "*", minute = "40", second = "0", persistent = false)
//    @AccessTimeout(value = -1)
    public void enviarNotasCreditosPendientes() {
        comprobante = new Comprobantes();
        comprobante = liquidacionService.getTipoComprobante("04");
//        System.out.println("enviar comprobantes-->");
        List<Liquidacion> lista = liquidacionService.getLiquidacionPendientes(comprobante);
        if (!lista.isEmpty()) {
            for (Liquidacion lq : lista) {
                reenviarDocumentoElectronica(lq, 2);
            }
        }
    }

    /**
     * ENVIA NOTAS DE DEBITOS PENDIENTES A LA 23 HORA Y 55 MINUTOS
     */
//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*",
//            year = "*", minute = "50", second = "0", persistent = false)
//    @AccessTimeout(value = -1)
    public void enviarNotaDebitosPendientes() {
        comprobante = new Comprobantes();
        comprobante = liquidacionService.getTipoComprobante("05");
        List<Liquidacion> lista = liquidacionService.getLiquidacionPendientes(comprobante);
        if (!lista.isEmpty()) {
            for (Liquidacion lq : lista) {
                reenviarNotaDebito(lq);
            }
        }
    }

//    /**
//     *
//     * ENVIA RETENCIONES PENDIENTES A LA 23 HORAS Y 30 MINUTOS
//     *
//     */
//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*",
//            year = "*", minute = "30", second = "0", persistent = false)
//    @AccessTimeout(value = -1)
//    public void enviarRetencionesPrendientes() {
//        comprobante = new Comprobantes();
//        comprobante = liquidacionService.getTipoComprobante("05");
////        comprobante = liquidacionService.getTipoComprobante("05");
//        System.out.println("comprobante ---> " + comprobante);
//        List<Liquidacion> lista = liquidacionService.getLiquidacionPendientes(comprobante);
//        if (!lista.isEmpty()) {
//            for (Liquidacion l : lista) {
//                System.out.println("ejecutando---> " + l.getCodigoComprobante());
//                reenviarComprobanteRetencion(l);
//            }
//        }
//    }
        public void reenviarDocumentoElectronica(Liquidacion liquidacion, int tipo) {
        Detalle detalle;
        Boolean accion = false;
        List<Detalle> detalles = new ArrayList<>();
        String Descripcion = "";
        String CodigoTarifa = "";
        String CodigoPrincipal = "";
        OrdenesEmitidas orden = new OrdenesEmitidas();
        int aux = 1;
        for (LiquidacionDetalle det : liquidacion.getLiquidacionDetalles()) {
            if (det.getExoneracion() != null && det.getExoneracion().getId() == null) {
                det.setExoneracion(null);
            }
            detalle = new Detalle();
            if (det.getRubro() != null) {
                CodigoPrincipal = det.getRubro().getId().toString();
                Descripcion = det.getRubro().getDescripcion();
                detalle.setIva(0.00);
                CodigoTarifa = "6";
            } else {
                accion = true;
                orden = ordenEmitidaService.getOrden(liquidacion);
                if (orden.getId() == null) {
                    return;
                }
                CodigoPrincipal = orden.getIdArrendamiento().getLocal().getIdTarifa().getCodigo();
                if (aux == 1) {
                    Descripcion = "ARRIENDO OPERADORA";
                    CodigoTarifa = "2";
                    detalle.setIva((double) 12);
                } else {
                    Descripcion = "ALICUOTA";
                    CodigoTarifa = "6";
                    detalle.setIva((double) 0);
                }
            }
            detalle.setCodigoPrincipal(CodigoPrincipal);
            detalle.setDescripcion(Descripcion);
            detalle.setValorTotal(det.getValor().doubleValue());
            detalle.setValorUnitario(det.getBaseImponible() != null ? det.getBaseImponible().doubleValue() : 0);
            detalle.setRecargo(0.0);
            detalle.setDescuento(det.getValorDescuento() != null ? det.getValorDescuento().doubleValue() : 0);
            detalle.setCantidad(det.getCantidad());
            detalle.setCodigoTarifa(CodigoTarifa);
            detalles.add(detalle);
            aux++;
        }

//        List<DetallePago> pagos = obtenerLiquidacionPagos(liquidacionPagoService.findAllLiquidacionPagoByLiquidacion(liquidacion)); 
//        cajero = cajeroService.findByCajero(usuarioService.findByUsuarioId(liquidacion.getUserCreador()).getUsuario());
//        comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
//        comprobanteElectronico.setTipoLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo());
//        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.FACTURA);
//        comprobanteElectronico.setDetallePagos(pagos);
//        comprobanteElectronico.setDetalle(detalles);
        if (orden.getId() != null) {
            comprobanteElectronico.setTramite(Long.parseLong(orden.getIdSolicitud()));
        } else {
            comprobanteElectronico.setTramite(null);
        }
        switch (tipo) {
            case 1:
                System.out.println("CASE: reenviarFacturaElectronicaSRI()");
                comprobanteElectronicaService.reenviarFacturaElectronicaSRI(comprobanteElectronico);
                break;
            case 2:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                comprobanteElectronico.setFechaEmisionDocumentoModifica(sdf.format(liquidacion.getFechaComprobanteRetenido()));
                comprobanteElectronico.setNumComprobanteModifica(liquidacion.getCodigoComprobanteRetenido());
                comprobanteElectronico.setMotivoNotaCredito(liquidacion.getObservacion());
                comprobanteElectronico.setTipoDocumentoModifica(ComprobantesCode.FACTURA);
                System.out.println(" to string " + comprobanteElectronico.toString());
                comprobanteElectronicaService.enviarNotaCreditoSRI(comprobanteElectronico);
                break;
        }
    }

    public void reenviarNotaDebito(Liquidacion liquidacion) {
//        System.out.println("liquidadcion to String" + liquidacion.toString());
        List<MotivoNotaDebito> motivoNotaDebitos = new ArrayList<>();
        MotivoNotaDebito motivoNotaDebito;
        for (LiquidacionMotivo motivo : liquidacion.getLiquidacionMotivos()) {
            motivoNotaDebito = new MotivoNotaDebito(motivo.getRazon(), motivo.getValor());
            motivoNotaDebitos.add(motivoNotaDebito);
        }
        List<LiquidacionPago> liquidacionPagos = liquidacionPagoService.findAllLiquidacionPagoByLiquidacion(liquidacion);
        List<DetallePago> pagos = null;//obtenerLiquidacionPagos(liquidacionPagoService.findAllLiquidacionPagoByLiquidacion(liquidacion));
        List<ImpuestoComprobanteElectronico> impuestosNotaDebitos = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
        double totalND = 0;
        if (!liquidacion.getLiquidacionDetalles().isEmpty()) {
            for (LiquidacionDetalle ld : liquidacion.getLiquidacionDetalles()) {
                if (ld.getRubro() != null) {
                    impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo(ld.getRubro().getRubroTipo().getCodigo());
                    impuestoComprobanteElectronico.setCodigoPorcentaje(ld.getRubro().getCodigo());
                    impuestoComprobanteElectronico.setTarifa(ld.getRubro().getPorcentaje());
                    impuestoComprobanteElectronico.setBaseImponible(ld.getBaseImponible());
                    impuestoComprobanteElectronico.setValor(ld.getImpuesto());
                    totalND = totalND + ld.getImpuesto().doubleValue();
                    impuestosNotaDebitos.add(impuestoComprobanteElectronico);
                }
                if (ld.getRubroIce() != null) {
                    impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo(ld.getRubroIce().getRubroTipo().getCodigo());
                    impuestoComprobanteElectronico.setCodigoPorcentaje(ld.getRubroIce().getCodigo());
                    impuestoComprobanteElectronico.setTarifa(ld.getRubroIce().getPorcentaje());
                    impuestoComprobanteElectronico.setBaseImponible(ld.getBaseImponible().subtract(ld.getIce()));
                    impuestoComprobanteElectronico.setValor(ld.getIce());
                    impuestosNotaDebitos.add(impuestoComprobanteElectronico);
                    totalND = totalND + ld.getIce().doubleValue();
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cajero = cajeroService.findByCajero(usuarioService.findByUsuarioId(liquidacion.getUserCreador()).getUsuario());
//        comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.NOTADEBITO);
        comprobanteElectronico.setFechaEmisionDocumentoModifica(sdf.format(liquidacion.getFechaComprobanteRetenido()));
        comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobante().getCodigo());
        comprobanteElectronico.setNumComprobanteModifica(liquidacion.getCodigoComprobanteRetenido());
        comprobanteElectronico.setValorTotalNotaDebito(new BigDecimal(totalND).setScale(2, RoundingMode.HALF_UP));
        comprobanteElectronico.setDetallePagos(pagos);
        comprobanteElectronico.setMotivosNotaDebito(motivoNotaDebitos);
        comprobanteElectronico.setImpuestosNotaDebitos(impuestosNotaDebitos);
        comprobanteElectronicaService.enviarNotaDebitoSRI(comprobanteElectronico);
    }

    public void reenviarComprobanteRetencion(Liquidacion liquidacion) {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion = new ArrayList<>();
        ImpuestoComprobanteElectronico impuestoComprobanteElectronico;
        //liquidacion.setLiquidacionDetalles(liquidacionDetalleService.findAllLiquidacionDetalleByLiquidacion_Id(liquidacion.getId()));
        Factura factura = new Factura();
        for (LiquidacionDetalle detalle : liquidacion.getLiquidacionDetalles()) {
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

//        comprobanteElectronico = initComprobanteElectronico(liquidacion, cajero);
        //COMPROBANTE RETENCION => 07
        comprobanteElectronico.setComprobanteCodigo(ComprobantesCode.COMPPROBANTERETENCION);
        comprobanteElectronico.setFechaEmisionDocumentoModifica(format.format(factura.getFechaFactura()));
        comprobanteElectronico.setTipoDocumentoModifica(liquidacion.getComprobanteModifica().getCodigo());
        comprobanteElectronico.setNumComprobanteModifica(factura.getNumFactura().replace("-", "").trim());
        comprobanteElectronico.setMes(liquidacion.getMes().toString());
        comprobanteElectronico.setAnio(liquidacion.getAnio().toString());
        comprobanteElectronico.setImpuestoComprobanteRetencion(impuestoComprobanteRetencion);
        comprobanteElectronicaService.enviarComprobanteRentencionSRI(comprobanteElectronico);
    }
}
