/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.util.ReflexionEntity;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;

/**
 *
 * @author dfcalderio
 * @param <T>
 */
@Transactional
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class AbstractService<T> implements Serializable {

    /**
     *
     */
    private final Class<T> entityClass;

    public AbstractService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract EntityManager getEntityManager();

    public T create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        getEntityManager().refresh(entity);
        //tx.commit();
        return entity;
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
        } catch (Exception e) {
            Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, "Remove Entity", e);
        }
    }

    public T find(Object id) {
//        return getEntityManager().find(entityClass, id);
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            Root from = cq.from(entityClass);
            // Fecth Lazy Load
            this.processLazyLoadField(from);
            cq.select(from);
            cq.where(cb.equal(from.get(ReflexionEntity.getNameIdEntity(entityClass)), id));
            return (T) getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root from = cq.from(entityClass);
        this.processLazyLoadField(from);
        cq.select(from);
        return getEntityManager().createQuery(cq).getResultList();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> findAllOrder(Map<String, ? extends String> orders) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = criteriaBuilder.createQuery();
        Root from = cq.from(entityClass);
        this.processLazyLoadField(from);
        CriteriaQuery select = cq.select(from);
        if (orders != null) {
            List<Order> listOrder = new ArrayList<>();
            for (Map.Entry<String, ? extends String> order : orders.entrySet()) {
                if (order.getValue().startsWith("ASC")) {
                    listOrder.add(criteriaBuilder.asc(from.get(order.getKey())));
                } else {
                    listOrder.add(criteriaBuilder.desc(from.get(order.getKey())));
                }
            }
            select.orderBy(listOrder);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root from = cq.from(entityClass);
        this.processLazyLoadField(from);
        cq.select(from);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public BigInteger max(String columnMax) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        Expression<BigInteger> max = rt.get(columnMax);
        BigInteger maxValue = BigInteger.ZERO;
        cq.select(getEntityManager().getCriteriaBuilder().max(max));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        try {
            maxValue = (BigInteger) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("Max " + columnMax + " from " + this.entityClass + ": " + e);
            maxValue = BigInteger.ZERO;
        }
        return maxValue;
    }

    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(final String name, Object... params) {
        javax.persistence.Query query = getEntityManager().createNamedQuery(
                name);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        final List<T> result = (List<T>) query.getResultList();
        return result;
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public <T> T findByNamedQuery1(final String name, Object... params) {
        javax.persistence.Query query = getEntityManager().createNamedQuery(
                name);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        Object result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
        }
        return (T) result;
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public Integer updateHql(final String name, Object... params) {
        javax.persistence.Query query = getEntityManager().createNamedQuery(
                name);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        Integer result = null;
        try {
            result = query.executeUpdate();
        } catch (NoResultException e) {
        }
        return result;
    }

//    public List<T> findByNamedQueryAndNamedParams(final String name,
//            final Map<String, ? extends Object> params) {
//        javax.persistence.Query query = getEntityManager().createNamedQuery(
//                name);
//
//        if (params != null) {
//            params.entrySet().forEach((param) -> {
//                query.setParameter(param.getKey(), param.getValue());
//            });
//        }
//
//        final List<T> result = (List<T>) query.getResultList();
//        return result;
//    }
    public <T> List<T> findObjectByParameterList(Class entity, Map<String, Object> paramt) {
        List<T> ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
            Root from = cq.from(entity);
            cq.select(from);
            Predicate[] restrictions = new Predicate[paramt.size()];
            int c = 0;
            for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                if (entry.getValue().toString().contains("%")) {
                    restrictions[c] = builder.like(from.get(entry.getKey()), entry.getValue().toString().trim());
                } else {
                    restrictions[c] = builder.equal(from.get(entry.getKey()), entry.getValue());
                }
                c++;
            }
            cq.where(restrictions);
            ob = getEntityManager().createQuery(cq).getResultList();
        } catch (NoResultException e) {
            Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, "Remove Entity", e);
            return null;
        }
        return ob;
    }

    public List<T> findByExample(T exampleInstance) {

        TypedQuery<T> query = getQueryByExample(exampleInstance);
        if (query == null) {
            return null;
        } else {
            return query.getResultList();
        }
    }

    private TypedQuery<T> getQueryByExample(T exampleInstance) {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> c = cb.createQuery(entityClass);
            Root<T> r = c.from(entityClass);
            this.processLazyLoadField(r);
            Predicate p = cb.conjunction();
            if (exampleInstance != null) {
                Metamodel mm = getEntityManager().getMetamodel();
                EntityType<T> et = mm.entity(entityClass);
                Set<Attribute<? super T, ?>> attrs = et.getAttributes();

                for (Attribute<? super T, ?> a : attrs) {
                    String name = a.getName();
                    String javaName = a.getJavaMember().getName();
                    String getter = "get"
                            + javaName.substring(0, 1).toUpperCase()
                            + javaName.substring(1);
                    Method m;

                    m = entityClass.getMethod(getter, (Class<?>[]) null);

                    if (m.invoke(exampleInstance, (Object[]) null) != null) {
                        p = cb.and(p, cb.equal(r.get(name), m.invoke(
                                exampleInstance, (Object[]) null)));
                    }
                }
            }
            c.select(r).where(p);
            TypedQuery<T> query = getEntityManager().createQuery(c);
            return query;

        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {

        }

        return null;

    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    private void processLazyLoadField(Root root) {
        /*List<Field> lazyLoad = ReflexionEntity.getFields(entityClass, OneToOne.class, ManyToOne.class, OneToMany.class, ManyToMany.class);
        if (Utils.isNotEmpty(lazyLoad)) {
            lazyLoad.forEach(field -> {
                boolean fecth = false;
                //OneToOne.class, ManyToOne.class, OneToMany.class, ManyToMany.class
                if (field.isAnnotationPresent(OneToOne.class)) {
                    OneToOne o = field.getAnnotation(OneToOne.class);
                    fecth = o.fetch().equals(FetchType.LAZY);
                } else if (field.isAnnotationPresent(ManyToOne.class)) {
                    ManyToOne o = field.getAnnotation(ManyToOne.class);
                    fecth = o.fetch().equals(FetchType.LAZY);
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    OneToMany o = field.getAnnotation(OneToMany.class);
                    fecth = o.fetch().equals(FetchType.LAZY);
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                    ManyToMany o = field.getAnnotation(ManyToMany.class);
                    fecth = o.fetch().equals(FetchType.LAZY);
                }
                if (fecth) {
                    root.fetch(field.getName(), JoinType.LEFT);
                }
            });
        }*/
    }

}
