/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
@Table(name = "th_tipo_rol", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThTipoRol.findAll", query = "SELECT t FROM ThTipoRol t"),
    @NamedQuery(name = "ThTipoRol.findById", query = "SELECT t FROM ThTipoRol t WHERE t.id = :id"),
    @NamedQuery(name = "ThTipoRol.findByRolGeneralAdicional", query = "SELECT t FROM ThTipoRol t INNER JOIN t.idMes mes INNER JOIN t.idTipo tp WHERE t.estado = true AND t.periodo = ?1 AND t.aprobado = true AND tp.codigo IN ('rol_adicional','rol_general') AND t.verificado = true AND t.contabilizado = false ORDER BY mes.orden ASC"),
    @NamedQuery(name = "ThTipoRol.findByDescripcion", query = "SELECT t FROM ThTipoRol t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "ThTipoRol.findByPeriodo", query = "SELECT t FROM ThTipoRol t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThTipoRol.findByIdMes", query = "SELECT t FROM ThTipoRol t WHERE t.idMes = :idMes"),
    @NamedQuery(name = "ThTipoRol.findByIdTipo", query = "SELECT t FROM ThTipoRol t WHERE t.idTipo = :idTipo"),
    @NamedQuery(name = "ThTipoRol.findByEstado", query = "SELECT t FROM ThTipoRol t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThTipoRol.findByAprobado", query = "SELECT t FROM ThTipoRol t WHERE t.aprobado = :aprobado"),
    @NamedQuery(name = "ThTipoRol.findByUserCreacion", query = "SELECT t FROM ThTipoRol t WHERE t.userCreacion = :userCreacion"),
    @NamedQuery(name = "ThTipoRol.findByFechaCreacion", query = "SELECT t FROM ThTipoRol t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThTipoRol.findByUserModificacion", query = "SELECT t FROM ThTipoRol t WHERE t.userModificacion = :userModificacion"),
    @NamedQuery(name = "ThTipoRol.findByFechaModificacion", query = "SELECT t FROM ThTipoRol t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThTipoRol.findByUserAprobacion", query = "SELECT t FROM ThTipoRol t WHERE t.userAprobacion = :userAprobacion"),
    @NamedQuery(name = "ThTipoRol.findByFechaAprobacion", query = "SELECT t FROM ThTipoRol t WHERE t.fechaAprobacion = :fechaAprobacion"),
    @NamedQuery(name = "ThTipoRol.findByUserRechazo", query = "SELECT t FROM ThTipoRol t WHERE t.userRechazo = :userRechazo"),
    @NamedQuery(name = "ThTipoRol.findByFechaRechazo", query = "SELECT t FROM ThTipoRol t WHERE t.fechaRechazo = :fechaRechazo")})
public class ThTipoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "id_mes", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idMes;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idTipo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @Size(max = 2147483647)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "user_modificacion")
    private String userModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "user_aprobacion")
    private String userAprobacion;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacion;
    @Size(max = 2147483647)
    @Column(name = "user_rechazo")
    private String userRechazo;
    @Column(name = "fecha_rechazo")
    @Temporal(TemporalType.DATE)
    private Date fechaRechazo;
    @Column(name = "verificado")
    private Boolean verificado;
    @Column(name = "contabilizado")
    private Boolean contabilizado;

    public ThTipoRol() {
        this.estado = true;
        this.aprobado = false;
        this.verificado = false;
        this.contabilizado = false;
    }

    public ThTipoRol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public CatalogoItem getIdMes() {
        return idMes;
    }

    public void setIdMes(CatalogoItem idMes) {
        this.idMes = idMes;
    }

    public CatalogoItem getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(CatalogoItem idTipo) {
        this.idTipo = idTipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUserAprobacion() {
        return userAprobacion;
    }

    public void setUserAprobacion(String userAprobacion) {
        this.userAprobacion = userAprobacion;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getUserRechazo() {
        return userRechazo;
    }

    public void setUserRechazo(String userRechazo) {
        this.userRechazo = userRechazo;
    }

    public Date getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
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
        if (!(object instanceof ThTipoRol)) {
            return false;
        }
        ThTipoRol other = (ThTipoRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThTipoRol[ id=" + id + " ]";
    }

}
