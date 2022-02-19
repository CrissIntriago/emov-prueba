/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class GeoVias implements Serializable {

    private BigInteger gid;
    private String nombre;
    private String claveCata;
    private String codigo;
    private Long orden;

    public BigInteger getGid() {
        return gid;
    }

    public void setGid(BigInteger gid) {
        this.gid = gid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClaveCata() {
        return claveCata;
    }

    public void setClaveCata(String claveCata) {
        this.claveCata = claveCata;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
