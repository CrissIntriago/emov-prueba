package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
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
public class DistributivoEscalaService extends AbstractService<DistributivoEscala> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DistributivoEscalaService() {
        super(DistributivoEscala.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    //trae escala Salarial por un distributivo y un periodo
    public DistributivoEscala getEscalaDistributivoAnio(Distributivo d, OpcionBusqueda o) {
        try {
            if (d.getId() == null) {
                return null;
            }
            Query query = em.createQuery("SELECT DISTINCT d FROM DistributivoEscala d WHERE d.distributivo =?1 and d.estado = TRUE and d.anio = ?2 ")
                    .setParameter(1, d).setParameter(2, o.getAnio());
            DistributivoEscala result = (DistributivoEscala) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    //trae todas las escalas de un  Distributivo en Especifico

    public List<DistributivoEscala> getAllEscalaDistributivoAnio(Distributivo d) {
        try {
            Query query = em.createQuery("SELECT d FROM DistributivoEscala d WHERE d.distributivo =?1 and d.estado = TRUE")
                    .setParameter(1, d);
            List<DistributivoEscala> escala = (List<DistributivoEscala>) query.getResultList();
            return escala;
        } catch (NoResultException e) {
            return null;
        }
    }

    //trae escalas y distributivo donde su servidor es null por un año especifico
    public List<DistributivoEscala> getEscalaDistributivoServidorNull(OpcionBusqueda o) {
        try {
            Query query
                    = em.createNativeQuery("select de.* from talento_humano.distributivo_escala de\n"
                            + "inner join talento_humano.distributivo d ON d.id = de.distributivo\n"
                            + "INNER JOIN talento_humano.valores_distributivo vd ON vd.distributivo = d.id\n"
                            + "inner join talento_humano.cargo crg ON crg.id = d.cargo\n"
                            + "INNER join partidas_distributivo pd on pd.distributivo = vd.id\n"
                            + "WHERE de.anio= ?1 and de.estado = TRUE and d.estado = TRUE\n"
                            + "and d.servidor_publico IS NULL and pd.reforma_codificado > 0 AND pd.codigo_reforma is null\n"
                            + "AND pd.codigo_reforma_traspaso is null AND "
                            + "vd.anio=?1 AND pd.partida_ap IS  NOT NULL AND d.estado = true\n"
                            + "group by de.id,crg.id\n"
                            + "order by crg.nombre_cargo asc", DistributivoEscala.class)
                            .setParameter(1, o.getAnio());
            List<DistributivoEscala> escala = (List<DistributivoEscala>) query.getResultList();
            return escala;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Servidor getServidorAnio(Short anio, String ci) {
        try {
            Query query = em.createQuery("SELECT DISTINCT s FROM DistributivoEscala de JOIN de.distributivo d JOIN d.servidorPublico s WHERE de.distributivo = d AND d.servidorPublico = s AND de.anio = ?1 AND s.estado = TRUE AND s.persona.identificacion = ?2 AND de.estado = TRUE")
                    .setParameter(1, anio).setParameter(2, ci);
            Servidor result = (Servidor) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public DistributivoEscala getRMU(Distributivo d, short anio) {
        try {
            Query query = em.createQuery("SELECT de FROM DistributivoEscala de JOIN de.distributivo d WHERE de.distributivo = ?1 AND de.anio = ?2")
                    .setParameter(1, d).setParameter(2, anio);
            DistributivoEscala rmu = (DistributivoEscala) query.getSingleResult();
            return rmu;
        } catch (Exception e) {
            return null;
        }
    }

    public DistributivoEscala getRMUProceso(Distributivo d, short anio) {
        try {
            Query query = em.createQuery("SELECT de FROM DistributivoEscala de JOIN de.distributivo d JOIN d.servidorPublico s "
                    + "WHERE s.distributivo = d AND de.distributivo = ?1 AND de.anio = ?2")
                    .setParameter(1, d).setParameter(2, anio);
            DistributivoEscala rmu = (DistributivoEscala) query.getSingleResult();

            return rmu;
        } catch (NoResultException e) {
            return null;
        }
    }
}
