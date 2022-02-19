/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.ViewReportes;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class ViewReporteServices extends AbstractService<ViewReportes> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ViewReporteServices() {
        super(ViewReportes.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
