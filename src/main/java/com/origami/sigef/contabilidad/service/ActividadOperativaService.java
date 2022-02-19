/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ActividadOperativaService extends AbstractService<ActividadOperativa> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ActividadOperativaService() {
        super(ActividadOperativa.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ActividadOperativa> getListActividades(short periodo, List<String> item) {

        Query query = em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.periodo = :periodo AND  a.estado = TRUE AND a.estadoPapp.codigo in(:items) AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).setParameter("items", item);
        List<ActividadOperativa> result = query.getResultList();
        return result;
    }

    public List<PlanLocalProgramaProyecto> getReformaPlanLocalProgramaProyecto(Short periodo, CatalogoItem c, boolean estado2) {
        //p.periodo= :periodo AND 
        List<PlanLocalProgramaProyecto> result = (List<PlanLocalProgramaProyecto>) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p INNER JOIN p.estadoPapp ep where ep.id= :estado AND p.estado= :estado2 AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).getResultList();
        return result;
    }

    public List<PlanAnualPoliticaPublica> getReformaPlanPoliticaPublica(Short periodo, CatalogoItem c, boolean estado2) {
        //p.periodo= :periodo AND 
        List<PlanAnualPoliticaPublica> result = (List<PlanAnualPoliticaPublica>) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p INNER JOIN p.estadoPapp ep where ep.id= :estado AND p.estado= :estado2 AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).getResultList();
        return result;
    }

    public List<PlanAnualProgramaProyecto> getReformaPlanAnulaProgramaProyecto(Short periodo, CatalogoItem c, boolean estado2) {
        //p.periodo= :periodo AND 
        List<PlanAnualProgramaProyecto> result = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p INNER JOIN p.estadoPapp ep where ep.id= :estado AND p.estado= :estado2 AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).getResultList();
        return result;
    }

    public List<ActividadOperativa> getReformaActividadOperativa(Short periodo, CatalogoItem c, boolean estado2) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a INNER JOIN a.estadoPapp ep where a.periodo= :periodo AND ep.id= :estado AND a.estado= :estado2 AND a.codigoReforma is null AND a.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).getResultList();
        return result;
    }

    public List<Producto> getReformaProducto(Short periodo, CatalogoItem c, boolean estado2) {
        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p INNER JOIN p.estadoPapp ep where p.periodo= :periodo AND ep.id= :estado AND p.estado= :estado2 AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).getResultList();
        return result;
    }

    public PlanLocalProgramaProyecto showPlanLocal(BigInteger reforma, BigInteger id) {
        PlanLocalProgramaProyecto result = (PlanLocalProgramaProyecto) em.createQuery("SELECT p from PlanLocalProgramaProyecto p WHERE p.codigoReforma=:reforma AND p.numeroOrdenId =:id AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public PlanAnualPoliticaPublica showPlanPoliticaPublica(BigInteger reforma, BigInteger id) {
        PlanAnualPoliticaPublica result = (PlanAnualPoliticaPublica) em.createQuery("SELECT p from PlanAnualPoliticaPublica p WHERE p.codigoReforma=:reforma AND p.numeroOrdenId= :id AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public PlanAnualProgramaProyecto showPlanAnualPrograma(BigInteger reforma, BigInteger id) {
        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p from PlanAnualProgramaProyecto p WHERE p.codigoReforma= :reforma AND p.numeroOrdenId= :id AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public ActividadOperativa showAcividad(BigInteger reforma, BigInteger id) {
        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT p from ActividadOperativa p WHERE p.codigoReforma= :reforma AND p.numeroOrdenId= :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;

    }

    /*Sandra*/
    public List<ActividadOperativa> getReformaActividadOperativaByUnidad(Short periodo, CatalogoItem c, boolean estado2, Long unidad) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a INNER JOIN a.estadoPapp ep INNER JOIN a.unidadResponsable u WHERE a.periodo= :periodo AND ep.id= :estado AND a.estado= :estado2 AND u.id = :unidad")
                .setParameter("periodo", periodo).setParameter("estado", c.getId()).setParameter("estado2", estado2).setParameter("unidad", unidad).getResultList();
        return result;
    }

    public PlanLocalProgramaProyecto showPlanLocalByTraspaso(BigInteger reforma, BigInteger id) {
        PlanLocalProgramaProyecto result = (PlanLocalProgramaProyecto) em.createQuery("SELECT p from PlanLocalProgramaProyecto p WHERE p.codigoReformaTraspaso=:reforma AND p.numeroOrdenId =:id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public PlanAnualPoliticaPublica showPlanPoliticaPublicaByTraspaso(BigInteger reforma, BigInteger id) {
        PlanAnualPoliticaPublica result = (PlanAnualPoliticaPublica) em.createQuery("SELECT p from PlanAnualPoliticaPublica p WHERE p.codigoReformaTraspaso=:reforma AND p.numeroOrdenId= :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public PlanAnualProgramaProyecto showPlanAnualProgramaByTraspaso(BigInteger reforma, BigInteger id) {
        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p from PlanAnualProgramaProyecto p WHERE p.codigoReformaTraspaso= :reforma AND p.numeroOrdenId= :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;
    }

    public ActividadOperativa showActividadByTraspaso(BigInteger reforma, BigInteger id) {
        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT p from ActividadOperativa p WHERE p.codigoReformaTraspaso= :reforma AND p.numeroOrdenId= :id ")
                .setParameter("reforma", reforma).setParameter("id", id).getSingleResult();
        return result;

    }

    public void aprobarTodo(Short periodo, CatalogoItem c) {

        Query query1 = em.createQuery("UPDATE Producto p SET p.estadoPapp= :estado1 WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("estado1", c).setParameter("periodo", periodo);
        int result1 = query1.executeUpdate();

        Query query2 = em.createQuery("UPDATE ActividadOperativa a SET a.estadoPapp= :estado2 WHERE a.periodo= :periodo AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("estado2", c).setParameter("periodo", periodo);
        int result2 = query2.executeUpdate();

        Query query3 = em.createQuery("UPDATE PlanAnualProgramaProyecto p SET p.estadoPapp= :estado3 WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado3", c).setParameter("periodo", periodo);
        int result3 = query3.executeUpdate();

        Query query4 = em.createQuery("UPDATE PlanAnualPoliticaPublica p SET p.estadoPapp= :estado4 WHERE p.periodo= :periodo AND  p.codigoReforma  IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado4", c).setParameter("periodo", periodo);
        int result4 = query4.executeUpdate();

        Query query5 = em.createQuery("UPDATE PlanLocalProgramaProyecto p SET p.estadoPapp= :estado5 WHERE p.periodo= :periodo AND  p.codigoReforma  IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado5", c).setParameter("periodo", periodo);
        int result5 = query5.executeUpdate();

    }

    public void aprobarDistributivosAndPD(Short periodo, CatalogoItem c) {
        Query query1 = em.createQuery("UPDATE PartidasDistributivo p SET p.estadoPartida=:estado1 WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado1", c).setParameter("periodo", periodo);
        int result1 = query1.executeUpdate();

        Query query2 = em.createQuery("UPDATE PartidasDistributivoAnexo a SET a.estadoPartida= :estado2 WHERE a.periodo= :periodo AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("estado2", c).setParameter("periodo", periodo);
        int result4 = query2.executeUpdate();

        Query query3 = em.createQuery("UPDATE ProformaPresupuestoPlanificado p SET p.estadoPartida= :estado3 WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("estado3", c).setParameter("periodo", periodo);
        int result5 = query3.executeUpdate();

    }

    public BigDecimal verificandoProductosActividades(ActividadOperativa a) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM Producto p INNER JOIN p.actividadOperativa a"
                + " WHERE a.id= :id AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL ")
                .setParameter("id", a.getId()).getSingleResult();
        return result;
    }

    public boolean removeActividad(ActividadOperativa ur) {
        try {
            ur = getEntityManager().find(getEntityClass(), ur.getId());
            getEntityManager().remove(ur);

            return true;
        } catch (Exception e) {
            Logger.getLogger(ActividadOperativaService.class.getName()).log(Level.SEVERE, "Eliminar usuario roles", e);
        }
        return false;
    }

    public Boolean getListaVerificacionActividades(String nombre, Short periodo) {
        List<ActividadOperativa> actividades = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a where a.nombreActividad= :nombre AND a.periodo=:periodo AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("nombre", nombre).setParameter("periodo", periodo).getResultList();

        if (!actividades.isEmpty()) {
            return true;
        }

        return false;
    }

    public Boolean getListaVerificacionActividadesReforma(String nombre, BigInteger b) {
        List<ActividadOperativa> actividades = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a where a.nombreActividad= :nombre AND a.codigoReforma= :reforma")
                .setParameter("nombre", nombre).setParameter("reforma", b).getResultList();

        if (!actividades.isEmpty()) {
            return true;
        }

        return false;
    }

    public Boolean getListaVerificacionActividadesReformaTraspaso(String nombre, BigInteger b) {
        List<ActividadOperativa> actividades = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a where a.nombreActividad= :nombre AND a.codigoReformaTraspaso= :reforma")
                .setParameter("nombre", nombre).setParameter("reforma", b).getResultList();

        if (!actividades.isEmpty()) {
            return true;
        }

        return false;
    }

    public List<ActividadOperativa> getActividadOriginal(Short periodo) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL AND a.periodo=:periodo")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }
}
