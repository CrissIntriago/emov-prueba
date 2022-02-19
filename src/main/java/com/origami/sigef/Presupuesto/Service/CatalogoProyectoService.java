/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.CatalogoProyecto;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless @javax.enterprise.context.Dependent
public class CatalogoProyectoService extends AbstractService<CatalogoProyecto> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoProyectoService() {
        super(CatalogoProyecto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Boolean consultarExitenciaMasterCatalogo(Short periodo) {
        Long result = (Long) getEntityManager().createQuery("SELECT COUNT(mc) FROM MasterCatalogo mc  INNER JOIN mc.tipo tp WHERE mc.anio = :periodo AND tp.codigo='CP'")
                .setParameter("periodo", periodo)
                .getSingleResult();
        return result > 0;
    }
}
