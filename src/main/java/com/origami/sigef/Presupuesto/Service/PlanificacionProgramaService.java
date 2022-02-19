/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.PlanificacionPrograma;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.PlanificacionPlan;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ENRIQUE
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PlanificacionProgramaService extends AbstractService<PlanificacionPrograma> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanificacionProgramaService() {
        super(PlanificacionPrograma.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PlanificacionPrograma> getListAll() {
        List<PlanificacionPrograma> result = (List<PlanificacionPrograma>) em.createQuery("SELECT p FROM PlanificacionPrograma p ").getResultList();
        return result;
    }

}
