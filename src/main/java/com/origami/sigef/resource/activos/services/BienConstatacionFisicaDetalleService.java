/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.services;

import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.activos.entities.BienConstatacionFisica;
import com.origami.sigef.resource.activos.entities.BienConstatacionFisicaDetalle;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Sandra Arroba
 */
@Stateless
@javax.enterprise.context.Dependent
public class BienConstatacionFisicaDetalleService extends AbstractService<BienConstatacionFisicaDetalle> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienConstatacionFisicaDetalleService() {
        super(BienConstatacionFisicaDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<BienConstatacionFisicaDetalle> getListConstatacionDetalleBienesByConstatacion(BienConstatacionFisica constatacion) {
        List<BienConstatacionFisicaDetalle> result = (List<BienConstatacionFisicaDetalle>) getEntityManager().createQuery("SELECT deb FROM BienConstatacionFisicaDetalle deb WHERE deb.constatacionFisica = :constatacion")
                .setParameter("constatacion", constatacion)
                .getResultList();
        return result;
    }

    public Boolean existenRegistros(BienConstatacionFisicaDetalle detalle) {
        Boolean existe;
        BienesItem bien = null;
        try {
            bien = (BienesItem) getEntityManager().createQuery("SELECT d FROM BienConstatacionFisicaDetalle d INNER JOIN d.bienesItem b INNER JOIN d.constatacionFisica c WHERE c.estado = true AND d.constatacionFisica = ?1 and d.bienesItem = ?2")
                    .setParameter(1, detalle.getConstatacionFisica()).setParameter(2, detalle.getBienesItem())
                    .getSingleResult();
        } catch (NoResultException e) {
            detalle = null;
        }
        if (detalle != null) {
            existe = Boolean.TRUE;
            return existe;
        }
        existe = Boolean.FALSE;
        return existe;
    }
}
