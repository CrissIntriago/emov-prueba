/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Transient;

/**
 *
 * @author ORIGAMI2
 */
public class ItemTarifarioModelTS implements Serializable{
    
    @Transient
    private Long id_tarifario;
    @Transient
    private String nombre;
    @Transient
    private BigDecimal valor;
    @Transient
    private String codigo;

    public ItemTarifarioModelTS() {

    }

    public ItemTarifarioModelTS(Long id) {
        this.id_tarifario = id;
    }

    public Long getId_tarifario() {
        return id_tarifario;
    }

    public void setId_tarifario(Long id_tarifario) {
        this.id_tarifario = id_tarifario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
