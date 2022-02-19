/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Models.SolicitudServiciosDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ricardo
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class SolicitudServiciosService extends AbstractService<SolicitudServicios> {

    private Map<String, Object> param;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SolicitudServiciosService() {
        super(SolicitudServicios.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<SolicitudServiciosDTO> findAllSolicitudGroupByServicio(Date fechaDesde, Date fechaHasta) {
        // EL 110 ID .. ES DE CATALOGO ITEM .. DIFERENTE DE ANULADOS..... 
        return em.createNativeQuery("SELECT sv.nombre , count(*) as cantidad FROM ventanilla.solicitud_servicios s\n"
                + "JOIN ventanilla.servicio_tipo st ON st.id=s.servicio_tipo JOIN ventanilla.servicio sv ON sv.id=st.servicio_id "
                + "WHERE s.fecha_creacion between ?1 and ?2 GROUP BY st.servicio_id,sv.nombre ORDER BY st.servicio_id", "SolicitudServiciosMapping")
                .setParameter(1, fechaDesde).setParameter(2, fechaHasta)
                .getResultList();
    }

    public void actualizarSolicitudPago(FinaRenLiquidacion liquidacion) {
        try {
            if (liquidacion != null && liquidacion.getId() != null && !Utils.isEmptyString(liquidacion.getCodigoVerificador())) {
                System.out.println("//Consultando solicitud servicios para el pago");
                SolicitudServicios ss = findByNamedQuery1("SolicitudServicios.findByReferenciaLiquidacionPago", liquidacion.getCodigoVerificador());
                if (ss != null && ss.getId() != null) {
                    ss.setEnObservacion(Boolean.FALSE);
                    ss.setPendientePago(Boolean.FALSE);
                    liquidacion.setCodigoVerificado(Boolean.TRUE);
                    em.merge(liquidacion);
                    edit(ss);
                    System.out.println("//Encontrado y actualizado");
                }
            }
        } catch (Exception e) {
            System.out.println("//Exception actualizarSolicitudPago " + e.getMessage());
        }
    }

}
