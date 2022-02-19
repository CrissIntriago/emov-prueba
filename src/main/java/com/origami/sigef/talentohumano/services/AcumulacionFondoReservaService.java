/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class AcumulacionFondoReservaService extends AbstractService<AcumulacionFondoReserva> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public AcumulacionFondoReservaService() {
        super(AcumulacionFondoReserva.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<AcumulacionFondoReserva> getListAcumulacionRubro(CatalogoItem tipo, Short anio) {
        int anterior = anio.intValue() - 1;
        Short anioAt = (short) anterior;
        List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = true AND a.tipoAcumulacion = ?1 AND (a.periodo BETWEEN ?2 AND ?3)")
                .setParameter(1, tipo).setParameter(2, anioAt).setParameter(3, anio)
                .getResultList();
        return resul;
    }

    public List<AcumulacionFondoReserva> listaAcumulacionFondos(CatalogoItem tipo) {
        List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.acumula = FALSE AND a.tipoAcumulacion = ?1")
                .setParameter(1, tipo)
                .getResultList();
        return resul;
    }

    public List<AcumulacionFondoReserva> listaAcumulacionFondosEverybody(CatalogoItem tipo) {
        List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.tipoAcumulacion = ?1 AND a.estadoVigente = TRUE")
                .setParameter(1, tipo)
                .getResultList();
        return resul;
    }

    public List<AcumulacionFondoReserva> listaAcumulacionFondosReserv(boolean acumula, CatalogoItem tipo, Date ini, Date fin) {
        List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.acumula = ?1 AND a.tipoAcumulacion = ?2 AND a.fechaInicio BETWEEN ?3 AND ?4")
                .setParameter(1, acumula)
                .setParameter(2, tipo).setParameter(3, ini).setParameter(4, fin)
                .getResultList();
        return resul;
    }

    public List<AcumulacionFondoReserva> listaAcumulacionFondosReservEverybody(CatalogoItem tipo, Date ini, Date fin) {
        List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.tipoAcumulacion = ?1 AND a.fechaInicio BETWEEN ?2 AND ?3")
                .setParameter(1, tipo)
                .setParameter(2, ini)
                .setParameter(3, fin)
                .getResultList();
        return resul;
    }

    public List<AcumulacionFondoReserva> AcumulacionFondosReservInNotBeneficioTercer(boolean acumula, CatalogoItem tipo, Date ini, Date fin) {
        try {
            List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM  AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.acumula = ?1 AND a.tipoAcumulacion = ?2 AND a.fechaInicio BETWEEN ?3 AND ?4 AND a.id not in \n"
                    + " ( select ac.id from BeneficiosDecimoTercero b JOIN b.acumulacionFondoReserva ac where b.estado = true)")
                    .setParameter(1, acumula)
                    .setParameter(2, tipo).setParameter(3, ini).setParameter(4, fin)
                    .getResultList();
            return resul;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<AcumulacionFondoReserva> AcumulacionFondosReservInNotBeneficioCuarto(boolean acumula, CatalogoItem tipo, Date ini, Date fin) {
        try {
            List<AcumulacionFondoReserva> resul = (List<AcumulacionFondoReserva>) em.createQuery("SELECT a FROM  AcumulacionFondoReserva a WHERE a.estado = TRUE AND a.derecho = TRUE AND a.acumula = ?1 AND a.tipoAcumulacion = ?2 AND a.fechaInicio BETWEEN ?3 AND ?4 AND a.id not in \n"
                    + " ( select ac.id from BeneficiosDecimoCuarto b JOIN b.acumulacionFondos ac where b.estado = true)")
                    .setParameter(1, acumula)
                    .setParameter(2, tipo).setParameter(3, ini).setParameter(4, fin)
                    .getResultList();
            return resul;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<FondosReserva> getFondoReservaAcumulado(TipoRol tipoRol, String codigo, Short periodo) {
        try {
            List<FondosReserva> resultado = (List<FondosReserva>) em.createQuery("SELECT fr FROM FondosReserva fr "
                    + "INNER JOIN fr.acumulacionFondos af INNER JOIN af.tipoAcumulacion ta "
                    + "WHERE fr.estado=TRUE AND fr.tipoRol=:tipoRol AND af.estado=TRUE "
                    + "AND ta.codigo=:codigo AND af.periodo=:periodo AND af.acumula=TRUE")
                    .setParameter("tipoRol", tipoRol)
                    .setParameter("codigo", codigo)
                    .setParameter("periodo", periodo)
                    .getResultList();
            return resultado;
        } catch (NoResultException e) {
            return null;
        }
    }

    public int getcopia(Date fechaIniOld, Date fechaFinOld, Date fechaIniNew, Date fechaFinNew, CatalogoItem tipoAcu, String user) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("INSERT INTO \n"
                + "talento_humano.acumulacion_fondo_reserva\n"
                + "(estado, servidor, fecha_inicio, derecho, acumula, fecha_creacion, usuario_creacion, tipo_acumulacion, fecha_fin, valor_parametrizado, estado_vigente)\n"
                + "(select estado, servidor, ?1, derecho, acumula, ?2, ?3, tipo_acumulacion, ?4, valor_parametrizado, ?5 \n"
                + " from talento_humano.acumulacion_fondo_reserva ac where ac.estado = true and ac.tipo_acumulacion = ?6 and (ac.fecha_inicio>= ?7 and ac.fecha_fin <= ?8))")
                .setParameter(1, fechaIniNew).setParameter(2, new Date()).setParameter(3, user).setParameter(4, fechaFinNew).setParameter(5, Boolean.TRUE)
                .setParameter(6, tipoAcu).setParameter(7, fechaIniOld).setParameter(8, fechaFinOld);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public int updateAcumulacion(Date ini, Date fin, CatalogoItem tipo, Boolean estado, String user) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.acumulacion_fondo_reserva\n"
                + "	SET estado_vigente=?1, usuario_modifica = ?2, fecha_modificacion = ?3\n"
                + "	WHERE (fecha_inicio >= ?4 AND fecha_fin <= ?5) and tipo_acumulacion = ?6")
                .setParameter(1, estado).setParameter(2, user).setParameter(3, new Date()).setParameter(4, ini).setParameter(5, fin).setParameter(6, tipo);
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    public Date getFechaMax(CatalogoItem tipoAcu) {
        try {
            Query query = em.createQuery("SELECT MAX(acu.fechaFin) FROM AcumulacionFondoReserva acu WHERE acu.estado = TRUE AND acu.tipoAcumulacion = ?1")
                    .setParameter(1, tipoAcu);
            Date fecha = (Date) query.getSingleResult();
            return fecha;
        } catch (Exception e) {
            return new Date();
        }
    }

    public List<AcumulacionFondoReserva> getListAcumulacion(CatalogoItem tipo, Date fechaIni, Date FechaFin) {
        try {
            Query query = em.createQuery("SELECT acu FROM AcumulacionFondoReserva acu WHERE acu.estado = TRUE AND acu.tipoAcumulacion = ?1 AND (acu.fechaInicio >= ?2 and acu.fechaFin <= ?3)")
                    .setParameter(1, tipo).setParameter(2, fechaIni).setParameter(3, FechaFin);
            List<AcumulacionFondoReserva> result = (List<AcumulacionFondoReserva>) query.getResultList();
            return result;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public BigDecimal getValorTotal(TipoRol rol, CatalogoItem tipo, Boolean acumula) {
        try {
            Query query = em.createQuery("SELECT SUM(f.valorFondos) FROM FondosReserva f WHERE f.tipoRol=?1 AND f.estado=TRUE AND f.acumulacionFondos.tipoAcumulacion=?2 AND f.acumulacionFondos.acumula=?3")
                    .setParameter(1, rol).setParameter(2, tipo).setParameter(3, acumula);
            BigDecimal result = (BigDecimal) query.getSingleResult();
            return result;
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
    public BigDecimal getValorTotalTotal(TipoRol rol, CatalogoItem tipo) {
        try {
            Query query = em.createQuery("SELECT SUM(f.valorFondos) FROM FondosReserva f WHERE f.tipoRol=?1 AND f.estado=TRUE AND f.acumulacionFondos.tipoAcumulacion=?2")
                    .setParameter(1, rol).setParameter(2, tipo);
            BigDecimal result = (BigDecimal) query.getSingleResult();
            return result;
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

}
