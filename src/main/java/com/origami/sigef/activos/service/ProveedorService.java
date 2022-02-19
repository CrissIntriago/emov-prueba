/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class ProveedorService extends AbstractService<Proveedor> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProveedorService(Class<Proveedor> entityClass) {
        super(entityClass);
    }

    public ProveedorService() {
        super(Proveedor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public String getQueryHQLExisteProveedor(Proveedor prov) {
        String query = null;
        if (prov.getCliente().getIdentificacion() != null) {
            return "SELECT p FROM Proveedor p JOIN p.cliente pp where pp.identificacion = :identificacion AND p.estado = true";
        }
        return query;
    }

    public Proveedor existeProveedor(Proveedor prov) {
        String queryString = getQueryHQLExisteProveedor(prov);

        Map<String, Object> params = getParameter(prov);

        final Query query = getEntityManager().createQuery(queryString);

        params.entrySet().forEach((param) -> {
            query.setParameter(param.getKey(), param.getValue());
        });
        List<Proveedor> result = query.getResultList();

        if (result != null) {
            if (!result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    private Map<String, Object> getParameter(Proveedor prov) {

        Map<String, Object> params = new HashMap<>();
        if (prov.getCliente().getIdentificacion() != null) {
            params.put("identificacion", prov.getCliente().getIdentificacion());
        }
        return params;
    }

    public Proveedor findByIdentificacion(String id) {
        try {
            return this.findByNamedQuery1("Proveedor.findByPersonaId", id);
        } catch (NoResultException e) {
        }
        return null;
    }

    public Cliente findByIdentificacionCliente(String id) {
        try {
            return this.findByNamedQuery1("Cliente.findByIdCliente", id);
        } catch (NoResultException e) {
        }
        return null;
    }

    public Boolean verificarGestionDeAdquisicion(Proveedor proveedor) {
        try {
            Proveedor resultado = (Proveedor) getEntityManager().createQuery("SELECT a.proveedor FROM Adquisiciones a WHERE a.proveedor=:proveedor AND a.estado = TRUE")
                    .setParameter("proveedor", proveedor)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
        }
        return false;
    }

    public Proveedor findByProveedorByCliente(Cliente cliente) {
        Query query = em.createQuery("SELECT p FROM Proveedor p WHERE p.cliente = ?1 AND p.estado = true AND p.cliente.estado = true")
                .setParameter(1, cliente);
        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            return (Proveedor) query.getResultList().get(0);
        } else {
            return null;
        }
    }

    public Boolean findProveedorRegistrado(Cliente cliente) {
        try {
            Proveedor resultado = (Proveedor) getEntityManager().createQuery("SELECT p FROM Proveedor p "
                    + "INNER JOIN p.cliente c WHERE p.estado=TRUE AND c.estado = TRUE AND c.identificacion=:identificacion")
                    .setParameter("identificacion", cliente.getIdentificacion().substring(0, 10))
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
        }
        return false;
    }

    public Proveedor findById(BigInteger id) {
        Proveedor resultado = (Proveedor) em.createQuery("SELECT p FROM Proveedor p WHERE p.id=:id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return resultado;
    }

    public Proveedor findByProveedor(String identificacion) {
        if (identificacion.length() == 13) {
            String temp = Utils.clone(identificacion);
            return findByNamedQuery1("Proveedor.findByPersonaIdentificacionRUC", temp.substring(0, 10));
        } else {
            return findByNamedQuery1("Proveedor.findByPersonaIdentificacion", identificacion);
        }
    }
}
