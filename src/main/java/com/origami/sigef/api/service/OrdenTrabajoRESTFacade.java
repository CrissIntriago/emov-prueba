/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.OrdenDet;
import com.origami.sigef.common.entities.OrdenTrabajo;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.ordenes.services.OrdenService;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ANGEL NAVARRO
 */
@Produces({"application/Json", "text/xml"})
@Path("/transporte/ordenes/")
public class OrdenTrabajoRESTFacade {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    protected EntityManager entityManager;
    @Inject
    protected OrdenService service;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public String find(@PathParam("id") Long id) {
        return new JsonUtil().toJson(entityManager.find(OrdenTrabajo.class, id));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String findAll() {
        List<OrdenTrabajo> find = find(true, -1, -1);
        if (find != null) {
            for (OrdenTrabajo ot : find) {
                ot.getCooperativa().setCooperativaVehiculoList(null);
                ot.getCooperativa().setCooperativaSocioList(null);
                ot.setResponsable(null);
                ot.setSupervisor(null);
            }
        }
        return new JsonUtil().toJson(find);
    }

    @POST
    @Path(value = "/sync/{numOrden}/usr/{usr}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response syncOrden(@PathParam("numOrden") Long numOrden, @PathParam("usr") String usr, String data) {
        JsonUtil jsonUtils = new JsonUtil();
        Boolean ok = false;
        try {
            String decodeData = java.net.URLDecoder.decode(data, StandardCharsets.UTF_8.name());
            decodeData = decodeData.replace("data={", "{");
            decodeData = decodeData.replace("}&", "}");
            //System.out.println(" ////\n" + decodeData);
            OrdenTrabajo ordenMovil = (OrdenTrabajo) jsonUtils.fromJson(decodeData, OrdenTrabajo.class);
//            String base64Image = data.split(",")[1];
//            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            if (ordenMovil != null) {
                //System.out.println(numOrden + " Sync db " + service.find(numOrden));
                OrdenTrabajo o = service.find(numOrden);
                o.setUsrAct(ordenMovil.getUsrAct());
                o.setFecAct(ordenMovil.getFecAct());
                o.setEstadoOt(ordenMovil.getEstadoOt());
                if (o.getDetelles() == null) {
                    o.setDetelles(new ArrayList<>());
                    for (OrdenDet detalle : ordenMovil.getDetelles()) {
                        detalle.setEstadoDet("CE");
                        detalle.setId(null);
                        detalle.setOrden(o);
                        o.getDetelles().add(detalle);
                    }
                } else {
                    for (OrdenDet detalle : o.getDetelles()) {
                        int indexOf = ordenMovil.getDetelles().indexOf(detalle);
                        if (indexOf > -1) {
                            OrdenDet od = ordenMovil.getDetelles().get(indexOf);
                            detalle.setDatoActulizado(od.getDatoActulizado());
                            detalle.setFecCre(od.getFecAct());
                            detalle.setFecAct(od.getFecAct());
                            detalle.setUsrAct(od.getUsrAct());
                            detalle.setEstadoDet("CE");
                        }
                    }
                    for (OrdenDet detalle : ordenMovil.getDetelles()) {
                        if (detalle.getNuevo()) {
                            detalle.setEstadoDet("CE");
                            detalle.setId(null);
                            detalle.setOrden(o);
                            o.getDetelles().add(detalle);
                        }
                    }
                }
                service.edit(o);
            }
            ok = true;
            Response.Status estatus;
            if (ok) {
                estatus = Response.Status.OK;
            } else {
                estatus = Response.Status.NOT_MODIFIED;
            }
            String js = jsonUtils.toJson("{}");
            return getResponse(estatus, js);
        } catch (IOException e) {
            System.out.println("Error " + e);
            return getResponse(Response.Status.EXPECTATION_FAILED, "Error al procesar datos " + e.getMessage());
        }
    }

    private Response getResponse(Response.Status status, Object obj) {
        return Response.status(status).entity(obj)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
                .header("Content-Type", "application/json;charset=UTF-8")
                .allow("OPTIONS")
                .build();
    }

    @GET
    @Path("name/{user}/{last}")
    @Produces({MediaType.APPLICATION_JSON})
    public String findAllUser(@PathParam("user") String user, @PathParam("last") String last) {
        List<OrdenTrabajo> findUser = findUser(true, -1, -1, user, Integer.valueOf(last));
        if (findUser != null) {
            for (OrdenTrabajo ot : findUser) {
                ot.getCooperativa().setCooperativaVehiculoList(null);
                ot.setResponsable(null);
                ot.setSupervisor(null);
            }
        }
        return new JsonUtil().toJson(findUser);
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public String findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        List<OrdenTrabajo> find = find(false, max, first);
        if (find != null) {
            for (OrdenTrabajo ot : find) {
                ot.getCooperativa().setCooperativaVehiculoList(null);
                ot.setResponsable(null);
                ot.setSupervisor(null);
            }
        }
        return new JsonUtil().toJson(find);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        try {
            Query query = entityManager.createQuery("SELECT count(o) FROM OrdenTrabajo AS o");
            return query.getSingleResult().toString();
        } finally {

        }
    }

    private List<OrdenTrabajo> find(boolean all, int maxResults, int firstResult) {
        try {
            Query query = entityManager.createQuery("SELECT object(o) FROM OrdenTrabajo AS o WHERE o.estadoOt = 'CR'");
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {

        }
    }

    private List<OrdenTrabajo> findUser(boolean all, int maxResults, int firstResult, String user, Integer last) {
        try {
            Query query = entityManager.createQuery("SELECT object(o) FROM OrdenTrabajo AS o INNER JOIN o.responsable r WHERE r.usuario = :user AND o.numOrden > :last AND o.estadoOt = 'CR'");
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            query.setParameter("user", user);
            query.setParameter("last", new BigInteger(last.toString()));
            return query.getResultList();
        } finally {

        }
    }

}
