/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.entities;

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
@Table(name = "tramites_usuarios", schema = "procesos")
@NamedQueries({
    @NamedQuery(name = "TramitesUsuarios.findAll", query = "SELECT t FROM TramitesUsuarios t"),
    @NamedQuery(name = "TramitesUsuarios.findById", query = "SELECT t FROM TramitesUsuarios t WHERE t.id = :id"),
    @NamedQuery(name = "TramitesUsuarios.findByEstado", query = "SELECT t FROM TramitesUsuarios t WHERE t.estado = :estado"),
    @NamedQuery(name = "TramitesUsuarios.findByTipoTramite", query = "SELECT t FROM TramitesUsuarios t WHERE t.estado = true AND t.tipoTramite = ?1 ORDER BY t.orden ASC"),
    @NamedQuery(name = "TramitesUsuarios.findByUsersResponsable", query = "SELECT t FROM TramitesUsuarios t WHERE t.usersResponsable = :usersResponsable"),
    @NamedQuery(name = "TramitesUsuarios.findByUsuarioCreacion", query = "SELECT t FROM TramitesUsuarios t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "TramitesUsuarios.findByFechaCreacion", query = "SELECT t FROM TramitesUsuarios t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TramitesUsuarios.findByUsuarioModificacion", query = "SELECT t FROM TramitesUsuarios t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "TramitesUsuarios.findByFechaModificacion", query = "SELECT t FROM TramitesUsuarios t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "TramitesUsuarios.findByNextUsers", query = "SELECT t FROM TramitesUsuarios t WHERE t.nextUsers = :nextUsers"),
    @NamedQuery(name = "TramitesUsuarios.findByVariosUsuarios", query = "SELECT t FROM TramitesUsuarios t WHERE t.variosUsuarios = :variosUsuarios"),
    @NamedQuery(name = "TramitesUsuarios.findByValorCondicion", query = "SELECT t FROM TramitesUsuarios t WHERE t.valorCondicion = :valorCondicion"),
    @NamedQuery(name = "TramitesUsuarios.findByCondicion", query = "SELECT t FROM TramitesUsuarios t WHERE t.condicion = :condicion"),
    @NamedQuery(name = "TramitesUsuarios.findByOrden", query = "SELECT t FROM TramitesUsuarios t WHERE t.orden = :orden")})
public class TramitesUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "users_responsable")
    private String usersResponsable;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "next_users")
    private String nextUsers;
    @Column(name = "varios_usuarios")
    private Boolean variosUsuarios;
    @Column(name = "valor_condicion")
    private Integer valorCondicion;
    @Column(name = "condicion")
    private Boolean condicion;
    @Column(name = "orden")
    private Integer orden;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;
    @Column(name = "url")
    private String url;
    @Column(name = "task_key")
    private String taskKey;

    public TramitesUsuarios() {
        this.estado = true;
        this.condicion = false;
        this.variosUsuarios = false;
        this.valorCondicion = 0;
    }

    public TramitesUsuarios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsersResponsable() {
        return usersResponsable;
    }

    public void setUsersResponsable(String usersResponsable) {
        this.usersResponsable = usersResponsable;
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

    public String getNextUsers() {
        return nextUsers;
    }

    public void setNextUsers(String nextUsers) {
        this.nextUsers = nextUsers;
    }

    public Boolean getVariosUsuarios() {
        return variosUsuarios;
    }

    public void setVariosUsuarios(Boolean variosUsuarios) {
        this.variosUsuarios = variosUsuarios;
    }

    public Integer getValorCondicion() {
        return valorCondicion;
    }

    public void setValorCondicion(Integer valorCondicion) {
        this.valorCondicion = valorCondicion;
    }

    public Boolean getCondicion() {
        return condicion;
    }

    public void setCondicion(Boolean condicion) {
        this.condicion = condicion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
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
        if (!(object instanceof TramitesUsuarios)) {
            return false;
        }
        TramitesUsuarios other = (TramitesUsuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.procesos.entities.TramitesUsuarios[ id=" + id + " ]";
    }

}
