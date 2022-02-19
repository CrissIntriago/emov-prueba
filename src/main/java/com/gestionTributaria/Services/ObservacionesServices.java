/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ObservacionesServices extends AbstractService<Observaciones> {

    private static final Logger LOG = Logger.getLogger(ObservacionesServices.class.getName());
     private static final long serialVersionUID = 1L;
     @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ObservacionesServices() {
          super(Observaciones.class);
    }
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
