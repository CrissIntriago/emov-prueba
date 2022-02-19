/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ANGEL NAVARRO
 */
@Table(name = "log_transaccion_detalle", schema = "audit")
@Entity
@NamedQueries({
    @NamedQuery(name = "LogTransaccionDetalle.findAll", query = "SELECT l FROM LogTransaccionDetalle l"),
    @NamedQuery(name = "LogTransaccionDetalle.findById", query = "SELECT l FROM LogTransaccionDetalle l WHERE l.id = :id")})
public class LogTransaccionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "log_transaccion", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private LogTransaccion logTransaccion;
    @Column
    private String titulo;
    @Column(nullable = false)
    private String campo;
    @Column(name = "dato_anterior")
    private String datoAnterior;
    @Column(name = "dato_actual")
    private String datoActual;

    public LogTransaccionDetalle() {
    }

    public LogTransaccionDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogTransaccion getLogTransaccion() {
        return logTransaccion;
    }

    public void setLogTransaccion(LogTransaccion logTransaccion) {
        this.logTransaccion = logTransaccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final LogTransaccionDetalle other = (LogTransaccionDetalle) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LogTransaccionDetalle{" + "id=" + id + ", titulo=" + titulo + ", campo=" + campo + ", datoAnterior=" + datoAnterior + ", datoActual=" + datoActual + '}';
    }

}
