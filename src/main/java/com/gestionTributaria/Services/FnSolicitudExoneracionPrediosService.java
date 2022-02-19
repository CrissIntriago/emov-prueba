/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
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
public class FnSolicitudExoneracionPrediosService extends AbstractService<FnSolicitudExoneracionPredios> {

    private static final Logger LOG = Logger.getLogger(FnSolicitudExoneracionPrediosService.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnSolicitudExoneracionPrediosService() {
        super(FnSolicitudExoneracionPredios.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnSolicitudExoneracionPredios> getExoneracionByPredio(CatPredio predio) {
        List<FnSolicitudExoneracionPredios> exoneracionPredio = new ArrayList<>();
        try {
            exoneracionPredio = (List<FnSolicitudExoneracionPredios>) em.createQuery("Select a from FnSolicitudExoneracionPredios a where a.predio.id=?1")
                    .setParameter(1, predio.getId()).getResultList();
            System.out.println("EL PREDIO: " + predio + " el resuldao de la funcion: " + exoneracionPredio);
        } catch (Exception ex) {
            Logger.getLogger(FnSolicitudExoneracionPredios.class.getName()).log(Level.SEVERE, "Error al buscar la exoneracion del predio", ex);
        }
        return exoneracionPredio;
    }

}
