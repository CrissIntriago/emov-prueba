/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@Stateless
@javax.enterprise.context.Dependent
public class CuotaAnticipoService extends AbstractService<CuotaAnticipo> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CuotaAnticipoService() {
        super(CuotaAnticipo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CuotaAnticipo> getCuotaList(AnticipoRemuneracion ant) {
        List<CuotaAnticipo> result = (List<CuotaAnticipo>) em.createQuery("SELECT c FROM CuotaAnticipo c WHERE c.anticipoRemuneracion = ?1")
                .setParameter(1, ant)
                .getResultList();
        return result;
    }

    public CuotaAnticipo getCuotaMes(AnticipoRemuneracion ant, TipoRol rolMes) {
        try {
            Query query = em.createQuery("SELECT c FROM CuotaAnticipo c WHERE c.estadoCuota = FALSE AND c.anticipoRemuneracion = ?1 AND c.mes = ?2")
                    .setParameter(1, ant).setParameter(2, rolMes.getMes().getDescripcion());
            CuotaAnticipo result = (CuotaAnticipo) query.getSingleResult();
            if (TalentoHumano.validarFechaCuota(result.getFechaCuota(), rolMes)) {
                return result;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean getCuotasDeuda(AnticipoRemuneracion ant) {
        Query query = em.createQuery("SELECT ac FROM CuotaAnticipo ac WHERE ac.anticipoRemuneracion = ?1 AND ac.estadoCuota = false")
                .setParameter(1, ant);
        List<AnticipoRemuneracion> result = (List<AnticipoRemuneracion>) query.getResultList();
        System.out.println("lista anticipo--> " + result);
        return result.isEmpty();
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE talento_humano.cuota_anticipo SET referencia_contable = null, periodo = null, fecha_pago = null, estado_cuota = false where referencia_contable = ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        return query.executeUpdate();
    }

    public int deleteCuotas(AnticipoRemuneracion anticipo) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM talento_humano.cuota_anticipo\n"
                + "	WHERE cuota_anticipo.anticipo_remuneracion = ?1")
                .setParameter(1, anticipo.getId());
        return query.executeUpdate();
    }
}
