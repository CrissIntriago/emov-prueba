/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * ||
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FnConvenioPagoDetallerService extends AbstractService<FnConvenioPagoDetalle> {

    private static final Logger LOG = Logger.getLogger(FinaRenTipoValorServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FnConvenioPagoDetallerService() {
        super(FnConvenioPagoDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FnConvenioPagoDetalle> findByDetallePago(FnConvenioPago convenio) {
        List<FnConvenioPagoDetalle> detalle = new ArrayList<>();
        try {
            detalle = (List<FnConvenioPagoDetalle>) em.createQuery("select a from FnConvenioPagoDetalle a where a.convenio=?1").setParameter(1, convenio).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(FnConvenioPagoDetalle.class.getName()).log(Level.SEVERE, "Error al buscar el detalle del convenio", ex);
        }
        return detalle;
    }

    public FnConvenioPago buscarConvenio(Long textoBusquedaConvenio) {

        FnConvenioPago con = new FnConvenioPago();
        try {
            con = (FnConvenioPago) em.createQuery("select a from FnConvenioPago a where a.id=?1").setParameter(1, textoBusquedaConvenio).getSingleResult();
        } catch (Exception e) {
        }
        return con;
    }

    public FnConvenioPago buscarConvenioPredio(String textoBusquedaConvenio) {

        FnConvenioPago con = new FnConvenioPago();
        try {
            con = (FnConvenioPago) em.createQuery("select a from FnConvenioPago a inner join a.predio p where p.claveCat=?1").setParameter(1, textoBusquedaConvenio).getSingleResult();
        } catch (Exception e) {
        }
        return con;
    }

    public FnConvenioPago buscarConvenioContribuyente(String textoBusquedaConvenio) {
        FnConvenioPago con = new FnConvenioPago();
        try {
            con = (FnConvenioPago) em.createQuery("select a from FnConvenioPago a inner join a.contribuyente p where p.identificacion=?1").setParameter(1, textoBusquedaConvenio).getSingleResult();
        } catch (Exception e) {
        }
        return con;
    }

    public FnConvenioPagoDetalle getCuotaDetalle(FinaRenLiquidacion liq) {
        try {
            return (FnConvenioPagoDetalle) em.createQuery("SELECT c FROM FnConvenioPagoDetalle c WHERE c.liquidacion = ?1 AND c.convenio = ?2 ")
                    .setParameter(1, liq).setParameter(2, liq.getConvenioPago()).getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FnConvenioPagoDetalle> findByDetallePagoPagado(FnConvenioPago convenio) {
        List<FnConvenioPagoDetalle> detalle = new ArrayList<>();
        try {
            detalle = (List<FnConvenioPagoDetalle>) em.createQuery("select a from FnConvenioPagoDetalle a where a.convenio=?1 and a.liquidacion.estadoLiquidacion=1").setParameter(1, convenio).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(FnConvenioPagoDetalle.class.getName()).log(Level.SEVERE, "Error al buscar el detalle del convenio", ex);
        }
        return detalle;
    }
}
