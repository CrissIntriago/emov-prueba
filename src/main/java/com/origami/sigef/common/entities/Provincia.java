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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "provincia")
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"),
    @NamedQuery(name = "Provincia.findById", query = "SELECT p FROM Provincia p WHERE p.id = :id"),
    @NamedQuery(name = "Provincia.findByCodProvincia", query = "SELECT p FROM Provincia p WHERE p.codProvincia = :codProvincia"),
    @NamedQuery(name = "Provincia.findByCodigo", query = "SELECT p FROM Provincia p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "Provincia.findByProvincia", query = "SELECT p FROM Provincia p WHERE UPPER(p.provincia) LIKE ?1"),
    @NamedQuery(name = "Provincia.findByHabilitado", query = "SELECT p FROM Provincia p WHERE p.habilitado = :habilitado"),
    @NamedQuery(name = "Provincia.findByDescripcion", query = "SELECT p FROM Provincia p WHERE p.descripcion = :descripcion")})
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "cod_provincia")
    private short codProvincia;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "provincia")
    private String provincia;
    @Basic(optional = false)
    @Column(name = "habilitado")
    private boolean habilitado;
    @Column(name = "descripcion")
    private String descripcion;

    public Provincia() {
    }

    public Provincia(Long id) {
        this.id = id;
    }

    public Provincia(Long id, short codProvincia, String codigo, String provincia, boolean habilitado) {
        this.id = id;
        this.codProvincia = codProvincia;
        this.codigo = codigo;
        this.provincia = provincia;
        this.habilitado = habilitado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(short codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Provincia[ id=" + id + " ]";
    }

}
