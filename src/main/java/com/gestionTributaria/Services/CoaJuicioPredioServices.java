/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaEstadoJuicio;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
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
public class CoaJuicioPredioServices extends AbstractService<CoaJuicioPredio> {

    private static final Logger LOG = Logger.getLogger(CoaJuicioPredioServices.class.getName());
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CoaJuicioPredioServices() {
        super(CoaJuicioPredio.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CoaJuicioPredio> findByIdJuicio(Long juicio) {
        List<CoaJuicioPredio> predioJuicios = null;
        try {
            predioJuicios = (List<CoaJuicioPredio>) em.createQuery("select e from CoaJuicioPredio e where e.juicio.id=?1 and e.estado=true").setParameter(1, juicio).getResultList();
            for (CoaJuicioPredio juicioItem : predioJuicios) {
                juicioItem.getLiquidacion().calcularPago();
                juicioItem.getLiquidacion().calcularCoactiva();
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "erroe al encontrar los datos de los predios en juicio coactivo", ex);
            return null;
        }
        return predioJuicios;
    }

    public List<CatPredio> fynByPredioJuicio(Long juicio) {
        List<CatPredio> predios = null;
        try {
            predios = (List<CatPredio>) em.createQuery("select e.predio.id from CoaJuicioPredio e where e.juicio.id=?1 GROUP BY e.predio").setParameter(1, juicio).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "erroe al encontrar los datos de los predios en juicio coactivo", ex);
            return null;
        }
        return predios;
    }

    public CoaEstadoJuicio findByEstado(String abrev) {
        CoaEstadoJuicio estado = new CoaEstadoJuicio();
        try {
            estado = (CoaEstadoJuicio) em.createQuery("Select a from CoaEstadoJuicio a where a.abreviatura=?1 and a.estado=true").setParameter(1, abrev).getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al encontrar el estado", ex);
            return null;
        }
        return estado;
    }

    public List<CoaJuicio> antecedentesJudiciales(CoaJuicio juicio) {
        List<CoaJuicio> juicios = new ArrayList<>();
        CoaJuicioPredio juicioPredio = new CoaJuicioPredio();
        List<CoaJuicioPredio> listaCoaJuicioPredio = new ArrayList<>();
        try {
            listaCoaJuicioPredio = (List<CoaJuicioPredio>) em.createQuery("select a from CoaJuicioPredio a where a.juicio.id=?1").setParameter(1, juicio.getId()).getResultList();
            if (!listaCoaJuicioPredio.isEmpty()) {
                juicioPredio = listaCoaJuicioPredio.get(0);
                //predios
                if (juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() == 2 || juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() == 3) {
                    juicios = (List<CoaJuicio>) em.createQuery("select DISTINCT a.juicio from CoaJuicioPredio a where a.predio.id=?1 order by a.juicio.anioJuicio desc").setParameter(1, juicioPredio.getPredio().getId()).getResultList();
                }
                //PATENTES
                if (juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() == 9) {
                    juicios = (List<CoaJuicio>) em.createQuery("select DISTINCT a.juicio from CoaJuicioPredio a where a.liquidacion=?1 order by a.juicio.anioJuicio desc").setParameter(1, juicioPredio.getLiquidacion()).getResultList();
                }
                //otras liquidaciones
                if (juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() != 2 && juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() != 3 && juicioPredio.getLiquidacion().getTipoLiquidacion().getId().intValue() != 9) {
                    juicios = (List<CoaJuicio>) em.createQuery("select DISTINCT a.juicio from CoaJuicioPredio a where a.liquidacion=?1 order by a.juicio.anioJuicio desc").setParameter(1, juicioPredio.getLiquidacion()).getResultList();
                }
            } else {
                juicios = new ArrayList<>();
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar antecedentes Judiciales", ex);
            return null;
        }
        return juicios;
    }

    public CoaJuicioPredio findCoaJuicioPredio(CoaJuicio juicio) {
        List<CoaJuicioPredio> listaCoaJuicio = new ArrayList<>();
        try {
            listaCoaJuicio = (List<CoaJuicioPredio>) em.createQuery("select a from CoaJuicioPredio a where a.juicio.id=?1").setParameter(1, juicio.getId()).getResultList();
            if (listaCoaJuicio.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar CoaJuiciopredio", ex);
        }
        return listaCoaJuicio.get(0);
    }

    public List<CoaJuicioPredio> findCoaJuiciosPredios(CoaJuicio juicio) {
        List<CoaJuicioPredio> juiciosPredios = new ArrayList<>();
        try {
            juiciosPredios = (List<CoaJuicioPredio>) em.createQuery("select a from CoaJuicioPredio a where a.juicio.id=?1").setParameter(1, juicio.getId()).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar CoaJuiciopredio", ex);
        }
        return juiciosPredios;
    }

    public List<FinaRenLiquidacion> findHistorialPagos(CoaJuicioPredio juicioPredio) {
        List<FinaRenLiquidacion> pagos = new ArrayList<>();
        try {
            if (juicioPredio != null) {
                pagos = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio.id=?1 order by a.anio desc").setParameter(1, juicioPredio.getPredio().getId()).getResultList();
            } else {
                pagos = new ArrayList<>();
            }
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar historial pagos", ex);
        }
        return pagos;
    }

    public List<CoaJuicio> findByPredio(CatPredio predio) {
        List<CoaJuicio> juicios = new ArrayList<>();
        try {
            juicios = (List<CoaJuicio>) em.createQuery("select DISTINCT a.juicio from CoaJuicioPredio a where a.predio=?1 and a.juicio.estadoJuicio=6")
                    .setParameter(1, predio).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar juicios", ex);
        }
        return juicios;
    }

    public List<FinaRenLiquidacion> HistorialPagos(CatPredio predio) {
        List<FinaRenLiquidacion> pagos = new ArrayList<>();
        try {
            pagos = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where a.predio=?1 and a.estadoLiquidacion=2").setParameter(1, predio).getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al procesar juicios", ex);
        }
        return pagos;
    }

    public List<CoaJuicio> findByJuicioPredios(CatPredio predio) {
        List<CoaJuicio> listasJuicios = new ArrayList<>();
        try {
            listasJuicios = (List<CoaJuicio>) em.createQuery("select DISTINCT a.juicio from CoaJuicioPredio a where a.predio.id =?1 and a.estadoJuicio.abreviatura = ?2"
                    + "order by a.anioDeuda asc").setParameter(1, predio.getId()).setParameter(2, "CDP").getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioPredio.class.getName()).log(Level.SEVERE, "Error al traer juicios por predio", ex);
        }
        return listasJuicios;
    }
}
