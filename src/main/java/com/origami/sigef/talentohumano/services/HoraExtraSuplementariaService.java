/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class HoraExtraSuplementariaService extends AbstractService<HoraExtraSuplementaria> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public HoraExtraSuplementariaService() {
        super(HoraExtraSuplementaria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<HoraExtraSuplementaria> getListaHoraExtrSupl(short anio, TipoRol mes) {
        try {
            Query query = em.createQuery("SELECT h FROM HoraExtraSuplementaria h WHERE h.estado = TRUE AND h.periodo = ?1 AND h.tipoRol = ?2 ORDER BY h.servidor.persona.identificacion")
                    .setParameter(1, anio)
                    .setParameter(2, mes);
            List<HoraExtraSuplementaria> result = (List<HoraExtraSuplementaria>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public List<HoraExtraSuplementaria> getHorasSuplemxTipoRol(TipoRol tipo) {
        try {
            Query query = em.createQuery("SELECT h FROM HoraExtraSuplementaria h WHERE h.estado = TRUE AND h.tipoRol = ?1")
                    .setParameter(1, tipo);
            List<HoraExtraSuplementaria> result = (List<HoraExtraSuplementaria>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public HoraExtraSuplementaria getHoraExtraSuplementaria(Servidor s, TipoRol r) {
        try {
            Query query = em.createQuery("SELECT h FROM HoraExtraSuplementaria h WHERE h.estado = TRUE AND h.servidor = ?1 AND h.tipoRol = ?2")
                    .setParameter(1, s)
                    .setParameter(2, r);
            HoraExtraSuplementaria result = (HoraExtraSuplementaria) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

}
