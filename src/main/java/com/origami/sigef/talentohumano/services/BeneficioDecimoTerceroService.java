package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiosDecimoTercero;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class BeneficioDecimoTerceroService extends AbstractService<BeneficiosDecimoTercero> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BeneficioDecimoTerceroService() {
        super(BeneficiosDecimoTercero.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BeneficiosDecimoTercero> getBeneficiosTerceroXtipo(TipoRolBeneficios tipo) {
        try {
            Query query =  em.createQuery("SELECT b FROM BeneficiosDecimoTercero b where b.estado = true AND b.tipoRolBeneficio = ?1")
                    .setParameter(1, tipo);
            List<BeneficiosDecimoTercero> result = query.getResultList() ;
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

}
