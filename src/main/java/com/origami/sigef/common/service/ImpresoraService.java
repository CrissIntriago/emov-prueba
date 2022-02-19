/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Impresora;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class ImpresoraService extends AbstractService<Impresora> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ImpresoraService() {
        super(Impresora.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
