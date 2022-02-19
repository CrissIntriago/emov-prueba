package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.ImpuestosAsignadosRetencion;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class ImpuestosAsignadosRetencionService extends AbstractService<ImpuestosAsignadosRetencion> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ImpuestosAsignadosRetencionService() {
        super(ImpuestosAsignadosRetencion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public ImpuestosAsignadosRetencion findByCodigo(String codigo) {
        try {
            return (ImpuestosAsignadosRetencion) getEntityManager().createQuery("SELECT d FROM ImpuestosAsignadosRetencion d WHERE d.codigo=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
