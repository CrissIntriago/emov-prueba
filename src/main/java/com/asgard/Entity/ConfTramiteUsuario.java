/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "conf_tramite_usuario", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfTramiteUsuario.findAll", query = "SELECT c FROM ConfTramiteUsuario c"),
    @NamedQuery(name = "ConfTramiteUsuario.findById", query = "SELECT c FROM ConfTramiteUsuario c WHERE c.id = :id"),
    @NamedQuery(name = "ConfTramiteUsuario.findByTipoTramite", query = "SELECT c FROM ConfTramiteUsuario c WHERE c.tipoTramite = :tipoTramite"),
    @NamedQuery(name = "ConfTramiteUsuario.findByTarea", query = "SELECT c FROM ConfTramiteUsuario c WHERE c.tarea = :tarea"),
    @NamedQuery(name = "ConfTramiteUsuario.findByZona", query = "SELECT c FROM ConfTramiteUsuario c WHERE c.zona = :zona"),
    @NamedQuery(name = "ConfTramiteUsuario.findByEstado", query = "SELECT c FROM ConfTramiteUsuario c WHERE c.estado = :estado")})
public class ConfTramiteUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo_tramite")
    private BigInteger tipoTramite;
    @Size(max = 2147483647)
    @Column(name = "tarea")
    private String tarea;
    @Size(max = 2147483647)
    @Column(name = "zona")
    private String zona;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamento servicio;
    @JoinColumn(name = "usuario_tareas", referencedColumnName = "id")
    @ManyToOne
    private ConfTarUsuarioTareas usuarioTareas;

    public ConfTramiteUsuario() {
    }

    public ConfTramiteUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(BigInteger tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public AppServiciosDepartamento getServicio() {
        return servicio;
    }

    public void setServicio(AppServiciosDepartamento servicio) {
        this.servicio = servicio;
    }

    public ConfTarUsuarioTareas getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(ConfTarUsuarioTareas usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
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
        if (!(object instanceof ConfTramiteUsuario)) {
            return false;
        }
        ConfTramiteUsuario other = (ConfTramiteUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.ConfTramiteUsuario[ id=" + id + " ]";
    }
    
}
