/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.models.RelacionLocalModel;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
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
@Named(value = "dgTalentoHumanoView")
@ViewScoped
public class ContContabilidadTalentoHumanoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroContable ContRegistroContable;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UserSession userSession;
    @Inject
    private ContCuentasService contCuentasService;

    private OpcionBusqueda opcionBusqueda;
    private ContDiarioGeneral contDiarioGeneral;
    private ContDiarioGeneralDetalle contDiarioGeneralDetalle;
    private PartePresupuestariaModel partePresupuestariaModel;
    private ThTipoRol thTipoRolSeleccionado;

    private LazyModel<ContCuentas> contCuentasLazy;

    private List<Short> listaPeriodo;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList;
    private List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete;
    private List<PartePresupuestariaModel> partePresupuestariaModelList;
    private List<ThTipoRol> thTipoRolList;
    private List<Long> idModulos;

    private BigDecimal totalDebe, totalHaber, totalComprometido, totalDevengado, totalEjecutado, diferencia;

    private Boolean view, tipoDlg, tipoRelacion;

    @PostConstruct
    public void initialize() {
        clear();
        formInicializar(false);
        loadDataRedirect();
        userSession.setIdComprobante(null);
        listaPeriodo = catalogoItemService.getPeriodo();
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
            contDiarioGeneralDetallesList = Utils.clone(this.ContRegistroContable.findByIdDiario(contDiarioGeneral));
            thTipoRolSeleccionado = ContRegistroContable.getThTipoRol(contDiarioGeneral.getId());
            view = Utils.clone(userSession.getViewDiario());
            userSession.setIdDiario(null);
        }
    }

    public void reporte(ContDiarioGeneral contDiarioGeneral, String tipoDocumento) {
        ContRegistroContable.ContabilidadImprimirReporte(contDiarioGeneral, tipoDocumento);
    }

    public void anular(ContDiarioGeneral contDiarioGeneral) {
        ContRegistroContable.ContabilidadAnular(contDiarioGeneral);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha anulado correctamente");
    }

    public void clear() {
        opcionBusqueda = new OpcionBusqueda();
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
    }

    public void formInicializar() {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneralDetallesList = new ArrayList<>();
        contDiarioGeneralDetallesDelete = new ArrayList<>();
        thTipoRolList = new ArrayList<>();
        idModulos = new ArrayList<>();
        thTipoRolSeleccionado = new ThTipoRol();
        view = true;
    }

    public void formInicializar(boolean accion) {
        contDiarioGeneral = new ContDiarioGeneral();
        contDiarioGeneral.setPeriodo(opcionBusqueda.getAnio());
        contDiarioGeneral.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
        contDiarioGeneral.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_financiero"));
        contDiarioGeneralDetallesList = new ArrayList<>();
        view = true;
        if (accion) {
            userSession.setIdDiario(null);
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
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        PrimeFaces.current().ajax().update("registroContableDetalleTable");
    }

    private void valoresCero(ContDiarioGeneralDetalle detalle) {
        detalle.setComprometido(BigDecimal.ZERO);
        detalle.setDevengado(BigDecimal.ZERO);
        detalle.setEjecutado(BigDecimal.ZERO);
        detalle.setPartidaPresupuestaria(null);
        detalle.setIdPresCatalogoPresupuestario(null);
        detalle.setIdPresPlanProgramatico(null);
        detalle.setIdPresFuenteFinanciamiento(null);
        partePresupuestariaModelList = new ArrayList<>();
        partePresupuestariaModel = null;

    }

    public void determinarRelacionPresupuestaria(ContDiarioGeneralDetalle detalle, boolean tipoRegistro) {
        if (contDiarioGeneral.getTipo() == null || contDiarioGeneral.getClase() == null) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            JsfUtil.addErrorMessage("AVISO!!!", "Debe seleccionar el tipo y la clase");
            return;
        }
        contDiarioGeneralDetalle = detalle;
        valoresCero(contDiarioGeneralDetalle);
        if (tipoRegistro) {
            detalle.setHaber(BigDecimal.ZERO);
        } else {
            detalle.setDebe(BigDecimal.ZERO);
        }
        relacion(tipoRegistro);
        calcularTotales();
    }

    private void relacion(Boolean accion) {
        partePresupuestariaModel = null;
        if (contDiarioGeneral.getTipo().getCodigo().equals("tipo_financiero")) {
            tipoRelacion = accion;
            if (tipoRelacion) {
                String[] arrayDebe = valoresService.findByCodigo(CONFIG.RESTRINGIR_DEBE).split(",");
                for (String text : arrayDebe) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            } else {
                String[] arrayHaber = valoresService.findByCodigo(CONFIG.RESTRINGIR_HABER).split(",");
                for (String text : arrayHaber) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().contains(text)) {
                        calcularTotales();
                        return;
                    }
                }
            }
            partePresupuestariaModelList = ContRegistroContable.relacionPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral, tipoRelacion);
            if (!partePresupuestariaModelList.isEmpty()) {
                List<String> partidasList = ContRegistroContable.verificarRelacion(contDiarioGeneralDetallesList, partePresupuestariaModelList);
                if (!partidasList.isEmpty()) {
                    RelacionLocalModel temp = ContRegistroContable.relacionLocal(contDiarioGeneralDetallesList, tipoRelacion, contDiarioGeneralDetalle, partidasList);
                    if (temp.getResultList() == null) {
                        contDiarioGeneralDetalle = temp.getContDiarioGeneralDetalle();
                        return;
                    } else {
                        partePresupuestariaModelList = temp.getResultList();
                    }
                    if (!partePresupuestariaModelList.isEmpty()) {
                        if (partePresupuestariaModelList.size() > 1) {
                            openDlgRelaciones();
                        } else {
                            partePresupuestariaModel = partePresupuestariaModelList.get(0);
                            guardarRelacionesPresupuestarias();
                        }
                    } else {
                        if (tipoRelacion) {
                            contDiarioGeneralDetalle.setDebe(BigDecimal.ZERO);
                        } else {
                            contDiarioGeneralDetalle.setHaber(BigDecimal.ZERO);
                        }
                        JsfUtil.addWarningMessage("AVISO!!", "El saldo de las partidas de devengado son iguales a las del ejecutado, no puede haber mas afectaciones");
                        return;
                    }
                } else {
                    if (partePresupuestariaModelList.size() > 1) {
                        openDlgRelaciones();
                    } else {
                        partePresupuestariaModel = partePresupuestariaModelList.get(0);
                        guardarRelacionesPresupuestarias();
                    }
                }
            }
        }
    }

    private void openDlgRelaciones() {
        if (!partePresupuestariaModelList.isEmpty()) {
            if (partePresupuestariaModelList.size() > 1) {
                PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').show()");
                PrimeFaces.current().ajax().update("partidaEstructuraRelacionadaForm");
            } else {
                partePresupuestariaModel = partePresupuestariaModelList.get(0);
                guardarRelacionesPresupuestarias();
            }
        }
    }

    public void guardarRelacionesPresupuestarias() {
        String msj = ContRegistroContable.guardarRelacionesPresupuestarias(partePresupuestariaModel, contDiarioGeneralDetalle, tipoRelacion);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        calcularTotales();
        PrimeFaces.current().executeScript("PF('partidaEstructuraRelacionadaDlg').hide()");
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
        contDiarioGeneral.setCodModulo(MOD_CONTABILIDAD.MOD_TALENTO_HUMANO);
        System.out.println("idModulos: " + idModulos);
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

    public void openDlgRolGeneral() {
        thTipoRolList = ContRegistroContable.getRoles(contDiarioGeneral.getPeriodo());
        JsfUtil.executeJS("PF('dlgRolGeneral').show()");
        PrimeFaces.current().ajax().update("dlgRolGeneralForm");
        PrimeFaces.current().ajax().update("gridRoles");
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
            }
        } else {
            updateContCuentas(true, detalle.getCodCuentaInsert());
            openDlgCuentas(true);
        }
    }

    public void loadData() {
        if (thTipoRolSeleccionado != null) {
            if (thTipoRolSeleccionado.getId() != null) {
                contDiarioGeneralDetallesList = ContRegistroContable.getDetalleRol(thTipoRolSeleccionado);
                if (contDiarioGeneralDetallesList.isEmpty()) {
                    JsfUtil.addSuccessMessage("AVISO!!!", "No se ha podido cargar la información");
                } else {
                    if (idModulos == null || !idModulos.isEmpty()) {
                        idModulos = new ArrayList<>();
                    }
                    idModulos.add(thTipoRolSeleccionado.getId());
                    contDiarioGeneral.setDescripcion("MODULO TALENTO HUMANO: " + thTipoRolSeleccionado.getDescripcion());
                    calcularTotales();
                    JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado correctamente la información");
                    JsfUtil.executeJS("PF('dlgRolGeneral').hide()");
                    PrimeFaces.current().ajax().update("gridDescripcion");
                    PrimeFaces.current().ajax().update("registroContableDetalleTable");
                }

            } else {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un rol");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un rol");
        }
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
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

    public List<PartePresupuestariaModel> getPartePresupuestariaModelList() {
        return partePresupuestariaModelList;
    }

    public void setPartePresupuestariaModelList(List<PartePresupuestariaModel> partePresupuestariaModelList) {
        this.partePresupuestariaModelList = partePresupuestariaModelList;
    }

    public PartePresupuestariaModel getPartePresupuestariaModel() {
        return partePresupuestariaModel;
    }

    public void setPartePresupuestariaModel(PartePresupuestariaModel partePresupuestariaModel) {
        this.partePresupuestariaModel = partePresupuestariaModel;
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

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<ThTipoRol> getThTipoRolList() {
        return thTipoRolList;
    }

    public void setThTipoRolList(List<ThTipoRol> thTipoRolList) {
        this.thTipoRolList = thTipoRolList;
    }

    public ThTipoRol getThTipoRolSeleccionado() {
        return thTipoRolSeleccionado;
    }

    public void setThTipoRolSeleccionado(ThTipoRol thTipoRolSeleccionado) {
        this.thTipoRolSeleccionado = thTipoRolSeleccionado;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

}
