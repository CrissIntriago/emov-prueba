/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.ProcesoServidor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProcesoServidorService extends AbstractService<ProcesoServidor> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcesoServidorService() {
        super(ProcesoServidor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ProcesoServidor findProcesoServidorByNTramite(BigInteger tramite) {
        try {
            Query query = em.createQuery("SELECT p FROM ProcesoServidor p WHERE p.nTramite = ?1")
                    .setParameter(1, tramite);
            return (ProcesoServidor) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean getLiquidaServidor(Servidor s) {
        try {
            Boolean result = (Boolean) em.createQuery("SELECT ps.liquida FROM ProcesoServidor ps WHERE ps.estadoProceso = 'COMPLETO' AND ps = ?1 ORDER BY ps.nTramite DESC")
                    .getResultStream().findFirst().orElse(null);
            return result;
        } catch (Exception e) {
            return true;
        }
    }
}
