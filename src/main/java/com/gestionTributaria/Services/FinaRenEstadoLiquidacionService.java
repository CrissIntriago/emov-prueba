/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
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
public class FinaRenEstadoLiquidacionService extends AbstractService<FinaRenEstadoLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FinaRenEstadoLiquidacionService.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenEstadoLiquidacionService() {
        super(FinaRenEstadoLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FinaRenEstadoLiquidacion findByParameter(String parametro) {
        FinaRenEstadoLiquidacion estadoLiquidacion = new FinaRenEstadoLiquidacion();
        try {
            estadoLiquidacion = (FinaRenEstadoLiquidacion) em.createQuery("select f from FinaRenEstadoLiquidacion as f where f.descripcion like ?1").setParameter(1, parametro).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error findByParameter service ", ex);
            return estadoLiquidacion;
        }
        return estadoLiquidacion;
    }

    public List<FinaRenEstadoLiquidacion> finAll() {
        List<FinaRenEstadoLiquidacion> estadoLiquidaciones = new ArrayList<>();
        try {
            estadoLiquidaciones = em.createQuery("Select a from FinaRenEstadoLiquidacion as a").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error finAll ", ex);
            return estadoLiquidaciones;
        }
        return estadoLiquidaciones;
    }

    public FinaRenEstadoLiquidacion findByCodigo(String code) {
        FinaRenEstadoLiquidacion estado = new FinaRenEstadoLiquidacion();
        List<FinaRenEstadoLiquidacion> estados = new ArrayList<>();
        try {
            estados = (List<FinaRenEstadoLiquidacion>) em.createQuery("select a from FinaRenEstadoLiquidacion a where a.codigo like ?1 ").setParameter(1, code).getResultList();
            if (!estados.isEmpty()) {
                estado = estados.get(0);
            } else {
                estado = null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar estado por codigo ", ex);
        }
        return estado;
    }
}
