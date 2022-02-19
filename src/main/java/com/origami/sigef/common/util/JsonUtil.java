/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ANGEL NAVARRO
 */
public class JsonUtil {

    public String toJson(Object value) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.FINAL);
            // Excluimos los campos que sean del mismo tipo de la clase a serializar
            // para evitar ciclo infinito
            builder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    if (f.getAnnotation(GsonExcludeField.class) != null) {
                        return true;
                    } else {
                        if (f.getName().contains("hibernate") || f.getName().contains("pcState") || f.getName().contains("pcDetached")
                                || f.getName().contains("pc_state") || f.getName().contains("pc_detached")) {
                            return true;
                        } else {
                            return f.getDeclaredType().equals(value.getClass());
                        }
                    }
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
//                    return clazz.equals(value.getClass());
                }
            })
                    .setPrettyPrinting();
            builder.serializeNulls();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            Gson gson = builder.create();
            return gson.toJson(value);
        } catch (Exception e) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return null;
    }

    public String toJsonNotSerializeNulls(Object value) {
        try {
            if (value == null) {
                return null;
            }
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.TRANSIENT);
            // Excluimos los campos que sean del mismo tipo de la clase a serializar
            // para evitar ciclo infinito
            builder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
//                    System.out.println(" f " + f.getName() + " " + f.getAnnotation(GsonExcludeField.class));
                    if (f.getAnnotation(GsonExcludeField.class) != null || f.getAnnotation(Transient.class) != null) {
                        return true;
                    } else {
                        return f.getDeclaredType().equals(value.getClass());
                    }
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
//                    return clazz.equals(value.getClass());
                }
            }).disableInnerClassSerialization()
                    .setPrettyPrinting().
                    setDateFormat("yyyy-MM-dd HH:mm:ss");
            Gson gson = builder.create();
            return gson.toJson(value);
        } catch (Exception e) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, "toJsonNotSerializeNulls", e);
        }
        return null;
    }

    /**
     * Si es arreglo de objeto debe enviar asi Document[].class y le retorna una
     * lista de esos objetos
     *
     * @param val
     * @param className
     * @return Lista de objetos si en un array, caso contrario el objeto
     * @throws IOException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object fromJson(String val, Class className) throws IOException {
        if (val == null) {
            return null;
        }
        if (val.trim().length() == 0) {
            return null;
        }
        if ("#".equals(val.trim())) {
            return null;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        if (className.isArray()) {
            Object[] fromJson = (Object[]) gson.fromJson(val, className);
            if (fromJson != null) {
                return Arrays.asList(fromJson);
            } else {
                return null;
            }
        } else {
            return gson.fromJson(val, className);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object fromJson(String val, Class className, @NotNull String... excludeField) throws IOException {
        if (val == null) {
            return null;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                //                .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                //                .registerTypeAdapter(Boolean.class, new BooleanSerializer())
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .setPrettyPrinting();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                for (String nameField : excludeField) {
                    if (nameField.equals(f.getName())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        Gson gson = gsonBuilder.create();
        gson.excluder().disableInnerClassSerialization();
        return gson.fromJson(val, className);
    }
}
