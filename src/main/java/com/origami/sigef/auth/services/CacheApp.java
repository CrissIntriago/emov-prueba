/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Menu;
import com.origami.sigef.common.entities.MenuRol;
import com.origami.sigef.common.entities.Modulo;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.VariableRecaudacion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.Constantes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Inicializa los datos en la cache cuando inicia la aplicacion.
 *
 * @author ANGEL NAVARRO
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class CacheApp {

    private static final Logger LOG = Logger.getLogger(CacheApp.class.getName());
    private ConcurrentMap<String, Object> cache;
    private final String cacheName = "MODULOS";
    @Inject
    private ModuloEjb moduloEjb;
    @Inject
    private MenuEjb menuEjb;
    @Inject
    private UserSession userSession;
    @Inject
    private ValoresService valService;

    /**
     * Inicializa los modulos del menu en cache.
     */
    @PostConstruct
    public void initialize() {
        System.out.println("///////loadApplicationVariables()");
        loadApplicationVariables();
        Map<String, String> orders = new HashMap<>();
        orders.put("orden", "ASC");
        List<Modulo> modulos = moduloEjb.findAllOrder(orders);
        if (Utils.isNotEmpty(modulos)) {
            if (cache == null) {
                cache = new ConcurrentHashMap<>();
            }
//            List<Modulo> filter = modulos.stream().filter((m) -> m.getVisualizar().equals(true)).collect(Collectors.toList());
            List<Modulo> filter = modulos;
            for (Modulo mod : filter) {
                List<Menu> items = mod.getMenuList();
                if (Utils.isEmpty(mod.getMenuList())) {
                    items = menuEjb.findByNamedQuery("Menu.findByMenuPadre", mod);
                }
                if (Utils.isNotEmpty(items)) {
                    for (Menu menu : items) {
                        Utils.isEmpty(menu.getMenuRolList());
                        this.loadItemMenu(menu);
                    }
                }
                if (items != null) {
                    mod.setMenuList(items);
                }
            }
            cache.put(cacheName, Utils.clone(filter));
        }
    }

    private void loadItemMenu(Menu menu) {
        if (menu == null) {
            return;
        }
        List<Menu> items = menu.getItemsMenu();
        if (Utils.isEmpty(items)) {
            items = menuEjb.findByMenuPadre(menu);
        }
        if (Utils.isNotEmpty(items)) {
            for (Menu item : items) {
                Utils.isEmpty(item.getMenuRolList());
                this.loadItemMenu(item);
            }
        }
        if (items != null) {
            menu.setItemsMenu(items);
        }
    }

    /**
     * Devuelve los datos que estan en cache por nombre de la misma.
     *
     * @param <T> Tipo de dato
     * @param nameCache Nombre de la cache a buscar.
     * @return Objecto en cache si existe, caso contrario null.
     */
    public <T> T getDataCache(String nameCache) {
        if (nameCache == null) {
            return (T) cache.get(this.cacheName);
        } else {
            return (T) cache.get(nameCache);
        }
    }

    /**
     * Agrega un nuevo objecto a la cache.
     *
     * @param nameCache Nombre de la cache.
     * @param data Objecto a agregar a la cache.
     */
    public void addCache(String nameCache, Object data) {
        cache.put(nameCache, data);
    }

    public void clear(String nameCache) {
        if (nameCache == null) {
            cache.remove(this.cacheName);
            initialize();
        } else {
            cache.remove(nameCache);
        }
    }

    public int hasUrlRole(String url) {
        if (url == null) {
            return 1;
        }
        try {
            List<Modulo> modulos = (List<Modulo>) cache.get(cacheName);
            Menu result = null;
            for (Modulo mod : modulos) {
                Menu m = filterUrl(mod.getMenuList(), url);
                if (m != null) {

                    result = m;
                    break;
                }
            }
            if (result != null) {
                if (result.isEsPublico()) {
                    return 0;
                }
                if (Utils.isNotEmpty(result.getMenuRolList())) {
                    for (MenuRol mr : result.getMenuRolList()) {
                        UsuarioRol roles = userSession.getRoles(mr);
                        if (roles != null) {
                            return 0;
                        }
                    }
                }
            } else {
                return 2;
            }
        } catch (Exception e) {
            Logger.getLogger(CacheApp.class.getName()).log(Level.SEVERE, url, e);
            return 3;
        }
        return 1;
    }

    private Menu filterUrl(List<Menu> menuList, String url) {
        if (Utils.isNotEmpty(menuList)) {
            for (Menu m : menuList) {
                if (m != null) {
                    if (Utils.isNotEmpty(m.getItemsMenu())) {
                        Menu mi = filterUrl(m.getItemsMenu(), url);
                        if (mi != null) {
                            return mi;
                        }
                    } else {
                        if (m.getPrettyPattern() != null) {
                            if (m.getPrettyPattern().trim().equals(url.trim())) {
                                return m;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void loadApplicationVariables() {
        try {
            CONFIG.SERVICE_URL = valService.findByCodigo(Constantes.DINARDAP_URL);
            CONFIG.SERVICE_USER = valService.findByCodigo(Constantes.DINARDAP_USER);
            CONFIG.SERVICE_PASS = valService.findByCodigo(Constantes.DINARDAP_PASS);
            CONFIG.FACTURA_AMBIENTE = valService.findAmbienteComprobantesElectronicos();
            VariableRecaudacion.COACTIVA = valService.findByCodigo1("COACTIVA").intValue();
            VariableRecaudacion.MES_DECUENTO_TOPE = valService.findByCodigo1("MES_DECUENTO_TOPE").intValue();
            VariableRecaudacion.MES_RECARGO = valService.findByCodigo1("MES_RECARGO").intValue();
            VariableRecaudacion.RECARGO = valService.findByCodigo1("RECARGO").intValue();
            System.out.println("recargo>>" + VariableRecaudacion.RECARGO);
            String amb = valService.findByCodigo("AMBIENTE_APP");
            System.out.println("///////" + amb + "/////ambiente Comprobantes Electronicos >>>" + CONFIG.FACTURA_AMBIENTE);
            if (amb.equalsIgnoreCase("DEV")) {
                CONFIG.IP_TC_WEB = "/META-INF/wsdl/dev/TramitePortalWS.wsdl";
                CONFIG.IP_TC_WEB1 = "/META-INF/wsdl/dev/IntegracionPortalWS.wsdl";
            } else {
                CONFIG.IP_TC_WEB = "/META-INF/wsdl/TramitePortalWS.wsdl";
                CONFIG.IP_TC_WEB1 = "/META-INF/wsdl/IntegracionPortalWS.wsdl";
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, cacheName, e);
        }
    }
}
