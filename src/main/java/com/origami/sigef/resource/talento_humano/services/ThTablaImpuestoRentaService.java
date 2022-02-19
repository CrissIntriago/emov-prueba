/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThTablaImpuestoRenta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Stateless
public class ThTablaImpuestoRentaService extends AbstractService<ThTablaImpuestoRenta> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThTablaImpuestoRentaService() {
        super(ThTablaImpuestoRenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
