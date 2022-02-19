package com.origami.sigef.resource.presupuesto.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CatalogoItemServices extends AbstractService<CatalogoItem> {

    private static final Logger LOG = Logger.getLogger(CatalogoItemServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoItemServices() {
        super(CatalogoItem.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatalogoItem> MostarTodoCatalogo(String codigo) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT m FROM CatalogoItem m INNER JOIN m.catalogo cc WHERE cc.codigo= :codigo")
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public CatalogoItem traerCatalogo(String codigo) {
        CatalogoItem resultado = (CatalogoItem) getEntityManager().createQuery("SELECT m FROM CatalogoItem m where m.codigo like ?1")
                .setParameter(1, codigo).getSingleResult();
        return resultado;
    }

}
