/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import java.io.Serializable;

/**
 *
 * @author ORIGAMI
 */
public class DevengadoCobradoDTO implements Serializable {

    private String cuenta;
    private CatalogoPresupuesto debito;
    private CatalogoPresupuesto credito;
    private CatalogoPresupuesto cobrado_devengado;
    private Boolean cta_pagar_cobrar;

    public DevengadoCobradoDTO() {
    }

    public DevengadoCobradoDTO(String cuenta, CatalogoPresupuesto debito, CatalogoPresupuesto credito, CatalogoPresupuesto cobrado_devengado, Boolean cta_pagar_cobrar) {
        this.cuenta = cuenta;
        this.debito = debito;
        this.credito = credito;
        this.cobrado_devengado = cobrado_devengado;
        this.cta_pagar_cobrar = cta_pagar_cobrar;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public CatalogoPresupuesto getDebito() {
        return debito;
    }

    public void setDebito(CatalogoPresupuesto debito) {
        this.debito = debito;
    }

    public CatalogoPresupuesto getCredito() {
        return credito;
    }

    public void setCredito(CatalogoPresupuesto credito) {
        this.credito = credito;
    }

    public CatalogoPresupuesto getCobrado_devengado() {
        return cobrado_devengado;
    }

    public void setCobrado_devengado(CatalogoPresupuesto cobrado_devengado) {
        this.cobrado_devengado = cobrado_devengado;
    }

    public Boolean getCta_pagar_cobrar() {
        return cta_pagar_cobrar;
    }

    public void setCta_pagar_cobrar(Boolean cta_pagar_cobrar) {
        this.cta_pagar_cobrar = cta_pagar_cobrar;
    }

}
