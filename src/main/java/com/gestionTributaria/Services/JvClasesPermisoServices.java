/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.JvClasesPermisos;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
public class JvClasesPermisoServices extends AbstractService<JvClasesPermisos> {

    private static final Logger LOG = Logger.getLogger(InteresesService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private ManagerService services;

    public JvClasesPermisoServices() {
        super(JvClasesPermisos.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<JvClasesPermisos> getItems(Long catalogo) {
        System.out.println("cata " + catalogo);
        return (List<JvClasesPermisos>) em.createQuery("SELECT e from JvClasesPermisos e where e.estado=true AND e.catalogo.id=:catalogo")
                .setParameter("catalogo", catalogo).getResultList();
    }

    public BigDecimal valorPermisoComisaraViaPublica(Long tipo, Integer clasePermiso, BigDecimal metros, Date fechaActual) {
        BigDecimal result = (BigDecimal) em.createNativeQuery("select asgard.calculo_via_publica(?1,?2,?3,?4)")
                .setParameter(1, tipo).setParameter(2, (Integer) clasePermiso).setParameter(3, (BigDecimal) metros).setParameter(4, fechaActual)
                .getResultStream().findFirst().orElse(BigDecimal.ZERO);

        System.out.println("result " + result);
        if (result == null) {

            result = BigDecimal.ZERO;

        }
        return result;
    }

}
