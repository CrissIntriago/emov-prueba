/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
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
 * @author ORIGAMI
 */
public class ProgramacionIngresoEgresoLazy extends LazyModel<ProgramacionIngresoEgreso> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Boolean tipoFlujo;

    public ProgramacionIngresoEgresoLazy() {
        super(ProgramacionIngresoEgreso.class);
        this.getSorteds().put("itemPresupuestario.codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public ProgramacionIngresoEgresoLazy(OpcionBusqueda busqueda, Boolean flujo) {
        super(ProgramacionIngresoEgreso.class);
        this.getSorteds().put("itemPresupuestario.codigo", "ASC");
        this.busqueda = busqueda;
        this.tipoFlujo = flujo;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        if (this.busqueda != null) {
            predicates.add(this.builder.equal(root.get("tipoFlujo"), this.tipoFlujo));
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        return predicates;
    }

}
