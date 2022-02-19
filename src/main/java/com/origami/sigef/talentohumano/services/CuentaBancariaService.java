/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CuentaBancariaService extends AbstractService<CuentaBancaria> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuentaBancariaService() {
        super(CuentaBancaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CuentaBancaria> getListaCuenta() {
        Query query = em.createQuery("SELECT b FROM CuentaBancaria b WHERE b.estado = TRUE");
        List<CuentaBancaria> result = (List<CuentaBancaria>) query.getResultList();
        return result;
    }

    public List<CuentaBancaria> getListaCuentaBancariaByAnio(Short anio) {
        Query query = em.createQuery("SELECT b FROM CuentaBancaria b WHERE b.estado = TRUE AND b.cuentaMovimiento.periodo = ?1")
                .setParameter(1, anio);
        List<CuentaBancaria> result = (List<CuentaBancaria>) query.getResultList();
        return result;
    }

    public List<ContCuentaEntidad> getListaCuentasEntidad() {
        Query query = em.createQuery("SELECT c FROM ContCuentaEntidad c WHERE c.estado = TRUE");
        List<ContCuentaEntidad> result = (List<ContCuentaEntidad>) query.getResultList();
        return result;
    }

    public Boolean getMovimientoCuenta(CuentaBancaria cta) {
        System.out.println("entro al service");
        try {
            Query query = em.createQuery("SELECT ct FROM CuentaBancaria ct WHERE ct= ?1 AND (ct.numeroCuenta in (SELECT tr.ctaCteBceIp  FROM Transferencias tr) OR ct.id in (SELECT cb.cuentaContableBanco from ConciliacionBancaria cb) OR ct IN (SELECT cp.cuentaBancaria FROM ComprobantePago cp))")
//            Query query = em.createNativeQuery("select * from talento_humano.cuenta_bancaria cb\n"
//                    + "where cb.id = ?1 AND\n"
//                    + "cb.estado = true\n"
//                    + "AND (cb.numero_cuenta in (select tr.cta_cte_bce_ip from contabilidad.transferencias tr) or\n"
//                    + "cb.id in ((select cb.cuenta_contable_banco from contabilidad.conciliacion_bancaria cb) )\n"
//                    + "or cb.id in ((SELECT cp.cuenta_bancaria FROM contabilidad.comprobante_pago cp)))", CuentaBancaria.class)
                    .setParameter(1, cta);
            CuentaBancaria cuenta = (CuentaBancaria) query.getSingleResult();
            return cuenta != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
