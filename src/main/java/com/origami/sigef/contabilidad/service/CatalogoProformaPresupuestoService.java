/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@Stateless
@javax.enterprise.context.Dependent
public class CatalogoProformaPresupuestoService extends AbstractService<CatalogoProformaPresupuesto> {

    @Inject
    private ManagerService service;
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoProformaPresupuestoService() {
        super(CatalogoProformaPresupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    /**
     * Consulta si existe un Catalogo Proforma Presupuesto
     *
     * @param anio
     * @param flujo
     * @return
     */
    public CatalogoProformaPresupuesto getCatalogoProformaPresupuesto(Short anio, boolean flujo) {
        try {
            List<CatalogoProformaPresupuesto> catalogo = (List<CatalogoProformaPresupuesto>) em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c WHERE c.periodo = :periodo AND c.tipoFlujo = :flujo AND c.estado = TRUE")
                    .setParameter("periodo", anio).setParameter("flujo", flujo).getResultList();

            if (!catalogo.isEmpty()) {
                return catalogo.get(0);
            }
            return null;
        } catch (Exception r) {
            return null;
        }

    }

    public CatalogoProformaPresupuesto getCatalogoProformaPresupuesto2(Short anio, boolean flujo) {

        CatalogoProformaPresupuesto catalogo = (CatalogoProformaPresupuesto) em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c WHERE c.periodo = :periodo AND c.tipoFlujo = :tipo AND c.estado = TRUE")
                .setParameter("periodo", anio).setParameter("tipo", flujo).getResultStream().findFirst().orElse(null);

        return catalogo;
    }

    public List<ProformaIngreso> getCatalogoPresupuesto(short anio, Short nivel, Boolean flujo) {
        Integer nivel_ = Integer.valueOf(nivel.toString());
        List<ProformaIngreso> result;
        if (nivel != null) {
            // String sql = "SELECT c FROM CatalogoPresupuesto c JOIN c.nivel n WHERE c.anio = :anio AND n.orden = :nivel AND c.estado = TRUE AND c.flujoIngreso = :flujo";
            Query query = getEntityManager().createQuery("SELECT p FROM ProformaIngreso p WHERE p.periodo=:anio AND p.item.confId.nivel=:nivel AND p.item.activo=TRUE AND p.item.ingreso=:flujo")
                    .setParameter("anio", anio)
                    .setParameter("nivel", nivel_)
                    .setParameter("flujo", flujo);
            result = query.getResultList();
            return result;
        }
        String sql = "SELECT c FROM ProformaIngreso c WHERE c.periodo = :anio AND c.activo = TRUE AND c.ingreso = :flujo";
        Query query = getEntityManager().createQuery(sql)
                .setParameter("anio", anio)
                .setParameter("flujo", flujo);
        result = query.getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> getProformaPresupuestoPlanificado(Short proforma) {
        String sql = "SELECT c FROM ProformaPresupuestoPlanificado c WHERE c.periodo = :periodo AND c.estado = TRUE";
        Query query = getEntityManager().createQuery(sql)
                .setParameter("periodo", proforma);
        List<ProformaPresupuestoPlanificado> result = query.getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> getProformaPresupuestoPlanificadoEqualsYear(CatalogoProformaPresupuesto proforma) {
        String sql = "SELECT c FROM ProformaPresupuestoPlanificado c WHERE c.periodo = :periodo AND c.generado = FALSE and c.estado = TRUE";
        Query query = getEntityManager().createQuery(sql)
                .setParameter("periodo", proforma.getPeriodoCatalogo().getAnio());
        List<ProformaPresupuestoPlanificado> result = query.getResultList();
        return result;
    }

    public List<CatalogoProformaPresupuesto> getFechaAprobada() {
        Query query = getEntityManager().createQuery("SELECT c FROM CatalogoProformaPresupuesto c where c.aprobado = true AND c.tipoFlujo = true");
        List<CatalogoProformaPresupuesto> result = query.getResultList();
        return result;
    }

    public BigDecimal getTotalPresupuesto(Short periodo) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(c.presupuestoInicial) FROM ProformaIngreso c INNER JOIN c.item i INNER JOIN i.confId n  WHERE c.periodo=:periodo AND i.ingreso =true AND i.movimiento=true and i.activo =true")
                .setParameter("periodo", periodo).getSingleResult();
        return valor;
    }

    public List<CatalogoProformaPresupuesto> getListaVerificacion(Short periodo, boolean aprobado) {

        List<CatalogoProformaPresupuesto> result = (List<CatalogoProformaPresupuesto>) em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c WHERE c.periodo= :periodo AND c.aprobado= :aprobado")
                .setParameter("periodo", periodo).setParameter("aprobado", aprobado).getResultList();
        return result;
    }

    public Boolean presupuestoEgresoAprobado(Short periodo) {
        List<CatalogoProformaPresupuesto> result = (List<CatalogoProformaPresupuesto>) em.createQuery("SELECT p FROM CatalogoProformaPresupuesto p WHERE p.estado =true AND p.periodo = ?1 AND p.aprobado = true AND p.tipoFlujo = false")
                .setParameter(1, periodo)
                .getResultList();
        return !result.isEmpty();
    }
    
    public Boolean validaCodigoPresupuestario () {
        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoPresupuestario is null and p.codigoReforma is null and p.codigoReformaTraspaso is null").getResultList();
        if (result.isEmpty()) {
            return true;
        }
        return false;
    }
}
