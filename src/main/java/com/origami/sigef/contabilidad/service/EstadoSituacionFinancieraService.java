/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.EstadoSituacionFinanciera;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class EstadoSituacionFinancieraService extends AbstractService<EstadoSituacionFinanciera> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EstadoSituacionFinancieraService() {
        super(EstadoSituacionFinanciera.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void deleteAll() {
        Query query = em.createNativeQuery("DELETE FROM contabilidad.estado_situacion_financiera");
        query.executeUpdate();
        System.out.println(query.executeUpdate());
    }

}
