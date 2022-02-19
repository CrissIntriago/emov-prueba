/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContFacturaDetalle;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContFacturaDetalleService extends AbstractService<ContFacturaDetalle> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContFacturaDetalleService() {
        super(ContFacturaDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PartePresupuestariaModel> facturaDetalleContable(List<Long> idList) {
        return (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT \n"
                + "cc.id AS idtemp, SUM(cfd.valor_detalle) AS saldodisponible, SUM(cfd.valor_detalle) AS montodisponible\n"
                + "FROM contabilidad.cont_factura_detalle cfd\n"
                + "INNER JOIN tesoreria.cuenta_contable_retencion ccr ON cfd.id_conf_retencion = ccr.id\n"
                + "INNER JOIN contabilidad.cont_cuentas cc ON ccr.cont_contable = cc.id\n"
                + "WHERE cfd.id_factura IN (?1)\n"
                + "GROUP BY 1", "BienesIngresoMapping")
                .setParameter(1, idList)
                .getResultList();
    }

    public List<ContFacturaDetalle> getDetalle(List<Factura> facturasSeleccionadas) {
        return (List<ContFacturaDetalle>) em.createQuery("SELECT dt FROM ContFacturaDetalle dt INNER JOIN dt.idFactura f WHERE dt.estado = true AND dt.idFactura in (?1) ORDER BY f.numFactura ASC")
                .setParameter(1, facturasSeleccionadas)
                .getResultList();
    }
}
