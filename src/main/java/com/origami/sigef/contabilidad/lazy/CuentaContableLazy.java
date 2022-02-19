/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.lazy;

import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author Dairon Freddy
 */
public class CuentaContableLazy extends LazyModel<CuentaContable> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private OpcionBusqueda busqueda;
    private Short anio;
    private String codigo;
    private String codigoStart = null;
    private String codigoEnd;
    private Boolean diarioGeneral = Boolean.FALSE;

    public CuentaContableLazy() {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
    }

    public CuentaContableLazy(Short anio) {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
    }

    public CuentaContableLazy(OpcionBusqueda busqueda) {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.busqueda = busqueda;
        this.ignoreFilters = new ArrayList<>();
    }

    public CuentaContableLazy(Short anio, String codigo) {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
        this.codigo = codigo;
    }

    public CuentaContableLazy(Short anio, String codigo, String codigoStart, String codigoEnd) {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.anio = anio;
        this.codigo = codigo;
        this.codigoStart = codigoStart;
        this.codigoEnd = codigoEnd;
    }

    public CuentaContableLazy(Boolean diarioGeneral, OpcionBusqueda opcionBusqueda) {
        super(CuentaContable.class);
        this.getSorteds().put("codigo", "ASC");
        this.ignoreFilters = new ArrayList<>();
        this.diarioGeneral = diarioGeneral;
        this.busqueda = opcionBusqueda;
    }

    @Override
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.builder.equal(root.get("estado"), true));
        if (Objects.equals(diarioGeneral, Boolean.TRUE)) {
            predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            predicates.add(this.builder.equal(root.get("estado"), this.diarioGeneral));
            predicates.add(this.builder.notLike(root.get("codigo"), "%11"));
            predicates.add(this.builder.notLike(root.get("codigo"), "%21"));
        }

        if (this.busqueda != null) {
            if (this.busqueda.getAnio() != null) {
                predicates.add(this.builder.equal(root.get("periodo"), this.busqueda.getAnio()));
            }
            if (this.busqueda.getTitulo() != null) {
                predicates.add(this.builder.equal(root.get("titulo"), this.busqueda.getTitulo()));
            }
            if (this.busqueda.getGrupo() != null) {
                predicates.add(this.builder.equal(root.get("grupo"), this.busqueda.getGrupo()));
            }
            if (this.busqueda.getSubGrupo() != null) {
                predicates.add(this.builder.equal(root.get("subGrupo"), this.busqueda.getSubGrupo()));
            }
            if (this.busqueda.getNivel1() != null) {
                predicates.add(this.builder.equal(root.get("cuentaNivel1"), this.busqueda.getNivel1()));
            }
            if (this.busqueda.getNivel2() != null) {
                predicates.add(this.builder.equal(root.get("cuentaNivel2"), this.busqueda.getNivel2()));
            }
            if (this.busqueda.getNivel3() != null) {
                predicates.add(this.builder.equal(root.get("cuentaNivel3"), this.busqueda.getNivel3()));
            }
            if (this.busqueda.getNivel4() != null) {
                predicates.add(this.builder.equal(root.get("cuentaNivel4"), this.busqueda.getNivel4()));
            }
        }

        if (this.anio != null) {
            predicates.add(this.builder.equal(root.get("movimiento"), true));
            predicates.add(this.builder.equal(root.get("periodo"), this.anio));

            if (!this.codigo.isEmpty()) {
                predicates.add(this.builder.like(root.get("codigo"), this.codigo + "%"));
            }
            if (!this.codigoStart.isEmpty() || this.codigoStart != null) {
                if (codigo.equals("911")) {
                    predicates.add(this.builder.like(root.get("nombre"), this.codigoEnd + "%"));
                } else {
                    predicates.add(this.builder.between(root.get("codigo"), this.codigo + this.codigoStart + "%", this.codigo + this.codigoEnd + "%"));
                }
            }
        }

        if (filters.containsKey("codigo")) {
            this.ignoreFilters.add("codigo");
            predicates.add(this.builder.like(builder.upper(root.get("codigo")), filters.get("codigo").toString().trim().toUpperCase() + "%"));
        }
        if (filters.containsKey("periodo")) {
            this.ignoreFilters.add("periodo");
            predicates.add(this.builder.equal(root.get("periodo"), Short.parseShort(filters.get("periodo").toString())));
        }
        predicates.addAll(this.findPropertyFilter(root, filters));
        return predicates;
    }

}
