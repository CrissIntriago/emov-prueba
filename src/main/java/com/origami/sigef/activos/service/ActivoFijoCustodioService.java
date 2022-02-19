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
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ActivoFijoCustodioService extends AbstractService<ActivoFijoCustodio> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ActivoFijoCustodioService() {
        super(ActivoFijoCustodio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ActivoFijoCustodio getUltimaActa(Boolean actaGuardalmacen) {
        try {
            /*Si el tipo de acta es igual a false quiere decir que pertenece a un servidor */
 /*Si el tipo de acta es igual a true quiere decir que pertenece a un guardalmacen */
            ActivoFijoCustodio resultado = (ActivoFijoCustodio) em.createQuery("SELECT c FROM ActivoFijoCustodio c "
                    + "WHERE c.actaGuardalmacen =:actaGuardalmacen ORDER BY c.id DESC")
                    .setParameter("actaGuardalmacen", actaGuardalmacen)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean consultarComponentesCustodio(ActivoFijoCustodio activoFijoCustodio) {
        List<ActivoFijoServidor> resultado = (List<ActivoFijoServidor>) em.createQuery("SELECT s FROM ActivoFijoServidor s "
                + "INNER JOIN s.activoFijoCustodio c "
                + "INNER JOIN s.bienesItem b "
                + "WHERE s.activoFijoCustodio =:activoFijoCustodio "
                + "AND b.tieneComponentes = TRUE")
                .setParameter("activoFijoCustodio", activoFijoCustodio)
                .getResultList();
        return resultado.isEmpty();
    }

    public List<Servidor> getCustodios() {
        List<Servidor> resultado = (List<Servidor>) em.createQuery("SELECT DISTINCT s FROM ActivoFijoCustodio c "
                + "INNER JOIN c.servidor s WHERE c.estado=TRUE")
                .getResultList();
        return resultado;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        List<UnidadAdministrativa> resultado = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT c.idUnidad FROM ActivoFijoServidor ser , ThServidorCargo sc  JOIN ser.activoFijoCustodio cus JOIN sc.idCargo c ON sc.idServidor = cus.servidor  WHERE ser.asignado = TRUE AND ser.estado = true")
                .getResultList();
        return resultado;
    }

    public List<ActivoFijoCustodio> getActasConTramite(Long numTramite) {

        List<ActivoFijoCustodio> resultado = (List<ActivoFijoCustodio>) em.createQuery("SELECT c FROM ActivoFijoCustodio c WHERE c.numeroTramite=:numTramite AND c.estado = TRUE")
                .setParameter("numTramite", BigInteger.valueOf(numTramite))
                .getResultList();
        return resultado;
    }

    public List<BienesItem> listadoDeBienes() {
        List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b WHERE b.itemBienBoolean = TRUE AND b.estado = TRUE")
                .getResultList();
        return resultado;
    }

    public void getAcualizarEstadosCustodio(String estado, long num) {
        BigInteger b = BigInteger.valueOf(num);
        Query query = em.createQuery("UPDATE ActivoFijoCustodio a set a.estadoProceso= :estado WHERE a.numeroTramite= :num").
                setParameter("estado", estado)
                .setParameter("num", b);
        int rowCount = query.executeUpdate();
        System.out.println("estado " + rowCount);
    }
}
