/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.PrestamosActivos;
import com.origami.sigef.common.entities.PrestamosActivosBienes;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class PrestamosActivosBienesService extends AbstractService<PrestamosActivosBienes> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PrestamosActivosBienesService() {
        super(PrestamosActivosBienes.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean getListaComponentes(PrestamosActivos prestamosActivos) {
        try {
            List<PrestamosActivosBienes> resultado = (List<PrestamosActivosBienes>) em.createQuery("SELECT p FROM PrestamosActivosBienes p "
                    + "WHERE p.prestamosActivos =:prestamosActivos "
                    + "AND p.estado = TRUE "
                    + "AND p.componente = TRUE")
                    .setParameter("prestamosActivos", prestamosActivos)
                    .getResultList();
            return resultado != null && !resultado.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}
