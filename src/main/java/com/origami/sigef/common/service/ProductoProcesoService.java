/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ProductoProceso;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProductoProcesoService extends AbstractService<ProductoProceso> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProductoProcesoService() {
        super(ProductoProceso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProductoProceso> getListEdit(Short periodo, BigInteger numTramite) {
        try {
            List<ProductoProceso> resultado = (List<ProductoProceso>) em.createQuery("SELECT p FROM ProductoProceso p WHERE EXTRACT(year from p.fechaTramite)=:periodo AND p.numetoTramite = :tramit ")
                    .setParameter("periodo", periodo.intValue()).setParameter("tramit", numTramite)
                    .getResultList();
            return resultado;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public BigDecimal valorProductoProceso(ProductoProceso p) {
        return (BigDecimal) em.createQuery("SELECT p.solicitado FROM ProductoProceso p where p.id=:id")
                .setParameter("id", p.getId()).getResultStream().findFirst().orElse(BigDecimal.ZERO);

    }
}
