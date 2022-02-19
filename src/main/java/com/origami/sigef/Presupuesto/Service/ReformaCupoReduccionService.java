/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Service;

import com.origami.sigef.Presupuesto.Entity.CupoReduccion;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Suarez
 */
@Singleton
public class ReformaCupoReduccionService extends AbstractService<CupoReduccion> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ReformaCupoReduccionService() {
        super(CupoReduccion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CupoReduccion> getListaVerificacion(ReformaIngresoSuplemento r) {
        List<CupoReduccion> result = (List<CupoReduccion>) em.createQuery("SELECT c FROM CupoReduccion c INNER JOIN c.reforma r WHERE r.id=:reforma AND c.unidadAdministrativa IS NOT NULL")
                .setParameter("reforma", r.getId()).getResultList();
        return result;
    }

    public BigDecimal getMontoDisponibleUnidad(BigInteger num, UnidadAdministrativa u, Short periodo) {
        BigDecimal result_uno = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u WHERE p.codigoReforma=:refuno and  a.codigoReforma=:refdos AND u.id=:unidad")
                .setParameter("refuno", num).setParameter("refdos", num).setParameter("unidad", u.getId()).getSingleResult();
        BigDecimal result_dos = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso  d "
                + "INNER JOIN d.solicitud s INNER JOIN s.estado e INNER JOIN d.actividadProducto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u "
                + " WHERE u.id= :id AND e.codigo in ('APRO','LIQUI') and s.periodo=:periodo")
                .setParameter("id", u.getId()).setParameter("periodo", periodo).getSingleResult();

        BigDecimal resultado = result_uno.subtract(result_dos);
        return resultado;

    }

    public BigDecimal getmontocodificado(BigInteger num, UnidadAdministrativa u) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM Producto p INNER JOIN p.actividadOperativa a INNER JOIN a.unidadResponsable u WHERE p.codigoReforma= :num AND u.id= :id")
                .setParameter("num", num).setParameter("id", u.getId()).getSingleResult();

        return result;
    }

    public BigDecimal getMontoDisponibleDistributivo(BigInteger num, Short periodo) {
        BigDecimal result_uno = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM PartidasDistributivo p WHERE p.codigoReforma=:num ")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        BigDecimal result_dos = (BigDecimal) em.createNativeQuery("select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d, \n"
                + "certificacion_presupuestaria_anual.solicitud_reserva_compromiso s,presupuesto p,\n"
                + "proforma_presupuesto_planificado pr, catalogo_item ci where\n"
                + "d.presupuesto=p.id and pr.partida_presupuestaria=p.partida \n"
                + "and pr.codigo='PD' and  d.solicitud=s.id and s.estado=ci.id and ci.codigo in ('APRO','LIQUI') and s.periodo=?1 ")
                .setParameter(1, periodo).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result_uno == null) {
            result_uno = BigDecimal.ZERO;
        }

        if (result_dos == null) {
            result_dos = BigDecimal.ZERO;
        }

        BigDecimal resultado = result_uno.subtract(result_dos);
        return resultado;

    }

    public BigDecimal getmontocodificadoDistributivo(BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0) FROM PartidasDistributivo p WHERE p.codigoReforma= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result == null) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public BigDecimal getMontoDisponibleDistributivoAnexo(BigInteger num, Short periodo) {
        BigDecimal result_uno = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0)  FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        BigDecimal result_dos = (BigDecimal) em.createNativeQuery("select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d, \n"
                + "certificacion_presupuestaria_anual.solicitud_reserva_compromiso s,presupuesto p,\n"
                + "proforma_presupuesto_planificado pr, catalogo_item ci where\n"
                + "d.presupuesto=p.id and pr.partida_presupuestaria=p.partida \n"
                + "and pr.codigo='PDA' and  d.solicitud=s.id and s.estado=ci.id and ci.codigo in ('APRO','LIQUI') and s.periodo=?1")
                .setParameter(1, periodo).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result_uno == null) {
            result_uno = BigDecimal.ZERO;
        }

        if (result_dos == null) {
            result_dos = BigDecimal.ZERO;
        }

        BigDecimal resultado = result_uno.subtract(result_dos);
        return resultado;

    }

    public BigDecimal getmontocodificadoDistributivoAnexo(BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.monto),0)  FROM PartidasDistributivoAnexo p WHERE p.codigoReforma= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result == null) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public BigDecimal getMontoDisponibleDirectas(BigInteger num, Short periodo) {
        BigDecimal result_uno = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.valor),0)  FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        BigDecimal result_dos = (BigDecimal) em.createNativeQuery("select COALESCE(sum(d.monto_solicitado),0) from certificacion_presupuestaria_anual.detalle_solicitud_compromiso d, \n"
                + "certificacion_presupuestaria_anual.solicitud_reserva_compromiso s,presupuesto p,\n"
                + "proforma_presupuesto_planificado pr, catalogo_item ci where\n"
                + "d.presupuesto=p.id and pr.partida_presupuestaria=p.partida \n"
                + "and pr.codigo='PDI' and  d.solicitud=s.id and s.estado=ci.id and ci.codigo in ('APRO','LIQUI') and s.periodo=?1 ")
                .setParameter(1, periodo).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result_uno == null) {
            result_uno = BigDecimal.ZERO;
        }

        if (result_dos == null) {
            result_dos = BigDecimal.ZERO;
        }

        BigDecimal resultado = result_uno.subtract(result_dos);
        return resultado;

    }

    public BigDecimal getmontocodificadoDirectas(BigInteger num) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.valor),0)  FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :num")
                .setParameter("num", num).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        if (result == null) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public List<String> getListaRoles(UnidadAdministrativa u) {
        List<String> result = (List<String>) em.createNativeQuery("select ci.texto from auth.rol r inner join unidad_administrativa u on r.unidad_administrativa=u.id\n"
                + "	inner join catalogo_item ci on r.categoria=ci.id where u.id=?1").setParameter(1, u.getId()).getResultList();
        return result;
    }

    public List<CupoReduccion> getCuposDetalle(ReformaIngresoSuplemento r) {
        List<CupoReduccion> result = (List<CupoReduccion>) em.createQuery("SELECT c FROM CupoReduccion c INNER JOIN c.reforma r WHERE r.id= :id AND c.unidadAdministrativa is not null")
                .setParameter("id", r.getId()).getResultList();
        return result;
    }

    public String getRolesUsuariosNameUser(UnidadAdministrativa data, String data2) {
        String result = (String) em.createNativeQuery("select u.usuario from auth.usuarios u inner join auth.usuario_rol ur \n"
                + "                ON ur.usuario = u.id INNER join auth.rol r ON r.id = ur.rol\n"
                + "               	inner join catalogo_item ci on r.categoria=ci.id inner join \n"
                + "				unidad_administrativa unidad on r.unidad_administrativa=unidad.id\n"
                + "				where unidad.id=?1 and ci.texto=?2").setParameter(1, data.getId()).setParameter(2, data2).getResultStream().findFirst().orElse(null);

        return result;
    }

    public CupoReduccion getShowValores(String data, ReformaIngresoSuplemento r) {

        CupoReduccion num = (CupoReduccion) em.createQuery("SELECT c FROM CupoReduccion c WHERE c.usuario= :user AND c.reforma= :reforma")
                .setParameter("user", data).setParameter("reforma", r).getSingleResult();
        return num;
    }

    public BigDecimal getValorTotalReduccion(BigInteger reforma, UnidadAdministrativa a) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reduccionEgreso),0) FROM Producto p INNER JOIN p.actividadOperativa a WHERE  p.codigoReforma= :reforma AND a.unidadResponsable= :unidad AND a.codigoReforma=:codigoreformados")
                .setParameter("reforma", reforma).setParameter("unidad", a).setParameter("codigoreformados", reforma).getSingleResult();
        return result;
    }

    public CupoReduccion getValoresCupo(String data, ReformaIngresoSuplemento r) {
        CupoReduccion result = (CupoReduccion) em.createQuery("SELECT c FROM CupoReduccion c WHERE c.otros= :codigo AND c.reforma= :id")
                .setParameter("codigo", data).setParameter("id", r).getSingleResult();
        return result;
    }

    public BigDecimal retornaValorReducido(BigInteger num, String data) {
        BigDecimal result = BigDecimal.ZERO;

        switch (data) {
            case "PD":
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0)  FROM ThCargoRubros p WHERE p.codigoReforma= :id AND p.idRubro.origen=TRUE")
                        .setParameter("id", num).getSingleResult();
                break;
            case "PDA":
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ThCargoRubros p WHERE p.codigoReforma= :id AND p.idRubro.origen=FALSE")
                        .setParameter("id", num).getSingleResult();
                break;
            default:
                result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(p.reformaReduccion),0) FROM ProformaPresupuestoPlanificado p WHERE p.codigoReforma= :id")
                        .setParameter("id", num).getSingleResult();
                break;
        }

        return result;

    }

    public ReformaIngresoSuplemento getReforma(BigInteger r) {
        ReformaIngresoSuplemento result = (ReformaIngresoSuplemento) em.createQuery("SELECT r FROM ReformaIngresoSuplemento r WHERE r.numTramite= :num AND r.tipo=false")
                .setParameter("num", r).getSingleResult();
        return result;

    }

    public List<UnidadAdministrativa> getCuposDetalleUnidades(ReformaIngresoSuplemento r) {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT c.unidadAdministrativa FROM CupoReduccion c INNER JOIN c.reforma r WHERE r.id= :id AND c.unidadAdministrativa IS NOT NULL AND c.responsable IS NOT NULL AND c.usuario IS NOT NULL ").setParameter("id", r.getId()).getResultList();
        return result;
    }

    public List<CupoReduccion> getCuposDetalleActual(ReformaIngresoSuplemento r) {
        List<CupoReduccion> result = (List<CupoReduccion>) em.createQuery("SELECT c FROM CupoReduccion c INNER JOIN c.reforma r WHERE r.id= :id ")
                .setParameter("id", r.getId()).getResultList();
        return result;
    }

    public CupoReduccion cupoReformasUser(String user, ReformaIngresoSuplemento r) {

        CupoReduccion c = (CupoReduccion) em.createQuery("SELECT c FROM CupoReduccion c INNER JOIN c.reforma r WHERE c.usuario= :user AND r.id= :id ")
                .setParameter("user", user).setParameter("id", r.getId()).getResultStream().findFirst().orElse(null);
        return c;
    }

    public List<CupoReduccion> getRevisionAsignacion(ReformaIngresoSuplemento r) {
        List<CupoReduccion> lista = em.createNativeQuery("select cupo.* from presupuesto.cupo_reduccion cupo\n"
                + "where cupo.reforma=?1 and cupo.usuario=\n"
                + "(select a.usuario from \n"
                + "(select c.usuario,count(c.*) as conteo from presupuesto.cupo_reduccion c\n"
                + "where c.responsable is not null and c.usuario is not null\n"
                + " and c.reforma= ?2\n"
                + "group by c.usuario having count(c.*)>1)  as a)", CupoReduccion.class).setParameter(1, r).setParameter(2, r).getResultList();

        return lista;
    }

    public CupoReduccion getDataUnidad(UnidadAdministrativa u, ReformaIngresoSuplemento r) {
        List<CupoReduccion> lista = em.createQuery("SELECT c FROM CupoReduccion c WHERE c.unidadAdministrativa= :unidad AND c.reforma=:reforma")
                .setParameter("unidad", u).setParameter("reforma", r).getResultList();
        if (!lista.isEmpty()) {
            return lista.get(0);
        }

        return null;
    }

}
