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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "secu_secuencia_tramite", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SecuSecuenciaTramite.findAll", query = "SELECT s FROM SecuSecuenciaTramite s"),
    @NamedQuery(name = "SecuSecuenciaTramite.findById", query = "SELECT s FROM SecuSecuenciaTramite s WHERE s.id = :id"),
    @NamedQuery(name = "SecuSecuenciaTramite.findByNumeroTramite", query = "SELECT s FROM SecuSecuenciaTramite s WHERE s.numeroTramite = :numeroTramite"),
    @NamedQuery(name = "SecuSecuenciaTramite.findByFecha", query = "SELECT s FROM SecuSecuenciaTramite s WHERE s.fecha = :fecha")})
public class SecuSecuenciaTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public SecuSecuenciaTramite() {
    }

    public SecuSecuenciaTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        if (!(object instanceof SecuSecuenciaTramite)) {
            return false;
        }
        SecuSecuenciaTramite other = (SecuSecuenciaTramite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.SecuSecuenciaTramite[ id=" + id + " ]";
    }
    
}
