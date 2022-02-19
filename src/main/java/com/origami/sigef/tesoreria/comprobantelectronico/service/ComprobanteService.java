package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class ComprobanteService extends AbstractService<Comprobantes> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ComprobanteService() {
        super(Comprobantes.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Comprobantes findByCodigo(String codigo) {
        try {
            return (Comprobantes) getEntityManager().createQuery("SELECT d FROM Comprobantes d WHERE d.codigo=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Comprobantes> findComprobantesRetienen() {
        List<Comprobantes> result = (List<Comprobantes>) getEntityManager().createQuery("SELECT c FROM Comprobantes c "
                + " WHERE c.retiene = true ")
                .getResultList();

        return result;
    }
    
    public List<Comprobantes> findComprobantesActivos() {
        List<Comprobantes> result = (List<Comprobantes>) getEntityManager().createQuery("SELECT c FROM Comprobantes c WHERE c.estado = true ")
                .getResultList();

        return result;
    }

}
