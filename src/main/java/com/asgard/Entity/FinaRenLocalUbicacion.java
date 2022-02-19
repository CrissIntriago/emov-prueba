/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "fina_ren_local_ubicacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenLocalUbicacion.findAll", query = "SELECT f FROM FinaRenLocalUbicacion f"),
    @NamedQuery(name = "FinaRenLocalUbicacion.findById", query = "SELECT f FROM FinaRenLocalUbicacion f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenLocalUbicacion.findByCiudadela", query = "SELECT f FROM FinaRenLocalUbicacion f WHERE f.ciudadela = :ciudadela"),
    @NamedQuery(name = "FinaRenLocalUbicacion.findByDescripcion", query = "SELECT f FROM FinaRenLocalUbicacion f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenLocalUbicacion.findByEstado", query = "SELECT f FROM FinaRenLocalUbicacion f WHERE f.estado = :estado")})
public class FinaRenLocalUbicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "ciudadela")
    private BigInteger ciudadela;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "ubicacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLocalComercial> finaRenLocalComercialList;

    public FinaRenLocalUbicacion() {
    }

    public FinaRenLocalUbicacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(BigInteger ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    
    public List<FinaRenLocalComercial> getFinaRenLocalComercialList() {
        return finaRenLocalComercialList;
    }

    public void setFinaRenLocalComercialList(List<FinaRenLocalComercial> finaRenLocalComercialList) {
        this.finaRenLocalComercialList = finaRenLocalComercialList;
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
        if (!(object instanceof FinaRenLocalUbicacion)) {
            return false;
        }
        FinaRenLocalUbicacion other = (FinaRenLocalUbicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenLocalUbicacion[ id=" + id + " ]";
    }
    
}
