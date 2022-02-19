/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.primefaces.webapp.MultipartRequest;
import org.primefaces.webapp.filter.FileUploadFilter;

/**
 *
 * @author ANGEL NAVARRO
 */
public class FileUploadPrimefaces extends FileUploadFilter {

    private static final Logger LOG = Logger.getLogger(FileUploadPrimefaces.class.getName());
    private String uploadDir;
    private static final String UPLOAD_DIRECTORY_PARAM = "uploadDirectory";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        uploadDir = filterConfig.getInitParameter(UPLOAD_DIRECTORY_PARAM);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("FileUploadFilter initiated successfully");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
        System.out.println("File ... isMultipart " + isMultipart);
        if (isMultipart) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Parsing file upload request");
            }
            ServletFileUpload servletFileUpload = new ServletFileUpload(createFileItemFactory(httpServletRequest));
            MultipartRequest multipartRequest = new MultipartRequest(httpServletRequest, servletFileUpload);

            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("File upload request parsed succesfully, continuing with filter chain with a wrapped multipart request");
            }

            filterChain.doFilter(multipartRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected FileItemFactory createFileItemFactory(HttpServletRequest httpServletRequest) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        if (uploadDir != null) {
            diskFileItemFactory.setRepository(new File(uploadDir));
        }
//        System.out.println("// Directory " + uploadDir);
        FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(httpServletRequest.getSession().getServletContext());
        if (fileCleaningTracker != null) {
            diskFileItemFactory.setFileCleaningTracker(fileCleaningTracker);
        }
        return diskFileItemFactory;
    }

}
