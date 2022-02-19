/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
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
public class ContratoReservaService extends AbstractService<ContratosReservaEjecuion> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContratoReservaService() {
        super(ContratosReservaEjecuion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<ContratosReservaEjecuion> getListaContratos(SolicitudReservaCompromiso s) {

        List<ContratosReservaEjecuion> result = (List<ContratosReservaEjecuion>) getEntityManager().createQuery("SELECT c FROM ContratosReservaEjecuion c INNER JOIN c.reserva rc where rc.id= :solicitud").setParameter("solicitud", s.getId()).getResultList();
        return result;
    }

    public void geteliminarContratos(SolicitudReservaCompromiso s) {
        String hql = "delete from  ContratosReservaEjecuion c WHERE c.reserva.id= :id";
        Query query = getEntityManager().createQuery(hql)
                .setParameter("id", s.getId());

        query.executeUpdate();
    }

    public List<Adquisiciones> findContratos(SolicitudReservaCompromiso certificacionPresupuestaria) {
        return (List<Adquisiciones>) em.createQuery(QUERY.FIND_CONTRATO_RESERVA)
                .setParameter(1, certificacionPresupuestaria)
                .getResultList();
    }
}
