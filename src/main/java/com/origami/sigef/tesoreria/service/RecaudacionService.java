/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.RecaudacionCobro;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RecaudacionService extends AbstractService<Recaudacion> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RecaudacionService() {
        super(Recaudacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer getCantReg(CatalogoItem catalogo) {
        Query query = em.createQuery("SELECT COUNT(r) FROM Recaudacion r WHERE r.estado = TRUE AND r.tipoRecaudacion = ?1")
                .setParameter(1, catalogo);
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public List<Recaudacion> recaudacionList() {
        Query query = em.createQuery("SELECT r from Recaudacion r where r.estado = true AND r.saldoAfectar > 0 ORDER BY r.fechaRegistro");
        List<Recaudacion> lista = (List<Recaudacion>) query.getResultList();
        return lista;
    }

    public List<Recaudacion> recaudaciones() {
        Query query = em.createQuery("SELECT r from Recaudacion r where r.estado = true AND r.ajuste IS NULL AND r.saldoAfectar > 0 ORDER BY r.fechaRegistro");
        List<Recaudacion> lista = (List<Recaudacion>) query.getResultList();
        return lista;
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        int aux = 0;
        Query query = getEntityManager().createNativeQuery("UPDATE tesoreria.recaudacion SET contabilizado = false WHERE diario_general= ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        aux = query.executeUpdate();
        Query query_2 = getEntityManager().createNativeQuery("UPDATE tesoreria.detalle_corte_orden_cobro SET valor_ajuste = FALSE  FROM tesoreria.detalle_corte_orden_cobro dco  INNER JOIN tesoreria.recaudacion rc ON dco.cobro_ajuste = rc.id  WHERE rc.diario_general = ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        aux = query_2.executeUpdate();
        return aux;
    }

    public List<Recaudacion> getListRecaudacionesByNumTramite(Long num) {
        try {
            return getEntityManager().createQuery("SELECT r FROM Recaudacion r WHERE r.numTramite = :num").setParameter("num", num).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
