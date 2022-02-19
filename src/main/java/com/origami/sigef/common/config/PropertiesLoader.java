/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.Variables;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import jodd.props.Props;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class PropertiesLoader {

    protected ServletContext sc;

    public void load() {
        Props props1 = new Props();
        try {
            props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props")); //DESARROLLO OFICINA
//            props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props.produccion")); //PRODUCCION
//            props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props.preproduccion")); //PREPRODUCCION
//            props1.load(sc.getResourceAsStream("/WEB-INF/sistemaC.props")); //HOME

            //SISTEMA
            SisVars.RUTA_DOCUMENTOS = props1.getValue("sistema.RUTA_DOCUMENTOS");
            FilesUtil.createDirectory(SisVars.RUTA_DOCUMENTOS);
            SisVars.RUTA_IMAGES = props1.getValue("sistema.RUTA_IMAGES");
            FilesUtil.createDirectory(SisVars.RUTA_IMAGES);
            SisVars.RUTA_IMAGES_QR = props1.getValue("sistema.RUTA_IMAGES_QR");
            FilesUtil.createDirectory(SisVars.RUTA_IMAGES_QR);
            SisVars.appQrService = props1.getValue("sistema.appQrService");
//            SisVars.appQrService = props1.getValue("sistema.appQrService");
            SisVars.appAtlantisUrl = props1.getValue("sistema.appAtlantisUrl");
            SisVars.wsEmail = props1.getValue("sistema.wsEmail");
            SisVars.wsFirmaEC = props1.getValue("sistema.wsFirmaEC") + "/duran/api/";
            SisVars.wsMedia = (System.getenv("ws") != null ? System.getenv("ws") : props1.getValue("sistema.ws")) + "/ws-media/duran/api/";
            SisVars.rutaFirmasElectronicas = props1.getValue("sistema.rutaFirmasElectronicas");
            FilesUtil.createDirectory(SisVars.rutaFirmasElectronicas);
            SisVars.RUTA_IMAGES_TEMP = props1.getValue("sistema.RUTA_IMAGES_TEMP");
            FilesUtil.createDirectory(SisVars.RUTA_IMAGES_TEMP);
            SisVars.RUTA_FILES_TEMP = props1.getValue("sistema.RUTA_FILES_TEMP");
            FilesUtil.createDirectory(SisVars.RUTA_FILES_TEMP);
            SisVars.RUTA_FILES_PRESUPUESTO = props1.getValue("sistema.RUTA_FILES_PRESUPUESTO");
            FilesUtil.createDirectory(SisVars.RUTA_FILES_PRESUPUESTO);
            SisVars.RUTA_FILES_CERTIFICACIONPAPP = props1.getValue("sistema.RUTA_FILES_CERTIFICACIONPAPP");
            FilesUtil.createDirectory(SisVars.RUTA_FILES_CERTIFICACIONPAPP);
            SisVars.IMPRIMIR_ETIQUETAS = props1.getBooleanValue("sistema.IMPRIMIR_ETIQUETAS");
            SisVars.RUTA_GESTION_TRIBUTARIA = props1.getValue("sistema.RUTA_GESTION_TRIBU");
            FilesUtil.createDirectory(SisVars.RUTA_GESTION_TRIBUTARIA);
            SisVars.RUTA_EVIDENCIA = props1.getValue("sistema.RUTA_DOCU_EVIDENCIA");
            SisVars.RUTA_FORMATS_REGISTROS_CONTABLES = props1.getValue("sistema.RUTA_FORMATS_REGISTROS_CONTABLES");
            FilesUtil.createDirectory(SisVars.RUTA_FORMATS_REGISTROS_CONTABLES);
            SisVars.RUTA_DOCUMENTOS_VENTANILLA = props1.getValue("sistema.RUTA_DOCUMENTOS_VENTANILLA");
            FilesUtil.createDirectory(SisVars.RUTA_DOCUMENTOS_VENTANILLA);
            SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS = props1.getValue("sistema.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS");
            FilesUtil.createDirectory(SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS);
            SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS = props1.getValue("sistema.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS");
            FilesUtil.createDirectory(SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS);
            SisVars.RUTA_REPOSITORIO = props1.getValue("sistema.RUTA_REPOSITORIO");
            FilesUtil.createDirectory(SisVars.RUTA_REPOSITORIO);
            SisVars.RUTA_CERTIFICADOS = props1.getValue("sistema.RUTA_CERTIFICADO");
            FilesUtil.createDirectory(SisVars.RUTA_CERTIFICADOS);
            //(OJO) para envio de correo solo para comprobantes Electronios
            //Variables.
            Variables.CORREO_ELECTRONICO = props1.getValue("mail.correo");
            Variables.CONTRASENIA = props1.getValue("mail.pass");
            Variables.SMTP_HOST = props1.getValue("mail.smtpHost");
            Variables.SMTP_PORT = props1.getValue("mail.smtpPort");
            Variables.ENTIDAD = props1.getValue("mail.entidad");
            Variables.SSL = props1.getValue("mail.ssl"); 
        } catch (Exception e) {
            Logger.getLogger(PropertiesLoader.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public PropertiesLoader(ServletContext sc) {
        this.sc = sc;
    }

}
