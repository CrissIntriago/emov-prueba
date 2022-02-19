/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ContBeneficiarioComprobantePagoService extends AbstractService<ContBeneficiarioComprobantePago> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ContBeneficiarioComprobantePagoService() {
        super(ContBeneficiarioComprobantePago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer nextRegistro(Short periodo) {
        Integer result = (Integer) em.createQuery(QUERY.NEXT_NUM_BENEFICIARIO)
                .setParameter(1, periodo)
                .getSingleResult();
        return result;
    }

    public String getNombresBeneficiariosComprobantesByIdComprobante(Long idComprobante) {

        try {
            String result = (String) em.createNativeQuery("SELECT string_agg(concat(c.nombre||' '||c.apellido ),' - ') \n"
                    + "FROM contabilidad.cont_beneficiario_comprobante_pago bcp\n"
                    + "INNER JOIN cliente c  ON c.id =  bcp.id_cliente\n"
                    + "WHERE id_comprobante_pago = ?1").setParameter(1, idComprobante).getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
