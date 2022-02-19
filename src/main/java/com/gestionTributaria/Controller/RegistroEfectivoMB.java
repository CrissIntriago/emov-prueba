/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.EfectivoDinero;
import com.gestionTributaria.Services.RegistroEfectivoServices;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class RegistroEfectivoMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(RegistroEfectivoMB.class.getName());
    private Date fechaActual;
    private EfectivoDinero efectivoDinero;
    private LazyModel<EfectivoDinero> registros;
    @Inject
    private RegistroEfectivoServices registroEfectivoServices;
    @Inject
    private UserSession session;
    @Inject
    private KatalinaService katalinaService;
    private EfectivoDinero registroEfectivo;

    private Cajero caja;
    private BigDecimal totalGenerado;

    @PostConstruct
    public void init() {
        totalGenerado = BigDecimal.ZERO;
        registroEfectivo = new EfectivoDinero();
        registros = new LazyModel<>(EfectivoDinero.class);
        registros.getFilterss().put("usuario", BigInteger.valueOf(session.getUserId()));
        registros.getSorteds().put("id", "desc");
        efectivoDinero = new EfectivoDinero();
        fechaActual = new Date();
        caja = katalinaService.findCajero();
    }

    public void sumarRegistro() {
        totalGenerado = BigDecimal.ZERO;
        if (efectivoDinero.getBillete100() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBillete100()).multiply(new BigDecimal(100)));
        }
        if (efectivoDinero.getBillete50() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBillete50()).multiply(new BigDecimal(50)));
        }
        if (efectivoDinero.getBillete20() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBillete20()).multiply(new BigDecimal(20)));
        }
        if (efectivoDinero.getBillete10() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBillete10()).multiply(new BigDecimal(10)));
        }
        if (efectivoDinero.getBillete5() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBillete5()).multiply(new BigDecimal(5)));
        }
        if (efectivoDinero.getBilleteMoneda1() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getBilleteMoneda1()).multiply(new BigDecimal(1)));
        }
        if (efectivoDinero.getMonedaDolar() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMonedaDolar()).multiply(new BigDecimal(1)));
        }
        if (efectivoDinero.getMoneda50() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMoneda50()).multiply(new BigDecimal(0.50)));
        }
        if (efectivoDinero.getMoneda25() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMoneda25()).multiply(new BigDecimal(0.25)));
        }
        if (efectivoDinero.getMoneda10() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMoneda10()).multiply(new BigDecimal(0.10)));
        }
        if (efectivoDinero.getMoneda5() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMoneda5()).multiply(new BigDecimal(0.05)));
        }
        if (efectivoDinero.getMoneda1() != null) {
            totalGenerado = totalGenerado.add(new BigDecimal(efectivoDinero.getMoneda1()).multiply(new BigDecimal(0.01)));
        }
        totalGenerado = totalGenerado.setScale(2, RoundingMode.HALF_UP);
        System.out.println("total generado >>" + totalGenerado);
    }

    public void saveRegistroEfectivo() {
        try {
            if (registroEfectivoServices.getRegistroByFechaAndCajero(fechaActual, caja)) {
                JsfUtil.addWarningMessage("ERROR", "No puede realizar Registros con la misma Fecha...");
                return;
            }
            if (efectivoDinero.getId() != null) {
                registroEfectivoServices.edit(efectivoDinero);
                JsfUtil.addSuccessMessage("Info:", "Se Edito con 'exito.");
                limpiar();
                JsfUtil.update("mainForm");
            } else {
                if (!efectivoDinero.getObservaciones().isEmpty() && efectivoDinero != null) {
                    efectivoDinero.setFechaRegistro(fechaActual);
                    efectivoDinero.setUsuario(BigInteger.valueOf(session.getUserId()));
                    efectivoDinero.setCaja(caja);
                    efectivoDinero = registroEfectivoServices.create(efectivoDinero);
                    JsfUtil.addSuccessMessage("Info:", "Se registro con 'exito.");
                    limpiar();
                    JsfUtil.update("mainForm");
                } else {
                    JsfUtil.addWarningMessage("Info:", "Debe registrar una observacion.");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error al registrar: " + ex.getMessage());
        }
    }

    public void abrirModal(EfectivoDinero cantidad) {
        System.out.println("cantidad" + cantidad);
        registroEfectivo = cantidad;
        JsfUtil.update("formInteres");
        JsfUtil.executeJS("PF('dlgRegistros').show()");
    }

    public void editar(EfectivoDinero efec) {
        System.out.println("cantidad" + efec);
        efectivoDinero = efec;
        JsfUtil.update("mainForm:panelRegistroDinero");
    }

    public void limpiar() {
        efectivoDinero = new EfectivoDinero();
        totalGenerado = BigDecimal.ZERO;
        JsfUtil.update(":mainForm:panelBilletes");
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public BigDecimal getTotalGenerado() {
        return totalGenerado;
    }

    public void setTotalGenerado(BigDecimal totalGenerado) {
        this.totalGenerado = totalGenerado;
    }

    public EfectivoDinero getEfectivoDinero() {
        return efectivoDinero;
    }

    public EfectivoDinero getRegistroEfectivo() {
        return registroEfectivo;
    }

    public void setRegistroEfectivo(EfectivoDinero registroEfectivo) {
        this.registroEfectivo = registroEfectivo;
    }

    public void setEfectivoDinero(EfectivoDinero efectivoDinero) {
        this.efectivoDinero = efectivoDinero;
    }

    public RegistroEfectivoServices getRegistroEfectivoServices() {
        return registroEfectivoServices;
    }

    public void setRegistroEfectivoServices(RegistroEfectivoServices registroEfectivoServices) {
        this.registroEfectivoServices = registroEfectivoServices;
    }

    public LazyModel<EfectivoDinero> getRegistros() {
        return registros;
    }

    public void setRegistros(LazyModel<EfectivoDinero> registros) {
        this.registros = registros;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }
//</editor-fold>

}
