/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author ANGEL NAVARRO
 */
@Table(name = "log_transaccion", schema = "audit")
@Entity
@NamedQueries({
    @NamedQuery(name = "LogTransaccion.findAll", query = "SELECT l FROM LogTransaccion l"),
    @NamedQuery(name = "LogTransaccion.findById", query = "SELECT m FROM LogTransaccion m WHERE m.id = :id")})
public class LogTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "fecha", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Column(nullable = false)
    private String entidad;
    @Column(name = "entidad_id")
    private BigInteger entidadId;
    @Column(name = "evento")
    private String evento;
    @Column(name = "dato_anterior")
    private String datoAnterior;
    @Column(name = "dato_actual")
    private String datoActual;
    @Column(name = "equipo")
    private String equipo;
    @Column(name = "ip_equipo")
    private String ipEquipo;
    @OneToMany(mappedBy = "logTransaccion", cascade = CascadeType.ALL)
    private List<LogTransaccionDetalle> logDetalles;
    @Transient
    private Object entity;
    @Transient
    private Object entity1;

    public LogTransaccion() {
    }

    public LogTransaccion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public BigInteger getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(BigInteger entidadId) {
        this.entidadId = entidadId;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getDatoAnterior() {
        return datoAnterior;
    }

    public void setDatoAnterior(String datoAnterior) {
        this.datoAnterior = datoAnterior;
    }

    public String getDatoActual() {
        return datoActual;
    }

    public void setDatoActual(String datoActual) {
        this.datoActual = datoActual;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getIpEquipo() {
        return ipEquipo;
    }

    public void setIpEquipo(String ipEquipo) {
        this.ipEquipo = ipEquipo;
    }

    public List<LogTransaccionDetalle> getLogDetalles() {
        return logDetalles;
    }

    public void setLogDetalles(List<LogTransaccionDetalle> logDetalles) {
        this.logDetalles = logDetalles;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Object getEntity1() {
        return entity1;
    }

    public void setEntity1(Object entity1) {
        this.entity1 = entity1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final LogTransaccion other = (LogTransaccion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LogTransaccion{" + "id=" + id + ", fecha=" + fecha + ", usuario=" + usuario + ", entidad=" + entidad + ", entidadId=" + entidadId + ", evento=" + evento + ", datoAnterior=" + datoAnterior + ", datoActual=" + datoActual + ", equipo=" + equipo + ", ipEquipo=" + ipEquipo + ", entity1=" + entity1 + '}';
    }

}
