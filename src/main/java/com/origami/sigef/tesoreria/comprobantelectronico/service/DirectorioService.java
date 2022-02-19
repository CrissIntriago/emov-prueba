package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Directorio;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class DirectorioService
        extends AbstractService<Directorio> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DirectorioService() {
        super(Directorio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Directorio findByCodigo(Integer code) {
        try {
            return (Directorio) getEntityManager().createQuery("SELECT d FROM Directorio d WHERE d.codigo=:code")
                    .setParameter("code", code).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
