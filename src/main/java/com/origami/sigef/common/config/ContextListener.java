/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 *
 * @author ANGEL NAVARRO
 */
@WebListener()
public class ContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // timezone y locale default para el sistema
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        Locale.setDefault(new Locale("es", "EC"));
        CONFIG.URL_APP = sce.getServletContext().getContextPath() + "/";
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("dd MM yyyy HH mm ss");
        ConvertUtils.register(dateConverter, java.util.Date.class);
        System.out.println("//// DATE CONVERTER REGISTERED");

        // carga de propiedades
        PropertiesLoader props1 = new PropertiesLoader(sce.getServletContext());
        props1.load();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver driver = null;
        // clear drivers
        while (drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error remover drivers. {0}", ex.getMessage());
            }
        }
        System.out.println("//// Close session factory!!!");
    }
}
