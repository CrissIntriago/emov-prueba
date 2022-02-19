/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DescuentoRubroValor;
import com.origami.sigef.common.entities.OtroDescuento;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class DescuentoRubroValorService extends AbstractService<DescuentoRubroValor> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DescuentoRubroValorService() {
        super(DescuentoRubroValor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<DescuentoRubroValor> getListaXservidor(OtroDescuento serv, ValoresRoles vr) {
        Query query = em.createQuery("SELECT d FROM DescuentoRubroValor d WHERE d.estado = TRUE AND d.otroDescuento = ?1 AND d.valorRol.valorParametrizable = ?2")
                .setParameter(1, serv).setParameter(2, vr.getValorParametrizable());
        List<DescuentoRubroValor> result = (List<DescuentoRubroValor>) query.getResultList();
        return result;
    }

    public List<DescuentoRubroValor> getListaXservidorOtroDesc(OtroDescuento serv) {
        Query query = em.createQuery("SELECT d FROM DescuentoRubroValor d WHERE d.estado = TRUE AND d.otroDescuento = ?1")
                .setParameter(1, serv);
        List<DescuentoRubroValor> result = (List<DescuentoRubroValor>) query.getResultList();
        return result;
    }

    public List<DescuentoRubroValor> rubroOtroDescuento(String codigo, TipoRol rol, RolesDePago asignacion) {
        try {
            Query query = em.createNativeQuery("SELECT * FROM talento_humano.descuento_rubro_valor dr\n"
                    + "INNER JOIN talento_humano.otro_descuento ot on ot.id = dr.otro_descuento\n"
                    + "where dr.estado = true and ot.estado = true and ot.rol_pago in (\n"
                    + "select rp.id from talento_humano.roles_de_pago rp\n"
                    + "	LEFT join talento_humano.valores_roles vr on vr.rol_pago = rp.id\n"
                    + "	INNER JOIN conf.parametros_talento_humano ph ON vr.valor_parametrizable = ph.id\n"
                    + "	INNER JOIN catalogo_item ci ON ci.id = ph.valores\n"
                    + "	where vr.estado = true AND ci.codigo = ?1)  \n"
                    + "	AND ot.tipo_rol = ?2 AND ot.rol_pago = ?3", DescuentoRubroValor.class)
                    .setParameter(1, codigo).setParameter(2, rol.getId()).setParameter(3, asignacion.getId());
            List<DescuentoRubroValor> result = (List<DescuentoRubroValor>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
}
