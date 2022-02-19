/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.talento_humano.entities.ThFormulario107;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThFormulario107Service extends AbstractService<ThFormulario107> {

    @Inject
    private ThConfigService thConfigService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThFormulario107Service() {
        super(ThFormulario107.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ThFormulario107> getList(Short anio) {
        return findByNamedQuery("ThFormulario107.findByPeriodo", anio);
    }

    public BigDecimal findByRubroByServidor(long idServidor, String codigo, Short periodo) {
        try {
            return (BigDecimal) em.createNativeQuery("SELECT \n"
                    + "COALESCE((SUM(dt.valor_ingreso) + SUM(dt.valor_egreso)),0.00) \n"
                    + "FROM talento_humano.th_liquidacion_rol_detalle dt\n"
                    + "INNER JOIN talento_humano.th_liquidacion_rol lr ON dt.id_liquidacion_rol = lr.id\n"
                    + "INNER JOIN talento_humano.th_tipo_rol ttr ON lr.id_tipo_rol = ttr.id\n"
                    + "INNER JOIN talento_humano.th_rubro tr ON dt.id_rubro = tr.id\n"
                    + "INNER JOIN talento_humano.th_servidor_cargo tsc ON lr.id_servidor_cargo = tsc.id\n"
                    + "INNER JOIN catalogo_item ci ON tr.clasificacion = ci.id\n"
                    + "WHERE ttr.aprobado = true AND ttr.estado = true AND tsc.id_servidor = ?1\n"
                    + "AND ci.texto = ?2 AND ttr.periodo = ?3")
                    .setParameter(1, idServidor)
                    .setParameter(2, thConfigService.findCode(codigo).getCodConfig())
                    .setParameter(3, periodo)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
