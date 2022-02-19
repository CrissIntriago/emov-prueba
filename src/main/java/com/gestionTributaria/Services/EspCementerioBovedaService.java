/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.EspCementerio;
import com.gestionTributaria.Entities.EspCementerioBoveda;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author DEVELOPER
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class EspCementerioBovedaService extends AbstractService<EspCementerioBoveda> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public EspCementerioBovedaService() {
        super(EspCementerioBoveda.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public boolean existeboveda(String manzana, BigDecimal lote, EspCementerio cementerio) {

        Long exits = (Long) em.createNativeQuery("select d.id from sgm.esp_cementerio_boveda d  where d.manzana=?1 and d.ancho=?2 and d.cementerio=?3;")
                .setParameter(1, manzana).setParameter(2, lote).setParameter(3, cementerio.getId()).getSingleResult();
        return exits != null;
    }

}
