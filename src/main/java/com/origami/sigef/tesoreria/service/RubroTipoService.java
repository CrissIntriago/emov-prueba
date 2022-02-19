/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.RubroTipo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class RubroTipoService extends AbstractService<RubroTipo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RubroTipoService() {
        super(RubroTipo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<RubroTipo> findByTipo(String tipo) {
        return this.findByNamedQuery("RubroTipo.findByTipo", tipo);
    }

    public List<RubroTipo> findByTipoFact() {
        List<RubroTipo> result = (List<RubroTipo>) em.createQuery("SELECT r FROM RubroTipo r WHERE r.estado = TRUE AND r.tipo IS NOT NULL")
                .getResultList();
        return result;
    }

    public List<RubroTipo> findByTipoItemTarifario() {
        List<RubroTipo> result = (List<RubroTipo>) em.createQuery("SELECT r FROM RubroTipo r WHERE r.estado = TRUE AND r.tipo IS NULL")
                .getResultList();
        return result;
    }

    public List<String> getTipos(String tipo) {
        List<String> resultado = (List<String>) em.createQuery("SELECT rt.descripcion FROM RubroTipo rt WHERE rt.estado=TRUE AND rt.tipo=:tipo")
                .setParameter("tipo", tipo)
                .getResultList();
        return resultado;
    }

    public RubroTipo getRubroTipoCodigo(String codigo) {
        try {
            Query query = em.createQuery("SELECT rt FROM RubroTipo rt WHERE rt.codigo = ?1 AND rt.estado = true")
                    .setParameter(1, codigo);
            RubroTipo result = (RubroTipo) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
