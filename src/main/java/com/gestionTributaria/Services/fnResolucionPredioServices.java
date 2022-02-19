/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Entities.FnResolucionPredio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMIEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class fnResolucionPredioServices extends AbstractService<FnResolucionPredio> {

    private static final Logger LOG = Logger.getLogger(fnResolucionPredioServices.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public fnResolucionPredioServices() {
        super(FnResolucionPredio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnResolucionPredio> findByIdResolucion(FnResolucion resol) {
        List<FnResolucionPredio> resoluciones = new ArrayList<>();
        try {
            resoluciones = (List<FnResolucionPredio>) em.createQuery("select a from FnResolucionPredio a where a.resolucion.id=?1").setParameter(1, resol.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar los predios relacionados con la resolución", ex);
        }
        return resoluciones;
    }
}
