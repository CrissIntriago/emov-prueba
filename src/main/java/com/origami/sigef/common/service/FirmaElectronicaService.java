/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.Usuarios;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Dependent
public class FirmaElectronicaService extends AbstractService<FirmaElectronica> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FirmaElectronicaService() {
        super(FirmaElectronica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FirmaElectronica getFirmaByUsuario(Usuarios u) {
        try {
            return (FirmaElectronica) em.createQuery("SELECT f FROM FirmaElectronica f where f.usuario = :usuario  and  f.estado = true").setParameter("usuario", u).getSingleResult();
        } catch (NoResultException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
