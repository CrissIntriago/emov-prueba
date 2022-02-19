/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.Utils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import org.primefaces.barcelona.domain.Document;

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "menu_rol", schema = "auth")
@NamedQueries({
    @NamedQuery(name = "MenuRol.findAll", query = "SELECT m FROM MenuRol m"),
    @NamedQuery(name = "MenuRol.findAllMenu", query = "SELECT m FROM MenuRol m WHERE m.menu = ?1"),
    @NamedQuery(name = "MenuRol.findAllNameRolMenu", query = "SELECT r.nombre FROM MenuRol m INNER JOIN m.rol r WHERE m.menu = ?1"),
    @NamedQuery(name = "MenuRol.findById", query = "SELECT m FROM MenuRol m WHERE m.id = :id"),
    @NamedQuery(name = "MenuRol.findByOpciones", query = "SELECT m FROM MenuRol m WHERE m.opciones = :opciones")})
public class MenuRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(name = "opciones")
    @Basic(fetch = FetchType.LAZY)
    private String opciones;
    @JoinColumn(name = "menu", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Menu menu;
    @JoinColumn(name = "rol", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rol rol;

    @Transient
    private List<String> permisosSeleccionados;
    @Transient
    private List<Document> permisos;

    public MenuRol() {
    }

    public MenuRol(Long id) {
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<String> getPermisosSeleccionados() {
        return permisosSeleccionados;
    }

    public void setPermisosSeleccionados(List<String> permisosSeleccionados) {
        this.permisosSeleccionados = permisosSeleccionados;
    }

    public List<Document> getPermisos() {
        if (permisos == null) {
            try {
                List<Document> acciones = (List<Document>) new JsonUtil().fromJson(this.getOpciones(), Document[].class);
                permisos = new ArrayList<>();
                if (Utils.isNotEmpty(acciones)) {
                    permisos.addAll(acciones);
                }
            } catch (IOException ex) {
                Logger.getLogger(MenuRol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return permisos;
    }

    public void setPermisos(List<Document> permisos) {
        this.permisos = permisos;
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
        if (!(object instanceof MenuRol)) {
            return false;
        }
        MenuRol other = (MenuRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MenuRol{id=").append(id);
        sb.append(", opciones=").append(opciones);
        sb.append(", menu=").append(menu);
        sb.append('}');
        return sb.toString();
    }

}
