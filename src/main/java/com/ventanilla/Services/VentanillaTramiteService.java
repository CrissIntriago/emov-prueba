/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Services;

import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.ventanilla.Entity.SolicitudServicios;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.inject.Inject;
import org.activiti.engine.runtime.ProcessInstance;

/**
 *
 * @author ricar
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class VentanillaTramiteService extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private SeqGenMan secuencia;

    public void iniciarTramiteVentanilla(String referenciaLiquidacion) {
        if (!Utils.isEmptyString(referenciaLiquidacion)) {
            SolicitudServicios ss = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByReferenciaLiquidacion", new Object[]{referenciaLiquidacion});
            if (ss != null && ss.getId() != null && ss.getPendientePago()) {
                System.out.println("inicio de tramite");
//                Map<String, Object> paramts = new Gson().fromJson(ss.getParamsTramite(), HashMap.class);
                Map<String, Object> paramts = new HashMap<>();
                ProcessInstance p = null;
                try {
                    p = engine.startProcessByDefinitionKey(ss.getTramite().getTipoTramite().getActivitykey(), (HashMap<String, Object>) paramts);
                } catch (Exception exception) {
                    LOG.log(Level.SEVERE, tramite.getTipoTramite().getDescripcion(), exception);
                }
                if (p != null) {
                    ss.setPendientePago(Boolean.FALSE);
                    ss.setEnObservacion(Boolean.FALSE);
                    ventanillaService.save(ss);
                    ss.getTramite().setNumTramite(secuencia.getSecuencia("NUMERO_TRAMITE").longValue());
                    if (ss.getTramite().getServicio() != null) {
                        ss.getTramite().setCodigo(ss.getTramite().getServicio().getAbreviatura() + "-" + ss.getTramite().getNumTramite());
                    }
                    ss.getTramite().setIdProceso(p.getId());
                    tramiteService.edit(ss.getTramite());
                }
            } else {
                System.out.println("PASO POR AQUI??");
                SolicitudServicios ssDB = (SolicitudServicios) ventanillaService.findByNamedQuery1("SolicitudServicios.findByReferenciaLiquidacionPago", new Object[]{referenciaLiquidacion});
                if (ssDB != null && ssDB.getId() != null) {
                    System.out.println("ACTUALIZANDO LA SOLICITUD SERVICIOS ??");
                    ssDB.setPendientePago(Boolean.FALSE);
                    ssDB.setEnObservacion(Boolean.FALSE);
                    ventanillaService.save(ssDB);
                }
            }
        }
    }

}
