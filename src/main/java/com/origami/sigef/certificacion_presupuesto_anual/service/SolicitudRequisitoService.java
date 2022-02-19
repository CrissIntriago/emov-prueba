/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class SolicitudRequisitoService extends AbstractService<SolicitudRequisito> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SolicitudRequisitoService() {
        super(SolicitudRequisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<SolicitudRequisito> getRequisitosRegistrados(SolicitudReservaCompromiso solicitudReservaCompromiso) {
        List<SolicitudRequisito> resultado = (List<SolicitudRequisito>) getEntityManager().createQuery("SELECT sr FROM SolicitudRequisito sr"
                + " WHERE sr.idSolicitudReserva= :solicitudReservaCompromiso ORDER BY sr.id ASC")
                .setParameter("solicitudReservaCompromiso", solicitudReservaCompromiso)
                .getResultList();
        return resultado;
    }

    public boolean getVerificacionContabilizado(SolicitudReservaCompromiso r) {
        boolean verificado = false;
        List<DetalleSolicitudCompromiso> lista = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.certificacionesPresupuestario=:id AND d.estadoDiario NOT IN('ANULADO') AND (d.totalDebe >=0 or d.totalHaber >=0)")
                .setParameter("id", r).getResultList();

        if (!lista.isEmpty()) {
            verificado = true;

        }

        return verificado;

    }

}
