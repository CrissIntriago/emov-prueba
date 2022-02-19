/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ThServidorService extends AbstractService<Servidor> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ThServidorService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ThServidorService() {
        super(Servidor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    private String getQueryHQLExisteServidor(Servidor s) {
        String query = null;
        if (s.getPersona().getIdentificacion() != null) {
            return "SELECT p FROM Servidor p JOIN p.persona pp where pp.identificacion = :identificacion AND p.estado = TRUE";
        }
        return query;
    }

    public List<Servidor> getListadoServidores() {
        //"SELECT pr from ProcedimientoRequisito WHERE pr.idProcedimiento=:procedimiento"
        List<Servidor> resultado = (List<Servidor>) em.createQuery("SELECT s FROM Servidor s WHERE s.estado = TRUE")
                .getResultList();
        return resultado;
    }

    public Servidor existeServidor(Servidor s) {
        String queryString = getQueryHQLExisteServidor(s);

        Map<String, Object> params = getParameter(s);

        final Query query = em.createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<Servidor> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(Servidor s) {

        Map<String, Object> params = new HashMap<>();
        if (s.getPersona().getIdentificacion() != null) {
            params.put("identificacion", s.getPersona().getIdentificacion());
        }
        return params;
    }

    public Servidor findByIdentificacion(String identificacion) {
        try {
            return this.findByNamedQuery1("Servidor.findByPersonaIdentificacion", identificacion);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, identificacion, e);
        }
        return null;
    }

    public Boolean findByServidor(String identificacion) {
        Servidor servidor = (Servidor) em.createQuery("SELECT s FROM Servidor s INNER JOIN s.persona p WHERE p.identificacion=?1 AND s.estado=TRUE")
                .setParameter(1, identificacion)
                .getResultStream().findFirst().orElse(null);
        return servidor != null;
    }

    public Cliente findByCliente(String id) {
        try {
            Cliente c;
            Query query = em.createQuery("SELECT c From Cliente c WHERE c.identificacion= ?1 AND c.estado = TRUE")
                    .setParameter(1, id);
            return c = (Cliente) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public Servidor findByPersonaIdentificacionProv(String id) {
        return this.findByNamedQuery1("Servidor.findByPersonaIdentificacionProv", id);
    }

    public List<Servidor> findServidorDistributivo() {
        try {
            Query query = em.createQuery("SELECT DISTINCT s from Servidor s JOIN s.distributivo d  where d.servidorPublico is not null and s.estado = TRUE and d.estado = TRUE");
            List<Servidor> listser = (List<Servidor>) query.getResultList();
            return listser;
        } catch (NoResultException e) {
            return null;
        }
    }

    //traer servidores que se encuentran en roles y en un periodo especifico and periodos *
    public List<Servidor> findServidorRoles(short periodo) {
        try {
            Query query = em.createQuery("SELECT r.servidor from RolesDePago r JOIN r.servidor s WHERE s.estado = TRUE and r.estado = true and r.periodo=?1 ")
                    .setParameter(1, periodo);
            List<Servidor> listser = (List<Servidor>) query.getResultList();
            return listser;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Servidor> findServidorNoRolesAndPe(short anio) {
        try {
            Query query = em.createQuery("SELECT s FROM DistributivoEscala de JOIN de.distributivo d JOIN d.servidorPublico s WHERE s.estado = true AND d.servidorPublico IS NOT NULL AND de.anio = ?1 AND s.id not in ( SELECT r.servidor FROM RolesDePago r where r.estado = true and r.periodo= ?1 )")
                    .setParameter(1, anio);
            List<Servidor> listser = (List<Servidor>) query.getResultList();
            return listser;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Servidor> listServidoresPeriodo(short anio) {
        try {
            Query query = em.createQuery("SELECT DISTINCT s FROM DistributivoEscala de JOIN de.distributivo d JOIN d.servidorPublico s WHERE de.distributivo = d AND d.servidorPublico = s AND de.anio = ?1 AND s.estado = TRUE AND de.estado = TRUE")
                    .setParameter(1, anio);
            List<Servidor> listser = (List<Servidor>) query.getResultList();
            return listser;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Proveedor findByProveedor(String id) {
        try {
            Proveedor p;
            Query query = em.createQuery("SELECT p From Proveedor p JOIN p.cliente c WHERE c.identificacion= ?1 AND p.estado = TRUE AND c.estado = TRUE ")
                    .setParameter(1, id);
            return p = (Proveedor) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<Servidor> getServidorXmesNotInDiaLaboradoMes(String tipoRolBuscar, TipoRol tipoRolSeleccionado) {
        try {
            List<Servidor> result;
            Query query = em.createQuery("SELECT s FROM Servidor s WHERE s.estado = TRUE AND s.distributivo IS NOT NULL AND s.id "
                    + "NOT IN ( SELECT d.servidor FROM DiasLaborado d JOIN d.tipoRol t JOIN t.tipoRol c WHERE d.estado = TRUE "
                    + "AND c.codigo = ?1 AND d.tipoRol = ?2 ) AND s.id NOT IN "
                    + " ( SELECT di.servidor FROM DiasLaborado di JOIN di.tipoRol ti WHERE di.estado = TRUE "
                    + "AND ti.tipoRol = ?2 )")
                    .setParameter(1, tipoRolBuscar)
                    .setParameter(2, tipoRolSeleccionado);
            return result = query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    //se le va a cambiar que traiga todos los de su asistencia
    public List<Servidor> getServidorInDiaLaborado(TipoRol t) {
        try {
            if (t == null) {
                return null;
            }
            if (t.getId() != null) {
                Query query = em.createQuery("SELECT d.servidor FROM DiasLaborado d WHERE UPPER(d.mes) = UPPER(?1) AND d.estado = TRUE AND d.periodo = ?2")
                        .setParameter(1, t.getMes().getCodigo()).setParameter(2, t.getAnio());
                return query.getResultList();
            }
        } catch (NoResultException e) {
            return null;
        }
        return null;

    }

    /**
     * *
     * Devuelve la máxima autoridad dependiendo de la Unidad Administrativa.
     *
     * @param unid
     * @return
     */
    public Servidor findServidorByUnidad(UnidadAdministrativa unid) {
        try {
            Servidor s;
            Query query = em.createQuery("SELECT s From Servidor s JOIN s.distributivo d "
                    + "JOIN d.unidadAdministrativa u JOIN u.tipoUnidad cu "
                    + "WHERE u.id = ?1 AND s.estado = TRUE AND d.cargo.nombreCargo LIKE CASE WHEN cu.codigo = 'DIR' THEN 'DIRECTOR%' "
                    + "WHEN cu.codigo = 'DE' THEN 'JEFE%' END  ")
                    .setParameter(1, unid.getId());
            return s = (Servidor) query.getResultStream().findFirst().orElse(null);
        } catch (NoResultException e) {
            return null;
        }

    }

    public boolean findServidor(String identificacion) {
        List<Servidor> servidorList = (List<Servidor>) em.createQuery(QUERY.FIND_SERVIDOR_IDENTIFICACION)
                .setParameter(1, identificacion)
                .getResultList();
        return !servidorList.isEmpty();
    }

}
