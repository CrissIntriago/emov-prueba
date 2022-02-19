/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AsientosContables;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class AsientosContablesService extends AbstractService<AsientosContables> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AsientosContablesService() {
        super(AsientosContables.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<AsientosContables> getAsientosContablesByTipo(Long codigo) {
        try {
            List<AsientosContables> resultado = (List<AsientosContables>) em.createQuery("SELECT a FROM AsientosContables a Where a.tipo = ?1")
                    .setParameter(1, BigInteger.valueOf(codigo))
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * trae todos los tipos de estado de Situacion Financiera excepto tipo
     * especificado
     *
     * @param grupo que no va a venir en la consulta(grupo)
     * @param codigo tipo de AsientoContable
     * @return lista sin un grupo especificado.
     */
    public List<AsientosContables> getDatosTipoAsientoFSinGrupo(Long codigo) {
        try {
            List<AsientosContables> resultado = (List<AsientosContables>) em.createQuery("SELECT a FROM AsientosContables a Where a.tipo = ?1 AND a.grupo <> 'VARIACIONES NO PRESUPUESTARIAS' ")
                    .setParameter(1, BigInteger.valueOf(codigo))
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * trae datos de un tipo de asiento contable en este caso del tipo y un
     * grupo en especifico.
     *
     *
     * @param grupo que va a venir en la consulta(grupo)
     * @param codigo tipo de AsientoContable
     * @return lista solo de un grupo especificado.
     */
    public List<AsientosContables> getDatosTipoAsientoFUnGrupo(Long codigo, String grupo) {
        try {
            List<AsientosContables> resultado = (List<AsientosContables>) em.createQuery("SELECT a FROM AsientosContables a Where a.tipo = ?1 AND a.grupo = ?2 ")
                    .setParameter(1, BigInteger.valueOf(codigo))
                    .setParameter(2, grupo)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }
}
