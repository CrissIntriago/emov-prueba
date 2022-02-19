/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.ServiceValidacionData;

import com.gestionTributaria.EntitiesValidacion.SeguimientoContibuyente;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@Stateless
public class SeguimientoContibuyenteService extends AbstractService<SeguimientoContibuyente> {
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public SeguimientoContibuyenteService() {
        super(SeguimientoContibuyente.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public SeguimientoContibuyente listaSeguimientoUser(Usuarios u) {
        
        SeguimientoContibuyente result;
        List<BigInteger> data = (List<BigInteger>) em.createNativeQuery("select s.id from seguimiento_contibuyente s where cast(s.fecha_hasta as date) >= now() and s.usuario=?1")
                .setParameter(1, u.getId()).getResultList();
        
        if (!data.isEmpty()) {
            data.forEach(x -> System.out.println("id " + x));
            result = getIdSearch(data.get(0));
            
            if (result != null) {
                return result;
            }
            
            return new SeguimientoContibuyente();
        }
        
        return new SeguimientoContibuyente();
    }
    
    public SeguimientoContibuyente getIdSearch(BigInteger id) {
        
        return (SeguimientoContibuyente) em.createQuery("SELECT s FROM SeguimientoContibuyente s where s.id=:id")
                .setParameter("id", id.longValue()).getResultStream().findFirst().orElse(new SeguimientoContibuyente());
    }
    
}
