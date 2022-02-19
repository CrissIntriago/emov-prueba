/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@Stateless @javax.enterprise.context.Dependent
public class FuenteFinanciamientoService extends AbstractService<FuenteFinanciamiento> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FuenteFinanciamientoService() {
        super(FuenteFinanciamiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<FuenteFinanciamiento> deleteFechaCaducidad(FuenteFinanciamiento fecha) {
        String sql = "SELECT p FROM PlanProgramatico p WHERE p.fechaCaducidad =" + fecha.getFechaCaducidad() + "AND p.estado = true";
        Query query = getEntityManager().createQuery(sql);
        List<FuenteFinanciamiento> result = query.getResultList();
        if (result != null) {
            if (!result.isEmpty()) {
                return (List<FuenteFinanciamiento>) result.get(0);
            }
        }
        return null;
    }
}
