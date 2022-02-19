/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.lazy;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
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
public class SoliciudReservaCompromisoLazy extends LazyModel<SolicitudReservaCompromiso> {

    private OpcionBusqueda busqueda;
    CatalogoItem ca = new CatalogoItem();

    public SoliciudReservaCompromisoLazy() {
        super(SolicitudReservaCompromiso.class);
        this.getSorteds().put("id", "ASC");

        this.ignoreFilters = new ArrayList<>();
    }

    public SoliciudReservaCompromisoLazy(CatalogoItem c) {
        super(SolicitudReservaCompromiso.class);
        this.getSorteds().put("id", "ASC");
        this.ca = c;

//        if(ca.getCodigo()=="REG"){
//          this.getSorteds().put("estado", ca.getTexto());
//        }
        this.ignoreFilters = new ArrayList<>();
    }

    public SoliciudReservaCompromisoLazy(OpcionBusqueda busqueda) {
        super(SolicitudReservaCompromiso.class);
        this.getSorteds().put("id", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {

        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();

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
        if (filters.containsKey("unidadRequiriente.id")) {
            this.ignoreFilters.add("unidadRequiriente.id");
            predicates.add(this.builder.equal(root.get("unidadRequiriente").get("id"), Long.parseLong(filters.get("unidadRequiriente.id").toString())));
        }

        if (filters.containsKey("beneficiario.id")) {
            this.ignoreFilters.add("beneficiario.id");
            predicates.add(this.builder.equal(root.get("beneficiario").get("id"), Long.parseLong(filters.get("beneficiario.id").toString())));
        }

        if (filters.containsKey("estado.id")) {
            this.ignoreFilters.add("estado.id");
            predicates.add(this.builder.equal(root.get("estado").get("id"), Long.parseLong(filters.get("estado.id").toString())));
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
