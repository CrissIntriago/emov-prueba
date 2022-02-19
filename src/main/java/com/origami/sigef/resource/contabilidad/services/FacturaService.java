/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.InventarioRegistro;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author OrigamiEc
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FacturaService extends AbstractService<Factura> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FacturaService() {
        super(Factura.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Factura> findFacturasByRegistro(InventarioRegistro registro) {
        return findByNamedQuery("Factura.findFacturasByRegistro", registro);
    }

    public List<Factura> findFacturaByNumeroComprobante(String numAutorizacion, Boolean tipoFactura) {
        List<Factura> resultado = (List<Factura>) em.createQuery("SELECT f FROM Factura f WHERE f.numAutorizacion = ?1 AND f.estado = TRUE AND f.facturaElectronica = ?2")
                .setParameter(1, numAutorizacion)
                .setParameter(2, tipoFactura)
                .getResultList();
        return resultado;
    }

    public boolean getvalidarFactura(Proveedor proveedor, Boolean facturaElectronica, String numFactura) {
        List<Factura> factura = (List<Factura>) em.createQuery("SELECT f FROM Factura f WHERE f.estado = true AND f.proveedor = ?1 AND f.facturaElectronica = ?2 AND f.numFactura = ?3")
                .setParameter(1, proveedor)
                .setParameter(2, facturaElectronica)
                .setParameter(3, numFactura)
                .getResultList();
        if (factura != null) {
            return !factura.isEmpty();
        } else {
            return false;
        }
    }

    public boolean getFacturasRetenidas(ContDiarioGeneral diario) {
        System.out.println("DIARIO ID: " + diario);
        List<Factura> facturas = (List<Factura>) em.createQuery("SELECT f FROM Factura f WHERE f.idConDiario = ?1 AND f.retenida = false AND f.estado = true")
                .setParameter(1, diario)
                .getResultList();
        System.out.println("FACTURAS: " + facturas);
        if (facturas != null) {
            return true;
        } else {
            return !facturas.isEmpty();
        }
    }

}
