/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.MsgFormatoNotificacion;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.models.CorreoArchivo;
import com.origami.sigef.common.service.interfaces.AsynchronousService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author gutya
 */
//@Stateless @javax.enterprise.context.Dependent(name = "asincrono")
@Stateless
@javax.enterprise.context.Dependent
public class AsynchronousEjb implements AsynchronousService {
    
    @Inject
    private ValoresService valoresService;
    @Inject
    private KatalinaService katalinaService;
    
    @Override
    @Asynchronous
    public void sendMailFactura(MsgFormatoNotificacion msgFormatoNotificacion, String pathXML,
            String pathPDF, String correoContribuyente, Long idOrdenCobro) {
        try {
            Correo correo = new Correo();
            List<CorreoArchivo> archivos = new ArrayList<>();
            CorreoArchivo xml = new CorreoArchivo();
            xml.setNombreArchivo(pathXML);
            xml.setTipoArchivo("xml");
            CorreoArchivo ride = new CorreoArchivo();
            ride.setNombreArchivo(pathPDF);
            ride.setTipoArchivo("pdf");
            archivos.add(ride);
            archivos.add(xml);
            correo.setDestinatario(correoContribuyente);
            correo.setAsunto(msgFormatoNotificacion.getHeader());
            correo.setMensaje(msgFormatoNotificacion.getFooter());
            correo.setArchivos(archivos);
            katalinaService.enviarCorreo(correo);
//        List<File> files;
//        File xml = new File(pathXML);
//        File ride = new File(pathPDF);
//        if (xml.exists() && ride.exists()) {
//            files = new ArrayList<>();
//            files.add(xml);
//            files.add(ride);
//
//            Email correo = new Email(correoContribuyente,
//                    msgFormatoNotificacion.getHeader(),
//                    msgFormatoNotificacion.getFooter(), files,
//                    Variables.CORREO_ELECTRONICO,
//                    Variables.CONTRASENIA,
//                    Variables.SMTP_HOST,
//                    Variables.SMTP_PORT,
//                    Variables.ENTIDAD
//            );
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            Future<Boolean> future = executorService.submit(() -> {
//                return correo.sendMail();
//            });
//            try {
//                //SI SE ENVIA EL CORREO SE ACTUALIZA EL VALOR
//                if (future.get()) {
//
//                }
        } catch (Exception ex) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

//    @Override
//    public void sendMailOrdenPago(String mensaje, String pathPDF, String destinatario) {
//        List<File> files;
//        File ride = new File(pathPDF);
//        if (ride.exists()) {
//            files = new ArrayList<>();
//            files.add(ride);
//            Email correo = new Email(destinatario, "ORDEN DE PAGO", mensaje, files,
//                    valoresService.findByCodigo(Variables.CORREO_ELECTRONICO),
//                    valoresService.findByCodigo(Variables.CONTRASENIA),
//                    valoresService.findByCodigo(Variables.SMTP_HOST),
//                    valoresService.findByCodigo(Variables.SMTP_PORT),
//                    valoresService.findByCodigo(Variables.ENTIDAD)
//            );
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            Future<Boolean> future = executorService.submit(() -> {
//                return correo.sendMail();
//            });
//            try {
//                //SI SE ENVIA EL CORREO SE ACTUALIZA EL VALOR
//                if (future.get()) {
//
//                }
//            } catch (InterruptedException | ExecutionException ex) {
//                Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}
