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
@Table(name = "cat_predio_fotos", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioFotos.findAll", query = "SELECT c FROM CatPredioFotos c"),
    @NamedQuery(name = "CatPredioFotos.findById", query = "SELECT c FROM CatPredioFotos c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioFotos.findByNombreArchivo", query = "SELECT c FROM CatPredioFotos c WHERE c.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "CatPredioFotos.findByPredio", query = "SELECT c FROM CatPredioFotos c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioFotos.findByExt", query = "SELECT c FROM CatPredioFotos c WHERE c.ext = :ext")})
public class CatPredioFotos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 500)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "predio")
    private BigInteger predio;
    @Size(max = 10)
    @Column(name = "ext")
    private String ext;

    public CatPredioFotos() {
    }

    public CatPredioFotos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public BigInteger getPredio() {
        return predio;
    }

    public void setPredio(BigInteger predio) {
        this.predio = predio;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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
        if (!(object instanceof CatPredioFotos)) {
            return false;
        }
        CatPredioFotos other = (CatPredioFotos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioFotos[ id=" + id + " ]";
    }

}
