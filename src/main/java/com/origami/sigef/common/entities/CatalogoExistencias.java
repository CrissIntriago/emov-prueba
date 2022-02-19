/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "catalogo_existencias", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoExistencias.findAll", query = "SELECT c FROM CatalogoExistencias c ORDER BY c.idExistencia"),
    @NamedQuery(name = "CatalogoExistencias.findById", query = "SELECT c FROM CatalogoExistencias c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoExistencias.findByIdExistencia", query = "SELECT c FROM CatalogoExistencias c WHERE c.idExistencia = :idExistencia"),
    @NamedQuery(name = "CatalogoExistencias.findByDescripcion", query = "SELECT c FROM CatalogoExistencias c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoExistencias.findByDescripcionLike", query = "SELECT c FROM CatalogoExistencias c WHERE UPPER(c.descripcion) LIKE ?1"),
    @NamedQuery(name = "CatalogoExistencias.findByUnidadMedida", query = "SELECT c FROM CatalogoExistencias c WHERE c.unidadMedida = :unidadMedida"),
    @NamedQuery(name = "CatalogoExistencias.findByCaduca", query = "SELECT c FROM CatalogoExistencias c WHERE c.caduca = :caduca"),
    @NamedQuery(name = "CatalogoExistencias.findByLote", query = "SELECT c FROM CatalogoExistencias c WHERE c.lote = :lote"),
    @NamedQuery(name = "CatalogoExistencias.findByItemPresupuestario", query = "SELECT c FROM CatalogoExistencias c WHERE c.itemPresupuestario = :itemPresupuestario")})
public class CatalogoExistencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "id_existencia")
    private String idExistencia;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "unidad_medida")
    private String unidadMedida;
    @Size(max = 1)
    @Column(name = "caduca")
    private String caduca;
    @Size(max = 1)
    @Column(name = "lote")
    private String lote;
    @Size(max = 2147483647)
    @Column(name = "item_presupuestario")
    private String itemPresupuestario;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Transient
    private Integer fila;

    public CatalogoExistencias() {
        fila = 1;
    }

    public CatalogoExistencias(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdExistencia() {
        return idExistencia;
    }

    public void setIdExistencia(String idExistencia) {
        this.idExistencia = idExistencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getCaduca() {
        return caduca;
    }

    public void setCaduca(String caduca) {
        this.caduca = caduca;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(String itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof CatalogoExistencias)) {
            return false;
        }
        CatalogoExistencias other = (CatalogoExistencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CatalogoExistencias[ id=" + id + " ]";
    }

}
