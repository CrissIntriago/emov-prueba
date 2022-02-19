/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.service;

import com.origami.sigef.arrendamiento.entities.Locales;
import com.origami.sigef.arrendamiento.entities.TarifasArriendo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class TarifasArriendoService extends AbstractService<TarifasArriendo> {

    private static final Logger LOG = Logger.getLogger(TarifasArriendoService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TarifasArriendoService() {
        super(TarifasArriendo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean findRelacionados(TarifasArriendo tarifasArriendo) {
        try {
            Query query = em.createQuery("SELECT l FROM Locales l WHERE l.estado = true AND l.idTarifa = ?1 AND l.estadoLocal = FALSE")
                    .setParameter(1, tarifasArriendo);
            List<Locales> resultado = (List<Locales>) query.getResultList();
            return resultado != null && !resultado.isEmpty();
        } catch (NoResultException e) {
            return false;
        }
    }

    public List<TarifasArriendo> getTarifasList() {
        try {
            Query query = em.createQuery("SELECT ta FROM TarifasArriendo ta WHERE ta.estado = TRUE ORDER BY ta.codigo ASC");
            List<TarifasArriendo> resultado = (List<TarifasArriendo>) query.getResultList();
            return resultado;
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<TarifasArriendo> getTarifasList(Mercado mc) {
        try {
            Query query = em.createQuery("SELECT ta FROM TarifasArriendo ta WHERE ta.estado = TRUE AND ta.mercado=:mercado ORDER BY ta.codigo ASC")
                    .setParameter("mercado", mc);
            List<TarifasArriendo> resultado = (List<TarifasArriendo>) query.getResultList();
            return resultado;
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<Mercado> getMercadosActivos() {
        List<Mercado> mercados = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT m FROM Mercado m where m.estado = TRUE ");
            mercados = (List<Mercado>) query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return mercados;
    }

    public Long getMaxcodigo(Mercado mc, TarifasArriendo tr) {
        try {
            
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
