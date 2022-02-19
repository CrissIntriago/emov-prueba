/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PredioAllService implements Serializable {

    private List<BloquePredio> bloques;
    private List<CtInfoLinderos> linderos;
    private List<GeoVias> vias;
    private BigDecimal frente;
    private BigDecimal area;

    public List<BloquePredio> getBloques() {
        return bloques;
    }

    public void setBloques(List<BloquePredio> bloques) {
        this.bloques = bloques;
    }

    public List<CtInfoLinderos> getLinderos() {
        return linderos;
    }

    public void setLinderos(List<CtInfoLinderos> linderos) {
        this.linderos = linderos;
    }

    public List<GeoVias> getVias() {
        return vias;
    }

    public void setVias(List<GeoVias> vias) {
        this.vias = vias;
    }

    public BigDecimal getFrente() {
        return frente;
    }

    public void setFrente(BigDecimal frente) {
        this.frente = frente;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BloquePredio getBloque(int numEdf) {
        if (bloques != null) {
            for (BloquePredio b : bloques) {
                if (b.getNumBloq() == numEdf) {
                    return b;
                }
            }
        }
        return null;
    }

}
