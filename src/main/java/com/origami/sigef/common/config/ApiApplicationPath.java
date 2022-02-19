/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//CLASE PARA QUE LOS SERVICIOS WEB EMPIECEN CON "/api/"
@ApplicationPath("api")
public class ApiApplicationPath extends Application {

}
