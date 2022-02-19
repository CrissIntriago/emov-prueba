/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.service.AbstractService;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class GarantiaService extends AbstractService<Garantias> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public GarantiaService() {
        super(Garantias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Garantias> findAllGarantias() {
        return em.createQuery("SELECT g FROM Garantias g WHERE g.estado = true ORDER BY g.adquisicion.numeroContrato")
                .getResultList();
    }

    public List<Garantias> findAllGarantiasByVigente(Date fechaActual) {
        return em.createQuery("SELECT g FROM Garantias g WHERE g.fechaHasta >= ?1 AND g.devolucion = false ORDER BY g.adquisicion.numeroContrato")
                .setParameter(1, fechaActual)
                .getResultList();
    }

    public List<Garantias> findAllGarantiasByVencidas(Date fechaActual) {
        return em.createQuery("SELECT g FROM Garantias g WHERE g.fechaHasta < ?1 AND g.devolucion = false ORDER BY g.adquisicion.numeroContrato")
                .setParameter(1, fechaActual)
                .getResultList();
    }

    public List<Garantias> findAllGarantiasByDevueltas() {
        return em.createQuery("SELECT g FROM Garantias g WHERE g.devolucion = true ORDER BY g.adquisicion.numeroContrato")
                .getResultList();
    }

    public List<Garantias> findAllGarantiasByAdquisicionAndRiesgoAseguradoAnFechas(Garantias garantia) {
        return em.createQuery("SELECT g FROM Garantias g "
                + "WHERE g.adquisicion = ?1 AND g.riesgoAsegurado = ?2 AND g.fechaDesde = ?3 AND g.fechaHasta = ?4")
                .setParameter(1, garantia.getAdquisicion())
                .setParameter(2, garantia.getRiesgoAsegurado())
                .setParameter(3, garantia.getFechaDesde())
                .setParameter(4, garantia.getFechaHasta())
                .getResultList();
    }

    public Garantias findGarantiaByNumTramite(Long numTramite) {
        Query query = em.createQuery("SELECT g FROM Garantias g WHERE g.numTramite = ?1")
                .setParameter(1, numTramite);
        if (!query.getResultList().isEmpty()) {
            return (Garantias) query.getResultList().get(0);
        }
        return new Garantias();
    }

}
