/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_secuencia_num_comprobante", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenSecuenciaNumComprobante.findAll", query = "SELECT r FROM RenSecuenciaNumComprobante r"),
    @NamedQuery(name = "RenSecuenciaNumComprobante.findById", query = "SELECT r FROM RenSecuenciaNumComprobante r WHERE r.id = :id"),
    @NamedQuery(name = "RenSecuenciaNumComprobante.findByAnio", query = "SELECT r FROM RenSecuenciaNumComprobante r WHERE r.anio = :anio"),
    @NamedQuery(name = "RenSecuenciaNumComprobante.findByNumComprobante", query = "SELECT r FROM RenSecuenciaNumComprobante r WHERE r.numComprobante = :numComprobante"),
    @NamedQuery(name = "RenSecuenciaNumComprobante.findByUsuario", query = "SELECT r FROM RenSecuenciaNumComprobante r WHERE r.usuario = :usuario")})
public class RenSecuenciaNumComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private BigInteger anio;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Size(max = 40)
    @Column(name = "usuario")
    private String usuario;

    public RenSecuenciaNumComprobante() {
    }

    public RenSecuenciaNumComprobante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof RenSecuenciaNumComprobante)) {
            return false;
        }
        RenSecuenciaNumComprobante other = (RenSecuenciaNumComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenSecuenciaNumComprobante[ id=" + id + " ]";
    }
    
}
