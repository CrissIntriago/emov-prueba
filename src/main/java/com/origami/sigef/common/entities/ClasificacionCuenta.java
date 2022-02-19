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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "clasificacion_cuenta")
@NamedQueries({
    @NamedQuery(name = "ClasificacionCuenta.findAll", query = "SELECT c FROM ClasificacionCuenta c"),
    @NamedQuery(name = "ClasificacionCuenta.findById", query = "SELECT c FROM ClasificacionCuenta c WHERE c.id = :id"),
    @NamedQuery(name = "ClasificacionCuenta.findByCodigo", query = "SELECT c FROM ClasificacionCuenta c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ClasificacionCuenta.findByDescripcion", query = "SELECT c FROM ClasificacionCuenta c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ClasificacionCuenta.findByOrden", query = "SELECT c FROM ClasificacionCuenta c WHERE c.orden = :orden")})
public class ClasificacionCuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private short orden;
    @JoinColumn(name = "tipo_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoCuenta;

    public ClasificacionCuenta() {
    }

    public ClasificacionCuenta(Long id) {
        this.id = id;
    }

    public ClasificacionCuenta(Long id, String codigo, String descripcion, short orden) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.orden = orden;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
        this.orden = orden;
    }

    public CatalogoItem getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(CatalogoItem tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
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
        if (!(object instanceof ClasificacionCuenta)) {
            return false;
        }
        ClasificacionCuenta other = (ClasificacionCuenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ClasificacionCuenta[ id=" + id + " ]";
    }

}
