/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.MantenimientoClave;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.Date;
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
public class MantenimientoClaveService extends AbstractService<MantenimientoClave> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MantenimientoClaveService() {
        super(MantenimientoClave.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public MantenimientoClave findByUsuario(String user,Boolean accion) {
        try {
            BigInteger result = null;
            result = (BigInteger) em.createNativeQuery("SELECT mc.id FROM auth.mantenimiento_clave mc WHERE mc.user_name=?1 \n"
                    + "AND cast (date_part('hour',age(cast (now() as timestamp), cast(mc.fecha_generacion as timestamp))) as bigint) < mc.tiempo_validez AND mc.activo=TRUE AND mc.servicio = ?2")
                    .setParameter(1, user)
                    .setParameter(2, accion)
                    .getSingleResult();
            return getFindById(result);
        } catch (Exception e) {
            return null;
        }
    }

    private MantenimientoClave getFindById(BigInteger id) {
        MantenimientoClave resultado = (MantenimientoClave) em.createQuery("SELECT c FROM MantenimientoClave c WHERE c.id=:id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return resultado;
    }
}
