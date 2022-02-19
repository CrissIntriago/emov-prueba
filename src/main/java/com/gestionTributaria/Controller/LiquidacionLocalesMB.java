/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenLocalComercial;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.RenActivosLocalComercial;
import com.gestionTributaria.Entities.RenBalanceLocalComercial;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "liquidacionLocales")
@ViewScoped
public class LiquidacionLocalesMB extends ReportGenealModel implements Serializable {

    private static final Logger LOG = Logger.getLogger(LiquidacionLocalesMB.class.getName());

    private LazyModel<FinaRenLiquidacion> lazy;
    private FinaRenLiquidacion liquidacion;
    private FinaRenLocalComercial local;
    private List<FinaRenLocalComercial> locales;
    private RenActivosLocalComercial activosLocal;
    private RenBalanceLocalComercial balance;
    private FinaRenPago pago;
    private Map<String, Object> param;
    @Inject
    private ServletSession ss;

    @Inject
    private UserSession session;

    @Inject
    private ManagerService manager;

    @PostConstruct
    protected void initView() {
        param = new HashMap<>();
        try {
            if (!ss.estaVacio()) {

                locales = (List<FinaRenLocalComercial>) ss.getValor("localComercial");

                local = locales.get(0);

            }
            System.out.println("local " + local);
            if (local == null) {
                //lazy = new LiquidacionesLazy(1);
                lazy = new LazyModel<>(FinaRenLiquidacion.class);

            } else {
                // lazy = new LiquidacionesLazy(1, local);
                lazy = new LazyModel<>(FinaRenLiquidacion.class);
                lazy.getFilterss().put("localComercial.patente", local.getPatente());
            }
            ss.setParametros(null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "initView()", e);
        }
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = manager.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    public void detalle(FinaRenLiquidacion liquidacion) {
        try {
            this.liquidacion = liquidacion;
            interes(this.liquidacion);
            this.liquidacion.calcularPago();
            this.local = (FinaRenLocalComercial) manager.find("SELECT r FROM FinaRenLocalComercial r INNER JOIN r.renLiquidacionCollection l WHERE l.id = :liquidacion", new String[]{"liquidacion"}, new Object[]{liquidacion.getId()});
            if (liquidacion.getRenPagoCollection() != null) {
                pago = new FinaRenPago();
                BigDecimal total = BigDecimal.ZERO;
                for (FinaRenPago p : liquidacion.getRenPagoCollection()) {
                    if (p.getEstado()) {
                        pago.setNumComprobante(p.getNumComprobante());
                        total = total.add(p.getValor());
                        pago.setObservacion(p.getObservacion());
                    }
                }
                pago.setValor(total);
            }
            if (this.local != null) {
                param = new HashMap<>();
                param.put("numLiquidacion", BigInteger.valueOf(liquidacion.getId()));
                param.put("estado", true);
                this.activosLocal = (RenActivosLocalComercial) manager.findByParameter(RenActivosLocalComercial.class, param);

                param = new HashMap<>();
                param.put("numLiquidacion", BigInteger.valueOf(liquidacion.getId()));
                param.put("estado", true);
                this.balance = manager.findByParameter(RenBalanceLocalComercial.class, param);
                //services.getBalanceLocal(liquidacion);
            } else {
                JsfUtil.addInformationMessage("Informacion", "Liquidacion no tiene un local ingresado.");
//                return;
            }
            calcularInteres();
            JsfUtil.executeJS("PF('detalle').show()");
            JsfUtil.update("detalle");
            JsfUtil.update("detalle:tabDetalle");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

//    public void imprimir(FinaRenLiquidacion liquidacion) {
////        System.out.print(liquidacion.getTipoLiquidacion().getCodigoTituloReporte());
////        System.out.println(liquidacion.getTipoLiquidacion().getNombreTitulo());
//        String path = JsfUtil.getRealPath("//");
//        ss.instanciarParametros();
//        //ss.setTieneDatasource(Boolean.TRUE);
//        ss.addParametro("LOGO", path.concat(SisVars.logoReportes));
//        ss.addParametro("LIQUIDACION", liquidacion.getId());
//        ss.setNombreSubCarpeta("rentas");
//        switch (liquidacion.getTipoLiquidacion().getCodigoTituloReporte().intValue()) {
//            case 11:
//                ss.setNombreReporte("activosTotales");
//                break;
//            case 14:
//                ss.setNombreReporte("patenteAnual");
//                break;
//            case 15:
//                ss.setNombreReporte("tasaHabilitacion");
//                break;
//            case 98:
//                ss.setNombreReporte("turismo");
//                break;
//            case 206:
//                ss.setNombreReporte("vallasPublicitarias");
//                break;
//            default:
//                return;
//        }
//        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
//    }
//    public void imprimirDetalle(FinaRenLiquidacion liquidacion) {
//        String path = JsfUtil.getRealPath("//");
//        ss.instanciarParametros();
//        //  ss.setTieneDatasource(Boolean.TRUE);
//        ss.addParametro("LOGO", path.concat(SisVars.logoReportes));
//        ss.addParametro("LIQUIDACION", liquidacion.getId());
//        ss.setNombreSubCarpeta("rentas");
//        switch (liquidacion.getTipoLiquidacion().getCodigoTituloReporte().intValue()) {
//            case 11:
//                ss.setNombreReporte("detalleActivosTotales");
//                break;
//            case 14:
//                ss.setNombreReporte("detallePatente");
//                break;
//            case 15:
//                ss.setNombreReporte("detalleTasaHabilitacion");
//                break;
//            default:
//                return;
//        }
//        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
//    }
    public void calcularInteres() {
        try {
            if (liquidacion != null) {
                List<RenParametrosInteresMulta> parametrosInteresMultas = manager.getListParametrosInteresMulta(liquidacion);
                System.out.println(parametrosInteresMultas == null);
                if (parametrosInteresMultas != null && !parametrosInteresMultas.isEmpty()) {//VERIFICAR SI EMITE MULTA-INTERES
                    for (RenParametrosInteresMulta interesMulta : parametrosInteresMultas) {
                        if (interesMulta.getTipo().equalsIgnoreCase("I")) {
                            Calendar fecha = Calendar.getInstance();
                            fecha.set(Calendar.DAY_OF_MONTH, interesMulta.getDia());
                            fecha.set(Calendar.MONTH, interesMulta.getMes() - 1);
                            fecha.set(Calendar.YEAR, liquidacion.getAnio());
                            liquidacion.setInteres(manager.generarInteres(liquidacion.getSaldo(), fecha.getTime(), null));
                        }
                        if (interesMulta.getTipo().equalsIgnoreCase("M")) {
                            liquidacion.setRecargo(manager.generarMultas(liquidacion, interesMulta));
                        }
                    }
                }
                liquidacion.calcularPago();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Calcular Multa", e);
        }
    }

    public void anular() {
        if (liquidacion != null) {
            liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
            manager.update(liquidacion);
            JsfUtil.addInformationMessage("Informacion", "Liquidacion anulada correctamente.");
        } else {
            JsfUtil.addErrorMessage("Error", "Liquidacion es nula vuelva a interlo.");
        }
    }

    public LazyModel<FinaRenLiquidacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FinaRenLiquidacion> lazy) {
        this.lazy = lazy;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
    }

    public List<FinaRenLocalComercial> getLocales() {
        return locales;
    }

    public void setLocales(List<FinaRenLocalComercial> locales) {
        this.locales = locales;
    }

    public RenActivosLocalComercial getActivosLocal() {
        return activosLocal;
    }

    public void setActivosLocal(RenActivosLocalComercial activosLocal) {
        this.activosLocal = activosLocal;
    }

    public RenBalanceLocalComercial getBalance() {
        return balance;
    }

    public void setBalance(RenBalanceLocalComercial balance) {
        this.balance = balance;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

}
