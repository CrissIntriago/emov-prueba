/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.SecuenciaMemo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class SecuenciaMemoService extends AbstractService<SecuenciaMemo> {

    private static final Logger LOG = Logger.getLogger(SecuenciaMemoService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public SecuenciaMemoService() {
        super(SecuenciaMemo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public SecuenciaMemo secuenciaMemoUno(Integer anio, String codigo) {
        List<SecuenciaMemo> result = (List<SecuenciaMemo>) em.createQuery("SELECT s from SecuenciaMemo s where s.codigo=:codigo AND s.anio=:anio")
                .setParameter("codigo", codigo).setParameter("anio", anio.shortValue()).getResultList();

        SecuenciaMemo data = new SecuenciaMemo();
        if (result != null && !result.isEmpty()) {
            data = result.get(0);
            if (result.get(0) != null && result.get(0).getNum() != null) {
                data.setNum(result.get(0).getNum() + 1L);
                System.out.println("num 1 " + data.getNum());
                return data;
            }
            data.setNum(1L);
            System.out.println("num 2 " + data.getNum());

            return data;
        }

        data.setCodigo(codigo);
        data.setAnio(anio.shortValue());
        data.setNum(1L);
        System.out.println("num 3 " + data.getNum());
        return data;
    }

}
