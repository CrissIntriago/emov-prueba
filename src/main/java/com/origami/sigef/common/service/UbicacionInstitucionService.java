/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.UbicacionInstitucion;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Dependent
public class UbicacionInstitucionService extends AbstractService<UbicacionInstitucion> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public UbicacionInstitucionService() {
        super(UbicacionInstitucion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean existenRegistros(UbicacionInstitucion ubi) {
        Boolean existe;
        UbicacionInstitucion ubicacion = null;
        try {
            ubicacion = (UbicacionInstitucion) em.createQuery("SELECT DISTINCT(u.ubicacionInstitucion)  FROM UnidadAdministrativa u  WHERE u.estado = true and u.estadoActivo = true AND u.ubicacionInstitucion = :ubi")
                    .setParameter("ubi", ubi)
                    .getSingleResult();
        } catch (NoResultException e) {
            ubicacion = null;
        }
        if (ubicacion != null) {
            existe = Boolean.TRUE;
            return existe;
        }
        existe = Boolean.FALSE;
        return existe;
    }

    public List<UbicacionInstitucion> getAllUbicacionInstitucionByEstadoTrue() {
        try {
            return em.createQuery("SELECT u FROM UbicacionInstitucion u where u.estado = true").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
