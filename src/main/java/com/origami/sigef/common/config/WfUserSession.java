/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author gutya
 */
public interface WfUserSession extends Serializable {

    String getDepartamento();

    Boolean getEsDirector();

    String getName();

    String getNameUser();

    String getNombrePersonaLogeada();

    Boolean getTemp();

    String getUrlSolicitada();

    Long getUserId();

    List<Long> getRoles();

    List<Long> getDepts();

    String getIpClient();

}
