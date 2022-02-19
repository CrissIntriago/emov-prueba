/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.conf.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.models.QUERY;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class PlanCuentasService extends AbstractService<PlanCuentas> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanCuentasService() {
        super(PlanCuentas.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean getVerificarNivel(Integer nivel, String codeTipo, boolean tipoConfPresupuesto) {
        Long result = (Long) em.createQuery(QUERY.FIND_NIVEL)
                .setParameter(1, nivel)
                .setParameter(2, codeTipo)
                .setParameter(3, tipoConfPresupuesto)
                .getSingleResult();
        return (result.intValue() > 0);
    }

    public Integer getUltimoNivel(String codeTipo, boolean tipoConfPresupuesto) {
        try {
            Integer result = (Integer) em.createQuery(QUERY.LAST_NIVEL)
                    .setParameter(1, codeTipo)
                    .setParameter(2, tipoConfPresupuesto)
                    .getSingleResult();
            return (result + 1);
        } catch (Exception e) {
            return 1;
        }
    }

    public List<PlanCuentas> getNivelesList(String parametro, Boolean accion) {
        try {
            List<PlanCuentas> result = (List<PlanCuentas>) em.createQuery(QUERY.LIST_NIVELES)
                    .setParameter(1, parametro)
                    .setParameter(2, accion)
                    .getResultList();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public PlanCuentas getFindNext(String parametro, int parametro_2, Boolean tipo) {
        try {
            PlanCuentas result = (PlanCuentas) em.createQuery(QUERY.OBJ_NIVEL)
                    .setParameter(1, parametro)
                    .setParameter(2, parametro_2)
                    .setParameter(3, tipo)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
