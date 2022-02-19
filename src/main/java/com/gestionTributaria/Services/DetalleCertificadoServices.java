/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Controller.DetalleCertificado;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class DetalleCertificadoServices extends AbstractService<DetalleCertificado> {

    private static final Logger LOG = Logger.getLogger(DetalleCertificadoServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleCertificadoServices() {
        super(DetalleCertificado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
