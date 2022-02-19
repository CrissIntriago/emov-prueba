/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.RenDepartamentoDetalleTitulos;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RenDepartamentoDetalleTitulosServices extends AbstractService<RenDepartamentoDetalleTitulos> {

//    @Inject
//    private ManagerService services;
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RenDepartamentoDetalleTitulosServices() {
        super(RenDepartamentoDetalleTitulos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
