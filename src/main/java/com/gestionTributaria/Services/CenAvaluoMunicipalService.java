/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CenAvaluoMunicipal;
import com.gestionTributaria.Entities.Obra;
import com.gestionTributaria.Entities.PredioAnio;
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
public class CenAvaluoMunicipalService extends AbstractService<CenAvaluoMunicipal> {

    private static final Logger LOG = Logger.getLogger(CatCiudadelasService.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CenAvaluoMunicipalService() {
        super(CenAvaluoMunicipal.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public List<CenAvaluoMunicipal> findAvaluoMunicipalRango(Integer anio_inicio, Integer anio_fin) {
        List<CenAvaluoMunicipal> avaluo = new ArrayList<>();
        try {
            avaluo = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal a where a.anioDesde=?1 and a.anioHasta=?2").setParameter(1, anio_inicio.shortValue()).setParameter(2, anio_fin.shortValue()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el avaluo por rangos", ex);
        }
        return avaluo;
    }

    public CenAvaluoMunicipal findAvaluoMunicipal_2010(PredioAnio avaluoAnio) {
        List<CenAvaluoMunicipal> avaluos = new ArrayList<>();
        try {
            avaluos = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal a where a.anioDesde=2010 and a.anioHasta=2025 and ?1>=a.avaluoDesde and ?2<= a.avaluoHasta").setParameter(1, avaluoAnio.getAvaluoMunicipal()).setParameter(2, avaluoAnio.getAvaluoMunicipal()).getResultList();
            if (avaluos.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el avalup por avaluo municipal 2010", ex);
        }
        return avaluos.get(0);
    }

    public CenAvaluoMunicipal findAvaluoMunicipal_2012(PredioAnio avaluoAnio) {
        List<CenAvaluoMunicipal> avaluos = new ArrayList<>();
        try {
            avaluos = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal a where a.anioDesde=2012 and a.anioHasta=2027 and ?1>=a.avaluoDesde and ?2<= a.avaluoHasta").setParameter(1, avaluoAnio.getAvaluoMunicipal()).setParameter(2, avaluoAnio.getAvaluoMunicipal()).getResultList();
            if (avaluos.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el avalup por avaluo municipal 2012", ex);
        }
        return avaluos.get(0);
    }

    public CenAvaluoMunicipal findAvaluoMunicipal_2013(Obra obra, BigInteger sector) {
        List<CenAvaluoMunicipal> avaluo = new ArrayList<>();
        CenAvaluoMunicipal cen = new CenAvaluoMunicipal();
        try {
            avaluo = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal a where a.anioDesde=2013 and a.sector=?1 and a.obra=?2 ").setParameter(1, sector).setParameter(2, BigInteger.valueOf(obra.getId())).getResultList();
            if (avaluo.isEmpty()) {
                cen = null;
            } else {
                cen = avaluo.get(0);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el avaluo por avaluo municipal 2013", ex);
        }
        return cen;
    }

    public CenAvaluoMunicipal findAvaluoMunicipalAnio(BigInteger anio) {
        CenAvaluoMunicipal avaluo = new CenAvaluoMunicipal();
        try {
            avaluo = (CenAvaluoMunicipal) em.createQuery("select a from CenAvaluoMunicipal a where a.anioDesde=?1").setParameter(1, anio.shortValue()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar el avalup por avaluo municipal anio", ex);
        }
        return avaluo;
    }

    public CenAvaluoMunicipal findAvaluoMunicipalAplicanTodoDuran(BigInteger obra, CatCiudadela ciudadela) {
        System.out.println("OBRA: " + obra);
        System.out.println("ciudadela: " + ciudadela.getId());
        List<CenAvaluoMunicipal> avaluo = new ArrayList<>();
        try {
            avaluo = (List<CenAvaluoMunicipal>) em.createQuery("select a from CenAvaluoMunicipal a"
                    + " where a.anioDesde=2013 and a.obra=?1 and a.codigoCiudadela.id=?2 ").setParameter(1, obra).setParameter(2, ciudadela.getId()).getResultList();
            if (avaluo.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar los avaluos que aplican a otodo duran", ex);
        }
        return avaluo.get(0);
    }

}
