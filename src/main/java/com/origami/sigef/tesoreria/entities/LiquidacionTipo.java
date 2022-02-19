/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author gutya
 */
@Entity
@Table(schema = "tesoreria", name = "liquidacion_tipo")
public class LiquidacionTipo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "transaccion_padre")
    private Long transaccionPadre;
    @Size(max = 20)
    @Column(name = "cta_transaccion")
    private String ctaTransaccion;
    @Column(name = "prefijo")
    private String prefijo;
    @Column(name = "nombre_titulo")
    private String nombreTitulo;
    @Size(max = 500)
    @Column(name = "nombre_transaccion")
    private String nombreTransaccion;
    @Column(name = "estado")
    private Boolean estado = Boolean.TRUE;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "permite_anulacion")
    private Boolean permiteAnulacion = Boolean.FALSE;
    @Column(name = "permite_exoneracion")
    private Boolean permiteExoneracion = Boolean.FALSE;

    public LiquidacionTipo() {
    }

    public LiquidacionTipo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTransaccionPadre() {
        return transaccionPadre;
    }

    public void setTransaccionPadre(Long transaccionPadre) {
        this.transaccionPadre = transaccionPadre;
    }

    public String getCtaTransaccion() {
        return ctaTransaccion;
    }

    public void setCtaTransaccion(String ctaTransaccion) {
        this.ctaTransaccion = ctaTransaccion;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public String getNombreTransaccion() {
        return nombreTransaccion;
    }

    public void setNombreTransaccion(String nombreTransaccion) {
        this.nombreTransaccion = nombreTransaccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Boolean getPermiteAnulacion() {
        return permiteAnulacion;
    }

    public void setPermiteAnulacion(Boolean permiteAnulacion) {
        this.permiteAnulacion = permiteAnulacion;
    }

    public Boolean getPermiteExoneracion() {
        return permiteExoneracion;
    }

    public void setPermiteExoneracion(Boolean permiteExoneracion) {
        this.permiteExoneracion = permiteExoneracion;
    }

}
