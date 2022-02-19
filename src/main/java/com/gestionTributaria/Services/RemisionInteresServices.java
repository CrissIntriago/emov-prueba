/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.RemisionInteresMulta;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RemisionInteresServices extends AbstractService<RemisionInteresMulta> {

    private static final Logger LOG = Logger.getLogger(RemisionInteresServices.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RemisionInteresServices() {
        super(RemisionInteresMulta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean aplicaMulta(FinaRenTipoLiquidacion tipoLiquidacion, Integer anio) {
        List<RemisionInteresMulta> list = (List<RemisionInteresMulta>) em.createQuery("SELECT r FROM RemisionInteresMulta r WHERE r.tipoLliquidacion=:tipo and r.anio=:anio")
                .setParameter("tipo", tipoLiquidacion).setParameter("anio", anio).getResultList();
        if (list != null && !list.isEmpty()) {

            if (list.get(0).getMulta() != null) {
                return list.get(0).getMulta();
            } else {
                return true;
            }

        }

        return true;
    }

    public boolean aplicaInterees(FinaRenTipoLiquidacion tipoLiquidacion, Integer anio) {
        List<RemisionInteresMulta> list = (List<RemisionInteresMulta>) em.createQuery("SELECT r FROM RemisionInteresMulta r WHERE r.tipoLliquidacion=:tipo and r.anio=:anio")
                .setParameter("tipo", tipoLiquidacion).setParameter("anio", anio).getResultList();
        if (list != null && !list.isEmpty()) {

            if (list.get(0).getInteres() != null) {
                return list.get(0).getInteres();
            } else {
                return true;
            }
        }

        return true;
    }

}
