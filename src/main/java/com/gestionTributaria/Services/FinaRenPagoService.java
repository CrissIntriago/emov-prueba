/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.asgard.Entity.FinaRenPagoRubro;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.models.CatPredioRuralDTO;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.ventanilla.Services.SolicitudServiciosService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
public class FinaRenPagoService extends AbstractService<FinaRenPago> {

    private static final Logger LOG = Logger.getLogger(FinaRenPagoService.class.getName());
    private static final long serialVersionUID = 1L;

    @javax.inject.Inject
    private ManagerService manager;
    @Inject
    private UserSession session;
    @Inject
    private ContRegistroContable registroContableService;

    @Inject
    private TmpService servicioBpmn;
    @Inject
    private SolicitudServiciosService solicitudServiciosService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public FinaRenPagoService() {
        super(FinaRenPago.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero, Boolean isSac) {
        Long numComprobante;
        if (pago.getNumComprobante() == null) {
            numComprobante = manager.getNumComprobante();
            pago.setNumComprobante(new BigInteger(numComprobante.toString()));
        }
        List<FinaRenPagoDetalle> detallePago;
        BigDecimal valorLiquidacion, pagoMinimo;
        FinaRenPagoRubro pagoRubro;
        BigDecimal valorRecaudacion;
        try {
            detallePago = (List<FinaRenPagoDetalle>) pago.getRenPagoDetalles();
            pago.setFechaPago(new Date());
            pago.setEstado(true);
            pago.setLiquidacion(liquidacion);
            pago.setCajero(cajero);
            pago.setDescuento(liquidacion.getDescuento());
            pago.setRecargo(liquidacion.getRecargo());
            pago.setContribuyente(liquidacion.getComprador());
            pago.setNombreContribuyente(liquidacion.getNombreComprador());
            pago.setInteres(liquidacion.getInteres());
            pago.setValorCoactiva(liquidacion.getValorCoactiva());
            if (session.getNameUser() != null) {
                pago.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
                pago.setIpUserSession(session.getIpClient());
            }
            pago = em.merge(pago);
            for (FinaRenPagoDetalle dp : detallePago) {
                dp.setPago(pago);
                em.merge(dp);
            }

            //pago minimo
            pagoMinimo = liquidacion.calculoMinimoPago();
            System.out.println("apago Minimo>>" + pagoMinimo);
            if (pago.getValor().compareTo(pagoMinimo) >= 0) {
                System.out.println("pago minimo realizado");
                /*Anyelo*/
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    liquidacion.setEstadoCoactiva(3);
                }
            }
            //pago para las liquidaciones
            liquidacion.setNumComprobante(pago.getNumComprobante());
            liquidacion.setSaldo(liquidacion.getSaldo().subtract(pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).subtract(pago.getValorCoactiva()).add(pago.getDescuento())));
            System.out.println("liquidacion--> " + liquidacion.getSaldo());
            if (liquidacion.getSaldo().compareTo(BigDecimal.ZERO) < 1) {
                liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(1L));
                liquidacion.setSaldo(BigDecimal.ZERO);
            }

            //VALOR DE LIQUIDACION
            valorLiquidacion = pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).subtract(pago.getValorCoactiva()).add(pago.getDescuento());
            System.out.println("valor de la Liquidacion>>" + valorLiquidacion);
            for (FinaRenDetLiquidacion rubro : liquidacion.getRenDetLiquidacionCollection()) {
                //RUBROS QUE ESTAN PENDIENTES DE RECAUDAR
                if (rubro != null) {
                    if (rubro.getValor() != null) {
                        if (!liquidacion.getTipoLiquidacion().getId().equals(13L)) {
                            rubro.setValorRecaudado(BigDecimal.ZERO.setScale(2));
                        }
                        if (rubro.getValor().compareTo(rubro.getValorRecaudado()) > 0) {
                            pagoRubro = new FinaRenPagoRubro();
                            BigDecimal valorRecaudar = rubro.getValor().subtract(rubro.getValorRecaudado());
                            if (valorLiquidacion.compareTo(valorRecaudar) >= 0) {//PAGO TOTAL DEL RUBRO / COMPLETA EL PAGO
                                //SE VERIFICA CUANTO SE VA A MENORAR DEL DINERO RECIBIDO
                                if (rubro.getValorRecaudado().compareTo(new BigDecimal("0.00")) <= 0) {
                                    valorRecaudacion = rubro.getValor();
                                } else {
                                    valorRecaudacion = rubro.getValor().subtract(rubro.getValorRecaudado());
                                }
                                rubro.setValorRecaudado(rubro.getValor());

                            } else {//PAGO PARCIAL DEL RUBRO
                                rubro.setValorRecaudado(rubro.getValorRecaudado().add(valorLiquidacion));
                                valorRecaudacion = valorLiquidacion;
                            }
                            rubro = em.merge(rubro);
                            //REGISTRO VALOR RECAUDADO POR RUBRO
                            pagoRubro.setPago(pago);
                            pagoRubro.setRubro(new FinaRenRubrosLiquidacion(rubro.getRubro().getId()));
                            pagoRubro.setValor(valorRecaudacion);
                            pagoRubro = em.merge(pagoRubro);
                            //ACTUALIZACION EN RUBROS MEJORAS
                            //REGISTRO DE RUBROS DE MEJORAS
                            valorLiquidacion = valorLiquidacion.subtract(valorRecaudar);
                            if (valorLiquidacion.compareTo(new BigDecimal("0.00")) <= 0) {
                                break;
                            }
                        }
                    }

                }
            }
            //ventanilla buscar el pago y luego actualizar la solicitud a pagada. 
            solicitudServiciosService.actualizarSolicitudPago(liquidacion);
            liquidacion = em.merge(liquidacion);
            servicioBpmn.culimnarProcesos(pago);
            // OBSERVACION DEL SALDO DE UNA LIQUIDACION
            if (liquidacion.getEstadoLiquidacion().getId() == 2L) {
                pago.setObservacion("Saldo: " + liquidacion.getSaldo() + ".");
                pago = em.merge(pago);
            }

            registroContableService.registroRecaudaciones(pago);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se registro el pago-->>>", e);
            return null;
        }
        return pago;
    }

    public List<FinaRenPago> getPagosByPredioTipoLiquidacionAnio(Long predio, CatPredioRuralDTO predioRural, FinaRenTipoLiquidacion tipo, Integer desde, Integer hasta) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            if (predio != null) {
                //patente 
                pagos = em.createQuery("select p from FinaRenPago p where p.estado= TRUE and (p.liquidacion.anio BETWEEN ?1 and ?2) and p.liquidacion.tipoLiquidacion.id = ?3 and p.liquidacion.predio.id= ?4 and p.liquidacion.estadoLiquidacion.id IN (1,2)").setParameter(1, desde)
                        .setParameter(2, hasta).setParameter(3, 2L).setParameter(4, predio).getResultList();
            }
            if (predioRural != null) {
                if (predioRural != null) {
                    pagos = em.createQuery("select p from FinaRenPago p where p.estado=true and (p.liquidacion.anio BETWEEN ?1 and ?2) and p.liquidacion.tipoLiquidacion.id=?3 and p.liquidacion.predioRustico=?4 and p.liquidacion.estadoLiquidacion.id in (1,2)").setParameter(1, desde)
                            .setParameter(2, hasta).setParameter(3, 3L).setParameter(4, predioRural.getPredioRustico().getId()).getResultList();
                }
                if (predioRural.getPredioRusctico2017() != null) {

                    pagos = em.createQuery("select a from FinaRenLiquidacion a where a.ruralExcel =?1 and a.anio=?2 and a.tipoLiquidacion.id=?3 and a.estadoLiquidacion.id in (1,2)").setParameter(1, predio).setParameter(2, desde).setParameter(3, 3L).getResultList();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eror en FinaRenaPagos getPagosByPredioTipoLiquidacionAnio", e);
            return pagos;
        }
        return pagos;
    }

    public List<FinaRenPago> getPagosByPredioTipoLiquidacionAnioPagada(Long predio, CatPredioRuralDTO predioRural, FinaRenTipoLiquidacion tipo, Integer desde, Integer hasta) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {//urbano
            if (predio != null) {
                pagos = em.createQuery("select a from FinaRenPago a where a.estado=TRUE and (a.liquidacion.anio between ?1 and ?2) and a.liquidacion.tipoLiquidacion.id=?3 and a.liquidacion.predio.id=?4 and a.liquidacion.estadoLiquidacion.id in (1)")
                        .setParameter(1, desde).setParameter(2, hasta).setParameter(3, 2L).setParameter(4, predio).getResultList();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eror en getPagosByPredioTipoLiquidacionAnioPagada", e);
            return pagos;
        }
        return pagos;
    }

    public List<FinaRenPago> obtenerPagos(FinaRenLiquidacion liquidacion, boolean estado) {
        List<FinaRenPago> renpagos = new ArrayList<>();
        try {
            renpagos = em.createQuery("select a from FinaRenPago a where a.estado=?1 and a.liquidacion.id=?2").setParameter(1, estado).setParameter(2, liquidacion.getId()).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Eror en obtenerPagos", e);
            return renpagos;
        }
        return renpagos;
    }

    public List<FinaRenPago> finAllPagoIndebido(FinaRenLiquidacion liquidacion) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            pagos = em.createQuery("Select a from FinaRenPago as a where a.liquidacion=?1 and a.estado=true and a.pagoIndebido=false").setParameter(1, liquidacion).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Eror en obtenerPagos finAllPagoIndebido", ex);
            return pagos;
        }
        return pagos;
    }

    public List<FinaRenPago> finAllPago(FinaRenLiquidacion liquidacion) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            pagos = (List<FinaRenPago>) em.createQuery("Select a from FinaRenPago as a where a.liquidacion.id =?1 and a.estado=true ").setParameter(1, liquidacion.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Eror en obtenerPagos finAllPagoIndebido", ex);
        }
        return pagos;
    }

    public List<FinaRenPago> getRenPagoByLiquidacion(FinaRenLiquidacion liqui) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT p FROM FinaRenPago p WHERE p.liquidacion = ?1 AND p.estado = TRUE AND p.pagoIndebido = FALSE ORDER BY p.numComprobante ASC")
                    .setParameter(1, liqui);
            pagos = (List<FinaRenPago>) query.getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return pagos;
    }

    public FinaRenPago getLiquidacionByFacturaPago(RenFactura fac) {
        try {
            FinaRenPago query = (FinaRenPago) em.createQuery("SELECT p FROM FinaRenPago p WHERE p.factura = ?1")
                    .setParameter(1, fac).getSingleResult();
            return query;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenDetLiquidacion getRubroLiquidacionByPrioridad(FinaRenTipoLiquidacion tipo, Long prioridad) {
        FinaRenDetLiquidacion rubro = new FinaRenDetLiquidacion();
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenDetLiquidacion r where r.rubro.codigoRubro = ?1 and r.liquidacion.tipoLiquidacion = ?2")
                    .setParameter(1, prioridad).setParameter(2, tipo);
            rubro = (FinaRenDetLiquidacion) query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return rubro;
    }

    public List<FinaRenPagoRubro> getRubrosbyPago(FinaRenPago pago, Boolean muni) {
        return (List<FinaRenPagoRubro>) em.createQuery("SELECT r FROM FinaRenPagoRubro r WHERE r.pago = ?1 AND r.rubro.rubroDelMunicipio = ?2")
                .setParameter(1, pago).setParameter(2, muni).getResultList();
    }

    public FinaRenPagoRubro getRubroByRubroAndPago(Long idRubro, Long idFinaRenPago) {
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenPagoRubro r WHERE r.rubro.id = ?1 AND r.pago.id = ?2")
                    .setParameter(1, idRubro).setParameter(2, idFinaRenPago);
            return (FinaRenPagoRubro) query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenPago getPagoByFactura(RenFactura fac) {
        try {
            return (FinaRenPago) em.createQuery("SELECT p FROM FinaRenPago p where p.factura = ?1 ")
                    .setParameter(1, fac).getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR en RENPAGOBYFACTURA", e);
            return null;
        }
    }

    public FinaRenPagoRubro getRubroByLiquidacionAndPagoAndRubro(Long idRubro, String codigoVerificador, String numComprobante) {
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenPagoRubro r JOIN r.rubro ru JOIN r.pago p JOIN p.liquidacion l WHERE ru.id= ?1 AND p.estado=true AND l.codigoVerificador=?2 AND p.numComprobante=?3")
                    .setParameter(1, idRubro).setParameter(2, codigoVerificador).setParameter(3, new BigInteger(numComprobante));
            return (FinaRenPagoRubro) query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public FinaRenPago getPagoByLiquidacionAndNumComprobante(Long idLiquidacion, String numComprobante) {
        try {
            Query query = em.createQuery("SELECT r FROM FinaRenPago r WHERE r.liquidacion.id = ?1 AND r.numComprobante = ?2 AND r.estado=true")
                    .setParameter(1, idLiquidacion).setParameter(2, numComprobante);
            return (FinaRenPago) query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<FinaRenPago> findByPagoLiquidacion(FinaRenLiquidacion liquidaciones) {
        List<FinaRenPago> pagos = new ArrayList<>();
        try {
            pagos = (List<FinaRenPago>) em.createQuery("Select a from FinaRenPago a where a.liquidacion.id=?1").setParameter(1, liquidaciones.getId()).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return pagos;
    }

    public FinaRenPagoDetalle findByFinaRenPago(FinaRenPago pago) {
        FinaRenPagoDetalle det = new FinaRenPagoDetalle();
        List<FinaRenPagoDetalle> detalle = new ArrayList<>();
        try {
            detalle = (List<FinaRenPagoDetalle>) em.createQuery("select a from FinaRenPagoDetalle a "
                    + "where a.pago.id=?1").setParameter(1, pago.getId()).getResultList();
            if (!detalle.isEmpty()) {
                det = detalle.get(0);
            } else {
                det = new FinaRenPagoDetalle();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return det;
    }

}
