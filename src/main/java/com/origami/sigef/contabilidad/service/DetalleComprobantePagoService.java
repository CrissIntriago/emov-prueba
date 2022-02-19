/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiarioComprobantePago;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.DetalleComprobantePago;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleComprobantePagoService extends AbstractService<DetalleComprobantePago> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleComprobantePagoService() {
        super(DetalleComprobantePago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleComprobantePago> getDetalleComprobantePago(ComprobantePago comprobantePago) {
        List<DetalleComprobantePago> resultado = (List<DetalleComprobantePago>) em.createQuery("SELECT d FROM DetalleComprobantePago d WHERE d.comprobantePago=:comprobantePago ORDER BY d.linea ASC")
                .setParameter("comprobantePago", comprobantePago)
                .getResultList();
        return resultado;
    }

    public BeneficiarioComprobantePago getUltimoNumeroReferencia(ComprobantePago comprobantePago) {
        try {
            BeneficiarioComprobantePago resultado = (BeneficiarioComprobantePago) em.createQuery("SELECT bcp FROM BeneficiarioComprobantePago bcp "
                    + "WHERE bcp.comprobantePago=:comprobantePago ORDER BY bcp.numeroTransferencia DESC")
                    .setParameter("comprobantePago", comprobantePago)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleComprobantePago> findAllDetalleComprobanteByCuentaContable(Long idCuenta, String fechaDesde, String fechaHasta) {

        return em.createQuery("SELECT detalle FROM DetalleComprobantePago detalle JOIN detalle.comprobantePago cp "
                + "WHERE detalle.cuentaContable.id = ?1 AND FUNCTION('to_char', cp.fechaComprobante,'YYYY-MM-DD') >= ?2 "
                + "AND FUNCTION('to_char', cp.fechaComprobante, 'YYYY-MM-DD') <= ?3  "
                + "ORDER BY cp.numComprobante")
                .setParameter(1, idCuenta)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .getResultList();

    }

    public List<DetalleComprobantePago> findAllDetalleComprobanteByCuentaContableSinDiarioGeneral(Long idCuenta, String fechaDesde, String fechaHasta) {

        return em.createQuery("SELECT detalle FROM DetalleComprobantePago detalle JOIN detalle.comprobantePago cp "
                + "WHERE detalle.cuentaContable.id = ?1 AND cp.diarioGeneral is null "
                + "AND FUNCTION('to_char', cp.fechaComprobante, 'YYYY-MM-DD') >= ?2 "
                + "AND FUNCTION('to_char', cp.fechaComprobante, 'YYYY-MM-DD') <= ?3 "
                + "ORDER BY cp.numComprobante")
                .setParameter(1, idCuenta)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .getResultList();

    }

    public DetalleComprobantePago getCuentaBancariaContableHaber(ComprobantePago comprobantePago) {
        try {
            DetalleComprobantePago resultado = (DetalleComprobantePago) em.createQuery("SELECT dcp FROM DetalleComprobantePago dcp WHERE dcp.comprobantePago=:comprobantePago AND dcp.haber>0")
                    .setParameter("comprobantePago", comprobantePago)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
}
