/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.origami.sigef.common.service.LogTransaccionService;
import com.origami.sigef.common.util.ReflexionEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.NoResultException;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 *
 * @author ANGEL NAVARRO
 */
public class LogEntityListener {

    private static final Logger LOG = Logger.getLogger(LogEntityListener.class.getName());

    private LogTransaccionService service;

    @PostPersist
    public void setCreatedPost(Object entity) {
        try {
            getService().saveLog(entity, null, "CREATE");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    @PreUpdate
    public void setUpdatedPost(Object entity) {
        try {
//            Object id = ReflexionEntity.getIdFromEntity(entity);
//            Map<String, Object> props = new HashMap<>();
////            props.put(QueryHints.NATIVE_CONNECTION, HintValues.TRUE);
//            props.put(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
//            props.put(QueryHints.CACHE_STORE_MODE, CacheStoreMode.BYPASS);
////            Object entity1 = getService().getEntityManager().find(entity.getClass(), id, props);
//            Object entity1 = find(entity.getClass(), id);
            getService().saveLog(entity, null, "UPDATE");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    @PreRemove
    public void setRemoveOn(Object entity) {
        try {
            getService().saveLog(entity, null, "REMOVE");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    @Transactional(MANDATORY)
    private LogTransaccionService getService() {
        if (service == null) {
            try {
                service = (LogTransaccionService) new InitialContext().lookup("java:module/LogTransaccionService");
            } catch (NamingException ex) {
                Logger.getLogger(LogEntityListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

    public Object find(Class entityClass, Object id) {
        try {
            CriteriaBuilder cb = getService().getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = getService().getEntityManager().getCriteriaBuilder().createQuery();
            Root from = cq.from(entityClass);
            cq.select(from);
            cq.where(cb.equal(from.get(ReflexionEntity.getNameIdEntity(entityClass)), id));
            return getService().getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
