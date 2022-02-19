/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.gestionTributaria.Services.TipoLiquidacionService;
import com.gestionTributaria.models.BusquedaPredios;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI2
 */
@Named
@ViewScoped
public class RecaudacionPredialMB extends BusquedaPredios implements Serializable {

    private static final Logger LOG = Logger.getLogger(RecaudacionPredialMB.class.getName());

    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private FinaRenPagoService pagoService;
    @Inject
    private InteresesService interesService;
    @Inject
    private RenRubroLiquidacionService rubroLiquidacionService;
    @Inject
    private TipoLiquidacionService tipoLiquidacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private UsuarioService userService;
    @Inject
    private CajeroService cajeroService;

    private List<FinaRenLiquidacion> emisionesPredialesTemp;
    protected List<FinaRenLiquidacion> emisionesACobrar;
    protected FinaRenLiquidacion emisionSeleccionada;
    private Boolean renderContextMenu;
    private FinaRenPago pagoCoactiva;
    private Cajero cajero;

    private Boolean esPagoCuota, esPagoCuotaCoactiva;
    protected List<NotasCredito> listPagoNota;
    protected Boolean variosPagos;
    private List<Banco> bancos;
    private List<Banco> tarjetas;

    @PostConstruct
    public void init() {
        renderContextMenu = Boolean.FALSE;
        emisionesPredialesTemp = new ArrayList<>();
        cajero = cajeroService.findByCajero(userSession.getNameUser());
    }

    public void seleccionarEmision(Long tipo) {
        System.out.println("Entro a emision...");
        if (emisionesPredialesTemp == null || emisionesPredialesTemp.isEmpty()) {
            calculoTotalPago(getEmisionesPrediales(), null);
        } else {
            emisionesACobrar = new ArrayList<>();
            emisionesACobrar.clear();
            for (FinaRenLiquidacion liq : emisionesPredialesTemp) {
                for (FinaRenLiquidacion l : liquidacionService.getEmisionesByPredio(liq)) {
                    if (l != null) {
                        emisionesACobrar.add(l);
                    }
                }
            }
            if (emisionesPredialesTemp.size() == 1) {
                this.setEmisionSeleccionada(new FinaRenLiquidacion());
                this.setEmisionSeleccionada(emisionesPredialesTemp.get(0));
                //System.out.println("EMISION SELECCIONADA  " + emisionSeleccionada);
                renderContextMenu = true;
            }
            if (emisionesPredialesTemp.size() != 1) {
                this.setEmisionSeleccionada(null);
                renderContextMenu = false;
            }

            //elimina valores repetidos
            Set<FinaRenLiquidacion> liquidacionCobro = new HashSet<>();
            liquidacionCobro.addAll(emisionesACobrar);
            emisionesACobrar.clear();
            emisionesACobrar.addAll(liquidacionCobro);
            calculoTotalPago(emisionesACobrar, null);
            this.getModelPago().setValorCobrar(totalEmisiones);
            this.getModelPago().setValorSaldoPagoFinal(totalEmisiones);
            JsfUtil.update("mainForm");

        }
    }

    public void procesarPago() {
        try {
            if (Utils.isNotEmpty(emisionesPrediales)) {
                if (Utils.isNotEmpty(emisionesACobrar)) {
                    for (FinaRenLiquidacion l : emisionesPredialesTemp) {
                        if (l.getAnio() > Utils.getAnio(new Date())) {
                            JsfUtil.addWarningMessage("Error", "Solo se puede realizar pago de emisión hasta el año actual.");
                            return;
                        }
                    }

                    emisionesACobrar = new ArrayList<>();
                    for (FinaRenLiquidacion liq : emisionesPredialesTemp) {
                        if (liq.getTipoLiquidacion().getId() == 2L) {
                            if (liq != null) {
                                emisionesACobrar.add(liq);
                                /*TRAIGO LAS NOTAS DE CREDITO*/
//                                listPagoNota = manager.findAll(NotasCredito.class);
//                                System.out.println("Cantidad A : " + listPagoNota.size());
                            }
                        }
                        if (liq.getTipoLiquidacion().getId() == 3L) {
                            if (liq != null) {
                                emisionesACobrar.add(liq);
                            }
                        }
                    }
                    if (emisionesACobrar != null && !emisionesACobrar.isEmpty()) {
                        calculoTotalPago(emisionesACobrar, null);
                        JsfUtil.update("formProcesar");
                        JsfUtil.executeJS("PF('dlgProcesar').show();");
                    } else {
                        JsfUtil.addWarningMessage("Mensaje", "No posee deuda.");

                    }
                } else {
                    JsfUtil.addWarningMessage("Mensaje", "Debe seleccionar la emision a cancelar.");
                }
            } else {
                JsfUtil.addWarningMessage("Mensaje", "Debe realizar la Busqueda.");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarValorPago() {
        calculoTotalPago(emisionesACobrar, getModelPago().getPagoTransferencia().getFecha());
    }

    public void realizarPago() {
        List<FinaRenPago> pagos = new ArrayList<>();
        FinaRenPago pago;
        pagoCoactiva = null;
        try {
            System.out.println("valor model--> " + getModelPago().getValorTotal());
            System.out.println("valor Emision--> " + getTotalEmisiones());
            if (getModelPago().getValorTotal().compareTo(getTotalEmisiones()) >= 0) {
                if (getModelPago().getValorTotal().compareTo(BigDecimal.ZERO.ZERO) > 0) {
                    for (FinaRenLiquidacion l : emisionesACobrar) {
                        l.calcularPago();
                        if (l.calculoMinimoPago(getModelPago().getValorTotal())) {
                            break;
                        }
                        FinaRenLiquidacion temp = null;
                        if (l.getEstadoCoactiva() != null && l.getEstadoCoactiva() == 2) {
                            if (l.getValorCoactiva().doubleValue() == 0) {
                                l.calcularPago();
                            }
//                            temp = liquidacionService.realizarUnPagoCoactiva(l, l.getValorCoactiva(), userSession.getNameUser());
//                            if (temp != null) {
//                                temp.setPagoFinal(temp.getTotalPago());
//                                pagoCoactiva = pagoService.realizarPago(l, getModelPago().realizarPago(temp), cajero, true);
//                            }
                        }
                        pago = pagoService.realizarPago(l, getModelPago().realizarPago(l), cajero, true);
                        if (temp != null) {
                            temp.setNumLiquidacion(new BigInteger(l.getTipoLiquidacion().getId().toString()));
                            liquidacionService.edit(temp);
                        }
                        if (pago != null) {
                            pagos.add(pago);
                        }
                        if (pagos.isEmpty()) {
                            JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor al Recargo+Interes");
                        }
                    }
                } else {
                    JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
                if (!pagos.isEmpty()) {
                    setPagoRealizado(Boolean.TRUE);
                }
            } else {
                JsfUtil.addWarningMessage("Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor al de la Recaudación");
            }
            this.consultarEmisiones();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<FinaRenLiquidacion> getEmisionesPredialesTemp() {
        return emisionesPredialesTemp;
    }

    public void setEmisionesPredialesTemp(List<FinaRenLiquidacion> emisionesPredialesTemp) {
        this.emisionesPredialesTemp = emisionesPredialesTemp;
    }

    public FinaRenLiquidacion getEmisionSeleccionada() {
        return emisionSeleccionada;
    }

    public void setEmisionSeleccionada(FinaRenLiquidacion emisionSeleccionada) {
        this.emisionSeleccionada = emisionSeleccionada;
    }

    public List<FinaRenLiquidacion> getEmisionesACobrar() {
        return emisionesACobrar;
    }

    public void setEmisionesACobrar(List<FinaRenLiquidacion> emisionesACobrar) {
        this.emisionesACobrar = emisionesACobrar;
    }

    public Boolean getRenderContextMenu() {
        return renderContextMenu;
    }

    public void setRenderContextMenu(Boolean renderContextMenu) {
        this.renderContextMenu = renderContextMenu;
    }

    public List<NotasCredito> getListPagoNota() {
        return listPagoNota;
    }

    public void setListPagoNota(List<NotasCredito> listPagoNota) {
        this.listPagoNota = listPagoNota;
    }

    public Boolean getVariosPagos() {
        return variosPagos;
    }

    public void setVariosPagos(Boolean variosPagos) {
        this.variosPagos = variosPagos;
    }

    public List<Banco> getBancos() {
        return bancos;
    }

    public void setBancos(List<Banco> bancos) {
        this.bancos = bancos;
    }

    public List<Banco> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Banco> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public Boolean getEsPagoCuota() {
        return esPagoCuota;
    }

    public void setEsPagoCuota(Boolean esPagoCuota) {
        this.esPagoCuota = esPagoCuota;
    }

    public Boolean getEsPagoCuotaCoactiva() {
        return esPagoCuotaCoactiva;
    }

    public void setEsPagoCuotaCoactiva(Boolean esPagoCuotaCoactiva) {
        this.esPagoCuotaCoactiva = esPagoCuotaCoactiva;
    }

}
