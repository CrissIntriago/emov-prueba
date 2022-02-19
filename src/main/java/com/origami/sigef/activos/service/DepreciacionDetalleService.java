/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.activos.entities.DepreciacionDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
public class DepreciacionDetalleService extends AbstractService<DepreciacionDetalle> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DepreciacionDetalleService() {
        super(DepreciacionDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DepreciacionDetalle> getList(Depreciacion depreciacion) {
        List<DepreciacionDetalle> resultado = (List<DepreciacionDetalle>) em.createQuery("SELECT dd FROM DepreciacionDetalle dd WHERE dd.depreciacion=:depreciacion AND dd.estado=TRUE ORDER BY dd.secuencial ASC")
                .setParameter("depreciacion", depreciacion)
                .getResultList();
        return resultado;
    }

    public List<PartePresupuestariaModel> getRegistroContable(Depreciacion depreciacion) {
        List<PartePresupuestariaModel> result = (List<PartePresupuestariaModel>) em.createNativeQuery("SELECT * FROM activos.fs_depreciacion_detalle(?1)", "depreciacionMapping")
                .setParameter(1, depreciacion.getId())
                .getResultList();
        return result;
    }

}
