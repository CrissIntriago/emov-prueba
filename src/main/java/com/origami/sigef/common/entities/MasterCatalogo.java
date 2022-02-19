/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "master_catalogo")
@NamedQueries({
    @NamedQuery(name = "MasterCatalogo.findAll", query = "SELECT m FROM MasterCatalogo m"),
    @NamedQuery(name = "MasterCatalogo.findById", query = "SELECT m FROM MasterCatalogo m WHERE m.id = :id"),
    @NamedQuery(name = "MasterCatalogo.findByCatalogoAndTipo", query = "SELECT m FROM MasterCatalogo m JOIN m.tipo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2 ORDER BY m.anio ASC"),
    @NamedQuery(name = "MasterCatalogo.findByAnio", query = "SELECT m FROM MasterCatalogo m WHERE m.anio = ?1"),
    @NamedQuery(name = "MasterCatalogo.findByNombre", query = "SELECT m FROM MasterCatalogo m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "MasterCatalogo.findByDescripcion", query = "SELECT m FROM MasterCatalogo m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "MasterCatalogo.findByCatalogoAndTipo2", query = "SELECT m FROM MasterCatalogo m JOIN m.tipo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2 AND m.anio= ?3 ORDER BY m.anio DESC"),
    @NamedQuery(name = "MasterCatalogo.findByMaxPeriodo", query = "SELECT m FROM MasterCatalogo m JOIN m.tipo t WHERE t.codigo = ?1 and m.anio >= ?2 ORDER BY m.anio")})
public class MasterCatalogo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodoCatalogo")
    private List<CatalogoProformaPresupuesto> catalogosProformasPresupuesto;

    public MasterCatalogo() {

    }

    public MasterCatalogo(Long id) {
        this.id = id;
    }

    public MasterCatalogo(Long id, short anio) {
        this.id = id;
        this.anio = anio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
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

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public List<CatalogoProformaPresupuesto> getCatalogosProformasPresupuesto() {
        return catalogosProformasPresupuesto;
    }

    public void setCatalogosProformasPresupuesto(List<CatalogoProformaPresupuesto> catalogosProformasPresupuesto) {
        this.catalogosProformasPresupuesto = catalogosProformasPresupuesto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
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
        if (!(object instanceof MasterCatalogo)) {
            return false;
        }
        MasterCatalogo other = (MasterCatalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.MasterCatalogo[ id=" + id + " ]";
    }

}
