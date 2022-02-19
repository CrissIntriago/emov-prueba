/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.Transferencias;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TransferenciasService extends AbstractService<Transferencias> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TransferenciasService() {
        super(Transferencias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ComprobantePago getComprobantePago(BigInteger numComprobante, Short periodo, String estado) {
        try {
            ComprobantePago resultado = (ComprobantePago) em.createQuery("SELECT cp FROM ComprobantePago cp "
                    + "WHERE cp.numComprobante=:numComprobante AND cp.periodo=:periodo AND cp.estado=:estado")
                    .setParameter("numComprobante", numComprobante)
                    .setParameter("periodo", periodo)
                    .setParameter("estado", estado)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public Transferencias getUltimaTransferencia(Short periodo) {
        try {
            Transferencias resultado = (Transferencias) em.createQuery("SELECT t FROM Transferencias t WHERE t.periodo=:periodo AND t.numReferencia IS NOT NULL ORDER BY t.id DESC")
                    .setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ComprobantePago> getComprobantesRegistrados(Transferencias transferencias) {
        List<ComprobantePago> resultado = (List<ComprobantePago>) em.createQuery("SELECT DISTINCT(dt.comprobantePago) FROM DetalleTransferencias dt WHERE dt.transferencia=:transferencias")
                .setParameter("transferencias", transferencias)
                .getResultList();
        return resultado;
    }

    public ComprobantePago getComprobantePagoSimilaresCuentasBanco(BigInteger numComprobante, Short periodo, String estado, String numCuentaBanco) {
        try {
            ComprobantePago resultado = (ComprobantePago) em.createQuery("SELECT cp FROM ComprobantePago cp "
                    + "INNER JOIN cp.cuentaBancaria cb "
                    + "WHERE cp.numComprobante=:numComprobante AND cp.periodo=:periodo AND cp.estado=:estado AND cb.numeroCuenta=:numCuentaBanco")
                    .setParameter("numComprobante", numComprobante)
                    .setParameter("periodo", periodo)
                    .setParameter("estado", estado)
                    .setParameter("numCuentaBanco", numCuentaBanco)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public ComprobantePago getComprobantePago(BigInteger numComprobante) {
        System.out.println("numero " + numComprobante);
        try {
            ComprobantePago resultado = (ComprobantePago) em.createQuery("SELECT cp FROM ComprobantePago cp WHERE  cp.numeroTramite =:numComprobante AND cp.estado='REGISTRADO' AND cp.periodo=:periodo")
                    .setParameter("numComprobante", numComprobante)
                    .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Transferencias> getlistaVerificadorTransferencias(BigInteger num) {
        List<Transferencias> result = (List<Transferencias>) em.createQuery("SELECT t FROM Transferencias t WHERE t.numeroTramite=:num AND t.periodo=:periodo "
                + "AND (t.estadoTransferencia='EMITIDO' OR t.estadoTransferencia='ACREDITADO-PARCIAL' OR t.estadoTransferencia='ACREDITADO')")
                .setParameter("num", num)
                .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                .getResultList();
        return result;
    }

    public List<Transferencias> getVerificadorTransferencias(BigInteger num) {
        List<Transferencias> result = (List<Transferencias>) em.createQuery("SELECT t FROM Transferencias t WHERE t.numeroTramite=:num AND t.periodo=:periodo")
                .setParameter("num", num)
                .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                .getResultList();
        return result;
    }

}
