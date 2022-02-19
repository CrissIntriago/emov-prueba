/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.FnLiquidacionConvenio;
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
public class FnLiquidacionConvenioService extends AbstractService<FnLiquidacionConvenio> {

    private static final Logger LOG = Logger.getLogger(FnLiquidacionConvenioService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnLiquidacionConvenioService() {
        super(FnLiquidacionConvenio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FnLiquidacionConvenio findLiquidacionConvenioIdLiquidacion(FinaRenLiquidacion liquidacion) {
        List<FnLiquidacionConvenio> listaLiquidacion = new ArrayList<>();
        try {
            listaLiquidacion = (List<FnLiquidacionConvenio>) em.createQuery("select a from FnLiquidacionConvenio a where a.liquidacion=?1").setParameter(1, liquidacion).getResultList();
            if (listaLiquidacion == null || listaLiquidacion.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(FnLiquidacionConvenio.class.getName()).log(Level.SEVERE, "Error al buscar la liquidacion en Liquidacion Convenio", ex);
        }
        return listaLiquidacion.get(0);
    }
}
