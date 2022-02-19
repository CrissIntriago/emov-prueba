/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Service;

import com.gestionTributaria.Comisaria.Entities.Citaciones;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
@javax.enterprise.context.Dependent
public class CitacionesService extends AbstractService<Citaciones> {

    private static final Logger LOG = Logger.getLogger(ComisariaServices.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CitacionesService() {
        super(Citaciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Citaciones> listaEventos(Date start, Date end, String user) {

        List<BigInteger> ids = (List<BigInteger>) em.createNativeQuery("select c.id from comisaria.citaciones c where c.usuario_ingreso= ?1 and cast(c.fecha as DATE) between ?2 and ?3")
                .setParameter(1, user).setParameter(2, start).setParameter(3, end).getResultList();
        List<Citaciones> result = new ArrayList<>();
        if (ids != null) {
            for (BigInteger item : ids) {
                result.add(em.find(Citaciones.class, item.longValue()));
            }
        }
        return result;

    }

    public List<Citaciones> listaEventosByComisaria(Date start, Date end, CatalogoItem comisaria) {
        List<BigInteger> ids = (List<BigInteger>) em.createNativeQuery("select c.id from comisaria.citaciones c where cast(c.fecha as DATE) between ?1 and ?2 and  c.comisaria = ?3").setParameter(1, start).setParameter(2, end).setParameter(3, comisaria.getId()).getResultList();
        List<Citaciones> result = new ArrayList<>();
        if (ids != null) {
            for (BigInteger item : ids) {
                result.add(em.find(Citaciones.class, item.longValue()));
            }
        }
        return result;

    }

    public Long numCitaciones() {
        BigInteger res = (BigInteger) em.createNativeQuery("SELECT nextval('comisaria.citaciones_num')").getResultStream().findFirst().orElse(BigInteger.ZERO);
        return res.longValue();
    }

    public List<Citaciones> findCitacionesNotComparecenciaByTramite(HistoricoTramites tramite) {
        try {
            return em.createQuery("SELECT c FROM Citaciones c WHERE c.tramite = :tramite and c.acuerdo IS NULL").setParameter("tramite", tramite.getId()).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "findCitacionesNotComparecenciaByTramite", e);
            return null;
        }
    }
}
