package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.ClaveAcceso;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class ClaveAccesoService extends AbstractService<ClaveAcceso> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ClaveAccesoService() {
        super(ClaveAcceso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public ClaveAcceso findByClaveAcceso(String claveAcceso) {
        try {
            return (ClaveAcceso) getEntityManager().createQuery("SELECT d FROM ClaveAcceso d WHERE d.claveAcceso=:claveAcceso")
                    .setParameter("claveAcceso", claveAcceso).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
