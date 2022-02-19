/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service.interfaces;

import javax.ejb.Local;
import org.activiti.engine.ProcessEngine;

/**
 *
 * @author User
 */
@Local
public interface BpmProcessEngine {

    public ProcessEngine getProcessEngine();

}
