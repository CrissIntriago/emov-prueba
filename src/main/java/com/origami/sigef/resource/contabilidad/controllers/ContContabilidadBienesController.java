/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
@Named(value = "dgBienesView")
@ViewScoped
public class ContContabilidadBienesController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ContCuentasService contCuentasService;

    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneral contDiarioGeneral;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private CatalogoMovimiento movimientoBienSeleccionado;

    private LazyModel<BienesMovimiento> bienesList;
    private LazyModel<ContCuentas> contCuentasLazy;

    private List<Short> listaPeriodo;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete;
    private List<CatalogoItem> clasesDiarioGeneral;
    private List<CatalogoItem> tiposDiarioGeneral;
    private List<Depreciacion> depreciacionList;
    private List<Long> idModulos;
    private List<CatalogoMovimiento> motivoMovimientosList;
    private List<BienesMovimiento> bienesListSeleccionados;

    private BigDecimal totalDebe, totalHaber, totalComprometido, totalDevengado, totalEjecutado, diferencia;

    private Boolean view, tipoDlg;

    @PostConstruct
    public void initialize() {
        clear();
        formInicializar();
        loadDataRedirect();
        listaPeriodo = catalogoItemService.getPeriodo();
        motivoMovimientosList = ContRegistroContable.getMotivoMovimientos();
        updateContCuentas(false, "");
        calcularTotales();
    }

    public void updateContCuentas(Boolean accion, String code) {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        if (accion) {
            contCuentasLazy.getFilterss().put("codigo:startsWith", code);
        }
    }

    public void loadDataRedirect() {
        if (userSession.getIdDiario() != null) {
            contDiarioGeneral = ContRegistroContable.findById(userSession.getIdDiario());
            List<ContDiarioGeneralDetalle> tempList = this.ContRegistroContable.findByIdDiario(contDiarioGeneral);
            contDiarioGeneralDetallesList = new ArrayList<>();
            for (ContDiarioGeneralDetalle temp : tempList) {
                contDiarioGeneralDetallesList.add(temp);
            }
            view = Utils.clone(userSession.getViewDiario());
            userSession.setIdDiario(null);
            actualizarTipol();
        }
    }

    public void clear() {
        opcionBusqueda = new OpcionBusqueda();
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
    }

    public void formInicializar() {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        clasesDiarioGeneral = catalogoItemService.findByCatalogo("diario_general_clases");
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
        idModulos = new ArrayList<>();
        bienesListSeleccionados = new ArrayList<>();
        tiposDiarioGeneral = new ArrayList<>();
        bienesList = null;
        view = true;
        movimientoBienSeleccionado = null;
    }

    public void actualizarTipol() {
        if (contDiarioGeneral.getClase() != null) {
            this.tiposDiarioGeneral = catalogoService.getTiposDiarioGeneral(contDiarioGeneral.getClase(), "diario_general_tipos");
        } else {
            this.tiposDiarioGeneral = new ArrayList<>();
        }
    }

    public void openDlgCuentas(Boolean accion) {
        tipoDlg = accion;
        JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void selectContCuenta(ContCuentas contCuentas) {
        try {
            if (tipoDlg) {
                contDiarioGeneralDetalle.setIdContCuentas(contCuentas);
            } else {
                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                detalle.setIdContCuentas(contCuentas);
                detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
                contDiarioGeneralDetallesList.add(detalle);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar cta. contable", e);
        }
        calcularTotales();
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    public void btnSearchContCuenta() {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        detalle.setNumeracion(contDiarioGeneralDetallesList.size() + 1);
        contDiarioGeneralDetallesList.add(detalle);
    }

    public void searchContCuenta(ContDiarioGeneralDetalle detalle) {
        contDiarioGeneralDetalle = detalle;
        contDiarioGeneralDetalle.setIdContCuentas(contCuentasService.findCodigo(detalle.getCodCuentaInsert()));
        if (contDiarioGeneralDetalle.getIdContCuentas() != null) {
            if (!contDiarioGeneralDetalle.getIdContCuentas().getMovimiento()) {
                contDiarioGeneralDetalle.setIdContCuentas(null);
                updateContCuentas(true, detalle.getCodCuentaInsert());
                openDlgCuentas(true);
            } else {
                calcularTotales();
            }
        } else {
            updateContCuentas(true, detalle.getCodCuentaInsert());
            openDlgCuentas(true);
        }
    }

    public void calcularTotales() {
        totalDebe = BigDecimal.ZERO;
        totalHaber = BigDecimal.ZERO;
        totalComprometido = BigDecimal.ZERO;
        totalDevengado = BigDecimal.ZERO;
        totalEjecutado = BigDecimal.ZERO;
        if (!contDiarioGeneralDetallesList.isEmpty()) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
                totalDebe = totalDebe.add(item.getDebe()).setScale(2, RoundingMode.HALF_UP);
                totalHaber = totalHaber.add(item.getHaber()).setScale(2, RoundingMode.HALF_UP);
                totalComprometido = totalComprometido.add(item.getComprometido()).setScale(2, RoundingMode.HALF_UP);
                totalDevengado = totalDevengado.add(item.getDevengado()).setScale(2, RoundingMode.HALF_UP);
                totalEjecutado = totalEjecutado.add(item.getEjecutado()).setScale(2, RoundingMode.HALF_UP);
            }
        }
        diferencia = totalDebe.subtract(totalHaber);
        if (totalDebe.equals(totalHaber)) {
            contDiarioGeneral.setCuadrado(Boolean.TRUE);
        } else {
            contDiarioGeneral.setCuadrado(Boolean.FALSE);
        }
    }

    public void saveDiario() {
        contDiarioGeneral.setDebe(totalDebe);
        contDiarioGeneral.setHaber(totalHaber);
        String mensaje = ContRegistroContable.validaciones(contDiarioGeneral, contDiarioGeneralDetallesList);
        if (mensaje.equals("")) {
            if (contDiarioGeneral.getId() != null) {
                ContRegistroContable.edit(contDiarioGeneral, contDiarioGeneralDetallesList, contDiarioGeneralDetallesDelete);
            } else {
                contDiarioGeneral = ContRegistroContable.create(contDiarioGeneral, contDiarioGeneralDetallesList, idModulos, true);
            }
            JsfUtil.executeJS("PF('dlgConfirmacion').show()");
            PrimeFaces.current().ajax().update("dlgConfirmacionForm");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
    }

    public void openConfirmacion(int code, String tipoDocumento) {
        switch (code) {
            case 1:
                JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diario/manual");
                break;
            case 2:
                System.out.println("CODE: " + contDiarioGeneral.getId());
                ContRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
                break;
            default:
                formInicializar();
                JsfUtil.executeJS("PF('dlgConfirmacion').hide()");
                PrimeFaces.current().ajax().update("formMain");
                break;
        }
    }

    public void deleteRegistro(ContDiarioGeneralDetalle detalle, int indice) {
        if (detalle.getId() != null) {
            contDiarioGeneralDetallesDelete.add(detalle);
            contDiarioGeneralDetallesList.remove(detalle);
        } else {
            contDiarioGeneralDetallesList.remove(indice);
        }
        actualizarList();
        calcularTotales();
    }

    public void actualizarList() {
        int aux = 1;
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesList) {
            item.setNumeracion(aux);
            aux += 1;
        }
    }

    public void openDlgBienes(String nameDlg, String nameForm) {
        formInicializar();
        JsfUtil.executeJS("PF('" + nameDlg + "').show()");
        PrimeFaces.current().ajax().update(nameForm);
    }

    public void openDlgDepreciaciones() {
        formInicializar();
        depreciacionList = ContRegistroContable.getDepreciacionList(contDiarioGeneral.getPeriodo());
        JsfUtil.executeJS("PF('depreciacionesDlg').show()");
        PrimeFaces.current().ajax().update("depreciacionesForm");
    }

    public void closeDlgDepreciaciones(Depreciacion depreciacion) {
        contDiarioGeneralDetallesList = ContRegistroContable.getDetalleDepreciacion(depreciacion);
        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
        actualizarTipol();
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_ajuste"));
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_DEPRECIACIONES);
        contDiarioGeneral.setDescripcion("P/R. DEPRECIACIONES CON CÓDIGO " + depreciacion.getCodigoDepreciacion() + ", DESDE: " + depreciacion.getFechaDesde() + ", HASTA: " + depreciacion.getFechaHasta());
        idModulos.add(depreciacion.getId());
        calcularTotales();
        JsfUtil.executeJS("PF('depreciacionesDlg').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
        PrimeFaces.current().ajax().update("formMain");
    }

    public void valorDebeHaber(ContDiarioGeneralDetalle detalle, Boolean accion) {
        if (accion) {
            detalle.setHaber(BigDecimal.ZERO);
        } else {
            detalle.setDebe(BigDecimal.ZERO);
        }
        calcularTotales();
    }

    public void actualizarRegistros() {
        if (movimientoBienSeleccionado != null) {
            this.bienesList = new LazyModel<>(BienesMovimiento.class);
            this.bienesList.getSorteds().put("orden", "ASC");
            this.bienesList.getFilterss().put("estado", true);
            this.bienesList.getFilterss().put("contabilizado", false);
            this.bienesList.getFilterss().put("periodo", contDiarioGeneral.getPeriodo());
            this.bienesList.getFilterss().put("motivoMovimiento", movimientoBienSeleccionado);
            bienesListSeleccionados = new ArrayList<>();
        } else {
            bienesList = null;
        }
        PrimeFaces.current().ajax().update("bienesTable");
    }

    public void bienesListSeleccionado() {
        if (bienesListSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar por lo menos un bien");
            return;
        }
        contDiarioGeneral.setDescripcion("P.R. BIENES DE INGRESO DE TIPO: " + movimientoBienSeleccionado.getTexto() + ", LOS CUALES SON:");
        for (BienesMovimiento item : bienesListSeleccionados) {
            idModulos.add(item.getId());
            contDiarioGeneral.setDescripcion(contDiarioGeneral.getDescripcion() + " " + item.getCodigo() + ", ");
        }
        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
        actualizarTipol();
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_ajuste"));
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_BIENES_INGRESO);
        contDiarioGeneralDetallesList = ContRegistroContable.getBienesIngreso(idModulos);
        calcularTotales();
        PrimeFaces.current().executeScript("PF('dlgBienes').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
        PrimeFaces.current().ajax().update("formMain");
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public ContDiarioGeneral getContDiarioGeneral() {
        return contDiarioGeneral;
    }

    public void setContDiarioGeneral(ContDiarioGeneral contDiarioGeneral) {
        this.contDiarioGeneral = contDiarioGeneral;
    }

    public List<ContDiarioGeneralDetalle> getContDiarioGeneralDetallesList() {
        return contDiarioGeneralDetallesList;
    }

    public void setContDiarioGeneralDetallesList(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList) {
        this.contDiarioGeneralDetallesList = contDiarioGeneralDetallesList;
    }

    public List<CatalogoItem> getClasesDiarioGeneral() {
        return clasesDiarioGeneral;
    }

    public void setClasesDiarioGeneral(List<CatalogoItem> clasesDiarioGeneral) {
        this.clasesDiarioGeneral = clasesDiarioGeneral;
    }

    public List<CatalogoItem> getTiposDiarioGeneral() {
        return tiposDiarioGeneral;
    }

    public void setTiposDiarioGeneral(List<CatalogoItem> tiposDiarioGeneral) {
        this.tiposDiarioGeneral = tiposDiarioGeneral;
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

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalEjecutado() {
        return totalEjecutado;
    }

    public void setTotalEjecutado(BigDecimal totalEjecutado) {
        this.totalEjecutado = totalEjecutado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public List<Depreciacion> getDepreciacionList() {
        return depreciacionList;
    }

    public void setDepreciacionList(List<Depreciacion> depreciacionList) {
        this.depreciacionList = depreciacionList;
    }

    public List<Long> getIdModulos() {
        return idModulos;
    }

    public void setIdModulos(List<Long> idModulos) {
        this.idModulos = idModulos;
    }

    public CatalogoMovimiento getMovimientoBienSeleccionado() {
        return movimientoBienSeleccionado;
    }

    public void setMovimientoBienSeleccionado(CatalogoMovimiento movimientoBienSeleccionado) {
        this.movimientoBienSeleccionado = movimientoBienSeleccionado;
    }

    public LazyModel<BienesMovimiento> getBienesList() {
        return bienesList;
    }

    public void setBienesList(LazyModel<BienesMovimiento> bienesList) {
        this.bienesList = bienesList;
    }

    public List<CatalogoMovimiento> getMotivoMovimientosList() {
        return motivoMovimientosList;
    }

    public void setMotivoMovimientosList(List<CatalogoMovimiento> motivoMovimientosList) {
        this.motivoMovimientosList = motivoMovimientosList;
    }

    public List<BienesMovimiento> getBienesListSeleccionados() {
        return bienesListSeleccionados;
    }

    public void setBienesListSeleccionados(List<BienesMovimiento> bienesListSeleccionados) {
        this.bienesListSeleccionados = bienesListSeleccionados;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

}
