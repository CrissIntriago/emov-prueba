/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.EspecificacionTecnica;
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
public class EspecificacionTecnicaService extends AbstractService<EspecificacionTecnica> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EspecificacionTecnicaService() {
        super(EspecificacionTecnica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<EspecificacionTecnica> especificacionList(SolicitudOrdenCompra sol) {
        try {
            Query query = em.createQuery("SELECT e FROM EspecificacionTecnica e WHERE e.ordenCompra = ?1")
                    .setParameter(1, sol);
            List<EspecificacionTecnica> lista = (List<EspecificacionTecnica>) query.getResultList();
            return lista;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
