/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.Servicio;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.SolicitudServicios;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.shiro.session.Session;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class VentanillaService extends AbstractService<ServicioRequisito> {

    private Map<String, Object> param;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public VentanillaService() {
        super(ServicioRequisito.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Object saveAll(Object entity) {

        Object ob = null;
        try {

            ob = save(entity); // RETORNA EL ID DEL OBJETO
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public Object save(Object o) {
        Object ob = null;
        try {
            ob = updateEntity(o);
        } catch (Exception e) {
            return null;
        }
        return ob;
    }

    public Object persist(Object o) {
        em.persist(o);
        return o;
    }

    public Object updateEntity(Object o) {
        Object ob = null;
        try {
            ob = em.merge(o);
        } catch (Exception e) {
            ob = null;
            System.err.println(e);
        }
        return ob;
    }

    public <T> T find(Class<T> entity, Object id) {
        T ob = null;
        try {
            ob = em.find(entity, id);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

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
            return null;
        } catch (HibernateException e) {
            System.err.println(e);
        }
        return ob;
    }

    public <T> T findByParameter(Class entity, Map<String, Object> paramt) {
        T ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            Query q = getEntityManager().createQuery(query);
            try {
                q.setMaxResults(1);
                ob = (T) q.getSingleResult();
            } catch (NoResultException ex) {
                System.out.println("NoResultException " + ex.getMessage());
                return null;
            }
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return ob;
    }

    public List findAllQuery(String query, Map<String, Object> paramt) {
        Session sess;
        Query q;
        List l = null;
        try {

            q = getEntityManager().createQuery(query);
            if (paramt != null) {
                paramt.entrySet().forEach((entrySet) -> {
                    q.setParameter(entrySet.getKey(), entrySet.getValue());
                });
            }
            l = (List) q.getResultList();
            l.size();
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            System.out.println("Error en findAllQuery> " + e.getMessage());
            e.printStackTrace();
        }
        return l;
    }

    public List findAll(Class entity, Map<String, Object> paramt) {

        List ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            Query q = getEntityManager().createQuery(query);
            ob = q.getResultList();
            if (ob != null) {
                ob.size();
                Hibernate.initialize(ob);
            }
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, "No vale el find All", e);
        }
        return ob;
    }

    private Predicate[] getPredicates(Root root, CriteriaBuilder builder, Map<String, Object> filters) {
        if (filters == null) {
            return null;
        }
        List<Predicate> predicates = new ArrayList<>();
        filters.entrySet().forEach((filEntry) -> {
            if (filEntry.getValue() instanceof Date) {
                Date date = (Date) filEntry.getValue();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                date = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                predicates.add(builder.between(root.get(filEntry.getKey()), date, cal.getTime()));
            } else {
                String key = filEntry.getKey();
                Join join = null;
                if (filEntry.getKey().contains(".")) {
                    String[] split = key.split("\\.");
                    int index = 0;
                    try {
                        for (String sp : split) {
                            if (index == 0) { // PRIMER RECORRIDO SETEA CRITERIA PRINCIPAL
                                join = root.join(sp);
                            } else if (index < (split.length - 1)) {
                                join = join.join(sp);
                            } else {
                                if (filEntry.getValue().equals(Date.class)) {
                                    Date date = (Date) filEntry.getValue();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    cal.add(Calendar.SECOND, -1);
                                    predicates.add(builder.between(join.get(sp), date, cal.getTime()));
                                } else {
//                                    if (NumberUtils.isNumber(filEntry.getValue().toString())) {
//                                        predicates.add(builder.equal(join.get(sp), ReflexionEntity.instanceConsString(filEntry.getValue().getClass(), filEntry.getValue().toString().trim()).toString()));
//                                    } else
                                    if (filEntry.getValue() instanceof Object[]) {
                                        predicates.add(join.get(sp).in(((Object[]) filEntry.getValue())));
                                    } else {
                                        predicates.add(builder.equal(join.get(sp), filEntry.getValue()));
                                    }
                                }
                            }
                            index++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    predicates.add(builder.equal(root.get(filEntry.getKey()), filEntry.getValue()));
                }
            }
        });
        return toArrayPredicates(predicates);
    }

    private Predicate[] toArrayPredicates(List<Predicate> predicates) {
        if (Utils.isNotEmpty(predicates)) {
            Predicate[] result = new Predicate[predicates.size()];
            return predicates.toArray(result);
        }
        return null;
    }

    public Object findUnique(String query, String[] par, Object[] val) {
        Object ob = null;
        try {
            ob = find(query, par, val);
            Hibernate.initialize(ob);
        } catch (Exception e) {
            System.err.println(e);
        }
        return ob;
    }

    public Object getNativeQuery(String query, String[] params, Object[] val) {
        Object ob = null;
        try {
            javax.persistence.Query q = getEntityManager().createNativeQuery(query);
            for (int i = 0; i < val.length; i++) {
                q.setParameter(i + 1, val[i]);
            }
            ob = (Object) q.getSingleResult();
            Hibernate.initialize(ob);

        } catch (Exception e) {
            System.err.println(e);
        }
        return ob;
    }

    public Object find(String query, String[] par, Object[] val) {
        Object ob = null;
        try {
            javax.persistence.Query cq = getEntityManager().createQuery(query);
            cq.setMaxResults(1);
            if (par != null) {
                for (int i = 0; i < par.length; i++) {
                    cq.setParameter(par[i], val[i]);
                }
            }
            ob = cq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }

    public boolean update(Object o) {
        boolean flag;
        Object ob = null;
        try {
            em.merge(o);
            flag = true;
        } catch (Exception e) {
            flag = false;
            System.err.println(e);
        }
        return flag;
    }

    public boolean deleteList(List entities) {

        try {
            for (Object entitie : entities) {
                em.remove(entitie);

            }

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    public boolean delete(Object entitie) {
        try {
            em.remove(entitie);
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    public Boolean verificarAsignatura(String abreviatura) {
        try {
            Servicio resultado = (Servicio) em.createQuery("SELECT activo FROM ventanilla.servicio  WHERE abreviatura=:abreviatura AND activo = TRUE")
                    .setParameter("abreviatura", abreviatura)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
        }
        return false;
    }

    public List<UsuarioRol> findAllUsuariosRolByDepartamento(UnidadAdministrativa departamento, Boolean asistente,
            Boolean directorAprobador, Boolean jefeAprobador, Boolean liquidador) {
        String sql;
        Boolean parametro;
        if (asistente != null) {
            sql = "SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r WHERE r.unidadAdministrativa.id= ?1 and ur.asistente= ?2";
            parametro = asistente;
        } else if (directorAprobador != null) {
            sql = "SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r WHERE r.unidadAdministrativa.id= ?1 and ur.directorAprobador= ?2";
            parametro = directorAprobador;
        } else if (jefeAprobador != null) {
            sql = "SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r WHERE r.unidadAdministrativa.id= ?1 and ur.jefeAprobador= ?2";
            parametro = jefeAprobador;
        } else if (liquidador != null) {
            sql = "SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r WHERE r.unidadAdministrativa.id= ?1 and ur.liquidador= ?2";
            parametro = liquidador;
        } else {
            return em.createQuery("SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r WHERE r.unidadAdministrativa.id=:departamento")
                    .setParameter("departamento", departamento.getId()).getResultList();
        }
        System.out.println("id departamento " + departamento.getId());
        return em.createQuery(sql)
                .setParameter(1, departamento.getId()).setParameter(2, parametro).getResultList();

    }

    public List<RegistroSolicitudRequisitos> findAllRegistroRequisitosBySolicitud(SolicitudServicios solicitudServicios) {
        try {
            return em.createQuery("SELECT r FROM RegistroSolicitudRequisitos r WHERE r.solicitudServicios.id=?1")
                    .setParameter(1, solicitudServicios.getId()).getResultList();
        } catch (Exception e) {
            System.out.println("Exception RegistroSolicitudRequisitos " + e.getMessage());
            return null;
        }
    }

    public SolicitudServicios findByIdTramite(HistoricoTramites tramite) {
        List<SolicitudServicios> solicitud = new ArrayList<>();
        SolicitudServicios sol = new SolicitudServicios();
        try {
            solicitud = (List<SolicitudServicios>) em.createQuery("select a from SolicitudServicios a where a.tramite.id =?1").setParameter(1, tramite.getId()).getResultList();
            if (solicitud.size() > 0) {
                sol = solicitud.get(0);
            } else {
                sol = null;
            }
        } catch (Exception ex) {
        }
        return sol;
    }
}
