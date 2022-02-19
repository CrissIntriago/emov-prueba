/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenTipoValor;
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
public class FinaRenTipoValorServices extends AbstractService<FinaRenTipoValor> {

    private static final Logger LOG = Logger.getLogger(FinaRenTipoValorServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenTipoValorServices() {
        super(FinaRenTipoValor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FinaRenTipoValor> getRenTipovalor(String prefijo) {
        List<FinaRenTipoValor> tipoValores = new ArrayList<>();
        try {
            return tipoValores = em.createQuery("Select a from FinaRenTipoValor a where a.prefijo= ?1").setParameter(1, prefijo).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE,null, ex);
            return tipoValores;
        }
    }
}
