/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@Stateless @javax.enterprise.context.Dependent
public class PlanNacionalObjetivoService extends AbstractService<PlanNacionalObjetivo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanNacionalObjetivoService() {
        super(PlanNacionalObjetivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public PlanNacionalObjetivo existePlan(PlanNacionalObjetivo obj) {
        String queryString = getQueryHQLExistePlan(obj);

        Map<String, Object> params = getParameter(obj);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanNacionalObjetivo> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private String getQueryHQLExistePlan(PlanNacionalObjetivo objetivo) {
        String query = null;
        if (objetivo != null) {
            return "SELECT obj FROM PlanNacionalObjetivo obj WHERE obj.codigo = :codigo AND obj.estado = true";
        }
        return query;
    }

    private Map<String, Object> getParameter(PlanNacionalObjetivo obj) {
        Map<String, Object> params = new HashMap<>();
        if (obj != null) {
            params.put("codigo", obj.getCodigo());

        }
        return params;
    }

    public List<PlanNacionalPolitica> getPoliticasByObjetivo(PlanNacionalObjetivo objetivo) {
        String sql = "SELECT p FROM PlanNacionalPolitica p JOIN p.objetivo e WHERE p.estado= TRUE AND e.id = " + objetivo.getId();
        Query query = getEntityManager().createQuery(sql);

        List<PlanNacionalPolitica> result = query.getResultList();

        return result;
    }

}
