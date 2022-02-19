package com.origami.sigef.tesoreria.comprobantelectronico.sri.logic;

import com.origami.sigef.common.util.Variables;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.PorcentajeImpuesto;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Detalle;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.FirmaDocElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ImpuestoComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.MotivoNotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.Impuesto;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.InfoTributaria;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.factura.Factura;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.NotaCredito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.TotalConImpuestos;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notadebito.NotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.retencion.ComprobanteRetencion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.retencion.ImpuestoRetencion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Calculos {

    private static Double porcentajeIva = 0.0;
    private static Double baseImponible = 0.0;

    public static Boolean validarCamposComprobanteElectronico(ComprobanteElectronico comprobanteElectronico) {
        if (comprobanteElectronico.getAmbiente() == null) {
            return false;
        }
        System.out.println("---> 1");
        if (comprobanteElectronico.getPuntoEmision() == null) {
            return false;
        }
        System.out.println("---> 2");
        if (comprobanteElectronico.getComprobanteCodigo() == null) {
            return false;
        }
        System.out.println("---> 3");
        if (comprobanteElectronico.getRucEntidad() == null) {
            return false;
        }
        System.out.println("---> 4");
        //VALIDAR CABECERA
        if (comprobanteElectronico.getCabecera() == null) {
            return false;
        }
        System.out.println("---> 5");
        if (comprobanteElectronico.getCabecera().getCedulaRuc() == null) {
            return false;
        }
        System.out.println("---> 6");
        if (comprobanteElectronico.getCabecera().getCorreo() == null) {
            return false;
        }
        System.out.println("---> 7");
        if (comprobanteElectronico.getCabecera().getDireccion() == null) {
            return false;
        }
        System.out.println("---> 8");
        if (comprobanteElectronico.getCabecera().getFechaEmision() == null) {
            System.out.println("fecha de eemision");
            return false;
        }
        System.out.println("---> 9");
        if (comprobanteElectronico.getCabecera().getPropietario() == null) {
            return false;
        }
        System.out.println("---> 10");
        if (comprobanteElectronico.getCabecera().getTelefono() == null) {
            return false;
        }
        System.out.println("---> 11");
        //VALIDAR DETALLE
        if (comprobanteElectronico.getDetalle() == null
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            return false;
        }
        System.out.println(">>1");
        Boolean noExisteError = Boolean.TRUE;
        if (comprobanteElectronico.getDetalle() != null
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            for (Detalle d : comprobanteElectronico.getDetalle()) {
                if (d.getCantidad() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (d.getDescripcion() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (d.getDescuento() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (d.getIva() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (d.getValorTotal() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (d.getValorUnitario() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
            }
            return noExisteError;
        }
        System.out.println(">>2");
        //VALIDAR FORMAS DE PAGO
        if (comprobanteElectronico.getDetallePagos() == null
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            return false;
        }
        System.out.println(">>3");
        if (comprobanteElectronico.getDetallePagos() != null
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)
                && !comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            for (DetallePago dp : comprobanteElectronico.getDetallePagos()) {
                if (dp.getFormaPago() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getTotal() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
            }
            return noExisteError;
        }
        System.out.println(">>4");
        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)
                || comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {
            if (comprobanteElectronico.getTipoDocumentoModifica() == null) {
                return false;
            }
            if (comprobanteElectronico.getFechaEmisionDocumentoModifica() == null) {
                return false;
            }
            if (comprobanteElectronico.getNumComprobanteModifica() == null) {
                return false;
            }
        }
        /*
            VALIDAR CAMPOS SEGUN TIPO COMPROBANTE
            1.- Nota Credito
         */
        System.out.println(">>5");
        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTACREDITO)) {
            if (comprobanteElectronico.getMotivoNotaCredito() == null) {
                return false;
            }
        }

        /*
            2 Nota Debito
         */
        System.out.println(">>6");
        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.NOTADEBITO)) {

            if (comprobanteElectronico.getImpuestosNotaDebitos() == null || comprobanteElectronico.getImpuestosNotaDebitos().isEmpty()) {
                return false;
            }

            if (comprobanteElectronico.getMotivosNotaDebito() == null) {
                return false;
            }
            return !comprobanteElectronico.getMotivosNotaDebito().isEmpty();
        }
        System.out.println(">>7");
        if (comprobanteElectronico.getComprobanteCodigo().equals(ComprobantesCode.COMPPROBANTERETENCION)) {
            System.out.println("prueba 1: " + comprobanteElectronico.getImpuestoComprobanteRetencion());
            if (comprobanteElectronico.getImpuestoComprobanteRetencion() == null) {
                return false;
            }
            System.out.println("prueba 2: " + comprobanteElectronico.getMes());
            if (comprobanteElectronico.getMes() == null) {
                return false;
            }
            System.out.println("prueba 3: " + comprobanteElectronico.getAnio());
            if (comprobanteElectronico.getAnio() == null) {
                return false;
            }
            noExisteError = Boolean.TRUE;
            System.out.println("prueba 4: " + comprobanteElectronico.getImpuestoComprobanteRetencion().size());
            for (ImpuestoComprobanteElectronico dp : comprobanteElectronico.getImpuestoComprobanteRetencion()) {
                if (dp.getBaseImponible() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getCodigo() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getCodigoPorcentaje() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getPorcentajeRetencion() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getCodigoDocumento() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getNumDocumento() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
                if (dp.getFechaEmisionDocumento() == null) {
                    noExisteError = Boolean.FALSE;
                    break;
                }
            }
            return noExisteError;
        }
        System.out.println(">>8");
        return true;
    }

    public static Map<String, Double> totalesFactura(ComprobanteElectronico comprobanteElectronico) {
        Double totalSinImpuestos = 0.0;
        Double totalDescuento = 0.0;
        Double totalIva = 0.0;

        Map<String, Double> detalleFactura = new HashMap<>();
        ///MODIFICAR PAARA CUANDO EL IVVA EXISTA O EXISTA ALGUN TIPO DE ICE
        for (Detalle d : comprobanteElectronico.getDetalle()) {
            ///TOTALES FACTURA
            //totalSinImpuestos = (d.getValorTotal() - d.getDescuento()) + totalSinImpuestos;
            totalSinImpuestos = (d.getValorTotal()) + totalSinImpuestos;
            totalDescuento = d.getDescuento() + totalDescuento;
            if (d.getIva() > 0) {
                porcentajeIva = d.getIva() / 100;
                totalIva = ((d.getValorTotal() - d.getDescuento()) * porcentajeIva) + totalIva;
            }
        }
        detalleFactura.put("subTotalNoOnjetoIva", totalSinImpuestos);
        detalleFactura.put("totalSinImpuestos", totalSinImpuestos);
//        detalleFactura.put("totalDescuento", totalDescuento);
        detalleFactura.put("totalDescuento", (totalDescuento + comprobanteElectronico.getDescuentoAdicional().doubleValue()));
        detalleFactura.put("totalIva", totalIva);
//        detalleFactura.put("importeTotalimporteTotal", totalIva + totalSinImpuestos);
        detalleFactura.put("importeTotalimporteTotal", (totalIva + totalSinImpuestos) - comprobanteElectronico.getDescuentoAdicional().doubleValue());
        return detalleFactura;
    }

    public static Factura.Detalles.Detalle.Impuestos obtenerImpuestosFactura(Detalle detalle, PorcentajeImpuesto porcentajeImpuesto) {
        Factura.Detalles.Detalle.Impuestos impuestos = new Factura.Detalles.Detalle.Impuestos();
        impuestos.getImpuesto().add(getImpuestoComprobante(detalle, porcentajeImpuesto));
        return impuestos;
    }

    public static NotaCredito.Detalles.Detalle.Impuestos obtenerImpuestosNotaCredito(Detalle detalle, PorcentajeImpuesto porcentajeImpuesto) {
        NotaCredito.Detalles.Detalle.Impuestos impuestos = new NotaCredito.Detalles.Detalle.Impuestos();
        impuestos.getImpuesto().add(getImpuestoComprobante(detalle, porcentajeImpuesto));
        return impuestos;
    }

    private static Impuesto getImpuestoComprobante(Detalle detalle, PorcentajeImpuesto porcentajeImpuesto) {
        Impuesto impuesto = new Impuesto();
        baseImponible = detalle.getValorTotal(); // - detalle.getDescuento();
        impuesto.setCodigo(porcentajeImpuesto.getCodigoImpuesto());
        impuesto.setCodigoPorcentaje(porcentajeImpuesto.getCodigoTarifa());
        impuesto.setBaseImponible(new BigDecimal(baseImponible).setScale(2, RoundingMode.HALF_UP));
        Map<String, BigDecimal> tarifaValor = tarifaValor(detalle, porcentajeImpuesto);
        impuesto.setTarifa(tarifaValor.get("TARIFA"));
        impuesto.setValor(tarifaValor.get("VALOR"));
        return impuesto;
    }

    public static Impuesto impuestoNotaDebito(ImpuestoComprobanteElectronico impuestoNotaDebito) {
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo(impuestoNotaDebito.getCodigo());
        impuesto.setCodigoPorcentaje(impuestoNotaDebito.getCodigoPorcentaje());
        impuesto.setBaseImponible(impuestoNotaDebito.getBaseImponible());
        impuesto.setTarifa(impuestoNotaDebito.getTarifa());
        impuesto.setValor(impuestoNotaDebito.getValor());
        return impuesto;
    }

    public static Factura.InfoFactura.TotalConImpuestos.TotalImpuesto generaTotalImpuesto(Detalle detalle,
            PorcentajeImpuesto porcentajeImpuesto) {

        ///TOTALES CON IMPUESTOS
        Factura.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
        //baseImponible = detalle.getValorTotal() - detalle.getDescuento();
        baseImponible = detalle.getValorTotal(); // - detalle.getDescuento();

        //IVA CODIGO SEGUN TABLA  6 DEL SRI
        totalImpuesto.setCodigo(detalle.getCodigoTarifa());
        totalImpuesto.setBaseImponible(new BigDecimal(baseImponible).setScale(2, RoundingMode.HALF_UP));
        totalImpuesto.setCodigo(porcentajeImpuesto.getCodigoImpuesto());
        totalImpuesto.setCodigoPorcentaje(porcentajeImpuesto.getCodigoTarifa());
        Map<String, BigDecimal> tarifaValor = tarifaValor(detalle, porcentajeImpuesto);
        totalImpuesto.setTarifa(tarifaValor.get("TARIFA"));
        totalImpuesto.setValor(tarifaValor.get("VALOR"));
        //totalImpuesto.setBaseImponible(totalImpuesto.getBaseImponible() .add(totalImpuesto.getValor()));
        return totalImpuesto;
    }

    public static TotalConImpuestos.TotalImpuesto generaTotalImpuestoNotaCredito(Detalle detalle, PorcentajeImpuesto porcentajeImpuesto) {

        ///TOTALES CON IMPUESTOS
        TotalConImpuestos.TotalImpuesto totalImpuesto = new TotalConImpuestos.TotalImpuesto();
        baseImponible = detalle.getValorTotal() - detalle.getDescuento();

        //IVA CODIGO SEGUN TABLA  6 DEL SRI
        totalImpuesto.setCodigo(detalle.getCodigoTarifa());
        totalImpuesto.setBaseImponible(new BigDecimal(baseImponible).setScale(2, RoundingMode.HALF_UP));
        totalImpuesto.setCodigo(porcentajeImpuesto.getCodigoImpuesto());
        totalImpuesto.setCodigoPorcentaje(porcentajeImpuesto.getCodigoTarifa());
        Map<String, BigDecimal> tarifaValor = tarifaValor(detalle, porcentajeImpuesto);
        totalImpuesto.setValor(tarifaValor.get("VALOR"));
        return totalImpuesto;
    }

    private static Map<String, BigDecimal> tarifaValor(Detalle detalle, PorcentajeImpuesto porcentajeImpuesto) {
        Map<String, BigDecimal> tarifaValor = new HashMap<>();
        BigDecimal tarifa, valor;
        if (detalle.getIva() > 0) {
            porcentajeIva = detalle.getIva() / 100;
            tarifa = new BigDecimal(porcentajeImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP);
            valor = new BigDecimal(baseImponible * porcentajeIva).setScale(2, RoundingMode.HALF_UP);
        } else {
            tarifa = BigDecimal.ZERO;
            valor = BigDecimal.ZERO;
        }
        tarifaValor.put("TARIFA", tarifa);
        tarifaValor.put("VALOR", valor);
        return tarifaValor;
    }

    public static Factura.InfoFactura.Pago generarPagosFactura(ComprobanteElectronico comprobanteElectronico) {
        Factura.InfoFactura.Pago pagos = new Factura.InfoFactura.Pago();
        Factura.InfoFactura.Pago.DetallePago pago;
        for (DetallePago detallePago : comprobanteElectronico.getDetallePagos()) {
            pago = new Factura.InfoFactura.Pago.DetallePago();
            pago.setFormaPago(detallePago.getFormaPago());
            if (detallePago.getPlazo() != null) {
                pago.setPlazo(detallePago.getPlazo().toString());
            }
            if (detallePago.getUnidadTiempo() != null) {
                pago.setUnidadTiempo(detallePago.getUnidadTiempo());
            }
            pago.setTotal(new BigDecimal(detallePago.getTotal()).setScale(2, RoundingMode.HALF_UP));
            pagos.getPagos().add(pago);
        }
        return pagos;
    }

    public static NotaDebito.InfoNotaDebito.Pago generarPagosNotaDebito(ComprobanteElectronico comprobanteElectronico) {
        NotaDebito.InfoNotaDebito.Pago pagos = new NotaDebito.InfoNotaDebito.Pago();
        NotaDebito.InfoNotaDebito.Pago.DetallePago pago;
        for (DetallePago detallePago : comprobanteElectronico.getDetallePagos()) {
            pago = new NotaDebito.InfoNotaDebito.Pago.DetallePago();
            pago.setFormaPago(detallePago.getFormaPago());
            if (detallePago.getPlazo() != null) {
                pago.setPlazo(detallePago.getPlazo().toString());
            }
            if (detallePago.getUnidadTiempo() != null) {
                pago.setUnidadTiempo(detallePago.getUnidadTiempo());
            }
            pago.setTotal(new BigDecimal(detallePago.getTotal()).setScale(2, RoundingMode.HALF_UP));
            pagos.getPagos().add(pago);
        }
        return pagos;
    }

    public static Factura.InfoAdicional generarInformacionAdicionalFactura(ComprobanteElectronico comprobanteElectronico) {

        Factura.InfoAdicional info = new Factura.InfoAdicional();
        Factura.InfoAdicional.CampoAdicional detalle = new Factura.InfoAdicional.CampoAdicional();
        Map<String, String> infoAdicional = infoAdicional(comprobanteElectronico);

        detalle.setNombre(Variables.DIRECCION);
        detalle.setValue(infoAdicional.get(Variables.DIRECCION));
        info.getCampoAdicional().add(detalle);

        detalle = new Factura.InfoAdicional.CampoAdicional();

        detalle.setNombre(Variables.TELEFONO);
        detalle.setValue(infoAdicional.get(Variables.TELEFONO));
        info.getCampoAdicional().add(detalle);

        detalle = new Factura.InfoAdicional.CampoAdicional();

        detalle.setNombre(Variables.CORREO);
        detalle.setValue(infoAdicional.get(Variables.CORREO));
        info.getCampoAdicional().add(detalle);

        detalle = new Factura.InfoAdicional.CampoAdicional();

        detalle.setNombre(Variables.TRAMITE);
        detalle.setValue(infoAdicional.get(Variables.TRAMITE));
        info.getCampoAdicional().add(detalle);

        detalle.setNombre(Variables.TITULO_CREDITO);
        detalle.setValue(infoAdicional.get(Variables.TITULO_CREDITO));
        info.getCampoAdicional().add(detalle);

        return info;
    }

    public static NotaCredito.InfoAdicional generarInformacionAdicionalNotaCredito(ComprobanteElectronico comprobanteElectronico) {

        NotaCredito.InfoAdicional info = new NotaCredito.InfoAdicional();
        NotaCredito.InfoAdicional.CampoAdicional detalle = new NotaCredito.InfoAdicional.CampoAdicional();
        Map<String, String> infoAdicional = infoAdicional(comprobanteElectronico);
        detalle.setNombre(Variables.DIRECCION);
        detalle.setValue(infoAdicional.get(Variables.DIRECCION));
        info.getCampoAdicional().add(detalle);

        detalle = new NotaCredito.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.TELEFONO);
        detalle.setValue(infoAdicional.get(Variables.TELEFONO));
        info.getCampoAdicional().add(detalle);

        detalle = new NotaCredito.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.CORREO);
        detalle.setValue(infoAdicional.get(Variables.CORREO));
        info.getCampoAdicional().add(detalle);

        return info;
    }

    public static NotaDebito.InfoAdicional generarInformacionAdicionalNotaDebito(ComprobanteElectronico comprobanteElectronico) {

        NotaDebito.InfoAdicional info = new NotaDebito.InfoAdicional();
        NotaDebito.InfoAdicional.CampoAdicional detalle = new NotaDebito.InfoAdicional.CampoAdicional();
        Map<String, String> infoAdicional = infoAdicional(comprobanteElectronico);
        detalle.setNombre(Variables.DIRECCION);
        detalle.setValue(infoAdicional.get(Variables.DIRECCION));
        info.getCampoAdicional().add(detalle);

        detalle = new NotaDebito.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.TELEFONO);
        detalle.setValue(infoAdicional.get(Variables.TELEFONO));
        info.getCampoAdicional().add(detalle);

        detalle = new NotaDebito.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.CORREO);
        detalle.setValue(infoAdicional.get(Variables.CORREO));
        info.getCampoAdicional().add(detalle);

        return info;
    }

    public static ComprobanteRetencion.InfoAdicional generarInformacionAdicionalCompobanteRetencion(ComprobanteElectronico comprobanteElectronico) {

        ComprobanteRetencion.InfoAdicional info = new ComprobanteRetencion.InfoAdicional();
        ComprobanteRetencion.InfoAdicional.CampoAdicional detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
        Map<String, String> infoAdicional = infoAdicional(comprobanteElectronico);
        detalle.setNombre(Variables.DIRECCION);
        detalle.setValue(infoAdicional.get(Variables.DIRECCION));
        info.getCampoAdicional().add(detalle);

        detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.TELEFONO);
        detalle.setValue(infoAdicional.get(Variables.TELEFONO));
        info.getCampoAdicional().add(detalle);

        detalle = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
        detalle.setNombre(Variables.CORREO);
        detalle.setValue(infoAdicional.get(Variables.CORREO));
        info.getCampoAdicional().add(detalle);

        return info;
    }

    private static Map<String, String> infoAdicional(ComprobanteElectronico comprobanteElectronico) {
        Map<String, String> infoAdicionalResult = new HashMap<>();
        infoAdicionalResult.put(Variables.DIRECCION, (comprobanteElectronico.getCabecera().getDireccion() != null && comprobanteElectronico.getCabecera().getDireccion().length() > 0) ? comprobanteElectronico.getCabecera().getDireccion() : "SIN INFORMACIÓN");
        infoAdicionalResult.put(Variables.TELEFONO, (comprobanteElectronico.getCabecera().getTelefono() != null && comprobanteElectronico.getCabecera().getTelefono().length() > 0) ? comprobanteElectronico.getCabecera().getTelefono() : "SIN INFORMACIÓN");
        infoAdicionalResult.put(Variables.CORREO, (comprobanteElectronico.getCabecera().getCorreo() != null && comprobanteElectronico.getCabecera().getCorreo().length() > 0) ? comprobanteElectronico.getCabecera().getCorreo() : "SIN INFORMACIÓN");
        infoAdicionalResult.put(Variables.TRAMITE, comprobanteElectronico.getTramite() != null ? comprobanteElectronico.getTramite().toString() : "SIN INFORMACIÓN");
        infoAdicionalResult.put(Variables.TITULO_CREDITO, comprobanteElectronico.getTramite() != null ? comprobanteElectronico.getTramite().toString() : "SIN INFORMACIÓN");
        return infoAdicionalResult;
    }

    public static InfoTributaria loadInfoTributaria(String secuencialComprobante, FirmaDocElectronico firmaDocElectronico) {
        InfoTributaria infoTributaria = new InfoTributaria();
        System.out.println("loadInfoTributaria secuencialComprobante " + secuencialComprobante);
        infoTributaria.setSecuencial(secuencialComprobante);
        //ambiente
        infoTributaria.setAmbiente(firmaDocElectronico.getAmbiente().getCodigo());
        infoTributaria.setTipoEmision(firmaDocElectronico.getTipoEmision().getCodigo());
        infoTributaria.setRazonSocial(firmaDocElectronico.getEntidad().getNombreEntidad());
        infoTributaria.setRuc(firmaDocElectronico.getEntidad().getRucEntidad());
        infoTributaria.setCodDoc(firmaDocElectronico.getComprobantes().getCodigo());
        infoTributaria.setEstab(firmaDocElectronico.getCajero().getEntidad().getEstablecimiento());
        infoTributaria.setPtoEmi(firmaDocElectronico.getCajero().getPuntoEmision());
        infoTributaria.setDirMatriz(firmaDocElectronico.getEntidad().getDireccion());
        if ((firmaDocElectronico.getEntidad().getNombreComercial() != null) && (!firmaDocElectronico.getEntidad().getNombreComercial().isEmpty())) {
            infoTributaria.setNombreComercial(firmaDocElectronico.getEntidad().getNombreComercial());
        }
        return infoTributaria;
    }

    public static Factura.InfoFactura loadInfoFactura(FirmaDocElectronico firmaDocElectronico, ComprobanteElectronico comprobanteElectronico) {
        Factura.InfoFactura infoFactura = new Factura.InfoFactura();
        infoFactura.setFechaEmision(DocumentosUtil.fechaEmision(comprobanteElectronico.getCabecera().getFechaEmision(), "dd/MM/yyyy"));
        infoFactura.setIdentificacionComprador(comprobanteElectronico.getCabecera().getCedulaRuc());
        infoFactura.setRazonSocialComprador(comprobanteElectronico.getCabecera().getPropietario());
        infoFactura.setDireccionComprador((comprobanteElectronico.getCabecera().getDireccion() != null && comprobanteElectronico.getCabecera().getDireccion().length() > 0) ? comprobanteElectronico.getCabecera().getDireccion() : "SIN INFORMACION");
        //infoFactura.setDireccionComprador(comprobanteElectronico.getCabecera().getDireccion() != null ? comprobanteElectronico.getCabecera().getDireccion() : "");
        ///OBTIENE DATOS DEL DETALLE DE LA FACTURA Y SACA LOS DESCUENTOS ENTREE OTROS VALORES
        Map<String, Double> detalleFactura = Calculos.totalesFactura(comprobanteElectronico);
        infoFactura.setTotalSinImpuestos(new BigDecimal(detalleFactura.get("totalSinImpuestos")).setScale(2, RoundingMode.HALF_UP));
        infoFactura.setTotalDescuento(new BigDecimal(detalleFactura.get("totalDescuento")).setScale(2, RoundingMode.HALF_UP));
        infoFactura.setPropina(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        /*FALTA SUMA DEL ICE =/ ESTA JODIDA(TOTAL DEL PRODUCTO - DESCUENTO) * IVA +(TOTAL DEL PRODUCTO - DESCUENTO) * ICE =IMPORTE TOTAL*/
        infoFactura.setImporteTotal(new BigDecimal(detalleFactura.get("importeTotalimporteTotal")).setScale(2, RoundingMode.HALF_UP));
        infoFactura.setMoneda(Variables.MONEDA);

        infoFactura.setPagos(generarPagosFactura(comprobanteElectronico));
        Map<String, String> datosTributarios = datosTributarios(firmaDocElectronico);
        infoFactura.setContribuyenteEspecial(datosTributarios.get("CONTRIBUYENTE_ESPECIAL"));
        infoFactura.setObligadoContabilidad(datosTributarios.get("OBLIGADO_COTABILIDAD"));
        return infoFactura;
    }

    public static NotaCredito.InfoNotaCredito loadInfoNotaCredito(FirmaDocElectronico firmaDocElectronico, ComprobanteElectronico comprobanteElectronico) {
        NotaCredito.InfoNotaCredito infoNotaCredito = new NotaCredito.InfoNotaCredito();
        infoNotaCredito.setFechaEmision(DocumentosUtil.fechaEmision(comprobanteElectronico.getCabecera().getFechaEmision(), "dd/MM/yyyy"));
        infoNotaCredito.setIdentificacionComprador(comprobanteElectronico.getCabecera().getCedulaRuc());
        infoNotaCredito.setRazonSocialComprador(comprobanteElectronico.getCabecera().getPropietario());
        //infoNotaCredito.setDirEstablecimiento(firmaDocElectronico.getDocElectronico().getEntidad().getDireccion() != null ? firmaDocElectronico.getDocElectronico().getEntidad().getDireccion() : "");
        ///OBTIENE DATOS DEL DETALLE DE LA FACTURA Y SACA LOS DESCUENTOS ENTREE OTROS VALORES
        Map<String, Double> detalleFactura = Calculos.totalesFactura(comprobanteElectronico);
        infoNotaCredito.setTotalSinImpuestos(new BigDecimal(detalleFactura.get("totalSinImpuestos")).setScale(2, RoundingMode.HALF_UP));
        infoNotaCredito.setValorModificacion(new BigDecimal(detalleFactura.get("importeTotalimporteTotal")).setScale(2, RoundingMode.HALF_UP));
        infoNotaCredito.setMoneda(Variables.MONEDA);
        infoNotaCredito.setCodDocModificado(comprobanteElectronico.getTipoDocumentoModifica());

        ///SE FORMATEA EL NUMERO DE COMPROBANTE PARA QUE SE
        String numDocModifica = comprobanteElectronico.getNumComprobanteModifica().substring(0, 3) + "-" + comprobanteElectronico.getNumComprobanteModifica().substring(3, 6) + "-" + comprobanteElectronico.getNumComprobanteModifica().substring(6);
        System.out.println("numDocModifica " + numDocModifica);
        infoNotaCredito.setNumDocModificado(numDocModifica);

        infoNotaCredito.setFechaEmisionDocSustento((DocumentosUtil.fechaEmision(comprobanteElectronico.getFechaEmisionDocumentoModifica(), "dd/MM/yyyy")));
        infoNotaCredito.setMotivo(comprobanteElectronico.getMotivoNotaCredito());

        Map<String, String> datosTributarios = datosTributarios(firmaDocElectronico);
        infoNotaCredito.setContribuyenteEspecial(datosTributarios.get("CONTRIBUYENTE_ESPECIAL"));
        infoNotaCredito.setObligadoContabilidad(datosTributarios.get("OBLIGADO_COTABILIDAD"));
        return infoNotaCredito;
    }

    public static NotaDebito.InfoNotaDebito loadInfoNotaDebito(FirmaDocElectronico firmaDocElectronico, ComprobanteElectronico comprobanteElectronico) {
        NotaDebito.InfoNotaDebito infoNotaDebito = new NotaDebito.InfoNotaDebito();
        infoNotaDebito.setFechaEmision(DocumentosUtil.fechaEmision(comprobanteElectronico.getCabecera().getFechaEmision(), "dd/MM/yyyy"));
        infoNotaDebito.setIdentificacionComprador(comprobanteElectronico.getCabecera().getCedulaRuc());
        infoNotaDebito.setRazonSocialComprador(comprobanteElectronico.getCabecera().getPropietario());
        infoNotaDebito.setTotalSinImpuestos(totalSinImpuestoNotaDebito(comprobanteElectronico));
        infoNotaDebito.setValorTotal(comprobanteElectronico.getValorTotalNotaDebito());
        infoNotaDebito.setCodDocModificado(comprobanteElectronico.getTipoDocumentoModifica());
        infoNotaDebito.setNumDocModificado(comprobanteElectronico.getNumComprobanteModifica());
        infoNotaDebito.setFechaEmisionDocSustento((DocumentosUtil.fechaEmision(comprobanteElectronico.getFechaEmisionDocumentoModifica(), "dd/MM/yyyy")));

        infoNotaDebito.setPagos(generarPagosNotaDebito(comprobanteElectronico));
        Map<String, String> datosTributarios = datosTributarios(firmaDocElectronico);
        infoNotaDebito.setContribuyenteEspecial(datosTributarios.get("CONTRIBUYENTE_ESPECIAL"));
        infoNotaDebito.setObligadoContabilidad(datosTributarios.get("OBLIGADO_COTABILIDAD"));
        return infoNotaDebito;
    }

    public static NotaDebito.Motivos obtenerMotivos(ComprobanteElectronico comprobanteElectronico) {
        NotaDebito.Motivos.Motivo motivo;
        NotaDebito.Motivos motivos = new NotaDebito.Motivos();
        for (MotivoNotaDebito motivoNotaDebito : comprobanteElectronico.getMotivosNotaDebito()) {
            motivo = new NotaDebito.Motivos.Motivo();
            motivo.setRazon(motivoNotaDebito.getRazon());
            motivo.setValor(motivoNotaDebito.getValor());
            motivos.getMotivo().add(motivo);
        }
        return motivos;
    }

    public static BigDecimal totalSinImpuestoNotaDebito(ComprobanteElectronico comprobanteElectronico) {
        BigDecimal totalSinImpuesto = BigDecimal.ZERO;
        for (MotivoNotaDebito motivoNotaDebito : comprobanteElectronico.getMotivosNotaDebito()) {
            totalSinImpuesto = totalSinImpuesto.add(motivoNotaDebito.getValor());
        }
        return totalSinImpuesto.setScale(2, RoundingMode.HALF_UP);
    }

    public static ComprobanteRetencion.InfoCompRetencion infoCompRetencion(FirmaDocElectronico firmaDocElectronico, ComprobanteElectronico comprobanteElectronico) {
        ComprobanteRetencion.InfoCompRetencion infoCompRetencion = new ComprobanteRetencion.InfoCompRetencion();
        infoCompRetencion.setFechaEmision(DocumentosUtil.fechaEmision(comprobanteElectronico.getCabecera().getFechaEmision(), "dd/MM/yyyy"));
        infoCompRetencion.setRazonSocialSujetoRetenido(comprobanteElectronico.getCabecera().getPropietario());
        infoCompRetencion.setIdentificacionSujetoRetenido(comprobanteElectronico.getCabecera().getCedulaRuc());
        String mesPeriodoF = "";
        if (comprobanteElectronico.getMes().length() == 1) {
            mesPeriodoF = "0" + comprobanteElectronico.getMes();
        } else {
            mesPeriodoF = comprobanteElectronico.getMes();
        }
        infoCompRetencion.setPeriodoFiscal(mesPeriodoF + "/" + comprobanteElectronico.getAnio());
        Map<String, String> datosTributarios = datosTributarios(firmaDocElectronico);
        infoCompRetencion.setContribuyenteEspecial(datosTributarios.get("CONTRIBUYENTE_ESPECIAL"));
        infoCompRetencion.setObligadoContabilidad(datosTributarios.get("OBLIGADO_COTABILIDAD"));
        return infoCompRetencion;
    }

    public static ComprobanteRetencion.Impuestos obtenerImpuestoComprobanteRetencion(ComprobanteElectronico comprobanteElectronico) {
        ComprobanteRetencion.Impuestos resultado = new ComprobanteRetencion.Impuestos();
        System.out.println("obtenerImpuestoComprobanteRetencion: ");
        System.out.println("comprobanteElectronico.getImpuestoComprobanteRetencion(): " + comprobanteElectronico.getImpuestoComprobanteRetencion().size());
        ImpuestoRetencion impuestoRetencion;
        BigDecimal totalRetenido = BigDecimal.ZERO;
        for (ImpuestoComprobanteElectronico i : comprobanteElectronico.getImpuestoComprobanteRetencion()) {
            impuestoRetencion = new ImpuestoRetencion();
            impuestoRetencion.setCodigo(i.getCodigo());
            impuestoRetencion.setCodigoRetencion(i.getCodigoPorcentaje());
            impuestoRetencion.setBaseImponible(i.getBaseImponible());
            impuestoRetencion.setPorcentajeRetener(i.getPorcentajeRetencion());
            totalRetenido = i.getBaseImponible().multiply(i.getPorcentajeRetencion()).setScale(2, RoundingMode.HALF_UP);
            totalRetenido = totalRetenido.divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            impuestoRetencion.setValorRetenido(totalRetenido);
            impuestoRetencion.setCodDocSustento(i.getCodigoDocumento());
            impuestoRetencion.setNumDocSustento(i.getNumDocumento());
            impuestoRetencion.setFechaEmisionDocSustento(DocumentosUtil.fechaEmision(i.getFechaEmisionDocumento(), "dd/MM/yyyy"));
            resultado.getImpuesto().add(impuestoRetencion);
            System.out.println("impuestoRetencion: " + impuestoRetencion.toString());

        }

        return resultado;
    }

    public static Map<String, String> datosTributarios(FirmaDocElectronico firmaDocElectronico) {
        Map<String, String> datosTributariosResult = new HashMap<>();
        //System.out.println("firmaDocElectronico.getEntidad(): " + firmaDocElectronico.getEntidad().toString());
        //datosTributariosResult.put("CONTRIBUYENTE_ESPECIAL", firmaDocElectronico.getEntidad().getContribuyenteEspecial());
        datosTributariosResult.put("OBLIGADO_COTABILIDAD", firmaDocElectronico.getEntidad().getContabilidad());
        return datosTributariosResult;
    }

}
