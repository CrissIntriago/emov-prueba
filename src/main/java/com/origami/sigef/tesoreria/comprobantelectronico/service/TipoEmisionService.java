package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.TipoEmision;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class TipoEmisionService extends AbstractService<TipoEmision> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TipoEmisionService() {
        super(TipoEmision.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public TipoEmision findByOnline(Boolean online) {
        try {
            return (TipoEmision) getEntityManager().createQuery("SELECT d FROM TipoEmision d WHERE d.esOnline=:online")
                    .setParameter("online", online).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
