/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.HisPersonaReferencias;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@Stateless
public class HisPersonaReferenciasService extends AbstractService<HisPersonaReferencias> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public HisPersonaReferenciasService() {
        super(HisPersonaReferencias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long totalRegistroUser(boolean validado) {
        return (Long) em.createQuery("SELECT COUNT(c) FROM Cliente c where c.validado=:validado")
                .setParameter("validado", validado).getResultStream().findFirst().orElse(BigInteger.ZERO);
    }

    public Long totalRegistro() {
        return (Long) em.createQuery("SELECT COALESCE(COUNT(c),0) FROM Cliente c")
                .getResultStream().findFirst().orElse(BigInteger.ZERO);
    }

    public Long totalRegistroUser(Usuarios u, Boolean validado) {
        return (Long) em.createQuery("SELECT COALESCE(COUNT(c),0) FROM Cliente c where c.usuarioValidador=:usuario and c.validado=:validado and c.validAdmin=false")
                .setParameter("usuario", u).setParameter("validado", validado)
                .getResultStream().findFirst().orElse(BigInteger.ZERO);
    }

}
