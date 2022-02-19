/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.origami.sigef.auth.services.CacheApp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewExpiredException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.util.Constants;

/**
 *
 * @author ANGEL NAVARRO
 */
@WebFilter(filterName = "AppUrlAuthorizationFilter")
public class AppUrlAuthorizationFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AppUrlAuthorizationFilter.class.getName());

    @Inject
    private CacheApp cacheApp;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding("UTF-8");
        // Authorize (allow) all domains to consume the content
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, UPDATE, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        response.addHeader("X-UA-Compatible", "IE=edge,chrome=1");
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        if (!request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
        }
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
        try {
            Subject subject = SecurityUtils.getSubject();
            String reqURI = request.getRequestURI();
//            System.out.println("//reqURI " + reqURI);
            if (reqURI.contains("javax.faces.resource") || reqURI.contains("resource") || reqURI.contains("/api/") 
                    || reqURI.contains("/Documento") || reqURI.contains("/images") || reqURI.contains("/TransaccionesBDP") 
                    || reqURI.contains("/WSProdubanco")) {
                chain.doFilter(request, response);
                return;
            }
            RequestDispatcher dispatcher = wrapper.getRequestDispatcher("/login");
            if (!subject.isAuthenticated()) {
                if (reqURI.contains("/recuperar/contrasenia")) {
                    dispatcher = wrapper.getRequestDispatcher(reqURI);
                }
                try {
                    dispatcher.forward(request, response);
                } catch (ViewExpiredException x) {
                    System.out.println("Ha expirado la vista " + reqURI);
                }
                return;
            }
            reqURI = reqURI.substring(request.getContextPath().length());
            // Chequeamos que si es dialog framework lo dejamos pasar
            String pfdlgcid = request.getParameter(Constants.DIALOG_FRAMEWORK.CONVERSATION_PARAM);
            if (pfdlgcid != null) {
                chain.doFilter(request, response);
                return;
            }
//            System.out.println("User login is admin " + subject.hasRole("admin") + " reqURI " + reqURI);
            if (reqURI.contains("//")) {
                reqURI = reqURI.replaceAll("//", "/");
            }
            if (reqURI.equals("/")) {
                reqURI += "procesos/bandeja-tareas";
            }
            if (subject.hasRole("admin")) {
                try {
                    dispatcher = wrapper.getRequestDispatcher(reqURI);
                    dispatcher.forward(request, response);
                } catch (IOException | ServletException iOException) {
                    LOG.log(Level.SEVERE, reqURI, iOException);
                }
            } else {
                int hasUrlRoles = cacheApp.hasUrlRole(reqURI);
//                System.out.println("Url " + reqURI + " econtrado " + hasUrlRoles);
                switch (hasUrlRoles) {
                    case 0:
                        dispatcher = wrapper.getRequestDispatcher(reqURI.trim());
                        dispatcher.forward(request, response);
                        break;
                    case 2:
                        System.out.println("///Codigo 2 no encontrado '" + reqURI + "'");
                        dispatcher = wrapper.getRequestDispatcher(reqURI);
                        response.addHeader("messageErrorAppms", reqURI);
                        dispatcher.forward(request, response);
                        break;
                    case 3:
                        System.out.println("///Codigo 3 '" + reqURI + "'");
                        dispatcher = wrapper.getRequestDispatcher("/error/page");
                        dispatcher.forward(request, response);
                        break;
                    default:
                        System.out.println("///Codigo 1 Sin Acceso  '" + reqURI + "'");
                        dispatcher = wrapper.getRequestDispatcher("/access-denied");
                        dispatcher.forward(request, response);
                        break;
                }
            }
        } catch (IOException | ServletException iOException) {
            LOG.log(Level.SEVERE, null, iOException);
            RequestDispatcher dispatcher = wrapper.getRequestDispatcher("/error/page");
            response.addHeader("messageErrorAppms", iOException.getMessage());
            dispatcher.forward(request, response);
        }
    }

}
