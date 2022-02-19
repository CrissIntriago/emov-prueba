/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Service;

import com.asgard.Entity.*;
import com.gestionTributaria.Comisaria.Entities.ComisariaRegistros;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.SolicitudServicios;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@Stateless
@javax.enterprise.context.Dependent
public class ComisariaServices extends AbstractService<ComisariaRegistros> {

    private static final Logger LOG = Logger.getLogger(ComisariaServices.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ComisariaServices() {
        super(ComisariaRegistros.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long numSolar() {
        BigInteger res = (BigInteger) em.createNativeQuery("SELECT nextval('comisaria.no_solar')").getResultStream().findFirst().orElse(BigInteger.ZERO);
        return res.longValue();
    }

    public Long numPermiso() {
        BigInteger res = (BigInteger) em.createNativeQuery("SELECT nextval('comisaria.no_permiso')").getResultStream().findFirst().orElse(BigInteger.ZERO);
        return res.longValue();
    }

    public List<FinaRenLiquidacion> getListLiqudiacionesComisaria(Long estado, Long[] items) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT f from FinaRenLiquidacion f where f.estadoLiquidacion.id=:estado and f.tipoLiquidacion.id in (:items)")
                .setParameter("estado", estado).setParameter("items", items).getResultList();
    }

    public List<ServicioRequisito> getRequisitosTrmaitesServices(Long tramite) {

        Long tipoContribuyente = 0L;
        List<ServicioRequisito> result;
        SolicitudServicios solicitud = (SolicitudServicios) em.createQuery("SELECT s from SolicitudServicios s where s.tramite.id=:idTramite")
                .setParameter("idTramite", tramite).getResultStream().findFirst().orElse(null);

        if (solicitud != null) {

            char dar = solicitud.getTipoContribuyente().toUpperCase().charAt(0);
            if (String.valueOf(dar).equals("N")) {
                tipoContribuyente = 1L;
            } else if (String.valueOf(dar).equals("J")) {
                tipoContribuyente = 2L;
            } else {
                tipoContribuyente = 3L;
            }

            result = (List<ServicioRequisito>) em.createQuery("SELECT s from ServicioRequisito s where s.servicioTipo.servicio.id=:servicio and s.servicioTipo.tipoContribuyentes.id=:tipo ORDER BY s.posicion ASC")
                    .setParameter("servicio", solicitud.getServicioTipo().getServicio().getId()).setParameter("tipo", tipoContribuyente).getResultList();

            return result;

        } else {
            return null;
        }

    }

    public SolicitudServicios getSolcitud(Long tramite) {

        List<SolicitudServicios> solicitudes = (List<SolicitudServicios>) em.createQuery("SELECT s from SolicitudServicios s where s.tramite.id=:idTramite")
                .setParameter("idTramite", tramite).getResultList();
        
        if (solicitudes != null && !solicitudes.isEmpty()) {
            return solicitudes.get(0);
        }

        return null;

    }

}
