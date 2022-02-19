/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaTraspaso;
import com.origami.sigef.Presupuesto.Entity.ReformaTraspaso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sandra
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class DetalleReformaTraspasoService extends AbstractService<DetalleReformaTraspaso>{

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DetalleReformaTraspasoService() {
        super(DetalleReformaTraspaso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<DetalleReformaTraspaso> getListDetalleReforma(Short periodo, CatalogoItem reforma, boolean tipo, String tipoTraspaso, ReformaTraspaso reformaId) {
        List<DetalleReformaTraspaso> listReformaTraspaso = em.createQuery("SELECT p FROM DetalleReformaTraspaso dt JOIN dt.reforma p WHERE p.periodo = ?1 AND p.estadoReforma = ?2 AND p.tipo = ?3 AND p.estado = true AND dt.tipoTraspaso = ?4 AND dt.reforma = ?5" )
                .setParameter(1, periodo).setParameter(2, reforma).setParameter(3, tipo).setParameter(4, tipoTraspaso).setParameter(5, reformaId).getResultList();
        return listReformaTraspaso;
    }
    
    public List<PartidasDistributivo> getListPartidaDistributivoReforma(Short anio, BigInteger reforma) {
        try {
            Query query = em.createQuery("SELECT pd FROM PartidasDistributivo pd JOIN pd.distributivo vd JOIN vd.distributivo d JOIN vd.valoresParametrizados p JOIN p.tipo c JOIN d.cargo ca WHERE pd.periodo = ?1 AND pd.estado = TRUE AND vd.estado = TRUE AND pd.codigoReformaTraspaso = ?2 ORDER BY ca.id")
                    .setParameter(1, anio).setParameter(2, reforma);
            List<PartidasDistributivo> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ProformaPresupuestoPlanificado> getListPartidaDirectaReforma(Short anio, BigInteger reforma) {
        try {
            Query query = em.createQuery("SELECT pd FROM ProformaPresupuestoPlanificado pd WHERE pd.periodo = ?1 AND pd.estado = TRUE AND pd.codigoReformaTraspaso = ?2")
                    .setParameter(1, anio).setParameter(2, reforma);
            List<ProformaPresupuestoPlanificado> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ProformaPresupuestoPlanificado> getPartidasDirectasOrig(Short anio, BigInteger reforma) {
        try {
            Query query = em.createQuery("SELECT pd FROM ProformaPresupuestoPlanificado pd WHERE pd.periodo = ?1 AND pd.estado = TRUE AND pd.codigoReforma = null AND (pd.codigoReformaTraspaso = ?2 OR pd.codigoReformaTraspaso = NULL)")
                    .setParameter(1, anio).setParameter(2, reforma);
            List<ProformaPresupuestoPlanificado> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ProformaPresupuestoPlanificado> getListPDIReformaT1(Short anio, BigInteger reforma) {
        try {
            Query query = em.createQuery("SELECT pd FROM ProformaPresupuestoPlanificado pd WHERE pd.periodo = ?1 AND pd.estado = TRUE AND pd.codigoReformaTraspaso = ?2 AND (pd.traspasoIncremento != 0 OR pd.traspasoReduccion != 0)")
                    .setParameter(1, anio).setParameter(2, reforma);
            List<ProformaPresupuestoPlanificado> result = query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<DetalleReformaTraspaso> getListDetalleReformaTraspasoByReforma(ReformaTraspaso reforma){
        Query query = em.createQuery("SELECT dt FROM DetalleReformaTraspaso dt JOIN dt.reforma r WHERE  r.id = ?1")
                    .setParameter(1, reforma.getId());
            List<DetalleReformaTraspaso> result = query.getResultList();
            return result;
    }
}
