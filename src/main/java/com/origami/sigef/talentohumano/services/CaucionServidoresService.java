/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CaucionServidores;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Origami
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CaucionServidoresService extends AbstractService<CaucionServidores> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CaucionServidoresService() {
        super(CaucionServidores.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public int getcopia(TipoRol actual, Date fecha, String usuario, TipoRol copia) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("INSERT INTO talento_humano.caucion_servidores\n"
                + "(servidor, distributivo, tipo_rol, estado, periodo, fecha_modificacion, usuario_modifica, valor_prima_neta, porcentaje, base_imponible, cuota_propocional, valor_parametrizado)\n"
                + "(SELECT \n"
                + "servidor, distributivo, ?1, true, ?2, ?3, ?4, valor_prima_neta, porcentaje, base_imponible, cuota_propocional,valor_parametrizado\n"
                + "FROM talento_humano.caucion_servidores c\n"
                + "WHERE c.estado = TRUE AND c.tipo_rol = ?5)")
                .setParameter(1, actual.getId()).setParameter(2, actual.getAnio())
                .setParameter(3, fecha).setParameter(4, usuario)
                .setParameter(5, copia.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public List<CaucionServidores> listPrestamoIESS(TipoRol rol) {
        if (rol.getId() == null) {
            return null;
        }
        List<CaucionServidores> result = (List<CaucionServidores>) em.createQuery("SELECT p FROM CaucionServidores p WHERE p.estado=TRUE AND p.tipoRol = ?1 AND p.periodo = ?2")
                .setParameter(1, rol).setParameter(2, rol.getAnio())
                .getResultList();
        return result;
    }

    public CaucionServidores caucionRubro(TipoRol rol, Servidor serv, String cod) {
        try {
            CaucionServidores result = (CaucionServidores) em.createNativeQuery("Select  * from talento_humano.caucion_servidores cs\n"
                    + "INNER JOIN conf.parametros_talento_humano ph on ph.id = cs.valor_parametrizado\n"
                    + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                    + "where cs.tipo_rol = ?1 AND cs.estado = true AND cs.servidor = ?2\n"
                    + "AND ci.codigo=?3", CaucionServidores.class)
                    .setParameter(1, rol.getId()).setParameter(2, serv.getId()).setParameter(3, cod)
                    .getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long numeroServidores(TipoRol rol) {
        if (rol != null) {
            Query query = em.createQuery("select COUNT(dia.servidor) from DiasLaborado dia where dia.estado = true and dia.tipoRol = ?1")
                    .setParameter(1, rol);
            Long suma = (Long) query.getSingleResult();
            return suma;
        }
        return null;
    }

    public List<Servidor> getServidorXmesNotInCaucionServidor(String tipoRolBuscar, String mes) {
        try {
            List<Servidor> result;
            Query query = em.createQuery("SELECT s FROM Servidor s WHERE s.estado = TRUE AND s.distributivo IS NOT NULL AND s.id NOT IN"
                    + " ( SELECT ca.servidor FROM CaucionServidores ca JOIN ca.tipoRol t JOIN t.tipoRol c JOIN t.mes m WHERE ca.estado = TRUE "
                    + "AND c.codigo = ?1 AND m.codigo = ?2 )")
                    .setParameter(1, tipoRolBuscar)
                    .setParameter(2, mes);
            return result = query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

}
