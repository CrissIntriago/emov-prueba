/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.lazy.LazyModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author Luis Suarez
 */
public class presupuestoLazy extends LazyModel<Presupuesto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    private Short busqueda;

    public presupuestoLazy() {

        super(Presupuesto.class);

        this.ignoreFilters = new ArrayList<>();
    }

    public presupuestoLazy(Short busqueda) {
        super(Presupuesto.class);
        this.getSorteds().put("partida", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();

//        if (filters.containsKey("estructuraProgramatica.id")) {
//            this.ignoreFilters.add("estructuraProgramatica.id");
//            predicates.add(this.builder.equal(root.get("estructuraProgramatica").get("id"), Long.parseLong(filters.get("estructuraProgramatica.id").toString())));
//        }
//
//        if (filters.containsKey("genero.id")) {
//            this.ignoreFilters.add("genero.id");
//            predicates.add(this.builder.equal(root.get("genero").get("id"), Long.parseLong(filters.get("genero.id").toString())));
//        }
//
//        if (filters.containsKey("fuente.id")) {
//            this.ignoreFilters.add("fuente.id");
//            predicates.add(this.builder.equal(root.get("fuente").get("id"), Long.parseLong(filters.get("fuente.id").toString())));
//        }
        if (this.busqueda != null) {
            if (this.busqueda != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda));
            }
            predicates.addAll(this.findPropertyFilter(root, filters));
        }

        if (filters.containsKey("item.id")) {
            this.ignoreFilters.add("item.id");
            predicates.add(this.builder.equal(root.get("item").get("id"), Long.parseLong(filters.get("item.id").toString())));
        }
        if (filters.containsKey("estructura.id")) {
            this.ignoreFilters.add("estructura.id");
            predicates.add(this.builder.equal(root.get("estructura").get("id"), Long.parseLong(filters.get("estructura.id").toString())));
        }

        if (filters.containsKey("fuente.id")) {
            this.ignoreFilters.add("fuente.id");
            predicates.add(this.builder.equal(root.get("fuente").get("id"), Long.parseLong(filters.get("fuente.id").toString())));
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
