/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author LuisPozoG
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class DistributivoAnexoService extends AbstractService<DistributivoAnexo> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public DistributivoAnexoService() {
        super(DistributivoAnexo.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DistributivoAnexo getValidateValor(ParametrosTalentoHumano p, OpcionBusqueda o) {
        try {
            Query query = em.createQuery("SELECT da FROM DistributivoAnexo da JOIN da.valorParametrizado p JOIN p.tipo c WHERE c.codigo = ?1 AND da.anio = ?2 AND da.estado = true")
                    .setParameter(1, p.getTipo().getCodigo()).setParameter(2, o.getAnio());
            DistributivoAnexo result = (DistributivoAnexo) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

//Traer todos los distributivos anexos para añadirlo y agregarle la partida si no existe ya por dicho año
    public List<DistributivoAnexo> getDisAnexosNoExistInPartidaAnexo(Short periodo) {
        try {
            Query query = em.createQuery("SELECT da FROM DistributivoAnexo da WHERE da.anio = ?1 and da.estado = true and da.id NOT IN (SELECT pd.distributivoAnexo FROM PartidasDistributivoAnexo pd WHERE pd.periodo = ?1 and pd.estado = true AND pd.codigoReforma  IS NULL AND  pd.codigoReformaTraspaso IS NULL)")
                    .setParameter(1, periodo);
            List<DistributivoAnexo> listaDisAnexo = query.getResultList();
            return listaDisAnexo;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<DistributivoAnexo> getDisAnexos(Short periodo) {
        try {
            Query query = em.createQuery("SELECT da FROM DistributivoAnexo da WHERE da.anio = ?1 and da.estado = true")
                    .setParameter(1, periodo);
            List<DistributivoAnexo> listaDisAnexo = query.getResultList();
            return listaDisAnexo;
        } catch (NoResultException e) {
            return null;
        }
    }

    public BigDecimal getValorActualDistributivo(DistributivoAnexo dis) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT d.monto FROM DistributivoAnexo d where  d = :dis")
                .setParameter("dis", dis).getSingleResult();
        if (valor == null) {
            valor = BigDecimal.ZERO;
            return valor;
        } else {
            return valor;
        }
    }

    public BigDecimal sumaVerificacion(BigInteger r) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM PartidasDistributivoAnexo p where  p.codigoReforma= :reforma").setParameter("reforma", r).getSingleResult();
        return valor;
    }

    public BigDecimal valorActualSuplemeto(PartidasDistributivoAnexo p) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM PartidasDistributivoAnexo p where  p.id= :id").setParameter("id", p.getId()).getSingleResult();
        return valor;
    }

    public BigDecimal valorActualReduccion(PartidasDistributivoAnexo p) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReduccion) FROM PartidasDistributivoAnexo p where p.id= :id").setParameter("id", p.getId()).getSingleResult();
        return valor;
    }

    public PartidasDistributivoAnexo valorActual(PartidasDistributivoAnexo p) {
        PartidasDistributivoAnexo valor = (PartidasDistributivoAnexo) em.createQuery("SELECT p FROM PartidasDistributivoAnexo p where  p.id= :reforma").setParameter("reforma", p.getId()).getSingleResult();
        return valor;
    }

    public BigDecimal valorCupoAsignado(BigInteger p) {
        BigDecimal valor = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemento) FROM PartidasDistributivoAnexo p where p.codigoReforma= :id").setParameter("id", p).getSingleResult();
        return valor;
    }

    public Boolean getComprobarPartida(DistributivoAnexo disAnexo) {
        Boolean resultBoolean = Boolean.FALSE;
        PartidasDistributivoAnexo resultado = (PartidasDistributivoAnexo) em.createQuery("SELECT pda FROM PartidasDistributivoAnexo pda WHERE pda.distributivoAnexo=:disAnexo AND pda.codigoReforma is null AND pda.codigoReformaTraspaso is null")
                .setParameter("disAnexo", disAnexo)
                .getResultStream().findFirst().orElse(null);
        if (resultado != null) {
            if (resultado.getPartidaAp() != null) {
                resultBoolean = Boolean.TRUE;
            }
        }
        return resultBoolean;
    }

    public void getDelePartidaValorDistributivo(DistributivoAnexo disAnexo) {
        Query query = em.createQuery("DELETE FROM PartidasDistributivoAnexo pda WHERE pda.distributivoAnexo=:disAnexo AND pda.codigoReforma is null AND pda.codigoReformaTraspaso is null")
                .setParameter("disAnexo", disAnexo);
        int result = query.executeUpdate();
    }
}
