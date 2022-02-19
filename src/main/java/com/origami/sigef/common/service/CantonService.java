/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.Canton;
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
public class CantonService extends AbstractService<Canton> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CantonService() {
        super(Canton.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Canton> getCantones(Provincia provincia) {
        //SELECT c FROM Canton c INNER JOIN c. c WHERE c.codigo =:codigo
        List<Canton> resultado = (List<Canton>) getEntityManager().createQuery("SELECT c FROM Canton c INNER JOIN c.idProvincia p WHERE p.id=:provincia AND p.habilitado = true AND c.habilitado=true")
                .setParameter("provincia", provincia.getId())
                .getResultList();
        return resultado;
    }

    public Canton getCanton(String name) {
        return (Canton) em.createQuery("SELECT c FROM Canton c where c.canton=:canton").setParameter("canton", name).getResultList().get(0);
    }
    
     public Canton getCantonCodigo(String codigo) {
        return (Canton) em.createQuery("SELECT c FROM Canton c where c.codigo =:canton").setParameter("canton", codigo).getResultList().get(0);
    }

    public List<Canton> getCantones() {
        return (List<Canton>) em.createQuery("select a from Canton a where a.habilitado=true").getResultList();
    }
}
