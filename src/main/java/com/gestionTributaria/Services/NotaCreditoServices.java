/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.NotasCredito;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class NotaCreditoServices extends AbstractService<NotasCredito> {

    private static final Logger LOG = Logger.getLogger(NotaCreditoServices.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public NotaCreditoServices() {
        super(NotasCredito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<NotasCredito> getNotasCreditoList() {
        try {
            return (List<NotasCredito>) em.createQuery("SELECT n FROM NotasCredito n WHERE n.saldo > 0.00 AND n.estado = 1")
                    .getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
