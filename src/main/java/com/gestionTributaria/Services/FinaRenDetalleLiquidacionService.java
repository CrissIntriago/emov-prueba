/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
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
public class FinaRenDetalleLiquidacionService extends AbstractService<FinaRenDetLiquidacion> {

    private static final Logger LOG = Logger.getLogger(FinaRenDetalleLiquidacionService.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenDetalleLiquidacionService() {
        super(FinaRenDetLiquidacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    public static String getDetalleDeLiquidacion = "SELECT r FROM RenDetLiquidacion r WHERE r.liquidacion = :liquidacion ORDER BY r.rubro ASC";

    public List<FinaRenDetLiquidacion> findAllTipoLiquidacion(FinaRenLiquidacion liquidacion) {
        List<FinaRenDetLiquidacion> detalleliquidaciones = new ArrayList<>();
        try {
            detalleliquidaciones = em.createQuery("select a from FinaRenDetLiquidacion a where a.liquidacion.id =?1 order by a.rubro asc").setParameter(1, liquidacion.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en FinaRenDetalleLiquidacionService findAllTipoLiquidacion", ex);
            return detalleliquidaciones;
        }
        return detalleliquidaciones;
    }

    public List<FinaRenDetLiquidacion> findByLiquidacionOrderAsc(FinaRenLiquidacion liquidacion) {
        List<FinaRenDetLiquidacion> detalleLiquidacion = new ArrayList<>();
        try {
            detalleLiquidacion = em.createQuery("select d from FinaRenDetLiquidacion as d where d.liquidacion = ?1 order by d.rubro.id asc").setParameter(1, liquidacion).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en FinaRenDetalleLiquidacionService findByLiquidacionOrderAsc", ex);
            return detalleLiquidacion;
        }
        return detalleLiquidacion;
    }

    public FinaRenDetLiquidacion liquisSaldoTerreno(Long num) {
        //BigDecimal y = BigDecimal.ZERO;
        BigInteger num2 = new BigInteger(num.toString());
        System.out.println("entro a  liquisaldo---->>  " + num);
        FinaRenDetLiquidacion x = null;
        try {
//            num2 = Long.parseLong(num);
//            System.out.println("num2 es--->  " + num2);
            x = (FinaRenDetLiquidacion) em.createQuery("SELECT rl FROM FinaRenDetLiquidacion rl inner join rl.liquidacion l "
                    + "WHERE l.estadoLiquidacion.id = 1  and rl.rubro.id = 584 and l.codigoVerificado = false and l.numComprobante=:tipo").setParameter("tipo", num2).getSingleResult();
//                System.out.println("kldfkfd---->  "+x.getValor());                          
        } catch (Exception e) {
            System.out.println("e " + e);
            x = null;
        }
        return x;
    }

    public List<FinaRenDetLiquidacion> findByidLiquidacion(FinaRenLiquidacion liquidacion) {
        List<FinaRenDetLiquidacion> detalleLiquidacion = new ArrayList<>();
        try {
            detalleLiquidacion = em.createQuery("select d from FinaRenDetLiquidacion as d where d.liquidacion.id = ?1").setParameter(1, liquidacion.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en FinaRenDetalleLiquidacionService findByLiquidacionOrderAsc", ex);
            detalleLiquidacion = null;
        }
        return detalleLiquidacion;
    }

    public List<FinaRenLiquidacion> finbyLiquidacionByPredio(CatPredio p, int anioDesde, int anioHasta) {
        FinaRenLiquidacion l = new FinaRenLiquidacion();
        List<FinaRenLiquidacion> liq = new ArrayList<>();
        try {
            liq = (List<FinaRenLiquidacion>) em.createQuery("select a from FinaRenLiquidacion a where "
                    + "a.predio=?1 and a.estadoLiquidacion=2 and a.anio between ?2 and ?3").setParameter(1, p).setParameter(2, anioDesde).setParameter(3, anioHasta).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar la liquidacion por el id del predio", ex);
        }
        return liq;
    }
}
