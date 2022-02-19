/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.common.service.interfaces.DatabaseLocal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author ANGEL NAVARRO
 */
@Stateless
@javax.enterprise.context.Dependent
public class DataSourcesConfig implements DatabaseLocal {

    private static final Logger LOG = Logger.getLogger(DataSourcesConfig.class.getName());

    @Inject
    private ValoresService valoresService;

    /**
     *
     * @param config_db NOMBRE DEL KEY CON QUE EMPIEZA LA CONFIGURACION DE
     * CONEXION A LA BASE DE DATOS
     * @return {@link DataSource} con la conexion a la base de dato.
     */
    @Override
    public DataSource getDataSource(String config_db) {
        BasicDataSource ds = null;
        List<Valores> valores = null;
        if (config_db == null) {
            valores = valoresService.findByNamedQuery("Valores.findCodeLike", "DAFAULT_BASE%");
        } else {
            valores = valoresService.findByNamedQuery("Valores.findCodeLike", config_db + "%");
        }
        try {
            ds = new BasicDataSource();
            for (Valores valor : valores) {
                if (valor.getCode().endsWith("_URL")) {
                    ds.setUrl(valor.getValorString());
                } else if (valor.getCode().endsWith("_USER")) {
                    ds.setUsername(valor.getValorString());
                } else if (valor.getCode().endsWith("_PASS")) {
                    ds.setPassword(valor.getValorString());
                } else if (valor.getCode().endsWith("_DRIVER")) {
                    ds.setDriverClassName(valor.getValorString());
                } else if (valor.getCode().endsWith("_MIN_POOL_SIZE")) {
                    ds.setMinIdle(valor.getValorNumeric().intValue());
                } else if (valor.getCode().endsWith("_MAX_IDLE_TIME")) {
                    ds.setMaxIdle(valor.getValorNumeric().intValue());
                } else if (valor.getCode().endsWith("_MAX_POOL_SIZE")) {
                    ds.setMaxActive(valor.getValorNumeric().intValue());
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
        }
        return ds;
    }

    @Override
    public Connection getConnection(String config_db) {
        List<Valores> valores = null;
        if (config_db == null) {
            valores = valoresService.findByNamedQuery("Valores.findCodeLike", "DAFAULT_BASE%");
        } else {
            valores = valoresService.findByNamedQuery("Valores.findCodeLike", config_db + "%");
        }
        String driver = null;
        String url = null;
        Properties props = new Properties();
        for (Valores valor : valores) {
            if (valor.getCode().endsWith("_URL")) {
                url = valor.getValorString();
            } else if (valor.getCode().endsWith("_USER")) {
                props.setProperty("user", valor.getValorString());
            } else if (valor.getCode().endsWith("_PASS")) {
                props.setProperty("password", valor.getValorString());
            } else if (valor.getCode().endsWith("_DRIVER")) {
                driver = valor.getValorString();
            }
        }
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "Intetando ejecutar driver " + driver, ex);
            }
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Intetando ejecutar driver " + url, ex);
            }
            return conn;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
        }
        return null;
    }
}
