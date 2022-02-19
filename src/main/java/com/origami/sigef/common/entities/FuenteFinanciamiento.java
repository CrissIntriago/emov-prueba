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
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "fuente_financiamiento", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FuenteFinanciamiento.findAll", query = "SELECT f FROM FuenteFinanciamiento f"),
    @NamedQuery(name = "FuenteFinanciamiento.findEstadoValido", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.estado=?1 AND f.periodo=?2"),
    @NamedQuery(name = "FuenteFinanciamiento.findById", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.id = :id"),
    @NamedQuery(name = "FuenteFinanciamiento.findByNombre", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.nombre = :nombre"),
    @NamedQuery(name = "FuenteFinanciamiento.findByPeriodo", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.periodo = :periodo"),
    @NamedQuery(name = "FuenteFinanciamiento.findByDescripcion", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FuenteFinanciamiento.findByFechaVigencia", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "FuenteFinanciamiento.findByFechaCaducidad", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.fechaCaducidad = :fechaCaducidad"),
    @NamedQuery(name = "FuenteFinanciamiento.findByFechaCreacion", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "FuenteFinanciamiento.findByUsuarioCreacion", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "FuenteFinanciamiento.findByFechaModificacion", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "FuenteFinanciamiento.findByUsuarioModifica", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "FuenteFinanciamiento.findByEstado", query = "SELECT f FROM FuenteFinanciamiento f WHERE f.estado = ?1")})
public class FuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "tipo_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoFuente;
    @OneToMany(mappedBy = "fuente")
    private List<CatalogoPresupuesto> catalogosPresupuesto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fuenteApA")
    private List<PartidasDistributivoAnexo> ListaPartidaDisAnexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fuenteApR")
    private List<ValoresRoles> ListaFuenteApRTipoRol;

    public FuenteFinanciamiento() {
        this.estado = Boolean.TRUE;
    }

    public FuenteFinanciamiento(Long id) {
        this.id = id;
    }

    public FuenteFinanciamiento(Long id, String nombre, short periodo, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.periodo = periodo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.estado = estado;
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

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(CatalogoItem tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public List<CatalogoPresupuesto> getCatalogosPresupuesto() {
        return catalogosPresupuesto;
    }

    public void setCatalogosPresupuesto(List<CatalogoPresupuesto> catalogosPresupuesto) {
        this.catalogosPresupuesto = catalogosPresupuesto;
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
        if (!(object instanceof FuenteFinanciamiento)) {
            return false;
        }
        FuenteFinanciamiento other = (FuenteFinanciamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject5.FuenteFinanciamiento[ id=" + id + " ]";
    }

}
