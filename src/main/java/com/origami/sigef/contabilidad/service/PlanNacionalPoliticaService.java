/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

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
public class PlanNacionalPoliticaService extends AbstractService<PlanNacionalPolitica> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanNacionalPoliticaService() {
        super(PlanNacionalPolitica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public PlanNacionalPolitica existePlan(PlanNacionalPolitica obj) {
        String queryString = getQueryHQLExistePlan(obj);

        Map<String, Object> params = getParameter(obj);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanNacionalPolitica> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private String getQueryHQLExistePlan(PlanNacionalPolitica pt) {
        String query = null;
        if (pt != null) {
            return "SELECT pt FROM PlanNacionalPolitica pt WHERE pt.codigo = :codigo AND pt.objetivo = :objetivo AND pt.estado = true";
        }
        return query;
    }

    private Map<String, Object> getParameter(PlanNacionalPolitica pt) {
        Map<String, Object> params = new HashMap<>();
        if (pt != null) {
            params.put("codigo", pt.getCodigo());
            params.put("objetivo", pt.getObjetivo());
        }
        return params;
    }
}
