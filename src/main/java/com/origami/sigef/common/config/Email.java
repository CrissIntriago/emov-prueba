/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.origami.sigef.common.util.Variables;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Origami
 */
public class Email implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected List<File> adjuntos = new ArrayList<>();
    protected String usuarioCorreo;
    protected String contraseniaCorreo;
    protected String rutaArchivo1;
    protected String nombreArchivo1;
    protected String rutaArchivo2;
    protected String nombreArchivo2;
    protected String destinatario;
    protected String copiaOcultaBCC;
    protected String copiaCC;
    protected String asunto;
    protected String mensaje;
    protected String smtpHost;
    protected String smtpPort;
    protected String entidad;

    public Email(String destinatario, String copiaOcultaBCC,
            String copiaCC, String asunto, String mensaje,
            List<File> files, String usuarioCorreo, String contraseniaCorreo, String smtpHost,
            String smtpPort, String entidad) {
        this.destinatario = destinatario;
        this.copiaOcultaBCC = copiaOcultaBCC;
        this.copiaCC = copiaCC;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.usuarioCorreo = usuarioCorreo;
        this.contraseniaCorreo = contraseniaCorreo;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.entidad = entidad;
        if (files != null) {
            this.adjuntos = files;
        }
    }

    public Email(String destinatario, String asunto, String mensaje, List<File> files,
            String usuarioCorreo, String contraseniaCorreo, String smtpHost, String smtpPort, String entidad) {
        this(destinatario, null, null, asunto, mensaje, files, usuarioCorreo,
                contraseniaCorreo, smtpHost, smtpPort, entidad);
    }

    public boolean sendMail() {
        try {
            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", smtpHost);
            if (Boolean.valueOf(Variables.SSL)) {
                props.setProperty("mail.smtp.ssl.enable", "true");
            } else {
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.user", usuarioCorreo);
            props.setProperty("mail.smtp.auth", "true");
            System.out.println("property>>" + props.toString());
            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(usuarioCorreo, entidad));
            mimeMessage.setSubject(asunto);
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            if (copiaOcultaBCC != null) {
                mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(copiaOcultaBCC));
            }
            if (copiaCC != null) {
                mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copiaCC));
            }
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(mensaje, "text/html; charset=utf-8");
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            MimeBodyPart file;
            for (File f : adjuntos) {
                file = new MimeBodyPart();
                file.attachFile(f);
                multipart.addBodyPart(file);
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            Transport transport = session.getTransport("smtp");
            transport.connect(usuarioCorreo, contraseniaCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception e) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

}
