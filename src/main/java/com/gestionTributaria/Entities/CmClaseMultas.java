/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cm_clase_multas", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CmClaseMultas.findAll", query = "SELECT c FROM CmClaseMultas c"),
    @NamedQuery(name = "CmClaseMultas.findById", query = "SELECT c FROM CmClaseMultas c WHERE c.id = :id"),
    @NamedQuery(name = "CmClaseMultas.findByDescripcion", query = "SELECT c FROM CmClaseMultas c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CmClaseMultas.findByFechaIngreso", query = "SELECT c FROM CmClaseMultas c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CmClaseMultas.findByObservacion", query = "SELECT c FROM CmClaseMultas c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "CmClaseMultas.findByValorDesde", query = "SELECT c FROM CmClaseMultas c WHERE c.valorDesde = :valorDesde"),
    @NamedQuery(name = "CmClaseMultas.findByValorHasta", query = "SELECT c FROM CmClaseMultas c WHERE c.valorHasta = :valorHasta"),
    @NamedQuery(name = "CmClaseMultas.findByUsuarioIngreso", query = "SELECT c FROM CmClaseMultas c WHERE c.usuarioIngreso = :usuarioIngreso")})
public class CmClaseMultas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_desde")
    private BigDecimal valorDesde;
    @Column(name = "valor_hasta")
    private BigDecimal valorHasta;
    @Size(max = 25)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "claseMulta", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CmTipoMultas> cmTipoMultasList;

    public CmClaseMultas() {
    }

    public CmClaseMultas(Long id) {
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getValorDesde() {
        return valorDesde;
    }

    public void setValorDesde(BigDecimal valorDesde) {
        this.valorDesde = valorDesde;
    }

    public BigDecimal getValorHasta() {
        return valorHasta;
    }

    public void setValorHasta(BigDecimal valorHasta) {
        this.valorHasta = valorHasta;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<CmTipoMultas> getCmTipoMultasList() {
        return cmTipoMultasList;
    }

    public void setCmTipoMultasList(List<CmTipoMultas> cmTipoMultasList) {
        this.cmTipoMultasList = cmTipoMultasList;
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
        if (!(object instanceof CmClaseMultas)) {
            return false;
        }
        CmClaseMultas other = (CmClaseMultas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CmClaseMultas[ id=" + id + " ]";
    }
    
}
