/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThPrestamoIess;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThPrestamoIessService extends AbstractService<ThPrestamoIess> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThPrestamoIessService() {
        super(ThPrestamoIess.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer groupData(Long id, Boolean tipo1, Boolean tipo2) {
        return (Integer) em.createNativeQuery("SELECT * FROM talento_humano.update_prestamo_iess(?1,?2,?3)")
                .setParameter(1, id)
                .setParameter(2, tipo1)
                .setParameter(3, tipo2)
                .getSingleResult();

    }
}
