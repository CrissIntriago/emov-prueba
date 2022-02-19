/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Erwin
 */
@Stateless @javax.enterprise.context.Dependent
public class ConstatacionFisicaInvService extends AbstractService<ConstatacionFisicaInventario> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ConstatacionFisicaInvService() {
        super(ConstatacionFisicaInventario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Long getOrderConstatacion() {
        try {
            Long result = findByNamedQuery1("ConstatacionFisicaInventario.findByOrdenConsta");
            if (result == null) {
                result = 1L;
            }
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ConstatacionFisicaInventario> getAllConstataciones() {
        return this.findByNamedQuery("ConstatacionFisicaInventario.findAll");
    }

}
