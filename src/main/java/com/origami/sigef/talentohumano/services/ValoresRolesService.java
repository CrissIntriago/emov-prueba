package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Pozo G
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class ValoresRolesService extends AbstractService<ValoresRoles> {
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public ValoresRolesService() {
        super(ValoresRoles.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<ValoresRoles> FindValoresRolesXRol(RolesDePago rol) {
        try {
            Query query = em.createQuery("SELECT v FROM ValoresRoles v WHERE v.rolPago = ?1 AND v.estado = TRUE ORDER BY v.partidaAp ASC ")
                    .setParameter(1, rol);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public ValoresRoles FindValoresTipo(RolesDePago rol, String cod) {
        try {
            Query query = em.createQuery("SELECT v FROM ValoresRoles v JOIN v.valorParametrizable.tipo tp WHERE v.rolPago = ?1 AND tp.codigo = ?2 AND v.estado = TRUE ORDER BY v.partidaAp ASC ")
                    .setParameter(1, rol).setParameter(2, cod);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ValoresRoles> FindValoresIngresoRolesXRol(RolesDePago rol) {
        try {
            Query query = em.createQuery("SELECT DISTINCT v FROM ValoresRoles v Join v.valorParametrizable vp WHERE v.rolPago = ?1 AND v.estado = TRUE  AND v.partidaAp IS NOT NULL AND vp.origen.codigo ='DIS-GEN' AND vp.clasificacion.codigo = 'I' ORDER BY v.partidaAp ASC ")
                    .setParameter(1, rol);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ValoresRoles> FindValoresXServidor(Servidor ser,Short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT v FROM ValoresRoles v WHERE v.rolPago.servidor = ?1 AND v.periodo = ?2 AND v.estado = TRUE ORDER BY v.partidaAp ASC ")
                    .setParameter(1, ser).setParameter(2,anio);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ValoresRoles> valorRolDescuento(RolesDePago rol) {
        try {
            Query query = em.createQuery("SELECT vr FROM ValoresRoles vr JOIN vr.rolPago r JOIN vr.valorParametrizable vp "
                    + "JOIN vp.tipo c where (c.codigo = 'RET_JUD' OR c.codigo = 'OTROS_E') AND vr.estado = TRUE AND vr.rolPago=?1")
                    .setParameter(1, rol);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal;
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<ValoresRoles> valorRolOnlyOtrosEgresos(RolesDePago rol) {
        try {
            Query query = em.createQuery("SELECT vr FROM ValoresRoles vr JOIN vr.rolPago r JOIN vr.valorParametrizable vp "
                    + "JOIN vp.tipo c where c.codigo = 'OTROS_E' AND vr.estado = TRUE AND vr.rolPago=?1")
                    .setParameter(1, rol);
            List<ValoresRoles> listVal = (List<ValoresRoles>) query.getResultList();
            return listVal;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public ValoresRoles FindValoresRolesXServidorXperiodo(Servidor s, short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT v FROM ValoresRoles v JOIN v.rolPago r "
                    + "JOIN v.valorParametrizable p JOIN p.tipo c WHERE v.estado = TRUE AND v.periodo = ?2 AND c.codigo = 'RET_JUD' AND r.servidor = ?1  ")
                    .setParameter(1, s)
                    .setParameter(2, anio);
            ValoresRoles result = (ValoresRoles) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ValoresRoles> rubrosTipoOrigen(short anio, RolesDePago rol, String cod1) {
        try {
            Query query = em.createQuery("SELECT v FROM ValoresRoles v JOIN v.valorParametrizable vp "
                    + "WHERE v.estado = TRUE AND v.periodo=?1 AND v.rolPago =?2 AND vp.estado = TRUE AND vp.origen.codigo = ?3")
                    .setParameter(1, anio)
                    .setParameter(2, rol)
                    .setParameter(3, cod1);
            List< ValoresRoles> result = (List< ValoresRoles>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
}
