/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planeamiento.Services;

import com.planeamiento.Entities.PlaneamientoUrbano;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI
 */
@Stateless
@Dependent
public class PlaneamientoUrbanoService extends AbstractService<PlaneamientoUrbano> {

    private static final Logger LOG = Logger.getLogger(PlaneamientoUrbano.class.getName());
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public PlaneamientoUrbanoService() {
        super(PlaneamientoUrbano.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
