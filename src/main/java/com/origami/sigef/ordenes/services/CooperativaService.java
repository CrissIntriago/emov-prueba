/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes.services;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CooperativaService extends AbstractService<Cooperativa> {

    private static final Logger LOG = Logger.getLogger(CooperativaService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;

    public CooperativaService() {
        super(Cooperativa.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Cooperativa save(Cooperativa coop) {
        coop.getEmpresa().setEstado(Boolean.TRUE);
        coop.getEmpresa().setUsuarioCreacion(session.getNameUser());
        coop.getEmpresa().setFechaCreacion(new Date());
        Cliente empresa = coop.getEmpresa();
        if (coop.getEmpresa().getId() == null) {
            empresa = clienteService.create(coop.getEmpresa());
        } else {
            clienteService.edit(empresa);
        }
        coop.setFechaMod(new Date());
        coop.setUsuarioMod(session.getNameUser());
        if (coop.getId() == null) {
            coop.setEmpresa(empresa);
            coop.setEstado("AC");
            coop.setFechaCre(new Date());
            coop.setUsuarioCre(session.getNameUser());;
            coop.setFechaRegistro(new Date());
            return this.create(coop);
        } else {
            this.edit(coop);
            return coop;
        }
    }

    public Cooperativa findByIdentificacion(String ciRuc) {
        try {
            if (ciRuc.length() <= 10) {
                return findByNamedQuery1("Cooperativa.findByEmpresaIdentificacion", ciRuc);
            } else {
                String ruc = ciRuc.substring(10);
                String ident = ciRuc.substring(0, 10);
                return findByNamedQuery1("Cooperativa.findByEmpresaIdentificacionRuc", ident, ruc);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, ciRuc, e);
        }
        return null;
    }

}
