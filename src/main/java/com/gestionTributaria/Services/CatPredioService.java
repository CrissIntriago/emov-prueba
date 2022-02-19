/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.Obra;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CatPredioService extends AbstractService<CatPredio> {

    private static final Logger LOG = Logger.getLogger(CatPredioService.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public CatPredioService() {
        super(CatPredio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatPredio> getPredioByParam(HashMap<String, Object> param) {
        try {
            List<CatPredio> predios = new ArrayList<>();
            if (param.get("tipoCOnsulta").equals("1")) {
                predios = (List<CatPredio>) em.createQuery("select a FROM CatPredio a where a.estado=:estado and a.parroquia=:parroquia order by a.id asc ").
                        setParameter("estado", param.get("estado")).setParameter("parroquia", param.get("parroquia")).getResultList();
            }
            if (param.get("tipoCOnsulta").equals("2")) {
                predios = (List<CatPredio>) em.createQuery("select a FROM CatPredio a where a.estado=:estado and a.parroquia=:parroquia and a.zona=:zona order by a.id asc ").
                        setParameter("estado", param.get("estado")).setParameter("parroquia", param.get("parroquia")).setParameter("zona", param.get("zona")).getResultList();
            }
            if (param.get("tipoCOnsulta").equals("3")) {
                predios = (List<CatPredio>) em.createQuery("select a FROM CatPredio a where a.estado=:estado and a.parroquia=:parroquia and a.zona=:zona "
                        + "and a.sector=:sector and a.mz=:mz and a.solar=:solar order by a.id asc ").
                        setParameter("estado", param.get("estado")).setParameter("parroquia", param.get("parroquia")).setParameter("zona", param.get("zona")).setParameter("sector", param.get("sector"))
                        .setParameter("mz", param.get("mz")).setParameter("solar", param.get("solar")).getResultList();
                System.out.println("resultado: " + predios);
            }
            if (param.get("tipoCOnsulta").equals("5")) {
                predios = (List<CatPredio>) em.createQuery("select a FROM CatPredio a where a.estado=:estado and a.id=:predio order by a.id asc ").
                        setParameter("estado", param.get("estado")).setParameter("predio", param.get("predio")).getResultList();
            }
            if (param.get("tipoCOnsulta").equals("6")) {
                predios = (List<CatPredio>) em.createQuery("select a from CatPredio a where a.estado=:estado and a.ciudadela=:ciudadela")
                        .setParameter("estado", param.get("estado")).setParameter("ciudadela", param.get("ciudadela")).getResultList();
            }
            if (predios == null) {
                predios = new ArrayList<>();
                return predios;
            }
            return predios;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "eRROR AL TARER PREDIOS", ex);
            return null;
        }
    }

    public BigDecimal areaSolar(HashMap<String, Object> param) {
        BigDecimal areaSolar = BigDecimal.ZERO;;
        try {
            areaSolar = (BigDecimal) em.createQuery("SELECT round(SUM(s.areaSolar),2) from CatPredio s where s.estado='A' and s.parroquia=:parroquia and s.zona=:zona and s.sector=:sector and s.mz=:mz and s.solar=:solar")
                    .setParameter("parroquia", param.get("parroquia")).setParameter("zona", param.get("zona")).setParameter("sector", param.get("sector")).setParameter("mz", param.get("mz"))
                    .setParameter("solar", param.get("solar")).getSingleResult();
            if (areaSolar == null) {
                areaSolar = BigDecimal.ZERO;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer area total", ex);
        }
        return areaSolar;
    }

    public BigDecimal totalAvaluo(HashMap<String, Object> param) {
        BigDecimal totalAvaluo = BigDecimal.ZERO;;
        try {
            totalAvaluo = (BigDecimal) em.createQuery("SELECT round(SUM(s.avaluoMunicipal),2) from CatPredio s where s.estado='A' and s.parroquia=:parroquia and s.zona=:zona and s.sector=:sector and s.mz=:mz and s.solar=:solar")
                    .setParameter("parroquia", param.get("parroquia")).setParameter("zona", param.get("zona")).setParameter("sector", param.get("sector")).setParameter("mz", param.get("mz"))
                    .setParameter("solar", param.get("solar")).getSingleResult();
            if (totalAvaluo == null) {
                totalAvaluo = BigDecimal.ZERO;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al calcular avaluo", ex);
        }
        return totalAvaluo;
    }

    public Long contarPredio(HashMap<String, Object> param) {
        Long contarPredio = 0L;
        try {
            contarPredio = (Long) em.createQuery("SELECT COUNT(p) FROM CatPredio p  where p.estado='A' and p.parroquia=:parroquia and p.zona=:zona and p.sector=:sector and p.mz=:mz and p.solar=:solar")
                    .setParameter("parroquia", param.get("parroquia")).setParameter("zona", param.get("zona")).setParameter("sector", param.get("sector")).setParameter("mz", param.get("mz"))
                    .setParameter("solar", param.get("solar")).getSingleResult();
            if (contarPredio == null) {
                contarPredio = 0L;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al contar predios", ex);
        }
        return contarPredio;
    }

    public BigDecimal calcularValorRecaudadoObra(Obra obra) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            total = (BigDecimal) em.createQuery("select round( sum(a.pagoAnual),2) from CatPredioSumasAnualesUbicacion a where a.obra.id=:obra").setParameter("obra", obra.getId()).getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al traer calculo total recaudado", ex);
        }
        return total;
    }

    public List<CatPredio> prediosNoAplicanCEM() {
        List<CatPredio> predios = new ArrayList<>();
        try {
            predios = (List<CatPredio>) em.createQuery("select a from CatPredio a where a.sector=6 and a.mz=57 and solar<>4").getResultList();
            if (!predios.isEmpty()) {
                predios.add((CatPredio) em.createQuery("select a from CatPredio a where a.numPredio=18658").getSingleResult());
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return predios;
    }

    public List<CatPredio> prediosUrbanos() {
        List<CatPredio> predioRurales = new ArrayList<CatPredio>();
        try {
            predioRurales = (List<CatPredio>) em.createQuery("select a from CatPredio a where a.tipoPredio='U' and a.estado='A' order by a.numPredio asc ").getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar predios rurales", ex);
        }
        return predioRurales;
    }

    public List<CatPredio> findByClaveCatastral(String claveCatastal, String tipoUrbanoRustico) {
        List<CatPredio> listaPredios = new ArrayList<>();
        try {
            listaPredios = (List<CatPredio>) em.createQuery("select a from CatPredio a where a.claveCat like ?1 and a.tipoPredio=?2").setParameter(1, claveCatastal + "%").setParameter(2, tipoUrbanoRustico).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar predios por claves catastral", ex);
            return null;
        }
        return listaPredios;
    }

    public CatPredio finByPredioClaveCatastral(String clave) {
        List<CatPredio> predio = new ArrayList<>();
        CatPredio pre = new CatPredio();
        try {
            predio = (List<CatPredio>) em.createQuery("select a from CatPredio a where a.claveCat like ?1").setParameter(1, clave).getResultList();
            if (predio.isEmpty()) {
                pre = null;
            } else {
                pre = predio.get(0);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar la clave catastral", ex);
        }
        return pre;
    }
}
