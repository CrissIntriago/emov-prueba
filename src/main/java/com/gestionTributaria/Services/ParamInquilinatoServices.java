/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.ParamInquilinato;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ParamInquilinatoServices extends AbstractService<ParamInquilinato> {

    private static final Logger LOG = Logger.getLogger(ObservacionesServices.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ParamInquilinatoServices() {
        super(ParamInquilinato.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
