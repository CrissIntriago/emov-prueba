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
public class Provincia implements Serializable {

    private String nombre;
    private String codigo;
    private Long idprovincia;
    private Double longitud;
    private Double latitud;

    public Provincia() {
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

    public Long getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(Long idprovincia) {
        this.idprovincia = idprovincia;
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
        return "Provincia{" + "nombre=" + nombre + ", codigo=" + codigo + ", idprovincia=" + idprovincia + ", longitud=" + longitud + ", latitud=" + latitud + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idprovincia);
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
        final Provincia other = (Provincia) obj;
        if (!Objects.equals(this.idprovincia, other.idprovincia)) {
            return false;
        }
        return true;
    }

}
