package com.origami.sigef.tesoreria.comprobantelectronico.service;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless @javax.enterprise.context.Dependent
public class RespuestaComprobanteService extends AbstractService<RespuestaComprobante> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RespuestaComprobanteService() {
        super(RespuestaComprobante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

}
