/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.contabilidad.model.VentaEst;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.model.TalonResumenModel;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class LiquidacionService extends AbstractService<Liquidacion> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LiquidacionService() {
        super(Liquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Liquidacion findByIdOrden(Long orden) {
        try {
            return (Liquidacion) getEntityManager().createQuery("SELECT d FROM Liquidacion d WHERE d.idOrden=:orden")
                    .setParameter("orden", orden).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Liquidacion findById(Long id) {
        return (Liquidacion) em.createQuery("SELECT l FROM Liquidacion l WHERE l.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
    }

    public Liquidacion findByNumeroComprobante(BigInteger numeroComprobante) {
        try {
            return (Liquidacion) getEntityManager().createQuery("SELECT d FROM Liquidacion d WHERE d.numeroComprobante=:numComprobante  ")
                    .setParameter("numComprobante", numeroComprobante).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public RenFactura findByNumeroComprobanteRenFactura(BigInteger numeroComprobante) {
        try {
            return (RenFactura) getEntityManager().createQuery("SELECT d FROM RenFactura d WHERE d.numeroComprobante=:numComprobante  ")
                    .setParameter("numComprobante", numeroComprobante).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Factura> findAllFacturasByDiarioGeneralBySolicitud(DiarioGeneral diario) {
        try {
            return em.createQuery("SELECT factura FROM Factura factura "
                    + "JOIN factura.inventarioRegistro invregis "
                    + "JOIN invregis.adquisiciones adq , ContratosReservaEjecuion contrato "
                    + "WHERE contrato.contrato = adq AND contrato.reserva = ?1 "
                    + "AND adq.estado = TRUE AND invregis.estado = TRUE AND factura.estado = TRUE AND factura.retenida = FALSE")
                    .setParameter(1, diario.getCertificacionesPresupuestario())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetalleTransaccion> findAllRetencionDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        return em.createQuery("SELECT detalle FROM DetalleTransaccion detalle "
                + "JOIN detalle.diarioGeneral diario WHERE "
                + "EXISTS(SELECT cuentaRet FROM CuentaContableRetencion cuentaRet "
                + "WHERE cuentaRet.cuentaContable = detalle.cuentaContable AND detalle.diarioGeneral = ?1) ")
                .setParameter(1, diarioGeneral)
                .getResultList();
    }

    public List<DetalleTransaccion> findAllDetalleTransaccionByCuentaAndDiario(Long idDiarioGeneral, Long idCuentaContable) {
        try {
            return em.createQuery("SELECT detalle FROM DetalleTransaccion detalle WHERE detalle.diarioGeneral.id = ?1 AND detalle.cuentaContable.id = ?2 ")
                    .setParameter(1, idDiarioGeneral)
                    .setParameter(2, idCuentaContable)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Factura> findAllFacturaByDiarioGeneralForBienes(DiarioGeneral diario) {
        return em.createQuery("SELECT f FROM BienesMovimiento b JOIN b.factura f, "
                + "ContratosReservaEjecuion c WHERE c.contrato = b.adquisiciones AND c.reserva = ?1 AND f.retenida = FALSE")
                .setParameter(1, diario.getCertificacionesPresupuestario())
                .getResultList();
    }

    public Liquidacion findLiquidacionByDiarioGeneral(DiarioGeneral d) {
        try {
            Query query = em.createQuery("SELECT l FROM Liquidacion l WHERE l.diarioGeneral = ?1 AND l.estado = true")
                    .setParameter(1, d);
            if (!query.getResultList().isEmpty()) {
                return (Liquidacion) query.getResultList().get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Liquidacion> findAtsByLiquidacion(Integer anio, Integer mes) {
        return em.createQuery("SELECT l FROM Liquidacion l WHERE l.anio = ?1 AND l.mes = ?2 AND l.estado = TRUE AND l.idLiquidacionRefenrencia is null")
                .setParameter(1, anio)
                .setParameter(2, mes)
                .getResultList();
    }

    public List<DetalleVentas> findAllVentasByLiquidacion(Integer anio, Integer mes) {
        return em.createNativeQuery("SELECT (case "
                + "when item.texto = 'RUC' then '04' "
                + "when item.texto = 'CEDULA' then '05' "
                + "when item.texto = 'PASAPORTE' then '06' "
                + "when item.texto = 'OTRO' then '07' "
                + "END ) as tpIdCliente, "
                + "c.identificacion as idCliente, "
                + "comp.codigo as tipoComprobante, "
                + "upper(substring(li.tipo_emision from 1 for 1)) as tipoEmision, "
                + "count(*) as numeroComprobantes, "
                + "(sum(li.total_pagar) - sum(li.sub_total) - sum(li.monto_iva)) as baseImponible, "
                + "sum(li.sub_total) as baseImpGrav, "
                + "sum(li.monto_iva) as montoIva "
                + "FROM tesoreria.liquidacion li "
                + "LEFT JOIN comprobantes_electronicos.comprobante comp ON li.comprobante = comp.id "
                + "LEFT JOIN public.cliente c ON c.id = li.cliente "
                + "LEFT JOIN public.catalogo_item item ON c.tipo_identificacion = item.id "
                + "WHERE li.estado = true AND li.id_liquidacion_referencia is not null AND "
                + "li.anio = ?1 AND li.mes = ?2 "
                + "GROUP BY li.comprobante, c.identificacion, "
                + "item.texto, c.nombre,li.tipo_emision, comp.codigo", "DetalleVentasValueMapping")
                .setParameter(1, anio)
                .setParameter(2, mes)
                .getResultList();
    }

    public List<TalonResumenModel> findAllComprasByLiquidacion(Integer anio, Integer mes) {
        // EL 110 ID .. ES DE CATALOGO ITEM .. DIFERENTE DE ANULADOS..... 
        return em.createNativeQuery("SELECT DISTINCT l.id as liquidacionId, f.id as facturaId, c.codigo as codCompra, c.descripcion as transaccion, "
                + "f.base_imponible_iva as bi_tarifa0, "
                + "f.base_imponible_diferente as bi_tarifa_diferente0, "
                + "0.00 as bi_no_objetivoIva, f.monto_iva as valor_iva FROM tesoreria.liquidacion_detalle d "
                + "JOIN tesoreria.liquidacion l ON d.liquidacion = l.id "
                + "JOIN comprobantes_electronicos.comprobante c ON l.comprobante_modifica = c.id "
                + "JOIN public.factura f ON d.factura = f.id "
                + "WHERE l.id_liquidacion_referencia is null AND l.estado = true "
                + "AND l.anio = ?1 and l.mes = ?2 AND l.estado_liquidacion <> 110 ", "TalonResumenMapping")
                .setParameter(1, anio)
                .setParameter(2, mes)
                .getResultList();
    }

    public List<String> getEstadosLiquidacion() {
        List<String> resultado = (List<String>) em.createQuery("SELECT DISTINCT(ci.texto) FROM Liquidacion lq INNER JOIN lq.estadoLiquidacion ci WHERE lq.estado=TRUE")
                .getResultList();
        return resultado;
    }

    public List<Liquidacion> getLiquidacionPendientes(Comprobantes comprobante) {
        Query query = em.createQuery("SELECT l FROM Liquidacion l WHERE l.estadoWs <> 'RECIBIDA;AUTORIZADO' AND l.comprobante = ?1")
                .setParameter(1, comprobante);
        List<Liquidacion> result = (List<Liquidacion>) query.getResultList();
        return result;
    }
    
    public List<Liquidacion> _getLiquidacionPendientes(Comprobantes comprobante, Comprobantes comprobante_1) {
        Query query = em.createQuery("SELECT l FROM Liquidacion l WHERE l.estadoWs <> 'RECIBIDA;AUTORIZADO' AND (l.comprobante = ?1 OR l.comprobante = ?2)")
                .setParameter(1, comprobante)
                .setParameter(2, comprobante_1);
        List<Liquidacion> result = (List<Liquidacion>) query.getResultList();
        return result;
    }

    public Comprobantes getTipoComprobante(String codigo) {
        try {
            Query query = em.createQuery("SELECT c FROM Comprobantes c WHERE c.codigo = ?1")
                    .setParameter(1, codigo);
            Comprobantes result = (Comprobantes) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<VentaEst> findAllVentasByEstablecimiento(Integer anio, Integer mes) {
        List<VentaEst> resultado = em.createNativeQuery("SELECT substring(l.codigo_comprobante from 1 for 3) as codEstab,\n"
                + "(sum(l.sub_total) + (sum(l.total_pagar) - sum(l.sub_total) - sum(l.monto_iva) )) as ventasEstab,\n"
                + "0.00 as ivaComp FROM tesoreria.liquidacion l\n"
                + "WHERE l.estado = true AND l.anio = ?1 AND l.mes= ?2 AND l.id_liquidacion_referencia is not null\n"
                + "AND l.tipo_emision <> 'ELECTRÓNICA' GROUP BY 1", "EstablecimientoValueMapping")
                .setParameter(1, anio)
                .setParameter(2, mes)
                .getResultList();
        if (resultado.isEmpty()) {
            VentaEst VentaEst = new VentaEst("001", BigDecimal.ZERO, BigDecimal.ZERO);
            List<VentaEst> result = new ArrayList<>();
            result.add(VentaEst);
            return result;
        } else {
            return resultado;
        }
    }
}
