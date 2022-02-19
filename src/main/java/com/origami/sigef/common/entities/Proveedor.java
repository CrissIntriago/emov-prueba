/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "proveedor")
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findById", query = "SELECT p FROM Proveedor p WHERE p.id = :id"),
    @NamedQuery(name = "Proveedor.findByIdentificacion", query = "SELECT p FROM Proveedor p INNER JOIN p.cliente cl WHERE p.estado = true AND cl.tipoIdentificacion = ?1 AND cl.identificacion = ?2"),
    @NamedQuery(name = "Proveedor.findByPersonaId", query = "SELECT p FROM Proveedor p JOIN p.cliente c WHERE c.identificacion = ?1"),
    @NamedQuery(name = "Proveedor.findByPersonaIdentificacion", query = "SELECT p FROM Proveedor p JOIN p.cliente c WHERE c.identificacion = ?1 AND c.estado = true"),
    @NamedQuery(name = "Proveedor.findByPersonaIdentificacionRUC", query = "SELECT p FROM Proveedor p JOIN p.cliente c INNER JOIN c.tipoIdentificacion tp WHERE c.identificacion = ?1 AND c.estado = true AND tp.codigo='RUC'"),
    @NamedQuery(name = "Proveedor.findByObservacion", query = "SELECT p FROM Proveedor p WHERE p.observacion = :observacion"),
    @NamedQuery(name = "Proveedor.findByEstado", query = "SELECT p FROM Proveedor p WHERE p.estado = :estado"),
    @NamedQuery(name = "Proveedor.findByTipoEntidad", query = "SELECT p FROM Proveedor p WHERE p.tipoEntidad = :tipoEntidad"),
    @NamedQuery(name = "Proveedor.findByRepresentanteLegal", query = "SELECT p FROM Proveedor p WHERE p.representanteLegal = :representanteLegal"),
    @NamedQuery(name = "Proveedor.findByUsuarioCreacion", query = "SELECT p FROM Proveedor p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Proveedor.findByFechaCreacion", query = "SELECT p FROM Proveedor p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Proveedor.findByUsuarioModificacion", query = "SELECT p FROM Proveedor p WHERE p.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Proveedor.findByFechaModificacion", query = "SELECT p FROM Proveedor p WHERE p.fechaModificacion = :fechaModificacion")})
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "tipo_entidad")
    private String tipoEntidad;
    @Column(name = "representante_legal")
    private Boolean representanteLegal;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "contacto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Cliente contacto;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente cliente;
    @Column(name = "actividad_comercial")
    private String actividadComercial;

    public Proveedor() {
        this.estado = Boolean.TRUE;
        this.representanteLegal = Boolean.FALSE;
    }

    public Proveedor(Long id) {
        this.id = id;
    }

    public Proveedor(Cliente cliente) {
        this.cliente = cliente;
    }

    public Proveedor(Long id, boolean estado, String usuarioCreacion, Date fechaCreacion) {
        this.id = id;
        this.estado = estado;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public Boolean getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Boolean representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Cliente getContacto() {
        return contacto;
    }

    public void setContacto(Cliente contacto) {
        this.contacto = contacto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getActividadComercial() {
        return actividadComercial;
    }

    public void setActividadComercial(String actividadComercial) {
        this.actividadComercial = actividadComercial;
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
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Proveedor[ id=" + id + " ]";
    }

}
