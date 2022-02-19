/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_tar_usuario_tareas", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfTarUsuarioTareas.findAll", query = "SELECT c FROM ConfTarUsuarioTareas c"),
    @NamedQuery(name = "ConfTarUsuarioTareas.findById", query = "SELECT c FROM ConfTarUsuarioTareas c WHERE c.id = :id"),
    @NamedQuery(name = "ConfTarUsuarioTareas.findByUsuario", query = "SELECT c FROM ConfTarUsuarioTareas c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "ConfTarUsuarioTareas.findByRol", query = "SELECT c FROM ConfTarUsuarioTareas c WHERE c.rol = :rol"),
    @NamedQuery(name = "ConfTarUsuarioTareas.findByEstado", query = "SELECT c FROM ConfTarUsuarioTareas c WHERE c.estado = :estado"),
    @NamedQuery(name = "ConfTarUsuarioTareas.findByTramite", query = "SELECT c FROM ConfTarUsuarioTareas c WHERE c.tramite = :tramite")})
public class ConfTarUsuarioTareas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private long usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private long rol;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "tramite")
    private Boolean tramite;
    @OneToMany(mappedBy = "usuarioTareas", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<ConfTarTareasAsignadas> confTarTareasAsignadasList;
    @OneToMany(mappedBy = "usuarioTareas", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<ConfTramiteUsuario> confTramiteUsuarioList;

    public ConfTarUsuarioTareas() {
    }

    public ConfTarUsuarioTareas(Long id) {
        this.id = id;
    }

    public ConfTarUsuarioTareas(Long id, long usuario, long rol) {
        this.id = id;
        this.usuario = usuario;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUsuario() {
        return usuario;
    }

    public void setUsuario(long usuario) {
        this.usuario = usuario;
    }

    public long getRol() {
        return rol;
    }

    public void setRol(long rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getTramite() {
        return tramite;
    }

    public void setTramite(Boolean tramite) {
        this.tramite = tramite;
    }

    
    public List<ConfTarTareasAsignadas> getConfTarTareasAsignadasList() {
        return confTarTareasAsignadasList;
    }

    public void setConfTarTareasAsignadasList(List<ConfTarTareasAsignadas> confTarTareasAsignadasList) {
        this.confTarTareasAsignadasList = confTarTareasAsignadasList;
    }

    
    public List<ConfTramiteUsuario> getConfTramiteUsuarioList() {
        return confTramiteUsuarioList;
    }

    public void setConfTramiteUsuarioList(List<ConfTramiteUsuario> confTramiteUsuarioList) {
        this.confTramiteUsuarioList = confTramiteUsuarioList;
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
        if (!(object instanceof ConfTarUsuarioTareas)) {
            return false;
        }
        ConfTarUsuarioTareas other = (ConfTarUsuarioTareas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfTarUsuarioTareas[ id=" + id + " ]";
    }
    
}
