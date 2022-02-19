/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api;

import com.google.gson.Gson;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteSRIService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.ComprobanteElectronicaService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteSRI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 *
 * @author gutya
 */
@Path(value = "/comprobanteElectronico/")
@Produces({"application/Json", "text/xml"})
@ApplicationScope
public class FacturacionElectronicaRest {

    private static final Logger LOG = Logger.getLogger(FacturacionElectronicaRest.class.getName());
    
    @Inject
    private ComprobanteElectronicaService comprobanteElectronicaService;
    @Inject 
    private ComprobanteSRIService comprobanteSRIService;

    @POST
    @Path(value = "facturaElectronica")
    @Consumes(MediaType.APPLICATION_JSON)
    public ComprobanteSRI iniciarEnvioFacturaElectronica(String comprobante) {
        ComprobanteElectronico comprobanteElectronico;
        try {
            Gson gson = new Gson();
            comprobanteElectronico = gson.fromJson(comprobante, ComprobanteElectronico.class);
            comprobanteElectronicaService.enviarFacturaElectronicaSRI(comprobanteElectronico);
            ComprobanteSRI comprobanteSRI = comprobanteSRIService.getComprobanteSRI(comprobanteElectronico.getIdLiquidacion());
            return gson.fromJson(comprobanteSRI.getResponse(), ComprobanteSRI.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Path(value = "batchFacturaElectronica")
    @Consumes(MediaType.APPLICATION_JSON)
    public void iniciarEnvioFacturaElectronicaList(String comprobante) {
//        System.out.println("comprobante: " + comprobante);
        Gson gson = new Gson();
        List<ComprobanteElectronico> comprobanteElectronico = new ArrayList<>(Arrays.asList(gson.fromJson(comprobante, ComprobanteElectronico[].class)));
        try {
            for (ComprobanteElectronico c : comprobanteElectronico) {
                comprobanteElectronicaService.enviarFacturaElectronicaSRI(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
