/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.PlanAnualDTO;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class PlanAnualProgramaProyectoService extends AbstractService<PlanAnualProgramaProyecto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PlanAnualProgramaProyectoService() {
        super(PlanAnualProgramaProyecto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PlanAnualProgramaProyecto> MostarNombresProyectos(PlanAnualPoliticaPublica p) {
        List<PlanAnualProgramaProyecto> resultado = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT m FROM PlanAnualProgramaProyecto m INNER JOIN m.planAnual cc WHERE cc.id= :id ")
                .setParameter("id", p.getId())
                .getResultList();
        return resultado;
    }
//
//    public List<PlanAnualDTO> actualizarTablaPorSeccion(Short periodo) {
//        Query query = em.createQuery("SELECT b.id As planAnualPoliticaPublica, a.id As planAnualProgramaProyecto "
//                + "FROM plan_anual_programa_proyecto a "
//                + "full JOIN plan_anual_politica_publica b "
//                + "ON a.plan_anual = b.id "
//                + "a.periodo=:periodo OR b.periodo=:periodo")
//                .setParameter("periodo", periodo);
//        List<Object[]> result = query.getResultList();
//        return null;
//    }

    public List<PlanAnualDTO> actualizarTablaPorSeccion(Short periodo) throws IOException {

        String sqlString = "SELECT cast(row_to_json(b) AS text) As planAnualPoliticaPublica , a.id As planAnualProgramaProyecto, "
                + "c.id As actividadOperativa, d.id As Producto "
                + "FROM plan_anual_programa_proyecto a "
                + "FULL OUTER JOIN plan_anual_politica_publica b "
                + "ON a.plan_anual = b.id "
                + "FULL OUTER JOIN actividad_operativa c "
                + "ON c.plan_programa_proyecto = a.id "
                + "FULL OUTER JOIN producto d "
                + "ON d.actividad_operativa = c.id "
                + "WHERE a.periodo =?1 or b.periodo =?2 or c.periodo = ?3 or d.periodo =?4";
        // + "ORDER BY a.id ASC";
        List<Object[]> result = em.createNativeQuery(sqlString, Object[].class).setParameter(1, periodo).setParameter(2, periodo).setParameter(3, periodo).setParameter(4, periodo).getResultList();
        if (Utils.isNotEmpty(result)) {
            List<PlanAnualDTO> listPlanAnual = new ArrayList<>();
            int i = 0;
            JsonUtil json = new JsonUtil();

            for (Object[] data : result) {
                PlanAnualDTO planDTO = new PlanAnualDTO();
//                PGobject obj = (PGobject) data[0];
//                if (obj != null) {
//                    obj.setType("jsonb");
//                }
                planDTO.setPlanAnualPoliticaPublica((PlanAnualPoliticaPublica) json.fromJson((String) data[0], PlanAnualPoliticaPublica.class, new String[]{"politicaNacional", "planLocal"}));
                planDTO.setPlanAnualProgramaProyecto(null);
                planDTO.setActividadOperativa(null);
                planDTO.setProducto(null);
                listPlanAnual.add(planDTO);
                i++;
            }
            return listPlanAnual;
        }
        return null;
    }

    public List<PlanAnualProgramaProyecto> planProceso(UnidadAdministrativa u, Short periodo, CatalogoItem item) {
        List<PlanAnualProgramaProyecto> result = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT DISTINCT pro FROM Producto p INNER JOIN p.actividadOperativa a JOIN a.planProgramaProyecto pro WHERE p.actividadOperativa = a AND a.planProgramaProyecto = pro AND pro.estado = TRUE AND a.unidadResponsable = ?1 AND pro.periodo = ?2 AND pro.estadoProceso = ?3 AND pro.codigoReforma IS NULL AND pro.codigoReformaTraspaso IS NULL")
                .setParameter(1, u).setParameter(2, periodo).setParameter(3, item).getResultList();
        return result;
    }

    public List<PappProceso> planProcesoSgte(CatalogoItem item, BigInteger num, Short periodo) {
      
        List<PappProceso> result = (List<PappProceso>) em.createQuery("SELECT pp FROM PappProceso pp WHERE pp.estadoProceso = ?1 AND pp.numeroTramite = ?2 AND extract(year from pp.fechaTramite) = ?3")
                .setParameter(1, item)
                .setParameter(2, num)
                .setParameter(3, periodo.intValue())
                .getResultList();
        return result;
    }

    public List<ActividadOperativa> planProcesoInicio(UnidadAdministrativa u, Short periodo, CatalogoItem item) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT DISTINCT a FROM Producto p INNER JOIN p.actividadOperativa a  WHERE p.actividadOperativa = a  AND a.unidadResponsable = ?1 AND a.periodo = ?2 AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter(1, u).setParameter(2, periodo).getResultList();
        return result;
    }

    public UnidadAdministrativa getUnidadResponsable(PlanAnualProgramaProyecto plan) {
        try {
            Query query
                    = em.createQuery("SELECT DISTINCT u FROM ActividadOperativa a INNER JOIN a.unidadResponsable u WHERE a.planProgramaProyecto = ?1")
                            .setParameter(1, plan);
            UnidadAdministrativa result = (UnidadAdministrativa) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Producto> productoPlanPryecto(ActividadOperativa plan, CatalogoItem item) {
//        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a JOIN a.planProgramaProyecto pro WHERE p.actividadOperativa = a AND a.planProgramaProyecto = pro AND pro = ?1 AND pro.periodo = ?2 AND NOT EXISTS(SELECT pp FROM ProductoProceso pp WHERE pp.producto = p)")
        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a  WHERE p.actividadOperativa = a  AND a = ?1 AND a.periodo = ?2 AND p.montoReformada > 0")
                .setParameter(1, plan).setParameter(2, plan.getPeriodo()).getResultList();
        return result;
    }

    public List<ProductoProceso> productoPropceso(CatalogoItem item, BigInteger num, ActividadOperativa plan) {
        List<ProductoProceso> result = (List<ProductoProceso>) em.createQuery("SELECT p FROM ProductoProceso p INNER JOIN p.producto pro INNER JOIN pro.actividadOperativa a where p.estadoProceso = ?1 AND p.numetoTramite = ?2 AND a= ?3")
                .setParameter(1, item).setParameter(2, num).setParameter(3, plan).getResultList();
        return result;
    }

}
