/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Provincia;
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

/**
 *
 * @author ANGEL NAVARRO
 */
@Produces({"application/Json", "text/xml"})
@Path("/transporte/provincia/")
public class ProvinciaRESTFacade {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    protected EntityManager entityManager;

    @GET
    @Produces({MediaType.APPLICATION_JSON})

    public String findAll() {
        return new JsonUtil().toJson(find(true, -1, -1));
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return new JsonUtil().toJson(find(false, max, first));
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)

    public String count() {
        try {
            Query query = entityManager.createQuery("SELECT count(o) FROM Provincia AS o");
            return query.getSingleResult().toString();
        } finally {

        }
    }

    private List<Provincia> find(boolean all, int maxResults, int firstResult) {
        try {
            Query query = entityManager.createQuery("SELECT object(o) FROM Provincia AS o");
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {

        }
    }

}
