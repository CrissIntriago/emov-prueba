/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.EspCementerio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class EspCemeterioService extends AbstractService<EspCementerio> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EspCemeterioService() {
        super(EspCementerio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
