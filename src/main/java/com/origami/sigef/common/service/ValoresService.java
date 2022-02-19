/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Ambiente;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class ValoresService extends AbstractService<Valores> {

    /**
     *
     */
//    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public ValoresService() {
        super(Valores.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public String findByCodigo(String codigo) {
        try {
            Valores valor = (Valores) getEntityManager().createQuery("SELECT d FROM Valores d WHERE d.code=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
            if (valor != null) {
                return valor.getValorString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal findByCodigo1(String codigo) {
        try {
            Valores valor = (Valores) getEntityManager().createQuery("SELECT d FROM Valores d WHERE d.code=:codigo")
                    .setParameter("codigo", codigo).getSingleResult();
            if (valor != null) {
                return valor.getValorNumeric();
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public Valores getValor(String code) {
        try {
            Query query = em.createQuery("SELECT d FROM Valores d WHERE d.code=?1")
                    .setParameter(1, code);
            Valores valor = (Valores) query.getSingleResult();
            return valor;
        } catch (Exception ex) {
            System.out.println("catch");
            return null;
        }
    }

    public String findAmbienteComprobantesElectronicos() {
        try {
            Ambiente valor = (Ambiente) getEntityManager().createQuery("SELECT a FROM Ambiente a WHERE a.activo = TRUE ")
                    .getSingleResult();
            if (valor != null) {
                return valor.getCodigo();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
