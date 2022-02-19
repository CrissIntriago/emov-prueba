/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Pozo G
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class TipoRolService extends AbstractService<TipoRol> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public TipoRolService() {
        super(TipoRol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //se lo usara para identificar, añadir tipo
    public List<TipoRol> listaRoles(short anio, CatalogoItem tipoRol) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.anio = ?1 AND t.tipoRol = ?2")
                .setParameter(1, anio).setParameter(2, tipoRol);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRoles(CatalogoItem tipoRol) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.tipoRol.codigo IN ('rol_general','rol_adicional','ROL-HORAS-EXTRAS')")
                .setParameter(1, tipoRol);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    //se lo usara para identificar roles x año
    public List<TipoRol> listaRolesXanio(short anio) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.anio = ?1 AND t.tipoRol.codigo IN ('rol_general','rol_adicional')")
                .setParameter(1, anio);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRolesFondoRe(short anio) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.anio = ?1 AND t.tipoRol.codigo IN ('ROL_FONDO_RESERVA')")
                .setParameter(1, anio);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRolesXanio() {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.tipoRol.codigo IN ('rol_general','rol_adicional')");
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRolesByAprobado(short anio, CatalogoItem tipoRol, CatalogoItem estadoAprobado) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.anio = ?1 AND t.tipoRol = ?2 AND t.estadoAprobacion = ?3")
                .setParameter(1, anio).setParameter(2, tipoRol)
                .setParameter(3, estadoAprobado);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRolesProcess(short anio, CatalogoItem tipoRol, CatalogoItem estadoAprobado, BigInteger num) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.estado = TRUE AND t.anio = ?1 AND t.tipoRol = ?2 AND (t.estadoAprobacion = ?3 OR t.numTramite = ?4)")
                .setParameter(1, anio).setParameter(2, tipoRol)
                .setParameter(3, estadoAprobado).setParameter(4, num);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public List<TipoRol> listaRolesAdicionalAndGenerelByEstadoRol(short anio, CatalogoItem estado) {
        System.out.println("CODIGO: " + estado);
        Query query = em.createQuery("SELECT t FROM TipoRol t JOIN t.tipoRol tp WHERE t.estado = TRUE AND t.anio = ?1 AND t.estadoAprobacion = ?2 and tp.codigo in ('rol_general','rol_adicional','ROL_FONDO_RESERVA')")
                .setParameter(1, anio)
                .setParameter(2, estado);
        List<TipoRol> result = (List<TipoRol>) query.getResultList();
        return result;
    }

    public TipoRol rolEncabezado(short anio, String mes, CatalogoItem tipo) {
        Query query = em.createQuery("SELECT t FROM TipoRol t JOIN t.mes m WHERE t.estado = TRUE AND t.anio = ?1 AND m.texto = ?2 AND t.tipoRol = ?3")
                .setParameter(1, anio)
                .setParameter(2, mes)
                .setParameter(3, tipo);
        TipoRol result = (TipoRol) query.getSingleResult();
        return result;
    }

    public TipoRol tipoRolParam(Long id) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.id = ?1")
                .setParameter(1, id);
        TipoRol result = (TipoRol) query.getSingleResult();
        return result;
    }

    public List<TipoRol> getTipoRolAprobado(String cod, Short anio) {
        Query query = em.createQuery("SELECT r FROM TipoRol r WHERE r.estado = true AND r.estadoAprobacion.codigo = ?1 AND r.anio = ?2")
                .setParameter(1, cod).setParameter(2, anio);
        List<TipoRol> lista = (List<TipoRol>) query.getResultList();
        return lista;
    }

    public List<TipoRol> getTipoRolAprobado(String cod, String cod2, Short anio) {
        Query query = em.createQuery("SELECT r FROM TipoRol r WHERE r.estado = true AND (r.estadoAprobacion.codigo = ?1 or r.estadoAprobacion.codigo = ?2) AND r.anio = ?3")
                .setParameter(1, cod).setParameter(2, cod2).setParameter(3, anio);
        List<TipoRol> lista = (List<TipoRol>) query.getResultList();
        return lista;
    }

    public List<TipoRol> getTipoRolAprobado(String cod, String cod2) {
        Query query = em.createQuery("SELECT r FROM TipoRol r WHERE r.estado = true AND (r.estadoAprobacion.codigo = ?1 or r.estadoAprobacion.codigo = ?2)AND r.tipoRol.codigo IN ('rol_general','rol_adicional')")
                .setParameter(1, cod).setParameter(2, cod2);
        List<TipoRol> lista = (List<TipoRol>) query.getResultList();
        return lista;
    }

    public List<TipoRol> getTipoRolesAprobados(short periodo, String codigo, String codigo_2) {
        List<TipoRol> resultado = (List<TipoRol>) em.createQuery("SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es "
                + "INNER JOIN tr.tipoRol ci WHERE es.codigo=:codigo AND tr.estado=TRUE AND tr.anio=:periodo "
                + "AND tr.diarioRolGeneral=FALSE AND ci.codigo=:codigo2 ORDER BY tr.id ASC")
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo)
                .setParameter("codigo2", codigo_2)
                .getResultList();
        return resultado;
    }

    public List<RolRubro> getRolRubros(TipoRol tipoRol) {
        List<RolRubro> resultado = (List<RolRubro>) em.createQuery("SELECT rr FROM RolRubro rr "
                + "INNER JOIN rr.valorAsignacion va "
                + "INNER JOIN rr.liquidacionRol lr "
                + "INNER JOIN lr.tipoRol tr "
                + "WHERE tr.id=:tipoRol AND rr.estado= TRUE AND va.estado= TRUE AND lr.estado= TRUE "
                + "ORDER BY va.partidaAp ASC")
                .setParameter("tipoRol", tipoRol.getId())
                .getResultList();
        return resultado;
    }

    public List<TipoRol> getTipoRolesFondosReserva(short periodo, String codigo, int cod) {
        String sql = "";
        switch (cod) {
            case 1:
                sql = "SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es "
                        + "WHERE es.codigo<>:codigo AND tr.estado=TRUE AND tr.anio=:periodo AND tr.diarioFondosReserva=FALSE ORDER BY tr.id ASC";
                break;
            case 2:
                sql = "SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es "
                        + "WHERE es.codigo<>:codigo AND tr.estado=TRUE AND tr.anio=:periodo AND tr.estadoDecimoTercero=FALSE ORDER BY tr.id ASC";
                break;
            case 3:
                sql = "SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es "
                        + "WHERE es.codigo<>:codigo AND tr.estado=TRUE AND tr.anio=:periodo AND tr.estadoDecimoCuarto=FALSE ORDER BY tr.id ASC";
                break;
            case 4:
                sql = "SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es INNER JOIN tr.tipoRol tp WHERE (es.codigo=:codigo OR  es.codigo='pagado-rol') "
                        + "AND tr.estado=TRUE AND tr.anio=:periodo AND tr.diarioFondosReserva=FALSE AND tp.codigo='ROL_FONDO_RESERVA' ORDER BY tr.id ASC";
                break;
            case 5:
                sql = "SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es INNER JOIN tr.tipoRol tp WHERE (es.codigo=:codigo OR  es.codigo='pagado-rol') "
                        + "AND tr.estado=TRUE AND tr.anio=:periodo AND tr.diarioRolGeneral=FALSE AND tp.codigo='ROL_FONDO_RESERVA' ORDER BY tr.id ASC";
                break;
        }
        List<TipoRol> resultado = (List<TipoRol>) em.createQuery(sql)
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public List<TipoRol> getTipoRolesFondosReserva(short periodo, String codigo) {
        List<TipoRol> resultado = (List<TipoRol>) em.createQuery("SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es "
                + "WHERE es.codigo=:codigo AND tr.estado=TRUE AND tr.anio=:periodo AND tr.diarioFondosReserva=FALSE ORDER BY tr.id ASC")
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }

    public int getRestablecerValoresRoles(DiarioGeneral diarioGeneral, CatalogoItem estadoAprobacion) {
        Query query = getEntityManager().createNativeQuery("UPDATE talento_humano.tipo_rol SET estado_aprobacion = ?1, diario_rol_general= false WHERE id_dg_rol_general = ?2")
                .setParameter(1, estadoAprobacion)
                .setParameter(2, diarioGeneral);
        return query.executeUpdate();
    }

    public int getRestablecerValoresFondos(DiarioGeneral diarioGeneral, CatalogoItem estadoAprobacion) {
        Query query = getEntityManager().createNativeQuery("UPDATE talento_humano.tipo_rol SET estado_aprobacion = ?1, diario_fondos_reserva = false WHERE id_dg_fondos_reserva = ?2")
                .setParameter(1, estadoAprobacion)
                .setParameter(2, diarioGeneral);
        return query.executeUpdate();
    }

    public TipoRol getRolRechazado(Long numTramite, Short periodo) {
        Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.numTramite = ?1 AND t.anio=?2")
                .setParameter(1, new BigInteger(numTramite.toString()))
                .setParameter(2, periodo);
        TipoRol result = (TipoRol) query.getSingleResult();
        return result;
    }

    public TipoRol getFindRol(Long numTramite, Short periodo) {
        try {
            Query query = em.createQuery("SELECT t FROM TipoRol t WHERE t.numTramite = ?1 AND t.anio=?2 AND t.estado=TRUE")
                    .setParameter(1, new BigInteger(numTramite.toString()))
                    .setParameter(2, periodo);
            TipoRol result = (TipoRol) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<TipoRol> getRolMes(String mes, String codigo) {
        List<TipoRol> resultado = (List<TipoRol>) em.createQuery("SELECT tr FROM TipoRol tr INNER JOIN tr.estadoAprobacion es INNER JOIN tr.mes m INNER JOIN tr.tipoRol tp WHERE tr.anio =:periodo AND tr.estado = true AND m.codigo =:mes AND es.codigo =:codigo AND (tp.codigo=:codigo_1 OR tp.codigo=:codigo_2)")
                .setParameter("mes", mes)
                .setParameter("periodo", Utils.getAnio(new Date()).shortValue())
                .setParameter("codigo", codigo)
                .setParameter("codigo_1", "rol_general")
                .setParameter("codigo_2", "rol_adicional")
                .getResultList();
        return resultado;
    }
}
