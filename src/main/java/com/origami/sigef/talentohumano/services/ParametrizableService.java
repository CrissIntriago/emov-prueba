/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ParametrizableService extends AbstractService<ParametrosTalentoHumano> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ParametrizableService() {
        super(ParametrosTalentoHumano.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //trae valores parametrizados por su origen y clasificacion
    public List<ParametrosTalentoHumano> findByValores(String cod1, String cod2) {
        try {
            Query query = em.createQuery("SELECT p FROM  ParametrosTalentoHumano p INNER JOIN p.clasificacion cla INNER JOIN p.origen org WHERE cla.codigo =:codigo1 AND org.codigo=:codigo2 AND p.estado=true")
                    .setParameter("codigo1", cod1).setParameter("codigo2", cod2);
            List<ParametrosTalentoHumano> parametros = query.getResultList();
            return parametros;
        } catch (NoResultException e) {
            return null;
        }

    }

    //trae valores parametrizados por su origen y clasificacion
    public List<ParametrosTalentoHumano> findByValoresAnexoAprobados(String cod1, short anio) {
        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo pda JOIN pda.distributivoAnexo da JOIN da.valorParametrizado p JOIN p.origen org WHERE pda.periodo = ?2 AND org.codigo=?1 AND pda.codigoReforma IS NULL AND pda.codigoReformaTraspaso  IS NULL")
                    .setParameter(1, cod1)
                    .setParameter(2, anio);
            List<ParametrosTalentoHumano> parametros = query.getResultList();
            return parametros;
        } catch (NoResultException e) {
            return null;
        }

    }

    // trae valores parametrizados por su clasificacion
    public List<ParametrosTalentoHumano> findByOnlyClasificacion(String cod1) {
        try {
            Query query = em.createQuery("SELECT p FROM  ParametrosTalentoHumano p INNER JOIN p.clasificacion cla WHERE cla.codigo =:codigo1 AND p.estado=true")
                    .setParameter("codigo1", cod1);
            List<ParametrosTalentoHumano> parametros = query.getResultList();
            return parametros;
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<ParametrosTalentoHumano> valoreRolesXclasificacion(TipoRol rol, CatalogoItem tipoRubro) {
        try {
            Query query = em.createNativeQuery("select \n"
                    + "ph.*\n"
                    + "from talento_humano.rol_rubro rr\n"
                    + "INNER JOIN talento_humano.liquidacion_rol lr on rr.liquidacion_rol = lr.id\n"
                    + "INNER JOIN talento_humano.roles_de_pago rp ON rp.id = lr.rol_pago\n"
                    + "INNER JOIN talento_humano.tipo_rol tr ON tr.id = lr.tipo_rol\n"
                    + "INNER JOIN catalogo_item cm ON cm.id = tr.mes\n"
                    + "INNER JOIN catalogo_item ct ON ct.id = tr.tipo_rol\n"
                    + "INNER JOIn public.catalogo_item ce on ce.id = tr.estado_aprobacion\n"
                    + "INNER JOIN talento_humano.valores_roles vr ON vr.id = rr.valor_asignacion\n"
                    + "INNER JOIN public.cuenta_contable cct ON cct.id = vr.cuenta_contable\n"
                    + "INNER JOIN conf.parametros_talento_humano ph ON ph.id = vr.valor_parametrizable\n"
                    + "INNER JOIN public.catalogo_item ci on ci.id = ph.valores\n"
                    + "INNER JOIN public.catalogo_item cc on cc.id = ph.clasificacion\n"
                    + "where tr.id=?1 and cc.id = ?2 and lr.estado = TRUE and rr.estado = true\n"
                    + "GROUP BY ph.id\n"
                    + "HAVING COUNT(ph.*) >= 1", ParametrosTalentoHumano.class)
                    .setParameter(1, rol.getId()).setParameter(2, tipoRubro.getId());
            List<ParametrosTalentoHumano> parametros = query.getResultList();
            return parametros;
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<ParametrosTalentoHumano> valoreRolesXplanillaIESS() {
        try {
            Query query = em.createNativeQuery("select * from conf.parametros_talento_humano ph\n"
                    + "inner join catalogo_item ci on ph.valores = ci.id\n"
                    + "where ph.estado = true and ci.codigo in ('PRES_HIP','PRES_QUI')", ParametrosTalentoHumano.class);
            List<ParametrosTalentoHumano> parametros = query.getResultList();
            return parametros;
        } catch (NoResultException e) {
            return null;
        }

    }

    public ParametrosTalentoHumano valorParametroTipo(String cod1) {
        try {
            String hql;
            if (cod1.equals("FR") || cod1.equals("APO_INDIV_CODIGO") || cod1.equals("APOR_IND_LOSEP") || cod1.equals("APOR_IND_LOEP")) {
                hql = "SELECT p FROM  ParametrosTalentoHumano p INNER JOIN p.tipo t WHERE t.codigo =:codigo1 AND p.estado=true AND p.medicionPorcentaje = TRUE";
            } else {
                hql = "SELECT DISTINCT p FROM  ParametrosTalentoHumano p INNER JOIN p.tipo t WHERE t.codigo =:codigo1 AND p.estado=true AND p.medicionPorcentaje = FALSE";
            }
            Query query = em.createQuery(hql)
                    .setParameter("codigo1", cod1);
            List<ParametrosTalentoHumano> parametros = (List<ParametrosTalentoHumano>) query.getResultList();
            return parametros.get(0);
        } catch (NoResultException e) {
            return null;
        }

    }

    public ParametrosTalentoHumano valorParametros(String codigo, Boolean porcentaje) {
        try {
            Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p INNER JOIN p.tipo t WHERE t.codigo = ?1 AND p.estado = TRUE AND p.medicionPorcentaje = ?2")
                    .setParameter(1, codigo).setParameter(2, porcentaje);
            ParametrosTalentoHumano result = (ParametrosTalentoHumano) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }        
    }

    public ParametrosTalentoHumano getParametroHoras(Long id) {
        Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p WHERE  p.id = ?1")
                .setParameter(1, id);
        ParametrosTalentoHumano result = (ParametrosTalentoHumano) query.getSingleResult();
        return result;
    }

    public List<ParametrosTalentoHumano> getlistaSBU(String cod) {
        Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p INNER JOIN p.tipo tp WHERE p.vigenciaHasta is null AND tp.codigo = ?1 AND p.estado = TRUE")
                .setParameter(1, cod);
        List<ParametrosTalentoHumano> result = (List<ParametrosTalentoHumano>) query.getResultList();
        return result;
    }

    public ParametrosTalentoHumano getParametroFechaMax() {
        try {
            Query query = em.createQuery("SELECT p FROM ParametrosTalentoHumano p WHERE p.vigenciaHasta = (SELECT MAX(pm.vigenciaHasta) FROM ParametrosTalentoHumano pm) AND p.estado = TRUE");
            ParametrosTalentoHumano result = (ParametrosTalentoHumano) query.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        }
    }

}
