/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroPago;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.services.ContBeneficiarioComprobantePagoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contComprobantePagoView")
@ViewScoped
public class ContComprobantePagoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private ContRegistroPago ContRegistroPago;
    @Inject
    private UserSession userSession;
    @Inject
    private ContBeneficiarioComprobantePagoService beneficiariosService;

    private ContComprobantePago contComprobantePago;
    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private PartePresupuestariaModel partePresupuestariaModel;
    private ContBeneficiarioComprobantePago beneficiario;

    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleListDelete;
    private List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagos;
    private List<Short> listaPeriodo;
    private List<PartePresupuestariaModel> partePresupuestariaModelList;
    private List<ContDiarioGeneral> contDiarioGeneralList;

    private LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel;
    private LazyModel<ContComprobantePago> contComprobantePagosLazyModel;

    private BigDecimal totalDebe, totalHaber, totalBeneficiario, totalEjecutado;

    private Integer idDiario;

    private Boolean view;

    private String observacionAnulacion;

    @PostConstruct
    public void initialize() {
        limpiar(false);
        loadLazyModel();
        loadDataRedirect();
        userSession.setIdDiario(null);
        contCuentaEntidadLazyModel = new LazyModel<>(ContCuentaEntidad.class);
        contCuentaEntidadLazyModel.getSorteds().put("id", "ASC");
        contCuentaEntidadLazyModel.getFilterss().put("estado", true);
        contCuentaEntidadLazyModel.getFilterss().put("tipoCuenta", true);
        calcularTotales();
    }

    public void loadDataRedirect() {
        if (userSession.getIdComprobante() != null) {
            contComprobantePago = ContRegistroPago.findById(userSession.getIdComprobante());
            List<ContDiarioGeneralDetalle> tempList = ContRegistroPago.findByIdComprobantePago(contComprobantePago);
            contDiarioGeneralDetalleList = new ArrayList<>();
            beneficiarioComprobantePagos = new ArrayList<>();
            for (ContDiarioGeneralDetalle temp : tempList) {
                contDiarioGeneralDetalleList.add(temp);
            }
            List<ContBeneficiarioComprobantePago> temp1List = ContRegistroPago.findByIdBeneficiaciosPagos(contComprobantePago);
            for (ContBeneficiarioComprobantePago temp : temp1List) {
                beneficiarioComprobantePagos.add(temp);
            }
            view = Utils.clone(userSession.getViewDiario());
            userSession.setIdComprobante(null);
            userSession.setViewComprobante(true);
        }
    }

    public void limpiar(Boolean accion) {
        opcionBusqueda = new OpcionBusqueda();
        contComprobantePago = new ContComprobantePago();
        listaPeriodo = catalogoItemService.getPeriodo();
        contComprobantePago.setFechaRegistro(new Date());
        contComprobantePago.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetalleList = new ArrayList<>();
        contDiarioGeneralDetalleListDelete = new ArrayList<>();
        beneficiarioComprobantePagos = new ArrayList<>();
        view = true;
        if (accion) {
            PrimeFaces.current().ajax().update("formMain");
        }
    }

    public void openDlg(String dlg) {
        Utils.openDialog("/facelet/view/commons/" + dlg + "", "45%", "70%");
    }

    public void selectContCuenta(SelectEvent evt) {
        try {
            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
            detalle.setIdContCuentas((ContCuentas) evt.getObject());
            detalle.setNumeracion(contDiarioGeneralDetalleList.size() + 1);
            contDiarioGeneralDetalleList.add(detalle);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar cta. contable", e);
        }
    }

    public void searchBeneficiario(ContBeneficiarioComprobantePago beneficiario) {
        this.beneficiario = beneficiario;
        Map<String, List<String>> params = new HashMap<>();
        params.put(CONFIG.PARAMETER_TIPO, Arrays.asList("true"));
        params.put(CONFIG.PARAMETER_RENDER, Arrays.asList("true"));
        params.put(CONFIG.ONE_TYPE, Arrays.asList("1"));
        Utils.openDialog("/facelet/view/commons/dlgBeneficiarios", "45%", "70%", params);
    }

    public void selectBeneficiario(SelectEvent evt) {
        beneficiario.setIdCliente((Cliente) evt.getObject());
        if (beneficiario.getIdCliente().getTipoBeneficiario()) {
            beneficiario.setTipoBeneficiario(false);
        } else {
            beneficiario.setTipoBeneficiario(true);
        }
        beneficiario.setIdDetalleBanco(ContRegistroPago.beneficiarioBanco(beneficiario.getIdCliente()));
        beneficiario.setMonto(totalDebe);
    }

    public void guardarRelacionesPresupuestarias() {
        if (partePresupuestariaModel != null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una relacion presupuestaria");
            return;
        }
        contDiarioGeneralDetalle.setEjecutado(contDiarioGeneralDetalle.getDebe());
        contDiarioGeneralDetalle.setPartidaPresupuestaria(partePresupuestariaModel.getPartidapresupuestaria());
        contDiarioGeneralDetalle.setIdPresCatalogoPresupuestario(new PresCatalogoPresupuestario(partePresupuestariaModel.getIdprescatalogopresupuestario()));
        contDiarioGeneralDetalle.setIdPresPlanProgramatico(new PresPlanProgramatico(partePresupuestariaModel.getIdpresplanprogramatico()));
        contDiarioGeneralDetalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(partePresupuestariaModel.getIdpresfuentefinanciamiento()));
    }

    public void calcularTotales() {
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        totalBeneficiario = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        if (!contDiarioGeneralDetalleList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalleList) {
                totalDebe = totalDebe.add(item.getDebe()).setScale(2, RoundingMode.HALF_UP);
                totalHaber = totalHaber.add(item.getHaber()).setScale(2, RoundingMode.HALF_UP);
                totalEjecutado = totalEjecutado.add(item.getEjecutado()).setScale(2, RoundingMode.HALF_UP);
            }
        }
        if (!beneficiarioComprobantePagos.isEmpty()) {
            for (ContBeneficiarioComprobantePago item : beneficiarioComprobantePagos) {
                totalBeneficiario = totalBeneficiario.add(item.getMonto()).setScale(2, RoundingMode.HALF_UP);
            }
        }
        if (beneficiarioComprobantePagos != null) {
            if (!beneficiarioComprobantePagos.isEmpty()) {
                if (beneficiarioComprobantePagos.size() == 1) {
                    if (totalHaber.equals(BigDecimal.ZERO)) {
                        beneficiarioComprobantePagos.get(0).setMonto(totalDebe.setScale(2, RoundingMode.HALF_UP));
                    } else {
                        beneficiarioComprobantePagos.get(0).setMonto(totalHaber.setScale(2, RoundingMode.HALF_UP));
                    }
                }
            }
        }
    }

    public void findDIario() {
        if (idDiario != null) {
            ContDiarioGeneral diario = ContRegistroContable.findByDiarioPeriodo(idDiario, contComprobantePago.getPeriodo());
            if (diario != null) {
                diarioSeleccionado(diario);
            } else {
                openDlgDiario();
            }
        } else {
            openDlgDiario();
        }
    }

    public void openDlgDiario() {
        if (contComprobantePago.getPeriodo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
            return;
        }
        contDiarioGeneralList = ContRegistroContable.getListDiarioGeneral(contComprobantePago.getPeriodo());
        JsfUtil.executeJS("PF('contDiarioGeneralDlg').show()");
        PrimeFaces.current().ajax().update("contDiarioGeneralForm");
    }

    public void diarioSeleccionado(ContDiarioGeneral diario) {
        beneficiarioComprobantePagos = new ArrayList<>();
        contDiarioGeneralDetalleList = ContRegistroPago.detalleComprobantePago(diario);
        contComprobantePago.setIdContDiarioGeneral(diario);
        contComprobantePago.setCuentaBancaria(null);
        contComprobantePago.setDescripcion("COMPROBANTE DE PAGO DEL REGISTRO CONTABLE NO." + diario.getNumRegistro() + ", " + diario.getDescripcion());
        SolicitudReservaCompromiso reserva = ContRegistroContable.getReservaCompromiso(diario);
        if (reserva != null) {
            if (reserva.getBeneficiario() != null) {
                ContBeneficiarioComprobantePago beneficiarioComprobantePago = new ContBeneficiarioComprobantePago();
                beneficiarioComprobantePago.setIdCliente(reserva.getBeneficiario());
                beneficiarioComprobantePago.setTipoBeneficiario(reserva.getTipoBeneficiario());
                if (beneficiarioComprobantePago.getTipoBeneficiario()) {
                    beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(Boolean.FALSE);
                } else {
                    beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(Boolean.FALSE);
                }
                beneficiarioComprobantePago.setIdDetalleBanco(ContRegistroPago.beneficiarioBanco(beneficiarioComprobantePago.getIdCliente()));
                beneficiarioComprobantePago.setMonto(totalDebe.setScale(2, RoundingMode.HALF_UP));
                beneficiarioComprobantePagos.add(beneficiarioComprobantePago);
            } else {
                List<BeneficiarioSolicitudReserva> aux = ContRegistroContable.beneficiarioComprobante(reserva);
                if (aux != null) {
                    if (!aux.isEmpty()) {
                        for (BeneficiarioSolicitudReserva item : aux) {
                            ContBeneficiarioComprobantePago beneficiarioComprobantePago = new ContBeneficiarioComprobantePago();
                            beneficiarioComprobantePago.setIdCliente(item.getBeneficiario());
                            beneficiarioComprobantePago.setTipoBeneficiario(item.getTipoBeneficiario());
                            if (beneficiarioComprobantePago.getTipoBeneficiario()) {
                                beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(Boolean.FALSE);
                            } else {
                                beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(Boolean.TRUE);
                            }
                            beneficiarioComprobantePago.setIdDetalleBanco(ContRegistroPago.beneficiarioBanco(beneficiarioComprobantePago.getIdCliente()));
                            beneficiarioComprobantePago.setMonto(item.getValor());
                            beneficiarioComprobantePagos.add(beneficiarioComprobantePago);
                        }
                    }
                }
            }
        } else {
            if (diario.getCodModulo().equals(3)) {
                ThTipoRol thTipoRol = ContRegistroPago.findThTipoRol(diario);
                if (thTipoRol != null) {
                    if (thTipoRol.getId() != null) {
                        List<ThLiquidacionRol> thLiquidacionRols = ContRegistroPago.getThLiquidacionRol(thTipoRol);
                        if (!thLiquidacionRols.isEmpty()) {
                            for (ThLiquidacionRol item : thLiquidacionRols) {
                                ContBeneficiarioComprobantePago beneficiarioComprobantePago = new ContBeneficiarioComprobantePago();
                                beneficiarioComprobantePago.setIdCliente(item.getIdServidorCargo().getIdServidor().getPersona());
                                beneficiarioComprobantePago.setTipoBeneficiario(Boolean.FALSE);
                                beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(Boolean.TRUE);
                                beneficiarioComprobantePago.setIdDetalleBanco(ContRegistroPago.beneficiarioBanco(beneficiarioComprobantePago.getIdCliente()));
                                beneficiarioComprobantePago.setMonto(item.getNetoRecibir());
                                beneficiarioComprobantePagos.add(beneficiarioComprobantePago);
                            }
                        }
                    }
                }
            }
        }
        calcularTotales();
        JsfUtil.executeJS("PF('contDiarioGeneralDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void openBancoDlg() {
        JsfUtil.executeJS("PF('bancoDlg').show()");
        PrimeFaces.current().ajax().update("bancoForm");
    }

    public void closeBancoDlg(ContCuentaEntidad contCuentaEntidad) {
        if (contComprobantePago.getCuentaBancaria() != null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Ya se ha selecionado una cuenta bancaria");
            return;
        }
        contDiarioGeneralDetalle = new ContDiarioGeneralDetalle();
        contDiarioGeneralDetalle.setIdContCuentas(contCuentaEntidad.getIdCuentaMovimiento());
        contDiarioGeneralDetalle.setNumeracion(contDiarioGeneralDetalleList.size() + 1);
        contDiarioGeneralDetalle.setHaber(totalDebe.setScale(2, RoundingMode.HALF_UP));
        contDiarioGeneralDetalleList.add(contDiarioGeneralDetalle);
        contComprobantePago.setCuentaBancaria(contCuentaEntidad);
        contDiarioGeneralDetalle = null;
        calcularTotales();
        JsfUtil.executeJS("PF('bancoDlg').hide()");
        PrimeFaces.current().ajax().update("bancoForm");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void deleteRegistro(ContDiarioGeneralDetalle detalle, int indice) {
        if (detalle.getId() != null) {
            contDiarioGeneralDetalleListDelete.add(detalle);
            contDiarioGeneralDetalleList.remove(detalle);
            if (detalle.getHaber().doubleValue() > 0) {
                contComprobantePago.setCuentaBancaria(null);
            }
        } else {
            contDiarioGeneralDetalleList.remove(indice);
            if (detalle.getHaber().doubleValue() > 0) {
                contComprobantePago.setCuentaBancaria(null);
            }
        }
        actualizarList();
        calcularTotales();
    }

    public void actualizarList() {
        int aux = 1;
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalleList) {
            item.setNumeracion(aux);
            aux += 1;
        }
    }

    public void valorAbono(ContDiarioGeneralDetalle detalle) {
        if (detalle.getDebe().doubleValue() > detalle.getMaxiValor().doubleValue()) {
            JsfUtil.addErrorMessage("AVISO!!!", "El monto ingresado es mayor a monto disponible");
            detalle.setDebe(detalle.getMaxiValor());
            return;
        } else {
            detalle.setEjecutado(detalle.getDebe());
        }

        calcularTotales();
    }

    public void saveComprobantePago() {
        contComprobantePago.setCodRegistro(0);
        if (totalDebe.setScale(2, RoundingMode.HALF_UP).equals(totalHaber.setScale(2, RoundingMode.HALF_UP))) {
            contComprobantePago.setCuadrado(Boolean.TRUE);
        }
        String mensaje = ContRegistroPago.validaciones(contComprobantePago, contDiarioGeneralDetalleList, beneficiarioComprobantePagos);
        if (mensaje.equals("")) {
            if (contComprobantePago.getId() != null) {
                ContRegistroPago.edit(contComprobantePago, contDiarioGeneralDetalleList, contDiarioGeneralDetalleListDelete, beneficiarioComprobantePagos, null);
            } else {
                contComprobantePago = ContRegistroPago.create(contComprobantePago, contDiarioGeneralDetalleList, beneficiarioComprobantePagos);
            }
            JsfUtil.executeJS("PF('dlgConfirmacion').show()");
            PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
    }

    public void loadLazyModel() {
        if (opcionBusqueda.getAnio() != null) {
            contComprobantePagosLazyModel = new LazyModel<>(ContComprobantePago.class);
            contComprobantePagosLazyModel.getSorteds().put("numRegistro", "ASC");
            contComprobantePagosLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            contComprobantePagosLazyModel = null;
        }
    }

    public void form(ContComprobantePago contComprobantePago, Boolean editView) {
        userSession.setIdComprobante(contComprobantePago.getId());
        userSession.setViewDiario(editView);
        if (contComprobantePago.getCodRegistro() == 0) {
            JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/comprobante/pago/completo");
        } else if (contComprobantePago.getCodRegistro() == 1) {
            JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/comprobante/pago/abono");
        }
    }

    public void reporte(ContComprobantePago contComprobantePago, String tipoDocumento) {
        ContRegistroPago.ComprobantPagoImprimirReporte(contComprobantePago, tipoDocumento);
    }

    public void anularComprobante(ContComprobantePago contComprobantePago) {
        contComprobantePago.setObservacion(observacionAnulacion);
        ContRegistroPago.anular(contComprobantePago);
        PrimeFaces.current().ajax().update("registroPagoTable");
        JsfUtil.addSuccessMessage("INFO!!", "Se ha anulado correctamente");
    }

    public void anularComprobante() {
        contComprobantePago.setObservacion(observacionAnulacion);
        //System.out.println("observacionAnulacion: "+contComprobantePago.getObservacion());
        ContRegistroPago.anular(contComprobantePago);
        JsfUtil.addSuccessMessage("INFO!!", "Se ha anulado correctamente");
    }

    public void openConfirmacion(int code, String tipoDocumento) {
        switch (code) {
            case 1:
                JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/comprobante/pagos/");
                break;
            case 2:
                System.out.println("CODE: " + contComprobantePago.getId());
                ContRegistroPago.ComprobantPagoImprimirReporte(contComprobantePago, tipoDocumento);
                break;
            default:
                limpiar(false);
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ContComprobantePago getContComprobantePago() {
        return contComprobantePago;
    }

    public void setContComprobantePago(ContComprobantePago contComprobantePago) {
        this.contComprobantePago = contComprobantePago;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetalleList() {
        return contDiarioGeneralDetalleList;
    }

    public void setContDiarioGeneralDetalleList(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleList) {
        this.contDiarioGeneralDetalleList = contDiarioGeneralDetalleList;
    }

    public List<ContBeneficiarioComprobantePago> getBeneficiarioComprobantePagos() {
        return beneficiarioComprobantePagos;
    }

    public void setBeneficiarioComprobantePagos(List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagos) {
        this.beneficiarioComprobantePagos = beneficiarioComprobantePagos;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ContDiarioGeneralDetalle getContDiarioGeneralDetalle() {
        return contDiarioGeneralDetalle;
    }

    public void setContDiarioGeneralDetalle(ContDiarioGeneralDetalle contDiarioGeneralDetalle) {
        this.contDiarioGeneralDetalle = contDiarioGeneralDetalle;
    }

    public PartePresupuestariaModel getPartePresupuestariaModel() {
        return partePresupuestariaModel;
    }

    public void setPartePresupuestariaModel(PartePresupuestariaModel partePresupuestariaModel) {
        this.partePresupuestariaModel = partePresupuestariaModel;
    }

    public List<PartePresupuestariaModel> getPartePresupuestariaModelList() {
        return partePresupuestariaModelList;
    }

    public void setPartePresupuestariaModelList(List<PartePresupuestariaModel> partePresupuestariaModelList) {
        this.partePresupuestariaModelList = partePresupuestariaModelList;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public BigDecimal getTotalBeneficiario() {
        return totalBeneficiario;
    }

    public void setTotalBeneficiario(BigDecimal totalBeneficiario) {
        this.totalBeneficiario = totalBeneficiario;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public List<ContDiarioGeneral> getContDiarioGeneralList() {
        return contDiarioGeneralList;
    }

    public void setContDiarioGeneralList(List<ContDiarioGeneral> contDiarioGeneralList) {
        this.contDiarioGeneralList = contDiarioGeneralList;
    }

    public Integer getIdDiario() {
        return idDiario;
    }

    public void setIdDiario(Integer idDiario) {
        this.idDiario = idDiario;
    }

    public LazyModel<ContCuentaEntidad> getContCuentaEntidadLazyModel() {
        return contCuentaEntidadLazyModel;
    }

    public LazyModel<ContComprobantePago> getContComprobantePagosLazyModel() {
        return contComprobantePagosLazyModel;
    }

    public void setContComprobantePagosLazyModel(LazyModel<ContComprobantePago> contComprobantePagosLazyModel) {
        this.contComprobantePagosLazyModel = contComprobantePagosLazyModel;
    }

    public void setContCuentaEntidadLazyModel(LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel) {
        this.contCuentaEntidadLazyModel = contCuentaEntidadLazyModel;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
//</editor-fold>

}
