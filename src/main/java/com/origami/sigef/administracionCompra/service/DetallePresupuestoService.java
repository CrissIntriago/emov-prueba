/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetallePresupuesto;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class DetallePresupuestoService extends AbstractService<DetallePresupuesto> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetallePresupuestoService() {
        super(DetallePresupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
