/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "ViewSystemDocs", urlPatterns = {"/ViewSystemDocs"})
public class ViewSystemDocs extends HttpServlet {
    
    private static final Logger LOG = Logger.getLogger(ViewSystemDocs.class.getName());
    
    @Inject
    private ServletSession ss;
    private boolean verbose = false;
    private boolean downland = false;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException Exception
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Map parametros;
        OutputStream os;
        try {
            parametros = ss.getParametros();
            if (parametros == null) {
                parametros = new HashMap();
            }
            if (ss.getNombreDocumento() != null) {
                ss.setNombreDocumento(ss.getNombreDocumento().replaceAll("(\\\\)|(\\\\)", "/"));
                File file = new File(ss.getNombreDocumento());
                if (file.exists()) {
//                    System.out.println("Documento: " + ss.getNombreDocumento() + " existe " + file.exists());
                    String fileName = file.getName();
                    byte[] bytes;
                    try ( FileInputStream fis = new FileInputStream(file)) {
                        bytes = new byte[fis.available()];
                        fis.read(bytes);
                    }
                    if (ss.getContentType() == null) {
                        response.setContentType("application/pdf");
                    } else {
                        response.setContentType(ss.getContentType());
                    }
                    response.setCharacterEncoding("UTF-8");
                    //response.setContentType("application/octet-stream");
//                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                    downland = Boolean.valueOf(parametros.get("download") + "");
//                    System.out.println("descargar>>" + downland);
                    if (downland) {
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; file-download=\"" + fileName + "\"");
                    } else {
                        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName);
                    }
                    System.out.println("Documento: " + ss.getNombreDocumento() + " existe " + file.exists());
                    System.out.println("response.getContentType()>>" + response.getContentType());
                    response.setContentLength(bytes.length);
                    os = response.getOutputStream();
                    os.write(bytes, 0, bytes.length);
                    os.flush();
                    os.close();
                } else {
                    System.out.println("Documento: " + ss.getNombreDocumento() + " existe " + file.exists());
                }
            }
            ss.borrarDatos();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        
    }
    
    protected void processRequestMultiFile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        ServletOutputStream out = null;
        try {
            if (ss.getReportes() == null) {
                this.printError(response, null, "No hay archivos para descargar.");
                return;
            }
            response.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding("UTF-8");
            String contentType = "Content-type: text/rtf";
            File file = null;
            verbose = (boolean) ss.getValor("verbose");
            response.setContentType("multipart/x-mixed-replace;boundary=END");
            out = response.getOutputStream();
            out.println();
            out.println("--END");
            for (Map<String, Object> ff : ss.getReportes()) {
                for (Map.Entry<String, Object> entry : ff.entrySet()) {
                    file = new File(entry.getKey());
                    contentType = "contentType: " + entry.getValue();
                }
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException fnfe) {
                    System.out.println("Could not find file " + file.getAbsolutePath());
                    continue;
                }
                try ( BufferedInputStream fif = new BufferedInputStream(fis)) {
                    out.println(contentType);
                    out.println("Content-Disposition: attachment; filename=\"" + file.getName() + "\"");
                    out.println();
                    if (verbose) {
                        System.out.println("Sending file " + file.getName() + " type " + contentType);
                    }
                    int data = 0;
                    while ((data = fif.read()) != -1) {
                        out.write(data);
                    }
                }
                out.println();
                out.println("--END");
                out.flush();
                if (verbose) {
                    System.out.println("Finished sending file " + file.getName());
                }
            }
            out.println("--END--");
            ss.setReportes(null);
        } catch (IOException e) {
            Logger.getLogger(ViewSystemDocs.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
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
            if (ss.getReportes() != null) {
                processRequestMultiFile(request, response);
            } else {
                processRequest(request, response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewSystemDocs.class.getName()).log(Level.SEVERE, null, ex);
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
//            Logger.getLogger(ViewSystemDocs.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "ViewSystemDocs descarga los archivos de una ruta del servidor";
    }// </editor-fold>

    private void printError(HttpServletResponse response, Exception ex, String body) throws IOException {
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
                    + "<title>Documento</title>"
                    + "</head>"
                    + "<body class=\"exception-body\">\n"
                    + "  <div class=\"exception-panel\"><img style=\"width: 100% !important;background: lavender;\" id=\"j_id_89\" src=\"" + CONFIG.URL_APP + "/resources/barcelona-layout/images/landing/landing-main.png\" alt=\"\" />\n"
                    + "    <div class=\"line\"></div>\n"
                    + (ex == null ? "    <h1>Documento no encontrado</h1>\n" : (ex + "\n"))
                    + "    <p>" + body + ".</p>"
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
}
