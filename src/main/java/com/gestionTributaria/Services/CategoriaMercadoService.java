/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CategoriaMercado;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CategoriaMercadoService extends AbstractService<CategoriaMercado>{

    private static final Logger LOG = Logger.getLogger(CategoriaMercadoService.class.getName());
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public CategoriaMercadoService(){
        super(CategoriaMercado.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<CategoriaMercado> getCategoriasMercado(){
        return (List<CategoriaMercado>) findByNamedQuery("CategoriaMercado.findAll");
    }
    
}
