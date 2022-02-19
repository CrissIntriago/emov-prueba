/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProgramacionIngresoEgresoService extends AbstractService<ProgramacionIngresoEgreso> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProgramacionIngresoEgresoService() {
        super(ProgramacionIngresoEgreso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigDecimal getTotalCatalogoProforma(short anio, Boolean tipoFlujo) {
        BigDecimal val;
        Query query = em.createNativeQuery("select cp.total from catalogo_proforma_presupuesto cp where cp.tipo_flujo = ?1 and cp.periodo = ?2")
                .setParameter(1, tipoFlujo)
                .setParameter(2, anio);
        val = (BigDecimal) query.getSingleResult();
        return val;
    }

    public BigDecimal getTotalItemEgreso(short anio) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT sum(pi.monto_actividad) FROM programacion_ingreso_egreso pi where pi.periodo=?1")
                .setParameter(1, anio);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalPIE(short anio, boolean flujo) {
        BigDecimal val;
        Query query = em.createNativeQuery("select sum(pi.total_anio) from programacion_ingreso_egreso pi where pi.periodo = ?1 and pi.tipo_flujo = ?2")
                .setParameter(1, anio)
                .setParameter(2, flujo);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getSumaCuatrimestre1(Short anio, Boolean flujo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(p.monto_cuatrimestre1) FROM programacion_ingreso_egreso p WHERE p.periodo = ?1 AND p.tipo_flujo = ?2")
                .setParameter(1, anio)
                .setParameter(2, flujo);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSumaMesses(Short anio, Boolean flujo, int mes) {
        List<BigDecimal> valor = new ArrayList<>();

        switch (mes) {
            case 1:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.enero),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 2:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.febrero),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 3:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.marzo),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 4:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.abril),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 5:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.mayo),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 6:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.junio),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 7:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.julio),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 8:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.agosto),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 9:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.septiembre),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 10:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.octubre),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 11:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.noviembre),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;
            case 12:
                valor = (List<BigDecimal>) em.createQuery("SELECT COALESCE(SUM(p.diciembre),0)  FROM ProgramacionIngresoEgreso p WHERE p.periodo=:periodo and p.tipoFlujo=:flujo")
                        .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();
                break;

        }

        if (valor != null && !valor.isEmpty()) {
            return valor.get(0);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSumaCuatrimestre2(Short anio, Boolean flujo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(p.monto_cuatrimestre2) FROM programacion_ingreso_egreso p WHERE p.periodo = ?1 AND p.tipo_flujo = ?2")
                .setParameter(1, anio)
                .setParameter(2, flujo);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSumaCuatrimestre3(Short anio, Boolean flujo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(p.monto_cuatrimestre3) FROM programacion_ingreso_egreso p WHERE p.periodo = ?1 AND p.tipo_flujo = ?2")
                .setParameter(1, anio)
                .setParameter(2, flujo);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSumaEnero(String mes, short anio) {
        BigDecimal val;
        Query query = em.createNativeQuery("select sum(pi." + mes + ") from programacion_ingreso_egreso pi where pi.periodo = ?2 and pi.tipo_flujo=true")
                .setParameter(2, anio);
        val = (BigDecimal) query.getSingleResult();
        if (val != null) {
            return val;
        }
        return BigDecimal.ZERO;
    }

    public List<CatalogoProformaPresupuesto> getProformaIngresoEgreso(Boolean flujo) {
        Query query = em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c WHERE c.estado = TRUE AND c.tipoFlujo = ?1 AND c.aprobado = TRUE")
                .setParameter(1, flujo);
        List<CatalogoProformaPresupuesto> result = query.getResultList();
        return result;
    }

    public List<Producto> getProductoWithAct(String codigo, Short periodo) {
        Query query = em.createQuery("SELECT p FROM Producto p WHERE p.periodo = ?1 AND p.codigoPresupuestario = ?2 AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter(1, periodo)
                .setParameter(2, codigo);
        List<Producto> result = query.getResultList();
        return result;
    }

    public CatalogoItem getestado(Short periodo) {
        CatalogoItem result = (CatalogoItem) em.createQuery("SELECT e FROM ProgramacionIngresoEgreso p INNER JOIN p.estadoA e WHERE p.periodo= :periodo").setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return result;
    }

    public void updateEstadoProgramacion(OpcionBusqueda p, CatalogoItem c) {
        Query query = em.createQuery("UPDATE ProgramacionIngresoEgreso p SET p.estadoA= :estado WHERE p.periodo= :periodo ")
                .setParameter("estado", c).setParameter("periodo", p.getAnio());
        int result = query.executeUpdate();
    }
}
