/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Entities.FnSolicitudTipoLiquidacionExoneracion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FnSolicitudExoneracionServices extends AbstractService<FnSolicitudExoneracion> {

    private static final Logger LOG = Logger.getLogger(FnSolicitudExoneracionServices.class.getName());
    @Inject
    private FnSolicitudTipoLiquidacionExoneracionService fnSolicitudTipoLiquidacionExoneracionService;
    @Inject
    private FnSolicitudExoneracionPrediosService exoneracionPrediosServices;
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnSolicitudExoneracionServices() {
        super(FnSolicitudExoneracion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FnSolicitudExoneracion registarDatoSolicitudExoneracion(FnSolicitudExoneracion solicitud, List<FnSolicitudTipoLiquidacionExoneracion> tipoLiquidacionesExoneracion, List<FnSolicitudExoneracionPredios> prediosSolicitud) {
        try {
            System.out.println("tipo liquidacion--> " + tipoLiquidacionesExoneracion);
            System.out.println("predio solicitud**************************************> " + prediosSolicitud);
            if (tipoLiquidacionesExoneracion != null && !tipoLiquidacionesExoneracion.isEmpty()) {
                for (FnSolicitudTipoLiquidacionExoneracion tipoLiquidacionesExoneracion1 : tipoLiquidacionesExoneracion) {
                    tipoLiquidacionesExoneracion1.setSolicitudExoneracion(solicitud);
                    if (tipoLiquidacionesExoneracion1.getId() == null) {
                        fnSolicitudTipoLiquidacionExoneracionService.create(tipoLiquidacionesExoneracion1);
                    } else {
                        fnSolicitudTipoLiquidacionExoneracionService.edit(tipoLiquidacionesExoneracion1);
                    }
                }
            }
            System.out.println("solicitud--> " + prediosSolicitud);
            if (prediosSolicitud != null && !prediosSolicitud.isEmpty()) {
                for (FnSolicitudExoneracionPredios prediosSolicitud1 : prediosSolicitud) {
                    prediosSolicitud1.setSolicitudExoneracion(solicitud);
                    System.out.println("EL DATO QUE ENTRA CON EL id" + prediosSolicitud1);
                    if (prediosSolicitud1.getId() == null) {
                        exoneracionPrediosServices.create(prediosSolicitud1);
                    } else {
                        exoneracionPrediosServices.edit(prediosSolicitud1);
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "DATOS SOLICITUD EXONERACION", e);
        }
        return solicitud;
    }

    public FnSolicitudExoneracion findByTramiteSolicitudExoneracion(Long idTramite) {
        List<FnSolicitudExoneracion> solicitudes = new ArrayList<>();
        FnSolicitudExoneracion solicitud = new FnSolicitudExoneracion();
        try {
            solicitudes = (List<FnSolicitudExoneracion>) em.createQuery("select a from FnSolicitudExoneracion a where a.tramite=?1 ").setParameter(1, BigInteger.valueOf(idTramite)).getResultList();
            if (solicitudes.size() > 0) {
                solicitud = solicitudes.get(0);
            } else {
                solicitud = null;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se encontro la solicitud con el numero del tramite", e);
        }
        return solicitud;
    }

}
