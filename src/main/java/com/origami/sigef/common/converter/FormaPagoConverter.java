package com.origami.sigef.common.converter;

import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "formaPagoConverter", forClass = FormaPago.class)
public class FormaPagoConverter implements Converter, Serializable {

    private EntityManagerService manager;

    public FormaPagoConverter() {
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
            return manager.getManager().find(FormaPago.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof FormaPago) {
            return ((FormaPago) value).getId().toString();
        }
        return null;
    }

}
