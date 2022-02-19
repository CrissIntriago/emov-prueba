/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.lazy;

import com.origami.sigef.common.entities.Proveedor;
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
public class ProveedorLazy extends LazyModel<Proveedor> {

    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public ProveedorLazy() {
        super(Proveedor.class);
        this.getSorteds().put("cliente.identificacion", "ASC");
        this.setDistinct(false);
        this.ignoreFilters = new ArrayList<>();
    }

    public ProveedorLazy(Short anio) {
        super(Proveedor.class);
        this.getSorteds().put("cliente.identificacion", "ASC");
        this.setDistinct(false);
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public ProveedorLazy(OpcionBusqueda busqueda) {
        super(Proveedor.class);
        this.getSorteds().put("cliente.identificacion", "ASC");
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

//        if (filters.containsKey("cliente.identificacion")) {
//            this.ignoreFilters.add("cliente.identificacion");
//            predicates.add(this.builder.equal(root.get("cliente").get("identificacion"), filters.get("cliente.identificacion")));
//        }
        if (filters.containsKey("cliente.nombre")) {
            this.ignoreFilters.add("cliente.nombre");
            predicates.add(this.builder.like(builder.upper(root.get("cliente").get("nombre")), filters.get("cliente.nombre").toString().toUpperCase()));
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
