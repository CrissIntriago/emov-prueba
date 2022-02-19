/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@Stateless @javax.enterprise.context.Dependent
public class PlanLocalSistemaService extends AbstractService<PlanLocalSistema> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanLocalSistemaService() {
        super(PlanLocalSistema.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    private String getQueryHQLExisteCuenta(PlanLocalSistema p) {
        String query = null;
        if (p.getCodigo() != null) {
            return "SELECT p FROM PlanLocalSistema p WHERE p.codigo = :codigo AND p.periodo = :periodo AND p.estado = true";
        }
        return query;
    }

    public PlanLocalSistema existeCuenta(PlanLocalSistema c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanLocalSistema> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(PlanLocalSistema p) {

        Map<String, Object> params = new HashMap<>();
        if (p.getCodigo() != null) {

            params.put("codigo", p.getCodigo());
            params.put("periodo", p.getPeriodo());

        }

        return params;
    }

    public List<PlanLocalObjetivo> getObjetivosByEje(PlanLocalSistema plan) {
        String sql = "SELECT p FROM PlanLocalObjetivo p JOIN p.sistema e WHERE p.estado = TRUE AND e.id = " + plan.getId();
        Query query = getEntityManager().createQuery(sql);

        List<PlanLocalObjetivo> result = query.getResultList();

        return result;
    }
    
    public List<PlanLocalSistema> getPlanSistema(){
        Query query = em.createQuery("SELECT pls FROM PlanLocalSistema pls where pls.estado = TRUE ORDER BY pls.codigo");
        List<PlanLocalSistema> lista = (List<PlanLocalSistema>)query.getResultList();
        return lista;
    }
    
    
    
    public List<PlanLocalSistema> getListSistemaPlan(PlanLocalSistema sistema, Boolean estado){
    return (List<PlanLocalSistema>) em.createQuery("SELECT c FROM PlanLocalSistema c WHERE c.estado = :estado")
            .setParameter("estado", estado);
    }
}
