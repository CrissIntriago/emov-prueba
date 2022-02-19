/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CuentaBancaria;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class BancoService extends AbstractService<Banco> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BancoService() {
        super(Banco.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Banco> getNumeroCuetas() {
        try {
            List<Banco> result = (List<Banco>) em.createQuery("SELECT b FROM Banco b WHERE b.numeroCuenta IS NOT NULL AND b.estado = TRUE")
                    //                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean getBancoUsadoBeneficiario(Banco banco) {
        List<CuentaBancaria> result = (List<CuentaBancaria>) em.createQuery("SELECT cb FROM CuentaBancaria cb WHERE cb.estado = TRUE AND cb.nombreBanco=:banco")
                .setParameter("banco", banco)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean getBancoUsadoCuentaBancaria(Banco banco) {
        List<DetalleBanco> result = (List<DetalleBanco>) em.createQuery("SELECT db FROM DetalleBanco db WHERE db.banco=:banco AND db.estado=TRUE")
                .setParameter("banco", banco)
                .getResultList();
        return !result.isEmpty();
    }

    public List<Banco> getBancoList() {
        List<Banco> lista = (List<Banco>) em.createQuery("SELECT b FROM Banco b WHERE b.estado = TRUE")
                .getResultList();
        return lista;
    }

    public Banco getBancoByNombreBanco(String nombreBanco) {
        List<Banco> lista = (List<Banco>) em.createQuery("SELECT b FROM Banco b WHERE b.nombreBanco = :nombreBanco")
                .setParameter("nombreBanco", nombreBanco)
                .getResultList();
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

}
