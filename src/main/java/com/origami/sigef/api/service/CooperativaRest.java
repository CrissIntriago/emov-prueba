/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.util.JsonUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ANGEL NAVARRO
 */
@Produces({"application/Json", "text/xml"})
@Path("/transporte/cooperativas/")
public class CooperativaRest {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    protected EntityManager entityManager;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public String find(@PathParam("id") Long id) {
        return new JsonUtil().toJson(entityManager.find(Cooperativa.class, id));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public String findAll() {
        System.out.println("/transporte/cooperativas/");
        return new JsonUtil().toJson(find(true, -1, -1));
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public String findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return new JsonUtil().toJson(find(false, max, first));
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String count() {
        try {
            Query query = entityManager.createQuery("SELECT count(o) FROM Cooperativa AS o");
            return query.getSingleResult().toString();
        } finally {
        }
    }

    private List<Cooperativa> find(boolean all, int maxResults, int firstResult) {
        try {
            Query query = entityManager.createQuery("SELECT object(o) FROM Cooperativa AS o");
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
        }
    }

}
