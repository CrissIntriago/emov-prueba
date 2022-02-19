/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.models.DetalleContableEmisionesModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContDiarioGeneralService extends AbstractService<ContDiarioGeneral> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContDiarioGeneralService() {
        super(ContDiarioGeneral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer nextRegistro(Short periodo, Boolean revisado) {
        Integer result = (Integer) em.createQuery(QUERY.NEXT_NUM_DIARIO)
                .setParameter(1, periodo)
                .setParameter(2, revisado)
                .getSingleResult();
        return result;
    }

    public ContDiarioGeneral getDiarioGeneralRetencionByNumTransaccion(BigInteger numeroTransaccion, short periodo) {
        try {
            ContDiarioGeneral diario = (ContDiarioGeneral) em.createQuery("SELECT d FROM ContDiarioGeneral d WHERE d.numRegistro = ?1 AND d.retencion = TRUE AND d.retenido = FALSE AND d.periodo = ?2")
                    .setParameter(1, numeroTransaccion)
                    .setParameter(2, periodo)
                    .getSingleResult();
            return diario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContDiarioGeneral getDiarioGeneralRetencionByNumTransaccion(BigInteger numeroTransaccion, Boolean retencion, Boolean retenido, short periodo) {
        try {
            ContDiarioGeneral diario = (ContDiarioGeneral) em.createQuery("SELECT d FROM ContDiarioGeneral d WHERE d.numRegistro = ?1 AND d.retencion = ?2 AND d.retenido = ?3 AND d.periodo = ?4")
                    .setParameter(1, numeroTransaccion)
                    .setParameter(2, retencion)
                    .setParameter(3, retenido)
                    .setParameter(4, periodo)
                    .getSingleResult();
            return diario;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ContDiarioGeneralDetalle> findAllRetencionDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        return em.createQuery("SELECT detalle FROM ContDiarioGeneralDetalle detalle JOIN detalle.idContDiarioGeneral diario WHERE EXISTS(SELECT cuentaRet FROM CuentaContableRetencion cuentaRet WHERE cuentaRet.contContable = detalle.idContCuentas AND detalle.idContDiarioGeneral = ?1) ")
                .setParameter(1, diarioGeneral)
                .getResultList();
    }

    public Boolean getDiarioRetencion(ContDiarioGeneral diario) {
        return (Boolean) em.createQuery("SELECT d.retenido FROM ContDiarioGeneral d where d.id =:id AND d.retencion = TRUE")
                .setParameter("id", diario.getId()).getSingleResult();
    }

    public List<Factura> findAllFacturasByDiarioGeneralBySolicitud(SolicitudReservaCompromiso solicitud) {
        try {
            return em.createQuery("SELECT factura FROM Factura factura JOIN factura.inventarioRegistro invregis JOIN invregis.adquisiciones adq , ContratosReservaEjecuion contrato "
                    + "WHERE contrato.contrato = adq AND contrato.reserva = ?1 "
                    + "AND adq.estado = TRUE AND invregis.estado = TRUE AND factura.estado = TRUE AND factura.retenida = FALSE")
                    .setParameter(1, solicitud)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Factura> findAllFacturaByDiarioGeneralForBienes(SolicitudReservaCompromiso solicitud) {
        return em.createQuery("SELECT f FROM BienesMovimiento b JOIN b.factura f, "
                + "ContratosReservaEjecuion c WHERE c.contrato = b.adquisiciones AND c.reserva = ?1 AND f.retenida = FALSE")
                .setParameter(1, solicitud)
                .getResultList();
    }

    public ContDiarioGeneral findByDiarioPeriodo(Integer idDiario, Short anio) {
        try {
            ContDiarioGeneral result = (ContDiarioGeneral) em.createQuery(QUERY.REGISTRO_CONTABLE_COMPROBANTE)
                    .setParameter(1, idDiario)
                    .setParameter(2, anio)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public double getSaldo(String partida, Short periodo) {
        BigDecimal valor = (BigDecimal) em.createNativeQuery("SELECT * FROM contabilidad.fs_saldo_comprometido(?1,?2,?3)")
                .setParameter(1, partida)
                .setParameter(2, periodo)
                .setParameter(3, MOD_CONTABILIDAD.MOD_PRESUPUESTO)
                .getSingleResult();
        return valor.doubleValue();
    }

    public void getUpdateDiario(Long id, int size) {
        Integer result = (Integer) em.createNativeQuery("SELECT * FROM contabilidad.fs_diario_retencion(?1,?2)")
                .setParameter(1, id)
                .setParameter(2, size)
                .getSingleResult();
    }

    public boolean updateDetalleFactura(Factura fac, ContDiarioGeneral diario) {
        List<ContDiarioGeneralDetalle> result = (List<ContDiarioGeneralDetalle>) em.createQuery("SELECT dt FROM ContDiarioGeneralDetalle dt "
                + "INNER JOIN dt.idDetalleFactura df WHERE dt.idContDiarioGeneral = ?2 "
                + "AND df.idFactura = ?1 AND dt.saldoRetencion > ?3")
                .setParameter(1, fac)
                .setParameter(2, diario)
                .setParameter(3, BigDecimal.ZERO)
                .getResultList();
        return result.isEmpty();
    }

    public List<ContDiarioGeneralDetalle> getTesoreriaDetalle(List<Long> idDiarioList, Short periodo) {
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        String idList = idDiarioList.toString().replace("[", "").replace("]", "");
        Integer update = (Integer) em.createNativeQuery("SELECT * FROM contabilidad.fs_update_rubros (?1,?2)")
                .setParameter(1, idList)
                .setParameter(2, periodo)
                .getSingleResult();
        List<DetalleContableEmisionesModel> temp = (List<DetalleContableEmisionesModel>) em.createNativeQuery(QUERY.DETALLE_TESORERIA, "tesoreriaEmisionesMapping")
                .setParameter(1, idDiarioList)
                .getResultList();
        if (!temp.isEmpty()) {
            result = loadData(temp);
        }
        return result;
    }

    private List<ContDiarioGeneralDetalle> loadData(List<DetalleContableEmisionesModel> list) {
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        for (DetalleContableEmisionesModel item : list) {
            ContDiarioGeneralDetalle aux = new ContDiarioGeneralDetalle();
            if (item.getId_cont_cuentas() != null) {
                aux.setIdContCuentas(new ContCuentas(item.getId_cont_cuentas(), item.getCod_cuenta(), item.getDescripcion()));
            }
            aux.setDebe(item.getDebe());
            aux.setHaber(item.getHaber());
            aux.setRubroLiquidacion(aux.getRubroLiquidacion());
            if (item.getTipo_registro() != null) {
                aux.setTipoRegistro(new CatalogoItem(item.getTipo_registro(), item.getCod_tipo()));
            }
            if (item.getId_pres_catalogo_presupuestario() != null) {
                aux.setIdPresCatalogoPresupuestario(new PresCatalogoPresupuestario(item.getId_pres_catalogo_presupuestario()));
            }
            if (item.getId_pres_plan_programatico() != null) {
                aux.setIdPresPlanProgramatico(new PresPlanProgramatico(item.getId_pres_plan_programatico()));
            }
            if (item.getId_pres_fuente_financiamiento() != null) {
                aux.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(item.getId_pres_fuente_financiamiento()));
            }
            if (item.getPartida_presupuestaria() != null) {
                aux.setPartidaPresupuestaria(item.getPartida_presupuestaria());
            }
            aux.setDevengado(item.getDevengado());
            aux.setEjecutado(item.getEjecutado());
            aux.setNumeracion(item.getNumeracion().intValue());
            result.add(aux);
        }
        return result;
    }

    public ContDiarioGeneral findAllDiarioGeneralClaseAndTipoAndPeriodo(Long idClase, Long idTipo, Short periodo) {
        try {
            List<ContDiarioGeneral> list = new ArrayList<>();
            list = em.createQuery("SELECT d FROM ContDiarioGeneral d WHERE d.clase.id = ?1 AND d.tipo.id = ?2 AND  d.periodo = ?3")
                    .setParameter(1, idClase)
                    .setParameter(2, idTipo)
                    .setParameter(3, periodo)
                    .getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionList(Short periodo) {
        return (List<FinaRenTipoLiquidacion>) em.createNativeQuery(QUERY.TIPO_LIQUIDACION, FinaRenTipoLiquidacion.class)
                .setParameter(1, periodo)
                .getResultList();
    }

    public int registro_contable_emisiones(FinaRenLiquidacion liqui, Integer codigoModulo, Date fecha, Short periodo, String cod_registro) {
        return (Integer) em.createNativeQuery(QUERY.GENERAR_RC_EMISIONES)
                .setParameter(1, liqui.getId())
                .setParameter(2, codigoModulo)
                .setParameter(3, fecha)
                .setParameter(4, periodo)
                .setParameter(5, cod_registro)
                .getSingleResult();
    }

    public List<ContDiarioGeneralDetalle> getTesoreriaPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado, Short periodo) {
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        Integer update = (Integer) em.createNativeQuery("SELECT * FROM contabilidad.fs_update_rubros_predios(?1,?2,?3,?4,?5)")
                .setParameter(1, codModulo)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .setParameter(4, tipoSeleccionado.getId())
                .setParameter(5, periodo)
                .getSingleResult();
        List<DetalleContableEmisionesModel> temp = (List<DetalleContableEmisionesModel>) em.createNativeQuery(QUERY.DETALLE_TESORERIA_PREDIOS, "tesoreriaEmisionesMapping")
                .setParameter(1, codModulo)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .setParameter(4, tipoSeleccionado.getId())
                .getResultList();
        if (!temp.isEmpty()) {
            result = loadData(temp);
        }
        return result;
    }

    public void updateRegistroContablePredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado) {
        Integer update = (Integer) em.createNativeQuery("UPDATE contabilidad.cont_diario_general\n"
                + "SET estado = false\n"
                + "WHERE tipo_liquidacion = ?4 AND cod_modulo = ?1\n"
                + "AND estado = true AND revisado = false\n"
                + "AND fecha_registro BETWEEN ?2 AND ?3")
                .setParameter(1, codModulo)
                .setParameter(2, fechaDesde)
                .setParameter(3, fechaHasta)
                .setParameter(4, tipoSeleccionado.getId())
                .executeUpdate();
    }

    public void anularPorPrescripcion(FinaRenLiquidacion liquidacion, Integer codModulo, String codDevengado) {
        Integer update = (Integer) em.createNativeQuery("SELECT * FROM contabilidad.fs_anulacion_prescripcion(?1,?2,?3,?4,?5)")
                .setParameter(1, liquidacion.getId())
                .setParameter(2, codModulo)
                .setParameter(3, Utils.getAnio(new Date()).shortValue())
                .setParameter(4, Utils.getAnio(new Date()))
                .setParameter(5, codDevengado)
                .getSingleResult();
    }

}
