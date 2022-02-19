/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.entities.RenFacturaPago;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.model.TalonResumenModel;
import com.origami.sigef.contabilidad.model.VentaEst;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
@Stateless
@javax.enterprise.context.Dependent
public class RenFacturaService extends AbstractService<RenFactura> {

    private static final Logger LOG = Logger.getLogger(RenFacturaService.class.getName());

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RenFacturaService() {
        super(RenFactura.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public RenFactura findById(Long id) {
        return (RenFactura) em.createQuery("SELECT l FROM RenFactura l WHERE l.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
    }

    public RenFactura findLiquidacionByDiarioGeneral(ContDiarioGeneral d) {
        try {
            Query query = em.createQuery("SELECT l FROM RenFactura l WHERE l.diario = ?1 AND l.estado = true")
                    .setParameter(1, d);
            if (!query.getResultList().isEmpty()) {
                return (RenFactura) query.getResultList().get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ContDiarioGeneralDetalle> findAllRetencionDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        return em.createQuery("SELECT detalle FROM ContDiarioGeneralDetalle detalle JOIN detalle.idContDiarioGeneral diario WHERE EXISTS(SELECT cuentaRet FROM CuentaContableRetencion cuentaRet WHERE cuentaRet.cuentaContable = detalle.id AND detalle.diarioGeneral = ?1) ")
                .setParameter(1, diarioGeneral)
                .getResultList();
    }

    public List<RenFactura> getRenFacturaTipoByComprobante(Long tipo, String estado) {
        try {
            List<RenFactura> result = (List<RenFactura>) em.createQuery("SELECT r FROM RenFactura r where r.comprobante.id = ?1 AND r.estadoWs = ?2")
                    .setParameter(1, tipo).setParameter(2, estado).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<RenFactura> findCompras(OpcionBusqueda opcionBusqueda, Integer mesSeleccionado) {
        return (List<RenFactura>) em.createQuery("SELECT rf FROM RenFactura rf INNER JOIN rf.estadoLiquidacion el "
                + "WHERE rf.estado = true AND rf.detalleCompras is not null AND rf.estadoWs = 'RECIBIDA;AUTORIZADO' "
                + "AND el.codigo = 'aceptada' AND rf.anio =?1 AND rf.mes = ?2")
                .setParameter(1, opcionBusqueda.getAnio().intValue())
                .setParameter(2, mesSeleccionado)
                .getResultList();
    }

    public List<DetalleVentas> findVentas(OpcionBusqueda opcionBusqueda, Integer mesSeleccionado) {
        return em.createNativeQuery("SELECT (case\n"
                + "when item.texto = 'RUC' then '04'\n"
                + "when item.texto = 'CEDULA' then '05'\n"
                + "when item.texto = 'PASAPORTE' then '06'\n"
                + "when item.texto = 'OTRO' then '07'\n"
                + "END ) as tpIdCliente,\n"
                + "c.identificacion as idCliente,\n"
                + "comp.codigo as tipoComprobante,\n"
                + "upper(substring(li.tipo_emision from 1 for 1)) as tipoEmision,\n"
                + "count(*) as numeroComprobantes,\n"
                + "(sum(li.total_pagar) - sum(li.sub_total) - sum(li.monto_iva)) as baseImponible,\n"
                + "sum(li.sub_total) as baseImpGrav,\n"
                + "sum(li.monto_iva) as montoIva\n"
                + "FROM tesoreria.ren_factura li\n"
                + "LEFT JOIN comprobantes_electronicos.comprobante comp ON li.comprobante = comp.id\n"
                + "LEFT JOIN public.cliente c ON c.id = li.solicitante\n"
                + "LEFT JOIN public.catalogo_item item ON c.tipo_identificacion = item.id\n"
                + "INNER JOIN public.catalogo_item c1 ON li.estado_liquidacion = c1.id\n"
                + "WHERE li.estado = true AND li.id_facrura_refenrencia is not null AND\n"
                + "li.anio = ?1 AND li.mes = ?2\n"
                + "AND c1.codigo = 'aceptada'\n"
                + "AND li.estado_ws = 'RECIBIDA;AUTORIZADO'\n"
                + "GROUP BY li.comprobante, c.identificacion,\n"
                + "item.texto, c.nombre,li.tipo_emision, comp.codigo", "DetalleVentasValueMapping")
                .setParameter(1, opcionBusqueda.getAnio().intValue())
                .setParameter(2, mesSeleccionado)
                .getResultList();
    }

    public List<RenFacturaPago> findLiquidacionCliente(String identificacion, Integer mesSeleccionado, OpcionBusqueda opcionBusqueda) {
        return em.createQuery("SELECT rfp FROM RenFacturaPago rfp INNER JOIN rfp.factura rf INNER JOIN rf.solicitante cl WHERE cl.identificacion = ?1 AND rf.mes = ?3 AND rf.anio=?2 AND rf.estado = true")
                .setParameter(1, identificacion)
                .setParameter(2, opcionBusqueda.getAnio().intValue())
                .setParameter(3, mesSeleccionado)
                .getResultList();
    }

    public List<VentaEst> findVentasEstablecimiento(Integer anio, Integer mes) {
        List<VentaEst> resultado = em.createNativeQuery("SELECT \n"
                + "substring(rf.codigo_comprobante from 1 for 3) as codEstab,\n"
                + "(sum(rf.sub_total) + (sum(rf.total_pagar) - sum(rf.sub_total) - sum(rf.monto_iva) )) as ventasEstab,\n"
                + "0.00 as ivaComp\n"
                + "FROM tesoreria.ren_factura rf\n"
                + "INNER JOIN catalogo_item ci On rf.estado_liquidacion = ci.id\n"
                + "WHERE rf.detalle_compras is null \n"
                + "AND rf.estado_ws = 'RECIBIDA;AUTORIZADO'\n"
                + "AND rf.id_facrura_refenrencia is not null\n"
                + "AND rf.estado = true AND rf.anio = ?1 AND rf.mes= ?2\n"
                + "AND ci.codigo = 'aceptada'\n"
                + "GROUP BY 1;", "EstablecimientoValueMapping")
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

    public List<TalonResumenModel> findAllComprasByLiquidacion(int anio, Integer mesSeleccionado) {
        return em.createNativeQuery("SELECT DISTINCT l.id as liquidacionId, f.id as facturaId, c.codigo as codCompra,\n"
                + "c.descripcion as transaccion,\n"
                + "f.base_imponible_iva as bi_tarifa0,\n"
                + "f.base_imponible_diferente as bi_tarifa_diferente0,\n"
                + "0.00 as bi_no_objetivoIva, f.monto_iva as valor_iva \n"
                + "FROM tesoreria.ren_factura_detalle d\n"
                + "JOIN tesoreria.ren_factura l ON d.ren_factura = l.id\n"
                + "JOIN comprobantes_electronicos.comprobante c ON l.comprobante_modifica = c.id\n"
                + "JOIN public.factura f ON d.factura = f.id\n"
                + "WHERE l.id_facrura_refenrencia is null AND l.estado = true\n"
                + "AND l.anio = ?1 and l.mes = ?2 AND l.estado_liquidacion <> 110", "TalonResumenMapping")
                .setParameter(1, anio)
                .setParameter(2, mesSeleccionado)
                .getResultList();
    }
}
