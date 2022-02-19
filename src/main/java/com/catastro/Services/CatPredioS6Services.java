/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatPredioS6;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class CatPredioS6Services extends AbstractService<CatPredioS6> {

    private static final Logger LOG = Logger.getLogger(CatPredioS4Services.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatPredioS6Services() {
        super(CatPredioS6.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public CatPredioS6 guardarPredioS6(CatPredioS6 catPredioS6) {
        try {
            if (catPredioS6.getId() == null) {
                return (CatPredioS6) create(catPredioS6);
            } else {
                edit(catPredioS6);
                return catPredioS6;
            }
        } catch (Exception e) {
            Logger.getLogger(CatPredioS6Services.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
