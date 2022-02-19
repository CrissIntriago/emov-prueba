/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.catalogoItem.service;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Edwin Castro
 */
@Stateless
@javax.enterprise.context.Dependent
public class CatalogoItemService extends AbstractService<CatalogoItem> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoItemService() {
        super(CatalogoItem.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<CatalogoItem> getHijosByPadre(CatalogoItem padre) {
        return findByNamedQuery("CatalogoItem.findByPadre", new Object[]{padre});
    }

    public List<CatalogoItem> findCatalogoItems(String codigo) {
        return this.findByNamedQuery("CatalogoItem.findByCatalogo", codigo);
    }

    //SOLO FILTRAR POR MOVIMIENTO SOLO DE INVENTARIO
    public List<CatalogoItem> findMovimientoInventario(String origen) {
        return this.findByNamedQuery("CatalogoItem.findByCatalogo", origen);
    }

    public List<CatalogoItem> findAllEstados(String cod2){
        return this.findByNamedQuery("CatalogoItem.findAllEstados", cod2);
    }

    public List<CatalogoItem> findCatalogoClasificacion1(String clas) {
        return this.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", clas);
    }

    public CatalogoItem getTipoIdentificacion(String codigo) {
        CatalogoItem resultado = (CatalogoItem) getEntityManager().createQuery("SELECT ci FROM CatalogoItem ci WHERE ci.codigo =:codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
        return resultado;
    }

    public List<CatalogoItem> getTipoIdentificacionCodigo(String codigo) {
        List<CatalogoItem> lista = new ArrayList<>();
        try {
            lista = (List<CatalogoItem>) getEntityManager().createQuery("SELECT ci FROM CatalogoItem ci WHERE ci.codigo =:codigo").setParameter("codigo", codigo).getResultList();
        } catch (Exception ex) {
            System.out.println("Error al buscar por codigo " + ex.getMessage());
        }
        return lista;
    }

    public CatalogoItem findCatalogoItemByCodigoAndCatalogo_Codigo(String codigoItem, String codigo) {
        try {
            CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT ci FROM CatalogoItem ci JOIN ci.catalogo ct WHERE ci.codigo = ?1 AND ct.codigo = ?2")
                    .setParameter(1, codigoItem)
                    .setParameter(2, codigo)
                    .getSingleResult();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> findCatalogoTipo1(String cat, String tip) {
        return this.findByNamedQuery("CatalogoItem.findCatalogotipo1", cat, tip);
    }

    public List<CatalogoItem> findCatalogotipoLike(String cat, String tip) {
        return this.findByNamedQuery("CatalogoItem.findCatalogotipoLike", cat, "%" + tip + "%");
    }

    public List<CatalogoItem> findByCedulaRuc(String dat, String dat2) {
        return this.findByNamedQuery("CatalogoItem.findByCedulaRuc", dat, dat2);
    }

    public List<CatalogoItem> findListCatalogoTipoBan(String dat) {
        return this.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", dat);
    }

    public CatalogoItem findByCedulaRucObj(String dat, String dat2) {
        return this.findByNamedQuery1("CatalogoItem.findByCedulaRuc", dat, dat2);
    }

    public CatalogoItem getEstadoConstatacion(String cod, String cod2) {
        return findByNamedQuery1("CatalogoItem.getEstadoConstatacion", cod, cod2);
    }

    public CatalogoItem getCatalogoI(String catalogo, String codigo) {
        List<CatalogoItem> result = findByNamedQuery("CatalogoItem.findByC", new Object[]{catalogo, codigo});
        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    public List<CatalogoItem> getTipoCuenta(String catalogo, String codigo) {
        List<CatalogoItem> result = findByNamedQuery("CatalogoItem.findByC", new Object[]{catalogo, codigo});
        return result;
    }

    public CatalogoItem getCtaCorriente() {
        Query query = getEntityManager().createQuery("SELECT i FROM CatalogoItem i WHERE i.id = 171");
        CatalogoItem result = (CatalogoItem) query.getSingleResult();
        return result;
    }

    public CatalogoItem getEstadoRol(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo = ?1")
                    .setParameter(1, codigo);
            CatalogoItem result = (CatalogoItem) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> getPadreCatalogoItem(CatalogoItem catalogoPadre) {
        try {
            List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery("SELECT c FROM CatalogoItem c WHERE c.padre=:catalogoPadre ORDER BY c.orden ASC")
                    .setParameter("catalogoPadre", catalogoPadre)
                    .getResultList();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public CatalogoItem getPadreCatalogoItem(CatalogoItem catalogoPadre, String data) {
        try {
            CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo=:codigo ORDER BY c.orden ASC")
                    .setParameter("codigo", data).getResultStream().findFirst().orElse(null);
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<CatalogoItem> findCatalogoItems(String catalogo, CatalogoItem itemPadre) {
        try {
            List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo ct "
                    + "WHERE ct.codigo = :catalogo AND c.padre=:catalogoPadre ORDER BY c.orden ASC")
                    .setParameter("catalogo", catalogo)
                    .setParameter("catalogoPadre", itemPadre)
                    .getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Busqueda de catalogo por item padre " + e.getMessage());
            return null;
        }
    }

    public List<CatalogoItem> findCatalogoItemsEQOrden(String codigo, Short orden) {
        return em.createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.orden >= ?2 ORDER BY c.orden")
                .setParameter(1, codigo).setParameter(2, orden).getResultList();
    }

    public List<CatalogoItem> findCatalogoItemByCodigoPadre(String codigoCatalogoPade) {
        return em.createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 ORDER BY c.orden")
                .setParameter(1, codigoCatalogoPade).getResultList();
    }

    public List<CatalogoItem> findCatalogoItemsOrden(String codigo, Short orden) {
        return em.createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.orden > ?2 ORDER BY c.orden")
                .setParameter(1, codigo).setParameter(2, orden).getResultList();
    }

    public List<CatalogoItem> findCatItemByTexto(String texto) {
        return em.createQuery("SELECT c FROM CatalogoItem c where c.texto = ?1").setParameter(1, texto).getResultList();
    }

    public void deleteCatalogoItems(CatalogoItem c) {
        try {
            em.createQuery("DELETE FROM CatalogoItem c Where c.id = :id").setParameter("id", c.getId()).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CatalogoItem> findByCatalogo(String codigoCatalogo) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery(QUERY.FIND_CATALOGO)
                .setParameter(1, codigoCatalogo)
                .getResultList();
        return resultado;
    }

    public List<CatalogoItem> findByCatalogoDiario(String codigoCatalogo, String diario) {
        List<CatalogoItem> resultado = (List<CatalogoItem>) em.createQuery(QUERY.FIND_CATALOGO_DIARIO)
                .setParameter(1, codigoCatalogo)
                .setParameter(2, diario)
                .getResultList();
        return resultado;
    }

    public List<Short> getPeriodo() {
        List<Short> result = new ArrayList<>();
        List<CatalogoItem> items = findByCatalogo("periodo_sistema");
        if (!items.isEmpty()) {
            for (CatalogoItem item : items) {
                short add = Short.parseShort(item.getTexto());
                result.add(add);
            }
        }
        return result;
    }

    public CatalogoItem getDefaultItem(String catalogo) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            CatalogoItem cc = getCatalogoNombre(catalogo);
            if (cc == null) {
                return null;
            }
            return (cc);

        } catch (Exception e) {
            Logger.getLogger(CatalogoItemService.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatalogoItem getItemByCatalagoOrder(String prediobloquerevestpiso, Integer orden) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("orden", orden);
            paramt.put("catalogo", getCatalogoNombre(prediobloquerevestpiso));

            return em.find(CatalogoItem.class, paramt);

        } catch (Exception e) {
            Logger.getLogger(CatalogoItemService.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatalogoItem getCatalogoNombre(String argumento) {
        // HiberUtil.newTransaction();
        Map<String, Object> paramt = new HashMap<>();
        paramt.put("nombre", argumento);

        return em.find(CatalogoItem.class, paramt);
    }
}
