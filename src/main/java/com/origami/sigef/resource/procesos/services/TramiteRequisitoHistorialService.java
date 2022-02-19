/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.services;

import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class TramiteRequisitoHistorialService extends AbstractService<TipoTramiteRequisitoHistorial> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TramiteRequisitoHistorialService() {
        super(TipoTramiteRequisitoHistorial.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
