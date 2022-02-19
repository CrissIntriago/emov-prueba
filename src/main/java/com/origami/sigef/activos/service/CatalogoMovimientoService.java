/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEc
 */
@Stateless @javax.enterprise.context.Dependent
public class CatalogoMovimientoService extends AbstractService<CatalogoMovimiento> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoMovimientoService() {
        super(CatalogoMovimiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<CatalogoMovimiento> findMovimientoIngresoInventario(String motivo, String codigo) {
        return this.findByNamedQuery("CatalogoMovimiento.findByOrigen", motivo, codigo);
    }

    public List<CatalogoMovimiento> findMovimientoIngresoEgresoInventario(String motivo, String motivo1) {
        return this.findByNamedQuery("CatalogoMovimiento.findByOrigenTodos", motivo, motivo1);
    }

}
