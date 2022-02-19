/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.config;

import com.origami.sigef.ats.modelAts.DetalleCompras;
import com.origami.sigef.ats.modelAts.FormasPagoModel;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.service.FacturaFormasPagoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaDetalle;
import com.origami.sigef.common.entities.RenFacturaPago;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.RenFacturaPagoService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Cabecera;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.DetallePago;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.ComprobantesCode;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionPago;
import com.origami.sigef.tesoreria.entities.LiquidacionTipo;
import com.origami.sigef.tesoreria.service.LiquidacionPagoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 * @author gutya
 */
public class AccesosComprobanteElectronico implements Serializable {

    private static final long serialVersionUID = 5L;

    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @EJB
    private CatalogoItemService catalogoItemService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private LiquidacionPagoService liquidacionPagoService;
    @Inject
    private FacturaFormasPagoService detalleComprasFormasPagoService;
    @Inject
    private RenFacturaPagoService renFacturaPagoService;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
    private FormasPagoModel formasPagoModel;

    public Proveedor buscarCliente(RenFactura liquidacion) {
        if (liquidacion.getSolicitante() != null) {
            Proveedor p = servidorService.findByProveedor(liquidacion.getProveedor().getCliente().getIdentificacion().substring(0, 10));
            if (p != null) {
                return p;
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogProveedor", null);
        }
        return null;
    }

    public Cliente buscarClienteByLiquidacion(RenFactura liquidacion) {
        if (!liquidacion.getSolicitante().getIdentificacion().isEmpty()) {
            Cliente c = clienteService.existeCliente(liquidacion.getSolicitante());
            if (c != null) {
                return c;
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogCliente", null);
        }
        return null;
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
//        liquidacion.setFechaEmision(new Date());
        return liquidacion;
    }

    public RenFactura initLiquidacion(RenFactura fac) {
//        liquidacion.setTipoLiquidacion(new LiquidacionTipo(2));
        fac.setSolicitante(fac.getSolicitante());
        fac.setEstadoLiquidacion(catalogoItemService.getCatalogoI("estado_fac", "ingresada"));
        fac.setEstadoPago(catalogoItemService.getCatalogoI("estado_pago", "cancelado"));
        fac.setEstado(Boolean.TRUE);
        fac.setSubTotal(BigDecimal.ZERO);
//        fac.setDescuentoValor(BigDecimal.ZERO);
//        fac.setRideEnviado(Boolean.FALSE);
//        fac.setGeneraFactura(Boolean.TRUE);
//        fac.setPesoTramite(0);
        fac.setFacturaSinTramite(Boolean.FALSE);
//        fac.setUserCreador(usuarioService.findByUsuario(userSession.getNameUser()).getId());
        System.out.println("fecha Emision>>>" + fac.getFechaEmision());
        if (fac.getFechaEmision() == null) {
            fac.setFechaEmision(new Date());
        }
        fac.setFecha(new Date());
//        fac.setFechaEmision(new Date());
        return fac;
    }

    public Boolean validar(RenFactura liquidacion) {
        if (liquidacion.getComprobante() == null) {
            JsfUtil.addErrorMessage("Debe seleccionar un Comprobante", "");
            return false;
        }
        if (liquidacion.getSolicitante() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un Sujeto Retenido", "");
            return false;
        }
        if (liquidacion.getCodigoComprobanteRetenido() == null || liquidacion.getCodigoComprobanteRetenido().isEmpty()) {
            JsfUtil.addErrorMessage("Debe ingresar un Código de Comprobante", "");
            return false;
        }
        if (liquidacion.getFechaEmision() == null) {
            JsfUtil.addErrorMessage("Debe ingresar un la Fecha del Comprobante", "");
            return false;
        }
        System.out.println("FIN:");
        return true;
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

    public Cabecera loadCabecera(Date fechaCreacion, Proveedor proveedor) {
        Cabecera cabecera = new Cabecera();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cabecera.setCedulaRuc(proveedor.getCliente().getIdentificacionCompleta());
        System.out.println("Cedula Proveedor: " + cabecera.getCedulaRuc());
        cabecera.setFechaEmision(fechaCreacion + "");
        cabecera.setFechaEmision(new SimpleDateFormat("yyyy-MM-dd").format(fechaCreacion));
        cabecera.setPropietario(proveedor.getCliente().getNombre());
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

    public Cabecera loadCabecera(Date fechaCreacion, Cliente cliente) {
        Cabecera cabecera = new Cabecera();
        if (cliente.getTipoIdentificacion().getCodigo().equals("RUC")) {
            cabecera.setCedulaRuc(cliente.getIdentificacionCompleta());
            cabecera.setPropietario(cliente.getNombre());
        } else {
            cabecera.setCedulaRuc(cliente.getIdentificacionCompleta());
            cabecera.setPropietario(cliente.getNombreCompleto());
        }
        System.out.println("Cedula Cliente: " + cabecera.getCedulaRuc());
        cabecera.setFechaEmision(fechaCreacion + "");
        cabecera.setFechaEmision(new SimpleDateFormat("yyyy-MM-dd").format(fechaCreacion));
        cabecera.setDireccion(cliente.getDireccion());
        cabecera.setCorreo(cliente.getEmail());
        cabecera.setTelefono(cliente.getTelefono());
        if (cliente.getTipoIdentificacion().getId().equals(12L)) {
            cabecera.setEsPasaporte(Boolean.TRUE);
        } else {
            cabecera.setEsPasaporte(Boolean.FALSE);
        }
        return cabecera;
    }

    public List<DetallePago> guardarLiquidacionPagos(Liquidacion liquidacion, List<LiquidacionPago> liquidacionPagos) {
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        FormaPago fp;
        for (LiquidacionPago lp : liquidacionPagos) {
            System.out.println("guardar liquidadacion");
            fp = lp.getFormaPago();
            lp.setId(null);
            lp.setEstado(Boolean.TRUE);
//            lp.setLiquidacion(liquidacion);
            lp = liquidacionPagoService.create(lp);
            pago = new DetallePago();
            pago.setFormaPago(fp.getCodigo());
            pago.setTotal(lp.getValor().doubleValue());
            pagos.add(pago);
        }
        return pagos;
    }

    public List<DetallePago> guardarLiquidacionPagos(RenFactura liquidacion, List<RenFacturaPago> liquidacionPagos) {
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        FormaPago fp;
        for (RenFacturaPago lp : liquidacionPagos) {
            System.out.println("guardar liquidadacion");
            fp = lp.getFormaPago();
            lp.setId(null);
            lp.setEstado(Boolean.TRUE);
            lp.setFactura(liquidacion);
            lp = renFacturaPagoService.create(lp);
            pago = new DetallePago();
            pago.setFormaPago(fp.getCodigo());
            pago.setTotal(lp.getValor().doubleValue());
            pagos.add(pago);
        }
        return pagos;
    }

    public List<DetallePago> obtenerLiquidacionPagos(List<LiquidacionPago> liquidacionPagos) {
        DetallePago pago;
        List<DetallePago> pagos = new ArrayList<>();
        FormaPago fp;
        if (!liquidacionPagos.isEmpty()) {
            for (LiquidacionPago lp : liquidacionPagos) {
                fp = lp.getFormaPago();
                pago = new DetallePago();
                pago.setFormaPago(fp.getCodigo());
                pago.setTotal(lp.getValor().doubleValue());
                pagos.add(pago);
            }
        }
        return pagos;
    }

    public ComprobanteElectronico initComprobanteElectronico(RenFactura liquidacion, Cajero cajero) {
        ComprobanteElectronico comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronico.setAmbiente(ComprobantesCode.AMBIENTE);
        comprobanteElectronico.setIsOnline(ComprobantesCode.ONLINE);
        comprobanteElectronico.setPuntoEmision(cajero.getPuntoEmision());
        comprobanteElectronico.setRucEntidad(cajero.getEntidad().getRucEntidad());
        comprobanteElectronico.setGeneraLiquidacion(Boolean.FALSE);
        //para los reenvios =o =/
        if (liquidacion.getClaveAcceso() != null) {
            comprobanteElectronico.setClaveAcceso(liquidacion.getClaveAcceso());
        }
        if (liquidacion.getNumeroComprobante() != null) {
            if (liquidacion.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                comprobanteElectronico.setNumComprobante(liquidacion.getNumeroComprobante());
            }
        }
        //INICIO - CABECERA
        if (liquidacion.getSolicitante() != null) {
            comprobanteElectronico.setCabecera(loadCabecera(liquidacion.getFechaEmision(), liquidacion.getSolicitante()));
        } else if (liquidacion.getProveedor() != null) {
            comprobanteElectronico.setCabecera(loadCabecera(liquidacion.getFechaEmision(), liquidacion.getProveedor().getCliente()));
        }
        //FIN - CABECERA

        return comprobanteElectronico;
    }

    public ComprobanteElectronico initComprobanteElectronico(Liquidacion liquidacion, Cajero cajero) {
        ComprobanteElectronico comprobanteElectronico = new ComprobanteElectronico();
        comprobanteElectronico.setIdLiquidacion(liquidacion.getId());
        comprobanteElectronico.setNumComprobante(liquidacion.getNumeroComprobante());
        comprobanteElectronico.setAmbiente(ComprobantesCode.AMBIENTE);
        comprobanteElectronico.setIsOnline(ComprobantesCode.ONLINE);
        comprobanteElectronico.setPuntoEmision(cajero.getPuntoEmision());
        comprobanteElectronico.setRucEntidad(cajero.getEntidad().getRucEntidad());
        comprobanteElectronico.setGeneraLiquidacion(Boolean.FALSE);
        //INICIO - CABECERA
        if (liquidacion.getSolicitante() != null) {
            comprobanteElectronico.setCabecera(loadCabecera(liquidacion.getFechaEmisionCabecera(), liquidacion.getSolicitante()));
        } else {
            comprobanteElectronico.setCabecera(loadCabecera(liquidacion.getFechaEmisionCabecera(), liquidacion.getCliente()));
        }
        //FIN - CABECERA

        return comprobanteElectronico;
    }

    public RenFactura inicializarLiquidacionUpdate(RenFactura liquidacion) {
        return new RenFactura(liquidacion.getSolicitante(), liquidacion.getProveedor(),
                liquidacion.getEstadoPago(), liquidacion.getEstadoLiquidacion(), liquidacion.getNumeroComprobante(),
                liquidacion.getCodigoComprobante(),
                liquidacion.getSubTotal(), liquidacion.getTotalPagar(),
                liquidacion.getAnio(), liquidacion.getMes(), liquidacion.getPeriodo(), liquidacion.getRideEnviado(),
                liquidacion.getGeneraFactura(), liquidacion.getFacturaSinTramite(), liquidacion.getTipoEmision(),
                liquidacion.getConsideraIva(),
                liquidacion.getComprobante(),
                liquidacion.getComprobanteModifica(), liquidacion.getDiario());
    }

    public RenFacturaDetalle inicializarLiquidacionDetalleUpdate(RenFacturaDetalle detalle) {
        return new RenFacturaDetalle(Utils.idTemp(), detalle.getRenFactura(), detalle.getRubro(), detalle.getCantidad(),
                detalle.getBaseImponible(), detalle.getFactura(), detalle.getImpuesto(),
                detalle.getIce(), detalle.getValor(), detalle.getValorRecaudado(),
                detalle.getCuentaContableRetencion());
    }

    //ATS SIN DETALLE DE COMPRAS
    public IVA createAts(RenFactura liquidacion, Cajero cajero) {
        return new IVA("R", cajero.getEntidad().getRucEntidad(), cajero.getEntidad().getNombreEntidad(), liquidacion.getAnio(),
                String.format("%02d", liquidacion.getMes()),
                cajero.getEntidad().getEstablecimiento(), BigDecimal.ZERO.setScale(2), "IVA");
    }

    public DetalleCompras createDetalleCompras(Factura factura, RenFactura liquidacion,
            ContDiarioGeneral diarioGeneral, Cajero cajero) {
        DetalleCompras detalleCompras = new DetalleCompras();
        String[] code = factura.getNumFactura().split("-");
        detalleCompras.setIdProv(liquidacion.getIdentificacionProveedor());
        detalleCompras.setTipoComprobante(liquidacion.getComprobanteModifica().getCodigo());
        detalleCompras.setFechaRegistro(format.format(diarioGeneral.getFechaRegistro()));
        detalleCompras.setEstablecimiento(code[0]);
        detalleCompras.setPuntoEmision(code[1]);
        detalleCompras.setSecuencial(code[2]);
        detalleCompras.setFechaEmision(format.format(factura.getFechaFactura()));
        detalleCompras.setAutorizacion(factura.getNumAutorizacion());
        detalleCompras.setBaseImponible(factura.getBaseImponibleIva().setScale(2, RoundingMode.HALF_UP)); // 0% AL IVA
        detalleCompras.setBaseImpGrav(factura.getBaseImponibleDiferente().setScale(2, RoundingMode.HALF_UP)); // esta es la de diferente de 0% al IVA
        detalleCompras.setMontoIce(factura.getMontoIce().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setMontoIva(factura.getMontoIva().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValRetBien10(factura.getRetencionIva10().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValRetServ20(factura.getRetencionIva20().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValorRetBienes(factura.getRetencionIva30().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValRetServ50(factura.getRetencionIva50().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValorRetServicios(factura.getRetencionIva70().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setValRetServ100(factura.getRetencionIva100().setScale(2, RoundingMode.HALF_UP));
        detalleCompras.setTotbasesImpReemb(BigDecimal.ZERO.setScale(2));
        detalleCompras.setNumFactura(factura.getNumFactura());
        detalleCompras.setEstabRetencion1(cajero.getEntidad().getEstablecimiento());
        detalleCompras.setPtoEmiRetencion1(cajero.getPuntoEmision());
        detalleCompras.setFechaEmiRet1(format.format(new Date()));
        return detalleCompras;
    }

    public Boolean validarDatosPagoFactura(Factura f, RenFactura l) {
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
        if (l.getDiario() == null) {
            JsfUtil.addErrorMessage("", "Necesita un Diario General");
            return false;
        }
        return true;
    }

    //VALIDACIONES DE FECHAS PERIODO FISCAL CON DIARIO GENERAL Y FECHA FACTURA CON LA FECHA DEL DIARIO
    public Boolean verificacionPeriodoFiscal(RenFactura liquidacion) {
        if (liquidacion.getDiario() != null) {
            if (Utils.getAnio(liquidacion.getDiario().getFechaRegistro()) > liquidacion.getAnio()) {
                JsfUtil.addWarningMessage("", "Advertencia verifique que el año del Diario General sea menor o igual al año del periodo fiscal");
                return false;
            }
            if (Utils.getMes(liquidacion.getDiario().getFechaRegistro()) > liquidacion.getMes()) {
                JsfUtil.addWarningMessage("", "Advertencia verifique que el mes del Diario General sea menor o igual al periodo fiscal");
                return false;
            }
        }
        return true;
    }

    public Boolean validarFacturasFechaDiario(RenFactura liquidacion, List<Factura> facturas) {
        if (!facturas.isEmpty()) {
            for (Factura f : facturas) {
                if (Utils.getMes(f.getFechaFactura()) > Utils.getMes(liquidacion.getDiario().getFechaRegistro())) {
                    JsfUtil.addWarningMessage("", "Advertencia el año de la factura debe ser menor o igual a la fecha del Diario General");
                    return false;
                }
                if (Utils.getAnio(f.getFechaFactura()) > Utils.getAnio(liquidacion.getDiario().getFechaRegistro())) {
                    JsfUtil.addWarningMessage("", "Advertencia el mes de la factura debe ser menor o igual a la de Diario General ");
                    return false;
                }
            }
        }
        return true;
    }
}
