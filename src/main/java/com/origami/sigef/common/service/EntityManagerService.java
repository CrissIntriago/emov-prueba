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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.hibernate.internal.SessionImpl;

/**
 *
 * @author Dairon Freddy
 */
@Stateless
@javax.enterprise.context.Dependent
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class EntityManagerService {

    private static final Logger LOG = Logger.getLogger(EntityManagerService.class.getName());

    @PersistenceContext(name = CONFIG.PERSISTENCE_UNIT)
    private EntityManager manager;

    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public <T> T find(Class entity, Object id) {
        try {
            return (T) getManager().find(entity, id);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Connection getConnection() {
//        return manager.unwrap(Connection.class);
//        return getManager().unwrap(Session.class).connection();
        return getManager().unwrap(SessionImpl.class).connection();
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
