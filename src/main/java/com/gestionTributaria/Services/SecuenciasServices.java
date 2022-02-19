/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class SecuenciasServices extends AbstractService<SecuenciaGeneral> {

    private static final Logger LOG = Logger.getLogger(SecuenciasServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SecuenciasServices() {
        super(SecuenciaGeneral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SecuenciaGeneral findNumberByCodigo(String codigo) {
        SecuenciaGeneral secuencia = new SecuenciaGeneral();
        try {
            secuencia = (SecuenciaGeneral) em.createQuery("Select a from SecuenciaGeneral a where a.code=?1").setParameter(1, codigo).getSingleResult();
            BigInteger siguiente = secuencia.getSecuencia().add(new BigInteger("1"));
            secuencia.setSecuencia(siguiente);
            edit(secuencia);
            return secuencia;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return secuencia;
        }
    }
}
