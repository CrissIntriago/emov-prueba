/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargoRepository;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class ThServidorCargoService extends AbstractService<ThServidorCargo> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThServidorCargoService() {
        super(ThServidorCargo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ThServidorCargo findByThServidor(Servidor thServidor) {
        ThServidorCargo result = (ThServidorCargo) em.createQuery(QUERY.FIND_SERVIDOR_CARGO)
                .setParameter(1, thServidor)
                .getSingleResult();
        return result;
    }

    public Integer getCount() {
        Long temp = (Long) em.createQuery("SELECT COUNT(tsc.id) FROM ThServidorCargo tsc WHERE tsc.activo = true")
                .getSingleResult();
        return temp.intValue();
    }

}
