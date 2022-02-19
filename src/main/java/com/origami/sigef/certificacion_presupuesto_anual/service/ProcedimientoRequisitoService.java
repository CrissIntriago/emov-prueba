/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Requisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProcedimientoRequisitoService extends AbstractService<ProcedimientoRequisito> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcedimientoRequisitoService() {
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

    public Long getCantidadRequerida(Procedimiento procedimiento, Boolean obligatorio) {
        Long resultado = (Long) em.createQuery("Select COUNT(p.obligatorio) FROM ProcedimientoRequisito p"
                + " WHERE p.obligatorio= :obligatorio AND p.idProcedimiento=:procedimiento")
                .setParameter("obligatorio", obligatorio)
                .setParameter("procedimiento", procedimiento)
                .getSingleResult();
        return resultado;
    }

    public Boolean getConsultarCertificacion(ProcedimientoRequisito procedimientoRequisito, Short periodo) {
        List<ProcedimientoRequisito> resultado = (List<ProcedimientoRequisito>) em.createQuery("SELECT s FROM SolicitudRequisito s "
                + "INNER JOIN s.idSolicitudReserva r "
                + "WHERE s.idProcedimientoRequisito=:procedimientoRequisito AND r.periodo=:periodo")
                .setParameter("procedimientoRequisito", procedimientoRequisito)
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public List<ProcedimientoRequisito> getListaRequisitos(Long id) {
        List<ProcedimientoRequisito> result = (List<ProcedimientoRequisito>) getEntityManager().createQuery("SELECT p FROM  ProcedimientoRequisito p INNER JOIN p.idProcedimiento ip INNER JOIN ip.idTipoTramite tt WHERE tt.id=:id")
                .setParameter("id", id).getResultList();
        return result;
    }

    public Boolean getTramiteAsociado(ProcedimientoRequisito procedimientoRequisito) {
        TipoTramiteRequisitoHistorial resultado = (TipoTramiteRequisitoHistorial) em.createQuery("SELECT th FROM TipoTramiteRequisitoHistorial th WHERE th.procedimientoRequisito=:procedimientoRequisito")
                .setParameter("procedimientoRequisito", procedimientoRequisito)
                .getResultStream().findFirst().orElse(null);
        return resultado != null;
    }

}
