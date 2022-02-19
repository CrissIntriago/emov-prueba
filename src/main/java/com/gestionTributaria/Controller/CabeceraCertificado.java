/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import java.io.Serializable;
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
 * @author Administrator
 */
@Entity
@Table(name = "cabecera_certificado", schema = "reportes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CabeceraCertificado.findAll", query = "SELECT c FROM CabeceraCertificado c"),
    @NamedQuery(name = "CabeceraCertificado.findById", query = "SELECT c FROM CabeceraCertificado c WHERE c.id = :id"),
    @NamedQuery(name = "CabeceraCertificado.findByFecha", query = "SELECT c FROM CabeceraCertificado c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CabeceraCertificado.findByNombreCompleto1", query = "SELECT c FROM CabeceraCertificado c WHERE c.nombreCompleto1 = :nombreCompleto1"),
    @NamedQuery(name = "CabeceraCertificado.findByTipoLiquidacion", query = "SELECT c FROM CabeceraCertificado c WHERE c.tipoLiquidacion = :tipoLiquidacion")})
public class CabeceraCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 50)
    @Column(name = "nombre_completo1")
    private String nombreCompleto1;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoLiquidacion;

    public CabeceraCertificado() {
    }

    public CabeceraCertificado(Long id) {
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

    public String getNombreCompleto1() {
        return nombreCompleto1;
    }

    public void setNombreCompleto1(String nombreCompleto1) {
        this.nombreCompleto1 = nombreCompleto1;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
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
        if (!(object instanceof CabeceraCertificado)) {
            return false;
        }
        CabeceraCertificado other = (CabeceraCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Controller.CabeceraCertificado[ id=" + id + " ]";
    }

}
