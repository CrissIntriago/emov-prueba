/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatCiudadela;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
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
public class CatCiudadelasService extends AbstractService<CatCiudadela> {

    private static final Logger LOG = Logger.getLogger(CatCiudadelasService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatCiudadelasService() {
        super(CatCiudadela.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatCiudadela> getAllCiudadelas() {
        List<CatCiudadela> listaCiudadelas = null;
        try {
            listaCiudadelas = em.createQuery("Select a from CatCiudadela as a where a.estado=true").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en CatCiudadelas getAllCiudadelas", ex);
        }
        return listaCiudadelas;
    }

    public List<CatCiudadela> getAllCiudadelasTodas() {
        List<CatCiudadela> listaCiudadelas = null;
        try {
            listaCiudadelas = em.createQuery("Select a from CatCiudadela as a").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en CatCiudadelas getAllCiudadelas", ex);
        }
        return listaCiudadelas;
    }

    public CatCiudadela findCiudadela(CatCiudadela idCatCiudadela) {
        CatCiudadela ciudadela = new CatCiudadela();
        try {
            ciudadela = (CatCiudadela) em.createQuery("select a from CatCiudadela a where a.id=?1").setParameter(1, idCatCiudadela.getId()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en CatCiudadelas getAllCiudadelas", ex);
        }
        return ciudadela;
    }
}
