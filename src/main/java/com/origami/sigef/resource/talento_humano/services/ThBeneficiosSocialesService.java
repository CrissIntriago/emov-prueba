/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSociales;
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
public class ThBeneficiosSocialesService extends AbstractService<ThBeneficiosSociales> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThBeneficiosSocialesService() {
        super(ThBeneficiosSociales.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean getFindData(ThTipoRol thTipoRolSeleccionado) {
        List<ThBeneficiosSociales> result = (List<ThBeneficiosSociales>) em.createQuery(QUERY.FIND_BENEFICIO_SOCIALES)
                .setParameter(1, thTipoRolSeleccionado)
                .getResultList();
        return result.isEmpty();
    }

    public void createBlock(Long id, boolean b, boolean b0, boolean b1, String nameUser, String codRubro, Integer codBeneficio) {
        Integer result = (Integer) em.createNativeQuery(QUERY.CREATE_BENEFICIO)
                .setParameter(1, id)
                .setParameter(2, b)
                .setParameter(3, b0)
                .setParameter(4, b1)
                .setParameter(5, nameUser)
                .setParameter(6, new Date())
                .setParameter(7, codRubro)
                .setParameter(8, codBeneficio)
                .getSingleResult();
    }

    public void delete(ThTipoRol thTipoRolSeleccionado, int code) {
        int result = 0;
        switch (code) {
            case 1:
                result = em.createNativeQuery(QUERY.DELETE_DECIMO_TERCERO)
                        .setParameter(1, thTipoRolSeleccionado)
                        .executeUpdate();
                break;
            case 2:
                result = em.createNativeQuery(QUERY.DELETE_DECIMO_CUARTO)
                        .setParameter(1, thTipoRolSeleccionado)
                        .executeUpdate();
                break;
            default:
                result = em.createNativeQuery(QUERY.DELETE_FONDO_RESERVA)
                        .setParameter(1, thTipoRolSeleccionado)
                        .executeUpdate();
                break;

        }
    }

}
