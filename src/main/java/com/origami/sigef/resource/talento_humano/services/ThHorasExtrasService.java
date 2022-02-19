/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.ThHorasExtras;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class ThHorasExtrasService extends AbstractService<ThHorasExtras> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThHorasExtrasService() {
        super(ThHorasExtras.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean getFindData(ThTipoRol thTipoRolSeleccionado) {
        List<ThHorasExtras> result = (List<ThHorasExtras>) em.createQuery(QUERY.FIND_HORAS_EXTRAS)
                .setParameter(1, thTipoRolSeleccionado)
                .getResultList();
        return result.isEmpty();
    }

    public void delete(ThTipoRol thTipoRolSeleccionado) {
        int result = em.createNativeQuery(QUERY.DELETE_HORAS_EXTRAS)
                .setParameter(1, thTipoRolSeleccionado.getId())
                .executeUpdate();
    }

    public void createBlock(Long id, String user, Date date, String codConfig, Short periodo) {
        Integer result = (Integer) em.createNativeQuery(QUERY.CREATE_HORAS_EXTRAS)
                .setParameter(1, id)
                .setParameter(2, codConfig)
                .setParameter(3, periodo)
                .setParameter(4, user)
                .setParameter(5, date)
                .getSingleResult();
    }
}
