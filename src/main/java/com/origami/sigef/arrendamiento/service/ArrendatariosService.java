/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.service;

import com.origami.sigef.arrendamiento.entities.Arrendamiento;
import com.origami.sigef.arrendamiento.entities.Arrendatarios;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ArrendatariosService extends AbstractService<Arrendatarios> {
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ArrendatariosService() {
        super(Arrendatarios.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<Arrendatarios> getArrendatariosList(Arrendamiento arrendamiento) {
        try {
            List<Arrendatarios> resultado = (List<Arrendatarios>) em.createQuery("SELECT a FROM Arrendatarios a WHERE a.idArriendamiento = ?1 AND a.estado = TRUE ")
                    .setParameter(1, arrendamiento).getResultList();
            return resultado;
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
