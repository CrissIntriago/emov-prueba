/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class TipoTurismo implements Serializable {

    private Integer id;
    private BigInteger idb;
    private String descripcion;

    public TipoTurismo() {
    }

    public TipoTurismo(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getIdb() {
        return idb;
    }

    public void setIdb(BigInteger idb) {
        this.idb = idb;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
