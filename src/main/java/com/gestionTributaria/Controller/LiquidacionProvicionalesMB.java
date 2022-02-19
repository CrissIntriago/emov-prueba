/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.RenBalancePatente;
import com.gestionTributaria.Entities.RenFactorPorCapital;
import com.gestionTributaria.Entities.RenFactorPorMetro;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "provicionalView")
@ViewScoped
public class LiquidacionProvicionalesMB implements Serializable {

    public static final Long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(LiquidacionProvicionalesMB.class.getName());

    @Inject
    private ManagerService services;

    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;

    //TITULO DE CREDITO
    private LazyModel<FinaRenLiquidacion> provicionales;
    private FinaRenLiquidacion provicional;
    private List<FinaRenRubrosLiquidacion> rubrosLiquidacion;
    private List<FinaRenDetLiquidacion> detalles;
    private FinaRenPatente patente;
    private RenBalancePatente balance;
    private boolean hacerEfectiva;
    private short tipoExoneracion = 0;
    private BigDecimal totalPago;
    private int mesesInteres = 0;
    private BigDecimal tasaPermiso;

    @PostConstruct
    public void initView() {
        totalPago = BigDecimal.ZERO;
        hacerEfectiva = false;
        tasaPermiso = BigDecimal.ZERO;
        provicionales = new LazyModel<>(FinaRenLiquidacion.class);
        provicionales.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(10L));

    }

    public void editarDatos(FinaRenLiquidacion liq) {
        provicional = liq;
        Long tipoLiquidacion = liq.getTipoLiquidacion().getId();
        rubrosLiquidacion = services.getRubrosPorLiquidacion(liq.getTipoLiquidacion().getId());
        balance = (RenBalancePatente) services.find("SELECT r FROM RenBalancePatente r WHERE r.liquidacion.id = :idLiquidacion", new String[]{"idLiquidacion"}, new Object[]{liq.getId()});

        switch (tipoLiquidacion.intValue()) {
            case 17: { // ACTIVOS TOTALES
                consultarRubros(provicional);
                JsfUtil.update("frmActivos");
                JsfUtil.executeJS("PF('dlgFormActivos').show();");
                break;
            }
            case 20: { // PATENTES
                consultarRubros(provicional);

                JsfUtil.update("frmPatente");
                JsfUtil.executeJS("PF('dlgFormPatente').show();");
                break;
            }
        }

    }

    public void guardarDatos() {
        String tipoLiquidacion = provicional.getTipoLiquidacion().getPrefijo();

        switch (tipoLiquidacion) {
            case "ACT": { // ACTIVOS TOTALES
                provicional.setSaldo(provicional.getTotalPago());
                for (FinaRenDetLiquidacion det : provicional.getRenDetLiquidacionCollection()) {
                    det.setValor(provicional.getTotalPago());
                    services.update(det);
                }
                JsfUtil.executeJS("PF('dlgFormActivos').hide();");
                break;
            }
            case "PAT": { // PATENTES
                System.out.println("Entro a guardar Pat");
                provicional.setSaldo(provicional.getTotalPago());
                List<FinaRenDetLiquidacion> dets = provicional.getRenDetLiquidacionCollection();
                for (FinaRenDetLiquidacion det : dets) {
                    if (det.getRubro().getId() == 518L) { //patente anuales esta bien
                        det.setValor(detalles.get(0).getValor());
                    }
                    if (det.getRubro().getId() == 200L) { //permiso de funcinamiento cambiar id
                        det.setValor(detalles.get(1).getValor());
                    }
//                    if (det.getRubro() == 201L) {
//                        det.setValor(detalles.get(2).getValor());
//                    }
                    services.update(det);

                }
                JsfUtil.executeJS("PF('dlgFormPatente').hide();");
                break;
            }
        }
        services.update(provicional);
        services.update(balance);
        JsfUtil.addInformationMessage("Info", "Datos guardados con éxito");
        initView();
        JsfUtil.update("mainForm");
    }

    public void anularLiquidacion(FinaRenLiquidacion liq) {
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
        services.update(liq);
        JsfUtil.addInformationMessage("Info", "Liquidacion anulada con éxito");
        initView();
        JsfUtil.update("mainForm");
    }

    public void hacerEfectiva(FinaRenLiquidacion liq) {
        // HiberUtil.newTransaction();
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        liq.setValidada(Boolean.TRUE);
        services.update(liq);
        JsfUtil.addInformationMessage("Info", "Liquidacion se ha generado para el cobro con éxito");
        initView();
        JsfUtil.update("mainForm");
    }

    public void imprimirComprobanteGestion(FinaRenLiquidacion liq) {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarParametros();
            ss.instanciarParametros();
            //ss.setTieneDatasource(Boolean.TRUE);

            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
            ss.addParametro("LOGO", path + SisVars.sisLogo);
            ss.addParametro("ID_LIQUIDACION", liq.getId());
            ss.addParametro("Firmas", ("/css/firmas/Rentas.png"));
            ss.addParametro("FirmaTesoreria", ("/css/firmas/Tesorero.png"));
            ss.addParametro("INTERES", BigDecimal.ZERO);
            if (liq.getTipoLiquidacion().getId() == 17L) {
                ss.setNombreReporte("gestionCobroActivosTotales");
            } else {
                ss.setNombreReporte("gestionCobroPatente");
            }

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Comprobantes");
        } catch (Exception e) {
            LOG.log(Level.OFF, null, e);
        }
    }

    public boolean renderAlcabalaSolicitante(FinaRenLiquidacion liq) {
        return liq.getTipoLiquidacion().getPrefijo().equals("ALC") && liq.getPredio() == null;
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        Boolean aplicaRemision = services.aplicaRemision(l);
        if (l.getTipoLiquidacion() != null) {
            if (l.getTipoLiquidacion().getTransaccionPadre() != null) {
                // 16L 00> permiso ambientale cambiar
                if (l.getTipoLiquidacion().getTransaccionPadre() == 16L && l.getAnio() < Utils.getAnio(new Date())) {
                    try {
                        Calendar fechaInteres = Calendar.getInstance();
                        fechaInteres.set(l.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
                        if (!aplicaRemision) {
                            l.setInteres(services.generarInteres(l.getSaldo(), fechaInteres.getTime(), null));
                        }
                        if (l.getInteres() == null) {
                            l.setInteres(BigDecimal.ZERO);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(LiquidacionProvicionalesMB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    interes2(l, aplicaRemision);
                }
            } else {
                interes2(l, aplicaRemision);
            }
        } else {
            interes2(l, aplicaRemision);

        }
        return l.getInteres();
    }

    public BigDecimal interes2(FinaRenLiquidacion l, Boolean aplicaRemision) {
        if (!aplicaRemision) {
            l.setInteres(services.interesCalculado(l, new Date()));
        } else {
            l.setInteres(BigDecimal.ZERO);
        }
        return l.getInteres();
    }

    public void consultarRubros(FinaRenLiquidacion liq) {

        detalles = new ArrayList();
        if (liq.getTipoLiquidacion() != null) {
        //ACTIVOS TOTALES
            if (liq.getTipoLiquidacion().getId() == 7) {
                if (balance != null) {
                    BigDecimal div = new BigDecimal("100");
                    BigDecimal base;

                    if (balance.getPorcentajeIngreso() == null || balance.getPorcentajeIngreso().compareTo(BigDecimal.ZERO) == 0) {
                        base = balance.getActivoTotal().subtract(balance.getPasivoTotal());
                    } else {
                        if (balance.getPorcentajeIngreso().compareTo(div) == 0) {
                            base = BigDecimal.ZERO;
                        } else {
                            base = balance.getActivoTotal().subtract(balance.getPasivoTotal()).multiply(balance.getPorcentajeIngreso()).divide(div);

                        }
                    }

                    detalles.add(0, new FinaRenDetLiquidacion(balance.getActivoTotal(), null, "Activos".toUpperCase()));
                    detalles.add(1, new FinaRenDetLiquidacion(balance.getPasivoTotal(), null, "Pasivos".toUpperCase()));
                    detalles.add(2, new FinaRenDetLiquidacion(balance.getActivoTotal().subtract(balance.getPasivoTotal()), null, "Diferencia".toUpperCase()));
                    detalles.add(3, new FinaRenDetLiquidacion(base, null, "Base Imponible".toUpperCase()));
                    detalles.add(4, new FinaRenDetLiquidacion(liq.getTotalPago(), null, "1.5 x mil".toUpperCase()));
                    detalles.add(5, new FinaRenDetLiquidacion(balance.getInteres(), null, "Interes".toUpperCase()));
                    totalPago = liq.getTotalPago().add(balance.getInteres());
                }
            } //PATENTE DE LOCALES COMERCIALES
            else if (liq.getTipoLiquidacion().getId() == 9L) {

                if (liq.getExoneracionDescripcion().equalsIgnoreCase("100% POR ARTESANO CALLFICADO POR LA JUNTA NACIONAL DE DEFENSA DEL ARTESANO.")) {
                    tipoExoneracion = 1;
                }
                if (liq.getExoneracionDescripcion().equalsIgnoreCase("HASTA LA 3RA PARTE POR DESCENSO EN LA UTILIDAD MAYOR AL 50% EN RELACIÓN CON EL PROMEDIO OBTENIDO EN LOS TRES AÑOS INMEDIATOS ANTERIORES.")) {
                    tipoExoneracion = 2;
                }
                if (liq.getExoneracionDescripcion().equalsIgnoreCase("50% POR HABER SUFRIDO PÉRDIDAS CONFORME A LA DECLARACIÓN ACEPTADA EN EL SRI, O POR ﬁSCALIZACIÓN EFECTUADA POR EL SRI O POR LA MUNICIPALIDAD O DISTRITO METROPOLITANO.")) {
                    tipoExoneracion = 3;
                }

                if (balance != null) {
                    for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                        detalles.add(new FinaRenDetLiquidacion(temp.getValor(), temp, temp.getDescripcion()));
                    }
                    for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                        if (temp.getCodigoRubro().intValue() == 1) {
                            detalles.get(0).setValor(liq.getTotalPago());

                        }
                        if (temp.getCodigoRubro().intValue() == 2) {
                            detalles.get(1).setValor(tasaPermiso);

                        }
                        if (temp.getCodigoRubro().intValue() == 3) {
                            detalles.get(2).setValor(balance.getInteres());

                        }

                    }
                }
                if (liq.getTotalPago() == null) {
                    liq.setTotalPago(BigDecimal.ZERO);
                }
                if (balance.getInteres() == null) {
                    balance.setInteres(BigDecimal.ZERO);
                }
                totalPago = liq.getTotalPago().add(balance.getInteres());
            }

        }
    }

    public void sumarActivos() {
        if (balance.getActivoTotal() == null) {
            balance.setActivoTotal(BigDecimal.ZERO);
        }
        if (balance.getPasivoTotal() == null) {
            balance.setPasivoTotal(BigDecimal.ZERO);
        }
        detalles.get(0).setValor(balance.getActivoTotal());
        detalles.get(2).setValor(balance.getActivoTotal().subtract(balance.getPasivoTotal()));
        impuesto();
    }

    public void sumarPasivos() {
        if (balance.getPasivoTotal() == null) {
            balance.setPasivoTotal(BigDecimal.ZERO);
        }
        detalles.get(1).setValor(balance.getPasivoTotal());
        detalles.get(2).setValor(balance.getActivoTotal().subtract(balance.getPasivoTotal()));

        impuesto();
    }

    public void impuesto() {
        BigDecimal base = baseImponible(); //(BigDecimal) util.getExpression("baseImponible", new Object[]{local});
        BigDecimal porcentajeImpuesto = BigDecimal.valueOf(1.5).divide(BigDecimal.valueOf(1000));
        BigDecimal total = base.multiply(porcentajeImpuesto); //(BigDecimal) util.getExpression("impuesto", new Object[]{local});
        BigDecimal interesAcumulado = BigDecimal.ZERO;
        System.out.println("1.5 x mil: " + total);

        if (balance.getPorcentajeParticipacion() != null) {
            total = total.multiply(balance.getPorcentajeParticipacion()).divide(new BigDecimal("100"));
        }

        if (total.compareTo(BigDecimal.TEN) < 0) {
            total = BigDecimal.TEN;
        }

        provicional.setTotalPago(total);
        totalPago = total;
        if (balance.getAnioBalance() != null) {
            mesesInteres = 0;
            provicional.setAnio(balance.getAnioBalance());
            balance.setInteres(interesAcumulado);
            balance.setMesesInteres(mesesInteres);
            totalPago = totalPago.add(interesAcumulado);
        } else {
            detalles.get(5).setValor(BigDecimal.ZERO);
            mesesInteres = 0;
        }

        detalles.get(3).setValor(base);
        detalles.get(4).setValor(total);

    }

    public BigDecimal baseImponible() {
        BigDecimal base = BigDecimal.ZERO;
        BigDecimal div = new BigDecimal("100");
        try {
            if (balance.getPorcentajeIngreso() == null || balance.getPorcentajeIngreso().compareTo(BigDecimal.ZERO) == 0) {
                base = balance.getActivoTotal().subtract(balance.getPasivoTotal());
            } else {
                if (balance.getPorcentajeIngreso().compareTo(div) == 0) {
                    base = BigDecimal.ZERO;
                } else {
                    base = balance.getActivoTotal().subtract(balance.getPasivoTotal()).multiply(balance.getPorcentajeIngreso()).divide(div);

                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Base Imponible", e);
        }
        return base;
    }

    public void generarValorLiquidacion() {
        try {

            System.out.println("Entro a liquidar");
            RenFactorPorMetro factor1;
            RenFactorPorCapital factor2;
            BigDecimal valorLiq;
            BigDecimal interesAcumulado = BigDecimal.ZERO;

            if (balance.getCapital() != null && balance.getCapital().compareTo(BigDecimal.ZERO) > 0) {
                factor2 = (RenFactorPorCapital) services.find("SELECT r FROM RenFactorPorCapital r WHERE :capital > r.desde AND :capital <= r.hasta", new String[]{"capital"}, new Object[]{balance.getCapital()});
                if (factor2.getAplicaPorcentaje()) {
                    valorLiq = balance.getCapital().multiply(factor2.getValor()).divide(new BigDecimal("100"));
                } else {
                    valorLiq = factor2.getValor();
                }
                String descripcion = "";
                switch (tipoExoneracion) {
                    case 1: {
                        valorLiq = BigDecimal.ZERO;
                        descripcion = "100% POR ARTESANO CALLFICADO POR LA JUNTA NACIONAL DE DEFENSA DEL ARTESANO.";
                        break;
                    }
                    case 2: {
                        valorLiq = valorLiq.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        descripcion = "HASTA LA 3RA PARTE POR DESCENSO EN LA UTILIDAD MAYOR AL 50% EN RELACIÓN CON EL PROMEDIO OBTENIDO EN LOS TRES AÑOS INMEDIATOS ANTERIORES.";
                        break;
                    }
                    case 3: {
                        valorLiq = valorLiq.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
                        descripcion = "50% POR HABER SUFRIDO PÉRDIDAS CONFORME A LA DECLARACIÓN ACEPTADA EN EL SRI, O POR ﬁSCALIZACIÓN EFECTUADA POR EL SRI O POR LA MUNICIPALIDAD O DISTRITO METROPOLITANO.";
                        break;
                    }
                }
                provicional.setExoneracionDescripcion(descripcion);

                if (balance.getPorcentajeParticipacion() != null) {

                    valorLiq = valorLiq.multiply(balance.getPorcentajeParticipacion()).divide(new BigDecimal("100"));
                }

                // Pago maximo de 25000 USD
                if (valorLiq.compareTo(new BigDecimal("25000")) > 0) {
                    valorLiq = new BigDecimal("25000");
                }
                // Pago minimo de 10 USD
                if (valorLiq.compareTo(BigDecimal.TEN) < 0) {
                    valorLiq = BigDecimal.TEN;
                }

                for (FinaRenRubrosLiquidacion temp : rubrosLiquidacion) {
                    if (temp.getCodigoRubro().intValue() == 1) {
                        detalles.get(0).setValor(valorLiq);
                        provicional.setTotalPago(valorLiq);
                        System.out.println("Rubro 1: " + valorLiq);
                    }
                    if (temp.getCodigoRubro().intValue() == 2) {
                        detalles.get(1).setValor(tasaPermiso);
                        provicional.setTotalPago(provicional.getTotalPago().add(tasaPermiso));

                    }
                    if (temp.getCodigoRubro().intValue() == 3) {

                        if (balance.getAnioBalance() != null) {
                            mesesInteres = 0;
                            provicional.setAnio(balance.getAnioBalance());

                            detalles.get(2).setValor(interesAcumulado);
                            provicional.setTotalPago(provicional.getTotalPago().add(detalles.get(2).getValor()));
                            balance.setInteres(interesAcumulado);
                            balance.setMesesInteres(mesesInteres);
                        } else {
                            detalles.get(2).setValor(BigDecimal.ZERO);
                            mesesInteres = 0;
                        }
                    }

                }

                balance.setMesesInteres(mesesInteres);
                balance.setInteres(interesAcumulado);
                totalPago = provicional.getTotalPago();

            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public LazyModel<FinaRenLiquidacion> getProvicionales() {
        return provicionales;
    }

    public void setProvicionales(LazyModel<FinaRenLiquidacion> provicionales) {
        this.provicionales = provicionales;
    }

    public FinaRenLiquidacion getProvicional() {
        return provicional;
    }

    public void setProvicional(FinaRenLiquidacion provicional) {
        this.provicional = provicional;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosLiquidacion() {
        return rubrosLiquidacion;
    }

    public void setRubrosLiquidacion(List<FinaRenRubrosLiquidacion> rubrosLiquidacion) {
        this.rubrosLiquidacion = rubrosLiquidacion;
    }

    public List<FinaRenDetLiquidacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FinaRenDetLiquidacion> detalles) {
        this.detalles = detalles;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public RenBalancePatente getBalance() {
        return balance;
    }

    public void setBalance(RenBalancePatente balance) {
        this.balance = balance;
    }

    public boolean isHacerEfectiva() {
        return hacerEfectiva;
    }

    public void setHacerEfectiva(boolean hacerEfectiva) {
        this.hacerEfectiva = hacerEfectiva;
    }

    public short getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(short tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public int getMesesInteres() {
        return mesesInteres;
    }

    public void setMesesInteres(int mesesInteres) {
        this.mesesInteres = mesesInteres;
    }

    public BigDecimal getTasaPermiso() {
        return tasaPermiso;
    }

    public void setTasaPermiso(BigDecimal tasaPermiso) {
        this.tasaPermiso = tasaPermiso;
    }

    
    
}
