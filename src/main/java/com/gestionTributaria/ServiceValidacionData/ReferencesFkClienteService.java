/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.ReferencesFkCliente;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@Stateless
public class ReferencesFkClienteService extends AbstractService<ReferencesFkCliente> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

  

    public ReferencesFkClienteService() {
        super(ReferencesFkCliente.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    
}
