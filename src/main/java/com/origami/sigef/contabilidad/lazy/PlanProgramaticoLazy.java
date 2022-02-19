
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.PlanProgramatico;
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
public class PlanProgramaticoLazy extends LazyModel<PlanProgramatico> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;

    public PlanProgramaticoLazy() {
        super(PlanProgramatico.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public PlanProgramaticoLazy(Short anio) {
        super(PlanProgramatico.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public PlanProgramaticoLazy(OpcionBusqueda busqueda) {
        super(PlanProgramatico.class);
        this.getSorteds().put("codigo", "ASC");
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
            if (this.busqueda.getTitulo() != null) {
                predicates.add(this.builder.equal(root.get("funcion"), this.busqueda.getTitulo()));
            }
            if (this.busqueda.getGrupo() != null) {
                predicates.add(this.builder.equal(root.get("programa"), this.busqueda.getGrupo()));
            }
            if (this.busqueda.getSubGrupo() != null) {
                predicates.add(this.builder.equal(root.get("subprograma"), this.busqueda.getSubGrupo()));
            }
        }
        if (filters.containsKey("clasificacion.id")) {
            this.ignoreFilters.add("clasificacion.id");
            predicates.add(this.builder.equal(root.get("clasificacion").get("id"), Long.parseLong(filters.get("clasificacion.id").toString())));
        }
        if (filters.containsKey("codigo")) {
            this.ignoreFilters.add("codigo");
            predicates.add(this.builder.like(builder.upper(root.get("codigo")), filters.get("codigo").toString().trim().toUpperCase() + "%"));

        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
