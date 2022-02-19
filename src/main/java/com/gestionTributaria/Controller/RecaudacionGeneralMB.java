/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.RemisionInteresService;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.gestionTributaria.Services.TipoLiquidacionService;
import com.gestionTributaria.models.PagoTituloReporteModel;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class RecaudacionGeneralMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionGeneralMB.class.getName());

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;
    @Inject
    private TipoLiquidacionService tipoLiquidacionService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private RenRubroLiquidacionService rubroService;
    @Inject
    private InteresesService interesService;
    @Inject
    private RemisionInteresService remisionInteres;
    @Inject
    private BancoService bancoService;
    @Inject
    private FinaRenPagoService renPagoService;
    @Inject
    private UsuarioService userService;
    @Inject
    private CajeroService cajeroService;

    private ManagerService manager;

    private LazyModel<FinaRenLiquidacion> lazyTitulosGenerados;
    private LazyModel<FinaRenLiquidacion> lazyTitulosPagados;
    private LazyModel<FinaRenLiquidacion> lazyTitulosAnulados;
    //private LazyModel<FinaRenLiquidacion> lazyTitulosCuotaCoactiva;

    private List<FinaRenTipoLiquidacion> tipoLiquidacionList;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenLiquidacion> liquidaciones;
    private Cajero cajero;

    private PagoTituloReporteModel modelPago;
    protected List<NotasCredito> listPagoNota;
//    protected Boolean pagoRealizado;
    protected Boolean variosPagos;
    private List<Banco> bancos;
    private List<Banco> tarjetas;

    @PostConstruct
    public void init() {
        this.titulosGenerales();
//        pagoRealizado = Boolean.FALSE;
        variosPagos = Boolean.FALSE;
        modelPago = new PagoTituloReporteModel();
        tipoLiquidacionList = tipoLiquidacionService.getLiquidacionesGenerales();
        cajero = cajeroService.findByCajero(session.getNameUser());
        this.bancos = bancoService.getBancoList();
        this.tarjetas = bancoService.getBancoList();
    }

    public void onChanngeTab(TabChangeEvent event) {
        try {
            if (event.getTab().getId().equals("tituloCredito")) {
                if (lazyTitulosGenerados == null) {
                    this.titulosGenerales();
                }
            }
            if (event.getTab().getId().equals("convenios")) {

            }
            if (event.getTab().getId().equals("convenioTerminado")) {

            }
            if (event.getTab().getId().equals("tituloCreditoPagado")) {
                if (this.lazyTitulosPagados == null) {
                    this.lazyTitulosPagados = new LazyModel<>(FinaRenLiquidacion.class);
                    this.lazyTitulosPagados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(1L)));
                    this.lazyTitulosPagados.getSorteds().put("numComprobante", "desc");
                    JsfUtil.update("mainForm:tvRecaudaciones:dataTitulosPagados");
                }
            }
            if (event.getTab().getId().equals("tituloCreditoAnulado")) {
                if (this.lazyTitulosAnulados == null) {
                    this.lazyTitulosAnulados = new LazyModel<>(FinaRenLiquidacion.class);
                    this.lazyTitulosAnulados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(4L)));
                    this.lazyTitulosAnulados.getSorteds().put("fechaIngreso", "desc");
                    JsfUtil.update("mainForm:tvRecaudaciones:dataTitulosAnulados");
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void titulosGenerales() {
        liquidacion = new FinaRenLiquidacion();
        liquidaciones = new ArrayList<>();
        lazyTitulosGenerados = new LazyModel<>(FinaRenLiquidacion.class);
        lazyTitulosGenerados.getFilterss().put("estadoLiquidacion:equals", Arrays.asList(new FinaRenEstadoLiquidacion(2L)));
        lazyTitulosGenerados.getFilterss().put("tipoLiquidacion:notEqual", Arrays.asList(new FinaRenTipoLiquidacion(3L), new FinaRenTipoLiquidacion(2L)));
        lazyTitulosGenerados.getSorteds().put("fechaIngreso", "desc");
    }

    public void procesarPago() {
//        this.pagoRealizado = Boolean.FALSE;
        if (this.liquidacion != null) {
            liquidacion.setInteres(this.interes(liquidacion));
            //PARA LOSS PERMISOS AMBIENTALES GENERAR INTERES
//            if (this.liquidacion.getTipoLiquidacion() != null) {
//                if (this.liquidacion.getTipoLiquidacion().getTransaccionPadre() != null) {
//                    this.liquidacion.setInteres(this.interes(liquidacion));
//                }
//            }
            liquidacion.calcularPago();
            modelPago = new PagoTituloReporteModel(this.liquidacion.getPagoFinal(), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
            modelPago.setValorCobrar(liquidacion.getPagoFinal());
//            System.out.println("saldo--> " + modelPago.getValorSaldoPagoFinal());
        }

    }

    public BigDecimal interes(FinaRenLiquidacion liq) {
        Boolean aplicaRemision = remisionInteres.aplicaRemision(liq);
        if (aplicaRemision) {
            liq.setInteres(BigDecimal.ZERO);
        } else {
            liq.setInteres(interesService.interesesCalculado(liq, new Date()));
        }
        return liq.getInteres();
    }

    public void pago() {
        try {
            if (this.liquidacion != null && this.modelPago != null && this.liquidacion.getTipoLiquidacion().getId().equals(181L) && this.liquidacion.getSaldo().compareTo(this.modelPago.getValorTotal()) > 0) {
                JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser igual a la recaudación");
                return;
            }
//            this.generarLiquidacion();
            this.realizarPago();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarLiquidacion() {
        if (liquidacion != null && liquidacion.getId() != null) {
            liquidacion.calcularPago();
        }
        liquidacion.setIpUserSession(session.getIpClient());
        liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liquidacion.setUsuarioIngreso(session.getNameUser());
//        liquidacion.getTipoLiquidacion().setRenRubrosLiquidacionCollection(rubrosSeleccionado);
        liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
        liquidacion.setValidada(Boolean.TRUE);
        liquidacion = liquidacionService.grabarLiquidacionPagada(liquidacion);
        JsfUtil.addWarningMessage("Mensaje", "Liquidacion: " + liquidacion.getIdLiquidacion() + " generada con exito");
    }

    public void realizarPago() {
        FinaRenPago p;
        FinaRenPago pagoCoactiva = null;
        if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
            liquidacion.calcularPago();
            try {
                FinaRenLiquidacion temp = null;
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    System.out.println("Tienen Coactiva..Falta desarrollar :v");
                }
                p = renPagoService.realizarPago(liquidacion, modelPago.realizarPago(liquidacion), cajero, variosPagos);
                liquidacion.setNumLiquidacion(new BigInteger(p.getNumComprobante() + ""));
                liquidacion.setNumComprobante(new BigInteger(p.getNumComprobante() + ""));
                liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-" + p.getNumComprobante());
                liquidacionService.edit(liquidacion);
                if (p != null) {
                    modelPago = new PagoTituloReporteModel(new BigDecimal("0.00"), this.variosPagos, this.modelPago.getPagoNotaCredio(), this.modelPago.getPagoCheque(), this.modelPago.getPagoTarjetaCredito(), this.modelPago.getPagoTransferencia());
//                    pagoRealizado = Boolean.TRUE;
                    liquidacion = null;
                    JsfUtil.addSuccessMessage("Recaudacion", "Cobro Realizado Con Éxito...");
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
            }
        } else {
            JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get and Set">
    public String getDescripcionRubro(Long idRubro) {
        return rubroService.getDescripcionRubro(idRubro);
    }

    public LazyModel<FinaRenLiquidacion> getLazyTitulosGenerados() {
        return lazyTitulosGenerados;
    }

    public void setLazyTitulosGenerados(LazyModel<FinaRenLiquidacion> lazyTitulosGenerados) {
        this.lazyTitulosGenerados = lazyTitulosGenerados;
    }

    public LazyModel<FinaRenLiquidacion> getLazyTitulosPagados() {
        return lazyTitulosPagados;
    }

    public void setLazyTitulosPagados(LazyModel<FinaRenLiquidacion> lazyTitulosPagados) {
        this.lazyTitulosPagados = lazyTitulosPagados;
    }

    public LazyModel<FinaRenLiquidacion> getLazyTitulosAnulados() {
        return lazyTitulosAnulados;
    }

    public void setLazyTitulosAnulados(LazyModel<FinaRenLiquidacion> lazyTitulosAnulados) {
        this.lazyTitulosAnulados = lazyTitulosAnulados;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionList() {
        return tipoLiquidacionList;
    }

    public void setTipoLiquidacionList(List<FinaRenTipoLiquidacion> tipoLiquidacionList) {
        this.tipoLiquidacionList = tipoLiquidacionList;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public PagoTituloReporteModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoTituloReporteModel modelPago) {
        this.modelPago = modelPago;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<NotasCredito> getListPagoNota() {
        return listPagoNota;
    }

    public void setListPagoNota(List<NotasCredito> listPagoNota) {
        this.listPagoNota = listPagoNota;
    }

    public List<Banco> getBancos() {
        return bancos;
    }

    public void setBancos(List<Banco> bancos) {
        this.bancos = bancos;
    }

    public List<Banco> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Banco> tarjetas) {
        this.tarjetas = tarjetas;
    }

//</editor-fold>
}
