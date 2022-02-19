/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.entity.DocumentoTramitePagos;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Suarez
 */

@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DocumentoTramitePagosService extends AbstractService<DocumentoTramitePagos> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DocumentoTramitePagosService() {
    super(DocumentoTramitePagos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
