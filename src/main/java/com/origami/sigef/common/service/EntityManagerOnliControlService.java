/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class EntityManagerOnliControlService {

    @PersistenceContext(name = CONFIG.PERSISTENCE)
    private EntityManager manager;

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public Connection getConnection() {
        return manager.unwrap(Connection.class);
    }

    public Object getNativeQuery(String query) {
        Query q;
        Object ob = null;
        try {
            q = manager.createNativeQuery(query);
            ob = (Object) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EntityManagerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public Object getNativeQuery(String query, Object[] val) {
        Query q;
        Object ob = null;
        try {
            q = manager.createNativeQuery(query);
            for (int i = 0; i < val.length; i++) {
                q.setParameter(i + 1, val[i]);
            }
            q.setMaxResults(1);
            ob = (Object) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EntityManagerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public List getNativeQueryFirstAndMaxResult(Class clase, String query, Object[] val, Integer first, Integer max) {
        Query q;
        List result = null;
        try {
            q = manager.createNativeQuery(query, clase);
            if (max != null) {
                q.setMaxResults(max);
            }
            if (first != null) {
                q.setFirstResult(first);
            }
            if (val != null) {
                for (int i = 1; i <= val.length; i++) {
                    q.setParameter(i, val[i - 1]);
                }
            }
            result = (List) q.getResultList();
            result.size();
        } catch (Exception e) {
            Logger.getLogger(EntityManagerService.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
