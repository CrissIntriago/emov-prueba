/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.LiquidacionTipo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class LiquidacionTipoService extends AbstractService<LiquidacionTipo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LiquidacionTipoService() {
        super(LiquidacionTipo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public LiquidacionTipo getTipoLiquidacion(String prefijo) {
        try {
            Query query = em.createQuery("SELECT lt FROM LiquidacionTipo lt WHERE lt.prefijo=?1 AND lt.estado=true")
                    .setParameter(1, prefijo);
            LiquidacionTipo result = (LiquidacionTipo) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
