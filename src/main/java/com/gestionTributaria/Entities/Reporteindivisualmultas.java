/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "reporteindivisualmultas", schema = Utils.SCHEMA_COMISARIA)
public class Reporteindivisualmultas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    private Long contribuyente;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String direccion;
    private String email;
    private String usuario_ingreso;
    private BigInteger comisaria;

    public Reporteindivisualmultas() {
    }

    public Reporteindivisualmultas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Long contribuyente) {
        this.contribuyente = contribuyente;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario_ingreso() {
        return usuario_ingreso;
    }

    public void setUsuario_ingreso(String usuario_ingreso) {
        this.usuario_ingreso = usuario_ingreso;
    }

    public BigInteger getComisaria() {
        return comisaria;
    }

    public void setComisaria(BigInteger comisaria) {
        this.comisaria = comisaria;
    }

    public String toString() {
        return "id" + id;
    }

}
