/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.RegimenLaboral;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEC
 */
@Stateless @javax.enterprise.context.Dependent
public class RegimenLaboralService extends AbstractService<RegimenLaboral> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RegimenLaboralService() {
        super(RegimenLaboral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

//    public List<RegimenLaboral> filtroRegimen(String[] items)
//    {
//    List<RegimenLaboral> listaRegimen=(List<RegimenLaboral>) getEntityManager().createQuery("SELECT r FROM RegimenLaboral r WHERE r.codigo IN (:items)").setParameter("items", items).getResultList();
//    return listaRegimen;
//    }
}
