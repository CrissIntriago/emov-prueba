/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThLiquidacionRolService extends AbstractService<ThLiquidacionRol> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThLiquidacionRolService() {
        super(ThLiquidacionRol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ThLiquidacionRol> getThTipoRolList(ThTipoRol thTipoRol) {
        return (List<ThLiquidacionRol>) em.createQuery("SELECT thl FROM ThLiquidacionRol thl INNER JOIN thl.idDiasLaborados dl INNER JOIN dl.servidor s INNER JOIN s.persona p WHERE thl.estado = true AND thl.idTipoRol = ?1 ORDER BY p.apellido ASC")
                .setParameter(1, thTipoRol)
                .getResultList();
    }

    public int createLiquidacion(ThTipoRol thTipoRol, String user, Date date) {
        return (Integer) em.createNativeQuery("SELECT * FROM talento_humano.fs_create_liquidacion(?1,?2,?3)")
                .setParameter(1, thTipoRol.getId())
                .setParameter(2, user)
                .setParameter(3, date)
                .getSingleResult();
    }

    public int updateLiquidacion(ThLiquidacionRol thLiquidacionRol, String user) {
        return (Integer) em.createNativeQuery("SELECT * FROM talento_humano.servidor_rubros_rol(?1,?2,?3,?4,?5)")
                .setParameter(1, thLiquidacionRol.getIdTipoRol().getId())
                .setParameter(2, thLiquidacionRol.getIdServidorCargo().getIdServidor())
                .setParameter(3, thLiquidacionRol.getIdDiasLaborados())
                .setParameter(4, user)
                .setParameter(5, new Date())
                .getSingleResult();
    }
}
