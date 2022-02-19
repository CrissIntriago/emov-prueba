/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Service;

import com.origami.sigef.Teletrbajo.Entity.HerramientasUtilizadas;
import com.origami.sigef.Teletrbajo.Entity.Teletrabajo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DEVELOPER
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class HerramientasUtilizadasService extends AbstractService<HerramientasUtilizadas> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public HerramientasUtilizadasService() {
        super(HerramientasUtilizadas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
     public void eliminar(Teletrabajo t) {
         Query query = em.createQuery("DELETE FROM HerramientasUtilizadas d WHERE d.teletrabajo.id=:data")
                .setParameter("data", t.getId());
        int result = query.executeUpdate();
     
    }

}
