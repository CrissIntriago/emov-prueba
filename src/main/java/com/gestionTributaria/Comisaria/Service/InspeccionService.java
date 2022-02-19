/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Service;

import com.gestionTributaria.Comisaria.Entities.Inspecciones;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
@javax.enterprise.context.Dependent
public class InspeccionService extends AbstractService<Inspecciones> {

    private static final Logger LOG = Logger.getLogger(ComisariaServices.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InspeccionService() {
        super(Inspecciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Long noSolicitud() {
        BigInteger res = (BigInteger) em.createNativeQuery("SELECT nextval('comisaria.solicitud_inspeccion')").getResultStream().findFirst().orElse(BigInteger.ZERO);
        System.out.println("res " + res);
        return res.longValue();
    }

    public boolean verificarSolciitud(Long numTramite) {

        List<Inspecciones> lista = (List<Inspecciones>) em.createQuery("SELECT i FROM Inspecciones i where i.numTramite=:num")
                .setParameter("num", numTramite).getResultList();

        if (lista != null && !lista.isEmpty()) {
            return true;
        }

        return false;
    }

    public List<Inspecciones> getListaInspecciones(Long numTramite) {
        return (List<Inspecciones>) em.createQuery("SELECT i from Inspecciones i where i.numTramite=:num AND i.delegado is null")
                .setParameter("num", numTramite).getResultList();
    }
}
