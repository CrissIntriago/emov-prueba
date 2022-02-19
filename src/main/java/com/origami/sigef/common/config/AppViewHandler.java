/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import com.origami.sigef.common.entities.MenuRol;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.barcelona.domain.Document;

/**
 *
 * @author ANGEL NAVARRO
 */
public class AppViewHandler extends ViewHandlerWrapper {

    private final ViewHandler wrapped;
    private UserSession userSession;
    private MenuRol menuRol;
    private List<Document> permisos;

    public AppViewHandler(ViewHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot view) {
        try {
            if (context.isPostback()) {
//                super.renderView(context, view);
                if (userSession == null) {
                    super.renderView(context, view);
                } else {
                    processView(context, view);
                }
            } else if (context.getPartialViewContext().isAjaxRequest()) {
                super.renderView(context, view);
            } else {
                if (userSession == null) {
                    userSession = (UserSession) JsfUtil.getSessionBean("userSession");
                }
                if (userSession == null) {
                    super.renderView(context, view);
                } else {
                    if (userSession.hasRole("admin")) {
                        super.renderView(context, view);
                    } else {
                        processView(context, view);
                    }
                }
            }
        } catch (IOException | FacesException ex) {
            Logger.getLogger(AppViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }

    private List<String> processViewTree(UIComponent component, String url) {
        try {
            if (Utils.isNotEmpty(permisos) && Utils.isNotEmpty(component.getChildren())) {
                for (UIComponent child : component.getChildren()) {
//                    System.out.println("Famili " + child.getFamily());
                    for (Document permiso : permisos) {
                        switch (permiso.getType()) {
                            case "id":
                                if (child.getId().equals(permiso.getName())) {
                                    renderedComponent(child, permiso.getEnable(), null);
                                }
                                break;
                            case "class": {
                                String get = (String) child.getAttributes().get("styleClass");
                                if (get != null) {
                                    if (get.contains(permiso.getName().trim())) {
                                        renderedComponent(child, permiso.getEnable(), null);
                                    }
                                }
                                break;
                            }
                            case "value": {
                                if (child.getAttributes().containsKey("value")) {
                                    Object geto = child.getAttributes().get("value");
                                    if (geto != null) {
                                        if (geto instanceof String) {
                                            String get = (String) geto;
                                            if (get.contains(permiso.getName())) {
                                                renderedComponent(child, permiso.getEnable(), null);
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    if (child.getChildCount() > 0) {
                        processViewTree(child, url);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            Logger.getLogger(AppViewHandler.class.getName()).log(Level.SEVERE, "Load url" + url, e);
        }
        return null;
    }

    private void renderedComponent(UIComponent child, boolean rendered, List<String> componentIds) {
        child.setRendered(rendered);
    }

    private void processView(FacesContext context, UIViewRoot view) throws IOException {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String requestURI = req.getRequestURI().replace(req.getContextPath() + "/", "");
        menuRol = Utils.clone(userSession.getMenuRol(requestURI));
        if (menuRol != null) {
//                    Pasamos el parametro para el titulo de la paguina
            JsfUtil.setSessionBean("titlePage", menuRol.getMenu().getDescripcion());
            permisos = Utils.clone(menuRol.getMenu().getPermisos());
            if (Utils.isNotEmpty(menuRol.getPermisos())) {
                for (Document p : permisos) {
                    if (!menuRol.getPermisos().contains(p)) {
                        p.setEnable(Boolean.FALSE);
                    }
                }
            }
        }
        processViewTree(view, requestURI);
        // Finally call super so JSF can do the rendering job.
        super.renderView(context, view);
    }
}
