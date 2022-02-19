/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class LiquidacionDetalleService extends AbstractService<LiquidacionDetalle> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public LiquidacionDetalleService() {
        super(LiquidacionDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public LiquidacionDetalle findById(Long id) {
        return (LiquidacionDetalle) em.createQuery("SELECT d FROM LiquidacionDetalle d WHERE d.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
    }

    public Factura findFacturaByLiquidacionDetalle(Long id) {
        try {
            return (Factura) em.createQuery("SELECT f FROM LiquidacionDetalle detalle JOIN detalle.factura f WHERE detalle.id =?1")
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public CuentaContableRetencion findCuentaRetencionByLiquidacionDetalle(Long id) {
        return (CuentaContableRetencion) em.createQuery("SELECT c FROM LiquidacionDetalle d JOIN d.cuentaContableRetencion c WHERE d.id = ?1")
                .setParameter(1, id)
                .getSingleResult();
    }

    public List<LiquidacionDetalle> findAllLiquidacionDetalleByLiquidacion_Id(Long idLiquidacion) {
        return em.createQuery("SELECT d FROM LiquidacionDetalle d WHERE d.liquidacion.id = ?1")
                .setParameter(1, idLiquidacion)
                .getResultList();
    }
    
    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE tesoreria.liquidacion_detalle SET contabilizado = false WHERE diario_general= ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        return query.executeUpdate();
    }
}
