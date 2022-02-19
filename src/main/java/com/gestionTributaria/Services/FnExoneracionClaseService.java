/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnExoneracionClase;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FnExoneracionClaseService extends AbstractService<FnExoneracionClase> {

    private static final Logger LOG = Logger.getLogger(FnExoneracionClaseService.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnExoneracionClaseService() {
        super(FnExoneracionClase.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnExoneracionClase> findAllExoneraciones() {
        List<FnExoneracionClase> exoneraciones = new ArrayList<>();
        try {
            return exoneraciones = em.createQuery("Select a from FnExoneracionClase a where a.estado=true").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return exoneraciones;
        }
    }

    public List<FnExoneracionClase> findAllClaseExoneracion() {
        List<FnExoneracionClase> exoneraciones = new ArrayList<>();
        try {
            return exoneraciones = em.createQuery("Select a from FnExoneracionClase a where a.id not in (31,30,2,3,5,6)").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return exoneraciones;
        }
    }
}
