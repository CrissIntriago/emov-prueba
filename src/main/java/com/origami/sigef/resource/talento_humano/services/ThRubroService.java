/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThRubroService extends AbstractService<ThRubro> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThRubroService() {
        super(ThRubro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigInteger findIdCuenta(Long idRubro, Long idServidor) {
        try {
            return (BigInteger) em.createNativeQuery("select * from talento_humano.id_cuenta_rubro_servidor(?1,?2)")
                    .setParameter(1, idRubro)
                    .setParameter(2, idServidor)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
