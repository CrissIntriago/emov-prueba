/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.TablaImpuestoRenta;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TablaImpuestoService extends AbstractService<TablaImpuestoRenta> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TablaImpuestoService() {
        super(TablaImpuestoRenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TablaImpuestoRenta getorcentaje(Short anio, BigDecimal valor) {
        try {
            Query query = em.createNativeQuery("select * from talento_humano.tabla_impuesto_renta ti where\n"
                    + "ti.periodo = ?1 and ?2 BETWEEN ti.fraccion_basica and ti.exceso_hasta", TablaImpuestoRenta.class)
                    .setParameter(1, anio)
                    .setParameter(2, valor);
            TablaImpuestoRenta result = (TablaImpuestoRenta) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
}
