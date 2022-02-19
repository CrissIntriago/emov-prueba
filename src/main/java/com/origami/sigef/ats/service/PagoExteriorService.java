/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.service;

import com.origami.sigef.ats.entities.PagoExterior;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jesus
 */
@Stateless
@javax.enterprise.context.Dependent
public class PagoExteriorService extends AbstractService<PagoExterior> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PagoExteriorService() {
        super(PagoExterior.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
