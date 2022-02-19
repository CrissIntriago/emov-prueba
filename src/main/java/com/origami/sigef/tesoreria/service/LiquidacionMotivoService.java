package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.LiquidacionMotivo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class LiquidacionMotivoService extends AbstractService<LiquidacionMotivo> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LiquidacionMotivoService() {
        super(LiquidacionMotivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
