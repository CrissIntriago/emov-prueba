/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatEscritura;
import com.gestionTributaria.Entities.CatPredio;
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
public class CatEscrituraServices extends AbstractService<CatEscritura> {

    private static final Logger LOG = Logger.getLogger(CatEscrituraServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatEscrituraServices() {
        super(CatEscritura.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public CatEscritura getCatEscrituraByPredioUltima(CatPredio catPredio) {
        CatEscritura escritura = new CatEscritura();
        try {
            escritura = (CatEscritura) em.createQuery("select e from CatEscritura e where e.predio=?1").setParameter(1, catPredio.getId()).getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(CatEscrituraServices.class.getName()).log(Level.SEVERE, null, ex);
            escritura = null;
        }
        return escritura;
    }

    public List<CatEscritura> getEscriturasByPredio(CatPredio catPredio) {
        List<CatEscritura> escrituras = new ArrayList<>();
        try {
            escrituras = em.createQuery("select a from CatEscritura a where a.predio=?1 ORDER BY e.fecCre DESC").setParameter(1, catPredio.getId()).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CatEscrituraServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return escrituras;
    }
}
