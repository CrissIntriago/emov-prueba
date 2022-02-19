/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Pais;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gutya
 */
@Stateless
public class PaisService extends AbstractService<Pais> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PaisService() {
        super(Pais.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
