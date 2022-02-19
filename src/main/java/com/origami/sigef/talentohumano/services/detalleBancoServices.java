/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@Stateless
@javax.enterprise.context.Dependent
public class detalleBancoServices extends AbstractService<DetalleBanco> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public detalleBancoServices() {
        super(DetalleBanco.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public DetalleBanco findByCodigoList(String cod) {
        return findByNamedQuery1("DetalleBanco.findByCodigoList", cod);
    }

    public List<DetalleBanco> findListBancoByServidor(Servidor serv) {
        return this.findByNamedQuery("DetalleBanco.findListBancoByServidor", serv);
    }

    public DetalleRegistro findByDetalleRegistro(Servidor s) {

        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DetalleRegistro d JOIN d.servidor s WHERE d.servidor = ?1")
                    .setParameter(1, s);
            DetalleRegistro result = (DetalleRegistro) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<DetalleBanco> findListBancoByProveedor(Proveedor pro) {
        return this.findByNamedQuery("DetalleBanco.findListBancoByProveedor", pro);
    }

    public DetalleBanco findDetalleCliente(Cliente cliente) {
        if (!cliente.getTipoBeneficiario()) {
            try {
                return (DetalleBanco) em.createQuery("SELECT db FROM DetalleBanco db INNER JOIN db.proveedor p WHERE p.cliente = ?1 AND db.estadoCuenta=true AND db.estado=true")
                        .setParameter(1, cliente)
                        .getSingleResult();
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                return (DetalleBanco) em.createQuery("SELECT db FROM DetalleBanco db INNER JOIN db.servidorPublico sp WHERE sp.persona = ?1 AND db.estadoCuenta=true AND db.estado=true")
                        .setParameter(1, cliente)
                        .getSingleResult();
            } catch (Exception e) {
                return null;
            }
        }
    }

}
