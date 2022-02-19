/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.arrendamiento.entities.Arrendamiento;
import com.origami.sigef.arrendamiento.entities.Locales;
import com.origami.sigef.common.config.CONFIG;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class LocalesService extends AbstractService<Locales>{
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LocalesService() {
        super(Locales.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean getRelacion(Locales locales) {
        List<Arrendamiento> resultado = (List<Arrendamiento>) em.createQuery("SELECT a FROM Arrendamiento a WHERE a.local=:idLocal AND a.estado=TRUE")
                .setParameter("idLocal", locales)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }
}
