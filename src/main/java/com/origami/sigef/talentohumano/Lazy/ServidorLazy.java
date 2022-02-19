/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.Lazy;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
 * @author Criss Intriago
 */
public class ServidorLazy extends LazyModel<Servidor> {

    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public ServidorLazy() {
        super(Servidor.class);
        this.getSorteds().put("persona.identificacion", "ASC");
        this.setDistinct(false);
        this.ignoreFilters = new ArrayList<>();
    }

    public ServidorLazy(Short anio) {
        super(Servidor.class);
        this.getSorteds().put("persona.identificacion", "ASC");
        this.setDistinct(false);
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public ServidorLazy(OpcionBusqueda busqueda) {
        super(Servidor.class);
        this.getSorteds().put("persona.identificacion", "ASC");
        this.setDistinct(false);
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

        filters.entrySet().forEach((Map.Entry<String, Object> entry) -> {
            System.out.println("FILTER: " + entry.getKey());
            System.out.println("VALUE: " + entry.getValue().toString());
        });

        if (filters.containsKey("persona.identificacion")) {
            this.ignoreFilters.add("persona.identificacion");
            predicates.add(this.builder.like(root.get("persona").get("identificacion"), filters.get("persona.identificacion").toString()));
        }
        if (filters.containsKey("persona.nombre")) {
            this.ignoreFilters.add("persona.nombre");
            predicates.add(this.builder.like(builder.upper(root.get("persona").get("nombre")), filters.get("persona.nombre").toString().toUpperCase()));
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
