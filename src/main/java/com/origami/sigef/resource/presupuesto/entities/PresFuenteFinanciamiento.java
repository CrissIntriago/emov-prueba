/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.entities;

import com.origami.sigef.common.entities.CatalogoItem;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "pres_fuente_financiamiento", schema = "presupuesto")
@NamedQueries({
    @NamedQuery(name = "PresFuenteFinanciamiento.findAll", query = "SELECT p FROM PresFuenteFinanciamiento p"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findById", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.id = :id"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByTipoFuente", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.tipoFuente = :tipoFuente"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByNombre", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByFechaCreacion", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByUsuarioCreacion", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByFechaModificacion", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByUsuarioModifica", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByEstado", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.estado = :estado"),
    @NamedQuery(name = "PresFuenteFinanciamiento.findByCodFuente", query = "SELECT p FROM PresFuenteFinanciamiento p WHERE p.codFuente = :codFuente")})
public class PresFuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tipo_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoFuente;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "cod_fuente")
    private String codFuente;

    public PresFuenteFinanciamiento() {
        this.estado = Boolean.TRUE;
    }

    public PresFuenteFinanciamiento(Long id) {
        this.id = id;
    }

    public PresFuenteFinanciamiento(Long id, CatalogoItem tipoFuente, String nombre, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica, boolean estado, String codFuente) {
        this.id = id;
        this.tipoFuente = tipoFuente;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
        this.estado = estado;
        this.codFuente = codFuente;
    }

    public PresFuenteFinanciamiento(Long idfuente, String codfuente, String nomfuente) {
        this.id = idfuente;
        this.codFuente = codfuente;
        this.nombre = nomfuente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(CatalogoItem tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodFuente() {
        return codFuente;
    }

    public void setCodFuente(String codFuente) {
        this.codFuente = codFuente;
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
        if (!(object instanceof PresFuenteFinanciamiento)) {
            return false;
        }
        PresFuenteFinanciamiento other = (PresFuenteFinanciamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento[ id=" + id + " ]";
    }

}
