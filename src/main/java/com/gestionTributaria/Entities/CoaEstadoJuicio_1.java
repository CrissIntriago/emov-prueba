/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "coa_estado_juicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaEstadoJuicio_1.findAll", query = "SELECT c FROM CoaEstadoJuicio_1 c"),
    @NamedQuery(name = "CoaEstadoJuicio_1.findById", query = "SELECT c FROM CoaEstadoJuicio_1 c WHERE c.id = :id"),
    @NamedQuery(name = "CoaEstadoJuicio_1.findByAbreviatura", query = "SELECT c FROM CoaEstadoJuicio_1 c WHERE c.abreviatura = :abreviatura"),
    @NamedQuery(name = "CoaEstadoJuicio_1.findByDescripcion", query = "SELECT c FROM CoaEstadoJuicio_1 c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CoaEstadoJuicio_1.findByEstado", query = "SELECT c FROM CoaEstadoJuicio_1 c WHERE c.estado = :estado"),
    @NamedQuery(name = "CoaEstadoJuicio_1.findBySecuencia", query = "SELECT c FROM CoaEstadoJuicio_1 c WHERE c.secuencia = :secuencia")})
public class CoaEstadoJuicio_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "secuencia")
    private Integer secuencia;
    @OneToMany(mappedBy = "estadoJuicio")
    private List<CoaJuicio> coaJuicioList;

    public CoaEstadoJuicio_1() {
    }

    public CoaEstadoJuicio_1(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    @XmlTransient
    public List<CoaJuicio> getCoaJuicioList() {
        return coaJuicioList;
    }

    public void setCoaJuicioList(List<CoaJuicio> coaJuicioList) {
        this.coaJuicioList = coaJuicioList;
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
        if (!(object instanceof CoaEstadoJuicio_1)) {
            return false;
        }
        CoaEstadoJuicio_1 other = (CoaEstadoJuicio_1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CoaEstadoJuicio_1[ id=" + id + " ]";
    }

}
