/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.origami.sigef.common.service.AbstractService;
import com.asgard.Entity.*;
import com.origami.sigef.common.config.CONFIG;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMIEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TituloCreditoService extends AbstractService<TituloCredito> {
    
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    private ManagerService managerService;
    
    public TituloCreditoService() {
        super(TituloCredito.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public TituloCredito findByLiquidacion(FinaRenLiquidacion liquidacion) {
        TituloCredito titulo = new TituloCredito();
        List<TituloCredito> titulos = new ArrayList<>();
        try {
            titulos = (List<TituloCredito>) em.createQuery("select a from TituloCredito a where a.idLiquidacion=?1").setParameter(1, BigInteger.valueOf(liquidacion.getId())).getResultList();
            if (!titulos.isEmpty()) {
                titulo = titulos.get(0);
            } else {
                titulo = null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return titulo;
    }
}
