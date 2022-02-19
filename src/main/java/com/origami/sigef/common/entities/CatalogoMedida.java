/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "catalogo_medida", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "CatalogoMedida.findAll", query = "SELECT c FROM CatalogoMedida c"),
    @NamedQuery(name = "CatalogoMedida.findById", query = "SELECT c FROM CatalogoMedida c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoMedida.findByDescripcion", query = "SELECT c FROM CatalogoMedida c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoMedida.findByEstado", query = "SELECT c FROM CatalogoMedida c WHERE c.estado = ?1")})

public class CatalogoMedida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @Column(nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(length = 255)
    private String descripcion;
    @JoinColumn(name = "tipo_medida", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoMedida;
//    @Basic(optional = false)
//    @NotNull
    @Column(nullable = false)
    private boolean estado;

    public CatalogoMedida() {
    }

    public CatalogoMedida(Long id) {
        this.id = id;
    }

    public CatalogoMedida(Long id, boolean estado) {
        this.id = id;
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

    public CatalogoItem getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(CatalogoItem tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof CatalogoMedida)) {
            return false;
        }
        CatalogoMedida other = (CatalogoMedida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CatalogoMedida[ id=" + id + " ]";
    }
}
