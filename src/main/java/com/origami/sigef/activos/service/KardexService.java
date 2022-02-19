/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetallaKardex;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.Kardex;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class KardexService extends AbstractService<Kardex> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public KardexService() {
        super(Kardex.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DetalleItem> getListaProductos(String est) {
        List<DetalleItem> result = (List<DetalleItem>) em.createQuery("SELECT DISTINCT(de) FROM InventarioItems i INNER JOIN "
                + "i.inventario inven INNER JOIN i.detalleItem de where inven.estadoAdicional = ?1 ").setParameter(1, est)
                .getResultList();
        return result;
    }

    public List<InventarioItems> getlistaInventario(DetalleItem i, String est, Date fechaDesde, Date fechaHasta) {

        List<BigInteger> listaid = (List<BigInteger>) em.createNativeQuery("SELECT io.id from activos.inventario i\n"
                + "INNER JOIN activos.inventario_items io ON io.inventario = i.id\n"
                + "where  io.detalle_item=?1 and i.estado_adicional=?2 and CAST(i.fecha_movimiento AS DATE) between ?3 and ?4 order by io.id,CAST(i.fecha_movimiento AS DATE) asc")
                .setParameter(1, i.getId()).setParameter(2, est).setParameter(3, fechaDesde).setParameter(4, fechaHasta).getResultList();

        List<InventarioItems> result = new ArrayList<>();

        listaid.stream().map((lis) -> {
            InventarioItems inv = new InventarioItems();
            inv = (InventarioItems) em.createQuery("SELECT i FROM InventarioItems i where i.id= :id").setParameter("id", lis.longValue()).getSingleResult();
            return inv;
        }).forEachOrdered((inv) -> {
            result.add(inv);
        });

        return result;

    }

    public BigDecimal getPrecioAntiguo(Long i) {
        BigDecimal result = (BigDecimal) em.createNativeQuery("SELECT b.precio FROM activos.inventario aa INNER JOIN activos.inventario_items  b\n"
                + "ON b.inventario = aa.id WHERE b.inventario=?1")
                .setParameter(1, i).getResultStream().findFirst().orElse(null);

        if (result == null) {
            result = BigDecimal.ZERO;

        }

        return result;
    }

    public InventarioItems getubicacionIrems(Long i) {

        BigInteger id = (BigInteger) em.createNativeQuery("SELECT aa.id FROM activos.inventario_items aa WHERE aa.detalle_item= ?1 limit 1 ")
                .setParameter(1, i).getSingleResult();

        InventarioItems result = (InventarioItems) em.createQuery("SELECT i FROM InventarioItems i where i.id= :id").setParameter("id", id.longValue()).getSingleResult();

        return result;
    }

    public List<InventarioItems> getlistaInventarioCalculoAnteior(Date fechaDesde, DetalleItem i, String est) {

        List<BigInteger> listaid = (List<BigInteger>) em.createNativeQuery("SELECT io.id from activos.inventario i\n"
                + "INNER JOIN activos.inventario_items io ON io.inventario = i.id\n"
                + "where CAST(i.fecha_movimiento AS DATE)<?1 and io.detalle_item= ?2 and i.estado_adicional = ?3 \n"
                + "order by io.id,CAST(i.fecha_movimiento AS DATE) asc")
                .setParameter(1, fechaDesde)
                .setParameter(2, i.getId())
                .setParameter(3, est).getResultList();

        List<InventarioItems> result = new ArrayList<>();

        listaid.stream().map((lis) -> {
            InventarioItems inv = new InventarioItems();
            inv = (InventarioItems) em.createQuery("SELECT i FROM InventarioItems i where i.id= :id").setParameter("id", lis.longValue()).getSingleResult();
            return inv;
        }).forEachOrdered((inv) -> {
            result.add(inv);
        });

        return result;

    }

    public List<BigInteger> getListaInventariosGeneralId(Date fechaDesde, Date fechaHasta, String est) {
        List<BigInteger> listaid = (List<BigInteger>) em.createNativeQuery("SELECT Distinct(det.id) from activos.inventario i\n"
                + "                INNER JOIN activos.inventario_items io\n"
                + "				ON io.inventario = i.id\n"
                + "				INNER join activos.detalle_item det \n"
                + "				ON det.id = io.detalle_item\n"
                + "                where CAST(i.fecha_movimiento AS DATE) between ?1 and ?2 and io.id is not null "
                + "                             and i.estado_adicional = ?3")
                .setParameter(1, (Date) fechaDesde)
                .setParameter(2, (Date) fechaHasta)
                .setParameter(3, est).getResultList();

        return listaid;

    }

    public List<InventarioItems> getlistainbentariosPorItems(Long id, Date fechaDesde, Date fechaHasta, String est) {

        List<InventarioItems> result = new ArrayList<>();
        List<BigInteger> lis = (List<BigInteger>) em.createNativeQuery("SELECT io.id from activos.inventario i\n"
                + "                INNER JOIN activos.inventario_items io\n"
                + "				ON io.inventario = i.id\n"
                + "				INNER join activos.detalle_item det \n"
                + "				ON det.id = io.detalle_item\n"
                + "                where CAST(i.fecha_movimiento AS DATE) between ?1 and ?2 and det.id=?3  and i.estado_adicional = ?4"
                + "                              order by io.id,CAST(i.fecha_movimiento AS DATE) asc")
                .setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, id).setParameter(4, est).getResultList();

        for (BigInteger li : lis) {

            InventarioItems i = new InventarioItems();
            i = (InventarioItems) em.createQuery("SELECT i FROM InventarioItems i WHERE i.id= :id ")
                    .setParameter("id", li.longValue()).getResultStream().findFirst().orElse(null);

            result.add(i);

        }

        return result;

    }

    public List<InventarioItems> getlistaInventarioCalculoAnteior(Date fechaDesde, Long i, String est) {

        List<BigInteger> listaid = (List<BigInteger>) em.createNativeQuery("SELECT io.id from activos.inventario i\n"
                + "INNER JOIN activos.inventario_items io ON io.inventario = i.id\n"
                + "where CAST(i.fecha_movimiento AS DATE)<?1 and io.detalle_item= ?2 and i.estado_adicional = ?3 \n"
                + "order by io.id,CAST(i.fecha_movimiento AS DATE) asc")
                .setParameter(1, fechaDesde)
                .setParameter(2, i)
                .setParameter(3, est).getResultList();

        List<InventarioItems> result = new ArrayList<>();

        listaid.stream().map((lis) -> {
            InventarioItems inv = new InventarioItems();
            inv = (InventarioItems) em.createQuery("SELECT i FROM InventarioItems i where i.id= :id").setParameter("id", lis.longValue()).getSingleResult();
            return inv;
        }).forEachOrdered((inv) -> {
            result.add(inv);
        });

        return result;

    }

    public List<DetallaKardex> getlistaKardexRealizados(Date fechaDesde, Date fechaHasta, DetalleItem d) {
        List<DetallaKardex> result;
        if (d == null) {
            result = (List<DetallaKardex>) em.createQuery("SELECT dk FROM DetallaKardex dk INNER JOIN dk.kardex k where k.fechaDesde= :desde AND k.fechaHasta=:hasta AND k.itemsProducto IS NULL")
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta).getResultList();

        } else {
            result = (List<DetallaKardex>) em.createQuery("SELECT dk FROM DetallaKardex dk INNER JOIN dk.kardex k INNER JOIN k.itemsProducto i where i.id= :id AND k.fechaDesde= :desde AND k.fechaHasta=:hasta")
                    .setParameter("id", d.getId())
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta).getResultList();

        }
        return result;
    }

    public Kardex getlistaVerificacion(Date fechaDesde, Date fechaHasta) {
        try {
            BigInteger idlong = (BigInteger) em.createNativeQuery("select k.id from activos.kardex k WHERE cast(k.fecha_desde as Date)=?1 and cast(k.fecha_hasta as Date)=?2 and k.items_producto is null")
                    .setParameter(1, fechaDesde)
                    .setParameter(2, fechaHasta).getResultStream().findFirst().orElse(null);
            if (idlong == null) {
                return null;
            }

            Kardex result = (Kardex) em.createQuery("SELECT k FROM Kardex k WHERE k.id= :id").setParameter("id", idlong.longValue()).getResultStream().findFirst().orElse(null);
            return result;
        } catch (NoResultException e) {
            return null;
        }

    }

    public Kardex getlistaVerificacion(DetalleItem d, Date fechaDesde, Date fechaHasta) {
        try {
            BigInteger idlong = (BigInteger) em.createNativeQuery("select k.id from activos.kardex k INNER JOIN activos.inventario_items i\n"
                    + "on k.items_producto=i.id WHERE i.id=?1 and cast(k.fecha_desde as Date)=?2 and cast(k.fecha_hasta as Date)=?3")
                    .setParameter(1, d.getId())
                    .setParameter(2, fechaDesde)
                    .setParameter(3, fechaHasta).getResultStream().findFirst().orElse(null);
            if (idlong == null) {
                return null;
            }

            Kardex result = (Kardex) em.createQuery("SELECT k FROM Kardex k WHERE k.id= :id").setParameter("id", idlong.longValue()).getResultStream().findFirst().orElse(null);
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteDetallekardex(Kardex k) {
        Query query = em.createQuery("DELETE FROM DetallaKardex d WHERE d.kardex=:kardex")
                .setParameter("kardex", k);
        int result = query.executeUpdate();
        Query query2 = em.createQuery("DELETE FROM Kardex d WHERE d.id=:kardex")
                .setParameter("kardex", k.getId());
        int result2 = query2.executeUpdate();
    }

}
