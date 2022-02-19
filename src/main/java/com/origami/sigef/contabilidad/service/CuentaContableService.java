/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CuentaContableService extends AbstractService<CuentaContable> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuentaContableService() {
        super(CuentaContable.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Short getMaxValueForChild(CuentaContable c, boolean titulo) {

        Short val = 0;

        if (titulo) {
            val = (Short) em.createQuery("SELECT MAX(c.titulo) FROM CuentaContable c WHERE c.periodo = :periodo AND c.estado = true")
                    .setParameter("periodo", c.getPeriodo())
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

    private String getQueryHQLMax(CuentaContable c) {
        String query = null;
        switch (c.getNivel().getOrden()) {
            case 1:
                return "SELECT MAX(c.grupo) FROM CuentaContable c WHERE c.titulo = :titulo AND c.periodo = :periodo AND c.estado = TRUE";
            case 2:
                return "SELECT MAX(c.subGrupo) FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.periodo = :periodo AND c.estado = TRUE";
            case 3:
                return "SELECT MAX(c.cuentaNivel1) FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.periodo = :periodo AND c.estado = TRUE";
            case 4:
                return "SELECT MAX(c.cuentaNivel2) FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.periodo = :periodo AND c.estado = TRUE";
            case 5:
                return "SELECT MAX(c.cuentaNivel3) FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.cuentaNivel2 = :nivel2 AND c.periodo = :periodo AND c.estado = TRUE";
            case 6:
                return "SELECT MAX(c.cuentaNivel4) FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.cuentaNivel2 =  :nivel2 AND c.cuentaNivel3 = :nivel3 AND c.periodo = :periodo AND c.estado = TRUE";

        }
        return query;
    }

    private String getQueryHQLExisteCuenta(CuentaContable c) {
        String query = null;
        switch (c.getNivel().getOrden()) {
            case 1:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.periodo = :periodo AND c.estado = true";
            case 2:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.periodo = :periodo AND c.estado = true";
            case 3:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.periodo = :periodo AND c.estado = true";
            case 4:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.periodo = :periodo AND c.estado = true";
            case 5:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.cuentaNivel2 = :nivel2 AND c.periodo = :periodo AND c.estado = true";
            case 6:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.cuentaNivel2 =  :nivel2 AND c.cuentaNivel3 = :nivel3 AND c.periodo = :periodo AND c.estado = true";
            case 7:
                return "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo AND c.grupo = :grupo AND c.subGrupo = :subgrupo AND c.cuentaNivel1 = :nivel1 AND c.cuentaNivel2 =  :nivel2 AND c.cuentaNivel3 = :nivel3 AND c.cuentaNivel4 = :nivel4 AND c.periodo = :periodo AND c.estado = true";

        }
        return query;
    }

    public List<CuentaContable> getHijosByPadre(CuentaContable padre) {
        return findByNamedQuery("CuentaContable.findByPadre", new Object[]{padre.getId()});
    }

    public List<CuentaContable> getHijosByPadreAndPeriodo(CuentaContable padre, Short periodo) {
        try {
            return em.createQuery("SELECT c FROM CuentaContable c WHERE c.padre = ?1 AND c.estado = TRUE AND c.periodo = ?2")
                    .setParameter(1, padre)
                    .setParameter(2, periodo)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CuentaContable existeCuenta(CuentaContable c) {
        String queryString = getQueryHQLExisteCuenta(c);

        Map<String, Object> params = getParameter(c);

        final Query query = em.createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<CuentaContable> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(CuentaContable c) {

        Map<String, Object> params = new HashMap<>();
        switch (c.getNivel().getOrden()) {
            case 1:
                params.put("titulo", c.getTitulo());
                params.put("periodo", c.getPeriodo());
                break;
            case 2:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("periodo", c.getPeriodo());
                break;
            case 3:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("subgrupo", c.getSubGrupo());
                params.put("periodo", c.getPeriodo());
                break;
            case 4:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("subgrupo", c.getSubGrupo());
                params.put("nivel1", c.getCuentaNivel1());
                params.put("periodo", c.getPeriodo());
                break;
            case 5:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("subgrupo", c.getSubGrupo());
                params.put("nivel1", c.getCuentaNivel1());
                params.put("nivel2", c.getCuentaNivel2());
                params.put("periodo", c.getPeriodo());
                break;
            case 6:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("subgrupo", c.getSubGrupo());
                params.put("nivel1", c.getCuentaNivel1());
                params.put("nivel2", c.getCuentaNivel2());
                params.put("nivel3", c.getCuentaNivel3());
                params.put("periodo", c.getPeriodo());
                break;
            case 7:
                params.put("titulo", c.getTitulo());
                params.put("grupo", c.getGrupo());
                params.put("subgrupo", c.getSubGrupo());
                params.put("nivel1", c.getCuentaNivel1());
                params.put("nivel2", c.getCuentaNivel2());
                params.put("nivel3", c.getCuentaNivel3());
                params.put("nivel4", c.getCuentaNivel4());
                params.put("periodo", c.getPeriodo());
                break;
        }

        return params;
    }

    public CuentaContable getPadre(CuentaContable catalogo, short anio) {
        try {
            Query query = em.createQuery("SELECT c FROM CuentaContable c WHERE c.id = ?1 AND c.periodo = ?2 AND c.estado = TRUE")
                    .setParameter(1, catalogo.getPadre().getId())
                    .setParameter(2, anio);
            CuentaContable result = (CuentaContable) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CuentaContable getCuentaContable(Long id) {
        try {
            Query query = em.createQuery("SELECT c FROM CuentaContable c WHERE c.id = ?1 AND c.estado = TRUE")
                    .setParameter(1, id);
            CuentaContable result = (CuentaContable) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getCuentasContables(short anio) {
        try {
            List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.credito IS NOT NULL AND c.debito IS NULL AND c.movimiento = TRUE AND c.periodo = ?1 AND c.estado = TRUE")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    //Trae todas las cuentas contables que sean de tipo movimiento y empiece por 213,113 ;se distingue por un año.
    public List<CuentaContable> getCuentasContablesXPeriodo(short anio) {
        try {
            List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.movimiento = TRUE AND c.periodo = ?1 AND c.estado = TRUE AND c.codigo LIKE '213%' OR c.codigo LIKE '113%'")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CuentaContable getContraCuenta(String cuentaId, Short anio) {
        try {
            Query query = em.createQuery("SELECT c FROM CuentaContable c JOIN c.cobradoDevengado cc WHERE cc.codigo = ?1 and c.estado = TRUE AND c.periodo = ?2")
                    .setParameter(1, cuentaId).setParameter(2, anio);
            CuentaContable result = (CuentaContable) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean existeAsociacion(CuentaContable cuenta) {
        try {
            List<CuentaContablecatalogoPresupuesto> result = (List<CuentaContablecatalogoPresupuesto>) em.createQuery("SELECT cc FROM CuentaContablecatalogoPresupuesto cc WHERE cc.cuentaContable.id = ?1 AND cc.catalogoPresupuesto IS NOT NULL")
                    .setParameter(1, cuenta.getId())
                    .getResultList();
            if (!result.isEmpty()) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    public List<CuentaContable> getcuentasPratrimoniales(short anio) {
        try {
            List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.nombre LIKE 'BANCO%' AND c.movimiento = false AND c.estado = TRUE AND c.periodo = ?1")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> findCtaContableByTipo(CuentaContable cta) {
        return this.findByNamedQuery("CuentaContable.findCtaContableByTipo", cta);
    }

    public List<CuentaContable> getHijosCtaBanco(String codigo, short anio) {
        try {
            String sql = "SELECT c FROM CuentaContable c WHERE c.codigo LIKE '" + codigo + "%' AND c.movimiento = true AND c.estado = TRUE AND c.periodo = ?1";
            List<CuentaContable> result = (List<CuentaContable>) em.createQuery(sql)
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getCuentasAnticipo(short anio) {
        try {
            List<CuentaContable> result = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE (c.codigo LIKE '1120101%' or c.codigo LIKE '1120102%' or c.codigo LIKE '1120103%') and c.periodo = ?1 and c.movimiento = true and c.estado = true")
                    .setParameter(1, anio)
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getCuentaMovimiento(Short periodo) {
        try {
            List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT DISTINCT c FROM BienesItem b "
                    + "INNER JOIN b.cuentaContable c "
                    + "WHERE c.codigo LIKE '14%' AND c.periodo =:periodo "
                    + "ORDER BY c.codigo ASC")
                    .setParameter("periodo", periodo)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public CuentaContable getCuentaXItem(CatalogoPresupuesto c) {
        try {
            Query query = em.createQuery("SELECT c FROM CuentaContable c WHERE c.debito = ?1 AND c.estado = TRUE")
                    .setParameter(1, c);
            CuentaContable cuenta = (CuentaContable) query.getSingleResult();
            return cuenta;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> getCuentasContablePeriodo(Short anio) {
        try {
            Query query = em.createQuery("SELECT c FROM CuentaContable c WHERE c.estado = TRUE AND c.periodo = ?1")
                    .setParameter(1, anio);
            List<CuentaContable> cuentas = (List<CuentaContable>) query.getResultList();
            return cuentas;
        } catch (Exception e) {
            return null;
        }
    }

    public List<CuentaContable> cuentaAnticipo(Short periodo) {
        try {
            List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.estado = TRUE and c.periodo = ?1 AND c.movimiento = true AND ( c.codigo like '11203%' or c.codigo like '11205%')")
                    .setParameter(1, periodo)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> cuentaDebito(Short periodo) {
        try {
            List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT c FROM CuentaContable c WHERE c.estado = TRUE and c.periodo = ?1 AND c.movimiento = true AND c.debito IS NOT NULL ORDER BY c.codigo ASC")
                    .setParameter(1, periodo)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CuentaContable> findAllAgrupacionAndGobiernoAndPeriodo(Boolean gobierno, Short periodo) {
        try {
            return em.createQuery("SELECT c FROM CuentaContable c WHERE c.gobierno = ?1 AND c.periodo = ?2 AND c.estado = true ORDER BY c.codigo ASC")
                    .setParameter(1, gobierno)
                    .setParameter(2, periodo)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CuentaContable findCuentaContableByCodigoAndPerido(String codigo, Short periodo) {
        try {
            return (CuentaContable) em.createQuery("SELECT c FROM CuentaContable c WHERE c.codigo = ?1 AND c.periodo = ?2 AND c.estado = TRUE")
                    .setParameter(1, codigo)
                    .setParameter(2, periodo)
                    .getSingleResult();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public List<CuentaContable> findAllCuentaContableByPeriodo(Short periodo) {
        try {
            return em.createQuery("SELECT c FROM CuentaContable c WHERE c.periodo = ?1 AND c.estado = true ORDER BY c.codigo")
                    .setParameter(1, periodo)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CuentaContable> getCuentaList(Short anio) {
        Query query = em.createQuery("SELECT c FROM CuentaContable c WHERE c.estado = TRUE AND c.periodo = ?1")
                .setParameter(1, anio);
        List<CuentaContable> lista = (List<CuentaContable>) query.getResultList();
        return lista;
    }

    public Short getUltimoNivel() {
        Query query = em.createQuery("SELECT MAX(n.orden) FROM CuentaContable cc INNER JOIN cc.nivel n");
        Short nivel = (Short) query.getSingleResult();
        return nivel;
    }

    public List<CuentaContablecatalogoPresupuesto> getListaPresupuestoAsociado(CuentaContable c) {
        List<CuentaContablecatalogoPresupuesto> result = (List<CuentaContablecatalogoPresupuesto>) em.createQuery("SELECT c FROM CuentaContablecatalogoPresupuesto c WHERE c.cuentaContable=:id")
                .setParameter("id", c).getResultList();
        return result;
    }

    public boolean getDiarioGeneral(CuentaContable c) {
        List<DetalleTransaccion> result = (List<DetalleTransaccion>) em.createQuery("SELECT dt.cuentaContable FROM DetalleTransaccion dt WHERE dt.cuentaContable=:id")
                .setParameter("id", c).getResultList();
        return !result.isEmpty();
    }

}
