/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.FlujoEfectivo;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class FlujoEfectivoService extends AbstractService<FlujoEfectivo> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FlujoEfectivoService() {
        super(FlujoEfectivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
