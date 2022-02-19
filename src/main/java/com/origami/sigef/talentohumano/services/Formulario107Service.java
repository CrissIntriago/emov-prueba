/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Formulario107;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jesus
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class Formulario107Service extends AbstractService<Formulario107> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public Formulario107Service() {
        super(Formulario107.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Servidor> findAllServidorRetencion(Short periodo) {
        try {
            return em.createQuery("SELECT DISTINCT ser FROM LiquidacionRol liquirol JOIN "
                    + "liquirol.rolPago rolespago JOIN rolespago.servidor ser "
                    + "WHERE ser.estado = true AND liquirol.estado = true AND rolespago.periodo = ?1")
                    .setParameter(1, periodo)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Formulario107> findAllFormulario107ByServidor(BigInteger idServidor, String estadoAprobado) {
        try {
            return em.createQuery("SELECT f FROM Formulario107 f WHERE f.servidorId = ?1 AND f.estadoAprobado <> ?2 ")
                    .setParameter(1, idServidor)
                    .setParameter(2, estadoAprobado)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal findByFondosReservaByServidor(Long idEstadoAprobacion, Long idServidor, Long idValorParametrizable) {
        try {
            return (BigDecimal) em.createQuery("SELECT SUM(rol.valorRubro) FROM RolRubro rol "
                    + "JOIN rol.liquidacionRol liqui JOIN liqui.tipoRol tipoRol "
                    + "JOIN liqui.rolPago rolesPago JOIN rol.valorAsignacion valRoles "
                    + "WHERE liqui.estado = true AND rol.estado = true AND tipoRol.estadoAprobacion.id = ?1 "
                    + "AND rolesPago.servidor.id = ?2 AND valRoles.valorParametrizable.id = ?3")
                    .setParameter(1, idEstadoAprobacion)
                    .setParameter(2, idServidor)
                    .setParameter(3, idValorParametrizable)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
