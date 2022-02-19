/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.AppDepartamentoDetalleTitulos;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PlanificacionService extends AbstractService<AppDepartamentoDetalleTitulos> {

    @Inject
    private ManagerService services;
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanificacionService() {
        super(AppDepartamentoDetalleTitulos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AppDepartamentoDetalleTitulos savePlanificacionTitulo(AppDepartamentoDetalleTitulos catPlanificacionTitulos) {
        AppDepartamentoDetalleTitulos cpt = null;
        try {
            if (catPlanificacionTitulos != null) {
                if (catPlanificacionTitulos.getId() != null) {
                    edit(catPlanificacionTitulos);
                    return services.find(AppDepartamentoDetalleTitulos.class, catPlanificacionTitulos.getId());
                } else {
                    catPlanificacionTitulos.setNumRecibo(getMaxNumReciboCatPlanificacionTitulos());
                    cpt = (AppDepartamentoDetalleTitulos) create(catPlanificacionTitulos);
                    return cpt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpt;
    }

    public BigInteger getMaxNumReciboCatPlanificacionTitulos() {
        BigInteger numRecibo = (BigInteger) em.createQuery("SELECT COALESCE(MAX(cp.numRecibo),0) from AppDepartamentoDetalleTitulos cp").getResultStream().findFirst().orElse(0L);
        System.out.println("numRecibo " + numRecibo);
        if (numRecibo == null) {
            return BigInteger.ONE;
        } else {
            return numRecibo.add(BigInteger.valueOf(1));
        }

    }

}
