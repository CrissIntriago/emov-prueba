/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class MercadoService extends AbstractService<Mercado> {

    private static final Logger LOG = Logger.getLogger(MercadoService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MercadoService() {
        super(Mercado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Mercado> getMercadosActivos() {
        try {
            return (List<Mercado>) em.createQuery("SELECT m FROM Mercado m WHERE m.estado = TRUE").getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

}
