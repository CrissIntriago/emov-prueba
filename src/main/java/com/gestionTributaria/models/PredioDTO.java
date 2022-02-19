/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class PredioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private CatPredioPropietario propietario;
    private CatPredio predio;

    public PredioDTO(CatPredioPropietario propietario, CatPredio predio) {
        this.propietario = propietario;
        this.predio = predio;
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        this.propietario = propietario;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    @Override
    public String toString() {
        return "PredioDTO{" + "propietario=" + propietario + ", predio=" + predio + '}';
    }

}
