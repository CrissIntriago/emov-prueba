package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class FormaPagoService extends AbstractService<FormaPago> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FormaPagoService() {
        super(FormaPago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public FormaPago findByCodigo(String codigo) {
        try {
            return (FormaPago) getEntityManager().createQuery("SELECT d FROM FormaPago d WHERE d.codigo=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
