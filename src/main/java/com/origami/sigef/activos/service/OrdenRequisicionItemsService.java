/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.OrdenRequisicion;
import com.origami.sigef.common.entities.OrdenRequisicionItems;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless @javax.enterprise.context.Dependent
public class OrdenRequisicionItemsService extends AbstractService<OrdenRequisicionItems> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public OrdenRequisicionItemsService() {
        super(OrdenRequisicionItems.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<OrdenRequisicionItems> getOrdenRequisicion() {
        Query query = getEntityManager().createQuery("SELECT DISTINCT r FROM OrdenRequisicionItems i JOIN i.ordenRequisicion r JOIN r.servidorSolicitante s JOIN s.persona p JOIN s.distributivo d JOIN d.cargo ca");
        List<OrdenRequisicionItems> ordItems = (List<OrdenRequisicionItems>) query.getResultList();
        return ordItems;
    }

    public List<OrdenRequisicionItems> getItemsByOrden(OrdenRequisicion orden) {
        System.out.println("ORDEN ID: " + orden.getId());
        Query query = getEntityManager().createQuery("SELECT i FROM OrdenRequisicionItems i WHERE i.ordenRequisicion.id = :ordenId")
                .setParameter("ordenId", orden.getId());
        List<OrdenRequisicionItems> ordenItems = query.getResultList();
        return ordenItems;
    }

    public List<OrdenRequisicionItems> findByIdCodigo(OrdenRequisicion ord) {
        return findByNamedQuery("OrdenRequisicionItems.findByIdCodigo", ord);
    }

    public List<DetalleItem> getListItemsByOrden(OrdenRequisicion orden) {
        Query query = getEntityManager().createQuery("SELECT d FROM OrdenRequisicionItems i JOIN i.item d WHERE i.ordenRequisicion = :orden")
                .setParameter("orden", orden);
        List<DetalleItem> ordenItems = query.getResultList();
        return ordenItems;
    }
}
