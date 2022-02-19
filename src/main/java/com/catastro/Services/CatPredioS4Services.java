/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatPredioS4;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class CatPredioS4Services extends AbstractService<CatPredioS4> {

    private static final Logger LOG = Logger.getLogger(CatPredioS4Services.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatPredioS4Services() {
        super(CatPredioS4.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public CatPredioS4 guardarPredioS4(CatPredioS4 s4) {
        Map<String, Object> paramt = new HashMap<>();
        CatPredioS4 ss4 = null;
        CatPredioS4 sss4 = null;
        try {
            paramt.put("idPredio", s4.getPredio().getId());
            ss4 = em.find(CatPredioS4.class, paramt);
            if (ss4 != null) {
                s4.setId(ss4.getId());
            }
            if (s4.getId() != null) {
                edit(s4);
                sss4 = s4;
            } else {
                sss4 = (CatPredioS4) create(s4);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return sss4;
    }
}
