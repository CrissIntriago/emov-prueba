/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.ws;

/**
 *
 * @author jesus
 */
public class DetalleItemWS {

    private Long id;
    private String descripcion;

    public DetalleItemWS(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public DetalleItemWS() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
