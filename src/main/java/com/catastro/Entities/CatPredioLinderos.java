/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_linderos", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioLinderos.findAll", query = "SELECT c FROM CatPredioLinderos c"),
    @NamedQuery(name = "CatPredioLinderos.findById", query = "SELECT c FROM CatPredioLinderos c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioLinderos.findByPredio", query = "SELECT c FROM CatPredioLinderos c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioLinderos.findByPredioColindante", query = "SELECT c FROM CatPredioLinderos c WHERE c.predioColindante = :predioColindante"),
    @NamedQuery(name = "CatPredioLinderos.findByColindante", query = "SELECT c FROM CatPredioLinderos c WHERE c.colindante = :colindante"),
    @NamedQuery(name = "CatPredioLinderos.findByOrientacion", query = "SELECT c FROM CatPredioLinderos c WHERE c.orientacion = :orientacion"),
    @NamedQuery(name = "CatPredioLinderos.findByEstado", query = "SELECT c FROM CatPredioLinderos c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatPredioLinderos.findByEstadoRespaldo", query = "SELECT c FROM CatPredioLinderos c WHERE c.estadoRespaldo = :estadoRespaldo"),
    @NamedQuery(name = "CatPredioLinderos.findByLongitud", query = "SELECT c FROM CatPredioLinderos c WHERE c.longitud = :longitud")})
public class CatPredioLinderos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "predio")
    private BigInteger predio;
    @Column(name = "predio_colindante")
    private BigInteger predioColindante;
    @Size(max = 2147483647)
    @Column(name = "colindante")
    private String colindante;
    @Column(name = "orientacion")
    private BigInteger orientacion;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "estado_respaldo")
    private String estadoRespaldo;
    @Column(name = "longitud")
    private BigInteger longitud;

    public CatPredioLinderos() {
    }

    public CatPredioLinderos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPredio() {
        return predio;
    }

    public void setPredio(BigInteger predio) {
        this.predio = predio;
    }

    public BigInteger getPredioColindante() {
        return predioColindante;
    }

    public void setPredioColindante(BigInteger predioColindante) {
        this.predioColindante = predioColindante;
    }

    public String getColindante() {
        return colindante;
    }

    public void setColindante(String colindante) {
        this.colindante = colindante;
    }

    public BigInteger getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(BigInteger orientacion) {
        this.orientacion = orientacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoRespaldo() {
        return estadoRespaldo;
    }

    public void setEstadoRespaldo(String estadoRespaldo) {
        this.estadoRespaldo = estadoRespaldo;
    }

    public BigInteger getLongitud() {
        return longitud;
    }

    public void setLongitud(BigInteger longitud) {
        this.longitud = longitud;
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
        if (!(object instanceof CatPredioLinderos)) {
            return false;
        }
        CatPredioLinderos other = (CatPredioLinderos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioLinderos[ id=" + id + " ]";
    }

}
