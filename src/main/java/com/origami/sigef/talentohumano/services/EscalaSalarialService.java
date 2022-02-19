/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless @javax.enterprise.context.Dependent
public class EscalaSalarialService extends AbstractService<EscalaSalarial> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EscalaSalarialService() {
        super(EscalaSalarial.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Distributivo> getDistributivoByEscalaSalarial(EscalaSalarial e) {
        Query query = getEntityManager().createQuery("SELECT d FROM Distributivo d WHERE d.escalaSalarial = ?1")
                .setParameter(1, e);
        List<Distributivo> result = query.getResultList();
        return result;
    }
//        private String getQueryHQLMismoId(EscalaSalarial e) {
//        String query = null;
//        if (e.getId() != null) {
//            return "SELECT d FROM distributivo d where d.escala_salarial =  ?1";
//        }
//        return query;
//    }

}
