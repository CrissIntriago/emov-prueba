/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.entities.SecuenciaGeneral;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless @javax.enterprise.context.Dependent
public class SecuenciaGeneralService extends AbstractService<SecuenciaGeneral> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SecuenciaGeneralService() {
        super(SecuenciaGeneral.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public SecuenciaGeneral findByCodigoAndAnio(String codigo, Integer anio) {
        try {
            return (SecuenciaGeneral) getEntityManager().createQuery("SELECT d FROM SecuenciaGeneral d WHERE d.code=:codigo and d.anio=:anio")
                    .setParameter("codigo", codigo)
                    .setParameter("anio", anio).getSingleResult();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
    
    public SecuenciaGeneral findByCodigoAndAmbiente(String codigo, Long ambiente) {
        try {
            return (SecuenciaGeneral) getEntityManager().createQuery("SELECT d FROM SecuenciaGeneral d WHERE d.code=:codigo AND d.ambiente =:ambiente")
                    .setParameter("codigo", codigo)
                    .setParameter("ambiente", ambiente).getSingleResult();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public List<Short> getListPeriodos() {
        return (List<Short>) getEntityManager().createQuery("SELECT d.anio FROM SecuenciaGeneral d GROUP BY 1 ASC").getResultList();
    }
    
}
