/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.entities.PrestamoIess;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PrestamoIESService extends AbstractService<PrestamoIess> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PrestamoIESService() {
        super(PrestamoIess.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public int getcopia(TipoRol actual, Date fecha, String usuario, TipoRol copia, CatalogoItem prestamo) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("INSERT INTO talento_humano.prestamo_iess "
                + "(servidor,distributivo,tipo_rol,tipo_prestamo,numero_cuota,valor_cuota,estado,periodo,fecha_creacion,usuario_creacion,valor_parametrizado) "
                + "(SELECT "
                + "servidor,distributivo,?1,tipo_prestamo,numero_cuota,valor_cuota,true,?2,?3,?4,valor_parametrizado "
                + "FROM talento_humano.prestamo_iess c "
                + "WHERE c.estado = TRUE AND c.tipo_rol = ?5 AND c.tipo_prestamo = ?6)")
                .setParameter(1, actual.getId()).setParameter(2, actual.getAnio())
                .setParameter(3, fecha).setParameter(4, usuario)
                .setParameter(5, copia.getId()).setParameter(6, prestamo.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public List<PrestamoIess> listPrestamoIESS(TipoRol rol, CatalogoItem prestamo) {
        if (rol == null) {
            return null;
        }
        if (rol.getId() == null) {
            return null;
        }
        List<PrestamoIess> result = (List<PrestamoIess>) em.createQuery("SELECT p FROM PrestamoIess p WHERE p.estado=TRUE AND p.tipoRol = ?1 AND p.tipoPrestamo = ?2 AND p.periodo = ?3")
                .setParameter(1, rol).setParameter(2, prestamo).setParameter(3, rol.getAnio())
                .getResultList();
        return result;
    }

    public List<PrestamoIess> prestamoIESSXserv(TipoRol rol, Servidor serv, ValoresRoles asig) {
        if (rol.getId() == null) {
            return null;
        }
        List<PrestamoIess> result = (List<PrestamoIess>) em.createQuery("SELECT p FROM PrestamoIess p WHERE p.estado=TRUE AND p.tipoRol = ?1  AND p.servidor = ?2 AND p.valorParametrizado = ?3")
                .setParameter(1, rol).setParameter(2, serv).setParameter(3, asig.getValorParametrizable())
                .getResultList();
        return result;
    }

    public List<PrestamoIess> prestamoRubro(Servidor serv, String cod, TipoRol rol) {
        List<PrestamoIess> result = (List<PrestamoIess>) em.createNativeQuery("SELECT * FROM talento_humano.prestamo_iess iess\n"
                + "INNER JOIN conf.parametros_talento_humano ph on ph.id = iess.valor_parametrizado\n"
                + "INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                + "where iess.estado = true and iess.servidor = ?1 AND ci.codigo = ?2 and iess.tipo_rol = ?3", PrestamoIess.class)
                .setParameter(1, serv.getId()).setParameter(2, cod).setParameter(3, rol.getId())
                .getResultList();
        return result;
    }

    public double getTotal(short periodo, CatalogoItem quirografario, TipoRol tipoRol) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT SUM(p.valorCuota) FROM PrestamoIess p WHERE p.estado=TRUE AND p.tipoRol = ?1 AND p.tipoPrestamo = ?2 AND p.periodo = ?3")
                .setParameter(3, periodo).setParameter(2, quirografario).setParameter(1, tipoRol)
                .getSingleResult();
        return result.doubleValue();
    }
}
