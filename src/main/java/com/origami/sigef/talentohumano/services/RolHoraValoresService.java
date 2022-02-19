/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.RolHorasExtrasSuplementarias;
import com.origami.sigef.common.entities.RolHorasValores;
import com.origami.sigef.common.service.AbstractService;
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
public class RolHoraValoresService extends AbstractService<RolHorasValores> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RolHoraValoresService() {
        super(RolHorasValores.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<RolHorasValores> getListaValorRolHoras(RolHorasExtrasSuplementarias hora) {
        try {
            Query query = em.createQuery("SELECT vr FROM RolHorasValores vr WHERE vr.rolHora = ?1 AND vr.estado = TRUE")
                    .setParameter(1, hora);
            List<RolHorasValores> result = (List<RolHorasValores>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int actualizarEstadoRubro(Boolean var, RolHorasExtrasSuplementarias rol) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.rol_horas_valores\n"
                + "	SET estado=?1\n"
                + "	WHERE rol_hora = ?2").setParameter(1, var).setParameter(2, rol.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

}
