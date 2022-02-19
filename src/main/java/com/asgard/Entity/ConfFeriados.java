/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_feriados", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfFeriados.findAll", query = "SELECT c FROM ConfFeriados c"),
    @NamedQuery(name = "ConfFeriados.findById", query = "SELECT c FROM ConfFeriados c WHERE c.id = :id"),
    @NamedQuery(name = "ConfFeriados.findByDescripcion", query = "SELECT c FROM ConfFeriados c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ConfFeriados.findByFecha", query = "SELECT c FROM ConfFeriados c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConfFeriados.findByUserIngreso", query = "SELECT c FROM ConfFeriados c WHERE c.userIngreso = :userIngreso"),
    @NamedQuery(name = "ConfFeriados.findByFechaIngreso", query = "SELECT c FROM ConfFeriados c WHERE c.fechaIngreso = :fechaIngreso")})
public class ConfFeriados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "user_ingreso")
    private BigInteger userIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    public ConfFeriados() {
    }

    public ConfFeriados(Long id) {
        this.id = id;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(BigInteger userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfFeriados)) {
            return false;
        }
        ConfFeriados other = (ConfFeriados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfFeriados[ id=" + id + " ]";
    }
    
}
