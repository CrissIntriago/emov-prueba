/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatParroquia;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Canton;
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
public class CatParroquiaService extends AbstractService<CatParroquia> {

    private static final Logger LOG = Logger.getLogger(CatPredioPropietarioService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public CatParroquiaService() {
        super(CatParroquia.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatParroquia> findAllParroquiaByCanton(Canton canton) {
        List<CatParroquia> parroquias = new ArrayList<>();
        try {
            parroquias = em.createQuery("select a from CatParroquia a where a.idCanton.id=?1").setParameter(1, canton.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "findAllParroquiaByCanton", ex);
        }
        return parroquias;
    }
}
