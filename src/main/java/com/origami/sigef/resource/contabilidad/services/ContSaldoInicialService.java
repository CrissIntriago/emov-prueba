/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContSaldoInicialService extends AbstractService<ContSaldoInicial> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContSaldoInicialService() {
        super(ContSaldoInicial.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getQuerySql(String code) {
        String result = (String) em.createNativeQuery("SELECT c FROM ConfQuerys c WHERE c.code = ?1")
                .setParameter(1, code)
                .getSingleResult();
        return result;
    }

    public ContSaldoInicial findContSaldoInicialByIdCuentaAndPeriodo(ContCuentas idCuenta, Short periodo) {
        try {
            return (ContSaldoInicial) em.createQuery("SELECT c FROM ContSaldoInicial c where c.idCuenta = :idCuenta AND c.periodo = :periodo")
                    .setParameter("idCuenta", idCuenta)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
