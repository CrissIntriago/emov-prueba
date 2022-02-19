/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
@Table(name = "th_dias_laborados", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThDiasLaborados.findAll", query = "SELECT t FROM ThDiasLaborados t"),
    @NamedQuery(name = "ThDiasLaborados.findById", query = "SELECT t FROM ThDiasLaborados t WHERE t.id = :id"),
    @NamedQuery(name = "ThDiasLaborados.findByServidorData", query = "SELECT t FROM ThDiasLaborados t WHERE t.estado = true AND t.servidor = ?1 AND t.idTipoRol = ?2"),
    @NamedQuery(name = "ThDiasLaborados.findByFechaCreacion", query = "SELECT t FROM ThDiasLaborados t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThDiasLaborados.findByUsuarioCreacion", query = "SELECT t FROM ThDiasLaborados t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThDiasLaborados.findByFechaModificacion", query = "SELECT t FROM ThDiasLaborados t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThDiasLaborados.findByUsuarioModificacion", query = "SELECT t FROM ThDiasLaborados t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThDiasLaborados.findByDiasLaborados", query = "SELECT t FROM ThDiasLaborados t WHERE t.diasLaborados = :diasLaborados"),
    @NamedQuery(name = "ThDiasLaborados.findByServidor", query = "SELECT (COUNT(t)=0) FROM ThDiasLaborados t WHERE t.servidor = ?1 AND t.idTipoRol = ?2 AND t.estado = true"),
    @NamedQuery(name = "ThDiasLaborados.findByTipoRol", query = "SELECT t FROM ThDiasLaborados t INNER JOIN t.servidor s INNER JOIN s.persona p WHERE t.idTipoRol = ?1 AND t.estado = true ORDER BY p.apellido ASC"),
    @NamedQuery(name = "ThDiasLaborados.findByEstado", query = "SELECT t FROM ThDiasLaborados t WHERE t.estado = :estado")})
public class ThDiasLaborados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "dias_laborados")
    private Integer diasLaborados;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "aparecer_rol")
    private Boolean aparecerRol;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;

    public ThDiasLaborados() {
        this.estado = Boolean.TRUE;
        this.aparecerRol = Boolean.TRUE;
    }

    public ThDiasLaborados(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Integer getDiasLaborados() {
        return diasLaborados;
    }

    public void setDiasLaborados(Integer diasLaborados) {
        this.diasLaborados = diasLaborados;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public Boolean getAparecerRol() {
        return aparecerRol;
    }

    public void setAparecerRol(Boolean aparecerRol) {
        this.aparecerRol = aparecerRol;
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
        if (!(object instanceof ThDiasLaborados)) {
            return false;
        }
        ThDiasLaborados other = (ThDiasLaborados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThDiasLaborados[ id=" + id + " ]";
    }

}
