/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLocalComercial;
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
public class FinaRenLocalComercialService extends AbstractService<FinaRenLocalComercial> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenLocalComercialService() {
        super(FinaRenLocalComercial.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FinaRenLocalComercial> findByNumPredio(Long numPredio, String numLocal) {
        List<FinaRenLocalComercial> localesComerciales = new ArrayList<>();
        try {
            localesComerciales = (List<FinaRenLocalComercial>) em.createQuery("select a from FinaRenLocalComercial a where a.predio=?1 and a.numLocal=?2").setParameter(1, numPredio).setParameter(2, numLocal).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(FinaRenLocalComercial.class.getName()).log(Level.SEVERE, "Error al buscar la liquidacion en Liquidacion Convenio", ex);
        }
        return localesComerciales;
    }
}
