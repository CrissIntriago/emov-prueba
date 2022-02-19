/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.services;

import com.origami.sigef.common.entities.Menu;
import com.origami.sigef.common.entities.Modulo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Angel Navarro
 */
@Stateless
@javax.enterprise.context.Dependent
public class ModuloEjb extends AbstractService<Modulo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ModuloEjb.class.getName());

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private CacheApp cache;
    @Inject
    private MenuEjb menuEjb;
    @Inject
    private UserSession userSession;

    public ModuloEjb() {
        super(Modulo.class);
    }

    @Override
    public EntityManager getEntityManager() {
//        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
//        return em;
        return em;
    }

    public Modulo save(Modulo rol) {
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

    public TreeNode loadMenu() {
        List<Modulo> modulos = cache.getDataCache(null);
        // ADD FILTER MENU USERS
        return new DefaultTreeNode(modulos, null);

    }

    public List<Modulo> loadModulos() {
        List<Modulo> modulos = cache.getDataCache(null);
        // ADD FILTER MENU USERS
        return filterModuloRol(modulos);

    }

    public void clearReloadMenu() {
        cache.clear(null);
    }

    private List<Modulo> filterModuloRol(List<Modulo> modulos) {
        try {
            List<Modulo> userAccess = new ArrayList<>(modulos.size());
            for (Modulo modulo : modulos) {
                Modulo temp = Utils.clone(modulo);
                if (temp.getVisualizar()) {
                    List<Menu> menuList;
                    // si es Administrador le damos todas las opciones
                    if (userSession.hasRole("admin")) {
                        menuList = modulo.getMenuList();
                    } else {
                        menuList = filterMenuRol(modulo.getMenuList());
                    }
                    if (Utils.isNotEmpty(menuList)) {
                        temp.setMenuList(Utils.clone(menuList));
                        userAccess.add(temp);
                    }
                }
            }
            return userAccess;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Filter Modulo rol", e);
            return null;
        }
    }

    /**
     * Verifica cada uno de los menu si tiene el rol del usuario logeado.
     *
     * @param menus Listado de menu que se chequeara
     * @return Listado filtrado
     */
    private List<Menu> filterMenuRol(List<Menu> menus) {
        try {
            if (Utils.isEmpty(menus)) {
                return null;
            }
            List<Menu> userAccessMenu = new ArrayList<>(menus.size());
            for (Menu menuItem : menus) {
                if (menuItem != null) {
                    if (menuItem.isEsPublico()) {
                        userAccessMenu.add(menuItem);
                    } else {
                        Menu menu = Utils.clone(menuItem);
                        List<String> nameRoles = null;
                        // Chequeamos que existan roles y extraemos los nombres
                        if (Utils.isNotEmpty(menu.getMenuRolList())) {
                            nameRoles = menu.getMenuRolList().stream().map(e -> e.getRol().getNombre()).collect(Collectors.toList());
                        } else {
                            nameRoles = menuEjb.getNameRoles(menuItem);
                        }
                        // Si hay menu hijos filtramos si contine el rol
                        if (Utils.isEmpty(menu.getItemsMenu())) {
                            if (Utils.isEmpty(nameRoles)) {
                                continue;
                            }
                            // Si es un menu cualquiera verificamos que tenga el rol
                            if (userSession.hasAnyRoles(nameRoles)) {
                                userAccessMenu.add(menu);
                            }
                        } else {
                            // enviamos verificar si los hijos tambien tienen el rol
                            menu.setItemsMenu(this.filterMenuRol(menu.getItemsMenu()));
                            if (Utils.isNotEmpty(menu.getItemsMenu())) {
                                userAccessMenu.add(menu);
                            }
                        }
                    }
                }
            }
            return userAccessMenu;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Filter Menu rol", e);
            return null;
        }
    }

    public CacheApp getCache() {
        return this.cache;
    }
}
