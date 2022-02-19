/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.persistence.criteria.Fetch;
import javax.validation.constraints.NotNull;
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
@Table(name = "fina_ren_tipo_entidad_bancaria", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenTipoEntidadBancaria.findAll", query = "SELECT f FROM FinaRenTipoEntidadBancaria f"),
    @NamedQuery(name = "FinaRenTipoEntidadBancaria.findById", query = "SELECT f FROM FinaRenTipoEntidadBancaria f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenTipoEntidadBancaria.findByDescripcion", query = "SELECT f FROM FinaRenTipoEntidadBancaria f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenTipoEntidadBancaria.findByFechaIngreso", query = "SELECT f FROM FinaRenTipoEntidadBancaria f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FinaRenTipoEntidadBancaria.findByEstado", query = "SELECT f FROM FinaRenTipoEntidadBancaria f WHERE f.estado = :estado")})
public class FinaRenTipoEntidadBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @OneToMany(mappedBy = "tipo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenEntidadBancaria> finaRenEntidadBancariaList;

    public FinaRenTipoEntidadBancaria() {
    }

    public FinaRenTipoEntidadBancaria(Long id) {
        this.id = id;
    }

    public FinaRenTipoEntidadBancaria(Long id, Date fechaIngreso, boolean estado) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    
    public List<FinaRenEntidadBancaria> getFinaRenEntidadBancariaList() {
        return finaRenEntidadBancariaList;
    }

    public void setFinaRenEntidadBancariaList(List<FinaRenEntidadBancaria> finaRenEntidadBancariaList) {
        this.finaRenEntidadBancariaList = finaRenEntidadBancariaList;
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
        if (!(object instanceof FinaRenTipoEntidadBancaria)) {
            return false;
        }
        FinaRenTipoEntidadBancaria other = (FinaRenTipoEntidadBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenTipoEntidadBancaria[ id=" + id + " ]";
    }
    
}
