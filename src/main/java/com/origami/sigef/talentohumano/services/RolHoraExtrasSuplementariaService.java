/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.RolHorasExtrasSuplementarias;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@Stateless
@javax.enterprise.context.Dependent
public class RolHoraExtrasSuplementariaService extends AbstractService<RolHorasExtrasSuplementarias> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RolHoraExtrasSuplementariaService() {
        super(RolHorasExtrasSuplementarias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<RolHorasExtrasSuplementarias> getListaRolHoras(TipoRol rol) {
        try {
            Query query = em.createQuery("SELECT h FROM RolHorasExtrasSuplementarias h where h.tipoRol = ?1 AND h.estado = true")
                    .setParameter(1, rol);
            List<RolHorasExtrasSuplementarias> result = (List<RolHorasExtrasSuplementarias>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal totalIngresoHora(TipoRol rol) {
        try {
            BigDecimal var = BigDecimal.ZERO;
            var = (BigDecimal) em.createQuery("SELECT SUM(r.netoRecibir) FROM RolHorasExtrasSuplementarias r WHERE r.estado = true AND r.tipoRol = ?1")
                    .setParameter(1, rol).getSingleResult();
            return var;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public Short totalHora(TipoRol rol) {
        try {
            Long var;
            var = (Long) em.createQuery("SELECT SUM(r.totalHora) FROM RolHorasExtrasSuplementarias r WHERE r.estado = true AND r.tipoRol = ?1")
                    .setParameter(1, rol).getSingleResult();
            return var.shortValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public int actualizarEstadoLiquidacion(Boolean var, TipoRol rol) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.rol_horas_extras_suplementarias\n"
                + "	SET estado=?1\n"
                + "	WHERE estado=true and tipo_rol=?2").setParameter(1, var).setParameter(2, rol.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

}
