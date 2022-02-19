/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Entidad.Service;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Criss Intriago
 */
@Stateless
@javax.enterprise.context.Dependent
public class DatosGeneralesEntidadService extends AbstractService<DatosGeneralesEntidad> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession us;

    public DatosGeneralesEntidadService() {
        super(DatosGeneralesEntidad.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<Provincia> getProvincias() {

        List<Provincia> resultado = (List<Provincia>) getEntityManager().createQuery("SELECT p FROM Provincia p WHERE p.habilitado = true")
                .getResultList();
        return resultado;
    }

    public List<Canton> getCantones(Provincia provincia) {
        if (provincia == null) {
            return null;
        }
        //SELECT c FROM Canton c INNER JOIN c. c WHERE c.codigo =:codigo
        List<Canton> resultado = (List<Canton>) getEntityManager().createQuery("SELECT c FROM Canton c INNER JOIN c.idProvincia p WHERE p.id=:provincia AND p.habilitado = true AND c.habilitado=true")
                .setParameter("provincia", provincia.getId())
                .getResultList();
        return resultado;
    }

    public Long getRegistroUnico() {
        Long resultado = (Long) getEntityManager().createQuery("SELECT COUNT(d) FROM DatosGeneralesEntidad d WHERE d.estado = true")
                .getSingleResult();
        return resultado;
    }

    public DatosGeneralesEntidad getEntidad() {
        try {
//            Query query = em.createQuery("SELECT e FROM DatosGeneralesEntidad e where e.estado = TRUE");
//            List<DatosGeneralesEntidad> entidad = (List<DatosGeneralesEntidad>) query.getResultList();
//            return entidad.get(0);
            return us.getUsuario().getEmpresaId();
        } catch (Exception e) {
            return null;
        }
    }
}
