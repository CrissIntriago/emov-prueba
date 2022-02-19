/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
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
public class FinaRenTipoLiquidacionService extends AbstractService<FinaRenTipoLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FinaRenTipoLiquidacionService.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenTipoLiquidacionService() {
        super(FinaRenTipoLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FinaRenTipoLiquidacion> findTipoLiquidacionAplicaCoactiva() {
        List<FinaRenTipoLiquidacion> aplicaCoactiva = new ArrayList<>();
        try {
            aplicaCoactiva = em.createQuery("select a from FinaRenTipoLiquidacion a where a.aplicaCoativa=TRUE").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer liquidaciones que aplican Coactiva", ex);
        }
        return aplicaCoactiva;
    }

    public List<FinaRenTipoLiquidacion> findAllTipoLiquidacion() {
        List<FinaRenTipoLiquidacion> aplicaCoactiva = new ArrayList<>();
        try {
            aplicaCoactiva = em.createQuery("select a from FinaRenTipoLiquidacion a WHERE a.estado=true ORDER BY a.nombreTitulo ASC").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer liquidaciones ", ex);
        }
        return aplicaCoactiva;
    }

    public List<FinaRenTipoLiquidacion> getEspeciesFindAll() {
        try {
            return (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT e FROM FinaRenTipoLiquidacion e WHERE e.especieValorada = TRUE AND e.tipo = 'ESP' ")
                    .getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
    }

    public List<FinaRenRubrosLiquidacion> obtenerRubros(Long idTipo) {
        try {
            return idTipo != null ? (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id=:idTipo AND r.estado=true")
                    .setParameter("idTipo", idTipo).getResultList()
                    : (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.estado=true").getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
