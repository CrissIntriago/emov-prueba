/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.arrendamiento.service;

import com.origami.sigef.arrendamiento.entities.Arrendamiento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ArrendamientoService extends AbstractService<Arrendamiento> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ArrendamientoService() {
        super(Arrendamiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Cliente findProveedor(String identificacion, String ruc) {
        try {
            String sql = "";
            if (ruc != null && ruc.length() > 0) {
                sql = "SELECT c FROM Cliente c WHERE CONCAT(c.identificacion, c.ruc) = ?1";
            } else {
                sql = "SELECT c FROM Cliente c WHERE c.identificacion = ?1";
            }
            Query query = em.createQuery(sql)
                    .setParameter(1, identificacion + ruc);
            Cliente resultado = (Cliente) query.getSingleResult();
            return resultado;
        } catch (NoResultException e) {
            return new Cliente();
        }
    }

    public List<Arrendamiento> findArrendamiento() {
        try {
            List<Arrendamiento> list = (List<Arrendamiento>) em.createQuery("SELECT a FROM Arrendamiento a WHERE a.estado = TRUE AND a.fechaVigencia>:fecha")
                    .setParameter("fecha", new Date())
                    .getResultList();
            return list;
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public Arrendamiento findById(BigInteger id) {
        Arrendamiento resultado = (Arrendamiento) em.createQuery("SELECT a FROM Arrendamiento a WHERE a.id=:id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return resultado;
    }
}
