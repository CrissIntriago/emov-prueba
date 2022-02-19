/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Entities.CtlgSalario;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TmpService extends BpmnBaseRoot {

    @Inject
    private ManagerService service;

    public void culimnarProcesos(FinaRenPago renPago) throws Exception {

        if (renPago != null && renPago.getLiquidacion() != null && renPago.getLiquidacion().getEstadoLiquidacion() != null) {
            System.out.println("entrando a terminar el proceso");
            if (renPago.getLiquidacion().getEstadoLiquidacion().equals(1L) || renPago.getLiquidacion().getEstadoLiquidacion().equals(8L)) {
                if (renPago.getLiquidacion().getTramite() != null) {
                    this.completarTareaAisladas(renPago.getLiquidacion().getTramite().longValue(), renPago.getCajero().getUsuario());
                }
           }
            System.out.println("saliendo de terminar el proceso");
        }

    }
}
