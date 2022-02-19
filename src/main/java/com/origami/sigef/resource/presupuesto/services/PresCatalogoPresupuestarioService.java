/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.services;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Model.GrupoPresupuestoModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.models.QUERY;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PresCatalogoPresupuestarioService extends AbstractService<PresCatalogoPresupuestario> {

    private static final Logger LOG = Logger.getLogger(PresCatalogoPresupuestarioService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PresCatalogoPresupuestarioService() {
        super(PresCatalogoPresupuestario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PresCatalogoPresupuestario> getPresupuestoIngreso(Boolean codigo, Short periodo) {
        List<PresCatalogoPresupuestario> resultado = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT c FROM CatalogoPresupuesto c WHERE c.flujoIngreso =:flujo AND c.anio=:anio")
                .setParameter("flujo", codigo).setParameter("anio", periodo)
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

    public BigDecimal getPresupuestoInicial(PresCatalogoPresupuestario catalogo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(presupuesto_inicial) FROM catalogo_presupuesto WHERE nivel = ?1 AND padre = (SELECT padre from catalogo_presupuesto where id= ?2)  AND anio = ?3 AND estado = TRUE AND flujo_ingreso= TRUE")
                .setParameter(1, catalogo.getConfId().getNivel())
                .setParameter(2, catalogo.getId())
                .setParameter(3, Utils.getAnio(new Date()).shortValue());
        val = (BigDecimal) query.getSingleResult();
        return val;
    }

    public BigDecimal getPresupuestoInicialReforma(PresCatalogoPresupuestario catalogo) {
        BigDecimal val;
        Query query = em.createNativeQuery("SELECT SUM(dt.codificado) FROM presupuesto.detalle_reforma_ingreso_suplemento dt  inner join catalogo_presupuesto cp \n"
                + "on dt.catalogo_presupuesto=cp.id\n"
                + "WHERE nivel = ?1 AND padre = \n"
                + "(SELECT padre from catalogo_presupuesto where id= ?2)  AND cp.anio = ?3 AND cp.estado = TRUE \n"
                + "AND cp.flujo_ingreso= TRUE")
                .setParameter(1, catalogo.getConfId().getNivel())
                .setParameter(2, catalogo.getId())
                .setParameter(3, Utils.getAnio(new Date()).shortValue());
        val = (BigDecimal) query.getSingleResult();
        return val;
    }

    public PresCatalogoPresupuestario getPadre(PresCatalogoPresupuestario catalogo) {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoPresupuesto c WHERE c.id = ?1 AND c.anio = ?2 AND c.estado = TRUE")
                    .setParameter(1, catalogo.getPadre().getId())
                    .setParameter(2, Utils.getAnio(new Date()).shortValue());
            PresCatalogoPresupuestario result = (PresCatalogoPresupuestario) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public DetalleReformaIngresoSuplemento getPadreReformas(PresCatalogoPresupuestario catalogo) {
        try {
            Query query = em.createQuery("SELECT dr FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.catalogoPresupuesto c WHERE c.id = ?1 AND c.anio = ?2 AND c.estado = TRUE")
                    .setParameter(1, catalogo.getPadre().getId())
                    .setParameter(2, Utils.getAnio(new Date()).shortValue());
            DetalleReformaIngresoSuplemento result = (DetalleReformaIngresoSuplemento) query.getSingleResult();

            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public PresFuenteFinanciamiento getFuente() {
        try {
            Query query = em.createQuery("SELECT f FROM PresFuenteFinanciamiento f JOIN f.tipoFuente ft WHERE ft.orden = ?1 AND f.estado = TRUE")
                    .setParameter(1, 1);
            PresFuenteFinanciamiento fuente = (PresFuenteFinanciamiento) query.getSingleResult();
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

    public List<PresCatalogoPresupuestario> asociacionPresupuestaria(boolean tipoFujo, short anio) {
        String sql = "SELECT c from CatalogoPresupuesto c WHERE c.flujoIngreso = ?1 AND c.anio = ?2 AND c.cuentaMovimiento = true AND c.estado = true ORDER BY c.codigo";
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(sql)
                .setParameter(1, tipoFujo)
                .setParameter(2, anio)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> cobradoDevengado(short anio) {
        String sql = "SELECT C from CatalogoPresupuesto C WHERE C.anio = ?1 AND C.nivel.orden = 2 AND C.estado = true ORDER BY C.codigo";
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(sql)
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> getListCatalogo(String codigo, short anio) {
        String sql = "SELECT c from CatalogoPresupuesto c WHERE c.codigo like'" + codigo + "%' and c.cuentaMovimiento = true and c.anio = ?1 AND c.estado = true ORDER BY c.codigo";
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(sql)
                .setParameter(1, anio)
                .getResultList();
        return result;
    }

    public Presupuesto getpartida(CuentaContable cuenta) {
        try {
            Query query = em.createQuery("SELECT p FROM Presupuesto p INNER JOIN p.item c WHERE c.id = ?1 AND c.estado = TRUE")
                    .setParameter(1, cuenta.getCredito().getId());
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

    public List<PresFuenteFinanciamiento> filtroFuenteFinanciamiento(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(e) FROM CatalogoPresupuesto p INNER JOIN p.fuente e WHERE p.anio= :anio ")
                .setParameter("anio", periodo);
        List<PresFuenteFinanciamiento> result = query.getResultList();
        return result;
    }

    public List<String> filtroCodigoC(Short periodo) {
        Query query = em.createQuery("SELECT DISTINCT(p.codigo) FROM CatalogoPresupuesto p WHERE p.anio= :anio ORDER BY p.codigo ")
                .setParameter("anio", periodo);
        List<String> result = query.getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> getListPresupuestoForArchivoPlano(Short periodo, Short niv) {
        Query query = em.createNativeQuery("SELECT DISTINCT cp.* FROM presupuesto p INNER JOIN presupuesto.pres_catalogo_presupuestario cp on p.item_new = cp.id \n"
                + "INNER JOIN conf.plan_cuentas n on cp.conf_id = n.id \n"
                + "WHERE  p.periodo = :periodo AND n.nivel = :nivel \n"
                + "UNION ALL\n"
                + "SELECT DISTINCT cp2.* FROM presupuesto p INNER JOIN presupuesto.pres_catalogo_presupuestario cp on p.item_new = cp.id \n"
                + "INNER JOIN conf.plan_cuentas n on cp.conf_id = n.id \n"
                + "INNER JOIN presupuesto.pres_catalogo_presupuestario cp2 ON cp2.id = cp.padre \n"
                + "INNER JOIN conf.plan_cuentas n2 on cp2.conf_id =n2.id \n"
                + "WHERE  p.periodo = :periodo AND n.nivel > :nivel AND n2.nivel = :nivel \n"
                + "ORDER BY codigo",PresCatalogoPresupuestario.class).setParameter("periodo", periodo).setParameter("nivel", niv.intValue());

        List<PresCatalogoPresupuestario> result = query.getResultList();
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

    public List<PresCatalogoPresupuestario> findTipoPresupuesto(boolean tipo) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.FIND_PRESUPUESTO_TIPO)
                .setParameter(1, tipo)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> findNivel(Integer nivel) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.FIND_NIVEL_PRESUPUESTO)
                .setParameter(1, nivel)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> findLikeCodigoPresupuesto(String codigo) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.FIND_CODIGO_LIKE_PRESUPUESTO)
                .setParameter(1, codigo + "%")
                .getResultList();
        return result;
    }

    public String getNextCode(PlanCuentas parametro, PresCatalogoPresupuestario parametro_2) {
        if (parametro_2 != null) {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_PRESUPUESTARIO_PADRE)
                    .setParameter(1, parametro)
                    .setParameter(2, parametro_2)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro);
        } else {
            String resultado = (String) em.createQuery(QUERY.NEXT_CODE_PRESUPUESTARIO)
                    .setParameter(1, parametro)
                    .getSingleResult();
            return determinarCodigo(resultado, parametro);
        }
    }

    private String determinarCodigo(String codigo, PlanCuentas parametro) {
        System.out.println("CODIGO: " + codigo);
        if (codigo != null) {
            if (parametro.getSeparador()) {//Si el codigo tiene separadores
                String code = "";
                if (codigo.contains(parametro.getCaracter())) {
                    String separador = parametro.getCaracter();
                    if (separador.equals(".")) {
                        separador = "\\" + separador;
                    }
                    String[] split = codigo.split(separador);
                    int pos = (split.length - 1);
                    String ultimo = split[pos];
                    int aux = Integer.parseInt(ultimo) + 1;
                    String concatenar;
                    if (parametro.getNumDigito() > 1) {
                        concatenar = Utils.completarCadenaConCeros(String.valueOf(aux), parametro.getNumDigito());
                    } else {
                        concatenar = String.valueOf(aux);
                    }
                    split[pos] = concatenar;
                    for (int x = 0; x < split.length; x++) {
                        code = code.concat(split[x]).concat(parametro.getCaracter());
                    }
                }
                return code;
            } else {//Si el codigo no tiene separadores
                int aux = Integer.parseInt(codigo) + 1;
                return String.valueOf(aux);
            }
        } else {
            return "1";
        }
    }

    public boolean findExiste(String codigo) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.EXITS_ITEM_PRESUPUESTARIO)
                .setParameter(1, codigo)
                .getResultList();
        return !result.isEmpty();
    }

    public List<PresCatalogoPresupuestario> getCatalogoPresupuestoByNivel(Integer nivel) {
        try {
            List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT p FROM PresCatalogoPresupuestario p WHERE p.confId.nivel = ?1 AND p.activo = TRUE ORDER BY p.codigo")
                    .setParameter(1, nivel).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<PresCatalogoPresupuestario> getHijosByPadre(PresCatalogoPresupuestario cat) {
        try {
            List<PresCatalogoPresupuestario> result
                    = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT p FROM PresCatalogoPresupuestario p where p.padre = ?1 AND p.activo = TRUE")
                            .setParameter(1, cat).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<PresCatalogoPresupuestario> filterCatalogoPresupuesto(String busqueda) {
        try {
            List<PresCatalogoPresupuestario> result
                    = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT p FROM PresCatalogoPresupuestario p WHERE p.activo = TRUE AND p.movimiento = TRUE AND UPPER(CONCAT(p.codigo,' ',p.descripcion)) LIKE UPPER(?1)")
                            .setParameter(1, busqueda).getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenRubrosLiquidacion> getFilterrubrosLiquidacion(String busqueda) {
        try {
            List<FinaRenRubrosLiquidacion> resul = (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT r FROM FinaRenRubrosLiquidacion r where r.estado = TRUE AND UPPER(r.descripcion) LIKE UPPER(?1)")
                    .setParameter(1, busqueda).getResultList();
            return resul;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenRubrosLiquidacion> getFinaRubrosConfCta(Boolean municipio) {
        try {
            String sql = "";
            List<FinaRenRubrosLiquidacion> resul = new ArrayList<>();
            if (municipio != null) {
                resul = (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT rb FROM FinaRenRubrosLiquidacion rb where rb.partida IS NULL AND rb.contCc IS NULL AND rb.contCp IS NULL AND rb.estado = TRUE AND rb.rubroDelMunicipio = ?1 ORDER BY rb.descripcion")
                        .setParameter(1, municipio)
                        .getResultList();
            } else {
                resul = (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT rb FROM FinaRenRubrosLiquidacion rb where rb.partida IS NULL AND rb.contCc IS NULL AND rb.contCp IS NULL AND rb.estado = TRUE ORDER BY rb.descripcion")
                        .getResultList();

            }
            return resul;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<PresCatalogoPresupuestario> presupuestoMovimiento() {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.ALL_CATALOGO_PRESUPUESTO)
                .getResultList();
        return result;
    }

    public List<PresCatalogoPresupuestario> presupuestoMovimiento(String codigo) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.ALL_CATALOGO_PRESUPUESTO_CODE)
                .setParameter(1, codigo.concat("%"))
                .getResultList();
        return result;
    }

    public boolean findHijos(PresCatalogoPresupuestario presCatalogoPresupuestario) {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery(QUERY.FIND_HIJOS_PRESUPUESTO)
                .setParameter(1, presCatalogoPresupuestario)
                .getResultList();
        return !result.isEmpty();
    }

    public List<PresCatalogoPresupuestario> getCatPresMovimiento() {
        List<PresCatalogoPresupuestario> result = (List<PresCatalogoPresupuestario>) em.createQuery("SELECT cp FROM PresCatalogoPresupuestario cp WHERE cp.activo=true AND cp.movimiento = true ORDER BY cp.codigo ASC")
                .getResultList();
        return result;
    }

}
