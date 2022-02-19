/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service.doc;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.doc.DocumentoBlob;
import com.origami.sigef.common.service.AbstractService;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

/**
 *
 * @author Sandra Arroba - Angel Navarro
 */
@Stateless
@javax.enterprise.context.Dependent
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DocumentoBlobService extends AbstractService<DocumentoBlob> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DocumentoBlobService() {
        super(DocumentoBlob.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
