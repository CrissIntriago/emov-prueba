/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.entities.transporte.CooperativaVehiculo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class CooperativaVehiculoService extends AbstractService<CooperativaVehiculo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;

    public CooperativaVehiculoService() {
        super(CooperativaVehiculo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void updateRevision(Cooperativa c) {
        this.updateHql("CooperativaVehiculo.update", c);
    }

}
