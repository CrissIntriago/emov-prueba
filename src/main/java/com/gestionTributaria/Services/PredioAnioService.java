/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.PredioAnio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
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
public class PredioAnioService extends AbstractService<PredioAnio> {

    private static final Logger LOG = Logger.getLogger(PredioAnioService.class.getName());
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public PredioAnioService() {
        super(PredioAnio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public PredioAnio findByAnioPredio(Integer anio, CatPredio predio) {
        ArrayList<PredioAnio> predioAnio = new ArrayList<>();
        PredioAnio avaluo = new PredioAnio();
        try {
            predioAnio = (ArrayList<PredioAnio>) em.createQuery("select a from PredioAnio a where a.anio=?1 and a.idPredio.id=?2").setParameter(1, anio.toString()).setParameter(2, predio.getId()).getResultList();
            if (predioAnio.isEmpty()) {
                avaluo = null;
            } else {
                avaluo = predioAnio.get(0);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error al traer predio anio", ex);
        }
        return avaluo;
    }
}
