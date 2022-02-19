/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

@ApplicationScoped
public class RutasSystemContr {

    @Inject
    private ServletContext ctx;

    public String getRootPath() {
        return ctx.getRealPath("/");
    }

    private Logger logger() {
        return Logger.getLogger(this.getClass().getName());
    }

}
