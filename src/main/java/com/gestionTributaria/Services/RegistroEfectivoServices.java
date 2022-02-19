/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.EfectivoDinero;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.util.Date;
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
public class RegistroEfectivoServices extends AbstractService<EfectivoDinero> {

    private static final Logger LOG = Logger.getLogger(RegistroEfectivoServices.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RegistroEfectivoServices() {
        super(EfectivoDinero.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean getRegistroByFechaAndCajero(Date fecha, Cajero caja) {
        try {
            System.out.println("fecha >>"+fecha);
            EfectivoDinero result = (EfectivoDinero) em.createQuery("SELECT d FROM EfectivoDinero d where d.fechaRegistro = ?1 AND d.caja = ?2")
                    .setParameter(1, fecha).setParameter(2, caja).getSingleResult();
            return result != null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return Boolean.FALSE;
        }
    }

}
