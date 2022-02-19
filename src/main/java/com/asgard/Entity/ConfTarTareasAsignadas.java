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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_tar_tareas_asignadas", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfTarTareasAsignadas.findAll", query = "SELECT c FROM ConfTarTareasAsignadas c"),
    @NamedQuery(name = "ConfTarTareasAsignadas.findById", query = "SELECT c FROM ConfTarTareasAsignadas c WHERE c.id = :id"),
    @NamedQuery(name = "ConfTarTareasAsignadas.findByPeso", query = "SELECT c FROM ConfTarTareasAsignadas c WHERE c.peso = :peso"),
    @NamedQuery(name = "ConfTarTareasAsignadas.findByCantidad", query = "SELECT c FROM ConfTarTareasAsignadas c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "ConfTarTareasAsignadas.findByFecha", query = "SELECT c FROM ConfTarTareasAsignadas c WHERE c.fecha = :fecha")})
public class ConfTarTareasAsignadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "peso")
    private BigInteger peso;
    @Column(name = "cantidad")
    private BigInteger cantidad;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "usuario_tareas", referencedColumnName = "id")
    @ManyToOne
    private ConfTarUsuarioTareas usuarioTareas;

    public ConfTarTareasAsignadas() {
    }

    public ConfTarTareasAsignadas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ConfTarUsuarioTareas getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(ConfTarUsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
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
        if (!(object instanceof ConfTarTareasAsignadas)) {
            return false;
        }
        ConfTarTareasAsignadas other = (ConfTarTareasAsignadas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfTarTareasAsignadas[ id=" + id + " ]";
    }
    
}
