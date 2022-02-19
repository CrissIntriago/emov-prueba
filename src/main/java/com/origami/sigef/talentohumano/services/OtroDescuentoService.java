/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.OtroDescuento;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class OtroDescuentoService extends AbstractService<OtroDescuento> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public OtroDescuentoService() {
        super(OtroDescuento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<OtroDescuento> getListaDwsc(TipoRol rol) {
        if (rol == null) {
            return null;
        }
        if (rol.getId() == null) {
            return null;
        }
        Query query = em.createQuery("SELECT d FROM OtroDescuento d WHERE d.estado = TRUE AND d.tipoRol = ?1")
                .setParameter(1, rol);
        List<OtroDescuento> result = (List<OtroDescuento>) query.getResultList();
        return result;
    }

    public OtroDescuento getOtroDescuentos(Servidor s, TipoRol r) {
        try {
            Query query = em.createQuery("SELECT o FROM OtroDescuento o WHERE o.estado =TRUE AND o.rolPago.servidor = ?1 AND o.tipoRol = ?2")
                    .setParameter(1, s).setParameter(2, r);
            OtroDescuento result = (OtroDescuento) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<OtroDescuento> getVerificacion(BigInteger num) {
        List<OtroDescuento> result = (List<OtroDescuento>) em.createQuery("SELECT o FROM OtroDescuento o WHERE o.numTramite= :num")
                .setParameter("num", num).getResultList();
        return result;
    }

    public Cliente clienteNotificacion(BigInteger num) {
        Cliente result = (Cliente) em.createQuery("SELECT  cl FROM OtroDescuento o LEFT JOIN o.rolPago r INNER JOIN r.servidor s INNER JOIN s.persona cl WHERE o.numTramite= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(null);
        return result;
    }

}
