/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;
 
import com.gestionTributaria.Entities.FormatoReporte;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class FormatoReporteService extends AbstractService<FormatoReporte> {

    private static final Logger LOG = Logger.getLogger(FormatoReporte.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FormatoReporteService() {
        super(FormatoReporte.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public FormatoReporte getFormatoReporteByCodigo(String codigo) {
        try {
            Query query = em.createQuery("SELECT t FROM FormatoReporte t WHERE t.codigo = ?1").setParameter(1, codigo);
            FormatoReporte result = (FormatoReporte) query.getSingleResult();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

}
