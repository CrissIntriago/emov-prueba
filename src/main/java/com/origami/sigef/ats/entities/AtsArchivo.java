/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author gutya
 */
@Entity
@Table(schema = "ats", name = "ats_archivo")
public class AtsArchivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private Long tramite;
    private Long diario;
    private String descripcion;
    private Integer anio;
    private Integer mes;
    private Date fecha;
    private String usuario;
    private String archivo;

    public AtsArchivo() {
    }

    public AtsArchivo(Long tramite, String descripcion, Date fecha,
            String usuario, String archivo, Integer anio, Integer mes) {
        this.tramite = tramite;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuario = usuario;
        this.archivo = archivo;
        this.anio = anio;
        this.mes = mes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Long getDiario() {
        return diario;
    }

    public void setDiario(Long diario) {
        this.diario = diario;
    }

}
