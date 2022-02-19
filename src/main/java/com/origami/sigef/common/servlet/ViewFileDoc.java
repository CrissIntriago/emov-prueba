/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.doc.DocumentoBlob;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "FileDoc", urlPatterns = {"/FileDoc"})
public class ViewFileDoc extends HttpServlet {

    @Inject
    private ServletSession ss;
    @Inject
    private FileUploadDoc fileService;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Map parametros;
        OutputStream outStream;
        if (ss.estaVacio()) {
            printError(response);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        parametros = ss.getParametros();
        response.setContentType((String) parametros.get("ContentType"));
        response.setCharacterEncoding("UTF-8");
        if (parametros.containsKey("download") && (Boolean) parametros.get("download")) {
            response.addHeader("Content-Disposition", "attachment; filename=" + ss.getNombreDocumento().replace(" ", "_"));
        } else {
            response.addHeader("Content-Disposition", "inline; filename=" + ss.getNombreDocumento().replace(" ", "_"));
        }
        try {
            outStream = response.getOutputStream();
            String tipo = request.getParameter("i");
            System.out.println("Parametro " + tipo);
            if ("1".equals(tipo)) {
                DocumentoBlob doc = (DocumentoBlob) ss.getParametros().get("blob");
                int size = fileService.viewDocumentoRepositorio(outStream, doc);
            } else {
                int size = fileService.viewDocumento(outStream, (Long) parametros.get("oid"));
            }
            outStream.flush();
            outStream.close();
            ss.borrarDatos();
        } catch (IOException e) {
            Logger.getLogger(ViewFileDoc.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void ConvertToPDF(String docPath, String pdfPath) {
        /*try {
            InputStream doc = new FileInputStream(new File(docPath));
            XWPFDocument document = new XWPFDocument(doc);
            PdfOptions options = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(pdfPath));
            PdfConverter.getInstance().convert(document, out, options);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ViewFileDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        try {
//            processRequest(request, response);
//        } catch (SQLException ex) {
//            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

//<editor-fold defaultstate="collapsed" desc="Method Utils">
    private void printError(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try ( PrintWriter salida = response.getWriter()) {
            salida.println("<!DOCTYPE html>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head id=\"j_id_3\">\n"
                    + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />\n"
                    + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/theme.css.xhtml?ln=primefaces-barcelona-green\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/fa/font-awesome.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/css/layout-blue.css.xhtml?ln=barcelona-layout\" />"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/jquery/jquery.js.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/core.js.xhtml?ln=primefaces&amp;v=7.0\"/>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.js.xhtml?ln=primefaces&amp;v=7.0\"/>"
                    + "<script type=\"text/javascript\">if(window.PrimeFaces){PrimeFaces.settings.locale='es_EC';PrimeFaces.settings.legacyWidgetNamespace=true;PrimeFaces.settings.projectStage='Development';}</script>\n"
                    + "<title>Archivo no encontrado</title>"
                    + "</head>"
                    + "<body class=\"exception-body\">\n"
                    + "  <div class=\"exception-panel\"><img style=\"width: 100% !important;background: lavender;\" id=\"j_id_89\" src=\"" + CONFIG.URL_APP + "/resources/barcelona-layout/images/landing/landing-main.png\" alt=\"\" />\n"
                    + "    <div class=\"line\"></div>\n"
                    + "    <h1>Archivo no encontrado</h1>\n"
                    + "    <p>Archivo no se ecuentra disponible, contacte al administrador.</p>"
                    + "    <button id=\"j_id_a9\" name=\"j_id_a9\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" "
                    + "            onclick=\"window.open('" + CONFIG.URL_APP + "/inicio','_self')\">"
                    + "     <span class=\"ui-button-text ui-c\" style=\"color: black;\">INICIO</span>"
                    + "    </button>"
                    + "    <script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.cw(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
                    + "  </div>"
                    + "</body>\n"
                    + "</html>");
        }
    }
//</editor-fold>
}
