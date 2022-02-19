/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiosSindicales;
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
public class BeneficiosSindicalesService extends AbstractService<BeneficiosSindicales> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BeneficiosSindicalesService() {
        super(BeneficiosSindicales.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BeneficiosSindicales> findByBeneficiosXTipo(TipoRolBeneficios tipo) {
        try {
            Query query = em.createQuery("SELECT b FROM BeneficiosSindicales b WHERE b.tipoRolBeneficios = ?1 AND b.estado = TRUE ")
                    .setParameter(1, tipo);
            List<BeneficiosSindicales> valores = query.getResultList();
            return valores;
        } catch (NoResultException e) {
            return null;
        }

    }
}
