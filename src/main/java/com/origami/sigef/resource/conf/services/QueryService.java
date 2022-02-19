/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.entities.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriagos
 */
@Stateless
public class QueryService extends AbstractService<Query> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public QueryService() {
        super(Query.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getQuerySql(String code) {
        String result = (String) em.createQuery("SELECT q.sql FROM Query q WHERE q.code = ?1")
                .setParameter(1, code)
                .getSingleResult();
        return result;
    }
}
