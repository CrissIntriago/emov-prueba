/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy.view;

import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
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
 * @author ORIGAMI-EC
 */
public class VistaGeneralPlanAnualLazy extends LazyModel<VistaGeneralPlanAnual> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Boolean traerDatosNull;

    public VistaGeneralPlanAnualLazy() {
        super(VistaGeneralPlanAnual.class);
        this.ignoreFilters = new ArrayList<>();
    }

    public VistaGeneralPlanAnualLazy(OpcionBusqueda busqueda) {
        super(VistaGeneralPlanAnual.class);
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public VistaGeneralPlanAnualLazy(Boolean traerDatosNull, OpcionBusqueda busqueda) {
        super(VistaGeneralPlanAnual.class);
        this.busqueda = busqueda;
        this.traerDatosNull = traerDatosNull;
        this.ignoreFilters = new ArrayList<>();
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();

        if (this.traerDatosNull != null) {
            if (this.traerDatosNull == true) {
                predicates.add(this.builder.equal(root.get("nombrePlanLocalProgramaProyecto"), null));
            }
        }
        if (this.busqueda != null) {
            if (this.busqueda.getAnio() != null) {

                predicates.add(this.builder.or(this.builder.equal(root.get("periodoPlanAnualPoliticaPublica"), this.busqueda.getAnio()),
                        this.builder.equal(root.get("periodoPlanAnulaProgramaProyecto"), this.busqueda.getAnio()),
                        this.builder.equal(root.get("periodoActividadOperativa"), this.busqueda.getAnio()),
                        this.builder.equal(root.get("periodoProducto"), this.busqueda.getAnio())
                ));
            }
        }

        predicates.addAll(this.findPropertyFilter(root, filters));
        return predicates;
    }
}
