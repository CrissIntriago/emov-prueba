/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroPago;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contPagoAbonosView")
@ViewScoped
public class ContComprobantePagoAbonoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private ContRegistroPago ContRegistroPago;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private UserSession userSession;

    private ContComprobantePago contComprobantePago;
    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private PartePresupuestariaModel partePresupuestariaModel;

    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetalleDeleteList;
    private List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagos;
    private List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagosDelete;
    private List<Short> listaPeriodo;
    private List<PartePresupuestariaModel> partePresupuestariaModelList;

    private double totalDebe, totalHaber, totalBeneficiario, totalEjecutado;

    private LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel;
    private LazyModel<ContCuentas> contCuentasLazyModel;
    private LazyModel<Servidor> servidorLazy;
    private LazyModel<Proveedor> proveedorLazy;

    private Boolean tipoListado, tipoDlg, view;

    @PostConstruct
    public void initialize() {
        formInicializar();
        listaPeriodo = catalogoItemService.getPeriodo();
        contCuentaEntidadLazyModel = new LazyModel<>(ContCuentaEntidad.class);
        contCuentaEntidadLazyModel.getSorteds().put("id", "ASC");
        contCuentaEntidadLazyModel.getFilterss().put("estado", true);
        contCuentaEntidadLazyModel.getFilterss().put("tipoCuenta", true);
        loadDataRedirect();
        calcularTotales();
    }

    public void formInicializar() {
        opcionBusqueda = new OpcionBusqueda();
        contComprobantePago = new ContComprobantePago();
        contComprobantePago.setFechaRegistro(new Date());
        contComprobantePago.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetalleList = new ArrayList<>();
        beneficiarioComprobantePagos = new ArrayList<>();
        beneficiarioComprobantePagosDelete = new ArrayList<>();
        contDiarioGeneralDetalleDeleteList = new ArrayList<>();
        tipoListado = false;
        view = true;
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

    public void openDlg(String dlg) {
        JsfUtil.executeJS("PF('contCuentasDlg').show()");
        PrimeFaces.current().ajax().update("contCuentasForm");
    }

    public void selectContCuenta(ContCuentas contCuentas) {
        try {
            if (tipoDlg) {
                contDiarioGeneralDetalle.setIdContCuentas(contCuentas);
            } else {
                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                detalle.setIdContCuentas(contCuentas);
                detalle.setNumeracion(contDiarioGeneralDetalleList.size() + 1);
                contDiarioGeneralDetalleList.add(detalle);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar cta. contable", e);
        }
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void searchBeneficiario() {
        if (tipoListado) {
            servidorLazy = new LazyModel<>(Servidor.class);
            servidorLazy.getSorteds().put("persona.apellido", "ASC");
            servidorLazy.getFilterss().put("estado", true);
            servidorLazy.getFilterss().put("persona.estado", true);
            servidorLazy.setDistinct(false);
        } else {
            proveedorLazy = new LazyModel<>(Proveedor.class);
            proveedorLazy.getSorteds().put("cliente.nombre", "ASC");
            proveedorLazy.getFilterss().put("estado", true);
            proveedorLazy.setDistinct(false);
        }
        JsfUtil.executeJS("PF('beneficiarioDlg').show()");
        PrimeFaces.current().ajax().update("beneficiarioForm");
    }

    public void closeDlg(Cliente cliente, Boolean tipo) {
        //Agregar datos del beneficiario
        ContBeneficiarioComprobantePago beneficiarioComprobantePago = new ContBeneficiarioComprobantePago();
        beneficiarioComprobantePago.setIdCliente(cliente);
        beneficiarioComprobantePago.getIdCliente().setTipoBeneficiario(tipoListado);
        beneficiarioComprobantePago.setTipoBeneficiario(!tipoListado);
        beneficiarioComprobantePago.setIdDetalleBanco(ContRegistroPago.beneficiarioBanco(beneficiarioComprobantePago.getIdCliente()));
        beneficiarioComprobantePago.setMonto(new BigDecimal(totalDebe));
        beneficiarioComprobantePagos.add(beneficiarioComprobantePago);
        JsfUtil.executeJS("PF('beneficiarioDlg').hide()");
        PrimeFaces.current().ajax().update("beneficiariosTable");
    }

    public void determinarRelacionPresupuestaria(ContDiarioGeneralDetalle detalle, Boolean accion) {
        if (accion) {
            detalle.setHaber(BigDecimal.ZERO);
            contDiarioGeneralDetalle = detalle;
            relacion();
        } else {
            detalle.setDebe(BigDecimal.ZERO);
        }
        calcularTotales();
    }

    public void relacion() {
        if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("2")) {//ejecutado
            partePresupuestariaModelList = ContRegistroContable.ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contComprobantePago.getPeriodo(), false, true);
        } else {//devengado
            partePresupuestariaModelList = ContRegistroContable.ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contComprobantePago.getPeriodo(), true, true);
        }
        if (partePresupuestariaModelList != null) {
            if (!partePresupuestariaModelList.isEmpty()) {
                if (partePresupuestariaModelList.size() > 1) {
                    PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                    PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
                } else {
                    partePresupuestariaModel = partePresupuestariaModelList.get(0);
                    if (Utils.redondearDosDecimales(partePresupuestariaModel.getSaldodisponible().doubleValue()) <= 0) {
                        JsfUtil.addWarningMessage("AVISO", "No exite alguna relacion contable o el saldo de la partida es menor o igual a cero");
                        return;
                    }
                    guardarRelacionesPresupuestarias();
                }
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        if (partePresupuestariaModel == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar una relacion presupuestaria");
            return;
        }
        if (partePresupuestariaModel.getSaldodisponible().doubleValue() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede seleccionar una partida donde el monto sea menor o igual a cero");
            return;
        }
        if (Utils.redondearDosDecimales(partePresupuestariaModel.getSaldodisponible().doubleValue()) < Utils.redondearDosDecimales(contDiarioGeneralDetalle.getDebe().doubleValue())) {
            JsfUtil.addWarningMessage("AVISO!!!", "El valor ingresado es superior al monto disponible de la partida seleccionada");
            return;
        }
        contDiarioGeneralDetalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_ejecucion"));
        contDiarioGeneralDetalle.setEjecutado(contDiarioGeneralDetalle.getDebe());
        contDiarioGeneralDetalle.setPartidaPresupuestaria(partePresupuestariaModel.getPartidapresupuestaria());
        contDiarioGeneralDetalle.setIdPresCatalogoPresupuestario(new PresCatalogoPresupuestario(partePresupuestariaModel.getIdprescatalogopresupuestario()));
        contDiarioGeneralDetalle.setIdPresPlanProgramatico(new PresPlanProgramatico(partePresupuestariaModel.getIdpresplanprogramatico()));
        contDiarioGeneralDetalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(partePresupuestariaModel.getIdpresfuentefinanciamiento()));
        calcularTotales();
        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
        PrimeFaces.current().ajax().update("beneficiariosTable");
    }

    public void calcularTotales() {
        totalDebe = 0;
        totalHaber = 0;
        totalBeneficiario = 0;
        totalEjecutado = 0;
        if (!contDiarioGeneralDetalleList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalleList) {
                totalDebe = totalDebe + item.getDebe().doubleValue();
                totalHaber = totalHaber + item.getHaber().doubleValue();
                totalEjecutado = totalEjecutado + item.getEjecutado().doubleValue();
            }
        }
        if (!beneficiarioComprobantePagos.isEmpty()) {
            for (ContBeneficiarioComprobantePago item : beneficiarioComprobantePagos) {
                totalBeneficiario = totalBeneficiario + item.getMonto().doubleValue();
            }
            if (beneficiarioComprobantePagos.size() == 1) {
                beneficiarioComprobantePagos.get(0).setMonto(new BigDecimal(totalDebe));
            }
        }
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
        contComprobantePago.setCuentaBancaria(contCuentaEntidad);
        contDiarioGeneralDetalle = new ContDiarioGeneralDetalle();
        contDiarioGeneralDetalle.setIdContCuentas(contCuentaEntidad.getIdCuentaMovimiento());
        contDiarioGeneralDetalle.setNumeracion(contDiarioGeneralDetalleList.size() + 1);
        contDiarioGeneralDetalle.setHaber(new BigDecimal(totalDebe));
        contDiarioGeneralDetalleList.add(contDiarioGeneralDetalle);
        contComprobantePago.setCuentaBancaria(contCuentaEntidad);
        contDiarioGeneralDetalle = null;
        calcularTotales();
        JsfUtil.executeJS("PF('bancoDlg').hide()");
        PrimeFaces.current().ajax().update("bancoForm");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
        PrimeFaces.current().ajax().update("beneficiariosTable");
    }

    public void save() {
        contComprobantePago.setCodRegistro(1);
        if (Utils.redondearDosDecimales(totalDebe) == Utils.redondearDosDecimales(totalHaber)) {
            contComprobantePago.setCuadrado(Boolean.TRUE);
        }
        String mensaje = ContRegistroPago.validaciones(contComprobantePago, contDiarioGeneralDetalleList, beneficiarioComprobantePagos);
        if (mensaje.equals("")) {
            if (contComprobantePago.getId() != null) {
                ContRegistroPago.edit(contComprobantePago, contDiarioGeneralDetalleList, contDiarioGeneralDetalleDeleteList, beneficiarioComprobantePagos, beneficiarioComprobantePagosDelete);
            } else {
                contComprobantePago = ContRegistroPago.create(contComprobantePago, contDiarioGeneralDetalleList, beneficiarioComprobantePagos);
            }
            JsfUtil.executeJS("PF('dlgConfirmacion').show()");
            PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
    }

    public void nuevaLinea() {
        ContDiarioGeneralDetalle insert = new ContDiarioGeneralDetalle();
        insert.setNumeracion(contDiarioGeneralDetalleList.size() + 1);
        contDiarioGeneralDetalleList.add(insert);
    }

    public void searchContCuenta(ContDiarioGeneralDetalle detalle) {
        contDiarioGeneralDetalle = detalle;
        contDiarioGeneralDetalle.setIdContCuentas(contCuentasService.findCodigo(detalle.getCodCuentaInsert()));
        if (contDiarioGeneralDetalle.getIdContCuentas() != null) {
            if (!contDiarioGeneralDetalle.getIdContCuentas().getMovimiento()) {
                contDiarioGeneralDetalle.setIdContCuentas(null);
                updateContCuentas(true, detalle.getCodCuentaInsert());
                openDlgCuentas(true);
            }
        } else {
            updateContCuentas(true, detalle.getCodCuentaInsert());
            openDlgCuentas(true);
        }
    }

    public void openDlgCuentas(Boolean accion) {
        tipoDlg = accion;
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void updateContCuentas(Boolean accion, String code) {
        contCuentasLazyModel = new LazyModel<>(ContCuentas.class);
        contCuentasLazyModel.getSorteds().put("codigo", "ASC");
        contCuentasLazyModel.getFilterss().put("estado", true);
        contCuentasLazyModel.getFilterss().put("activo", true);
        contCuentasLazyModel.getFilterss().put("movimiento", true);
        if (accion) {
            contCuentasLazyModel.getFilterss().put("codigo:startsWith", code);
        } else {
            contCuentasLazyModel.getFilterss().put("codigo:startsWith", "2");
        }
    }

    public void validarMonto(ContBeneficiarioComprobantePago beneficiarioComprobantePago) {
        if (totalDebe > beneficiarioComprobantePago.getMonto().doubleValue()) {
            JsfUtil.addWarningMessage("AVISO", "El monto ingresado es mayor al monto a la suma del debe");
            beneficiarioComprobantePago.setMonto(BigDecimal.ZERO);
            return;
        }
        calcularTotales();
    }

    public void deleteRegistro(ContDiarioGeneralDetalle detalle, int index) {
        if (detalle.getHaber().doubleValue() > 0) {
            contComprobantePago.setCuentaBancaria(null);
        }
        if (detalle.getId() != null) {
            contDiarioGeneralDetalleDeleteList.add(detalle);
            contDiarioGeneralDetalleList.remove(detalle);
        } else {
            contDiarioGeneralDetalleList.remove(index);
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

    public void deleteRegistroBeneficiario(ContBeneficiarioComprobantePago beneficiarioComprobantePago, int index) {
        if (beneficiarioComprobantePago.getId() != null) {
            beneficiarioComprobantePagosDelete.add(beneficiarioComprobantePago);
            beneficiarioComprobantePagos.remove(beneficiarioComprobantePago);
        } else {
            beneficiarioComprobantePagos.remove(index);
        }
        calcularTotales();
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
                formInicializar();
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

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

    public double getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(double totalDebe) {
        this.totalDebe = totalDebe;
    }

    public double getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(double totalHaber) {
        this.totalHaber = totalHaber;
    }

    public double getTotalBeneficiario() {
        return totalBeneficiario;
    }

    public void setTotalBeneficiario(double totalBeneficiario) {
        this.totalBeneficiario = totalBeneficiario;
    }

    public double getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(double totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public LazyModel<ContCuentaEntidad> getContCuentaEntidadLazyModel() {
        return contCuentaEntidadLazyModel;
    }

    public void setContCuentaEntidadLazyModel(LazyModel<ContCuentaEntidad> contCuentaEntidadLazyModel) {
        this.contCuentaEntidadLazyModel = contCuentaEntidadLazyModel;
    }

    public LazyModel<ContCuentas> getContCuentasLazyModel() {
        return contCuentasLazyModel;
    }

    public void setContCuentasLazyModel(LazyModel<ContCuentas> contCuentasLazyModel) {
        this.contCuentasLazyModel = contCuentasLazyModel;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public Boolean getTipoListado() {
        return tipoListado;
    }

    public void setTipoListado(Boolean tipoListado) {
        this.tipoListado = tipoListado;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
