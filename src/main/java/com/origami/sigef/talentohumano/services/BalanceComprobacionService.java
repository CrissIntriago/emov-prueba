/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BalanceComprobacion;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BalanceComprobacionService extends AbstractService<BalanceComprobacion> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BalanceComprobacionService() {
        super(BalanceComprobacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //usaré estos 
    //se usa para flujo efectivo y estado resultado
    public SaldoDebeHaberDTO getSaldosDebeHaberFlujoEfectivo(Date desde, Date hasta, Short anio, String codigo) {
        try {
            Query query = em.createNativeQuery(" SELECT sum(coalesce (a.sDebe,0)) as saldoDebe, sum(coalesce(a.sHaber,0)) as saldoHaber from  ( \n"
                    + "	select sum(COALESCE ( d.debe,0)) as sDebe , sum(COALESCE (d.haber,0)) as sHaber from contabilidad.cont_diario_general dg \n"
                    + "		inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "		inner join catalogo_item t ON t.id = dg.tipo \n"
                    + "		where dg.periodo = ?1\n"
                    + "		and dg.fecha_registro  BETWEEN  ?2 AND ?3 \n"
                    + "		AND (t.codigo in  ('tipo_ajuste' ,'tipo_financiero') OR t.codigo is null )\n"
                    + "		AND c.codigo LIKE ?4||'%' \n"
                    + "	UNION\n"
                    + "		select sum(COALESCE ( dcp.debe,0)) as sDebe, sum(COALESCE (dcp.haber,0)) as sHaber from contabilidad.cont_comprobante_pago cp\n"
                    + "		inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "		left join contabilidad.cont_diario_general dg on dg.id = cp.id_cont_diario_general\n"
                    + "		left join catalogo_item t ON t.id = dg.tipo\n"
                    + "		where cp.periodo = ?1\n"
                    + "		and cp.fecha_registro  BETWEEN ?2 AND ?3 \n"
                    + "		AND (t.codigo in  ('tipo_ajuste' ,'tipo_financiero') OR t.codigo is null )\n"
                    + "		AND c.codigo LIKE ?4||'%'\n"
                    + ") as a ", "SaldoDebeHaberMapping")
                    .setParameter(1, anio)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, codigo);
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
//balance general tipo sinafig

    public SaldoDebeHaberDTO getSaldosDebeHaberFlujoAcum(String codigo, Date desde, Date hasta, Short anio, Boolean cierre) {
        try {
            String tipo;
            if (cierre) {
                tipo = "";
            } else {
                tipo = ",'tipo_cierre'";
            }
            Query query = em.createNativeQuery(" SELECT sum(coalesce (a.sDebe,0)) as saldoDebe, sum(coalesce(a.sHaber,0)) as saldoHaber from  ( \n"
                    + "	select sum(COALESCE ( d.debe,0)) as sDebe , sum(COALESCE (d.haber,0)) as sHaber from contabilidad.cont_diario_general dg \n"
                    + "		inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "		inner join catalogo_item t ON t.id = dg.tipo \n"
                    + "		where dg.periodo = ?1 /*and dg.anulado = false*/\n"
                    + "		and dg.fecha_registro  BETWEEN  ?2 AND ?3 \n"
                    + "		AND (t.codigo NOT IN  ('tipo_apertura'" + tipo + ") OR t.codigo is null)  \n"
                    + "		AND c.codigo LIKE ?4||'%'\n"
                    + "	UNION\n"
                    + "		select sum(COALESCE ( dcp.debe,0)) as sDebe, sum(COALESCE (dcp.haber,0)) as sHaber from contabilidad.cont_comprobante_pago cp\n"
                    + "		inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "		left join contabilidad.cont_diario_general dg on dg.id = cp.id_cont_diario_general\n"
                    + "		left join catalogo_item t ON t.id = dg.tipo\n"
                    + "		where cp.periodo = ?1 /*and cp.estado = true*/ \n"
                    + "		and cp.fecha_registro  BETWEEN ?2 AND ?3 \n"
                    + "		AND (t.codigo NOT IN  ('tipo_apertura'" + tipo + ") OR t.codigo is null)\n"
                    + "		and c.codigo LIKE ?4||'%'\n"
                    + ") as a", "SaldoDebeHaberMapping")
                    .setParameter(1, anio)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, codigo);
            return (SaldoDebeHaberDTO) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SaldoDebeHaberDTO getSaldosDebeHaberInicioAcum(String codigo, Date desde, Date hasta, Short anio) {
        try {
            Query query = em.createNativeQuery("SELECT sum(coalesce (a.sDebe,0)) as saldoDebe, sum(coalesce(a.sHaber,0)) as saldoHaber from  ( \n"
                    + "	select sum(COALESCE ( d.debe,0)) as sDebe , sum(COALESCE (d.haber,0)) as sHaber from contabilidad.cont_diario_general dg \n"
                    + "		inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "		inner join catalogo_item t ON t.id = dg.tipo \n"
                    + "		where dg.periodo = ?1 and dg.anulado = false\n"
                    + "		and dg.fecha_registro  BETWEEN  ?2 AND ?3 \n"
                    + "		AND t.codigo = 'tipo_apertura'  \n"
                    + "		AND c.codigo LIKE ?4||'%'\n"
                    + "	UNION\n"
                    + "		select sum(COALESCE ( dcp.debe,0)) as sDebe, sum(COALESCE (dcp.haber,0)) as sHaber from contabilidad.cont_comprobante_pago cp\n"
                    + "		inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "		left join contabilidad.cont_diario_general dg on dg.id = cp.id_cont_diario_general\n"
                    + "		left join catalogo_item t ON t.id = dg.tipo\n"
                    + "		where cp.periodo = ?1 and cp.estado = true \n"
                    + "		and cp.fecha_registro  BETWEEN ?2 AND ?3 \n"
                    + "		AND t.codigo = 'tipo_apertura'  \n"
                    + "		and c.codigo LIKE ?4||'%'\n"
                    + "	UNION\n"
                    + "		select Coalesce(si.saldo_debe,0)as sDebe, Coalesce(si.saldo_haber,0) as sHaber from contabilidad.cont_saldo_inicial si \n"
                    + "		inner join contabilidad.cont_cuentas c on c.id = si.id_cuenta\n"
                    + "		where si.periodo = ?1 AND ( Coalesce(si.saldo_debe,0) > 0 or Coalesce(si.saldo_haber,0) > 0 ) and c.codigo LIKE ?4||'%'\n"
                    + ") as a", "SaldoDebeHaberMapping")
                    .setParameter(1, anio)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, codigo);
            return (SaldoDebeHaberDTO) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //no acumula
    public SaldoDebeHaberDTO getSaldosDebeHaberFlujoNoAcum(String codigo, Date desde, Date hasta, Short anio, Boolean cierre) {
        try {
            String tipo;
            if (cierre) {
                tipo = "";
            } else {
                tipo = ",'tipo_cierre'";
            }
            Query query = em.createNativeQuery("SELECT sum(coalesce (a.sDebe,0)) as saldoDebe, sum(coalesce(a.sHaber,0)) as saldoHaber from  ( \n"
                    + "	select sum(COALESCE ( d.debe,0)) as sDebe , sum(COALESCE (d.haber,0)) as sHaber from contabilidad.cont_diario_general dg \n"
                    + "		inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "		inner join catalogo_item t ON t.id = dg.tipo \n"
                    + "		where dg.periodo = ?1 /*and dg.anulado = false*/\n"
                    + "		and dg.fecha_registro  BETWEEN  ?2 AND ?3 \n"
                    + "		AND (t.codigo NOT IN  ('tipo_apertura'" + tipo + ") OR t.codigo is null)  \n"
                    + "		AND c.codigo = ?4  \n"
                    + "	UNION\n"
                    + "		select sum(COALESCE ( dcp.debe,0)) as sDebe, sum(COALESCE (dcp.haber,0)) as sHaber from contabilidad.cont_comprobante_pago cp\n"
                    + "		inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "		left join contabilidad.cont_diario_general dg on dg.id = cp.id_cont_diario_general\n"
                    + "		left join catalogo_item t ON t.id = dg.tipo\n"
                    + "		where cp.periodo = ?1 /*and cp.estado = true*/ \n"
                    + "		and cp.fecha_registro  BETWEEN ?2 AND ?3 \n"
                    + "		AND (t.codigo NOT IN  ('tipo_apertura'" + tipo + ") OR t.codigo is null)\n"
                    + "		and c.codigo = ?4\n"
                    + ") as a", "SaldoDebeHaberMapping")
                    .setParameter(1, anio)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, codigo);
            return (SaldoDebeHaberDTO) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SaldoDebeHaberDTO getSaldosDebeHaberInicioNoAcum(String codigo, Date desde, Date hasta, Short anio) {
        try {
            Query query = em.createNativeQuery("SELECT sum(coalesce (a.sDebe,0)) as saldoDebe, sum(coalesce(a.sHaber,0)) as saldoHaber from  ( \n"
                    + "	select sum(COALESCE ( d.debe,0)) as sDebe , sum(COALESCE (d.haber,0)) as sHaber from contabilidad.cont_diario_general dg \n"
                    + "		inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "		inner join catalogo_item t ON t.id = dg.tipo \n"
                    + "		where dg.periodo = ?1 and dg.anulado = false\n"
                    + "		and dg.fecha_registro  BETWEEN  ?2 AND ?3 \n"
                    + "		AND t.codigo = 'tipo_apertura'  \n"
                    + "		AND c.codigo = ?4  \n"
                    + "	UNION\n"
                    + "		select sum(COALESCE ( dcp.debe,0)) as sDebe, sum(COALESCE (dcp.haber,0)) as sHaber from contabilidad.cont_comprobante_pago cp\n"
                    + "		inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "		inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "		left join contabilidad.cont_diario_general dg on dg.id = cp.id_cont_diario_general\n"
                    + "		left join catalogo_item t ON t.id = dg.tipo\n"
                    + "		where cp.periodo = ?1 and cp.estado = true \n"
                    + "		and cp.fecha_registro  BETWEEN ?2 AND ?3 \n"
                    + "		AND t.codigo = 'tipo_apertura'  \n"
                    + "		and c.codigo = ?4\n"
                    + "	UNION\n"
                    + "		select Coalesce(si.saldo_debe,0)as sDebe, Coalesce(si.saldo_haber,0) as sHaber from contabilidad.cont_saldo_inicial si \n"
                    + "		inner join contabilidad.cont_cuentas c on c.id = si.id_cuenta\n"
                    + "		where si.periodo = ?1 AND ( Coalesce(si.saldo_debe,0) > 0 or Coalesce(si.saldo_haber,0) > 0 ) and c.codigo = ?4\n"
                    + ") as a ", "SaldoDebeHaberMapping")
                    .setParameter(1, anio)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, codigo);
            return (SaldoDebeHaberDTO) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

///// traer todas las cuentas x catalogo si es o no YA ACTUALIZADO 
    /*
    Estado true = Gobierno || false = No Gobierno
     */
    public List<ContCuentas> getCuentasIfOrNotGobierno(Date desde, Date hasta, Short anio, Boolean estado) {
        try {
            Query query = em.createNativeQuery("select distinct c.* from contabilidad.cont_diario_general dg \n"
                    + "	inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "	inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "	where dg.periodo = ?1 /*and dg.anulado = false*/  and dg.fecha_registro BETWEEN ?2 AND ?3 and c.gobierno = ?4\n"
                    + "UNION\n"
                    + "select distinct c.* from contabilidad.cont_comprobante_pago cp\n"
                    + "	inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "	inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "	where cp.periodo = ?1 /*and cp.estado = true*/ And cp.fecha_registro BETWEEN ?2 AND ?3 	and c.gobierno = ?4\n"
                    + "UNION\n"
                    + "select distinct c.* from contabilidad.cont_saldo_inicial si \n"
                    + "	inner join contabilidad.cont_cuentas c on c.id = si.id_cuenta\n"
                    + "	where si.periodo = ?1 AND ( Coalesce(si.saldo_debe,0) > 0 or Coalesce(si.saldo_haber,0) > 0 ) and c.gobierno = ?4", ContCuentas.class)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(1, anio)
                    .setParameter(4, estado);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<String> getAllCuentasContablesPlus(Integer tamaño, Date desde, Date hasta, Short anio) {
        try {
            Query query = em.createNativeQuery("select distinct SUBSTRING(c.codigo,1, ?1) as cod from contabilidad.cont_diario_general dg \n"
                    + "	inner join contabilidad.cont_diario_general_detalle d ON d.id_cont_diario_general = dg.id \n"
                    + "	inner join contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas\n"
                    + "	where dg.periodo = ?4 and dg.anulado = false  and dg.fecha_registro BETWEEN ?2 AND ?3\n"
                    + "	UNION\n"
                    + "	select distinct SUBSTRING(c.codigo,1, ?1) as cod from contabilidad.cont_comprobante_pago cp\n"
                    + "	inner join contabilidad.cont_diario_general_detalle dcp ON dcp.id_cont_comprobante_pago = cp.id\n"
                    + "	inner join contabilidad.cont_cuentas c ON c.id = dcp.id_cont_cuentas\n"
                    + "	where cp.periodo = ?4 and cp.estado = true And cp.fecha_registro BETWEEN ?2 AND ?3 	\n"
                    + "	UNION\n"
                    + "	select distinct SUBSTRING (c.codigo,1,?1) as cod from contabilidad.cont_saldo_inicial si \n"
                    + "	inner join contabilidad.cont_cuentas c on c.id = si.id_cuenta\n"
                    + "	where si.periodo = ?4 AND ( Coalesce(si.saldo_debe,0) > 0 or Coalesce(si.saldo_haber,0) > 0 )")
                    .setParameter(1, tamaño)
                    .setParameter(2, desde)
                    .setParameter(3, hasta)
                    .setParameter(4, anio);
            List<String> resultado = query.getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteAll() {
        Query query = em.createNativeQuery("DELETE FROM contabilidad.balance_comprobacion ");
        query.executeUpdate();
//        System.out.println(query.executeUpdate());

    }
}
