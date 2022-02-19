/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.ViewPatronElectoral;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@Stateless
public class ViewPatronElectoralService extends AbstractService<ViewPatronElectoral> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ViewPatronElectoralService() {
        super(ViewPatronElectoral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ViewPatronElectoral searchPersonaCedula(String cedula) {
        return (ViewPatronElectoral) em.createQuery("SELECT f FROM ViewPatronElectoral f WHERE f.cedula=:cedula")
                .setParameter("cedula", cedula).getResultStream().findFirst().orElse(null);
    }

    public ViewPatronElectoral searchPersonaNombresCompletos(String nombres) {
        return (ViewPatronElectoral) em.createQuery("SELECT f FROM ViewPatronElectoral f WHERE f.nombresCompleto like :nombre")
                .setParameter("nombre", nombres).getResultStream().findFirst().orElse(null);
    }

}
