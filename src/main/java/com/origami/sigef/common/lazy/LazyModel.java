/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.lazy;

import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Criteria;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ANGEL NAVARRO
 * @param <T>
 */
@SuppressWarnings("serial")
public class LazyModel<T extends Object> extends LazyDataModel<T> {

    protected volatile EntityManager manager;
    @SuppressWarnings("rawtypes")
    protected Class entity;
    protected CriteriaBuilder builder;
    @SuppressWarnings("rawtypes")
    protected Root root;
    protected int rowCount = 0;
    protected String defaultSorted = "id";
    protected String defaultSortOrder = "ASC";
    protected Map<String, Object> filterss;
    protected List<String> ignoreFilters;
    protected Map<String, String> Sorteds;
    private Map<String, FilterMatchMode> filterMatchMode;
    private boolean distinct = true;
    private List<T> listaCarga;

    /**
     * Lazy por defecto que realiza la busqueda por demanda
     *
     * @param entityClass Class de la entidad
     */
    public LazyModel(Class<T> entityClass) {
        try {
            EntityManagerService managerService = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
            manager = managerService.getManager();
            this.entity = entityClass;
            builder = manager.getCriteriaBuilder();
        } catch (NamingException ex) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LazyModel(List<T> entityClass) {
        try {
            EntityManagerService managerService = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
            manager = managerService.getManager();
            builder = manager.getCriteriaBuilder();
        } catch (NamingException ex) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lazy por defecto que realiza la busqueda por demanda
     *
     * @param entity Entity a la que se hacer lazy
     * @param fieldSorted Propiedad por defecto para realizar el order by
     */
    public LazyModel(Class<T> entity, String fieldSorted) {
        try {
            this.entity = entity;
            this.defaultSorted = fieldSorted;
            EntityManagerService managerService = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
            manager = managerService.getManager();
            builder = manager.getCriteriaBuilder();
        } catch (NamingException ex) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lazy por defecto que realiza la busqueda por demanda
     *
     * @param entity Entity a la que se hacer lazy
     * @param defaultSorted Propiedad por defecto para realizar el filtro
     * @param defaultSortOrder Propiedad por defecto para realizar el ordenado
     */
    public LazyModel(Class<T> entity, String defaultSorted, String defaultSortOrder) {
        try {
            this.entity = entity;
            this.defaultSorted = defaultSorted;
            this.defaultSortOrder = defaultSortOrder;
            EntityManagerService managerService = (EntityManagerService) new InitialContext().lookup("java:module/EntityManagerService");
            manager = managerService.getManager();
            builder = manager.getCriteriaBuilder();
        } catch (NamingException ex) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Filtro que se realizara cada vez que se realize la busqueda
     *
     * @param crit {@link Criteria} de hibernate para realizar las consulta a la
     * base de datos
     * @param filters key nombre de la propiedad que se realizara la busqueda,
     * value valor del filtro
     * @return
     * @throws Exception se lanza error para el caso que lo halla
     *
     * <p>
     * Ver especificaciones {@link LazyModel#getFilterss()
     * </p>
     */
    @SuppressWarnings("rawtypes")
    public List<Predicate> criteriaFilterSetup(CriteriaQuery crit, Map<String, Object> filters) throws Exception {
        List<Predicate> predicates = new ArrayList<>();
        try {
            if (this.filterss != null) {
                predicates.addAll(this.findPropertyFilter(root, this.filterss));
            }
            // LLAMAMOS EL ME  PARA REALIZAR LOS FILTER
            predicates.addAll(this.findPropertyFilter(root, filters));
            return predicates;
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected void addWhere(List<Predicate> predicates, CriteriaQuery crit) {
        if (Utils.isNotEmpty(predicates)) {
            Predicate[] result = new Predicate[predicates.size()];
            crit.where(predicates.toArray(result));
        }
    }

    /**
     * Implementacion del load del lazy
     *
     * @param first primer registro
     * @param pageSize Tamanio de registro de cada pagina
     * @param sortField Campos por los que se realizar el ordenamiento
     * @param sortOrder tipo de Ordenamiento en que se realizaran el
     * ordenamiento
     * @param filters key nombre de la propiedad que se realizara la busqueda,
     * value valor del filtro
     * @return lista de registro con el tamanio de pagina
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List result = null;
        result = new ArrayList();
        try {
            getFiltersMatchMode();
            ignoreFilters = new ArrayList<>();
            //CREAMOS UN CRITERIA QUERY PARA EL CONTEO DE LOS REGISTROS
            String pk = ReflexionEntity.getNameIdEntity(entity);
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            // CREAMOS EL SELECT DE LA ENTITY
            root = countQuery.from(entity);
            countQuery = countQuery.select(builder.countDistinct(root.get(pk))); //CREAMOS EL ROOT ENTITY
            // Agregamos las clausulas where
            addWhere(this.criteriaFilterSetup(countQuery, filters), countQuery);
            // OBTENEMOS ELCONTEO
            rowCount = manager.createQuery(countQuery.distinct(distinct)).getSingleResult().intValue();
//            System.out.println("RowCount "+rowCount);
            // CREAMOS EL QUERY PARA LOS DATOS
            CriteriaQuery<Object> query = builder.createQuery(entity);
            //CREAMOS EL ROOT ENTITY
            root = query.from(entity);
            // SELECT DE LA ENTITY
            query.select(root);
            // Agregamos las clausulas where
            addWhere(this.criteriaFilterSetup(query, filters), query);
            // ORDENAMOS LOS REGISTROS
            this.criteriaSortSetup(query, sortField, sortOrder);
            query.distinct(distinct);
            result.add(entity);
            result = this.criteriaPageSetup(query, first, pageSize).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, this.getEntity().getName(), ex);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public T getRowData(Object key) {
        T ob = null;
        try {
            ob = (T) manager.find(entity, key);
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public T getRowData(String rowKey) {
        T ob = null;
        Object x = rowKey;
        try {
            if (NumberUtils.isDigits(rowKey)) {
                ob = (T) manager.find(entity, Long.parseLong(rowKey));
            } else {
                ob = (T) manager.find(entity, rowKey);
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        try {
            if (rowIndex == -1 || getPageSize() == 0) {
                super.setRowIndex(-1);
            } else {
                super.setRowIndex(rowIndex % getPageSize());
            }
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @SuppressWarnings("rawtypes")
    public TypedQuery criteriaPageSetup(CriteriaQuery crit, int first, int pageSize) {
        try {
            TypedQuery typedQuery = this.manager.createQuery(crit);
            typedQuery.setFirstResult(first);
            typedQuery.setMaxResults(pageSize);
            return typedQuery;
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public void criteriaSortSetup(CriteriaQuery crit, String field, SortOrder order) {
        try {
            if (field != null) {
                crit.orderBy((order == SortOrder.ASCENDING) ? builder.asc(root.get(field))
                        : builder.desc(root.get(field)));
            } else if (!getSorteds().isEmpty()) {
                List<Order> orders = new ArrayList<>(getSorteds().size());
                for (Map.Entry<String, String> entry : getSorteds().entrySet()) {
                    if (entry.getValue().toUpperCase().startsWith("ASC")) {
                        orders.add(builder.asc(this.getExpression(root, entry.getKey(), JoinType.LEFT)));
                    } else {
                        orders.add(builder.desc(this.getExpression(root, entry.getKey(), JoinType.LEFT)));
                    }
                }
                crit.orderBy(orders);
            } else {
                crit.orderBy((defaultSortOrder.equalsIgnoreCase("ASC")) ? builder.asc(root.get(defaultSorted))
                        : builder.desc(root.get(defaultSorted)));
            }
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @SuppressWarnings("rawtypes")
    public void criteriaSortSetup(CriteriaQuery crit, List<SortMeta> sortMetas) {
        try {
            if (Utils.isEmpty(sortMetas)) {
                sortMetas.forEach((sortMeta) -> {
                    if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
                        crit.orderBy(builder.asc(root.get(defaultSorted)));
                    } else {
                        crit.orderBy(builder.desc(root.get(defaultSorted)));
                    }
                });
            }
        } catch (Exception e) {
            Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, null, e);
        }
    }
//<editor-fold defaultstate="collapsed" desc="PEDICATES">

    /**
     * Realiza la busqueda de la propiedad que esta en el filter si hay una
     * propieda que esta dentro de otra entity entonces se obtiene la entity y
     * se agrega ese criteria al criteria principal
     *
     * @param crit {@link Criteria} para realizar la cunsulta a la db
     * @param filters {@link Map} con los filter ingresados en el dataTable
     * @return Listado de predicados
     */
    @SuppressWarnings("rawtypes")
    protected List<Predicate> findPropertyFilter(Root crit, Map<String, Object> filters) {

        List<Predicate> predicates = new ArrayList<>();
        if (filters == null) {
            return predicates;
        }
        filters.entrySet().forEach((Map.Entry<String, Object> entry) -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (ignoreFilters == null) {
                ignoreFilters = new ArrayList<>();
            }
            if (!ignoreFilters.contains(key)) {
                // Si es una clave Conpuesta solo se realiza envia busca esa propiedad en la misma entity
                // No soporta relaciones de entity con clave compuestas.
                Predicate findProperty = this.findProperty(crit, key, value);
                if (findProperty != null) {
                    predicates.add(findProperty);
                }
            }
        });
        return predicates;
    }

    /**
     * Realiza el recorrido realizando un split sobre el nombre del campo.
     *
     * @param crit Criteria
     * @param key Campo en para realizar el ordenamiento
     * @return Criteria para el campo solicitado.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Predicate findProperty(Root crit, String key, Object value) {
        Join join = null;
        String filterFuntion = null;
        FilterMatchMode fieldFilterMatchMode = null;
        String keyAux = null;
        //System.out.println("filterMatchMode  " + filterMatchMode);
        if (filterMatchMode != null) {
            fieldFilterMatchMode = filterMatchMode.get(key);
        } else {
            fieldFilterMatchMode = FilterMatchMode.CONTAINS;
        }

        if (key.contains(":")) {
            String[] split = key.split(":");
            key = split[0];
            filterFuntion = split[1];
            if (split.length > 2) {
                keyAux = split[2];
            }
        }
        // Si es una clave Conpuesta solo se realiza envia busca esa propiedad en la misma entity
        // No soporta relaciones de entity con clave compuestas.
        if (key.contains(".") && !key.contains("PK")) {
            String[] split = key.split("\\.");
            int index = 0;
            try {
                for (String sp : split) {
                    if (index == 0) { // PRIMER RECORRIDO SETEA CRITERIA PRINCIPAL
                        join = crit.join(sp);
                    } else if (index < (split.length - 1)) {
                        join = join.join(sp);
                    } else {
                        Class type = (Class) ReflexionEntity.getTypeObject(LazyModel.this.entity, key);
                        key = sp;
                        return getPredicateField(sp, type, join, value, filterFuntion, fieldFilterMatchMode);
                    }
                    index++;
                }
            } catch (Exception e) {
                Logger.getLogger(LazyModel.class.getName()).log(Level.SEVERE, "findProperty key " + key, e);
            }
        } else {
            if (filterFuntion != null) {
                if ((filterFuntion.equalsIgnoreCase("notEqual")
                        || filterFuntion.equalsIgnoreCase("noEqual")) && value == null) {
                    return builder.isNotNull(root.get(key));
                } else if (filterFuntion.equalsIgnoreCase("equal") && value == null) {
                    return builder.isNull(root.get(key));
                } else if (filterFuntion.equalsIgnoreCase("or")) {
                    List<String> fields = new ArrayList<>();
                    fields.add(key);
                    if (keyAux.contains(";")) {
                        fields.addAll(Arrays.asList(keyAux.split(";")));
                    }
                    if (value instanceof Collection) {
                        return getOrPredicate(root, fields, (List) value);
                    } else {
                        return getOrPredicate(root, fields, Arrays.asList(value));
                    }
                } else {
                    Class type = (Class) ReflexionEntity.getTypeObject(LazyModel.this.entity, key);
                    return getPredicateField(key, type, root, value, filterFuntion, fieldFilterMatchMode);
                }
            } else {
                Class type = (Class) ReflexionEntity.getTypeObject(LazyModel.this.entity, key);
                return getPredicateField(key, type, root, value, filterFuntion, fieldFilterMatchMode);
            }
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Predicate getPredicateField(String key, Class type, From join, Object value, String filterFuntion, FilterMatchMode fieldFilterMatchMode) {
        if (value == "") {
            return null;
        }
        if (filterFuntion != null && filterFuntion.equalsIgnoreCase("between")) {
            if (value instanceof Collection) {
                List<Comparable> dates = (List<Comparable>) value;
                return builder.between(join.get(key), dates.get(0), dates.get(1));
            }
        }
//        System.out.println("Value " + value + " type " + type);
        if (value instanceof Collection) {
            if (filterFuntion != null) {
                if (filterFuntion.equalsIgnoreCase("notEqual")) {
                    fieldFilterMatchMode = FilterMatchMode.NOT_EQUAL;
                }
            } else {
                fieldFilterMatchMode = FilterMatchMode.EXACT;
            }
            if (fieldFilterMatchMode == null) {
                fieldFilterMatchMode = FilterMatchMode.EXACT;
            }
            switch (fieldFilterMatchMode) {
                case NOT_EQUAL:
                    return builder.not(join.get(key).in((Collection) value));
                default:
                    return join.get(key).in((Collection) value);
            }
        } else if (value instanceof Object[]) {
            Object[] values = (Object[]) value;
            if (values[0] instanceof String) {
                Collection c = new ArrayList(values.length);
                for (Object data : (Object[]) value) {
                    c.add(ReflexionEntity.instanceConsString((Class) type, data.toString()));
                }
                return join.get(key).in(c);
            } else {
                return join.get(key).in(((Object[]) value));
            }
        } else if (value instanceof Date) {
            if (filterFuntion != null) {
                if (filterFuntion.equalsIgnoreCase("equal")) {
                    fieldFilterMatchMode = FilterMatchMode.EXACT;
                } else if (filterFuntion.equalsIgnoreCase("notEqual")) {
                    fieldFilterMatchMode = FilterMatchMode.NOT_EQUAL;
                } else {
                    fieldFilterMatchMode = FilterMatchMode.fromUiParam(filterFuntion);
                }
            }
//            System.out.println(key + " Fechas " + value + " fieldFilterMatchMode " + fieldFilterMatchMode);
            if (value instanceof Collection) {
                List dates = (List) value;
                if (dates.size() > 1) {
                    return builder.between(join.get(key), (Comparable) dates.get(0), (Comparable) dates.get(1));
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) dates.get(0));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    cal.add(Calendar.SECOND, -1);
                    return this.builder.between(join.get(key), (Date) dates.get(0), cal.getTime());
                }
            } else {
                if (fieldFilterMatchMode == null) {
                    fieldFilterMatchMode = FilterMatchMode.EQUALS;
                }
                Date date = (Date) value;
                switch (fieldFilterMatchMode) {
                    case LESS_THAN:
                        return builder.lessThan(join.get(key), date);
                    case LESS_THAN_EQUALS:
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(date);
                        cal1.add(Calendar.DAY_OF_MONTH, 1);
                        cal1.add(Calendar.SECOND, -1);
                        return builder.lessThanOrEqualTo(join.get(key), cal1.getTime());
                    case GREATER_THAN:
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(date);
                        cal2.add(Calendar.DAY_OF_MONTH, 1);
                        cal2.add(Calendar.SECOND, -1);
                        return builder.greaterThan(join.get(key), date);
                    case GREATER_THAN_EQUALS:
                        return builder.greaterThanOrEqualTo(join.get(key), date);
                    case NOT_EQUAL:
                        return builder.notEqual(join.get(key), date);
                    default:
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        cal.add(Calendar.SECOND, -1);
                        return this.builder.between(join.get(key), date, cal.getTime());
                }
            }
        } else if (type.equals(String.class) || type.equals(Character.class)) {
            String val = (value == null? "" : (value.toString().trim().toUpperCase()));
            if (filterFuntion != null) {
                if (filterFuntion.equalsIgnoreCase("equal")) {
                    fieldFilterMatchMode = FilterMatchMode.EXACT;
                } else if (filterFuntion.equalsIgnoreCase("notEqual")) {
                    fieldFilterMatchMode = FilterMatchMode.NOT_EQUAL;
                } else {
                    fieldFilterMatchMode = FilterMatchMode.fromUiParam(filterFuntion);
                }
            }
            if (fieldFilterMatchMode == null) {
                fieldFilterMatchMode = FilterMatchMode.CONTAINS;
            }
            switch (fieldFilterMatchMode) {
                case STARTS_WITH:
                    val = val + "%";
                    return builder.like(builder.upper(join.get(key)), val);
                case ENDS_WITH:
                    val = "%" + val;
                    return builder.like(builder.upper(join.get(key)), val);
                case CONTAINS:
                    val = "%" + val + "%";
                    return builder.like(builder.upper(join.get(key)), val);
                case NOT_EQUAL:
                    return builder.notEqual(builder.upper(join.get(key)), val);
                default:
                    return builder.equal(builder.upper(join.get(key)), val);
            }
        } else if (NumberUtils.isDigits(value.toString())) {
            Number val = (Number) ReflexionEntity.instanceConsString((Class) type, value.toString().trim());
            if (filterFuntion != null) {
                if (filterFuntion.equalsIgnoreCase("equal")) {
                    fieldFilterMatchMode = FilterMatchMode.EXACT;
                } else if (filterFuntion.equalsIgnoreCase("notEqual")) {
                    fieldFilterMatchMode = FilterMatchMode.NOT_EQUAL;
                } else {
                    fieldFilterMatchMode = FilterMatchMode.fromUiParam(filterFuntion);
                }
            }
            if (fieldFilterMatchMode == null) {
                fieldFilterMatchMode = FilterMatchMode.EXACT;
            }
            switch (fieldFilterMatchMode) {
                case LESS_THAN:
                    return builder.lessThan(join.get(key), (Comparable) val);
                case LESS_THAN_EQUALS:
                    return builder.lessThanOrEqualTo(join.get(key), (Comparable) val);
                case GREATER_THAN:
                    return builder.greaterThan(join.get(key), (Comparable) val);
                case NOT_EQUAL:
                    return builder.notEqual(join.get(key), (Comparable) val);
                case GREATER_THAN_EQUALS:
                    return builder.greaterThanOrEqualTo(join.get(key), (Comparable) val);
                default:
                    return builder.equal(join.get(key), ReflexionEntity.instanceConsString((Class) type, value.toString().trim()));
            }
        } else {
            return builder.equal(join.get(key), value);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Expression getExpression(Root crit, String key) {
        if (key.contains(".") && !key.contains("PK")) {
            Join join = null;
            String[] split = key.split("\\.");
            int index = 0;
            for (String sp : split) {
                if (index == 0) {
                    join = crit.join(sp);
                } else if (index < (split.length - 1)) {
                    join = join.join(sp);
                } else {
                    return join.get(sp);
                }
                index++;
            }
        } else {
            return crit.get(key);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    protected Expression getExpression(Root crit, String key, JoinType joinType) {
        if (key.contains(".") && !key.contains("PK")) {
            Join join = null;
            String[] split = key.split("\\.");
            int index = 0;
            for (String sp : split) {
                if (index == 0) {
                    join = crit.join(sp, joinType);
                } else if (index < (split.length - 1)) {
                    join = join.join(sp, joinType);
                } else {
                    return join.get(sp);
                }
                index++;
            }
        } else {
            return crit.get(key);
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Predicate getOrPredicate(From from, List<String> field2, List orList) {
        if (orList != null) {
            Predicate[] p = new Predicate[orList.size()];
            int index = 0;
            for (Object or : orList) {
                if (or instanceof String) {
                    Predicate[] pf = new Predicate[field2.size()];
                    int indexf = 0;
                    for (String field : field2) {
                        pf[indexf] = this.builder.like(builder.upper(from.get(field)), "%".concat(or.toString().toUpperCase()).concat("%"));
                        indexf++;
                    }
                    p[index] = this.builder.or(pf);
                } else {
                    Predicate[] pf = new Predicate[field2.size()];
                    int indexf = 0;
                    for (String field : field2) {
                        pf[indexf] = this.builder.equal(from.get(field), or);
                        indexf++;
                    }
                    p[index] = this.builder.or(pf);
                }
                index++;
            }
            return this.builder.or(p);
        }
        return null;
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntity() {
        return entity;
    }

    public String getDefaultSorted() {
        return defaultSorted;
    }

    public void setDefaultSorted(String defaultSorted) {
        this.defaultSorted = defaultSorted;
    }

    public String getDefaultSortOrder() {
        return defaultSortOrder;
    }

    public void setDefaultSortOrder(String defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
    }

    /**
     * Aqui puede agregar los filtro ejemplos de filtros sobre las propiedades
     * de la entidad:
     *
     * <ul>
     * <li>Normal: getFilterss().put("orden", 1)</li>
     * <li>Objeto: getFilterss().put("modulo", new Modulo(1L))</li>
     * <li>Join: getFilterss().put("modulo.orden", 1)</li>
     * <li>In list: getFilterss().put("modulo.orden", Arrays.asList(1,2,3))</li>
     * <li>In list: getFilterss().put("campo1:or:campo2",
     * Arrays.asList(1,2,3))</li>
     * <li>In list: getFilterss().put("modulo", Arrays.asList(new Modulo(1L),new
     * Modulo(2L)))</li>
     * </ul>
     * Ejemplo con equal, notEqual Between
     * <ul>
     * <li>NotEqual: getFilterss().put("funcionario:notEqual", null);</li>
     * <li>Equal: getFilterss().put("funcionario:equal", null);</li>
     * <li>Between: lazyModel.getFilterss().put("fechaConstatacion",
     * Arrays.asList(getFechaDesde(), getFechaHasta()));</li>
     * </ul>
     *
     * @return Mapa de parametro.
     */
    public Map<String, Object> getFilterss() {
        if (filterss == null) {
            filterss = new HashMap<>();
        }
        return filterss;
    }

    /**
     * Valor de los filtros ingresados <code>filterss</code>
     *
     * @param filterss El key es el nombre de la propiedad y el value el valor
     * del campo
     * <p>
     * Ver especificaciones {@link LazyModel#getFilterss()}
     * </p>
     */
    public void setFilterss(Map<String, Object> filterss) {
        this.filterss = filterss;
    }

    public Map<String, String> getSorteds() {
        if (Sorteds == null) {
            Sorteds = new LinkedHashMap<>();
        }
        return Sorteds;
    }

    public void setSorteds(Map<String, String> Sorteds) {
        this.Sorteds = Sorteds;
    }

//</editor-fold>
    @Override
    public int getRowCount() {
        return rowCount;
    }

    public Date getFechaHasta(Date actual) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(actual);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * Reemplaza el EntityManager que tiene por defecto.
     *
     * @param manager Entity manager.
     */
    public void setManager(EntityManager manager) {
        this.manager = manager;
        this.builder = this.manager.getCriteriaBuilder();
    }

    /**
     * Starting from the view root, which identifies the datatable to retrieve
     * information from, Map filterMatchMode, containing pairs of
     * {@code <filtered field id, match mode>}. Filtered field id is evaluated
     * from the 'filterBy' attribute of the column in the following way:
     * #{item.name} -> name #{item.category.name} -> category.name
     */
    public void getFiltersMatchMode() {
        if (filterMatchMode == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (UIComponent.getCurrentComponent(facesContext) instanceof DataTable) {
                DataTable table = (DataTable) UIComponent.getCurrentComponent(facesContext);
                filterMatchMode = new HashMap<>(table.getColumns().size());
                for (UIColumn column : table.getColumns()) {
                    ValueExpression filterExpression = column.getValueExpression("filterBy");
                    if (null != filterExpression) {
                        String filterExpressionString = filterExpression.getExpressionString();
                        String filteredField = filterExpressionString.substring(
                                filterExpressionString.indexOf('.') + 1,
                                filterExpressionString.indexOf('}'));
                        FilterMatchMode matchMode = FilterMatchMode.fromUiParam(column.getFilterMatchMode());
                        filterMatchMode.put(filteredField, matchMode);
                    }
                }
            }
        }
    }
 public void addFilter(String field, Object b) {
        getFilterss().put(field, b);
    }

    public void addSorted(String field, String b) {
        getSorteds().put(field, b);
    }
}