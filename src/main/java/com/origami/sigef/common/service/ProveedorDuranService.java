/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ProveedorDuran;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProveedorDuranService extends AbstractService<ProveedorDuran> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProveedorDuranService() {
        super(ProveedorDuran.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProveedorDuran> getProveedorIfNotExist() {
        try {

            return em.createQuery("SELECT d FROM ProveedorDuran d where d.identificacion not in (SELECT c.identificacion FROM Proveedor p JOIN p.cliente c ) AND d.tipoPersona in ('NATURAL','JURIDICO') ").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ProveedorDuran> getProveedorDuranByTipo(String tipo) {
        try {

            return em.createQuery("SELECT d FROM ProveedorDuran d where d.tipo =:tipo AND d.tipoPersona in ('NATURAL','JURIDICO') ")
                    .setParameter("tipo", tipo)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
