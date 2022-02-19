/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AsientosContables;
import com.origami.sigef.common.entities.EjecucionPresupuestaria;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class EjecucionPresupuestariaService extends AbstractService<EjecucionPresupuestaria> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EjecucionPresupuestariaService() {
        super(EjecucionPresupuestaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /*MOVIMIENTOS DE PARTIDAS INDIVIDUALES*/
    public String identificarPartida(Short periodo, String partida) {
        try {
            String query = (String) em.createQuery("select ppn.codigo from ProformaPresupuestoPlanificado ppn WHERE ppn.partidaPresupuestaria = :partida AND ppn.codigoReforma IS NULL "
                    + " AND ppn.codigoReformaTraspaso IS NULL AND ppn.periodo = :periodo")
                    .setParameter("periodo", periodo).setParameter("partida", partida).getSingleResult();
//            String result = (String) query.getSingleResult();
            return query;
        } catch (NoResultException e) {
            return null;
        }
    }

    /*REFORMAS*/
    public BigDecimal getReformaByPartidaFechaDesdeHastaIngresos(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta) {
        try {
            Query query;
            String condicion = "";
            String movimientosPartidasIndividuales = "AND cp.codigo = SUBSTRING ( ?4 ,1 , 6 )";
            String ejecucionPresupuestaria = "AND cp.codigo LIKE (?4 || '%') ";

            if (reporte.equals("MOV")) {
                condicion = movimientosPartidasIndividuales;
            } else if (reporte.equals("EP")) {
                condicion = ejecucionPresupuestaria;
            }
            if (reporte.equals("AP")) {/*PARA LOS ARCHIVOS PLANOS*/
                query = em.createNativeQuery("SELECT COALESCE(sum(dt.suplemento) - sum(dt.reducido),0)\n"
                        + "FROM presupuesto.detalle_reforma_ingreso_suplemento dt\n"
                        + "INNER JOIN presupuesto.proforma_ingreso pi ON pi.id = dt.proforma_ingreso\n"
                        + "INNER JOIN presupuesto.pres_catalogo_presupuestario cp ON cp.id = pi.item\n"
                        + "INNER JOIN presupuesto.reforma_ingreso_suplemento ri ON dt.reforma =  ri.id\n"
                        + "INNER JOIN catalogo_item ca ON ca.id = ri.estado\n"
                        + "WHERE cp.estado = true AND ca.codigo = 'APRO' AND pi.periodo = ?1 "
                        + "AND to_char(ri.fecha_aprobacion , 'YYYY-MM') >= ?2 AND to_char(ri.fecha_aprobacion , 'YYYY-MM') <= ?3 "
                        + "AND cp.codigo = ?4")
                        .setParameter(1, anio).setParameter(2, desde).setParameter(3, hasta).setParameter(4, partida);
            } else {

                query = em.createNativeQuery("(SELECT COALESCE(sum(dt.suplemento) - sum(dt.reducido),0)\n"
                        + "FROM presupuesto.detalle_reforma_ingreso_suplemento dt\n"
                        + "INNER JOIN presupuesto.proforma_ingreso pi ON pi.id = dt.proforma_ingreso\n"
                        + "INNER JOIN presupuesto.pres_catalogo_presupuestario cp ON cp.id = pi.item\n"
                        + "INNER JOIN presupuesto.reforma_ingreso_suplemento ri ON dt.reforma =  ri.id\n"
                        + "INNER JOIN catalogo_item ca ON ca.id = ri.estado\n"
                        + "WHERE cp.estado = true AND ca.codigo = 'APRO' AND pi.periodo = ?1 "
                        + "AND CAST(ri.fecha_aprobacion as DATE) BETWEEN ?2 AND ?3 "
                        + condicion
                        + ")")
                        .setParameter(1, anio).setParameter(2, fecha_desde).setParameter(3, fecha_hasta).setParameter(4, partida);
            }
            BigDecimal result = (BigDecimal) query.getSingleResult();

            if (result == null) {
                result = BigDecimal.ZERO;
            }
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query Ingreso: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getReformaByPartidaFechaDesdeHastaPAPP(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta, Boolean mov) {
        try {
            System.out.println("anio: " + anio + ", fecha_desde: " + fecha_desde + ", fecha_hasta" + fecha_hasta + ", partida: " + partida + ", reporte: " + reporte + ", desde: " + desde + ", hasta: " + hasta + ", mov: " + mov);
            Query query;
            String condicion = "";
            String movimientosPartidasIndividuales = "AND pr.codigo_presupuestario = ?4 ";
            String ejecucionPresupuestaria = "AND crp.codigo LIKE (?4||'%') "; //REVISAR
            if (reporte.equals("MOV")) {
                condicion = movimientosPartidasIndividuales;
            } else if (reporte.equals("EP")) {
                condicion = ejecucionPresupuestaria;
            }

            if (reporte.equals("AP")) {/*PARA LOS ARCHIVOS PLANOS*/
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
                df1.parse(desde);
                Calendar fecha = df1.getCalendar();
                Date fechaDesde = fecha.getTime();
                df2.parse(hasta);
                Calendar fechaH = df2.getCalendar();
                fechaH.set(Calendar.DAY_OF_MONTH, fechaH.getActualMaximum(Calendar.DAY_OF_MONTH));
                fechaH.add(Calendar.DAY_OF_MONTH, 1);
                fechaH.add(Calendar.SECOND, -1);
                Date fechaHasta = fechaH.getTime();
                query = em.createNativeQuery("select presupuesto.fct_reforma_producto_codigo_ip(?1,?2,?3, ?4, ?5)")
                        .setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, anio).setParameter(4, partida).setParameter(5, mov);
//                query = em.createNativeQuery("SELECT "
//                        + "(sum(sum(pr.traspaso_incremento)) over (order by pr.id desc))+ "
//                        + "(sum(sum(pr.suplemento_egreso)) over (order by pr.id desc)) - "
//                        + "(sum(sum(pr.traspaso_reduccion)) over (order by pr.id desc)) - "
//                        + "(sum(sum(pr.reduccion_egreso)) over (order by pr.id desc)) as reforma "
//                        + "FROM producto pr INNER JOIN catalogo_item e on e.id = pr.estado_papp INNER JOIN presupuesto.pres_catalogo_presupuestario crp on crp.id = pr.item_new "
//                        //                    + "LEFT JOIN cuentacontable_catalogopresupuesto cccp ON cccp.catalogo_presupuesto = crp.id "
//                        //                    + "LEFT JOIN cuenta_contable cc ON cc.id = cccp.cuenta_contable "
//                        + "WHERE (pr.codigo_reforma_traspaso IS NOT NULL OR pr.codigo_reforma IS NOT NULL) AND pr.periodo = ?1 "
//                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
//                        + "WHERE r.id =pr.codigo_reforma_traspaso  and to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?2 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?3 )='APRT' "
//                        + "OR "
//                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
//                        + "WHERE r.id =pr.codigo_reforma AND to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?2 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?3 )='APRO') "
//                        + "AND crp.codigo = ?4 "
//                        + "GROUP BY pr.codigo_presupuestario, pr.traspaso_incremento, pr.suplemento_egreso, "
//                        + "pr.estado_papp, e.codigo, pr.codigo_reforma, pr.codigo_reforma_traspaso, pr.id, "
//                        + "pr.traspaso_reduccion, pr.reduccion_egreso, pr.periodo order by pr.id asc limit 1 ")
//                        .setParameter(1, anio).setParameter(2, desde).setParameter(3, hasta).setParameter(4, partida);
            } else {

                query = em.createNativeQuery("SELECT "
                        + "(sum(sum(pr.traspaso_incremento)) over (order by pr.id desc))+ "
                        + "(sum(sum(pr.suplemento_egreso)) over (order by pr.id desc)) - "
                        + "(sum(sum(pr.traspaso_reduccion)) over (order by pr.id desc)) - "
                        + "(sum(sum(pr.reduccion_egreso)) over (order by pr.id desc)) as reforma "
                        + "FROM producto pr INNER JOIN catalogo_item e on e.id = pr.estado_papp "
                        + "INNER JOIN presupuesto.pres_catalogo_presupuestario crp on crp.id = pr.item_new "
                        //                    + "LEFT JOIN cuentacontable_catalogopresupuesto cccp ON cccp.catalogo_presupuesto = crp.id "
                        //                    + "LEFT JOIN cuenta_contable cc ON cc.id = cccp.cuenta_contable "
                        + "WHERE (pr.codigo_reforma_traspaso IS NOT NULL OR pr.codigo_reforma IS NOT NULL) AND pr.periodo = ?1 "
                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
                        + "WHERE r.id =pr.codigo_reforma_traspaso  and CAST(r.fecha_aprobacion as DATE) BETWEEN ?2 AND ?3)='APRT' "
                        + "OR "
                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
                        + "WHERE r.id =pr.codigo_reforma AND CAST(r.fecha_aprobacion as DATE) BETWEEN ?2 AND ?3)='APRO') "
                        + condicion
                        + "GROUP BY pr.codigo_presupuestario, pr.traspaso_incremento, pr.suplemento_egreso, "
                        + "pr.estado_papp, e.codigo, pr.codigo_reforma, pr.codigo_reforma_traspaso, pr.id, "
                        + "pr.traspaso_reduccion, pr.reduccion_egreso, pr.periodo order by pr.id asc limit 1 ")
                        .setParameter(1, anio).setParameter(2, fecha_desde).setParameter(3, fecha_hasta).setParameter(4, partida);
            }
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getReformaByPartidaFechaDesdeHastaPD(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta, Boolean mov) {
        try {
            Query query;
            String condicion = "";
            String movimientosPartidasIndividuales = "AND pd.partida_presupuestaria = ?5 ";
            String ejecucionPresupuestaria = "AND SUBSTRING ( pd.partida_presupuestaria ,6 , 11 ) LIKE (?5||'%') ";
            if (reporte.equals("MOV")) {
                condicion = movimientosPartidasIndividuales;
            } else if (reporte.equals("EP")) {
                condicion = ejecucionPresupuestaria;
            }

            if (reporte.equals("AP")) {/*PARA LOS ARCHIVOS PLANOS*/
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
                df1.parse(desde);
                Calendar fecha = df1.getCalendar();
                Date fechaDesde = fecha.getTime();
                df2.parse(hasta);
                Calendar fechaH = df2.getCalendar();
                fechaH.set(Calendar.DAY_OF_MONTH, fechaH.getActualMaximum(Calendar.DAY_OF_MONTH));
                fechaH.add(Calendar.DAY_OF_MONTH, 1);
                fechaH.add(Calendar.SECOND, -1);
                Date fechaHasta = fechaH.getTime();
                query = em.createNativeQuery("select presupuesto.fct_reforma_distributivo_codigo_ip(?1,?2,?3, ?4, ?5)").setParameter(1, fechaDesde).setParameter(2, fechaHasta)
                        .setParameter(3, anio).setParameter(4, partida).setParameter(5, mov);
//                query = em.createNativeQuery("SELECT "
//                        + "(sum(sum(pd.traspaso_incremento)) over (order by pd.id ASC))+ "
//                        + "(sum(sum(pd.reforma_suplemento)) over (order by pd.id ASC)) - "
//                        + "(sum(sum(pd.traspaso_reduccion)) over (order by pd.id ASC)) - "
//                        + "(sum(sum(pd.reforma_reduccion)) over (order by pd.id ASC)) as straspaso "
//                        + "FROM talento_humano.th_cargo_rubros pd INNER JOIN catalogo_item e on e.id = pd.estado_partida "
//                        + " INNER JOIN talento_humano.th_rubro tr ON tr.id = pd.id_rubro "
//                        + "WHERE tr.origen = true AND (pd.codigo_reforma_traspaso IS NOT NULL OR pd.codigo_reforma IS NOT NULL) "
//                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
//                        + "WHERE r.id =pd.codigo_reforma_traspaso AND to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?1 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?2 )='APRT' "
//                        + "OR "
//                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
//                        + "WHERE r.id =pd.codigo_reforma AND to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?3 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?4 )='APRO') "
//                        + "AND SUBSTRING ( pd.partida_presupuestaria ,6 , 11 ) = ?5 AND pd.periodo = ?6 "
//                        + "GROUP BY pd.partida_presupuestaria, pd.traspaso_incremento, pd.reforma_suplemento, pd.traspaso_reduccion, "
//                        + "pd.estado_partida, e.codigo, pd.codigo_reforma, pd.codigo_reforma_traspaso, pd.id, pd.reforma_reduccion "
//                        + "order by pd.id desc limit 1").setParameter(1, desde).setParameter(2, hasta)
//                        .setParameter(3, desde).setParameter(4, hasta).setParameter(5, partida).setParameter(6, anio);
            } else {
                query = em.createNativeQuery("SELECT "
                        + "(sum(sum(pd.traspaso_incremento)) over (order by pd.id ASC))+ "
                        + "(sum(sum(pd.reforma_suplemento)) over (order by pd.id ASC)) - "
                        + "(sum(sum(pd.traspaso_reduccion)) over (order by pd.id ASC)) - "
                        + "(sum(sum(pd.reforma_reduccion)) over (order by pd.id ASC)) as straspaso "
                        + "FROM talento_humano.th_cargo_rubros pd INNER JOIN catalogo_item e on e.id = pd.estado_partida "
                        + " INNER JOIN talento_humano.th_rubro tr ON tr.id = pd.id_rubro "
                        + "WHERE tr.origen = true AND (pd.codigo_reforma_traspaso IS NOT NULL OR pd.codigo_reforma IS NOT NULL) "
                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
                        + "WHERE r.id =pd.codigo_reforma_traspaso AND CAST(r.fecha_aprobacion as DATE) BETWEEN ?1 AND ?2)='APRT' "
                        + "OR "
                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
                        + "WHERE r.id =pd.codigo_reforma AND CAST(r.fecha_aprobacion AS DATE) BETWEEN ?3 AND ?4)='APRO') "
                        + condicion + " AND pd.periodo = ?6 "
                        + "GROUP BY pd.partida_presupuestaria, pd.traspaso_incremento, pd.reforma_suplemento, pd.traspaso_reduccion, "
                        + "pd.estado_partida, e.codigo, pd.codigo_reforma, pd.codigo_reforma_traspaso, pd.id, pd.reforma_reduccion "
                        + "order by pd.id desc limit 1").setParameter(1, fecha_desde).setParameter(2, fecha_hasta)
                        .setParameter(3, fecha_desde).setParameter(4, fecha_hasta).setParameter(5, partida).setParameter(6, anio);
            }
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getReformaByPartidaFechaDesdeHastaPDA(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta) {
        try {
            Query query;
            String condicion = "";
            String movimientosPartidasIndividuales = "AND pda.partida_presupuestaria = ?5 ";
            String ejecucionPresupuestaria = "AND SUBSTRING ( pda.partida_presupuestaria ,6 , 11 ) LIKE (?5||'%') ";
            if (reporte.equals("MOV")) {
                condicion = movimientosPartidasIndividuales;
            } else if (reporte.equals("EP")) {
                condicion = ejecucionPresupuestaria;
            }

            if (reporte.equals("AP")) {/*PARA LOS ARCHIVOS PLANOS*/
                query = em.createNativeQuery("SELECT "
                        + "(sum(sum(pda.traspaso_incremento)) over (order by pda.id ASC))+ "
                        + "(sum(sum(pda.reforma_suplemento)) over (order by pda.id ASC)) - "
                        + "(sum(sum(pda.traspaso_reduccion)) over (order by pda.id ASC)) - "
                        + "(sum(sum(pda.reforma_reduccion)) over (order by pda.id ASC)) as straspaso "
                        + "FROM talento_humano.th_cargo_rubros pda INNER JOIN catalogo_item e on e.id = pda.estado_partida "
                        + " INNER JOIN talento_humano.th_rubro tr ON tr.id = pda.id_rubro "
                        + "WHERE tr.origen = false AND (pda.codigo_reforma_traspaso IS NOT NULL OR pda.codigo_reforma IS NOT NULL) "
                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
                        + "WHERE r.id =pda.codigo_reforma_traspaso AND to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?1 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?2 )='APRT' "
                        + "OR "
                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
                        + "WHERE r.id =pda.codigo_reforma AND to_char(r.fecha_aprobacion , 'YYYY-MM') >= ?3 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?4 )='APRO') "
                        + " AND SUBSTRING ( pda.partida_presupuestaria ,6 , 11 )= ?5  AND pda.periodo = ?6 "
                        + "GROUP BY pda.partida_presupuestaria, pda.traspaso_incremento, pda.reforma_suplemento, pda.estado_partida, "
                        + "e.codigo, pda.codigo_reforma, pda.codigo_reforma_traspaso, pda.id, pda.traspaso_reduccion, "
                        + "pda.reforma_reduccion order by pda.id desc limit 1").setParameter(1, desde).setParameter(2, hasta)
                        .setParameter(3, desde).setParameter(4, hasta).setParameter(5, partida).setParameter(6, anio);
            } else {
                query = em.createNativeQuery("SELECT "
                        + "(sum(sum(pda.traspaso_incremento)) over (order by pda.id ASC))+ "
                        + "(sum(sum(pda.reforma_suplemento)) over (order by pda.id ASC)) - "
                        + "(sum(sum(pda.traspaso_reduccion)) over (order by pda.id ASC)) - "
                        + "(sum(sum(pda.reforma_reduccion)) over (order by pda.id ASC)) as straspaso "
                        + "FROM talento_humano.th_cargo_rubros pda INNER JOIN catalogo_item e on e.id = pda.estado_partida "
                        + " INNER JOIN talento_humano.th_rubro tr ON tr.id = pda.id_rubro "
                        + "WHERE tr.origen = false AND (pda.codigo_reforma_traspaso IS NOT NULL OR pda.codigo_reforma IS NOT NULL) "
                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma "
                        + "WHERE r.id =pda.codigo_reforma_traspaso AND CAST(r.fecha_aprobacion AS DATE) BETWEEN ?1 AND ?2)='APRT' "
                        + "OR "
                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado "
                        + "WHERE r.id =pda.codigo_reforma AND CAST(r.fecha_aprobacion AS DATE) BETWEEN ?3 AND ?4)='APRO') "
                        + condicion + " AND pda.periodo = ?6 "
                        + "GROUP BY pda.partida_presupuestaria, pda.traspaso_incremento, pda.reforma_suplemento, pda.estado_partida, "
                        + "e.codigo, pda.codigo_reforma, pda.codigo_reforma_traspaso, pda.id, pda.traspaso_reduccion, "
                        + "pda.reforma_reduccion order by pda.id desc limit 1").setParameter(1, fecha_desde).setParameter(2, fecha_hasta)
                        .setParameter(3, fecha_desde).setParameter(4, fecha_hasta).setParameter(5, partida).setParameter(6, anio);
            }
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getReformaByPartidaFechaDesdeHastaPDI(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta, Boolean mov) {
        try {
            Query query;
            String condicion = "";
            String movimientosPartidasIndividuales = "AND pi.partida_presupuestaria = ?1 ";
            String ejecucionPresupuestaria = "AND SUBSTRING ( pi.partida_presupuestaria ,6 , 11 ) LIKE (?1||'%') ";
            if (reporte.equals("MOV")) {
                condicion = movimientosPartidasIndividuales;
            } else if (reporte.equals("EP")) {
                condicion = ejecucionPresupuestaria;
            }

            if (reporte.equals("AP")) {/*PARA LOS ARCHIVOS PLANOS*/
//                System.out.println("dEsde: "+desde+ "hasta; "+hasta);
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
                df1.parse(desde);
                Calendar fecha = df1.getCalendar();
                Date fechaDesde = fecha.getTime();
                df2.parse(hasta);
                Calendar fechaH = df2.getCalendar();
//                System.out.println("Time: "+df2.getCalendar().getTime());
                fechaH.set(Calendar.DAY_OF_MONTH, fechaH.getActualMaximum(Calendar.DAY_OF_MONTH));
                fechaH.add(Calendar.DAY_OF_MONTH, 1);
                fechaH.add(Calendar.SECOND, -1);
                Date fechaHasta = fechaH.getTime();

//                System.out.println("fechaDesde: " + fechaDesde + " fechaHasta: " + fechaHasta + " anio: " + anio + " partida: " + partida + " mov: " + mov);
                query = em.createNativeQuery("SELECT presupuesto.fct_reforma_pd_codigo_ip(?1,?2,?3,?4,?5)").setParameter(1, fechaDesde).setParameter(2, fechaHasta)
                        .setParameter(3, anio).setParameter(4, partida).setParameter(5, mov);
//                query = em.createNativeQuery("SELECT\n"
//                        + "(sum(sum(pi.traspaso_incremento)) over (order by pi.id ASC))+\n"
//                        + "(sum(sum(pi.reforma_suplemento)) over (order by pi.id ASC)) -\n"
//                        + "(sum(sum(pi.traspaso_reduccion)) over (order by pi.id ASC)) -\n"
//                        + "(sum(sum(pi.reforma_reduccion)) over (order by pi.id ASC)) as straspaso\n"
//                        + "FROM proforma_presupuesto_planificado pi INNER JOIN catalogo_item e on e.id = pi.estado_partida\n"
//                        + "WHERE (pi.codigo_reforma_traspaso IS NOT NULL OR pi.codigo_reforma IS NOT NULL)\n"
//                        + " AND SUBSTRING ( pi.partida_presupuestaria ,6 , 11 ) = ?1   AND pi.periodo = ?2 "
//                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma\n"
//                        + "WHERE r.id =pi.codigo_reforma_traspaso AND to_char(r.fecha_aprobacion , 'YYYY-MM')  >= ?3 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?4 )='APRT'\n"
//                        + "OR\n"
//                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado\n"
//                        + "WHERE r.id =pi.codigo_reforma AND to_char(r.fecha_aprobacion , 'YYYY-MM')  >= ?5 AND to_char(r.fecha_aprobacion , 'YYYY-MM') <= ?6 )='APRO') "
//                        + "GROUP BY pi.partida_presupuestaria, pi.traspaso_incremento, pi.reforma_suplemento, pi.estado_partida, e.codigo,\n"
//                        + "pi.codigo_reforma, pi.codigo_reforma_traspaso, pi.id, pi.traspaso_reduccion, pi.reforma_reduccion\n"
//                        + "order by pi.id desc limit 1").setParameter(1, partida).setParameter(2, anio)
//                        .setParameter(3, desde).setParameter(4, hasta).setParameter(5, desde).setParameter(6, hasta);
            } else {
                query = em.createNativeQuery("SELECT\n"
                        + "(sum(sum(pi.traspaso_incremento)) over (order by pi.id ASC))+\n"
                        + "(sum(sum(pi.reforma_suplemento)) over (order by pi.id ASC)) -\n"
                        + "(sum(sum(pi.traspaso_reduccion)) over (order by pi.id ASC)) -\n"
                        + "(sum(sum(pi.reforma_reduccion)) over (order by pi.id ASC)) as straspaso\n"
                        + "FROM proforma_presupuesto_planificado pi INNER JOIN catalogo_item e on e.id = pi.estado_partida\n"
                        + "WHERE (pi.codigo_reforma_traspaso IS NOT NULL OR pi.codigo_reforma IS NOT NULL)\n"
                        + condicion + "  AND pi.periodo = ?2 "
                        + "AND ((SELECT es.codigo FROM presupuesto.reforma_traspaso r INNER JOIN catalogo_item es on es.id = r.estado_reforma\n"
                        + "WHERE r.id =pi.codigo_reforma_traspaso AND CAST(r.fecha_aprobacion as DATE) BETWEEN ?3 AND ?4)='APRT'\n"
                        + "OR\n"
                        + "(SELECT es.codigo FROM presupuesto.reforma_ingreso_suplemento r INNER JOIN catalogo_item es on es.id = r.estado\n"
                        + "WHERE r.id =pi.codigo_reforma AND CAST(r.fecha_aprobacion as DATE) BETWEEN ?5 AND ?6)='APRO') "
                        + "GROUP BY pi.partida_presupuestaria, pi.traspaso_incremento, pi.reforma_suplemento, pi.estado_partida, e.codigo,\n"
                        + "pi.codigo_reforma, pi.codigo_reforma_traspaso, pi.id, pi.traspaso_reduccion, pi.reforma_reduccion\n"
                        + "order by pi.id desc limit 1").setParameter(1, partida).setParameter(2, anio)
                        .setParameter(3, fecha_desde).setParameter(4, fecha_hasta).setParameter(5, fecha_desde).setParameter(6, fecha_hasta);
            }
//            System.out.println("Result: " + query.getSingleResult());
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getReformaEgresos(Short anio, Date fecha_desde, Date fecha_hasta, String partida, String reporte, String desde, String hasta, Boolean movimiento) {
        BigDecimal totalReformas = BigDecimal.ZERO;
        BigDecimal reformaPAPP = getReformaByPartidaFechaDesdeHastaPAPP(anio, fecha_desde, fecha_hasta, partida, reporte, desde, hasta, movimiento);
        BigDecimal reformaPD = getReformaByPartidaFechaDesdeHastaPD(anio, fecha_desde, fecha_hasta, partida, reporte, desde, hasta, movimiento);
//        BigDecimal reformaPDA = getReformaByPartidaFechaDesdeHastaPDA(anio, fecha_desde, fecha_hasta, partida, reporte, desde, hasta);
        BigDecimal reformaPDI = getReformaByPartidaFechaDesdeHastaPDI(anio, fecha_desde, fecha_hasta, partida, reporte, desde, hasta, movimiento);
//        System.out.println("reformaPDI: " + reformaPDI);
        totalReformas = reformaPAPP.add(reformaPD).add(reformaPDI);
        return totalReformas;
    }

    /*EJECUCION PRESUPUESTARIA*/
    public List<AsientosContables> getEstadoSituacionF(Long codigo) {
        try {
            List<AsientosContables> resultado = (List<AsientosContables>) em.createQuery("SELECT a FROM AsientosContables a Where a.tipo = ?1 ORDER BY a.orden ASC")
                    .setParameter(1, BigInteger.valueOf(codigo))
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public PresCatalogoPresupuestario getCatalogoPresupuesto(String codigo, Integer nivel) {
        try {
            Query query = em.createQuery("SELECT cp FROM PresCatalogoPresupuestario cp INNER JOIN cp.confId n "
                    + "WHERE n.nivel = :nivel AND cp.codigo = :codigo ")
                    .setParameter("codigo", codigo).setParameter("nivel", nivel);
            PresCatalogoPresupuestario result = (PresCatalogoPresupuestario) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public ProformaIngreso getProformaIngreso(String codigo, Integer nivel, Short periodo) {
        try {
            Query query = em.createQuery("SELECT pi FROM ProformaIngreso pi JOIN pi.item cp INNER JOIN cp.confId n "
                    + "WHERE n.nivel = :nivel AND cp.codigo = :codigo AND pi.periodo = :per ")
                    .setParameter("codigo", codigo).setParameter("nivel", nivel).setParameter("per", periodo);
            ProformaIngreso result = (ProformaIngreso) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCuentaContableByCatalogoPresupuesto(Short periodo, String codigo, String gastos) {
        try {
            Query query = (Query) em.createNativeQuery("SELECT cc.codigo from contabilidad.cont_cuenta_partida pc INNER JOIN contabilidad.cont_cuentas cc on cc.id = pc.id_cuenta\n"
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cp on cp.id = pc.id_partida_1\n"
                    + "INNER JOIN conf.plan_cuentas cx on cx.id = cc.conf_id \n"
                    + "INNER JOIN conf.plan_cuentas ccp on ccp.id = cp.conf_id "
                    + " WHERE ccp.nivel = 2 AND cx.nivel = 4 AND cp.codigo = ?2 AND cc.codigo LIKE ?3")
                    .setParameter(2, codigo).setParameter(3, gastos + "%");
            String result = (String) query.getResultStream().findFirst().orElse("-");
            return result;
        } catch (NoResultException e) {
            return "-";
        }
    }

    public String identificarCatalogoPresupuesto(Short periodo, String partida) {
        try {
            String query = (String) em.createQuery("select ppn.codigo from ProformaPresupuestoPlanificado ppn WHERE ppn.partidaPresupuestaria = :partida AND ppn.periodo = :periodo")
                    .setParameter("periodo", periodo).setParameter("partida", partida).getSingleResult();
//            String result = (String) query.getSingleResult();
            return query;
        } catch (NoResultException e) {
            return null;
        }
    }

    public BigDecimal getPresupuestoInicialIngresosByCatalogoPresupuesto(Short anio, String codigo_cuenta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("select COALESCE(sum(pi.presupuesto_inicial),0.00) from presupuesto.proforma_ingreso pi inner join presupuesto.pres_catalogo_presupuestario cap ON cap.id = pi.item\n"
                    + "where pi.periodo = ?1 and cap.ingreso = true and cap.movimiento = true and cap.codigo like ?2").setParameter(1, anio).setParameter(2, codigo_catalogo + "%");
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPresupuestoInicialEgresosByCatalogoPresupuesto(Short anio, String codigo_cuenta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("SELECT COALESCE(sum(ppn.valor),0.00)\n"
                    + "FROM proforma_presupuesto_planificado ppn\n"
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cap on cap.id = ppn.item_new\n"
                    + "WHERE  ppn.periodo = ?1 AND cap.ingreso = false\n"
                    + "AND ppn.codigo_reforma_traspaso IS NULL AND ppn.codigo_reforma IS NULL\n"
                    + "AND cap.codigo LIKE ?2 ").setParameter(1, anio).setParameter(2, codigo_catalogo + "%");
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getDevengadoByCuenta(Short anio, Date fecha_desde, Date fecha_hasta, String codigo_cuenta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("SELECT COALESCE(sum(dt.devengado),0.00) FROM contabilidad.cont_diario_general_detalle dt "
                    + "INNER JOIN contabilidad.cont_diario_general dg ON dg.id = dt.id_cont_diario_general "
                    + "INNER JOIN catalogo_item ti on ti.id = dg.tipo "
                    + "LEFT JOIN presupuesto.pres_catalogo_presupuestario cat ON dt.id_pres_catalogo_presupuestario = cat.id "
                    //                    + "/*LEFT JOIN cuentacontable_catalogopresupuesto cccp ON cccp.catalogo_presupuesto = cat.id "
                    //                    + "LEFT JOIN cuenta_contable cc ON cc.id = cccp.cuenta_contable */ "
                    + "WHERE  ti.codigo IN ('tipo_financiero','tipo_ajuste', 'tipo_presupuesto') "
                    + "  AND cast(dg.fecha_registro as DATE) BETWEEN ?1 AND ?2 AND cat.codigo LIKE ?3 "
            //                    + "  /*AND cc.codigo LIKE ?4*/ "
            ).setParameter(1, fecha_desde).setParameter(2, fecha_hasta) //dame unos minutos porfa
                    .setParameter(3, codigo_catalogo + "%");
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getSumCodificadoOrDevengadoByCodigo(String codigo, String usuario, boolean codificado) {
        try {
            Query query;
            if (codificado) {
                query = em.createNativeQuery(" SELECT COALESCE(sum(ep.codificado),0.00) FROM presupuesto.ejecucion_presupuestaria ep \n"
                        + "  INNER JOIN contabilidad.asientos_contables ac on ep.asiento_contable = ac.id\n"
                        + "  WHERE ac.grupo = ?1").setParameter(1, codigo);
            } else {
                query = em.createNativeQuery("SELECT COALESCE(sum(ep.devengado),0.00) FROM presupuesto.ejecucion_presupuestaria ep \n"
                        + "  INNER JOIN contabilidad.asientos_contables ac on ep.asiento_contable = ac.id\n"
                        + "  WHERE ac.grupo = ?1").setParameter(1, codigo);
            }
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch: 0.00");
            return BigDecimal.ZERO;
        }
    }

    /*DEVENGADO DE ARCHIVOS PLANOS*/
    public BigDecimal getDevengadoByCodigoItemPresupuestario(Short anio, String fecha_desde, String fecha_hasta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("SELECT COALESCE(sum(dt.devengado),0.00) FROM contabilidad.cont_diario_general_detalle dt\n"
                    + "INNER JOIN contabilidad.cont_diario_general dg ON dg.id = dt.id_cont_diario_general\n"
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cat ON dt.id_pres_catalogo_presupuestario = cat.id\n"
                    + "INNER JOIN catalogo_item ti on ti.id = dg.tipo \n"
                    + "WHERE  ti.codigo IN ('tipo_financiero','tipo_ajuste', 'tipo_presupuesto')\n"
                    + "AND to_char(dg.fecha_registro , 'YYYY-MM') <= ?1 AND to_char(dg.fecha_registro , 'YYYY-MM') >= ?2 AND cat.codigo like ?3 \n"
                    + "AND dg.periodo = ?4").setParameter(1, fecha_hasta).setParameter(2, fecha_desde)
                    .setParameter(3, codigo_catalogo + "%").setParameter(4, anio);
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    /*COMPROMETIDO DE EGRESOS DE CEDULA ARCHIVOS PLANOS*/
    public BigDecimal getComprometidoEgresosByCodigoItemPresupuestario(Short anio, String fecha_desde, String fecha_hasta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("SELECT COALESCE((SELECT COALESCE(sum(dsc.monto_comprometido),0.00) FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso dsc \n"
                    + "INNER JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso sr ON sr.id = dsc.solicitud \n"
                    + "INNER JOIN catalogo_item ca ON ca.id = sr.estado \n"
                    + "WHERE to_char(sr.fecha_aprobacion , 'YYYY-MM') >= ?1 AND to_char(sr.fecha_aprobacion , 'YYYY-MM') <= ?2 AND dsc.estado = true \n"
                    + "AND ca.codigo in ('APRO','LIQUI','LIBE') AND \n"
                    + "(CASE WHEN dsc.actividad_producto IS NOT NULL \n"
                    + "     THEN \n"
                    + "         (SELECT cpro.codigo FROM producto pro \n"
                    + "          INNER JOIN presupuesto.pres_catalogo_presupuestario cpro on cpro.id = pro.item_new  \n"
                    + "          WHERE cpro.ingreso = false AND pro.periodo = ?3\n"
                    + "          AND pro.id = dsc.actividad_producto) \n"
                    + "     WHEN dsc.partidas_directa IS NOT NULL \n"
                    + "     THEN \n"
                    + " 	(SELECT cpre.codigo FROM proforma_presupuesto_planificado pr   \n"
                    + "          INNER JOIN presupuesto.pres_catalogo_presupuestario cpre on cpre.id = pr.item_new\n"
                    + "          WHERE cpre.ingreso = false AND pr.periodo = ?3 \n"
                    + "          AND pr.id = dsc.partidas_directa) \n"
                    + "     WHEN dsc.re_distributivo IS NOT NULL \n"
                    + "     THEN \n"
                    + " 	(SELECT cpre.codigo FROM talento_humano.th_cargo_rubros th\n"
                    + "          INNER JOIN presupuesto.pres_catalogo_presupuestario cpre on cpre.id = th.id_presupuesto\n"
                    + "          WHERE cpre.ingreso = false AND th.periodo = ?3\n"
                    + "          AND th.id = dsc.re_distributivo) \n"
                    + "    ELSE (SELECT cpre.codigo FROM presupuesto pre  \n"
                    + "          INNER JOIN presupuesto.pres_catalogo_presupuestario cpre on cpre.id = pre.item_new\n"
                    + "          WHERE cpre.ingreso = false AND pre.periodo = ?3 \n"
                    + "          AND pre.id = dsc.presupuesto) \n"
                    + "END) LIKE ?4 ), 0)"
                    + " + "
                    + "COALESCE((SELECT sum(dt.comprometido)\n"
                    + "FROM contabilidad.cont_diario_general_detalle dt\n"
                    + " INNER JOIN contabilidad.cont_diario_general dg ON dg.id = dt.id_cont_diario_general\n"
                    + " INNER JOIN presupuesto.pres_catalogo_presupuestario cat ON dt.id_pres_catalogo_presupuestario = cat.id\n"
                    + "INNER JOIN catalogo_item t ON t.id = dg.tipo\n"
                    + "WHERE to_char(dg.fecha_registro , 'YYYY-MM')  >= ?1 AND to_char(dg.fecha_registro , 'YYYY-MM') <= ?2  AND cat.estado = true\n"
                    + "AND dg.periodo = ?3 AND t.codigo in ('tipo_financiero','tipo_presupuesto') AND cat.estado = true\n"
                    + "AND dt.id_detalle_reserva_compromiso IS NULL\n"
                    + "AND cat.codigo LIKE ?4),0)").setParameter(1, fecha_desde).setParameter(2, fecha_hasta)
                    .setParameter(3, anio).setParameter(4, codigo_catalogo + "%");
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getRecaudadoIngresosByCodigoItemPresupuestario(Short anio, String fecha_desde, String fecha_hasta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery(" SELECT COALESCE(sum(dt.ejecutado),0.00) FROM contabilidad.cont_diario_general_detalle dt\n"
                    + " INNER JOIN contabilidad.cont_diario_general dg ON dg.id = dt.id_cont_diario_general\n"
                    + " LEFT JOIN presupuesto.pres_catalogo_presupuestario cat ON dt.id_pres_catalogo_presupuestario = cat.id\n"
                    + " INNER JOIN catalogo_item ti on ti.id = dg.tipo\n"
                    + " WHERE cat.ingreso = true \n"
                    + " AND ti.codigo in ('tipo_financiero','tipo_ajuste', 'tipo_presupuesto')\n"
                    + " AND to_char(dg.fecha_registro , 'YYYY-MM') >= ?1 AND to_char(dg.fecha_registro, 'YYYY-MM') <=  ?2 \n"
                    + " AND cat.codigo like ?3 AND dg.periodo = ?4").setParameter(1, fecha_desde).setParameter(2, fecha_hasta)
                    .setParameter(3, codigo_catalogo + "%").setParameter(4, anio);
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPagadoEgresosByCodigoItemPresupuestario(Short anio, String fecha_desde, String fecha_hasta, String codigo_catalogo) {
        try {
            Query query = em.createNativeQuery("SELECT COALESCE(  "
                    + "(  "
                    + "COALESCE((select sum(COALESCE ( d.ejecutado,0)) as pagado from contabilidad.cont_diario_general_detalle d "
                    + "INNER JOIN contabilidad.cont_diario_general dg  ON dg.id = d.id_cont_diario_general "
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cap ON cap.id = d.id_pres_catalogo_presupuestario "
                    + "INNER JOIN catalogo_item t ON t.id = dg.tipo "
                    + "WHERE cap.estado = true  AND dg.estado = true "
                    + "AND to_char(dg.fecha_registro , 'YYYY-MM') >= ?1 AND to_char(dg.fecha_registro , 'YYYY-MM') <=  ?2 "
                    + "AND t.codigo in  ('tipo_ajuste' ,'tipo_financiero', 'tipo_presupuesto') "
                    + "AND cap.codigo = p.codigo),0.00) +  "
                    + "COALESCE((SELECT sum(COALESCE(dc.ejecutado,0)) as pagado FROM contabilidad.cont_comprobante_pago d "
                    + "INNER JOIN contabilidad.cont_diario_general_detalle dc ON dc.id_cont_comprobante_pago = d.id "
                    + "INNER JOIN contabilidad.cont_diario_general dg ON dg.id = d.id_cont_diario_general "
                    + "INNER JOIN catalogo_item t ON t.id = dg.tipo "
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cap ON cap.id = dc.id_pres_catalogo_presupuestario "
                    + "WHERE cap.estado = true  "
                    + " AND to_char(d.fecha_registro , 'YYYY-MM') >= ?1 AND to_char(d.fecha_registro , 'YYYY-MM') <=  ?2  "
                    + "AND t.codigo in  ('tipo_ajuste' ,'tipo_financiero','tipo_presupuesto') "
                    + "AND cap.codigo = p.codigo "
                    + "LIMIT 1),0.00) + "
                    + "	COALESCE((SELECT COALESCE(sum(dc.ejecutado),0) as pagado "
                    + "FROM contabilidad.cont_comprobante_pago d "
                    + "INNER JOIN contabilidad.cont_diario_general_detalle dc ON dc.id_cont_comprobante_pago = d.id "
                    + "INNER JOIN presupuesto.pres_catalogo_presupuestario cap ON cap.id = dc.id_pres_catalogo_presupuestario "
                    + "WHERE cap.estado = true AND d.id_cont_diario_general is null "
                    + "AND to_char(d.fecha_registro , 'YYYY-MM') >= ?1 AND to_char(d.fecha_registro , 'YYYY-MM')<=  ?2  "
                    + "AND cap.codigo = p.codigo ),0.00) "
                    + ") "
                    + "	,0.00) "
                    + "FROM presupuesto.pres_catalogo_presupuestario p "
                    + "WHERE p.estado = true AND p.ingreso = false  "
                    + " AND p.codigo = ?3").setParameter(1, fecha_desde).setParameter(2, fecha_hasta)
                    .setParameter(3, codigo_catalogo);
            BigDecimal result = (BigDecimal) query.getResultStream().findFirst().orElse(BigDecimal.ZERO);
            return result;
        } catch (NoResultException e) {
            System.out.println("Catch Query: 0.00");
            return BigDecimal.ZERO;
        }
    }

}
