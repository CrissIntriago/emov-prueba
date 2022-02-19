/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ControlCuentaPresupuestario;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMIEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ReformaSuplementoIngresoService extends AbstractService<ReformaIngresoSuplemento> {

    @Inject
    private ManagerService service;

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReformaSuplementoIngresoService() {
        super(ReformaIngresoSuplemento.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Short> getListaPeriodosAprobados(Boolean flujo, Boolean aprobado) {
        List<Short> periodos = (List<Short>) em.createNativeQuery("select ca.periodo from catalogo_proforma_presupuesto ca where ca.aprobado=?1 and ca.tipo_flujo=?2")
                .setParameter(1, aprobado).setParameter(2, flujo).getResultList();
        return periodos;
    }

    public Integer maxNumeracion() {
        Integer numeracion = 0;
        Integer num = (Integer) em.createNativeQuery("SELECT MAX(r.numeracion) FROM presupuesto.reforma_ingreso_suplemento r ").getSingleResult();
        if (num == null) {
            numeracion = 1;
        } else {
            numeracion = num + 1;
        }

        return numeracion;
    }

    public List<ProformaIngreso> getListaCatalogoPresupuesto(Short periodo, Boolean flujo) {
        List<ProformaIngreso> data = (List<ProformaIngreso>) em.createQuery("SELECT cp FROM ProformaIngreso cp where cp.periodo= :periodo  order by cp.item.codigo ASC")
                .setParameter("periodo", periodo).getResultList();
        return data;
    }

    public List<CatalogoPresupuesto> getListaCatalogoPresupuesto(Boolean flujo) {
        List<CatalogoPresupuesto> data = (List<CatalogoPresupuesto>) em.createQuery("SELECT cp FROM CatalogoPresupuesto cp where cp.estado=true AND cp.flujoIngreso= :flujo order by cp.codigo ASC")
                .setParameter("flujo", flujo).getResultList();
        return data;
    }

    public List<CatalogoItem> getListaItemFiltro(Short periodo, Boolean flujo) {
        List<CatalogoItem> data = (List<CatalogoItem>) em.createQuery("SELECT DISTINCT(cp.clasificacion) FROM CatalogoPresupuesto cp where cp.anio= :periodo AND cp.estado=true AND cp.flujoIngreso= :flujo order by cp.clasificacion.codigo ASC")
                .setParameter("periodo", periodo).setParameter("flujo", flujo).getResultList();
        return data;

    }

    public CatalogoItem getlistaEstado(String estado, String tipo) {

        CatalogoItem data = (CatalogoItem) em.createQuery("SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE ci.codigo= :estado AND c.codigo= :tipo")
                .setParameter("estado", estado).setParameter("tipo", tipo).getSingleResult();
        return data;
    }

    public List<Nivel> getListaNiveles(Short periodo, Boolean flujo) {
        List<Nivel> data = (List<Nivel>) em.createQuery("SELECT DISTINCT(n) FROM CatalogoPresupuesto cp INNER JOIN cp.nivel n where cp.anio= :periodo AND cp.estado=true AND cp.flujoIngreso= :flujo order by n.orden ASC")
                .setParameter("periodo", periodo).setParameter("flujo", flujo).getResultList();
        return data;
    }

    public List<ReformaIngresoSuplemento> listaReformaSuplmento(boolean tipo) {
        List<ReformaIngresoSuplemento> lista = (List<ReformaIngresoSuplemento>) em.createQuery("SELECT r from ReformaIngresoSuplemento r  WHERE r.tipo= :tipo")
                .setParameter("tipo", tipo).getResultList();
        return lista;
    }

    public List<Cupos> listaReformaSuplmento2(boolean tipo, UnidadAdministrativa u) {
        List<Cupos> lista = (List<Cupos>) em.createQuery("SELECT c from Cupos c INNER JOIN c.reforma r INNER JOIN c.unidadAdministrativa u  WHERE r.tipo= :tipo AND u.id= :unidad")
                .setParameter("tipo", tipo).setParameter("unidad", u.getId()).getResultList();
        return lista;
    }

    public BigDecimal getTotalSuplemento(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(dr.suplemento),0) FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.reforma r WHERE r.id= :id ").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getTotalReduccionReforma(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(dr.reducido),0) FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.reforma  r WHERE r.id= :id").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getTotalReduccion(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(dr.reducido),0) FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.reforma r WHERE r.id= :id ").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getTotalReformaPinicial(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(dr.proformaIngreso.presupuestoCodificado),0) FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.reforma  r WHERE r.id= :id AND dr.proformaIngreso.item.confId.nivel =2").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public BigDecimal getTotalReformaPcodificado(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(dr.codificado),0) FROM DetalleReformaIngresoSuplemento dr INNER JOIN dr.reforma  r WHERE r.id= :id AND dr.proformaIngreso.item.confId.nivel =2").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

    public List<CatalogoProformaPresupuesto> getPeriodoVer(Short periodo) {
        List<CatalogoProformaPresupuesto> result = (List<CatalogoProformaPresupuesto>) em.createQuery("SELECT c FROM CatalogoProformaPresupuesto c where c.aprobado=true AND c.periodo=:periodo").setParameter("periodo", periodo).getResultList();
        return result;
    }

    public BigDecimal getCupoAsignado(BigInteger b) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=TRUE")
                .setParameter("reforma", b).getSingleResult();
        return result;
    }
    
     public BigDecimal getCupoAsignadoDa(BigInteger b) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=FALSE")
                .setParameter("reforma", b).getSingleResult();
        return result;
    }

    public List<DetalleReformaIngresoSuplemento> getDetalleReormaIngreso(ReformaIngresoSuplemento r) {
        List<DetalleReformaIngresoSuplemento> result = (List<DetalleReformaIngresoSuplemento>) em.createQuery("SELECT r FROM DetalleReformaIngresoSuplemento r INNER JOIN  r.proformaIngreso.item pr WHERE r.reforma= :reforma ORDER BY pr.codigo ASC")
                .setParameter("reforma", r).getResultList();
        return result;
    }

    public void actualizarEstadoPapp(CatalogoItem c, BigInteger b) {

        Query query1 = em.createQuery("UPDATE Producto p SET p.estadoPapp= :estado1 WHERE p.codigoReforma= :reforma1")
                .setParameter("estado1", c).setParameter("reforma1", b);
        int result1 = query1.executeUpdate();

        Query query2 = em.createQuery("UPDATE ActividadOperativa a SET a.estadoPapp= :estado2 WHERE a.codigoReforma= :reforma2")
                .setParameter("estado2", c).setParameter("reforma2", b);
        int result2 = query2.executeUpdate();

        System.out.println("actualizados " + result1 + "\t \t" + result2);

//        Query query3 = em.createQuery("UPDATE PlanAnualProgramaProyecto p SET p.estadoPapp= :estado3 WHERE p.codigoReforma= :reforma3")
//                .setParameter("estado3", c).setParameter("reforma3", b);
//        int result3 = query3.executeUpdate();
//
//        Query query4 = em.createQuery("UPDATE PlanAnualPoliticaPublica p SET p.estadoPapp= :estado4 WHERE p.codigoReforma= :reforma4")
//                .setParameter("estado4", c).setParameter("reforma4", b);
//        int result4 = query4.executeUpdate();
//
//        Query query5 = em.createQuery("UPDATE PlanLocalProgramaProyecto p SET p.estadoPapp= :estado5 WHERE p.codigoReforma= :reforma5")
//                .setParameter("estado5", c).setParameter("reforma5", b);
//        int result5 = query5.executeUpdate();
    }

    public CatalogoItem getEstadoNombre(BigInteger b) {
        CatalogoItem c = (CatalogoItem) em.createQuery("SELECT p.estadoPapp FROM Producto p WHERE p.codigoReforma =:reforma").setParameter("reforma", b).getResultStream().findFirst().orElse(null);
        return c;
    }

    public BigDecimal getSuplementoPapp(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<Producto> p = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();

        if (p.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.suplementoEgreso),0) FROM Producto p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                return BigDecimal.ZERO;
            }
        }
        return result;
    }

    public BigDecimal getSuplementoDistributivo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<PartidasDistributivo> lista = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p where p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();

        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM PartidasDistributivo p WHERE  p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }

        return result;
    }

    public BigDecimal getSuplementoDistributivoNuevo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<ThCargoRubros> lista = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p where p.codigoReforma= :reforma AND p.idRubro.origen=TRUE AND p.idRubro.ingreso=TRUE")
                .setParameter("reforma", b).getResultList();

        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ThCargoRubros p WHERE  p.codigoReforma = :reforma AND p.idRubro.origen=TRUE AND p.idRubro.ingreso=TRUE")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }

        return result;
    }

    public BigDecimal getSuplementoDistributivoAnexo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<PartidasDistributivoAnexo> lista = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {

            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;

    }

    public BigDecimal getSuplementoDistributivoAnexoNuevo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<ThCargoRubros> lista = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=FALSE AND p.idRubro.ingreso=TRUE")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {

            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=FALSE AND p.idRubro.ingreso=TRUE")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;

    }

    public BigDecimal getSuplementoPartidasDirectas(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<ProformaPresupuestoPlanificado> lista = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma ")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;
    }

    public BigDecimal getReduccionPapp(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<Producto> p = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();

        if (p.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.reduccionEgreso) FROM Producto p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                return BigDecimal.ZERO;
            }
        }
        return result;
    }

    public BigDecimal getReduccionDistributivo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<PartidasDistributivo> lista = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM PartidasDistributivo p where p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();

        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM PartidasDistributivo p WHERE  p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }

        return result;
    }

    public BigDecimal getReduccionDistributivoNuevo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<PartidasDistributivo> lista = (List<PartidasDistributivo>) em.createQuery("SELECT p FROM ThCargoRubros p where p.codigoReforma= :reforma AND p.idRubro.origen=TRUE AND p.idRubro.ingreso=TRUE")
                .setParameter("reforma", b).getResultList();

        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ThCargoRubros p WHERE  p.codigoReforma= :reforma AND p.idRubro.origen=TRUE AND p.idRubro.ingreso=TRUE")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }

        return result;
    }

    public BigDecimal getReduccionDistributivoAnexo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<PartidasDistributivoAnexo> lista = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {

            result = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;

    }

    public BigDecimal getReduccionDistributivoAnexoNuevo(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;
        List<PartidasDistributivoAnexo> lista = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=FALSE AND p.idRubro.ingreso=TRUE")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {

            result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.idRubro.origen=FALSE AND p.idRubro.ingreso=TRUE")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);

            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;

    }

    public BigDecimal getVerificarAprobacionPapp(ReformaIngresoSuplemento r) {
        if (r == null) {
            return BigDecimal.ZERO;
        }
        BigInteger b = BigInteger.valueOf(r.getId());
        BigDecimal result = BigDecimal.ZERO;

        if (r.getTipo()) {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.suplementoEgreso) FROM Producto p WHERE  p.codigoReforma= :reforma").setParameter("reforma", b).getResultStream().findFirst().orElse(null);
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.reduccionEgreso) FROM Producto p WHERE p.codigoReforma= :reforma").setParameter("reforma", b).getResultStream().findFirst().orElse(null);
        }

        if (result == null) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public BigDecimal getReduccionPartidasDirectas(BigInteger b) {
        BigDecimal result = BigDecimal.ZERO;

        List<ProformaPresupuestoPlanificado> lista = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        if (lista.isEmpty()) {
            result = BigDecimal.ZERO;
        } else {
            result = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                    .setParameter("reforma", b).getResultStream().findFirst().orElse(null);
            if (result == null) {
                result = BigDecimal.ZERO;
            }
        }
        return result;
    }

    public List<DetalleReformaIngresoSuplemento> getDetalleItem(ReformaIngresoSuplemento r) {
        List<DetalleReformaIngresoSuplemento> result = (List<DetalleReformaIngresoSuplemento>) em.createQuery("SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.reforma= :reforma order by d.id asc")
                .setParameter("reforma", r).getResultList();

        return result;
    }

    // VERIFICAR SI TODO ESTA CORRECTO CON RESPECTO A LA REFORMA DE INGRESO Y EGRESO
    public BigDecimal totalIngresoReforma(ReformaIngresoSuplemento r) {
        if (r == null) {
            return BigDecimal.ZERO;
        }
        BigInteger reforma = BigInteger.valueOf(r.getId());
        BigDecimal montoIngreso = BigDecimal.ZERO;
        if (r.getTipo()) {
            montoIngreso = (BigDecimal) em.createQuery("SELECT SUM(d.suplemento) FROM DetalleReformaIngresoSuplemento d WHERE  d.reforma= :reforma").setParameter("reforma", r).getResultStream().findFirst().orElse(null);
            if (montoIngreso != null) {

            } else {
                montoIngreso = BigDecimal.ZERO;

            }

        } else {
            montoIngreso = (BigDecimal) em.createQuery("SELECT SUM(d.reducido) FROM DetalleReformaIngresoSuplemento d WHERE d.reforma= :reforma").setParameter("reforma", r).getResultStream().findFirst().orElse(null);
            if (montoIngreso != null) {

            } else {
                montoIngreso = BigDecimal.ZERO;

            }

        }
        return montoIngreso;
    }

    public BigDecimal totalEgresoReforma(ReformaIngresoSuplemento r) {
        if (r == null) {
            return BigDecimal.ZERO;
        }
        BigInteger reforma = BigInteger.valueOf(r.getId());
        BigDecimal monto_Papp = BigDecimal.ZERO, monto_Dist = BigDecimal.ZERO, montoPartidasDirectas = BigDecimal.ZERO;
        BigDecimal resultadoEgreso = BigDecimal.ZERO;

        List<Producto> listaProducto = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();
        List<ThCargoRubros> listaDistributivo = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();
//        List<PartidasDistributivoAnexo> listaDistributivoAnexo = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
//                .setParameter("reforma", reforma).getResultList();
        List<ProformaPresupuestoPlanificado> listaPArtidasDirectas = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();

        if (r.getTipo()) {

            if (listaProducto.isEmpty()) {

                monto_Papp = BigDecimal.ZERO;
            } else {
                monto_Papp = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.suplementoEgreso),0) FROM Producto p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

            if (listaDistributivo.isEmpty()) {
                monto_Dist = BigDecimal.ZERO;
            } else {
                monto_Dist = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

//            if (listaDistributivoAnexo.isEmpty()) {
//                montoDist_Anexo = BigDecimal.ZERO;
//            } else {
//                montoDist_Anexo = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
//                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
//            }
            if (listaPArtidasDirectas.isEmpty()) {
                montoPartidasDirectas = BigDecimal.ZERO;
            } else {
                montoPartidasDirectas = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaSuplemento),0) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

        } else {

            if (listaProducto.isEmpty()) {
                monto_Papp = BigDecimal.ZERO;

            } else {
                monto_Papp = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reduccionEgreso),0) FROM Producto p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

            if (listaDistributivo.isEmpty()) {
                monto_Dist = BigDecimal.ZERO;
            } else {
                monto_Dist = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ThCargoRubros p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);

            }
//            if (listaDistributivoAnexo.isEmpty()) {
//                montoDist_Anexo = BigDecimal.ZERO;
//            } else {
//                montoDist_Anexo = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM PartidasDistributivoAnexo p WHERE  p.codigoReforma= :reforma")
//                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
//            }

            if (listaPArtidasDirectas.isEmpty()) {
                montoPartidasDirectas = BigDecimal.ZERO;
            } else {
                montoPartidasDirectas = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ProformaPresupuestoPlanificado p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }
        }

        resultadoEgreso = monto_Papp.add(monto_Dist).add(montoPartidasDirectas);

        return resultadoEgreso;
    }

    public Boolean getReformaEquilibrada(ReformaIngresoSuplemento r) {
        Boolean verificador = true;
        BigInteger reforma = BigInteger.valueOf(r.getId());
        //VARIABLES GLOBALES
        BigDecimal resultadoIngreso = BigDecimal.ZERO, resultadoEgreso = BigDecimal.ZERO;

        //VARIABLES PARA INGRESO
        BigDecimal montoIngreso = BigDecimal.ZERO;

        //VARIBALES PARA EGRESO
        BigDecimal monto_Papp = BigDecimal.ZERO, monto_Dist = BigDecimal.ZERO, montoPartidasDirectas = BigDecimal.ZERO;

        //<editor-fold defaultstate="collapsed" desc="INGRESO">
        if (r.getTipo()) {
            montoIngreso = (BigDecimal) em.createQuery("SELECT SUM(d.suplemento) FROM DetalleReformaIngresoSuplemento d WHERE  d.reforma= :reforma").setParameter("reforma", r).getResultStream().findFirst().orElse(null);
            if (montoIngreso == null) {
                montoIngreso = BigDecimal.ZERO;
            }
        } else {
            montoIngreso = (BigDecimal) em.createQuery("SELECT SUM(d.reducido) FROM DetalleReformaIngresoSuplemento d WHERE d.reforma= :reforma").setParameter("reforma", r).getResultStream().findFirst().orElse(null);
            if (montoIngreso == null) {
                montoIngreso = BigDecimal.ZERO;
            }
        }
//</editor-fold>

        resultadoIngreso = montoIngreso;

        //<editor-fold defaultstate="collapsed" desc="EGRESO">
        List<Producto> listaProducto = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();
        List<ThCargoRubros> listaDistributivo = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();
//        List<PartidasDistributivoAnexo> listaDistributivoAnexo = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
//                .setParameter("reforma", reforma).getResultList();
        List<ProformaPresupuestoPlanificado> listaPArtidasDirectas = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", reforma).getResultList();

        if (r.getTipo()) {

            if (listaProducto.isEmpty()) {

                monto_Papp = BigDecimal.ZERO;
            } else {
                monto_Papp = (BigDecimal) em.createQuery("SELECT SUM(p.suplementoEgreso) FROM Producto p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

            if (listaDistributivo.isEmpty()) {
                monto_Dist = BigDecimal.ZERO;
            } else {
                monto_Dist = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM ThCargoRubros p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

//            if (listaDistributivoAnexo.isEmpty()) {
//                montoDist_Anexo = BigDecimal.ZERO;
//            } else {
//                montoDist_Anexo = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma")
//                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
//            }
            if (listaPArtidasDirectas.isEmpty()) {
                montoPartidasDirectas = BigDecimal.ZERO;
            } else {
                montoPartidasDirectas = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

        } else {

            if (listaProducto.isEmpty()) {
                monto_Papp = BigDecimal.ZERO;

            } else {
                monto_Papp = (BigDecimal) em.createQuery("SELECT SUM(p.reduccionEgreso) FROM Producto p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }

            if (listaDistributivo.isEmpty()) {
                monto_Dist = BigDecimal.ZERO;
            } else {
                monto_Dist = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM ThCargoRubros p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);

            }
//            if (listaDistributivoAnexo.isEmpty()) {
//                montoDist_Anexo = BigDecimal.ZERO;
//            } else {
//                montoDist_Anexo = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM PartidasDistributivoAnexo p WHERE  p.codigoReforma= :reforma")
//                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
//            }

            if (listaPArtidasDirectas.isEmpty()) {
                montoPartidasDirectas = BigDecimal.ZERO;
            } else {
                montoPartidasDirectas = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM ProformaPresupuestoPlanificado p WHERE  p.codigoReforma= :reforma")
                        .setParameter("reforma", reforma).getResultStream().findFirst().orElse(null);
            }
        }

//</editor-fold>
        resultadoEgreso = monto_Papp.add(monto_Dist).add(montoPartidasDirectas);

        if (resultadoIngreso.doubleValue() == resultadoEgreso.doubleValue()) {

            verificador = true;
        } else {
            verificador = false;
        }

        return verificador;
    }

    public void actualizarIngresoEgreso(ReformaIngresoSuplemento r, CatalogoItem c) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        Query query1 = em.createQuery("UPDATE ThCargoRubros p SET p.estadoPartida = :estado WHERE p.codigoReforma= :reforma")
                .setParameter("estado", c).setParameter("reforma", reforma);
        int result1 = query1.executeUpdate();
//        Query query2 = em.createQuery("UPDATE PartidasDistributivoAnexo p SET p.estadoPartida= :estado WHERE p.codigoReforma= :reforma")
//                .setParameter("estado", c).setParameter("reforma", reforma);
//        int result2 = query2.executeUpdate();
        Query query3 = em.createQuery("UPDATE ProformaPresupuestoPlanificado p SET p.estadoPartida= :estado WHERE p.codigoReforma= :reforma")
                .setParameter("estado", c).setParameter("reforma", reforma);
        int result3 = query3.executeUpdate();

    }

    //ACTUALIZACION
    public List<CatalogoPresupuesto> getListaRefromaIngreso(Short periodo) {

        List<CatalogoPresupuesto> result = (List<CatalogoPresupuesto>) em.createQuery("SELECT d FROM CatalogoPresupuesto d WHERE d.anio= :anio AND d.estado=true")
                .setParameter("anio", periodo).getResultList();
        return result;

    }

    public ProformaIngreso getShowUTem(BigInteger id) {
//        String refe = id.toString();
//        Long referencia = Long.valueOf(refe);
        ProformaIngreso result = (ProformaIngreso) em.createQuery("SELECT c FROM ProformaIngreso c WHERE c.id= :referencia AND c.item.activo=TRUE ")
                .setParameter("referencia", id.longValue()).getResultStream().findFirst().orElse(null);
        return result;

    }

    public List<PlanLocalProgramaProyecto> showPlanaLocalNuevo(BigInteger b) {
        List<PlanLocalProgramaProyecto> result = (List<PlanLocalProgramaProyecto>) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanLocalProgramaProyecto getPlanlocal(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanLocalProgramaProyecto result = (PlanLocalProgramaProyecto) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PlanAnualPoliticaPublica> showPlanaPoliticalNuevo(BigInteger b) {
        List<PlanAnualPoliticaPublica> result = (List<PlanAnualPoliticaPublica>) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanAnualPoliticaPublica getPlanPolitica(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanAnualPoliticaPublica result = (PlanAnualPoliticaPublica) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PlanAnualProgramaProyecto> showPlanAnualNuevo(BigInteger b) {
        List<PlanAnualProgramaProyecto> result = (List<PlanAnualProgramaProyecto>) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PlanAnualProgramaProyecto getPlanAnual(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PlanAnualProgramaProyecto result = (PlanAnualProgramaProyecto) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ActividadOperativa> showActividadOperativaNuevo(BigInteger b) {
        List<ActividadOperativa> result = (List<ActividadOperativa>) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.codigoReforma= :reforma")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ActividadOperativa getActividadOperativa(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        ActividadOperativa result = (ActividadOperativa) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<Producto> showProductoNuevo(BigInteger b) {
        //AND p.codigoPresupuestario IS NOT NULL
        List<Producto> result = (List<Producto>) em.createQuery("SELECT p FROM Producto p WHERE p.codigoReforma= :reforma ")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public Producto getProductoNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        Producto result = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ThCargoRubros> showPartidasDistributivoNnuevo(BigInteger b) {
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.codigoReforma= :reforma AND p.estadoPartida IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ThCargoRubros getPartidasDistributivoNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = b.longValue();

        ThCargoRubros result = (ThCargoRubros) em.createQuery("SELECT p FROM ThCargoRubros p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<PartidasDistributivoAnexo> showPartidasDistributivoAnexoNnuevo(BigInteger b) {
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :reforma AND p.partidaAp IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public PartidasDistributivoAnexo getPartidasDistributivoAnexoNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        PartidasDistributivoAnexo result = (PartidasDistributivoAnexo) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ProformaPresupuestoPlanificado> showPartidasDirectasNnuevo(BigInteger b) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NOT NULL")
                .setParameter("reforma", b).getResultList();
        return result;
    }

    public ProformaPresupuestoPlanificado getPartidasDirectasNuevo(BigInteger b) {
        String refe = b.toString();
        Long referencia = Long.valueOf(refe);

        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.id= :referencia ")
                .setParameter("referencia", referencia).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ProformaPresupuestoPlanificado> showPartidaDirectas(short periodo) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND p.codigoReformaTraspaso IS NULL AND p.codigoReforma IS NULL AND p.codigo='PDI' AND p.partidaPresupuestaria IS NOT NULL")
                .setParameter("periodo", periodo).getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> showPartidaDirectasReforma(short periodo, ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND  p.codigoReforma= :reforma AND p.codigo='PDI' AND p.partidaPresupuestaria IS NOT NULL")
                .setParameter("periodo", periodo).setParameter("reforma", reforma).getResultList();
        return result;
    }

    //consultas para Reforma Proforma
    public List<ProformaPDTO> gePappGroup(short periodo) {

        Query query = em.createQuery("SELECT p.codigoPresupuestario AS partida,COALESCE(SUM(p.monto),0) AS total,SUM(p.suplementoEgreso) as suplemento,SUM(p.reduccionEgreso) as reduccion, "
                + "SUM(p.traspasoIncremento) as incrementoT, SUM(p.traspasoReduccion) reduccionT,"
                + "p.estructuraProgramatica AS estructuraProgramatica ,p.itemPresupuestario AS itemPresupuestario, "
                + "p.fuenteDirecta AS fuente FROM Producto p "
                + "WHERE p.periodo= :periodo and p.codigoReforma IS NULL and p.codigoPresupuestario is not null and p.codigoReformaTraspaso IS NULL GROUP BY p.codigoPresupuestario,p.estructuraProgramatica,p.itemPresupuestario,p.fuenteDirecta")
                .setParameter("periodo", periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);
                ppdto.setReduccionTraspaso((BigDecimal) data[5]);
                ppdto.setEstructuraProgramatica((PlanProgramatico) data[6]);
                ppdto.setItemPresupuestario((CatalogoPresupuesto) data[7]);
                ppdto.setFuenteDirecta((CatalogoItem) data[8]);
                list.add(ppdto);
            }
            return list;
        }

        return null;

    }

    public List<ProformaPDTO> gePappGroupReforma(short periodo, ReformaIngresoSuplemento r) {

        BigInteger reforma = BigInteger.valueOf(r.getId());
        Query query = em.createNativeQuery("select p.codigo_presupuestario as partida, COALESCE(SUM(p.monto),0) as total,\n"
                + "SUM(COALESCE(p.suplemento_egreso,0)) as suplemento,\n"
                + "SUM(COALESCE(p.reduccion_egreso,0)) as reduccion, \n"
                + "                        SUM(p.traspaso_incremento) as incrementoT, \n"
                + "						SUM(p.traspaso_reduccion) as reduccionT,\n"
                + "              p.estructura_new AS estructuraProgramatica ,\n"
                + "			  p.item_new AS itemPresupuestario, \n"
                + "                   p.fuente_new AS fuente\n"
                + "           from  producto p \n"
                + "             where p.periodo=?1 and p.codigo_reforma=?2\n"
                + "            and p.codigo_presupuestario is not null\n"
                + "             \n"
                + "              group by  p.codigo_presupuestario,p.estructura_new,p.item_new,p.fuente_new")
                .setParameter(1, periodo).setParameter(2, reforma);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                PresPlanProgramatico p = new PresPlanProgramatico();
                PresCatalogoPresupuestario ca = new PresCatalogoPresupuestario();

                PresFuenteFinanciamiento fu = new PresFuenteFinanciamiento();

                BigInteger a = (BigInteger) data[6], b = (BigInteger) data[7], c = (BigInteger) data[8];

                p = service.find(PresPlanProgramatico.class, a.longValue());
                ca = service.find(PresCatalogoPresupuestario.class, b.longValue());
                fu = service.find(PresFuenteFinanciamiento.class, c.longValue());

                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setReformaCodificado(ppdto.getTotal().add(ppdto.getReformaSuplemento()).subtract(ppdto.getReformaReduccion()));
                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);
                ppdto.setReduccionTraspaso((BigDecimal) data[5]);
                ppdto.setEstructuraNew(p);
                ppdto.setItemNew(ca);
                ppdto.setFuenteNew(fu);
                list.add(ppdto);
            }
            return list;
        }

        return null;

    }

    public PlanProgramatico devulveObjeto(Short periodo, BigInteger id) {
        PlanProgramatico plan = (PlanProgramatico) em.createQuery("SELECT p FROM PlanProgramatico p WHERE p.id=:id AND p.periodo=:periodo")
                .setParameter("id", id.longValue())
                .setParameter("periodo", periodo)
                .getResultStream().findFirst().orElse(null);
        return plan;
    }

    public CatalogoPresupuesto devulveObjetoItem(BigInteger id) {
        CatalogoPresupuesto item = (CatalogoPresupuesto) em.createQuery("SELECT p FROM CatalogoPresupuesto p WHERE p.id=:id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return item;
    }

    public FuenteFinanciamiento devulveObjetoFuente(Short periodo, BigInteger id) {
        FuenteFinanciamiento fuente = (FuenteFinanciamiento) em.createQuery("SELECT ae FROM FuenteFinanciamiento ae WHERE ae.id= :id AND ae.periodo= :periodo")
                .setParameter("id", id.longValue())
                .setParameter("periodo", periodo)
                .getSingleResult();
        return fuente;
    }

    public CatalogoItem devulveObjetoFuenteDirecta(BigInteger id) {
        CatalogoItem fuenteDirecta = (CatalogoItem) em.createQuery("SELECT ae FROM CatalogoItem ae WHERE ae.id= :id")
                .setParameter("id", id.longValue())
                .getSingleResult();
        return fuenteDirecta;
    }

    public List<ProformaPDTO> showCodigosAgrupados(Short periodo) {

        Query nativeQuery = em.createNativeQuery("select  pa.partida_presupuestaria,COALESCE(sum(pa.monto),0) as total,\n"
                + "sum(COALESCE(pa.reforma_suplemento,0)) as suplemento, \n"
                + "sum(COALESCE(pa.reforma_reduccion,0)) as reduccion,\n"
                + "  sum(COALESCE(pa.traspaso_incremento,0)) as incrementoT, \n"
                + "  sum(COALESCE(pa.traspaso_reduccion,0)) as reduccionT,\n"
                + "  pa.id_estructura,pa.id_presupuesto,pa.id_fuente\n"
                + "    from talento_humano.th_cargo_rubros pa \n"
                + "          where pa.periodo=?1 AND pa.partida_presupuestaria is not null  AND pa.codigo_reforma is null\n"
                + "		  and pa.codigo_reforma_traspaso is null\n"
                + "           GROUP BY pa.partida_presupuestaria,pa.id_estructura,pa.id_presupuesto,pa.id_fuente");
        nativeQuery.setParameter(1, periodo);
        List<Object[]> result = nativeQuery.getResultList();

        if (Utils.isNotEmpty(result)) {

            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {

                PresPlanProgramatico p = new PresPlanProgramatico();
                PresCatalogoPresupuestario ca = new PresCatalogoPresupuestario();

                PresFuenteFinanciamiento fu = new PresFuenteFinanciamiento();
                ProformaPDTO ppdto = new ProformaPDTO();
                p = service.find(PresPlanProgramatico.class, (BigInteger) data[6]);
                ca = service.find(PresCatalogoPresupuestario.class, (BigInteger) data[7]);
                fu = service.find(PresFuenteFinanciamiento.class, (BigInteger) data[8]);

                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);

                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);

                ppdto.setReduccionTraspaso((BigDecimal) data[5]);

                ppdto.setEstructuraNew(p);

                ppdto.setItemNew(ca);
                ppdto.setFuenteNew(fu);
//                ppdto.setFuente(fu);
                list.add(ppdto);

            }
            return list;
        }
        return null;
    }

    public List<ProformaPDTO> showCodigosAgrupadosReformas(Short periodo, ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        Query nativeQuery = em.createNativeQuery("select  pa.partida_presupuestaria,COALESCE(SUM(pa.monto),0) as total,\n"
                + "                sum(COALESCE(pa.reforma_suplemento,0)) as suplemento, \n"
                + "                sum(COALESCE(pa.reforma_reduccion,0)) as reduccion,\n"
                + "                 sum(COALESCE(pa.traspaso_incremento,0)) as incrementoT, \n"
                + "                 sum(COALESCE(pa.traspaso_reduccion,0)) as reduccionT,\n"
                + "                 pa.id_estructura,pa.id_presupuesto,pa.id_fuente, \n"
                + "				 case r.origen when true then 'PD' else 'PDA' end as tipo\n"
                + "                 from talento_humano.th_cargo_rubros pa inner join\n"
                + "				 talento_humano.th_rubro r ON r.id = pa.id_rubro\n"
                + "                  where pa.periodo=?1 AND pa.partida_presupuestaria is not null  AND pa.codigo_reforma=?2\n"
                + "                 GROUP BY pa.partida_presupuestaria,pa.id_estructura,pa.id_presupuesto,pa.id_fuente,tipo");
        nativeQuery.setParameter(1, periodo).setParameter(2, reforma);
        List<Object[]> result = nativeQuery.getResultList();

        if (Utils.isNotEmpty(result)) {

            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {

                PresPlanProgramatico p = new PresPlanProgramatico();
                PresCatalogoPresupuestario ca = new PresCatalogoPresupuestario();

                PresFuenteFinanciamiento fu = new PresFuenteFinanciamiento();
                ProformaPDTO ppdto = new ProformaPDTO();

                BigInteger a = (BigInteger) data[6], b = (BigInteger) data[7], c = (BigInteger) data[8];

                p = service.find(PresPlanProgramatico.class, a.longValue());
                ca = service.find(PresCatalogoPresupuestario.class, b.longValue());
                fu = service.find(PresFuenteFinanciamiento.class, c.longValue());

                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setIncrementoTraspaso((BigDecimal) data[4]);
                ppdto.setReduccionTraspaso((BigDecimal) data[5]);
                ppdto.setEstructuraNew(p);
                ppdto.setItemNew(ca);
                ppdto.setFuenteNew(fu);
                ppdto.setCodigo((String) data[6]);
//                ppdto.setFuente(fu);
                list.add(ppdto);

            }
            return list;
        }
        return null;
    }

    public List<ProformaPresupuestoPlanificado> proformaVieja(short periodo) {
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p WHERE p.periodo=:periodo AND p.codigoReforma  IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo).getResultList();
        return result;

    }

    public ProformaPresupuestoPlanificado editProformavieja(ProformaPresupuestoPlanificado p) {
        ProformaPresupuestoPlanificado result = (ProformaPresupuestoPlanificado) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p where p.id= :id")
                .setParameter("id", p.getId()).getResultStream().findFirst().orElse(null);
        return result;
    }

    public List<ThCargoRubros> partidasDis(ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        List<ThCargoRubros> result = (List<ThCargoRubros>) em.createQuery("SELECT p FROM ThCargoRubros p  WHERE p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NULL")
                .setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<PartidasDistributivoAnexo> partidasDisAnexo(ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        List<PartidasDistributivoAnexo> result = (List<PartidasDistributivoAnexo>) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p  WHERE p.codigoReforma= :reforma AND p.partidaAp IS NULL")
                .setParameter("reforma", reforma).getResultList();
        return result;
    }

    public List<ProformaPresupuestoPlanificado> partidasDirect(ReformaIngresoSuplemento r) {
        BigInteger reforma = BigInteger.valueOf(r.getId());
        List<ProformaPresupuestoPlanificado> result = (List<ProformaPresupuestoPlanificado>) em.createQuery("SELECT p FROM ProformaPresupuestoPlanificado p  WHERE p.codigoReforma= :reforma AND p.partidaPresupuestaria IS NULL")
                .setParameter("reforma", reforma).getResultList();
        return result;
    }

    public Presupuesto getReformaPresupuestoObject(Presupuesto p) {
        Presupuesto presupuesto = (Presupuesto) em.createQuery("SELECT p FROM Presupuesto p WHERE p.id= :id ").setParameter("id", p.getId()).getSingleResult();
        return presupuesto;
    }

    public PlanLocalProgramaProyecto getPlanLocalCreado(BigInteger nuevo) {
        PlanLocalProgramaProyecto p = (PlanLocalProgramaProyecto) em.createQuery("SELECT p FROM PlanLocalProgramaProyecto p where p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;
    }

    public PlanAnualPoliticaPublica getPlanPoliticaCreado(BigInteger nuevo) {
        PlanAnualPoliticaPublica p = (PlanAnualPoliticaPublica) em.createQuery("SELECT p FROM PlanAnualPoliticaPublica p WHERE p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;
    }

    public PlanAnualProgramaProyecto getPlanAnualCreado(BigInteger nuevo) {
        PlanAnualProgramaProyecto p = (PlanAnualProgramaProyecto) em.createQuery("SELECT p FROM PlanAnualProgramaProyecto p WHERE p.registroNuevoReferencia= :nuevo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);
        return p;

    }

    public ActividadOperativa getActividadCreado(BigInteger nuevo) {
        ActividadOperativa p = (ActividadOperativa) em.createQuery("SELECT a FROM ActividadOperativa a WHERE a.registroNuevoReferencia= :nuevo AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL")
                .setParameter("nuevo", nuevo).getResultStream().findFirst().orElse(null);

        return p;
    }

    public BigDecimal totalProformaIngresoEgreso(Short periodo, boolean tipo) {
        BigDecimal v = BigDecimal.ZERO;

        if (tipo) {
            v = (BigDecimal) em.createQuery("SELECT SUM(c.presupuestoCodificado) FROM CatalogoPresupuesto c INNER JOIN c.nivel n WHERE  c.flujoIngreso= true AND c.anio= :periodo and n.orden=4 ")
                    .setParameter("periodo", periodo).getSingleResult();

            if (v == null) {
                v = BigDecimal.ZERO;
            }

        } else {
            v = (BigDecimal) em.createQuery("SELECT SUM(p.reformaCodificado) from ProformaPresupuestoPlanificado p WHERE p.periodo= :periodo AND p.codigoReforma IS NULL and p.codigoReformaTraspaso IS NULL ")
                    .setParameter("periodo", periodo).getSingleResult();

            if (v == null) {
                v = BigDecimal.ZERO;
            }

        }

        return v;
    }

    public void actualizarTotalCatalgoProforma(BigDecimal ingreso, BigDecimal egreso, short periodo) {

        Query query = em.createQuery("UPDATE CatalogoProformaPresupuesto c SET c.total= :totalingreso WHERE c.tipoFlujo=true AND c.periodo= :periodo AND c.estado= true")
                .setParameter("totalingreso", ingreso)
                .setParameter("periodo", periodo);
        int result = query.executeUpdate();

        Query query2 = em.createQuery("UPDATE CatalogoProformaPresupuesto c SET c.total= :totalegreso WHERE c.tipoFlujo=false AND c.periodo= :periodo AND c.estado= true")
                .setParameter("totalegreso", ingreso)
                .setParameter("periodo", periodo);
        int result2 = query2.executeUpdate();
    }

    public ControlCuentaPresupuestario getVerificarMesAnio(Short mes, Short periodo) {
        ControlCuentaPresupuestario c = (ControlCuentaPresupuestario) em.createQuery("SELECT c FROM ControlCuentaPresupuestario c WHERE c.orden= :mes AND c.periodo= :periodo ")
                .setParameter("mes", mes).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return c;
    }

    public List<ReformaIngresoSuplemento> getSolictudesTramite(BigInteger num) {

        List<ReformaIngresoSuplemento> result = (List<ReformaIngresoSuplemento>) em.createQuery("SELECT r FROM ReformaIngresoSuplemento r WHERE r.numTramite= :num")
                .setParameter("num", num).getResultList();
        return result;
    }

    public Cupos cupoReformasUser(String user, ReformaIngresoSuplemento r) {

        Cupos c = (Cupos) em.createQuery("SELECT c FROM Cupos c INNER JOIN c.reforma r WHERE c.usuario= :user AND r.id= :id ")
                .setParameter("user", user).setParameter("id", r.getId()).getResultStream().findFirst().orElse(null);
        return c;
    }

    public ReformaIngresoSuplemento getUnidadReforma(BigInteger num) {

        ReformaIngresoSuplemento c = (ReformaIngresoSuplemento) em.createQuery("SELECT r FROM ReformaIngresoSuplemento r where r.numTramite= :num  ")
                .setParameter("num", num).getResultStream().findFirst().orElse(null);
        return c;
    }

    public BigDecimal getTotalSuplementoAsignado(ReformaIngresoSuplemento r) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(c.montoCupo),0)  FROM Cupos c INNER JOIN c.reforma r WHERE r.id= :id ").setParameter("id", r.getId()).getSingleResult();
        return result;
    }

}
