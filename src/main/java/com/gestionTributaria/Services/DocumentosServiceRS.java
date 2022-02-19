/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.Documentos;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DocumentosServiceRS extends AbstractService<Documentos> {

    private static final Logger LOG = Logger.getLogger(DocumentosServiceRS.class.getName());

    private static final long serialVersionUID = 1L;

    @Inject
    private ManagerService manager;
    @Inject
    private UserSession session;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DocumentosServiceRS() {
        super(Documentos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
