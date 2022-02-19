/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThTipoRolService extends AbstractService<ThTipoRol> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThTipoRolService() {
        super(ThTipoRol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ThTipoRol> getRolAnio(Short anio) {
        List<ThTipoRol> result = (List<ThTipoRol>) em.createQuery(QUERY.FIND_TIPO_ROL_PERIODO)
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public List<ThTipoRol> getServidorPublico(Servidor servidor, Short periodo) {
        return (List<ThTipoRol>) em.createQuery(QUERY.FIND_ROLES_SERVIDOR_PERIODO)
                .setParameter(1, servidor)
                .setParameter(2, periodo)
                .getResultList();
    }

    public Integer getCount(ThTipoRol thtipoRol) {
        Long temp = (Long) em.createQuery("SELECT COUNT(dl.id) FROM ThDiasLaborados dl WHERE dl.estado = true AND dl.idTipoRol = ?1 AND dl.aparecerRol = true")
                .setParameter(1, thtipoRol)
                .getSingleResult();
        return temp.intValue();
    }
}
