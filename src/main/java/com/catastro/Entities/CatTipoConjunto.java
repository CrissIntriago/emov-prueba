/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.gestionTributaria.Entities.CatCiudadela;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Collection;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_tipo_conjunto", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatTipoConjunto.findAll", query = "SELECT c FROM CatTipoConjunto c"),
    @NamedQuery(name = "CatTipoConjunto.findById", query = "SELECT c FROM CatTipoConjunto c WHERE c.id = :id"),
    @NamedQuery(name = "CatTipoConjunto.findByNombre", query = "SELECT c FROM CatTipoConjunto c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatTipoConjunto.findByEstado", query = "SELECT c FROM CatTipoConjunto c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatTipoConjunto.findByOrden", query = "SELECT c FROM CatTipoConjunto c WHERE c.orden = :orden")})
public class CatTipoConjunto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 80)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "orden")
    private Integer orden;
    @OneToMany(mappedBy = "codTipoConjunto", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private Collection<CatCiudadela> catCiudadelaCollection;

    public CatTipoConjunto() {
    }

    public CatTipoConjunto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Collection<CatCiudadela> getCatCiudadelaCollection() {
        return catCiudadelaCollection;
    }

    public void setCatCiudadelaCollection(Collection<CatCiudadela> catCiudadelaCollection) {
        this.catCiudadelaCollection = catCiudadelaCollection;
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
        if (!(object instanceof CatTipoConjunto)) {
            return false;
        }
        CatTipoConjunto other = (CatTipoConjunto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatTipoConjunto[ id=" + id + " ]";
    }

}
