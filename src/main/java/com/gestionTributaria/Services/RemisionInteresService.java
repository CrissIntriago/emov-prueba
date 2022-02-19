/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnExoneracionLiquidaciones;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnSolicitudExoneraciones;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class RemisionInteresService extends AbstractService<FnExoneracionLiquidaciones> {

    @Inject
    private ManagerService services;
    @Inject
    private UserSession us;

    private static final Logger LOG = Logger.getLogger(RemisionInteresService.class.getName());

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public RemisionInteresService() {
        super(FnExoneracionLiquidaciones.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Boolean aplicaRemision(FinaRenLiquidacion liquidacion) {
        Boolean aplicaRemision = Boolean.FALSE;
        try {
            FnExoneracionLiquidaciones result = (FnExoneracionLiquidaciones) em.createQuery("SELECT f FROM FnExoneracionLiquidaciones f WHERE f.liquidacion = ?1 and f.estado = true and f.exoneracion.estado.id = 1")
                    .setParameter(1, liquidacion).getResultList().stream().findFirst().orElse(null);
            if (result != null) {
                if (new Date().before(result.getExoneracion().getFechaPagoMaximo())) {
                    aplicaRemision = Boolean.TRUE;
                }
            } else {
                aplicaRemision = Boolean.FALSE;
            }
            return aplicaRemision;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return Boolean.FALSE;
        }

    }
    
     public List<FinaRenLiquidacion> liquidacionesPermitidas(List<FinaRenLiquidacion> renLiquidacions) throws ParseException {
        List<FinaRenLiquidacion> liquidacions = new ArrayList<>();
        Date fechaMaximaRemision = new SimpleDateFormat("dd-MM-yyyy").parse(SisVars.fechaMaximaRemisionInteres);
        Date periodoLectura;
        String periodo = "";
        //Lectura l = null;
        for (FinaRenLiquidacion rl : renLiquidacions) {
            switch (rl.getTipoLiquidacion().getId().intValue()) {
                case 13:
                    if (rl.getAnio() <= 2017) {
                        liquidacions.add(rl);
                    }
                    break;
//                case 275:
//                    l = (Lectura) manager.find(QuerysAguaPotable.getLecturasByLiquidacion, new String[]{"liquidacion"}, new Object[]{rl.getId()});
//                    periodo = "30-" + l.getMes().toString() + "-" + l.getAnio().toString();
//                    periodoLectura = new SimpleDateFormat("dd-MM-yyyy").parse(periodo);
//                    if (periodoLectura.before(fechaMaximaRemision)) {
//                        liquidacions.add(rl);
//                    }
//                    break;

                default:
                    if (rl.getAnio() <= 2017) {
                        liquidacions.add(rl);
                    }
                    break;
            }
        }

        return liquidacions;
    }

    public FnSolicitudExoneraciones saveFnRemisionSolicitudProceso(List<FinaRenLiquidacion> liquidaciones) {
        try {
            liquidaciones = liquidacionesPermitidas(liquidaciones);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        BigDecimal valorInteres = BigDecimal.ZERO;
        BigDecimal valorRecargo = BigDecimal.ZERO;
        BigDecimal valorMulta = BigDecimal.ZERO;
        BigDecimal valorTotalPago = BigDecimal.ZERO;
        BigDecimal valorTotalRemision = BigDecimal.ZERO;

        FnSolicitudExoneraciones fnRemisionSolicitud;
        FnExoneracionLiquidaciones fnRemisionLiquidacion;

        List<FnExoneracionLiquidaciones> fnRemisionInteresLiquidacions;
        try {

            fnRemisionInteresLiquidacions = new ArrayList<>();
            fnRemisionLiquidacion = null;
            for (FinaRenLiquidacion rl : liquidaciones) {
                fnRemisionLiquidacion = new FnExoneracionLiquidaciones();
                fnRemisionLiquidacion.setEstado(Boolean.FALSE);
                fnRemisionLiquidacion.setFechaAplicacion(new Date());
                fnRemisionLiquidacion.setUsuarioAplicacion(new Usuarios(us.getUserId()));
                fnRemisionLiquidacion.setLiquidacion(rl);
                fnRemisionLiquidacion.setInteres(rl.getInteres());
                fnRemisionLiquidacion.setMultas(BigDecimal.ZERO);
                fnRemisionLiquidacion.setRecargo(rl.getRecargo());
                fnRemisionInteresLiquidacions.add(fnRemisionLiquidacion);
                valorInteres = valorInteres.add(rl.getInteres());
                valorRecargo = valorRecargo.add(rl.getRecargo());
                valorTotalPago = valorTotalPago.add(rl.getTotalPago());
            }
            valorTotalRemision = valorInteres.add(valorRecargo);
            fnRemisionSolicitud = new FnSolicitudExoneraciones();
            if (liquidaciones.get(liquidaciones.size() - 1).getComprador() != null) {
                fnRemisionSolicitud.setSolicitante(liquidaciones.get(liquidaciones.size() - 1).getComprador());
            }
            fnRemisionSolicitud.setMultas(valorMulta);
            fnRemisionSolicitud.setInteres(valorInteres);
            fnRemisionSolicitud.setRecargo(valorRecargo);
            fnRemisionSolicitud.setTotalRemision(valorTotalRemision);
            fnRemisionSolicitud.setTotalPago(valorTotalPago);
            fnRemisionSolicitud.setFechaAprobacion(new Date());
            fnRemisionSolicitud.setEstado(new FnEstadoExoneracion(2L));

            Date fechaPagoMaximo = new SimpleDateFormat("dd-MM-yyyy").parse(SisVars.fechaPublicacionOrdenanza);

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaPagoMaximo);
            cal.add(Calendar.MONTH, 3);

            fechaPagoMaximo = cal.getTime();

            CatalogoItem tipoSolicitud = null;

            if (Utils.isNotEmpty(fnRemisionInteresLiquidacions)) {
                FinaRenLiquidacion liquidacion = fnRemisionInteresLiquidacions.get(0).getLiquidacion();
                ///VERIFICA A QUE TIPO SE LE  DARA LA  REMISION : CUENTAS  -  PREDIO - LIQUIDACION EN GENERAL
                if (liquidacion != null) {//CUENTA
//                    if (liquidacion.getCuenta() != null) {
//                        tipoSolicitud = (CatalogoItem) services.find(Querys.getCtlgItemByCatalogoCodeName,
//                                new String[]{"catalogo", "codename"}, new Object[]{"solicitud.remision_interes", "sacdap"});
//                    } else {//
//                        if (liquidacion.getPredio() != null) {
//                            tipoSolicitud = (CatalogoItem) services.find(Querys.getCtlgItemByCatalogoCodeName,
//                                    new String[]{"catalogo", "codename"}, new Object[]{"solicitud.remision_interes", "sap"});
//                        } else {
//                            tipoSolicitud = (CatalogoItem) services.find(Querys.getCtlgItemByCatalogoCodeName,
//                                    new String[]{"catalogo", "codename"}, new Object[]{"solicitud.remision_interes", "sal"});
//                        }
//                    }
                }
            }
            fnRemisionSolicitud.setTramiteTipo(tipoSolicitud);
            fnRemisionSolicitud.setFechaIngreso(new Date());
            fnRemisionSolicitud.setUsuarioCreacion(new Usuarios(us.getUserId()));
            fnRemisionSolicitud.setFechaPagoMaximo(fechaPagoMaximo);
            fnRemisionSolicitud.setExoneracionTipo(new FnExoneracionTipo(60L));

            fnRemisionSolicitud = (FnSolicitudExoneraciones) services.save(fnRemisionSolicitud);

            for (FnExoneracionLiquidaciones remisionLiquidacion : fnRemisionInteresLiquidacions) {
                remisionLiquidacion.setExoneracion(fnRemisionSolicitud);
                services.save(remisionLiquidacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fnRemisionSolicitud;

    }

}
