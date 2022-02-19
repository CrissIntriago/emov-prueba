/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.OrdenDet;
import com.origami.sigef.common.entities.OrdenTrabajo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsonUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dairon Freddy
 */
@javax.ejb.Stateless @javax.enterprise.context.Dependent
public class OrdenService extends AbstractService<OrdenTrabajo> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private UserSession session;

    public OrdenService() {
        super(OrdenTrabajo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public OrdenTrabajo save(OrdenTrabajo v, List<OrdenDet> detalles) {
        if (v.getId() == null) {
            if (detalles == null) {
                JsonUtil j = new JsonUtil();
                detalles = new ArrayList<>(1);
                OrdenDet d = new OrdenDet();
                d.setClazz(v.getCooperativa().getClass().getName());
                d.setDatoActual(j.toJson(v.getCooperativa()));
                d.setEstadoDet("CR");
                d.setFecAct(new Date());
                d.setFecCre(new Date());
                d.setIdentificador(BigInteger.valueOf(v.getCooperativa().getId()));
                d.setUsrAct(session.getNameUser());
                d.setOrden(v);
                detalles.add(d);
            }
            BigInteger max = this.max("numOrden");
            if (max == null) {
                max = BigInteger.ZERO;
            }
            v.setNumOrden(max.add(BigInteger.ONE));
            v.setFecCre(new Date());
            v.setUsrCre(session.getNameUser());
            v.setEstadoOt("CR");
            v.setDetelles(detalles);
            v = this.create(v);
            return v;
        } else {
            v.setFecAct(new Date());
            v.setUsrAct(session.getNameUser());
            this.edit(v);
            if (detalles != null) {
                for (OrdenDet detalle : detalles) {
                    if (detalle.getId() == null) {
                        em.persist(detalle);
                    } else {
                        em.merge(detalle);
                    }
                }
            }
            return v;
        }
    }

}
