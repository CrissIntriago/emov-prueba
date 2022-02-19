/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.model;

import java.io.Serializable;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class Imagenes implements Serializable {

    private String descripcion;
    private String url;

    public Imagenes() {
    }

    public Imagenes(String descripcion, String url) {
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
