/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.gestionTributaria.Entities.CtlgDescuentoEmision;
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CoaJuicioService extends AbstractService<CoaJuicio> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    private ManagerService managerService;

    public CoaJuicioService() {
        super(CoaJuicio.class);
    }
    @Inject
    private CoaJuicioPredioServices coaJuicioPredioServices;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private FinaRenDetalleLiquidacionService finaRenDetalleService;
    @Inject
    private CoaJuicioService juicioService;
    @Inject
    private RenRubroLiquidacionService rubroService;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public HistoricoTramites saveJuicioCoactivoNuevo(HistoricoTramites ht, CoaJuicio juicio, List<FinaRenLiquidacion> list, FinaRenRubrosLiquidacion finaRenRubroLiquidacion) {
        CoaJuicioPredio det;
        try {
            TipoTramite tipo = (TipoTramite) em.createQuery("select e from TipoTramite e where e.estado = :estado and e.activitykey = :key")
                    .setParameter("estado", true).setParameter("key", "juicioCoactiva").getResultList();
            ht.setTipoTramite(tipo);
            //ht.set("Pendiente");
            ht.setFecha(new Date());
            ht.getTipoTramite().setDescripcion(tipo.getDescripcion());
//            ht.setNumTramite(sec.getSecuenciasTram("SGM"));
            ht = (HistoricoTramites) managerService.save(ht);

            juicio.setTramite(ht);
            juicio = (CoaJuicio) managerService.save(juicio);
            for (FinaRenLiquidacion l : list) {
                det = new CoaJuicioPredio();
                det.setJuicio(juicio);
                det.setAnioDeuda(l.getAnio());
                if (l.getPredio() != null) {
                    det.setPredio(l.getPredio());
                }
//                if (l.getCuenta() != null) {
//                    det.setCuenta(l.getCuenta());
//                }
                if (l.getConvenioPago() != null) {
                    det.setConvenioPago(l.getConvenioPago());
                }
                det.setValorDeuda(l.getTotalPago());

                det.setAbogadoJuicio(juicio.getAbogadoJuicio());
                det.setLiquidacion(l);
                coaJuicioPredioServices.create(det);
            }
//            ht.setCoaJuicio(juicio);

            /*EN ESTADO INICIAL LAS EMISIONES AUN NO ESTAN EN COACTIVA*/
            for (FinaRenLiquidacion l : list) {
                l.setCoactiva(Boolean.TRUE);
                l.setEstadoCoactiva(2);
                finaRenLiquidacionService.edit(l);
            }
        } catch (Exception e) {
            Logger.getLogger(CoaJuicio.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return ht;
    }

    public CoaJuicio saveJuicioCoactivoNuevo(CoaJuicio juicio, List<FinaRenLiquidacion> liquidaciones) {
        CoaJuicioPredio det;
        Double totalJuicio = 0.00;
        try {
            //creo el juicio
            juicio = (CoaJuicio) create(juicio);
            for (FinaRenLiquidacion l : liquidaciones) {
                anadirRubroLiquidacion(l);
                det = new CoaJuicioPredio();
                det.setJuicio(juicio);
                det.setAnioDeuda(l.getAnio());
                if (l.getPredio().getId() != null) {
                    det.setPredio(l.getPredio());
                }
                det.setValorDeuda(l.getTotalPago());
                det.setAbogadoJuicio(juicio.getAbogadoJuicio());
                det.setLiquidacion(l);
                det.setEstado(Boolean.TRUE);
                coaJuicioPredioServices.create(det);
            }
            //cambio totas las liquidaciones de estado
            for (FinaRenLiquidacion l : liquidaciones) {
                l.setCoactiva(Boolean.TRUE);
                l.setEstadoCoactiva(2);
                l.calcularPagoConCoactiva();
                finaRenLiquidacionService.edit(l);
                totalJuicio = totalJuicio + l.getPagoFinal().doubleValue();
            }
            juicio.setTotalDeuda(BigDecimal.valueOf(totalJuicio));
            juicioService.edit(juicio);
            //añadimos el rubro
        } catch (Exception e) {
            System.out.println("Mensaje: " + e.getMessage());
            return null;
        }
        return juicio;
    }

    public void anadirRubroLiquidacion(FinaRenLiquidacion liquidacionSeleccionada) {
        FinaRenDetLiquidacion detalle = new FinaRenDetLiquidacion();

        //rubro urbano
        FinaRenRubrosLiquidacion rubroUrbano = rubroService.FindByDescripcion("CITACION POR BOLETA (URBANO)");
        //rubro boleta
        FinaRenRubrosLiquidacion rubroRural = rubroService.FindByDescripcion("CITACION POR BOLETA (RURAL)");
        FinaRenRubrosLiquidacion diferenciaXTasaResolucion = rubroService.FindByDescripcion("DIFERENCIA DE TASA ORDENANZA COACTIVA");
        //urbano
        if (liquidacionSeleccionada.getPredio().getTipoPredio().equals("U")) {
            detalle.setEstado(Boolean.TRUE);
            detalle.setLiquidacion(liquidacionSeleccionada);
            detalle.setRubro(rubroUrbano);
            detalle.setValor(rubroUrbano.getValor());
            detalle.setValorRecaudado(BigDecimal.ZERO);
            finaRenDetalleService.create(detalle);
            //"CITACION POR BOLETA (URBANO)"
            detalle = new FinaRenDetLiquidacion();
            detalle.setEstado(Boolean.TRUE);
            detalle.setLiquidacion(liquidacionSeleccionada);
            detalle.setRubro(diferenciaXTasaResolucion);
            detalle.setValor(diferenciaXTasaResolucion.getValor());
            detalle.setValorRecaudado(BigDecimal.ZERO);
            finaRenDetalleService.create(detalle);

//
            liquidacionSeleccionada.setTotalPago(liquidacionSeleccionada.getTotalPago().add(rubroUrbano.getValor()).add(diferenciaXTasaResolucion.getValor()));
            liquidacionSeleccionada.setSaldo(liquidacionSeleccionada.getSaldo().add(rubroUrbano.getValor()).add(diferenciaXTasaResolucion.getValor()));
        } else {
            //rural
            detalle.setEstado(Boolean.TRUE);
            detalle.setLiquidacion(liquidacionSeleccionada);
            detalle.setRubro(rubroRural);
            detalle.setValor(rubroRural.getValor());
            detalle.setValorRecaudado(BigDecimal.ZERO);
            finaRenDetalleService.create(detalle);
            detalle = new FinaRenDetLiquidacion();
            detalle.setEstado(Boolean.TRUE);
            detalle.setLiquidacion(liquidacionSeleccionada);
            detalle.setRubro(diferenciaXTasaResolucion);
            detalle.setValor(diferenciaXTasaResolucion.getValor());
            detalle.setValorRecaudado(BigDecimal.ZERO);
            finaRenDetalleService.create(detalle);
            liquidacionSeleccionada.setSaldo(liquidacionSeleccionada.getSaldo().add(rubroRural.getValor()).add(diferenciaXTasaResolucion.getValor()));
            liquidacionSeleccionada.setTotalPago(liquidacionSeleccionada.getTotalPago().add(rubroRural.getValor()).add(diferenciaXTasaResolucion.getValor()));
//            liquidacionSeleccionada.setSaldo(liquidacionSeleccionada.getSaldo().add(rubroRural.getValor()).add(diferenciaXTasaResolucion.getValor()));

        }

    }

    public Boolean consultaJuicioByNumeroYanio(Integer numero, Integer tiempo) {
        try {
            List<CoaJuicio> juicio;
            if (numero == null) {
                numero = 0;
            }
            juicio = (List<CoaJuicio>) em.createQuery("SELECT a FROM CoaJuicio a where a.anioJuicio = :anioJuicio"
                    + " and a.numeroJuicio = : numeroJuicio").setParameter("anioJuicio", tiempo)
                    .setParameter("numeroJuicio", numero).getResultList();
            return juicio.size() > 0;
        } catch (Exception e) {
            System.out.println("Mensaje: " + e.getMessage());
            return true;
        }
    }

    public Boolean guardarJuicioCoactivoAntiguo(CoaJuicio juicio, List<FinaRenLiquidacion> list) {
        CoaJuicioPredio det;
        try {
            juicio = (CoaJuicio) managerService.save(juicio);;
            for (FinaRenLiquidacion l : list) {
                det = new CoaJuicioPredio();
                det.setJuicio(juicio);
                det.setAbogadoJuicio(juicio.getAbogadoJuicio());
//                det.setLiquidacion(l);
                managerService.save(l);
            }

            for (FinaRenLiquidacion l : list) {
                l.setCoactiva(Boolean.TRUE);
                //l.setEstadoCoactiva(3);
                managerService.save(l);
                System.out.println("estado liquidacion coactiva --> " + l.getCoactiva());
            }
        } catch (Exception e) {
            System.out.println("Mensaje: " + e.getMessage());
            return false;
        }
        return true;
    }

    public FinaRenLiquidacion realizarDescuentoRecargaInteresPredial(FinaRenLiquidacion emision, Date fechaPago) {
        Boolean aplicaRemision = managerService.aplicaRemision(emision);
        Map<String, Object> paramt;
        CtlgDescuentoEmision descuento;
        FinaRenRubrosLiquidacion rubroLiquidacion;
        Date fecha = new Date();
        if (fechaPago != null) {
            fecha = fechaPago;
        }
        Integer dia = Utils.getDateValues("D", fecha);
        Integer mes = Utils.getDateValues("M", fecha);
        Integer anio = Utils.getDateValues("Y", fecha);
        BigDecimal valorImpuesto = BigDecimal.ZERO;
        try {
            //UNA EMISION SIEMPRE TIENE EL RUBRO DE IMPUESTO PREDIAL
            for (FinaRenDetLiquidacion rubro : emision.getFinaRenDetLiquidacionList()) {
                rubroLiquidacion = (FinaRenRubrosLiquidacion) managerService.find(FinaRenRubrosLiquidacion.class, rubro.getRubro());
                if (rubroLiquidacion != null) {
                    if (rubroLiquidacion.getCodigoRubro() != null && rubroLiquidacion.getCodigoRubro().equals(1L)) {
                        valorImpuesto = rubro.getValor();
                        break;
                    }

                }
            }
            //SE REALIZA UNA SOLO VEZ EL RECARGO O EL DESCUENTO
            emision.setRecargo(new BigDecimal("0.00"));
            emision.setDescuento(new BigDecimal("0.00"));
            if (emision.getRenPagoCollection() == null || emision.getRenPagoCollection().isEmpty()) {
                paramt = new HashMap<>();
                if (mes + 1 < 7 && anio.equals(emision.getAnio())) {
                    //SE REALIZA DECUENTO - DEACUERDO AL MES Y QUINCENA ANTES DEL MES DE JULIO
                    Object[] parametros = new Object[]{mes + 1, dia > 15 ? 2 : 1};
//                    "SELECT d FROM CtlgDescuentoEmision d WHERE d.numMes=:mes AND d.numQuincena=:quincena"
                    descuento = (CtlgDescuentoEmision) managerService.findNativeQuery("SELECT d FROM CtlgDescuentoEmision d WHERE d.numMes=?1 AND d.numQuincena=?2", parametros);
                    emision.setDescuento(valorImpuesto.multiply(descuento.getPorcentaje()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
                } else {
                    // SE REALIZA RECARGO - DESPUES DE JUNIO 10% DEL IMPUESTO
                    if (!aplicaRemision) {
                        if (Objects.equals(emision.getAnio(), Utils.getAnio(new Date()))) {
                            emision.setRecargo(valorImpuesto.multiply(new BigDecimal("10")).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
                        }
                    }

                }
            }
            //INTERES EMISION PREDIAL AÑO VENCIDO
            emision.setInteres(new BigDecimal("0.00"));
            if (!aplicaRemision) {
                if (emision.getAnio() < anio) {
                    if (emision.getRenPagoCollection() == null || emision.getRenPagoCollection().isEmpty()) {// CONSULTAR CON UN LSTADO
                        Calendar fechaInteres = Calendar.getInstance();
                        fechaInteres.set(emision.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
                        emision.setInteres(managerService.generarInteres(emision.getSaldo(), fechaInteres.getTime(), fechaPago));

                    } else {
                        //CONSULTAR ULTIMO PAGO - SI EL ULTIMO PAGO FUE REALIZADO EN EL MISMO ANIO DE EMISION LA FECHA DE INTERES TB DESDE EL PRIMER DIA DE LA EMISION VENCDA
                        emision.setInteres(managerService.generarInteres(emision.getSaldo(), ((List<FinaRenPago>) emision.getRenPagoCollection()).get(emision.getRenPagoCollection().size() - 1).getFechaPago(), fechaPago));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return emision;
    }

    public List<FinaRenLiquidacion> getEmisionesCoactivaAntigua(CatPredioModel predio) {
        try {
            Calendar fecha = Calendar.getInstance();
            Object[] objeto = new Object[]{predio, fecha.get(Calendar.YEAR)};
            String query = "SELECT e FROM RenLiquidacion e WHERE (e.estadoLiquidacion = 1 or e.estadoLiquidacion = 2) AND e.predio=:?1 AND e.anio<:?2 AND e NOT IN (SELECT jp.liquidacion FROM CoaJuicioPredio jp WHERE jp.liquidacion.predio=:?1 AND jp.estado=TRUE) ORDER BY e.anio ASC";
            return managerService.findNativeQueryList(FinaRenLiquidacion.class, query, objeto);
        } catch (Exception e) {
            Logger.getLogger(CoaJuicioService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public CoaJuicio findByNumeroTramite(HistoricoTramites tramite) {
        CoaJuicio juicio = null;
        try {
            juicio = (CoaJuicio) em.createQuery("Select e from CoaJuicio e where e.tramite.id=?1 and e.estado=true").setParameter(1, tramite.getId()).getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioService.class.getName()).log(Level.SEVERE, "Error al encontrar el tramite", ex);
            juicio = null;
        }
        return juicio;
    }

    public List<CoaJuicio> findByJuicioConvenio() {
        List<CoaJuicio> listaJuicioConvenio = new ArrayList<>();
        try {
            listaJuicioConvenio = (List<CoaJuicio>) em.createQuery("select a from CoaJuicio a where a.estadoJuicio.abreviatura=?1").setParameter(1, "CDP").getResultList();
        } catch (Exception ex) {
            Logger.getLogger(CoaJuicioService.class.getName()).log(Level.SEVERE, "Error al encontrar juicio con convenio de pago", ex);
        }
        return listaJuicioConvenio;
    }
}
