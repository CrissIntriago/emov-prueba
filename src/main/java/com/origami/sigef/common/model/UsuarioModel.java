/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.model;

/**
 *
 * @author gutya
 */
public class UsuarioModel {

    private Long id;
    private String usuario;
    private String contrasenia;
    private String identificacion;
    private String ruc;
    private String nombre;
    private String apellido;

    public UsuarioModel() {
    }

    public UsuarioModel(Long id, String usuario, String contrasenia,
            String identificacion, String ruc, String nombre, String apellido) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.identificacion = identificacion;
        this.ruc = ruc;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

}
