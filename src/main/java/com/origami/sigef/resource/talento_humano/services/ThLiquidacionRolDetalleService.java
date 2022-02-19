/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.models.RolDetalleModel;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRolDetalle;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThLiquidacionRolDetalleService extends AbstractService<ThLiquidacionRolDetalle> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThLiquidacionRolDetalleService() {
        super(ThLiquidacionRolDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<RolDetalleModel> getDetalleRol(ThTipoRol thTipoRolSeleccionado) {
        List<RolDetalleModel> result = (List<RolDetalleModel>) em.createNativeQuery("SELECT lrd.id_cuenta as idcuenta, lrd.id_pres_estructura_programatica as idestructura,\n"
                + "lrd.id_pres_catalogo_presupuesto as idpresupuesto, lrd.id_pres_fuente_financiamiento as idfuente,\n"
                + "lrd.partida_presupuestaria as partida, COALESCE(SUM(lrd.valor_ingreso),0.00) as debe, COALESCE(SUM(lrd.valor_egreso),0.00) as haber\n"
                + "FROM talento_humano.th_liquidacion_rol_detalle lrd\n"
                + "INNER JOIN talento_humano.th_liquidacion_rol lr ON lrd.id_liquidacion_rol = lr.id\n"
                + "WHERE lr.id_tipo_rol = ?1 AND lr.estado = true AND lrd.estado = true\n"
                + "GROUP BY lrd.id_cuenta, lrd.id_pres_estructura_programatica, lrd.id_pres_catalogo_presupuesto,\n"
                + "lrd.id_pres_fuente_financiamiento, lrd.partida_presupuestaria\n"
                + "ORDER BY lrd.partida_presupuestaria, lrd.id_cuenta", "MappingRolDetalle")
                .setParameter(1, thTipoRolSeleccionado.getId())
                .getResultList();
        return result;
    }
}
