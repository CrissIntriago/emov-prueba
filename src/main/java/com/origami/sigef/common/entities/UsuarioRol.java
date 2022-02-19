/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.audit.ShowName;
import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import javax.persistence.*;

/**
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "usuario_rol", schema = "auth")
@NamedQueries({
    @NamedQuery(name = "UsuarioRol.findAll", query = "SELECT ur FROM UsuarioRol ur"),
    @NamedQuery(name = "UsuarioRol.findById", query = "SELECT m FROM UsuarioRol m WHERE m.id = :id"),
    @NamedQuery(name = "UsuarioRol.findByOpciones", query = "SELECT m FROM UsuarioRol m WHERE m.opciones = :opciones")})
public class UsuarioRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @ShowName(name = "Caduca clave")
    private Long id;
    @Column(length = 500)
    private String opciones;
    @JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Usuarios usuario;
    @JoinColumn(name = "rol", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @ShowName(name = "Rol", isReferences = true)
    private Rol rol;
    @Column(name = "asistente")
    private Boolean asistente;
    @Column(name = "director_aprobador")
    private Boolean directorAprobador;
    @Column(name = "jefe_aprobador")
    private Boolean jefeAprobador;
    @Column(name = "liquidador")
    private Boolean liquidador;

    @Transient
    private Boolean eliminado;

    public UsuarioRol() {
    }

    public UsuarioRol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Boolean getAsistente() {
        return asistente;
    }

    public void setAsistente(Boolean asistente) {
        this.asistente = asistente;
    }

    public Boolean getDirectorAprobador() {
        return directorAprobador;
    }

    public void setDirectorAprobador(Boolean directorAprobador) {
        this.directorAprobador = directorAprobador;
    }

    public Boolean getJefeAprobador() {
        return jefeAprobador;
    }

    public void setJefeAprobador(Boolean jefeAprobador) {
        this.jefeAprobador = jefeAprobador;
    }

    public Boolean getLiquidador() {
        return liquidador;
    }

    public void setLiquidador(Boolean liquidador) {
        this.liquidador = liquidador;
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
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsuarioRol{" + "id=" + id + ", opciones=" + opciones + ", rol=" + rol + '}';
    }

}
