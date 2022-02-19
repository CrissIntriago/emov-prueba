/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.ventanilla.Entity.SolicitudServicios;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ricardo
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReporteVentanillaService extends AbstractService<SolicitudServicios> {

    private Map<String, Object> param;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReporteVentanillaService() {
        super(SolicitudServicios.class);

    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
