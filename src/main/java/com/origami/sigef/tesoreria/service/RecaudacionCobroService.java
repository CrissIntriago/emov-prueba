/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RecaudacionCobro;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class RecaudacionCobroService extends AbstractService<RecaudacionCobro> {
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public RecaudacionCobroService() {
        super(RecaudacionCobro.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
