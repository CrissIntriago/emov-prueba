/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ReclasificacionCuentas;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import com.origami.sigef.contabilidad.service.ReclasificacionCuentasService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "reclasificacionCuentasView")
@ViewScoped
public class ReclasificacionCuentasController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private UserSession userSession;
    @Inject
    private ReclasificacionCuentasService reclasificacionCuentasService;
    @Inject
    private MasterCatalogoService masterCatalogoService;

    private OpcionBusqueda opcionBusqueda;
    private CuentaContable cuentaContableSeleccionado;
    private ReclasificacionCuentas cuentaReclasificable;

    private LazyModel<ReclasificacionCuentas> reclasificacioCuentasLazy;
    private LazyModel<CuentaContable> cuentaContableLazyModel;

    private List<ReclasificacionCuentas> reclasificacionCuentasList;
    private List<MasterCatalogo> periodos;
    private List<ReclasificacionCuentas> cuentasEditarList;

    private Boolean mensajeError;
    private Boolean mensajeAviso1;
    private Boolean mensajeAviso2;
    private Boolean mensajeAviso3;
    private Boolean mensajeAviso4;
    private Boolean mensajeCorrecto;
    private Boolean botonCargarDatos;
    private Boolean editarDatos;
    private Boolean tablaEditar;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        if (periodos != null) {
            if (!periodos.isEmpty()) {
                short anioMaximo = (short) (periodos.size() - 1);
                opcionBusqueda.setAnio(periodos.get(anioMaximo).getAnio());
                opcionBusqueda.setAnioSiguiente((short) (opcionBusqueda.getAnio().intValue() + 1));
            }
        }
        confirmarCancelar("INICIO");
    }

    public void actualizarGridConsulta() {
        confirmarCancelar("INICIO");
        if (opcionBusqueda.getAnio() != null) {
            opcionBusqueda.setAnioSiguiente((short) (opcionBusqueda.getAnio().intValue() + 1));
        } else {
            opcionBusqueda.setAnioSiguiente(null);
        }
        PrimeFaces.current().ajax().update("fieldMenu");
        PrimeFaces.current().ajax().update("fieldMensajes");
    }

    public void consultar() {
        if (opcionBusqueda.getAnio() != null && opcionBusqueda.getAnioSiguiente() != null) {
            Boolean condicion1 = reclasificacionCuentasService.getValidacionPeriodoCerrado(opcionBusqueda.getAnio());
            if (condicion1) {
                Boolean condicion2 = reclasificacionCuentasService.getReclasificacionesTranspasadas(opcionBusqueda.getAnio());
                if (condicion2) {
                    mensajeAviso2 = Boolean.TRUE;
                    tablaEditar = Boolean.FALSE;
                    actualizarLazyReclasificacion(true);
                } else {
                    Boolean condicion3 = reclasificacionCuentasService.getValidacionPeridoSiguiente(opcionBusqueda.getAnioSiguiente(), "CC");
                    if (condicion3) {
                        Boolean condicion4 = reclasificacionCuentasService.getCuentaContablePeriodoSiguiente(opcionBusqueda.getAnioSiguiente());
                        if (!condicion4) {
                            mensajeAviso4 = Boolean.TRUE;
                        }
                    } else {
                        mensajeAviso3 = Boolean.TRUE;
                    }
                }
            } else {
                mensajeAviso1 = Boolean.TRUE;
            }
        } else {
            mensajeError = Boolean.TRUE;
        }
        if (!mensajeAviso1 && !mensajeAviso2 && !mensajeAviso3 && !mensajeAviso4 && !mensajeError) {
            mensajeCorrecto = Boolean.TRUE;
            Boolean consulta1 = reclasificacionCuentasService.getReclasificacionesRegistradas(opcionBusqueda.getAnio());
            if (consulta1) {
                actualizarLazyReclasificacion(false);
                editarDatos = Boolean.TRUE;
            } else {
                botonCargarDatos = Boolean.TRUE;
            }
        }
        if (mensajeAviso4 || mensajeAviso3 || mensajeAviso1 || mensajeError) {
            this.reclasificacioCuentasLazy = null;
        }
        actualizarFormulariosTablas();
    }

    public void openDlgCuentas(ReclasificacionCuentas cuentaSeleccionada) {
        this.cuentaReclasificable = new ReclasificacionCuentas();
        this.cuentaContableSeleccionado = new CuentaContable();
        this.cuentaReclasificable = cuentaSeleccionada;
        if (cuentaSeleccionada.getReclasificar()) {
            this.cuentaContableLazyModel = new LazyModel<>(CuentaContable.class);
            this.cuentaContableLazyModel.getSorteds().put("codigo", "ASC");
            this.cuentaContableLazyModel.getFilterss().put("estado", true);
            this.cuentaContableLazyModel.getFilterss().put("movimiento", true);
            this.cuentaContableLazyModel.getFilterss().put("periodo", opcionBusqueda.getAnioSiguiente());
            PrimeFaces.current().executeScript("PF('cuentasContablesAllDLG').show()");
            PrimeFaces.current().ajax().update("cuentasContablesAllTable");
        } else {
            PrimeFaces.current().executeScript("PF('mensajeConfirmacion').show()");
        }
    }

    public void añadirCuentaContable() {
        this.cuentaReclasificable.setCuentaContableNueva(cuentaContableSeleccionado);
        this.cuentaReclasificable = new ReclasificacionCuentas();
        PrimeFaces.current().executeScript("PF('cuentasContablesAllDLG').hide()");
        PrimeFaces.current().ajax().update("reclasificacionTable");
    }

    public void closeDlgCuentaContable(Boolean accion) {
        if (cuentaReclasificable != null) {
            this.cuentaReclasificable.setReclasificar(accion);
            this.cuentaReclasificable = new ReclasificacionCuentas();
        }
        PrimeFaces.current().executeScript("PF('cuentasContablesAllDLG').hide()");
        PrimeFaces.current().ajax().update("reclasificacionTable");
    }

    public void cargarDatos() {
        if (reclasificacionCuentasList != null && !reclasificacionCuentasList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO", "Ya se han cargado los Datos para realizar la reclasificación");
        } else {
            List<CuentaContable> cuentaContablePeriodoActual = reclasificacionCuentasService.getCuentaContablePeriodo(opcionBusqueda.getAnio());
            cuentaContablePeriodoActual.forEach((cuentaContable) -> {
                ReclasificacionCuentas reclasificacionNueva = new ReclasificacionCuentas();
                SaldoDebeHaberDTO saldoDebeHaberDTO = reclasificacionCuentasService.getSaldosDebeHaber(cuentaContable, opcionBusqueda.getAnio());
                if (saldoDebeHaberDTO != null) {
                    if (saldoDebeHaberDTO.getSaldoDebe() != null && saldoDebeHaberDTO.getSaldoHaber() != null) {
                        reclasificacionNueva.setCuentaContableAnterior(cuentaContable);
                        double residuo = saldoDebeHaberDTO.getSaldoDebe().doubleValue() - saldoDebeHaberDTO.getSaldoHaber().doubleValue();
                        if (residuo != 0) {
                            if (residuo > 0) {
                                reclasificacionNueva.setSaldoDebe(new BigDecimal(residuo));
                                reclasificacionNueva.setSaldoHaber(BigDecimal.ZERO);
                            } else if (residuo < 0) {
                                residuo = residuo * (-1);
                                reclasificacionNueva.setSaldoDebe(BigDecimal.ZERO);
                                reclasificacionNueva.setSaldoHaber(new BigDecimal(residuo));
                            }
                            reclasificacionCuentasList.add(reclasificacionNueva);
                        }
                    }
                }
            });
            tablaEditar = Boolean.TRUE;
            actualizarFormulariosTablas();
        }
    }

    public void editarDatos() {
        if (reclasificacionCuentasService.getControl(opcionBusqueda.getAnio(), opcionBusqueda.getAnioSiguiente())) {
            if (reclasificacionCuentasList != null && !reclasificacionCuentasList.isEmpty()) {
                JsfUtil.addWarningMessage("AVISO", "Ya están cargado los datos para editarlos");
            } else {
                List<ReclasificacionCuentas> reclasificacionesGuardadas = reclasificacionCuentasService.getReclasificacionesGuardadas(opcionBusqueda.getAnio());
                for (ReclasificacionCuentas reclasificacion : reclasificacionesGuardadas) {
                    reclasificacionCuentasList.add(reclasificacion);
                }
                tablaEditar = Boolean.TRUE;
                actualizarFormulariosTablas();
            }
        } else {
            JsfUtil.addErrorMessage("ERROR!!", "No se puede editar por que ya se generaron los traspasos");
        }
    }

    public void quitarReclasificacion(Boolean comando) {
        if (comando) {
            this.cuentaReclasificable.setCuentaContableNueva(null);
            this.cuentaReclasificable.setReclasificar(Boolean.FALSE);
        } else {
            this.cuentaReclasificable.setReclasificar(Boolean.TRUE);
        }
        this.cuentaReclasificable =  new ReclasificacionCuentas();
        PrimeFaces.current().executeScript("PF('mensajeConfirmacion').hide()");
        PrimeFaces.current().ajax().update("reclasificacionTable");
    }

    public void guardarReclasificaciones() {
        if (reclasificacionCuentasList != null && !reclasificacionCuentasList.isEmpty()) {
            for (ReclasificacionCuentas reclasificar : reclasificacionCuentasList) {
                if (reclasificar.getId() != null) {
                    reclasificar.setUsuarioModificacion(userSession.getNameUser());
                    reclasificar.setFechaModificacion(new Date());
                    reclasificacionCuentasService.edit(reclasificar);
                } else {
                    reclasificar.setUsuarioCreacion(userSession.getNameUser());
                    reclasificar.setFechaCreacion(new Date());
                    reclasificacionCuentasService.create(reclasificar);
                }
            }
            confirmarCancelar("REINICIAR");
            this.editarDatos = Boolean.TRUE;
            this.mensajeCorrecto = Boolean.TRUE;
            actualizarLazyReclasificacion(false);
            actualizarFormulariosTablas();
            JsfUtil.addInformationMessage("AVISO", "Se ha guardado correctamente la información");
        } else {
            JsfUtil.addWarningMessage("AVISO", "El listado de las cuentas contables a reclasificar estan vacias");
        }
    }

    private void actualizarFormulariosTablas() {
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("formReclasificacion");
        PrimeFaces.current().ajax().update("reclasificacionTableView");
        PrimeFaces.current().ajax().update("reclasificacionTable");
    }

    private void actualizarLazyReclasificacion(Boolean accion) {
        this.reclasificacioCuentasLazy = new LazyModel<>(ReclasificacionCuentas.class);
        this.reclasificacioCuentasLazy.getSorteds().put("cuentaContableAnterior.codigo", "ASC");
        this.reclasificacioCuentasLazy.getFilterss().put("cuentaContableAnterior.periodo", opcionBusqueda.getAnio());
        this.reclasificacioCuentasLazy.setDistinct(false);
        this.reclasificacioCuentasLazy.getFilterss().put("traspaso", accion);
    }

    public void confirmarCancelar(String accion) {
        this.mensajeError = Boolean.FALSE;
        this.mensajeAviso1 = Boolean.FALSE;
        this.mensajeAviso2 = Boolean.FALSE;
        this.mensajeAviso3 = Boolean.FALSE;
        this.mensajeAviso4 = Boolean.FALSE;
        this.mensajeCorrecto = Boolean.FALSE;
        this.botonCargarDatos = Boolean.FALSE;
        this.editarDatos = Boolean.FALSE;
        this.tablaEditar = Boolean.FALSE;
        this.cuentaContableSeleccionado = new CuentaContable();
        this.cuentaReclasificable = new ReclasificacionCuentas();
        this.reclasificacionCuentasList = new ArrayList<>();
        this.cuentasEditarList = new ArrayList<>();
        if (accion.equals("CANCELAR")) {
            actualizarFormulariosTablas();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET -SET">
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<ReclasificacionCuentas> getReclasificacionCuentasList() {
        return reclasificacionCuentasList;
    }

    public void setReclasificacionCuentasList(List<ReclasificacionCuentas> reclasificacionCuentasList) {
        this.reclasificacionCuentasList = reclasificacionCuentasList;
    }

    public Boolean getEditarDatos() {
        return editarDatos;
    }

    public void setEditarDatos(Boolean editarDatos) {
        this.editarDatos = editarDatos;
    }

    public LazyModel<ReclasificacionCuentas> getReclasificacioCuentasLazy() {
        return reclasificacioCuentasLazy;
    }

    public void setReclasificacioCuentasLazy(LazyModel<ReclasificacionCuentas> reclasificacioCuentasLazy) {
        this.reclasificacioCuentasLazy = reclasificacioCuentasLazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public Boolean getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(Boolean mensajeError) {
        this.mensajeError = mensajeError;
    }

    public Boolean getMensajeAviso1() {
        return mensajeAviso1;
    }

    public void setMensajeAviso1(Boolean mensajeAviso1) {
        this.mensajeAviso1 = mensajeAviso1;
    }

    public Boolean getMensajeAviso2() {
        return mensajeAviso2;
    }

    public void setMensajeAviso2(Boolean mensajeAviso2) {
        this.mensajeAviso2 = mensajeAviso2;
    }

    public Boolean getMensajeAviso3() {
        return mensajeAviso3;
    }

    public void setMensajeAviso3(Boolean mensajeAviso3) {
        this.mensajeAviso3 = mensajeAviso3;
    }

    public Boolean getMensajeAviso4() {
        return mensajeAviso4;
    }

    public void setMensajeAviso4(Boolean mensajeAviso4) {
        this.mensajeAviso4 = mensajeAviso4;
    }

    public Boolean getMensajeCorrecto() {
        return mensajeCorrecto;
    }

    public void setMensajeCorrecto(Boolean mensajeCorrecto) {
        this.mensajeCorrecto = mensajeCorrecto;
    }

    public Boolean getBotonCargarDatos() {
        return botonCargarDatos;
    }

    public void setBotonCargarDatos(Boolean botonCargarDatos) {
        this.botonCargarDatos = botonCargarDatos;
    }

    public LazyModel<CuentaContable> getCuentaContableLazyModel() {
        return cuentaContableLazyModel;
    }

    public void setCuentaContableLazyModel(LazyModel<CuentaContable> cuentaContableLazyModel) {
        this.cuentaContableLazyModel = cuentaContableLazyModel;
    }

    public CuentaContable getCuentaContableSeleccionado() {
        return cuentaContableSeleccionado;
    }

    public void setCuentaContableSeleccionado(CuentaContable cuentaContableSeleccionado) {
        this.cuentaContableSeleccionado = cuentaContableSeleccionado;
    }

    public MasterCatalogoService getMasterCatalogoService() {
        return masterCatalogoService;
    }

    public void setMasterCatalogoService(MasterCatalogoService masterCatalogoService) {
        this.masterCatalogoService = masterCatalogoService;
    }

    public Boolean getTablaEditar() {
        return tablaEditar;
    }

    public void setTablaEditar(Boolean tablaEditar) {
        this.tablaEditar = tablaEditar;
    }

    public ReclasificacionCuentas getCuentaReclasificable() {
        return cuentaReclasificable;
    }

    public void setCuentaReclasificable(ReclasificacionCuentas cuentaReclasificable) {
        this.cuentaReclasificable = cuentaReclasificable;
    }
//</editor-fold>

}
