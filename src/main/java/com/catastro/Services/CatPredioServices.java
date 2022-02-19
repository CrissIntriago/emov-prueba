/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Services;

import com.catastro.Entities.CatPredioLinderos;
import com.catastro.Entities.CatPredioS4;
import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
public class CatPredioServices extends AbstractService<CatPredio> {

    private static final Logger LOG = Logger.getLogger(CatPredioServices.class.getName());
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatPredioServices() {
        super(CatPredio.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public CatPredio getPredioId(Long id) {
        CatPredio p = null;
        try {
            p = em.find(CatPredio.class, id);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return p;
    }

    public CatPredio getPredioByClaveCatAnt(String claveCatAnt) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("predialant", claveCatAnt);

            return em.find(CatPredio.class, paramt);

        } catch (Exception e) {
            Logger.getLogger(CatPredioServices.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatPredio getPredioByClaveCat(String claveCat) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("claveCat", claveCat);
            return em.find(CatPredio.class, paramt);
        } catch (Exception e) {
            Logger.getLogger(CatPredioServices.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatPredio getPredioNumPredio(BigInteger numPredio) {
        CatPredio p = null;
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("numPredio", numPredio);
            p = em.find(CatPredio.class, paramt);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return p;
    }

    public Boolean existePredio(CatPredio s1) {
        CatPredio p = null;
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("zonap", s1.getZona());
            paramt.put("sectorp", s1.getSector());
            paramt.put("mzp", s1.getMz());
            paramt.put("solarp", s1.getSolar());
            paramt.put("bloquep", s1.getBloque());
            paramt.put("pisop", s1.getPiso());
            paramt.put("unidadp", s1.getUnidad());
            paramt.put("provincia", s1.getProvincia());
            paramt.put("canton", s1.getCanton());
            paramt.put("parroquia", s1.getParroquia());
            p = em.find(CatPredio.class, paramt);
            return p != null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "existePredio", e);
            return null;
        }
    }

    public CatPredio generarNumPredio(CatPredio predio) {
        try {
            return generarNumPredioAndGuardarCatPredio(predio);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatPredio generarNumPredioAndGuardarCatPredio(CatPredio predio) {
        try {
            if (predio.getId() == null) {
                return (CatPredio) create(predio);
            } else {
                edit(predio);
                return predio;
            }

        } catch (Exception e) {
            Logger.getLogger(CatPredio.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List<CatPredioLinderos> guardarLinderos(List<CatPredioLinderos> linderos) {
        List<CatPredioLinderos> ld = new ArrayList<>();
        try {
            for (CatPredioLinderos lindero : linderos) {
                em.persist(lindero);
                ld.add(lindero);
            }
            return ld;

        } catch (Exception e) {
            Logger.getLogger(CatastroServices.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public CatPredioS4 guardarPredioS4(CatPredioS4 s4) {
        CatPredioS4 ss4 = null;
        CatPredioS4 sss4 = null;
        try {
            ss4 = (CatPredioS4) em.createQuery("select c from CatPredioS4 c where c.predio.id =?1 ").setParameter(1, s4.getPredio().getId()).getSingleResult();
            if (ss4 != null) {
                s4.setId(ss4.getId());
            }

            em.persist(s4);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return sss4;
    }

    public Long CountPredios() {
        System.out.println("Entra al metodos");
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT count(*) FROM CatPredio p").getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long PrediosActivos() {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT count(*) FROM CatPredio p WHERE p.estado='A'").getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long PrediosInactivos() {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT COUNT(*) FROM CatPredio p WHERE p.estado='I'").getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long numPrediosHistorico() {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT COUNT(p) FROM CatPredio p WHERE p.estado='H'").getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long TotalesPrediosUrbanosPorTipoPredio(String tipo) {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("select count (a.id) from CatPredio a where a.tipoPredio = ?1 and a.estado='A'").setParameter(1, tipo).getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long TotalesPrediosPrivados(String nombre) {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT count(a.id) FROM CatPredio a WHERE a.propiedad.descripcion like ?1").setParameter(1, nombre).getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public Long TotalesPrediosPublicos(String nombre) {
        Long cantidad = 0L;
        try {
            cantidad = (Long) em.createQuery("SELECT count(a.id) FROM CatPredio a WHERE a.propiedad.descripcion like ?1").setParameter(1, nombre).getSingleResult();
        } catch (Exception ex) {
            cantidad = 0L;
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public BigDecimal TotalesAvaluosTerrenos() {
        BigDecimal cantidad = new BigDecimal("0.00");
        try {
            cantidad = (BigDecimal) em.createQuery("SELECT sum(a.avaluoSolar) FROM CatPredio a").getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public BigDecimal TotalesAvaluosConstruccion() {
        BigDecimal cantidad = new BigDecimal("0.00");
        try {
            cantidad = (BigDecimal) em.createQuery("SELECT sum(a.avaluoConstruccion) FROM CatPredio a").getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public BigDecimal TotalesAvaluosPropiedad() {
        BigDecimal cantidad = new BigDecimal("0.00");
        try {
            cantidad = (BigDecimal) em.createQuery("SELECT sum(a.avaluoMunicipal) FROM CatPredio a").getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public BigInteger getMaxCatPredio() {
        BigInteger total = null;
        try {
            total = (BigInteger) em.createQuery("SELECT MAX(cp.numPredio) FROM CatPredio cp").getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return total;
    }

}
