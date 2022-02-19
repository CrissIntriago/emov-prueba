/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.AvalBandaImpositiva;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.origami.sigef.common.config.CONFIG;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class AvalBandaImpositivaService extends AbstractService<AvalBandaImpositiva> {
      private static final Logger LOG = Logger.getLogger(AvalBandaImpositivaService.class.getName());
    
    private static final long serialVersionUID = 1L;
     @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AvalBandaImpositivaService() {
        super(AvalBandaImpositiva.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
