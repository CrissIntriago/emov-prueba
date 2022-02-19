/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless @javax.enterprise.context.Dependent
public class CargoService extends AbstractService<Cargo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CargoService() {
        super(Cargo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    private String getQueryHQLExisteCargo(Cargo c) {
        String query = null;
        if (c.getNombreCargo() != null) {
            return "SELECT p FROM Cargo p WHERE p.nombreCargo = :nombreCargo AND p.estado = TRUE";
        }
        return query;
    }

    public Cargo existeCargo(Cargo c) {
        String queryString = getQueryHQLExisteCargo(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<Cargo> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(Cargo c) {

        Map<String, Object> params = new HashMap<>();
        if (c.getNombreCargo() != null) {
            params.put("nombreCargo", c.getNombreCargo());
        }
        return params;
    }

}
