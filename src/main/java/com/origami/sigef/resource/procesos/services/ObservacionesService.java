/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.services;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class ObservacionesService extends AbstractService<Observaciones> {

    private static final Logger LOG = Logger.getLogger(ObservacionesService.class.getName());

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession us;

    public ObservacionesService() {
        super(Observaciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Observaciones guardarObservaciones(HistoricoTramites tramite, String usuario, String textObs, String tarea) {
        try {
            Observaciones obs = new Observaciones();
            obs.setEstado(Boolean.TRUE);
            obs.setFecCre(new Date());
            obs.setIdTramite(tramite);
            obs.setObservacion(textObs);
            obs.setTarea(tarea);
            obs.setUserCre(usuario);
            return create(obs);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, tarea, e);
        }
        return null;
    }

    public Observaciones guardarObservaciones(HistoricoTramites tramite, String textObs, String tarea) {
        try {
            Observaciones obs = new Observaciones();
            obs.setEstado(Boolean.TRUE);
            obs.setFecCre(new Date());
            obs.setIdTramite(tramite);
            obs.setObservacion(textObs);
            obs.setTarea(tarea);
            obs.setUserCre(us.getName());
            obs = create(obs);
            return obs;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Save obs " + tarea, e);
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(Observaciones obs) {
        obs.setId(null);
        getEntityManager().persist(obs);
    }
}
