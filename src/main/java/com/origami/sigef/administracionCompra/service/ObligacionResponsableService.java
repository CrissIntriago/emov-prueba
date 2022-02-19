/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ObligacionResponsable;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class ObligacionResponsableService extends AbstractService<ObligacionResponsable> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ObligacionResponsableService() {
        super(ObligacionResponsable.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ObligacionResponsable> obligacionList(SolicitudOrdenCompra sol) {
        try {
            Query query = em.createQuery("SELECT o FROM ObligacionResponsable o WHERE o.ordenCompra = ?1")
                    .setParameter(1, sol);
            List<ObligacionResponsable> lista = (List<ObligacionResponsable>) query.getResultList();
            return lista;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
