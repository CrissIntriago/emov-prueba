/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.talentohumano.services.CuentaBancariaService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "cuentaBancariaView")
@ViewScoped
public class CuentaBancariaController implements Serializable {

    @Inject
    private CuentaContableService cuentaContableService;

    @Inject
    private BancoService bancoService;
    @Inject
    private CuentaBancariaService ctaBancariaService;
    @Inject
    private UserSession userSession;

    private LazyModel<CuentaBancaria> lazy;

    private List<CuentaContable> cuentasPatrimoniales;
    private CuentaContable ctaMovimineto;
    private List<CuentaContable> cuentasMovimiento;
    private Banco numBanco;
    private List<Banco> numeroBancos;
    private CuentaBancaria ctaSeleccionada;
    private short anio;
    private CuentaBancaria ctaBancaria;

    @PostConstruct
    public void init() {
        ctaBancaria = new CuentaBancaria();
        ctaSeleccionada = new CuentaBancaria();
        lazy = new LazyModel<>(CuentaBancaria.class);
        lazy.getFilterss().put("estado", Boolean.TRUE);
        anio = Utils.getAnio(new Date()).shortValue();
        cuentasPatrimoniales = cuentaContableService.getcuentasPratrimoniales(anio);
        numeroBancos = bancoService.findAll();
        cuentasMovimiento = new ArrayList<>();
    }

    public void ctaMovimiento(CuentaContable cuenta) {
        cuentasMovimiento = cuentaContableService.getHijosCtaBanco(cuenta.getCodigo(), anio);
    }

    public void guardar() {
        boolean edit = ctaBancaria.getId() != null;
        try {
            if (edit) {
                ctaBancaria.setFechaModificacion(new Date());
                ctaBancaria.setUsuarioModifica(userSession.getNameUser());
                ctaBancariaService.edit(ctaBancaria);
            } else {
                ctaBancaria.setFechaCreacion(new Date());
                ctaBancaria.setUsuarioCreacion(userSession.getNameUser());
                ctaBancaria = ctaBancariaService.create(ctaBancaria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ctaBancaria = new CuentaBancaria();
        JsfUtil.addInformationMessage("Cuentas Bancarias", "Datos " + (edit ? "Editados" : "Registrados") + " Correctamente");
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void cancelar() {
        ctaBancaria = new CuentaBancaria();
        JsfUtil.addInformationMessage("Cuentas Bancarias", "Proceso cancelado con xito");
        PrimeFaces.current().ajax().update("frmMain");
    }

    public void editar(CuentaBancaria cta) {
        cuentasMovimiento = cuentaContableService.getHijosByPadre(cta.getCuentaPatrimonial());
        this.ctaBancaria = cta;
        ctaBancaria.setCuentaMovimiento(cta.getCuentaMovimiento());

    }

    public void eliminar(CuentaBancaria cta) {
        System.out.println("cuenta eliminar --> "+cta);
        if (ctaBancariaService.getMovimientoCuenta(cta)) {
            JsfUtil.addWarningMessage("La Cuenta Seleccionanda se encuentra en movimiento", "");
            return;
        }
        cta.setEstado(Boolean.FALSE);
        ctaBancariaService.edit(cta);
    }

//<editor-fold defaultstate="collapsed" desc="GETTERs & SETTERs">
    public LazyModel<CuentaBancaria> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<CuentaBancaria> lazy) {
        this.lazy = lazy;
    }

    public List<CuentaContable> getCuentasPatrimoniales() {
        return cuentasPatrimoniales;
    }

    public void setCuentasPatrimoniales(List<CuentaContable> cuentasPatrimoniales) {
        this.cuentasPatrimoniales = cuentasPatrimoniales;
    }

    public List<CuentaContable> getCuentasMovimiento() {
        return cuentasMovimiento;
    }

    public void setCuentasMovimiento(List<CuentaContable> cuentasMovimiento) {
        this.cuentasMovimiento = cuentasMovimiento;
    }

    public List<Banco> getNumeroBancos() {
        return numeroBancos;
    }

    public void setNumeroBancos(List<Banco> numeroBancos) {
        this.numeroBancos = numeroBancos;
    }

    public CuentaBancaria getCtaSeleccionada() {
        return ctaSeleccionada;
    }

    public void setCtaSeleccionada(CuentaBancaria ctaSeleccionada) {
        this.ctaSeleccionada = ctaSeleccionada;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public CuentaBancaria getCtaBancaria() {
        return ctaBancaria;
    }

    public void setCtaBancaria(CuentaBancaria ctaBancaria) {
        this.ctaBancaria = ctaBancaria;
    }

    public CuentaContable getCtaMovimineto() {
        return ctaMovimineto;
    }

    public void setCtaMovimineto(CuentaContable ctaMovimineto) {
        this.ctaMovimineto = ctaMovimineto;
    }

    public Banco getNumBanco() {
        return numBanco;
    }

    public void setNumBanco(Banco numBanco) {
        this.numBanco = numBanco;
    }
//</editor-fold>

}
