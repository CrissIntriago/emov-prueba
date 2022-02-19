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
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RenRubroLiquidacionService extends AbstractService<FinaRenRubrosLiquidacion> {
    
    private static final Logger LOG = Logger.getLogger(RenRubroLiquidacionService.class.getName());
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public RenRubroLiquidacionService() {
        super(FinaRenRubrosLiquidacion.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public String getDescripcionRubro(Long idRubro) {
        try {
            Query query = em.createQuery("SELECT r.descripcion FROM FinaRenRubrosLiquidacion r WHERE r.id=:idRubro")
                    .setParameter("idRubro", idRubro);
            return (String) query.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public List<FinaRenRubrosLiquidacion> findAllNotificaciones(Long idTipoLiquidacion) {
        List<FinaRenRubrosLiquidacion> tipoNotificaiones = new ArrayList<>();
        try {
            tipoNotificaiones = em.createQuery("Select a from FinaRenRubrosLiquidacion a where a.descripcion like ?1 and a.tipoLiquidacion.id=?2").setParameter(1, "%CITACION%").setParameter(2, idTipoLiquidacion).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en traer Notificaions: ", ex);
        }
        return tipoNotificaiones;
    }
    
    public List<FinaRenRubrosLiquidacion> getRubrosBytipoLiquidacion(FinaRenTipoLiquidacion tipo) {
        try {
            return (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.estado = TRUE AND r.tipoLiquidacion = ?1 ")
                    .setParameter(1, tipo).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public FinaRenRubrosLiquidacion FindByDescripcion(String descripcion) {
        List<FinaRenRubrosLiquidacion> rubro = new ArrayList<>();
        try {
            rubro = (List<FinaRenRubrosLiquidacion>) em.createQuery("Select a from FinaRenRubrosLiquidacion a where a.descripcion like ?1 and a.estado=true").setParameter(1, descripcion).getResultList();
            if (rubro.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el rubro: ", ex);
        }
        return rubro.get(0);
    }
}
