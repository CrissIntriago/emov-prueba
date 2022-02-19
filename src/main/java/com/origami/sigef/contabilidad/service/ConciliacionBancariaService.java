/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ConciliacionBancaria;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jesus
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ConciliacionBancariaService extends AbstractService<ConciliacionBancaria> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ConciliacionBancariaService() {
        super(ConciliacionBancaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ConciliacionBancaria findConciliacionBancariaByMesAndAnio(Integer mes, Integer anio, Long idCuenta) {
        Query query = em.createQuery("SELECT c FROM ConciliacionBancaria c WHERE c.mes = ?1 AND c.anio = ?2 AND c.cuentaContable = ?3 ")
                .setParameter(1, mes)
                .setParameter(2, anio)
                .setParameter(3, idCuenta);
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            return (ConciliacionBancaria) query.getResultList().get(0);
        }
        return null;
    }

    public List<ConciliacionBancaria> findDebeAndHaberByDiarioGeneralDetalle(Long idCuenta, String fecha, String fechaDefault) {
        Query query = em.createNativeQuery("SELECT COALESCE(d.debe, 0.00) AS debe, COALESCE(d.haber,0.00) as haber FROM contabilidad.detalle_transaccion d "
                + "JOIN public.cuenta_contable c ON d.cuenta_contable = c.id "
                + "JOIN contabilidad.diario_general dg ON dg.id = d.diario_general "
                + "WHERE c.id = ?1 "
                + "AND to_char(dg.fecha_elaboracion, 'YYYY-MM') <= ?2 "
                + "AND to_char(dg.fecha_elaboracion, 'YYYY-MM')  >= ?3 "
                + "ORDER BY dg.numero_transaccion", "debeHaberDiarioComprobante")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha)
                .setParameter(3, fechaDefault);
        if (!query.getResultList().isEmpty()) {
            return (List<ConciliacionBancaria>) query.getResultList();
        }
        return null;
    }

    public List<ConciliacionBancaria> findHaberAndHaberByComprobantePagoDetalle(Long idCuenta, String fecha, String fechaDefault) {
        Query query = em.createNativeQuery("SELECT COALESCE(dcp.debe,0.00) AS debe, COALESCE(dcp.haber,0.00) as haber FROM contabilidad.detalle_comprobante_pago dcp\n"
                + "JOIN contabilidad.comprobante_pago cp ON dcp.comprobante_pago = cp.id "
                + "LEFT JOIN contabilidad.diario_general dg ON cp.diario_general = dg.id "
                + "JOIN public.cuenta_contable c ON dcp.cuenta_contable = c.id "
                + "WHERE c.id = ?1 AND  "
                + "CASE WHEN cp.diario_general is not null THEN "
                + "to_char(dg.fecha_elaboracion, 'YYYY-MM') <= ?2 "
                + "AND to_char(dg.fecha_elaboracion, 'YYYY-MM') >= ?3 "
                + "ELSE to_char(cp.fecha_comprobante, 'YYYY-MM') <= ?2 "
                + "AND to_char(cp.fecha_comprobante, 'YYYY-MM') >= ?3 END "
                + "ORDER BY cp.num_comprobante", "debeHaberDiarioComprobante")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha)
                .setParameter(3, fechaDefault);
        if (!query.getResultList().isEmpty()) {
            return (List<ConciliacionBancaria>) query.getResultList();
        }
        return null;
    }

    //Query para traer todo el saldo inicial de Cp and Dg antes de la fecha enviada
    public SaldoDebeHaberDTO findHaberAndHaberByComprobantePagoAndDiarioGeneralDetalle(Long idCuenta, String fecha) {
        try {
            Query query = em.createNativeQuery("select   sum(a.debes)as saldoDebe , sum (a.habers) as saldoHaber from (\n"
                    + "	SELECT COALESCE(d.debe, 0.00) AS debes, COALESCE(d.haber,0.00) as habers FROM contabilidad.detalle_transaccion d \n"
                    + "JOIN public.cuenta_contable c ON d.cuenta_contable = c.id \n"
                    + "JOIN contabilidad.diario_general dg ON dg.id = d.diario_general \n"
                    + "WHERE c.id = ?1 \n"
                    + "AND to_char(dg.fecha_elaboracion, 'YYYY-MM') <  ?2\n"
                    + "UNION\n"
                    + "SELECT COALESCE(dcp.debe,0.00) AS debes, COALESCE(dcp.haber,0.00) as habers FROM contabilidad.detalle_comprobante_pago dcp\n"
                    + "JOIN contabilidad.comprobante_pago cp ON dcp.comprobante_pago = cp.id \n"
                    + "LEFT JOIN contabilidad.diario_general dg ON cp.diario_general = dg.id \n"
                    + "JOIN public.cuenta_contable c ON dcp.cuenta_contable = c.id \n"
                    + "WHERE c.id = ?1 AND \n"
                    + "CASE WHEN cp.diario_general is not null THEN \n"
                    + "to_char(dg.fecha_elaboracion, 'YYYY-MM') < ?2\n"
                    + "ELSE to_char(cp.fecha_comprobante, 'YYYY-MM') < ?2 END) as a;", "SaldoDebeHaberMapping")
                    .setParameter(1, idCuenta)
                    .setParameter(2, fecha);
            SaldoDebeHaberDTO resultado = (SaldoDebeHaberDTO) query.getSingleResult();
            if (resultado.getSaldoDebe() == null) {
                resultado.setSaldoDebe(BigDecimal.ZERO);
            }
            if (resultado.getSaldoHaber() == null) {
                resultado.setSaldoHaber(BigDecimal.ZERO);
            }
            return resultado;

        } catch (Exception e) {
            return null;
        }
    }
}
