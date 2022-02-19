/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContTransferencias;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContTransferenciasService extends AbstractService<ContTransferencias> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContTransferenciasService() {
        super(ContTransferencias.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getHeaderTransfereciaZip(ContTransferencias contTransferencias) {
        return (String) em.createNativeQuery("SELECT * FROM contabilidad.fs_cabezera_zip(?1)")
                .setParameter(1, contTransferencias.getId())
                .getSingleResult();
    }

    public String getHeaderTransfereciaAP(ContTransferencias contTransferencias) {
        return (String) em.createNativeQuery("SELECT * FROM contabilidad.fs_cabezera_ap(?1)")
                .setParameter(1, contTransferencias.getId())
                .getSingleResult();
    }

    public BigInteger nextRegistro(Short periodo) {
        Integer result = (Integer) em.createQuery(QUERY.NEXT_NUM_TRANSFERENCIA)
                .setParameter(1, periodo)
                .getSingleResult();
        return BigInteger.valueOf(result);
    }

    public void updateEstado(ContTransferencias contTransferencias, Date fechaAcreditacion) {
        int aux = (int) em.createNativeQuery("UPDATE contabilidad.cont_transferencias_detalle SET transferencia = true, fecha_acreditacion = ?2, estado ='PAGADO' WHERE id_cont_transferencia = ?1 AND fecha_anulacion is null")
                .setParameter(1, contTransferencias.getId())
                .setParameter(2, fechaAcreditacion)
                .executeUpdate();
        Integer aux1 = (Integer) em.createNativeQuery("SELECT * FROM contabilidad.update_estado_comprobante(?1)")
                .setParameter(1, contTransferencias.getId())
                .getSingleResult();
    }

}
