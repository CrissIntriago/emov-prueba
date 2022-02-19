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
@Table(name = "bita_generacion_docs", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BitaGeneracionDocs.findAll", query = "SELECT b FROM BitaGeneracionDocs b"),
    @NamedQuery(name = "BitaGeneracionDocs.findById", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.id = :id"),
    @NamedQuery(name = "BitaGeneracionDocs.findByUsuario", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.usuario = :usuario"),
    @NamedQuery(name = "BitaGeneracionDocs.findByFechaGeneracion", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.fechaGeneracion = :fechaGeneracion"),
    @NamedQuery(name = "BitaGeneracionDocs.findByAclLogin", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.aclLogin = :aclLogin"),
    @NamedQuery(name = "BitaGeneracionDocs.findByMovimiento", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.movimiento = :movimiento"),
    @NamedQuery(name = "BitaGeneracionDocs.findByFicha", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.ficha = :ficha"),
    @NamedQuery(name = "BitaGeneracionDocs.findByCertificado", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.certificado = :certificado"),
    @NamedQuery(name = "BitaGeneracionDocs.findByObservacion", query = "SELECT b FROM BitaGeneracionDocs b WHERE b.observacion = :observacion")})
public class BitaGeneracionDocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    @Column(name = "acl_login")
    private BigInteger aclLogin;
    @Column(name = "movimiento")
    private BigInteger movimiento;
    @Column(name = "ficha")
    private BigInteger ficha;
    @Column(name = "certificado")
    private BigInteger certificado;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;

    public BitaGeneracionDocs() {
    }

    public BitaGeneracionDocs(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public BigInteger getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(BigInteger aclLogin) {
        this.aclLogin = aclLogin;
    }

    public BigInteger getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(BigInteger movimiento) {
        this.movimiento = movimiento;
    }

    public BigInteger getFicha() {
        return ficha;
    }

    public void setFicha(BigInteger ficha) {
        this.ficha = ficha;
    }

    public BigInteger getCertificado() {
        return certificado;
    }

    public void setCertificado(BigInteger certificado) {
        this.certificado = certificado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
        if (!(object instanceof BitaGeneracionDocs)) {
            return false;
        }
        BitaGeneracionDocs other = (BitaGeneracionDocs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.BitaGeneracionDocs[ id=" + id + " ]";
    }
    
}
