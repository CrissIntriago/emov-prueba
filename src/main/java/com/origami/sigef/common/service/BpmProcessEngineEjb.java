/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.ApplicationContextUtils;
import com.origami.sigef.common.service.interfaces.BpmProcessEngine;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import org.activiti.engine.ProcessEngine;

/**
 *
 * @author User
 */
@Stateless @javax.enterprise.context.Dependent
public class BpmProcessEngineEjb implements BpmProcessEngine {

    private ProcessEngine processEngine;

    @PostConstruct
    public void init() {
        processEngine = (ProcessEngine) ApplicationContextUtils.getBean("processEngine");
    }

    @Override
    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

}
