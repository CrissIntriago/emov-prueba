/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless @javax.enterprise.context.Dependent
public class detalleRegistroService extends AbstractService<DetalleRegistro> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public detalleRegistroService() {
        super(DetalleRegistro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public DetalleRegistro getDetalleRegistroByServidor(Servidor s) {
        try {
            System.out.println("D: "+s);
            Query query = getEntityManager().createQuery("SELECT d FROM DetalleRegistro d WHERE d.servidor = ?1 AND d.estado = TRUE")
                    .setParameter(1, s);
            DetalleRegistro detalle = (DetalleRegistro) query.getSingleResult();
            return detalle;
        } catch (NoResultException e) {
            return null;
        }
    }
}
