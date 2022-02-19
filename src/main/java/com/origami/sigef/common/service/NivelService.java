/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.Nivel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Dairon Freddy
 */
@Stateless
@javax.enterprise.context.Dependent
public class NivelService extends AbstractService<Nivel> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public NivelService() {
        super(Nivel.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Nivel getProximoNivel(Nivel nivel) {
        short orden = (short) (nivel.getOrden() + 1);
        List<Nivel> result = (List<Nivel>) em.createQuery("SELECT n FROM Nivel n JOIN n.tipo t WHERE t.id = :id AND n.orden = :orden")
                .setParameter("id", nivel.getTipo().getId())
                .setParameter("orden",orden).getResultList();
        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    public Nivel getFirstNivel(String catalogo, String codigo) {

        List<Nivel> result = (List<Nivel>) em.createQuery("SELECT n FROM Nivel n JOIN n.tipo t JOIN t.catalogo c WHERE c.codigo = :codigo AND t.codigo = :codigo2 ORDER BY n.orden ASC")
                .setParameter("codigo", catalogo).setParameter("codigo2", codigo).getResultList();
        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    public Nivel getNivelOrden(String catalogo, String codigo, Short orden) {

        List<Nivel> result = findByNamedQuery("Nivel.findByCatalogoAndCodigoAndOrden", new Object[]{catalogo, codigo, orden});
        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }
}
