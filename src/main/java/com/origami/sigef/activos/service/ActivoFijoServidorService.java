/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActivoFijoCustodio;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ActivoFijoServidorService extends AbstractService<ActivoFijoServidor> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ActivoFijoServidorService() {
        super(ActivoFijoServidor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ActivoFijoServidor> getBienesAsignados(ActivoFijoCustodio activoFijoCustodio) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT a FROM ActivoFijoServidor a "
                + "WHERE a.estado = TRUE AND a.asignado = TRUE AND a.activoFijoCustodio =:activoFijoCustodio")
                .setParameter("activoFijoCustodio", activoFijoCustodio)
                .getResultList();
        return resultado;
    }

    public List<BienesItem> getComponentes(BienesItem grupoPadre) {

        List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE b.grupoPadre = :grupoPadre AND b.estado = TRUE ")
                .setParameter("grupoPadre", grupoPadre)
                .getResultList();
        return resultado;
    }

    public Boolean verificarEstadoBienes(String estadoBien) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT a FROM ActivoFijoServidor a "
                + "INNER JOIN a.bienesItem bi INNER JOIN bi.estadoBien es WHERE es.texto=:estadoBien AND a.asignado = TRUE")
                .setParameter("estadoBien", estadoBien)
                .getResultList();
        return resultado != null && !resultado.isEmpty();
    }

    public Boolean verificarBienesCustodio(Servidor servidor) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT s FROM ActivoFijoServidor s "
                + "INNER JOIN s.activoFijoCustodio c "
                + "WHERE s.asignado = true AND c.servidor =:servidor")
                .setParameter("servidor", servidor)
                .getResultList();
        return resultado.isEmpty();
    }

    /**
     * *
     * se lo cambio por la relacion de THServidorCargo
     *
     * @param unidad
     * @return
     */
    public Boolean verificarBienesDepartamento(String unidad) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT a FROM ActivoFijoServidor a , ThServidorCargo sc JOIN a.activoFijoCustodio cus JOIN sc.idCargo c ON sc.idServidor = cus.servidor WHERE c.idUnidad.nombre =:unidad AND a.asignado = true")
                .setParameter("unidad", unidad)
                .getResultList();
        return resultado.isEmpty();
    }

    public Boolean verificarBienesAsignados(ActivoFijoCustodio activoFijoCustodio) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT s FROM ActivoFijoServidor s WHERE s.activoFijoCustodio =:activoFijoCustodio")
                .setParameter("activoFijoCustodio", activoFijoCustodio)
                .getResultList();
        return resultado.isEmpty();
    }
}
