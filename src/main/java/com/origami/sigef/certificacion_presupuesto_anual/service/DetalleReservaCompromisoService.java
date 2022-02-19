/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMIEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class DetalleReservaCompromisoService extends AbstractService<DetalleSolicitudCompromiso> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleReservaCompromisoService() {
        super(DetalleSolicitudCompromiso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;

    }

    public void getRemove(DetalleSolicitudCompromiso detalle) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso WHERE id= ?1")
                .setParameter(1, detalle);
        int aux = query.executeUpdate();
    }

    public int removeDetReservaUpdate(SolicitudReservaCompromiso s) {
        try {
            List<DetalleSolicitudCompromiso> ds = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT ds from DetalleSolicitudCompromiso ds where ds.solicitud=:solicitud")
                    .setParameter("solicitud", s).getResultList();
            if (!ds.isEmpty()) {
                ds.forEach(item -> {remove(item);});
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
