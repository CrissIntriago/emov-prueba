/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoObservacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class FnConvenioPagoObservacionService extends AbstractService<FnConvenioPagoObservacion> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnConvenioPagoObservacionService() {
        super(FnConvenioPagoObservacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FnConvenioPagoObservacion findByLiquidacion(FnConvenioPago convenio) {
        FnConvenioPagoObservacion convenioObservacion = new FnConvenioPagoObservacion();
        try {
            convenioObservacion = (FnConvenioPagoObservacion) em.createQuery("select a from   ").setParameter(1, convenio).getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(FnConvenioPagoObservacion.class.getName()).log(Level.SEVERE, "Error al buscar la liquidacion en Liquidacion Convenio", ex);
        }
        return convenioObservacion;
    }

    public FnConvenioPagoObservacion findByConvenioObservacion(FnConvenioPago convenio) {
        List<FnConvenioPagoObservacion> convenioPago = new ArrayList<>();
        try {
            System.out.println("CONVENIO " + convenio);
            convenioPago = (List<FnConvenioPagoObservacion>) em.createQuery("select a from FnConvenioPagoObservacion a where a.convenio=?1 and a.estadoConvenio=3").setParameter(1, convenio).getResultList();
            if (convenioPago.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(FnConvenioPagoObservacion.class.getName()).log(Level.SEVERE, "Error al buscar la observaci[on del convenio", ex);
        }
        return convenioPago.get(0);
    }
}
