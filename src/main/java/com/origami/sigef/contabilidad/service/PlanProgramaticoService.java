/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.PlanProgramatico;
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
public class PlanProgramaticoService extends AbstractService<PlanProgramatico> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanProgramaticoService() {
        super(PlanProgramatico.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Short getMaxValueForChild(PlanProgramatico p, boolean funcion) {

        Short val = 0;

        if (funcion) {
            val = (Short) getEntityManager().createQuery("SELECT MAX(p.funcion) FROM PlanProgramatico p WHERE p.estado = :estado")
                    .setParameter("estado", p.getEstado())
                    .getSingleResult();
        } else {

            String queryString = getQueryHQLMax(p);
            Map<String, Object> params = getParameter(p);

            final Query query = getEntityManager().createQuery(queryString);

            params.entrySet().forEach((param) -> {
                query.setParameter(param.getKey(), param.getValue());
            });

            val = (Short) query.getSingleResult();
        }

        if (val != null) {
            val++;
        } else {
            val = 1;
        }
        return val;
    }

    private String getQueryHQLMax(PlanProgramatico p) {
        String query = null;
        switch (p.getNivel().getOrden()) {
            case 1:
                return "SELECT MAX(p.programa) FROM PlanProgramatico p WHERE p.funcion = :funcion AND p.periodo = :periodo AND p.estado = TRUE";
            case 2:
                return "SELECT MAX(p.subprograma) FROM PlanProgramatico p WHERE p.funcion = :funcion AND p.programa = :programa AND p.periodo = :periodo AND p.estado = TRUE";
        }
        return query;
    }

    private String getQueryHQLExisteCuenta(PlanProgramatico p) {
        String query = null;
        switch (p.getNivel().getOrden()) {
            case 1:
                return "SELECT p FROM PlanProgramatico p WHERE p.funcion = :funcion AND p.periodo = :periodo AND p.estado = true";
            case 2:
                return "SELECT p FROM PlanProgramatico p WHERE p.funcion = :funcion AND p.programa = :programa AND p.periodo = :periodo AND p.estado = true";
            case 3:
                return "SELECT p FROM PlanProgramatico p WHERE p.funcion = :funcion AND p.programa = :programa AND p.subprograma = :subprograma AND p.periodo = :periodo AND p.estado = true";

        }
        return query;
    }

    public List<PlanProgramatico> deleteFechaCaducidad(PlanProgramatico fecha) {
        String sql = "SELECT p FROM PlanProgramatico p WHERE p.fechaCaducidad =" + fecha.getFechaCaducidad() + "AND p.estado = true";
        Query query = getEntityManager().createQuery(sql);
        List<PlanProgramatico> result = query.getResultList();
        if (result != null) {
            if (!result.isEmpty()) {
                return (List<PlanProgramatico>) result.get(0);
            }
        }
        return null;
    }

    public List<PlanProgramatico> getHijosByPadre(PlanProgramatico padre) {
        return findByNamedQuery("PlanProgramatico.findByPadre", new Object[]{padre.getId()});
    }

    public PlanProgramatico existeCuenta(PlanProgramatico p) {
        String queryString = getQueryHQLExisteCuenta(p);

        Map<String, Object> params = getParameter(p);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<PlanProgramatico> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(PlanProgramatico p) {

        Map<String, Object> params = new HashMap<>();
        switch (p.getNivel().getOrden()) {
            case 1:
                params.put("funcion", p.getFuncion());
                params.put("periodo", p.getPeriodo());
                break;
            case 2:
                params.put("funcion", p.getFuncion());
                params.put("programa", p.getPrograma());
                params.put("periodo", p.getPeriodo());
                break;
            case 3:
                params.put("funcion", p.getFuncion());
                params.put("programa", p.getPrograma());
                params.put("subprograma", p.getSubprograma());
                params.put("periodo", p.getPeriodo());
                break;
            case 4:
                params.put("funcion", p.getFuncion());
                params.put("gruprograma", p.getPrograma());
                params.put("subprograma", p.getSubprograma());
                params.put("periodo", p.getPeriodo());
                break;

        }

        return params;
    }

}
