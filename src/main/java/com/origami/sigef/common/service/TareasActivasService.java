/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.resource.procesos.models.TareasActivas;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless @javax.enterprise.context.Dependent
public class TareasActivasService extends AbstractService<TareasActivas> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TareasActivasService() {
        super(TareasActivas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public TareasActivas findTaskIdByNumTramite(Long numTramite) {
        try {
            TareasActivas result = (TareasActivas) getEntityManager().createQuery("SELECT t FROM TareasActivas t where t.numTramite= :numTramite")
                    .setParameter("numTramite", numTramite)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }

    }

}
