/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "fn_resolucion_tipo", schema = "sgm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnResolucionTipo.findAll", query = "SELECT f FROM FnResolucionTipo f"),
    @NamedQuery(name = "FnResolucionTipo.findByDescripcion", query = "SELECT f FROM FnResolucionTipo f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FnResolucionTipo.findByAbreviatura", query = "SELECT f FROM FnResolucionTipo f WHERE f.abreviatura = :abreviatura"),
    @NamedQuery(name = "FnResolucionTipo.findById", query = "SELECT f FROM FnResolucionTipo f WHERE f.id = :id")})
public class FnResolucionTipo implements Serializable {
    

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "codigo")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    @Size(max = 2147483647)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    public FnResolucionTipo() {
    }

    public FnResolucionTipo(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof FnResolucionTipo)) {
            return false;
        }
        FnResolucionTipo other = (FnResolucionTipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Controller.FnResolucionTipo[ id=" + id + " ]";
    }
    
}
