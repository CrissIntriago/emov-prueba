/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CenAvaluoMunicipal;
import com.gestionTributaria.Entities.Obra;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
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
public class ObraServices extends AbstractService<Obra> {
    
    private static final Logger LOG = Logger.getLogger(ObraServices.class.getName());
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public ObraServices() {
        super(Obra.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<Obra> getObras() {
        List<Obra> obras = new ArrayList<>();
        try {
            obras = (List<Obra>) em.createQuery("Select a from Obra a where a.estado=true ").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar la obra", ex);
        }
        return obras;
    }
    
    public List<CatalogoItem> getConfiguracioness() {
        List<CatalogoItem> configuraciones = new ArrayList<>();
        try {
            configuraciones = (List<CatalogoItem>) em.createQuery("select a from CatalogoItem a where a.catalogo.codigo like ?1 ").setParameter(1, "configuracion_cem").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar las configuraciones", ex);
        }
        return configuraciones;
    }
    
    public List<CenAvaluoMunicipal> findConfiguracion(Obra obra) {
        List<CenAvaluoMunicipal> configuraciones = new ArrayList<>();
        try {
            configuraciones = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal "
                    + "a where a.obra = ?1 ").setParameter(1, BigInteger.valueOf(obra.getId())).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar las configuraciones", ex);
        }
        return configuraciones;
    }
}
