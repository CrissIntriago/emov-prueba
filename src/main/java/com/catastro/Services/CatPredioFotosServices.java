/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatPredioFotos;
import com.gestionTributaria.Entities.CatPredio;
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
public class CatPredioFotosServices extends AbstractService<CatPredioFotos> {

    private static final Logger LOG = Logger.getLogger(CatEscrituraServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatPredioFotosServices() {
        super(CatPredioFotos.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatPredioFotos> CatPredioFotosgetFotosIdPredio(CatPredio catPredio) {
        List<CatPredioFotos> fotos = null;
        try {
            fotos = (List<CatPredioFotos>) em.createQuery("select e from FotoPredio e where e.idPredio = ?1").setParameter(1, catPredio.getId()).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CatEscrituraServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fotos;
    }

}
