/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class ProcedimientoService extends AbstractService<Procedimiento> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProcedimientoService() {
        super(Procedimiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Procedimiento> getProcedimientos(String codigo) {
        List<Procedimiento> resultado = (List<Procedimiento>) getEntityManager().createQuery("SELECT p FROM Procedimiento p INNER JOIN p.idTipoTramite pt WHERE p.estado=true AND pt.abreviatura=:codigo ORDER BY p.nombre ASC")
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public Boolean getConsultarRelacionCertificacion(Procedimiento procedimiento) {
        List<Procedimiento> resultado = (List<Procedimiento>) em.createQuery("SELECT c FROM SolicitudRequisito a "
                + "INNER JOIN a.idProcedimientoRequisito b "
                + "INNER JOIN b.idProcedimiento c "
                + "WHERE c.id=:procedimiento")
                .setParameter("procedimiento", procedimiento.getId())
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public Procedimiento getProcedimientoNoAplica(String codigo) {
        try {
            Procedimiento resultado = (Procedimiento) em.createQuery("SELECT p FROM Procedimiento p WHERE p.nombre=:codigo AND p.estado=TRUE")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean getTramiteAsociado(Procedimiento procedimiento) {
        TipoTramiteRequisitoHistorial resultado = (TipoTramiteRequisitoHistorial) em.createQuery("SELECT th FROM TipoTramiteRequisitoHistorial th "
                + "INNER JOIN th.procedimientoRequisito pr INNER JOIN pr.idProcedimiento p WHERE p.id=?1 AND p.estado=TRUE")
                .setParameter(1, procedimiento.getId())
                .getResultStream().findFirst().orElse(null);
        return resultado != null;
    }
}
