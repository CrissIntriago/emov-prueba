/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.transporte.Vehiculo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import java.util.Date;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class VehiculoService extends AbstractService<Vehiculo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;

    public VehiculoService() {
        super(Vehiculo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Vehiculo save(Vehiculo v) {
        if (v.getId() == null) {
            v.setFechaCre(new Date());
            v.setFechaMod(new Date());
            v.setUsuarioCre(session.getNameUser());
            v.setUsuarioMod(session.getNameUser());
            v.setEstado("AC");
            return this.create(v);
        } else {
            v.setFechaMod(new Date());
            v.setUsuarioMod(session.getNameUser());
            this.edit(v);
            return v;
        }

    }

}
