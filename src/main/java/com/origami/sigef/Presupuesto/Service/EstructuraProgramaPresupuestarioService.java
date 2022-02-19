/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.EstructuraProgramaPresupuestario;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless @javax.enterprise.context.Dependent
public class EstructuraProgramaPresupuestarioService extends AbstractService<EstructuraProgramaPresupuestario> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EstructuraProgramaPresupuestarioService() {
        super(EstructuraProgramaPresupuestario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Short getMaxValueForChild(EstructuraProgramaPresupuestario p, boolean funcion) {

        Short val = 0;

        if (funcion) {
            val = (Short) getEntityManager().createQuery("SELECT MAX(p.funcion) FROM EstructuraProgramaPresupuestario p WHERE p.estado = :estado")
                    .setParameter("estado", p.isEstado())
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

    private String getQueryHQLMax(EstructuraProgramaPresupuestario p) {
        String query = null;
        switch (p.getNivel().getOrden()) {
            case 1:
                return "SELECT MAX(p.programa) FROM EstructuraProgramaPresupuestario p WHERE p.funcion = :funcion AND p.periodo = :periodo AND p.estado = TRUE";
            case 2:
                return "SELECT MAX(p.subprograma) FROM EstructuraProgramaPresupuestario p WHERE p.funcion = :funcion AND p.programa = :programa AND p.periodo = :periodo AND p.estado = TRUE";
        }
        return query;
    }

    private String getQueryHQLExisteCuenta(EstructuraProgramaPresupuestario p) {
        String query = null;
        switch (p.getNivel().getOrden()) {
            case 1:
                return "SELECT p FROM EstructuraProgramaPresupuestario p WHERE p.funcion = :funcion AND p.periodo = :periodo AND p.estado = true";
            case 2:
                return "SELECT p FROM EstructuraProgramaPresupuestario p WHERE p.funcion = :funcion AND p.programa = :programa AND p.periodo = :periodo AND p.estado = true";
            case 3:
                return "SELECT p FROM EstructuraProgramaPresupuestario p WHERE p.funcion = :funcion AND p.programa = :programa AND p.subprograma = :subprograma AND p.periodo = :periodo AND p.estado = true";

        }
        return query;
    }

    public List<EstructuraProgramaPresupuestario> deleteFechaCaducidad(EstructuraProgramaPresupuestario fecha) {
        String sql = "SELECT p FROM EstructuraProgramaPresupuestario p WHERE p.fechaCaducidad =" + fecha.getFechaCaducidad() + "AND p.estado = true";
        Query query = getEntityManager().createQuery(sql);
        List<EstructuraProgramaPresupuestario> result = query.getResultList();
        if (result != null) {
            if (!result.isEmpty()) {
                return (List<EstructuraProgramaPresupuestario>) result.get(0);
            }
        }
        return null;
    }

    public List<EstructuraProgramaPresupuestario> getHijosByPadre(EstructuraProgramaPresupuestario padre) {
        return findByNamedQuery("EstructuraProgramaPresupuestario.findByPadre", new Object[]{padre.getId()});
    }

    public EstructuraProgramaPresupuestario existeCuenta(EstructuraProgramaPresupuestario p) {
        String queryString = getQueryHQLExisteCuenta(p);

        Map<String, Object> params = getParameter(p);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<EstructuraProgramaPresupuestario> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(EstructuraProgramaPresupuestario p) {

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

    public Boolean consultarExitenciaMasterCatalogo(Short periodo) {
        Long result = (Long) getEntityManager().createQuery("SELECT COUNT(mc) FROM MasterCatalogo mc  INNER JOIN mc.tipo tp WHERE mc.anio = :periodo AND tp.codigo='CP'")
                .setParameter("periodo", periodo)
                .getSingleResult();
        return result > 0;
    }

}
