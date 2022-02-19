/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.contabilidad.model.BeneficiarioTransferenciasModel;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContTransferencias;
import com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
    
/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContTransferenciasDetalleService extends AbstractService<ContTransferenciasDetalle> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContTransferenciasDetalleService() {
        super(ContTransferenciasDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<String> getRowTransferenciaZip(ContTransferencias contTransferencias) {
        return (List<String>) em.createNativeQuery("SELECT * FROM contabilidad.fs_detalle_zip(?1)")
                .setParameter(1, contTransferencias.getId())
                .getResultList();
    }

    public List<String> getRowTransferenciaAP(ContTransferencias contTransferencias) {
        return (List<String>) em.createNativeQuery("SELECT * FROM contabilidad.fs_detalle_ap(?1)")
                .setParameter(1, contTransferencias.getId())
                .getResultList();
    }

    public List<ContComprobantePago> findTransferenciaComprobante(ContTransferencias contTransferencias) {
        return (List<ContComprobantePago>) em.createQuery("SELECT DISTINCT(ccp) FROM ContTransferenciasDetalle ctd INNER JOIN ctd.idContComprobantePago ccp WHERE ctd.idContTransferencia = ?1")
                .setParameter(1, contTransferencias)
                .getResultList();
    }

    public ContTransferenciasDetalle getLastContTransferenciasDetalleByPeriodo(Short periodo) {
        try {
            return (ContTransferenciasDetalle) em.createQuery("SELECT td FROM ContTransferenciasDetalle td WHERE td.idContTransferencia.periodo = :periodo ORDER BY td.referencia DESC").setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BeneficiarioTransferenciasModel> getBeneficiarioContTransferencias(Short periodo) {
        String sql = "SELECT \n"
                + "	dt.identificacion As identificacion, \n"
                + "	dt.nombre_beneficiario As nombre\n"
                + "	FROM contabilidad.cont_transferencias_detalle dt\n"
                + "	INNER JOIN contabilidad.cont_transferencias t ON dt.id_cont_transferencia = t.id	\n"
                + "	WHERE t.periodo=?1\n"
                + "	GROUP BY 1,2\n"
                + "	ORDER BY dt.nombre_beneficiario ASC\n"
                + "	";
        List< Object[]> result = em.createNativeQuery(sql).setParameter(1, periodo).getResultList();
        if (result != null) {
            List<BeneficiarioTransferenciasModel> list = new ArrayList<>();
            for (Object[] objecto : result) {
                BeneficiarioTransferenciasModel data = new BeneficiarioTransferenciasModel();
                data.setIdentificacion((String) objecto[0]);
                data.setNombre((String) objecto[1]);
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }

    public List<String> getEstadosContTransferencias(Short periodo) {
        List<String> resultado = (List<String>) em.createQuery("SELECT t.estadoTransferencia FROM ContTransferencias t  WHERE t.periodo=:periodo GROUP BY t.estadoTransferencia ORDER BY t.estadoTransferencia ASC")
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;

    }
}
