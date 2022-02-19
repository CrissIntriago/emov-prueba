package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.PorcentajeImpuesto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class PorcentajeImpuestoService extends AbstractService<PorcentajeImpuesto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PorcentajeImpuestoService() {
        super(PorcentajeImpuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public PorcentajeImpuesto findByCodigoAndValorPorcentaje(String codigo, Double porcentaje) {
        try {
            return (PorcentajeImpuesto) getEntityManager().createQuery("SELECT d FROM PorcentajeImpuesto d "
                    + "WHERE d.codigoTarifa=:codigo AND d.porcentaje=:porcentaje")
                    .setParameter("codigo", codigo)
                    .setParameter("porcentaje", porcentaje).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
