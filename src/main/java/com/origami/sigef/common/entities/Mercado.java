/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.arrendamiento.entities.DetalleMercado;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "mercado", schema = "arriendo")
@NamedQueries({
    @NamedQuery(name = "Mercado.findAll", query = "SELECT m FROM Mercado m"),
    @NamedQuery(name = "Mercado.findById", query = "SELECT m FROM Mercado m WHERE m.id = :id"),
    @NamedQuery(name = "Mercado.findByCategoria", query = "SELECT m FROM Mercado m WHERE m.categoria = :categoria"),
    @NamedQuery(name = "Mercado.findByNombre", query = "SELECT m FROM Mercado m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Mercado.findByUbicacion", query = "SELECT m FROM Mercado m WHERE m.ubicacion = :ubicacion"),
    @NamedQuery(name = "Mercado.findByDescripcion", query = "SELECT m FROM Mercado m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Mercado.findByUsuarioCreacion", query = "SELECT m FROM Mercado m WHERE m.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Mercado.findByUsuarioModificacion", query = "SELECT m FROM Mercado m WHERE m.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Mercado.findByFechaCreacion", query = "SELECT m FROM Mercado m WHERE m.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Mercado.findByFechaModificacion", query = "SELECT m FROM Mercado m WHERE m.fechaModificacion = :fechaModificacion")})
public class Mercado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIME)
    private Date fechaModificacion;

    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem categoria;

    @OneToMany(mappedBy = "mercado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleMercado> detalleMercado;

    public Mercado() {
        this.estado = Boolean.TRUE;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CatalogoItem categoria) {
        this.categoria = categoria;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<DetalleMercado> getDetalleMercado() {
        return detalleMercado;
    }

    public void setDetalleMercado(List<DetalleMercado> detalleMercado) {
        this.detalleMercado = detalleMercado;
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
        if (!(object instanceof Mercado)) {
            return false;
        }
        Mercado other = (Mercado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Mercado{" + "id=" + id + '}';
    }

}
