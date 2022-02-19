/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DetalleTransferencias;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DetalleTransaccionService extends AbstractService<DetalleTransaccion> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleTransaccionService() {
        super(DetalleTransaccion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleTransaccion> getDetalleTransaccion(DiarioGeneral diarioGeneral) {
        List<DetalleTransaccion> resultado = (List<DetalleTransaccion>) em.createQuery("SELECT d FROM DetalleTransaccion d WHERE d.diarioGeneral=:diarioGeneral ORDER BY d.contador ASC")
                .setParameter("diarioGeneral", diarioGeneral)
                .getResultList();
        return resultado;
    }

    public List<DetalleTransaccion> getDetalleTransaccionDevengados(DiarioGeneral diarioGeneral) {
        List<DetalleTransaccion> resultado = (List<DetalleTransaccion>) em.createQuery("SELECT d FROM DetalleTransaccion d "
                + "WHERE d.diarioGeneral=:diarioGeneral AND d.partidaPresupuestaria IS NOT NULL ORDER BY d.devengado ASC")
                .setParameter("diarioGeneral", diarioGeneral)
                .getResultList();
        return resultado;
    }

    public List<DetalleTransaccion> getDetalleTransaccionDevengadosPlanilla(DiarioGeneral diarioGeneral) {
        List<DetalleTransaccion> resultado = (List<DetalleTransaccion>) em.createNativeQuery("select *  from (\n"
                + "	select id, diario_general, cuenta_contable, (devengado - ejecutado_) as debe, haber, tipo_transaccion,\n"
                + "	partida_presupuestaria, estructura_programatica, (devengado - ejecutado_) as comprometido, \n"
                + "	(devengado - ejecutado_) as devengado, ejecutado, contador, comprobante_pago, cedula_presupuestaria, fuente,\n"
                + "	tipo_Devengado, dato_cargado, id_detalle_reserva\n"
                + "	from (select d.*, (CASE WHEN dc.id_detalle_reserva is null THEN 0 ELSE dc.ejecutado END) as ejecutado_\n"
                + "		  from contabilidad.detalle_transaccion d\n"
                + "		  left JOIN contabilidad.detalle_comprobante_pago dc ON d.id_detalle_reserva = dc.id_detalle_reserva\n"
                + "		  WHERE d.cedula_presupuestaria is not null\n"
                + "		  order by d.id_detalle_reserva asc\n"
                + "		 ) temporal_table\n"
                + ") detalle_transaccion WHERE comprometido >0 AND diario_general = ?1 ORDER BY devengado ASC", DetalleTransaccion.class)
                .setParameter(1, diarioGeneral.getId())
                .getResultList();
        return resultado;
    }

    public DetalleTransaccion findByPadreDiarioGeneral(Long idDiario, Long idCuenta) {
        Query query = em.createNativeQuery("SELECT SUM(d.debe) AS sumaDebe, SUM(d.haber) AS sumaHaber FROM "
                + "contabilidad.detalle_transaccion d JOIN public.cuenta_contable c ON c.id = d.cuenta_contable "
                + "WHERE d.diario_general = ?1 AND c.id = ?2")
                .setParameter(1, idDiario)
                .setParameter(2, idCuenta);
        List<Object[]> result = query.getResultList();

        if (result != null) {
            List<DetalleTransaccion> list = new ArrayList<>();
            for (Object[] data : result) {
                DetalleTransaccion d = new DetalleTransaccion();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }

    public DetalleTransaccion findByPadreDetalleTransaccionExceptoDiarioApertura(String fechaDesde, String fechaHasta, Long idDiario, Long idCuenta,
            Long idTransaccionCierre) {

        List<Object[]> result = new ArrayList<>();
        if (idTransaccionCierre != null) {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber "
                    + "FROM contabilidad.detalle_transaccion detalle "
                    + "JOIN contabilidad.diario_general d ON d.id = detalle.diario_general "
                    + "JOIN public.cuenta_contable c ON c.id = detalle.cuenta_contable "
                    + "WHERE to_char(fecha_elaboracion,'YYYY-MM') <= ?1 "
                    + "AND to_char(fecha_elaboracion,'YYYY-MM') >= ?2 AND detalle.diario_general <> ?3 AND d.estado = true "
                    + "AND c.id = ?4 "
                    + "AND d.tipo <> ?5";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idDiario)
                    .setParameter(4, idCuenta)
                    .setParameter(5, idTransaccionCierre);
            result = query.getResultList();
        } else {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber "
                    + "FROM contabilidad.detalle_transaccion detalle "
                    + "JOIN contabilidad.diario_general d ON d.id = detalle.diario_general "
                    + "JOIN public.cuenta_contable c ON c.id = detalle.cuenta_contable "
                    + "WHERE to_char(fecha_elaboracion,'YYYY-MM') <= ?1 "
                    + "AND to_char(fecha_elaboracion,'YYYY-MM') >= ?2 AND detalle.diario_general <> ?3 AND d.estado = true "
                    + "AND c.id = ?4";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idDiario)
                    .setParameter(4, idCuenta);
            result = query.getResultList();
        }

        if (result != null) {
            List<DetalleTransaccion> list = new ArrayList<>();
            for (Object[] data : result) {
                DetalleTransaccion d = new DetalleTransaccion();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }

    public DetalleTransaccion findByPadreDetalleTransaccionNoExisteDiarioApertura(String fechaDesde, String fechaHasta, Long idCuenta,
            Long idTransaccionCierre) {

        List<Object[]> result = new ArrayList<>();
        if (idTransaccionCierre != null) {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber "
                    + "FROM contabilidad.detalle_transaccion detalle "
                    + "JOIN contabilidad.diario_general d ON d.id = detalle.diario_general "
                    + "JOIN public.cuenta_contable c ON c.id = detalle.cuenta_contable "
                    + "WHERE to_char(fecha_elaboracion,'YYYY-MM') <= ?1 "
                    + "AND to_char(fecha_elaboracion,'YYYY-MM') >= ?2 AND d.estado = true "
                    + "AND c.id = ?3 "
                    + "AND d.tipo <> ?4";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idCuenta)
                    .setParameter(4, idTransaccionCierre);
            result = query.getResultList();
        } else {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber "
                    + "FROM contabilidad.detalle_transaccion detalle "
                    + "JOIN contabilidad.diario_general d ON d.id = detalle.diario_general "
                    + "JOIN public.cuenta_contable c ON c.id = detalle.cuenta_contable "
                    + "WHERE to_char(fecha_elaboracion,'YYYY-MM') <= ?1 "
                    + "AND to_char(fecha_elaboracion,'YYYY-MM') >= ?2 AND d.estado = true "
                    + "AND c.id = ?3";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idCuenta);
            result = query.getResultList();
        }

        if (result != null) {
            List<DetalleTransaccion> list = new ArrayList<>();
            for (Object[] data : result) {
                DetalleTransaccion d = new DetalleTransaccion();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }

    public List<DetalleTransaccion> findAllDetalleByCuentaContableHijasAndFecha(String fecha, Long idCuentaPadre, Long idTransaccionCierre) {
        try {
            List<Object[]> result = new ArrayList<>();
            if (idTransaccionCierre != null) {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.diario_general, "
                        + "detalle.cuenta_contable, "
                        + "detalle.debe, "
                        + "detalle.haber FROM contabilidad.detalle_transaccion detalle "
                        + "JOIN contabilidad.diario_general d ON detalle.diario_general = d.id "
                        + "JOIN public.cuenta_contable c ON detalle.cuenta_contable = c.id "
                        + "WHERE to_char(fecha_elaboracion,'YYYY-MM') = ?1 "
                        + "AND c.padre = ?2 AND d.tipo <> ?3 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuentaPadre)
                        .setParameter(3, idTransaccionCierre);
                result = query.getResultList();
            } else {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.diario_general, "
                        + "detalle.cuenta_contable, "
                        + "detalle.debe, "
                        + "detalle.haber FROM contabilidad.detalle_transaccion detalle "
                        + "JOIN contabilidad.diario_general d ON detalle.diario_general = d.id "
                        + "JOIN public.cuenta_contable c ON detalle.cuenta_contable = c.id "
                        + "WHERE to_char(fecha_elaboracion,'YYYY-MM') = ?1 "
                        + "AND c.padre = ?2 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuentaPadre);
                result = query.getResultList();
            }
            if (result != null) {
                List<DetalleTransaccion> list = new ArrayList<>();
                for (Object[] data : result) {
                    DetalleTransaccion d = new DetalleTransaccion(((BigInteger) data[0]).longValue());
                    d.setDiarioGeneral(new DiarioGeneral(((BigInteger) data[1]).longValue()));
                    d.setCuentaContable(new CuentaContable(((BigInteger) data[2]).longValue()));
                    d.setDebe((BigDecimal) data[3]);
                    d.setHaber((BigDecimal) data[4]);
                    list.add(d);
                }
                return list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetalleTransaccion> findAllDetalleByCuentaContablePadreAndFecha(String fecha, Long idCuenta, Long idTransaccionCierre) {
        try {
            List<Object[]> result = new ArrayList<>();
            if (idTransaccionCierre != null) {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.diario_general, "
                        + "detalle.cuenta_contable, "
                        + "detalle.debe, "
                        + "detalle.haber FROM contabilidad.detalle_transaccion detalle "
                        + "JOIN contabilidad.diario_general d ON detalle.diario_general = d.id  "
                        + "JOIN public.cuenta_contable c ON detalle.cuenta_contable = c.id  "
                        + "WHERE to_char(fecha_elaboracion,'YYYY-MM') = ?1 "
                        + "AND c.id = ?2 AND d.tipo <> ?3 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuenta)
                        .setParameter(3, idTransaccionCierre);
                result = query.getResultList();
            } else {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.diario_general, "
                        + "detalle.cuenta_contable, "
                        + "detalle.debe, "
                        + "detalle.haber FROM contabilidad.detalle_transaccion detalle "
                        + "JOIN contabilidad.diario_general d ON detalle.diario_general = d.id  "
                        + "JOIN public.cuenta_contable c ON detalle.cuenta_contable = c.id  "
                        + "WHERE to_char(fecha_elaboracion,'YYYY-MM') = ?1 "
                        + "AND c.id = ?2 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuenta);
                result = query.getResultList();
            }
            if (result != null) {
                List<DetalleTransaccion> list = new ArrayList<>();
                for (Object[] data : result) {
                    DetalleTransaccion d = new DetalleTransaccion(((BigInteger) data[0]).longValue());
                    d.setDiarioGeneral((new DiarioGeneral((Long) data[1])));
                    d.setCuentaContable(new CuentaContable((Long) data[2]));
                    d.setDebe((BigDecimal) data[3]);
                    d.setHaber((BigDecimal) data[4]);
                    list.add(d);
                }
                return list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DetalleTransaccion findDetalleTransaccionById(Long id) {
        try {
            return (DetalleTransaccion) em.createQuery("SELECT d FROM DetalleTransaccion d WHERE d.id = ?1")
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CuentaContable findCuentaContableHijasDebeAndHaber(Long idCuentaPadre) {
        try {
            List<Object[]> result = new ArrayList<>();
            result = em.createNativeQuery("SELECT sum(c.saldo_inicial_debe) as debe, sum(c.saldo_inicial_haber) as haber "
                    + "FROM public.cuenta_contable c "
                    + "WHERE c.id = ?1")
                    .setParameter(1, idCuentaPadre)
                    .getResultList();
            if (result != null) {
                List<CuentaContable> list = new ArrayList<>();
                for (Object[] data : result) {
                    CuentaContable cuenta = new CuentaContable();
                    cuenta.setSaldoInicialDebe((BigDecimal) data[0]);
                    cuenta.setSaldoInicialHaber((BigDecimal) data[1]);
                    list.add(cuenta);
                }
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleTransaccion> findAllDiarioDetalleByCuentaAndFechaElaboracion(Long idCuenta, String fechaDesde, String fechaHasta) {
        return em.createQuery("SELECT d FROM DetalleTransaccion d "
                + "WHERE d.cuentaContable.id = ?1 "
                + "AND FUNCTION('to_char', d.diarioGeneral.fechaElaboracion, 'YYYY-MM-DD') >= ?2 "
                + "AND FUNCTION('to_char', d.diarioGeneral.fechaElaboracion, 'YYYY-MM-DD') <= ?3 "
                //  + "AND d.diarioGeneral.fechaElaboracion BETWEEN ?2 AND ?3 "
                + "AND d.diarioGeneral.estado = true ORDER BY d.diarioGeneral.numeroTransaccion")
                .setParameter(1, idCuenta)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .getResultList();

    }

    public List<DetalleTransferencias> getSinAcreditar(Transferencias transferencias) {
        List<DetalleTransferencias> resultado = (List<DetalleTransferencias>) em.createQuery("SELECT d FROM DetalleTransferencias d WHERE d.transferencia=:transferencia AND d.fechaAcreditacion is null AND d.fechaAnulacion is null ORDER BY d.referencia ASC")
                .setParameter("transferencia", transferencias)
                .getResultList();
        return resultado;
    }
}
