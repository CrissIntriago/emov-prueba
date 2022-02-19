/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Lazy;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
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
 * @author alexa
 */
public class ReformaSuplementoIngresoLazy extends LazyModel<DetalleReformaIngresoSuplemento> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private OpcionBusqueda busqueda;

    public ReformaSuplementoIngresoLazy() {

        super(DetalleReformaIngresoSuplemento.class);

        this.ignoreFilters = new ArrayList<>();
    }

    public ReformaSuplementoIngresoLazy(OpcionBusqueda busqueda) {
        super(DetalleReformaIngresoSuplemento.class);

        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.builder.equal(root.get("catalogoPresupuesto.estado"), true));
        predicates.add(this.builder.equal(root.get("catalogoPresupuesto.flujoIngreso"), true));

        if (filters.containsKey("catalogoPresupuesto.nivel.id")) {
            this.ignoreFilters.add("catalogoPresupuesto.nivel.id");
            predicates.add(this.builder.equal(root.get("catalogoPresupuesto").get("nivel").get("id"), Long.parseLong(filters.get("catalogoPresupuesto.nivel.id").toString())));
        }

        if (filters.containsKey("catalogoPresupuesto.nivel.id")) {
            this.ignoreFilters.add("nivel.id");
            predicates.add(this.builder.equal(root.get("nivel").get("id"), Long.parseLong(filters.get("nivel.id").toString())));
        }

        if (filters.containsKey("catalogoPresupuesto.codigo")) {
            this.ignoreFilters.add("catalogoPresupuesto.codigo");
            predicates.add(this.builder.like(builder.upper(root.get("catalogoPresupuesto").get("codigo")), filters.get("codigo").toString().trim().toUpperCase() + "%"));

        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }

}
