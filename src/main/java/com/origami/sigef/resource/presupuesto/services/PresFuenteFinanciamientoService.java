/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.services;

import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class PresFuenteFinanciamientoService extends AbstractService<PresFuenteFinanciamiento> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PresFuenteFinanciamientoService() {
        super(PresFuenteFinanciamiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PresFuenteFinanciamiento> getFuenteFinanciamiento() {
        List<PresFuenteFinanciamiento> result = (List<PresFuenteFinanciamiento>) em.createQuery(QUERY.ALL_FUENTE_FINANCIAMIENTO)
                .getResultList();
        return result;
    }
    
    public List<PresFuenteFinanciamiento> getFteFinanTrue() {
        List<PresFuenteFinanciamiento> result = (List<PresFuenteFinanciamiento>) em.createQuery("SELECT ff FROM PresFuenteFinanciamiento ff WHERE ff.estado=true ORDER BY ff.codFuente ASC")
                .getResultList();
        return result;
    }

}
