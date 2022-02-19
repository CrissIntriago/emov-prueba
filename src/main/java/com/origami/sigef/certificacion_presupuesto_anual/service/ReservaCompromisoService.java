/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReservaCompromisoService extends AbstractService<SolicitudReservaCompromiso> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReservaCompromisoService() {
        super(SolicitudReservaCompromiso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer getMaxCodigo(Short periodo) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        Integer sumando = 0;
        Integer val = (Integer) em.createNativeQuery("SELECT MAX(s.secuencial) FROM certificacion_presupuestaria_anual.solicitud_reserva_compromiso s where s.periodo=?3")
                .setParameter(3, periodo)
                .getSingleResult();
        if (val == null) {
            sumando = 1;
        } else {
            sumando = val + 1;
        }
        return sumando;
    }

    public List<UnidadAdministrativa> getListaUnidadesReservas() {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT(u) FROM SolicitudReservaCompromiso s INNER JOIN s.unidadRequiriente u ").getResultList();
        return result;

    }

    public List<Producto> listadoProductoActividades(Short periodo, UnidadAdministrativa u) {
        List<UnidadAdministrativa> ListadoGlobal = new ArrayList<>();
        List<Producto> listProductosActividadGlobal = new ArrayList<>();
        if (getPeriodoAprobado(periodo)) {
            UnidadAdministrativa unidades = (UnidadAdministrativa) em.createQuery("SELECT uni FROM UnidadAdministrativa uni where uni.id= :id AND uni.estado=true")
                    .setParameter("id", u.getId()).getResultStream().findFirst().orElse(null);

            if (unidades != null) {
                ListadoGlobal.add(unidades);
            }
            List<UnidadAdministrativa> unidadesRecursivas = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                    .setParameter("id", u.getId()).getResultList();

            for (UnidadAdministrativa uni : unidadesRecursivas) {
                List<UnidadAdministrativa> unidadesRecursivas2 = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                        .setParameter("id", uni.getId()).getResultList();
                ListadoGlobal.add(uni);

                for (UnidadAdministrativa item2 : unidadesRecursivas2) {
                    List<UnidadAdministrativa> unidadesRecursivas3 = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                            .setParameter("id", item2.getId()).getResultList();

                    ListadoGlobal.add(item2);
                    for (UnidadAdministrativa item3 : unidadesRecursivas3) {
                        List<UnidadAdministrativa> unidadesRecursivas4 = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                                .setParameter("id", item3.getId()).getResultList();

                        ListadoGlobal.add(item3);
                        for (UnidadAdministrativa item4 : unidadesRecursivas4) {
                            List<UnidadAdministrativa> unidadesRecursivas5 = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                                    .setParameter("id", item4.getId()).getResultList();
                            ListadoGlobal.add(item4);

                            for (UnidadAdministrativa item5 : unidadesRecursivas5) {
                                List<UnidadAdministrativa> unidadesRecursivas6 = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u INNER JOIN u.padre p WHERE p.id= :id AND p.estado=true AND u.estado=true ")
                                        .setParameter("id", item5.getId()).getResultList();
                                ListadoGlobal.add(item5);
                                for (UnidadAdministrativa item6 : unidadesRecursivas6) {
                                    ListadoGlobal.add(item6);
                                }

                            }
                        }
                    }

                }

            }
            for (UnidadAdministrativa itemGlobal : ListadoGlobal) {

                List<Producto> ListProductosActividad = (List<Producto>) em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a WHERE a.unidadResponsable= :unidad AND p.periodo= :periodo AND p.codigoReforma IS NULL  AND p.codigoReformaTraspaso IS NULL AND a.codigoReformaTraspaso IS NULL")
                        .setParameter("unidad", itemGlobal).setParameter("periodo", periodo).getResultList();
                for (Producto pr : ListProductosActividad) {
                    listProductosActividadGlobal.add(pr);

                }
            }

        }

        return listProductosActividadGlobal;

    }
    
    public List<Producto> listadoProductoActividadesMaxAut(Short periodo) {
        List<Producto> ListProductosActividad = (List<Producto>) em.createQuery("SELECT p FROM Producto p INNER JOIN p.actividadOperativa a WHERE p.periodo= :periodo AND p.codigoReforma IS NULL  AND p.codigoReformaTraspaso IS NULL ")
                        .setParameter("periodo", periodo).getResultList();
        return ListProductosActividad;
    }

    public Presupuesto getPresupuestpsolicitado(String partida, Short periodo) {
        Presupuesto result = (Presupuesto) em.createQuery("SELECT p FROM Presupuesto p  where p.partida= :partida AND p.periodo= :periodo")
                .setParameter("periodo", periodo).setParameter("partida", partida).getSingleResult();
        return result;
    }

    public CatalogoItem getestados(String codigoItem, String estado) {
        CatalogoItem data = (CatalogoItem) em.createQuery("SELECT c FROM CatalogoItem c INNER JOIN c.catalogo cc where c.codigo= :codigoItem AND cc.codigo= :codigo")
                .setParameter("codigoItem", codigoItem).setParameter("codigo", estado).getSingleResult();
        return data;
    }

    public boolean getPeriodoAprobado(Short periodo) {
        boolean periodoAprobado = true;
        CatalogoProformaPresupuesto result = (CatalogoProformaPresupuesto) em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c WHERE c.tipoFlujo=false AND c.periodo= :periodo")
                .setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (result != null) {
            if (result.getAprobado()) {
                periodoAprobado = true;
            } else {
                periodoAprobado = false;
            }

        } else {
            periodoAprobado = false;
        }

        return periodoAprobado;
    }

    public BigDecimal getSueldoDisponible(Producto producto, Short periodo) {
        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc  WHERE d.actividadProducto.id= :id AND d.periodo= :periodo AND sc.estado.codigo NOT IN('RECHA','ADA') ")
                .setParameter("id", producto.getId()).setParameter("periodo", periodo).getSingleResult();

        return resultMax;
    }

    public DetalleSolicitudCompromiso getSueldoDisponibleSolicitado(Producto producto, Short periodo) {

        DetalleSolicitudCompromiso detalle = (DetalleSolicitudCompromiso) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.montoDisponible= :monto AND d.actividadProducto.id= :producto ")
                .setParameter("monto", getSueldoDisponible(producto, periodo)).setParameter("producto", producto.getId()).getSingleResult();

        return detalle;
    }

    public List<Presupuesto> getPArtidasTotales(Short periodo, String[] codigo) {
        List<Presupuesto> listaProforma = (List<Presupuesto>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p, Presupuesto pre WHERE p.periodo= :periodo AND p.codigo IN (:codigo) AND pre.partida=p.partidaPresupuestaria")
                .setParameter("periodo", periodo).setParameter("codigo", codigo).getResultList();
        return listaProforma;
    }

    public List<ProformaPresupuestoPlanificado> GetPartidasDirectcasAndDistributivo(Short periodo, String[] codigo) {
        List<ProformaPresupuestoPlanificado> listaProforma = new ArrayList<>();

        if (getPeriodoAprobado(periodo)) {
            listaProforma = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND p.codigo IN (:codigo)")
                    .setParameter("periodo", periodo).setParameter("codigo", codigo).getResultList();

        }

        return listaProforma;

    }

    public BigDecimal getSueldoDisponiblePartidas(ProformaPresupuestoPlanificado p, Short periodo) {

        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT MIN(d.montoDisponible) FROM DetalleSolicitudCompromiso d WHERE d.partidasDirecta.id= :id AND d.periodo= :periodo ")
                .setParameter("id", p.getId()).setParameter("periodo", periodo).getSingleResult();

        return resultMax;
    }

    public DetalleSolicitudCompromiso getSueldoDisponibleObjetoPritdas(ProformaPresupuestoPlanificado p, Short periodo) {

        DetalleSolicitudCompromiso detalle = (DetalleSolicitudCompromiso) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.montoDisponible= :monto AND d.partidasDirecta.id= :id ")
                .setParameter("monto", getSueldoDisponiblePartidas(p, periodo)).setParameter("id", p.getId()).getSingleResult();

        return detalle;
    }

    public List<Presupuesto> getPresupuestoPartidasDandPD(Short periodo, List<String> codigo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createQuery("SELECT DISTINCT(p) FROM Presupuesto p,ProformaPresupuestoPlanificado pr WHERE pr.partidaPresupuestaria=p.partida AND pr.codigo IN (:codigo) AND p.periodo= :periodo AND pr.codigoReforma IS NULL AND pr.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).setParameter("codigo", codigo).getResultList();
        return result;

    }

    public BigDecimal getSueldoDisponiblePresupuesto(Presupuesto p, Short periodo) {

        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc WHERE d.presupuesto.id= :id AND d.periodo= :periodo AND d.refDistributivo is null AND sc.estado.codigo NOT IN('RECHA','ADA') ")
                .setParameter("id", p.getId()).setParameter("periodo", periodo).getSingleResult();

        return resultMax;
    }

    public BigDecimal getSueldoDisponiblePartida(Presupuesto p, Short periodo, BigInteger ref) {

        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc WHERE d.presupuesto.id= :id AND d.periodo= :periodo AND d.refDistributivo=:ref AND sc.estado.codigo NOT IN('RECHA','ADA') ")
                .setParameter("id", p.getId()).setParameter("periodo", periodo).setParameter("ref", ref).getSingleResult();

        return resultMax;
    }

    public DetalleSolicitudCompromiso getSueldoDisponibleObjetoPresupuesto(Presupuesto p, Short periodo) {

        DetalleSolicitudCompromiso detalle = (DetalleSolicitudCompromiso) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.montoDisponible= :monto AND d.presupuesto.id= :id ")
                .setParameter("monto", getSueldoDisponiblePresupuesto(p, periodo)).setParameter("id", p.getId()).getSingleResult();

        return detalle;
    }

    public BigDecimal sumaTotalDeReservasDevengado(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(de.montoSolicitado),0) FROM DetalleSolicitudCompromiso de INNER JOIN de.refDistributivo pres INNER JOIN de.solicitud so where de.periodo= :periodo AND pres.id= :id AND so.estado.codigo in ('APRO','LIQUI','LIBE')")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public BigDecimal sumaTotalDeReservasDevengadoPd(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(de.montoSolicitado),0) FROM DetalleSolicitudCompromiso de INNER JOIN de.partidasDirecta pres INNER JOIN de.solicitud so where de.periodo= :periodo AND pres.id= :id AND so.estado.codigo in ('APRO','LIQUI','LIBE')")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public BigDecimal sumaTotalDeReservasPDA(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(de.montoSolicitado),0) FROM DetalleSolicitudCompromiso de INNER JOIN de.presupuesto pres INNER JOIN de.solicitud so where de.periodo= :periodo AND pres.id= :id AND de.refDistributivo is null AND so.estado.codigo in ('APRO','LIQUI')")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public BigDecimal sumaTotalDeReservasPD(Short periodo, BigInteger ref) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(de.montoSolicitado),0) FROM DetalleSolicitudCompromiso de INNER JOIN de.presupuesto pres INNER JOIN de.solicitud so where de.periodo= :periodo AND de.refDistributivo=:ref  AND so.estado.codigo in ('APRO','LIQUI')")
                .setParameter("periodo", periodo).setParameter("ref", ref).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public int actualizarReservaPresupuesto(BigDecimal valor, Short periodo, Long d) {

        Query query = em.createQuery("UPDATE ThCargoRubros p SET p.reserva= :reserva WHERE p.id=:id ")
                .setParameter("reserva", valor)
                .setParameter("id", d);
        int result = query.executeUpdate();
        return result;
    }

    public int actualizarReservaPresupuestoPd(BigDecimal valor, Short periodo, Long d) {

        Query query = em.createQuery("UPDATE ProformaPresupuestoPlanificado p SET p.reserva= :reserva WHERE p.id=:id ")
                .setParameter("reserva", valor)
                .setParameter("id", d);
        int result = query.executeUpdate();
        return result;
    }

    public BigDecimal sumaTotalDeReservasProducto(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(de.montoSolicitado),0) FROM DetalleSolicitudCompromiso de INNER JOIN de.actividadProducto ap INNER JOIN de.solicitud so where de.periodo= :periodo AND ap.id= :id AND so.estado.codigo in ('APRO','LIQUI','LIBE')")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public int actualizarReservaProducto(BigDecimal valor, Short periodo, Long p) {

        String partida = (String) em.createQuery("SELECT p.codigoPresupuestario FROM Producto p where p.id= :id and p.periodo= :anio ")
                .setParameter("id", p).setParameter("anio", periodo)
                .getResultStream().findFirst().orElse(null);

        Query query = em.createQuery("UPDATE Presupuesto p SET p.reserva= :reserva WHERE p.partida= :partida AND p.periodo= :periodo ")
                .setParameter("reserva", valor)
                .setParameter("partida", partida)
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();
        return result;
    }

    public int updateReservaProducto(BigDecimal valor, Short periodo, Long p) {

        BigDecimal monto = (BigDecimal) em.createQuery("SELECT p.monto FROM Producto p WHERE p.id= :id1").setParameter("id1", p).getSingleResult();
        BigDecimal saldo = monto.subtract(valor);
        Query query = em.createQuery("UPDATE Producto p SET p.reserva= :reserva, p.saldoDisponible= :saldo WHERE p.id= :id AND p.periodo= :periodo")
                .setParameter("reserva", valor)
                .setParameter("saldo", saldo)
                .setParameter("id", p)
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();
        return result;
    }

    public BigDecimal sumaTotalComprometidoPresupuesto(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT SUM(de.montoComprometido) FROM DetalleSolicitudCompromiso de INNER JOIN de.refDistributivo pres INNER JOIN de.solicitud so where de.periodo= :periodo AND pres.id= :id AND so.estado.codigo='APRO'")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

     public BigDecimal sumaTotalComprometidoPresupuestoPdi(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT SUM(de.montoComprometido) FROM DetalleSolicitudCompromiso de INNER JOIN de.partidasDirecta pres INNER JOIN de.solicitud so where de.periodo= :periodo AND pres.id= :id AND so.estado.codigo='APRO'")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    
    public int actualizarComprometidoReservaPresupuesto(BigDecimal valor, Short periodo, Long d) {

        Query query = em.createQuery("UPDATE ThCargoRubros p SET p.comprometido= :reserva WHERE p.id=:id ")
                .setParameter("reserva", valor)
                .setParameter("id", d);

        int result = query.executeUpdate();
        return result;
    }

    public int actualizarComprometidoReservaPresupuestoPd(BigDecimal valor, Short periodo, Long d) {

        Query query = em.createQuery("UPDATE ProformaPresupuestoPlanificado p SET p.comprometido= :reserva WHERE p.id=:id ")
                .setParameter("reserva", valor)
                .setParameter("id", d);

        int result = query.executeUpdate();
        return result;
    }

    public BigDecimal sumaTotalCompromisoProducto(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT SUM(de.montoComprometido) FROM DetalleSolicitudCompromiso de INNER JOIN de.actividadProducto ap INNER JOIN de.solicitud so where de.periodo= :periodo AND ap.id= :id AND so.estado.codigo='APRO'")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public int actualizarComprometidoReservaProducto(BigDecimal valor, Short periodo, Long p) {

        String partida = (String) em.createQuery("SELECT p.codigoPresupuestario FROM Producto p where p.id= :id and p.periodo= :anio")
                .setParameter("id", p).setParameter("anio", periodo)
                .getResultStream().findFirst().orElse(null);

        Query query = em.createQuery("UPDATE Producto p set p.comprometido= :reserva where   ")
                .setParameter("reserva", valor)
                .setParameter("partida", partida)
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();
        return result;
    }

    public int updateComprometidoReservaProducto(BigDecimal valor, Short periodo, Long p) {
        Query query = em.createQuery("UPDATE Producto p SET p.comprometido= :reserva WHERE p.id= :id AND p.periodo= :periodo")
                .setParameter("reserva", valor)
                .setParameter("id", p)
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();
        return result;
    }

    public List<ThCargoRubros> listaReservasSinRepetir(Short periodo) {
        return (List<ThCargoRubros>) em.createQuery("SELECT DISTINCT(pre) FROM DetalleSolicitudCompromiso p INNER JOIN p.refDistributivo pre where p.periodo= :periodo AND p.refDistributivo IS NOT NULL").setParameter("periodo", periodo).getResultList();

    }

    public List<ProformaPresupuestoPlanificado> listaReservasPdSinRepetir(Short periodo) {
        return (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT DISTINCT(pre) FROM DetalleSolicitudCompromiso p INNER JOIN p.partidasDirecta pre where p.periodo= :periodo AND p.partidasDirecta IS NOT NULL").setParameter("periodo", periodo).getResultList();

    }

    public List<DetalleSolicitudCompromiso> reservaDetalle(long id) {
        Query query = em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id = ?1")
                .setParameter(1, id);
        List<DetalleSolicitudCompromiso> result = query.getResultList();
        return result;
    }

    public List<Producto> listaReservasSinRepetirProducto(Short periodo) {
        List<Producto> result = (List<Producto>) em.createQuery("SELECT DISTINCT(pre) FROM DetalleSolicitudCompromiso p INNER JOIN p.actividadProducto pre INNER JOIN p.solicitud sr where p.periodo= :periodo AND pre.periodo= :periodo2 AND p.actividadProducto IS NOT NULL AND sr.estado.codigo  IN ('APRO','LIQUI')")
                .setParameter("periodo", periodo).setParameter("periodo2", periodo).getResultList();
        return result;
    }

    public List<DetalleSolicitudCompromiso> getListaDetlleSolciitud(SolicitudReservaCompromiso s) {
        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud= :solicitud")
                .setParameter("solicitud", s).getResultList();

        return result;
    }

    public List<DetalleSolicitudCompromiso> getEdicionDetalleProducto(SolicitudReservaCompromiso s) {

        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id =:solicitud AND d.actividadProducto IS NOT NULL ")
                .setParameter("solicitud", s.getId()).getResultList();
        return result;

    }

    public List<DetalleSolicitudCompromiso> getEdicionDetallePDAndD(SolicitudReservaCompromiso s) {

        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id =:solicitud AND d.refDistributivo IS NOT NULL ")
                .setParameter("solicitud", s.getId()).getResultList();
        return result;

    }

    public List<DetalleSolicitudCompromiso> getEdicionDetallePD(SolicitudReservaCompromiso s) {

        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id =:solicitud AND d.partidasDirecta IS NOT NULL ")
                .setParameter("solicitud", s.getId()).getResultList();
        return result;

    }

    public void eliminarSOlicitudesCascade(SolicitudReservaCompromiso s) {
        String hql = "delete from DetalleSolicitudCompromiso d WHERE d.solicitud.id= :id";
        Query query = em.createQuery(hql)
                .setParameter("id", s.getId());

        query.executeUpdate();
    }

    public void eliminarSoclitudRequisitios(SolicitudReservaCompromiso sa) {

        Query query = em.createQuery("delete from SolicitudRequisito s WHERE s.idSolicitudReserva.id=:id")
                .setParameter("id", sa.getId());

        query.executeUpdate();
    }

    /**
     *
     * @param p hacer referencia a la suma de los productos
     * @return
     */
    public BigDecimal getSumaEdicionProductos(Producto p) {
        BigDecimal suma = BigDecimal.ZERO;

        if (p != null) {
            suma = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.actividadProducto.id=:idproducto AND sc.estado.codigo NOT IN('RECHA','ADA')")
                    .setParameter("idproducto", p.getId()).getSingleResult();

            if (suma == null) {
                suma = BigDecimal.ZERO;
            }

        }
        return suma;

    }

    public BigDecimal getValorActual(DetalleSolicitudCompromiso p) {
        BigDecimal suma = BigDecimal.ZERO;

        if (p != null) {
            suma = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d where d.id= :id")
                    .setParameter("id", p.getId()).getSingleResult();

            if (suma == null) {
                suma = BigDecimal.ZERO;
            }

        }
        return suma;

    }

    public BigDecimal getSumaEdicionPresupuesto(Long id, Boolean distributivo) {
        BigDecimal suma2 = BigDecimal.ZERO;
        if (id != null) {
            if (distributivo) {
                suma2 = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.refDistributivo.id=:id AND sc.estado.codigo NOT IN('RECHA','ADA')")
                        .setParameter("id", id).getSingleResult();

                if (suma2 == null) {
                    suma2 = BigDecimal.ZERO;
                }

            } else {
                suma2 = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.partidasDirecta.id =:id AND sc.estado.codigo NOT IN('RECHA','ADA')")
                        .setParameter("id", id).getSingleResult();

                if (suma2 == null) {
                    suma2 = BigDecimal.ZERO;
                }
            }
        }

        return suma2;
    }

    public PartidasDistributivo getValorDisributivoEdicion(BigInteger id) {

        PartidasDistributivo result = (PartidasDistributivo) em.createQuery("SELECT p FROM PartidasDistributivo p WHERE p.codigoReforma is null AND p.codigoReformaTraspaso IS NULL AND p.estado=true AND p.id=:id")
                .setParameter("id", id.longValue()).getSingleResult();
        return result;
    }

    public SolicitudReservaCompromiso verfifcarExistenciaReserva(SolicitudReservaCompromiso s) {
        SolicitudReservaCompromiso result = (SolicitudReservaCompromiso) em.createQuery("SELECT s FROM SolicitudReservaCompromiso s WHERE s.id= :id")
                .setParameter("id", s.getId()).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<UnidadAdministrativa> getListaUnidadesReserva() {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT(u) FROM SolicitudReservaCompromiso s INNER JOIN s.unidadRequiriente u").getResultList();

        return result;
    }

    public List<Cliente> getListaBeneficiarioReserva() {
        List<Cliente> result = (List<Cliente>) em.createQuery("SELECT DISTINCT(b) FROM SolicitudReservaCompromiso s INNER JOIN s.beneficiario b").getResultList();

        return result;
    }

    public List<CatalogoItem> getListaEstadosReserva() {
        List<CatalogoItem> result = (List<CatalogoItem>) em.createQuery("SELECT DISTINCT(e) FROM SolicitudReservaCompromiso s INNER JOIN s.estado e").getResultList();

        return result;
    }

    //RESERVA ANUALDAS
    public BigDecimal sumaTotalDeReservasProductoAnulados(Short periodo, Long p) {
        BigDecimal suma = (BigDecimal) em.createQuery("SELECT SUM(de.montoSolicitado) FROM DetalleSolicitudCompromiso de INNER JOIN de.actividadProducto ap INNER JOIN de.solicitud so where de.periodo= :periodo AND ap.id= :id AND so.estado.codigo='ADA'")
                .setParameter("periodo", periodo).setParameter("id", p).getSingleResult();

        if (suma == null) {
            suma = BigDecimal.ZERO;
        }
        return suma;
    }

    public List<Producto> listaReservasSinRepetirProductoAnulados(Short periodo) {
        List<Producto> result = (List<Producto>) em.createQuery("SELECT DISTINCT(pre) FROM DetalleSolicitudCompromiso p INNER JOIN p.actividadProducto pre INNER JOIN p.solicitud sr where p.periodo= :periodo AND pre.periodo= :periodo2 AND p.actividadProducto IS NOT NULL AND sr.estado.codigo='ADA'")
                .setParameter("periodo", periodo).setParameter("periodo2", periodo).getResultList();
        return result;
    }

    public String conceptoPartidas(String partida, Short periodo) {
        String re = "";
        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p"
                + " where p.partidaPresupuestaria= :partida AND p.estado=true AND p.periodo= :periodo AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);

        if (result == null) {
            re = "NO HAY";
        } else {
            re = result.getCodigo();
        }

        return re;

    }

    public List<SolicitudReservaCompromiso> listasinContratoyReserva(Short periodo) {
        List<SolicitudReservaCompromiso> result = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT sr FROM SolicitudReservaCompromiso sr WHERE sr.id NOT IN (SELECT cr.reserva.id FROM ContratosReservaEjecuion cr) AND NOT EXISTS (SELECT dg FROM DiarioGeneral dg WHERE dg.certificacionesPresupuestario = sr AND dg.estado = true AND dg.periodo = ?1) AND sr.estado.codigo = 'APRO' and sr.periodo = ?2 ORDER BY sr.secuencial")
                .setParameter(1, periodo)
                .setParameter(2, periodo).getResultList();
        return result;

    }

    public List<SolicitudReservaCompromiso> listasinComprometerTotalmenteReserva(Short periodo) {
        List<SolicitudReservaCompromiso> result = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT sr FROM SolicitudReservaCompromiso sr WHERE sr.id IN (SELECT cr.solicitud.id FROM DetalleSolicitudCompromiso cr WHERE cr.montoComprometido < cr.montoSolicitado AND cr.periodo = ?1) AND sr.estado.codigo = 'APRO' and sr.periodo = ?2 ORDER BY sr.secuencial")
                .setParameter(1, periodo)
                .setParameter(2, periodo).getResultList();
        return result;

    }

    public List<SolicitudReservaCompromiso> listaSolicitud(BigInteger num) {
        List<SolicitudReservaCompromiso> result = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT s FROM SolicitudReservaCompromiso s WHERE s.numTramite= :num AND s.periodo =:periodo")
                .setParameter("num", num)
                .setParameter("periodo", Short.parseShort(Utils.getAnio(new Date()) + ""))
                .getResultList();
        return result;
    }

    private Cliente getCliente(String idCliente) {
        long id = Long.parseLong(idCliente);
        Cliente result = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.id=:idCliente")
                .setParameter("idCliente", id).getResultStream().findFirst().orElse(new Cliente());
        return result;
    }

    public Cliente devuelveClienteNotitfacion(BigInteger num) {
        Cliente cliente = new Cliente();
        try {
            Query query = em.createNativeQuery("SELECT cli.id FROM auth.usuarios us\n"
                    + "INNER JOIN talento_humano.servidor ser ON us.funcionario=ser.id\n"
                    + "INNER JOIN public.cliente cli ON ser.persona = cli.id\n"
                    + "WHERE us.usuario = (SELECT obs.user_cre FROM procesos.observaciones obs\n"
                    + "WHERE obs.id = (SELECT min(ob.id) FROM procesos.historico_tramites ht\n"
                    + "inner join procesos.observaciones ob ON ob.id_tramite = ht.id\n"
                    + "WHERE ht.num_tramite = ?1))")
                    .setParameter(1, num);
            List<Object> result = query.getResultList();
            if (result != null) {
                cliente = getCliente(result.get(0).toString());
            }
            return cliente;
        } catch (Exception e) {
            return cliente;
        }
    }

    public Cliente devuelveClienteNotitfacionDos(String user) {

//        BigInteger id = retornarEmail(num);
        Cliente result = (Cliente) em.createNativeQuery("select cli.* from\n"
                + "procesos.historico_tramites ht\n"
                + "inner join procesos.observaciones ob ON ob.id_tramite = ht.id\n"
                + "INNER JOIN auth.usuarios u ON u.usuario = ob.user_cre\n"
                + "inner join talento_humano.servidor serv on serv.id = u.funcionario\n"
                + "inner join cliente cli ON cli.id = serv.persona\n"
                + "where u.usuario=?1", Cliente.class).setParameter(1, user).getResultStream().findFirst().orElse(null);

        return result;
    }

    public BigInteger traemeCliente(String user) {
        try {
            BigInteger email = (BigInteger) em.createNativeQuery("select cli.id from auth.usuarios us\n"
                    + "                inner join talento_humano.servidor serv on serv.id = us.funcionario\n"
                    + "                inner join talento_humano.distributivo d on d.id = serv.distributivo\n"
                    + "                inner join public.unidad_administrativa ud on d.unidad_administrativa = ud.id\n"
                    + "                inner join talento_humano.cargo car ON car.id = d.cargo\n"
                    + "                inner join cliente cli ON cli.id = serv.persona\n"
                    + "                where us.usuario=?1").setParameter(1, user).getSingleResult();
            return email;
        } catch (Exception e) {
            return BigInteger.ZERO;
        }
    }
    
    public BigDecimal obtTotalComprometido (String partida) {
        try {
            BigDecimal total = (BigDecimal) em.createNativeQuery("SELECT COALESCE(SUM(dgdt.comprometido),0) total_comprometido from contabilidad.cont_diario_general_detalle dgdt WHERE dgdt.partida_presupuestaria = ?1").setParameter(1, partida).getSingleResult();
            return total;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    
    public BigDecimal obtTotalReservado (String partida) {
        try {
            BigDecimal total = (BigDecimal) em.createNativeQuery("SELECT COALESCE(ds.monto_solicitado,0) monto_solicitado\n" +
                                                                   "FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso ds\n" +
                                                                  "INNER JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso sr ON ds.solicitud = sr.id\n" +
                                                                  "INNER JOIN catalogo_item ca ON ca.id = sr.estado\n" +
                                                                  "WHERE ds.estado = true\n" +
                                                                  "  AND ca.codigo IN ('APRO','LIQUI','LIBE')\n" +
                                                                  "  AND (CASE WHEN ds.actividad_producto IS NOT NULL THEN\n" +
                                                                  "	     (SELECT pro.codigo_presupuestario FROM producto pro WHERE pro.id = ds.actividad_producto)\n" +
                                                                  "	  ELSE\n" +
                                                                  "	     (SELECT pre.partida FROM presupuesto pre WHERE pre.id = ds.presupuesto)\n" +
                                                                  "	  END) = ?1\n" +
                                                                  "  AND (CASE WHEN ds.actividad_producto IS NOT NULL THEN\n" +
                                                                  "	     (SELECT cpre.ingreso FROM producto pro\n" +
                                                                  "  	      INNER JOIN presupuesto.pres_catalogo_presupuestario cpre ON cpre.id = pro.item_new\n" +
                                                                  "	      WHERE pro.id = ds.actividad_producto)\n" +
                                                                  "       ELSE\n" +
                                                                  "	      (SELECT pre.tipo FROM presupuesto pre WHERE pre.id = ds.presupuesto)\n" +
                                                                  "	   END) = false").setParameter(1, partida).getSingleResult();
            return total;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    

    public BigInteger traemeCliente_2(String user) {
        try {
            BigInteger email = (BigInteger) em.createNativeQuery("select cli.id from auth.usuarios us\n"
                    + "inner join talento_humano.servidor serv on serv.id = us.funcionario\n"
                    + "inner join cliente cli ON cli.id = serv.persona\n"
                    + "where us.usuario=?1").setParameter(1, user).getSingleResult();
            return email;
        } catch (Exception e) {
            return BigInteger.ZERO;
        }
    }

    public Cliente devuelveClienteNotitfacion2(String user) {
        BigInteger id = traemeCliente(user);
        Cliente result = null;
        if (id.intValue() == 0) {
            id = traemeCliente_2(user);
        }
        if (id.intValue() > 0) {
            result = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.id= :id").setParameter("id", id.longValue()).getSingleResult();
        }
        return result;
    }

    public List<SolicitudReservaCompromiso> getReservaCompromisoApro() {
        try {
//            Query query = em.createNativeQuery("select DISTINCT sr.* from certificacion_presupuestaria_anual.solicitud_reserva_compromiso sr\n"
//                    + "inner join certificacion_presupuestaria_anual.detalle_solicitud_compromiso d ON sr.id = d.solicitud\n"
//                    + "where sr.estado = 416 AND d.monto_comprometido =0", SolicitudReservaCompromiso.class
//            );
            Query query = em.createQuery("SELECT sr FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sr WHERE sr.estado.id = 416 AND d.montoComprometido = 0");
            List<SolicitudReservaCompromiso> lista = (List<SolicitudReservaCompromiso>) query.getResultList();
            return lista;
        } catch (Exception e) {
            return null;
        }
    }

    public List<DetalleSolicitudCompromiso> getDetalleReservaApro(SolicitudReservaCompromiso reserva) {
        Query query = em.createQuery("SELECT dt FROM DetalleSolicitudCompromiso dt WHERE dt.solicitud = ?1 AND dt.montoComprometido = ?2")
                .setParameter(1, reserva).setParameter(2, BigDecimal.ZERO);
        List<DetalleSolicitudCompromiso> lista = (List<DetalleSolicitudCompromiso>) query.getResultList();
        return lista;
    }

    /*Sin Flujo de Proceso*/
    public List<Presupuesto> getPresupuestoPDandPDAandPDI(Short periodo, List<String> codigo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createQuery("SELECT DISTINCT(p) FROM Presupuesto p,ProformaPresupuestoPlanificado pr WHERE pr.partidaPresupuestaria=p.partida AND pr.codigo IN (:items) AND p.periodo= :periodo AND pr.codigoReforma IS NULL AND pr.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).setParameter("items", codigo).getResultList();
        return result;
    }

    public List<SolicitudReservaCompromiso> getReservaCompromisoAprobadaLiquidada() {
        try {
            Query query = em.createQuery("SELECT DISTINCT s from SolicitudReservaCompromiso s JOIN s.estado e \n"
                    + "where e.codigo in ('APRO','LIQUI') order by s.secuencial ASC ");
            List<SolicitudReservaCompromiso> lista = (List<SolicitudReservaCompromiso>) query.getResultList();
            return lista;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCodigoRol(String usuario, String rol) {
        String roluser = "";
        List<CatalogoItem> listRol = (List<CatalogoItem>) em.createNativeQuery("select ci.* from auth.usuarios u inner join auth.usuario_rol ur ON ur.usuario = u.id\n"
                + "                                  inner join auth.rol r ON r.id = ur.rol inner join catalogo_item ci on r.categoria=ci.id\n"
                + "                		     where u.estado = true AND u.estado=true AND u.usuario= ?1", CatalogoItem.class
        )
                .setParameter(1, usuario).getResultList();

        if (!listRol.isEmpty()) {
            for (CatalogoItem r : listRol) {
                if (r.getCodigo().equals(rol)) {
                    roluser = r.getCodigo();
                }
            }
        }

        return roluser;
    }

    public List<Presupuesto> listaPresupuestoPDI(Short periodo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createNativeQuery("SELECT DISTINCT(pre.*) FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso p\n"
                + "INNER JOIN presupuesto pre on p.presupuesto=pre.id\n"
                + "INNER join proforma_presupuesto_planificado pro\n"
                + "on pro.partida_presupuestaria=pre.partida\n"
                + "inner join certificacion_presupuestaria_anual.solicitud_reserva_compromiso s\n"
                + "ON s.id = p.solicitud inner join catalogo_item ci ON ci.id = s.estado\n"
                + "where p.periodo= ?1 AND p.presupuesto IS NOT NULL and ci.codigo in ('APRO','LIQUI')\n"
                + "and pro.codigo='PDI'", Presupuesto.class
        )
                .setParameter(1, periodo).getResultList();
        return result;
    }

    public List<Presupuesto> listaPresupuestoPDA(Short periodo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createNativeQuery("SELECT DISTINCT(pre.*) FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso p\n"
                + "INNER JOIN presupuesto pre on p.presupuesto=pre.id\n"
                + "INNER join proforma_presupuesto_planificado pro\n"
                + "on pro.partida_presupuestaria=pre.partida\n"
                + "inner join certificacion_presupuestaria_anual.solicitud_reserva_compromiso s\n"
                + "ON s.id = p.solicitud inner join catalogo_item ci ON ci.id = s.estado\n"
                + "where p.periodo= ?1 AND p.presupuesto IS NOT NULL and ci.codigo in ('APRO','LIQUI')\n"
                + "and pro.codigo='PDA'", Presupuesto.class
        )
                .setParameter(1, periodo).getResultList();
        return result;
    }

    public List<BigInteger> listaPresupuestoPD(Short periodo) {
        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("SELECT DISTINCT(p.re_distributivo) FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso p\n"
                + "        inner join certificacion_presupuestaria_anual.solicitud_reserva_compromiso s\n"
                + "        ON s.id = p.solicitud inner join catalogo_item ci ON ci.id = s.estado\n"
                + "        where p.periodo= ?1 AND p.presupuesto IS NOT NULL and p.re_distributivo is not null\n"
                + "		and ci.codigo in ('APRO','LIQUI')")
                .setParameter(1, periodo).getResultList();
        return result;

    }

    public void updatePDIReservas(BigDecimal valor, Short periodo, Presupuesto p) {

        Query query = em.createQuery("UPDATE Presupuesto p SET p.reserva= :reserva WHERE p.partida= :partida AND p.periodo= :periodo ")
                .setParameter("reserva", valor)
                .setParameter("partida", p.getPartida())
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();

        Query query2 = em.createQuery("UPDATE ProformaPresupuestoPlanificado p SET p.reserva= :reserva WHERE p.partidaPresupuestaria= :partida  AND p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("reserva", valor)
                .setParameter("partida", p.getPartida())
                .setParameter("periodo", periodo);
        int result2 = query2.executeUpdate();

        if (result > 0 && result2 > 0) {
            System.out.println("Reservas actualizadas");
        }
    }

    public void updatePDAReservas(BigDecimal valor, Short periodo, Presupuesto p) {

        Query query = em.createQuery("UPDATE Presupuesto p SET p.reserva= :reserva WHERE p.partida= :partida AND p.periodo= :periodo")
                .setParameter("reserva", valor)
                .setParameter("partida", p.getPartida())
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();

        Query query2 = em.createQuery("UPDATE PartidasDistributivoAnexo p set p.reserva=:reserva WHERE p.partidaAp=:partida AND p.periodo=:periodo AND p.codigoReforma is null AND p.codigoReformaTraspaso is null AND p.estado=true")
                .setParameter("reserva", valor)
                .setParameter("partida", p.getPartida())
                .setParameter("periodo", periodo);
        int result2 = query2.executeUpdate();

        if (result > 0 && result2 > 0) {
            System.out.println("Reservas actualizadas");
        }
    }

    public void updatePDReservas(BigDecimal valor, Short periodo, BigInteger id) {

//        Query query = em.createQuery("UPDATE Presupuesto p SET p.reserva= :reserva WHERE p.partida= :partida AND p.periodo= :periodo")
//                .setParameter("reserva", valor)
//                .setParameter("partida", p.getPartida())
//                .setParameter("periodo", periodo);
//        int result = query.executeUpdate();
        Query query2 = em.createQuery("UPDATE PartidasDistributivo p set p.reserva=:reserva WHERE p.id=:id AND p.periodo=:periodo AND p.codigoReforma is null AND p.codigoReformaTraspaso is null AND p.estado=true")
                .setParameter("reserva", valor)
                .setParameter("id", id.longValue())
                .setParameter("periodo", periodo);
        int result2 = query2.executeUpdate();

        if (result2 > 0) {
            System.out.println("Reservas actualizadas");
        }
    }

    public List<PartidasDistributivo> getListaDistributivosDetallados(String partida, Short periodo) {
        try {
            List<PartidasDistributivo> result = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p"
                    + " WHERE p.reformaCodificado >0 AND p.codigoReforma is null AND p.codigoReformaTraspaso is null AND p.partidaAp= :partida AND p.periodo=:periodo ")
                    .setParameter("partida", partida).setParameter("periodo", periodo).getResultList();
            return result;

        } catch (Exception e) {
            return null;
        }
    }

    public Presupuesto buscarPresupuesto(String partida, Short periodo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createQuery("SELECT p FROM Presupuesto p WHERE p.partida=:partida AND p.periodo=:periodo")
                .setParameter("partida", partida).setParameter("periodo", periodo).getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

    public BigDecimal montoDistributivo(BigInteger id) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(p.reformaCodificado,0) FROM PartidasDistributivo p WHERE p.id=:id")
                .setParameter("id", id.longValue()).getSingleResult();

        return result;
    }

    public BigDecimal getSumaEdicionPresupuestoDos(Presupuesto pre, BigInteger ref) {
        BigDecimal suma2 = BigDecimal.ZERO;
        if (pre != null) {
            suma2 = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.presupuesto.id= :id AND d.refDistributivo=:ref AND sc.estado.codigo NOT IN('RECHA','ADA')")
                    .setParameter("id", pre.getId().longValue()).setParameter("ref", ref).getSingleResult();

            if (suma2 == null) {
                suma2 = BigDecimal.ZERO;
            }
        }
        return suma2;
    }

    //NUEVO
    public BigDecimal getSueldoDisponibleCargosRubros(ThCargoRubros p, Short periodo) {

        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc WHERE d.refDistributivo.id = :id AND d.periodo = :periodo AND sc.estado.codigo NOT IN('RECHA','ADA') ")
                .setParameter("id", p.getId()).setParameter("periodo", periodo).getSingleResult();

        return resultMax;
    }

    public List<Short> listaAniosAprobados(Boolean aprobado) {
        return (List<Short>) em.createQuery("SELECT DISTINCT c.periodo FROM CatalogoProformaPresupuesto c WHERE c.aprobado=:aprobado ORDER by c.periodo DESC")
                .setParameter("aprobado", aprobado).getResultList();
    }

    public BigDecimal getSueldoDisponiblePD(ProformaPresupuestoPlanificado p, Short periodo) {
        BigDecimal resultMax = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc WHERE d.partidasDirecta.id = :id AND d.periodo = :periodo AND sc.estado.codigo NOT IN('RECHA','ADA') ")
                .setParameter("id", p.getId()).setParameter("periodo", periodo).getSingleResult();

        return resultMax;
    }

}
