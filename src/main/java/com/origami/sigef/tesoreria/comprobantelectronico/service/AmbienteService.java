package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@javax.enterprise.context.Dependent
public class AmbienteService extends AbstractService<Ambiente> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AmbienteService() {
        super(Ambiente.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Ambiente findByCodigo(String codigo) {
        try {
            return (Ambiente) getEntityManager().createQuery("SELECT d FROM Ambiente d WHERE d.codigo=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Ambiente findAmbienteActivo() {
        try {
            return (Ambiente) getEntityManager().createQuery("SELECT d FROM Ambiente d WHERE d.activo = TRUE ")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
