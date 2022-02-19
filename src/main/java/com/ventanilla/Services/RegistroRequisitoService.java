package com.ventanilla.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ricardo
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RegistroRequisitoService extends AbstractService<RegistroSolicitudRequisitos> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RegistroRequisitoService() {
        super(RegistroSolicitudRequisitos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
