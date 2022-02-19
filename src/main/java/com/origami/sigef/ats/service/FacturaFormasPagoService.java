/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.service;

import com.origami.sigef.ats.entities.FacturaFormasPago;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI
 */
@Stateless
public class FacturaFormasPagoService extends AbstractService<FacturaFormasPago> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FacturaFormasPagoService() {
        super(FacturaFormasPago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FacturaFormasPago> findAllFormaPagoByFactura(Factura factura) {
        return em.createQuery("SELECT f FROM FacturaFormasPago f WHERE f.factura = ?1")
                .setParameter(1, factura)
                .getResultList();
    }

    public FacturaFormasPago findFacturaFormaPagobyFacturAndPago(Factura factura, FormaPago formaPago) {
        Query query = em.createQuery("SELECT f FROM FacturaFormasPago f WHERE f.factura = ?1 AND f.formaPago = ?2")
                .setParameter(1, factura)
                .setParameter(2, formaPago);
        if (!query.getResultList().isEmpty()) {
            return (FacturaFormasPago) query.getResultList().get(0);
        }
        return null;
    }
}
