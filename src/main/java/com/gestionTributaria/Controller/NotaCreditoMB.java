/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.common.entities.Cliente;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.gestionTributaria.Entities.NotaDetalle;
import com.gestionTributaria.Entities.NotasCredito;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.NotaCreditoServices;
import com.gestionTributaria.Services.NotaDetalleService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class NotaCreditoMB implements Serializable {

    private List<CatalogoItem> tipoNotas = new ArrayList<>();
    private CatalogoItem tipoNota;
    private NotaDetalle notaDetalle;
    private NotasCredito notaCredito;
    private LazyModel<FinaRenLiquidacion> liquidaciones;
    private FinaRenLiquidacion liquidacion;
    private Boolean pagoIndebido;
    private List<FinaRenPago> listaCobros;
    private FinaRenPago cobros;
    private String ciRuc;
    private LazyModel<Cliente> solicitantes;
    @Inject
    private ManagerService managerService;
    @Inject
    private NotaCreditoServices creditoService;
    @Inject
    private UserSession session;
    @Inject
    private NotaDetalleService notaDetalleService;
    private Cliente ente;
    private LazyModel<NotasCredito> notasCredito;

    @PostConstruct
    public void initView() {
        try {
            listaCobros = new ArrayList<>();
            cobros = new FinaRenPago();
            pagoIndebido = Boolean.TRUE;
            solicitantes = new LazyModel<>(Cliente.class);
            liquidacion = new FinaRenLiquidacion();
            tipoNota = new CatalogoItem();
            notaCredito = new NotasCredito();
            liquidaciones = new LazyModel<>(FinaRenLiquidacion.class);
            notasCredito = new LazyModel<>(NotasCredito.class);
            tipoNotas = managerService.findAllEasy("Select a from CatalogoItem a where a.catalogo.codigo='tipoNotaCredito'");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void mostrarDialogoLiquidaciones() {
        JsfUtil.executeJS("PF('dlgLiquidaciones').show()");
    }

    public void openDlgConstribuyentes() {
        JsfUtil.executeJS("PF('dlgSolicitante').show();");
    }

    public void actualizarDatos() {
        JsfUtil.update(":mainForm");
        JsfUtil.executeJS("PF('dlgLiquidaciones').hide()");
    }

    public void seleccionarEnte() {
        if (this.ente == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar un solicitante del listado");
        } else {
            this.ciRuc = this.ente.getIdentificacion();
            notaCredito.setContribuyente(ente);
            notaCredito.setValor(BigDecimal.ZERO);
            notaCredito.setValorPagado(BigDecimal.ZERO);
            notaCredito.setSaldo(BigDecimal.ZERO);

            JsfUtil.update("frmSolicitante");
            JsfUtil.update("registroNotadeCredito");
            JsfUtil.addInformationMessage("Mensaje", "Contribuyente seleccionado.");
            JsfUtil.executeJS("PF('dlgSolicitante').hide();");
        }
    }

    public void seleccionar() {
        BigDecimal total = new BigDecimal("0");
        if (listaCobros.isEmpty()) { //iba el nombre del comprador pero la base esta null
            JsfUtil.addWarningMessage("Advertencia", "Debe seleccionar una liquidacion del listado");
        } else {
            ciRuc = listaCobros.get(0).getLiquidacion().getComprador().getIdentificacion() == null ? listaCobros.get(0).getLiquidacion().getComprador().getRuc() : listaCobros.get(0).getLiquidacion().getComprador().getIdentificacion();
            notaCredito.setEstado(Short.parseShort("0"));
            notaCredito.setContribuyente(listaCobros.get(0).getLiquidacion().getComprador());
            notaCredito.setValor(BigDecimal.ZERO);
            for (FinaRenPago a : listaCobros) {
                total = total.add(a.getValor());
            }
            notaCredito.setValorPagado(total);
            notaCredito.setSaldo(total);
            cobros = listaCobros.get(0);
            JsfUtil.update("frmLiquidacionesPagadas");
            JsfUtil.update("registroNotadeCredito");
            JsfUtil.addInformationMessage("Mensaje", "Contribuyente seleccionado.");
            JsfUtil.executeJS("PF('dlgLiquidaciones').hide();");
        }
    }

    public void guardar() {
        try {
            if (notaCredito.getId() == null) {
                notaCredito.setFechaIngreso(new Date());
                notaCredito.setUsuarioIngreso(session.getNameUser());
            }
            notaCredito.setTipoNota(BigInteger.valueOf(tipoNota.getId()));

            if (notaCredito.getContribuyente() == null) {
                JsfUtil.addInformationMessage("Info", "Debe seleccionar el Contribuyente para poder generar la Nota de credito.");
                return;
            }
            if (notaCredito.getObservacion().isEmpty()) {
                JsfUtil.addWarningMessage("Info", "Debe ingresar una observacion");
                return;
            }
            notaCredito = (NotasCredito) creditoService.create(notaCredito);
            for (FinaRenPago pagos : listaCobros) {
                notaDetalle = new NotaDetalle();
                notaDetalle.setIdNota(notaCredito);
                notaDetalle.setFechaIngreso(new Date());
                notaDetalle.setLiquidacion(pagos.getLiquidacion());
                notaDetalle.setUsuarioCreacion(session.getNameUser());
                notaDetalle.setComprobante(pagos.getNumComprobante().toString());
                notaDetalle.setFechaPago(pagos.getFechaPago());
                notaDetalle.setValor(pagos.getValor());
                notaDetalleService.create(notaDetalle);
            }
            if (notaCredito.getId() != null) {
                JsfUtil.addInformationMessage("Info", "Datos Grabados Correctamente: ");
//                this.imprimirReporte(notaCredito);
            } else {
                JsfUtil.addErrorMessage("Info", "Error al Registrar la Nota de Credito. Inténtelo nuevamente");
            }
            notaCredito = new NotasCredito();
            clearData();
            listaCobros.clear();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Info", "Error al Registrar la Nota de Credito. Inténtelo nuevamente");
        }
    }

    public void checkMayor() {
        if (pagoIndebido) {
            if (notaCredito.getValorPagado().compareTo(notaCredito.getValor()) < 0) {
                JsfUtil.addWarningMessage("Mensaje", "El pago real debe ser menor o igual al valor cobrado.");
                notaCredito.setValor(BigDecimal.ZERO);
                notaCredito.setSaldo(notaCredito.getValorPagado());
            } else {
                notaCredito.setSaldo(notaCredito.getValorPagado().subtract(notaCredito.getValor()));
            }
        }
    }

    private void clearData() {
        notaCredito.setLiquidacion(null);
        notaCredito.setContribuyente(null);
        notaCredito.setValor(BigDecimal.ZERO);
        notaCredito.setValorPagado(BigDecimal.ZERO);
        notaCredito.setSaldo(BigDecimal.ZERO);
        notaCredito.setCodigo(null);
        ciRuc = null;
    }

    public void changeTipoNota() {
        pagoIndebido = !pagoIndebido;
        ente = null;
        liquidacion = null;
        clearData();

    }

    public void pagoSeleccionado(SelectEvent objeto) {
        FinaRenPago pago = (FinaRenPago) objeto.getObject();
        if (pago.getLiquidacion().getComprador() == null) {
            JsfUtil.addWarningMessage("Mensaje", "El pago seleccionado no posee un comprador");
            listaCobros.remove(pago);
            JsfUtil.update("frmLiquidacionesPagadas");
        } else {
            if (listaCobros.size() > 1) {
                if ((listaCobros.get(0).getNombreContribuyente().equals(pago.getNombreContribuyente())) == false) {
                    JsfUtil.addWarningMessage("Error", "Debe ser el mismo contribuyente");
                    listaCobros.remove(pago);
                    JsfUtil.update("frmLiquidacionesPagadas");
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET AND SET">
    public List<CatalogoItem> getTipoNotas() {
        return tipoNotas;
    }

    public void setTipoNotas(List<CatalogoItem> tipoNotas) {
        this.tipoNotas = tipoNotas;
    }

    public Boolean getPagoIndebido() {
        return pagoIndebido;
    }

    public void setPagoIndebido(Boolean pagoIndebido) {
        this.pagoIndebido = pagoIndebido;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public CatalogoItem getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(CatalogoItem tipoNota) {
        this.tipoNota = tipoNota;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(LazyModel<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public ManagerService getManagerService() {
        return managerService;
    }

    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    public NotasCredito getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(NotasCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    public boolean isPagoIndebido() {
        return pagoIndebido;
    }

    public void setPagoIndebido(boolean pagoIndebido) {
        this.pagoIndebido = pagoIndebido;
    }

    public FinaRenPago getCobros() {
        return cobros;
    }

    public void setCobros(FinaRenPago cobros) {
        this.cobros = cobros;
    }

    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public LazyModel<NotasCredito> getNotasCredito() {
        return notasCredito;
    }

    public void setNotasCredito(LazyModel<NotasCredito> notasCredito) {
        this.notasCredito = notasCredito;
    }

    public List<FinaRenPago> getListaCobros() {
        return listaCobros;
    }

    public void setListaCobros(List<FinaRenPago> listaCobros) {
        this.listaCobros = listaCobros;
    }

    public NotaCreditoServices getCreditoService() {
        return creditoService;
    }

    public void setCreditoService(NotaCreditoServices creditoService) {
        this.creditoService = creditoService;
    }
//</editor-fold>

}
