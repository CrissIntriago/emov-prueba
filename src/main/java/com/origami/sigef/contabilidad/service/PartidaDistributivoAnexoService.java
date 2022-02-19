/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author LuisPozoG
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PartidaDistributivoAnexoService extends AbstractService<PartidasDistributivoAnexo> {
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public PartidaDistributivoAnexoService() {
        super(PartidasDistributivoAnexo.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<PartidasDistributivoAnexo> getPartidasDistributivoForYear(Short periodo) {
        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.estado=true and p.periodo = ?1 and p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL "
                    + "ORDER BY p.distributivoAnexo.valorParametrizado.tipo.texto ASC, p.distributivoAnexo.nombre ASC").
                    setParameter(1, periodo);
            List<PartidasDistributivoAnexo> listResultPartidasAnexo = query.getResultList();
            return listResultPartidasAnexo;
        } catch (NoResultException e) {
            return null;
        }
    }

    //Luis Pozo G traer todos los distributivos Anexos por periodo y su Partida Ap is not null es para presupuesto egreso
    public List<PartidasDistributivoAnexo> showAllPartidasAnexo(Short periodo) {
        
        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo = ?1 AND p.partidaAp IS NOT NULL AND p.codigoReforma  IS NULL AND p.codigoReformaTraspaso  IS NULL").setParameter(1, periodo);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<PartidasDistributivoAnexo> showAllPartidasAnexoReforma(Short periodo, ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo = ?1 AND p.partidaAp IS NOT NULL AND p.codigoReforma= ?2 ")
                    .setParameter(1, periodo).setParameter(2, reforma);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<PartidasDistributivoAnexo> getDisAnexosEstadoFalse(Short periodo) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivoAnexo pd JOIN pd.distributivoAnexo da where pd.periodo = ?1 AND pd.estado = TRUE "
                    + "AND da.estado = FALSE AND da.anio = ?1 ORDER BY da.valorParametrizado.tipo ASC, da.nombre ASC")
                    .setParameter(1, periodo);
            List<PartidasDistributivoAnexo> listaDisAnexo = query.getResultList();
            return listaDisAnexo;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public PartidasDistributivoAnexo getPartidaAnexoXRubro(ParametrosTalentoHumano rubro, short periodo) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivoAnexo pd JOIN pd.distributivoAnexo da WHERE da.anio = ?2 AND da.valorParametrizado = ?1 AND pd.estado = TRUE and pd.periodo=?2 AND da.estado = TRUE and pd.codigoReforma IS NULL AND pd.codigoReformaTraspaso IS NULL AND"
                    + "")
                    .setParameter(1, rubro)
                    .setParameter(2, periodo);
            PartidasDistributivoAnexo result = (PartidasDistributivoAnexo) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
            
        }
    }
    
    public List<PartidasDistributivoAnexo> getPartidasAnexo(CatalogoItem c, short periodo) {
//        AND p.estadoPartida=:estado
//.setParameter("estado", c)
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p where p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL  AND p.periodo= :periodo and p.estado= true")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }
    
    public List<ThCargoRubros> getPartidasAnexoReformaVerificacion(BigInteger id) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p where p.codigoReforma= :reforma")
                .setParameter("reforma", id).getResultList();
        return result;
    }
    
    public List<ThCargoRubros> getPartidasAnexoReforma(BigInteger id) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p where p.codigoReforma= :reforma and p.partidaPresupuestaria IS NOT NULL ORDER BY p.idPresupuesto.descripcion ASC")
                .setParameter("reforma", id).getResultList();
        return result;
    }
    
    public List<ThCargoRubros> getPartidasAnexoReformaNuevo(BigInteger id) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p where p.codigoReforma= :reforma and p.partidaPresupuestaria IS NULL ORDER BY p.id ASC")
                .setParameter("reforma", id).getResultList();
        return result;
    }
    
    public List<PartidasDistributivoAnexo> getPartidasAnexoReformaModificacion(BigInteger id) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p where p.codigoReforma= :reforma and p.partidaAp IS NOT NULL")
                .setParameter("reforma", id).getResultList();
        return result;
    }
    
    public List<PartidasDistributivoAnexo> getPartidasAnexoReformaTraspaso(BigInteger id) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p where p.codigoReformaTraspaso = :reforma ORDER BY p.distributivoAnexo.valorParametrizado.tipo.texto ASC, p.distributivoAnexo.nombre ASC")
                .setParameter("reforma", id).getResultList();
        return result;
    }
    
    public ThCargoRubros getValorActual(ThCargoRubros p) {
        ThCargoRubros result = (ThCargoRubros) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.id= :id")
                .setParameter("id", p.getId()).getSingleResult();
        return result;
    }
    
    public List<PartidasDistributivoAnexo> showAllPartidasAnexoReforma(Short periodo, ReformaTraspaso r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        try {
            Query query = em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.periodo = ?1 AND p.partidaAp IS NOT NULL AND p.codigoReformaTraspaso= ?2 ")
                    .setParameter(1, periodo).setParameter(2, reforma);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<PartidasDistributivoAnexo> getDistributivoAnexoOriginal(Short periodo) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL AND p.estado=true AND p.periodo=:periodo")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }
    
    public Boolean validateIfExistPartidaInPeriodo(String codigo, Short anio) {
        try {
            Query query = em.createQuery("Select pa FROM PartidasDistributivoAnexo pa WHERE pa.codigoReforma IS NULL AND pa.codigoReformaTraspaso IS NULL AND pa.partidaAp = :codigo AND pa.periodo = :anio and pa.estado = TRUE")
                    .setParameter("codigo", codigo)
                    .setParameter("anio", anio);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean validateIfExistPartidaInPeriodoReforma(String codigo, Short anio, BigInteger reforma) {
        try {
            Query query = em.createQuery("Select pa FROM PartidasDistributivoAnexo pa WHERE pa.codigoReforma IS NULL AND pa.codigoReformaTraspaso IS NULL AND pa.partidaAp = :codigo AND pa.periodo = :anio and pa.codigoReforma=:reforma and pa.estado = TRUE")
                    .setParameter("codigo", codigo)
                    .setParameter("anio", anio).
                    setParameter("reforma", reforma);
            List<PartidasDistributivoAnexo> result = query.getResultList();
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    public BigDecimal getMontoReservado(String partida, Short periodo) {
        BigDecimal lista = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE e.codigo IN ('LIQUI','APRO') AND d.presupuesto.partida=:partida AND s.periodo=:periodo")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return lista;
    }
    
}
