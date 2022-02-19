/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.models.ListadoInventarioModel;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class DetalleItemService extends AbstractService<DetalleItem> {

    private static final Logger LOG = Logger.getLogger(DetalleItemService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleItemService() {
        super(DetalleItem.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public CatalogoItem getTipoActivoInv() {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo = ?2")
                    .setParameter(1, "tipo_activo")
                    .setParameter(2, "INV");
            CatalogoItem tipo = (CatalogoItem) query.getSingleResult();
            return tipo;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getNivelOrden(String catalogo, String codigo, Short orden) {

        Long result = findByNamedQuery1("GrupoNiveles.findByMaxOrden", new Object[]{catalogo, codigo, orden});
        if (result == null) {
            result = 1L;
        }
        return result;
    }

    public Long getNivelOrdenItem() {
        try {
            Query query = getEntityManager().createQuery("SELECT MAX(i.orden)+1 FROM DetalleItem i JOIN i.tipoActivo t JOIN t.catalogo c WHERE i.estado = true AND c.codigo = ?1 AND t.codigo = ?2")
                    .setParameter(1, "tipo_activo")
                    .setParameter(2, "INV");
            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getNivelOrdenSubgrupo(GrupoNiveles g) {
        try {
            Query query = getEntityManager().createQuery("SELECT MAX(i.orden)+1 FROM DetalleItem i WHERE i.asignarGrupo = :subgrupo ")
                    .setParameter("subgrupo", g);
            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public DetalleItem getItem(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT i FROM DetalleItem i WHERE i.estado= true AND i.codigoAgrupado = ?1 AND i.cuentaContable IS NULL ")
                    .setParameter(1, codigo);
            DetalleItem item = (DetalleItem) query.getSingleResult();
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

//    public List<CuentaContable> getCuentaContable(String codigo, Short periodo, boolean estado) {
//
//        Query query = getEntityManager().createQuery("SELECT p FROM CuentaContable p WHERE p.codigo= :codigo and p.periodo= :periodo and p.estado= :estado")
//                .setParameter("codigo", codigo)
//                .setParameter("periodo", periodo)
//                .setParameter("estado", estado);;
//        List<CuentaContable> result = query.getResultList();
//
//        return result;
//
//    }
    public ContCuentas getCuentaContable(String codigo, String codigoTipoGastos) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c WHERE c.estado= true  AND c.codigo = :codigo "
                    + "AND c.codigo like :tipoGasto "
                    + "AND c.movimiento=true")
                    .setParameter("codigo", codigo)
                    .setParameter("tipoGasto", codigoTipoGastos + "%");
            ContCuentas cta = (ContCuentas) query.getSingleResult();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas getCuentaContableGastos(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c WHERE c.estado= true  AND c.codigo = :codigo AND c.movimiento=true and c.codigo like '6%'")
                    .setParameter("codigo", codigo);
            ContCuentas cta = (ContCuentas) query.getSingleResult();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getPeriodo() {
        try {
            Query query = getEntityManager().createQuery("SELECT DISTINCT c.periodo FROM CuentaContable c WHERE c.estado= true");
            List<CuentaContable> cta = (List<CuentaContable>) query.getSingleResult();
//            if(cta.getPeriodo() != periodo){
//                JsfUtil.addWarningMessage("Info", "Ingrese correctamente el Período");
//            }
            return cta;
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<ContCuentas> getTipoGastos(String codigo1, List<String> codigo2, String codigo3) {
        Query query = getEntityManager().createNativeQuery("Select c.* from contabilidad.cont_cuentas c INNER JOIN conf.plan_cuentas t ON c.conf_id = t.id"
                + " WHERE c.estado= true AND c.codigo LIKE ?1 AND c.movimiento = ?2"
                + " AND t.codigo = 'plan_cuenta_contable' AND t.nivel = 3 "
                + " UNION"
                + " Select c.* from contabilidad.cont_cuentas c INNER JOIN conf.plan_cuentas t ON c.conf_id = t.id"
                + " WHERE c.estado= true AND c.codigo in (?3) AND c.movimiento = ?4"
                + " AND t.codigo = 'plan_cuenta_contable' AND t.nivel = 4 "
                + " UNION"
                + " Select c.* from contabilidad.cont_cuentas c INNER JOIN conf.plan_cuentas t ON c.conf_id = t.id"
                + " WHERE c.estado= true AND c.codigo LIKE ?5 AND c.movimiento = ?6"
                + " AND t.codigo = 'plan_cuenta_contable' AND t.nivel = 4 "
                + " ORDER BY codigo", ContCuentas.class)
                .setParameter(1, codigo1 + "%").setParameter(2, Boolean.FALSE)
                .setParameter(3, codigo2).setParameter(4, Boolean.FALSE)
                .setParameter(5, codigo3 + "%").setParameter(6, Boolean.FALSE);
        List<ContCuentas> cta = (List<ContCuentas>) query.getResultList();
        return cta;
    }

    public List<CuentaContable> getCuentaByTGastos(String codigo, Short periodo) {
        Query query = getEntityManager().createQuery("select c from CuentaContable c WHERE c.estado= true AND c.codigo = :codigo AND c.movimiento= :agrupacion AND c.periodo= :periodo")
                .setParameter("codigo", codigo)
                .setParameter("agrupacion", false)
                .setParameter("periodo", periodo);
        List<CuentaContable> cta = (List<CuentaContable>) query.getResultList();
        return cta;

    }

    public ContCuentas findByPadreOfTGastos(ContCuentas c) {
        try {
            Query query = getEntityManager().createQuery("SELECT c.padre FROM ContCuentas c WHERE c.id = ?1 AND c.estado = TRUE")
                    .setParameter(1, c.getId());
            ContCuentas cta = (ContCuentas) query.getSingleResult();
            return cta;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Short getCtaContable(DetalleItem i) {
        Short val;
        DetalleItem item;
        item = (DetalleItem) getEntityManager().createQuery("SELECT i FROM DetalleItem i WHERE i.id = ?1 AND i.estado = true AND i.cuentaContable = ?2")
                .setParameter(1, i.getId())
                .setParameter(2, i.getCuentaContable())
                .getSingleResult();
        if (item.getCuentaContable() == null) {
            val = 0;
        } else {
            val = 1;
        }
        return val;
    }

    public Boolean existenRegistros(DetalleItem item) {
        Boolean existe;
        DetalleItem detalle = null;
        try {
            detalle = (DetalleItem) getEntityManager().createQuery("SELECT DISTINCT d FROM InventarioItems i INNER JOIN i.inventario inv INNER JOIN i.detalleItem d WHERE inv.estado = true AND i.detalleItem = ?1")
                    .setParameter(1, item)
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

    public Boolean verificarCambiosTipoInventario(DetalleItem item) {
        DetalleItem detItem;
        Boolean cambios;
        try {
            detItem = (DetalleItem) getEntityManager().createQuery("SELECT i FROM DetalleItem i WHERE i.id = ?1 AND i.estado = true AND i.tipoGasto = ?2")
                    .setParameter(1, item.getId())
                    .setParameter(2, item.getTipoGasto())
                    .getSingleResult();
        } catch (NoResultException e) {
            detItem = null;
        }
        if (detItem != null) {
            cambios = Boolean.FALSE; //No existen cambios
        } else {
            cambios = Boolean.TRUE; //Existen cambios
        }
        return cambios;
    }

//    public List<DetalleItem> getItemsParameter(CuentaContable c, Short periodo) {
//        return findByNamedQuery("DetalleItem.getItemsParameter", c, periodo);
//    }
//    public List<DetalleItem> getItemsParameter1(String cuenta, Short periodo) {
//        return findByNamedQuery("DetalleItem.getItemsParameter1", cuenta, periodo);
//    }
    public List<DetalleItem> getItemsParameter2(Long asignar) {
        return findByNamedQuery("DetalleItem.getItemsParameter2", asignar);
    }

    public DetalleItem findByCodigoList(String cod) {
        return findByNamedQuery1("DetalleItem.findByCodigoList", cod);
        }

    public List<DetalleItem> getDetalleItemInventario(String busquedaItem) {
        try {
            Query query = getEntityManager().createQuery("SELECT i FROM DetalleItem i WHERE i.estado= true AND i.descripcion LIKE ?1")
                    .setParameter(1, busquedaItem);
            List<DetalleItem> items = (List<DetalleItem>) query.getResultList();
            return items;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ContCuentas> getCuentasContablesOfDetalleItems() {
        try {
            return em.createQuery("SELECT DISTINCT d.cuentaContable  FROM DetalleItem d WHERE d.estado = TRUE ORDER BY d.cuentaContable.codigo ASC").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleItem> getDetalleItemByGrupoNivel(GrupoNiveles grupo) {
        try {
            return em.createQuery("SELECT d FROM DetalleItem d WHERE d.estado = TRUE AND d.asignarGrupo = :grupo").setParameter("grupo", grupo).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleItem> getItemsByCuentaContableIsNull() {
        try {
            return em.createQuery("SELECT d FROM DetalleItem d WHERE d.estado = TRUE AND d.cuentaContable IS NULL ORDER BY d.codigo ASC")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetalleItem> getItemsByCuentaContableIsNOtNull() {
        try {
            return em.createQuery("SELECT d FROM DetalleItem d WHERE d.estado = TRUE AND d.cuentaContable IS NOT NULL ORDER BY d.codigo ASC")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetalleItem> getItemsByCuentaContable() {
        try {
            return em.createQuery("SELECT d FROM DetalleItem d WHERE d.estado = TRUE ORDER BY d.codigo ASC")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ListadoInventarioModel> getItemsModel(Long area, Long grupo, Long subgrupo, Long item) {
        return (List<ListadoInventarioModel>) em.createNativeQuery("select * from activos.fs_items_model_by_grupos( ?1 , ?2 , ?3 , ?4)", "listadoItemsInventarioMapping")
                .setParameter(1, area != null ? area : 0)
                .setParameter(2, grupo != null ? grupo : 0)
                .setParameter(3, subgrupo != null ? subgrupo : 0)
                .setParameter(4, item != null ? item : 0)
                .getResultList();
    }

    public List<ListadoInventarioModel> getItemsContablesModel(Long cuenta) {
        return (List<ListadoInventarioModel>) em.createNativeQuery("select * from activos.fs_items_model_by_cuenta( ?1)", "listadoItemsInventarioMapping")
                .setParameter(1, cuenta != null ? cuenta : 0)
                .getResultList();
    }
}
