/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author Luis Suarez
 */
public class UnidadAdministrativaLazy extends LazyModel<UnidadAdministrativa> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public UnidadAdministrativaLazy() {
        super(UnidadAdministrativa.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.setDistinct(false);
    }

    public UnidadAdministrativaLazy(Short anio) {
        super(UnidadAdministrativa.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
        this.setDistinct(false);
    }

    public UnidadAdministrativaLazy(OpcionBusqueda busqueda) {
        super(UnidadAdministrativa.class);
        this.getSorteds().put("codigo", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
        this.setDistinct(false);
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.builder.equal(root.get("estado"), true));

        if (filters.containsKey("nivel.id")) {
            this.ignoreFilters.add("nivel.id");
            predicates.add(this.builder.equal(root.get("nivel").get("id"), Long.parseLong(filters.get("nivel.id").toString())));
        }

        if (filters.containsKey("padre.id")) {
            this.ignoreFilters.add("padre.id");
            predicates.add(this.builder.equal(root.get("padre").get("id"), Long.parseLong(filters.get("padre.id").toString())));
        }

        if (filters.containsKey("tipoUnidad.id")) {
            this.ignoreFilters.add("tipoUnidad.id");
            predicates.add(this.builder.equal(root.get("tipoUnidad").get("id"), Long.parseLong(filters.get("tipoUnidad.id").toString())));
        }

        if (filters.containsKey("fechaVigencia")) {
            this.ignoreFilters.add("fechaVigencia");
            predicates.add(this.builder.like(builder.upper(root.get("fechaVigencia")), filters.get("fechaVigencia").toString().trim().toUpperCase() + "%"));

        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }

}
