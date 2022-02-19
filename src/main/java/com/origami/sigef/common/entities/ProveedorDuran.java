/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Luis Pozo gonzabay
 */
@Entity
@Table(name = "proveedor_duran", schema = "migracion")
public class ProveedorDuran implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    @Column(name = "tipo_persona")
    private String tipoPersona;
    @Column(name = "codigo_banco")
    private String codigoBanco;
    @Column(name = "nombre_banco")
    private String nombreBanco;
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Column(name = "estado_cuenta")
    private String estadoCuenta;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;

    public ProveedorDuran() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public String toString() {
        return "ProveedorDuran{" + "id=" + id + ", tipo=" + tipo + ", identificacion=" + identificacion + ", nombre=" + nombre + ", nombreComercial=" + nombreComercial + ", tipoPersona=" + tipoPersona + ", codigoBanco=" + codigoBanco + ", nombreBanco=" + nombreBanco + ", numeroCuenta=" + numeroCuenta + ", estadoCuenta=" + estadoCuenta + ", tipoCuenta=" + tipoCuenta + '}';
    }

}
