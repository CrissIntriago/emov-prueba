/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Models;

/**
 *
 * @author Administrator
 */
public enum TipoEvento {

    PENDIENTE("advertencia", "advertencia"),
    URGENTE("importante", "importante"),
    CANCELADO("especial", "especial");

    private final String descripcion;
    private final String css;

    private TipoEvento(String descricao, String css) {
        this.css = css;
        this.descripcion = descricao;
    }

    public String getCss() {
        return css;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
