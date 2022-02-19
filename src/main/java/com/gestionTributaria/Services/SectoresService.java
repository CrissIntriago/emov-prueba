/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatSectores;
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
public class SectoresService extends AbstractService<CatSectores> {

    private static final Logger LOG = Logger.getLogger(SectoresService.class.getName());
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public SectoresService() {
        super(CatSectores.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatSectores> getAllSectores() {
        List<CatSectores> listaSectores = new ArrayList<>();
        try {
            listaSectores = (List<CatSectores>) em.createQuery("select a from CatSectores a WHERE a.idSector between 1 and 9").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error al traer sectores", ex);
        }
        return listaSectores;
    }
}
