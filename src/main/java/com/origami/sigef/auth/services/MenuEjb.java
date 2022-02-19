/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.services;

import com.origami.sigef.common.entities.Menu;
import com.origami.sigef.common.entities.MenuRol;
import com.origami.sigef.common.entities.Modulo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Edwin Castro
 */
@Stateless
@javax.enterprise.context.Dependent
public class MenuEjb extends AbstractService<Menu> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(MenuEjb.class.getName());

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MenuEjb() {
        super(Menu.class);
    }

    @Override
    public EntityManager getEntityManager() {
//        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
//        return em;
        return em;
    }

    public Menu save(Menu rol) {
        if (rol != null) {
            if (rol.getId() == null) {
                return this.create(rol);
            } else {
                this.edit(rol);
                return rol;
            }
        }
        return null;
    }

    public Menu save(Menu rol, List<MenuRol> roles) {
        if (rol != null) {
            if (rol.getId() == null) {
                rol = this.create(rol);
            } else {
                rol.setMenuRolList(null);
                this.edit(rol);
            }
            if (Utils.isNotEmpty(roles)) {
                for (MenuRol r : roles) {
                    if (r.getId() == null) {
                        r.setMenu(rol);
                        getEntityManager().persist(r);
                    } else {
                        getEntityManager().merge(r);
                    }
                }
            }
        }
        return rol;
    }

    public TreeNode createNode(Modulo mod) {
        Menu root = new Menu();
        root.setId(mod.getId());
        root.setDescripcion(mod.getNombre());
        root.setPrettyId("-");
        root.setHrefUrl("#");
        TreeNode nodeMenus = new DefaultTreeNode("Módulo " + mod.getNombre(), root, null);
        if (Utils.isNotEmpty(mod.getMenuList())) {
            for (Menu menu : mod.getMenuList()) {
                createItemsMenu(nodeMenus, menu);
            }
        }
        mod.setNodeMenus(nodeMenus);
        return nodeMenus;
    }

    private void createItemsMenu(TreeNode node, Menu menu) {
        if (menu != null) {
            TreeNode menus = new DefaultTreeNode(menu, node);
            menus.setSelectable(true);
            List<Menu> items = menu.getItemsMenu();
            if (Utils.isEmpty(items)) {
                items = this.findByMenuPadre(menu);
            }
            System.out.println("Menu " + menu.getDescripcion() + " Items " + items);
            if (Utils.isNotEmpty(items)) {
                for (Menu item : items) {
                    createItemsMenu(menus, item);
                }
            }
        }
    }

    public List<Menu> findByMenuPadre(Menu menu) {
        Menu m = new Menu();
        m.setMenuPadre(menu);
        return findByExample(m);
//        return findByNamedQuery("Menu.findByMenuPadreAll", menu.getId());
    }

    public boolean removeMenuRol(MenuRol menuRol) {
        try {
            MenuRol mr = getEntityManager().find(MenuRol.class, menuRol.getId());
//            getEntityManager().detach(mr);
            getEntityManager().remove(mr);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar menu rol", e);
        }
        return false;
    }

    public List<MenuRol> getRoles(Menu menu) {
        try {
            TypedQuery<MenuRol> createNamedQuery = getEntityManager().createNamedQuery("MenuRol.findAllMenu", MenuRol.class);
            createNamedQuery.setParameter(1, menu);
            return createNamedQuery.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar menu rol", e);
        }
        return null;
    }

    public List<String> getNameRoles(Menu menu) {
        try {
            Query createNamedQuery = getEntityManager().createNamedQuery("MenuRol.findAllNameRolMenu");
            createNamedQuery.setParameter(1, menu);
            return createNamedQuery.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eliminar menu rol", e);
            return null;
        }
    }

}
