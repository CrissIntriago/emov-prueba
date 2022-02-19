/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.CatalogoMedida;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@Stateless @javax.enterprise.context.Dependent
public class CatalogoMedidaService extends AbstractService<CatalogoMedida> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoMedidaService() {
        super(CatalogoMedida.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

//    public List<CatalogoMedida> getHijosByPadre(CatalogoMedida padre) {
//        return findByNamedQuery("CatalogoMedida.findByPadre", new Object[]{padre.getId()});
//    }
    
    public List<DetalleItem> verificarEnDetalleItem(CatalogoMedida med) {
        Query query = em.createQuery("SELECT i FROM DetalleItem i WHERE i.estado = true AND i.tipoMedida.id = :medida").setParameter("medida", med.getId());
        List<DetalleItem> result = query.getResultList();
        return result;
    }

}
