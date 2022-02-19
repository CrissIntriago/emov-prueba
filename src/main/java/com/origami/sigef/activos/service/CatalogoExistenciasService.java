/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.CatalogoExistencias;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class CatalogoExistenciasService extends AbstractService<CatalogoExistencias> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoExistenciasService() {
        super(CatalogoExistencias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public CatalogoExistencias getIdExistencia(String existencia) {
        try {
            Query query = getEntityManager().createQuery("select c from CatalogoExistencias c WHERE c.idExistencia = :existencia and c.estado = true")
                    .setParameter("existencia", existencia);
            CatalogoExistencias exist = (CatalogoExistencias) query.getSingleResult();

            return exist;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoExistencias> getCatalogoDesxripcion(String catalogo) {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoExistencias c WHERE c.descripcion LIKE '%?1%'")
                    .setParameter(1, catalogo);
            List<CatalogoExistencias> result = (List<CatalogoExistencias>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean existenRegistros(CatalogoExistencias cat) {
        Boolean existe;
        CatalogoExistencias detalle = null;
        try {
            detalle = (CatalogoExistencias) em.createQuery("SELECT DISTINCT(d.catalogoExistencias)  FROM DetalleItem d  WHERE d.estado = true AND d.catalogoExistencias = :cat")
                    .setParameter("cat", cat)
                    .getSingleResult();
        } catch (NoResultException e) {
            detalle = null;
        }
        if (detalle != null) {
            existe = Boolean.TRUE;
            return existe;
        }
        existe = Boolean.FALSE;
        return existe;
    }

}
