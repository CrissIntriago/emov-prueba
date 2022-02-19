/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TipoLiquidacionService extends AbstractService<FinaRenTipoLiquidacion> {

    private static final Logger LOG = Logger.getLogger(TipoLiquidacionService.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TipoLiquidacionService() {
        super(FinaRenTipoLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FinaRenTipoLiquidacion> getLiquidacionesGenerales() {
        try {
            Query query = em.createQuery("SELECT t FROM FinaRenLiquidacion r INNER JOIN r.tipoLiquidacion t WHERE t.id NOT IN (3,2) GROUP BY t.id ORDER BY t.nombreTransaccion ASC");
            List<FinaRenTipoLiquidacion> result = query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenTipoLiquidacion getTipoLiquidacionBYprefijo(String prefijo) {
        try {
            Query query = em.createQuery("SELECT t FROM FinaRenTipoLiquidacion t WHERE t.prefijo = ?1").setParameter(1, prefijo);
            FinaRenTipoLiquidacion result = (FinaRenTipoLiquidacion) query.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidacionespredio(CatPredio predio) {
        try {
            Query query = em.createQuery("SELECT t.tipoLiquidacion FROM FinaRenLiquidacion t WHERE t.predio = ?1 AND t.estadoLiquidacion in (2,8) GROUP BY 1")
                    .setParameter(1, predio);
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidacionesByCliente(Cliente cliente) {
        try {
            Query query = em.createQuery("SELECT t.tipoLiquidacion FROM FinaRenLiquidacion t WHERE t.comprador = ?1 AND t.estadoLiquidacion in (2,8) GROUP BY 1")
                    .setParameter(1, cliente);
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidaciones() {
        try {
            Query query = em.createQuery("SELECT t.tipoLiquidacion FROM FinaRenLiquidacion t WHERE t.estadoLiquidacion = 2 GROUP BY 1");
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidacionesLocales() {
        try {
            Query query = em.createQuery("SELECT t FROM FinaRenTipoLiquidacion t WHERE t.estado = true and t.id in (7,177,72,65,66,67,68,9) order by t.id asc");
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidacionesLocalesMasivo() {
        try {
            Query query = em.createQuery("SELECT t FROM FinaRenTipoLiquidacion t WHERE t.estado = true and t.id in (7,177,72,65,66,67,68,9,2,3)");
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenTipoLiquidacion> tipoliquidacionesGenerales() {
        try {
            Query query = em.createQuery("SELECT t FROM FinaRenTipoLiquidacion t WHERE t.estado = true order by id asc");
            List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
