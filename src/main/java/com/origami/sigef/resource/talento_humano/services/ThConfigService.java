/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThConfigService extends AbstractService<ThConfig> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThConfigService() {
        super(ThConfig.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ThConfig findCode(String CONFIG_DECIMO_TERCERO) {
        ThConfig result = (ThConfig) em.createQuery("SELECT th FROM ThConfig th WHERE th.code =?1")
                .setParameter(1, CONFIG_DECIMO_TERCERO)
                .getSingleResult();
        return result;
    }

}
