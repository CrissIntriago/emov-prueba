/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.converter;

import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.util.ReflexionEntity;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dfcalderio
 */
@Named(value = "utilConverter")
@RequestScoped
public class UtilConverter implements Converter, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManagerService managerService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent c, String value) {
        if (value == null || value.isEmpty() == true || value.equals("")) {
            return null;
        }
        String clazz = null;
        try {
            String[] p = value.split(":");
            if (p == null || p.length < 3) {
                return null;
            }
            clazz = p[0];
            return managerService.getManager().find(Class.forName(p[0]), ReflexionEntity.instanceConsString(p[2], p[1]));
        } catch (NullPointerException | ELException | NumberFormatException e) {
            HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
            Logger.getLogger(UtilConverter.class.getName()).log(Level.SEVERE, req.getRequestURL().toString() + " >> " + clazz + " >> " + value, e);
        } catch (ClassNotFoundException ex) {
            HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
            Logger.getLogger(UtilConverter.class.getName()).log(Level.SEVERE, req.getRequestURL().toString() + " >> " + clazz, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent c, Object value) {
        if (value == null) {
            return null;
        }
        try {
            String val = ReflexionEntity.getIdEntity(value);
            return val;
        } catch (Exception ex) {
            Logger.getLogger(UtilConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
