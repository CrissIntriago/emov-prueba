/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaMedidasCautelares;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.service.AbstractService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class MedidasCautelaresServices extends AbstractService<CoaMedidasCautelares> {

    private static final Logger LOG = Logger.getLogger(MedidasCautelaresServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public MedidasCautelaresServices() {
        super(CoaMedidasCautelares.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatalogoItem> findMedidasCautelares(CoaJuicio juicio) {
        List<CatalogoItem> medidas = new ArrayList<>();
        try {
            medidas = (List<CatalogoItem>) em.createQuery("Select a.medidaCautelar from CoaMedidasCautelares a where a.juicios.id=?1 and a.estado=true").setParameter(1, juicio.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error al traer las medidac cautelares del juico: ", ex.getMessage());
        }
        return medidas;
    }

    public int actualizarMedidasCautelares(Long idJuicio) {
        int result = em.createQuery("UPDATE CoaMedidasCautelares a SET a.estado=false where a.juicios.id=?1").setParameter(1, idJuicio).executeUpdate();
        return result;
    }

    public CoaMedidasCautelares findMedidaCauleraExistente(Long idJuicio, Long idCatalogo) {
        CoaMedidasCautelares medida = new CoaMedidasCautelares();
        try {
            medida = (CoaMedidasCautelares) em.createQuery("select a from CoaMedidasCautelares a where a.juicios.id=?1 and a.medidaCautelar.id=?2").setParameter(1, idJuicio).setParameter(2, idCatalogo).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error albuscar la existencia" + ex.getMessage());
        }
        return medida;
    }

    public List<CoaMedidasCautelares> findMedidasCautelaresJuicio(CoaJuicio juicio) {
        List<CoaMedidasCautelares> medidas = new ArrayList<>();
        try {
            medidas = (List<CoaMedidasCautelares>) em.createQuery("Select a from CoaMedidasCautelares a where a.juicios.id=?1 and a.estado=true").setParameter(1, juicio.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error al traer las medidac cautelares del juico: ", ex.getMessage());
        }
        return medidas;
    }
}
