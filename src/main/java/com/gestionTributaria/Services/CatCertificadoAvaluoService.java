/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;
import com.gestionTributaria.Entities.CatCertificadoAvaluo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI1
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CatCertificadoAvaluoService extends AbstractService<CatCertificadoAvaluo> {

    private static final Logger LOG = Logger.getLogger(CatCertificadoAvaluoService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatCertificadoAvaluoService() {
        super(CatCertificadoAvaluo.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }
}
