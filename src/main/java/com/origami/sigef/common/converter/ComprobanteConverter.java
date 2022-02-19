/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.converter;

import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author gutya
 */
@FacesConverter(value = "comprobantesConverter", forClass = Comprobantes.class)
public class ComprobanteConverter implements Converter, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private EntityManagerService manager;

    public ComprobanteConverter() {
        try {
            manager = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
        } catch (NamingException ex) {
            Logger.getLogger(EntityConverterOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer id = -1;
        try {
            id = Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
        try {
            return (Comprobantes) manager.getManager().find(Comprobantes.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Comprobantes) {
            return ((Comprobantes) value).getId().toString();
        }
        return null;
    }

}
