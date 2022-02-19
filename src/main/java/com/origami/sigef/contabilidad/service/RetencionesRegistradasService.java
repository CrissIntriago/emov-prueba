/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RetencionesRegistradas;
import com.origami.sigef.common.service.AbstractService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class RetencionesRegistradasService extends AbstractService<RetencionesRegistradas> {
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RetencionesRegistradasService() {
        super(RetencionesRegistradas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
