
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.FuenteFinanciamiento;
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
public class FuenteFinanciamientoLazy extends LazyModel<FuenteFinanciamiento> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public FuenteFinanciamientoLazy() {
        super(FuenteFinanciamiento.class);
        this.getSorteds().put("tipoFuente.id", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public FuenteFinanciamientoLazy(Short anio) {
        super(FuenteFinanciamiento.class);
        this.getSorteds().put("tipoFuente.id", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public FuenteFinanciamientoLazy(OpcionBusqueda busqueda) {
        super(FuenteFinanciamiento.class);
        this.getSorteds().put("tipoFuente.id", "ASC");
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

//        if (this.busqueda != null) {
//            if (this.busqueda.getAnio() != null) {
//                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
//            }
//            if (filters.containsKey("tipoFuente.id")) {
//                this.ignoreFilters.add("tipoFuente.id");
//                predicates.add(this.builder.equal(root.get("tipoFuente").get("id"), Long.parseLong(filters.get("tipoFuente.id").toString())));
//            }
//        }
        if (filters.containsKey("tipoFuente.id")) {
            this.ignoreFilters.add("tipoFuente.id");
            predicates.add(this.builder.equal(root.get("tipoFuente").get("id"), Long.parseLong(filters.get("tipoFuente.id").toString())));
        }
        if (filters.containsKey("id")) {
            this.ignoreFilters.add("id");
            predicates.add(this.builder.like(builder.upper(root.get("id")), filters.get("id").toString().trim().toUpperCase() + "%"));

        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }

}
