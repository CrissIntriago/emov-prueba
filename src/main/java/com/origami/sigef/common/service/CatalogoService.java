/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.Catalogo;
import com.origami.sigef.common.entities.CatalogoItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Dairon Freddy
 */
@Stateless
@javax.enterprise.context.Dependent
public class CatalogoService extends AbstractService<Catalogo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoService() {
        super(Catalogo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    private String getQueryHQLExisteCuenta(Catalogo c) {
        String query = null;
        if (c.getCodigo() != null) {
            return "SELECT c FROM Catalogo c WHERE c.codigo = :codigo";
        }
        return query;
    }

    public List<CatalogoItem> getItemsByCatalogo(String catalogo) {

        List<CatalogoItem> result = (List<CatalogoItem>) getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = :catalogo  ORDER BY c.orden")
                .setParameter("catalogo", catalogo)
                .getResultList();

        return result;
    }

    public List<Catalogo> getItemCatalogo(List<String> opciones) {

        List<Catalogo> result = (List<Catalogo>) getEntityManager().createQuery("SELECT c FROM Catalogo c WHERE c.codigo in (:items)")
                .setParameter("items", opciones)
                .getResultList();

        return result;
    }

    public CatalogoItem getItemByCatalogoAndCodigo(String catalogo, String codigo) {
        try {
            List<CatalogoItem> result = (List<CatalogoItem>) getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc where cc.codigo= :catalogo AND c.codigo = :codigo")
                    .setParameter("catalogo", catalogo)
                    .setParameter("codigo", codigo)
                    .getResultList();

            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public CatalogoItem getItemByCatalogoAndDescripcion(String catalogo, String codigo) {
        CatalogoItem result = new CatalogoItem();
        try {
            result = (CatalogoItem) getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc where cc.codigo= :catalogo AND c.texto = :descripcion")
                    .setParameter("catalogo", catalogo)
                    .setParameter("descripcion", codigo)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            Logger.getLogger(CatalogoItem.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public List<CatalogoItem> getClasificacionPresupuesto(String codigo) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo= :codigo AND c.codigo='I' OR cc.codigo= :codigo2 AND c.codigo='E'")
                .setParameter("codigo", codigo)
                .setParameter("codigo2", codigo)
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> getClasificacionPresupuestoItem(String codigo, String codigoItem) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo= :codigo AND c.codigo= :codigoItem")
                .setParameter("codigo", codigo)
                .setParameter("codigoItem", codigoItem)
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> getMuestraTipo(String catalogo, List<String> items) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT c FROM CatalogoItem c INNER JOIN c.catalogo cc WHERE cc.codigo = :catalogo AND c.codigo IN (:items)")
                .setParameter("catalogo", catalogo)
                .setParameter("items", items)
                .getResultList();
        return resultado;
    }

    public List<Long> getMuestraTipoLong(String catalogo, String[] items) {
        List<Long> resultado = (List<Long>) em.createQuery("SELECT c.id FROM CatalogoItem c INNER JOIN c.catalogo cc WHERE cc.codigo = :catalogo AND c.codigo IN (:items)")
                .setParameter("catalogo", catalogo)
                .setParameter("items", items)
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> MostarTodoCatalogoItems(Catalogo c) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT "
                + "m FROM CatalogoItem m INNER JOIN m.catalogo cc WHERE cc.id= :id order by m.codigo asc")
                .setParameter("id", c.getId())
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> MostarTodoCatalogo(String codigo) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) getEntityManager().createQuery("SELECT m FROM CatalogoItem m INNER JOIN m.catalogo cc WHERE cc.codigo= :codigo AND m.estado=true ")
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public List<String> porcentajeAvances(String codigo) {
        List<String> resultado = (List<String>) getEntityManager().createQuery("SELECT m.texto FROM CatalogoItem m INNER JOIN m.catalogo cc WHERE cc.codigo= :codigo")
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public CatalogoItem getTipoItem(String codigo) {
        try {
            CatalogoItem result = (CatalogoItem) getEntityManager().createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo = :codigo")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    private Map<String, Object> getParameter(Catalogo c) {

        Map<String, Object> params = new HashMap<>();
        if (c.getCodigo() != null) {
            params.put("codigo", c.getCodigo());
        }
        return params;
    }

    public Catalogo existeCuenta(Catalogo c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<Catalogo> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    public List<CatalogoItem> getTiposDiarioGeneral(CatalogoItem clase, String codigoCatalogo) {
        String sql = "SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo=:codigoCatalogo AND ci.codigo NOT LIKE 'tipo_presupuesto'";
        List<CatalogoItem> result = (List<CatalogoItem>) em.createQuery(sql)
                .setParameter("codigoCatalogo", codigoCatalogo)
                .getResultList();
        return result;
    }

    public List<CatalogoItem> getTiposDiarioGeneralApertura(String codigoCatalogo, String desc) {
        String sql = "";
        sql = "SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo = :codigoCatalogo AND ci.descripcion = :desc";
        List<CatalogoItem> result = (List<CatalogoItem>) em.createQuery(sql)
                .setParameter("codigoCatalogo", codigoCatalogo)
                .setParameter("desc", desc)
                .getResultList();
        return result;
    }

    public List<CatalogoItem> getListaEnterCatalogoItem(List<String> codigos) {
        List<CatalogoItem> lista = (List<CatalogoItem>) em.createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo in (:codigo)").setParameter("codigo", codigos).getResultList();
        return lista;
    }

    public List<CatalogoItem> getListaCatalogoItem(List<String> codigos) {
        List<CatalogoItem> lista = (List<CatalogoItem>) em.createQuery("SELECT c FROM CatalogoItem c WHERE c.catalogo.codigo in (:codigo)").setParameter("codigo", codigos).getResultList();
        return lista;
    }

}
