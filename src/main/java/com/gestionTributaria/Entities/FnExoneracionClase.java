/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_exoneracion_clase", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnExoneracionClase.findAll", query = "SELECT f FROM FnExoneracionClase f"),
    @NamedQuery(name = "FnExoneracionClase.findById", query = "SELECT f FROM FnExoneracionClase f WHERE f.id = :id"),
    @NamedQuery(name = "FnExoneracionClase.findByIdAnterior", query = "SELECT f FROM FnExoneracionClase f WHERE f.idAnterior = :idAnterior"),
    @NamedQuery(name = "FnExoneracionClase.findByDescripcion", query = "SELECT f FROM FnExoneracionClase f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FnExoneracionClase.findByEstado", query = "SELECT f FROM FnExoneracionClase f WHERE f.estado = :estado"),
    @NamedQuery(name = "FnExoneracionClase.findByFechaIngreso", query = "SELECT f FROM FnExoneracionClase f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FnExoneracionClase.findByUsuarioCreacion", query = "SELECT f FROM FnExoneracionClase f WHERE f.usuarioCreacion = :usuarioCreacion")})
public class FnExoneracionClase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_anterior")
    private BigInteger idAnterior;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 20)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @OneToMany(mappedBy = "exoneracionClase", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnExoneracionTipo> fnExoneracionTipoList;

    public FnExoneracionClase() {
    }

    public FnExoneracionClase(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getIdAnterior() {
        return idAnterior;
    }

    public void setIdAnterior(BigInteger idAnterior) {
        this.idAnterior = idAnterior;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    
    public List<FnExoneracionTipo> getFnExoneracionTipoList() {
        return fnExoneracionTipoList;
    }

    public void setFnExoneracionTipoList(List<FnExoneracionTipo> fnExoneracionTipoList) {
        this.fnExoneracionTipoList = fnExoneracionTipoList;
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
        if (!(object instanceof FnExoneracionClase)) {
            return false;
        }
        FnExoneracionClase other = (FnExoneracionClase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnExoneracionClase[ id=" + id + " ]";
    }
    
}
