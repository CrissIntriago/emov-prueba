/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThRetencionesImpuestoRenta;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Stateless
public class ThRetencionesImpuestoRentaService extends AbstractService<ThRetencionesImpuestoRenta> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThRetencionesImpuestoRentaService() {
        super(ThRetencionesImpuestoRenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean loadData(ThTipoRol thTipoRol, String codConfig, String usercreacion, Date fechacreacion) {
        int values = (Integer) em.createNativeQuery("select * from talento_humano.fs_generar_impuesto_renta(?1,?2,?3,?4,?5)")
                .setParameter(1, thTipoRol.getId())
                .setParameter(2, codConfig)
                .setParameter(3, thTipoRol.getPeriodo())
                .setParameter(4, usercreacion)
                .setParameter(5, fechacreacion)
                .getSingleResult();
        List<ThRetencionesImpuestoRenta> result = findByNamedQuery("ThRetencionesImpuestoRenta.findByIdTipoRol", thTipoRol);
        return !result.isEmpty();
    }
}
