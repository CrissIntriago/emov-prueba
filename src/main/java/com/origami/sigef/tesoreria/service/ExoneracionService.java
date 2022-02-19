/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.Exoneracion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless @javax.enterprise.context.Dependent
public class ExoneracionService extends AbstractService<Exoneracion> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ExoneracionService() {
        super(Exoneracion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Exoneracion> findAllExoneracion() {
        List<Exoneracion> result = (List<Exoneracion>) getEntityManager().createQuery("SELECT c FROM Exoneracion c "
                + " WHERE c.estado = true ")
                .getResultList();
        return result;
    }

}
