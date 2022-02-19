/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.service;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.DetalleCorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.entities.LiquidacionTipo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CorteOrdenCobroService extends AbstractService<CorteOrdenCobro> {

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CorteOrdenCobroService() {
        super(CorteOrdenCobro.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Integer getCantReg() {
        Query query = em.createQuery("SELECT COUNT(c) FROM CorteOrdenCobro c WHERE c.estado = TRUE AND c.codigo is not null");
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public List<CorteOrdenCobro> listaOrdenCobroEmitidas() {
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.estado = TRUE AND c.codigo IS NOT NULL ORDER BY c.fechaCorte");
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        return result;
    }

    public List<CorteOrdenCobro> listaOrdenCobroEmitidasProcess(Long numTramite) {
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.estado = TRUE AND c.numTramite = ?1 ORDER BY c.fechaCorte")
                .setParameter(1, numTramite);
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        return result;
    }

    public List<CorteOrdenCobro> getListaOrdenCobro(String tipo) {
        List<CorteOrdenCobro> listaCorte = new ArrayList<>();
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.tipoCorte = ?1 AND c.estado = TRUE AND c.fechaCorte = (SELECT MAX(ct.fechaCorte) FROM CorteOrdenCobro ct where ct.estado = true)")
                .setParameter(1, tipo);
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        List<DetalleCorteOrdenCobro> lista = new ArrayList<>();
        if (result != null) {
            for (CorteOrdenCobro c : result) {
                lista = listaDetalleOrdenCobro(c);
                if (!lista.isEmpty()) {
                    listaCorte.add(c);
                }
            }
        }
        return listaCorte;
    }
    public List<CorteOrdenCobro> getListaOrdenCobro(String tipo,Short periodo) {
        List<CorteOrdenCobro> listaCorte = new ArrayList<>();
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.tipoCorte = ?1 AND c.estado = TRUE AND c.fechaCorte = (SELECT MAX(ct.fechaCorte) FROM CorteOrdenCobro ct where ct.estado = true AND ct.periodo=?2)")
                .setParameter(1, tipo)
                .setParameter(2, periodo);
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        List<DetalleCorteOrdenCobro> lista = new ArrayList<>();
        if (result != null) {
            for (CorteOrdenCobro c : result) {
                lista = listaDetalleOrdenCobro(c);
                if (!lista.isEmpty()) {
                    listaCorte.add(c);
                }
            }
        }
        return listaCorte;
    }

    public List<CorteOrdenCobro> getListaOrdenCobro() {
        List<CorteOrdenCobro> listaCorte = new ArrayList<>();
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.estado = TRUE AND c.fechaCorte = (SELECT MAX(ct.fechaCorte) FROM CorteOrdenCobro ct where ct.estado = true)");
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        List<DetalleCorteOrdenCobro> lista = new ArrayList<>();
        if (result != null) {
            for (CorteOrdenCobro c : result) {
                lista = listaDetalleOrdenCobro(c);
                if (!lista.isEmpty()) {
                    listaCorte.add(c);
                }
            }
        }
        return listaCorte;
    }

    public List<CorteOrdenCobro> getListaOrdenCobros() {
        List<CorteOrdenCobro> listaCorte = new ArrayList<>();
        Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.estado = TRUE AND c.estadoCorte = 'EMITIDO' ORDER BY c.id DESC");
        List<CorteOrdenCobro> result = (List<CorteOrdenCobro>) query.getResultList();
        List<DetalleCorteOrdenCobro> lista = new ArrayList<>();
        if (result != null) {
            for (CorteOrdenCobro c : result) {
                lista = listaDetalleOrdenCobro(c);
                if (!lista.isEmpty()) {
                    listaCorte.add(c);
                }
            }
        }
        return listaCorte;
    }

    public CorteOrdenCobro getCorteOrdenByNumTramite(Long numTramite) {
        try {
            Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.numTramite = :num AND c.estado = TRUE")
                    .setParameter("num", numTramite);
            return (CorteOrdenCobro) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<DetalleCorteOrdenCobro> listaDetalleOrdenCobro(CorteOrdenCobro corte) {
        List<DetalleCorteOrdenCobro> lista = new ArrayList<>();
        Query query = em.createQuery("SELECT d FROM DetalleCorteOrdenCobro d WHERE d.corteOrdenCobro = ?1 ORDER BY d.id_banco")
                .setParameter(1, corte);
        lista = (List<DetalleCorteOrdenCobro>) query.getResultList();
        return lista;
    }

    public CorteOrdenCobro getOrdenFechaMax(String tipo,Short periodo) {
        try {
            Query query = em.createQuery("SELECT c FROM CorteOrdenCobro c WHERE c.tipoCorte = ?1 AND c.estado = TRUE AND c.periodo = ?2 "
                    + "AND c.fechaCorte = (SELECT MAX(ct.fechaCorte) FROM CorteOrdenCobro ct where ct.estado = true AND ct.periodo = ?2)")
                    .setParameter(1, tipo)
                    .setParameter(2, periodo);
            CorteOrdenCobro result = (CorteOrdenCobro) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int getRestablecerValoresEmisiones(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE tesoreria.corte_orden_cobro SET contabilizado_cobro_caja = false WHERE diario_general_cobro_caja = ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        return query.executeUpdate();
    }

    public int getRestablecerValoresCobroCaja(DiarioGeneral diarioGeneral) {
        Query query = getEntityManager().createNativeQuery("UPDATE tesoreria.corte_orden_cobro SET contabilizado_emision = false WHERE diario_general_emision= ?1")
                .setParameter(1, diarioGeneral.getNumeroTransaccion());
        return query.executeUpdate();
    }

    public List<LiquidacionDetalle> getDetalleLiquidacion(Date fechaEmision, Date fechaRegMax, LiquidacionTipo tipo) {
        try {
            String sql = "";
            if (fechaRegMax != null) {
                sql = "SELECT dl FROM LiquidacionDetalle dl INNER JOIN dl.liquidacion lq WHERE lq.estadoWs = 'RECIBIDA;AUTORIZADO' AND lq.fechaEmision <= ?1 AND lq.fechaEmision >= ?2 AND dl.itemTarifario IS NOT NULL AND lq.estadoVerificado = FALSE AND lq.tipoLiquidacion = ?3";
            } else {
                sql = "SELECT dl FROM LiquidacionDetalle dl INNER JOIN dl.liquidacion lq WHERE lq.estadoWs = 'RECIBIDA;AUTORIZADO' AND lq.fechaEmision <= ?1 AND dl.itemTarifario IS NOT NULL AND lq.estadoVerificado = FALSE AND lq.tipoLiquidacion = ?3";
            }
            Query query = em.createQuery(sql)
                    .setParameter(1, fechaEmision).setParameter(2, fechaRegMax).setParameter(3, tipo);
            List<LiquidacionDetalle> result = (List<LiquidacionDetalle>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<BigDecimal> getValorTotal(Date fechaEmision, Date fechaRegMax, LiquidacionTipo tipo) {
        try {
            String sql = "";
            if (fechaRegMax != null) {
                sql = "SELECT sum(dl.valor) FROM LiquidacionDetalle dl INNER JOIN dl.liquidacion lq WHERE lq.estadoWs = 'RECIBIDA;AUTORIZADO' AND lq.fechaEmision <= ?1 AND lq.fechaEmision >= ?2 AND dl.itemTarifario IS NOT NULL AND lq.estadoVerificado = FALSE AND lq.tipoLiquidacion = ?3";
            } else {
                sql = "SELECT sum(dl.valor) FROM LiquidacionDetalle dl INNER JOIN dl.liquidacion lq WHERE lq.estadoWs = 'RECIBIDA;AUTORIZADO' AND lq.fechaEmision <= ?1 AND dl.itemTarifario IS NOT NULL AND lq.estadoVerificado = FALSE AND lq.tipoLiquidacion = ?3";
            }
            Query query = em.createQuery(sql)
                    .setParameter(1, fechaEmision).setParameter(2, fechaRegMax).setParameter(3, tipo);
            List<BigDecimal> result = (List<BigDecimal>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Liquidacion> getLiquidacionDetalle(LiquidacionTipo tipo) {
        try {
            Query query
                    = em.createQuery("SELECT lq FROM LiquidacionDetalle ld INNER JOIN ld.liquidacion lq WHERE lq.estadoWs = 'RECIBIDA;AUTORIZADO' AND lq.tipoLiquidacion = ?1 AND lq.estado = TRUE AND ld.itemTarifario IS NOT NULL AND lq.fechaEmision = (SELECT MAX(liq.fechaEmision) FROM Liquidacion liq)")
                            .setParameter(1, tipo);
            List<Liquidacion> result = (List<Liquidacion>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public LiquidacionTipo getTipoLiquidacion(String prefijo) {
        try {
            Query query = em.createQuery("SELECT lq FROM LiquidacionTipo lq where lq.prefijo = ?1")
                    .setParameter(1, prefijo);
            LiquidacionTipo result = (LiquidacionTipo) query.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
