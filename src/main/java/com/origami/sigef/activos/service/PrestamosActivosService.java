/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.PrestamosActivos;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class PrestamosActivosService extends AbstractService< PrestamosActivos> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PrestamosActivosService() {
        super(PrestamosActivos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PrestamosActivos> getComodatoByFechaDevolucionComodato() {
        try {
            return (List<PrestamosActivos>) em.createQuery("SELECT p FROM PrestamosActivos p WHERE p.fechaDevolucionComodato <= CURRENT_TIMESTAMP AND p.motivo = 'COMODATO' AND p.estado = true AND p.estadoPrestamo = false ")
                  .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
