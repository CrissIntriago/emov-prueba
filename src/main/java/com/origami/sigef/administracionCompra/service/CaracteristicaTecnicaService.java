/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CaracteristicaTecnica;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class CaracteristicaTecnicaService extends AbstractService<CaracteristicaTecnica> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CaracteristicaTecnicaService() {
        super(CaracteristicaTecnica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CaracteristicaTecnica> caracteristicaList(SolicitudOrdenCompra sol) {
        try {
            Query query = em.createQuery("SELECT c FROM CaracteristicaTecnica c WHERE c.ordenCompra = ?1")
                    .setParameter(1, sol);
            List<CaracteristicaTecnica> lista = (List<CaracteristicaTecnica>) query.getResultList();
            return lista;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
