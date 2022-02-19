/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.CatPredioSumasAnualesUbicacion;
import com.asgard.Entity.CatUbicacion;
import com.gestionTributaria.Entities.Obra;
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
public class CatPredioSumasAnualesUbicacionServices extends AbstractService<CatPredioSumasAnualesUbicacion> {

    private static final Logger LOG = Logger.getLogger(CatPredioSumasAnualesUbicacionServices.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public CatPredioSumasAnualesUbicacionServices() {
        super(CatPredioSumasAnualesUbicacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatPredioSumasAnualesUbicacion> findAll(Obra obra) {
        List<CatPredioSumasAnualesUbicacion> calculos = new ArrayList<>();
        try {
            calculos = (ArrayList<CatPredioSumasAnualesUbicacion>) em.createQuery("select a from CatPredioSumasAnualesUbicacion a where a.obra.id=?1").setParameter(1, obra.getId()).getResultList();
        } catch (Exception ex) {
            System.out.println("Error al traer los valroe anuales");
        }
        return calculos;
    }

    public Boolean verificarUbicacionObraAnioExistente(Integer anio, Obra obra, CatUbicacion ubicacion) {
        List<CatPredioSumasAnualesUbicacion> calculosAnuales = new ArrayList<>();
        try {
            calculosAnuales = (List<CatPredioSumasAnualesUbicacion>) em.createQuery("select a from CatPredioSumasAnualesUbicacion a where "
                    + "a.anio=?1 and a.ubicacion.id=?2 and a.obra.id=?3").setParameter(1, anio).setParameter(2, ubicacion.getId()).setParameter(3, obra.getId()).getResultList();
            if (calculosAnuales.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "eRROR AL TARER PREDIOS", ex);
            return true;
        }
    }
}
