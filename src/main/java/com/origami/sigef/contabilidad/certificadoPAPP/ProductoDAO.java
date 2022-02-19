/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.certificadoPAPP;

import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.service.PappProcesoService;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author ORIGAMI2
 */
public class ProductoDAO implements Serializable {

    @Inject
    private PappProcesoService procesoService;

    private Producto producto;
    private BigDecimal montoSolcitado;
   
    private Boolean estado;


    @PostConstruct
    public void init() {
        this.estado = Boolean.FALSE;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getMontoSolcitado() {
        return montoSolcitado;
    }

    public void setMontoSolcitado(BigDecimal montoSolcitado) {
        this.montoSolcitado = montoSolcitado;
    }

   

    

}
