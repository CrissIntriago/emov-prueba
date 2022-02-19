/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.entities.Requisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless @javax.enterprise.context.Dependent
public class RequisitoService extends AbstractService<Requisito> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RequisitoService() {
        super(Requisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Requisito> getRequisitos() {
        List<Requisito> resultado = (List<Requisito>) getEntityManager().createQuery("SELECT r FROM Requisito r WHERE r.estado= true ORDER BY r.nombre ASC")
                .getResultList();
        return resultado;
    }

    public Boolean getTramiteAsociado(Requisito requisito) {
        TipoTramiteRequisitoHistorial resultado = (TipoTramiteRequisitoHistorial) em.createQuery("SELECT th FROM TipoTramiteRequisitoHistorial th INNER JOIN th.procedimientoRequisito pr INNER JOIN pr.idRequisito p WHERE p.id=?1 AND p.estado=TRUE")
                .setParameter(1, requisito.getId())
                .getResultStream().findFirst().orElse(null);
        return resultado != null;
    }
    
}
