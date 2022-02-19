/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author kriiz
 */
public class ProformaPresupuestoPlanificadoLazy extends LazyModel<ProformaPresupuestoPlanificado> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Short busqueda;
    private CatalogoProformaPresupuesto catalogoProforma;

    public ProformaPresupuestoPlanificadoLazy() {
        super(ProformaPresupuestoPlanificado.class);
    }

    public ProformaPresupuestoPlanificadoLazy(Short busqueda) {
        super(ProformaPresupuestoPlanificado.class);
        this.getSorteds().put("id", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public ProformaPresupuestoPlanificadoLazy(CatalogoProformaPresupuesto catalogoProforma) {
        super(ProformaPresupuestoPlanificado.class);
        this.getSorteds().put("id", "ASC");
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
            predicates.add(this.builder.equal(root.get("periodo"), this.busqueda));
        }

        //predicates.add(this.builder.equal(root.get("generado"), true));
//        if (this.busqueda != null) {
//            if (this.busqueda != null) {
//                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda));
//            }
//        }
//        if (this.catalogoProforma != null) {
//            if (this.catalogoProforma.getPeriodoCatalogo() != null) {
//                predicates.add(this.builder.equal(root.get("periodo"), this.catalogoProforma.getPeriodoCatalogo().getAnio()));
//            }
//        }
        if (filters.containsKey("estructuraProgramatica.id")) {
            this.ignoreFilters.add("estructuraProgramatica.id");
            predicates.add(this.builder.equal(root.get("estructuraProgramatica").get("id"), Long.parseLong(filters.get("estructuraProgramatica.id").toString())));
        }

        if (filters.containsKey("itemPresupuestario.id")) {
            this.ignoreFilters.add("itemPresupuestario.id");
            predicates.add(this.builder.equal(root.get("itemPresupuestario").get("id"), Long.parseLong(filters.get("itemPresupuestario.id").toString())));
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        return predicates;
        //addWhere(predicates, crit);
    }

}
