/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProductoProceso;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PappProcesoService extends AbstractService<PappProceso> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PappProcesoService() {
        super(PappProceso.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getEmail(Long id) {
        List<String> result = (List<String>) em.createNativeQuery("select cli.email from auth.usuarios us\n"
                + "inner join talento_humano.servidor serv on serv.id = us.funcionario\n"
                + "inner join talento_humano.distributivo d on d.id = serv.distributivo\n"
                + "inner join public.unidad_administrativa ud on d.unidad_administrativa = ud.id\n"
                + "inner join talento_humano.cargo car ON car.id = d.cargo\n"
                + "inner join cliente cli ON cli.id = serv.persona\n"
                + "where us.id = ?1").setParameter(1, id).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return "";
    }

    public PappProceso getSecuencial(short periodo) {
        try {
            PappProceso resultado = (PappProceso) em.createQuery("SELECT p FROM PappProceso p WHERE EXTRACT(year from p.fechaTramite)=:periodo AND p.secuencial = (SELECT MAX(papp.secuencial) from PappProceso papp WHERE EXTRACT(year from papp.fechaTramite)=:periodo ) ORDER BY p.secuencial DESC")
                    .setParameter("periodo", (int) periodo)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<PappProceso> getListConsulta(Short periodo, CatalogoItem catalogoItem3) {
        try {
            List<PappProceso> resultado = (List<PappProceso>) em.createQuery("SELECT p FROM PappProceso p WHERE EXTRACT(year from p.fechaTramite)=:periodo  ORDER BY p.secuencial ASC")
                    .setParameter("periodo", periodo.intValue())
                    .getResultList();
            return resultado;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public PappProceso getPappProceso(short periodo, BigInteger tramit) {
        try {
            PappProceso resultado = (PappProceso) em.createQuery("SELECT p FROM PappProceso p WHERE EXTRACT(year from p.fechaTramite)=:periodo AND p.numeroTramite = :tramite ")
                    .setParameter("periodo", (int) periodo).setParameter("tramite", tramit)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal getValorAproabdo(Producto prod) {
        List<BigDecimal> result = (List<BigDecimal>) em.createQuery("SELECT coalesce(sum(p.solicitado),0) FROM ProductoProceso p where p.solicitado is not null and p.producto=:prod AND p.estadoProceso.codigo in ('APRO')")
                .setParameter("prod", prod).getResultList();
        System.out.println("result" + result.size());
        if (result != null && !result.isEmpty() && result.get(0) != null) {
            System.out.println("result.get(0)" + result.get(0));
            return result.get(0);
        }
        return BigDecimal.ZERO;
    }

    public List<PappProceso> getListaPappProceso(BigInteger tramit) {

        return (List<PappProceso>) em.createQuery("SELECT p FROM PappProceso p WHERE p.numeroTramite = :tramite ")
                .setParameter("tramite", tramit)
                .getResultList();

    }

    public List<ProductoProceso> getListaProductoProceso(BigInteger tramit) {

        return (List<ProductoProceso>) em.createQuery("SELECT p FROM ProductoProceso p WHERE p.numetoTramite = :tramite ")
                .setParameter("tramite", tramit)
                .getResultList();

    }

}
