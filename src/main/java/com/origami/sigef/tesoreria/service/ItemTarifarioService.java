/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ItemTarifario;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.Rubro;
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
public class ItemTarifarioService extends AbstractService<ItemTarifario> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ItemTarifarioService() {
        super(ItemTarifario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer getCantidadReg() {
        Query query = em.createQuery("SELECT COUNT(it) FROM ItemTarifario it WHERE it.estado = TRUE");
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public int getCopiaItem(Short newPeriodo, Short opcionBusqueda, String usuarioCreacion, Date fechaCreacion) {
        int executeUpdate = 0;
        System.out.println("new anio " + newPeriodo + " viejo periodo: " + opcionBusqueda + " usuario: " + usuarioCreacion + " fecha creacion: " + fechaCreacion);
        Query query = em.createNativeQuery("INSERT INTO tesoreria.item_tarifario(\n"
                + "rubro_tipo, codigo, descripcion, valor, estado, periodo, vigente, vigencia_hasta,"
                + " id_item_tarifa, codigo_item, fecha_creacion, usuario_creacion)\n"
                + "(select rubro_tipo, codigo, descripcion, valor, estado, ?1, vigente, vigencia_hasta,"
                + " id_item_tarifa, codigo_item, ?2, ?3\n"
                + "from tesoreria.item_tarifario it\n"
                + "where it.estado = true and it.periodo = ?4)")
                .setParameter(1, newPeriodo).setParameter(2, fechaCreacion).setParameter(3, usuarioCreacion)
                .setParameter(4, opcionBusqueda);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public Boolean getExisteItem(Long idItem, Short anio) {
        try {
            Query query = em.createQuery("SELECT i from ItemTarifario i where i.estado = TRUE AND i.idItemTarifa = ?1 AND i.periodo = ?2")
                    .setParameter(1, idItem).setParameter(2, anio);
            List<ItemTarifario> lista = (List<ItemTarifario>) query.getResultList();
            return !lista.isEmpty();
        } catch (Exception ex) {
            return false;
        }
    }

    public List<ItemTarifario> getListaItem(Short anio) {
        try {
            Query query = em.createQuery("SELECT i FROM ItemTarifario i WHERE i.estado = TRUE AND i.periodo = ?1")
                    .setParameter(1, anio);
            List<ItemTarifario> lista = (List<ItemTarifario>) query.getResultList();
            return lista;
        } catch (Exception ex) {
            return null;
        }
    }

    public Boolean getitemMovimiento(ItemTarifario item) {
        try {
            List<ItemTarifario> resultado
                    = (List<ItemTarifario>) em.createQuery("SELECT it FROM ItemTarifario it WHERE it in (SELECT dt.itemTarifa FROM DetalleCorteOrdenCobro dt) AND it = ?1")
                            .setParameter(1, item).getResultList();
            return resultado.isEmpty();
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    public ItemTarifario getItemTarifarioByRubro(Rubro r, Short anio) {
        try {
            Query query = em.createQuery("SELECT it FROM ItemTarifario it WHERE it.rubroTipo = ?1 AND it.codigoItem = ?2 AND it.estado=TRUE AND it.periodo = ?3")
                    .setParameter(1, r.getRubroTipo()).setParameter(2, r.getCodigo()).setParameter(3, anio);
            ItemTarifario result = (ItemTarifario) query.getResultStream().findFirst().orElse(null);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
