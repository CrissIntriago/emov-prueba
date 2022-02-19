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
import javax.validation.constraints.Size;

/**
 *
 * @author Luis Pozo gonzabay
 */
@Entity
@Table(name = "impresora", schema = "conf")
@NamedQueries({
    @NamedQuery(name = "Impresora.findAll", query = "SELECT i FROM Impresora i"),
    @NamedQuery(name = "Impresora.findById", query = "SELECT i FROM Impresora i WHERE i.id = :id"),
    @NamedQuery(name = "Impresora.findByCodigo", query = "SELECT i FROM Impresora i WHERE i.codigo = :codigo"),
    @NamedQuery(name = "Impresora.findByNombre", query = "SELECT i FROM Impresora i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "Impresora.findByDescripcion", query = "SELECT i FROM Impresora i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Impresora.findByEsDefault", query = "SELECT i FROM Impresora i WHERE i.esDefault = :esDefault")})
public class Impresora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "es_default")
    private Boolean esDefault;

    public Impresora() {
    }

    public Impresora(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsDefault() {
        return esDefault;
    }

    public void setEsDefault(Boolean esDefault) {
        this.esDefault = esDefault;
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
        if (!(object instanceof Impresora)) {
            return false;
        }
        Impresora other = (Impresora) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Impresora[ id=" + id + " ]";
    }

}
