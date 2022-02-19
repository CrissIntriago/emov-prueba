/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
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
public class PlanLocalObjetivoService extends AbstractService<PlanLocalObjetivo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanLocalObjetivoService() {
        super(PlanLocalObjetivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    private String getQueryHQLExisteCuenta(PlanLocalObjetivo p) {
        String query = null;
        if (p.getCodigo() != null) {
            return "SELECT p FROM PlanLocalObjetivo p WHERE p.codigo = :codigo AND p.periodo = :periodo AND p.estado = true";
        }
        return query;
    }

    public PlanLocalObjetivo existeCuenta(PlanLocalObjetivo c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanLocalObjetivo> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(PlanLocalObjetivo p) {

        Map<String, Object> params = new HashMap<>();
        if (p.getCodigo() != null) {

            params.put("codigo", p.getCodigo());
            params.put("periodo", p.getPeriodo());

        }
        return params;
    }

    public List<PlanLocalPolitica> getObjetivosByEje(PlanLocalObjetivo objetivo) {
        String sql = "SELECT p FROM PlanLocalPolitica p JOIN p.objetivo e WHERE p.estado = true AND e.id = " + objetivo.getId();
        Query query = getEntityManager().createQuery(sql);

        List<PlanLocalPolitica> result = query.getResultList();

        return result;
    }
}
