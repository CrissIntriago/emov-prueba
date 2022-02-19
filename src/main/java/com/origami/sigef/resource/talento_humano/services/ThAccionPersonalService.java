/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThAccionPersonal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThAccionPersonalService extends AbstractService<ThAccionPersonal> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThAccionPersonalService() {
        super(ThAccionPersonal.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long getOrderaccionPersonal() {
        try {
            Long result = findByNamedQuery1("ThAccionPersonal.findByOrdenMax");
            if (result == null) {
                result = 1L;
            }
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
}
