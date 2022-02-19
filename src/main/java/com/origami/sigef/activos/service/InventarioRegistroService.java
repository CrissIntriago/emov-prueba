/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.service;

import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioRegistro;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author OrigamiEc
 */
@Stateless
@javax.enterprise.context.Dependent
public class InventarioRegistroService extends AbstractService<InventarioRegistro> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InventarioRegistroService() {
        super(InventarioRegistro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<InventarioRegistro> findRegistroInvByNumInv(Inventario inv) {
        return findByNamedQuery("InventarioRegistro.findRegistroInvByNumInv", inv);
    }

    public Long findIdRegistroInvByNumInv(Inventario inv) {
        return findByNamedQuery1("InventarioRegistro.findIdRegistroInvByNumInv", inv);
    }

    public Adquisiciones getAdquisicionByInventarioRegistro(Inventario inv) {
        Adquisiciones adq = (Adquisiciones) em.createNativeQuery("Select a.* from  activos.inventario_registro ir\n"
                + "inner join activos.inventario i on i.id = ir.movimiento\n"
                + "inner join activos.adquisiciones a on a.id = ir.adquisiciones\n"
                + "WHERE i.id = ?1 ", Adquisiciones.class)
                .setParameter(1, inv.getId()).getResultStream().findFirst().orElse(null);
        return adq;
    }

}
