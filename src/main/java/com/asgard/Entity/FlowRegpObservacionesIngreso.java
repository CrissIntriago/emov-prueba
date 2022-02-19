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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_regp_observaciones_ingreso", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findAll", query = "SELECT f FROM FlowRegpObservacionesIngreso f"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findById", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.id = :id"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByNumero", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.numero = :numero"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByEnte", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.ente = :ente"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByUserIngreso", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.userIngreso = :userIngreso"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByFechaIngreso", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByUserEdicion", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.userEdicion = :userEdicion"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByFechaEdicion", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.fechaEdicion = :fechaEdicion"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByObservacion1", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.observacion1 = :observacion1"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByObservacion2", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.observacion2 = :observacion2"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByObservacion3", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.observacion3 = :observacion3"),
    @NamedQuery(name = "FlowRegpObservacionesIngreso.findByEstado", query = "SELECT f FROM FlowRegpObservacionesIngreso f WHERE f.estado = :estado")})
public class FlowRegpObservacionesIngreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private BigInteger numero;
    @Column(name = "ente")
    private BigInteger ente;
    @Column(name = "user_ingreso")
    private BigInteger userIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "user_edicion")
    private BigInteger userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Size(max = 2147483647)
    @Column(name = "observacion1")
    private String observacion1;
    @Size(max = 2147483647)
    @Column(name = "observacion2")
    private String observacion2;
    @Size(max = 2147483647)
    @Column(name = "observacion3")
    private String observacion3;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FlowRegpLiquidacion liquidacion;

    public FlowRegpObservacionesIngreso() {
    }

    public FlowRegpObservacionesIngreso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    public BigInteger getEnte() {
        return ente;
    }

    public void setEnte(BigInteger ente) {
        this.ente = ente;
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

    public BigInteger getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(BigInteger userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public String getObservacion1() {
        return observacion1;
    }

    public void setObservacion1(String observacion1) {
        this.observacion1 = observacion1;
    }

    public String getObservacion2() {
        return observacion2;
    }

    public void setObservacion2(String observacion2) {
        this.observacion2 = observacion2;
    }

    public String getObservacion3() {
        return observacion3;
    }

    public void setObservacion3(String observacion3) {
        this.observacion3 = observacion3;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public FlowRegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FlowRegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        if (!(object instanceof FlowRegpObservacionesIngreso)) {
            return false;
        }
        FlowRegpObservacionesIngreso other = (FlowRegpObservacionesIngreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowRegpObservacionesIngreso[ id=" + id + " ]";
    }
    
}
