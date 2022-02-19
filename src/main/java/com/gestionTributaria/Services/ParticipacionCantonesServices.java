/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.ParticipacionCantones;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
@javax.enterprise.context.Dependent
public class ParticipacionCantonesServices extends AbstractService<ParticipacionCantones> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ParticipacionCantonesServices() {
        super(ParticipacionCantones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
