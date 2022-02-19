package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteSRI;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@javax.enterprise.context.Dependent
public class ComprobanteSRIService extends AbstractService<ComprobanteSRI> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ComprobanteSRIService() {
        super(ComprobanteSRI.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public ComprobanteSRI getComprobanteSRI(Long idLiquidacion) {
        List<ComprobanteSRI> result = (List<ComprobanteSRI>) em.createQuery("SELECT cs FROM ComprobanteSRI cs WHERE cs.idOrdenPago=?1 ORDER BY cs.id DESC")
                .setParameter(1, idLiquidacion)
                .getResultList();
        return result.get(0);
    }

}
