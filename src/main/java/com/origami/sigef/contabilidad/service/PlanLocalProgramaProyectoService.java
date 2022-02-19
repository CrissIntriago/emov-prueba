/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
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
public class PlanLocalProgramaProyectoService extends AbstractService<PlanLocalProgramaProyecto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanLocalProgramaProyectoService() {
        super(PlanLocalProgramaProyecto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    private String getQueryHQLExisteCuenta(PlanLocalProgramaProyecto p) {
        String query = null;
        if (p.getCodigo() != null) {
            return "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.codigo = :codigo AND p.periodo = :periodo AND p.estado = true";
        }
        return query;
    }

    public PlanLocalProgramaProyecto existeCuenta(PlanLocalProgramaProyecto c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanLocalProgramaProyecto> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(PlanLocalProgramaProyecto p) {

        Map<String, Object> params = new HashMap<>();
        if (p.getCodigo() != null) {

            params.put("codigo", p.getCodigo());
            params.put("periodo", p.getPeriodo());

        }

        return params;
    }
}
