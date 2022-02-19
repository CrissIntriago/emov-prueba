/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.audit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author ANGEL NAVARRO
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ShowName {

    public String name();

    public boolean isReferences() default false;
}
