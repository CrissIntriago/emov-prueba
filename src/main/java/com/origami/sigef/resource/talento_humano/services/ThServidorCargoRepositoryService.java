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
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargoRepository;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThServidorCargoRepositoryService extends AbstractService<ThServidorCargoRepository> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThServidorCargoRepositoryService() {
        super(ThServidorCargoRepository.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ThServidorCargoRepository findServidor(Servidor thServidor) {
        ThServidorCargoRepository result = (ThServidorCargoRepository) em.createQuery(QUERY.FIND_SERVIDOR_CARGO_ACTIVO)
                .setParameter(1, new BigInteger(thServidor.getId() + ""))
                .getSingleResult();
        return result;
    }

    public List<ThServidorCargoRepository> findServidorCargo(Long thServidor) {
        List<ThServidorCargoRepository> result = (List<ThServidorCargoRepository>) em.createQuery(QUERY.FIND_SERVIDOR_CARGO_REPOSITORIO)
                .setParameter(1, new BigInteger(thServidor + ""))
                .getResultList();
        return result;
    }

}
