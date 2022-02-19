/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;
import com.gestionTributaria.Entities.NotaDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author Administrator
 */
public class NotaDetalleService extends AbstractService<NotaDetalle>{
    
     private static final Logger LOG = Logger.getLogger(NotaDetalleService.class.getName());
     private static final long serialVersionUID = 1L;
     @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public NotaDetalleService() {
          super(NotaDetalle.class);
    }
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
