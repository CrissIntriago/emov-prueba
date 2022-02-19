/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionTipo;
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
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FnTipoExoneracionService extends AbstractService<FnExoneracionTipo> {

    private static final Logger LOG = Logger.getLogger(FnTipoExoneracionService.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnTipoExoneracionService() {
        super(FnExoneracionTipo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnExoneracionTipo> findAllExoneracionesByIdTipo(Long id) {
        List<FnExoneracionTipo> exoneracionesTipos = new ArrayList<>();
        try {
            return exoneracionesTipos = em.createQuery("Select  a from FnExoneracionTipo a where a.estado=true and a.exoneracionClase.id= ?1").setParameter(1, id).getResultList();

        } catch (Exception ex) {
            LOG.log(Level.ALL.SEVERE, null, ex);
            return exoneracionesTipos;
        }
    }

    public FnExoneracionTipo findById(Long id) {
        FnExoneracionTipo exoneracionTipo = new FnExoneracionTipo();
        try {
            exoneracionTipo = (FnExoneracionTipo) em.createQuery("Select a from FnExoneracionTipo a where a.id=?1").setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.ALL.SEVERE, "Error al buscar por id el tipo Exoneracion", ex);
        }
        return exoneracionTipo;
    }
}
