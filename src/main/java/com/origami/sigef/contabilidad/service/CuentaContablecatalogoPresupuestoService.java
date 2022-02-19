/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEC
 */
@Stateless @javax.enterprise.context.Dependent
public class CuentaContablecatalogoPresupuestoService extends AbstractService<CuentaContablecatalogoPresupuesto> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuentaContablecatalogoPresupuestoService() {
        super(CuentaContablecatalogoPresupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<CuentaContablecatalogoPresupuesto> getCatalogoPresupuesto(long catalogo) {
        //String sql = "select * from cuentacontable_catalogopresupuesto cc where cc.cuenta_contable = ?1";
        List<CuentaContablecatalogoPresupuesto> result = (List<CuentaContablecatalogoPresupuesto>) getEntityManager().createQuery("SELECT cc FROM CuentaContablecatalogoPresupuesto cc WHERE cc.cuentaContable.id = ?1")
                .setParameter(1, catalogo)
                .getResultList();
        return result;
    }
}
