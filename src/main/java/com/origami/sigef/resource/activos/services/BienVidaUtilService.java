/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.services;

import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.activos.entities.BienVidaUtil;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sandra Arroba
 */
@Stateless
@javax.enterprise.context.Dependent
public class BienVidaUtilService extends AbstractService<BienVidaUtil> {

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public BienVidaUtilService() {
        super(BienVidaUtil.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

//    public List<ContCuentas> getClasificacionByTipoBien(Short orden, String codigoBLD, String codigoNot1, String codigoNot2) {
//        Query query = getEntityManager().createQuery("SELECT c FROM CuentaContable c JOIN c.nivel n JOIN n.tipo t WHERE c.estado = :estado AND c.codigo LIKE :codigoBLD "
//                + "AND c.codigo NOT LIKE :var1 AND c.codigo NOT LIKE :var2 AND (c.codigo not LIKE :codigoNot1 and c.codigo not LIKE :codigoNot2)\n"
//                + "AND c.movimiento = :movimiento AND t.codigo = 'CC' AND n.orden = :orden ORDER BY c.codigo")
//                .setParameter("estado", Boolean.TRUE)
//                .setParameter("codigoBLD", codigoBLD + "%")
//                .setParameter("var1", "___98%") //fijo
//                .setParameter("var2", "___99%") //fijo
//                .setParameter("codigoNot1", "__" + codigoNot1 + "%") //6 fijo para BLD
//                .setParameter("codigoNot2", "__" + codigoNot2 + "%") //9 fijo para BLD
//                .setParameter("movimiento", Boolean.FALSE)
//                .setParameter("orden", orden);
//        List<ContCuentas> cta = query.getResultList();
//        return cta;
//    }
    public List<ContCuentas> getClasificacionByTipoBien(Integer orden, String codigoBLD, String codigoNot1, String codigoNot2) {
        Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c JOIN c.confId n  WHERE c.estado = :estado "
                + "AND c.codigo LIKE :codigoBLD AND c.codigo NOT LIKE :var1 AND c.codigo NOT LIKE :var2 "
                + "AND (c.codigo not LIKE :codigoNot1 and c.codigo not LIKE :codigoNot2) AND c.movimiento = :movimiento "
                + "AND n.codigo = 'plan_cuenta_contable' AND n.nivel = :orden ORDER BY c.codigo")
                .setParameter("estado", Boolean.TRUE)
                .setParameter("codigoBLD", codigoBLD + "%")
                .setParameter("var1", "___98%") //fijo
                .setParameter("var2", "___99%") //fijo
                .setParameter("codigoNot1", "__" + codigoNot1 + "%") //6 fijo para BLD
                .setParameter("codigoNot2", "__" + codigoNot2 + "%") //9 fijo para BLD
                .setParameter("movimiento", Boolean.FALSE)
                .setParameter("orden", orden);
        List<ContCuentas> cta = query.getResultList();
        return cta;
    }

    public ContCuentas getCuentaContable(String codigo, String codigoClasificacion) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c WHERE c.estado= true  AND c.codigo = :codigo "
                    + "AND c.codigo like :tipoGasto AND c.movimiento=true")
                    .setParameter("codigo", codigo)
                    .setParameter("tipoGasto", codigoClasificacion + "%");
            ContCuentas cta = (ContCuentas) query.getSingleResult();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ContCuentas getPadreOfCta(ContCuentas cuenta) {
        try {
            Query query = getEntityManager().createQuery("SELECT c.padre FROM ContCuentas c WHERE c.estado= true  AND c.id = :id "
                    + "AND c.movimiento=false")
                    .setParameter("id", cuenta.getId());
            ContCuentas cta = (ContCuentas) query.getResultStream().findFirst().orElse(null);
            return cta;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BienVidaUtil> getVidaUtilByCuenta(ContCuentas cuenta) {
        try {
            Query query = getEntityManager().createQuery("select v from BienVidaUtil v inner join v.cuentaContable c where c.codigo = :codigo AND v.estado = TRUE")
                    .setParameter("codigo", cuenta.getCodigo());
            List<BienVidaUtil> cta = (List<BienVidaUtil>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BienVidaUtil> verificarListaVidaUtil(String descripcion, ContCuentas cuenta) {
        Query query = getEntityManager().createQuery("Select v from BienVidaUtil v inner join v.cuentaContable c where v.descripcion = :descripcion AND c.codigo = :codigo AND v.estado = TRUE")
                .setParameter("descripcion", descripcion)
                .setParameter("codigo", cuenta.getCodigo());
        List<BienVidaUtil> cta = (List<BienVidaUtil>) query.getResultList();
        return cta;
    }

    public List<ContCuentas> getCuentaContableDeActivosDepreciables(String codigo) {
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM ContCuentas c WHERE c.estado = true "
                    + "AND c.codigo like :codigo AND c.movimiento=true ORDER BY c.codigo ASC")
                    .setParameter("codigo", codigo + "%");
            List<ContCuentas> cta = (List<ContCuentas>) query.getResultList();
            return cta;
        } catch (NoResultException e) {
            return null;
        }
    }

}
