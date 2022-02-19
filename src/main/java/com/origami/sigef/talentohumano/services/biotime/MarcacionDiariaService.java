/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services.biotime;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.view.biotime.MarcacionDiaria;
import com.origami.sigef.common.service.AbstractService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class MarcacionDiariaService extends AbstractService<MarcacionDiaria> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_BIOTIME)
    private EntityManager em;

    public MarcacionDiariaService() {
        super(MarcacionDiaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
