/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.common.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class ListadoInventarioModel implements Serializable {

    private Long id;
    private String codigoAgrupado;
    private String descripcion;
    private String area;
    private String grupo;
    private String subgrupo;
    private String medida;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal total;
    private Short estante;
    private Short fila;
    private Short columna;
    private Short cajon;
    private Short cuadrante;
    private String cuentaContable;
    private String nombreCuenta;

    public ListadoInventarioModel() {
    }

    public ListadoInventarioModel(Long id, String codigoAgrupado, String descripcion, String area, String grupo, String subgrupo, String medida, Integer cantidad, BigDecimal precio, BigDecimal total, Short estante, Short fila, Short columna, Short cajon, Short cuadrante, String cuentaContable, String nombreCuenta) {
        this.id = id;
        this.codigoAgrupado = codigoAgrupado;
        this.descripcion = descripcion;
        this.area = area;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.medida = medida;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.estante = estante;
        this.fila = fila;
        this.columna = columna;
        this.cajon = cajon;
        this.cuadrante = cuadrante;
        this.cuentaContable = cuentaContable;
        this.nombreCuenta = nombreCuenta;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoAgrupado() {
        return codigoAgrupado;
    }

    public void setCodigoAgrupado(String codigoAgrupado) {
        this.codigoAgrupado = codigoAgrupado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Short getEstante() {
        return estante;
    }

    public void setEstante(Short estante) {
        this.estante = estante;
    }

    public Short getFila() {
        return fila;
    }

    public void setFila(Short fila) {
        this.fila = fila;
    }

    public Short getColumna() {
        return columna;
    }

    public void setColumna(Short columna) {
        this.columna = columna;
    }

    public Short getCajon() {
        return cajon;
    }

    public void setCajon(Short cajon) {
        this.cajon = cajon;
    }

    public Short getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(Short cuadrante) {
        this.cuadrante = cuadrante;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

}
