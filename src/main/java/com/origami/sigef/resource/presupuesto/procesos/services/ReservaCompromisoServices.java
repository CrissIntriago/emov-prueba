package com.origami.sigef.resource.presupuesto.procesos.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReservaCompromisoServices extends AbstractService<SolicitudReservaCompromiso> {

    private static final Logger LOG = Logger.getLogger(ReservaCompromisoServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReservaCompromisoServices() {
        super(SolicitudReservaCompromiso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<UnidadAdministrativa> getListaUnidadesReservas() {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT(u) FROM SolicitudReservaCompromiso s INNER JOIN s.unidadRequiriente u ").getResultList();
        return result;

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

    public List<DetalleSolicitudCompromiso> getEdicionDetalleProducto(SolicitudReservaCompromiso s) {

        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id =:solicitud AND d.actividadProducto IS NOT NULL ")
                .setParameter("solicitud", s.getId()).getResultList();
        return result;

    }

    public List<DetalleSolicitudCompromiso> getEdicionDetallePDAndD(SolicitudReservaCompromiso s) {

        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud.id =:solicitud AND d.presupuesto IS NOT NULL ")
                .setParameter("solicitud", s.getId()).getResultList();
        return result;

    }

    public BigDecimal getSumaEdicionPresupuesto(Presupuesto pre) {
        BigDecimal suma2 = BigDecimal.ZERO;
        if (pre != null) {
            suma2 = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.presupuesto.id= :id AND sc.estado.codigo NOT IN('RECHA','ADA')")
                    .setParameter("id", pre.getId().longValue()).getSingleResult();

            if (suma2 == null) {
                suma2 = BigDecimal.ZERO;
            }

        }

        return suma2;
    }

    public BigDecimal getSumaEdicionPresupuestoDistributivo(Presupuesto pre, BigInteger ref) {
        BigDecimal suma2 = BigDecimal.ZERO;
        if (pre != null) {
            suma2 = (BigDecimal) em.createQuery("SELECT SUM(d.montoSolicitado) FROM DetalleSolicitudCompromiso d INNER JOIN d.solicitud sc where d.presupuesto.id= :id AND d.refDistributivo=:ref AND sc.estado.codigo NOT IN('RECHA','ADA')")
                    .setParameter("id", pre.getId()).setParameter("ref", ref).getSingleResult();

            if (suma2 == null) {
                suma2 = BigDecimal.ZERO;
            }

        }

        return suma2;
    }

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

    public List<Presupuesto> getPresupuestoPartidasDandPD(Short periodo, List<String> codigo) {
        List<Presupuesto> result = (List<Presupuesto>) em.createQuery("SELECT DISTINCT(p) FROM Presupuesto p,ProformaPresupuestoPlanificado pr WHERE pr.partidaPresupuestaria=p.partida AND pr.codigo IN (:codigo) AND p.periodo= :periodo AND pr.codigoReforma IS NULL AND pr.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).setParameter("codigo", codigo).getResultList();
        return result;

    }

    public List<DetalleSolicitudCompromiso> getListaDetlleSolciitud(SolicitudReservaCompromiso s) {
        List<DetalleSolicitudCompromiso> result = (List<DetalleSolicitudCompromiso>) em.createQuery("SELECT d FROM DetalleSolicitudCompromiso d where d.solicitud= :solicitud")
                .setParameter("solicitud", s).getResultList();

        return result;
    }

    public void updateContabilizado(Long item, Boolean contabilizado) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery(QUERY.UPDATE_RESERVA_COMPROMISO)
                .setParameter(1, item)
                .setParameter(2, contabilizado);
        executeUpdate = query.executeUpdate();
    }
}
