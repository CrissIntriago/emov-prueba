/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Controller.CabeceraCertificado;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CertificadosServices extends AbstractService<CabeceraCertificado> {

    private static final Logger LOG = Logger.getLogger(CabeceraCertificado.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CertificadosServices() {
        super(CabeceraCertificado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
