/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Comisaria.Service;

import com.gestionTributaria.Entities.InquilinatoCarpetaDetalle;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Stateless
@javax.enterprise.context.Dependent
public class InquilinatoCarpetaDetalleService extends AbstractService<InquilinatoCarpetaDetalle> {

    private static final Logger LOG = Logger.getLogger(InquilinatoCarpetaDetalleService.class.getName());

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InquilinatoCarpetaDetalleService() {
        super(InquilinatoCarpetaDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
