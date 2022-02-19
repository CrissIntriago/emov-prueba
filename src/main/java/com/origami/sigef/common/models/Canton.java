/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.models;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class Canton implements Serializable {

    private String nombre;
    private String codigo;
    private Long idcanton;
    private Double longitud;
    private Double latitud;

    public Canton() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getIdcanton() {
        return idcanton;
    }

    public void setIdcanton(Long idcanton) {
        this.idcanton = idcanton;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    @Override
    public String toString() {
        return "Canton{" + "nombre=" + nombre + ", codigo=" + codigo + ", idcanton=" + idcanton + ", longitud=" + longitud + ", latitud=" + latitud + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idcanton);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Canton other = (Canton) obj;
        if (!Objects.equals(this.idcanton, other.idcanton)) {
            return false;
        }
        return true;
    }

}
