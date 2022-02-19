/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author ANGEL NAVARRO
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private static final Logger LOG = Logger.getLogger(CustomExceptionHandler.class.getName());

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable throwable = context.getException();
            FacesContext fc = FacesContext.getCurrentInstance();
            try {
                ExternalContext ext = fc.getExternalContext();
                Map<String, String> requestHeader = ext.getRequestHeaderMap();
                Map<String, Object> requestMap = ext.getRequestMap();
                if (requestHeader != null) {
                    requestHeader.put("messageErrorApp", throwable.toString());
                    requestMap.put("messageErrorAppms", throwable.getMessage());
                    requestMap.put("messageErrorApp", throwable.toString());
                    String id = context.getComponent() == null ? "" : context.getComponent().getFamily() + ":id " + context.getComponent().getId();
                    String urlRefered = requestHeader.get("referer");
                    if (Objects.nonNull(urlRefered)) {
                        LOG.log(Level.WARNING, "Url: " + urlRefered + " componente " + id + " Error >>" + throwable.getMessage(), throwable);
                    } else {
                        LOG.log(Level.SEVERE, "urlRefered null >> " + throwable.getMessage() + " - " + id, throwable);
                    }
                } else {
                    LOG.log(Level.SEVERE, "Error >> " + throwable.getMessage(), throwable);
                }
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                nav.handleNavigation(fc, null, "/error");
                fc.renderResponse();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "handle >> " + throwable.getMessage(), throwable);
            } finally {
                iterator.remove();
            }
        }
        // Let the parent handle the rest
        getWrapped().handle();
    }
}
