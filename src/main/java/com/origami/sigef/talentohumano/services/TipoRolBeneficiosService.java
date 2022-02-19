/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TipoRolBeneficiosService extends AbstractService<TipoRolBeneficios> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TipoRolBeneficiosService() {
        super(TipoRolBeneficios.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<TipoRolBeneficios> findByTipoRolByTipo(String codigo) {
        try {
            Query query = em.createQuery("SELECT t FROM TipoRolBeneficios t JOIN t.tipo c WHERE t.estado = TRUE AND c.codigo = ?1 ")
                    .setParameter(1, codigo);
            List<TipoRolBeneficios> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;

        }
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral, CatalogoItem estadoAprobacion) {
        Query query = getEntityManager().createNativeQuery("UPDATE talento_humano.tipo_rol_beneficios SET estado_aprobacion = ?1, diario_rol_beneficios = false WHERE id_diario_general = ?2")
                .setParameter(1, estadoAprobacion)
                .setParameter(2, diarioGeneral);
        return query.executeUpdate();
    }
}
