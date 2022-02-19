/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BeneficiarioComprobantePagoService extends AbstractService<BeneficiarioComprobantePago> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BeneficiarioComprobantePagoService() {
        super(BeneficiarioComprobantePago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DetalleBanco getCuentaBancaria(Cliente cliente, Boolean tipoBeneficiario) {
        try {
            String sql;
            if (tipoBeneficiario) {
                sql = "SELECT db FROM DetalleBanco db INNER JOIN db.proveedor pr WHERE pr.cliente=:cliente AND db.estadoCuenta = TRUE AND db.estado = TRUE ";
            } else {
                sql = "SELECT db FROM DetalleBanco db INNER JOIN db.servidorPublico sb WHERE sb.persona=:cliente AND db.estadoCuenta = TRUE AND db.estado = TRUE ";
            }
            DetalleBanco resultado = (DetalleBanco) em.createQuery(sql)
                    .setParameter("cliente", cliente)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public DetalleBanco getCuentaBancariaRJ(Cliente cliente, Boolean tipoBeneficiario) {
        try {
            String sql;
            if (tipoBeneficiario) {
                sql = "SELECT db FROM DetalleBanco db INNER JOIN db.proveedor pr WHERE pr.cliente=:cliente AND db.estadoCuenta = TRUE AND db.estado = TRUE AND db.ctaRetenciones = TRUE";
            } else {
                sql = "SELECT db FROM DetalleBanco db INNER JOIN db.servidorPublico sb WHERE sb.persona=:cliente AND db.estadoCuenta = TRUE AND db.estado = TRUE ";
            }
            DetalleBanco resultado = (DetalleBanco) em.createQuery(sql)
                    .setParameter("cliente", cliente)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<BeneficiarioComprobantePago> getBeneficiarioComprobante(ComprobantePago comprobante) {
        List<BeneficiarioComprobantePago> resultado = (List<BeneficiarioComprobantePago>) em.createQuery("SELECT bcp FROM BeneficiarioComprobantePago bcp "
                + "WHERE bcp.comprobantePago=:comprobante ORDER BY bcp.numeroTransferencia ASC")
                .setParameter("comprobante", comprobante)
                .getResultList();
        return resultado;
    }

    public BeneficiarioComprobantePago getBeneficiarioComprobantePago(ComprobantePago comprobantePago, String identificacion) {
        BeneficiarioComprobantePago resultado = (BeneficiarioComprobantePago) em.createQuery(" SELECT bcp FROM BeneficiarioComprobantePago bcp "
                + "INNER JOIN bcp.beneficiario c "
                + "WHERE bcp.comprobantePago=:comprobantePago AND c.identificacion=:identificacion")
                .setParameter("comprobantePago", comprobantePago)
                .setParameter("identificacion", identificacion)
                .getSingleResult();
        return resultado;
    }

    public BeneficiarioComprobantePago getBeneficiarioComprobantePago(BigInteger num) {
        try {
            BeneficiarioComprobantePago resultado = (BeneficiarioComprobantePago) em.createQuery("SELECT b FROM BeneficiarioComprobantePago b INNER JOIN b.comprobantePago cp WHERE cp.numeroTramite= :num AND cp.estado='REGISTRADO'")
                    .setParameter("num", num).getSingleResult();
            return resultado;
        } catch (NoResultException r) {
            return null;
        }
    }

    public List<BeneficiarioComprobantePago> getBeneficiarioComprobante(BigInteger numTramite) {
        List<BeneficiarioComprobantePago> resultado = (List<BeneficiarioComprobantePago>) em.createQuery("SELECT bcp FROM BeneficiarioComprobantePago bcp INNER JOIN bcp.comprobantePago cp "
                + "WHERE cp.numeroTramite=:numTramite ORDER BY bcp.numeroTransferencia ASC")
                .setParameter("numTramite", numTramite)
                .getResultList();
        return resultado;
    }

}
