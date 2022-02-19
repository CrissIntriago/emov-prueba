/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CheckList;
import com.gestionTributaria.Entities.CoaJuicio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigInteger;
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
public class CheckListService extends AbstractService<CheckList> {

    private static final Logger LOG = Logger.getLogger(CheckListService.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CheckListService() {
        super(CheckList.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public CheckList findbyIdTramite(Long idTramite) {
        CheckList memo = new CheckList();
        List<CheckList> memos = new ArrayList<>();
        try {
            memos = (List<CheckList>) em.createQuery("select a from CheckList a where a.tramite=?1 ").setParameter(1, BigInteger.valueOf(idTramite)).getResultList();
            if (!memos.isEmpty()) {
                memo = memos.get(0);
            } else {
                memo = null;
            }

        } catch (Exception ex) {
            Logger.getLogger(CheckList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memo;
    }
}
