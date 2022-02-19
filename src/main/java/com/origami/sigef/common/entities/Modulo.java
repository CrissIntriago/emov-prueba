/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ANGEL NAVARRO
 */
@Table(name = "modulo", schema = "auth")
@Entity
@NamedQueries({
    @NamedQuery(name = "Modulo.findAll", query = "SELECT m FROM Modulo m"),
    @NamedQuery(name = "Modulo.findById", query = "SELECT m FROM Modulo m WHERE m.id = :id"),
    @NamedQuery(name = "Modulo.findByDescripcion", query = "SELECT m FROM Modulo m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Modulo.findByIcono", query = "SELECT m FROM Modulo m WHERE m.icono = :icono"),
    @NamedQuery(name = "Modulo.findByNombre", query = "SELECT m FROM Modulo m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Modulo.findByWidgetVar", query = "SELECT m FROM Modulo m WHERE m.widgetVar = :widgetVar"),
    @NamedQuery(name = "Modulo.findByOrden", query = "SELECT m FROM Modulo m WHERE m.orden = :orden")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Modulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(length = 255)
    private String descripcion;
    @Size(max = 255)
    @Column(length = 255)
    private String icono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "widget_var", length = 2147483647)
    private String widgetVar;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int orden;
    @OneToMany(mappedBy = "modulo", fetch = FetchType.LAZY)
    @OrderBy(value = "orden ASC")
    @Where(clause = "menu_padre IS NULL AND descripcion IS NOT NULL")
    private List<Menu> menuList;
    @Where(clause = "pretty_pattern IS NULL OR pretty_pattern = ''")
    @OneToMany(mappedBy = "modulo", fetch = FetchType.LAZY)
    @OrderBy(value = "orden ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Menu> subMenuList;
    @Column(name = "visualizar")
    private Boolean visualizar = true;

    @Transient
    private MenuModel model;
    @Transient
    private TreeNode nodeMenus;

    public Modulo() {
    }

    public Modulo(Long id) {
        this.id = id;
    }

    public Modulo(Long id, String nombre, int orden) {
        this.id = id;
        this.nombre = nombre;
        this.orden = orden;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getWidgetVar() {
        return widgetVar;
    }

    public void setWidgetVar(String widgetVar) {
        this.widgetVar = widgetVar;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public TreeNode getNodeMenus() {
        if (nodeMenus == null) {
            createNode();
        }
        return nodeMenus;
    }

    public void setNodeMenus(TreeNode nodeMenus) {
        this.nodeMenus = nodeMenus;
    }

    public Boolean getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
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
        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modulo{" + "id=" + id + ", icono=" + icono + ", nombre=" + nombre + ", widgetVar=" + widgetVar + ", orden=" + orden + '}';
    }

    public void createNode() {
        Menu root = new Menu();
        root.setId(this.getId());
        root.setDescripcion(getNombre());
        root.setPrettyId("-");
        root.setHrefUrl("#");
        nodeMenus = new DefaultTreeNode("Modulo " + getNombre(), root, null);
        if (Utils.isNotEmpty(this.getMenuList())) {
//            TreeNode node = new DefaultTreeNode(root, nodeMenus);
            for (Menu menu : this.getMenuList()) {
                createItemsMenu(nodeMenus, menu);
            }
        }
    }

    private TreeNode createItemsMenu(TreeNode node, Menu menu) {
        if (menu != null) {
            TreeNode menus = new DefaultTreeNode(menu, node);
            menus.setRowKey("id");
            menus.setSelectable(true);
            if (Utils.isNotEmpty(menu.getItemsMenu())) {
                for (Menu item : menu.getItemsMenu()) {
                    createItemsMenu(menus, item);
                }
            }
            return menus;
        }
        return null;
    }

    public MenuModel getMenuModel() {
        if (model != null) {
            return model;
        } else {
            model = new DefaultMenuModel();
            if (Utils.isNotEmpty(this.getMenuList())) {
                for (Menu menu : this.getMenuList()) {
                    try {
                        if (menu != null) {
                            if (Utils.isNotEmpty(menu.getItemsMenu())) {
                                DefaultSubMenu submenu = new DefaultSubMenu(menu.getDescripcion());
                                submenu.setIcon(menu.getIcono());
                                createItemMenu(submenu, menu);
                                model.addElement(submenu);
                            } else {
                                DefaultMenuItem item = new DefaultMenuItem(menu.getDescripcion());
                                item.setIcon(menu.getIcono()); // Icono del menu
                                item.setUrl(getUrl(menu.getPrettyPattern())); // Configuracion de Pretty
                                model.addElement(item);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
            return model;
        }
    }

    private void createItemMenu(DefaultSubMenu submenu, Menu menu) {
        if (menu == null) {
            return;
        }
        if (Utils.isNotEmpty(menu.getItemsMenu())) {
            for (Menu item : menu.getItemsMenu()) {
                if (item != null) {
                    if (Utils.isEmpty(item.getItemsMenu())) {
                        DefaultMenuItem itemm = new DefaultMenuItem(item.getDescripcion());
                        itemm.setIcon(item.getIcono());
                        itemm.setIconPos("l");
                        itemm.setUrl(getUrl(item.getPrettyPattern()));
                        submenu.addElement(itemm);
                    } else {
                        DefaultSubMenu submenu1 = new DefaultSubMenu(item.getDescripcion());
                        submenu1.setIcon(item.getIcono());
                        createItemMenu(submenu1, item);
                        submenu.addElement(submenu1);
                    }
                }
            }
        }
    }

    private String getUrl(String url) {
        if (url == null) {
            return "#";
        }
        if (url.startsWith("http")) {
            return url;
        } else {
            return JsfUtil.getHostContextUrl() + (url.startsWith("/") ? url.substring(1).trim() : url.trim());
        }
    }

    public List<Menu> getSubMenuList() {
        return subMenuList;
    }

    public void setSubMenuList(List<Menu> subMenuList) {
        this.subMenuList = subMenuList;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
