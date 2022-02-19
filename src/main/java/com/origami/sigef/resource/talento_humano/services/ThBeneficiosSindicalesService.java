/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSindicales;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThBeneficiosSindicalesService extends AbstractService<ThBeneficiosSindicales> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThBeneficiosSindicalesService() {
        super(ThBeneficiosSindicales.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public int loadData(Short anio, String user, ThTipoRol thTipoRol, String codConfig) {
        return (Integer) em.createNativeQuery("SELECT * FROM talento_humano.fs_create_beneficios_sindicales(?1,?2,?3,?4,?5)")
                .setParameter(1, thTipoRol.getId())
                .setParameter(2, anio)
                .setParameter(3, user)
                .setParameter(4, new Date())
                .setParameter(5, codConfig)
                .getSingleResult();
    }
}
