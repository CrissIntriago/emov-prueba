/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.Vacaciones;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class VacacionesService extends AbstractService<Vacaciones> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public VacacionesService() {
        super(Vacaciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Vacaciones> getVacacionesXservidor(Servidor ser) {
        try {
            Query query = em.createQuery("SELECT v FROM Vacaciones v  WHERE v.estado=true AND v.servidor = ?1 ")
                    .setParameter(1, ser);
            List<Vacaciones> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
}
