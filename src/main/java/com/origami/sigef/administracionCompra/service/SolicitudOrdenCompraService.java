/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.administracionCompra.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CaracteristicaTecnica;
import com.origami.sigef.common.entities.EspecificacionTecnica;
import com.origami.sigef.common.entities.SolicitudOrdenCompra;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class SolicitudOrdenCompraService extends AbstractService<SolicitudOrdenCompra> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SolicitudOrdenCompraService() {
        super(SolicitudOrdenCompra.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer getCantReg(String codigo) {
        Query query = em.createQuery("SELECT COUNT(c) FROM SolicitudOrdenCompra c INNER JOIN c.adquisicion ad WHERE c.estado = TRUE AND ad.tipoProceso.codigo=?1 ")
                .setParameter(1, codigo);
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public SolicitudOrdenCompra getOrdenCompraXNumTramite(Long id) {
        try {
            Query query = em.createQuery("SELECT c from SolicitudOrdenCompra c WHERE c.idTramite=:idTramite")
                    .setParameter("idTramite", id);
            SolicitudOrdenCompra result = (SolicitudOrdenCompra) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean findCatalogoElectronico(BigInteger numTramite) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("select so.* from administracion.solicitud_orden_compra so\n"
                + "INNER JOIN activos.adquisiciones ad ON so.adquisicion= ad.id\n"
                + "INNER JOIN public.catalogo_item ci ON ad.tipo_proceso=ci.id\n"
                + "WHERE so.estado=true AND so.numero_tramite= ?1 AND ci.codigo=?2\n"
                + "AND EXTRACT(YEAR from so.fecha_elaboracion) = ?3", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, "tipo_proceso_catalogo")
                .setParameter(3, Utils.getAnio(new Date()))
                .getResultList();
        return !result.isEmpty();
    }

    public boolean findSinProcesar(BigInteger numTramite) {
        List<SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("select so.* from administracion.solicitud_orden_compra so\n"
                + "INNER JOIN activos.adquisiciones ad ON so.adquisicion= ad.id\n"
                + "INNER JOIN public.catalogo_item ci ON ad.tipo_proceso=ci.id\n"
                + "WHERE so.estado=true AND so.numero_tramite= ?1 AND ci.codigo=?2\n"
                + "AND EXTRACT(YEAR from so.fecha_elaboracion) = ?3 AND so.procesado=false", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, "tipo_proceso_catalogo")
                .setParameter(3, Utils.getAnio(new Date()))
                .getResultList();
        return !result.isEmpty();
    }

    public List< SolicitudOrdenCompra> getListSolicitud(BigInteger numTramite) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("select so.* from administracion.solicitud_orden_compra so\n"
                + "WHERE so.estado=true AND so.numero_tramite= ?1\n"
                + "AND EXTRACT(YEAR from so.fecha_elaboracion) = ?2", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, Utils.getAnio(new Date()))
                .getResultList();
        return result;
    }

    public String getResponsable(BigInteger numTramite) {
        List<String> result = (List<String>) em.createNativeQuery("SELECT u.usuario FROM administracion.solicitud_orden_compra soc\n"
                + "INNER JOIN activos.adquisiciones a ON soc.adquisicion = a.id\n"
                + "INNER JOIN activos.responsable_adquisicion ra ON ra.adquisicion = a.id\n"
                + "INNER JOIN auth.usuarios u ON u.funcionario=ra.responsable\n"
                + "WHERE soc.estado=true AND soc.numero_tramite=?1")
                .setParameter(1, numTramite)
                .getResultList();
        return result.get(0);
    }

    public boolean getConsultarTipo(BigInteger numTramite, String codigoTipoOrden, short periodo) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("SELECT so.* FROM administracion.solicitud_orden_compra so\n"
                + "INNER JOIN activos.adquisiciones ad ON so.adquisicion=ad.id\n"
                + "INNER JOIN catalogo_item ci ON ad.tipo_proceso = ci.id\n"
                + "WHERE so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2) \n"
                + "AND so.estado = true AND ci.codigo =?3", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, periodo)
                .setParameter(3, codigoTipoOrden)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean getConsultarTipoAdquiscion(BigInteger numTramite, String codigoTipoOrden, String codigoAdquisicion, short periodo) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("SELECT so.* FROM administracion.solicitud_orden_compra so\n"
                + "INNER JOIN activos.adquisiciones ad ON so.adquisicion=ad.id\n"
                + "INNER JOIN catalogo_item ci ON ad.tipo_proceso = ci.id\n"
                + "INNER JOIN catalogo_item c ON ad.tipo_adquisicion = c.id\n"
                + "WHERE so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2) \n"
                + "AND so.estado = true AND ci.codigo =?3 AND c.codigo =?4", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, periodo)
                .setParameter(3, codigoTipoOrden)
                .setParameter(4, codigoAdquisicion)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean getConsultarBienes(BigInteger numTramite, boolean accion, short periodo) {
        List< SolicitudOrdenCompra> result = new ArrayList<>();
        String sql = "";
        if (accion) {//caracteristicas tecnicas
            sql = "SELECT so.* FROM administracion.caracteristica_tecnica ct\n"
                    + "INNER JOIN administracion.solicitud_orden_compra so ON ct.orden_compra = so.id\n"
                    + "WHERE so.estado=true AND ct.bien=true AND so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2)";
        } else {//especificaciones tecnicas
            sql = "SELECT so.* FROM administracion.especificacion_tecnica et\n"
                    + "INNER JOIN administracion.solicitud_orden_compra so ON et.orden_compra = so.id\n"
                    + "WHERE so.estado = true AND so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2);";
        }
        result = (List<SolicitudOrdenCompra>) em.createNativeQuery(sql, SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, periodo)
                .getResultList();
        System.out.println("getConsultarBienes() " + result.size() + " numTramite: " + numTramite);
        return !result.isEmpty();
    }

    public boolean getConsultarSinProcesar(BigInteger numTramite) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("SELECT so.* FROM administracion.solicitud_orden_compra so\n"
                + "INNER JOIN activos.adquisiciones ad ON so.adquisicion=ad.id\n"
                + "WHERE so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2)\n"
                + "AND so.estado = true AND so.procesado = false", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, Utils.getAnio(new Date()).shortValue())
                .getResultList();
        return result.isEmpty();
    }

    public boolean getConsultarBienesInventario(SolicitudOrdenCompra ordenCompra) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("select so.* from administracion.especificacion_tecnica et\n"
                + "inner join administracion.solicitud_orden_compra so on et.orden_compra = so.id\n"
                + "where so.id=?1", SolicitudOrdenCompra.class)
                .setParameter(1, ordenCompra.getId())
                .getResultList();
        System.out.println("1. getConsultarBienesInventario() " + result.size() + " ordenCompra: " + ordenCompra);
        return !result.isEmpty();
    }

    public boolean getConsultarBienesInventario(BigInteger numTramite, short anio) {
        List< SolicitudOrdenCompra> result = (List<SolicitudOrdenCompra>) em.createNativeQuery("select so.* from administracion.especificacion_tecnica et\n"
                + "inner join administracion.solicitud_orden_compra so on et.orden_compra = so.id\n"
                + "WHERE so.numero_tramite= ?1 AND (EXTRACT(YEAR FROM so.fecha_elaboracion) = ?2)\n"
                + "AND so.estado = true AND so.procesado = false", SolicitudOrdenCompra.class)
                .setParameter(1, numTramite)
                .setParameter(2, anio)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean findEspecificacionTecnica(List<SolicitudOrdenCompra> idOrdenes) {
        List< EspecificacionTecnica> result = (List<EspecificacionTecnica>) em.createQuery("SELECT et FROM EspecificacionTecnica et WHERE et.ordenCompra in (?1)")
                .setParameter(1, idOrdenes)
                .getResultList();
        return !result.isEmpty();
    }

    public boolean findInventario(List<SolicitudOrdenCompra> idOrdenes) {
        List< CaracteristicaTecnica> result = (List<CaracteristicaTecnica>) em.createQuery("SELECT ct FROM CaracteristicaTecnica ct WHERE ct.ordenCompra in (?1) AND ct.bien=true")
                .setParameter(1, idOrdenes)
                .getResultList();
        return !result.isEmpty();
    }

}
