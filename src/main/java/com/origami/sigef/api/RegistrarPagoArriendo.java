/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api;

import com.google.gson.Gson;
import com.origami.sigef.arrendamiento.entities.OrdenesEmitidas;
import com.origami.sigef.arrendamiento.service.OrdenesEmitidasService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.InfoAdicional;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.PagoArriendo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
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
 * @author ANGEL NAVARRO
 */
@Path(value = "/arriendo/process/")
@Produces({"application/Json", "text/xml"})
@ApplicationScope
public class RegistrarPagoArriendo {

    private static final Logger LOG = Logger.getLogger(RegistrarPagoArriendo.class.getName());
    @Inject
    private OrdenesEmitidasService ordenesEmitidasService;

    @POST
    @Path(value = "registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    public InfoAdicional iniciarEnvioFacturaElectronica(String pagoData) {
        System.out.println("comprobante: " + pagoData);
        PagoArriendo pago;
        InfoAdicional respuesta = new InfoAdicional();
        try {
            Gson gson = new Gson();
            pago = gson.fromJson(pagoData, PagoArriendo.class);
            OrdenesEmitidas ordenesEmitidas = ordenesEmitidasService.findByNamedQuery1("OrdenesEmitidas.findByIdSolicitud", pago.getIdSolicitud());
            if (ordenesEmitidas != null) {
                ordenesEmitidas.setOrdenPagada(pago.getPagado());
//                ordenesEmitidas.setJsPago(pagoData);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(pago.getFechaPago());
//                ordenesEmitidas.setFechaPago(date);
                if (pago.getPagado()) {
                    ordenesEmitidas.setMontoPagado(ordenesEmitidas.getMontoPagar());
                } else {
                    ordenesEmitidas.setMontoPagado(ordenesEmitidas.getMontoPagado().add(pago.getValorPagado()));
                }
                ordenesEmitidasService.edit(ordenesEmitidas);
                respuesta.setValor("1");
                respuesta.setNombre("Pago registrado con exito.");
            } else {
                respuesta.setValor("0");
                respuesta.setNombre("No se encontro el número de orden de pago.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, pagoData, e);
            respuesta.setValor("0");
            respuesta.setNombre("Ocurrio un error al procesar la orden de pago.");
        }
        return respuesta;
    }

}
