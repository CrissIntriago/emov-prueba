/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api.service;

import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.origami.sigef.ws.models.RespuestaConsulta;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Administrator
 */
@Produces({"application/Json", "text/xml"})
@Path("/transacciones/banca")
public class TransaccionesBanca {

    @Inject
    private RecaudacionInteface rs;
    @Inject
    private FinaRenLiquidacionService frls;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/facilito/auxiliar/{identificacion}")
    public String facilitoConsultaAuxiliar(@PathParam("identificacion") String identificacion) {
        List<RespuestaConsulta> response;
        try {
            System.out.println("// identificacion: " + identificacion);
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/facilito/consulta/{tipo}/{referencia}")
    public String facilitoConsultaDeuda(@PathParam("tipo") String tipo, @PathParam("referencia") String referencia) {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String facilitoPago(String identificacion) {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String facilitoReverso(String identificacion) {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String facilitoConciliacion(String identificacion) {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String produbancoConsulta(String identificacion) {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String produbancoPago(String identificacion) {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String produbancoReverso(String identificacion) {
        return null;
    }

}
