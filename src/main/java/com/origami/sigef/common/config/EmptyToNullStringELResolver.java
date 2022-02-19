/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;

/**
 *
 * @author ANGEL NAVARRO
 */
public class EmptyToNullStringELResolver extends ELResolver {

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }

    @Override
    public Object convertToType(ELContext context, Object value, Class<?> targetType) {
        if (value == null && targetType == String.class) {
            context.setPropertyResolved(true);
        }

        return value;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        // NOOP.
    }

}
