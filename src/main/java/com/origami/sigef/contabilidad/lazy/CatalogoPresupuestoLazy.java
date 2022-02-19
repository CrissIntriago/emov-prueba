/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
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
public class CatalogoPresupuestoLazy extends LazyModel<CatalogoPresupuesto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;
    private CatalogoProformaPresupuesto catalogoProforma;

    public CatalogoPresupuestoLazy() {
        super(CatalogoPresupuesto.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public CatalogoPresupuestoLazy(Short anio) {
        super(CatalogoPresupuesto.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public CatalogoPresupuestoLazy(OpcionBusqueda busqueda) {
        super(CatalogoPresupuesto.class);
        this.getSorteds().put("codigo", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public CatalogoPresupuestoLazy(CatalogoProformaPresupuesto catalogoProforma) {
        super(CatalogoPresupuesto.class);
        this.getSorteds().put("codigo", "ASC");
        this.catalogoProforma = catalogoProforma;
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
                predicates.add(this.builder.equal(root.get("anio"), this.busqueda.getAnio()));
            }
            if (this.busqueda.getTitulo() != null) {
                predicates.add(this.builder.equal(root.get("titulo"), this.busqueda.getTitulo()));
            }
            if (this.busqueda.getGrupo() != null) {
                predicates.add(this.builder.equal(root.get("naturaleza"), this.busqueda.getGrupo()));
            }
            if (this.busqueda.getSubGrupo() != null) {
                predicates.add(this.builder.equal(root.get("subGrupo"), this.busqueda.getSubGrupo()));
            }
            if (this.busqueda.getNivel1() != null) {
                predicates.add(this.builder.equal(root.get("rubro"), this.busqueda.getNivel1()));
            }
            // addWhere(predicates, crit);
        }
        if (this.catalogoProforma != null) {

            predicates.add(this.builder.equal(root.get("flujoIngreso"), true));
            if (this.catalogoProforma.getPeriodoCatalogo() != null) {
                predicates.add(this.builder.equal(root.get("anio"), this.catalogoProforma.getPeriodoCatalogo().getAnio()));
            } else {
                predicates.add(this.builder.equal(root.get("anio"), this.anio));
            }
        }
        if (filters.containsKey("codigo")) {
            this.ignoreFilters.add("codigo");
            predicates.add(this.builder.like(builder.upper(root.get("codigo")), filters.get("codigo").toString().trim().toUpperCase() + "%"));

        }

        predicates.addAll(this.findPropertyFilter(root, filters));

        return predicates;

    }
}
