/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.entities.DetalleConstFisicaInventario;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class DetalleConstFisicaService extends AbstractService<DetalleConstFisicaInventario> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleConstFisicaService() {
        super(DetalleConstFisicaInventario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<DetalleConstFisicaInventario> getListDetalleItemCons(ConstatacionFisicaInventario cons) {
//        System.out.println("services de constatacion " + cons);
        return findByNamedQuery("DetalleConstFisicaInventario.getListDetalleItemCons", cons);
    }
}
