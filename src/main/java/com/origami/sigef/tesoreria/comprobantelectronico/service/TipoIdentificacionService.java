package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.TipoIdentificacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class TipoIdentificacionService extends AbstractService<TipoIdentificacion> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TipoIdentificacionService() {
        super(TipoIdentificacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public TipoIdentificacion findByTamanio(Integer size) {
        System.out.println("size: " + size);
        try {
            return (TipoIdentificacion) getEntityManager().createQuery("SELECT d FROM TipoIdentificacion d WHERE d.tamanio=:size")
                    .setParameter("size", size).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
