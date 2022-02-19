/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class ThCargoService extends AbstractService<ThCargo> {

    @Inject
    private ValoresService valoresService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThCargoService() {
        super(ThCargo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    private String getQueryHQLExisteCargo(ThCargo c) {
        String query = null;
        if (c.getNombreCargo() != null) {
            return "SELECT p FROM ThCargo p WHERE p.nombreCargo = :nombreCargo AND p.estado = TRUE";
        }
        return query;
    }

    public ThCargo existeCargo(ThCargo c) {
        String queryString = getQueryHQLExisteCargo(c);

        Map<String, Object> params = getParameter(c);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<ThCargo> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(ThCargo c) {

        Map<String, Object> params = new HashMap<>();
        if (c.getNombreCargo() != null) {
            params.put("nombreCargo", c.getNombreCargo());
        }
        return params;
    }

    public String getCode() {
        String code = valoresService.findByCodigo(CONFIG.CODE_CARGO);
        BigInteger result = (BigInteger) em.createNativeQuery(QUERY.COUNT_CARGO)
                .setParameter(1, code)
                .getSingleResult();
        return code + (result.intValue() + 1);
    }

    public List<ThCargo> findCargoParametro(Boolean codigo) {
        List<ThCargo> result = new ArrayList<>();
        if (codigo) {//Todos
            result = (List<ThCargo>) em.createQuery(QUERY.FIND_CARGO_PARAMETRO_1)
                    .getResultList();
        } else if (codigo) {//activos
            result = (List<ThCargo>) em.createQuery(QUERY.FIND_CARGO_PARAMETRO_2)
                    .getResultList();
        }
        return result;
    }

    public List<String> findActivos(Boolean estadoActivo) {
        List<String> result = (List<String>) em.createNativeQuery(QUERY.FIND_CARGO_ESTADO)
                .setParameter(1, estadoActivo)
                .getResultList();
        return result;
    }

    public List<String> getCargosActivos() {
        return (List<String>) em.createQuery("SELECT DISTINCT(tc.nombreCargo) FROM ThCargo tc WHERE tc.estado = true AND tc.activo = true ORDER BY tc.nombreCargo")
                .getResultList();
    }

    public List<ThCargo> getCargos(String thCargoSeleccionado, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion, UnidadAdministrativa unidadFind) {
        boolean parametro_1 = false;
        String valor_1 = "";
        boolean parametro_2 = false;
        String valor_2 = "";
        boolean parametro_3 = false;
        String valor_3 = "";
        if (filtroContrato != null) {
            parametro_1 = true;
            valor_1 = filtroContrato.getId().toString();
        }
        if (filtroClasificacion != null) {
            parametro_2 = true;
            valor_2 = filtroClasificacion.getId().toString();
        }
        if (unidadFind != null) {
            parametro_3 = true;
            valor_3 = unidadFind.getId().toString();
        }
        return (List<ThCargo>) em.createNativeQuery("SELECT thc.* FROM talento_humano.th_cargo thc WHERE thc.estado = true AND thc.nombre_cargo = ?1 "
                + "AND (CASE WHEN true = ?2 THEN thc.id_contrato = CAST(CONCAT(?3,NULL) AS bigint) ELSE true END) "
                + "AND (CASE WHEN true = ?4 THEN thc.id_catalogo_item = CAST(CONCAT(?5,NULL) AS bigint) ELSE true END) "
                + "AND (CASE WHEN true = ?6 THEN thc.id_unidad = CAST(CONCAT(?7,NULL) AS bigint) ELSE true END)", ThCargo.class)
                .setParameter(1, thCargoSeleccionado)
                .setParameter(2, parametro_1)
                .setParameter(3, valor_1)
                .setParameter(4, parametro_2)
                .setParameter(5, valor_2)
                .setParameter(6, parametro_3)
                .setParameter(7, valor_3)
                .getResultList();
    }

}
