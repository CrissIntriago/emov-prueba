/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RenFacturaPago;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
@javax.enterprise.context.Dependent
public class RenFacturaPagoService extends AbstractService<RenFacturaPago> {
    
    private static final Logger LOG = Logger.getLogger(RenFacturaPagoService.class.getName());
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public RenFacturaPagoService() {
        super(RenFacturaPago.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
