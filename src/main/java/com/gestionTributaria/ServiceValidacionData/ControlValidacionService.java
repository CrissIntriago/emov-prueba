/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.ControlValidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DEVELOPER
 */
@Stateless
public class ControlValidacionService extends AbstractService<ControlValidacion> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ControlValidacionService() {
        super(ControlValidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    
    
    
}
