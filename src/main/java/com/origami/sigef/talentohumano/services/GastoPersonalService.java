/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.talentohumano.model.TotalGastosPersonales;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author OrigamiEC
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class GastoPersonalService extends AbstractService<GastoPersonal> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public GastoPersonalService() {
        super(GastoPersonal.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigDecimal getRMU(short anio, Cliente ci) {
        BigDecimal rmu = BigDecimal.ZERO;
        try {
            Query query = em.createQuery("SELECT de.remuneracionDolares FROM DistributivoEscala de INNER JOIN de.distributivo d WHERE de.distributivo = d AND de.anio = ?1 AND d.servidorPublico.persona.identificacion = ?2 AND d.estado = TRUE")
                    .setParameter(1, anio)
                    .setParameter(2, ci.getIdentificacion());
            rmu = (BigDecimal) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Distributivo sin renumeracio remuneracionDolares");
        }
        return rmu;
    }

    public Servidor servidorCC(long servi) {
        Servidor serv;
        try {
            if (servi > 0) {
                Query query = em.createQuery("SELECT d FROM Distributivo d JOIN d.servidorPublico s WHERE d.servidorPublico.id = ?1")
                        .setParameter(1, servi);
                Distributivo dis = (Distributivo) query.getSingleResult();
                return dis.getServidorPublico();
            }
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public long getServidorPublico(short anio, String ci) {
        long servidor;
        try {
            Query query = em.createNativeQuery("SELECT d.servidor_publico FROM talento_humano.servidor s "
                    + "INNER JOIN public.cliente c ON c.id = s.persona "
                    + "INNER JOIN talento_humano.distributivo d ON s.distributivo = d.id "
                    + "INNER JOIN talento_humano.distributivo_escala de ON de.distributivo = d.id "
                    + "WHERE s.estado = true AND de.anio = ?1 and d.rmu is not null AND c.identificacion = ?2")
                    .setParameter(1, anio)
                    .setParameter(2, ci);
            servidor = (Long) query.getSingleResult();
            return servidor;
        } catch (NoResultException e) {
            return 0;
        }
    }

    public List<GastoPersonal> listGastoPersonal(Short anio) {
        Query query = em.createQuery("SELECT g FROM GastoPersonal g where g.estado = TRUE AND g.ejercicioFiscal = ?1")
                .setParameter(1, anio);
        List<GastoPersonal> result = (List<GastoPersonal>) query.getResultList();
        return result;
    }

    public GastoPersonal existeRegistro(Servidor ser, Short anio) {
        try {
            Query query = em.createQuery("SELECT g FROM GastoPersonal g WHERE g.servidorPublico=?1 AND g.ejercicioFiscal=?2")
                    .setParameter(1, ser).setParameter(2, anio);
            GastoPersonal result = (GastoPersonal) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public TotalGastosPersonales getValoresTotales(Short periodo) {
        return (TotalGastosPersonales) em.createNativeQuery("select sum(ingreso_gravado) as ingreso_gravado , sum(otros_ingresos) as otros_ingresos,sum(total_ingreso) as total_ingreso\n"
                + ",sum(gasto_vestimenta) as gasto_vestimenta,sum(gasto_educacion) as gasto_educacion,sum(gasto_salud) as gasto_salud\n"
                + ",sum(gasto_turismo) as gasto_turismo,sum(gasto_alimentacion) as gasto_alimentacion,sum(gasto_vivienda) as gasto_vivienda,\n"
                + "sum(total_gasto) as total_gasto\n"
                + "from talento_humano.gasto_personal\n"
                + "where ejercicio_fiscal = ?1", "TotalGastosMapping")
                .setParameter(1, periodo)
                .getSingleResult();
    }

}
