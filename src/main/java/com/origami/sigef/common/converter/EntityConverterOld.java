/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.converter;

import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.util.ReflexionEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author dfcalderio
 */
@SuppressWarnings("rawtypes")
@FacesConverter("entityConverter")
public class EntityConverterOld implements Converter {

    private EntityManagerService manager;

    public EntityConverterOld() {
        try {
            manager = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
        } catch (NamingException ex) {
            Logger.getLogger(EntityConverterOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent c, String value) {
        if (value == null || value.isEmpty() == true || value.equals("")) {
            return null;
        }
        try {
            String[] p = value.split(":");
            if (p == null || p.length < 3) {
                return null;
            }
            return manager.getManager().find(Class.forName(p[0]), ReflexionEntity.instanceConsString(p[2], p[1]));
        } catch (NullPointerException | ELException | NumberFormatException e) {
            Logger.getLogger(EntityConverterOld.class.getName()).log(Level.SEVERE, value, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EntityConverterOld.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EntityConverterOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
