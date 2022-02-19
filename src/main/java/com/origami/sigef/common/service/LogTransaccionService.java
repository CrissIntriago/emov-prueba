/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.audit.ShowName;
import com.origami.sigef.common.entities.LogTransaccion;
import com.origami.sigef.common.entities.LogTransaccionDetalle;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class LogTransaccionService extends AbstractService<LogTransaccion> {

    private static final Logger LOG = Logger.getLogger(LogTransaccionService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;

    @Inject
    private Event<LogTransaccion> logEvent;

    public LogTransaccionService() {
        super(LogTransaccion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    /**
     *
     * @param entity Entidad a guardar, actualizar o eliminar
     * @param entity1 Entidad auxiliar cuando va a actualizar
     * @param evento Evento 'UPDATE', 'CREATE', 'REMOVE'
     * @return True si fue con exito el guardado del log, caso contrario false.
     */
    public void saveLog(Object entity, Object entity1, String evento) {
        try {
            LogTransaccion log = new LogTransaccion();
            log.setEntity(entity);
//            if (evento.equalsIgnoreCase("UPDATE") && entity1 == null) {
//                entity1 = find(log.getEntity().getClass(), ReflexionEntity.getIdFromEntity(log.getEntity()));
//            }
            log.setEntity1(entity1);
            log.setEquipo(session.getHostClient());
            log.setEvento(evento);
            log.setFecha(new Date());
            log.setIpEquipo(session.getIpClient());
            if (session.getName() == null) {
                log.setUsuario("System");
            } else {
                log.setUsuario(session.getName());
            }
//                logEvent.fire(log);
            logCreate(log);
        } catch (Exception e) {
            System.out.println("\\\\saveLog getEntity");
            LOG.log(Level.SEVERE, evento, e);
        }
    }

    public void logEntity(@Observes LogTransaccion log) {
        JsonUtil ju = new JsonUtil();
        unproxy(log.getEntity(), null);
        try {
            if (log.getEvento().equalsIgnoreCase("UPDATE")) {
                unproxy(log.getEntity1(), null);
                log.setDatoActual(ju.toJsonNotSerializeNulls(log.getEntity()));
                log.setDatoAnterior(ju.toJsonNotSerializeNulls(log.getEntity1()));
                System.out.println("//Entity: " + log.getEntity());
                System.out.println("//Entity 1: " + log.getEntity1());
                log.setLogDetalles(processDetail(log, log.getEntity(), log.getEntity1()));
            } else {
                log.setDatoActual(ju.toJsonNotSerializeNulls(log.getEntity()));
                log.setLogDetalles(processDetail(log, log.getEntity(), null));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "toJsonNotserializeNulls", e);
        }
        log.setEntidad(log.getEntity().getClass().getName());
        Object idFromEntity = ReflexionEntity.getIdFromEntity(log.getEntity());
        if (idFromEntity != null) {
            BigInteger id = new BigInteger(idFromEntity.toString());
            log.setEntidadId(id);
        }
        this.create(log);
        //getEntityManager().merge(log.getEntity());
        System.out.println("id " + log);
    }

    public void logCreate(LogTransaccion log) {
        JsonUtil ju = new JsonUtil();
//        unproxy(log.getEntity(), null);
        try {
            log.setDatoActual(ju.toJsonNotSerializeNulls(log.getEntity()));
            if (log.getEvento().equalsIgnoreCase("UPDATE")) {
//                unproxy(log.getEntity1(), null);
                log.setDatoAnterior(ju.toJsonNotSerializeNulls(log.getEntity1()));
            }
            log.setLogDetalles(processDetail(log, log.getEntity(), log.getEntity1()));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "toJsonNotserializeNulls " + log.getEntidad() + " with id " + log.getId(), e);
        }
        log.setEntidad(log.getEntity().getClass().getName());
        Object idFromEntity = ReflexionEntity.getIdFromEntity(log.getEntity());
        if (idFromEntity != null) {
            log.setEntidadId(new BigInteger(idFromEntity.toString()));
        }
        if (getEntityManager().isJoinedToTransaction()) {
            getEntityManager().joinTransaction();
        }
        try {
            getEntityManager().persist(log);
//        getEntityManager().refresh(log);
//        this.create(log);
            //getEntityManager().merge(log.getEntity());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Persist Log " + log.getEntidad() + " with id " + log.getId(), e);
        }
    }

    /**
     * Los campos que provocan ciclo circular se le sea null.
     *
     * @param entity Entiti a procesar
     * @param ignors Lista de class a ignorar.
     */
    private void unproxy(Object entity, List ignors) {
        if (entity == null) {
            return;
        }
        List<Field> fields = null;
        try {
            fields = ReflexionEntity.getFields(entity.getClass(), OneToMany.class, OneToOne.class, ManyToOne.class);
        } catch (Exception e) {
            System.out.println("Get Fields " + e);
            return;
        }
        if (ignors == null) {
            ignors = new ArrayList();
        }
        ignors.add(entity.getClass());
        if (Utils.isNotEmpty(fields)) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object get = field.get(entity);
                    if (get != null) {
                        if (get instanceof Collection) {
                            Collection l = (Collection) get;
//                            System.out.println(field.getName() + " field " + field.getType());
                            for (Object object : l) {
                                unproxy(object, ignors);
                            }
                        } else {
//                            System.out.println(field.getName() + " field " + field.getType() + " >> " + ignors.contains(field.getType()));
                            if (ignors.contains(field.getType())) {
                                field.set(entity, null);
                            } else {
                                unproxy(get, ignors);
                            }
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(LogTransaccionService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private List<LogTransaccionDetalle> processDetail(LogTransaccion log, Object entity, Object entity11) {
        try {
            List<Field> fields = ReflexionEntity.getFields(entity.getClass(), (Class[]) null);
            List<LogTransaccionDetalle> detalles = new ArrayList<>(fields.size());
            for (Field field : fields) {
                if (field.getModifiers() == Modifier.PRIVATE
                        || field.getModifiers() == Modifier.PROTECTED
                        || field.getModifiers() == Modifier.PUBLIC) {
                    field.setAccessible(true);
                    if (entity11 == null) {
                        LogTransaccionDetalle e = new LogTransaccionDetalle();
                        e.setCampo(field.getName());
                        ShowName nameField = field.getAnnotation(ShowName.class);
                        if (nameField != null) {
                            e.setTitulo(nameField.name().toUpperCase());
                        } else {
                            e.setTitulo(getNameField(field.getName()));
                        }
                        e.setDatoActual(field.get(entity) + "");
                        e.setDatoAnterior("");
                        e.setLogTransaccion(log);
                        detalles.add(e);
                    } else { // UPDATE
                        Object value = field.get(entity);
                        Object value1 = field.get(entity11);
                        if (value instanceof Collection) {

                        } else {
                            if (!Objects.equals(value, value1)) {
                                LogTransaccionDetalle e = new LogTransaccionDetalle();
                                e.setCampo(field.getName());
                                ShowName nameField = field.getAnnotation(ShowName.class);
                                if (nameField != null) {
                                    e.setTitulo(nameField.name());
                                } else {
                                    e.setTitulo(getNameField(field.getName()));
                                }
                                e.setDatoActual((value == null ? "" : value) + "");
                                e.setDatoAnterior((value1 == null ? "" : value1) + "");
                                e.setLogTransaccion(log);
                                detalles.add(e);
                            }
                        }
                    }
                }
            }
            return detalles;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return null;
    }

    private String getNameField(String name) {
        String[] r = name.split("(?=\\p{Lu})");
        String campo = "";
        for (String value : r) {
            campo += value + " ";
        }
        return campo.trim().toUpperCase();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object find(Class entityClass, Object id) {
        try {
            if (getEntityManager().isJoinedToTransaction()) {
                getEntityManager().joinTransaction();
            }
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            Root from = cq.from(entityClass);
            cq.select(from);
            cq.where(cb.equal(from.get(ReflexionEntity.getNameIdEntity(entityClass)), id));
            return getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
