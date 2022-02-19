/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.math3.stat.descriptive.summary.Product;

/**
 *
 * @author Criss
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ProductoService extends AbstractService<Producto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ProductoService() {
        super(Producto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigDecimal SumatoriaProformaEgresosProducto(String c) {
        BigDecimal val = new BigDecimal("0");

        val = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p where p.codigoPresupuestario=:codigo")
                .setParameter("codigo", c)
                .getSingleResult();
        return val;

    }

    public Producto obtenerObjeto(String c) {
        Producto val = new Producto();
        val = (Producto) em.createQuery("SELECT p FROM Producto p where p.codigoPresupuestario=:codigo")
                .setParameter("codigo", c)
                .setMaxResults(1)
                .getSingleResult();
        return val;

    }

    public List<String> CodigoSinRepetir() {
        List<String> result = (List<String>) em.createQuery("SELECT DISTINCT (p.codigoPresupuestario) FROM Producto p").getResultList();
        return result;
    }

    public void eliminarPstoPlanificado(Short periodo, String partida) {

        String hql = "delete from ProformaPresupuestoPlanificado p WHERE p.periodo = :periodo and p.partidaPresupuestaria= :partida";
        Query query = em.createQuery(hql)
                .setParameter("periodo", periodo)
                .setParameter("partida", partida);

        int rowCount = query.executeUpdate();

    }

    public BigDecimal getSumaProducto(ActividadOperativa actividad, short periodo, List<String> items) {

        BigDecimal val = BigDecimal.ZERO;
        val = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p WHERE p.actividadOperativa = :actividad AND p.periodo = :periodo AND p.estado=TRUE AND p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.actividadOperativa.codigoReformaTraspaso IS NULL AND p.actividadOperativa.codigoReforma IS NULL")
                .setParameter("actividad", actividad)
                .setParameter("periodo", periodo)
                // .setParameter("items", items)
                .getSingleResult();

        if (val == null) {
            val = BigDecimal.ZERO;
        }
        return val;
    }
    
    
    

    public List<Producto> getListProductos(short periodo, List<String> item) {

        Query query = em.createQuery("SELECT p FROM Producto p WHERE p.periodo = :periodo AND p.estado = TRUE AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo);
        List<Producto> result = query.getResultList();
        return result;
    }

    public BigDecimal getSumaTotalProductos(Long id, Short periodo) {

        BigDecimal resultSuma = BigDecimal.ZERO;
        if (id == null) {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p WHERE  p.estado=TRUE AND p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso  IS NULL")
                    .setParameter("periodo", periodo).getSingleResult();
        } else {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u   WHERE p.estado=TRUE AND p.periodo= :periodo AND u.id= :id AND p.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL ")
                    .setParameter("periodo", periodo).setParameter("id", id).getSingleResult();
        }

        if (resultSuma == null) {
            resultSuma = BigDecimal.ZERO;
        }

        return resultSuma;
    }

    public BigDecimal getSumaTotalProductosReformas(Long id, Short periodo, BigInteger r) {

        BigDecimal resultSuma = BigDecimal.ZERO;
        if (id == null) {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p WHERE p.estado=TRUE AND p.periodo= :periodo AND p.codigoReforma=:reforma")
                    .setParameter("periodo", periodo).setParameter("reforma", r).getSingleResult();
        } else {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(p.monto) FROM Producto p  INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u "
                    + "  WHERE p.estado=TRUE AND p.periodo= :periodo AND  u.id= :id AND p.codigoReforma=:reforma AND a.codigoReforma= :codreforma ")
                    .setParameter("periodo", periodo).setParameter("id", id).setParameter("reforma", r).setParameter("codreforma", r).getSingleResult();
        }

        if (resultSuma == null) {
            resultSuma = BigDecimal.ZERO;
        }

        return resultSuma;
    }

    public BigDecimal getSumaTotalActividad(Long id, Short periodo) {

        BigDecimal resultSuma = BigDecimal.ZERO;
        if (id == null) {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(a.monto) FROM ActividadOperativa a WHERE   a.estado=TRUE AND a.periodo= :periodo AND a.codigoReforma is null AND a.codigoReformaTraspaso IS NULL")
                    .setParameter("periodo", periodo).getSingleResult();
        } else {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(a.monto) FROM ActividadOperativa a INNER JOIN a.unidadResponsable u  WHERE a.estado=TRUE AND a.periodo= :periodo AND u.id= :id and a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                    .setParameter("periodo", periodo).setParameter("id", id).getSingleResult();
        }

        if (resultSuma == null) {
            resultSuma = BigDecimal.ZERO;
        }

        return resultSuma;
    }

    public BigDecimal getSumaTotalActividadReformas(Long id, Short periodo, BigInteger r) {

        BigDecimal resultSuma = BigDecimal.ZERO;
        if (id == null) {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(a.monto) FROM ActividadOperativa a WHERE  a.estado=TRUE AND a.periodo= :periodo AND a.codigoReforma= :reforma")
                    .setParameter("periodo", periodo).setParameter("reforma", r).getSingleResult();
        } else {
            resultSuma = (BigDecimal) em.createQuery("SELECT SUM(a.monto) FROM ActividadOperativa a INNER JOIN a.unidadResponsable u  WHERE a.estado=TRUE AND a.periodo= :periodo AND u.id= :id and a.codigoReforma= :reforma2")
                    .setParameter("periodo", periodo).setParameter("id", id).setParameter("reforma2", r).getSingleResult();
        }

        if (resultSuma == null) {
            resultSuma = BigDecimal.ZERO;
        }

        return resultSuma;
    }

    public Producto getProductoActual(Producto p) {
        Producto result = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id= :id").setParameter("id", p.getId()).getSingleResult();
        return result;
    }

    public List<Producto> getListaProductoByPeriodo(Short periodo) {
        List<Producto> pro = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.periodo = :periodo AND p.estado = true AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL").setParameter("periodo", periodo).getResultList();
        return pro;
    }

    public List<Producto> porductoReformadosPapp(BigInteger num, UnidadAdministrativa u) {
        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma=:num AND p.actividadOperativa.unidadResponsable=:unidad ORDER BY p.actividadOperativa,p.descripcion asc")
                .setParameter("num", num).setParameter("unidad", u).getResultList();
        return result;
    }

    public BigDecimal productoReservado(Producto p, boolean reforma) {
        BigDecimal result = BigDecimal.ZERO;
        if (reforma) {
            result = (BigDecimal) em.createNativeQuery("select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d"
                    + " INNER join certificacion_presupuestaria_anual.solicitud_reserva_compromiso s ON s.id = d.solicitud\n"
                    + "inner join  producto p on d.actividad_producto=p.id inner join catalogo_item ci ON s.estado=ci.id\n"
                    + "where ci.codigo in ('LIQUI', 'APRO') and d.actividad_producto=?1 and d.periodo=?2")
                    .setParameter(1, p.getNumeroOrdenId().longValue()).setParameter(2, p.getPeriodo()).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        } else {
            result = (BigDecimal) em.createNativeQuery("select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d"
                    + " INNER join certificacion_presupuestaria_anual.solicitud_reserva_compromiso s ON s.id = d.solicitud\n"
                    + "inner join  producto p on d.actividad_producto=p.id inner join catalogo_item ci ON s.estado=ci.id\n"
                    + "where ci.codigo in ('LIQUI', 'APRO') and d.actividad_producto=?1 and d.periodo=?2")
                    .setParameter(1, p.getId()).setParameter(2, p.getPeriodo()).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        }

        return result;
    }

}
