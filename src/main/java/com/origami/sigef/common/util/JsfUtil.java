package com.origami.sigef.common.util;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities) {
        int size = entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(final String title, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(final String title, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, title, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addInformationMessage(String summary, String details) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, details);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addWarningMessage(String summary, String details) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static String getRequestParameter(final String key) {
        return FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get(key);
    }

    @SuppressWarnings("rawtypes")
    public static Object getObjectFromRequestParameter(final String requestParameterName, final Converter converter, final UIComponent component) {
        final String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static Boolean isAjaxRequest() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.isPostback();
    }

    public static String getHostContextUrl() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        return url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
    }

    public static Object getSessionBean(String sesionName) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get(sesionName);
    }

    public static Object setSessionBean(String sesionName, Object bean) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put(sesionName, bean);
    }

    public static <T> T getBean(final String beanName) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (T) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, beanName);
    }

    public static void executeJS(String js) {
        PrimeFaces.current().executeScript(js);
    }

    public static void redirect(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void redirectFaces(String url) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            ec.redirect(ec.getRequestContextPath() + url);
        } catch (IOException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void redirectNewTab(String url) {
        PrimeFaces.current().executeScript("window.open('" + url + "', '_blank');");
    }

    public static void update(String update) {
        PrimeFaces.current().ajax().update(update);
    }

    public static String getRealPath(String subpath) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRealPath(subpath);
    }

    public static void closeDialog(Object data) {
        PrimeFaces.current().dialog().closeDynamic(data);
    }
}
