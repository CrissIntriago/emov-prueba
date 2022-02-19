/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThGrupoServidor;
import com.origami.sigef.resource.talento_humano.entities.ThGrupoServidorDetalle;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThGrupoServidorDetalleService extends AbstractService<ThGrupoServidorDetalle> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThGrupoServidorDetalleService() {
        super(ThGrupoServidorDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Servidor> getServidores() {
        return (List<Servidor>) em.createNativeQuery("SELECT s.* FROM talento_humano.servidor s\n"
                + "INNER JOIN cliente cl ON s.persona = cl.id\n"
                + "WHERE s.estado = true AND s.jubilado = false\n"
                + "AND NOT EXISTS (SELECT NULL\n"
                + "			FROM talento_humano.th_grupo_servidor_detalle d\n"
                + "			WHERE d.id_servidor = s.id AND d.estado = true)\n"
                + "ORDER BY cl.apellido ASC ", Servidor.class)
                .getResultList();
    }

    public Integer getCantidad(ThGrupoServidor thGrupoServidor) {
        return (Integer) em.createNativeQuery("SELECT COUNT(*) FROM talento_humano.th_grupo_servidor_detalle s WHERE s.estado = true AND s.id_grupo = ?1")
                .setParameter(1, thGrupoServidor.getId())
                .getSingleResult();
    }
}
