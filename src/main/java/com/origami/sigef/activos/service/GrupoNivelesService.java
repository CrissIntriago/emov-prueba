/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Sandra Arroba
 */
@Stateless
@javax.enterprise.context.Dependent
public class GrupoNivelesService extends AbstractService<GrupoNiveles> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public GrupoNivelesService() {
        super(GrupoNiveles.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<GrupoNiveles> getHijosByPadre(GrupoNiveles padre) {
        return findByNamedQuery("GrupoNiveles.findByPadre", new Object[]{padre.getId()});
    }

    public Short getOrdenSubgrupo(GrupoNiveles c) {

        Short val = 0;

        val = (Short) getEntityManager().createQuery("SELECT n.orden FROM GrupoNiveles c INNER JOIN c.nivel n WHERE c.id = :orden")
                .setParameter("orden", c.getId())
                .getSingleResult();
        return val;
    }

    public Long getNivelOrden(String catalogo, String codigo, Short orden) {

        Long result = findByNamedQuery1("GrupoNiveles.findByMaxOrden", new Object[]{catalogo, codigo, orden});
        if (result == null) {
            result = 1L;
        }
        return result;
    }

    public Long getNivelOrdenSub(String catalogo, String codigo, Short orden, GrupoNiveles padre) {

        Long result = findByNamedQuery1("GrupoNiveles.findByMaxOrdenSub", new Object[]{catalogo, codigo, orden, padre});
        if (result == null) {
            result = 1L;
        }
        return result;
    }

    public GrupoNiveles findByPadreGrupo(GrupoNiveles g) {
        try {
            return this.findByNamedQuery1("GrupoNiveles.findByD", g.getId());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public GrupoNiveles findGrupoNivelByCodigo(String codigo) {
        try {
            return (GrupoNiveles) em.createQuery("SELECT g FROM GrupoNiveles g WHERE g.codigo = :codigo and g.padre is null").setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }
    
  public Boolean existenRegistrosArea(GrupoNiveles cat) {
        Boolean existe;
        GrupoNiveles detalle = null;
        try {
            detalle = (GrupoNiveles) em.createQuery("SELECT DISTINCT(d.areaNivel)  FROM DetalleItem d  WHERE d.estado = true AND d.areaNivel = :cat")
                    .setParameter("cat", cat)
                    .getSingleResult();
        } catch (NoResultException e) {
            detalle = null;
        }
        if (detalle != null) {
            existe = Boolean.TRUE;
            return existe;
        }
        existe = Boolean.FALSE;
        return existe;
    }
  public Boolean existenRegistrosGrupo(GrupoNiveles cat) {
        Boolean existe;
        GrupoNiveles detalle = null;
        try {
            detalle = (GrupoNiveles) em.createQuery("SELECT DISTINCT(d.asignarGrupo)  FROM DetalleItem d  WHERE d.estado = true AND d.asignarGrupo = :cat")
                    .setParameter("cat", cat)
                    .getSingleResult();
        } catch (NoResultException e) {
            detalle = null;
        }
        if (detalle != null) {
            existe = Boolean.TRUE;
            return existe;
        }
        existe = Boolean.FALSE;
        return existe;
    }

    public GrupoNiveles findByPadreGrupo(Long id) {
        try {
            return this.findByNamedQuery1("GrupoNiveles.findByD", id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<GrupoNiveles> findByPadres() {
        return this.findByNamedQuery("GrupoNiveles.findByPadres");
    }

    public List<GrupoNiveles> findGrupoByPadreEscogido(GrupoNiveles area) {
        return this.findByNamedQuery("GrupoNiveles.findGrupoByPadreEscogido", area);
    }

    public List<GrupoNiveles> findSubGrupoByGrupoEscogido(GrupoNiveles grupo) {
        return this.findByNamedQuery("GrupoNiveles.findSubGrupoByGrupoEscogido", grupo);
    }

    public List<GrupoNiveles> getItemsParameter(Long id) {
        return this.findByNamedQuery("GrupoNiveles.getItemsParameter", id);
    }

    public List<GrupoNiveles> getItemsParameter1(Long id) {
        return this.findByNamedQuery("GrupoNiveles.getItemsParameter", id);
    }

}
