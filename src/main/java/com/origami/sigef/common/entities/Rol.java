/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "rol", schema = "auth", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre"})})
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findById", query = "SELECT r FROM Rol r WHERE r.id = :id"),
    @NamedQuery(name = "Rol.findByNombre", query = "SELECT r FROM Rol r WHERE r.nombre = ?1"),
    @NamedQuery(name = "Rol.findByNombreDistinctId", query = "SELECT r FROM Rol r WHERE r.nombre = ?1 AND r.id <> ?2"),
    @NamedQuery(name = "Rol.findByEstado", query = "SELECT r FROM Rol r WHERE r.estado = :estado"),
    @NamedQuery(name = "Rol.findByIsDirector", query = "SELECT r FROM Rol r WHERE r.isDirector = :isDirector"),
    @NamedQuery(name = "Rol.findByEstadOrderNombre", query = "SELECT r FROM Rol r WHERE estado = true ORDER BY nombre ASC"),
    @NamedQuery(name = "Rol.findByUnidadAdministrativa", query = "SELECT r FROM Rol r WHERE r.unidadAdministrativa = ?1")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;
    @Basic(optional = false)
    @Column(nullable = false)
    private boolean estado;
    @Column(name = "is_director")
    private Boolean isDirector;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rol", fetch = FetchType.LAZY)
    @GsonExcludeField
    private List<MenuRol> menuRolList;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem categoria;
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<UsuarioRol> usuarioRolesList;

    public Rol() {
    }

    public Rol(Long id) {
        this.id = id;
    }

    public Rol(Long id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Boolean getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(Boolean isDirector) {
        this.isDirector = isDirector;
    }

    public List<MenuRol> getMenuRolList() {
        return menuRolList;
    }

    public void setMenuRolList(List<MenuRol> menuRolList) {
        this.menuRolList = menuRolList;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public CatalogoItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CatalogoItem categoria) {
        this.categoria = categoria;
    }

    public List<UsuarioRol> getUsuarioRolesList() {
        return usuarioRolesList;
    }

    public void setUsuarioRolesList(List<UsuarioRol> usuarioRolesList) {
        this.usuarioRolesList = usuarioRolesList;
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
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rol{" + "id=" + id + ", nombre=" + nombre + ", menuRolList=" + menuRolList + '}';
    }

}
