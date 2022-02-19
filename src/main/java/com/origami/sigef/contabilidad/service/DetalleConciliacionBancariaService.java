/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetalleConciliacionBancaria;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jesus
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleConciliacionBancariaService extends AbstractService<DetalleConciliacionBancaria> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleConciliacionBancariaService() {
        super(DetalleConciliacionBancaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleConciliacionBancaria> findAllComprobantePagoforDetalleConciliacionDebito(Long idCuenta, String fecha) {
        Query query = em.createNativeQuery("SELECT (case \n" +
                "    when cp.fecha_registro is not null then \n" +
                "    cp.fecha_registro \n" +
                "    else diario.fecha_registro \n" +
                "    end) as fecha, \n" +
                "    diario.num_registro as numDiarioGeneral, \n" +
                "    cp.num_registro as numComprobantePago, \n" +
                "    UPPER(concat(client.nombre,' ', client.apellido)) as beneficiario, \n" +
                "    beneficiario_cp.monto as valor,\n" +
                "    tr.num_referencia as spi,\n" +
                "    client.estado as estado, \n" +
                "    diarioAnulado.num_registro as referencia,\n" +
                "    diarioAnulado.fecha_registro as fechaReferencia,\n" +
                "    (case when cp.num_registro is not null then cp.descripcion else \n" +
                "    (case when diario.num_registro is not null then diario.descripcion else '' end)\n" +
                "end ) as detalle\n" +
                "FROM contabilidad.cont_diario_general_detalle dgd\n" +
                "JOIN contabilidad.cont_cuentas c ON dgd.id_cont_cuentas = c.id \n" +
                "JOIN contabilidad.cont_comprobante_pago cp ON dgd.id_cont_comprobante_pago = cp.id \n" +
                "JOIN contabilidad.cont_beneficiario_comprobante_pago beneficiario_cp ON beneficiario_cp.id_comprobante_pago = cp.id \n" +
                "JOIN contabilidad.cont_transferencias_detalle td ON td.id_cont_comprobante_pago = cp.id\n" +
                "JOIN contabilidad.cont_transferencias tr on td.id_cont_transferencia = tr.id\n" +
                "JOIN public.cliente client ON beneficiario_cp.id_cliente = client.id \n" +
                "LEFT JOIN contabilidad.cont_diario_general diario ON cp.id_cont_diario_general = diario.id \n" +
                "LEFT JOIN contabilidad.cont_diario_general diarioAnulado ON diarioAnulado.id = diario.id_diario_general \n" +
                "WHERE c.id = ?1\n" +
                "AND to_char(cp.fecha_registro, 'YYYY-MM') = ?2\n" +
                "AND beneficiario_cp.monto is not null AND beneficiario_cp.monto <> 0.00 ORDER BY cp.num_registro","CPConciliacionBancariaValueMapping")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha);
        return idTempConciliacionBancaria((List<DetalleConciliacionBancaria>) query.getResultList(), false);
    }

    public List<DetalleConciliacionBancaria> findAllDiarioGeneralforDetalleConciliacionDebito(Long idCuenta, String fecha) {
        Query query = em.createNativeQuery("SELECT diario.fecha_elaboracion as fecha, "
                + "diario.numero_transaccion as numDiarioGeneral, "
                + "(CASE WHEN diario.beneficiario is not null then "
                + "UPPER(concat(client.nombre,' ', client.apellido)) "
                + "else "
                + "UPPER(concat(sol_cliente.nombre,' ', sol_cliente.apellido)) "
                + "end) as beneficiario, "
                + "detalledg.haber as valor, "
                + "diario.estado_diario as estado, "
                + "diarioAnulado.numero_transaccion as referencia, "
                + "diarioAnulado.fecha_elaboracion as fechaReferencia, "
                + "(case when diario.numero_transaccion is not null then diario.observacion else '' end) as detalle "
                + "FROM contabilidad.detalle_transaccion detalledg "
                + "JOIN contabilidad.diario_general diario ON detalledg.diario_general = diario.id "
                + "LEFT JOIN contabilidad.diario_general diarioAnulado ON diarioAnulado.id =  diario.ref_diario_anulado "
                + "JOIN public.cuenta_contable c ON detalledg.cuenta_contable = c.id "
                + "LEFT JOIN public.cliente client ON diario.beneficiario = client.id "
                + "LEFT JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso solicitud "
                + "ON diario.certificaciones_presupuestario = solicitud.id "
                + "LEFT JOIN public.cliente sol_cliente ON sol_cliente.id = solicitud.beneficiario "
                + "WHERE c.id = ?1  "
                + "AND to_char(diario.fecha_elaboracion, 'YYYY-MM') = ?2 AND detalledg.haber <> 0.00 "
                + "ORDER BY diario.numero_transaccion", "DGConciliacionBancariaValueMapping")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha);
        return idTempConciliacionBancaria((List<DetalleConciliacionBancaria>) query.getResultList(), false);
    }

    public List<DetalleConciliacionBancaria> findAllDiarioGeneralforDetalleConciliacionCredito(Long idCuenta, String fecha) {
        Query query = em.createNativeQuery("SELECT diario.fecha_elaboracion as fecha, "
                + "diario.numero_transaccion as numDiarioGeneral, "
                + "(CASE WHEN diario.beneficiario is not null then "
                + "UPPER(concat(client.nombre,' ', client.apellido)) "
                + "else "
                + "UPPER(concat(sol_cliente.nombre,' ', sol_cliente.apellido)) "
                + "end) as beneficiario, "
                + "detalledg.debe as valor, "
                + "diario.estado_diario as estado, "
                + "diarioAnulado.numero_transaccion as referencia, "
                + "diarioAnulado.fecha_elaboracion as fechaReferencia, "
                + "(case when diario.numero_transaccion is not null then diario.observacion else '' end) as detalle "
                + "FROM contabilidad.detalle_transaccion detalledg "
                + "JOIN contabilidad.diario_general diario ON detalledg.diario_general = diario.id "
                + "LEFT JOIN contabilidad.diario_general diarioAnulado ON diarioAnulado.id = diario.ref_diario_anulado "
                + "JOIN public.cuenta_contable c ON detalledg.cuenta_contable = c.id "
                + "LEFT JOIN public.cliente client ON diario.beneficiario = client.id "
                + "LEFT JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso solicitud "
                + "ON diario.certificaciones_presupuestario = solicitud.id "
                + "LEFT JOIN public.cliente sol_cliente ON sol_cliente.id = solicitud.beneficiario "
                + "WHERE c.id = ?1 AND detalledg.debe is not null AND detalledg.debe <> 0.00 "
                + "AND to_char(diario.fecha_elaboracion, 'YYYY-MM') = ?2 ORDER BY diario.numero_transaccion", "DGConciliacionBancariaValueMapping")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha);
        return idTempConciliacionBancaria((List<DetalleConciliacionBancaria>) query.getResultList(), true);
    }

    private List<DetalleConciliacionBancaria> idTempConciliacionBancaria(List<DetalleConciliacionBancaria> conciliacion, Boolean credito) {
        if (credito) {
            if (!conciliacion.isEmpty()) {
                for (DetalleConciliacionBancaria c : conciliacion) {
                    c.setId(Utils.idTemp());
                    c.setTipo("CLB");
                }
                return conciliacion;
            }
        } else {
            if (!conciliacion.isEmpty()) {
                for (DetalleConciliacionBancaria c : conciliacion) {
                    c.setId(Utils.idTemp());
                    c.setTipo("DLB");
                }
                return conciliacion;
            }
        }
        return null;
    }

    public BigDecimal finValueCreditoByComprobantePagoAndDiarioGeneral(Long idCuenta, String fecha, Short periodo, String fechaDefault) {
        return (BigDecimal) em.createNativeQuery("SELECT "
                + "COALESCE(sum(de_diario.debe) + (SELECT "
                + "COALESCE(sum(de_comp.debe), 0.00) as total_debe "
                + "FROM contabilidad.comprobante_pago comp "
                + "LEFT JOIN contabilidad.diario_general diario ON comp.diario_general = diario.id "
                + "LEFT JOIN contabilidad.detalle_comprobante_pago de_comp ON de_comp.comprobante_pago = comp.id "
                + "JOIN public.cuenta_contable c ON de_comp.cuenta_contable = c.id "
                + "WHERE c.id = ?1 AND "
                + "(case when diario.fecha_elaboracion is not null then "
                + "(to_char(diario.fecha_elaboracion, 'YYYY-MM') <= ?2 "
                + "AND to_char(diario.fecha_elaboracion, 'YYYY-MM') >= ?4) "
                + "else (to_char(comp.fecha_comprobante, 'YYYY-MM') <= ?2 "
                + "AND to_char(comp.fecha_comprobante, 'YYYY-MM') >= ?4) "
                + "end) AND "
                + "c.estado = true AND c.periodo = ?3 "
                + "AND c.movimiento = true), 0.00) as total_debe "
                + "FROM contabilidad.diario_general diario "
                + "JOIN contabilidad.detalle_transaccion de_diario ON de_diario.diario_general = diario.id "
                + "JOIN public.cuenta_contable c ON de_diario.cuenta_contable = c.id "
                + "WHERE c.id = ?1 AND "
                + "to_char(diario.fecha_elaboracion, 'YYYY-MM') <= ?2 AND "
                + "to_char(diario.fecha_elaboracion, 'YYYY-MM') >= ?4 AND "
                + "c.estado = true AND c.periodo = ?3 "
                + "AND c.movimiento = true;")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha)
                .setParameter(3, periodo)
                .setParameter(4, fechaDefault)
                .getSingleResult();
    }

    public BigDecimal finValueDebitoByComprobantePagoAndDiarioGeneral(Long idCuenta, String fecha, Short periodo, String fechaDefault) {
        return (BigDecimal) em.createNativeQuery("SELECT "
                + "COALESCE(sum(de_diario.haber) + (SELECT "
                + "COALESCE(sum(de_comp.haber), 0.00) as total_haber "
                + "FROM contabilidad.comprobante_pago comp "
                + "LEFT JOIN contabilidad.diario_general diario ON comp.diario_general = diario.id "
                + "LEFT JOIN contabilidad.detalle_comprobante_pago de_comp ON de_comp.comprobante_pago = comp.id "
                + "JOIN public.cuenta_contable c ON de_comp.cuenta_contable = c.id "
                + "WHERE c.id = ?1 AND "
                + "(case when diario.fecha_elaboracion is not null then "
                + "(to_char(diario.fecha_elaboracion, 'YYYY-MM') <= ?2 "
                + "AND to_char(diario.fecha_elaboracion, 'YYYY-MM') >= ?4) "
                + "else (to_char(comp.fecha_comprobante, 'YYYY-MM') <= ?2 "
                + "AND to_char(comp.fecha_comprobante, 'YYYY-MM') >= ?4) "
                + "end) AND "
                + "c.estado = true AND c.periodo = ?3 "
                + "AND c.movimiento = true), 0.00) as total_haber "
                + "FROM contabilidad.diario_general diario "
                + "JOIN contabilidad.detalle_transaccion de_diario ON de_diario.diario_general = diario.id "
                + "JOIN public.cuenta_contable c ON de_diario.cuenta_contable = c.id "
                + "WHERE c.id = ?1 AND to_char(diario.fecha_elaboracion, 'YYYY-MM') <= ?2 AND "
                + "to_char(diario.fecha_elaboracion, 'YYYY-MM') >= ?4 AND "
                + "c.estado = true AND c.periodo = ?3 "
                + "AND c.movimiento = true;")
                .setParameter(1, idCuenta)
                .setParameter(2, fecha)
                .setParameter(3, periodo)
                .setParameter(4, fechaDefault)
                .getSingleResult();
    }

    public List<DetalleConciliacionBancaria> findAllDetalleConciliacionByNoEfectivo(String fecha, Long idCuenta, String pagoNefe, String depositoNefe, String credNefe, String debitoNoefe) {
        return em.createQuery("SELECT d FROM DetalleConciliacionBancaria d WHERE "
                + "d.conciliacionBancaria.periodo < ?1 AND d.conciliacionBancaria.cuentaContable = ?2 "
                + "AND (d.conciliacion = ?3 OR d.conciliacion = ?4 OR d.conciliacion = ?5 OR d.conciliacion = ?6)")
                .setParameter(1, fecha)
                .setParameter(2, idCuenta)
                .setParameter(3, pagoNefe)
                .setParameter(4, depositoNefe)
                .setParameter(5, credNefe)
                .setParameter(6, debitoNoefe)
                .getResultList();
    }
}
