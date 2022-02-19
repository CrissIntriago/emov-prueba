/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class SolicitudRequisitoServices extends AbstractService<SolicitudRequisito> {

    private static final Logger LOG = Logger.getLogger(SolicitudRequisitoServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SolicitudRequisitoServices() {
        super(SolicitudRequisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<SolicitudRequisito> getRequisitosRegistrados(SolicitudReservaCompromiso solicitudReservaCompromiso) {
        List<SolicitudRequisito> resultado = (List<SolicitudRequisito>) getEntityManager().createQuery("SELECT sr FROM SolicitudRequisito sr"
                + " WHERE sr.idSolicitudReserva=: solicitudReservaCompromiso ORDER BY sr.id ASC")
                .setParameter("solicitudReservaCompromiso", solicitudReservaCompromiso)
                .getResultList();
        return resultado;
    }
}
