/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.lazy;

import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author User
 */
public class TareasWFLazy extends LazyModel<TareasActivas> {

    protected String taskDefKey;
    protected String usuario = "admin";
    protected OpcionBusqueda busqueda;

    public TareasWFLazy() {
        super(TareasActivas.class, "numTramite");
        this.setDistinct(false);
        busqueda= new OpcionBusqueda();
    }

    public TareasWFLazy(String user) {
        super(TareasActivas.class, "numTramite");
        this.usuario = user;
        busqueda= new OpcionBusqueda();
        this.setDistinct(false);
    }
    
    public TareasWFLazy(String user,OpcionBusqueda opcionBusqueda) {
        super(TareasActivas.class, "numTramite");
        this.usuario = user;
        busqueda= opcionBusqueda;
        this.setDistinct(false);
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        try {
            if (filters == null) {
                filters = new HashMap<String, Object>();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (usuario != null) {
                predicates.add(this.getOrPredicate(root, Arrays.asList("assignee", "candidate"), Arrays.asList(usuario)));
            }
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
            if (!filters.isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, filters));
            }
            if (!getFilterss().isEmpty()) {
                predicates.addAll(this.findPropertyFilter(root, getFilterss()));
            }
            return predicates;
        } catch (Exception e) {
            Logger.getLogger(TareasWFLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
