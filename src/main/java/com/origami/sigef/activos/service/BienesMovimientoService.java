/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
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
public class BienesMovimientoService extends AbstractService<BienesMovimiento> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienesMovimientoService() {
        super(BienesMovimiento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Long getNivelBienesMov() {
        try {
            Query query = getEntityManager().createQuery("SELECT MAX(b.orden)+1 FROM BienesMovimiento b WHERE b.estado = :estado")
                    .setParameter("estado", Boolean.TRUE);

            Long item = (Long) query.getSingleResult();
            if (item == null) {
                item = 1L;
            }
            return item;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<ContCuentas> getClasificacionByTipoBien(Long orden, String codigoBLD, String codigoBSC, String codigoNot1, String codigoNot2, String codigoNot3, String codigoNot4) {
        Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c JOIN c.confId t WHERE c.estado = :estado\n"
                + "AND (c.codigo LIKE :codigoBLD OR c.codigo LIKE :codigoBSC)\n"
                + "AND c.codigo NOT LIKE :var1 AND c.codigo NOT LIKE :var2\n"
                + "AND (c.codigo not LIKE :codigoNot1 and c.codigo not LIKE :codigoNot2)\n"
                + "AND (c.codigo NOT LIKE :codigoNot3 and c.codigo not like :codigoNot4)\n"
                + "AND c.movimiento = :movimiento AND t.codigo = :codigoNivel \n"
                + "AND t.nivel = :orden ORDER BY c.codigo")
                .setParameter("estado", Boolean.TRUE)
                .setParameter("codigoBLD", codigoBLD + "%")
                .setParameter("codigoBSC", codigoBSC + "%")
                .setParameter("var1", "___98%") //fijo
                .setParameter("var2", "___99%") //fijo
                .setParameter("codigoNot1", "__" + codigoNot1 + "%") //6 fijo para BLD, puede ser null cuando es BSC
                .setParameter("codigoNot2", "__" + codigoNot2 + "%") //9 fijo para BLD, puede ser null cuando es BSC
                .setParameter("codigoNot3", "__" + codigoNot3 + "%") //variable, puede ser null
                .setParameter("codigoNot4", "__" + codigoNot4 + "%") //variable, puede ser null
                .setParameter("movimiento", Boolean.FALSE)
                .setParameter("codigoNivel", "plan_cuenta_contable")
                .setParameter("orden", orden.intValue());
        List<ContCuentas> cta = query.getResultList();
        return cta;
    }

    public List<CuentaContable> getClasificacionPrueba1(Long orden, String codigoBSC, String nombre, String codigoNot1) {
        Query query = getEntityManager().createQuery("SELECT c FROM CuentaContable c JOIN c.nivel n JOIN n.tipo t WHERE c.estado = :estado\n"
                + "AND c.codigo LIKE :codigoBSC\n"
                + "AND c.nombre LIKE :nombre AND c.codigo NOT LIKE :codigoNot1\n"
                + "AND c.movimiento = :movimiento AND t.codigo = :codigoNivel \n"
                + "AND n.orden = :orden ORDER BY c.codigo")
                .setParameter("estado", Boolean.TRUE)
                .setParameter("codigoBSC", codigoBSC + "%")
                .setParameter("nombre", nombre + "%")
                .setParameter("movimiento", Boolean.FALSE)
                .setParameter("codigoNivel", "CC")
                .setParameter("orden", orden.shortValue());
        List<CuentaContable> cta = query.getResultList();
        return cta;
    }

    public List<ContCuentas> getFilterOfCuenta(String codigo, Long orden, String nombre) {
        if (codigo.equals("911")) {
            Query query = getEntityManager().createQuery("SELECT DISTINCT ct FROM ContCuentas c JOIN c.confId t JOIN c.padre ct WHERE c.estado = :estado AND ct.codigo LIKE :codigo AND c.descripcion LIKE :nombre AND "
                    + "c.movimiento = :movimiento AND t.codigo = :codigoNivel")
                    .setParameter("estado", Boolean.TRUE)
                    .setParameter("codigo", codigo + "%")
                    .setParameter("nombre", nombre + "%")
                    .setParameter("movimiento", Boolean.TRUE)
                    .setParameter("codigoNivel", "plan_cuenta_contable");
            List<ContCuentas> cta = query.getResultList();
            return cta;
        } else {
            Query query = getEntityManager().createQuery("SELECT DISTINCT c FROM ContCuentas c JOIN c.confId t WHERE c.estado = :estado AND c.codigo LIKE :codigo AND "
                    + "c.movimiento = :movimiento AND t.codigo = 'plan_cuenta_contable' and c.codigo not like :codigoNot1 and c.codigo not like :codigoNot2 AND t.nivel = :orden ")
                    .setParameter("estado", Boolean.TRUE)
                    .setParameter("codigo", codigo + "%")
                    .setParameter("movimiento", Boolean.FALSE)
                    .setParameter("codigoNot1", codigo + "98%")
                    .setParameter("codigoNot2", codigo + "99%")
                    .setParameter("orden", orden.intValue());
            List<ContCuentas> cta = query.getResultList();
            return cta;
        }
    }

    public BienesMovimiento findMovimientoBienesByTramite(Long tramite) {
        try {
            BienesMovimiento result = (BienesMovimiento) getEntityManager().createQuery("SELECT m FROM BienesMovimiento m WHERE m.numeroTramite = ?1")
                    .setParameter(1, tramite)
                    .getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public BienesItem findBienesItemByMovimiento(BienesMovimiento mov) {
        try {
            BienesItem result = (BienesItem) getEntityManager().createQuery("SELECT b FROM BienesItem b WHERE b.bienesMovimiento = ?1")
                    .setParameter(1, mov)
                    .getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienesMovimiento> getTramitelistaBienes(long num) {
        List<BienesMovimiento> result = (List<BienesMovimiento>) getEntityManager().createQuery("SELECT b FROM BienesMovimiento b where b.numeroTramite= :num").setParameter("num", num).getResultList();
        return result;
    }

    public void getAcualiizarEstadosSql(String estado, long num) {

        Query query = getEntityManager().createQuery("UPDATE BienesMovimiento i set i.estadoAdicional= :estado WHERE i.numeroTramite= :num").setParameter("estado", estado).setParameter("num", num);

        int rowCount = query.executeUpdate();
        System.out.println("estado " + rowCount);
    }

    public List<BienesItem> getListadetalle(BienesMovimiento b) {

        List<BienesItem> result = (List<BienesItem>) getEntityManager().createQuery("SELECT b FROM BienesItem b INNER JOIN b.bienesMovimiento bi where bi.id= :id").setParameter("id", b.getId()).getResultList();
        return result;
    }

    public List<BienesItem> getlistaBienesByTramite(long num) {
        List<BienesItem> result = (List<BienesItem>) getEntityManager().createQuery("SELECT b FROM BienesItem b INNER JOIN b.bienesMovimiento bi where bi.numeroTramite= :num").setParameter("num", num).getResultList();
        return result;
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("update activos.bienes_movimiento set contabilizado= false where transaccion_contable = ?1")
                .setParameter(1, diarioGeneral.getId().intValue());
        return query.executeUpdate();
    }
}
