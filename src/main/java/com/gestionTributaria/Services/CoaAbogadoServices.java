/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CoaAbogado;
import com.origami.sigef.common.config.CONFIG;
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
public class CoaAbogadoServices extends AbstractService<CoaAbogado> {

    private static final Logger LOG = Logger.getLogger(CoaAbogadoServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CoaAbogadoServices() {
        super(CoaAbogado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public CoaAbogado findByIdentificacion() {
        CoaAbogado abogado = null;
        try {
            abogado = (CoaAbogado) em.createQuery("select e from CoaAbogado e where e.authUsuarios.ente.identificacion = :identificacion").getSingleResult();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return abogado;
    }

    public List<CoaAbogado> getAllabogados() {
        List<CoaAbogado> abogados = new ArrayList<>();
        try {
            abogados = (List<CoaAbogado>) em.createQuery("select a from CoaAbogado a where a.estado=true").getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaAbogado.class.getName()).log(Level.SEVERE, "ERROR AL TRAER ABOGADOS", ex);
            return null;
        }
        return abogados;
    }
}
