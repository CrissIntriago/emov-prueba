/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProcedimientoRequisitoServices extends AbstractService<ProcedimientoRequisito> {

    private static final Logger LOG = Logger.getLogger(ReservaCompromisoServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcedimientoRequisitoServices() {
        super(ProcedimientoRequisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ProcedimientoRequisito> getRequisitosDelProcedimiento(Procedimiento procedimiento) {
        List<ProcedimientoRequisito> resultado = (List<ProcedimientoRequisito>) em.createQuery("SELECT pr FROM ProcedimientoRequisito pr INNER JOIN pr.idRequisito r WHERE pr.idProcedimiento=:procedimiento AND r.estado=TRUE ORDER BY pr.id ASC")
                .setParameter("procedimiento", procedimiento)
                .getResultList();
        return resultado;
    }
}
