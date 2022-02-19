/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatTipoConjunto;
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
public class CatTipoConjuntoServices extends AbstractService<CatTipoConjunto> {

    private static final Logger LOG = Logger.getLogger(CatPredioServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatTipoConjuntoServices() {
        super(CatTipoConjunto.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatTipoConjunto> getAllTipoConjunto() {
        List<CatTipoConjunto> tipoConjuntos = null;
        try {
            tipoConjuntos = em.createQuery("Select a from CatTipoConjunto as a where a.estado=true").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en CatCiudadelas getAllCiudadelas", ex);
        }
        return tipoConjuntos;
    }

}
