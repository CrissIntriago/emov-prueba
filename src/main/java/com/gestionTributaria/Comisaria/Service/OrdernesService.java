/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Service;

import com.gestionTributaria.Entities.CoaAbogado;
import com.gestionTributaria.Entities.Ordenes;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMIEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class OrdernesService extends AbstractService<Ordenes> {
    
    private static final Logger LOG = Logger.getLogger(OrdernesService.class.getName());
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public OrdernesService() {
        super(Ordenes.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public Ordenes findByOrden(Long numeroTranite) {
        List<Ordenes> ordenes = new ArrayList<>();
        Ordenes orden = new Ordenes();
        try {
            System.out.println("Numero de tramite: " + numeroTranite);
            ordenes = (List<Ordenes>) em.createQuery("select a from Ordenes a where a.numTramite=?1 ").setParameter(1, BigInteger.valueOf(numeroTranite)).getResultList();
            if (ordenes.isEmpty()) {
                orden = null;
            } else {
                orden = ordenes.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaAbogado.class.getName()).log(Level.SEVERE, "ERROR AL TRAER Orden", ex);
        }
        return orden;
    }
}
