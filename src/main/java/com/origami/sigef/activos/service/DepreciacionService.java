/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.service.AbstractService;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jintr
 */
public class DepreciacionService extends AbstractService<Depreciacion> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DepreciacionService() {
        super(Depreciacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Depreciacion> getList(boolean estado) {
        List<Depreciacion> resultado = (List<Depreciacion>) em.createQuery("SELECT d FROM Depreciacion d WHERE d.estado=:estado ORDER BY d.secuencial ASC")
                .setParameter("estado", estado)
                .getResultList();
        return resultado;
    }

    public List<Depreciacion> getListNoContabilizadas(boolean estado, short periodo) {
        List<Depreciacion> resultado = (List<Depreciacion>) em.createQuery("SELECT d FROM Depreciacion d WHERE d.estado=TRUE AND d.contabilizado=:estado AND d.periodo=:periodo ORDER BY d.secuencial ASC")
                .setParameter("estado", estado)
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }

    public Integer getIndice() {
        Integer resultado = (Integer) em.createNativeQuery("SELECT (CASE WHEN max(d.secuencial) is null THEN 1 ELSE (max(d.secuencial) +1 )END ) as indice \n"
                + "FROM activos.depreciacion d WHERE d.estado=true")
                .getSingleResult();
        return resultado;
    }

    public List<BienesItem> getListaBienes(Date fecha_fin) {

        List<BienesItem> resultado = (List<BienesItem>) em.createNativeQuery("SELECT CAST(b.fecha_adquisicion as date), b.* FROM activos.bienes_item b \n"
                + "INNER JOIN catalogo_item ci ON b.tipo_bien = ci.id\n"
                + "INNER JOIN catalogo_item es ON b.estado_bien=es.id\n"
                + "INNER JOIN contabilidad.cont_cuentas cc ON b.cuenta_contable = cc.id\n"
                + "WHERE ci.codigo=?1 AND es.codigo<>?2 AND b.estado=true\n"
                + "AND b.componente=false AND b.item_bien_boolean=true \n"
                + "AND CAST(b.fecha_adquisicion as date) <=?3\n"
                + "ORDER BY cc.codigo ASC", BienesItem.class)
                .setParameter(1, "BLD")
                .setParameter(2, "bien_baja")
                .setParameter(3, fecha_fin)
                .getResultList();
        System.out.println("resultado " + resultado);
        return resultado;
    }

    public boolean getUltimoRegistro(Date fechaDesde) {
        Depreciacion result = (Depreciacion) em.createQuery("SELECT d FROM Depreciacion d WHERE d.estado = true ORDER BY d.id DESC").getResultStream().findFirst().orElse(null);
        if (result == null) {
            return false;
        } else {
            if (fechaDesde.compareTo(result.getFechaHasta()) > 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public List<BienesItem> getListaBienesAgregados(Depreciacion depreciacion) {
        List<BienesItem> resultado = (List<BienesItem>) em.createNativeQuery("SELECT b.* FROM activos.bienes_item b\n"
                + "INNER JOIN catalogo_item ci ON b.tipo_bien = ci.id\n"
                + "INNER JOIN catalogo_item es ON b.estado_bien=es.id\n"
                + "INNER JOIN contabilidad.cont_cuentas cc ON b.cuenta_contable = cc.id\n"
                + "WHERE ci.codigo=?1 AND es.codigo<>?2 AND b.estado=true\n"
                + "AND b.componente=false AND b.item_bien_boolean=true\n"
                + "AND NOT EXISTS (SELECT dd.id FROM activos.depreciacion_detalle dd\n"
                + "WHERE dd.depreciacion = ?3 AND dd.estado = true AND dd.id_bien = b.id)\n"
                + "AND CAST(b.fecha_adquisicion as date) <=?4\n"
                + "ORDER BY cc.codigo ASC", BienesItem.class)
                .setParameter(1, "BLD")
                .setParameter(2, "bien_baja")
                .setParameter(3, depreciacion.getId())
                .setParameter(4, depreciacion.getFechaHasta())
                .getResultList();
        System.out.println("resultado " + resultado);
        return resultado;
    }

    public int getRestablecerValores(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("update activos.depreciacion set contabilizado= false where id_diario_general = ?1")
                .setParameter(1, diarioGeneral.getId().intValue());
        return query.executeUpdate();
    }
}
