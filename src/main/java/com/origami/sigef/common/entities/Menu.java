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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.primefaces.barcelona.domain.Document;

/**
 *
 * @author ANGEL NAVARRO
 */
@Table(name = "menu", schema = "auth")
@Entity
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m"),
    @NamedQuery(name = "Menu.findById", query = "SELECT m FROM Menu m WHERE m.id = :id"),
    @NamedQuery(name = "Menu.findByMenuPadreAll", query = "SELECT m FROM Menu m INNER JOIN m.menuPadre mp WHERE mp.id = ?1 ORDER BY m.orden ASC"),
    @NamedQuery(name = "Menu.findByMenuPadre", query = "SELECT m FROM Menu m WHERE m.menuPadre IS NULL AND m.modulo = ?1 ORDER BY m.orden ASC"),
    @NamedQuery(name = "Menu.findByPrettyIsNotNull", query = "SELECT m FROM Menu m WHERE m.prettyPattern IS NOT NULL AND (m.hrefUrl IS NOT NULL OR m.hrefUrl <> '') ORDER BY m.orden ASC"),
    @NamedQuery(name = "Menu.findByIcono", query = "SELECT m FROM Menu m WHERE m.icono = :icono"),
    @NamedQuery(name = "Menu.findByDescripcion", query = "SELECT m FROM Menu m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Menu.findByOrden", query = "SELECT m FROM Menu m WHERE m.orden = :orden"),
    @NamedQuery(name = "Menu.findByPrettyId", query = "SELECT m FROM Menu m WHERE m.prettyId = :prettyId"),
    @NamedQuery(name = "Menu.findByPrettyPattern", query = "SELECT m FROM Menu m WHERE m.prettyPattern = :prettyPattern")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "href_url", length = 255)
    private String hrefUrl;
    @Size(max = 255)
    @Column(length = 255)
    private String icono;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String descripcion;
    @Basic(optional = false)
    @Column(nullable = false)
    private int orden;
    @Size(max = 2147483647)
    @Column(name = "pretty_id", length = 2147483647)
    private String prettyId;
    @Size(max = 2147483647)
    @Column(name = "pretty_pattern", length = 2147483647)
    private String prettyPattern;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<MenuRol> menuRolList;
    @OneToMany(mappedBy = "menuPadre", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @OrderBy(value = "orden ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Menu> itemsMenu;
    @JoinColumn(name = "menu_padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Menu menuPadre;
    @JoinColumn(name = "modulo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Modulo modulo;
    @Column(name = "es_publico")
    private boolean esPublico;
    @Transient
    private List<Document> permisos;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(Long id, String descripcion, int orden) {
        this.id = id;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHrefUrl() {
        if (hrefUrl != null) {
            if (!hrefUrl.startsWith("/")) {
                hrefUrl = "/" + hrefUrl;
            }
        }
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getPrettyId() {
        if (prettyId == null) {
            prettyId = "#";
        }
        if (prettyId.trim().length() == 0) {
            prettyId = "#";
        }
        return prettyId;
    }

    public void setPrettyId(String prettyId) {
        this.prettyId = prettyId;
    }

    public String getPrettyPattern() {
        return prettyPattern;
    }

    public void setPrettyPattern(String prettyPattern) {
        this.prettyPattern = prettyPattern;
    }

    public List<MenuRol> getMenuRolList() {
        return menuRolList;
    }

    public void setMenuRolList(List<MenuRol> menuRolList) {
        this.menuRolList = menuRolList;
    }

    public List<Menu> getItemsMenu() {
        return itemsMenu;
    }

    public void setItemsMenu(List<Menu> itemsMenu) {
        this.itemsMenu = itemsMenu;
    }

    public Menu getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(Menu menuPadre) {
        this.menuPadre = menuPadre;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public boolean isEsPublico() {
        return esPublico;
    }

    public void setEsPublico(boolean esPublico) {
        this.esPublico = esPublico;
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
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", icono=" + icono + ", descripcion=" + descripcion + ", orden=" + orden + ", prettyId=" + prettyId + ", prettyPattern=" + prettyPattern + '}';
    }

    public List<Document> getPermisos() {
        if (permisos == null) {
            try {
                List<Document> acciones = (List<Document>) new JsonUtil().fromJson(this.getPrettyId(), Document[].class);
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
}
