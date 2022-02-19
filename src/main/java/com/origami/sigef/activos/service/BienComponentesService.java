/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.BienComponentes;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEc
 */
@Stateless @javax.enterprise.context.Dependent
public class BienComponentesService extends AbstractService<BienComponentes> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienComponentesService() {
        super(BienComponentes.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }
}
