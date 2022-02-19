/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.PlanLocalSistema;
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
 * @author ORIGAMI2
 */
public class PlanLocalSistemaLazy extends LazyModel<PlanLocalSistema> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public PlanLocalSistemaLazy() {
        super(PlanLocalSistema.class);
        this.getSorteds().put("id", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public PlanLocalSistemaLazy(Short anio) {
        super(PlanLocalSistema.class);
        this.getSorteds().put("id", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public PlanLocalSistemaLazy(OpcionBusqueda busqueda) {
        super(PlanLocalSistema.class);
        this.getSorteds().put("id", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.builder.equal(root.get("estado"), true));

        if (this.busqueda != null) {
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
