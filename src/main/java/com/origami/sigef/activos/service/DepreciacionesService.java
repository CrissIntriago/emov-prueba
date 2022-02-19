/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Depreciaciones;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DepreciacionesService extends AbstractService<Depreciaciones> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DepreciacionesService() {
        super(Depreciaciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void eliminandoRegistros() {
        String sql = "delete from Depreciaciones d ";
        Query query = em.createQuery(sql);
        query.executeUpdate();
    }

    public List<CuentaContable> getListaCuentasContable(Short anio, String codigoTipo) {
        List<CuentaContable> resultado = (List<CuentaContable>) em.createQuery("SELECT DISTINCT b.cuentaContable FROM BienesItem b INNER JOIN b.tipoBien tb where b.periodo=:anio AND b.itemBienBoolean = TRUE AND tb.codigo=:codigoTipo")
                .setParameter("anio", anio)
                .setParameter("codigoTipo", codigoTipo)
                .getResultList();
        return resultado;
    }

    public List<BienesItem> getListaBienes(CuentaContable cuenta, Short anio) {
        List<BienesItem> resultado = (List<BienesItem>) em.createQuery("SELECT b FROM BienesItem b where b.periodo=:anio AND b.itemBienBoolean = TRUE AND b.componente = FALSE AND b.cuentaContable=:cuenta")
                .setParameter("anio", anio)
                .setParameter("cuenta", cuenta)
                .getResultList();
        return resultado;
    }
}
