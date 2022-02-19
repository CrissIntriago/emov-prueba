/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.util.MimeTypeUtils;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Boletas", urlPatterns = {"/Boletas"})
public class Boletas extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Inject
    private ServletSession ss;
    @Inject
    private ValoresService valoresService;
    @Inject
    private EntityManagerService service;
    private Boolean localSave = false, download = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Boolean mostrarFechaImpr = Boolean.valueOf(valoresService.findByCodigo("MOSTRAR_FECHA_IMPRESION"));
        String filename = null;
        Map parametros;
        JasperPrint jasperPrint=null;
        OutputStream outStream;
        Connection conn = null;
        try {
            response.addHeader("secure-av", "Secure");
            response.addHeader("httponly-av", "HttpOnly");
            response.addHeader("X-UA-Compatible", "IE=edge,chrome=1");
            request.setCharacterEncoding("UTF-8");
            parametros = ss.getParametros();
            if (parametros == null) {
                parametros = new HashMap();
            }
            filename = ss.getNombreDocumento().concat(".").concat(ss.getContentType());
            filename = filename.replace(" ", "_");
//        System.out.println("parametros " + parametros);
            if (ss.getContentType().equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf; filename=" + filename + "; filename*= UTF-8''" + filename + "");
//                System.out.println("*********************************************AQUI ENTRO*********************************************");
            } else if (ss.getContentType().equalsIgnoreCase("xlsx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("docx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document; name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("png")) {
                response.setContentType(MimeTypeUtils.IMAGE_PNG_VALUE + ";name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("jpg")) {
                response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE + ";name=" + filename);
            } else {
                response.setContentType(MimeTypeUtils.ALL_VALUE + ";name=" + filename);
            }
            localSave = Boolean.valueOf(parametros.get("localSave") + "");
            download = Boolean.valueOf(parametros.get("downland") + "");
            if (localSave || download) {
                response.addHeader("Content-Disposition", "attachment; name=title; filename=" + filename + "; file-download=" + filename);
            } else {
                response.addHeader("Content-Disposition", "inline; filename=" + filename + "; file-download=" + filename + "; filename*=UTF-8''" + filename);
            }
            //aqui ingreso las hojas adicionales 
            System.out.println("ESTO SALE "+ss.getAgregarReporte());
            System.out.println("LOS REPORTES "+ss.getReportes());
            if (ss.getAgregarReporte()) {
                for (Map<String, Object> reporte : ss.getReportes()) {
                    String ruta = getServletContext().getRealPath(rutaReporte((String) reporte.get("nombreReporte"), (String) reporte.get("nombreSubCarpeta")));
                    JasperPrint jasperPrint2 = JasperFillManager.fillReport(ruta, reporte, conn);
                    if (jasperPrint2.getPages() != null && jasperPrint2.getPages().size() > 0) {
                        jasperPrint.addPage(jasperPrint2.getPages().get(0));
                    }
                }
            }

        } catch (Exception ex) {
            if (conn != null) {
                conn.close();
            }
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, "Error el ejecutar reporte " + ss.getNombreReporte(), ex);
            printError(response, ex);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void encuadernar(JasperPrint jasperPrint, Integer margen) {
        if (margen == null) {
            margen = 30;
        }
        List<JRPrintPage> pages = jasperPrint.getPages();
        JRPrintPage page;
        List<JRPrintElement> elements;
        for (int i = 1; i < pages.size() + 1; i++) {
            page = (JRPrintPage) pages.get(i - 1);
            elements = page.getElements();
            if (i % 2 != 0) {//IMPAR
                for (JRPrintElement temp : elements) {
                    temp.setX(temp.getX() + margen);
                }
            } else {//PAR
                for (JRPrintElement temp : elements) {
                    temp.setX(temp.getX() - margen);
                }
            }
        }
    }

    private String rutaReporte(String nombreReporte, String nombreSubCarpeta) {
        if (nombreSubCarpeta == null) {
            return "//reportes//" + nombreReporte + ".jasper";
        } else {
            return "//reportes//" + nombreSubCarpeta + "//" + nombreReporte + ".jasper";
        }
    }

    private void printError(HttpServletResponse response, Exception ex) throws IOException {
        response.setContentType("text/html");
        try ( PrintWriter salida = response.getWriter()) {
            salida.println("<!DOCTYPE html>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:p=\"http://primefaces.org/ui\" >"
                    + "<head id=\"j_id_3\">\n"
                    + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />\n"
                    + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/theme.css.xhtml?ln=primefaces-barcelona-green\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/fa/font-awesome.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/css/layout-blue.css.xhtml?ln=barcelona-layout\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/css/layout-blue.css.xhtml?ln=barcelona-layout\" />"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/jquery/jquery.js.xhtml?ln=primefaces&amp;v=7.0\"></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/jquery/jquery-plugins.js.xhtml?ln=primefaces&v=7.0\" ></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/core.js.xhtml?ln=primefaces&amp;v=7.0\" ></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.js.xhtml?ln=primefaces&amp;v=7.0\" ></script>"
                    + "<title>Documento</title>"
                    + "</head>"
                    + "<body class=\"exception-body\">\n"
                    + "  <div class=\"exception-panel\"><img style=\"width: 100% !important;background: lavender; padding: 20px;\" id=\"j_id_89\" src=\"" + CONFIG.URL_APP + "/resources/barcelona-layout/images/landing/landing-main.png\" alt=\"\" />\n"
                    + "    <div class=\"line\"></div>\n"
                    + (ex == null ? "    <h1>Documento no encontrado</h1>\n" : (ex + "\n"))
                    + "    <p>Documento no se encuentra disponible, contacte al administrador.</p>"
                    //                    + "<p:commandLink value=\"INICIO\" onclick=\"window.open('" + CONFIG.URL_APP + "/inicio','_self')\" /> "
                    + "    <button id=\"j_id_a9\" name=\"j_id_a9\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" "
                    + "            onclick=\"window.open('" + CONFIG.URL_APP + "/inicio','_self')\">"
                    + "     <span class=\"ui-button-text ui-c\" style=\"color: black;\">INICIO</span>"
                    + "    </button> <br/><br/><br/><br/>"
                    //                                        + "    <script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.ab(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
                    + "  </div>"
                    + "</body>\n"
                    + "</html>");
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Boletas.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Boletas.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
