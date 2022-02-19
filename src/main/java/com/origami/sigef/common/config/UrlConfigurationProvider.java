/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.origami.sigef.auth.services.MenuEjb;
import com.origami.sigef.common.entities.Menu;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

/**
 *
 * @author ANGEL NAVARRO
 */
@RewriteConfiguration
public class UrlConfigurationProvider extends HttpConfigurationProvider {

    private static final Logger LOG = Logger.getLogger(UrlConfigurationProvider.class.getName());
    @Inject
    private MenuEjb menuEjb;

    @Override
    public Configuration getConfiguration(ServletContext context) {
        if (CONFIG.URL_APP == null) {
            CONFIG.URL_APP = context.getContextPath();
        }
        ConfigurationBuilder begin = ConfigurationBuilder.begin();
        try {
//            List<Menu> menus = menuEjb.findByNamedQuery("Menu.findByPrettyIsNotNull", (Object[]) null);
            List<Menu> menus = menuEjb.findAll();
            if (menus != null) {
                menus.forEach((menu) -> {
                    if (menu.getPrettyPattern() != null && menu.getPrettyPattern().trim().length() > 0) {
                        if (menu.getHrefUrl() != null && menu.getHrefUrl().trim().length() > 0) {
                            begin.addRule(Join.path(menu.getPrettyPattern()).to(menu.getHrefUrl().trim()).withInboundCorrection()).withId(menu.getId().toString());
                        }
                    }
                });
            }
            begin.addRule(Join.path("/error/404").to("404.xhtml"));
            begin.addRule(Join.path("/error/expired-page").to("expired.xhtml"));
            begin.addRule(Join.path("/error/page").to("error.xhtml"));
            begin.addRule(Join.path("/error/general").to("empty.xhtml"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return begin;
    }

    @Override
    public int priority() {
        return 100;
    }

}
