/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.service;

import com.origami.sigef.ats.entities.AtsArchivo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gutya
 */
@Stateless
public class AtsArchivoService extends AbstractService<AtsArchivo> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AtsArchivoService() {
        super(AtsArchivo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<AtsArchivo> findAllAtsArchivos(Long tramite) {

        List<AtsArchivo> result = (List<AtsArchivo>) em.createQuery("SELECT c FROM AtsArchivo c  "
                + "WHERE c.tramite=:tramite ")
                .setParameter("tramite", tramite)
                .getResultList();

        return result;
    }

}
