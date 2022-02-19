/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANGEL NAVARRO
 */
public class SqlUtils {

    private static final Logger LOG = Logger.getLogger(SqlUtils.class.getName());

    /**
     * Realiza la consulta a con la conecion dada y transforma el resultado a un
     * modelo de datos si este es proporcionado sino solo devuelve un solo dato
     * tipo objecto.
     *
     * @param <T>
     * @param c Conecion, debe ser cerrada manualmente la conexion no se cierra
     * en este metodo.
     * @param sql Sentencia de consulta sql
     * @param paramt parametros de la consulta si los hay
     * @param resultClass Objeto a transformar el resultado.
     * @return Objeto con el resultado de la consulta, Si el {@code resultClass}
     * devuelve un solo objeto
     * @throws SQLException
     */
    public static <T> T find(Connection c, String sql, List<Object> paramt, T resultClass) throws SQLException {
        T ob = resultClass;
        try {
            if (c == null) {
                return null;
            }
            try (PreparedStatement ps = c.prepareCall(sql)) {
                int countParamt = 1;
                if (paramt != null) {
                    for (Object object : paramt) {
                        ps.setObject(countParamt, object);
                        countParamt++;
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (resultClass != null) {
                            ResultSetMetaData metaData = rs.getMetaData();
                            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                String field = Utils.transformUpperCase(metaData.getColumnName(i).toLowerCase());
                                ReflexionEntity.setCampo(resultClass, field, rs.getObject(i));
                            }
                        } else {
                            ob = (T) rs.getObject(1);
                        }
                    }
                    rs.close();
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    /**
     * Realiza la consulta a con la conecion dada y transforma el resultado a un
     * modelo de datos si este es proporcionado sino solo devuelve un solo dato
     * tipo objecto.
     *
     * @param <T>
     * @param c Conecion, debe ser cerrada manualmente la conexion no se cierra
     * en este metodo.
     * @param sql Sentencia de consulta sql
     * @param paramt parametros de la consulta si los hay
     * @param resultClass Objeto a transformar el resultado.
     * @return Objeto con el resultado de la consulta, Si el {@code resultClass}
     * devuelve un solo objeto
     * @throws SQLException
     */
    public static List findAll(Connection c, String sql, List<Object> paramt, Class resultClass) throws SQLException {
        List result = null;
        try {
            if (c == null) {
                return null;
            }
            try (PreparedStatement ps = c.prepareCall(sql)) {
                int countParamt = 1;
                if (paramt != null) {
                    for (Object object : paramt) {
                        ps.setObject(countParamt, object);
                        countParamt++;
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (result == null) {
                            result = new ArrayList<>();
                        }
                        Object ob = null;
                        if (resultClass != null) {
                            ob = resultClass.getDeclaredConstructors()[0].newInstance();
                            ResultSetMetaData metaData = rs.getMetaData();
                            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                String field = Utils.transformUpperCase(metaData.getColumnName(i).toLowerCase());
                                ReflexionEntity.setCampo(ob, field, rs.getObject(i));
                            }
                        } else {
                            ob = rs.getObject(1);
                        }
                        result.add(ob);
                    }
                    rs.close();
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(SqlUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Returna el primary key de la tabla
     *
     * @param c Connection sql, debe ser cerrada manualmente la conexion no se
     * cierra en este metodo.
     * @param sql Sentemcia de insercion
     * @param paramt Parametros
     * @return id de la tabla.
     * @throws java.sql.SQLException
     */
    public static Long insert(Connection c, String sql, List<Object> paramt) throws SQLException {
        Long x = null;
        try {
            if (c != null) {
                c.setAutoCommit(false);
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    int countParamt = 1;
                    for (Object object : paramt) {
                        ps.setObject(countParamt, object);
                        countParamt++;
                    }
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        x = rs.getLong(1);
                    }
                    rs.close();
                    c.commit();
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, String.format("Sentencia %s", sql), e);
        }
        return x;
    }

    public static Long execute(Connection c, String sqlExecute, List<Object> paramt) {
        Long x = null;
        try {
            if (c != null) {
                c.setAutoCommit(false);
                try (PreparedStatement ps = c.prepareStatement(sqlExecute)) {
                    int countParamt = 1;
                    if (paramt != null) {
                        for (Object object : paramt) {
                            ps.setObject(countParamt, object);
                            countParamt++;
                        }
                    }
                    ps.executeUpdate();
                    x = Long.valueOf(ps.getUpdateCount());
                    c.commit();
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, String.format("Sentencia %s", sqlExecute), e);
        }
        return x;
    }

}
