/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThConfLiquidacionRolService extends AbstractService<ThConfLiquidacionRol> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThConfLiquidacionRolService() {
        super(ThConfLiquidacionRol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public int updateValores(ThServidorCargo thServidorCargo, ThCargo idCargo, Short periodo) {
        return (Integer) em.createNativeQuery("SELECT * FROM talento_humano.fs_conf_liquidacion_roles(?1,?2,?3)")
                .setParameter(1, thServidorCargo.getId())
                .setParameter(2, idCargo.getId())
                .setParameter(3, periodo)
                .getSingleResult();
    }

    public List<ThConfLiquidacionRol> getCuentasRubroEgreso(ThServidorCargo thServidorCargo, Short anio) {
        return (List<ThConfLiquidacionRol>) em.createQuery("SELECT tclr FROM ThConfLiquidacionRol tclr INNER JOIN tclr.idRubro rb WHERE tclr.idServidorCargo = ?1 AND tclr.estado = true AND tclr.periodo = ?2 AND rb.ingreso = false ORDER BY rb.nombre ASC")
                .setParameter(1, thServidorCargo)
                .setParameter(2, anio)
                .getResultList();
    }

    public void getUpdateRubroCuenta(ThRubro idRubro, ContCuentas idCuenta, Short anio, CatalogoItem contrato, CatalogoItem clasificacion, ThRegimenLaboral regimen) {
        Integer value = (Integer) em.createNativeQuery("SELECT * FROM talento_humano.fs_update_rubros_egresos(?1,?2,?3,?4,?5,?6)")
                .setParameter(1, idRubro.getId())
                .setParameter(2, idCuenta.getId())
                .setParameter(3, anio)
                .setParameter(4, contrato.getId())
                .setParameter(5, clasificacion.getId())
                .setParameter(6, regimen.getId())
                .getSingleResult();
    }
}
