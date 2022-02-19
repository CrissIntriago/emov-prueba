/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThRegimenLaboralService extends AbstractService<ThRegimenLaboral> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThRegimenLaboralService() {
        super(ThRegimenLaboral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean findCode(String code) {
        try {
            ThRegimenLaboral result = (ThRegimenLaboral) em.createQuery(QUERY.FIND_CODE_TH_RL)
                    .setParameter(1, code)
                    .getSingleResult();
            return result.getId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public List<ThRegimenLaboral> getListEscala(ThRegimenLaboral thRegimenLaboral) {
        List<ThRegimenLaboral> result = (List<ThRegimenLaboral>) em.createQuery(QUERY.FIND_LIST_TH_RL)
                .setParameter(1, thRegimenLaboral)
                .getResultList();
        return result;
    }

    public List<ThRegimenLaboral> getRegimenActivo() {
        return (List<ThRegimenLaboral>) em.createQuery("SELECT rl FROM ThRegimenLaboral rl WHERE rl.estado = true ORDER BY rl.nombre")
                .getResultList();
    }
}
