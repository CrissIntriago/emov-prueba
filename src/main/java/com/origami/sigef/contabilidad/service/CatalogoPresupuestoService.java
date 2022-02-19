/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Model.GrupoPresupuestoModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
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
public class CatalogoPresupuestoService extends AbstractService<CatalogoPresupuesto> {

    /**
     *
     */
    @Inject
    private ContCuentaPartidaService contCuentaPartidaService;

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatalogoPresupuestoService() {
        super(CatalogoPresupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Short getMaxValueForChild(CatalogoPresupuesto c, boolean titulo) {

        Short val = 0;

        if (titulo) {
            val = (Short) em.createQuery("SELECT MAX(c.titulo) FROM CatalogoPresupuesto c WHERE c.anio = :anio")
                    .setParameter("anio", c.getAnio())
                    .getSingleResult();
        } else {

            String queryString = getQueryHQLMax(c);
            Map<String, Object> params = getParameter(c);

            final Query query = em.createQuery(queryString);

            params.entrySet().forEach((param) -> {
                query.setParameter(param.getKey(), param.getValue());
            });

            val = (Short) query.getSingleResult();
        }

        if (val != null) {
            val++;
        } else {
            val = 1;
        }
        return val;
    }

    private String getQueryHQLMax(CatalogoPresupuesto c) {
        String query = null;
        switch (c.getNivel().getOrden()) {
            case 1:
                return "SELECT MAX(c.naturaleza) FROM CatalogoPresupuesto c WHERE c.titulo= :titulo  AND c.anio= :anio AND c.estado = TRUE";

            case 2:
                return "SELECT MAX(c.subGrupo) FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.naturaleza = :naturaleza AND c.anio = :anio AND c.estado = TRUE";
            case 3:
                return "SELECT MAX(c.rubro) FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.naturaleza = :naturaleza AND c.subGrupo = :subGrupo AND c.anio = :anio "
                        + "AND c.estado = TRUE";

        }
        return query;
    }

    private String getQueryHQLExisteCuenta(CatalogoPresupuesto c) {
        if (c.getNivel() == null) {
            return null;
        }
        String query = null;
        switch (c.getNivel().getOrden()) {
            case 1:
                return "SELECT c FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.anio = :anio AND c.estado = true";
            case 2:
                return "SELECT c FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.naturaleza = :naturaleza AND c.anio = :anio AND c.estado = true";
            case 3:
                return "SELECT c FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.naturaleza = :naturaleza AND c.subGrupo = :subGrupo AND c.anio = :anio AND c.estado = true";
            case 4:
                return "SELECT c FROM CatalogoPresupuesto c WHERE c.titulo = :titulo AND c.naturaleza = :naturaleza AND c.subGrupo = :subGrupo AND c.rubro = :rubro AND c.anio = :anio AND c.estado = true";

        }
        return query;
    }

    public List<CatalogoPresupuesto> getHijosByPadre(CatalogoPresupuesto padre) {
        return findByNamedQuery("CatalogoPresupuesto.findByPadre", new Object[]{padre.getId()});
    }

    public CatalogoPresupuesto existeCuenta(CatalogoPresupuesto c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = em.createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<CatalogoPresupuesto> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(CatalogoPresupuesto c) {

        Map<String, Object> params = new HashMap<>();
        switch (c.getNivel().getOrden()) {
            case 1:
                params.put("titulo", c.getTitulo());
                params.put("anio", c.getAnio());
                break;
            case 2:
                params.put("titulo", c.getTitulo());
                params.put("naturaleza", c.getNaturaleza());
                params.put("anio", c.getAnio());
                break;
            case 3:
                params.put("titulo", c.getTitulo());
                params.put("naturaleza", c.getNaturaleza());
                params.put("subGrupo", c.getSubGrupo());
                params.put("anio", c.getAnio());
                break;
            case 4:
                params.put("titulo", c.getTitulo());
                params.put("naturaleza", c.getNaturaleza());
                params.put("subGrupo", c.getSubGrupo());
                params.put("rubro", c.getRubro());
                params.put("anio", c.getAnio());
                break;

        }
        return params;
    }

    public List<ProformaIngreso> getPresupuestoIngreso(Boolean codigo, Short periodo) {
        List<ProformaIngreso> resultado = (List<ProformaIngreso>) em.createQuery("SELECT c FROM ProformaIngreso c where  c.periodo=:anio")
                .setParameter("anio", periodo)
                .getResultList();
        return resultado;
    }

    public BigDecimal getPresupuestoTotal(CatalogoProformaPresupuesto proforma, Boolean flujo) {
        BigDecimal val;

        val = (BigDecimal) em.createQuery("SELECT SUM(c.presupuestoCodificado) FROM CatalogoPresupuesto c WHERE c.padre IS NULL AND c.anio = :anio AND c.flujoIngreso = :flujo and c.estado=true")
                .setParameter("anio", proforma.getPeriodoCatalogo().getAnio())
                .setParameter("flujo", flujo)
                .getSingleResult();

        return val;
    }

    public BigDecimal getPresupuestoInicial(CatalogoPresupuesto catalogo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(presupuesto_inicial) FROM catalogo_presupuesto WHERE nivel = ?1 AND padre = (SELECT padre from catalogo_presupuesto where id= ?2)  AND anio = ?3 AND estado = TRUE AND flujo_ingreso= TRUE")
                .setParameter(1, catalogo.getNivel().getOrden())
                .setParameter(2, catalogo.getId())
                .setParameter(3, catalogo.getAnio());
        val = (BigDecimal) query.getSingleResult();
        return val;
    }

    public BigDecimal getPresupuestoInicialReforma(CatalogoPresupuesto catalogo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(dt.codificado) FROM presupuesto.detalle_reforma_ingreso_suplemento dt  inner join catalogo_presupuesto cp \n"
                + "on dt.catalogo_presupuesto=cp.id\n"
                + "WHERE nivel = ?1 AND padre = \n"
                + "(SELECT padre from catalogo_presupuesto where id= ?2)  AND cp.anio = ?3 AND cp.estado = TRUE \n"
                + "AND cp.flujo_ingreso= TRUE")
                .setParameter(1, catalogo.getNivel().getOrden())
                .setParameter(2, catalogo.getId())
                .setParameter(3, catalogo.getAnio());
        val = (BigDecimal) query.getSingleResult();
        return val;
    }

    public CatalogoPresupuesto getPadre(CatalogoPresupuesto catalogo) {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoPresupuesto c WHERE c.id = ?1 AND c.anio = ?2 AND c.estado = TRUE")
                    .setParameter(1, catalogo.getPadre().getId())
                    .setParameter(2, catalogo.getAnio());
            CatalogoPresupuesto result = (CatalogoPresupuesto) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public DetalleReformaIngresoSuplemento getPadreReformas(CatalogoPresupuesto catalogo) {
        try {
            Query query = em.createQuery("SELECT dr FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.catalogoPresupuesto c WHERE c.id = ?1 AND c.anio = ?2 AND c.estado = TRUE")
                    .setParameter(1, catalogo.getPadre().getId())
                    .setParameter(2, catalogo.getAnio());
            DetalleReformaIngresoSuplemento result = (DetalleReformaIngresoSuplemento) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public FuenteFinanciamiento getFuente() {
        try {
            Query query = em.createQuery("SELECT f FROM FuenteFinanciamiento f JOIN f.tipoFuente ft WHERE ft.orden = ?1 AND f.estado = TRUE")
                    .setParameter(1, 1);
            FuenteFinanciamiento fuente = (FuenteFinanciamiento) query.getSingleResult();
            return fuente;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getAsociacionPresupuestaria(short anio) {
        List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT cc FROM CuentaContable cc WHERE cc.periodo = ?1 AND cc.movimiento = true AND cc.estado = true")
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public List<CatalogoPresupuesto> asociacionPresupuestaria(boolean tipoFujo, short anio) {
        String sql = "SELECT c from CatalogoPresupuesto c WHERE c.flujoIngreso = ?1 AND c.anio = ?2 AND c.cuentaMovimiento = true AND c.estado = true ORDER BY c.codigo";
        List<CatalogoPresupuesto> result = (List<CatalogoPresupuesto>) em.createQuery(sql)
                .setParameter(1, tipoFujo)
                .setParameter(2, anio)
                .getResultList();
        return result;
    }

    public List<CatalogoPresupuesto> cobradoDevengado(short anio) {
        String sql = "SELECT C from CatalogoPresupuesto C WHERE C.anio = ?1 AND C.nivel.orden = 2 AND C.estado = true ORDER BY C.codigo";
        List<CatalogoPresupuesto> result = (List<CatalogoPresupuesto>) em.createQuery(sql)
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public List<CatalogoPresupuesto> getListCatalogo(String codigo, short anio) {
        String sql = "SELECT c from CatalogoPresupuesto c WHERE c.codigo like'" + codigo + "%' and c.cuentaMovimiento = true and c.anio = ?1 AND c.estado = true ORDER BY c.codigo";
        List<CatalogoPresupuesto> result = (List<CatalogoPresupuesto>) em.createQuery(sql)
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public Presupuesto getpartida(ContCuentas cuenta) {
        try {
            System.out.println("cont cuenta >> " + cuenta);
            List<PresCatalogoPresupuestario> pre = contCuentaPartidaService.getPresCatalogoPresupuestarioByContCuenta(cuenta);

            Query query = em.createQuery("SELECT p FROM Presupuesto p INNER JOIN p.itemNew c WHERE c.id = ?1 AND c.estado = TRUE")
                    .setParameter(1, pre.get(0).getId());
            Presupuesto result = (Presupuesto) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> filtroClasificacionCatPre(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM CatalogoPresupuesto p INNER JOIN p.clasificacion e WHERE p.anio= :anio")
                .setParameter("anio", periodo);
        List<CatalogoItem> result = query.getResultList();
        return result;
    }

    public List<Nivel> filtroNivelOrden(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM CatalogoPresupuesto p INNER JOIN p.nivel e WHERE p.anio= :anio  ORDER BY e.orden ")
                .setParameter("anio", periodo);
        List<Nivel> result = query.getResultList();
        return result;
    }

    public List<FuenteFinanciamiento> filtroFuenteFinanciamiento(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM CatalogoPresupuesto p INNER JOIN p.fuente e WHERE p.anio= :anio ")
                .setParameter("anio", periodo);
        List<FuenteFinanciamiento> result = query.getResultList();
        return result;
    }

    public List<String> filtroCodigoC(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(p.codigo) FROM CatalogoPresupuesto p WHERE p.anio= :anio ORDER BY p.codigo ")
                .setParameter("anio", periodo);
        List<String> result = query.getResultList();
        return result;
    }

    public List<CatalogoPresupuesto> getListPresupuestoForArchivoPlano(Short periodo, Short niv) {
        Query query = em.createQuery("SELECT DISTINCT cp FROM Presupuesto p INNER JOIN p.item cp INNER JOIN cp.nivel n "
                + " WHERE cp.anio = :periodo AND n.orden = :nivel ORDER BY cp.codigo").setParameter("periodo", periodo).setParameter("nivel", niv);

        List<CatalogoPresupuesto> result = query.getResultList();
        return result;
    }

    public List<GrupoPresupuestoModel> getListadoGruposPresupuestio(Short periodo) {
        return em.createNativeQuery("SELECT ctp.titulo,ctp.naturaleza, concat(ctp.titulo,ctp.naturaleza) AS grupo  FROM public.presupuesto p\n"
                + "INNER JOIN catalogo_presupuesto ctp ON p.item = ctp.id\n"
                + "WHERE p.periodo =?1 \n"
                + "GROUP by 1,2,3\n"
                + "ORDER BY 3 ASC;", "GrupoPresupuestarioMapping")
                .setParameter(1, periodo)
                .getResultList();
    }
}
