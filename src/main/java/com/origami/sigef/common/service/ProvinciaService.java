/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.Provincia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class ProvinciaService extends AbstractService<Provincia> {
    
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public ProvinciaService() {
        super(Provincia.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }
    
    public List<Provincia> getProvincias() {
        List<Provincia> resultado = (List<Provincia>) getEntityManager().createQuery("SELECT p FROM Provincia p WHERE p.habilitado = true")
                .getResultList();
        return resultado;
    }
    
    public Provincia provinciaHallada(String codigo) {
        return (Provincia) em.createQuery("SELECT p FROM Provincia p where p.codigo=:codigo").setParameter("codigo", codigo).getResultList().get(0);
    }
    
}
