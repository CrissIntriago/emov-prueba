/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.services;

import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Angel Navarro
 */
@Stateless
@javax.enterprise.context.Dependent
public class RolService extends AbstractService<Rol> {

    private static final Logger LOG = Logger.getLogger(RolService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RolService() {
        super(Rol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public Rol save(Rol rol) {
        if (rol != null) {
            if (rol.getId() == null) {
                rol.setEstado(true);
                return this.create(rol);
            } else {
                this.edit(rol);
                return rol;
            }
        }
        return null;
    }

    public List<Rol> rolesUnidadAdministrativa(UnidadAdministrativa ua) {
        try {
            return this.findByNamedQuery("Rol.findByUnidadAdministrativa", new Object[]{ua});
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public boolean existe(Rol rol) {
        if (rol.getId() == null) {
            Rol r = findByNamedQuery1("Rol.findByNombre", rol.getNombre());
            return (r != null);
        } else {
            Rol r = findByNamedQuery1("Rol.findByNombreDistinctId", rol.getNombre(), rol.getId());
            return (r != null);
        }
    }

}
