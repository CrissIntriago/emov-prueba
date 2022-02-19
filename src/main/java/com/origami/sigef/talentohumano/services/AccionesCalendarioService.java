/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.AccionesCalendario;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI
 */
@Stateless @javax.enterprise.context.Dependent
public class AccionesCalendarioService extends AbstractService<AccionesCalendario> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AccionesCalendarioService() {
        super(AccionesCalendario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<AccionesCalendario> getAccionesCalendario(Date startDate, Date endDate) {
        Query query = getEntityManager().createQuery("SELECT c FROM AccionesCalendario c WHERE c.inicioDate BETWEEN ?1 AND ?2")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        List<AccionesCalendario> result = query.getResultList();
        return result;
    }

    public List<AccionesCalendario> getAccionesCalendarioBuscar(Servidor ser, Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT c FROM AccionesCalendario c WHERE c.servidor = ?1 AND c.inicioDate = ?2 AND c.finDate = ?3 AND c.estado = true")
               .setParameter(1, ser)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        List<AccionesCalendario> result = query.getResultList();
        return result;
    }
}
