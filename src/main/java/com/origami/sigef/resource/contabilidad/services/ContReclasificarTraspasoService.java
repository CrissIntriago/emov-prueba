/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.resource.contabilidad.entities.ContReclasificarTraspaso;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriagos
 */
@Stateless
public class ContReclasificarTraspasoService extends AbstractService<ContReclasificarTraspaso> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContReclasificarTraspasoService() {
        super(ContReclasificarTraspaso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void loadData(String nameUser, Short busqueda, Short busqueda2) {
        int i = (Integer)em.createNativeQuery("SELECT * FROM contabilidad.fs_reclasificar_cuentas(?1,?2,?3,?4)")
                .setParameter(1, nameUser)
                .setParameter(2, busqueda)
                .setParameter(3, busqueda2)
                .setParameter(4, new Date())
                .getSingleResult();
    }

    public void getTraspasar(String nameUser, Short busqueda, Short busqueda2) {
        int i = (Integer)em.createNativeQuery("SELECT * FROM contabilidad.fs_traspasar_cuentas(?1,?2,?3,?4)")
                .setParameter(1, nameUser)
                .setParameter(2, busqueda)
                .setParameter(3, busqueda2)
                .setParameter(4, new Date())
                .getSingleResult();
    }

}
