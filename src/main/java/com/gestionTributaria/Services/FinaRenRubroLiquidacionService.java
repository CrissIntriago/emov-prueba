package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FinaRenRubroLiquidacionService extends AbstractService<FinaRenRubrosLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FinaRenRubroLiquidacionService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenRubroLiquidacionService() {
        super(FinaRenRubrosLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FinaRenRubrosLiquidacion findByIdRubroLiquidacion(Long idRubro) {
        FinaRenRubrosLiquidacion rubro = new FinaRenRubrosLiquidacion();
        try {
            rubro = (FinaRenRubrosLiquidacion) em.createQuery("select a from"
                    + " FinaRenRubrosLiquidacion a where a.id=?1").setParameter(1, idRubro).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el rubro liquidacion", ex);
            return null;
        }
        return rubro;
    }
}
