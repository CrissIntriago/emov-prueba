/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.FnResolucion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ResolucionesService extends AbstractService<FnResolucion> {
    
    private static final Logger LOG = Logger.getLogger(ResolucionesService.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    private ManagerService managerService;
    
    public ResolucionesService() {
        super(FnResolucion.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public FnResolucion findByJuicio(CoaJuicio juicio) {
        FnResolucion resolucion = new FnResolucion();
        try {
            System.out.println("ID DEL JUIICO " + juicio.getId());
            resolucion = (FnResolucion) em.createQuery("select a from FnResolucion a where a.juico.id=?1").setParameter(1, juicio.getId()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.ALL.SEVERE, "Error al buscar el juicio", ex);
        }
        return resolucion;
    }
    
    public FnResolucion findByResolucion(FnResolucion resolucion) {
        FnResolucion reso = new FnResolucion();
        try {
            reso = (FnResolucion) em.createQuery("SELECT a from FnResolucion a where a.id=?1").setParameter(1, resolucion.getId()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.ALL.SEVERE, "Error al buscar la resolucion", ex);
        }
        return reso;
    }
    
    public List<FnResolucion> findByTramite(HistoricoTramites tramite) {
        List<FnResolucion> reso = new ArrayList();
        try {
            reso = (List<FnResolucion>) em.createQuery("SELECT a from FnResolucion a where a.tramite=?1").setParameter(1, BigInteger.valueOf(tramite.getId())).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.ALL.SEVERE, "Error al buscar la resolucion por tramite", ex);
        }
        return reso;
    }
    
}
