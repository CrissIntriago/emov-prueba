/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
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
public class FnExoneracionLiquidacionService extends AbstractService<FnExoneracionLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FnExoneracionLiquidacionService.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnExoneracionLiquidacionService() {
        super(FnExoneracionLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnExoneracionLiquidacion> findByLiquidacion(FinaRenLiquidacion liquidacion) {
        List<FnExoneracionLiquidacion> listaExoneraciones = null;
        try {
            listaExoneraciones = em.createQuery("select a from FnExoneracionLiquidacion a where a.liquidacionOriginal.id=?1").setParameter(1, liquidacion.getId()).getResultList();

        } catch (Exception ex) {
            Logger.getLogger(FnExoneracionLiquidacionService.class.getName()).log(Level.SEVERE, null, ex);
            return listaExoneraciones;
        }
        return listaExoneraciones;
    }

    public List<FinaRenLiquidacion> traerLiquidacionesSolicitud(FnSolicitudExoneracion exoneracion) {
        List<FinaRenLiquidacion> liquidaciones = new ArrayList<>();
        try {
            liquidaciones = em.createQuery("select a.liquidacionOriginal from FnExoneracionLiquidacion a where a.exoneracion.id =?1").setParameter(1, exoneracion.getId()).getResultList();

        } catch (Exception ex) {
            Logger.getLogger(FnExoneracionLiquidacionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return liquidaciones;
    }

    public List<FnExoneracionLiquidacion> traerSolicitudesLiquidacion(FnSolicitudExoneracion exoneracion) {
        List<FnExoneracionLiquidacion> liquidacionesSolicitud = new ArrayList<>();
        try {
            liquidacionesSolicitud = em.createQuery("select a from FnExoneracionLiquidacion a where a.exoneracion.id =?1").setParameter(1, exoneracion.getId()).getResultList();

        } catch (Exception ex) {
            Logger.getLogger(FnExoneracionLiquidacionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return liquidacionesSolicitud;
    }

    public FnExoneracionLiquidacion traerSolicitudesLiquidacionXExoneracionLiquidacion(FnSolicitudExoneracion exoneracion, FinaRenLiquidacion liquidacion) {
        List<FnExoneracionLiquidacion> liquidacionesSolicitud = new ArrayList<>();
        FnExoneracionLiquidacion liquidacionSolicitud = new FnExoneracionLiquidacion();
        try {
            liquidacionesSolicitud = em.createQuery("select a from FnExoneracionLiquidacion a where a.exoneracion.id =?1 and a.liquidacionOriginal.id=?2").setParameter(1, exoneracion.getId()).setParameter(2, liquidacion.getId()).getResultList();

            if (!liquidacionesSolicitud.isEmpty()) {
                liquidacionSolicitud = liquidacionesSolicitud.get(0);
            } else {
                liquidacionSolicitud = null;
            }

        } catch (Exception ex) {
            Logger.getLogger(FnExoneracionLiquidacionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return liquidacionSolicitud;
    }
}
