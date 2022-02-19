/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
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
public class PlanNacionalEjeService extends AbstractService<PlanNacionalEje> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanNacionalEjeService() {
        super(PlanNacionalEje.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public PlanNacionalEje existePlan(PlanNacionalEje eje) {
        String queryString = getQueryHQLExistePlan(eje);

        Map<String, Object> params = getParameter(eje);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanNacionalEje> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private String getQueryHQLExistePlan(PlanNacionalEje eje) {
        String query = null;
        if (eje != null) {
            return "SELECT eje FROM PlanNacionalEje eje WHERE eje.codigo = :codigo AND eje.estado = true";
        }
        return query;
    }

    private Map<String, Object> getParameter(PlanNacionalEje eje) {
        Map<String, Object> params = new HashMap<>();
        if (eje != null) {
            params.put("codigo", eje.getCodigo());
        }
        return params;
    }

    public List<PlanNacionalObjetivo> getObjetivosByEje(PlanNacionalEje eje) {
        String sql = "SELECT p FROM PlanNacionalObjetivo p JOIN p.eje e WHERE p.estado = TRUE AND e.id = " + eje.getId();
        Query query = getEntityManager().createQuery(sql);

        List<PlanNacionalObjetivo> result = query.getResultList();

        return result;
    }

}
