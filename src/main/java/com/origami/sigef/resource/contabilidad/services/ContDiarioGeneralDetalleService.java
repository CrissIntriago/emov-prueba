/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContDiarioGeneralDetalleService extends AbstractService<ContDiarioGeneralDetalle> {

    private static final Logger LOG = Logger.getLogger(ContDiarioGeneralDetalleService.class.getName());

    @Inject
    private ContCuentasService contCuentasService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContDiarioGeneralDetalleService() {
        super(ContDiarioGeneralDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PartePresupuestariaModel> detalleReservaCompromiso(Long id, Integer cod_modulo, String cod_tipo) {
        List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery(QUERY.DETALLE_RESERVA_MODEL, "DetalleReservaMapping")
                .setParameter(1, id)
                .setParameter(2, cod_modulo)
                .setParameter(3, cod_tipo)
                .getResultList();
        return result;
    }

    public List<ContDiarioGeneralDetalle> cuentasComprobanteList(ContDiarioGeneral diarioGeneral) {
        List<PartePresupuestariaModel> temp = (List<PartePresupuestariaModel>) em.createNativeQuery(QUERY.CUENTAS_COMPROBANTE, "CuentaComprobanteMapping")
                .setParameter(1, diarioGeneral)
                .getResultList();
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        for (PartePresupuestariaModel item : temp) {
            ContDiarioGeneralDetalle object = new ContDiarioGeneralDetalle(contCuentasService.find(item.getIdtemp()), item.getSaldodisponible());
            result.add(object);
        }
        return result;
    }

    public List<ContDiarioGeneralDetalle> cuentasComprobantePartidaList(ContDiarioGeneral diarioGeneral) {
        List<PartePresupuestariaModel> temp = (List<PartePresupuestariaModel>) em.createNativeQuery(QUERY.CUENTAS_COMPROBANTE_PARTIDA, "CuentaComprobantePartidaMapping")
                .setParameter(1, diarioGeneral)
                .getResultList();
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        for (PartePresupuestariaModel item : temp) {
            ContDiarioGeneralDetalle object = new ContDiarioGeneralDetalle(item.getPartidapresupuestaria(), new PresCatalogoPresupuestario(item.getIdprescatalogopresupuestario()),
                    new PresPlanProgramatico(item.getIdpresplanprogramatico()), new PresFuenteFinanciamiento(item.getIdpresfuentefinanciamiento()), item.getSaldodisponible());
            result.add(object);
        }
        return result;
    }

    public boolean diarioDevengadoTotal(ContDiarioGeneral idContDiarioGeneral) {
        List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT * FROM contabilidad.fs_cuenta_comprobante(?1) WHERE saldodisponible > 0", "CuentaComprobanteMapping")
                .setParameter(1, idContDiarioGeneral)
                .getResultList();
        return result.isEmpty();
    }

    /**
     * * Metodo que trae el
     *
     * @param idCuenta contCuentas id
     * @param fechaDesde fecha desde numeroRegistro
     * @param fechaHasta fecha hasta numeroRegistro
     * @return retorna registros ContDiarioGeneralDetalle que sean de Diario
     * General
     */
    public List<ContDiarioGeneralDetalle> findAllDiarioGeneralDetalleByIdContCuentasAndFechaRegistro(Long idCuenta, Date fechaDesde, Date fechaHasta) {
        try {
            return em.createQuery("SELECT d FROM ContDiarioGeneralDetalle d WHERE d.idContCuentas.id = ?1 AND d.idContDiarioGeneral.fechaRegistro BETWEEN ?2 AND ?3 AND d.idContDiarioGeneral.anulado = false ORDER BY d.idContDiarioGeneral.numRegistro ")
                    .setParameter(1, idCuenta)
                    .setParameter(2, fechaDesde)
                    .setParameter(3, fechaHasta)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ContDiarioGeneralDetalle> findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistro(Long idCuenta, Date fechaDesde, Date fechaHasta) {
        try {
            return em.createQuery("SELECT d FROM ContDiarioGeneralDetalle d WHERE d.idContCuentas.id = ?1 AND d.idContComprobantePago.fechaRegistro BETWEEN ?2 AND ?3 AND d.idContComprobantePago.estado = true ORDER BY d.idContComprobantePago.numRegistro ")
                    .setParameter(1, idCuenta)
                    .setParameter(2, fechaDesde)
                    .setParameter(3, fechaHasta)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ContDiarioGeneralDetalle> findAllDiarioGeneralDetalleOfDetalleComprobantePagoByIdContCuentasAndFechaRegistroSinDiarioGeneral(Long idCuenta, Date fechaDesde, Date fechaHasta) {
        try {
            return em.createQuery("SELECT d FROM ContDiarioGeneralDetalle d WHERE d.idContCuentas.id = ?1 AND d.idContComprobantePago.fechaRegistro BETWEEN ?2 AND ?3 AND d.idContComprobantePago.estado = true AND d.idContComprobantePago.idContDiarioGeneral IS NULL ORDER BY d.idContComprobantePago.numRegistro  ")
                    .setParameter(1, idCuenta)
                    .setParameter(2, fechaDesde)
                    .setParameter(3, fechaHasta)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PartePresupuestariaModel> getParteEmisionDiaria(String fechaEmision) {
        try {
            List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery("select * from contabilidad.partes_emisiones(?1) order by rubro,saldodisponible desc", "parteEmisionMapping")
                    .setParameter(1, fechaEmision).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error en getParteEmisionDiaria - ContDiarioGeneralDetalleService", e);
            return null;
        }
    }

    public List<PartePresupuestariaModel> getContabilidad(List<Long> idModulos) {
        String idList = idModulos.toString().replace("[", "").replace("]", "");
        return (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT * FROM activos.fs_bienes_ingreso(?1)", "BienesIngresoMapping")
                .setParameter(1, idList)
                .getResultList();
    }

    public List<PartePresupuestariaModel> getemisionFondoTerceros(String fechaEmision) {
        try {
            List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery("select * from contabilidad.fs_recaudacion_fondo_terceros(?1) order by rubro,saldodisponible desc", "emisionFondoTerceroMapping")
                    .setParameter(1, fechaEmision).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error en getParteEmisionDiaria - ContDiarioGeneralDetalleService", e);
            return null;
        }
    }

    public List<PartePresupuestariaModel> getContabilidadInvIngreso(List<Long> idModulos) {
        String idList = idModulos.toString().replace("[", "").replace("]", "");
        return (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT * FROM activos.fs_inventario_ingreso(?1)", "BienesIngresoMapping")
                .setParameter(1, idList)
                .getResultList();
    }

    public List<PartePresupuestariaModel> getContabilidadInvSalidas(List<Long> idModulos) {
        String idList = idModulos.toString().replace("[", "").replace("]", "");
        return (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT * FROM activos.fs_inventario_salida(?1)", "BienesIngresoMapping")
                .setParameter(1, idList)
                .getResultList();
    }

    public Boolean determinarRetencion(List<ContCuentas> idCuentasContables, Short periodo) {
        List<CuentaContableRetencion> result = (List<CuentaContableRetencion>) em.createQuery("SELECT ccr FROM CuentaContableRetencion ccr WHERE ccr.contContable in (?1) AND ccr.estado = true")
                .setParameter(1, idCuentasContables)
                .getResultList();
        return !result.isEmpty();
    }

    public List<ContDiarioGeneralDetalle> getDetalleFactura(List<Factura> facturas) {
        return (List<ContDiarioGeneralDetalle>) em.createQuery("SELECT d FROM ContDiarioGeneralDetalle d INNER JOIN d.idDetalleFactura f WHERE f.idFactura in (?1)")
                .setParameter(1, facturas)
                .getResultList();
    }

    public ContDiarioGeneralDetalle findByPadreDiarioGeneral(Long idDiario, Long idCuenta) {
        Query query = em.createNativeQuery("SELECT SUM(d.debe) AS sumaDebe, SUM(d.haber) AS sumaHaber FROM contabilidad.cont_diario_general_detalle d "
                + "JOIN contabilidad.cont_cuentas c ON c.id = d.id_cont_cuentas WHERE d.id_cont_diario_general = ?1 AND c.id = ?2")
                .setParameter(1, idDiario)
                .setParameter(2, idCuenta);
        List<Object[]> result = query.getResultList();

        if (result != null) {
            List<ContDiarioGeneralDetalle> list = new ArrayList<>();
            for (Object[] data : result) {
                ContDiarioGeneralDetalle d = new ContDiarioGeneralDetalle();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }

    public ContDiarioGeneralDetalle findDetalleTransaccionById(Long id) {
        try {
            return (ContDiarioGeneralDetalle) em.createQuery("SELECT d FROM ContDiarioGeneralDetalle d WHERE d.id = ?1")
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ContDiarioGeneralDetalle> findAllDetalleByCuentaContablePadreAndFecha(String fecha, Long idCuenta, Long idTransaccionCierre) {
        try {
            List<Object[]> result = new ArrayList<>();
            if (idTransaccionCierre != null) {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.id_cont_diario_general,  detalle.id_cont_cuentas, detalle.debe, detalle.haber \n"
                        + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                        + "JOIN contabilidad.cont_diario_general d ON detalle.id_cont_diario_general = d.id  \n"
                        + "JOIN contabilidad.cont_cuentas c ON detalle.id_cont_cuentas = c.id  \n"
                        + "WHERE to_char(d.fecha_registro,'YYYY-MM') = ?1 AND c.id = ?2 AND d.tipo <> ?3 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuenta)
                        .setParameter(3, idTransaccionCierre);
                result = query.getResultList();
            } else {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.id_cont_diario_general, detalle.id_cont_cuentas, detalle.debe, detalle.haber \n"
                        + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                        + "JOIN contabilidad.cont_diario_general d ON detalle.id_cont_diario_general = d.id \n"
                        + "JOIN contabilidad.cont_cuentas c ON detalle.id_cont_cuentas = c.id  \n"
                        + "WHERE to_char(d.fecha_registro,'YYYY-MM') = ?1 AND c.id = ?2 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuenta);
                result = query.getResultList();
            }
            if (result != null) {
                List<ContDiarioGeneralDetalle> list = new ArrayList<>();
                for (Object[] data : result) {
                    ContDiarioGeneralDetalle d = new ContDiarioGeneralDetalle(((BigInteger) data[0]).longValue());
                    d.setIdContDiarioGeneral((new ContDiarioGeneral((Long) data[1])));
                    d.setIdContCuentas(new ContCuentas((Long) data[2]));
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

    public List<ContDiarioGeneralDetalle> findAllDetalleByCuentaContableHijasAndFecha(String fecha, Long idCuentaPadre, Long idTransaccionCierre) {
        try {
            List<Object[]> result = new ArrayList<>();
            if (idTransaccionCierre != null) {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.id_cont_diario_general, detalle.id_cont_cuentas, detalle.debe, detalle.haber \n"
                        + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                        + "JOIN contabilidad.cont_diario_general d ON detalle.id_cont_diario_general = d.id \n"
                        + "JOIN contabilidad.cont_cuentas c ON detalle.id_cont_cuentas = c.id \n"
                        + "WHERE to_char(d.fecha_registro,'YYYY-MM') = ?1 AND c.padre = ?2 AND d.tipo <> ?3 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuentaPadre)
                        .setParameter(3, idTransaccionCierre);
                result = query.getResultList();
            } else {
                Query query = em.createNativeQuery("SELECT detalle.id, detalle.id_cont_diario_general, detalle.id_cont_cuentas, detalle.debe, detalle.haber \n"
                        + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                        + "JOIN contabilidad.cont_diario_general d ON detalle.id_cont_diario_general = d.id \n"
                        + "JOIN contabilidad.cont_cuentas c ON detalle.id_cont_cuentas = c.id \n"
                        + "WHERE to_char(d.fecha_registro,'YYYY-MM') = ?1 AND c.padre = ?2 AND d.estado = true")
                        .setParameter(1, fecha)
                        .setParameter(2, idCuentaPadre);
                result = query.getResultList();
            }
            if (result != null) {
                List<ContDiarioGeneralDetalle> list = new ArrayList<>();
                for (Object[] data : result) {
                    ContDiarioGeneralDetalle d = new ContDiarioGeneralDetalle(((BigInteger) data[0]).longValue());
                    d.setIdContDiarioGeneral(new ContDiarioGeneral(((BigInteger) data[1]).longValue()));
                    d.setIdContCuentas(new ContCuentas(((BigInteger) data[2]).longValue()));
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

    public ContDiarioGeneralDetalle findByPadreDetalleTransaccionExceptoDiarioApertura(String fechaDesde, String fechaHasta, Long idDiario, Long idCuenta,
            Long idTransaccionCierre) {

        List<Object[]> result = new ArrayList<>();
        if (idTransaccionCierre != null) {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber \n"
                    + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                    + "JOIN contabilidad.cont_diario_general d ON d.id = detalle.id_cont_diario_general \n"
                    + "JOIN contabilidad.cont_cuentas c ON c.id = detalle.id_cont_cuentas \n"
                    + "WHERE to_char(d.fecha_registro,'YYYY-MM') <= ?1 AND to_char(d.fecha_registro,'YYYY-MM') >= ?2 AND detalle.id_cont_diario_general <> ?3  \n"
                    + "AND c.id = ?4 AND d.tipo <> ?5 AND d.estado = true";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idDiario)
                    .setParameter(4, idCuenta)
                    .setParameter(5, idTransaccionCierre);
            result = query.getResultList();
        } else {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber \n"
                    + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                    + "JOIN contabilidad.cont_diario_general d ON d.id = detalle.id_cont_diario_general \n"
                    + "JOIN contabilidad.cont_cuentas c ON c.id = detalle.id_cont_cuentas \n"
                    + "WHERE to_char(d.fecha_registro,'YYYY-MM') <= ?1 AND to_char(d.fecha_registro,'YYYY-MM') >= ?2 AND detalle.id_cont_diario_general <> ?3  \n"
                    + "AND c.id = ?4 AND d.estado = true";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idDiario)
                    .setParameter(4, idCuenta);
            result = query.getResultList();
        }

        if (result != null) {
            List<ContDiarioGeneralDetalle> list = new ArrayList<>();
            for (Object[] data : result) {
                ContDiarioGeneralDetalle d = new ContDiarioGeneralDetalle();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }

    public ContDiarioGeneralDetalle findByPadreDetalleTransaccionNoExisteDiarioApertura(String fechaDesde, String fechaHasta, Long idCuenta,
            Long idTransaccionCierre) {

        List<Object[]> result = new ArrayList<>();
        if (idTransaccionCierre != null) {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber \n"
                    + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                    + "JOIN contabilidad.cont_diario_general d ON d.id = detalle.id_cont_diario_general \n"
                    + "JOIN contabilidad.cont_cuentas c ON c.id = detalle.id_cont_cuentas \n"
                    + "WHERE to_char(d.fecha_registro,'YYYY-MM') <= ?1 AND to_char(d.fecha_registro,'YYYY-MM') >= ?2 AND c.id = ?3 AND d.tipo <> ?4 \n"
                    + "AND d.estado = true";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idCuenta)
                    .setParameter(4, idTransaccionCierre);
            result = query.getResultList();
        } else {
            String sql = "SELECT SUM(detalle.debe) as sumaDebe, SUM(detalle.haber) as sumHaber \n"
                    + "FROM contabilidad.cont_diario_general_detalle detalle \n"
                    + "JOIN contabilidad.cont_diario_general d ON d.id = detalle.id_cont_diario_general \n"
                    + "JOIN contabilidad.cont_cuentas c ON c.id = detalle.id_cont_cuentas \n"
                    + "WHERE to_char(d.fecha_registro,'YYYY-MM') <= ?1 AND to_char(d.fecha_registro,'YYYY-MM') >= ?2 AND c.id = ?3 \n"
                    + "AND d.estado = true";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta)
                    .setParameter(3, idCuenta);
            result = query.getResultList();
        }

        if (result != null) {
            List<ContDiarioGeneralDetalle> list = new ArrayList<>();
            for (Object[] data : result) {
                ContDiarioGeneralDetalle d = new ContDiarioGeneralDetalle();
                d.setDebe((BigDecimal) data[0]);
                d.setHaber((BigDecimal) data[1]);
                list.add(d);
            }
            return list.get(0);
        }
        return null;
    }
}
