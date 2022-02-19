/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.entities.TramitesUsuarios;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class TramitesUsuariosService extends AbstractService< TramitesUsuarios> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TramitesUsuariosService() {
        super(TramitesUsuarios.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
