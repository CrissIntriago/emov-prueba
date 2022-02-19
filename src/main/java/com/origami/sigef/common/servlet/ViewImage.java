/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Fernando, Carlos Loor Vargas
 */
@WebServlet(name = "ViewDocs", urlPatterns = {"/ViewDocs"})
public class ViewImage extends HttpServlet {

    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

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
        if (ss.getTempData() == null) {
            System.out.println("No datos para mostrar.");
            return;
        }
        OutputStream outStream;
        request.setCharacterEncoding("UTF-8");
        response.setContentType(ss.getContentType());
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "inline; filename=" + ss.getNombreDocumento());
        InputStream input = (InputStream) ss.getTempData();
        try {
            outStream = response.getOutputStream();
            IOUtils.copy(input, outStream, 1024 * 10);
            outStream.flush();
            outStream.close();
            ss.borrarDatos();
        } catch (IOException e) {
            Logger.getLogger(ViewFileDoc.class.getName()).log(Level.SEVERE, null, e);
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
        /**
         * Detectar si es un id long o UUID
         */
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            return;
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
