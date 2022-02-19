/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CoaJuicio;
import com.origami.sigef.common.service.KatalinaService;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author ORIGAMIEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class CoactivaConvenioService extends BpmnBaseRoot implements Serializable {
//Finaliza Coactiva por convenio de pago

    @Inject
    private CoaJuicioService coaJuicioService;
    @Inject
    private CoaJuicioPredioServices coaJuicioPredioService;
    @Inject
    private FnConvenioPagoService convenioService;
    @Inject
    private FnConvenioPagoDetallerService convenioPagoDetalle;
    @Inject
    private CoaJuicioPredioServices coaJuicioPredioServices;
    @Inject
    private KatalinaService katalinaService;

    private List<FnConvenioPago> convenios;
    private List<CoaJuicio> juiciosConvenio;
    private List<FnConvenioPagoDetalle> detalleConvenio;

//    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "*", dayOfMonth = "*",
//            year = "*", minute = "*", second = "30", persistent = false)
//    @AccessTimeout(value = -1)
    public void finalizarProcesoCoactivoConvenioPagado() {
        try {
            Double totalCuotas = 0.00;
            Double totalJuicioCandelado = 0.00;
            convenios = convenioService.findAll();
            for (FnConvenioPago c : convenios) {
                detalleConvenio = new ArrayList<>();
                juiciosConvenio = (List<CoaJuicio>) coaJuicioPredioService.findByPredio(c.getPredio());
                //coutas del convenio pagadas
                detalleConvenio = convenioPagoDetalle.findByDetallePagoPagado(c);
                //sumamos las cuotas pagadas
                for (FnConvenioPagoDetalle conv : detalleConvenio) {
                    totalCuotas = totalCuotas + conv.getDeuda().doubleValue();
                }
                for (CoaJuicio j : juiciosConvenio) {
                    if (j.getPagadoPorConvenio() == false || j.getPagadoPorConvenio() == null) {
                        if (totalCuotas - totalJuicioCandelado >= j.getTotalDeuda().doubleValue()) {
                            j.setEstadoJuicio(coaJuicioPredioServices.findByEstado("ED"));
                            j.setPagadoPorConvenio(Boolean.TRUE);
                            coaJuicioService.edit(j);
                            enviarCorreo(j);
                        }
                    } else {
                        totalJuicioCandelado = totalJuicioCandelado + j.getTotalDeuda().doubleValue();
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarCorreo(CoaJuicio juicio) {
        try {
            Correo c = new Correo();
            c.setDestinatario("david271998@gmail.com");
            c.setAsunto("CITACION PARA COACTIVA");
            c.setMensaje("<html lang=\"es\">\n"
                    + "<head>\n"
                    + "<meta charset=\"utf-8\"/>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<p style=\"width:200px;\">SR(a). _____________________________________ POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE SE LE ACABA DE APLICAR UN JUICIO DE COACTIVA POR FAVOR CONTINUAR CON EL TRAMITE DESDE EL SISTEMA ORIGAMIGT\n"
                    + " SEGÚN EL NUMERO DE JUICIO N°" + juicio.getNumeroJuicio() + " </p>"
                    + "</body>\n"
                    + "</html>");
            System.out.println("mensaje>>>" + c.getMensaje());
            katalinaService.enviarCorreo(c);
            coaJuicioService.edit(juicio);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "ERROR ENVIAR CORREO>>>>", ex);
        }
    }
}
