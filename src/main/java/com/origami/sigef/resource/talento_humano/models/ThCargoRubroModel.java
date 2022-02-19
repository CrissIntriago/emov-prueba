/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.models;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;

/**
 *
 * @author Administrator
 */
public class ThCargoRubroModel {

    private ThCargo thCargo;
    private ThRubro thRubro;
    private ContCuentas contCuentas;

    public ThCargoRubroModel() {
    }

    public ThCargoRubroModel(ThCargo thCargo, ThRubro thRubro) {
        this.thCargo = thCargo;
        this.thRubro = thRubro;
    }

    public ThCargoRubroModel(ThCargo thCargo, ThRubro thRubro, ContCuentas contCuentas) {
        this.thCargo = thCargo;
        this.thRubro = thRubro;
        this.contCuentas = contCuentas;
    }

    public ThCargo getThCargo() {
        return thCargo;
    }

    public void setThCargo(ThCargo thCargo) {
        this.thCargo = thCargo;
    }

    public ThRubro getThRubro() {
        return thRubro;
    }

    public void setThRubro(ThRubro thRubro) {
        this.thRubro = thRubro;
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

}
