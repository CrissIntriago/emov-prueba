/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenIntereses;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Entities.CtlgDescuentoEmision;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class InteresesService extends AbstractService<FinaRenIntereses> {

    private static final Logger LOG = Logger.getLogger(InteresesService.class.getName());
    private static final long serialVersionUID = 1L;
    @Inject
    private ManagerService manager;
    @Inject
    private RemisionInteresService interesService;
    @Inject
    private FinaRenPagoService pagoService;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public InteresesService() {
        super(FinaRenIntereses.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BigDecimal interesesCalculado(FinaRenLiquidacion emision, Date hasta) {
        try {
            BigDecimal interesValor = BigDecimal.ZERO;
            BigDecimal interes = this.interesAcumulado(emision, hasta);
            interesValor = interes.multiply(emision.getTotalPago() != null ? emision.getTotalPago() : BigDecimal.ZERO).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            return interesValor;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public BigDecimal interesAcumulado(FinaRenLiquidacion emision, Date hasta) {
        BigDecimal interes = BigDecimal.ZERO;
        RenParametrosInteresMulta parametrosInteresMulta = new RenParametrosInteresMulta();
        try {
            parametrosInteresMulta = (RenParametrosInteresMulta) em.createQuery("SELECT p FROM RenParametrosInteresMulta p WHERE p.tipoLiquidacion.id =:tipoLiquidacion AND p.tipo =:tipo")
                    .setParameter("tipoLiquidacion", emision.getTipoLiquidacion().getId()).setParameter("tipo", "I").getSingleResult();
        } catch (NoResultException e) {
            parametrosInteresMulta = null;
        }

        int anioActual = Utils.getAnio(new Date());
        int mesActual = Utils.getMes(new Date());
        int anioLiq = emision.getAnio();
        Calendar calendar = Calendar.getInstance();
        Date fechaInicio = calendar.getTime();
        Date fechafin = new Date();
        boolean calcular = true;
//        System.out.println("años de emision " + emision.getAnio() + " año actual " + anioActual);
//        if (parametrosInteresMulta != null) {
        if (emision.getAnio() < anioActual) {
//            calendar.set((emision.getAnio() + 1), (parametrosInteresMulta.getMes() - 1), parametrosInteresMulta.getDia(), 0, 0, 0);
            calendar.set(emision.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
            fechaInicio = calendar.getTime();
        } else {
            calcular = false;
        }
        if (calcular) {
            interes = this.getRenIntereses(fechafin, fechaInicio);
            if (interes == null) {
                interes = BigDecimal.ZERO;
            }
        }
        return interes;
    }

    public FinaRenLiquidacion realizarDescuentoRecargaInteresPredial(FinaRenLiquidacion emision, Date fechaPago) {
        try {
//            this.em.detach(emision);
            long endTime = 0;
            long totalSum = 0;
            long startTime = System.currentTimeMillis();
            Boolean aplicaRemision = Boolean.FALSE;
            Map<String, Object> paramt;
            CtlgDescuentoEmision descuento;
            Date fecha = new Date();
            if (fechaPago != null) {
                fecha = fechaPago;
            }
            Integer dia = Utils.getDateValues("D", fecha);
            Integer mes = Utils.getDateValues("M", fecha);
            Integer anio = Utils.getDateValues("Y", fecha);
            BigDecimal valorImpuesto = BigDecimal.ZERO;
            for (FinaRenDetLiquidacion rubro : emision.getRenDetLiquidacionCollection()) {
                if (rubro.getRubro() != null) {
                    if (rubro.getRubro().getCodigoRubro() != null && rubro.getRubro().getCodigoRubro().equals(1L)) {
                        valorImpuesto = rubro.getValor();
                        break;
                    }

                }
            }
//            valorImpuesto = emision.getValorRubroPrioritario();
            endTime = System.currentTimeMillis() - startTime;
            //imprime tiempo transcurrido en ms
            System.out.println("Despues de for " + endTime + " milisegundos.");
            //SE REALIZA UNA SOLO VEZ EL RECARGO O EL DESCUENTO
            totalSum = 0;
            startTime = System.currentTimeMillis();
            emision.setRecargo(new BigDecimal("0.00"));
            emision.setDescuento(new BigDecimal("0.00"));
//            this.em.detach(emision);
            if (Utils.isEmpty(pagoService.getRenPagoByLiquidacion(emision))) {
                paramt = new HashMap<>();
                if (mes + 1 < 7 && anio.equals(emision.getAnio())) {
                    //SE REALIZA DECUENTO - DEACUERDO AL MES Y QUINCENA ANTES DEL MES DE JULIO
                    descuento = (CtlgDescuentoEmision) em.createQuery("SELECT d FROM CtlgDescuentoEmision d WHERE d.numMes=:mes AND d.numQuincena=:quincena")
                            .setParameter("mes", mes + 1).setParameter("quincena", dia > 15 ? 2 : 1).getSingleResult();
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
            endTime = System.currentTimeMillis() - startTime;
            //imprime tiempo transcurrido en ms
            System.out.println("Despues de calcular intereses y recargos " + endTime + " milisegundos.");
            //INTERES EMISION PREDIAL AÑO VENCIDO
            emision.setInteres(new BigDecimal("0.00"));
            if (!aplicaRemision) {
                if (emision.getAnio() < anio) {
                    if (Utils.isEmpty(pagoService.getRenPagoByLiquidacion(emision))) {// CONSULTAR CON UN LSTADO
                        Calendar fechaInteres = Calendar.getInstance();
                        fechaInteres.set(emision.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
//                        System.out.println("Anio " + emision.getAnio() + " fechaInteres " + fechaInteres.getTime() + " fechaPago " + fecha);
                        emision.setInteres(this.generarInteres(emision.getSaldo(), fecha, fechaInteres.getTime()));
                    } else {
                        //CONSULTAR ULTIMO PAGO - SI EL ULTIMO PAGO FUE REALIZADO EN EL MISMO ANIO DE EMISION LA FECHA DE INTERES TB DESDE EL PRIMER DIA DE LA EMISION VENCDA
                        emision.setInteres(this.generarInteres(emision.getSaldo(), ((List<FinaRenPago>) emision.getRenPagoCollection()).get(emision.getRenPagoCollection().size() - 1).getFechaPago(), fechaPago));
                    }
                }
            }

            return emision;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public BigDecimal generarInteres(BigDecimal valor, Date fecha, Date fechaPago) {
        BigDecimal interes, a;
        BigDecimal interesValor = new BigDecimal("0.00");
        Calendar fechaHasta = Calendar.getInstance();
        if (fechaPago != null) {
            fechaHasta.setTime(fechaPago);
        }
        try {
            System.out.println("antes de generar los intereses >>>");
            interes = this.getRenIntereses(fecha, fechaPago);
            // a = (BigDecimal) manager.getNativeQuery(QuerysFinanciero.getInteresNativo, new Object[]{new SimpleDateFormat("dd-MM-YYYY").format(fecha), new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime())});
            if (interes != null) {
                if (valor != null) {
                    interesValor = interes.multiply(valor).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                JsfUtil.addWarningMessage("Error", "Verifique en el Mantenimiento de Intereses este agreado el procentaje de interes correspondiente al " + Utils.getAnio(fecha));
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return interesValor;
    }

    public BigDecimal getRenIntereses(Date fechafin, Date fechaInicio) {
        BigDecimal interes = BigDecimal.ZERO;
        long totalSum = 0;
        long startTime = System.currentTimeMillis();
        try {
            interes = (BigDecimal) em.createNativeQuery("SELECT sum(r.porcentaje) FROM asgard.fina_ren_intereses r WHERE \n"
                    + "( r.hasta <= TO_DATE(:fechafin,'DD-MM-YYYY') OR (r.hasta >=  TO_DATE(:fechafin,'DD-MM-YYYY') \n"
                    + "AND DATE_PART('month', r.hasta) = DATE_PART('month', TO_DATE(:fechafin,'DD-MM-YYYY')) \n"
                    + "AND DATE_PART('YEAR', r.hasta) <= DATE_PART('year', TO_DATE(:fechafin,'DD-MM-YYYY')))) \n"
                    + "AND r.id IN(SELECT id FROM asgard.fina_ren_intereses i WHERE i.desde >=  TO_DATE(:fechaini,'DD-MM-YYYY') \n"
                    + "OR (i.desde <=  TO_DATE(:fechaini,'DD-MM-YYYY') AND DATE_PART('month', i.desde) = DATE_PART('month', TO_DATE(:fechaini,'DD-MM-YYYY')) "
                    + "AND DATE_PART('YEAR', i.desde) >= DATE_PART('year', TO_DATE(:fechaini,'DD-MM-YYYY'))));")
                    .setParameter("fechaini", new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio))
                    .setParameter("fechafin", new SimpleDateFormat("dd-MM-YYYY").format(fechafin)).getSingleResult();
            long endTime = System.currentTimeMillis() - startTime;
            //imprime tiempo transcurrido en ms
            System.out.println(" intereses Duración " + endTime + " milisegundos.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return BigDecimal.ZERO;
        }
        return interes;
    }
}
