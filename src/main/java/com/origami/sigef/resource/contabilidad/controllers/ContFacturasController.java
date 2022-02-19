/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.ats.entities.FacturaFormasPago;
import com.origami.sigef.ats.entities.PagoExterior;
import com.origami.sigef.ats.entities.PaisAts;
import com.origami.sigef.ats.entities.PaisParaisoFiscal;
import com.origami.sigef.ats.entities.SustentoComprobante;
import com.origami.sigef.ats.service.FacturaFormasPagoService;
import com.origami.sigef.ats.service.PagoExteriorService;
import com.origami.sigef.ats.service.PaisAtsService;
import com.origami.sigef.ats.service.PaisParaisoFiscalService;
import com.origami.sigef.ats.service.SustentoComprobanteService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContFacturaDetalle;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralService;
import com.origami.sigef.resource.contabilidad.services.ContFacturaDetalleService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.FormaPagoService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contFacturasView")
@ViewScoped
public class ContFacturasController implements Serializable {

    @Inject
    private FacturaService facturaService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ValoresService valService;
    @Inject
    private ContFacturaDetalleService contFacturaDetalleService;
    @Inject
    private PagoExteriorService pagoExteriorService;
    @Inject
    private PaisParaisoFiscalService paisParaisoFiscalService;
    @Inject
    private PaisAtsService paisAtsService;
    @Inject
    private FacturaFormasPagoService facturaFormasPagoService;
    @Inject
    private FormaPagoService formaPagoService;
    @Inject
    private SustentoComprobanteService sustentoComprobanteService;
    @Inject
    private ContDiarioGeneralService contDiarioGeneralService;

    private LazyModel<Proveedor> proveedorLazy;
    private LazyModel<Factura> facturaLazy;
    private LazyModel<CuentaContableRetencion> retencionesLazy;

    private List<ContFacturaDetalle> contFacturaDetalleList, deleteFacturaDetalle;
    private List<PaisAts> paises;
    private List<PaisParaisoFiscal> paisParaisoFiscal;
    private List<FacturaFormasPago> facturaFormasPago;
    private List<FormaPago> formasPagos;
    private List<SustentoComprobante> sustentosComprobantes;

    private Factura factura;
    private FormaPago formaPagoSelection;

    private double sumRenta, sumIva;

    private Boolean collapsed, disabledPago, disabledRegimenGeneral, disabledParaisoFiscal, disabledDenominacion, renderedBotones;

    @PostConstruct
    public void initialize() {
        facturaLazy = new LazyModel<>(Factura.class);
        facturaLazy.getSorteds().put("fechaFactura", "ASC");
        facturaLazy.getFilterss().put("estado", true);
        paises = paisAtsService.findByNamedQuery("PaisAts.findAll");
        paisParaisoFiscal = paisParaisoFiscalService.findByNamedQuery("PaisParaisoFiscal.findAll");
        formasPagos = formaPagoService.findAll();
        sustentosComprobantes = sustentoComprobanteService.findAll();
        closeForm();
        if (sustentosComprobantes != null && !sustentosComprobantes.isEmpty()) {
            factura.setCodSustento(sustentosComprobantes.get(1).getCodigo());
        }
        if (formasPagos != null && !formasPagos.isEmpty()) {
            FacturaFormasPago facturaFormaPago = new FacturaFormasPago(factura, formaPagoService.findByCodigo("20"));
            facturaFormasPago.add(facturaFormaPago);
        }
    }

    public void formatNumeroComprobante() {
        factura.setNumFactura(null);
        if (factura.getEntidad() != null && !factura.getEntidad().isEmpty()) {
            factura.setEntidad(StringUtils.leftPad(factura.getEntidad(), 3, "0"));
        }
        if (factura.getEstablecimiento() != null && !factura.getEstablecimiento().isEmpty()) {
            factura.setEstablecimiento(StringUtils.leftPad(factura.getEstablecimiento(), 3, "0"));
        }
        if (factura.getSecuencia() != null && !factura.getSecuencia().isEmpty()) {
            factura.setSecuencia(StringUtils.leftPad(factura.getSecuencia(), 9, "0"));
        }
    }

    public void openDlg(Factura factura) {
        collapsed = Boolean.FALSE;
        if (factura != null) {
            this.factura = factura;
            if (this.factura.getRetencionIva() == null) {
                this.factura.setRetencionIva(BigDecimal.ZERO);
            }
            if (this.factura.getRetencionRenta() == null) {
                this.factura.setRetencionRenta(BigDecimal.ZERO);
            }
            contFacturaDetalleList = contFacturaDetalleService.findByNamedQuery("ContFacturaDetalle.findByIdFactura", this.factura);
            loadDatosPago();
            loadDatosRegimenExterior();
            facturaFormasPago = facturaFormasPagoService.findAllFormaPagoByFactura(this.factura);
        } else {
            this.factura = new Factura();
        }
    }

    public void openDlgProveedor() {
        proveedorLazy = new LazyModel<>(Proveedor.class);
        proveedorLazy.getSorteds().put("cliente.nombre", "ASC");
        proveedorLazy.getFilterss().put("estado", true);
        proveedorLazy.setDistinct(false);
        PrimeFaces.current().executeScript("PF('dlgProveedor').show()");
        PrimeFaces.current().ajax().update("dataTableProveedor");
    }

    public void closeDlgProveedor(Proveedor proveedor) {
        this.factura.setProveedor(proveedor);
        PrimeFaces.current().executeScript("PF('dlgProveedor').hide()");
    }

    public void delete(Factura factura) {
        factura.setEstado(Boolean.FALSE);
        facturaService.edit(factura);
        JsfUtil.addSuccessMessage("INFO!!!", "Factura se elimino con éxito");
    }

    public void openDlgConfRetenciones() {
        if (factura.getBaseImponibleIva().doubleValue() <= 0 && factura.getBaseImponibleDiferente().doubleValue() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar Base imponible IVA 0% o  Base imponible IVA diferente de 0%");
            if (!contFacturaDetalleList.isEmpty()) {
                for (ContFacturaDetalle item : contFacturaDetalleList) {
                    item.setValorDetalle(BigDecimal.ZERO);
                    item.setValorBase(BigDecimal.ZERO);
                }
            }
            return;
        }
        retencionesLazy = new LazyModel<>(CuentaContableRetencion.class);
        retencionesLazy.getFilterss().put("estado", true);
        PrimeFaces.current().executeScript("PF('dlgRetenciones').show()");
        PrimeFaces.current().ajax().update("dataTableRetenciones");
    }

    public void closeDlgConfRetenciones(CuentaContableRetencion retencion) {
        ContFacturaDetalle detalle = new ContFacturaDetalle();
        detalle.setIdConfRetencion(retencion);
        contFacturaDetalleList.add(detalle);
        PrimeFaces.current().executeScript("PF('dlgRetenciones').hide()");
        PrimeFaces.current().ajax().update("retencionesTable");
    }

    public void calcularValorRubro(ContFacturaDetalle detalle) {
        double validador = validacion(Utils.redondearDosDecimales(detalle.getValorBase().doubleValue()));
        if (validador > 0) {
            double diferencia = validador - detalle.getValorBase().doubleValue();
            if (detalle.getIdConfRetencion().getRetencion().getRubroTipo().getCodigo().equals("1")) {
                if (diferencia > 0) {
                    detalle.setValorBase(factura.getSubtotalRenta().subtract(new BigDecimal(diferencia)));
                } else {
                    detalle.setValorBase(factura.getSubtotalRenta());
                }
            } else {
                if (diferencia > 0) {
                    detalle.setValorBase(factura.getMontoIva().subtract(new BigDecimal(diferencia)));
                } else {
                    detalle.setValorBase(factura.getMontoIva());
                }
            }
        }
        detalle.setValorDetalle(calcularRetencion(detalle.getIdConfRetencion().getRetencion().getValor(), detalle.getValorBase()));
        calcularValores();
        PrimeFaces.current().ajax().update("retencionesTable");
        PrimeFaces.current().ajax().update("retencionIva");
        PrimeFaces.current().ajax().update("retencionRenta");
    }

    public void calcularValores() {
        factura.setRetencionIva(BigDecimal.ZERO);
        factura.setRetencionRenta(BigDecimal.ZERO);
        for (ContFacturaDetalle detalle : contFacturaDetalleList) {
            if (detalle.getIdConfRetencion().getRetencion().getRubroTipo().getCodigo().equals("1")) {
                factura.setRetencionRenta(factura.getRetencionRenta().add(detalle.getValorDetalle().setScale(2, RoundingMode.HALF_UP)));//renta 
            } else {
                factura.setRetencionIva(factura.getRetencionIva().add(detalle.getValorDetalle().setScale(2, RoundingMode.HALF_UP)));//iva
            }
        }
    }

    public double validacion(double valor) {
        sumRenta = 0;
        sumIva = 0;
        for (ContFacturaDetalle item : contFacturaDetalleList) {
            if (item.getIdConfRetencion().getRetencion().getRubroTipo().getCodigo().equals("1")) {//renta
                sumRenta = sumRenta + Utils.redondearDosDecimales(item.getValorBase().doubleValue());
            } else {
                sumIva = sumIva + Utils.redondearDosDecimales(item.getValorBase().doubleValue());
            }
        }
        if (sumIva > Utils.redondearDosDecimales(factura.getMontoIva().doubleValue())) {
            return sumIva;
        } else if (sumRenta > Utils.redondearDosDecimales(factura.getSubtotalRenta().doubleValue())) {
            return sumIva;
        } else {
            return 0;
        }
    }

    private BigDecimal calcularRetencion(Double valorRubro, BigDecimal valor) {
        return new BigDecimal((valorRubro / 100) * valor.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    public void calcularSubTotal(Boolean accion) {
        factura.setSubtotalRenta(factura.getBaseImponibleIva().add(factura.getBaseImponibleDiferente()));
        if (accion) {
            calcularIva();
        }
    }

    public void calcularIva() {
        CONFIG.IVA = Integer.parseInt(valService.findByCodigo(Constantes.IVA));
        factura.setMontoIva(new BigDecimal((factura.getBaseImponibleDiferente().doubleValue() * CONFIG.IVA) / 100));
    }

    public void save() {
        //validar numero de factura
        if (factura.getEntidad() == null || factura.getEntidad().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el No. de la entidad");
            return;
        }
        if (factura.getEstablecimiento() == null || factura.getEstablecimiento().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el No. del establecimiento");
            return;
        }
        if (factura.getSecuencia() == null || factura.getSecuencia().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el No. de la secuencia");
            return;
        }
        //validar numero de autorizacion
        if (factura.getNumAutorizacion() == null || factura.getNumAutorizacion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el No. de la autorización");
            return;
        } else {
            if (factura.getNumAutorizacion().length() < 10) {
                JsfUtil.addWarningMessage("AVISO!!!", "El número de dígitos es menor a 10");
                return;
            } else {
                if (factura.getNumAutorizacion().length() < 47) {
                    JsfUtil.addWarningMessage("AVISO!!!", "El número de dígitos es menor a 47");
                    return;
                } else {
                    if (factura.getNumAutorizacion().length() < 49) {
                        JsfUtil.addWarningMessage("AVISO!!!", "El número de dígitos es menor a 49");
                        return;
                    } else if (factura.getNumAutorizacion().length() > 49) {
                        JsfUtil.addWarningMessage("AVISO!!!", "El número de dígitos es mayor a 49");
                        return;
                    }
                }
            }
        }
        if (factura.getCodSustento() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un sustento comprobante");
            return;
        }
        //validar fecha de emision
        if (factura.getFechaFactura() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar la fecha de emisión");
            return;
        }
        //validamos proveedor
        if (factura.getProveedor() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar el proveedor");
            return;
        }
        Boolean edit = factura.getId() != null;
        factura.setNumFactura(factura.getEntidad().concat("-").concat(factura.getEstablecimiento().concat("-")).concat(factura.getSecuencia()));
        savePagoExterior();
        if (edit) {
            if (factura.getIdConDiario() != null && !factura.getRetenida()) {
                factura.getIdConDiario().setRetenido(Boolean.FALSE);
                contDiarioGeneralService.edit(factura.getIdConDiario());
            }
            facturaService.edit(factura);
            //editar o crear detalle
            if (!contFacturaDetalleList.isEmpty()) {
                for (ContFacturaDetalle detalle : contFacturaDetalleList) {
                    if (detalle.getId() != null) {
                        contFacturaDetalleService.edit(detalle);
                    } else {
                        detalle.setIdFactura(factura);
                        contFacturaDetalleService.create(detalle);
                    }
                }
            }
            //elimina data
            if (!deleteFacturaDetalle.isEmpty()) {
                for (ContFacturaDetalle detalle : deleteFacturaDetalle) {
                    detalle.setEstado(Boolean.FALSE);
                    contFacturaDetalleService.edit(detalle);
                }
            }
            //agregamos formas de pago
            if (!facturaFormasPago.isEmpty()) {
                for (FacturaFormasPago formaPago : facturaFormasPago) {
                    if (formaPago.getId() != null) {
                        facturaFormasPagoService.edit(formaPago);
                    } else {
                        formaPago.setFactura(factura);
                        facturaFormasPagoService.create(formaPago);
                    }
                }
            }
        } else {
            //validar si ya existe
            if (facturaService.getvalidarFactura(factura.getProveedor(), factura.getFacturaElectronica(), factura.getNumFactura())) {
                JsfUtil.addWarningMessage("AVISO!!!", "La factura ingresada ya se encuentra registrada");
                return;
            }
            factura = facturaService.create(factura);
            //creamos detalle
            if (!contFacturaDetalleList.isEmpty()) {
                for (ContFacturaDetalle detalle : contFacturaDetalleList) {
                    detalle.setIdFactura(factura);
                    contFacturaDetalleService.create(detalle);
                }
            }
            if (!facturaFormasPago.isEmpty()) {
                for (FacturaFormasPago formaPago : facturaFormasPago) {
                    formaPago.setFactura(factura);
                    facturaFormasPagoService.create(formaPago);
                }
            }
        }
        JsfUtil.executeJS("PF('dataTableFactura').hide()");
        PrimeFaces.current().ajax().update("createEditPanel");
        PrimeFaces.current().ajax().update("dataTableFactura");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void savePagoExterior() {
        if (factura.getPagoExterior() != null) {
            if (factura.getPagoExterior().getId() != null) {
                pagoExteriorService.edit(factura.getPagoExterior());
            } else {
                pagoExteriorService.create(factura.getPagoExterior());
            }
        }
    }

    public void saveFormasPago(Factura factura) {
        if (facturaFormasPago.isEmpty()) {
            for (FacturaFormasPago pago : facturaFormasPago) {
                if (pago.getId() != null) {
                    facturaFormasPagoService.edit(pago);
                } else {
                    facturaFormasPagoService.create(pago);
                }
            }
        }
    }

    public void closeForm() {
        factura = new Factura();
        formaPagoSelection = new FormaPago();
        factura.setPagoExterior(new PagoExterior());
        contFacturaDetalleList = new ArrayList<>();
        deleteFacturaDetalle = new ArrayList<>();
        facturaFormasPago = new ArrayList<>();
        collapsed = Boolean.TRUE;
        disabledRegimenGeneral = Boolean.TRUE;
        disabledParaisoFiscal = Boolean.TRUE;
        disabledDenominacion = Boolean.TRUE;
        renderedBotones = Boolean.TRUE;
        disabledPago = Boolean.TRUE;
    }

    public void deleteDetalle(ContFacturaDetalle detalle, int index) {
        ContFacturaDetalle temp = detalle;
        if (detalle.getId() != null) {
            deleteFacturaDetalle.add(detalle);
            contFacturaDetalleList.remove(detalle);
        } else {
            temp = contFacturaDetalleList.get(index);
            contFacturaDetalleList.remove(index);
        }
        if (detalle.getIdConfRetencion().getRetencion().getRubroTipo().getCodigo().equals("1")) {//renta
            factura.setRetencionRenta(factura.getRetencionRenta().subtract(temp.getValorDetalle()).setScale(2, RoundingMode.HALF_UP));
        } else {//iva
            factura.setRetencionIva(factura.getRetencionIva().subtract(temp.getValorDetalle()).setScale(2, RoundingMode.HALF_UP));
        }
        PrimeFaces.current().ajax().update("retencionesTable");
        PrimeFaces.current().ajax().update("retencionIva");
        PrimeFaces.current().ajax().update("retencionRenta");
    }

    public void loadDatosPago() {
        if (factura.getPagoExterior() != null) {
            if (factura.getPagoExterior().getPagoLocExt() != null
                    && !factura.getPagoExterior().getPagoLocExt().isEmpty()) {
                switch (factura.getPagoExterior().getPagoLocExt()) {
                    case "01":
                        disabledPago = Boolean.TRUE;
                        disabledRegimenGeneral = Boolean.TRUE;
                        disabledParaisoFiscal = Boolean.TRUE;
                        disabledDenominacion = Boolean.TRUE;
                        factura.getPagoExterior().setTipoRegi(null);
                        factura.getPagoExterior().setPaisEfecPago(null);
                        factura.getPagoExterior().setPaisEfecPagoGen(null);
                        factura.getPagoExterior().setPaisEfecPagoParFis(null);
                        factura.getPagoExterior().setAplicConvDobTrib(null);
                        factura.getPagoExterior().setPagExtSujRetNorLeg(null);
                        break;
                    case "02":
                        disabledPago = Boolean.FALSE;
                        disabledRegimenGeneral = Boolean.TRUE;
                        disabledParaisoFiscal = Boolean.TRUE;
                        disabledDenominacion = Boolean.TRUE;
                        break;
                }
            }
        }
    }

    public void loadDatosRegimenExterior() {
        if (factura.getPagoExterior().getTipoRegi() != null && !factura.getPagoExterior().getTipoRegi().isEmpty()) {
            switch (factura.getPagoExterior().getTipoRegi()) {
                case "01":
                    disabledRegimenGeneral = Boolean.FALSE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.TRUE;
                    break;
                case "02":
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.FALSE;
                    disabledDenominacion = Boolean.TRUE;
                    break;
                case "03":
                    disabledRegimenGeneral = Boolean.TRUE;
                    disabledParaisoFiscal = Boolean.TRUE;
                    disabledDenominacion = Boolean.FALSE;
                    break;
            }
        }
    }

    public void aggFormasPago() {
        if (formaPagoSelection != null && formaPagoSelection.getId() != null) {
            FacturaFormasPago facturaFormaPago = new FacturaFormasPago(factura, formaPagoSelection);
            if (!facturaFormasPago.isEmpty()) {
                for (FacturaFormasPago pago : facturaFormasPago) {
                    if (facturaFormaPago.getFormaPago().getCodigo().equals(pago.getFormaPago().getCodigo())) {
                        JsfUtil.addErrorMessage("", "Ya selecciono el método de Pago");
                        return;
                    }
                }
            }
            facturaFormasPago.add(facturaFormaPago);
            JsfUtil.addSuccessMessage("", facturaFormaPago.getFormaPago().getDescripcion() + " agregado con éxito");
            formaPagoSelection = new FormaPago();
        }
    }

    public void eliminarFormasPago(FacturaFormasPago pago, int index) {
        if (pago.getId() != null) {
            facturaFormasPagoService.remove(pago);
            facturaFormasPago.remove(pago);
        } else {
            facturaFormasPago.remove(index);
        }
        JsfUtil.addSuccessMessage("", "Pago eliminado con éxito");
        JsfUtil.update("dataTableFormasPago");
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public LazyModel<Factura> getFacturaLazy() {
        return facturaLazy;
    }

    public void setFacturaLazy(LazyModel<Factura> facturaLazy) {
        this.facturaLazy = facturaLazy;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public List<ContFacturaDetalle> getContFacturaDetalleList() {
        return contFacturaDetalleList;
    }

    public void setContFacturaDetalleList(List<ContFacturaDetalle> contFacturaDetalleList) {
        this.contFacturaDetalleList = contFacturaDetalleList;
    }

    public LazyModel<CuentaContableRetencion> getRetencionesLazy() {
        return retencionesLazy;
    }

    public void setRetencionesLazy(LazyModel<CuentaContableRetencion> retencionesLazy) {
        this.retencionesLazy = retencionesLazy;
    }

    public Boolean getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

    public Boolean getDisabledPago() {
        return disabledPago;
    }

    public void setDisabledPago(Boolean disabledPago) {
        this.disabledPago = disabledPago;
    }

    public Boolean getDisabledRegimenGeneral() {
        return disabledRegimenGeneral;
    }

    public void setDisabledRegimenGeneral(Boolean disabledRegimenGeneral) {
        this.disabledRegimenGeneral = disabledRegimenGeneral;
    }

    public Boolean getDisabledParaisoFiscal() {
        return disabledParaisoFiscal;
    }

    public void setDisabledParaisoFiscal(Boolean disabledParaisoFiscal) {
        this.disabledParaisoFiscal = disabledParaisoFiscal;
    }

    public Boolean getDisabledDenominacion() {
        return disabledDenominacion;
    }

    public void setDisabledDenominacion(Boolean disabledDenominacion) {
        this.disabledDenominacion = disabledDenominacion;
    }

    public List<PaisAts> getPaises() {
        return paises;
    }

    public void setPaises(List<PaisAts> paises) {
        this.paises = paises;
    }

    public List<PaisParaisoFiscal> getPaisParaisoFiscal() {
        return paisParaisoFiscal;
    }

    public void setPaisParaisoFiscal(List<PaisParaisoFiscal> paisParaisoFiscal) {
        this.paisParaisoFiscal = paisParaisoFiscal;
    }

    public List<FacturaFormasPago> getFacturaFormasPago() {
        return facturaFormasPago;
    }

    public void setFacturaFormasPago(List<FacturaFormasPago> facturaFormasPago) {
        this.facturaFormasPago = facturaFormasPago;
    }

    public List<FormaPago> getFormasPagos() {
        return formasPagos;
    }

    public void setFormasPagos(List<FormaPago> formasPagos) {
        this.formasPagos = formasPagos;
    }

    public FormaPago getFormaPagoSelection() {
        return formaPagoSelection;
    }

    public void setFormaPagoSelection(FormaPago formaPagoSelection) {
        this.formaPagoSelection = formaPagoSelection;
    }

    public Boolean getRenderedBotones() {
        return renderedBotones;
    }

    public void setRenderedBotones(Boolean renderedBotones) {
        this.renderedBotones = renderedBotones;
    }

    public List<SustentoComprobante> getSustentosComprobantes() {
        return sustentosComprobantes;
    }

    public void setSustentosComprobantes(List<SustentoComprobante> sustentosComprobantes) {
        this.sustentosComprobantes = sustentosComprobantes;
    }

}
