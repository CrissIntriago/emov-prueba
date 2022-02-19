/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContComprobantePagoService extends AbstractService<ContComprobantePago> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContComprobantePagoService() {
        super(ContComprobantePago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer nextRegistro(Short periodo) {
        Integer result = (Integer) em.createQuery(QUERY.NEXT_NUM_COMPROBANTE)
                .setParameter(1, periodo)
                .getSingleResult();
        return result;
    }

    public ContComprobantePago getLastContComprobantePago(Short periodo) {
        try {
            return (ContComprobantePago) em.createQuery("SELECT c FROM ContComprobantePago c where c.periodo = :periodo ORDER BY c.id DESC")
                    .setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
