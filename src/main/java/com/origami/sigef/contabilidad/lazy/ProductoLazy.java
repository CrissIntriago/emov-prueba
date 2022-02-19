/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author ARTURO404
 */
public class ProductoLazy extends LazyModel<Producto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    private OpcionBusqueda busqueda;
    private Boolean traerDatosNull;

    private BigInteger num = BigInteger.ONE;
    private BigInteger numTras = BigInteger.ONE;

    public ProductoLazy() {

        super(Producto.class);
        this.getSorteds().put("actividadOperativa", "ASC");

        this.ignoreFilters = new ArrayList<>();
    }

    public ProductoLazy(OpcionBusqueda busqueda) {
        super(Producto.class);
        this.getSorteds().put("actividadOperativa", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public ProductoLazy(Boolean traerDatosNull, OpcionBusqueda busqueda) {
        super(Producto.class);
        this.getSorteds().put("actividadOperativa", "ASC");
        this.busqueda = busqueda;
        this.traerDatosNull = traerDatosNull;
        this.ignoreFilters = new ArrayList<>();
    }

    public ProductoLazy(OpcionBusqueda busqueda, BigInteger num) {
        super(Producto.class);
        this.getSorteds().put("actividadOperativa", "ASC");

        this.busqueda = busqueda;
        this.num = num;
        this.traerDatosNull = traerDatosNull;
        this.ignoreFilters = new ArrayList<>();
    }

    public ProductoLazy(OpcionBusqueda busqueda, BigInteger num, Boolean traspaso) {
        super(Producto.class);
        this.getSorteds().put("actividadOperativa", "ASC");

        this.busqueda = busqueda;
        this.numTras = num;
        this.traerDatosNull = traerDatosNull;
        this.ignoreFilters = new ArrayList<>();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.builder.equal(root.get("estado"), true));
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
        if (this.traerDatosNull != null) {
            if (this.traerDatosNull == true) {
                // CORREGIR
                predicates.add(this.builder.equal(root.get("actividadOperativa").get("planProgramaProyecto").get("planAnual"), null));
            }
        }

        if (this.num.intValue() > 0) {

            predicates.add(this.builder.equal(root.get("codigoReforma"), this.num));

        }
        
        if (this.numTras.intValue() > 0) {

            predicates.add(this.builder.equal(root.get("codigoReformaTraspaso"), this.numTras));

        }

        if (this.busqueda != null) {
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
        }
        if (filters.containsKey("actividadOperativa.id")) {
            this.ignoreFilters.add("actividadOperativa.id");
            predicates.add(this.builder.equal(root.get("actividadOperativa").get("id"), Long.parseLong(filters.get("actividadOperativa.id").toString())));
        }

        if (filters.containsKey("fuente.id")) {
            this.ignoreFilters.add("fuente.id");
            predicates.add(this.builder.equal(root.get("fuente").get("id"), Long.parseLong(filters.get("fuente.id").toString())));
        }

        if (filters.containsKey("itemPresupuestario.id")) {
            this.ignoreFilters.add("itemPresupuestario.id");
            predicates.add(this.builder.equal(root.get("itemPresupuestario").get("id"), Long.parseLong(filters.get("itemPresupuestario.id").toString())));
        }

        if (filters.containsKey("estructuraProgramatica.id")) {
            this.ignoreFilters.add("estructuraProgramatica.id");
            predicates.add(this.builder.equal(root.get("estructuraProgramatica").get("id"), Long.parseLong(filters.get("estructuraProgramatica.id").toString())));
        }

        if (filters.containsKey("id")) {
            this.ignoreFilters.add("id");
            predicates.add(this.builder.like(builder.upper(root.get("id")), filters.get("id").toString().trim().toUpperCase() + "%"));

        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
