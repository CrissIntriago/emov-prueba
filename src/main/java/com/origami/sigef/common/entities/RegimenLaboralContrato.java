/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "regimen_laboral_contrato")
@NamedQueries({
    @NamedQuery(name = "RegimenLaboralContrato.findAll", query = "SELECT r FROM RegimenLaboralContrato r"),
    @NamedQuery(name = "RegimenLaboralContrato.findById", query = "SELECT r FROM RegimenLaboralContrato r WHERE r.id = :id"),
    @NamedQuery(name = "RegimenLaboralContrato.findByCodigo", query = "SELECT r FROM RegimenLaboralContrato r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "RegimenLaboralContrato.findByNombre", query = "SELECT r FROM RegimenLaboralContrato r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "RegimenLaboralContrato.findByDescripcion", query = "SELECT r FROM RegimenLaboralContrato r WHERE r.descripcion = :descripcion")})
public class RegimenLaboralContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "padre")
    private List<RegimenLaboralContrato> hijos;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RegimenLaboralContrato padre;

    public RegimenLaboralContrato() {
    }

    public RegimenLaboralContrato(Long id) {
        this.id = id;
    }

    public RegimenLaboralContrato(Long id, String codigo, String nombre, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public List<RegimenLaboralContrato> getHijos() {
        return hijos;
    }

    public void setHijos(List<RegimenLaboralContrato> hijos) {
        this.hijos = hijos;
    }

    public RegimenLaboralContrato getPadre() {
        return padre;
    }

    public void setPadre(RegimenLaboralContrato padre) {
        this.padre = padre;
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
        if (!(object instanceof RegimenLaboralContrato)) {
            return false;
        }
        RegimenLaboralContrato other = (RegimenLaboralContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RegimenLaboralContrato[ id=" + id + " ]";
    }

}
