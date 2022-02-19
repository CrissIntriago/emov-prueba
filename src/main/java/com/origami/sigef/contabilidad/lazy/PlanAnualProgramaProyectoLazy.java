/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.math.BigInteger;
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
public class PlanAnualProgramaProyectoLazy extends LazyModel<PlanAnualProgramaProyecto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Boolean traerDatosNull;
    private BigInteger num = BigInteger.ZERO;

    public PlanAnualProgramaProyectoLazy() {
        super(PlanAnualProgramaProyecto.class);
        this.getSorteds().put("planAnual", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public PlanAnualProgramaProyectoLazy(OpcionBusqueda busqueda) {
        super(PlanAnualProgramaProyecto.class);
        this.getSorteds().put("planAnual", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public PlanAnualProgramaProyectoLazy(Boolean traerDatosNull, OpcionBusqueda busqueda) {
        super(PlanAnualProgramaProyecto.class);
        this.getSorteds().put("planAnual", "ASC");
        this.busqueda = busqueda;
        this.traerDatosNull = traerDatosNull;
        this.ignoreFilters = new ArrayList<>();
    }

    public PlanAnualProgramaProyectoLazy(OpcionBusqueda busqueda, BigInteger num) {
        super(PlanAnualProgramaProyecto.class);
        this.getSorteds().put("planAnual", "ASC");
        this.num = num;
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
        }

        if (this.num.intValue() > 0) {

            predicates.add(this.builder.equal(root.get("codigoReforma"), this.num));

        }

        if (this.traerDatosNull != null) {
            if (this.traerDatosNull == true) {
                predicates.add(this.builder.equal(root.get("planAnual"), null));
            }
        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String format = simpleDateFormat.format(new Date());
//        predicates.add(this.builder.greaterThanOrEqualTo(root.get("fechaCaducidad"), simpleDateFormat.parse(format)));
        predicates.addAll(this.findPropertyFilter(root, filters));
        //addWhere(predicates, crit);
        return predicates;
    }
}
