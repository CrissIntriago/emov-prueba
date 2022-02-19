/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service.procesoService;

import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisito;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author alexa
 */
@Stateless @javax.enterprise.context.Dependent
public class ProcesoService extends AbstractService<TipoTramiteRequisito> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcesoService() {
        super(TipoTramiteRequisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<TipoTramite> getListaTipoTramite() {
        List<TipoTramite> result = (List<TipoTramite>) getEntityManager().createQuery("SELECT h FROM TipoTramite h ").getResultList();
        return result;

    }

    public List<ProcedimientoRequisito> getListaRequisito(Long t) {
        List<ProcedimientoRequisito> result = (List<ProcedimientoRequisito>) 
                getEntityManager().createQuery("SELECT p FROM  ProcedimientoRequisito p INNER JOIN p.idProcedimiento ip INNER JOIN ip.idTipoTramite tt INNER JOIN p.idRequisito re WHERE tt.id=:id AND ip.estado = TRUE AND re.estado=TRUE")
                .setParameter("id", t).getResultList();
        return result;

    }
    
    public List<ProcedimientoRequisito> getListaRequisitoProcesos(Long t) {
        List<ProcedimientoRequisito> result = (List<ProcedimientoRequisito>) getEntityManager().createQuery("SELECT FROM ProcedimientoRequisito p  INNER JOIN p.idProcedimiento pr INNER JOIN")
                .setParameter("id", t).getResultList();
        return result;

    }

}
