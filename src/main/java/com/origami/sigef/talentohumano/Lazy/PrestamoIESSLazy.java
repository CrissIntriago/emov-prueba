/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.Lazy;

import com.origami.sigef.common.entities.PrestamoIess;
import com.origami.sigef.common.entities.CatalogoItem;
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
public class PrestamoIESSLazy extends LazyModel<PrestamoIess> {

    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private CatalogoItem tipoPrestamo;

    public PrestamoIESSLazy() {
        super(PrestamoIess.class);
        this.getSorteds().put("id", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public PrestamoIESSLazy(OpcionBusqueda busqueda, CatalogoItem catalogo) {
        super(PrestamoIess.class);
        this.getSorteds().put("id", "ASC");
        this.busqueda = busqueda;
        this.tipoPrestamo = catalogo;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        if (this.busqueda != null) {
            predicates.add(this.builder.equal(root.get("estado"), true));
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
            if (this.tipoPrestamo != null) {
                predicates.add(this.builder.equal(root.get("tipoPrestamo"), this.tipoPrestamo));
            }
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        return predicates;
    }

}
