/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.arrendamiento.service;

import com.origami.sigef.arrendamiento.entities.DetalleMercado;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class DetalleMercadoService extends AbstractService<DetalleMercado> {

    private static final Logger LOG = Logger.getLogger(DetalleMercadoService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleMercadoService() {
        super(DetalleMercado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleMercado> getDetalle(Mercado mc) {
        try {
            System.out.println("mercado>>" + mc);
            return (List<DetalleMercado>) em.createQuery("SELECT d FROM DetalleMercado d WHERE d.mercado = ?1 ")
                    .setParameter(1, mc).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
