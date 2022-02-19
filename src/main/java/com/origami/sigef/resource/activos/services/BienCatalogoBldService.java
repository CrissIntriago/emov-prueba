/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BienCatalogoBldService extends AbstractService<BienCatalogoBld> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienCatalogoBldService() {
        super(BienCatalogoBld.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BienCatalogoBld getIdBienes(String bienes) {
        try {
            Query query = em.createQuery("SELECT c from BienCatalogoBld c WHERE c.idBien = :bienes and c.estado = true")
                    .setParameter("bienes", bienes);
            BienCatalogoBld exist = (BienCatalogoBld) query.getSingleResult();

            return exist;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean existenRegistros(BienCatalogoBld cat) {
        Boolean existe;
        BienCatalogoBld detalle = null;
        try {
            detalle = (BienCatalogoBld) em.createQuery("SELECT DISTINCT(b.catalogoBienes)  FROM BienesItem b  WHERE b.estado = true AND b.catalogoBienes = :cat")
                    .setParameter("cat", cat)
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
