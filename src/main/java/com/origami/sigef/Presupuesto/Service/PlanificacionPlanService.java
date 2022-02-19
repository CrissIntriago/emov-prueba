/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.PlanificacionPrograma;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
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
public class PlanificacionPlanService extends AbstractService<PlanificacionPlan> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanificacionPlanService() {
        super(PlanificacionPlan.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PlanificacionPlan> getListAll() {
        List<PlanificacionPlan> result = (List<PlanificacionPlan>) em.createQuery("SELECT p FROM PlanificacionPlan p ").getResultList();
        return result;
    }
    
    public BigInteger getNewIdPlanificacion(String tipo) {
        BigInteger id = getIdPlanificacion(tipo);
        setIdPlanificacion(id.add(BigInteger.ONE),tipo);
        return id;
    }

    public BigInteger getIdPlanificacion(String tipo) {
        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery(QUERY.FIND_ID_PLANIFICACION).setParameter(1, tipo).getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return BigInteger.ZERO;
    }

    public void setIdPlanificacion(BigInteger nuevoId, String tipo) {
        em.createNativeQuery(QUERY.UPDATE_ID_PLANIFICACION).setParameter(1, nuevoId).setParameter(2, tipo).executeUpdate();
    }

    public String getIdObjetivoLocal(PlanLocalObjetivo objetivo) {
        List<String> result = (List<String>) em.createNativeQuery(QUERY.FIND_MAX_CODE_OBJETIVO_LOCAL).setParameter(1, objetivo.getId()).getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public PlanLocalPolitica getPoliticaNoAplica(PlanLocalObjetivo objetivo) {
        List<PlanLocalPolitica> result = (List<PlanLocalPolitica>) em.createQuery("SELECT p FROM PlanLocalPolitica p where p.objetivo =:objetivo and p.descripcion = 'NO APLICA'")
                .setParameter("objetivo", objetivo).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
    
    public PlanificacionPlan obtieneNombrePlan (String codigo) {
        List<PlanificacionPlan> result = (List<PlanificacionPlan>) em.createQuery("SELECT p FROM PlanificacionPlan p where p.codigo = :codigo")
                .setParameter("codigo", codigo).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        PlanificacionPlan plan = new PlanificacionPlan();
        plan.setDescripcion("NO APLICA");
        return plan;
    }
    
    public PlanificacionPrograma obtieneNombrePrograma (String codigo) {
        List<PlanificacionPrograma> result = (List<PlanificacionPrograma>) em.createQuery("SELECT p FROM PlanificacionPrograma p where p.codigo = :codigo")
                .setParameter("codigo", codigo).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        PlanificacionPrograma programa = new PlanificacionPrograma();
        programa.setDescripcion("NO APLICA");
        return programa;
    }
    
    public PlanificacionPlan obtienePlanNoAplica (PlanAnualPoliticaPublica politica) {
        List<PlanificacionPlan> result = (List<PlanificacionPlan>) em.createQuery("SELECT p FROM PlanificacionPlan p where p.politicaPublica = :politica AND p.estado = TRUE")
                .setParameter("politica", politica).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        PlanificacionPlan plan = new PlanificacionPlan();
        return plan;
    }
    
    public PlanificacionPrograma obtieneProyectoNoAplica (PlanificacionPlan plan) {
        List<PlanificacionPrograma> result = (List<PlanificacionPrograma>) em.createQuery("SELECT p FROM PlanificacionPrograma p where p.panificacion = :plan AND p.estado = TRUE")
                .setParameter("plan", plan).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        PlanificacionPrograma programa = new PlanificacionPrograma();
        return programa;
    }
}
